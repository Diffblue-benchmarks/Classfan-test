package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategy;
import com.baison.e3plus.basebiz.order.api.service.IOrderExemptReviewStrategyService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderExemptReviewStrategyMapper;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class OrderExemptReviewStrategyService implements IOrderExemptReviewStrategyService {

    @Autowired
    private OrderExemptReviewStrategyMapper orderExemptReviewStrategyMapper;

    @Override
    public OrderExemptReviewStrategy[] findObjectByCode(String token, Object[] codes) {
        String[] codeArr = Arrays.stream(codes).map(t -> String.valueOf(t)).toArray(String[]::new);

        OrderExemptReviewStrategy[] orderExemptReviewStrategies = orderExemptReviewStrategyMapper.selectByCode(codeArr);
        return orderExemptReviewStrategies;
    }

    @Override
    public ServiceResult modifyObject(String token, OrderExemptReviewStrategy[] beans) {
        ServiceResult serviceResult = new ServiceResult();
        if (ArrayUtils.isEmpty(beans)) {
            return serviceResult;
        }

        for (OrderExemptReviewStrategy orderExemptReviewStrategy : beans) {
            orderExemptReviewStrategy.setLaskDateUpdate(new Date());
            orderExemptReviewStrategyMapper.updateByPrimaryKeySelectiveBatch(orderExemptReviewStrategy);
        }

        return serviceResult;
    }

}
