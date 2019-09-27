package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RetailOrderSettleDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderSettleDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IRetailOrderSettleDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerRetailOrderSettleDetailService")
public class ProviderRetailOrderSettleDetailController {

	@Autowired
	private IRetailOrderSettleDetailService iRetailOrderSettleDetailService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderSettleDetail[] beans) {
		return iRetailOrderSettleDetailService.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderSettleDetail[] beans) {
		return iRetailOrderSettleDetailService.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iRetailOrderSettleDetailService.remove(token, ids);
	}
	
	@PostMapping(value = "findById")
	public RetailOrderSettleDetail[] findById(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "pkIds", required = false) Object[] pkIds, 
			@RequestParam(name = "fields", required = false) String[] fields) {
		return iRetailOrderSettleDetailService.findById(token, pkIds, fields);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iRetailOrderSettleDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public RetailOrderSettleDetail[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iRetailOrderSettleDetailService.queryPage(token, selector,pageSize , pageIndex);
	}
	

}
