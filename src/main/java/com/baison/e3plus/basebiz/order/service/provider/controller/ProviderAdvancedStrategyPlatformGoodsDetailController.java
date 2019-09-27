package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedStrategyPlatformGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedStrategyPlatformGoodsDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IAdvancedStrategyPlatformGoodsDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerAdvancedStrategyPlatformGoodsDetailService")
public class ProviderAdvancedStrategyPlatformGoodsDetailController {

	@Autowired
	private IAdvancedStrategyPlatformGoodsDetailService iAdvancedStrategyPlatformGoodsDetailService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedStrategyPlatformGoodsDetail[] beans) {
		return iAdvancedStrategyPlatformGoodsDetailService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedStrategyPlatformGoodsDetail[] beans) {
		return iAdvancedStrategyPlatformGoodsDetailService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iAdvancedStrategyPlatformGoodsDetailService.removeObject(token, ids);
	}
	
	@PostMapping(value = "queryObject")
	public AdvancedStrategyPlatformGoodsDetail[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedStrategyPlatformGoodsDetailService.queryObject(token, selector);
	}
	
	@PostMapping(value = "findObjectById")
	public AdvancedStrategyPlatformGoodsDetail[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iAdvancedStrategyPlatformGoodsDetailService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedStrategyPlatformGoodsDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public AdvancedStrategyPlatformGoodsDetail[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iAdvancedStrategyPlatformGoodsDetailService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	

}
