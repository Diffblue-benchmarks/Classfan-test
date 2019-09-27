package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.BaseOrderTest;
import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderBill;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAuthObjectService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerCodeRuleService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerDataAuthService;
import com.baison.e3plus.common.cncore.util.MockBeanUtil;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OriginalRetailOrderBillServiceTest extends BaseOrderTest {

    @InjectMocks
    @Autowired
    private OriginalRetailOrderBillService originalRetailOrderBillService;

    @Mock
    private OriginalRetailOrdGoodsDetailService originalRetailOrdGoodsDetailService;

    @Mock
    private OriginalRetailOrderDistributionInfoService  originalRetailOrderDistributionInfoService;

    @Mock
    public ConsumerAuthObjectService authObjectService;

    @Mock
    public ConsumerDataAuthService dataAuthService;

    @Mock
    private ConsumerCodeRuleService codeRuleService;

    @Test
    public void create(){
        String json = "{\"abolishBy\": \"admin\",\"abolishDate\": \"2019-09-12 11:41:13\",\"belowOrderDate\": \"2019-09-12 11:40:51\",\"completeBy\": \"admin\",\"completeDate\": \"2019-09-12 11:41:18\",\"consumerMessage\": \"11111\",\"createDate\": \"2019-09-12 11:40:07\",\"id\": \"3\",\"makingOrderBy\": \"admin\",\"makingOrderDate\": \"2019-09-12 11:40:58\",\"modifyBy\": \"admin\",\"modifyDate\": \"2019-09-12 11:41:21\",\"orderStatus\": 0,\"orderType\": 1,\"originalOrderBillNo\": \"SO201909120000001\",\"payDate\": \"2019-09-12 11:40:41\",\"payStatus\": 1,\"platformId\": 1,\"platfromCode\": \"1\",\"platfromName\": \"宝洁\",\"qty\": 1,\"remark\": \"11111\",\"sellerMessage\": \"1111\",\"shopCode\": \"1\",\"shopId\": 1,\"shopName\": \"宝洁天猫\",\"totalTaxPrice\": 1,\"tradeNo\": \"1234567890102\",\"transferOrderBy\": \"admin\",\"transferOrderDate\": \"2019-09-12 11:41:10\" }";
        OriginalRetailOrderBill originalRetailOrderBill = MockBeanUtil.initBeanFromJSON(json, OriginalRetailOrderBill.class);
        List<OriginalRetailOrderBill> beans = new ArrayList<>();
        beans.add(originalRetailOrderBill);
        Mockito.when(codeRuleService.generateCode(Mockito.anyString(),Mockito.anyString(), Mockito.any())).thenReturn("SO2019091200000002");

        originalRetailOrderBillService.create(testToken, beans);
    }


}