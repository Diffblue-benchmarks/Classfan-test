package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import org.apache.ibatis.annotations.Mapper;

import com.baison.e3plus.basebiz.order.api.model.AdvancedRetailOrderRejection;

@Mapper
public interface AdvancedRetailOrderRejectionMapper {
    int deleteByPrimaryKey(String id);

    int insert(AdvancedRetailOrderRejection record);

    int insertSelective(AdvancedRetailOrderRejection record);

    AdvancedRetailOrderRejection selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AdvancedRetailOrderRejection record);

    int updateByPrimaryKey(AdvancedRetailOrderRejection record);

    int insertbatch(AdvancedRetailOrderRejection[] retailOrderRejections);
}