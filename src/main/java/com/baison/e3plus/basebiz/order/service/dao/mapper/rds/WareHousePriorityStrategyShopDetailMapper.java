package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyShopDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyShopDetailExample;

/**
 * 仓库发货优先级策略店铺明细
 * 
 * @author cong
 *
 */
@Mapper
public interface WareHousePriorityStrategyShopDetailMapper {
    int deleteByPrimaryKey(List<Long> ids);

    int insert(WareHousePriorityStrategyShopDetail record);

    int insertSelective(WareHousePriorityStrategyShopDetail record);

    List<WareHousePriorityStrategyShopDetail> selectByExample(WareHousePriorityStrategyShopDetailExample example);

    WareHousePriorityStrategyShopDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WareHousePriorityStrategyShopDetail record);

    int updateByPrimaryKey(WareHousePriorityStrategyShopDetail record);
    
    Long getListCount(Map<String, Object> args);

	/**
	 * 批量插入数据
	 * 
	 * @param records
	 */
	void batchInsertSelective(List<WareHousePriorityStrategyShopDetail> records);

	/**
	 * 批量更新数据插入数据
	 * 
	 * @param records
	 */
	void batchUpdateByPrimaryKeySelective(List<WareHousePriorityStrategyShopDetail> records);
	
	/**
	 * 
	 * @param strategyIds
	 * @return
	 */
	int removeByStrategyId(List<Long> strategyIds);
}