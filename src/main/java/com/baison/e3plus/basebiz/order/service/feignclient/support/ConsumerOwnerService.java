
package com.baison.e3plus.basebiz.order.service.feignclient.support;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.owner.Owner;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IOwnerService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerOwnerService")
public interface ConsumerOwnerService {
	
	@PostMapping("/providerOwnerService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, @RequestBody Owner[] owner);
	
	@PostMapping("/providerOwnerService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, @RequestBody Owner[] owner);
	
	@PostMapping("/providerOwnerService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerOwnerService/findObjectById")
	public Owner[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerOwnerService/queryObject")
	public Owner[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerOwnerService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerOwnerService/queryPageObject")
	public Owner[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	
	@PostMapping("/providerOwnerService/enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody List<Object> ids);
	
	@PostMapping("/providerOwnerService/disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody List<Object> ids);
	
}