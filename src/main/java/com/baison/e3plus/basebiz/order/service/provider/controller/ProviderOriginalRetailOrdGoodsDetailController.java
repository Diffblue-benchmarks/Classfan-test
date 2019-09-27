package com.baison.e3plus.basebiz.order.service.provider.controller;

import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrdGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrdGoodsDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产者模块-IOriginalRetailOrdGoodsDetailService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerOriginalRetailOrdGoodsDetailService")
public class ProviderOriginalRetailOrdGoodsDetailController {

	@Autowired
	private IOriginalRetailOrdGoodsDetailService originalRetailOrdGoodsDetailService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<OriginalRetailOrdGoodsDetail> beans) {
		return originalRetailOrdGoodsDetailService.create(token, beans);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return originalRetailOrdGoodsDetailService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public OriginalRetailOrdGoodsDetail[] queryPage(@RequestParam(name = "token", required = true) String token,
													@RequestBody E3Selector selector,
													@RequestParam(name = "pageSize", required = true) int pageSize,
													@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return originalRetailOrdGoodsDetailService.queryPage(token, selector,pageSize , pageIndex);
	}



}
