package com.baison.e3plus.basebiz.order.service.feignclient.support;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.channel.Channel;
import com.baison.e3plus.common.cncore.query.E3Selector;

/**
 * 消费者模块-IAdvancedChannelService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdvancedChannelService")
public interface ConsumerAdvancedChannelService {

	@PostMapping("/providerAdvancedChannelService/findChannelById")
	Channel[] findChannelById(@RequestParam(name = "token", required = true) String token,
			@RequestBody List<Object> ids);

	@PostMapping("/providerAdvancedChannelService/queryPageChannel")
	Channel[] queryPageChannel(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = false) int pageSize,
			@RequestParam(name = "pageIndex", required = false) int pageIndex);
	
	@PostMapping("/providerAdvancedChannelService/findObjectByCodeArray")
	Channel[] findObjectByCodeArray(@RequestParam(name = "token", required = true)  String token, @RequestParam(name = "code", required = true)String code);

}
