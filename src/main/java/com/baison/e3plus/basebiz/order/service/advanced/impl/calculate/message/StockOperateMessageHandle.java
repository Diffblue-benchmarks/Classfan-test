package com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message;

import java.util.List;

import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerStockOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baison.e3plus.common.mq.BapONSMQProducer;
import com.baison.e3plus.common.bscore.mq.BapMQMessage;
import com.baison.e3plus.common.bscore.mq.IBapMQMessageListener;
import com.baison.e3plus.common.bscore.mq.IBapMQProducer;
import com.baison.e3plus.common.bscore.utils.ObjectUtil;
import com.baison.e3plus.common.core.util.ServiceUtils;
import com.baison.e3plus.basebiz.order.service.redis.calculate.AdvancedOrderDistributeRedisManage;
import com.baison.e3plus.basebiz.order.service.redis.calculate.DistributeOrderStatus;
import com.baison.e3plus.biz.stock.api.model.stock.StockOperateParas;
import com.baison.e3plus.biz.stock.api.redis.AdvancedStockRedisManage;

/**
 * 异步锁定库存消息处理器，需要保证一定锁定成功
 * <p>
 * 如果出现多次锁定失败，则需要人工处理
 * 
 * @author cong
 *
 */
public class StockOperateMessageHandle implements IBapMQMessageListener {

	@Autowired
	private ConsumerStockOperateService stockOperateService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int FAILED_COUNT = 3;

	private String topic;

	public StockOperateMessageHandle(String topic) {
		this.topic = topic;
	}

	@Override
	public void onMessage(String key, BapMQMessage message) {

		IncreLockStockMQMessageContent msgContent = JSON.parseObject(JSON.toJSONString(message.getMessage()),
				IncreLockStockMQMessageContent.class);

		String token = msgContent.getToken();
		List<StockOperateParas> stockParas = msgContent.getStockParas();

		try {
			// 再添加数据库锁定数
			stockOperateService.incDbLockStock(token, stockParas);

			// 标记redis中订单的状态为 2-db
			AdvancedOrderDistributeRedisManage.setOrderStatus(stockParas.get(0).getBillCode(), DistributeOrderStatus.DB);
		} catch (Exception e) {
			// 如果错误，记录错误原因，然后异步重新扣减
			log.error(e.getMessage(), e);

			Long failedCount = AdvancedStockRedisManage.increStockOperateFailedCount(msgContent.getUuid());
			if (failedCount > FAILED_COUNT) {
				log.info("订单[" + stockParas.get(0).getBillCode() + "]第[" + failedCount + "]次扣减库存失败，异步重新扣减");
				reIncreDbLockStock(msgContent);
			} else {
				log.info("订单[" + stockParas.get(0).getBillCode() + "]第[" + FAILED_COUNT + "]次扣减库存失败，人工处理！");
				log.info("manual_handle_bill | paras:" + ObjectUtil.serializeByObjectMapper(stockParas));
			}
		}
	}

	private void reIncreDbLockStock(IncreLockStockMQMessageContent msgContent) {
		// 发送消息让MQ消费者异步锁定数据库库存
		IBapMQProducer stockOperateProducer = ServiceUtils
				.getService(OrderDistributeMessageConstance.LOCK_DB_STOCK_PRODUCER, BapONSMQProducer.class);

		BapMQMessage massage = new BapMQMessage(OrderDistributeMessageConstance.LOCK_DB_STOCK, msgContent);
		stockOperateProducer.send(massage);
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public String getTag() {
		return OrderDistributeMessageConstance.LOCK_DB_STOCK;
	}

}
