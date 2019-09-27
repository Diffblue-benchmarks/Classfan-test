package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterPriorityDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedAdapterPriorityDetailExample;

import java.util.List;

public interface AdvancedAdapterPriorityDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdvancedAdapterPriorityDetail record);

    int insertSelective(AdvancedAdapterPriorityDetail record);

    List<AdvancedAdapterPriorityDetail> selectByExample(AdvancedAdapterPriorityDetailExample example);

    AdvancedAdapterPriorityDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdvancedAdapterPriorityDetail record);

    int updateByPrimaryKey(AdvancedAdapterPriorityDetail record);

    int insertBatch(AdvancedAdapterPriorityDetail[] beans);

    int updateByPrimaryKeySelectiveBatch(AdvancedAdapterPriorityDetail[] sortedBeans);

    int deleteByPrimaryKeyBatch(Long[] idArr);

    List<AdvancedAdapterPriorityDetail> selectByPrimaryKeyBatch(Long[] idsArr);

    long countByExample(AdvancedAdapterPriorityDetailExample example);

    int deleteByExpressStrategyIdBatch(Long[] idArr);

    List<AdvancedAdapterPriorityDetail> queryAvailableDeliveryType(Long[] idArr);
}