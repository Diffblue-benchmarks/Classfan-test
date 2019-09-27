package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RetailReturnGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnGoodsDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IRetailReturnGoodsDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "provideriRetailReturnGoodsDetailService")
public class ProviderRetailReturnGoodsDetailController {

	@Autowired
	private IRetailReturnGoodsDetailService iRetailReturnGoodsDetailService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnGoodsDetail[] beans) {
		return iRetailReturnGoodsDetailService.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnGoodsDetail[] beans) {
		return iRetailReturnGoodsDetailService.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iRetailReturnGoodsDetailService.remove(token, ids);
	}
	
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iRetailReturnGoodsDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public RetailReturnGoodsDetail[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iRetailReturnGoodsDetailService.queryPage(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping("updateBarcoded")
	public int updateBarcode(@RequestParam(name = "newBarcode", required = false) String newBarcode, @RequestParam(name = "oldBarcode", required = false) String oldBarcode) {
		return iRetailReturnGoodsDetailService.updateBarcode(newBarcode, oldBarcode);
	}

}
