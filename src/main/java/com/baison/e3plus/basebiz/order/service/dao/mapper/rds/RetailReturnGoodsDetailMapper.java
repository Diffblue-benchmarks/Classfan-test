package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baison.e3plus.basebiz.order.api.model.RetailReturnGoodsDetail;

@Mapper
public interface RetailReturnGoodsDetailMapper {
	/**
	 * 根据销售退单id删除
	 * 
	 * @param returnId
	 * @return
	 */
	int deleteByReturnId(List<Object> returnIds);

	int deleteByPrimaryKey(List<Object> ids);

	int insert(RetailReturnGoodsDetail record);

	int insertSelective(List<RetailReturnGoodsDetail> records);

	RetailReturnGoodsDetail selectByPrimaryKey(@Param("id") Object id, @Param("fields") String fields);

	int updateByPrimaryKeySelective(RetailReturnGoodsDetail record);

	int updateByPrimaryKey(RetailReturnGoodsDetail record);

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
	List<RetailReturnGoodsDetail> queryPage(Map<String, Object> args);
	
	/**
	 * 批量插入数据
	 * 
	 * @param records
	 */
	void batchInsertSelective(List<RetailReturnGoodsDetail> records);
	
	/**
	 * 批量更新数据
	 * @param records
	 */
	void batchUpdateByPrimaryKeySelective(List<RetailReturnGoodsDetail> records);
	
	
	/**
	 * 统计
	 * 
	 * @param args
	 * @return
	 */
	List<RetailReturnGoodsDetail> sum(Map<String, Object> args);
	
	/**
	 * 通过Barcode更新所有Barcode
	 * 
	 * @param newBarcode
	 * @param oldBarcode
	 * @return
	 */
	int updateBarcode(@Param("newBarcode") String newBarcode, @Param("oldBarcode") String oldBarcode);
}