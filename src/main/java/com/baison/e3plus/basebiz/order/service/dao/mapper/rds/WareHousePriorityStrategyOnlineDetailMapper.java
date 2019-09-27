package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyOnlineDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyOnlineDetailExample;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 仓库发货优先级策略电商仓明细
 *
 * @author cong
 */
@Mapper
public interface WareHousePriorityStrategyOnlineDetailMapper {
    int deleteByPrimaryKey(List<Long> ids);

    int insert(WareHousePriorityStrategyOnlineDetail record);

    int insertSelective(WareHousePriorityStrategyOnlineDetail record);

    List<WareHousePriorityStrategyOnlineDetail> selectByExample(WareHousePriorityStrategyOnlineDetailExample example);

    WareHousePriorityStrategyOnlineDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WareHousePriorityStrategyOnlineDetail record);

    int updateByPrimaryKey(WareHousePriorityStrategyOnlineDetail record);

    Long getListCount(WareHousePriorityStrategyOnlineDetailExample example);

    /**
     * 批量插入数据
     *
     * @param records
     */
    void batchInsertSelective(List<WareHousePriorityStrategyOnlineDetail> records);

    /**
     * 批量更新数据插入数据
     *
     * @param records
     */
    void batchUpdateByPrimaryKeySelective(List<WareHousePriorityStrategyOnlineDetail> records);

    /**
     * @param strategyIds
     * @return
     */
    int removeByStrategyId(List<Long> strategyIds);

    int deleteByPrimayKey(Long pid);

    int deleteWarehousePriorityIdBatch(List<Long> warehousePriorityIdList);
}