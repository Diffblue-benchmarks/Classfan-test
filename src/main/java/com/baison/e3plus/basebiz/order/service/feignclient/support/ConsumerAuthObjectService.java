package com.baison.e3plus.basebiz.order.service.feignclient.support;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.userauth.model.AuthObject;
import com.baison.e3plus.biz.support.api.userauth.model.DataItem;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.orm.core.AbstractBean;

/**
 * 消费者模块-IAuthObjectService
 * 
 * @author xinghua.liu
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAuthObjectService")
public interface ConsumerAuthObjectService {

	@PostMapping("/providerAuthObjectService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody AuthObject[] object);

	@PostMapping("/providerAuthObjectService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody AuthObject[] object);

	@PostMapping("/providerAuthObjectService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerAuthObjectService/findObjectById")
	public AuthObject[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerAuthObjectService/queryObject")
	public AuthObject[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerAuthObjectService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerAuthObjectService/getListCountByADS")
	public long getListCountByADS(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerAuthObjectService/queryPageObject")
	public AuthObject[] queryPageObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerAuthObjectService/queryPageObjectByADS")
	public AuthObject[] queryPageObjectByADS(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerAuthObjectService/importData")
	ServiceResult importData(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "businessObjectMetaId", required = true) String businessObjectMetaId,
			@RequestParam(name = "templateId", required = true) String templateId,
			@RequestBody Map<String, List<String[]>> dataMap);

	@PostMapping("/providerAuthObjectService/exportData")
	ServiceResult exportData(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "templateId", required = true) String templateId, @RequestBody E3Selector selector);

	@PostMapping("/providerAuthObjectService/importDetailDatas")
	ServiceResult importDetailDatas(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "businessObjectMetaId", required = true) String businessObjectMetaId,
			@RequestParam(name = "billFormId", required = true) String billFormId,
			@RequestParam(name = "templateId", required = true) String templateId,
			@RequestBody Map<String, List<String[]>> dataMap);

	@PostMapping("/providerAuthObjectService/saveImportDatas")
	ServiceResult saveImportDatas(@RequestParam(name = "token", required = true) String token,
			@RequestBody AbstractBean[] saveBeans, @RequestParam(name = "billId", required = true) String billId);

	@PostMapping("/providerAuthObjectService/enable")
	ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody String id);

	@PostMapping("/providerAuthObjectService/disable")
	ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody String id);

	@PostMapping("/providerAuthObjectService/queryDataItemsByAuthObjectId")
	DataItem[] queryDataItemsByAuthObjectId(@RequestParam(name = "token", required = true) String token,
			@RequestBody String authObjectId);

}
