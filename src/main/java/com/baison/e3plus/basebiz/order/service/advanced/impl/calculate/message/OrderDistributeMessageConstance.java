package com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message;

/**
 * 分单处理常量
 * 
 * @author cong
 *
 */
public class OrderDistributeMessageConstance {

	/**
	 * 分单处理 MQ对应的tag
	 */
	public static final String ORDER_DISTRIBUTE = "ORDER_DISTRIBUTE";

	/**
	 * 分单扣减库存成功后回调外部系统消息tag
	 */
	public static final String ORDER_DISTRIBUTE_SUCCESS = "ORDER_DISTRIBUTE_SUCCESS";

	/**
	 * 增加数据库库存消息tag
	 */
	public static final String LOCK_DB_STOCK = "LOCK_DB_STOCK";
	
	/**
	 * 增加数据库库存消息发送者
	 */
	public static final String LOCK_DB_STOCK_PRODUCER = "lockDbStockProducer";
	
	/**
	 * 分单处理消息发送者
	 */
	public static final String ORDER_DISTRIBUTE_PRODUCER = "orderDistributeProducer";
	
	/**
	 * 分单成功异步通知其他系统消息发送者
	 */
	public static final String DISTRIBUTE_SUCCESS_PRODUCER = "distributeSuccessProducer";

	
}
