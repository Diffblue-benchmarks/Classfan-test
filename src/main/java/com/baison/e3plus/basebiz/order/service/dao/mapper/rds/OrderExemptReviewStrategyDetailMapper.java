package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategyDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.OrderExemptReviewStrategyDetailExample;

import java.util.List;

public interface OrderExemptReviewStrategyDetailMapper {

    Long count(OrderExemptReviewStrategyDetailExample example);

    List<OrderExemptReviewStrategyDetail> selectByExample(OrderExemptReviewStrategyDetailExample example);

    int insertSelective(OrderExemptReviewStrategyDetail record);

    int updateByPrimaryKeySelectiveBatch(OrderExemptReviewStrategyDetail record);

    int deleteByPrimaryKey(String id);

}
