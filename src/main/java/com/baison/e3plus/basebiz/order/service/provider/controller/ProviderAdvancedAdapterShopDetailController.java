package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterShopDetail;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedAdapterShopDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IAdvancedAdapterShopDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerAdvancedAdapterShopDetailServiceService")
public class ProviderAdvancedAdapterShopDetailController {

	@Autowired
	private IAdvancedAdapterShopDetailService iAdvancedAdapterShopDetailService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedAdapterShopDetail[] beans) {
		return iAdvancedAdapterShopDetailService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedAdapterShopDetail[] beans) {
		return iAdvancedAdapterShopDetailService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iAdvancedAdapterShopDetailService.removeObject(token, ids);
	}
	
	@PostMapping(value = "queryObject")
	public AdvancedAdapterShopDetail[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedAdapterShopDetailService.queryObject(token, selector);
	}
	
	@PostMapping(value = "findObjectById")
	public AdvancedAdapterShopDetail[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iAdvancedAdapterShopDetailService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedAdapterShopDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public AdvancedAdapterShopDetail[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iAdvancedAdapterShopDetailService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	

}
