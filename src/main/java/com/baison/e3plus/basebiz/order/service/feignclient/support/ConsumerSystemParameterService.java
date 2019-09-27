package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.parameter.SystemParameter;

/**
 * 消费者模块-ISystemParameterService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerSystemParameterService")
public interface ConsumerSystemParameterService{
	
	@PostMapping("/providerSystemParameterService/findByIkey")
	SystemParameter findByIkey(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "key", required = true) String key);

}
