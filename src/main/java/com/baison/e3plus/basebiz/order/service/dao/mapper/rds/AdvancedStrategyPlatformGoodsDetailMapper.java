package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedStrategyPlatformGoodsDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedStrategyPlatformGoodsDetailExample;

import java.util.List;

public interface AdvancedStrategyPlatformGoodsDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdvancedStrategyPlatformGoodsDetail record);

    int insertSelective(AdvancedStrategyPlatformGoodsDetail record);

    List<AdvancedStrategyPlatformGoodsDetail> selectByExample(AdvancedStrategyPlatformGoodsDetailExample example);

    AdvancedStrategyPlatformGoodsDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdvancedStrategyPlatformGoodsDetail record);

    int updateByPrimaryKey(AdvancedStrategyPlatformGoodsDetail record);

    int insertBatch(AdvancedStrategyPlatformGoodsDetail[] beans);

    int updateByPrimaryKeySelectiveBatch(AdvancedStrategyPlatformGoodsDetail[] sortedBeans);

    int deleteByPrimaryKeyBatch(Long[] idArr);

    List<AdvancedStrategyPlatformGoodsDetail> selectByPrimaryKeyBatch(Object[] ids);

    long countByExample(AdvancedStrategyPlatformGoodsDetailExample example);

    int deleteByExpressStrategyIdBatch(Long[] idArr);
}