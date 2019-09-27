package com.baison.e3plus.basebiz.order.service.redis.calculate;

/**
 * redis中订单状态
 * 
 * @author cong
 *
 */
public class DistributeOrderStatus {

	/**
	 * 订单还未开始扣减库存
	 */
	public static final String EMPTY = "0";

	/**
	 * 订单已经扣减了redis中的库存，还未锁定数据库库存
	 */
	public static final String REIDS = "1";

	/**
	 * 订单已经锁定数据库库存
	 */
	public static final String DB = "2";
	
	/**
	 * 订单取消
	 */
	public static final String CANCEL = "3";
}
