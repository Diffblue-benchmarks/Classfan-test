package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.ODSShopRefusalRecord;
import com.baison.e3plus.basebiz.order.service.dao.model.example.ODSShopRefusalRecordExample;

import java.util.List;

public interface ODSShopRefusalRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ODSShopRefusalRecord record);

    int insertSelective(ODSShopRefusalRecord record);

    List<ODSShopRefusalRecord> selectByExample(ODSShopRefusalRecordExample example);

    ODSShopRefusalRecord selectByPrimaryKey(Long id);
    
    List<ODSShopRefusalRecord> selectByPrimaryKeyBatch(Long[] idArr);

    int updateByPrimaryKeySelective(ODSShopRefusalRecord record);

    int updateByPrimaryKey(ODSShopRefusalRecord record);
    
    int insertBatch(ODSShopRefusalRecord[] beans);
    
    int updateByPrimaryKeySelectiveBatch(ODSShopRefusalRecord[] beans);
    
    int deleteByPrimaryKeyBatch(Long[] idArr);
    
    long countByExample(ODSShopRefusalRecordExample example);
}