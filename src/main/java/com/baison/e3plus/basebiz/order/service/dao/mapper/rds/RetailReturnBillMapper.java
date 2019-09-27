package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baison.e3plus.basebiz.order.api.model.RetailReturnBill;

/**
 * 销售退单接口
 * 
 * @author panpan.jia	
 *
 * @since 2018-11-08
 */
@Mapper
public interface RetailReturnBillMapper{
    
	int deleteByPrimaryKey(List<Object> ids);

	int insert(RetailReturnBill record);

	int insertSelective(List<RetailReturnBill> records);

	List<RetailReturnBill> selectByPrimaryKey(@Param("ids")List<Object> ids, @Param("fields") String fields);

	int updateByPrimaryKeySelective(List<RetailReturnBill> records);

	int updateByPrimaryKey(RetailReturnBill record);

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
	List<RetailReturnBill> queryPage(Map<String, Object> args);
	
	/**
	 * 批量插入数据
	 * @param records
	 */
	void batchInsertSelective(List<RetailReturnBill> records);
	
	/**
	 * 批量更新数据
	 * @param records
	 */
	void batchUpdateByPrimaryKeySelective(List<RetailReturnBill> records);
	
	/**
	 * 动态查询结果
	 * 
	 * @param args
	 * @return
	 */
	List<RetailReturnBill> queryObjectBySql(@Param("sql") String sql);

	/**
	 * 根据id批量更新 oms状态
	 * @param map
	 * @return
	 */
	int updateBatchByBillId(Map map);

	List<RetailReturnBill>  selectBySap(@Param("fields") String fields);

	int updateIsConfirm(Map map);
	
	int updateByBillNo(@Param("billno")String billno, @Param("receiverName")String receiverName, @Param("receiverMobile")String receiverMobile, @Param("receiverTel")String receiverTel, @Param("key")String key);
}