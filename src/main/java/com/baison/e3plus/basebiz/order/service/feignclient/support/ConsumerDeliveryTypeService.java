
package com.baison.e3plus.basebiz.order.service.feignclient.support;


import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.delivery.DeliveryType;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IDeliveryTypeService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerDeliveryTypeService")
public interface ConsumerDeliveryTypeService {
	
	@PostMapping("/providerDeliveryTypeService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, @RequestBody DeliveryType[] deliveryType);
	
	@PostMapping("/providerDeliveryTypeService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, @RequestBody DeliveryType[] deliveryType);
	
	@PostMapping("/providerDeliveryTypeService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerDeliveryTypeService/findObjectById")
	public DeliveryType[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerDeliveryTypeService/queryObject")
	public DeliveryType[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerDeliveryTypeService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerDeliveryTypeService/queryPageObject")
	public DeliveryType[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	
	@PostMapping("/providerDeliveryTypeService/enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerDeliveryTypeService/disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
}