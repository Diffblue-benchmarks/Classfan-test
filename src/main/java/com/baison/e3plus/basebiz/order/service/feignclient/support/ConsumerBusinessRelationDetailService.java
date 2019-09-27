package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.channel.BusinessRelationDetail;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IBusinessRelationDetailService
 * 
 * @author xinghua.liu
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerBusinessRelationDetailService")
public interface ConsumerBusinessRelationDetailService {

	@PostMapping("/providerBusinessRelationDetailService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody BusinessRelationDetail[] beans);

	@PostMapping("/providerBusinessRelationDetailService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody BusinessRelationDetail[] beans);

	@PostMapping("/providerBusinessRelationDetailService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerBusinessRelationDetailService/findObjectById")
	public BusinessRelationDetail[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerBusinessRelationDetailService/queryObject")
	public BusinessRelationDetail[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerBusinessRelationDetailService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerBusinessRelationDetailService/queryPageObject")
	public BusinessRelationDetail[] queryPageObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	@PostMapping("/providerBusinessRelationDetailService/getCurrentChannel")
	public ServiceResult getCurrentChannel(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "saleChannelId", required = true) Long saleChannelId,
			@RequestParam(name = "purStockChannelId", required = true) Long purStockChannelId);

}
