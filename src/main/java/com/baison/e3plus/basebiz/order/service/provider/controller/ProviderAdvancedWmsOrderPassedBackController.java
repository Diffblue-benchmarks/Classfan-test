package com.baison.e3plus.basebiz.order.service.provider.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.AdvancedWmsOrderPassedBack;
import com.baison.e3plus.basebiz.order.api.passedback.IAdvancedWmsOrderPassedBackService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IAdvancedWmsOrderPassedBackService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerAdvancedWmsOrderPassedBackService")
public class ProviderAdvancedWmsOrderPassedBackController {

	@Autowired
	private IAdvancedWmsOrderPassedBackService iAdvancedWmsOrderPassedBackService;
	
	@PostMapping(value = "createPassedBack")
	public ServiceResult createPassedBack(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billType", required = false) String billType, 
			@RequestParam(name = "billNo", required = false) String billNo, 
			@RequestParam(name = "wmsBillNo", required = false) String wmsBillNo, 
			@RequestParam(name = "postData", required = false) String postData) {
		return iAdvancedWmsOrderPassedBackService.createPassedBack(token, billType, billNo, wmsBillNo, postData);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iAdvancedWmsOrderPassedBackService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPageObject")
	public AdvancedWmsOrderPassedBack[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iAdvancedWmsOrderPassedBackService.queryPageObject(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "updateBatchById")
	public int updateBatchById(@RequestBody Map map) {
		return iAdvancedWmsOrderPassedBackService.updateBatchById(map);
	}
	
	@PostMapping(value = "updateBatch")
	public int updateBatch(@RequestBody List<AdvancedWmsOrderPassedBack> bean) {
		return iAdvancedWmsOrderPassedBackService.updateBatch(bean);
	}
	
	

}
