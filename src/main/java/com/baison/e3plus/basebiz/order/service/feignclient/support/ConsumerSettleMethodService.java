
package com.baison.e3plus.basebiz.order.service.feignclient.support;


import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.settlemethod.SettleMethod;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-ISettleMethodService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerSettleMethodService")
public interface ConsumerSettleMethodService {
	
	@PostMapping("/providerSettleMethodService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, @RequestBody SettleMethod[] settleMethod);
	
	@PostMapping("/providerSettleMethodService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, @RequestBody SettleMethod[] settleMethod);
	
	@PostMapping("/providerSettleMethodService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerSettleMethodService/remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerSettleMethodService/findObjectById")
	public SettleMethod[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerSettleMethodService/queryObject")
	public SettleMethod[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerSettleMethodService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerSettleMethodService/queryPageObject")
	public SettleMethod[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	
	@PostMapping("/providerSettleMethodService/enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerSettleMethodService/disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
}