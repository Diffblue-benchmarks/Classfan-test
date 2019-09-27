package com.baison.e3plus.basebiz.order.service.provider.controller;

import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategy;
import com.baison.e3plus.basebiz.order.api.service.IOrderExemptReviewStrategyService;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 生产者模块-IOrderDistributeStrategyService
 * 
 * @author jianwen.zeng
 *
 */
@RestController
@RequestMapping(value = "providerOrderExemptReviewStrategyService")
public class ProviderOrderExemptReviewStrategyController {

	@Autowired
	private IOrderExemptReviewStrategyService iOrderExemptReviewStrategyService;
	
	@PostMapping(value = "findObjectByCode")
	public OrderExemptReviewStrategy[] findObjectByCode(@RequestParam(name = "token", required = true) String token,
												  @RequestBody Object[] codes) {
		return iOrderExemptReviewStrategyService.findObjectByCode(token, codes);
	}

	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
									  @RequestBody OrderExemptReviewStrategy[] orderExemptReviewStrategys) {
		return iOrderExemptReviewStrategyService.modifyObject(token, orderExemptReviewStrategys);
	}

}
