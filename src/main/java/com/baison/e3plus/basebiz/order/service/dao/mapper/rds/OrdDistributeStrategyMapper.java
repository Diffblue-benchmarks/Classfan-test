package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;

import com.baison.e3plus.basebiz.order.api.model.OrderDistributeStrategy;
import com.baison.e3plus.basebiz.order.service.dao.model.example.OrdDistributeStrategyExample;

public interface OrdDistributeStrategyMapper {
    int deleteByPrimaryKey(Long orderDistributeStrategyId);

    int insert(OrderDistributeStrategy record);

    int insertSelective(OrderDistributeStrategy record);

    OrderDistributeStrategy selectByPrimaryKey(Long orderDistributeStrategyId);

    int updateByPrimaryKeySelective(OrderDistributeStrategy record);

    int updateByPrimaryKey(OrderDistributeStrategy record);

    List<OrderDistributeStrategy> selectByExample(OrdDistributeStrategyExample example);

    int enableByPrimaryKey(Long id);

    int disableByPrimaryKey(Long id);

    List<OrderDistributeStrategy> selectAll();

    Long count();

    List<OrderDistributeStrategy> selectByPrimaryKeyBatch(Long[] idArr);

    int insertBatch(OrderDistributeStrategy[] beans);

    int deleteByPrimaryKeyBatch(Long[] idArr);

    int enableByPrimaryKeyBatch(Long[] idArr);

    int disableByPrimaryKeyBatch(Long[] idArr);

    int updateByPrimaryKeySelectiveBatch(OrderDistributeStrategy[] inputBill);

    List<Long> selectIdsByExample(OrdDistributeStrategyExample example);
}