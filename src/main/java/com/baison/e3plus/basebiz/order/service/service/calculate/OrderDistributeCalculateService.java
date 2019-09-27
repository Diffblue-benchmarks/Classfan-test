package com.baison.e3plus.basebiz.order.service.service.calculate;

import com.alibaba.fastjson.JSONObject;
import com.baison.e3plus.basebiz.goods.api.model.barcode.Barcode;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.ODSShopRefusalRecord;
import com.baison.e3plus.basebiz.order.api.model.ODSSkuPrioritySetting;
import com.baison.e3plus.basebiz.order.api.model.OrderDistributeStrategy;
import com.baison.e3plus.basebiz.order.api.model.OrderOdsShopScope;
import com.baison.e3plus.basebiz.order.api.model.OrderOdsShopScopeDetail;
import com.baison.e3plus.basebiz.order.api.model.OrderOdsWareahouse;
import com.baison.e3plus.basebiz.order.api.model.RequestRetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.RequestRetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.model.RequestRetailOrderGoodsDetail;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderGoodsDetail;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderSettleDetail;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriority;
import com.baison.e3plus.basebiz.order.api.model.adapter.OmsRetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.calculate.DeliveryResponse;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderAdapterResponse;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDisCalContext;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDisSKUDetail;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeCalculateException;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeLog;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeLogWithBLOBs;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeParas;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeProcess;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeResponse;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderResponseInfo;
import com.baison.e3plus.basebiz.order.api.model.calculate.SendSKUDetail;
import com.baison.e3plus.basebiz.order.api.model.calculate.WareHouseInfo;
import com.baison.e3plus.basebiz.order.api.model.calculate.WareHouseSkuQtyInfo;
import com.baison.e3plus.basebiz.order.api.service.IODSSkuPrioritySettingService;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderBillService;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyService;
import com.baison.e3plus.basebiz.order.api.service.NodeConstant;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedExpressStrategyService;
import com.baison.e3plus.basebiz.order.api.service.calculate.IOrderDistributeCalculateService;
import com.baison.e3plus.basebiz.order.api.service.odsbase.IODSShopRefusalRecordService;
import com.baison.e3plus.basebiz.order.api.service.odsbase.IOrderDistributeStrategyService;
import com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message.IncreLockStockMQMessageContent;
import com.baison.e3plus.basebiz.order.service.advanced.impl.calculate.message.OrderDistributeMessageConstance;
import com.baison.e3plus.basebiz.order.service.configuration.OrderProperties;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderDistributeLogMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.OrderDistributeLogExample;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerBarcodeService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerStockOperateService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerVirtualWareHouseStockOperateService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdministrationAreaWithFashionService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedShopService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerCustomerService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerShopVirtualHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerVirtualWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerWareHouseGroupRealDetailService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerWareHouseGroupVirtualDetailService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerWhareaTypeService;
import com.baison.e3plus.basebiz.order.service.redis.calculate.AdvancedOrderDistributeRedisManage;
import com.baison.e3plus.basebiz.order.service.redis.calculate.DistributeOrderStatus;
import com.baison.e3plus.biz.stock.api.model.stock.StockAvailableQty;
import com.baison.e3plus.biz.stock.api.model.stock.StockLockParas;
import com.baison.e3plus.biz.stock.api.model.stock.StockOperateParas;
import com.baison.e3plus.biz.stock.api.model.stock.StockOperateResult;
import com.baison.e3plus.biz.stock.api.model.stock.VirtualStockAvailableQty;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.shop.AdvancedShop;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.shop.ShopVirtualHouse;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.virtualwarehouse.VirtualWareHouse;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedWareHouse;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehousegroup.WareHouseGroupRealDetail;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehousegroup.WareHouseGroupVirtualDetail;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.delivery.DeliveryType;
import com.baison.e3plus.biz.support.api.publicrecord.model.administrationarea.AdministrationArea;
import com.baison.e3plus.biz.support.api.publicrecord.model.administrationarea.AdministrationAreaWithFashion;
import com.baison.e3plus.biz.support.api.publicrecord.model.customer.CustomerSku;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouseWithFashion;
import com.baison.e3plus.biz.support.api.publicrecord.model.whareatype.WhareaType;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.linq.IGroup;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.bscore.mq.BapMQMessage;
import com.baison.e3plus.common.bscore.mq.IBapMQProducer;
import com.baison.e3plus.common.bscore.utils.BooleanUtil;
import com.baison.e3plus.common.bscore.utils.CollectionUtil;
import com.baison.e3plus.common.bscore.utils.JSONUtil;
import com.baison.e3plus.common.bscore.utils.ObjectUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.common.CacheFlag;
import com.baison.e3plus.common.cncore.common.E3ObjectUtil;
import com.baison.e3plus.common.cncore.common.Status;
import com.baison.e3plus.common.cncore.common.exception.ServiceResultErrorException;
import com.baison.e3plus.common.cncore.log.BusinessTraceUtils;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3OrderByField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.session.Session;
import com.baison.e3plus.common.core.util.ServiceUtils;
import com.baison.e3plus.common.mq.BapONSMQProducer;
import com.baison.e3plus.common.orm.metadata.Condition;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分单策略计算服务
 *
 * @author cong
 */
@Service
public class OrderDistributeCalculateService implements IOrderDistributeCalculateService {

    private Logger logger = LoggerFactory.getLogger(OrderDistributeCalculateService.class);

    @Autowired
    private ConsumerIdService idService;

    @Autowired
    private ConsumerAdvancedShopService shopService;

    @Autowired
    private ConsumerStockOperateService stockOperateService;

    @Autowired
    private ConsumerVirtualWareHouseStockOperateService virtualStockOperateService;

    @Autowired
    private IOrderDistributeStrategyService odsService;

    @Autowired
    private ConsumerShopVirtualHouseService shopVirtualHouseService;

    @Autowired
    private ConsumerVirtualWareHouseService virtualWareHouseService;

    @Autowired
    private ConsumerWareHouseGroupVirtualDetailService groupVirtualDetailService;

    @Autowired
    private ConsumerWareHouseGroupRealDetailService groupRealDetailService;

    @Autowired
    private ConsumerAdvancedWareHouseService wareHouseService;

    @Autowired
    private IODSShopRefusalRecordService shopRefusalRecordService;

    @Autowired
    private ConsumerSingleProductService skuService;

    @Autowired
    private IWareHousePriorityStrategyService wareHousePriorityService;

    @Autowired
    private ConsumerAdministrationAreaWithFashionService administrationService;

    @Autowired
    private IODSSkuPrioritySettingService settingService;

    @Autowired
    private OrderDistributeLogMapper odsLogMapper;

    @Autowired
    private IAdvancedExpressStrategyService deliveryService;

    @Autowired
    private IRetailOrderBillService retailOrderBillService;

    @Autowired
    private ConsumerWhareaTypeService whareaTypeService;

    @Autowired
    private ConsumerBarcodeService consumerBarcodeService;

    @Autowired
    private ConsumerCustomerService consumerCustomerService;

    /**
     * 正品库位id，缓存下，如果不存在去库位档案查询
     */
    private Long whareaTypeId;

    /**
     * 实仓缓存
     */
    private Map<Long, WareHouse> wareHouseCache = new ConcurrentHashMap<>();

    /**
     * 分配池缓存
     */
    private Map<Long, VirtualWareHouse> virtualWareHouseCache = new ConcurrentHashMap<>();

    /**
     * 行政区域经纬度缓存
     */
    private Map<String, AdministrationAreaWithFashion> administrationAreaCache = new ConcurrentHashMap<>();

    /**
     * sku code-sku 缓存
     */
    private Map<String, SimpleSingleProduct> skuIdMapCache = new ConcurrentHashMap<>();

    /**
     * 店铺-分配池-实仓关系缓存 <shopId,<virtualWareHouseId,wareHouseId>>
     */
    private Map<Long, Map<Long, List<Long>>> shopVirtualRealWareHouseRelationCache = new ConcurrentHashMap<>();

    /**
     * 店铺缓存
     */
    private Map<String, Shop> shopCache = new HashMap<>();

    /**
     * 是否同步扣减数据库库存
     */
    private boolean isSynchronizedLockDbStock = true;

    /**
     * 缓存标记
     */
    private boolean enableCache = true;

    @Autowired
    private OrderProperties orderProperties;

    @Override
    public OrderDistributeResponse distributeOrder(String token, OrderDistributeParas paras) {
        BusinessTraceUtils.setLocalNodeIfNotExit(NodeConstant.NODE_DISTRIBUTE_ORDER, paras.getOrderBillNo());
        String orderBillNo = paras.getOrderBillNo();

        OrderDistributeResponse response = queryResponse(token, orderBillNo);
        // 如果查询不到分单记录，那么就是销售订单撤销掉了，需要重新分单
        if (response != null) {
            return response;
        }

        // redis setnx 方法设置订单的状态为 0-empty, 如果设置失败，说明订单已经被分单处理，或者正在分单中。。。
        if (!AdvancedOrderDistributeRedisManage.markOrderStatusInitail(orderBillNo)) {
            response = new OrderDistributeResponse();
            response.setIsSuccess(false);
            response.setErrorCode(AdvancedOrderErrorCode.ORDER_IS_DISTRIBUTING);
            response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.ORDER_IS_DISTRIBUTING, orderBillNo));
            return response;
        }

        // 调用分单
        try {
            response = innerDistributeOrder(token, paras);

            if (!response.getIsSuccess()) {
                if (AdvancedOrderErrorCode.INCRE_REDIS_FAILED.equals(response.getErrorCode())) {
                    Logger log = LoggerFactory.getLogger("e3.orderDistributeLog");
                    log.info(token + "|" + ObjectUtil.serializeByObjectMapper(paras));
                }
            }
        } catch (OrderDistributeCalculateException oe) {
            response = new OrderDistributeResponse();
            response.setOrderBillNo(orderBillNo);
            response.setIsSuccess(false);
            response.setDesc(oe.getErrorMsg());
            logger.error(oe.getMessage(), (Throwable) oe);
        } catch (RuntimeException e) {
            response = new OrderDistributeResponse();
            response.setOrderBillNo(orderBillNo);
            response.setIsSuccess(false);
            response.setDesc(getPrintStackTrace(e));
            logger.error(e.getMessage(), (Throwable) e);
        } catch (Exception e) {
            // 未知异常
            response = new OrderDistributeResponse();
            response.setIsSuccess(false);
            response.setOrderBillNo(orderBillNo);
            response.setDesc(getPrintStackTrace(e));
            logger.error(e.getMessage(), (Throwable) e);

        } finally {
            logOrderDistribute(response);
            AdvancedOrderDistributeRedisManage.deleteOrderStatus(orderBillNo);
            BusinessTraceUtils.clearLocalNode();
        }

        return response;
    }

    private void logOrderDistribute(OrderDistributeResponse response) {
        final String nodeName = NodeConstant.NODE_DISTRIBUTE_ORDER;
        if (response != null && response.getIsSuccess()) {
            List<OrderResponseInfo> orders = response.getOrderResponseInfo();
            for (OrderResponseInfo order : orders) {
                BusinessTraceUtils.writeLogWithDefaultUpper(response.getOrderBillNo(), nodeName, order.getOrderBillNo(),
                    "外部订单[" + response.getOrderBillNo() + "]生成E3订单[" + order.getOrderBillNo() + "]");
                SendSKUDetail[] details = order.getSkuDetail();
                if (details != null) {
                    for (SendSKUDetail detail : details) {
                        BusinessTraceUtils.writeLog(nodeName, order.getOrderBillNo(),
                            nodeName + "$DETAIL", detail.getBarcode(),
                            "订单[" + order.getOrderBillNo() + "]包含商品[" + detail.getBarcode() + "]是否赠品[" + detail.getIsGift() + "]");
                    }
                }
            }
        } else if (response != null) {
            BusinessTraceUtils.writeLogWithDefaultUpper(response.getOrderBillNo(), nodeName, response.getOrderBillNo(),
                "分单[" + response.getOrderBillNo() + "]失败," + response.getDesc());
        }

    }

    /**
     * 校验缓存版本，如果有更新，删除所有本地缓存
     */
    private void checkCacheVersion() {
        if (enableCache) {
            Long shopLocalVersion = ISupportServiceCache.getLocalVersionCache(CacheFlag.SHOP_CACHE_VERSION);
            Long shopRemoteVersion = ISupportServiceCache.getRemoteVersion(CacheFlag.SHOP_CACHE_VERSION);
            if (!shopRemoteVersion.equals(shopLocalVersion)) {
                shopCache.clear();
                ISupportServiceCache.setLocalVersionCache(shopRemoteVersion, CacheFlag.SHOP_CACHE_VERSION);
            }

            Long wareHouseLocalVersion = ISupportServiceCache.getLocalVersionCache(CacheFlag.WAREHOUSE_CACHE_FLAG);
            Long wareHouseRemoteVersion = ISupportServiceCache.getRemoteVersion(CacheFlag.WAREHOUSE_CACHE_FLAG);
            if (!wareHouseRemoteVersion.equals(wareHouseLocalVersion)) {
                wareHouseCache.clear();
                ISupportServiceCache.setLocalVersionCache(wareHouseRemoteVersion, CacheFlag.WAREHOUSE_CACHE_FLAG);
            }

            Long virWareHouseLocalVersion = ISupportServiceCache
                .getLocalVersionCache(CacheFlag.VIRTUAL_WAREHOUSE_CACHE_VERSION);
            Long virWareHouseRemoteVersion = ISupportServiceCache
                .getRemoteVersion(CacheFlag.VIRTUAL_WAREHOUSE_CACHE_VERSION);
            if (!virWareHouseRemoteVersion.equals(virWareHouseLocalVersion)) {
                virtualWareHouseCache.clear();
                ISupportServiceCache.setLocalVersionCache(virWareHouseRemoteVersion,
                    CacheFlag.VIRTUAL_WAREHOUSE_CACHE_VERSION);
            }

            Long administrationLocalVersion = ISupportServiceCache
                .getLocalVersionCache(CacheFlag.ADMINISTRACTION_CACHE_VERSION);
            Long administrationRemoteVersion = ISupportServiceCache
                .getRemoteVersion(CacheFlag.ADMINISTRACTION_CACHE_VERSION);
            if (!administrationRemoteVersion.equals(administrationLocalVersion)) {
                administrationAreaCache.clear();
                ISupportServiceCache.setLocalVersionCache(administrationRemoteVersion,
                    CacheFlag.ADMINISTRACTION_CACHE_VERSION);
            }

            Long warehouseGroupLocalVersion = ISupportServiceCache
                .getLocalVersionCache(CacheFlag.WAREHOUSEGROUP_CACHE_VERSION);
            Long warehouseGroupRemoteVersion = ISupportServiceCache
                .getRemoteVersion(CacheFlag.WAREHOUSEGROUP_CACHE_VERSION);
            if (!shopRemoteVersion.equals(shopLocalVersion)
                || !warehouseGroupRemoteVersion.equals(warehouseGroupLocalVersion)) {
                shopVirtualRealWareHouseRelationCache.clear();

                ISupportServiceCache.setLocalVersionCache(shopRemoteVersion, CacheFlag.SHOP_CACHE_VERSION);
                ISupportServiceCache.setLocalVersionCache(warehouseGroupRemoteVersion,
                    CacheFlag.WAREHOUSEGROUP_CACHE_VERSION);
            }
        }
    }

    private String getPrintStackTrace(Exception e) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        try {
            sw.close();
        } catch (IOException e1) {
        }
        return sw.toString();
    }

    private OrderDistributeResponse innerDistributeOrder(String token, OrderDistributeParas paras) {

        // 处理请求参数，相同的sku数量合并在一起
        OrderDisSKUDetail[] requestSkuDetail = paras.getSkuDetail();
        OrderDisSKUDetail[] skuDetails = requestSkuDetail;
        List<OrderDisSKUDetail> skuList = new ArrayList<>();
        for (OrderDisSKUDetail detail : skuDetails) {
            OrderDisSKUDetail existSkuList = LinqUtil.first(skuList,
                f -> f.getSku().equals(detail.getSku()) && f.getIsGift().equals(detail.getIsGift()));
            if (existSkuList != null) {
                existSkuList.setQty(existSkuList.getQty() + detail.getQty());
            } else {
                skuList.add(detail);
            }
        }
        paras.setSkuDetail(skuList.toArray(new OrderDisSKUDetail[skuList.size()]));

        checkCacheVersion();

        //SKU转换处理，69码转旧码
        OrderDistributeResponse transResponse = transSku(token, paras);
        if (transResponse != null) {
            return transResponse;
        }

        /**
         * 调用订单分单计算
         */
        OrderDistributeResponse response = new OrderDistributeResponse();

        // 校验参数并初始化店铺信息
        checkParasAndInitalShopInfo(token, paras);

        OrderDisCalContext context = new OrderDisCalContext(token, paras);

        // 过滤订单分单策略
        findFitOrderDistributeStrategy(token, context);

        // 分单计算
        orderDistributeCalculate(token, context, response);

        List<String> onlineWareHouseFirstSkuList = context.getOnlineWareHouseFirstSkuList();
        if (onlineWareHouseFirstSkuList != null && onlineWareHouseFirstSkuList.size() > 0) {
            // 如果结果有拆单，而且某一单中商品全部是非电商仓优先，则需要重新按照非电商仓优先重新适配一遍
            handleIfAllNotOnlineFirst(token, response, onlineWareHouseFirstSkuList, requestSkuDetail, paras);
        }

        if (!response.getIsSuccess()) {
            //记录运维日志
            BusinessTraceUtils.writeLogWithDefaultUpper(response.getOrderBillNo(), NodeConstant.NODE_DISTRIBUTE_ORDER, response.getOrderBillNo(),
                "订单[" + response.getOrderBillNo() + "]分单失败,分单过程：" + ObjectUtil.serializeByObjectMapper(context.getProcesses()));
            logResponse(token, context.getProcesses(), response);
            return response;
        }

        // 如果存在缺货sku，返回给调用方
        handdleOutOfStockSku(response, paras);

        // 将适配结果按照仓库+库位分组，拆成不同的订单，创建新的单号
        handleResponseSkuInfo(response, paras);

        // 如果只有一个赠品sku, 判断是否不允许纯赠品单
        Boolean isPureGift = BooleanUtil.parse(context.getRequestParas().getIsPureGift());
        if (!isPureGift) {
            boolean hasSku = false;
            for (OrderResponseInfo info : response.getOrderResponseInfo()) {
                if (info.getSkuDetail().length > 0) {
                    hasSku = true;
                    break;
                }
            }
            if (!hasSku && response.getCannotGiftSku().size() > 0) {
                response.setIsSuccess(false);
                response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.NOT_ALLOWED_PURE_GIFT));
            }
        }

        if (response.getIsSuccess()) {
            // 根据仓库适配结果锁定库存
            increLockStock(token, paras, response);
        }

        if (response.getIsSuccess()) {

            // 增加redis中仓库的日接单量，日结单量增加即使失败不影响最终分单
            try {
                for (OrderResponseInfo info : response.getOrderResponseInfo()) {
                    AdvancedOrderDistributeRedisManage.increWareHouseDailyReceiptOrderCount(info.getWareHouseCode());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        //记录运维日志
        BusinessTraceUtils.writeLogWithDefaultUpper(response.getOrderBillNo(), NodeConstant.NODE_DISTRIBUTE_ORDER, response.getOrderBillNo(),
            "订单[" + response.getOrderBillNo() + "]分单完成,分单过程：" + ObjectUtil.serializeByObjectMapper(context.getProcesses()));
        // 记录返回结果
        logResponse(token, context.getProcesses(), response);

        return response;
    }

    /**
     * SKU转换处理 SKU需要转换的话，则只允许一个明细
     *
     * @param token
     * @param paras
     * @return
     */
    private OrderDistributeResponse transSku(String token, OrderDistributeParas paras) {
        if (!paras.isTransSku()) {
            return null;
        }
        if (paras.getSkuDetail() == null || paras.getSkuDetail().length > 1) {
            OrderDistributeResponse response = new OrderDistributeResponse();
            response.setIsSuccess(false);
            response.setErrorCode(AdvancedOrderErrorCode.ORDER_IS_MUTI_DETAIL);
            response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.ORDER_IS_MUTI_DETAIL, paras.getOrderBillNo()));
            return response;
        }
        OrderDisSKUDetail skuDetail = paras.getSkuDetail()[0];
        E3Selector barcodeSelector = new E3Selector();
        barcodeSelector.addFilterField(new E3FilterField("barcode", skuDetail.getBarcode()));
        barcodeSelector.addFilterField(new E3FilterField("status", "1"));
        barcodeSelector.addOrderByField(new E3OrderByField("enableDate"));
        barcodeSelector.addSelectFields("singleProductCode");
        barcodeSelector.addSelectFields("singleProductId");
        Barcode[] barcodes = consumerBarcodeService.queryBarcode(token, barcodeSelector);
        if (barcodes == null || barcodes.length == 0) {
            OrderDistributeResponse response = new OrderDistributeResponse();
            response.setIsSuccess(false);
            response.setErrorCode(AdvancedOrderErrorCode.ORDER_TRANS_OLD_FAIL_BARCODE);
            response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.ORDER_TRANS_OLD_FAIL_BARCODE, skuDetail.getBarcode()));
            return response;
        }
        Date nowTime = new Date();
        Map<Long, List<Barcode>> barcodeMap = Arrays.stream(barcodes).collect(Collectors.groupingBy(Barcode::getSingleProductId));
        E3Selector csSelector = new E3Selector();
        csSelector.addFilterField(new E3FilterField("singleProductId", barcodeMap.keySet()));
        csSelector.addFilterField(new E3FilterField("status", "1"));
        csSelector.addFilterField(new E3FilterField("enableDate", Condition.LESSEQUAL, nowTime, E3FilterField.ANDOperator));
        csSelector.addFilterField(new E3FilterField("disableDate", Condition.GREATER, nowTime, E3FilterField.ANDOperator));
        csSelector.addFilterField(new E3FilterField("customerCode", paras.getChannelCode()));
        csSelector.addSelectFields("singleProductId");
        CustomerSku[] customerSkus = consumerCustomerService.queryCustomerSku(token, csSelector);
        if (customerSkus == null || customerSkus.length == 0) {
            OrderDistributeResponse response = new OrderDistributeResponse();
            response.setIsSuccess(false);
            response.setErrorCode(AdvancedOrderErrorCode.ORDER_TRANS_OLD_FAIL_CUSTOMERSKU);
            response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.ORDER_TRANS_OLD_FAIL_CUSTOMERSKU, paras.getChannelCode()));
            return response;
        }
        for (CustomerSku sku : customerSkus) {
            List<Barcode> skuBarcodes = barcodeMap.get(sku.getSingleProductId());
            for (Barcode barcode : skuBarcodes) {
                OrderDisSKUDetail transSkuDetail = new OrderDisSKUDetail();
                transSkuDetail.setBarcode(skuDetail.getBarcode());
                transSkuDetail.setIsGift(skuDetail.getIsGift());
                transSkuDetail.setSku(barcode.getSingleProductCode());
                skuDetail.getTransDetails().add(transSkuDetail);
            }
        }

        return null;
    }

    private void handleIfAllNotOnlineFirst(String token, OrderDistributeResponse response,
        List<String> onlineWareHouseFirstSkuList, OrderDisSKUDetail[] requestSkuDetail,
        OrderDistributeParas paras) {

        SendSKUDetail[] fitSkuDetail = response.getSkuDetail();
        List<SendSKUDetail> fitSkuDetailList = new ArrayList<>(Arrays.asList(fitSkuDetail));
        List<IGroup<Long, SendSKUDetail>> groupSkuDatas = LinqUtil.groupBy(fitSkuDetailList, k -> k.getWareHouseId());
        if (groupSkuDatas.size() > 1) {
            List<SendSKUDetail> notOnlineSkuList = new ArrayList<>();
            for (IGroup<Long, SendSKUDetail> groupData : groupSkuDatas) {
                boolean addFlag = true;
                for (SendSKUDetail detail : groupData) {
                    if (onlineWareHouseFirstSkuList.contains(detail.getSku())) {
                        addFlag = false;
                        break;
                    }
                }

                if (addFlag) {
                    notOnlineSkuList.addAll(groupData);
                }
            }

            if (notOnlineSkuList.size() > 0) {
                fitSkuDetailList.removeAll(notOnlineSkuList);

                OrderDisSKUDetail[] requestSkuDetails = createRequestSkuDetail(requestSkuDetail, notOnlineSkuList);
                OrderDistributeParas paras2 = new OrderDistributeParas();
                paras2.setOrderBillNo(paras.getOrderBillNo());
                paras2.setSupportCashOnDelivery(paras.getSupportCashOnDelivery());
                paras2.setPreSaleOrderBill(paras.getPreSaleOrderBill());
                paras2.setShopCode(paras.getShopCode());
                paras2.setShop(paras.getShop());
                paras2.setProvinceCode(paras.getProvinceCode());
                paras2.setCityCode(paras.getCityCode());
                paras2.setAreaCode(paras.getAreaCode());
                paras2.setIsAllowPart(paras.getIsAllowPart());
                paras2.setIsPureGift(paras.getIsPureGift());
                paras2.setSkuDetail(requestSkuDetails);

                OrderDistributeResponse response2 = new OrderDistributeResponse();
                OrderDisCalContext context2 = new OrderDisCalContext(token, paras2);

                // 将已适配相同sku的仓库排除在外，有可能一个sku被拆到不同的仓库了，那么已经被拆到电商仓的不用再适配
                List<String> notOnlineSku = LinqUtil.select(notOnlineSkuList, s -> s.getSku());
                for (SendSKUDetail detail : fitSkuDetailList) {
                    if (notOnlineSku.contains(detail.getSku())) {
                        List<String> refuseWarehouseSkuList = context2.getRefuseWareHouseSkuMap()
                            .get(detail.getWareHouseCode());
                        if (refuseWarehouseSkuList == null) {
                            refuseWarehouseSkuList = new ArrayList<>();
                            context2.getRefuseWareHouseSkuMap().put(detail.getWareHouseCode(), refuseWarehouseSkuList);
                        }
                        refuseWarehouseSkuList.add(detail.getSku());
                    }
                }

                // 过滤订单分单策略
                findFitOrderDistributeStrategy(token, context2);

                // 分单计算
                orderDistributeCalculate(token, context2, response2);

                fitSkuDetailList.addAll(Arrays.asList(response2.getSkuDetail()));

                response.setSkuDetail(fitSkuDetailList.toArray(new SendSKUDetail[fitSkuDetailList.size()]));
            }
        }
    }

    /**
     * 将适配结果按照仓库+库位分组，拆成不同的订单，创建新的单号
     *
     * @param response
     */
    private void handleResponseSkuInfo(OrderDistributeResponse response, OrderDistributeParas paras) {
        SendSKUDetail[] skuDetails = response.getSkuDetail();

        if (whareaTypeId == null) {
            // 库位档案查询正品库位
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("type", "0"));// 正品库位
            selector.addFilterField(new E3FilterField("status", "1"));// 启用状态
            selector.addSelectFields("whareaTypeId");
            selector.addSelectFields("type");
            WhareaType[] wt = whareaTypeService.queryWhareaType(Session.ADMINTOKEN, selector);
            if (wt != null && wt.length > 0) {
                whareaTypeId = wt[0].getWhareaTypeId();
            }
        }

        for (SendSKUDetail skuDetail : skuDetails) {
            skuDetail.setWhareaTypeId(whareaTypeId);
        }

        boolean isCreateNewBillNo = BooleanUtil.parse(paras.getIsCreateNewBillNo());

        List<IGroup<Long, SendSKUDetail>> groupDatas = LinqUtil.groupBy(Arrays.asList(skuDetails),
            k -> k.getWareHouseId());

        // 需要拆单，返回多个单据编号
        for (int i = 1; i <= groupDatas.size(); i++) {
            IGroup<Long, SendSKUDetail> groupData = groupDatas.get(i - 1);
            Long warehouseId = groupData.getKey();
            OrderResponseInfo info = new OrderResponseInfo();
            info.setWareHouseId(warehouseId.toString());
            info.setWareHouseCode(groupData.get(0).getWareHouseCode());
            info.setWhareaTypeId(whareaTypeId);
            info.setSkuDetail(groupData.toArray(new SendSKUDetail[groupData.size()]));

            if (isCreateNewBillNo) {
                if (response.getCannotGiftSku().size() == 0 && response.getOutOfStockSku().size() == 0
                    && groupDatas.size() == 1) {
                    info.setOrderBillNo(response.getOrderBillNo());
                } else {
                    String newBillNo = createNewBillNo(response.getOrderBillNo(), i);
                    info.setOrderBillNo(newBillNo);

                    if (i == groupDatas.size()) {
                        int nextNumber = i + 1;
                        if (response.getOutOfStockSku().size() > 0) {
                            response.setOutOfStockBillNo(createNewBillNo(response.getOrderBillNo(), nextNumber));
                            nextNumber++;
                        }

                        if (response.getCannotGiftSku().size() > 0) {
                            response.setCannotGiftBillNo(createNewBillNo(response.getOrderBillNo(), nextNumber));
                        }
                    }
                }
            } else {
                info.setOrderBillNo(response.getOrderBillNo());
            }

            response.getOrderResponseInfo().add(info);
        }

        response.setSkuDetail(null);
    }

    private String createNewBillNo(String billNo, int i) {
        StringBuilder newBillNo = new StringBuilder(billNo);
        for (int j = 0; j < (3 - String.valueOf(i).length()); j++) {
            newBillNo.append("0");
        }
        newBillNo.append(i);

        return newBillNo.toString();
    }

    private void handdleOutOfStockSku(OrderDistributeResponse response, OrderDistributeParas paras) {
        Map<String, Integer> fitSkuQtyMap = new HashMap<>();

        OrderDisSKUDetail[] requestSkuDetail = paras.getSkuDetail();
        List<OrderDisSKUDetail> cannotGiftSku = response.getCannotGiftSku();

        for (SendSKUDetail skuDetail : response.getSkuDetail()) {
            Integer qty = fitSkuQtyMap.get(skuDetail.getSku());
            if (qty == null) {
                qty = 0;
            }
            qty += skuDetail.getQty();

            fitSkuQtyMap.put(skuDetail.getSku(), qty);
        }

        boolean isAllGift = true;
        for (OrderDisSKUDetail detail : requestSkuDetail) {

            OrderDisSKUDetail existData = LinqUtil.first(cannotGiftSku,
                f -> f.getSku().equals(detail.getSku()) && f.getIsGift().equals(detail.getIsGift()));

            if (existData != null) {
                continue;
            }

            Integer fitSkuQty = fitSkuQtyMap.get(detail.getSku());
            if (fitSkuQty == null) {
                fitSkuQty = 0;
            }
            if (detail.getQty() > fitSkuQty) {
                OrderDisSKUDetail outOfStockSku = new OrderDisSKUDetail();
                outOfStockSku.setSku(detail.getSku());
                outOfStockSku.setQty(detail.getQty() - fitSkuQty);
                List<OrderDisSKUDetail> outOfStockSkuList = response.getOutOfStockSku();
                outOfStockSkuList.add(outOfStockSku);
                if (!BooleanUtil.parse(detail.getIsGift())) {
                    isAllGift = false;
                }
            }
        }

        // 判断缺货sku里面，如果全部是赠品且不允许纯赠品，则将缺货sku添加到赠品集合
        boolean isPureGift = BooleanUtil.parse(paras.getIsPureGift());
        if (!isPureGift && isAllGift) {
            List<OrderDisSKUDetail> outOfSkuList = response.getOutOfStockSku();
            cannotGiftSku.addAll(outOfSkuList);
            outOfSkuList.clear();
        }
    }

    private OrderDisSKUDetail[] createRequestSkuDetail(OrderDisSKUDetail[] orderDisSKUDetails,
        List<SendSKUDetail> notOnlineSkuList) {

        List<OrderDisSKUDetail> detailList = new ArrayList<>();
        for (SendSKUDetail notOnlineSku : notOnlineSkuList) {
            OrderDisSKUDetail orderDisSKUDetail = findOrderDisSKUDetail(orderDisSKUDetails, notOnlineSku.getSku());
            orderDisSKUDetail.setQty(notOnlineSku.getQty());
            detailList.add(orderDisSKUDetail);
        }

        return detailList.toArray(new OrderDisSKUDetail[detailList.size()]);
    }

    private OrderDisSKUDetail findOrderDisSKUDetail(OrderDisSKUDetail[] orderDisSKUDetails, String sku) {

        for (OrderDisSKUDetail detail : orderDisSKUDetails) {
            if (detail.getSku().equals(sku)) {
                return detail;
            }
        }

        return null;
    }

    /**
     * 安踏的分单规则：优先发货当前店铺组织相同分配池的仓库，如果有勾选电商仓优先发货，则优先从当前组织仓库的电商仓发货
     *
     * @param token
     * @param context
     * @param response
     */
    private void orderDistributeCalculate(String token, OrderDisCalContext context, OrderDistributeResponse response) {

        response.setOrderBillNo(context.getRequestParas().getOrderBillNo());

        List<OrderDistributeStrategy> odstrategys = context.getOdstrategys();
        if (odstrategys.size() == 0) {
            return;
        }

        // 店铺
        Shop shop = context.getRequestParas().getShop();
        Long channelId = shop.getChannelId();

        /**
         * 找到当前店铺关联的分配池，分配池关联的仓库进行适配，如果有组织优先，则先找到和店铺相同组织的分配池关联的仓库进行适配
         */
        List<OrderOdsWareahouse> fitWareHouses = findShopVirtualWareHouses(token, context, shop);

        // 支持预售订单，预售订单只能匹配预售订单的仓库和分配池，非预售订单只能适配非预售订单的分配池和仓库
        int supportPreOrder = context.getRequestParas().getPreSaleOrderBill();
        // 店铺所有的分配池
        Set<VirtualWareHouse> virtualWareHouseSet = new HashSet<>();
        for (OrderOdsWareahouse odsHouse : fitWareHouses) {
            List<VirtualWareHouse> virtualWareHouses = odsHouse.getVirtualWareHouses();
            for (VirtualWareHouse vw : virtualWareHouses) {
                Integer vwSupportPreOrder = vw.getSupportPreOrder();
                if (vwSupportPreOrder == null) {
                    vwSupportPreOrder = 0;// 默认不是预售分配池
                }

                if (supportPreOrder == 1 && vwSupportPreOrder == 0) {
                    continue; // 预售订单非预售分配池
                }
                if (supportPreOrder == 0 && vwSupportPreOrder == 1) {
                    continue;// 非预售订单预售分配池
                }
                virtualWareHouseSet.add(vw);
            }
        }

        Collections.sort(odstrategys, (c1, c2) -> c1.getPriority() - c2.getPriority());
        for (OrderDistributeStrategy strategy : odstrategys) {

            String orderDesc = StringUtil.format("订单【{0}】，策略【{1}】：", context.getRequestParas().getOrderBillNo(),
                strategy.getCode());

            logger.debug(orderDesc + "开始分单...");

            // 组织优先
            Boolean isChannelFirst = BooleanUtil.parse(strategy.getIsChannelFirst());
            if (isChannelFirst) {
                // 找到跟当前店铺同组织的分配池对应的仓库适配
                List<OrderOdsWareahouse> shopChannelWareHouses = LinqUtil.select(fitWareHouses, s -> {
                    List<VirtualWareHouse> virtualWareHouses = s.getVirtualWareHouses();
                    for (VirtualWareHouse vw : virtualWareHouses) {
                        if (vw.getChannelId().equals(channelId)) {
                            return s;
                        }
                    }
                    return null;
                });

                // 同店铺组织分配池
                List<VirtualWareHouse> shopChannelVirtualWareHouses = LinqUtil.where(virtualWareHouseSet,
                    f -> f.getChannelId().equals(channelId));
                context.setVirtualWareHouse(shopChannelVirtualWareHouses);

                String desc = orderDesc + "同组织优先发货，分配池："
                    + LinqUtil.select(shopChannelVirtualWareHouses, f -> f.getCode());
                logger.debug(desc);
                context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

                desc = orderDesc + "同组织优先发货，仓库：" + LinqUtil.select(shopChannelWareHouses, f -> f.getWareHouseCode());
                logger.debug(desc);
                context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

                fitWareHouseAccordingIfOnlineFirst(strategy, shopChannelWareHouses, context, response, channelId);

                boolean finished = checkOrderDistributeFinished(context.getRequestParas().getSkuDetail(),
                    response.getSkuDetail());
                if (!finished) {
                    // 其他分配池仓库发货
                    List<OrderOdsWareahouse> otherChannelWareHouses = LinqUtil.except(fitWareHouses,
                        shopChannelWareHouses);

                    // 其他组织分配池
                    List<VirtualWareHouse> otherShopChannelVirtualWareHouses = LinqUtil.except(virtualWareHouseSet,
                        shopChannelVirtualWareHouses);
                    context.setVirtualWareHouse(otherShopChannelVirtualWareHouses);

                    desc = orderDesc + "其他组织发货，分配池："
                        + LinqUtil.select(otherShopChannelVirtualWareHouses, f -> f.getCode());
                    logger.debug(desc);
                    context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

                    desc = orderDesc + "其他组织发货，仓库："
                        + LinqUtil.select(otherChannelWareHouses, f -> f.getWareHouseCode());
                    logger.debug(desc);
                    context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

                    fitWareHouseAccordingIfOnlineFirst(strategy, otherChannelWareHouses, context, response, channelId);
                }

            } else {
                context.setVirtualWareHouse(new ArrayList<>(virtualWareHouseSet));

                String desc = orderDesc + "无组织优先级，分配池：" + LinqUtil.select(virtualWareHouseSet, f -> f.getCode());
                logger.debug(desc);
                context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

                desc = orderDesc + "无组织优先级，仓库：" + LinqUtil.select(fitWareHouses, f -> f.getWareHouseCode());
                logger.debug(desc);
                context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

                fitWareHouseAccordingIfOnlineFirst(strategy, fitWareHouses, context, response, channelId);
            }

            // 计算过程冗余分单策略代码
            for (OrderDistributeProcess process : context.getProcesses()) {
                process.setStrategyCode(strategy.getCode());
            }

            if (isOrderDistributeSuccess(response.getSkuDetail())) {
                response.setIsSuccess(true);
                response.setStrategyCode(strategy.getCode());
                break;
            } else {
                response.setIsSuccess(false);
                response.setSkuDetail(null);
            }
        }

        SendSKUDetail[] skuDetails = response.getSkuDetail();
        if (!response.getIsSuccess() && (skuDetails == null || skuDetails.length == 0)) {
            response.setErrorCode(AdvancedOrderErrorCode.NO_FIT_STRATEGY);
            response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.NO_FIT_STRATEGY,
                LinqUtil.select(odstrategys, s -> s.getCode()).toString()));
        } else {
            // 仓库类型字段赋值，后面做库存锁定时用到的参数
            Map<String, WareHouse> wareHouses = context.getWareHouses();

            for (SendSKUDetail detail : skuDetails) {
                AdvancedWareHouse advancedWareHouse = (AdvancedWareHouse) wareHouses.get(detail.getWareHouseCode());
                detail.setWareHouseType(advancedWareHouse.getType());
                detail.setProvinceId(String.valueOf(advancedWareHouse.getProvinceId()));
            }
        }

        if (response.getIsSuccess()) {
            // 判断赠品是否可以单独赠送，如果不能单独一单，那么标记该赠品为不可赠送
            boolean isPureGift = BooleanUtil.parse(context.getRequestParas().getIsPureGift());
            List<SendSKUDetail> sendSkuDetailList = new ArrayList<>(Arrays.asList(skuDetails));
            List<SendSKUDetail> removeSkuDetailList = new ArrayList<>();
            if (!isPureGift) {
                List<IGroup<Long, SendSKUDetail>> groupByDetails = LinqUtil.groupBy(sendSkuDetailList,
                    s -> s.getWareHouseId());

                for (IGroup<Long, SendSKUDetail> groupByDetail : groupByDetails) {
                    boolean isAllGift = true;
                    for (SendSKUDetail detail : groupByDetail) {
                        if (!BooleanUtil.parse(detail.getIsGift())) {
                            isAllGift = false;
                            break;
                        }
                    }
                    if (isAllGift) {
                        for (SendSKUDetail detail : groupByDetail) {
                            OrderDisSKUDetail ods = new OrderDisSKUDetail();
                            ods.setSku(detail.getSku());
                            ods.setQty(detail.getQty());
                            ods.setBarcode(detail.getBarcode());
                            ods.setIsGift(detail.getIsGift());
                            response.getCannotGiftSku().add(ods);
                            removeSkuDetailList.add(detail);
                        }
                    }
                }
            }
            if (!removeSkuDetailList.isEmpty()) {
                sendSkuDetailList.removeAll(removeSkuDetailList);
                response.setSkuDetail(sendSkuDetailList.toArray(new SendSKUDetail[sendSkuDetailList.size()]));
                context.getProcesses().add(new OrderDistributeProcess(
                    "不允许纯赠品单，赠品单sku:" + ObjectUtil.serializeByObjectMapper(removeSkuDetailList)));
            }

        }
    }

    /**
     * 找到当前店铺关联的分配池，然后再找到分配池关联的仓库
     *
     * @param token
     * @param context
     * @param shop
     * @return
     */
    private List<OrderOdsWareahouse> findShopVirtualWareHouses(String token, OrderDisCalContext context, Shop shop) {

        Map<Long, List<Long>> virtualRealWareHouseRelation = null;
        if (enableCache) {
            virtualRealWareHouseRelation = shopVirtualRealWareHouseRelationCache.get(shop.getShopId());
        }

        if (virtualRealWareHouseRelation == null) {
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("shopId", shop.getShopId()));
            selector.addSelectFields("virtualHouseId");

            ShopVirtualHouse[] shopVirtualHouses = shopVirtualHouseService.queryObject(token, selector);

            if (shopVirtualHouses == null || shopVirtualHouses.length == 0) {
                throw new OrderDistributeCalculateException("e3.errorCode.stock.orderdistribute.000011",
                    shop.getCode());
            }

            List<Long> virtualWareHouseIds = new ArrayList<>();
            for (ShopVirtualHouse svh : shopVirtualHouses) {
                virtualWareHouseIds.add(svh.getVirtualHouseId());
            }

            // 查询分配池关联的仓库
            selector = new E3Selector();
            selector.addFilterField(new E3FilterField("virtualWareHouseId", virtualWareHouseIds));
            selector.addSelectFields("wareHouseGroupId");
            selector.addSelectFields("virtualWareHouseId");
            // 仓库组分配池明细
            WareHouseGroupVirtualDetail[] virtualDetails = groupVirtualDetailService.queryObject(token, selector);
            if (virtualDetails == null || virtualDetails.length == 0) {
                throw new OrderDistributeCalculateException("e3.errorCode.stock.orderdistribute.000012",
                    virtualWareHouseIds);
            }

            // 一个分配池可能关联多个仓库组
            Map<Long, List<Long>> virtualWareHouseGroupMap = new HashMap<>();
            List<Long> wareHouseGroupIds = new ArrayList<>();
            for (WareHouseGroupVirtualDetail virtualDetail : virtualDetails) {
                List<Long> wareHouseGroupIdList = virtualWareHouseGroupMap.get(virtualDetail.getVirtualWareHouseId());
                if (wareHouseGroupIdList == null) {
                    wareHouseGroupIdList = new ArrayList<>();
                    virtualWareHouseGroupMap.put(virtualDetail.getVirtualWareHouseId(), wareHouseGroupIdList);
                }
                if (!wareHouseGroupIdList.contains(virtualDetail.getWareHouseGroupId())) {
                    wareHouseGroupIdList.add(virtualDetail.getWareHouseGroupId());
                }
                if (!wareHouseGroupIds.contains(virtualDetail.getWareHouseGroupId())) {
                    wareHouseGroupIds.add(virtualDetail.getWareHouseGroupId());
                }
            }
            selector = new E3Selector();
            selector.addFilterField(new E3FilterField("wareHouseGroupId", wareHouseGroupIds));
            selector.addSelectFields("wareHouseGroupId");
            selector.addSelectFields("wareHouseId");
            // 仓库组实仓明细
            WareHouseGroupRealDetail[] realWareHouseDetails = groupRealDetailService.queryObject(token, selector);
            if (realWareHouseDetails == null || realWareHouseDetails.length == 0) {
                throw new OrderDistributeCalculateException("e3.errorCode.stock.orderdistribute.000013",
                    wareHouseGroupIds);
            }

            Map<Long, List<Long>> realWareHouseGroupMap = new HashMap<>();
            for (WareHouseGroupRealDetail detail : realWareHouseDetails) {

                List<Long> wareHouseIds = realWareHouseGroupMap.get(detail.getWareHouseGroupId());
                if (wareHouseIds == null) {
                    wareHouseIds = new ArrayList<>();
                    realWareHouseGroupMap.put(detail.getWareHouseGroupId(), wareHouseIds);
                }

                if (!wareHouseIds.contains(detail.getWareHouseId())) {
                    wareHouseIds.add(detail.getWareHouseId());
                }
            }

            // 分配池-实仓关系 <分配池id,<实仓id>>
            virtualRealWareHouseRelation = new HashMap<>();

            for (Long virtualWareHouseId : virtualWareHouseIds) {
                List<Long> realWareHouseList = virtualRealWareHouseRelation.get(virtualWareHouseId);
                if (realWareHouseList == null) {
                    realWareHouseList = new ArrayList<>();
                    virtualRealWareHouseRelation.put(virtualWareHouseId, realWareHouseList);
                }

                List<Long> wareHouseGroupIdList = virtualWareHouseGroupMap.get(virtualWareHouseId);

                if (wareHouseGroupIdList == null) {
                    // 分配池没有关联仓库组
                    continue;
                }

                for (Long wareHouseGroupId : wareHouseGroupIdList) {
                    List<Long> wareHouseIds = realWareHouseGroupMap.get(wareHouseGroupId);
                    for (Long wareHouseId : wareHouseIds) {
                        if (!realWareHouseList.contains(wareHouseId)) {
                            realWareHouseList.add(wareHouseId);
                        }
                    }
                }
            }

            // 放入缓存
            if (enableCache) {
                shopVirtualRealWareHouseRelationCache.put(shop.getShopId(), virtualRealWareHouseRelation);
            }
        }

        // 分配池
        List<Long> needQueryVirWareHouseIds = new ArrayList<>();
        Map<Long, VirtualWareHouse> virtualWareHouseMap = new HashMap<>();

        for (Long virtualWareHouseId : virtualRealWareHouseRelation.keySet()) {
            if (enableCache && virtualWareHouseCache.containsKey(virtualWareHouseId)) {
                virtualWareHouseMap.put(virtualWareHouseId, virtualWareHouseCache.get(virtualWareHouseId));
            } else {
                needQueryVirWareHouseIds.add(virtualWareHouseId);
            }
        }

        if (needQueryVirWareHouseIds != null && needQueryVirWareHouseIds.size() > 0) {
            // 查询分配池
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("virtualWareHouseId", needQueryVirWareHouseIds));
            selector.addFilterField(new E3FilterField("status", Status.ENABLE));
            selector.addFilterField(new E3FilterField("type", "0")); // 分单适配正品类型的分配池
            selector.addSelectFields("virtualWareHouseId");
            selector.addSelectFields("code");
            selector.addSelectFields("name");
            selector.addSelectFields("channelId");
            selector.addSelectFields("supportPreOrder");
            VirtualWareHouse[] virtualWareHouses = virtualWareHouseService.queryObject(token, selector);
            for (VirtualWareHouse virWareHouse : virtualWareHouses) {
                virtualWareHouseMap.put(virWareHouse.getVirtualWareHouseId(), virWareHouse);
                if (enableCache) {
                    virtualWareHouseCache.put(virWareHouse.getVirtualWareHouseId(), virWareHouse);
                }
            }
        }

        // 实仓
        Map<Long, WareHouse> wareHouseMap = new HashMap<>();
        Set<Long> queryWareHouseIds = new HashSet<>();
        for (Long virtualWareHouseId : virtualRealWareHouseRelation.keySet()) {

            if (!virtualWareHouseMap.containsKey(virtualWareHouseId)) {
                // 数据库查不到分配池，可能被停用了，或者不是正品类型的分配池
                continue;
            }

            List<Long> wareHouseIds = virtualRealWareHouseRelation.get(virtualWareHouseId);
            for (Long wareHouseId : wareHouseIds) {
                if (enableCache && wareHouseCache.containsKey(wareHouseId)) {
                    wareHouseMap.put(wareHouseId, wareHouseCache.get(wareHouseId));
                } else {
                    queryWareHouseIds.add(wareHouseId);
                }
            }
        }

        if (queryWareHouseIds.size() > 0) {
            // 查询实仓
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("wareHouseId", queryWareHouseIds));
            selector.addFilterField(new E3FilterField("status", Status.ENABLE));
            selector.addSelectFields("wareHouseId");
            selector.addSelectFields("code");
            selector.addSelectFields("name");
            selector.addSelectFields("district.code");
            selector.addSelectFields("district.longitude");
            selector.addSelectFields("district.latitude");
            selector.addSelectFields("city.code");
            selector.addSelectFields("city.longitude");
            selector.addSelectFields("city.latitude");
            selector.addSelectFields("province.code");
            selector.addSelectFields("province.longitude");
            selector.addSelectFields("province.latitude");
            selector.addSelectFields("type");
            selector.addSelectFields("provinceId");
            selector.addSelectFields("dailyRecieptOrderQty");
            selector.addSelectFields("noShipment");
            selector.addSelectFields("supportCashOnDelivery");
            selector.addSelectFields("supportPreOrder");
            WareHouse[] wareHouses = wareHouseService.queryObject(token, selector);

            for (WareHouse wareHouse : wareHouses) {
                wareHouseMap.put(wareHouse.getWareHouseId(), wareHouse);
                if (enableCache) {
                    wareHouseCache.put(wareHouse.getWareHouseId(), wareHouse);
                }
            }
        }

        Map<String, WareHouse> contextWareHouses = context.getWareHouses();
        for (WareHouse wareHouse : wareHouseMap.values()) {
            contextWareHouses.put(wareHouse.getCode(), wareHouse);
        }

        List<OrderOdsWareahouse> odsWareHouses = new ArrayList<>();
        for (Long virtualWareHouseId : virtualRealWareHouseRelation.keySet()) {
            VirtualWareHouse virWareHouse = virtualWareHouseMap.get(virtualWareHouseId);
            if (virWareHouse == null) {
                continue;// 有可能分配池被禁用了
            }
            List<Long> wareHouseIds = virtualRealWareHouseRelation.get(virtualWareHouseId);
            for (Long wareHouseId : wareHouseIds) {

                OrderOdsWareahouse odsWareHouse = findExistWareHouse(odsWareHouses, wareHouseId);
                if (odsWareHouse != null) {
                    odsWareHouse.getVirtualWareHouses().add(virWareHouse);
                    continue;
                }

                AdvancedWareHouse wareHouse = (AdvancedWareHouse) wareHouseMap.get(wareHouseId);
                if (wareHouse == null) {
                    // 该仓库被停用了
                    continue;
                }

                odsWareHouse = new OrderOdsWareahouse();
                odsWareHouse.setWareHouseId(wareHouseId);
                odsWareHouse.setWareHouseCode(wareHouse.getCode());
                odsWareHouse.setWareHouseName(wareHouse.getName());
                odsWareHouse.setWareHouseType(wareHouse.getType());
                odsWareHouse.getVirtualWareHouses().add(virWareHouse);
                odsWareHouses.add(odsWareHouse);
            }
        }

        return odsWareHouses;
    }

    private OrderOdsWareahouse findExistWareHouse(List<OrderOdsWareahouse> odsWareHouses, Long wareHouseId) {
        for (OrderOdsWareahouse wareHouse : odsWareHouses) {
            if (wareHouse.getWareHouseId().equals(wareHouseId)) {
                return wareHouse;
            }
        }
        return null;
    }

    /**
     * 适配仓库，判断是否优先发电商仓
     *
     * @param strategy
     * @param fitWareHouses
     * @param context
     * @param response
     * @param channelId
     * @return
     */
    private void fitWareHouseAccordingIfOnlineFirst(OrderDistributeStrategy strategy,
        List<OrderOdsWareahouse> fitWareHouses, OrderDisCalContext context, OrderDistributeResponse response,
        Long channelId) {

        String token = context.getToken();

        OrderDisSKUDetail[] skuDetails = context.getRequestParas().getSkuDetail();

        // 初始化请求单品的id
        initialSkuId(context);

        Map<String, SimpleSingleProduct> skuIdMap = context.getSkuMap();

        List<Integer> skuIds = new ArrayList<>();
        for (SimpleSingleProduct singleProduct : skuIdMap.values()) {
            skuIds.add(singleProduct.getSingleProductId().intValue());
        }

        boolean isOnlineShopFirst = BooleanUtil.parse(strategy.geteCommerceFirst());
        if (isOnlineShopFirst) {
            // 如果订单中的sku存在电商仓优先的，那么整单所有sku全部电商仓优先
            List<Integer> onlineFirstSkuIdList = queryEcommerceFirstSku(token, skuIds);

            if (onlineFirstSkuIdList.size() > 0) {
                List<String> onlineFirstSkuList = new ArrayList<>();
                for (Integer skuId : onlineFirstSkuIdList) {
                    for (Entry<String, SimpleSingleProduct> entry : skuIdMap.entrySet()) {
                        if (entry.getValue().getSingleProductId().intValue() == skuId.intValue()) {
                            onlineFirstSkuList.add(entry.getKey());
                        }
                    }
                }

                context.setOnlineWareHouseFirstSkuList(onlineFirstSkuList);
                isOnlineShopFirst = true;
            } else {
                isOnlineShopFirst = false;
            }
        }

        String orderDesc = StringUtil.format("订单【{0}】，策略【{1}】：", context.getRequestParas().getOrderBillNo(),
            strategy.getCode());

        if (isOnlineShopFirst) {// 电商仓优先

            // 电商仓
            List<OrderOdsWareahouse> onlineShopWareHouses = new ArrayList<>();
            // 门店仓
            List<OrderOdsWareahouse> relShopWareHouses = new ArrayList<>();

            for (OrderOdsWareahouse wareHouse : fitWareHouses) {

                String type = wareHouse.getWareHouseType();
                if ("0".equals(type)) {
                    // 电商仓
                    onlineShopWareHouses.add(wareHouse);
                } else {
                    relShopWareHouses.add(wareHouse);
                }
            }

            String desc = orderDesc + "电商仓优先发货，电商仓：" + LinqUtil.select(onlineShopWareHouses, s -> s.getWareHouseCode());
            logger.debug(desc);
            context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

            // 先匹配电商仓
            initialWareHouseDatas(token, onlineShopWareHouses, strategy, strategy.getCode(), context);

            // 计算仓库组合
            calculateWareHouseCombine(context, response, strategy.getCode());

            if (checkOrderDistributeFinished(context.getRequestParas().getSkuDetail(), response.getSkuDetail())) {
                return;
            }

            desc = orderDesc + "非电商仓发货，非电商仓：" + LinqUtil.select(relShopWareHouses, s -> s.getWareHouseCode());
            logger.debug(desc);
            context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

            initialWareHouseDatas(token, relShopWareHouses, strategy, strategy.getCode(), context);

            calculateWareHouseCombine(context, response, strategy.getCode());

        } else {

            String desc = orderDesc + "无电商仓优先发货，仓库：" + LinqUtil.select(fitWareHouses, s -> s.getWareHouseCode());
            logger.debug(desc);
            context.getProcesses().add(new OrderDistributeProcess(strategy.getCode(), desc));

            // 电商仓，非电商仓一起适配
            initialWareHouseDatas(token, fitWareHouses, strategy, strategy.getCode(), context);

            calculateWareHouseCombine(context, response, strategy.getCode());

        }
    }

    /**
     * 根据订单sku需求数找出发货仓库组合
     *
     * @param context
     * @param response
     */
    private void calculateWareHouseCombine(OrderDisCalContext context, OrderDistributeResponse response,
        String strategyCode) {

        List<WareHouseInfo> wareHouseInfoList = context.getWareHouseInfoList();

        OrderDistributeParas requestParas = context.getRequestParas();

        Boolean isPart = BooleanUtil.parse(requestParas.getIsAllowPart());

        // 找到未适配的sku数量
        OrderDisSKUDetail[] requiredSkuDetail = findNotFitSkuDetails(requestParas, response);

        // 初始化所有仓库的库存，不要循环每次只初始化一个仓库库存，hsf调用并发高了会很浪费时间
        initailWareHouseSkuAvailableQty(context.getToken(), context, wareHouseInfoList, requiredSkuDetail);

        Map<String, List<String>> refuseWareHouseSkuMap = context.getRefuseWareHouseSkuMap();
        wareHouseInfoList.stream().forEach(info -> {

            List<String> refuseSkuList = refuseWareHouseSkuMap.get(info.getWareHouseCode());

            int totalSkuQty = 0;
            for (OrderDisSKUDetail needSkuDetail : requiredSkuDetail) {
                // 库存数
                OrderDisSKUDetail existDetail = LinqUtil.first(info.getWareHouseSKUQtys(),
                    f -> f.getSku().equals(needSkuDetail.getSku()));
                if (existDetail == null || (refuseSkuList != null && refuseSkuList.contains(existDetail.getSku()))) {// 存在拒单sku
                    continue;
                }

                totalSkuQty += existDetail.getQty();
            }
            info.setTotalSkuQty(totalSkuQty);
        });

        String orderDesc = StringUtil.format("订单【{0}】，策略【{1}】：", context.getRequestParas().getOrderBillNo(),
            strategyCode);

        String desc = orderDesc + "排序前仓库优先级，物理距离，sku可用数如下...";
        context.getProcesses().add(new OrderDistributeProcess(desc));

        desc = orderDesc + ObjectUtil.serializeByObjectMapper(wareHouseInfoList);
        context.getProcesses().add(new OrderDistributeProcess(desc));

        // 判断是否能够单仓发货
        boolean ifSingleWareHouseSend = fitSingleWareHouse(context, response, requiredSkuDetail);

        // 拆分
        // 不能单仓发，但允许拆分；且SKU不做转换处理
        if (isPart && !ifSingleWareHouseSend && !requestParas.isTransSku()) {
            Map<String, WareHouse> wareHouses = context.getWareHouses();

            /**
             * 排序：
             * <p>
             * 1.总库存数高的排在最前面，保证最小拆单
             * <p>
             * 2.仓库优先级高的排在前面
             * <p>
             * 3.距离近的排在前面
             */
            wareHouseInfoList.sort((c1, c2) -> {

                if (c1.getTotalSkuQty().compareTo(c2.getTotalSkuQty()) == 0) {

                    if (c1.getWareHousePriority().compareTo(c2.getWareHousePriority()) == 0) {
                        return c1.getDistance().compareTo(c2.getDistance());
                    } else {
                        return c1.getWareHousePriority().compareTo(c2.getWareHousePriority());
                    }

                } else {
                    // 数量多的排在前面
                    return c2.getTotalSkuQty().compareTo(c1.getTotalSkuQty());
                }

            });

            desc = orderDesc + "拆单，仓库排序，" + LinqUtil.select(wareHouseInfoList, s -> s.getWareHouseCode());
            context.getProcesses().add(new OrderDistributeProcess(desc));

            List<SendSKUDetail> matchWareHouses = new ArrayList<>();

            Map<String, SimpleSingleProduct> skuIdMap = context.getSkuMap();

            for (OrderDisSKUDetail detail : requiredSkuDetail) {
                Map<String, Integer> totalFitSkuQtyMap = new HashMap<>();

                String uniqueKey = detail.getSku() + detail.getIsGift();

                List<String> wareHouseAvailableQtyList = new ArrayList<>();

                for (WareHouseInfo info : wareHouseInfoList) {
                    List<String> refuseSkuList = refuseWareHouseSkuMap.get(info.getWareHouseCode());
                    if (refuseSkuList != null && refuseSkuList.contains(detail.getSku())) {
                        // 存在拒单sku
                        continue;
                    }

                    Integer totalFitSkuQty = totalFitSkuQtyMap.get(uniqueKey);
                    if (totalFitSkuQty == null) {
                        totalFitSkuQty = 0;
                    }

                    // 还未指定的订单sku数量
                    int needQty = detail.getQty() - totalFitSkuQty;

                    if (needQty <= 0) {// 当前单品已分完，不需要再进行分配
                        break;
                    }

                    // 找到当前仓库的sku可用数
                    WareHouseSkuQtyInfo wareHouseSkuQtyInfo = findWareHouseSkuQty(info, detail);
                    int availableQty = wareHouseSkuQtyInfo.getQty();

                    wareHouseAvailableQtyList.add(info.getWareHouseCode() + "/" + availableQty);

                    if (availableQty <= 0) {
                        continue;
                    }

                    SendSKUDetail sendDetail = new SendSKUDetail();
                    sendDetail.setWareHouseCode(info.getWareHouseCode());
                    sendDetail.setWareHouseId(wareHouses.get(info.getWareHouseCode()).getWareHouseId());
                    sendDetail.setSku(detail.getSku());
                    sendDetail.setIsGift(detail.getIsGift());
                    sendDetail.setBarcode(detail.getBarcode());
                    SimpleSingleProduct ssp = skuIdMap.get(detail.getSku());
                    sendDetail.setSkuId(ssp.getSingleProductId());
                    sendDetail.setGoodsId(ssp.getGoodsId());

                    int leftQty = needQty - availableQty;
                    matchWareHouses.add(sendDetail);

                    if (leftQty > 0) {
                        sendDetail.setQty(availableQty);
                        totalFitSkuQtyMap.put(uniqueKey, totalFitSkuQty + availableQty);
                    } else {
                        sendDetail.setQty(needQty);
                        totalFitSkuQtyMap.put(uniqueKey, totalFitSkuQty + needQty);
                    }

                }

                if (totalFitSkuQtyMap.get(uniqueKey) == null || detail.getQty() > totalFitSkuQtyMap.get(uniqueKey)) {
                    // 有sku未适配完，分配失败
                    // 分单失败需要把所有仓库的当前sku记录下来，方便查询日志
                    desc = ResourceUtils.get("e3.stock.orderdistribute.000017", detail.getSku(), detail.getQty(),
                        wareHouseAvailableQtyList.toString());
                    context.getProcesses().add(new OrderDistributeProcess(desc));
                }
            }

            SendSKUDetail[] skuDetails = response.getSkuDetail();

            if (matchWareHouses.size() > 0) {
                if (skuDetails == null) {
                    response.setSkuDetail(matchWareHouses.toArray(new SendSKUDetail[matchWareHouses.size()]));
                } else {
                    matchWareHouses.addAll(Arrays.asList(skuDetails));
                    response.setSkuDetail(matchWareHouses.toArray(new SendSKUDetail[matchWareHouses.size()]));
                }

                desc = "多仓发货，适配结果:{0}";
                context.getProcesses().add(new OrderDistributeProcess(
                    orderDesc + StringUtil.format(desc, ObjectUtil.serializeByObjectMapper(matchWareHouses))));
            }
        }
    }

    private OrderDisSKUDetail[] findNotFitSkuDetails(OrderDistributeParas requestParas,
        OrderDistributeResponse response) {
        SendSKUDetail[] sendSkuDetails = response.getSkuDetail();
        OrderDisSKUDetail[] totalNeedSkuDetails = requestParas.getSkuDetail();
        if (sendSkuDetails.length == 0) {
            return totalNeedSkuDetails;
        }
        List<OrderDisSKUDetail> notFitSkuDetails = new ArrayList<>();
        for (OrderDisSKUDetail requestDetail : totalNeedSkuDetails) {
            SendSKUDetail sendDetail = null;
            if (requestParas.isTransSku()) {
                //检查转换后的sku适配数量
                for (OrderDisSKUDetail transDetail : requestDetail.getTransDetails()) {
                    for (SendSKUDetail detail : sendSkuDetails) {
                        if (detail.getSku().equals(transDetail.getSku())
                            && detail.getOriginalSku().equals(requestDetail.getSku())
                            && ObjectUtil.equals(detail.getIsGift(), transDetail.getIsGift())) {
                            if (sendDetail == null) {
                                sendDetail = new SendSKUDetail();
                                sendDetail.setQty(0);
                            }
                            sendDetail.setQty(sendDetail.getQty() + detail.getQty());
                            break;
                        }
                    }
                }
            } else {
                for (SendSKUDetail detail : sendSkuDetails) {
                    if (detail.getSku().equals(requestDetail.getSku())
                        && ObjectUtil.equals(detail.getIsGift(), requestDetail.getIsGift())) {
                        sendDetail = detail;
                        break;
                    }
                }
            }
            if (sendDetail == null) {
                notFitSkuDetails.add(requestDetail);
            } else {
                if (sendDetail.getQty() >= requestDetail.getQty()) {
                    continue;
                } else {
                    if (requestParas.isTransSku()) {
                        logger.error("当前SKU转换理论不应该出现部分匹配，requestParas：" + requestParas);
                    }
                }
                OrderDisSKUDetail fitDetail = new OrderDisSKUDetail();
                fitDetail.setSku(requestDetail.getSku());
                fitDetail.setBarcode(requestDetail.getBarcode());
                fitDetail.setIsGift(requestDetail.getIsGift());
                fitDetail.setQty(requestDetail.getQty() - sendDetail.getQty());
                notFitSkuDetails.add(fitDetail);
            }
        }

        return notFitSkuDetails.toArray(new OrderDisSKUDetail[notFitSkuDetails.size()]);
    }

    /**
     * 一仓发货
     *
     * @param context
     * @return
     */
    private boolean fitSingleWareHouse(OrderDisCalContext context, OrderDistributeResponse response,
        OrderDisSKUDetail[] requiredSkuDetail) {

        // 如果不允许拆分，那么排在最前面的仓库如果不能发货，那么就没有一个仓库满足单仓发货
        List<WareHouseInfo> wareHouseInfoList = context.getWareHouseInfoList();
        Map<String, WareHouse> wareHouses = context.getWareHouses();
        Map<String, SimpleSingleProduct> skuIdMap = context.getSkuMap();
        Map<String, List<String>> refuseWareHouseSkuMap = context.getRefuseWareHouseSkuMap();

        List<SendSKUDetail> matchWareHouses = new ArrayList<>();

        /**
         * 排序：
         * <p>
         * 1.仓库优先级高的排在前面
         * <p>
         * 2.距离近的排在前面
         * <p>
         * 3.总库存数高的排在最前面，保证库存多的先发货
         */
        wareHouseInfoList.sort((c1, c2) -> {

            if (c1.getWareHousePriority().compareTo(c2.getWareHousePriority()) == 0) {

                if (c1.getDistance().compareTo(c2.getDistance()) == 0) {
                    // 数量多的排在前面
                    return c2.getTotalSkuQty().compareTo(c1.getTotalSkuQty());
                } else {
                    return c1.getDistance().compareTo(c2.getDistance());
                }

            } else {
                return c1.getWareHousePriority().compareTo(c2.getWareHousePriority());
            }

        });

        boolean success = false;
        for (WareHouseInfo wareHouseInfo : wareHouseInfoList) {
            if (refuseWareHouseSkuMap.containsKey(wareHouseInfo.getWareHouseCode())) {
                // 有拒单记录的仓库不参与适配
                continue;
            }

            List<String> wareHouseAvailableQtyList = new ArrayList<>();

            boolean skuQtyFit = true;

            for (OrderDisSKUDetail detail : requiredSkuDetail) {
                int needQty = detail.getQty();
                if (context.getRequestParas().isTransSku()) {
                    List<SendSKUDetail> sendDetailList = new ArrayList<>();
                    int totalAvailaleQty = 0;
                    WareHouseSkuQtyInfo wareHouseSkuQtyInfo = findWareHouseSkuQty(wareHouseInfo, detail);
                    for (OrderDisSKUDetail transDetail : detail.getTransDetails()) {
                        int availableQty = wareHouseSkuQtyInfo.getTransSkuQty(transDetail.getSku());
                        if (availableQty > 0) {
                            Integer currentQty = availableQty > needQty ? needQty : availableQty;
                            totalAvailaleQty = totalAvailaleQty + currentQty;
                            SendSKUDetail sendDetail = new SendSKUDetail();
                            sendDetail.setOriginalSku(detail.getSku());
                            sendDetail.setWareHouseCode(wareHouseInfo.getWareHouseCode());
                            sendDetail.setWareHouseId(wareHouses.get(wareHouseInfo.getWareHouseCode()).getWareHouseId());
                            sendDetail.setSku(transDetail.getSku());
                            sendDetail.setIsGift(transDetail.getIsGift());
                            sendDetail.setBarcode(transDetail.getBarcode());
                            SimpleSingleProduct ssp = skuIdMap.get(transDetail.getSku());
                            sendDetail.setSkuId(ssp.getSingleProductId());
                            sendDetail.setGoodsId(ssp.getGoodsId());
                            sendDetail.setQty(currentQty);
                            sendDetailList.add(sendDetail);
                            if (needQty == totalAvailaleQty) {
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    if (needQty > totalAvailaleQty) {
                        wareHouseAvailableQtyList.add(detail.getSku() + "/" + needQty + "/" + totalAvailaleQty);
                        skuQtyFit = false;
                        break;
                    } else {
                        matchWareHouses.addAll(sendDetailList);
                    }
                } else {
                    // 找到当前仓库的sku可用数
                    WareHouseSkuQtyInfo wareHouseSkuQtyInfo = findWareHouseSkuQty(wareHouseInfo, detail);
                    Integer availableQty = wareHouseSkuQtyInfo.getQty();
                    if (availableQty < needQty) {
                        wareHouseAvailableQtyList.add(detail.getSku() + "/" + needQty + "/" + availableQty);
                        skuQtyFit = false;
                        break;
                    }
                    SendSKUDetail sendDetail = new SendSKUDetail();
                    sendDetail.setWareHouseCode(wareHouseInfo.getWareHouseCode());
                    sendDetail.setWareHouseId(wareHouses.get(wareHouseInfo.getWareHouseCode()).getWareHouseId());
                    sendDetail.setSku(detail.getSku());
                    sendDetail.setIsGift(detail.getIsGift());
                    sendDetail.setBarcode(detail.getBarcode());
                    SimpleSingleProduct ssp = skuIdMap.get(detail.getSku());
                    sendDetail.setSkuId(ssp.getSingleProductId());
                    sendDetail.setGoodsId(ssp.getGoodsId());
                    sendDetail.setQty(needQty);
                    matchWareHouses.add(sendDetail);
                }

            }

            if (skuQtyFit) {
                success = true;
                break;
            } else {
                // 分单失败需要把所有仓库的当前sku记录下来，方便查询日志
                String desc = ResourceUtils.get("e3.stock.orderdistribute.000024", wareHouseInfo.getWareHouseCode(),
                    wareHouseAvailableQtyList.toString());
                context.getProcesses().add(new OrderDistributeProcess(desc));

                matchWareHouses.clear();
            }
        }

        if (success) {
            response.setSkuDetail(matchWareHouses.toArray(new SendSKUDetail[matchWareHouses.size()]));
            String desc = "仓库【{0}】单仓发货，适配结果为：{1}";
            context.getProcesses().add(new OrderDistributeProcess(StringUtil.format(desc,
                matchWareHouses.get(0).getWareHouseCode(), ObjectUtil.serializeByObjectMapper(matchWareHouses))));
        }

        return success;
    }

    private void initailWareHouseSkuAvailableQty(String token, OrderDisCalContext context,
        List<WareHouseInfo> wareHouseInfos, OrderDisSKUDetail[] requestDetail) {

        Map<String, WareHouse> wareHouses = context.getWareHouses();

        Map<String, SimpleSingleProduct> skuIdMap = context.getSkuMap();
        List<Long> skuIds = new ArrayList<>();
        for (SimpleSingleProduct singleProduct : skuIdMap.values()) {
            skuIds.add(singleProduct.getSingleProductId());
        }

        List<WareHouse> wareHouseList = new ArrayList<>();
        for (WareHouseInfo info : wareHouseInfos) {
            WareHouse wareHouse = wareHouses.get(info.getWareHouseCode());
            wareHouseList.add(wareHouse);
        }

        // 仓库对应单品的可用数
        // <仓库code,<skuId,availableQty>>
        Map<String, Map<Long, Integer>> allWareHouseSkuAvailableQtyMap = querySkuAvailabelQty(token,
            context.getVirtualWareHouse(), wareHouseList, skuIds);

        for (WareHouseInfo info : wareHouseInfos) {

            String wareHouseCode = info.getWareHouseCode();
            Map<Long, Integer> skuAvailableQtyMap = allWareHouseSkuAvailableQtyMap.get(wareHouseCode);

            for (OrderDisSKUDetail detail : requestDetail) {

                if (context.getRequestParas().isTransSku()) {
                    for (OrderDisSKUDetail transDetail : detail.getTransDetails()) {
                        OrderDisSKUDetail odsSkuDetail = new OrderDisSKUDetail();
                        odsSkuDetail.setSku(transDetail.getSku());

                        Long skuId = skuIdMap.get(transDetail.getSku()).getSingleProductId();

                        Integer qty = skuAvailableQtyMap.get(skuId);
                        odsSkuDetail.setQty(qty == null ? 0 : qty);
                        info.getWareHouseSKUQtys().add(odsSkuDetail);
                    }
                } else {
                    OrderDisSKUDetail odsSkuDetail = new OrderDisSKUDetail();
                    odsSkuDetail.setSku(detail.getSku());

                    Long skuId = skuIdMap.get(detail.getSku()).getSingleProductId();

                    Integer qty = skuAvailableQtyMap.get(skuId);
                    odsSkuDetail.setQty(qty == null ? 0 : qty);
                    info.getWareHouseSKUQtys().add(odsSkuDetail);
                }
            }
        }
    }

    /**
     * 查询sku在实仓库存和在分配池中库存总和，取其中较小的库存作为库用库存
     *
     * @param token
     * @param virtualWareHouses
     * @param wareHouses
     * @param skuIds
     * @return
     */
    private Map<String, Map<Long, Integer>> querySkuAvailabelQty(String token, List<VirtualWareHouse> virtualWareHouses,
        List<WareHouse> wareHouses, List<Long> skuIds) {

        // 分配池库存
        List<Long> virtualWareHouseIds = LinqUtil.select(virtualWareHouses, s -> s.getVirtualWareHouseId());

        List<VirtualStockAvailableQty> virtualWareHouseStocks = virtualStockOperateService.querySkuAvailableAty(token,
            virtualWareHouseIds, skuIds);
        Map<Long, Integer> virtualSkuQtyMap = new HashMap<>();
        for (VirtualStockAvailableQty vsaq : virtualWareHouseStocks) {
            Integer totalQty = virtualSkuQtyMap.get(vsaq.getSingleProductId());
            if (totalQty == null) {
                totalQty = 0;
            }
            totalQty += vsaq.getAvailableQty();
            virtualSkuQtyMap.put(vsaq.getSingleProductId(), totalQty);
        }

        List<AdvancedWareHouse> advancedWareHouses = new ArrayList<>(wareHouses.size());
        for (WareHouse w : wareHouses) {
            advancedWareHouses.add((AdvancedWareHouse) w);
        }

        // 实仓库存
        List<StockAvailableQty> datas = stockOperateService.querySkuAvailableAty(token, advancedWareHouses, skuIds);

        Map<String, Map<Long, Integer>> resultMap = new HashMap<>();
        if (datas != null && datas.size() > 0) {

            for (StockAvailableQty stockQty : datas) {

                Map<Long, Integer> dataMap = resultMap.get(stockQty.getWareHouseCode());
                if (dataMap == null) {
                    dataMap = new HashMap<>();
                    resultMap.put(stockQty.getWareHouseCode(), dataMap);
                }

                Integer availableQty = Math.min(stockQty.getAvailableQty(),
                    virtualSkuQtyMap.get(stockQty.getSingleProductId()));

                dataMap.put(stockQty.getSingleProductId(), availableQty);
            }
        }

        return resultMap;
    }

    private void initialSkuId(OrderDisCalContext context) {

        OrderDistributeParas requestParas = context.getRequestParas();
        Map<String, SimpleSingleProduct> skuIdMap = context.getSkuMap();

        List<String> skus = new ArrayList<>();
        if (requestParas.isTransSku()) {
            for (OrderDisSKUDetail detail : requestParas.getSkuDetail()) {
                for (OrderDisSKUDetail dd : detail.getTransDetails()) {
                    String sku = dd.getSku();
                    if (skuIdMapCache.containsKey(sku) && enableCache) {
                        skuIdMap.put(sku, skuIdMapCache.get(sku));
                    } else {
                        skus.add(sku);
                    }
                }
            }
        } else {
            for (OrderDisSKUDetail detail : requestParas.getSkuDetail()) {

                String sku = detail.getSku();
                if (skuIdMapCache.containsKey(sku) && enableCache) {
                    skuIdMap.put(sku, skuIdMapCache.get(sku));
                } else {
                    skus.add(sku);
                }
            }
        }
        if (!skus.isEmpty()) {
            // 根据sku查询id
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("code", skus));
            selector.addSelectFields("singleProductId");
            selector.addSelectFields("code");
            selector.addSelectFields("goodsId");
            SimpleSingleProduct[] sps = skuService.querySimpleSingleProduct(context.getToken(), selector);

            // 存在sku找不到
            if (sps == null || sps.length != skus.size()) {
                List<String> notFindSku = new ArrayList<>(skus);
                if (sps != null) {
                    for (SimpleSingleProduct sp : sps) {
                        if (skus.contains(sp.getCode())) {
                            notFindSku.remove(sp.getCode());
                        }
                    }
                }
                throw new OrderDistributeCalculateException(AdvancedOrderErrorCode.SKU_NOT_EXIST, notFindSku.toString());
            }

            for (SimpleSingleProduct sp : sps) {
                skuIdMap.put(sp.getCode(), sp);
                if (enableCache) {
                    skuIdMapCache.put(sp.getCode(), sp);
                }
            }
        }
    }

    private WareHouseSkuQtyInfo findWareHouseSkuQty(WareHouseInfo info, OrderDisSKUDetail requestDetail) {
        WareHouseSkuQtyInfo wareHouseSkuQtyInfo = new WareHouseSkuQtyInfo();
        wareHouseSkuQtyInfo.initSku(requestDetail);
        if (CollectionUtil.isEmpty(requestDetail.getTransDetails())) {
            for (OrderDisSKUDetail detail : info.getWareHouseSKUQtys()) {
                if (detail.getSku().equals(requestDetail.getSku())) {
                    wareHouseSkuQtyInfo.setQty(detail.getQty());
                    break;
                }
            }
        } else {
            for (OrderDisSKUDetail transDetail : requestDetail.getTransDetails()) {
                wareHouseSkuQtyInfo.initTransSku(transDetail);
                for (OrderDisSKUDetail detail : info.getWareHouseSKUQtys()) {
                    if (detail.getSku().equals(transDetail.getSku())) {
                        wareHouseSkuQtyInfo.setTransSkuQty(transDetail.getSku(), detail.getQty());
                        break;
                    }
                }
            }
        }
        return wareHouseSkuQtyInfo;
    }

    /**
     * 初始化仓库的相关数据
     *
     * @param token
     * @param context
     */
    protected void initialWareHouseDatas(String token, List<OrderOdsWareahouse> wareHouses,
        OrderDistributeStrategy strategy, String strategyCode, OrderDisCalContext context) {

        context.getWareHouseCodes().clear();
        if (wareHouses == null) {
            throw new OrderDistributeCalculateException("e3.errorCode.order.clothes.0010", strategyCode);
        }

        // 初始化参与分单仓库信息，过滤出可分单的仓库
        initailDistributeWareHouseInfo(token, context, wareHouses, strategyCode);

        OrderDistributeParas requestParas = context.getRequestParas();
        List<String> areaCodes = getAreaCode(requestParas);

        Map<String, AdministrationAreaWithFashion> administrationAreaMap = new HashMap<>();

        List<String> queryCodes = new ArrayList<>();
        for (String areaCode : areaCodes) {
            AdministrationAreaWithFashion administrationArea = null;
            if (enableCache) {
                administrationArea = administrationAreaCache.get(areaCode);
            }
            if (administrationArea == null) {
                queryCodes.add(areaCode);
            } else {
                administrationAreaMap.put(areaCode, administrationArea);
            }
        }

        if (queryCodes.size() > 0) {
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("code", queryCodes));
            selector.addSelectFields("code");
            selector.addSelectFields("longitude");
            selector.addSelectFields("latitude");
            AdministrationArea[] administrationAreas = administrationService.queryPageAdministrationArea(token,
                selector, -1, -1);

            for (AdministrationArea area : administrationAreas) {
                administrationAreaMap.put(area.getCode(), (AdministrationAreaWithFashion) area);
                if (enableCache) {
                    administrationAreaCache.put(area.getCode(), (AdministrationAreaWithFashion) area);
                }
            }

        }

        List<WareHouseInfo> wareHouseInfoList = context.getWareHouseInfoList();
        Map<String, WareHouse> wareHousesMap = context.getWareHouses();

        /**
         * 查询仓库的优先级, 超过日接单量的仓库优先级设置为最低（值最大）
         */
        List<Long> queryWareHouseIds = new ArrayList<>();
        for (WareHouseInfo wareHouseInfo : wareHouseInfoList) {

            WareHouse wareHouse = wareHousesMap.get(wareHouseInfo.getWareHouseCode());
            Integer dailyRecieptOrderQty = ((WareHouseWithFashion) wareHouse).getDailyRecieptOrderQty();
            if (dailyRecieptOrderQty == null) {
                dailyRecieptOrderQty = 100000;
            }
            Integer wareHouseDailyReceiptOrderCount = AdvancedOrderDistributeRedisManage
                .getWareHouseDailyReceiptOrderCount(wareHouseInfo.getWareHouseCode());

            if (wareHouseDailyReceiptOrderCount >= dailyRecieptOrderQty) {
                // 优先级设置为最低
                wareHouseInfo.setWareHousePriority(Integer.MAX_VALUE);
            } else {
                queryWareHouseIds.add(wareHouse.getWareHouseId());
            }
        }

        Long shopId = requestParas.getShop().getShopId();
        Long proviceId = requestParas.getProvinceCode() == null ? null
            : administrationAreaMap.get(requestParas.getProvinceCode()).getAdministrationAreaId();
        Long cityId = requestParas.getCityCode() == null ? null
            : administrationAreaMap.get(requestParas.getCityCode()).getAdministrationAreaId();
        Long areaId = requestParas.getAreaCode() == null ? null
            : administrationAreaMap.get(requestParas.getAreaCode()).getAdministrationAreaId();

        WareHousePriority[] wareHousePrioritys = wareHousePriorityService.queryPriorityByWareHouseAndArea(token, shopId,
            queryWareHouseIds.toArray(new Long[queryWareHouseIds.size()]), areaId, cityId, proviceId);
        Map<Long, Integer> wareHousePriorityMap = new HashMap<>();
        for (WareHousePriority wareHousePriority : wareHousePrioritys) {
            wareHousePriorityMap.put(wareHousePriority.getWarehouseId(), wareHousePriority.getPriority());
        }

        for (WareHouseInfo info : wareHouseInfoList) {
            AdvancedWareHouse wareHouse = (AdvancedWareHouse) wareHousesMap.get(info.getWareHouseCode());

            Integer priority = wareHousePriorityMap.get(wareHouse.getWareHouseId());
            if (priority == null) {
                priority = Integer.MAX_VALUE;
            }
            if (info.getWareHousePriority() == null) {
                info.setWareHousePriority(priority);
            }

            // 如果是电商仓，则把距离设置为0。仓库优先级一样的情况下，电商仓默认排在门店仓前面
            if (!wareHouse.getType().equals("1")) { // 非门店仓
                info.setDistance(0d);
                continue;
            }

            // 计算仓库到收货地址的距离
            Double w_longitude = null;
            Double w_latitude = null;
            AdministrationAreaWithFashion district = (AdministrationAreaWithFashion) wareHouse.getDistrict();
            if (district != null) {
                w_longitude = district.getLongitude();
                w_latitude = district.getLatitude();
            }
            if (w_longitude == null || w_latitude == null) {
                district = (AdministrationAreaWithFashion) wareHouse.getCity();
                if (district != null) {
                    w_longitude = district.getLongitude();
                    w_latitude = district.getLatitude();
                }
            }
            if (w_longitude == null || w_latitude == null) {
                district = (AdministrationAreaWithFashion) wareHouse.getProvince();
                if (district != null) {
                    w_longitude = district.getLongitude();
                    w_latitude = district.getLatitude();
                }
            }

            if (district == null) {
                throw new OrderDistributeCalculateException("e3.errorCode.stock.orderdistribute.000014",
                    wareHouse.getCode());
            }
            if (w_longitude == null || w_latitude == null) {
                throw new OrderDistributeCalculateException("e3.errorCode.stock.orderdistribute.000015",
                    district.getCode());
            }

            Double t_longitude = null;
            Double t_latitude = null;
            AdministrationAreaWithFashion administrationArea = administrationAreaMap.get(requestParas.getAreaCode());
            if (administrationArea != null) {
                t_longitude = administrationArea.getLongitude();
                t_latitude = administrationArea.getLatitude();
            }

            if (t_longitude == null || t_latitude == null) {
                administrationArea = administrationAreaMap.get(requestParas.getCityCode());
                if (administrationArea != null) {
                    t_longitude = administrationArea.getLongitude();
                    t_latitude = administrationArea.getLatitude();
                }
            }

            if (t_longitude == null || t_latitude == null) {
                administrationArea = administrationAreaMap.get(requestParas.getProvinceCode());
                if (administrationArea != null) {
                    t_longitude = administrationArea.getLongitude();
                    t_latitude = administrationArea.getLatitude();
                }
            }

            if (administrationArea.getLongitude() == null || administrationArea.getLatitude() == null) {
                throw new OrderDistributeCalculateException("e3.errorCode.stock.orderdistribute.000015",
                    administrationArea.getCode());
            }
            Double distance = E3ObjectUtil.calculateDistance(w_longitude, w_latitude, t_longitude, t_latitude);
            info.setDistance(distance);
        }

    }

    private void initailDistributeWareHouseInfo(String token, OrderDisCalContext context,
        List<OrderOdsWareahouse> odsWareHouses, String strategyCode) {

        Map<String, WareHouse> wareHousesMap = context.getWareHouses();

        List<String> wareHouseCodes = context.getWareHouseCodes();
        List<WareHouse> wareHouses = new ArrayList<>();
        for (OrderOdsWareahouse odsWareHouse : odsWareHouses) {
            String wareHouseCode = odsWareHouse.getWareHouseCode();
            WareHouse wareHouse = wareHousesMap.get(wareHouseCode);

            wareHouseCodes.add(wareHouseCode);
            wareHouses.add(wareHouse);
        }

        List<String> skus = context.getSkuMap().keySet().stream().collect(Collectors.toList());

        // 过滤不能参与分单的仓库
        List<String> canNotDistributeWareHouse = queryCanNotDistributeWarehouse(token, context, wareHouses, skus,
            strategyCode);
        if (canNotDistributeWareHouse != null && canNotDistributeWareHouse.size() > 0) {
            wareHouseCodes.removeAll(canNotDistributeWareHouse);
        }

        List<WareHouseInfo> wareHouseInfoList = context.getWareHouseInfoList();
        wareHouseInfoList.clear();
        for (String wareHouseCode : wareHouseCodes) {
            WareHouseInfo info = new WareHouseInfo();
            info.setWareHouseCode(wareHouseCode);
            wareHouseInfoList.add(info);
        }
    }

    private List<String> queryCanNotDistributeWarehouse(String token, OrderDisCalContext context,
        List<WareHouse> wareHouses, List<String> skus, String strategyCode) {
        List<String> canNotDisWareHouses = new ArrayList<>();

        OrderDistributeParas requestParas = context.getRequestParas();
        // 支持货到付款
        int supportCashOnDelivery = requestParas.getSupportCashOnDelivery();

        // 支持预售订单
        int supportPreOrder = requestParas.getPreSaleOrderBill();

        List<String> wareHouseCodes = new ArrayList<>();
        for (WareHouse wareHouse : wareHouses) {
            String code = wareHouse.getCode();
            // 禁止发货仓库
            if (wareHouse.getNoShipment() == 1) {
                canNotDisWareHouses.add(code);
                String desc = ResourceUtils.get(AdvancedOrderErrorCode.WAREHOUSE_CANNOT_FIT, wareHouse.getCode());
                context.getProcesses().add(new OrderDistributeProcess(desc));

                continue;
            }

            // 货到付款的订单只能适配支持货到付款的仓库
            if (supportCashOnDelivery == 1 && wareHouse.getSupportCashOnDelivery() == 0) {
                canNotDisWareHouses.add(code);
                String desc = ResourceUtils.get(AdvancedOrderErrorCode.WAREHOUSE_CANNOT_DISTRIBUTE, wareHouse.getCode());
                context.getProcesses().add(new OrderDistributeProcess(desc));

                continue;
            }

            Integer rwSupportPreOrder = wareHouse.getSupportPreOrder();
            if (rwSupportPreOrder == null) {
                rwSupportPreOrder = 0;// 默认不是预售仓库
            }
            // 预售订单只能支持预售订单的仓库发货
            if (supportPreOrder == 1 && rwSupportPreOrder == 0) {
                canNotDisWareHouses.add(code);
                String desc = ResourceUtils.get(AdvancedOrderErrorCode.WAREHOUSE_CANNOT_DISTRIBUTE_PREORDER,
                    wareHouse.getCode());
                context.getProcesses().add(new OrderDistributeProcess(desc));

                continue;
            }

            // 非预售订单只能支持非预售订单的仓库发货
            if (supportPreOrder == 0 && rwSupportPreOrder == 1) {
                canNotDisWareHouses.add(code);
                String desc = ResourceUtils.get(AdvancedOrderErrorCode.WAREHOUSE_CANNOT_DISTRIBUTE_NOTPREORDER,
                    wareHouse.getCode());
                context.getProcesses().add(new OrderDistributeProcess(desc));

                continue;
            }

            wareHouseCodes.add(code);
        }

        // 店仓退回来的sku不再参与后面的适配
        AdvancedShop shop = (AdvancedShop) context.getRequestParas().getShop();
        Integer hour = orderProperties.getShopReject().getDelayHour().get(shop.getBrand().getCode());
        if (hour == null) {
            hour = 24;
        }
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("singleProductCode", skus));
        selector.addFilterField(new E3FilterField("wareHouseCode", wareHouseCodes));
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR_OF_DAY, hour);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        selector.addFilterField(new E3FilterField("createDate", ">=", format.format(c.getTime()), "and"));
        selector.addSelectFields("wareHouseCode");

        ODSShopRefusalRecord[] datas = shopRefusalRecordService.queryObject(token, selector);

        if (datas != null && datas.length > 0) {
            Map<String, List<String>> refuseWareHouseSkuMap = context.getRefuseWareHouseSkuMap();
            for (ODSShopRefusalRecord record : datas) {
                List<String> refuseSkuList = refuseWareHouseSkuMap.get(record.getWareHouseCode());
                if (refuseSkuList == null) {
                    refuseSkuList = new ArrayList<>();
                    refuseWareHouseSkuMap.put(record.getWareHouseCode(), refuseSkuList);
                }
                refuseSkuList.add(record.getSingleProductCode());
            }
        }

        return canNotDisWareHouses;
    }

    /**
     * 校验是否所有sku适配完成
     *
     * @param requiredSkuDetail
     * @param distributeSkuDetail
     * @return
     */
    private boolean checkOrderDistributeFinished(OrderDisSKUDetail[] requiredSkuDetail,
        SendSKUDetail[] distributeSkuDetail) {

        boolean finished = true;

        for (OrderDisSKUDetail skuDetail : requiredSkuDetail) {
            int requiredQty = skuDetail.getQty();
            int sendQty = getDistributedQty(distributeSkuDetail, skuDetail.getSku());

            if (requiredQty > sendQty) {
                finished = false;
                break;
            }
        }

        return finished;
    }

    /**
     * 校验是否分单成功：只要有sku适配到仓库，则成功
     *
     * @param distributeSkuDetails
     * @return
     */
    private boolean isOrderDistributeSuccess(SendSKUDetail[] distributeSkuDetails) {
        if (distributeSkuDetails == null || distributeSkuDetails.length == 0) {
            return false;
        }

        boolean success = false;
        for (SendSKUDetail detail : distributeSkuDetails) {
            if (detail.getQty() > 0) {
                success = true;
                break;
            }
        }
        return success;

    }

    private int getDistributedQty(SendSKUDetail[] distributeSkuDetail, String sku) {
        int distributedQty = 0;
        for (SendSKUDetail detail : distributeSkuDetail) {
            if (detail.getSku().equals(sku)) {
                distributedQty += detail.getQty();
            }
        }

        return distributedQty;
    }

    private List<Integer> queryEcommerceFirstSku(String token, List<Integer> skuIds) {

        List<ODSSkuPrioritySetting> datas = settingService.queryBySingleProductId(token, skuIds);

        List<Integer> skuIdList = new ArrayList<>();
        if (datas != null && datas.size() > 0) {
            for (ODSSkuPrioritySetting data : datas) {
                skuIdList.add(data.getSingleProductId());
            }
        }

        return skuIdList;
    }

    /**
     * 过滤订单分单策略
     *
     * @param token
     * @param context
     */
    private void findFitOrderDistributeStrategy(String token, OrderDisCalContext context) {

        OrderDistributeParas requestParas = context.getRequestParas();

        // 查询分单策略
        OrderDistributeStrategy[] odsDatas = odsService.queryObjectByOrderDisCalContext(token,
            context.getRequestParas().getIsAllowPart());

        if (odsDatas == null || odsDatas.length == 0) {
            throw new OrderDistributeCalculateException("e3.errorCode.order.clothes.0008");
        }

        List<OrderDistributeStrategy> fitStrategys = context.getOdstrategys();
        for (OrderDistributeStrategy strategy : odsDatas) {

            // 店铺范围过滤
            List<OrderOdsShopScope> shopScopes = strategy.getShopScope();
            if (shopScopes == null || shopScopes.size() == 0) {
                continue;
            }
            for (OrderOdsShopScope scope : shopScopes) {
                if (strategyShopFit(scope, requestParas.getShop())) {
                    fitStrategys.add(strategy);
                    break;
                }
            }
        }

        if (fitStrategys.size() == 0) {
            throw new OrderDistributeCalculateException("e3.errorCode.order.clothes.0008");
        }

    }

    private boolean strategyShopFit(OrderOdsShopScope scope, Shop shop) {

        Byte scopeType = scope.getScopeType();

        switch (scopeType) {
            case 0: // 全部
                return true;
            case 2: // 具体店铺
                List<OrderOdsShopScopeDetail> shopDetails = scope.getShopDetails();
                if (shopDetails == null || shopDetails.size() == 0) {
                    return false;
                }

                List<String> shopCodes = LinqUtil.select(shopDetails, s -> s.getShopCode());
                return shopCodes.contains(shop.getCode());
            case 1: // 指定店铺范围
                String scopeCondition = scope.getScopeCondition();
                JSONObject conditionObject = JSONUtil.createJsonObject(scopeCondition);

                Field.setAccessible(Shop.class.getFields(), true);
                for (String fieldName : conditionObject.keySet()) {
                    Object value = JSONUtil.getValue(conditionObject, fieldName, null);
                    List<String> values = new ArrayList<>();
                    if (value instanceof List) {
                        for (Object val : (List<?>) value) {
                            values.add(val.toString());
                        }

                    } else if (value.getClass().isArray()) {
                        for (Object val : (Object[]) value) {
                            values.add(val.toString());
                        }
                    } else {
                        values.add(value.toString());
                    }

                    Method method = ObjectUtil.getMethod(Shop.class, "get" + StringUtil.toFirstCharUpper(fieldName));

                    try {
                        Object valueData = method.invoke(shop);

                        if (!values.contains(valueData.toString())) {
                            return false;
                        }
                    } catch (Exception e) {
                        return false;
                    }

                }

                break;
            default:
                return false;
        }

        return true;
    }

    private List<String> getAreaCode(OrderDistributeParas requestParas) {
        List<String> areaCodes = new ArrayList<>();

        if (requestParas.getAreaCode() != null) {
            areaCodes.add(requestParas.getAreaCode());
        }

        if (requestParas.getCityCode() != null) {
            areaCodes.add(requestParas.getCityCode());
        }
        if (requestParas.getProvinceCode() != null) {
            areaCodes.add(requestParas.getProvinceCode());
        }
        if (requestParas.getContryCode() != null) {
            areaCodes.add(requestParas.getContryCode());
        }

        /**
         * 如果订单没有收获地址（香港过来的订单），那么地址默认取中国
         */
        if (areaCodes.size() == 0) {
            areaCodes.add("1");
        }

        return areaCodes;
    }

    /**
     * 校验参数并初始化店铺信息
     *
     * @param token
     * @param paras
     */
    private void checkParasAndInitalShopInfo(String token, OrderDistributeParas paras) {

        if (paras.getOrderBillNo() == null) {
            throw new OrderDistributeCalculateException("e3.errorCode.order.clothes.0003");
        }

        if (paras.getShopCode() == null) {
            throw new OrderDistributeCalculateException("e3.errorCode.order.clothes.0004");
        }

        // 香港店铺订单没有区域信息
        // if (paras.getProvinceCode() == null) {
        // throw new
        // OrderDistributeCalculateException("e3.errorCode.order.clothes.0005");
        // }

        if (paras.getSkuDetail() == null) {
            throw new OrderDistributeCalculateException("e3.errorCode.order.clothes.0007");
        }

        if (paras.getIsAllowPart() == null) {
            paras.setIsAllowPart("1");
        }

        if (enableCache && shopCache.containsKey(paras.getShopCode())) {
            paras.setShop(shopCache.get(paras.getShopCode()));
        } else {
            // 查询店铺信息
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("code", paras.getShopCode()));

            selector.addSelectFields("shopId");
            selector.addSelectFields("code");
            selector.addSelectFields("name");
            selector.addSelectFields("shopProperty");
            selector.addSelectFields("areaId");
            selector.addSelectFields("channelId");
            selector.addSelectFields("platformId");
            selector.addSelectFields("associatedChannelId");
            selector.addSelectFields("brandId");
            selector.addSelectFields("brand.code");
            Shop[] shops = shopService.queryPageShop(token, selector, -1, -1);
            if (shops == null || shops.length == 0) {
                throw new OrderDistributeCalculateException("e3.errorCode.order.clothes.0009", paras.getShopCode());
            }
            paras.setShop(shops[0]);

            if (enableCache) {
                shopCache.put(shops[0].getCode(), shops[0]);
            }
        }
    }

    private void logResponse(String token, List<OrderDistributeProcess> processes, OrderDistributeResponse response) {
        Session session = LoginUtil.checkUserLogin(token);

        OrderDistributeLogWithBLOBs log = new OrderDistributeLogWithBLOBs();
        log.setId(idService.nextId());
        log.setOrderbillno(response.getOrderBillNo());
        log.setIssuccess(response.getIsSuccess() ? "1" : "0");
        log.setResponse(ObjectUtil.serializeByObjectMapper(response));
        if (orderProperties.getLog().getEnable()) {
            // process内容比较多，在开启写库日志时，才保存到数据库
            log.setProcesses(ObjectUtil.serializeByObjectMapper(processes));
        }

        log.setCreateby(session.userCode);
        log.setCreatedate(new Date());
        Byte status = 0;
        log.setStatus(status);

        logger.info("OrderDistributeLogWithBLOBs:" + log);
//		odsLogMapper.insert(log);	TODO 序列化失败
    }

    @Override
    public void increLockStock(String token, OrderDistributeParas paras, OrderDistributeResponse response) {

        String orderBillNo = paras.getOrderBillNo();

        List<StockOperateParas> stockParas = createParas(token, paras, response);

        if (stockParas.size() == 0) {
            response.setIsSuccess(false);
            response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.LOCK_DB_FAILED_NO_SKU));
            return;
        }

        response.setStockParas(stockParas);

        // 先扣减redis中可用数
        StockOperateResult result = stockOperateService.decreRedisStock(token, stockParas);

        if (!result.getIsSuccess()) {
            response.setIsSuccess(false);
            response.setErrorCode(AdvancedOrderErrorCode.INCRE_REDIS_FAILED);

            // 扣减redis库存失败
            String desc = ResourceUtils.get(AdvancedOrderErrorCode.INCRE_REDIS_FAILED, result.getErrorMsg());
            response.setDesc(desc);
            return;
        }

        // 标记redis中订单的状态为 1-redis
        AdvancedOrderDistributeRedisManage.setOrderStatus(orderBillNo, DistributeOrderStatus.REIDS);

        if (isSynchronizedLockDbStock) {
            // 同步扣减db库存
            synchronizedLockDbStock(token, stockParas, paras, response);
        } else {
            // 异步
            reIncreDbLockStock(token, stockParas);
        }

        // 如果扣减数据库存失败，需要回退redis中的数量
        if (!response.getIsSuccess()) {
            stockOperateService.backRedisSkuQty(stockParas);
        }

    }

    private void synchronizedLockDbStock(String token, List<StockOperateParas> stockParas, OrderDistributeParas paras,
        OrderDistributeResponse response) {

        // 再添加数据库锁定数
        StockOperateResult result = null;
        try {
            result = stockOperateService.incDbLockStock(token, stockParas);

            if (!result.getIsSuccess()) {
                response.setIsSuccess(false);
                response.setErrorCode(AdvancedOrderErrorCode.LOCK_DB_FAILED);

                List<SendSKUDetail> sendSkuDetails = Arrays.asList(response.getSkuDetail());
                // 数据库sku可用库存不足
                List<String> skuCode = new ArrayList<>();
                for (StockOperateParas p : stockParas) {
                    SendSKUDetail sp = LinqUtil.first(sendSkuDetails, f -> f.getSkuId().equals(p.getSingleProductId()));
                    if (sp != null) {
                        skuCode.add(sp.getBarcode() == null ? sp.getSku() : sp.getBarcode());
                    }
                }

                response.setDesc(
                    ResourceUtils.get(AdvancedOrderErrorCode.LOCK_DB_FAILED_SKU_QTY_NOT_ENOUGH, skuCode.toString()));
                return;
            }
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            response.setIsSuccess(false);
            response.setErrorCode(AdvancedOrderErrorCode.LOCK_DB_FAILED);
            String desc = StringUtil.format(ResourceUtils.get(AdvancedOrderErrorCode.LOCK_DB_FAILED), e.getMessage());
            response.setDesc(desc);
            return;
        }

        String orderBillNo = paras.getOrderBillNo();
        List<StockLockParas> stockDetails = result.getStocks();

        response.setStockDetails(stockDetails);
        response.setVirtualStockLockIds(result.getVirtualStockLockIds());

        /**
         * 返回具体锁定的分配池
         */
        Map<String, List<String>> billNoWithVirtualWareHouseCodes = result.getBillNoWithVirtualWareHouseCodes();
        for (OrderResponseInfo info : response.getOrderResponseInfo()) {
            if (billNoWithVirtualWareHouseCodes == null) {
                continue;
            }
            List<String> wareHouseCodes = billNoWithVirtualWareHouseCodes.get(info.getOrderBillNo());
            info.setVirtualWareHouseCodes(wareHouseCodes);
        }

        // 判断订单是否取消，如果取消，则需要回退数据库锁定数量
        try {
            // 如果订单取消
            String orderStatus = AdvancedOrderDistributeRedisManage.getOrderStatus(orderBillNo);
            if (orderStatus.endsWith(DistributeOrderStatus.CANCEL)) {
                response.setErrorCode(AdvancedOrderErrorCode.ORDER_CANCEL);
                response.setIsSuccess(false);
                response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.ORDER_CANCEL));

                // 回退数据库锁定数，要保证一定回退成功
                decreDbLockStock(token, stockDetails, result.getVirtualStockLockIds());
                return;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            // 未知异常，回退数据库锁定数，要保证一定回退成功
            response.setErrorCode(AdvancedOrderErrorCode.UNKNOWN);
            response.setIsSuccess(false);
            response.setDesc(ResourceUtils.get(AdvancedOrderErrorCode.UNKNOWN));
            decreDbLockStock(token, stockDetails, result.getVirtualStockLockIds());

            return;
        }

        // 标记redis中订单的状态为 2-db
        AdvancedOrderDistributeRedisManage.setOrderStatus(orderBillNo, DistributeOrderStatus.DB);
    }

    /**
     * 回退数据库锁定数，要保证一定回退成功
     *
     * @param token
     * @param stockParas
     */
    private void decreDbLockStock(String token, List<StockLockParas> stockParas, List<String> virtualStockLockIds) {
        try {
            stockOperateService.backDbLockStock(token, stockParas, virtualStockLockIds);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void reIncreDbLockStock(String token, List<StockOperateParas> stockParas) {
        // 发送消息让MQ消费者异步锁定数据库库存
        IBapMQProducer stockOperateProducer = ServiceUtils
            .getService(OrderDistributeMessageConstance.LOCK_DB_STOCK_PRODUCER, BapONSMQProducer.class);

        IncreLockStockMQMessageContent message = new IncreLockStockMQMessageContent(token, stockParas);

        BapMQMessage massage = new BapMQMessage(OrderDistributeMessageConstance.LOCK_DB_STOCK, message);
        stockOperateProducer.send(massage);
    }

    private List<StockOperateParas> createParas(String token, OrderDistributeParas paras,
        OrderDistributeResponse response) {

        List<StockOperateParas> stockParasList = new ArrayList<>();

        for (OrderResponseInfo info : response.getOrderResponseInfo()) {
            for (SendSKUDetail skuDetail : info.getSkuDetail()) {
                stockParasList.add(createStockOperateParas(paras, info.getOrderBillNo(), skuDetail));
            }
        }

        return stockParasList;
    }

    private StockOperateParas createStockOperateParas(OrderDistributeParas odpParas, String billNo,
        SendSKUDetail skuDetail) {
        StockOperateParas paras = new StockOperateParas();

        paras.setBillCode(billNo);
        paras.setShopCode(odpParas.getShopCode());
        paras.setIsSharing2Exclusion(odpParas.getIsSharing2Exclusion());
        paras.setBillType(BillType.RETAILORDERBILL);
        paras.setWareHouseId(skuDetail.getWareHouseId());

        paras.setWareHouseType(skuDetail.getWareHouseType());
        if ("1".equals(paras.getWareHouseType())) { // 门店仓
            paras.setAreaId(skuDetail.getProvinceId().toString());
        } else {
            paras.setAreaId(skuDetail.getWareHouseCode());
        }

        paras.setSingleProductId(skuDetail.getSkuId());
        paras.setWhareaTypeId(skuDetail.getWhareaTypeId());
        paras.setQty(skuDetail.getQty());

        return paras;
    }

    @Override
    public void cancelOrderDistribute(String token, String orderNo) {
        AdvancedOrderDistributeRedisManage.setOrderStatus(orderNo, DistributeOrderStatus.CANCEL);
    }

    @Override
    public OrderDistributeResponse queryResponse(String token, String orderNo) {
        OrderDistributeLogExample condition = new OrderDistributeLogExample();// status
        // 0-正常
        condition.createCriteria().andOrderbillnoEqualTo(orderNo).andIssuccessEqualTo("1").andStatusEqualTo((byte) 0);
        List<OrderDistributeLogWithBLOBs> datas = odsLogMapper.selectByExampleWithBLOBs(condition);

        if (datas != null && datas.size() > 0) {
            return ObjectUtil.deSrializeByObjectMapper(datas.get(0).getResponse(), OrderDistributeResponse.class);
        }
        return null;
    }

    @Override
    public void disableOrderDistributeLog(String token, String orderNo) {
        OrderDistributeLogExample condition = new OrderDistributeLogExample();// status
        // 0-正常
        condition.createCriteria().andOrderbillnoEqualTo(orderNo).andStatusEqualTo((byte) 0);
        List<OrderDistributeLog> datas = odsLogMapper.selectByExample(condition);

        if (datas != null && datas.size() > 0) {
            for (OrderDistributeLog log : datas) {
                log.setStatus((byte) 1);// 1-作废
                odsLogMapper.updateByPrimaryKeySelective(log);
            }
        }
    }

    @Override
    @Transactional
    public OrderAdapterResponse adapterOrder(String token, RequestRetailOrderBill order) {

        OrderAdapterResponse response = new OrderAdapterResponse();

        long t1 = System.currentTimeMillis();
        long t2 = System.currentTimeMillis();
        logger.info("订单【" + order.getBillNo() + "】，促销适配时间：" + (t2 - t1));

        // 如果指定了仓库和库位则不需要订单适配仓库
        OrderDistributeResponse distributeResponse = null;
        if (order.getWareHouseId() == null && order.getWhareatypeId() == null) {
            OrderDistributeParas paras = createOrderDistributeParas(order);
            distributeResponse = distributeOrder(token, paras);
            response.setOrderDistributeResponse(distributeResponse);
            if (!distributeResponse.getIsSuccess()) {
                response.setIsSuccess(false);
                response.setDesc(distributeResponse.getDesc());
                return response;
            }
        }

        long t3 = System.currentTimeMillis();
        logger.info("订单【" + order.getBillNo() + "】，分单适配时间：" + (t3 - t2));

        try {
            // 拆单的情况下，每单都需要适配快递接口
            List<OmsRetailOrderBill> deliveryParas = createDeliveryParas(order, distributeResponse);
            List<RetailOrderBill> saveBills = new ArrayList<>();
            for (OmsRetailOrderBill deliveryPara : deliveryParas) {
                ServiceResult deliveryResult = deliveryService.filterExpress(token, deliveryPara);

                if (deliveryResult.hasError()) {
                    throw new ServiceResultErrorException(deliveryResult);
                }
                OrderResponseInfo distributeOrderInfo = null;
                if (distributeResponse != null) {
                    distributeOrderInfo = LinqUtil.first(distributeResponse.getOrderResponseInfo(),
                        f -> f.getOrderBillNo().equals(deliveryPara.getBillNo()));
                }
                DeliveryType deliveryType = (DeliveryType) deliveryResult.getSuccessObject().get(0);

                RetailOrderBill saveBill = createRetailOrderBill(order, distributeOrderInfo, deliveryType);
                saveBills.add(saveBill);

                DeliveryResponse deliveryResponse = new DeliveryResponse(deliveryPara.getBillNo(),
                    deliveryType.getCode(), deliveryType.getName(), deliveryType.getStatus());

                response.getDeliveryResponses().add(deliveryResponse);
            }

            long t4 = System.currentTimeMillis();
            logger.info("订单【" + order.getBillNo() + "】，快递适配时间：" + (t4 - t3));

            ServiceResult result = null;
            List<Object> successBills = null;

            if (saveBills.size() == 1) {
                // 从数据库查询该订单是否存在
                RetailOrderBill[] dbBill = retailOrderBillService.findById(token,
                    new Object[] {saveBills.get(0).getBillNo()}, new String[] {"id"});
                if (dbBill != null && dbBill.length > 0) {
                    saveBills.get(0).setId(dbBill[0].getId());
                }
            }

            if (distributeResponse == null) {// 如果没有调用分单，需要调用订单提交接口锁定库存

                if (saveBills.get(0).getId() == null) {
                    result = retailOrderBillService.create(token,
                        saveBills.toArray(new RetailOrderBill[saveBills.size()]));
                } else {
                    result = retailOrderBillService.modify(token,
                        saveBills.toArray(new RetailOrderBill[saveBills.size()]));
                }

                result = retailOrderBillService.submit(token, saveBills.get(0).getBillNo());
                successBills = result.getSuccessObject();
            } else {
                if (saveBills.size() == 1 && saveBills.get(0).getId() != null) {
                    result = retailOrderBillService.modify(token,
                        saveBills.toArray(new RetailOrderBill[saveBills.size()]));

                } else {
                    result = retailOrderBillService.create(token,
                        saveBills.toArray(new RetailOrderBill[saveBills.size()]));
                }
                successBills = result.getSuccessObject();
            }

            if (result.hasError()) {
                throw new ServiceResultErrorException(result);
            }

            for (Object bill : successBills) {
                if (bill instanceof RetailOrderBill) {
                    Map<String, Object> billIdMap = new HashMap<>();
                    RetailOrderBill b = (RetailOrderBill) bill;
                    billIdMap.put("billNo", b.getBillNo());
                    billIdMap.put("billId", b.getId().toString());
                    response.getBillIds().add(billIdMap);
                }
            }

        } catch (Exception e) {
            if (distributeResponse != null) {
                // 出异常需要撤回分单锁定的库存
                stockOperateService.backRedisSkuQty(distributeResponse.getStockParas());
                decreDbLockStock(token, distributeResponse.getStockDetails(),
                    distributeResponse.getVirtualStockLockIds());
            }

            logger.error(e.getMessage(), e);
            if (e instanceof ServiceResultErrorException) {
                throw (ServiceResultErrorException) e;
            } else {
                throw new RuntimeException(e);
            }
        }

        response.setIsSuccess(true);
        return response;
    }

    private RetailOrderBill createRetailOrderBill(RequestRetailOrderBill order, OrderResponseInfo distributeOrderInfo,
        DeliveryType deliveryType) {
        RetailOrderBill retailOrderBill = new RetailOrderBill();
        retailOrderBill.setPayState(order.getPayState());
        retailOrderBill.setCustomerId(order.getCustomerId());
        retailOrderBill.setChannelId(order.getChannelId());
        retailOrderBill.setRemarks(order.getRemarks());
        retailOrderBill.setCurrencyTypeId(order.getCurrencyTypeId());
        retailOrderBill.setStatus(order.getStatus());
        retailOrderBill.setAmountPaid(order.getAmountPaid());
        retailOrderBill.setShopId(order.getShopId());
        retailOrderBill.setVipCardNo(order.getVipCardNo());
        retailOrderBill.setDistributionState(order.getDistributionState());
        retailOrderBill.setSourceBillNo(order.getSourceBillNo());
        retailOrderBill.setBusinessDate(order.getBusinessDate());
        retailOrderBill.setExpressFee(order.getExpressFee());
        retailOrderBill.setDeliveryType(deliveryType.getType());
        retailOrderBill.setIsFromOms(order.getIsFromOms());
        retailOrderBill.setSupportCashOnDelivery(order.getSupportCashOnDelivery());
        retailOrderBill.setPreSaleOrderBill(order.getPreSaleOrderBill());

        if (distributeOrderInfo != null) {
            retailOrderBill.setQty(distributeOrderInfo.getSkuDetail().length + 0d);
            retailOrderBill.setWarehouseId(Long.parseLong(distributeOrderInfo.getWareHouseId()));
            retailOrderBill.setBillNo(distributeOrderInfo.getOrderBillNo());
            retailOrderBill.setWhareatypeId(distributeOrderInfo.getWhareaTypeId());
        } else {
            retailOrderBill.setQty(order.getQty());
            retailOrderBill.setWarehouseId(order.getWareHouseId());
            retailOrderBill.setBillNo(order.getBillNo());
            retailOrderBill.setWhareatypeId(order.getWhareatypeId());
        }

        // 商品明细
        List<RetailOrderGoodsDetail> goodsDetails = createGoodsDetails(order, retailOrderBill, distributeOrderInfo);
        retailOrderBill.setGoodsDetail(goodsDetails);

        // 配送明细
        List<RetailOrderDistributionInfo> distributionInfos = createDistributeInfos(order.getDistributionInfos(),
            deliveryType);
        retailOrderBill.setAdvancedDistributionInfos(distributionInfos);

        // 结算明细
        List<RetailOrderSettleDetail> settleDetail = createSettleDetails(order.getSettleDetails(), goodsDetails);
        retailOrderBill.setSettleDetails(settleDetail);

        return retailOrderBill;
    }

    private List<RetailOrderSettleDetail> createSettleDetails(List<RetailOrderSettleDetail> settleDetails,
        List<RetailOrderGoodsDetail> goodsDetails) {

        List<RetailOrderSettleDetail> resultDetails = new ArrayList<>();

        double totalAmountSettle = 0d;
        for (RetailOrderGoodsDetail goodsDetail : goodsDetails) {
            totalAmountSettle += goodsDetail.getAmountSettle();
        }

        // 结算方式只有一条，不然不好拆分
        for (RetailOrderSettleDetail settleDetail : settleDetails) {
            RetailOrderSettleDetail addSettleDetail = new RetailOrderSettleDetail();
            addSettleDetail.setBillNo(settleDetail.getBillNo());
            addSettleDetail.setSettlementId(settleDetail.getSettlementId());
            addSettleDetail.setPayDate(settleDetail.getPayDate());
            addSettleDetail.setAmountSettle(new BigDecimal(totalAmountSettle));
            addSettleDetail.setAmountPaidIn(addSettleDetail.getAmountSettle());
            addSettleDetail.setIntegralUsed(settleDetail.getIntegralUsed());

            resultDetails.add(addSettleDetail);
        }

        return resultDetails;
    }

    private List<RetailOrderDistributionInfo> createDistributeInfos(
        List<RequestRetailOrderDistributionInfo> distributionInfos, DeliveryType deliveryType) {

        List<RetailOrderDistributionInfo> distributeInfos = new ArrayList<>();
        for (RequestRetailOrderDistributionInfo info : distributionInfos) {
            RetailOrderDistributionInfo saveInfo = new RetailOrderDistributionInfo();
            saveInfo.setBillNo(info.getBillNo());
            saveInfo.setZip(info.getZipCode());
            saveInfo.setCityId(info.getCityId());
            saveInfo.setDistrictId(info.getDistrictId());
            saveInfo.setRetailOrderBillId(info.getRetailOrderBillId());
            saveInfo.setDeliveryTypeId(info.getDeliveryTypeId());
            saveInfo.setDeliveryNo(info.getDeliveryNo());
            saveInfo.setCountryId(info.getCountryId());
            saveInfo.setOnlineShopTransNo(info.getOnlineShopTransNo());
            saveInfo.setTbDistriPurNo(info.getTbDistriPurNo());
            saveInfo.setTbDistriTradeNo(info.getTbDistriTradeNo());
            saveInfo.setAddress(info.getAddress());
            saveInfo.setAlipayTransNo(info.getAlipayTransNo());
            saveInfo.setProvinceId(info.getProvinceId());
            saveInfo.setWeigh(info.getWeigh());
            saveInfo.setWarehouseOutId(info.getWarehouseOutId());
            saveInfo.setWhAreaTypeOutId(info.getWhAreaTypeOutId());
            saveInfo.setWareHouseDefaultId(info.getWareHouseDefaultId());
            saveInfo.setWareHouseRealId(info.getWareHouseRealId());

            distributeInfos.add(saveInfo);
        }

        return distributeInfos;
    }

    private List<RetailOrderGoodsDetail> createGoodsDetails(RequestRetailOrderBill order,
        RetailOrderBill retailOrderBill, OrderResponseInfo info) {

        List<RequestRetailOrderGoodsDetail> goodsDetails = order.getGoodsDetail();
        List<RetailOrderGoodsDetail> resultGoodsDetails = new ArrayList<>();

        if (info != null) {
            double totalAmountTag = 0d;
            double amountTotal = 0d;
            double payableAmount = 0d;

            SendSKUDetail[] skuDetails = info.getSkuDetail();
            for (SendSKUDetail detail : skuDetails) {
                RequestRetailOrderGoodsDetail requestGoodsDetail = LinqUtil.first(goodsDetails,
                    f -> f.getSingleProductCode().equals(detail.getSku()));
                if (detail == null) {
                    continue;
                }

                RetailOrderGoodsDetail resultGoodsDetail = createRetailOrderGoodsDetail(requestGoodsDetail);
                resultGoodsDetail.setQty(detail.getQty());

                totalAmountTag += requestGoodsDetail.getAmountTag();
                amountTotal += requestGoodsDetail.getAmount();
                payableAmount += requestGoodsDetail.getPrice();

                resultGoodsDetails.add(resultGoodsDetail);
            }

            retailOrderBill.setAmountTag(totalAmountTag);
            retailOrderBill.setAmountTotal(amountTotal);
            retailOrderBill.setPayableAmount(payableAmount);
        } else {
            for (RequestRetailOrderGoodsDetail requestGoodsDetail : order.getGoodsDetail()) {
                RetailOrderGoodsDetail resultGoodsDetail = createRetailOrderGoodsDetail(requestGoodsDetail);
                resultGoodsDetail.setQty(requestGoodsDetail.getQty());
                resultGoodsDetails.add(resultGoodsDetail);
            }
            retailOrderBill.setAmountTag(order.getAmountTag());
            retailOrderBill.setAmountTotal(order.getAmountTotal());
            retailOrderBill.setPayableAmount(order.getPayableAmount());
        }

        return resultGoodsDetails;
    }

    private RetailOrderGoodsDetail createRetailOrderGoodsDetail(RequestRetailOrderGoodsDetail requestGoodsDetail) {
        RetailOrderGoodsDetail resultGoodsDetail = new RetailOrderGoodsDetail();
        resultGoodsDetail.setAmountTag(requestGoodsDetail.getAmountTag());
        resultGoodsDetail.setBarcode(requestGoodsDetail.getBarcode());
        resultGoodsDetail.setAmount(requestGoodsDetail.getAmount());

        resultGoodsDetail.setPriceCost(requestGoodsDetail.getPriceCost());
        resultGoodsDetail.setIsGift(requestGoodsDetail.getIsGift());
        resultGoodsDetail.setAmountDiscount(requestGoodsDetail.getAmountDiscount());
        resultGoodsDetail.setPrice(requestGoodsDetail.getPrice());
        resultGoodsDetail.setAmountCost(requestGoodsDetail.getAmountCost());
        resultGoodsDetail.setAmountSettle(requestGoodsDetail.getAmountSettle());
        resultGoodsDetail.setAmountStandard(requestGoodsDetail.getAmountStandard());
        resultGoodsDetail.setDiscount(requestGoodsDetail.getDiscount());
        resultGoodsDetail.setBillNo(requestGoodsDetail.getBillNo());
        resultGoodsDetail.setSingleProductId(requestGoodsDetail.getSingleproductId());
        resultGoodsDetail.setPriceTag(requestGoodsDetail.getPriceTag());
        resultGoodsDetail.setGoodsId(requestGoodsDetail.getGoodsId());
        resultGoodsDetail.setPriceStandard(requestGoodsDetail.getPriceStandard());

        return resultGoodsDetail;
    }

    private List<OmsRetailOrderBill> createDeliveryParas(RequestRetailOrderBill order,
        OrderDistributeResponse distributeResponse) {
        List<OmsRetailOrderBill> omsBills = new ArrayList<>();
        List<RequestRetailOrderDistributionInfo> distributionInfos = order.getDistributionInfos();
        RequestRetailOrderDistributionInfo distributionInfo = distributionInfos.get(0);
        if (distributeResponse == null) {
            OmsRetailOrderBill bill = new OmsRetailOrderBill();
            bill.setBillNo(order.getBillNo());
            bill.setWareHouseId(order.getWareHouseId());
            bill.setShopId(order.getShopId());
            bill.setPlatformCode(order.getPlatformCode());
            bill.setPayState(String.valueOf(order.getPayState()));
            bill.setAmountTotal(order.getAmountTotal());
            bill.setProvinceId(distributionInfo.getProvinceId());
            bill.setCityId(distributionInfo.getCityId());
            bill.setDistrictId(distributionInfo.getDistrictId());
            bill.setOrderType(order.getOrderType());
            bill.setPlatformGoodsIds(order.getPlatformGoodsIds());
            omsBills.add(bill);
        } else {
            for (OrderResponseInfo info : distributeResponse.getOrderResponseInfo()) {
                OmsRetailOrderBill bill = new OmsRetailOrderBill();
                bill.setBillNo(info.getOrderBillNo());
                bill.setWareHouseId(Long.parseLong(info.getWareHouseId()));
                bill.setShopId(order.getShopId());
                bill.setPlatformCode(order.getPlatformCode());
                bill.setPayState(String.valueOf(order.getPayState()));
                bill.setAmountTotal(calculateAmountTotal(order.getGoodsDetail(), info.getSkuDetail()));
                bill.setProvinceId(distributionInfo.getProvinceId());
                bill.setCityId(distributionInfo.getCityId());
                bill.setDistrictId(distributionInfo.getDistrictId());
                bill.setOrderType(order.getOrderType());
                bill.setPlatformGoodsIds(order.getPlatformGoodsIds());
                omsBills.add(bill);
            }
        }
        return omsBills;
    }

    private Double calculateAmountTotal(List<RequestRetailOrderGoodsDetail> goodsDetails, SendSKUDetail[] skuDetails) {
        Double totalAmount = 0d;
        for (SendSKUDetail skuDetail : skuDetails) {
            RequestRetailOrderGoodsDetail goodsDetail = LinqUtil.first(goodsDetails,
                f -> f.getSingleProductCode().equals(skuDetail.getSku()));

            totalAmount += goodsDetail.getAmount();
        }

        return totalAmount;
    }

    private OrderDistributeParas createOrderDistributeParas(RequestRetailOrderBill order) {

        OrderDistributeParas paras = new OrderDistributeParas();
        paras.setOrderBillNo(order.getBillNo());
        paras.setSupportCashOnDelivery(order.getSupportCashOnDelivery());
        paras.setPreSaleOrderBill(order.getPreSaleOrderBill());
        paras.setShopCode(order.getShopCode());

        List<RequestRetailOrderDistributionInfo> distributionInfos = order.getDistributionInfos();
        RequestRetailOrderDistributionInfo distributionInfo = distributionInfos.get(0);
        paras.setContryCode(distributionInfo.getCountryId().toString());
        paras.setProvinceCode(distributionInfo.getProvinceId().toString());
        paras.setCityCode(distributionInfo.getCityId().toString());
        paras.setAreaCode(distributionInfo.getDistrictId().toString());
        paras.setIsAllowPart("1");
        paras.setIsPureGift(order.getIsPureGift());
        List<OrderDisSKUDetail> skuDetail = createOdsSkuDetail(order.getGoodsDetail());
        paras.setSkuDetail(skuDetail.toArray(new OrderDisSKUDetail[skuDetail.size()]));
        return paras;
    }

    private List<OrderDisSKUDetail> createOdsSkuDetail(List<RequestRetailOrderGoodsDetail> goodsDetails) {

        List<OrderDisSKUDetail> skuDetails = new ArrayList<>();
        for (RequestRetailOrderGoodsDetail goodsDetail : goodsDetails) {
            OrderDisSKUDetail skuDetail = new OrderDisSKUDetail();
            skuDetail.setSku(goodsDetail.getSingleProductCode());
            skuDetail.setQty(goodsDetail.getQty());
            skuDetail.setIsGift(goodsDetail.getIsGift());
            skuDetails.add(skuDetail);
        }
        return skuDetails;
    }
}
