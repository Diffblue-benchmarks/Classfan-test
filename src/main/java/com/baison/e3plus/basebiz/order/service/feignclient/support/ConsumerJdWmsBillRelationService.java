
package com.baison.e3plus.basebiz.order.service.feignclient.support;



import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.business.advanced.model.JdWmsBillRelation;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;


/**
 * 消费者模块-IJdWmsBillRelationService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerJdWmsBillRelationService")
public interface ConsumerJdWmsBillRelationService {

	@PostMapping("/providerJdWmsBillRelationService/createObject")
	ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody JdWmsBillRelation[] beans);
	
	@PostMapping("/providerJdWmsBillRelationService/modifyObject")
	ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody JdWmsBillRelation[] beans);
	
	@PostMapping("/providerJdWmsBillRelationService/queryObject")
	JdWmsBillRelation[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
}