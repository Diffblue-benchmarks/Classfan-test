package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OriginalRetailOrderBillMapper  {


	/**
	 * 批量插入数据
	 *
	 * @param records
	 * @return
	 */
	int insertSelective(List<OriginalRetailOrderBill> records);

	/**
	 * 通过订单编号，根据所需查询字段
	 *
	 * @param billNos
	 * @param fields
	 * @return
	 */
	List<OriginalRetailOrderBill> selectByPrimaryKey(@Param("billNos") List<Object> billNos, @Param("fields") String fields);

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
	List<OriginalRetailOrderBill> queryPage(Map<String, Object> args);

	/**
	 * 根据单号批量更新 订单状态
	 * @param map
	 * @return
	 */
	int updateOrderStatus(Map map);


}