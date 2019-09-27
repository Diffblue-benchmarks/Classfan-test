package com.baison.e3plus.basebiz.order.service.redis.calculate;

import com.baison.e3plus.common.redis.BS2RedisPool;
import com.baison.e3plus.common.redis.util.RedisKeyUtils;
import com.baison.e3plus.common.redis.util.RedisStringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;

public class AdvancedOrderDistributeRedisManage {

	private static final String ORDER_DISTRIBUTE = "ODS_";
	private static final String ORDER_DISTRIBUTE_STATUS = "ODSS_";
	private static final String ORDER_WAREHOUSE_RECEIPTCOUNT="OWRC_";

	private static final int LIVE_SECONDS = 24 * 60 * 60;

	private static BS2RedisPool pool;
	
	static {
		pool = BS2RedisPool.getDeaultPool();
	}

	/**
	 * 设置订单的状态
	 * 
	 * @param orderNo
	 * @param status
	 */
	public static void setOrderStatus(String orderNo, String status) {
		JedisCommands client = null;

		try {
			client = pool.getSource();

			RedisStringUtils.setKeySecondsValue((Jedis) client, ORDER_DISTRIBUTE_STATUS + orderNo, LIVE_SECONDS,
					status);

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
			
			return RedisKeyUtils.deleteKey((Jedis) client, ORDER_DISTRIBUTE_STATUS + orderNo);

		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
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

			boolean success = RedisStringUtils.putKeyValue((Jedis) client, ORDER_DISTRIBUTE_STATUS + orderNo,
					DistributeOrderStatus.EMPTY);

			if (success) {
				RedisKeyUtils.setExpireKey((Jedis) client, ORDER_DISTRIBUTE_STATUS + orderNo, LIVE_SECONDS);
			}

			return success;

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

			return RedisStringUtils.getStringByKey((Jedis) client, ORDER_DISTRIBUTE_STATUS + orderNo);

		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}

	public static Long increOrderDistributeFailedCount(String orderNo) {
		JedisCommands client = null;

		try {
			client = pool.getSource();

			Long count = RedisStringUtils.incrKeyByIncrement((Jedis) client, ORDER_DISTRIBUTE + orderNo, 1L);

			RedisKeyUtils.setExpireKey((Jedis) client, ORDER_DISTRIBUTE + orderNo, LIVE_SECONDS);

			return count;
		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}
	
	/**
	 * 设置仓库日接单量
	 * 
	 * @param wareHouseCode
	 */
	public static void increWareHouseDailyReceiptOrderCount(String wareHouseCode) {
		JedisCommands client = null;
		try {
			client = pool.getSource();

			Long count = RedisStringUtils.incrKeyByIncrement((Jedis) client,
					ORDER_WAREHOUSE_RECEIPTCOUNT + wareHouseCode, 1L);

			if (count == 1) {
				RedisKeyUtils.setExpireKey((Jedis) client, ORDER_WAREHOUSE_RECEIPTCOUNT + wareHouseCode, LIVE_SECONDS);
			}

		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}
	
	/**
	 * 获取仓库日接单量
	 * 
	 * @param wareHouseCode
	 */
	public static Integer getWareHouseDailyReceiptOrderCount(String wareHouseCode) {
		JedisCommands client = null;
		try {
			client = pool.getSource();

			String count = RedisStringUtils.getStringByKey((Jedis) client,
					ORDER_WAREHOUSE_RECEIPTCOUNT + wareHouseCode);

			if (count != null) {
				return Integer.parseInt(count);
			} else {
				return 0;
			}

		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}
	
	public static void initailFailedCount(String orderNo) {
		JedisCommands client = null;

		try {
			client = pool.getSource();
			
			RedisStringUtils.setKeyValue((Jedis) client, ORDER_DISTRIBUTE + orderNo, "0");
			
		} finally {
			if (client != null && client instanceof Jedis) {
				((Jedis) client).close();
			}
		}
	}
}
