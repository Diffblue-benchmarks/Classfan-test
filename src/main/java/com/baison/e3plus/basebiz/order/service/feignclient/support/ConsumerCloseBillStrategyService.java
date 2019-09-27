
package com.baison.e3plus.basebiz.order.service.feignclient.support;


import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.business.advanced.model.closebill.CloseBillStrategy;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;


/**
 * 消费者模块-ICloseBillStrategyService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerCloseBillStrategyService")
public interface ConsumerCloseBillStrategyService {

	@PostMapping("/providerCloseBillStrategyService/queryObject")
	public CloseBillStrategy[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
	@PostMapping("/providerCloseBillStrategyService/queryPageObjectByADS")
	public CloseBillStrategy[] queryPageObjectByADS(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = false) int pageSize,
			@RequestParam(name = "pageIndex", required = false) int pageIndex);
	
	@PostMapping("/providerCloseBillStrategyService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody CloseBillStrategy[] closeBillStrategys);
	
	@PostMapping("/providerCloseBillStrategyService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody CloseBillStrategy[] closeBillStrategys);
	
	@PostMapping("/providerCloseBillStrategyService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "ids", required = false) Object[] ids);
	
	@PostMapping("/providerCloseBillStrategyService/getListCountByADS")
	public long getListCountByADS(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
	@PostMapping(value = "findObjectById")
	public CloseBillStrategy[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "ids", required = false) Object[] ids);
	
	@PostMapping(value = "modifyObject2")
	public ServiceResult modifyObject2(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "ids", required = false) Object[] ids, @RequestParam(name = "dayOfMonth", required = false) Integer dayOfMonth);
	
	@PostMapping(value = "enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "ids", required = false) Object[] ids);
	
	@PostMapping(value = "disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "ids", required = false) Object[] ids);
}