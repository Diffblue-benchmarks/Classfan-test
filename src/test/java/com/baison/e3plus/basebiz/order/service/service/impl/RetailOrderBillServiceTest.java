package com.baison.e3plus.basebiz.order.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleSingleProduct;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleGoods;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.basebiz.order.BaseOrderTest;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderGoodsDetail;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailOrderBillMapper;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedWareHouse;
import com.baison.e3plus.common.cncore.common.BillStatus;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.util.MockBeanUtil;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class RetailOrderBillServiceTest extends BaseOrderTest {

    @InjectMocks
    @Autowired
    RetailOrderBillService retailOrderBillService;
    @Autowired
    RetailOrderBillMapper retailOrderBillMapper;
    @Mock
    OriginalRetailOrderBillService originalRetailOrderBillService;
    @Mock
    private RetailOrderDistributionInfoService distributionInfoService;
    @Mock
    private RetailOrderGoodsDetailService goodsDetailService;

    @Test
    public void submit() {
    }

    @Test
    public void complete() {
    }

    @Test
    public void term() {
    }

    @Test
    public void retreat() {
    }

    @Test
    public void create() {
    }

    @Test
    public void modify() {
    }

    @Test
    public void updateByPrimaryKeySelective() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void queryPage() {
    }

    @Test
    public void getListCount() {
    }

    @Test
    public void queryObjectBySql() {
    }

    @Test
    public void updateBySql() {
    }

    @Test
    public void queryPageBySetMealRetailOrderGoods() {
    }

    @Test
    public void getSetMealRetailOrderGoodsCount() {
    }

    @Test
    public void savePassedBackBill() {
    }

    @Test
    public void releaseBillLockStock() {
    }

    @Test
    public void updateBatchByBillNo() {
    }

    @Test
    public void selectBySap() {
    }

    @Test
    public void selectBillNoBySap() {
    }

    @Test
    public void updateIsConfirm() {
    }

    @Test
    public void queryRetailOrderBillMapToOms() {
    }

    @Test
    public void getRetailOrderBillToOmsCount() {
    }

    @Test
    public void batchUpdateHasReturnByBillNo() {
    }

    @Test
    public void createRetailOrderBillFromOrignalBill() {
    }

    @Test
    public void ewmsCheckOutAndSendOut() {
        String reqStr = "{\"orders\": [ { \"deliveryOrder\": {\"deliveryOrderCode\":\"FLUX20190905000001\",\"deliveryOrderId\":\"1111111111111111\",\"warehouseCode\":\"TMLG\",\"orderType\":\"JYCK\",\"status\":\"DELIVERED\",\"outBizCode\":\"11111111111111111\",\"confirmType\": \"0\",\"orderConfirmTime\":\"2019-09-05 18:23:34\",\"operatorCode\":\"ZHANGSAN\",\"operatorName\":\"张三\",\"operateTime\":\"2019-09-05 18:33:34\",\"storageFee\":\"23.00\",\"invoices\":{\"invoice\":{\"header\":\"上海百胜软件股份有限公司\",\"amount\":\"12.00\",\"content\":\"洗护用品\",\"detail\":{\"items\":{\"item\":{\"itemName\": \"海飞丝\",\"unit\": \"瓶\", \"price\": \"29.90\",\"quantity\": {\"#text\":\"1/quantity>\",\"amount\":\"29.90\" }}}}}}}}  ,{\"deliveryOrder\": {\"deliveryOrderCode\": \"FLUX20190905000002\",\"deliveryOrderId\": \"1111111111111121\",\"warehouseCode\": \"TMHP\",\"orderType\": \"JYCK\",\"status\": \"CLOSED\",           \"outBizCode\": \"11111111111111122\",           \"confirmType\": \"0\",           \"orderConfirmTime\": \"2019-09-06 18:23:34\",           \"operatorCode\": \"ZHANGSAN\",           \"operatorName\": \"张三\",           \"operateTime\": \"2019-09-05 18:33:34\",           \"storageFee\": \"23.00\",           \"invoices\": {             \"invoice\": {               \"header\": \"上海百胜软件股份有限公司\",               \"amount\": \"12.00\",               \"content\": \"洗护用品\",               \"detail\": {                 \"items\": {                   \"item\": {                     \"itemName\": \"海飞丝\",                     \"unit\": \"瓶\",                     \"price\": \"29.90\",                     \"quantity\": {                       \"#text\": \"1/quantity>\",                       \"amount\": \"29.90\"                     }                   }                 }               }             }           }         }       }     ]   }";
        RetailOrderDistributionInfo retailOrderDistributionInfo = new RetailOrderDistributionInfo();
        retailOrderDistributionInfo.setBillNo("aaa");
        Mockito.when(distributionInfoService.queryPage(Mockito.any(),Mockito.any(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(new RetailOrderDistributionInfo[]{retailOrderDistributionInfo});
        RetailOrderGoodsDetail retailOrderGoodsDetail = new RetailOrderGoodsDetail();
        retailOrderGoodsDetail.setBillNo("aaa");
        retailOrderGoodsDetail.setRetailOrderBillId(123L);
        retailOrderGoodsDetail.setGoodsId(123L);
        SimpleGoods goods = new SimpleGoods();
        goods.setGoodsId(123L);
        retailOrderGoodsDetail.setGoods(goods);
        AdvancedSimpleSingleProduct sg = new AdvancedSimpleSingleProduct();
        sg.setGoodsId(123L);
        sg.setSingleProductId(123L);
        sg.setCode("aaa");
        retailOrderGoodsDetail.setSingleProduct(sg);
        retailOrderGoodsDetail.setQty(1);
        Mockito.when(goodsDetailService.queryGoodsDetailByBillNo(Mockito.any(),Mockito.any())).thenReturn(new RetailOrderGoodsDetail[]{retailOrderGoodsDetail});
        Mockito.when(consumerStockOperateService.decreStockAndLockStock(Mockito.any(),Mockito.any())).thenReturn(new ServiceResult());
        AdvancedWareHouse wareHouse = new AdvancedWareHouse();
        wareHouse.setWareHouseId(123L);
        Mockito.when(consumerAdvancedWareHouseService.queryWareHouse(Mockito.any(),Mockito.any())).thenReturn(new AdvancedWareHouse[]{wareHouse});
        RetailOrderBill retailOrderBill = new RetailOrderBill();
        retailOrderBill.setId(123L);
        retailOrderBill.setBillNo("aaa");
        retailOrderBill.setStatus(Integer.parseInt(BillStatus.INITIAL));
        retailOrderBill.setDistributionState("6");
        retailOrderBill.setIsFromOms("0");
        retailOrderBillMapper.insert(retailOrderBill);
        retailOrderBillService.ewmsCheckOutAndSendOut(testToken, reqStr);
    }

    @Test
    public void pushBillsToewms() {
        RetailOrderBill retailOrderBill = new RetailOrderBill();
        retailOrderBill.setId(123L);
        retailOrderBill.setStatus(Integer.parseInt(BillStatus.INITIAL));
        retailOrderBill.setDistributionState("6");
        retailOrderBill.setIsFromOms("0");
        retailOrderBillMapper.insert(retailOrderBill);
        Mockito.when(originalRetailOrderBillService.updateOrderStatus(Mockito.any(),Mockito.anyLong(),Mockito.any())).thenReturn(1);
        retailOrderBillService.pushBillsToewms(testToken,"");
    }

    @Test
    public void pushBillsToewmsBranch1() {
        Mockito.when(originalRetailOrderBillService.updateOrderStatus(Mockito.any(),Mockito.anyLong(),Mockito.any())).thenReturn(1);
        retailOrderBillService.pushBillsToewms(testToken,"");
    }

    @Test
    public void pushBillsToewmsBranch2() {
        RetailOrderBill retailOrderBill = new RetailOrderBill();
        retailOrderBill.setId(123L);
        retailOrderBill.setStatus(Integer.parseInt(BillStatus.INITIAL));
        retailOrderBill.setDistributionState("6");
        retailOrderBill.setIsFromOms("0");
        retailOrderBillMapper.insert(retailOrderBill);
        Mockito.when(originalRetailOrderBillService.updateOrderStatus(Mockito.any(),Mockito.anyLong(),Mockito.any())).thenReturn(0);
        retailOrderBillService.pushBillsToewms(testToken,"");
    }

    @Test
    public void testEwmsStockout() {
        RetailOrderBill retailOrderBill = new RetailOrderBill();
        retailOrderBill.setId(123L);
        retailOrderBill.setStatus(Integer.parseInt(BillStatus.INITIAL));
        retailOrderBill.setBillNo("billNo");
        retailOrderBillMapper.insert(retailOrderBill);

        Mockito.when(consumerStockLockLogService.queryObject(Mockito.any(), Mockito.any())).thenReturn(null);
        retailOrderBillService.ewmsStockout(testToken, "billNo");
    }

}