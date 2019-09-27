package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategy;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyExample;

/**
 * 仓库发货优先级策略
 * 
 * @author cong
 *
 */
@Mapper
public interface WareHousePriorityStrategyMapper {
	int deleteByPrimaryKey(List<Long> ids);

	int insert(WareHousePriorityStrategy record);

	int insertSelective(WareHousePriorityStrategy record);

	List<WareHousePriorityStrategy> selectByExample(WareHousePriorityStrategyExample example);

	WareHousePriorityStrategy selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(WareHousePriorityStrategy record);

	int updateByPrimaryKey(WareHousePriorityStrategy record);

	Long getListCount(WareHousePriorityStrategyExample example);

	/**
	 * 批量插入数据
	 * 
	 * @param records
	 */
	void batchInsertSelective(List<WareHousePriorityStrategy> records);

	/**
	 * 批量更新数据插入数据
	 * 
	 * @param records
	 */
	void batchUpdateByPrimaryKeySelective(List<WareHousePriorityStrategy> records);
}