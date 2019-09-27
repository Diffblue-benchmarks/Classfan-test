package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterWareHouseDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedAdapterWareHouseDetailExample;

import java.util.List;

public interface AdvancedAdapterWareHouseDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdvancedAdapterWareHouseDetail record);

    int insertSelective(AdvancedAdapterWareHouseDetail record);

    List<AdvancedAdapterWareHouseDetail> selectByExample(AdvancedAdapterWareHouseDetailExample example);

    AdvancedAdapterWareHouseDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdvancedAdapterWareHouseDetail record);

    int updateByPrimaryKey(AdvancedAdapterWareHouseDetail record);

    long countByExample(AdvancedAdapterWareHouseDetailExample example);

    int insertBatch(AdvancedAdapterWareHouseDetail[] beans);

    int updateByPrimaryKeySelectiveBatch(AdvancedAdapterWareHouseDetail[] sortedBeans);

    int deleteByPrimaryKeyBatch(Long[] idsArr);

    List<AdvancedAdapterWareHouseDetail> selectByPrimaryKeyBatch(Long[] idArr);

    int deleteByExpressStrategyIdBatch(Long[] idArr);
}