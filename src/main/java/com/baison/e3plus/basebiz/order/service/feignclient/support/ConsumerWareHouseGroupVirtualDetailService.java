package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehousegroup.WareHouseGroupVirtualDetail;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IWareHouseGroupVirtualDetailService
 * 
 * @author xinghua.liu
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerWareHouseGroupVirtualDetailService")

public interface ConsumerWareHouseGroupVirtualDetailService {


	@PostMapping("/providerWareHouseGroupVirtualDetailService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody WareHouseGroupVirtualDetail[] beans);

	@PostMapping("/providerWareHouseGroupVirtualDetailService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody WareHouseGroupVirtualDetail[] beans);

	@PostMapping("/providerWareHouseGroupVirtualDetailService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerWareHouseGroupVirtualDetailService/findObjectById")
	public WareHouseGroupVirtualDetail[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerWareHouseGroupVirtualDetailService/queryObject")
	public WareHouseGroupVirtualDetail[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerWareHouseGroupVirtualDetailService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerWareHouseGroupVirtualDetailService/queryPageObject")
	public WareHouseGroupVirtualDetail[] queryPageObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);


}
