package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.ODSSkuPrioritySetting;
import com.baison.e3plus.basebiz.order.api.service.IODSSkuPrioritySettingService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IODSSkuPrioritySettingService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerODSSkuPrioritySettingService")
public class ProviderODSSkuPrioritySettingController {

	@Autowired
	private IODSSkuPrioritySettingService iODSSkuPrioritySettingService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody ODSSkuPrioritySetting[] beans) {
		return iODSSkuPrioritySettingService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody ODSSkuPrioritySetting[] beans) {
		return iODSSkuPrioritySettingService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iODSSkuPrioritySettingService.removeObject(token, ids);
	}
	
	@PostMapping(value = "findObjectById")
	public ODSSkuPrioritySetting[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iODSSkuPrioritySettingService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iODSSkuPrioritySettingService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public ODSSkuPrioritySetting[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iODSSkuPrioritySettingService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	
	

}
