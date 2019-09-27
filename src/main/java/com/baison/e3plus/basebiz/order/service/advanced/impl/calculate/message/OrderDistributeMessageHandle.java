package com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message;

import com.baison.e3plus.common.bscore.mq.BapMQMessage;
import com.baison.e3plus.common.bscore.mq.IBapMQMessageListener;
import com.baison.e3plus.common.bscore.mq.IBapMQProducer;
import com.baison.e3plus.common.bscore.utils.ObjectUtil;
import com.baison.e3plus.common.core.util.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baison.e3plus.common.mq.BapONSMQProducer;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeParas;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeResponse;
import com.baison.e3plus.basebiz.order.api.service.calculate.IOrderDistributeCalculateService;
import com.baison.e3plus.basebiz.order.service.redis.calculate.AdvancedOrderDistributeRedisManage;
import com.baison.e3plus.basebiz.order.service.redis.calculate.DistributeOrderStatus;

/**
 * 分单消息处理器
 * 
 * @author cong
 *
 */
public class OrderDistributeMessageHandle implements IBapMQMessageListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private String topic;

	private static final int FAILED_COUNT = 2;

	public OrderDistributeMessageHandle(String topic) {
		this.topic = topic;
	}

	@Override
	public void onMessage(String key, BapMQMessage message) {

		String msgStr = ObjectUtil.serializeByObjectMapper(message.getMessage());
		OrderDistributeMQMessageContent content = ObjectUtil.deSrializeByObjectMapper(msgStr,
				OrderDistributeMQMessageContent.class);

		String token = content.getToken();
		OrderDistributeParas paras = content.getOrderDistributeParas();

		String orderBillNo = paras.getOrderBillNo();
		
		AdvancedOrderDistributeRedisManage odsRedisManage = new AdvancedOrderDistributeRedisManage();
		
		long count = odsRedisManage.increOrderDistributeFailedCount(paras.getOrderBillNo());
		if (count > FAILED_COUNT) {
			log.info("-----订单["+paras.getOrderBillNo()+"]第["+count+"]次分单失败，写日志");
			// 记录日志
			doLog((OrderDistributeMQMessageContent) message.getMessage(), "");
			odsRedisManage.initailFailedCount(paras.getOrderBillNo());
			
			return;
		}

		log.info("-----MQ开始处理分单，单号：" + orderBillNo);
		long start_date = System.currentTimeMillis();

		// redis setnx 方法设置订单的状态为 0-empty, 如果设置失败，说明订单已经被分单处理，不需要再进行分单，防止重复分单
		if (!odsRedisManage.markOrderStatusInitail(orderBillNo)) {
			log.info("-----订单【" + orderBillNo + "】正在分单中...");
			return;
		}

		IOrderDistributeCalculateService service = ServiceUtils.getService(IOrderDistributeCalculateService.class);

		// 调用分单
		OrderDistributeResponse response = service.distributeOrder(token, paras);

		try {
			if (response.getIsSuccess()) {
				long end_date = System.currentTimeMillis();
				log.info("-----单号：" + orderBillNo + ", 分单成功, 耗时：" + (end_date - start_date));

				// 如果成功，异步返回分单信息
				callDistributeSuccess(response);

			} else {
				log.info("-----单号：" + orderBillNo + "，分单失败，具体原因查看数据库日志");

				// 如果订单状态处于0-empty， 才需要异步重新分单
				String orderStatus = odsRedisManage.getOrderStatus(orderBillNo);
				
				if (!DistributeOrderStatus.REIDS.equals(orderStatus)) {
					// 如果已经扣减了redis中的库存，然后异步在扣减数据库库存，那么订单分单标记是不用清除掉的
					odsRedisManage.deleteOrderStatus(orderBillNo);
				}
				
				if (DistributeOrderStatus.EMPTY.equals(orderStatus)) {
					reDistributeOrder(message, count);
				}
				
			}
		} catch (Exception e) {
			odsRedisManage.deleteOrderStatus(orderBillNo);
			throw new RuntimeException(e);
		}

	}

	private void callDistributeSuccess(OrderDistributeResponse response) {
		IBapMQProducer distributeSuccessProducer = ServiceUtils
				.getService(OrderDistributeMessageConstance.DISTRIBUTE_SUCCESS_PRODUCER, BapONSMQProducer.class);
		BapMQMessage successMsg = new BapMQMessage(OrderDistributeMessageConstance.ORDER_DISTRIBUTE_SUCCESS, response);
		distributeSuccessProducer.send(successMsg);
	}

	private void reDistributeOrder(BapMQMessage message, long count) {

		// 塞回分单列表，进行重新分单
		IBapMQProducer distributeProducer = ServiceUtils
				.getService(OrderDistributeMessageConstance.ORDER_DISTRIBUTE_PRODUCER, BapONSMQProducer.class);
		distributeProducer.send(message, count * 10 * 1000);

	}

	private void doLog(OrderDistributeMQMessageContent content, String errorMsg) {
		Logger log = LoggerFactory.getLogger("e3.orderDistributeLog");
		log.info(content.getToken() + "|" + ObjectUtil.serializeByObjectMapper(content.getOrderDistributeParas()));
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public String getTag() {
		return OrderDistributeMessageConstance.ORDER_DISTRIBUTE;
	}

}
