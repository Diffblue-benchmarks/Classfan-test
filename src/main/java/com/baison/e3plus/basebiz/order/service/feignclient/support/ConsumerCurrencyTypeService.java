
package com.baison.e3plus.basebiz.order.service.feignclient.support;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.currencytype.CurrencyType;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IClerkLevelService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerCurrencyTypeService")
public interface ConsumerCurrencyTypeService {
	
	@PostMapping("/providerCurrencyTypeService/createCurrencyType")
	public ServiceResult createCurrencyType(@RequestParam(name = "token", required = true) String token, @RequestBody List<CurrencyType> CurrencyTypes);
	
	@PostMapping("/providerCurrencyTypeService/modifyCurrencyType")
	public ServiceResult modifyCurrencyType(@RequestParam(name = "token", required = true) String token, @RequestBody List<CurrencyType> CurrencyTypes);
	
	@PostMapping("/providerCurrencyTypeService/removeCurrencyType")
	public ServiceResult removeCurrencyType(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerCurrencyTypeService/findCurrencyTypeById")
	public CurrencyType[] findCurrencyTypeById(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerCurrencyTypeService/findCurrencyTypeList")
	public CurrencyType[] findCurrencyTypeList(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerCurrencyTypeService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerCurrencyTypeService/queryPageCurrencyType")
	public CurrencyType[] queryPageCurrencyType(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	
	@PostMapping("/providerCurrencyTypeService/enableCurrencyType")
	public ServiceResult enableCurrencyType(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerCurrencyTypeService/disableCurrencyType")
	public ServiceResult disableCurrencyType(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
}