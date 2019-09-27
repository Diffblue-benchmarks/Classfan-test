package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterShopDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedAdapterShopDetailExample;

import java.util.List;

public interface AdvancedAdapterShopDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdvancedAdapterShopDetail record);

    int insertSelective(AdvancedAdapterShopDetail record);

    List<AdvancedAdapterShopDetail> selectByExample(AdvancedAdapterShopDetailExample example);

    AdvancedAdapterShopDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdvancedAdapterShopDetail record);

    int updateByPrimaryKey(AdvancedAdapterShopDetail record);

    int insertBatch(AdvancedAdapterShopDetail[] bills);

    long countByExample(AdvancedAdapterShopDetailExample example);

    int updateByPrimaryKeySelectiveBatch(AdvancedAdapterShopDetail[] sortedBeans);

    int deleteByPrimaryKeyBatch(Object[] ids);

    List<AdvancedAdapterShopDetail> selectByPrimaryKeyBatch(Long[] idArr);

    int deleteByExpressStrategyIdBatch(Long[] idArr);
}