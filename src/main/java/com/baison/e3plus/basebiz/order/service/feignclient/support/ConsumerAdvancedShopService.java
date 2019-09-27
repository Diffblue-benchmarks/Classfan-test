package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.common.cncore.query.E3Selector;


/**
 * 消费者模块-IAdvancedShopService
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdvancedShopService")
public interface ConsumerAdvancedShopService {

	@PostMapping("/providerAdvancedShopService/queryPageShop")
	public Shop[] queryPageShop(@RequestParam(name="token",required=true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name="pageSize",required=true) int pageSize,
			@RequestParam(name="pageIndex",required=true) int pageIndex);
	
	@PostMapping("/providerAdvancedShopService/getListCount")
	public long getListCount(@RequestParam(name="token",required=true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerAdvancedShopService/findShop")
	public Shop[] findShop(@RequestParam(name="token",required=true)String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerAdvancedShopService/findShopById")
	public Shop[] findShopById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "id", required = false) Object[] id);

}
