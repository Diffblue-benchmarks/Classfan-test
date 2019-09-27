package com.baison.e3plus.basebiz.order.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RequestRetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderAdapterResponse;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeParas;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeResponse;
import com.baison.e3plus.basebiz.order.api.service.calculate.IOrderDistributeCalculateService;

/**
 * 生产者模块-IOrderDistributeCalculateService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerOrderDistributeCalculateService")
public class ProviderOrderDistributeCalculateController {

	@Autowired
	private IOrderDistributeCalculateService service;
	
	@PostMapping(value = "distributeOrder")
	public OrderDistributeResponse distributeOrder(@RequestParam(name = "token", required = true) String token, 
			@RequestBody OrderDistributeParas paras) {
		return service.distributeOrder(token, paras);
	}
	
	@PostMapping(value = "cancelOrderDistribute")
	public void cancelOrderDistribute(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "orderNo", required = false) String orderNo) {
		service.cancelOrderDistribute(token, orderNo);
	}
	
	@PostMapping(value = "adapterOrder")
	public OrderAdapterResponse adapterOrder(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RequestRetailOrderBill order) {
		return service.adapterOrder(token, order);
	}
	

}
