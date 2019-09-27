package com.baison.e3plus.basebiz.order.service.provider.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedExpressStrategy;
import com.baison.e3plus.basebiz.order.api.model.adapter.OmsRetailOrderBill;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedExpressStrategyService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IAdvancedExpressStrategyService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerAdvancedExpressStrategyService")
public class ProviderAdvancedExpressStrategyController {

	@Autowired
	private IAdvancedExpressStrategyService iAdvancedExpressStrategyService;
	
	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedExpressStrategy[] beans) {
		return iAdvancedExpressStrategyService.createObject(token, beans);
	}
	
	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, 
			@RequestBody AdvancedExpressStrategy[] beans) {
		return iAdvancedExpressStrategyService.modifyObject(token, beans);
	}
	
	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iAdvancedExpressStrategyService.removeObject(token, ids);
	}
	
	@PostMapping(value = "queryObject")
	public AdvancedExpressStrategy[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedExpressStrategyService.queryObject(token, selector);
	}
	
	@PostMapping(value = "findObjectById")
	public AdvancedExpressStrategy[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids) {
		return iAdvancedExpressStrategyService.findObjectById(token, ids);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedExpressStrategyService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public AdvancedExpressStrategy[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iAdvancedExpressStrategyService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iAdvancedExpressStrategyService.enable(token, ids);
	}
	
	@PostMapping(value = "disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iAdvancedExpressStrategyService.disable(token, ids);
	}
	
	@PostMapping(value = "filterExpress")
	public ServiceResult filterExpress(@RequestParam(name = "token", required = true) String token, @RequestBody OmsRetailOrderBill bill) {
		return iAdvancedExpressStrategyService.filterExpress(token, bill);
	}

	
	@PostMapping(value = "queryStrategyIdByWareHouseId")
	public Map<Long,List<Long>> queryStrategyIdByWareHouseId(@RequestParam(name = "token", required = true) String token, @RequestBody Set<Long> wareHouseIdSet) {
		return iAdvancedExpressStrategyService.queryStrategyIdByWareHouseId(token, wareHouseIdSet);
	}
	
	

}
