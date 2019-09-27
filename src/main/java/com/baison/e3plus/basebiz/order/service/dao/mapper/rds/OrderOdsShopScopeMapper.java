package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;


import java.util.List;

import com.baison.e3plus.basebiz.order.api.model.OrderOdsShopScope;
import com.baison.e3plus.basebiz.order.api.model.OrderOdsShopScopeDetail;
import com.baison.e3plus.basebiz.order.service.dao.model.example.OrderOdsShopScopeExample;

public interface OrderOdsShopScopeMapper {
    int deleteByPrimaryKey(Long shopScopeId);

    int insert(OrderOdsShopScope record);

    int insertSelective(OrderOdsShopScope record);

    List<OrderOdsShopScope> selectByExample(OrderOdsShopScopeExample example);

    List<OrderOdsShopScope> selectByOdsId(Long shopScopeId);

    int updateByPrimaryKeySelective(OrderOdsShopScope record);

    int updateByPrimaryKey(OrderOdsShopScope record);

    int deleteByOdsIdBatch(Long[] strategyIdArr);

    int deleteDetailByOdsIdBatch(Long[] strategyIdArr);

    int insertBatch(List<OrderOdsShopScope> saveShopScopes);

    List<OrderOdsShopScope> selectByOdsIdBatch(Long[] idsArr);

    int insertDetailBatch(List<OrderOdsShopScopeDetail> saveDetailList);
}