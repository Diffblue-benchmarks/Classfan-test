package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategy;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IWareHousePriorityStrategyService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerWareHousePriorityStrategyService")
public class ProviderWareHousePriorityStrategyController {

	@Autowired
	private IWareHousePriorityStrategyService service;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody WareHousePriorityStrategy[] beans) {
		return service.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody WareHousePriorityStrategy[] beans) {
		return service.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "pkIds", required = false) Long[] pkIds) {
		return service.remove(token, pkIds);
	}
	
	@PostMapping(value = "findById")
	public WareHousePriorityStrategy findById(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "pkIds", required = false) Long pkIds) {
		return service.findById(token, pkIds);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return service.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public WareHousePriorityStrategy[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return service.queryPage(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "strategyId", required = false) Long strategyId) {
		return service.enable(token, strategyId);
	}
	
	@PostMapping(value = "disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "strategyId", required = false) Long strategyId) {
		return service.disable(token, strategyId);
	}

}
