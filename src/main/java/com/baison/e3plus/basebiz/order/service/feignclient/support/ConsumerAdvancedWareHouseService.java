
package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedWareHouse;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IAdvancedWareHouseService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdvancedWareHouseService")
public interface ConsumerAdvancedWareHouseService {
	@PostMapping("/providerAdvancedWareHouseService/findObjectByCodeArray")
	public WareHouse[] findObjectByCodeArray(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "code", required = true) String code);

	@PostMapping("/providerAdvancedWareHouseService/generateAutoIncrease")
	Long generateAutoIncrease();

	@PostMapping("/providerAdvancedWareHouseService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody WareHouse[] object);

	@PostMapping("/providerAdvancedWareHouseService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody WareHouse[] object);

	@PostMapping("/providerAdvancedWareHouseService/queryPageGroupWareHouse")
	public WareHouse[] queryPageGroupWareHouse(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerAdvancedWareHouseService/getListGroupCount")
	public long getListGroupCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
	@PostMapping("/providerAdvancedWareHouseService/findObjectById")
	public WareHouse[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);
	
	@PostMapping("/providerAdvancedWareHouseService/queryObject")
	public AdvancedWareHouse[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
	@PostMapping("/providerAdvancedWareHouseService/checkVersion")
	public boolean checkVersion();
	
	@PostMapping("/providerAdvancedWareHouseService/queryWareHouse")
	public WareHouse[] queryWareHouse(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
}