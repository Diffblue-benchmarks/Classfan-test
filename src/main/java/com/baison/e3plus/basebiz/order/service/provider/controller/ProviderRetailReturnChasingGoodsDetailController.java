package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RetailReturnChasingGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnChasingGoodsDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IAdvancedExpressStrategyService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerRetailReturnChasingGoodsDetailService")
public class ProviderRetailReturnChasingGoodsDetailController {

	@Autowired
	private IRetailReturnChasingGoodsDetailService iRetailReturnChasingGoodsDetailService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnChasingGoodsDetail[] beans) {
		return iRetailReturnChasingGoodsDetailService.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnChasingGoodsDetail[] beans) {
		return iRetailReturnChasingGoodsDetailService.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iRetailReturnChasingGoodsDetailService.remove(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iRetailReturnChasingGoodsDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public RetailReturnChasingGoodsDetail[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iRetailReturnChasingGoodsDetailService.queryPage(token, selector,pageSize , pageIndex);
	}
	

}
