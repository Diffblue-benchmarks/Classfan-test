package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RetailOrderGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderGoodsDetailService;
import com.baison.e3plus.biz.support.api.base.E3SelectorTemp;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3OrderByField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IRetailOrderGoodsDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerRetailOrderGoodsDetailService")
public class ProviderRetailOrderGoodsDetailController {

	@Autowired
	private IRetailOrderGoodsDetailService iRetailOrderGoodsDetailService;
	
	@PostMapping("updateBarcoded")
	public int updateBarcode(@RequestParam(name = "newBarcode", required = false) String newBarcode, @RequestParam(name = "oldBarcode", required = false) String oldBarcode) {
		return iRetailOrderGoodsDetailService.updateBarcode(newBarcode, oldBarcode);
	}
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderGoodsDetail[] beans) {
		return iRetailOrderGoodsDetailService.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderGoodsDetail[] beans) {
		return iRetailOrderGoodsDetailService.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iRetailOrderGoodsDetailService.remove(token, ids);
	}
	
	@PostMapping(value = "findById")
	public RetailOrderGoodsDetail[] findById(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "pkIds", required = false) Object[] pkIds, 
			@RequestParam(name = "fields", required = false) String[] fields) {
		return iRetailOrderGoodsDetailService.findById(token, pkIds, fields);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iRetailOrderGoodsDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public RetailOrderGoodsDetail[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iRetailOrderGoodsDetailService.queryPage(token, selector,pageSize , pageIndex);
	}

}
