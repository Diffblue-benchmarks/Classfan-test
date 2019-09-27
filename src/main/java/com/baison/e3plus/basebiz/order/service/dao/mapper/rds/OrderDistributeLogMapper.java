package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;

import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeLog;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeLogWithBLOBs;
import com.baison.e3plus.basebiz.order.service.dao.model.example.OrderDistributeLogExample;

public interface OrderDistributeLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderDistributeLogWithBLOBs record);

    int insertSelective(OrderDistributeLogWithBLOBs record);

    List<OrderDistributeLogWithBLOBs> selectByExampleWithBLOBs(OrderDistributeLogExample example);

    List<OrderDistributeLog> selectByExample(OrderDistributeLogExample example);

    OrderDistributeLogWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDistributeLog record);

    int updateByPrimaryKeyWithBLOBs(OrderDistributeLogWithBLOBs record);

    int updateByPrimaryKey(OrderDistributeLog record);
}