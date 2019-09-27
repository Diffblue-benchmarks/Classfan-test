package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterWareHouseDetail;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedAdapterWareHouseDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IAdvancedAdapterWareHouseDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerAdvancedAdapterWareHouseDetailService")
public class ProviderAdvancedAdapterWareHouseDetailController {

	@Autowired
	private IAdvancedAdapterWareHouseDetailService iAdvancedAdapterWareHouseDetailService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedAdapterWareHouseDetail[] beans) {
		return iAdvancedAdapterWareHouseDetailService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedAdapterWareHouseDetail[] beans) {
		return iAdvancedAdapterWareHouseDetailService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iAdvancedAdapterWareHouseDetailService.removeObject(token, ids);
	}
	
	@PostMapping(value = "queryObject")
	public AdvancedAdapterWareHouseDetail[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedAdapterWareHouseDetailService.queryObject(token, selector);
	}
	
	@PostMapping(value = "findObjectById")
	public AdvancedAdapterWareHouseDetail[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iAdvancedAdapterWareHouseDetailService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedAdapterWareHouseDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public AdvancedAdapterWareHouseDetail[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iAdvancedAdapterWareHouseDetailService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	

}
