package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.administrationarea.AdministrationArea;
import com.baison.e3plus.common.cncore.query.E3Selector;
/**
 * 消费者模块-IAdministrationAreaWithFashionService
 * 
 * @author xinghua.liu
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdministrationAreaWithFashionService")
public interface ConsumerAdministrationAreaWithFashionService {


	@PostMapping("/providerAdministrationAreaWithFashionService/findAdministrationAreaList")
	AdministrationArea [] findAdministrationAreaList(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
	@PostMapping("/providerAdministrationAreaWithFashionService/queryPageAdministrationArea")
	AdministrationArea[] queryPageAdministrationArea(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = false) int pageSize, 
			@RequestParam(name = "pageIndex", required = false) int pageIndex);


}
