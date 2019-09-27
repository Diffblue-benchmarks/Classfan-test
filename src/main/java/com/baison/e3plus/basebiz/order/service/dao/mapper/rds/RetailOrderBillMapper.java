package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baison.e3plus.basebiz.order.api.model.RetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.SetMealRetailOrderGoods;

@Mapper
public interface RetailOrderBillMapper {

	int deleteByPrimaryKey(List<Object> ids);

	int insert(RetailOrderBill record);

	int insertSelective(List<RetailOrderBill> records);

	List<RetailOrderBill> selectByPrimaryKey(@Param("billNos") List<Object> billNos, @Param("fields") String fields);

	int updateByPrimaryKeySelective(List<RetailOrderBill> records);

	int updateByPrimaryKey(RetailOrderBill record);

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
	List<RetailOrderBill> queryPage(Map<String, Object> args);
	
	/**
	 * 动态查询结果
	 *
	 * @param args
	 * @return
	 */
	List<RetailOrderBill> queryObjectBySql(@Param("sql") String sql);
	void updateBySql(@Param("sql") String sql);

	
	/**
	 * 批量插入数据
	 * @param records
	 */
	void batchInsertSelective(List<RetailOrderBill> records);
	
	/**
	 * 批量更新数据插入数据
	 * @param records
	 */
	void batchUpdateByPrimaryKeySelective(List<RetailOrderBill> records);
	
	/**
	 * 套餐销售明细查询
	 * 根据商品规格查询 setMealRetailOrderGoodsVO所需字段
	 * @param setMealSpecs 商品规格
	 * @return
	 */
	List<SetMealRetailOrderGoods> queryPageBySetMealRetailOrderGoods(@Param("setMealSpecs")String[] setMealSpecs);
	
	/**
	 * 套餐销售明细查询总数
	 * 根据商品规格查询
	 * @param args
	 * @return
	 */
	Long getSetMealRetailOrderGoodsCount(@Param("setMealSpecs")String[] setMealSpecs);

	/**
	 * 根据单号批量更新 oms状态
	 * @param map
	 * @return
	 */
	int updateBatchByBillNo(Map map);

	List<RetailOrderBill> selectBySap(@Param("fields") String fields);

	int updateIsConfirm(Map map);

    /**
     * 根据OMS过滤条件动态查询结果
     *
     * @param args
     * @return
     */
    List<Map<String, Object>> queryRetailOrderBillMapToOms(Map<String, Object> args);

	/**
	 * 根据OMS过滤条件动态查询结果
	 * 查询总数
	 *
	 * @param args
	 * @return
	 */
	Long getRetailOrderBillToOmsCountByMap(Map<String, Object> args);

	/**
	 * 根据订单编号 批量更新 销售订单 是否有退单记录
	 * @param  retailOrderBills
	 * @return
	 */
	int batchUpdateHasReturnByBillNo(List<RetailOrderBill> retailOrderBills);

	/**
	 * 批量中止
	 * @param param
	 * @return
	 */
	int batchTerm(Map<String,Object> param);

	/**
	 * 批量回传修改订单状态与发货状态
	 * @param param
	 * @return
	 */
    int batchCheckOutByDeliveryOrderCode(Map<String, Object> param);

	/**
	 * 批量发出修改订单发货状态
	 * @param param
	 * @return
	 */
	int batchSendOutByDeliveryOrderCode(Map<String, Object> param);

	int updateDistributionStateById(RetailOrderBill retailOrderBill);
}