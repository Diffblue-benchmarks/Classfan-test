package com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message;

import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeParas;

/**
 * 分单处理消息内容
 * 
 * @author cong
 *
 */
public class OrderDistributeMQMessageContent {

	private String token;

	private OrderDistributeParas orderDistributeParas;

	public OrderDistributeMQMessageContent() {
	}

	public OrderDistributeMQMessageContent(String token, OrderDistributeParas orderDistributeParas) {
		this.token = token;
		this.orderDistributeParas = orderDistributeParas;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public OrderDistributeParas getOrderDistributeParas() {
		return orderDistributeParas;
	}

	public void setOrderDistributeParas(OrderDistributeParas orderDistributeParas) {
		this.orderDistributeParas = orderDistributeParas;
	}

}
