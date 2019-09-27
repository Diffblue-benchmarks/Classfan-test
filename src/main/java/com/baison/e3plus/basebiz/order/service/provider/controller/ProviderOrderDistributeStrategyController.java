package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.OrderDistributeStrategy;
import com.baison.e3plus.basebiz.order.api.service.odsbase.IOrderDistributeStrategyService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IOrderDistributeStrategyService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerOrderDistributeStrategyService")
public class ProviderOrderDistributeStrategyController {

	@Autowired
	private IOrderDistributeStrategyService iOrderDistributeStrategyService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody OrderDistributeStrategy[] beans) {
		return iOrderDistributeStrategyService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody OrderDistributeStrategy[] beans) {
		return iOrderDistributeStrategyService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iOrderDistributeStrategyService.removeObject(token, ids);
	}
	
	@PostMapping(value = "queryObject")
	public OrderDistributeStrategy[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iOrderDistributeStrategyService.queryObject(token, selector);
	}
	
	@PostMapping(value = "findObjectById")
	public OrderDistributeStrategy[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iOrderDistributeStrategyService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iOrderDistributeStrategyService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public OrderDistributeStrategy[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iOrderDistributeStrategyService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iOrderDistributeStrategyService.enable(token, ids);
	}
	
	@PostMapping(value = "disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iOrderDistributeStrategyService.disable(token, ids);
	}
	

}
