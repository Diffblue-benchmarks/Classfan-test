package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.business.advanced.api.model.shop.ShopVirtualHouse;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IShopVirtualHouseService
 * 
 * @author xinghua.liu
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerShopVirtualHouseService")

public interface ConsumerShopVirtualHouseService {

	@PostMapping("/providerShopVirtualHouseService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody ShopVirtualHouse[] beans);

	@PostMapping("/providerShopVirtualHouseService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody ShopVirtualHouse[] beans);

	@PostMapping("/providerShopVirtualHouseService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerShopVirtualHouseService/findObjectById")
	public ShopVirtualHouse[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerShopVirtualHouseService/queryObject")
	public ShopVirtualHouse[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerShopVirtualHouseService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerShopVirtualHouseService/queryPageObject")
	public ShopVirtualHouse[] queryPageObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerShopVirtualHouseService/enableObjects")
	public ServiceResult enableObjects(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] Ids);

	@PostMapping("/providerShopVirtualHouseService/disableObjects")
	public ServiceResult disableObjects(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] Ids);

	@PostMapping("/providerShopVirtualHouseService/enable")
	ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

	@PostMapping("/providerShopVirtualHouseService/disable")
	ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

}
