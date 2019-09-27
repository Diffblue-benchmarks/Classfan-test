package com.baison.e3plus.basebiz.order.service.provider.controller;

import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategyDetail;
import com.baison.e3plus.basebiz.order.api.service.IOrderExemptReviewStrategyDetailService;
import com.baison.e3plus.common.cncore.query.E3Selector;
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
@RequestMapping(value = "providerOrderExemptReviewStrategyDetailService")
public class ProviderOrderExemptReviewStrategyDetailController {

	@Autowired
	private IOrderExemptReviewStrategyDetailService iOrderExemptReviewStrategyDetailService;

	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iOrderExemptReviewStrategyDetailService.getListCount(token, selector);
	}

	@PostMapping(value = "queryObject")
	public OrderExemptReviewStrategyDetail[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iOrderExemptReviewStrategyDetailService.queryObject(token, selector);
	}

	@PostMapping(value = "queryPageObject")
	public OrderExemptReviewStrategyDetail[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
													 @RequestParam(name = "pageSize", required = true) int pageSize,
													 @RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iOrderExemptReviewStrategyDetailService.queryPageObject(token, selector, pageSize , pageIndex);
	}

	@PostMapping(value = "createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
									  @RequestBody OrderExemptReviewStrategyDetail[] orderExemptReviewStrategyDetails) {
		return iOrderExemptReviewStrategyDetailService.createObject(token, orderExemptReviewStrategyDetails);
	}

	@PostMapping(value = "modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
									  @RequestBody OrderExemptReviewStrategyDetail[] orderExemptReviewStrategyDetails) {
		return iOrderExemptReviewStrategyDetailService.modifyObject(token, orderExemptReviewStrategyDetails);
	}

	@PostMapping(value = "removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
									  @RequestBody Object[] ids) {
		return iOrderExemptReviewStrategyDetailService.removeObject(token, ids);
	}

}
