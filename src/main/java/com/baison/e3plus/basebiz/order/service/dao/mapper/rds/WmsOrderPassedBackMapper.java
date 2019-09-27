package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import org.apache.ibatis.annotations.Mapper;

import com.baison.e3plus.basebiz.order.api.model.AdvancedWmsOrderPassedBack;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOrderPassedBackMapper {
    int deleteByPrimaryKey(String id);

    int insert(AdvancedWmsOrderPassedBack record);

    int insertSelective(AdvancedWmsOrderPassedBack record);

    AdvancedWmsOrderPassedBack selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AdvancedWmsOrderPassedBack record);

    int updateByPrimaryKey(AdvancedWmsOrderPassedBack record);

    int getListCount(Map map);

    AdvancedWmsOrderPassedBack[] querPage(Map map);

    int saveBatch(AdvancedWmsOrderPassedBack[] bean);
    int updateBatch(List<AdvancedWmsOrderPassedBack> bean);
    int updateBatchById(Map map);


}