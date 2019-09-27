
package com.baison.e3plus.basebiz.order.service.feignclient.support;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.coderule.CodeRule;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IClerkLevelService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerCodeRuleService")
public interface ConsumerCodeRuleService {
	
	@PostMapping("/providerCodeRuleService/createCodeRules")
	public ServiceResult createCodeRules(@RequestParam(name = "token", required = true) String token, @RequestBody List<CodeRule> CodeRules);
	
	@PostMapping("/providerCodeRuleService/modifyCodeRules")
	public ServiceResult modifyCodeRules(@RequestParam(name = "token", required = true) String token, @RequestBody List<CodeRule> CodeRules);
	
	@PostMapping("/providerCodeRuleService/removeCodeRules")
	public ServiceResult removeCodeRules(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerCodeRuleService/findById")
	public CodeRule[] findById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "id", required = false) Object id);
	
	@PostMapping("/providerCodeRuleService/findCodeRules")
	public CodeRule[] findCodeRules(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerCodeRuleService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerCodeRuleService/queryPageCodeRules")
	public CodeRule[] queryPageCodeRules(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	
	@PostMapping("/providerCodeRuleService/enableCodeRules")
	public ServiceResult enableCodeRules(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerCodeRuleService/disableCodeRules")
	public ServiceResult disableCodeRules(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerCodeRuleService/generateCode")
	public String generateCode(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "javaBeanName", required = true) String javaBeanName, @RequestBody Object object);
	
}