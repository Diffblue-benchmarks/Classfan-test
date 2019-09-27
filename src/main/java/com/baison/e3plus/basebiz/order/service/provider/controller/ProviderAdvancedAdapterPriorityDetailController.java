package com.baison.e3plus.basebiz.order.service.provider.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterPriorityDetail;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedAdapterPriorityDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IAdvancedAdapterPriorityDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerAdvancedAdapterPriorityDetailServiceService")
public class ProviderAdvancedAdapterPriorityDetailController {

	@Autowired
	private IAdvancedAdapterPriorityDetailService iAdvancedAdapterPriorityDetailService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedAdapterPriorityDetail[] beans) {
		return iAdvancedAdapterPriorityDetailService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedAdapterPriorityDetail[] beans) {
		return iAdvancedAdapterPriorityDetailService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iAdvancedAdapterPriorityDetailService.removeObject(token, ids);
	}
	
	@PostMapping(value = "queryObject")
	public AdvancedAdapterPriorityDetail[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedAdapterPriorityDetailService.queryObject(token, selector);
	}
	
	@PostMapping(value = "findObjectById")
	public AdvancedAdapterPriorityDetail[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iAdvancedAdapterPriorityDetailService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedAdapterPriorityDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public AdvancedAdapterPriorityDetail[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iAdvancedAdapterPriorityDetailService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "queryAvailableDeliveryType")
	public Map<Long,List<Long>> queryAvailableDeliveryType(@RequestParam(name = "token", required = true) String token, @RequestBody Map<Long,List<Long>> strategyIds) {
		return iAdvancedAdapterPriorityDetailService.queryAvailableDeliveryType(token, strategyIds);
	}
	
	

}
