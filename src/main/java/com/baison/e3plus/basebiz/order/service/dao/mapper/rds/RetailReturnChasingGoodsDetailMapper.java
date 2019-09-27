package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baison.e3plus.basebiz.order.api.model.RetailReturnChasingGoodsDetail;

@Mapper
public interface RetailReturnChasingGoodsDetailMapper {
	/**
	 * 根据销售退单id删除
	 * 
	 * @param returnId
	 * @return
	 */
	int deleteByReturnId(List<Object> returnIds);

	int deleteByPrimaryKey(List<Object> ids);

	int insert(RetailReturnChasingGoodsDetail record);

	int insertSelective(List<RetailReturnChasingGoodsDetail> records);

	RetailReturnChasingGoodsDetail selectByPrimaryKey(@Param("id") Object id, @Param("fields") String fields);

	int updateByPrimaryKeySelective(RetailReturnChasingGoodsDetail record);

	int updateByPrimaryKey(RetailReturnChasingGoodsDetail record);

	/**
	 * 查询总数
	 * 
	 * @param args
	 * @return
	 */
	Long getListCount(Map<String, Object> args);

	/**
	 * 分页查询结果
	 * 
	 * @param args
	 * @return
	 */
	List<RetailReturnChasingGoodsDetail> queryPage(Map<String, Object> args);

	/**
	 * 批量插入数据
	 * 
	 * @param records
	 */
	void batchInsertSelective(List<RetailReturnChasingGoodsDetail> records);
	
	/**
	 * 批量更新数据
	 * @param records
	 */
	void batchUpdateByPrimaryKeySelective(List<RetailReturnChasingGoodsDetail> records);
}