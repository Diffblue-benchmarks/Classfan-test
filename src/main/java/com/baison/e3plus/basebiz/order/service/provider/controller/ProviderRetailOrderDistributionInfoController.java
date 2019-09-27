package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderDistributionInfoService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IRetailOrderDistributionInfoService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerRetailOrderDistributionInfoService")
public class ProviderRetailOrderDistributionInfoController {

	@Autowired
	private IRetailOrderDistributionInfoService iRetailOrderDistributionInfoService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderDistributionInfo[] beans) {
		return iRetailOrderDistributionInfoService.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderDistributionInfo[] beans) {
		return iRetailOrderDistributionInfoService.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iRetailOrderDistributionInfoService.remove(token, ids);
	}
	
	@PostMapping(value = "findById")
	public RetailOrderDistributionInfo[] findById(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "pkIds", required = false) Object[] pkIds, 
			@RequestParam(name = "fields", required = false) String[] fields) {
		return iRetailOrderDistributionInfoService.findById(token, pkIds, fields);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iRetailOrderDistributionInfoService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public RetailOrderDistributionInfo[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iRetailOrderDistributionInfoService.queryPage(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "updateByBillNo")
	public void updateByBillNo(@RequestParam(name = "billNo", required = false) String billNo, 
			@RequestParam(name = "receiver", required = false) String receiver, 
			@RequestParam(name = "tel", required = false) String tel, 
			@RequestParam(name = "receiverTel", required = false) String receiverTel,
			@RequestParam(name = "key", required = false) String key) {
		iRetailOrderDistributionInfoService.updateByBillNo(billNo, receiver, tel, receiverTel, key);
	}
	

}
