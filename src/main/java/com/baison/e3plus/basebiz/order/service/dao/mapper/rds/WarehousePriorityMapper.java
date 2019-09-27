package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriority;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityExample;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehousePriorityMapper {
	int deleteByPrimaryKey(Object[] id);

	int insert(WareHousePriority record);

	int insertSelective(WareHousePriority record);

	List<WareHousePriority> selectByExample(WareHousePriorityExample example);

	WareHousePriority selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(WareHousePriority record);

	int modify(List<WareHousePriority> record);

	int updateByPrimaryKey(WareHousePriority record);

	int create(List<WareHousePriority> record);

	WareHousePriority[] queryPage(Map<String, Object> args);

	Long getListCount(Map<String, Object> args);

	WareHousePriority[] queryWareHousePriorityByWareHouseAndArea(@Param("wareHouseIds") List<Long> wareHouseIds,
			@Param("areaId") Long areaId);
}