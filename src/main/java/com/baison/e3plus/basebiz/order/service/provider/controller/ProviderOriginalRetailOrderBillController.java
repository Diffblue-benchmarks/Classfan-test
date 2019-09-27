package com.baison.e3plus.basebiz.order.service.provider.controller;

import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderBill;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrderBillService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产者模块-IOriginalRetailOrderBillService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerOriginalRetailOrderBillService")
public class ProviderOriginalRetailOrderBillController {

	@Autowired
	private IOriginalRetailOrderBillService originalRetailOrderBillService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<OriginalRetailOrderBill> beans) {
		return originalRetailOrderBillService.create(token, beans);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return originalRetailOrderBillService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public OriginalRetailOrderBill[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return originalRetailOrderBillService.queryPage(token, selector,pageSize , pageIndex);
	}

	@PostMapping(value = "updateOrderStatus")
	public int updateOrderStatus(@RequestParam(name = "token", required = true) String token,
								 @RequestParam(name = "orderStatus", required = true) Long orderStatus,
								 @RequestBody List<String> billNos){
		return originalRetailOrderBillService.updateOrderStatus(token, orderStatus , billNos);
	}

	@PostMapping(value = "queryOrderBill")
	public List<OriginalRetailOrderBill> queryOrderBill(@RequestParam(name = "token", required = true) String token){
		return originalRetailOrderBillService.queryOrderBill(token);
	}

	@PostMapping(value = "queryObject")
	public List<OriginalRetailOrderBill> queryObject(@RequestParam(name = "token", required = true) String token,
														@RequestBody E3Selector selector){
		return originalRetailOrderBillService.queryObject(token, selector);
	}

}
