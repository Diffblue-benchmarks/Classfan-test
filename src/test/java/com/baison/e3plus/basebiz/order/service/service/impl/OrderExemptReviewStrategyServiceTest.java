package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.BaseOrderTest;
import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategy;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderExemptReviewStrategyMapper;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class OrderExemptReviewStrategyServiceTest extends BaseOrderTest {

    @Autowired
    private OrderExemptReviewStrategyService orderExemptReviewStrategyService;
    @Autowired
    private OrderExemptReviewStrategyMapper orderExemptReviewStrategyMapper;

    @Test
    public void testFindObjectByCode() {
        OrderExemptReviewStrategy orderExemptReviewStrategy = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy.setOrderExemptReviewStrategyId(10001L);
        orderExemptReviewStrategy.setCode("101");
        orderExemptReviewStrategy.setName("name");
        orderExemptReviewStrategy.setVal("val");
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy);

        OrderExemptReviewStrategy[] orderExemptReviewStrategys = orderExemptReviewStrategyService.findObjectByCode(testToken, new Object[] {"101"});
        assertEquals(true, orderExemptReviewStrategys.length > 0);
    }

    @Test
    public void testModifyObject() {
        OrderExemptReviewStrategy orderExemptReviewStrategy = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy.setOrderExemptReviewStrategyId(10001L);
        orderExemptReviewStrategy.setCode("101");
        orderExemptReviewStrategy.setName("name");
        orderExemptReviewStrategy.setVal("val");
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy);

        orderExemptReviewStrategy.setVal("valTest");
        ServiceResult result = orderExemptReviewStrategyService.modifyObject(testToken, new OrderExemptReviewStrategy[]{orderExemptReviewStrategy});
        assertEquals(false, result.hasError() || result.hasWarn());
    }

}
