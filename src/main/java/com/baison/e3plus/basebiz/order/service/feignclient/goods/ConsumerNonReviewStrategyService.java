package com.baison.e3plus.basebiz.order.service.feignclient.goods;

import com.baison.e3plus.basebiz.order.api.config.BasebizOrderApiConfig;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderBill;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = BasebizOrderApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerNonReviewStrategyService")
public interface ConsumerNonReviewStrategyService {
    @PostMapping("/providerNonReviewService/nonReviewStrategy")
    public ServiceResult nonReviewStrategy(@RequestParam(name = "token", required = true) String token,
                                           @RequestBody RetailOrderBill[] retailOrderBills);
}
