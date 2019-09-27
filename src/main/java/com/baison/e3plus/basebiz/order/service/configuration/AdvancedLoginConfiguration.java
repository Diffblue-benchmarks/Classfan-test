package com.baison.e3plus.basebiz.order.service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedLoginService;
import com.baison.e3plus.biz.support.api.manage.advanced.service.IAdvancedLoginService;
import com.baison.e3plus.biz.support.api.user.model.LoginRequest;
import com.baison.e3plus.biz.support.api.user.model.LoginResult;
import com.baison.e3plus.biz.support.api.user.model.UserInfo;

/**
 * 架构调整适配AbstractBaseService中IAuthService的实例化
 * 
 * @author xinghua.liu
 */
@Configuration
@ConditionalOnClass(IAdvancedLoginService.class)
public class AdvancedLoginConfiguration {

	@Autowired
	ConsumerAdvancedLoginService service;

	@Bean(name = "advancedLoginService")
	@Primary
	public IAdvancedLoginService getIAdvancedLoginService() {
		return new IAdvancedLoginService() {

			@Override
			public LoginResult login(LoginRequest loginRequest) {
				return service.login(loginRequest);
			}

			@Override
			public LoginResult login(String token) {
				return service.login(token);
			}

			@Override
			public void logout(String token) {
				service.logout(token);
			}

			@Override
			public LoginResult setValidPassword(LoginRequest request) {
				return service.setValidPassword(request);
			}

			@Override
			public LoginResult getVerificationCode(LoginRequest request) {
				return service.getVerificationCode(request);
			}

			@Override
			public LoginResult setPasswordByCode(LoginRequest request) {
				return service.setPasswordByCode(request);
			}

			@Override
			public UserInfo queryUserByUserCode(String username) {
				return service.queryUserByUserCode(username);
			}

			@Override
			public LoginResult singlePointLogin(LoginRequest loginRequest) {
				return null;
			}
			
		};

	}
}
