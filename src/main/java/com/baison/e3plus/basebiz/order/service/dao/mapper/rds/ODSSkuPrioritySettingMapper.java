package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.ODSSkuPrioritySetting;
import com.baison.e3plus.basebiz.order.service.dao.model.example.ODSSkuPrioritySettingExample;

import java.util.List;

public interface ODSSkuPrioritySettingMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ODSSkuPrioritySetting record);

    int insertSelective(ODSSkuPrioritySetting record);

    List<ODSSkuPrioritySetting> selectByExample(ODSSkuPrioritySettingExample example);

    ODSSkuPrioritySetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ODSSkuPrioritySetting record);

    int updateByPrimaryKey(ODSSkuPrioritySetting record);

    int insertBatch(ODSSkuPrioritySetting[] beans);

    int deleteByPrimaryKeyBatch(Long[] idArr);

    List<ODSSkuPrioritySetting> selectByPrimaryKeyBatch(Long[] idArr);

    long countByExample(ODSSkuPrioritySettingExample example);

    int updateByPrimaryKeySelectiveBatch(ODSSkuPrioritySetting[] sortedBeans);
}