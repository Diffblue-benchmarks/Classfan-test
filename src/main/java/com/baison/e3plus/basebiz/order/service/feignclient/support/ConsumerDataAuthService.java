package com.baison.e3plus.basebiz.order.service.feignclient.support;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.common.cncore.session.Session;
import com.baison.e3plus.common.orm.metadata.Condition;

/**
 * 消费者模块-IDataAuthService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerDataAuthService")
public interface ConsumerDataAuthService {
	
	@PostMapping("/providerDataAuthService/getFilterCondition")
	List<Condition> getFilterCondition(@RequestBody Session session,
			@RequestParam(name = "pk", required = false) String pk,
			@RequestParam(name = "authObjectId", required = false) String authObjectId,
			@RequestParam(name = "operatorName", required = false) String operatorName);
}
