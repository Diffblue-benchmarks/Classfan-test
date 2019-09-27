package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.virtualwarehouse.VirtualWareHouse;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IVirtualWareHouseService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerVirtualWareHouseService")

public interface ConsumerVirtualWareHouseService {


	@PostMapping("/providerVirtualWareHouseService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody VirtualWareHouse[] beans);

	@PostMapping("/providerVirtualWareHouseService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody VirtualWareHouse[] beans);

	@PostMapping("/providerVirtualWareHouseService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerVirtualWareHouseService/findObjectById")
	public VirtualWareHouse[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerVirtualWareHouseService/queryObject")
	public VirtualWareHouse[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerVirtualWareHouseService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerVirtualWareHouseService/queryPageObject")
	public VirtualWareHouse[] queryPageObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerVirtualWareHouseService/enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] Ids);

	@PostMapping("/providerVirtualWareHouseService/disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] Ids);

}
