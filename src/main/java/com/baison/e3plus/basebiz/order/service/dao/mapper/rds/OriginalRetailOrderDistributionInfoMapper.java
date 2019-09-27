package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderDistributionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OriginalRetailOrderDistributionInfoMapper {


    /**
     * 批量插入
     *
     * @mbggenerated
     */
    int insertSelective(List<OriginalRetailOrderDistributionInfo> records);

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
    List<OriginalRetailOrderDistributionInfo> queryPage(Map<String, Object> args);

    /**
     * 按订单号批量查询数据
     *
     * @param billNoList
     * @return
     */
    List<OriginalRetailOrderDistributionInfo> selectByBillNo(@Param("billNoList") List<String> billNoList);
}
