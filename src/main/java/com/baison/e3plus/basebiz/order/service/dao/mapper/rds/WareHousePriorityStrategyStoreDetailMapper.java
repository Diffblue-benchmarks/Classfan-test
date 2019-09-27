package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyStoreDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyStoreDetailExample;
/**
 * 仓库发货优先级策略门店仓明细
 * 
 * @author cong
 *
 */
@Mapper
public interface WareHousePriorityStrategyStoreDetailMapper {
    int deleteByPrimaryKey(List<Long> ids);

    int insert(WareHousePriorityStrategyStoreDetail record);

    int insertSelective(WareHousePriorityStrategyStoreDetail record);

    List<WareHousePriorityStrategyStoreDetail> selectByExample(WareHousePriorityStrategyStoreDetailExample example);

    WareHousePriorityStrategyStoreDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WareHousePriorityStrategyStoreDetail record);

    int updateByPrimaryKey(WareHousePriorityStrategyStoreDetail record);
    
    Long getListCount(WareHousePriorityStrategyStoreDetailExample example);

	/**
	 * 批量插入数据
	 * 
	 * @param records
	 */
	void batchInsertSelective(List<WareHousePriorityStrategyStoreDetail> records);

	/**
	 * 批量更新数据插入数据
	 * 
	 * @param records
	 */
	void batchUpdateByPrimaryKeySelective(List<WareHousePriorityStrategyStoreDetail> records);
	
	/**
	 * 
	 * @param strategyIds
	 * @return
	 */
	int removeByStrategyId(List<Long> strategyIds);

    int deleteByPrimayKey(Long pid);
    
    /**
     * 验证 国家、省、市、区、仓库的唯一性
     * @param wareHousePriorityStrategyStoreDetail
     * @return
     */
    int validUnique(WareHousePriorityStrategyStoreDetail wareHousePriorityStrategyStoreDetail);
}