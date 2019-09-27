
package com.baison.e3plus.basebiz.order.service.feignclient.stock;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.stock.api.config.BizStockApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.basebiz.goods.api.model.setmealgoods.SetMealGoodsSpec;
import com.baison.e3plus.biz.stock.api.model.stock.StockAvailableQty;
import com.baison.e3plus.biz.stock.api.model.stock.StockInfo;
import com.baison.e3plus.biz.stock.api.model.stock.StockLockParas;
import com.baison.e3plus.biz.stock.api.model.stock.StockOperateParas;
import com.baison.e3plus.biz.stock.api.model.stock.StockOperateResult;
import com.baison.e3plus.biz.stock.api.model.synchrostrategy.StockSynchroResult;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedWareHouse;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IStockOperateService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizStockApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerStockOperateService")
public interface ConsumerStockOperateService {

	@PostMapping("/providerStockOperateService/pushStockSynchroInfo")
	public void pushStockSynchroInfo(@RequestParam(name = "token", required = true) String token,
			@RequestBody List<StockSynchroResult> successObject);

	@PostMapping("/providerStockOperateService/calSynPlatformGoods")
	public void calSynPlatformGoods(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "shopId", required = true) Long shopId, @RequestBody List<Long> skuIds);

	@PostMapping("/providerStockOperateService/calcShopSetMealCount")
	public ServiceResult calcShopSetMealCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody List<SetMealGoodsSpec> specList, @RequestParam(name = "shopId", required = true) Long shopId);
	
	@PostMapping("/providerStockOperateService/incDbLockStock")
	public StockOperateResult incDbLockStock(@RequestParam(name = "token", required = true) String token,
			@RequestBody List<StockOperateParas> stockParas);
	
	@PostMapping("/providerStockOperateService/querySkuAvailableAty")
	public List<StockAvailableQty> querySkuAvailableAty(@RequestParam(name = "token", required = true) String token,
			@RequestBody List<AdvancedWareHouse> wareHouses, @RequestParam(name = "skuIds", required = false) List<Long> skuIds);
	
	@PostMapping("/providerStockOperateService/decreRedisStock")
	public StockOperateResult decreRedisStock(@RequestParam(name = "token", required = true) String token,
			@RequestBody List<StockOperateParas> stockParas);
	
	@PostMapping("/providerStockOperateService/backRedisSkuQty")
	public void backRedisSkuQty(@RequestBody List<StockOperateParas> successDatas);
	

	@PostMapping("/providerStockOperateService/backDbLockStock")
	public void backDbLockStock(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<StockLockParas> stockParas, 
			@RequestParam(name = "virtualStockLockIds", required = false) List<String> virtualStockLockIds);
	
	@PostMapping("/providerStockOperateService/decreStockAndLockStock")
	public ServiceResult decreStockAndLockStock(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<StockInfo> stockInfos);
	
	@PostMapping("/providerStockOperateService/releaseLockStock")
	public ServiceResult releaseLockStock(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<StockInfo> stockInfos);
			


}