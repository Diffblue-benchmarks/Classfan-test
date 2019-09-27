package com.baison.e3plus.basebiz.order.service.provider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyOnlineDetail;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyOnlineDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IWareHousePriorityStrategyOnlineDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerWareHousePriortyStrategyOnlineDetailService")
public class ProviderWareHousePriortyStrategyOnlineDetailController {

	@Autowired
	private IWareHousePriorityStrategyOnlineDetailService service;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody WareHousePriorityStrategyOnlineDetail[] beans) {
		return service.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody WareHousePriorityStrategyOnlineDetail[] beans) {
		return service.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Long[] ids) {
		return service.remove(token, ids);
	}
	@PostMapping(value = "findById")
	public WareHousePriorityStrategyOnlineDetail findById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Long ids) {
		return service.findById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return service.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public WareHousePriorityStrategyOnlineDetail[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return service.queryPage(token, selector,pageSize , pageIndex);
	}
	
	
	@PostMapping(value = "importData")
	public String importData(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<WareHousePriorityStrategyOnlineDetail> details, 
			@RequestParam(name = "pid", required = false) Long pid) {
		return service.importData(token, details,pid);
	}
	

}
