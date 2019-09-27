package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.BaseOrderTest;
import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategyDetail;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderExemptReviewStrategyDetailMapper;
import com.baison.e3plus.common.bscore.utils.UUIDUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class OrderExemptReviewStrategyDetailServiceTest extends BaseOrderTest {

    @Autowired
    private OrderExemptReviewStrategyDetailService orderExemptReviewStrategyDetailService;
    @Autowired
    private OrderExemptReviewStrategyDetailMapper orderExemptReviewStrategyDetailMapper;

    @Test
    public void testCreateObject() {
        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail = new OrderExemptReviewStrategyDetail();
        orderExemptReviewStrategyDetail.setOrderExemptReviewStrategyDetailId(UUIDUtil.generate());
        orderExemptReviewStrategyDetail.setSingleProductId(1L);
        orderExemptReviewStrategyDetail.setSingleProductCode("code");
        orderExemptReviewStrategyDetail.setType("1");
        ServiceResult result = orderExemptReviewStrategyDetailService.createObject(testToken, new OrderExemptReviewStrategyDetail[]{orderExemptReviewStrategyDetail});
        assertEquals(false, result.hasError() || result.hasWarn());
    }

    @Test
    public void testModifyObject() {
        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail = new OrderExemptReviewStrategyDetail();
        orderExemptReviewStrategyDetail.setOrderExemptReviewStrategyDetailId(UUIDUtil.generate());
        orderExemptReviewStrategyDetail.setSingleProductId(1L);
        orderExemptReviewStrategyDetail.setSingleProductCode("code");
        orderExemptReviewStrategyDetail.setType("1");
        orderExemptReviewStrategyDetailMapper.insertSelective(orderExemptReviewStrategyDetail);

        orderExemptReviewStrategyDetail.setSingleProductDescribe("test");
        ServiceResult result = orderExemptReviewStrategyDetailService.modifyObject(testToken, new OrderExemptReviewStrategyDetail[]{orderExemptReviewStrategyDetail});
        assertEquals(false, result.hasError() || result.hasWarn());
    }

    @Test
    public void testRemoveObject() {
        Object pk = UUIDUtil.generate();
        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail = new OrderExemptReviewStrategyDetail();
        orderExemptReviewStrategyDetail.setOrderExemptReviewStrategyDetailId(String.valueOf(pk));
        orderExemptReviewStrategyDetail.setSingleProductId(1L);
        orderExemptReviewStrategyDetail.setSingleProductCode("code");
        orderExemptReviewStrategyDetail.setType("1");
        orderExemptReviewStrategyDetailMapper.insertSelective(orderExemptReviewStrategyDetail);

        ServiceResult result = orderExemptReviewStrategyDetailService.removeObject(testToken, new Object[]{pk});
        assertEquals(false, result.hasError() || result.hasWarn());
    }

    @Test
    public void testGetListCount() {
        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail = new OrderExemptReviewStrategyDetail();
        orderExemptReviewStrategyDetail.setOrderExemptReviewStrategyDetailId(UUIDUtil.generate());
        orderExemptReviewStrategyDetail.setSingleProductId(1L);
        orderExemptReviewStrategyDetail.setSingleProductCode("code");
        orderExemptReviewStrategyDetail.setType("1");
        orderExemptReviewStrategyDetailMapper.insertSelective(orderExemptReviewStrategyDetail);

        long count = orderExemptReviewStrategyDetailService.getListCount(testToken, new E3Selector());
        assertEquals(true, count > 0);
    }

    @Test
    public void testQueryObject() {
        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail = new OrderExemptReviewStrategyDetail();
        orderExemptReviewStrategyDetail.setOrderExemptReviewStrategyDetailId(UUIDUtil.generate());
        orderExemptReviewStrategyDetail.setSingleProductId(1L);
        orderExemptReviewStrategyDetail.setSingleProductCode("code");
        orderExemptReviewStrategyDetail.setType("1");
        orderExemptReviewStrategyDetailMapper.insertSelective(orderExemptReviewStrategyDetail);

        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("type", "1"));
        OrderExemptReviewStrategyDetail[] orderExemptReviewStrategyDetails = orderExemptReviewStrategyDetailService.queryObject(testToken, selector);
        assertEquals(true, orderExemptReviewStrategyDetails.length > 0);
    }

    @Test
    public void testQueryPageObject() {
        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail = new OrderExemptReviewStrategyDetail();
        orderExemptReviewStrategyDetail.setOrderExemptReviewStrategyDetailId(UUIDUtil.generate());
        orderExemptReviewStrategyDetail.setSingleProductId(1L);
        orderExemptReviewStrategyDetail.setSingleProductCode("code");
        orderExemptReviewStrategyDetail.setType("1");
        orderExemptReviewStrategyDetailMapper.insertSelective(orderExemptReviewStrategyDetail);

        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("type", "1"));
        OrderExemptReviewStrategyDetail[] orderExemptReviewStrategyDetails = orderExemptReviewStrategyDetailService.queryPageObject(testToken, selector, -1, -1);
        assertEquals(true, orderExemptReviewStrategyDetails.length > 0);
    }

}
