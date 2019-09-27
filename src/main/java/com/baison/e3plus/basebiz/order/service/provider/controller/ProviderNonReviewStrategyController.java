package com.baison.e3plus.basebiz.order.service.provider.controller;

import com.baison.e3plus.basebiz.order.api.model.NonReviewStrategy;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderBill;
import com.baison.e3plus.basebiz.order.api.service.INonReviewStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 免审策略判断
 * @author liang.zeng
 */
@RestController
@RequestMapping(value = "providerNonReviewStrategyService")
public class ProviderNonReviewStrategyController {

    @Autowired
    private INonReviewStrategyService nonReviewStrategyService;

    @PostMapping(value = "nonReviewStrategy")
    public NonReviewStrategy nonReviewStrategy(@RequestParam(name = "token", required = true) String token,
        @RequestBody RetailOrderBill[] beans) {
        return nonReviewStrategyService.nonReviewStrategy(token, beans);
    }


}
