
package com.baison.e3plus.basebiz.order.service.feignclient.support;


import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.platform.Platform;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IPlatformService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerPlatformService")
public interface ConsumerPlatformService {
	
	@PostMapping("/providerPlatformService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, @RequestBody Platform[] platform);
	
	@PostMapping("/providerPlatformService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, @RequestBody Platform[] platform);
	
	@PostMapping("/providerPlatformService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerPlatformService/findObjectById")
	public Platform[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerPlatformService/queryObject")
	public Platform[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerPlatformService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerPlatformService/queryPageObject")
	public Platform[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	
	@PostMapping("/providerPlatformService/enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerPlatformService/disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
}