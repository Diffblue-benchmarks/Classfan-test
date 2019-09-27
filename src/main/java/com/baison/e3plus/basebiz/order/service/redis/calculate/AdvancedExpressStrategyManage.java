package com.baison.e3plus.basebiz.order.service.redis.calculate;

import com.baison.e3plus.common.redis.BS2RedisPool;
import com.baison.e3plus.common.redis.util.RedisKeyUtils;
import com.baison.e3plus.common.redis.util.RedisStringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;

public class AdvancedExpressStrategyManage {
	
	private static final String ORDER_EXPRESS_STRATEGY="OES_";

	private static final int LIVE_SECONDS = 24 * 60 * 60;

	private static BS2RedisPool pool;
	
	static {
		pool = BS2RedisPool.getDeaultPool();
	}
	
	/**
	 * 标记订单状态为初始
	 * 
	 * @param orderNo
	 * @return
	 */
	public static boolean markOrderStatusInitail(String orderNo) {
		JedisCommands client = null;

		try {
			client = pool.getSource();

			boolean success = RedisStringUtils.putKeyValue((Jedis) client, ORDER_EXPRESS_STRATEGY + orderNo,
					DistributeOrderStatus.EMPTY);

			if (success) {
				RedisKeyUtils.setExpireKey((Jedis) client, ORDER_EXPRESS_STRATEGY + orderNo, LIVE_SECONDS);
			}

			return success;

		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}
	
	/**
	 * 删除订单的状态标记
	 * 
	 * @param orderNo
	 * @param status
	 */
	public static boolean deleteOrderStatus(String orderNo) {
		JedisCommands client = null;

		try {
			client = pool.getSource();
			
			return RedisKeyUtils.deleteKey((Jedis) client, ORDER_EXPRESS_STRATEGY + orderNo);

		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}
	
	/**
	 * 获取订单状态
	 * 
	 * @param orderNo
	 * @return
	 */
	public static String getOrderStatus(String orderNo) {
		JedisCommands client = null;

		try {
			client = pool.getSource();

			return RedisStringUtils.getStringByKey((Jedis) client, ORDER_EXPRESS_STRATEGY + orderNo);

		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}
	
}
