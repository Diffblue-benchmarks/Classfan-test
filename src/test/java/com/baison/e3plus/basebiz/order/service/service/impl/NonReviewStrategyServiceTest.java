package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleSingleProduct;
import com.baison.e3plus.basebiz.order.BaseOrderTest;
import com.baison.e3plus.basebiz.order.api.model.*;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderExemptReviewStrategyDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderExemptReviewStrategyMapper;
import com.baison.e3plus.common.bscore.utils.UUIDUtil;
import com.baison.e3plus.common.cncore.result.model.ErrorObject;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.util.MockBeanUtil;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NonReviewStrategyServiceTest extends BaseOrderTest {
    @InjectMocks
    @Autowired
    NonReviewStrategyService nonAuditStrategyService;
    @Mock
    OrderExemptReviewStrategyService orderExemptReviewStrategyService;
    @Mock
    OrderExemptReviewStrategyDetailService orderExemptReviewStrategyDetailService;
    @Autowired
    OrderExemptReviewStrategyMapper orderExemptReviewStrategyMapper;
    @Autowired
    private OrderExemptReviewStrategyDetailMapper orderExemptReviewStrategyDetailMapper;

    @Test
    public void testNonReviewStrategy() throws FileNotFoundException {
        String jsonFilePath = "/NonAuditStrategyServiceTest.json";
        RetailOrderBill[] retailOrderBills = MockBeanUtil.initArrayFromJSONFile(jsonFilePath,"createObjectJSON",RetailOrderBill.class);
        System.out.println(retailOrderBills.length);

        insertData1 ("1");

      //  insertData2 ("1","100","1000");

       // insertData3 ("1","100");

      //  insertData4 ("1");

        //指定SKU不免审
        List<RetailOrderGoodsDetail> retailOrderGoodsDetails = new ArrayList<>();
        for (RetailOrderBill bills : retailOrderBills) {
            AdvancedSimpleSingleProduct simpleSingleProduct = new AdvancedSimpleSingleProduct();
            simpleSingleProduct.setSingleProductId(7L);
            simpleSingleProduct.setCode("jdtest2XLCN");
            RetailOrderGoodsDetail retailOrderGoodsDetail = new RetailOrderGoodsDetail();
            retailOrderGoodsDetail.setSingleProductId(7L);
            retailOrderGoodsDetail.setSingleProduct((AdvancedSimpleSingleProduct) simpleSingleProduct);
            retailOrderGoodsDetails.add(retailOrderGoodsDetail);
            bills.setGoodsDetail(retailOrderGoodsDetails);
        }
      //  insertData6 ("1");

        //卖家留言
      //  insertData7 ("1");


        //买家留言
     //   insertData8 ("1");

        Mockito.when(orderExemptReviewStrategyService.findObjectByCode(Mockito.any(),Mockito.any())).thenReturn(new OrderExemptReviewStrategy[]{});
        NonReviewStrategy nonReviewStrategy = nonAuditStrategyService.nonReviewStrategy(testToken,retailOrderBills);

        List<RetailOrderBill> failedList = nonReviewStrategy.getFailureList();
        List<RetailOrderBill> successList = nonReviewStrategy.getSuccessList();
        List<RetailOrderBill> delayList = nonReviewStrategy.getDelayList();
        StringBuffer sb = new StringBuffer();
        for (RetailOrderBill retailOrderBill : failedList) {
            //sb.append(erro.getErrorMsg() + "  " + erro.getErrorObject().toString() + "、");
           // System.out.println(sb.toString());
           // RetailOrderBill retailOrderBill  = (RetailOrderBill) erro.getErrorObject();
            System.out.println("不免审订单 billNo:"+retailOrderBill.getBillNo());
        }
        System.out.println("不免审订单数："+failedList.size());
        System.out.println("免审订单数:"+successList.size());
        for (RetailOrderBill retailOrderBill : successList) {
            //RetailOrderBill  retailOrderBill = (RetailOrderBill) result.getSuccessObject().get(i);
            System.out.println("免审订单 billNo:"+retailOrderBill.getBillNo());
        }
        System.out.println("延迟免审时间未到订单数:"+delayList.size());
        //List<ErrorObject> warnObject = result.getWarnObject();
        for (RetailOrderBill retailOrderBill : delayList) {
           // sb.append(erro.getErrorMsg() + "  " + erro.getErrorObject().toString() + "、");
            //System.out.println(sb.toString());
            //RetailOrderBill retailOrderBill  = (RetailOrderBill) erro.getErrorObject();
            System.out.println("延迟免审订单时间:"+retailOrderBill.getExemptAuditDelayTime());
        }

        assertEquals(true,failedList.size() > 0);
    }
    @Test
    public void testNonReviewStrategy2() throws FileNotFoundException {
        String jsonFilePath = "/NonAuditStrategyServiceTest.json";
        RetailOrderBill[] retailOrderBills = MockBeanUtil.initArrayFromJSONFile(jsonFilePath,"createObjectJSON",RetailOrderBill.class);
        System.out.println(retailOrderBills.length);

        insertData1 ("1");

        insertData2 ("1","300","1000");

        insertData3 ("1","100");

        insertData4 ("1");

        //指定SKU不免审
        List<RetailOrderGoodsDetail> retailOrderGoodsDetails = new ArrayList<>();
        for (RetailOrderBill bills : retailOrderBills) {
            AdvancedSimpleSingleProduct simpleSingleProduct = new AdvancedSimpleSingleProduct();
            simpleSingleProduct.setSingleProductId(7L);
            simpleSingleProduct.setCode("jdtest2XLCN");
            RetailOrderGoodsDetail retailOrderGoodsDetail = new RetailOrderGoodsDetail();
            retailOrderGoodsDetail.setSingleProductId(7L);
            retailOrderGoodsDetail.setSingleProduct((AdvancedSimpleSingleProduct) simpleSingleProduct);
            retailOrderGoodsDetails.add(retailOrderGoodsDetail);
            bills.setGoodsDetail(retailOrderGoodsDetails);
        }
        insertData6 ("1");

        //卖家留言
        insertData7 ("1");

        //买家留言
        insertData8 ("1");

        Mockito.when(orderExemptReviewStrategyService.findObjectByCode(Mockito.any(),Mockito.any())).thenReturn(new OrderExemptReviewStrategy[]{});
        NonReviewStrategy nonReviewStrategy = nonAuditStrategyService.nonReviewStrategy(testToken,retailOrderBills);

        List<RetailOrderBill> failedList = nonReviewStrategy.getFailureList();
        List<RetailOrderBill> successList = nonReviewStrategy.getSuccessList();
        List<RetailOrderBill> delayList = nonReviewStrategy.getDelayList();
        StringBuffer sb = new StringBuffer();
        for (RetailOrderBill retailOrderBill : failedList) {
            //sb.append(erro.getErrorMsg() + "  " + erro.getErrorObject().toString() + "、");
            // System.out.println(sb.toString());
            // RetailOrderBill retailOrderBill  = (RetailOrderBill) erro.getErrorObject();
            System.out.println("不免审订单 billNo:"+retailOrderBill.getBillNo());
        }
        System.out.println("不免审订单数："+failedList.size());
        System.out.println("免审订单数:"+successList.size());
        for (RetailOrderBill retailOrderBill : successList) {
            //RetailOrderBill  retailOrderBill = (RetailOrderBill) result.getSuccessObject().get(i);
            System.out.println("免审订单 billNo:"+retailOrderBill.getBillNo());
        }
        System.out.println("延迟免审时间未到订单数:"+delayList.size());
        //List<ErrorObject> warnObject = result.getWarnObject();
        for (RetailOrderBill retailOrderBill : delayList) {
            // sb.append(erro.getErrorMsg() + "  " + erro.getErrorObject().toString() + "、");
            //System.out.println(sb.toString());
            //RetailOrderBill retailOrderBill  = (RetailOrderBill) erro.getErrorObject();
            System.out.println("延迟免审订单时间:"+retailOrderBill.getExemptAuditDelayTime());
        }

        assertEquals(true,failedList.size() > 0);
    }


    //启动订单免审&通知配货策略
    private void insertData1 (String val) {
        OrderExemptReviewStrategy orderExemptReviewStrategy = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy.setOrderExemptReviewStrategyId(1L);
        orderExemptReviewStrategy.setCode("001");
        orderExemptReviewStrategy.setVal(val);
        orderExemptReviewStrategyMapper.updateByPrimaryKeySelectiveBatch(orderExemptReviewStrategy);
    }
    //免审订单金额范围
    private void insertData2 (String val,String val2,String val3) {
        OrderExemptReviewStrategy orderExemptReviewStrategy2 = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy2.setOrderExemptReviewStrategyId(2L);
        orderExemptReviewStrategy2.setCode("002");
        orderExemptReviewStrategy2.setVal(val);
        orderExemptReviewStrategy2.setVal2(val2);
        orderExemptReviewStrategy2.setVal3(val3);
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy2);
    }
    //免审订单商品总数量上限（包含设置的上限数量）
    private void insertData3 (String val,String val2) {
        OrderExemptReviewStrategy orderExemptReviewStrategy3 = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy3.setOrderExemptReviewStrategyId(3L);
        orderExemptReviewStrategy3.setCode("003");
        orderExemptReviewStrategy3.setVal(val);
        orderExemptReviewStrategy3.setVal2(val2);
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy3);
    }
    //订单延迟免审（仅针对指定的延迟配货商店）
    private void insertData4 (String val) {
        OrderExemptReviewStrategy orderExemptReviewStrategy4 = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy4.setOrderExemptReviewStrategyId(4L);
        orderExemptReviewStrategy4.setCode("004");
        orderExemptReviewStrategy4.setVal(val);
        orderExemptReviewStrategy4.setVal2("10");
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy4);

        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail = new OrderExemptReviewStrategyDetail();
        String pk = UUIDUtil.generate();
        orderExemptReviewStrategyDetail.setOrderExemptReviewStrategyDetailId(pk);
        orderExemptReviewStrategyDetail.setShopId(166L);
        orderExemptReviewStrategyDetail.setShopName("ZZAF_ANTAFXJDOB");
        orderExemptReviewStrategyDetail.setType("3");
        orderExemptReviewStrategyDetailMapper.insertSelective(orderExemptReviewStrategyDetail);
    }
    //指定SKU不免审
    private void insertData6 (String val) {
        OrderExemptReviewStrategyDetail orderExemptReviewStrategyDetail6 = new OrderExemptReviewStrategyDetail();
        String pk2 = UUIDUtil.generate();
        orderExemptReviewStrategyDetail6.setOrderExemptReviewStrategyDetailId(pk2);
        orderExemptReviewStrategyDetail6.setSingleProductId(7L);
        orderExemptReviewStrategyDetail6.setSingleProductCode("jdtest2XLCN1");
        orderExemptReviewStrategyDetail6.setType("1");
        orderExemptReviewStrategyDetailMapper.insertSelective(orderExemptReviewStrategyDetail6);

        OrderExemptReviewStrategy orderExemptReviewStrategy6 = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy6.setOrderExemptReviewStrategyId(6L);
        orderExemptReviewStrategy6.setCode("006");
        orderExemptReviewStrategy6.setVal(val);
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy6);
    }
    //卖家有留言则订单不免审
    private void insertData7 (String val) {
        OrderExemptReviewStrategy orderExemptReviewStrategy7 = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy7.setOrderExemptReviewStrategyId(7L);
        orderExemptReviewStrategy7.setCode("007");
        orderExemptReviewStrategy7.setVal(val);
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy7);
    }
    //消费者有留言则订单不免审
    private void insertData8 (String val) {
        OrderExemptReviewStrategy orderExemptReviewStrategy8 = new OrderExemptReviewStrategy();
        orderExemptReviewStrategy8.setOrderExemptReviewStrategyId(8L);
        orderExemptReviewStrategy8.setCode("008");
        orderExemptReviewStrategy8.setVal(val);
        orderExemptReviewStrategyMapper.insertSelective(orderExemptReviewStrategy8);
    }
}
