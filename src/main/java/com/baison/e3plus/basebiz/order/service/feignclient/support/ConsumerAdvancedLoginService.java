package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.user.model.LoginRequest;
import com.baison.e3plus.biz.support.api.user.model.LoginResult;
import com.baison.e3plus.biz.support.api.user.model.UserInfo;



/**
 * 消费者模块-IAdvancedLoginService
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdvancedLoginService")
public interface ConsumerAdvancedLoginService {
	@PostMapping("/providerAdvancedLoginService/loginForloginReq")
	LoginResult login(@RequestBody LoginRequest loginRequest);
	@PostMapping("/providerAdvancedLoginService/loginForToken")
	LoginResult login(@RequestParam(name = "token", required = true)String token);

	@PostMapping("/providerAdvancedLoginService/logout")
	void logout(@RequestParam(name = "token", required = true)String token);

	@PostMapping("/providerAdvancedLoginService/setValidPassword")
	LoginResult setValidPassword(@RequestBody LoginRequest request);

	@PostMapping("/providerAdvancedLoginService/getVerificationCode")
	LoginResult getVerificationCode( @RequestBody LoginRequest request);

	@PostMapping("/providerAdvancedLoginService/setPasswordByCode")
	LoginResult setPasswordByCode(  @RequestBody LoginRequest request);
	@PostMapping("/providerAdvancedLoginService/queryUserByUserCode")
	UserInfo queryUserByUserCode(@RequestParam(name = "token", required = true) String username);

}
