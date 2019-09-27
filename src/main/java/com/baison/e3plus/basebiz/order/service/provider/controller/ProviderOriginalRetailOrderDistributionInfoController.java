package com.baison.e3plus.basebiz.order.service.provider.controller;

import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrderDistributionInfoService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产者模块-IOriginalRetailOrderDistributionInfoService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerOriginalRetailOrderDistributionInfoService")
public class ProviderOriginalRetailOrderDistributionInfoController {

	@Autowired
	private IOriginalRetailOrderDistributionInfoService originalRetailOrderDistributionInfoService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<OriginalRetailOrderDistributionInfo> beans) {
		return originalRetailOrderDistributionInfoService.create(token, beans);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return originalRetailOrderDistributionInfoService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public OriginalRetailOrderDistributionInfo[] queryPage(@RequestParam(name = "token", required = true) String token,
														   @RequestBody E3Selector selector,
														   @RequestParam(name = "pageSize", required = true) int pageSize,
														   @RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return originalRetailOrderDistributionInfoService.queryPage(token, selector,pageSize , pageIndex);
	}



}
