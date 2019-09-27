package com.baison.e3plus.basebiz.order.service.feignclient.stock;



import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.stock.api.config.BizStockApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.sales.api.model.thirdParty.RetailTeturnBillEntity;
import com.baison.e3plus.biz.sales.api.model.thirdParty.RetailTeturnBillsEntity;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IAdvancedOrderOperateStockService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizStockApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdvancedOrderOperateStockService")
public interface ConsumerAdvancedOrderOperateStockService {

	@PostMapping("/providerAdvancedOrderOperateStockService/submitLockStock")
	public ServiceResult submitLockStock(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailTeturnBillEntity retailTeturnBillEntity);
	
	@PostMapping("/providerAdvancedOrderOperateStockService/retailOrderSubmitLockStock")
	public ServiceResult retailOrderSubmitLockStock(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailTeturnBillEntity retailTeturnBillEntity);
	
	@PostMapping("/providerAdvancedOrderOperateStockService/retailOrderReleaseLockStock")
	public ServiceResult retailOrderReleaseLockStock(@RequestParam(name = "token", required = true) String token, @RequestBody RetailTeturnBillEntity retailTeturnBillEntity);
	
	@PostMapping("/providerAdvancedOrderOperateStockService/retailReturnOrderCheck")
	public ServiceResult retailReturnOrderCheck(@RequestParam(name = "token", required = true) String token, @RequestBody RetailTeturnBillsEntity retailTeturnBillsEntity);
	
}
