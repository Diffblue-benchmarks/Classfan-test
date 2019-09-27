package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrdGoodsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OriginalRetailOrdGoodsDetailMapper {

	/**
	 * 批量插入
	 *
	 * @param records
	 * @return
	 */
	int insertSelective(List<OriginalRetailOrdGoodsDetail> records);

	/**
	 *  通过订单id，根据所需查询字段
	 *
	 * @param ids
	 * @param fields
	 * @return
	 */
	List<OriginalRetailOrdGoodsDetail> selectByPrimaryKey(@Param("ids") List<Object> ids, @Param("fields") String fields);

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
	List<OriginalRetailOrdGoodsDetail> queryPage(Map<String, Object> args);

	/**
	 * 批量根据单据号查询
	 *
	 * @param billNoList
	 * @return
	 */
	List<OriginalRetailOrdGoodsDetail> selectByBillNo(@Param("billNoList") List<String> billNoList);


	/**
	 * 批量根据单据ID查询
	 *
	 * @param billIdList
	 * @return
	 */
	List<OriginalRetailOrdGoodsDetail> selectByBillId(@Param("billIdList") List<String> billIdList);

}