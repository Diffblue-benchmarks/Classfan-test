package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.ODSShopRefusalRecord;
import com.baison.e3plus.basebiz.order.api.service.odsbase.IODSShopRefusalRecordService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IODSShopRefusalRecordService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerODSShopRefusalRecordService")
public class ProviderODSShopRefusalRecordController {

	@Autowired
	private IODSShopRefusalRecordService iODSShopRefusalRecordService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody ODSShopRefusalRecord[] beans) {
		return iODSShopRefusalRecordService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody ODSShopRefusalRecord[] beans) {
		return iODSShopRefusalRecordService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iODSShopRefusalRecordService.removeObject(token, ids);
	}
	
	@PostMapping(value = "queryObject")
	public ODSShopRefusalRecord[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iODSShopRefusalRecordService.queryObject(token, selector);
	}
	
	@PostMapping(value = "findObjectById")
	public ODSShopRefusalRecord[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iODSShopRefusalRecordService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iODSShopRefusalRecordService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public ODSShopRefusalRecord[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iODSShopRefusalRecordService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	

}
