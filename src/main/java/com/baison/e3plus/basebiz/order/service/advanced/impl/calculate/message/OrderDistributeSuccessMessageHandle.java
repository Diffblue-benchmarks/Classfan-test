package com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baison.e3plus.common.bscore.mq.BapMQMessage;
import com.baison.e3plus.common.bscore.mq.IBapMQMessageListener;
import com.baison.e3plus.common.bscore.utils.ObjectUtil;

/**
 * 分单成功消息处理
 * 
 * @author cong
 *
 */
public class OrderDistributeSuccessMessageHandle implements IBapMQMessageListener {

	private String topic;
	
	private Logger log = LoggerFactory.getLogger(OrderDistributeSuccessMessageHandle.class);

	public OrderDistributeSuccessMessageHandle(String topic) {
		this.topic = topic;
	}

	@Override
	public void onMessage(String key, BapMQMessage message) {
		//FileUtil.write(ObjectUtil.serializeByObjectMapper(message), new File("/home/admin/log/log.log"), true);
		log.info(ObjectUtil.serializeByObjectMapper(message));
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public String getTag() {
		return OrderDistributeMessageConstance.ORDER_DISTRIBUTE_SUCCESS;
	}

}
