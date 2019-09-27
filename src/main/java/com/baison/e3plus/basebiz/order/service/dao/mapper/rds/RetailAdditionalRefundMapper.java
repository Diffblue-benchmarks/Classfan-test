package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;


import com.baison.e3plus.basebiz.order.api.model.RetailAdditionalRefund;
import com.baison.e3plus.basebiz.order.service.dao.model.example.RetailAdditionalRefundExample;

import java.util.List;

/**
 * 额外退款报表
 *
 * @author peng.liao
 */
public interface RetailAdditionalRefundMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RetailAdditionalRefund record);

    int insertSelective(RetailAdditionalRefund record);

    List<RetailAdditionalRefund> selectByExample(RetailAdditionalRefundExample example);

    RetailAdditionalRefund selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RetailAdditionalRefund record);

    int updateByPrimaryKey(RetailAdditionalRefund record);

    /**
     * 批量插入数据
     * 备注：为了防止重复插入，当数据库唯一索引存在时，则做该条数据的更新。
     * @param list
     */
    void batchInsert(List<RetailAdditionalRefund> list);

}