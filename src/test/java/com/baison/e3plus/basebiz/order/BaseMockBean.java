package com.baison.e3plus.basebiz.order;

import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerAdvancedGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerBarcodeService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerPlatformGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerStockLockLogService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerStockOperateService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.*;
import com.baison.e3plus.common.redis.BS2RedisPool;
//import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.Jedis;

/**
 * @author liang.zeng
 */
public class BaseMockBean {

    /*@MockBean
    protected AmqpTemplate amqpTemplate;*/
    @MockBean
    protected ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @MockBean
    protected Jedis bjedis;
    @MockBean
    protected BS2RedisPool bs2RedisPool;

    //com.baison.e3plus.biz.stock.service.feignclient下的接口MOCK  --- start

    @MockBean
    protected ConsumerAdvancedChannelService consumerAdvancedChannelService;
    @MockBean
    protected ConsumerAdvancedGoodsService consumerAdvancedGoodsService;
    @MockBean
    protected ConsumerAdvancedWareHouseService consumerAdvancedWareHouseService;
    @MockBean
    protected ConsumerCloseBillStrategyService consumerCloseBillStrategyService;
    @MockBean
    protected ConsumerBusinessRelationDetailService consumerBusinessRelationDetailService;
    @MockBean
    protected ConsumerIdService consumerIdService;
    @MockBean
    protected ConsumerCodeRuleService consumerCodeRuleService;
    @MockBean
    protected ConsumerSingleProductService consumerSingleProductService;
    @MockBean
    protected ConsumerBarcodeService consumerBarcodeService;
    @MockBean
    protected ConsumerWhareaTypeService consumerWhareaTypeService;
    @MockBean
    protected ConsumerOwnerService consumerOwnerService;
    @MockBean
    protected ConsumerVirtualWareHouseService consumerVirtualWareHouseService;
    @MockBean
    protected ConsumerCurrencyTypeService consumerCurrencyTypeService;
    @MockBean
    protected ConsumerDeliveryTypeService consumerDeliveryTypeService;
    @MockBean
    protected ConsumerCustomerService consumerCustomerService;
    @MockBean
    protected ConsumerSystemParameterService consumerSystemParameterService;
    @MockBean
    protected ConsumerSystemOperateLogService consumerSystemOperateLogService;
    @MockBean
    protected ConsumerWareHouseGroupVirtualDetailService consumerWareHouseGroupVirtualDetailService;
    @MockBean
    protected ConsumerAdvancedShopService consumerAdvancedShopService;
    @MockBean
    protected ConsumerWareHouseGroupRealDetailService consumerWareHouseGroupRealDetailService;
    @MockBean
	protected ConsumerUnitConverRelationService consumerUnitConverRelationService;
    @MockBean
    protected ConsumerPlatformGoodsService consumerPlatformGoodsService;
    @MockBean
    protected ConsumerShopVirtualHouseService consumerShopVirtualHouseService;
    @MockBean
    protected ConsumerStockOperateService consumerStockOperateService;
    @MockBean
    protected ConsumerStockLockLogService consumerStockLockLogService;

    //com.baison.e3plus.biz.stock.service.feignclient下的接口MOCK  --- end
}
