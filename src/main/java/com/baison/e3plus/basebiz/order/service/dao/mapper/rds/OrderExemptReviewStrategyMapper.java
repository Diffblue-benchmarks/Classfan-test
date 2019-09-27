package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategy;

public interface OrderExemptReviewStrategyMapper {

    OrderExemptReviewStrategy[] selectByCode(String[] code);

    int insertSelective(OrderExemptReviewStrategy orderExemptReviewStrategy);

    int updateByPrimaryKeySelectiveBatch(OrderExemptReviewStrategy orderExemptReviewStrategy);

}
