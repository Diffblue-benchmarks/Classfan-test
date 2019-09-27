package com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message;

import java.util.List;

import com.baison.e3plus.biz.stock.api.model.stock.StockOperateParas;
import com.baison.e3plus.common.bscore.utils.UUIDUtil;

/**
 * 添加数据库锁定数MQ消息
 * 
 * @author cong
 *
 */
public class IncreLockStockMQMessageContent {

	public IncreLockStockMQMessageContent() {
	}

	public IncreLockStockMQMessageContent(String token, List<StockOperateParas> stockParas) {
		this.token = token;
		this.stockParas = stockParas;
		this.uuid = UUIDUtil.generate();
	}

	private List<StockOperateParas> stockParas;

	private String token;

	private String uuid;

	public List<StockOperateParas> getStockParas() {
		return stockParas;
	}

	public void setStockParas(List<StockOperateParas> stockParas) {
		this.stockParas = stockParas;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
