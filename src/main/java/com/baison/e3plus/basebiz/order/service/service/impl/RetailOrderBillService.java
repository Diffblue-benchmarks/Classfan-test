package com.baison.e3plus.basebiz.order.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleSingleProduct;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleGoods;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.basebiz.order.api.common.OrderStatus;
import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.*;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderBillService;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderDistributionInfoService;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderGoodsDetailService;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderSettleDetailService;
import com.baison.e3plus.basebiz.order.api.service.calculate.IOrderDistributeCalculateService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailOrderBillMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailOrderDistributionInfoMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailOrderGoodsDetailMapper;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerAdvancedGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerBarcodeService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerAdvancedOrderOperateStockService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerStockLockLogService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerStockOperateService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.*;
import com.baison.e3plus.biz.stock.api.model.stock.GoodsStockParas;
import com.baison.e3plus.biz.stock.api.model.stock.StockInfo;
import com.baison.e3plus.biz.stock.api.model.stock.StockLockLog;
import com.baison.e3plus.biz.sales.api.model.thirdParty.RetailTeturnBillEntity;
import com.baison.e3plus.biz.stock.api.model.virtualwarehouse.VirtualDistributeParam;
import com.baison.e3plus.biz.stock.api.model.virtualwarehouse.VirtualDistributeParamDetail;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.AdvancedChannel;
import com.baison.e3plus.biz.support.api.business.advanced.model.closebill.CloseBillStrategy;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedWareHouse;
import com.baison.e3plus.biz.support.api.goods.model.brand.Brand;
import com.baison.e3plus.biz.support.api.manyunit.model.ExchangeRate;
import com.baison.e3plus.biz.support.api.parameter.SystemParameter;
import com.baison.e3plus.biz.support.api.publicrecord.model.businesstype.BsType;
import com.baison.e3plus.biz.support.api.publicrecord.model.channel.BusinessRelationDetail;
import com.baison.e3plus.biz.support.api.publicrecord.model.channel.Channel;
import com.baison.e3plus.biz.support.api.publicrecord.model.currencytype.CurrencyType;
import com.baison.e3plus.biz.support.api.publicrecord.model.customer.Customer;
import com.baison.e3plus.biz.support.api.publicrecord.model.owner.Owner;
import com.baison.e3plus.biz.support.api.publicrecord.model.platform.Platform;
import com.baison.e3plus.biz.support.api.publicrecord.model.settlemethod.SettleMethod;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.biz.support.api.publicrecord.model.whareatype.WhareaType;
import com.baison.e3plus.biz.support.api.user.model.UserErrorCode;
import com.baison.e3plus.biz.support.api.user.model.UserInfo;
import com.baison.e3plus.biz.support.api.userauth.model.AuthObject;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.bscore.other.ServiceUtils;
import com.baison.e3plus.common.bscore.utils.CollectionUtil;
import com.baison.e3plus.common.bscore.utils.DateUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.common.BillStatus;
import com.baison.e3plus.common.cncore.common.Status;
import com.baison.e3plus.common.cncore.common.exception.ServiceResultErrorException;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.session.Session;
import com.baison.e3plus.common.cncore.util.RSAVerifyUtil;
import com.baison.e3plus.common.orm.metadata.Condition;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * 销售订单查询接口实现
 *
 * @author cong
 */
@Slf4j
@Service
public class RetailOrderBillService implements IRetailOrderBillService {

    @Autowired
    private IRetailOrderGoodsDetailService goodsDetailService;
    public void setGoodsDetailService(IRetailOrderGoodsDetailService goodsDetailService) {
        this.goodsDetailService = goodsDetailService;
    }

    @Autowired
    private IRetailOrderSettleDetailService settleDetailService;

    public void setDistributionInfoService(IRetailOrderDistributionInfoService distributionInfoService) {
        this.distributionInfoService = distributionInfoService;
    }

    @Autowired
    private IRetailOrderDistributionInfoService distributionInfoService;

    @Autowired
    private ConsumerCodeRuleService codeRuleService;

    @Autowired
    private ConsumerAdvancedChannelService channelService;

    @Autowired
    private ConsumerExchangeRateService exchangeRateService;

    @Autowired
    private ConsumerCurrencyTypeService currencyTypeService;

    @Autowired
    private ConsumerAdvancedShopService shopService;

    @Autowired
    private ConsumerWhareaTypeService whareaTypeService;

    @Autowired
    private ConsumerBrandService brandService;

    @Autowired
    private ConsumerAdvancedWareHouseService wareHouseService;

    @Autowired
    private ConsumerPlatformService platformService;

    @Autowired
    private ConsumerCustomerService customerService;

    @Autowired
    private ConsumerStockOperateService stockService;

    @Autowired
    private ConsumerStockLockLogService lockLogService;

    @Autowired
    private ConsumerAdvancedOrderOperateStockService oosService;

    @Autowired
    private ConsumerAdvancedGoodsService goodsService;

    @Autowired
    private ConsumerSingleProductService skuService;

    @Autowired
    private ConsumerOwnerService ownerService;

    @Autowired
    private ConsumerSettleMethodService settleMethodService;

    @Autowired
    private ConsumerIdService idService;

    @Autowired
    private ConsumerBarcodeService consumerBarcodeService;

    @Autowired
    private ConsumerBusinessRelationDetailService detailService;

    @Autowired
    private ConsumerSystemParameterService consumerSystemParameterService;



    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    private RetailOrderBillMapper retailOrderBillMapper;

    @Autowired
    private ConsumerCloseBillStrategyService closeBillStrategyService;

    @Autowired
    public ConsumerAuthObjectService authObjectService;

    @Autowired
    public ConsumerDataAuthService dataAuthService;

    @Autowired
    private ConsumerAdvancedLoginService loginService;

    @Autowired
    private OriginalRetailOrderBillService originalRetailOrderBillService;

    @Autowired
    private ConsumerBsTypeService consumerBsTypeService;

    public void setOriginalRetailOrderBillService(OriginalRetailOrderBillService originalRetailOrderBillService) {
        this.originalRetailOrderBillService = originalRetailOrderBillService;
    }

    @Autowired(required = false)
    private RetailOrderGoodsDetailMapper detailMapper;

    @Autowired(required = false)
    private RetailOrderDistributionInfoMapper distributionInfoMapper;

    @Override
    @Transactional
    public ServiceResult submit(String token, String billNo) {
        Session session = LoginUtil.checkUserLogin(token);

        long start = System.currentTimeMillis();
        ServiceResult result = new ServiceResult();

        RetailOrderBill dbOrder = findAndCheckOrder(token, result, billNo, OrderStatus.SUBMIT);
        if (result.hasError()) {
            return result;
        }
        if (OrderStatus.SUBMIT.equals(dbOrder.getStatus())) {
            return result;
        }
        RetailOrderGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token, result, dbOrder);

        if (result.hasError()) {
            return result;
        }

        dbOrder.setSubmitBy(session.userCode);
        dbOrder.setSubmitDate(new Date());

        long end = System.currentTimeMillis();
        logProcess("1.订单校验耗时:", end - start);

        start = System.currentTimeMillis();
        List<VirtualDistributeParam> vdparamList = new ArrayList<>();
        // 商店id
        Long shopId = dbOrder.getShopId();
        E3Selector selector = new E3Selector();
        selector.addSelectFields("code");
        selector.addSelectFields("wareHouseId");
        selector.addFilterField(new E3FilterField("shopId", shopId));
        Shop[] shops = shopService.findShop(token, selector);
        Shop shop = shops[0];
        String shopCode = shop.getCode();

        // 操作分配池库存
        VirtualDistributeParam vdparam = new VirtualDistributeParam();
        vdparam.setShopCode(shopCode);
        vdparam.setBillNo(dbOrder.getBillNo());

        List<VirtualDistributeParamDetail> paraDetailList = new ArrayList<>();
        for (RetailOrderGoodsDetail goodsDetail : goodsDetails) {
            VirtualDistributeParamDetail paraDetail = new VirtualDistributeParamDetail();
            paraDetail.setSingleId(goodsDetail.getSingleProductId());
            paraDetail.setWareHouseId(dbOrder.getWarehouseId());
            paraDetail.setWhareaTypeId(dbOrder.getWhareatypeId());
            // paraDetail.setWareHouseCode(dbOrder.getWareHouseCode());
            paraDetail.setQty(goodsDetail.getQty().intValue());

            paraDetailList.add(paraDetail);
        }
        vdparam.setDetails(paraDetailList);
        vdparamList.add(vdparam);

        dbOrder.setStatus(OrderStatus.SUBMIT);

        end = System.currentTimeMillis();
        logProcess("2.设置订单状态耗时:", end - start);

        retailOrderBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailOrderBill[]{dbOrder}));

        GoodsStockParas[] stockParas = createGoodsStockParas(dbOrder, goodsDetails);

        RetailTeturnBillEntity retailTeturnBillEntity = new RetailTeturnBillEntity();
        retailTeturnBillEntity.setParas(stockParas);
        retailTeturnBillEntity.setVdparamList(vdparamList);
        ServiceResult lockResult = oosService.submitLockStock(token, retailTeturnBillEntity);
        if (lockResult.hasError()) {
            throw new ServiceResultErrorException(lockResult);
        }

        // TODO 如果不走分单的场景，需要扣减信用额度，这个等直营上线再考虑

        result.addSuccessObject(dbOrder);
        return result;
    }

    private GoodsStockParas[] createGoodsStockParas(RetailOrderBill dbOrder, RetailOrderGoodsDetail[] goodsDetails) {
        List<GoodsStockParas> paras = new ArrayList<>();
        for (RetailOrderGoodsDetail detail : goodsDetails) {
            GoodsStockParas para = new GoodsStockParas();
            para.setBillDate(dbOrder.getBusinessDate());
            para.setBillId(dbOrder.getId().toString());
            para.setBillNo(dbOrder.getBillNo());
            para.setGoodsCode("");
            para.setGoodsId(detail.getGoodsId());
            para.setOwnerId(detail.getOwnerId());
            para.setQuantity(detail.getQty());
            para.setSingleProductId(detail.getSingleProductId());
            para.setWareHouseId(dbOrder.getWarehouseId());
            para.setWhareaTypeId(
                    detail.getWhAreaTypeId() != null ? detail.getWhAreaTypeId() : dbOrder.getWhareatypeId());

            paras.add(para);
        }

        return paras.toArray(new GoodsStockParas[paras.size()]);
    }

    private void logProcess(String message, long lostTime) {
        logger.info("DistributeOrder123:" + message + lostTime);
    }

    @Override
    @Transactional
    public ServiceResult complete(String token, String billNo, Date wmsDate) {
        Session session = LoginUtil.checkUserLogin(token);
        ServiceResult result = new ServiceResult();
        RetailOrderBill dbOrder = findAndCheckOrder(token, result, billNo, OrderStatus.COMPLETE);
        if (result.hasError()) {
            return result;
        }
        if (OrderStatus.COMPLETE.equals(dbOrder.getStatus())) {
            return result;
        }
        RetailOrderGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token, result, dbOrder);

        if (result.hasError()) {
            return result;
        }

        dbOrder.setStatus(OrderStatus.COMPLETE);

        dbOrder.setCompleteBy(session.userCode);

        dbOrder.setCompleteDate(new Date());
        //wms回传时间
        dbOrder.setWmsDate(wmsDate);

        // 配送状态:已出库
        dbOrder.setDistributionState("7");

        dbOrder.setGoodsDetail(Arrays.asList(goodsDetails));

        Shop shop = findShopById(token, dbOrder.getShopId());

        result = closeBill(token, dbOrder, BillType.RETAILORDERBILL, shop.getChannelId());

        if (result.hasError()) {
            return result;
        }

        retailOrderBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailOrderBill[]{dbOrder}));

        List<StockInfo> stockInfos = createStockInfos(token, dbOrder, goodsDetails);
        //释放库存锁并扣减库存
        result = stockService.decreStockAndLockStock(token, stockInfos);

        if (result.hasError()) {
            throw new ServiceResultErrorException(result);
        }
        return result;
    }

    /**
     * 根据店铺ID获取与上级组织的关系
     *
     * @param token
     * @param shopId
     * @return
     */
    private Shop findShopById(String token, Long shopId) {
        Shop shop = null;
        if (null == shopId) {
            return shop;
        }
        E3Selector shopE3Selector = new E3Selector();
        shopE3Selector.addFilterField(new E3FilterField("shopId", shopId));
        shopE3Selector.addSelectFields("channelId");
        Shop[] shops = shopService.findShop(token, shopE3Selector);
        if (null != shops && shops.length > 0) {
            shop = shops[0];
        }
        return shop;
    }

    private ServiceResult closeBill(String token, RetailOrderBill orderBill, String billType, Long channelId) {
        ServiceResult result = new ServiceResult();

        LocalDate localDate = LocalDate.now();
        // 查询品牌信息
        Long brandId = null;
        E3Selector brandselector = new E3Selector();
        brandselector.addSelectFields("brandId");
        if (orderBill.getGoodsDetail() == null || orderBill.getGoodsDetail().get(0) == null || orderBill.getGoodsDetail().get(0).getGoodsId() == null) {
            result.addErrorObject(null, AdvancedOrderErrorCode.CLOSE_BILL_003, ResourceUtils.get(AdvancedOrderErrorCode.CLOSE_BILL_003));
            return result;
        }
        brandselector.addFilterField(new E3FilterField("goodsId", orderBill.getGoodsDetail().get(0).getGoodsId()));
        SimpleGoods[] e3goods = goodsService.queryAdvancedSimpleGoods(token, brandselector);
        if (null != e3goods && e3goods.length > 0) {
            brandId = e3goods[0].getBrandId();
        }

        // 验证单据的
        E3Selector closeBillSelector = new E3Selector();
        if (brandId == null) {
            result.addErrorObject(null, AdvancedOrderErrorCode.CLOSE_BILL_001, ResourceUtils.get(AdvancedOrderErrorCode.CLOSE_BILL_001));
            return result;
        }

        if (channelId == null) {
            result.addErrorObject(null, AdvancedOrderErrorCode.CLOSE_BILL_002, ResourceUtils.get(AdvancedOrderErrorCode.CLOSE_BILL_001));
            return result;
        }
        closeBillSelector.addFilterField(new E3FilterField("brandId", brandId));
        closeBillSelector.addFilterField(new E3FilterField("channelId", channelId));
        closeBillSelector.addFilterField(new E3FilterField("billType", billType));
        closeBillSelector.addFilterField(new E3FilterField("status", Status.ENABLE));

        // 符合的记录只有一条
        CloseBillStrategy[] strategys = closeBillStrategyService.queryObject(token, closeBillSelector);
        if (null != strategys && strategys.length >= 1) {
            CloseBillStrategy strategy = strategys[0];
            Date closeDate = strategy.getCloseDate();

            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            // 将时分秒,毫秒域清零
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            if (null != closeDate && (cal.getTime().after(closeDate))) {
                LocalDate nextMonth = localDate.plusMonths(1);
                LocalDate nextMonthFirstDay = LocalDate.of(nextMonth.getYear(), nextMonth.getMonthValue(), 1);
                Date newBillDate = Date.from(nextMonthFirstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
                orderBill.setBusinessDate(newBillDate);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public ServiceResult term(String token, String billNo) {

        ServiceResult result = new ServiceResult();
        Session session = LoginUtil.checkUserLogin(token);

        RetailOrderBill dbOrder = findAndCheckOrder(token, result, billNo, OrderStatus.TERM);
        if (result.hasError()) {
            return result;
        }
        if (OrderStatus.TERM.equals(dbOrder.getStatus())) {
            return result;
        }
        RetailOrderGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token, result, dbOrder);

        if (result.hasError()) {
            return result;
        }

        boolean needUpdateStock = OrderStatus.SUBMIT.equals(dbOrder.getStatus());

        dbOrder.setAbolishBy(session.userCode);
        dbOrder.setAbolishDate(new Date());
        dbOrder.setStatus(OrderStatus.TERM);

        retailOrderBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailOrderBill[]{dbOrder}));

        // 释放促销的已赠送数量
        /*List<PromotionExchangeParams> exchangeInfos = createExchangeInfos(token, goodsDetails);
        if (exchangeInfos.size() > 0) {
            try {
                exchangeService.releaseExchangeQtyGiven(token, exchangeInfos);
            } catch (Exception e) {
            	logger.error(e.getMessage(), (Throwable) e);
                result.addErrorObjectNotThrow(billNo, "", e.getMessage());
                throw new ServiceResultErrorException(result);
            }
        }*/

        if (needUpdateStock) {

            List<StockInfo> stockInfos = createStockInfos(token, dbOrder, goodsDetails);
            result = stockService.releaseLockStock(token, stockInfos);
            if (result.hasError()) {
                throw new ServiceResultErrorException(result);
            }

        }
        return result;
    }

    private Map<Integer, List<Integer>> getPreStatus() {
        Map<Integer, List<Integer>> result = new HashMap<>();

        // 提交状态的前置状态
        result.put(OrderStatus.SUBMIT, Arrays.asList(OrderStatus.INITIAL));

        // 完成状态的前置状态
        result.put(OrderStatus.COMPLETE, Arrays.asList(OrderStatus.SUBMIT));

        // 初始状态的前置状态
        result.put(OrderStatus.INITIAL, Arrays.asList(OrderStatus.SUBMIT));

        // 终止状态的前置状态
        result.put(OrderStatus.TERM, Arrays.asList(OrderStatus.INITIAL, OrderStatus.SUBMIT));

        return result;
    }

    private RetailOrderBill findAndCheckOrder(String token, ServiceResult result, String billNo, Integer targetStatus) {
        RetailOrderBill dbOrder = null;
        if (billNo == null) {
            result.addErrorObject("", "", "input billNo is null!");
            return null;
        }

        Map<String, Object> args = new HashMap<>();
        args.put("billNo", billNo);

        List<RetailOrderBill> dbOrders = retailOrderBillMapper.queryPage(args);
        if (dbOrders == null || dbOrders.size() == 0) {
            result.addErrorObject("", "", "data not exist in db!");
            return null;
        }

        dbOrder = dbOrders.get(0);
        // 校验状态是否合法
        if (!getPreStatus().get(targetStatus).contains(dbOrder.getStatus())) {
            result.addErrorObject("", "e3.errorCode.publicrecord.44001", StringUtil.format(
                    ResourceUtils.get("e3.errorCode.publicrecord.44001"), OrderStatus.getStatusDesc(targetStatus)));
            return null;
        }

        // 校验库位
        if (dbOrder.getWhareatypeId() == null) {
            result.addErrorObject("", AdvancedOrderErrorCode.RETAILBILL_WHAREATYPE_IS_NULL,
                    ResourceUtils.get(AdvancedOrderErrorCode.RETAILBILL_WHAREATYPE_IS_NULL));
            return null;
        }

        return dbOrder;
    }

    private RetailOrderGoodsDetail[] findAndCheckGoodsDetails(String token, ServiceResult result,
                                                              RetailOrderBill bill) {
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("retailOrderBillId", bill.getId()));
        if (bill.getBillNo() != null) {
            selector.addFilterField(new E3FilterField("billNo", bill.getBillNo()));
        }

        RetailOrderGoodsDetail[] goodsDetails = goodsDetailService.queryPage(token, selector, -1, -1);
        if (goodsDetails == null || goodsDetails.length == 0) {
            result.addErrorObject("", AdvancedOrderErrorCode.STOCK_012, ResourceUtils.get("e3.errorCode.stock.99012"));
            return null;
        }

        for (RetailOrderGoodsDetail retailOrderGoodsDetail : goodsDetails) {
            if (retailOrderGoodsDetail.getSingleProductId() == null
                    || (bill.getWhareatypeId() == null && bill.getShopId() == null)) {
                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_015);
            }
            if (retailOrderGoodsDetail.getQty() == null || retailOrderGoodsDetail.getQty().equals(0)) {
                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_048,
                        ResourceUtils.get("e3.errorCode.stock.99048", retailOrderGoodsDetail.getGoodsId()));
            }
        }

        return goodsDetails;
    }

    @Override
    @Transactional
    public ServiceResult retreat(String token, String billNo) {

        ServiceResult result = new ServiceResult();

        RetailOrderBill dbOrder = findAndCheckOrder(token, result, billNo, OrderStatus.INITIAL);
        if (result.hasError()) {
            return result;
        }

        RetailOrderGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token, result, dbOrder);

        if (result.hasError()) {
            return result;
        }

        dbOrder.setStatus(OrderStatus.INITIAL);
        retailOrderBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailOrderBill[]{dbOrder}));

        // 撤销订单，将分单结果数据设为废除，以便可以重新分单
        IOrderDistributeCalculateService calculateService = ServiceUtils
                .getService(IOrderDistributeCalculateService.class);
        calculateService.disableOrderDistributeLog(token, dbOrder.getBillNo());

        List<StockInfo> stockInfos = createStockInfos(token, dbOrder, goodsDetails);

        result = stockService.releaseLockStock(token, stockInfos);

        if (result.hasError()) {
            throw new ServiceResultErrorException(result);
        }

        return result;
    }

    private List<StockInfo> createStockInfos(String token, RetailOrderBill dbOrder,
                                             RetailOrderGoodsDetail[] goodsDetails) {

        AdvancedWareHouse wareHouse = queryWareHouse(token, dbOrder.getWarehouseId());
        String areaId = null;
        if ("1".equals(wareHouse.getType())) {// 门店仓
            areaId = wareHouse.getProvinceId().toString();
        } else {
            areaId = wareHouse.getCode();
        }

        List<StockInfo> stockInfos = new ArrayList<>();
        for (RetailOrderGoodsDetail detail : goodsDetails) {
            StockInfo stockInfo = new StockInfo(null, null, dbOrder.getWarehouseId(), dbOrder.getWhareatypeId(),
                    detail.getGoodsId(), detail.getGoods().getCode(), detail.getSingleProductId(),
                    detail.getSingleProduct().getCode(), null, null, detail.getQty(), dbOrder.getId().toString(),
                    dbOrder.getBillNo(), BillType.RETAILORDERBILL, dbOrder.getBusinessDate(), new Date());

            stockInfo.setAreaId(areaId);
            stockInfos.add(stockInfo);
        }

        return stockInfos;
    }

    private AdvancedWareHouse queryWareHouse(String token, Long wareHouseId) {
        /**
         * 查询仓库信息
         */
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("wareHouseId", wareHouseId));
        selector.addSelectFields("type");
        selector.addSelectFields("provinceId");
        selector.addSelectFields("code");
        WareHouse[] datas = wareHouseService.queryWareHouse(token, selector);
        if (datas == null || datas.length == 0) {
            throw new RuntimeException("order`s wareHouseId is not found! wareHouseId : " + wareHouseId);
        }

        return (AdvancedWareHouse) datas[0];
    }

    @Override
    @Transactional
    public ServiceResult create(String token, RetailOrderBill[] beans) {

        try {
            ServiceResult result = new ServiceResult();
            Session session = LoginUtil.checkUserLogin(token);

            RetailOrderBill[] dataArray = new RetailOrderBill[0];
            if (beans != null && beans.length > 0) {
                dataArray = beans;
            }

            // 判断传入的单据编号是否存在重复，有则提示
            // 本身单据编号重复集合
            List<String> billNoList = new ArrayList<>();
            // 与数据库单据编号重复集合
            List<String> billNoListInDb = new ArrayList<>();

            String[] billNos = Arrays.stream(beans).map(t -> t.getBillNo()).toArray(String[]::new);
            E3Selector selector = new E3Selector();
            selector.addSelectFields("billNo");
            selector.addFilterField(new E3FilterField("billNo", "in", billNos, E3FilterField.ANDOperator));
            RetailOrderBill[] retailOrderBills = queryPage(token, selector, -1, -1);
            for (int i = 0; i < beans.length; i++) {
                for (int j = i + 1; j < beans.length; j++) {
                    if (beans[i].getBillNo() != null && beans[i].getBillNo().equals(beans[j].getBillNo())) {
                        billNoList.add(beans[i].getBillNo());
                        break;
                    }
                }
                for (RetailOrderBill retailOrderBill : retailOrderBills) {
                    if (beans[i].getBillNo() != null && beans[i].getBillNo().equals(retailOrderBill.getBillNo())) {
                        billNoListInDb.add(beans[i].getBillNo());
                    }
                }
            }
            if (!billNoList.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(ResourceUtils.get("e3.errorCode.order.clothes.08004"));
                for (String billNoStr : billNoList) {
                    stringBuffer.append(billNoStr + ",");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                result.addErrorObject(null, "", stringBuffer.toString());
                return result;
            }
            if (!billNoListInDb.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(ResourceUtils.get("e3.errorCode.order.clothes.08004"));
                for (String billNoStr : billNoListInDb) {
                    stringBuffer.append(billNoStr + ",");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                result.addErrorObject(null, "", stringBuffer.toString());
                return result;
            }

            // 生成单据编号和单据ID
            for (RetailOrderBill bill : dataArray) {
                String billNo = bill.getBillNo();
                if (StringUtil.isEmptyOrNull(billNo)) {
                    billNo = codeRuleService.generateCode(token, "RetailOrderBill", bill);
                    bill.setBillNo(billNo);
                }
                bill.setId(idService.nextId());
            }

            // 安踏OMS推送过来的订单为提交状态，如果有给状态，则不赋值状态字段
            boolean needInitailStatus = true;
            for (RetailOrderBill bill : beans) {
                if (bill.getStatus() != null) {
                    needInitailStatus = false;

                    bill.setCreateBy(session.userCode);
                    if (bill.getCreateDate() == null) {
                        bill.setCreateDate(new Date());
                    }
//                    bill.setChannelId(session.channelId);

                    bill.setSubmitBy(session.userCode);
                    if (bill.getSubmitDate() == null) {
                        bill.setSubmitDate(new Date());
                    }
                }
            }

            if (needInitailStatus) {
                for (RetailOrderBill bill : beans) {
                    bill.setCreateBy(session.userCode);
                    bill.setCreateDate(new Date());
                    bill.setStatus(OrderStatus.INITIAL);
                }
            }

            // 给明细的单据编号赋值
            setRetailOrderBillNo(token, dataArray);

            // 给本位币、汇率类型、汇率赋值
            setRetailBaseCurrency(token, dataArray);

            List<RetailOrderBill> dataList = Arrays.asList(dataArray);

            retailOrderBillMapper.insertSelective(dataList);
            result.getSuccessObject().addAll(dataList);

            // 保存明细
            saveDetails(token, dataArray);

            if (!result.hasError()) {
                // 如果状态是 sumit（OMS走分单过来的订单，创建时为提交状态），那么需要判断信用额度是否充足，如果不充足，则需要调用撤销操作，
                // 释放库存，将订单置为未确认并通知oms

                // TODO 直营上线时，才有信用额度控制
            }

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void saveDetails(String token, RetailOrderBill[] dataArray) {

        // 商品明细
        List<RetailOrderGoodsDetail> goodsDetails = new ArrayList<>();
        // 结算明细
        List<RetailOrderSettleDetail> settleDetails = new ArrayList<>();
        // 配送信息明细
        List<RetailOrderDistributionInfo> distributeInfoList = new ArrayList<>();
        for (RetailOrderBill bill : dataArray) {
            if (bill.getGoodsDetail() != null) {
                for (RetailOrderGoodsDetail detail : bill.getGoodsDetail()) {
                    detail.setRetailOrderBillId(bill.getId());
                    goodsDetails.add(detail);
                }
            }

            if (bill.getSettleDetails() != null) {
                for (RetailOrderSettleDetail detail : bill.getSettleDetails()) {
                    detail.setRetailOrderBillId(bill.getId());
                    settleDetails.add(detail);
                }
            }

            if (bill.getAdvancedDistributionInfos() != null) {
                for (RetailOrderDistributionInfo detail : bill.getAdvancedDistributionInfos()) {
                    detail.setRetailOrderBillId(bill.getId());
                    distributeInfoList.add(detail);
                }
            }
        }

        if (goodsDetails != null && goodsDetails.size() > 0) {
            goodsDetailService.create(token, goodsDetails.toArray(new RetailOrderGoodsDetail[goodsDetails.size()]));
        }

        if (settleDetails.size() > 0) {
            settleDetailService.create(token, settleDetails.toArray(new RetailOrderSettleDetail[settleDetails.size()]));
        }

        if (distributeInfoList.size() > 0) {
            distributionInfoService.create(token,
                    distributeInfoList.toArray(new RetailOrderDistributionInfo[distributeInfoList.size()]));
        }
    }

    protected void setRetailBaseCurrency(String token, RetailOrderBill[] dataArray) {
        List<RetailOrderBill> dataList = Arrays.asList(dataArray);
        List<Long> channelIds = LinqUtil.select(dataList, s -> s.getChannelId());
        List<Long> standardCurrencyIds = new ArrayList<>();// 本位币ids
        Map<Long, AdvancedChannel> channelMap = getChannelMap(token, channelIds, standardCurrencyIds);
        for (RetailOrderBill bill : dataArray) {
            Long currencyTypeId = bill.getCurrencyTypeId();// 结算币
            Long baseCurrencyId = bill.getBaseCurrencyId();// 本位币
            if (baseCurrencyId != null) {
                setAmountTax(token, currencyTypeId, baseCurrencyId, bill);
                continue;
            }
            // 本位币取值逻辑：取商店所属上级销售组织本位币
            Long channelId = bill.getChannelId();
            AdvancedChannel channel = channelMap.get(channelId);
            if (channel == null) {
                continue;
            }
            baseCurrencyId = channel.getStandardCurrencyId();
            bill.setBaseCurrencyId(baseCurrencyId);

            // 设置明细上的含税金额本位币等
            setAmountTax(token, currencyTypeId, baseCurrencyId, bill);
        }

    }

    protected void setAmountTax(String token, Long currencyTypeId, Long baseCurrencyId, RetailOrderBill bill) {
        ExchangeRate exchangeRate = getExchangeRateMap(token, currencyTypeId, baseCurrencyId);
        if (exchangeRate != null) {
            bill.setExchangeratetype(exchangeRate.getExchangeratetype());// 汇率类型
            bill.setExchangerate(exchangeRate.getExchangerate());
        }

        // 设置明细上的含税金额本位币=含税金额*汇率
        Double totalTaxPrice = bill.getTotalTaxPrice() == null ? 0 : bill.getTotalTaxPrice();// 含税金额
        Double exchangerate2 = bill.getExchangerate() == null ? 0 : bill.getExchangerate();// 汇率
        Double amountStandardMoney = totalTaxPrice * exchangerate2;
        bill.setTotalTaxPriceStandardMoney(amountStandardMoney);// 含税金额本位币

        List<RetailOrderGoodsDetail> goodsDetails = bill.getGoodsDetail();
        if (goodsDetails == null || goodsDetails.size() == 0) {
            return;
        }

        for (RetailOrderGoodsDetail goodsDetail : goodsDetails) {
            Double amount = goodsDetail.getAmount() == null ? 0d : goodsDetail.getAmount();
            goodsDetail.setTotalTaxPrice(amount);// 含税金额
            goodsDetail.setTotalTaxPriceStandardMoney(amount * exchangerate2);// 含税金额本位币
        }
    }

    protected ExchangeRate getExchangeRateMap(String token, Long currencyTypeId, Long standardCurrencyId) {
        if (currencyTypeId == null || standardCurrencyId == null) {
            return null;
        }
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("status", Status.ENABLE));
        selector.addFilterField(new E3FilterField("basecurrency", currencyTypeId));// 原币-结算币
        selector.addFilterField(new E3FilterField("exchangecurrency", standardCurrencyId));// 目标币-本位币
        selector.addSelectFields("Exchangeratetype");
        selector.addSelectFields("Exchangerate");
        ExchangeRate[] exchangeRates = exchangeRateService.query(token, selector);
        if (exchangeRates == null || exchangeRates.length == 0) {
            return null;
        }
        return exchangeRates[0];
    }

    protected Map<Long, AdvancedChannel> getChannelMap(String token, List<Long> channelIds,
                                                   List<Long> standardCurrencyIds) {
        Map<Long, AdvancedChannel> channelMap = new HashMap<>();
        if (channelIds == null || channelIds.size() == 0) {
            return channelMap;
        }
        E3Selector selector = new E3Selector();
        selector.addSelectFields("channelId");
        selector.addSelectFields("code");
        selector.addSelectFields("name");
        selector.addSelectFields("standardCurrencyId");
        selector.addFilterField(new E3FilterField("channelId", channelIds));
        Channel[] channels = channelService.queryPageChannel(token, selector, -1, -1);
        if (channels == null || channels.length == 0) {
            return channelMap;
        }

        Long localCurrencyId = null;
        for (Channel object : channels) {
            if (object instanceof AdvancedChannel) {
                AdvancedChannel channel = (AdvancedChannel) object;
                Long standardCurrencyId = channel.getStandardCurrencyId();
                if (standardCurrencyId == null) {
                    if (localCurrencyId == null) {
                        localCurrencyId = getLocalCurrencyId(token);
                    }
                    standardCurrencyId = localCurrencyId;
                    channel.setStandardCurrencyId(standardCurrencyId);
                }
                // 本位币
                standardCurrencyIds.add(standardCurrencyId);

                // 如果组织档案上本位币为空，取币别档案上勾选本位币那条记录
                channelMap.put(channel.getChannelId(), channel);
            }
        }

        return channelMap;
    }

    private Long getLocalCurrencyId(String token) {
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("localcurrency", "1"));
        selector.addSelectFields("CurrencyId");
        CurrencyType[] currencyTypeList = currencyTypeService.findCurrencyTypeList(token, selector);
        if (currencyTypeList == null || currencyTypeList.length == 0) {
            return null;
        }
        return currencyTypeList[0].getCurrencyId();
    }

    protected void setRetailOrderBillNo(String token, RetailOrderBill[] dataArray) {
        for (RetailOrderBill saveBean : dataArray) {
            String billNo = saveBean.getBillNo();
            Long retailBillId = saveBean.getId();
            List<RetailOrderGoodsDetail> goodsDetails = saveBean.getGoodsDetail();
            if (goodsDetails != null && goodsDetails.size() > 0) {
                for (RetailOrderGoodsDetail goodsDetail : goodsDetails) {
                    goodsDetail.setBillNo(billNo);
                    goodsDetail.setRetailOrderBillId(retailBillId);
                }
            }
            List<RetailOrderSettleDetail> settleDetails = saveBean.getSettleDetails();
            if (settleDetails != null && settleDetails.size() > 0) {
                for (RetailOrderSettleDetail settleDetail : settleDetails) {
                    settleDetail.setBillNo(billNo);
                    settleDetail.setRetailOrderBillId(retailBillId);
                }
            }

            List<? extends RetailOrderDistributionInfo> distributionInfos = saveBean.getAdvancedDistributionInfos();
            if (distributionInfos != null && distributionInfos.size() > 0) {
                for (RetailOrderDistributionInfo distributionInfo : distributionInfos) {
                    distributionInfo.setBillNo(billNo);
                    distributionInfo.setRetailOrderBillId(retailBillId);
                }
            }
        }
    }

    @Override
    @Transactional
    public ServiceResult modify(String token, RetailOrderBill[] beans) {

        try {
            RetailOrderBill[] dataArray = beans;

            ServiceResult result = new ServiceResult();
            // 只有初始、提交状态的订单才能被修改
            for (RetailOrderBill bill : dataArray) {
                if (!(OrderStatus.INITIAL.equals(bill.getStatus()) || OrderStatus.SUBMIT.equals(bill.getStatus()))) {
                    result.addErrorObject("", AdvancedOrderErrorCode.ORDER_CANNOT_MODIFY,
                        ResourceUtils.get(AdvancedOrderErrorCode.ORDER_CANNOT_MODIFY));
                    break;
                }
            }
            if (result.hasError()) {
                throw new ServiceResultErrorException(result);
            }

            List<RetailOrderBill> dataList = Arrays.asList(dataArray);
            retailOrderBillMapper.updateByPrimaryKeySelective(dataList);

            /**
             * 商品明细同步规则，直接删掉已有明细数据，将传过来的明细重新创建一遍
             */
            // 根据单号删除商品明细
            List<Object> billNos = new ArrayList<>();
            for (RetailOrderBill bill : dataList) {
                billNos.add(bill.getBillNo());
            }
            goodsDetailService.removeByBillNo(token, billNos);

            // 重新创建
            List<RetailOrderGoodsDetail> goodsDetails = new ArrayList<>();
            for (RetailOrderBill bill : dataArray) {
                Map<String, Object> args = new HashMap<>();
                args.put("billNo", bill.getBillNo());

                List<RetailOrderGoodsDetail> goodsDetail = bill.getGoodsDetail();
                if (goodsDetail != null && goodsDetail.size() > 0) {
                    goodsDetail.forEach(c -> {
                        c.setBillNo(bill.getBillNo());
                        c.setRetailOrderBillId(bill.getId());
                    });
                    goodsDetails.addAll(goodsDetail);
                }
            }
            if (goodsDetails.size() > 0) {
                goodsDetailService.create(token, goodsDetails.toArray(new RetailOrderGoodsDetail[goodsDetails.size()]));
            }

            // 配送明细
            List<RetailOrderDistributionInfo> distributionInfos = new ArrayList<>();
            for (RetailOrderBill bill : dataArray) {
                if (bill.getAdvancedDistributionInfos() != null) {
                    distributionInfos.addAll(bill.getAdvancedDistributionInfos());
                }
            }

            if (distributionInfos != null && distributionInfos.size() > 0) {
                distributionInfoService.saveAndmodify(token, distributionInfos);
            }

            result.getSuccessObject().addAll(dataList);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ServiceResult updateByPrimaryKeySelective(String token, RetailOrderBill[] objects) {
        ServiceResult result = new ServiceResult();
        if (objects == null || objects.length == 0) {
            result.addErrorObject("", "no input data");
            return result;
        }

        List<RetailOrderBill> dataList = Arrays.asList(objects);
        List<List<RetailOrderBill>> partition = Lists.partition(dataList, 200);
        for (List<RetailOrderBill> retailOrderBills : partition) {
            retailOrderBillMapper.updateByPrimaryKeySelective(retailOrderBills);
        }

        return result;
    }

    @Override
    @Transactional
    public ServiceResult remove(String token, Object[] pkIds) {
        ServiceResult result = new ServiceResult();

        if (pkIds == null || pkIds.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }

        retailOrderBillMapper.deleteByPrimaryKey(Arrays.asList(pkIds));

        // 删除明细
        result = goodsDetailService.removeByRetailOrderId(token, pkIds);
        if (result.hasError()) {
            throw new ServiceResultErrorException(result);
        }

        result = settleDetailService.removeByRetailOrderId(token, pkIds);
        if (result.hasError()) {
            throw new ServiceResultErrorException(result);
        }

        result = distributionInfoService.removeByRetailOrderId(token, pkIds);
        if (result.hasError()) {
            throw new ServiceResultErrorException(result);
        }

        return result;
    }

    @Override
    public RetailOrderBill[] findById(String token, Object[] billNos, String[] selectFields) {
        if (billNos == null || billNos.length == 0) {
            return null;
        }

        String fields = null;
        if (selectFields != null) {
            StringBuilder sb = new StringBuilder("id, bill_no");
            for (String field : selectFields) {
                sb.append(",").append(field);
            }
            fields = sb.toString();
        }
        List<RetailOrderBill> bills = null;
        try {
            List<Object> billNoList = Arrays.asList(billNos);
            bills = retailOrderBillMapper.selectByPrimaryKey(billNoList, fields);
            queryBaseFieldDatas(token, bills);

            for (RetailOrderBill bill : bills) {
                // 配送方式明细数据和主表一起返回
                E3Selector selector = new E3Selector();
                selector.addFilterField(new E3FilterField("billNo", bill.getBillNo()));
                RetailOrderDistributionInfo[] distributeInfo = distributionInfoService.queryPage(token, selector, -1,
                        -1);
                if (distributeInfo != null) {
                    bill.setAdvancedDistributionInfos(Arrays.asList(distributeInfo));
                }
            }

            //结算方式
            if (bills != null && bills.size() > 0) {
                Set<Long> settleMethodIdSet = new HashSet<>();
                List<List<RetailOrderSettleDetail>> collect = bills.stream().map(t -> t.getSettleDetails()).collect(Collectors.toList());
                for (List<RetailOrderSettleDetail> retailOrderSettleDetails : collect) {
                    if (retailOrderSettleDetails != null && retailOrderSettleDetails.size() > 0) {
                        for (RetailOrderSettleDetail retailOrderSettleDetail : retailOrderSettleDetails) {
                            if (retailOrderSettleDetail.getSettlementId() != null) {
                                settleMethodIdSet.add(Long.valueOf(retailOrderSettleDetail.getSettlementId()));
                            }
                        }
                    }
                }

                if (settleMethodIdSet != null && settleMethodIdSet.size() > 0) {
                    E3Selector e3Selector = new E3Selector();
                    e3Selector.addFilterField(new E3FilterField("settleMethodId", "in", settleMethodIdSet.toArray(new Long[0]), E3FilterField.ANDOperator));
                    e3Selector.addSelectFields("code");
                    e3Selector.addSelectFields("name");
                    SettleMethod[] settleMethods = settleMethodService.queryObject(token, e3Selector);
                    if (settleMethods != null && settleMethods.length > 0) {
                        Map<String, SettleMethod> stringSettleMethodMap = Arrays.stream(settleMethods).collect(Collectors.toMap(t -> String.valueOf(t.getSettleMethodId()), t -> t));

                        for (RetailOrderBill bill : bills) {
                            List<RetailOrderSettleDetail> settleDetails = bill.getSettleDetails();
                            if (settleDetails != null && settleDetails.size() > 0) {
                                for (RetailOrderSettleDetail settleDetail : settleDetails) {
                                    settleDetail.setSettleMethod(stringSettleMethodMap.get(String.valueOf(settleDetail.getSettlementId())));
                                }
                            }
                        }
                    }
                }
            }

            return bills.toArray(new RetailOrderBill[bills.size()]);
        } catch (Exception e) {
            logger.error("错误信息： " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public RetailOrderBill[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {

        Map<String, Object> args = getQueryPageSelectorConditionMap(token,selector);

        if (pageSize > 0 && pageIndex >= 0) {
            int stratRow = pageIndex * pageSize;

            args.put("stratRow", stratRow);
            args.put("endRow", pageSize);
        }

        List<RetailOrderBill> datas = new ArrayList<>();
        try {
            datas = retailOrderBillMapper.queryPage(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        queryBaseFieldDatas(token, datas);

        return datas.toArray(new RetailOrderBill[datas.size()]);
    }

    @Override
    public long getListCount(String token, E3Selector selector) {

        Map<String, Object> args = getQueryPageSelectorConditionMap(token,selector);

        return retailOrderBillMapper.getListCount(args);
    }

    private Map<String, Object> getQueryPageSelectorConditionMap(String token,E3Selector selector){
        Map<String, Object> args = new HashMap<>();
        for (E3FilterField field : selector.getFilterFields()) {
            if (field instanceof E3FilterGroup) {
                for (E3FilterField grupField : ((E3FilterGroup) field).getFilterFields()) {
                    if (grupField.getFieldName().toLowerCase().indexOf("date") > 0) {
                        if (grupField.getConditionOperator().equals(">=")) {
                            args.put(grupField.getFieldName() + "_start", grupField.getValue());
                        } else {
                            args.put(grupField.getFieldName() + "_end", grupField.getValue());
                        }
                    } else {
                        if("billno".equals(grupField.getFieldName().toLowerCase()) || "sourcebillno".equals(grupField.getFieldName().toLowerCase())){
                            args.put(grupField.getFieldName(), String.valueOf(grupField.getOriginValue()).toUpperCase());
                        } else {
                            args.put(grupField.getFieldName(), grupField.getOriginValue());
                        }

                    }
                }
            } else{
                if ("submitDate".equals(field.getFieldName())) {
                    args.put(field.getFieldName(), DateUtil.convertToDate(field.getValue(), "yyyy-MM-dd HH:mm:ss"));
                } else if (field.getFieldName().toLowerCase().indexOf("date") > 0) {
                    if (field.getConditionOperator().equals(">=")) {
                        args.put(field.getFieldName() + "_start", field.getValue());
                    } else {
                        args.put(field.getFieldName() + "_end", field.getValue());
                    }
                } else if (field.getFieldName().toLowerCase().equals("status")) {// 状态全部改为in for
                    if (field.getValue() instanceof Object[]) {
                        args.put(field.getFieldName(), field.getValue());
                    } else {
                        args.put(field.getFieldName(), new Object[]{field.getValue()});
                    }
                } else if ("shopId".equals(field.getFieldName()) && field.getConditionOperator().equals(Condition.NOEQUAL)) {
                    args.put(field.getFieldName() + "Not", field.getOriginValue());
                } else {
                    args.put(field.getFieldName(), field.getOriginValue());
                }
            }
        }

        Session session = LoginUtil.checkUserLogin(token);
        if (!isSuperAdmin(session.userRole)) {
            // 处理权限
            verifyPermission(token, session, args);
        }
        return args;
    }

    private boolean isSuperAdmin(String userRole) {
        return UserInfo.SUPER_ADMIN.equals(userRole);
    }

    public String ConditionIn(E3FilterField field) {
        StringBuilder sb = new StringBuilder();
        if (field.getValue() instanceof String[]) {
            String[] status = (String[]) field.getValue();
            for (String s : status) {
                sb.append(s).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return null;
    }

    /**
     * 查询销售订单中基础档案字段的数据
     *
     * @param token
     * @param datas
     */
    private void queryBaseFieldDatas(String token, List<RetailOrderBill> datas) {

        if (datas == null || datas.size() == 0) {
            return;
        }

        // 客户id
        List<Long> customerIds = new ArrayList<>();
        // 组织id
        List<Long> channelIds = new ArrayList<>();
        // 币别id
        List<Long> currencyTypeIds = new ArrayList<>();
        // 平台id
        List<Long> platfrormIds = new ArrayList<>();
        // 仓库id
        List<Long> warehouseIds = new ArrayList<>();
        // 商店id
        List<Long> shopIds = new ArrayList<>();
        // 品牌id
        List<Long> brandIds = new ArrayList<>();
        // 库位id
        List<Long> whareatypeIds = new ArrayList<>();
        //业务类型
        List<String> bsTypeIdList = new ArrayList<>();
        //支付方式
        List<Integer> settleMethodIds = new ArrayList<>();

        for (RetailOrderBill bill : datas) {
            if (bill.getPaySettleMethodId() != null)
                settleMethodIds.add(bill.getPaySettleMethodId());
            if (bill.getSettleDetails() != null && bill.getSettleDetails().size() > 0) {
                settleMethodIds.add(bill.getSettleDetails().get(0).getSettlementId());
            }

            if (bill.getCustomerId() != null) {
                customerIds.add(bill.getCustomerId());
            }
            if (bill.getChannelId() != null) {
                channelIds.add(bill.getChannelId());
            }
            if (bill.getSettleChannelId() != null) {
                channelIds.add(bill.getSettleChannelId());
            }
            if (bill.getStockChannelId() != null) {
                channelIds.add(bill.getStockChannelId());
            }
            if (bill.getPlatformId() != null) {
                platfrormIds.add(bill.getPlatformId());
            }
            if (bill.getCurrencyTypeId() != null) {
                currencyTypeIds.add(bill.getCurrencyTypeId());
            }
            if (bill.getBaseCurrencyId() != null) {
                currencyTypeIds.add(bill.getBaseCurrencyId());
            }
            if (bill.getWarehouseId() != null) {
                warehouseIds.add(bill.getWarehouseId());
            }
            if (bill.getShopId() != null) {
                shopIds.add(bill.getShopId());
            }
            if (bill.getWhareatypeId() != null) {
                whareatypeIds.add(bill.getWhareatypeId());
            }
            if (bill.getBrandId() != null) {
                brandIds.add(bill.getBrandId());
            }
            if(bill.getOrderType()!=null){
                bsTypeIdList.add(bill.getOrderType());
            }
        }

        E3Selector selector = new E3Selector();
        E3FilterField field = new E3FilterField("", "");
        selector.addFilterField(field);
        selector.addSelectFields("code");
        selector.addSelectFields("name");

        if (settleMethodIds.size() > 0) {
            field.setFieldName("settleMethodId");
            field.setValue(settleMethodIds);
            SettleMethod[] settleMethods = settleMethodService.queryObject(token, selector);
            Map<Long, SettleMethod> mapDatas = new HashMap<>();
            for (SettleMethod settleMethod : settleMethods) {
                mapDatas.put(settleMethod.getSettleMethodId(), settleMethod);
            }
            datas.forEach(c -> {
                SettleMethod settleMethod = mapDatas.get(c.getPaySettleMethodId() == null ? null : Long.valueOf(c.getPaySettleMethodId()));
                c.setSettleMethod(settleMethod);
                c.getSettleDetails().forEach(settleDetail -> {
                    SettleMethod settleMethodDetail = mapDatas.get(settleDetail.getSettlementId() == null ? null : Long.valueOf(settleDetail.getSettlementId()));
                    settleDetail.setSettleMethod(settleMethodDetail);
                });
            });
            ;

        }
        if (customerIds.size() > 0) {
            field.setFieldName("customerId");
            field.setValue(customerIds);
            Customer[] customers = customerService.queryCustomer(token, selector);
            if (customers != null && customers.length > 0) {
                Map<Long, Customer> mapDatas = new HashMap<>();
                for (Customer customer : customers) {
                    mapDatas.put(customer.getCustomerId(), customer);
                }
                datas.forEach(c -> {
                    Customer customer = mapDatas.get(c.getCustomerId());
                    c.setCustomer(customer);
                });
            }
        }

        // 组织
        if (channelIds.size() > 0) {
            field.setFieldName("channelId");
            field.setValue(channelIds);
            Channel[] channels = channelService.queryPageChannel(token, selector, -1, -1);
            if (channels != null && channels.length > 0) {
                Map<Long, Channel> mapDatas = new HashMap<>();
                for (Channel channel : channels) {
                    mapDatas.put(channel.getChannelId(), channel);
                }
                datas.forEach(c -> {
                    Channel channel = mapDatas.get(c.getChannelId());
                    c.setChannel(channel);

                    Channel settleChannel = mapDatas.get(c.getSettleChannelId());
                    c.setSettleChanel(settleChannel);

                    Channel stockChannel = mapDatas.get(c.getStockChannelId());
                    c.setStockChannel(stockChannel);
                });
            }
        }

        // 币别
        if (currencyTypeIds.size() > 0) {
            E3Selector selector1 = new E3Selector();
            selector1.addFilterField(new E3FilterField("currencyId", currencyTypeIds));
            selector1.addSelectFields("currencyCode");
            selector1.addSelectFields("currencyName");
            CurrencyType[] currencyTypes = currencyTypeService.queryPageCurrencyType(token, selector1, -1, -1);
            if (currencyTypes != null && currencyTypes.length > 0) {
                Map<Long, CurrencyType> mapDatas = new HashMap<>();
                for (CurrencyType currencyType : currencyTypes) {
                    mapDatas.put(currencyType.getCurrencyId(), currencyType);
                }
                datas.forEach(c -> {
                    CurrencyType currencyType = mapDatas.get(c.getCurrencyTypeId());
                    c.setCurrencyType(currencyType);

                    c.setBaseCurrency(mapDatas.get(c.getBaseCurrencyId()));
                });
            }
        }

        // 平台
        if (platfrormIds.size() > 0) {
            field.setFieldName("platformId");
            field.setValue(platfrormIds);
            Platform[] platforms = platformService.queryObject(token, selector);
            if (platforms != null && platforms.length > 0) {
                Map<Long, Platform> mapDatas = new HashMap<>();
                for (Platform platform : platforms) {
                    mapDatas.put(platform.getPlatformId(), platform);
                }
                datas.forEach(c -> {
                    c.setPlatfrorm(mapDatas.get(c.getPlatformId()));
                });
            }
        }

        // 仓库
        if (warehouseIds.size() > 0) {
            field.setFieldName("warehouseId");
            field.setValue(warehouseIds);
            WareHouse[] warehouses = wareHouseService.queryObject(token, selector);
            if (warehouses != null && warehouses.length > 0) {
                Map<Long, WareHouse> mapDatas = new HashMap<>();
                for (WareHouse warehouse : warehouses) {
                    mapDatas.put(warehouse.getWareHouseId(), warehouse);
                }
                datas.forEach(c -> {
                    c.setWarehouse(mapDatas.get(c.getWarehouseId()));
                });
            }
        }

        // 商店
        if (shopIds.size() > 0) {
            E3Selector selector1 = new E3Selector();
            selector1.addFilterField(new E3FilterField("shopId", shopIds));
            selector1.addSelectFields("code");
            selector1.addSelectFields("name");
            selector1.addSelectFields("channelId");
            Shop[] shops = shopService.queryPageShop(token, selector1, -1, -1);
            if (shops != null && shops.length > 0) {
                Map<Long, Shop> mapDatas = new HashMap<>();
                for (Shop shop : shops) {
                    mapDatas.put(shop.getShopId(), shop);
                }
                datas.forEach(c -> {
                    c.setShop(mapDatas.get(c.getShopId()));
                });
            }
        }

        // 品牌
        if (brandIds.size() > 0) {
            field.setFieldName("brandId");
            field.setValue(brandIds);
            Brand[] brands = brandService.queryBrand(token, selector);
            if (brands != null && brands.length > 0) {
                Map<Long, Brand> mapDatas = new HashMap<>();
                for (Brand brand : brands) {
                    mapDatas.put(brand.getBrandId(), brand);
                }
                datas.forEach(c -> {
                    c.setBrand(mapDatas.get(c.getBrandId()));
                });
            }
        }

        // 库位
        if (whareatypeIds.size() > 0) {
            field.setFieldName("whareatypeId");
            field.setValue(whareatypeIds);
            WhareaType[] whareaTypes = whareaTypeService.queryWhareaType(token, selector);
            if (whareaTypes != null && whareaTypes.length > 0) {
                Map<Long, WhareaType> mapDatas = new HashMap<>();
                for (WhareaType whareaType : whareaTypes) {
                    mapDatas.put(whareaType.getWhareaTypeId(), whareaType);
                }
                datas.forEach(c -> {
                    c.setWhareaType(mapDatas.get(c.getWhareatypeId()));
                });
            }
        }

        //类型
        if(bsTypeIdList.size()>0){
            selector.addFilterField(new E3FilterField("code",bsTypeIdList));
            BsType[] bstypes = consumerBsTypeService.query(token,selector);
            if(bstypes!=null&&bstypes.length>0){
                Map<String,BsType> bsTypeMap = new HashMap<>(bstypes.length);
                for(BsType type : bstypes){
                    bsTypeMap.put(type.getCode(),type);
                }
                datas.forEach(c -> {
                    c.setBsType(bsTypeMap.get(c.getOrderType()));
                });
            }
        }

    }

    private void queryBaseDetailFieldDatas(String token, List<RetailOrderBill> bills) {

        // 货主id
        List<Long> ownerIds = new ArrayList<>();
        // skuid
        List<Long> singleproductIds = new ArrayList<>();
        // 商品id
        List<Long> goodsIds = new ArrayList<>();

        for (RetailOrderBill bill : bills) {
            for (RetailOrderGoodsDetail detail : bill.getGoodsDetail()) {
                if (detail.getOwnerId() != null) {
                    ownerIds.add(detail.getOwnerId());
                }
                if (detail.getSingleProductId() != null) {
                    singleproductIds.add(detail.getSingleProductId());
                }
                if (detail.getGoodsId() != null) {
                    goodsIds.add(detail.getGoodsId());
                }
            }
        }

        E3Selector selector = new E3Selector();
        E3FilterField field = new E3FilterField("", "");
        selector.addFilterField(field);
        selector.addSelectFields("code");
        selector.addSelectFields("name");

        // 货主
        Map<Long, Owner> ownerMapDatas = new HashMap<>();
        if (ownerIds.size() > 0) {
            field.setFieldName("id");
            field.setValue(ownerIds);
            Owner[] owners = ownerService.queryObject(token, selector);
            if (owners != null && owners.length > 0) {
                for (Owner owner : owners) {
                    ownerMapDatas.put(owner.getId(), owner);
                }
            }
        }

        // sku
        Map<Long, SimpleSingleProduct> skuMapDatas = new HashMap<>();
        if (singleproductIds.size() > 0) {
            E3Selector skuSelector = new E3Selector();
            skuSelector.addFilterField(new E3FilterField("singleProductId", singleproductIds));
            skuSelector.addSelectFields("singleProductId");
            skuSelector.addSelectFields("code");
            skuSelector.addSelectFields("name");
            skuSelector.addSelectFields("spec1");
            skuSelector.addSelectFields("attspec1.code");
            skuSelector.addSelectFields("attspec1.name");
            skuSelector.addSelectFields("spec2");
            skuSelector.addSelectFields("attspec2.code");
            skuSelector.addSelectFields("attspec2.name");
            skuSelector.addSelectFields("spec3");
            skuSelector.addSelectFields("attspec3.code");
            skuSelector.addSelectFields("attspec3.name");
            skuSelector.addSelectFields("e3Goods.code");
            skuSelector.addSelectFields("e3Goods.baseUnit.code");
            SimpleSingleProduct[] skus = skuService.querySimpleSingleProduct(token, skuSelector);
            if (skus != null && skus.length > 0) {
                for (SimpleSingleProduct sku : skus) {
                    skuMapDatas.put(sku.getSingleProductId(), sku);
                }
            }
        }

        // 商品
        Map<Long, SimpleGoods> goodsMapDatas = new HashMap<>();
        if (goodsIds.size() > 0) {
            selector.addSelectFields("goodsId");
            selector.addSelectFields("brandId");
            selector.addSelectFields("brand.code");
            selector.addSelectFields("brand.name");
            field.setFieldName("goodsId");
            field.setValue(goodsIds);
            SimpleGoods[] goods = goodsService.queryAdvancedSimpleGoods(token, selector);
            if (goods != null && goods.length > 0) {
                for (SimpleGoods g : goods) {
                    goodsMapDatas.put(g.getGoodsId(), g);
                }
            }
        }

        for (RetailOrderBill bill : bills) {
            List<RetailOrderGoodsDetail> datas = bill.getGoodsDetail();
            datas.forEach(c -> {
                c.setOwner(ownerMapDatas.get(c.getOwnerId()));
                c.setSingleProduct((AdvancedSimpleSingleProduct) skuMapDatas.get(c.getSingleProductId()));
                c.setGoods(goodsMapDatas.get(c.getGoodsId()));
            });
        }
    }

    @Override
    public RetailOrderBill[] queryObjectBySql(String token, String sql) {
        try {
            List<RetailOrderBill> bills = retailOrderBillMapper.queryObjectBySql(sql);
            if (bills != null && !bills.isEmpty()) {
                queryBaseFieldDatas(token, bills);
                queryBaseDetailFieldDatas(token, bills);
                return bills.toArray(new RetailOrderBill[bills.size()]);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void updateBySql(String token, String sql) {
        try {
            retailOrderBillMapper.updateBySql(sql);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public SetMealRetailOrderGoods[] queryPageBySetMealRetailOrderGoods(String token, Map<String,Object> filterMaps,
                                                                        int pageSize, int pageIndex) {

        if (pageSize > 0 && pageIndex >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize);
        }
//        List<SetMealRetailOrderGoods> setMealRetailOrderGoods = adsRetailOrderBillMapper
//                .queryPageBySetMealRetailOrderGoods(filterMaps);
//        if (setMealRetailOrderGoods == null || setMealRetailOrderGoods.size() == 0) {
//            return null;
//        }
//        return setMealRetailOrderGoods.toArray(new SetMealRetailOrderGoods[setMealRetailOrderGoods.size()]);
        return null;
    }

    @Override
    public long getSetMealRetailOrderGoodsCount(String token,Map<String,Object> filterMaps) {
//        return adsRetailOrderBillMapper.getSetMealRetailOrderGoodsCount(filterMaps);
    	return 0L;
    }

    @Override
    @Transactional
    public ServiceResult savePassedBackBill(String token, RetailOrderBill[] bean,
                                            List<RetailOrderDistributionInfo> info, List<String> billNos, Date wmsDate) {
        ServiceResult result = new ServiceResult();
        this.updateByPrimaryKeySelective(token, bean);
        if (null != info && info.size() > 0)
            distributionInfoService.saveAndmodify(token, info);
        if (null != billNos && billNos.size() > 0) {
            for (String billNo : billNos) {
                result = this.complete(token, billNo, wmsDate);
            }
        }
        return result;
    }

    @Override
    public ServiceResult releaseBillLockStock(String token, String billNo) {

        ServiceResult result = new ServiceResult();

        // 根据单据编号找到锁定记录
        E3Selector selector = new E3Selector();
        selector.addSelectFields("billId");
        selector.addSelectFields("billcode");
        selector.addSelectFields("billType");
        selector.addSelectFields("channelId");
        selector.addSelectFields("ownerId");
        selector.addSelectFields("wareHouseId");
        selector.addSelectFields("whareaTypeId");
        selector.addSelectFields("goodsId");
        selector.addSelectFields("singleProductId");
        selector.addSelectFields("qtyBfChange");
        selector.addSelectFields("qtyAfChange");
        selector.addSelectFields("qty");
        selector.addSelectFields("billDate");
        selector.addFilterField(new E3FilterField("billCode", billNo));

        StockLockLog[] stockLockLogs = lockLogService.queryObject(token, selector);
        if (stockLockLogs == null || stockLockLogs.length == 0) {
            return result;
        }

        // 同一个订单仓库一样
        AdvancedWareHouse wareHouse = queryWareHouse(token, stockLockLogs[0].getWareHouseId());

        String areaId = null;
        if ("1".equals(wareHouse.getType())) {// 门店仓
            areaId = wareHouse.getProvinceId().toString();
        } else {
            areaId = wareHouse.getCode();
        }

        List<StockInfo> stockInfos = new ArrayList<>();
        for (StockLockLog lockLog : stockLockLogs) {
            StockInfo stockInfo = new StockInfo(null, null, lockLog.getWareHouseId(), lockLog.getWhareaTypeId(),
                    lockLog.getGoodsId(), "", lockLog.getSingleProductId(),
                    "", null, null, lockLog.getQty(), billNo,
                    billNo, BillType.RETAILORDERBILL, lockLog.getBillDate(), new Date());

            stockInfo.setAreaId(areaId);
            stockInfos.add(stockInfo);
        }

        result = stockService.releaseLockStock(token, stockInfos);

        if (result.hasError()) {
            throw new ServiceResultErrorException(result);
        }

        // 撤销订单，将分单结果数据设为废除，以便可以重新分单
        IOrderDistributeCalculateService calculateService = ServiceUtils
                .getService(IOrderDistributeCalculateService.class);
        calculateService.disableOrderDistributeLog(token, billNo);

        return result;
    }

    @Override
    public int updateBatchByBillNo(Map map) {
        return retailOrderBillMapper.updateBatchByBillNo(map);
    }

    @Override
    public RetailOrderBill[] selectBySap(String token, String selectfields) {
        List<RetailOrderBill> list = retailOrderBillMapper.selectBySap(selectfields);
        queryBaseFieldDatas(token, list);
        queryBaseDetailFieldDatas(token, list);
        return list.toArray(new RetailOrderBill[0]);
    }

    @Override
    public RetailOrderBill[] selectBillNoBySap(String token, String selectfields) {
        List<RetailOrderBill> list = retailOrderBillMapper.selectBySap(selectfields);
        return list.toArray(new RetailOrderBill[0]);
    }

    @Override
    public int updateIsConfirm(Map map) {
        return retailOrderBillMapper.updateIsConfirm(map);
    }

    @Override
    public List<Map<String, Object>> queryRetailOrderBillMapToOms(String token, E3Selector selector, int pageSize, int pageIndex,boolean isADS) {
        Map<String, Object> args = getBillToOmsSelectorConditionMap(token,selector,isADS);
        List<Map<String, Object>> datas = new ArrayList<>();
        if(args == null){
            Map<String, Object> msg = new HashMap<>();
            String errorCode = ResourceUtils.get("e3.errorCode.support." + UserErrorCode.USERNAME_ISNOT_EXIST);
            msg.put("error",errorCode);
            datas.add(msg);
            return datas;
        }
        int stratRow = pageIndex * pageSize;
        args.put("stratRow", stratRow);
        args.put("endRow", pageSize);
        try {
//            if(isADS){
//                datas = adsRetailOrderBillMapper.queryRetailOrderBillMapToOms(args);
//            } else {
                datas = retailOrderBillMapper.queryRetailOrderBillMapToOms(args);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getRetailOrderBillMap(token, datas);
    }

    @Override
    public long getRetailOrderBillToOmsCount(String token, E3Selector selector,boolean isADS) {
        Map<String, Object> args = getBillToOmsSelectorConditionMap(token,selector,isADS);
        Long count = 0L;
        if(args == null){
            return count;
        }
        try {
//            if(isADS){
//                count = adsRetailOrderBillMapper.getRetailOrderBillToOmsCountByMap(args);
//            } else {
                count = retailOrderBillMapper.getRetailOrderBillToOmsCountByMap(args);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public int batchUpdateHasReturnByBillNo(List<RetailOrderBill> retailOrderBills) {
        if(retailOrderBills == null || retailOrderBills.size() == 0){
            return 0;
        }
        return retailOrderBillMapper.batchUpdateHasReturnByBillNo(retailOrderBills);
    }

    @Override
    public ServiceResult createRetailOrderBillFromOrignalBill(String token) {
       /* //查找状态为初始的原始零售订单
        List<OriginalRetailOrderBill> originalRetailOrderBillList = originalRetailOrderBillService.queryOrderBill(token);
        check(token,originalRetailOrderBillList);
        List<RetailOrderBill> retailOrderBillList = new ArrayList<>(originalRetailOrderBillList.size());
        List<RetailOrderGoodsDetail> orderGoodsDetailList = new ArrayList<>();
        for(OriginalRetailOrderBill originalRetailOrderBill : originalRetailOrderBillList){
            RetailOrderBill retailOrderBill = transOriginalRetailBill2RetailOrderBill(originalRetailOrderBill);
            retailOrderBill.setId(idService.nextId());
            retailOrderBillMapper.insert(retailOrderBill);
            Long billId = retailOrderBill.getId();
            List<RetailOrderGoodsDetail> detaiList = transOriginalRetailBillDetails2RetailOrderBillDetail(token,billId,originalRetailOrderBill.getOriginalRetailOrdGoodsDetailList());
            detailMapper.insertSelective(detaiList);
        }*/
        return new ServiceResult();
    }



    private List<Map<String, Object>> getRetailOrderBillMap(String token, List<Map<String, Object>> args) {
        if (args == null || args.size() == 0) {
            return null;
        }
        // 货号
        Set<String> goodsIds = new HashSet<>();
        // 平台id
        Set<String> platfrormIds = new HashSet<>();
        // 商店id
        Set<String> shopIds = new HashSet<>();

        for (Map<String, Object> arg : args) {

            for (Map.Entry<String, Object> mapEntry : arg.entrySet()) {
                if ("goods_id".equals(mapEntry.getKey())) {
                    goodsIds.add(String.valueOf(mapEntry.getValue()));
                }
                if ("platform_id".equals(mapEntry.getKey())) {
                    platfrormIds.add(String.valueOf(mapEntry.getValue()));
                }
                if ("shop_id".equals(mapEntry.getKey())) {
                    shopIds.add(String.valueOf(mapEntry.getValue()));
                }
                if("receiver".equals(mapEntry.getKey())){
                    if(isBase64(String.valueOf(mapEntry.getValue()))){
                        arg.put("receiver", RSAVerifyUtil.decodeBase64(String.valueOf(mapEntry.getValue())));
                    };
                }
                if("receiver_tel".equals(mapEntry.getKey())){
                    if(isBase64(String.valueOf(mapEntry.getValue()))){
                        arg.put("receiver_tel", RSAVerifyUtil.decodeBase64(String.valueOf(mapEntry.getValue())));
                    };
                }
            }

            // 平台
            if (platfrormIds.size() > 0) {
                E3Selector s = new E3Selector();
                E3FilterField field = new E3FilterField("", "");
                field.setFieldName("platformId");
                field.setValue(platfrormIds);
                s.addFilterField(field);
                s.addSelectFields("name");
                Platform[] platforms = platformService.queryObject(token, s);
                if (platforms != null && platforms.length > 0) {
                    for (Platform platform : platforms) {
                        arg.put("platformName", platform.getName());
                    }
                }
            }
            // 商店
            if (shopIds.size() > 0) {
                E3Selector s = new E3Selector();
                s.addFilterField(new E3FilterField("shopId", shopIds));
                s.addSelectFields("name");
                Shop[] shops = shopService.queryPageShop(token, s, -1, -1);
                if (shops != null && shops.length > 0) {
                    for (Shop shop : shops) {
                        arg.put("shopName", shop.getName());
                    }
                }
            }

            if (goodsIds.size() > 0) {
                E3Selector s = new E3Selector();
                s.addFilterField(new E3FilterField("goodsId", goodsIds));
                s.addSelectFields("code");
                SimpleGoods[] goods = goodsService.queryAdvancedSimpleGoods(token, s);
                for (SimpleGoods good : goods) {
                    arg.put("goodsCode", good.getCode());
                }
            }
        }
        return args;
    }

    /**
     * 判断字符串是否base64编码
     * @param str
     * @return
     */
    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    private Map<String, Object> getBillToOmsSelectorConditionMap(String token,E3Selector selector,boolean isADS){
        Map<String, Object> args = new HashMap<>();
        for (E3FilterField field : selector.getFilterFields()) {
            if (field instanceof E3FilterGroup) {
                for (E3FilterField grupField : ((E3FilterGroup) field).getFilterFields()) {
                    if (grupField.getFieldName().toLowerCase().indexOf("date") > 0) {
                        if (grupField.getConditionOperator().equals(">=")) {
                            args.put(grupField.getFieldName() + "_start", grupField.getValue());
                        } else {
                            args.put(grupField.getFieldName() + "_end", grupField.getValue());
                        }
                    } else {
                        args.put(grupField.getFieldName(), grupField.getOriginValue());
                    }
                }
            } else {
                if (field.getFieldName().toLowerCase().indexOf("date") > 0) {
                    if (field.getConditionOperator().equals(">=")) {
                        args.put(field.getFieldName() + "_start", field.getValue());
                    } else {
                        args.put(field.getFieldName() + "_end", field.getValue());
                    }
                } else if(!isADS && "code".equals(field.getFieldName())){
                    E3Selector s = new E3Selector();
                    s.addFilterField(new E3FilterField("code", field.getOriginValue()));
                    s.addSelectFields("goodsId");
                    SimpleGoods[] goods = goodsService.queryAdvancedSimpleGoods(token, s);
                    if(goods != null && goods.length > 0){
                        StringBuffer goodsIds = new StringBuffer();
                        for (SimpleGoods good : goods) {
                            //满足mybatis里面的in查询
                            goodsIds.append(good.getGoodsId()).append(",");
                        }
                        goodsIds.deleteCharAt(goodsIds.length() - 1);
                        args.put("goodsId", goodsIds.toString());
                    }
                } else if ("receiverTel".equals(field.getFieldName())){
                    args.put("receiverTel", RSAVerifyUtil.encodeBase64(String.valueOf(field.getOriginValue())));
                } else {
                    args.put(field.getFieldName(), field.getOriginValue());
                }
            }
        }
        String userCode = String.valueOf(args.get("userCode"));
        if (StringUtil.isNotEmptyOrNull(userCode)) {
            UserInfo user = loginService.queryUserByUserCode(userCode);
            if(user == null){
                return null;
            }
            E3Selector selectorChannel = new E3Selector();
            selectorChannel.addFilterField(new E3FilterField("saleChannelId",user.getChannelId()));
            //type 6 业务类型关系
            selectorChannel.addFilterField(new E3FilterField("type", 6));
            selectorChannel.addSelectFields("buyChannelId");
            BusinessRelationDetail[] result = detailService.queryObject(token, selectorChannel);
            if(result != null && result.length > 0){
                Set<Long> channelIds = new HashSet<>();
                for (BusinessRelationDetail businessRelationDetail : result) {
                    channelIds.add(businessRelationDetail.getBuyChannelId());
                }
                args.put("channelId", channelIds);
            }
        }
        return args;
    }

    /**
     * 验证权限
     */
    private void verifyPermission(String token,Session session,Map<String, Object> args){
        // 处理权限
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("code", "RetailOrderBill"));
        AuthObject[] authObjects = authObjectService.queryObject(token, selector);
        if (authObjects != null && authObjects.length >= 0) {
            List<Condition> authConditions = dataAuthService.getFilterCondition(session, "",
                    authObjects[0].getAuthObjectId(), "view");
            if (authConditions.size() > 0) {
                addAuthCondition(args, authConditions);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addAuthCondition(Map<String, Object> args, List<Condition> authConditions) {

        for (Condition condition : authConditions) {
            List<Condition> childConditions = condition.getChildConditions();
            if (childConditions == null || childConditions.size() == 0) {
                String fieldName = condition.getExpression();

                if (fieldName.indexOf(".") != -1) {
                    fieldName = fieldName.split("\\.")[1];
                }

                String conditionValue = condition.getValue();
                String operator = condition.getOperation();

                List<Object> authValues = new ArrayList<>();
                if (Condition.IN.equals(operator)) {
                    String[] splitData = conditionValue.split(Condition.IN_SPLIT);
                    for (String data : splitData) {
                        authValues.add(data);
                    }
                } else {
                    authValues.add(conditionValue);
                }

                Object value = args.get(fieldName);
                List<Object> values = null;
                if (value == null) {
                    values = authValues;
                } else {
                    List<Object> existValues = new ArrayList<>();
                    if (value instanceof List) {
                        for (Object v : (List<Object>) value) {
                            existValues.add(v.toString());
                        }
                    } else if (value.getClass().isArray()) {
                        for (Object v : (Object[]) value) {
                            existValues.add(v.toString());
                        }
                    } else {
                        existValues.add(value.toString());
                    }
                    // 取交集
                    values = LinqUtil.intersect(authValues, existValues);
                    if (values.size() == 0) {
                        //如果没有交集，则不可能查出数据，将当前条件字段给出一个不存在的值
                        values.add(0);
                    }
                }

                args.put(fieldName, values);
            } else {
                addAuthCondition(args, childConditions);
            }
        }
    }

    /**
     * eWMS发货回传，修改订单发货状态为“”已发货“”；
     * eWMS出库回传，修改订单状态为“”已完成“”、发货状态为“”已出库“”，释放锁定库存扣减在库库存；
     * @param token
     * @param param
     * @return
     */
    @Override
    public String ewmsCheckOutAndSendOut(String token, String param) {
        String flag = "failure";
        String code = "0001";
        String message = "同步失败！";
        JSONObject sparam = JSONObject.parseObject(param);
        JSONArray orders = sparam.getJSONArray("orders");
        //已发货订单 存放出库单号，对应零售订单里面的物流单号
        List<String> deliveredList = new ArrayList<>();
        //已出库订单 存放出库单号，对应零售订单里面的物流单号
        List<String> closedList = new ArrayList<>();
        List<RetailOrderBill> bills = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            JSONObject order = orders.getJSONObject(i);
            JSONObject deliveryOrder = order.getJSONObject("deliveryOrder");
            String status = deliveryOrder.getString("status");
            //物流单号
            String deliveryOrderCode = deliveryOrder.getString("deliveryOrderCode");
            //字段非空校验
            List<String> errormsg = validateEmptyField(deliveryOrder);
            if(!errormsg.isEmpty()){
                message = errormsg.toString();
                return resultUtil(flag,code,message);
            }
            //校验出库单号是否存在
            Map<String, Object> params = new HashedMap<>();
            params.put("billNo",deliveryOrderCode);
            //校验出库单号是否存在
            E3Selector selector = new E3Selector();
            selector.addSelectFields("id");
            selector.addSelectFields("status");
            selector.addSelectFields("billNo");
            selector.addSelectFields("distributionState");
            selector.addSelectFields("originalRetailBillNo");
            selector.addFilterField(new E3FilterField("billNo", deliveryOrderCode));
            RetailOrderBill[] retailOrderBills = queryPage(token, selector, -1, -1);
            if(retailOrderBills.length == 0 || retailOrderBills == null){
                message = "出库单号不存在！";
                return resultUtil(flag,code,message);
            }
            //校验eERP系统中存在单据状态[已确认]发货状态[拣货中]的零售订单
            RetailOrderBill bill = retailOrderBills[0];
            String errorStr = validateStatusExites(bill,status);
            if(StringUtils.isNoneBlank(errorStr)){
                message = errorStr;
                return resultUtil(flag,code,message);
            }

            if("DELIVERED".equals(status)){
                deliveredList.add(deliveryOrderCode);
            }
            else if ("CLOSED".equals(status)) {
                closedList.add(deliveryOrderCode);
                bill.setStatus(8);
                bill.setDistributionState("8");
            }
            bills.add(bill);
        }
        E3Selector e3 = new E3Selector();
        e3.addSelectFields("billNo");
        if(!CollectionUtil.isEmpty(deliveredList)){
            //e3.addFilterField(new E3FilterField("deliveryNo",deliveredList));
            //RetailOrderDistributionInfo[] retailOrderDistributionInfos = distributionInfoService.queryPage(token, e3, -1, -1);
            //List<String> sendBillNos = Stream.of(retailOrderDistributionInfos).map(RetailOrderDistributionInfo::getBillNo).collect(Collectors.toList());
            int i = batchSendOutByDeliveryOrderCode(token, deliveredList);
            if(i > 0){
                flag = "success";
                code = "0000";
                message = "成功";
            }
        }
        if(!CollectionUtil.isEmpty(closedList)){
            //e3.addFilterField(new E3FilterField("deliveryNo",closedList));
            //RetailOrderDistributionInfo[] retailOrderDistributionInfos = distributionInfoService.queryPage(token, e3, -1, -1);
            //List<String> closedBillNos = Stream.of(retailOrderDistributionInfos).map(RetailOrderDistributionInfo::getBillNo).collect(Collectors.toList());
            batchCheckOutByDeliveryOrderCode(token,closedList);
           /* E3Selector e3Selector = new E3Selector();
            e3Selector.addSelectFields("id");
            e3Selector.addSelectFields("status");
            e3Selector.addSelectFields("billNo");
            e3Selector.addSelectFields("distributionState");
            e3Selector.addSelectFields("originalRetailBillNo");
            e3Selector.addFilterField(new E3FilterField("billNo",closedList));
            //查询主单据
            RetailOrderBill[] retailOrderBills = queryPage(token, e3Selector, -1, -1);*/
            //获取原始零售订单编号
            List<String> oriBillNos = bills.stream().map(RetailOrderBill::getOriginalRetailBillNo).collect(Collectors.toList());
            //原始零售订单编号 sourceBillNo
            long completeStatus = Long.parseLong(BillStatus.COMPLETE);
            //将原始零售订单编号修改为完成
            if(!oriBillNos.isEmpty()){
               originalRetailOrderBillService.updateOrderStatus(token,completeStatus, oriBillNos);
            }
            //查询明细
            E3Selector d3 = new E3Selector();
            d3.addFilterField(new E3FilterField("billNo",closedList));
            RetailOrderGoodsDetail[] retailOrderGoodsDetails = goodsDetailService.queryPage(token, d3, -1, -1);
            //释放库存锁并扣减库存
            ServiceResult result = decreStockAndLockStock(token, bills.toArray(new RetailOrderBill[]{}), retailOrderGoodsDetails);
            if(!result.hasError() && !result.hasWarn()){
                flag = "success";
                code = "0000";
                message = "成功";
            }
        }
        return resultUtil(flag,code,message);
    }

    private String validateStatusExites(RetailOrderBill bill, String status) {
        String errorStr = "";
        Integer billstatus = bill.getStatus();
        String distributionState = bill.getDistributionState();
        if("DELIVERED".equals(status)){
            if(billstatus != 10 && !"5".equals(distributionState)){
                errorStr = "单据状态[已确认]发货状态[拣货中]的零售订单不存在！";
            }
        }
        else if ("CLOSED".equals(status)) {
            if(billstatus != 10 && !"7".equals(distributionState)){
                errorStr = "单据状态[已确认]发货状态[已发出]的零售订单不存在！";
            }
        }
        return errorStr;
    }


    /**
     * 校验非空字段
     * @param deliveryOrder
     * @return
     */
    private List<String> validateEmptyField(JSONObject deliveryOrder) {
        String deliveryOrderCode = deliveryOrder.getString("deliveryOrderCode");
        List<String> errors = new ArrayList<>();
        if(StringUtils.isBlank(deliveryOrderCode)){
            errors.add("出库单号不能为空!");
        }
        String warehouseCode = deliveryOrder.getString("warehouseCode");
        if(StringUtils.isBlank(warehouseCode)){
            errors.add("仓库编码不能为空!");
        }
        String orderType = deliveryOrder.getString("orderType");
        if(StringUtils.isBlank(orderType)){
            errors.add("出库单类型不能为空!");
        }
        return errors;
    }

    /**
     * 释放库存锁并扣减库存
     * @param token
     * @param retailOrderBills
     * @param retailOrderGoodsDetails
     * @return
     */
    private ServiceResult decreStockAndLockStock(String token,RetailOrderBill[] retailOrderBills,RetailOrderGoodsDetail[] retailOrderGoodsDetails){
        List<RetailOrderGoodsDetail> retailOrderGoodsDetailList = Arrays.asList(retailOrderGoodsDetails);
        ServiceResult result = new ServiceResult();
        //key为订单编号
        Map<String, List<RetailOrderGoodsDetail>> detailMap = retailOrderGoodsDetailList.stream().collect(Collectors.groupingBy(RetailOrderGoodsDetail::getBillNo));
        for (RetailOrderBill retailOrderBill : retailOrderBills) {
            String billNo = retailOrderBill.getBillNo();
            List<RetailOrderGoodsDetail> details = detailMap.get(billNo);
            List<StockInfo> stockInfos = createStockInfos(token, retailOrderBill, details.toArray(new RetailOrderGoodsDetail[]{}));
            //释放库存锁并扣减库存
            result = stockService.decreStockAndLockStock(token, stockInfos);
            if (result.hasError()) {
                //throw new ServiceResultErrorException(result);
                return result;
            }
        }
        return result;
    }

    /**
     * 批量回传修改订单状态与发货状态
     * @param token
     * @param ids
     * @return
     */
    private int batchCheckOutByDeliveryOrderCode(String token,List<String> ids){
        Session session = LoginUtil.checkUserLogin(token);
        Map<String,Object> param = new HashedMap<>();
        param.put("distributionState","8");
        param.put("completeBy",session.userName);
        param.put("completeDate",new Date());
        param.put("status",BillStatus.COMPLETE);
        param.put("ids",ids);
        return retailOrderBillMapper.batchCheckOutByDeliveryOrderCode(param);
    }

    /**
     * 批量发出修改订单发货状态
     * @param token
     * @param ids
     * @return
     */
    private int batchSendOutByDeliveryOrderCode(String token,List<String> ids){
        Map<String,Object> param = new HashedMap<>();
        param.put("distributionState","7");
        param.put("ids",ids);
        return retailOrderBillMapper.batchSendOutByDeliveryOrderCode(param);
    }

    /**
     * 重试到“实物缺货停止刷新时间”将当日所有发货状态为“”实物缺货“”’状态订单返回给ECIP缺货，并终止零售订单和原始零售订单
     * @param token
     * @param param
     * @return
     */
    @Override
    public String pushBillsToewms(String token, String param) {
        String flag = "failure";
        String code = "";
        String message = "";
        E3Selector selector = new E3Selector();
        List<String> disStatus = new ArrayList<>();
        disStatus.add("6");
        //发货状态：0-初始 1-完成预分配 2-预分配缺货 3-补货中 4-通知配货 5-拣货中 6-实物缺货 7-已发出 8-已出库 9-订单终止
        selector.addFilterField(new E3FilterField("distributionState",disStatus));
        selector.addFilterField(new E3FilterField("status",BillStatus.INITIAL));
        RetailOrderBill[] retailOrderBills = queryPage(token, selector, -1, -1);
        //来源单据编号(原始零售订单编码)
        List<String> sourceBillNos = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (RetailOrderBill retailOrderBill : retailOrderBills) {
            ids.add(retailOrderBill.getId());
            sourceBillNos.add(retailOrderBill.getOriginalRetailBillNo());
        }
        if(CollectionUtil.isEmpty(ids)){
            flag = "success";
            code = "0000";
            message= "成功";
            return resultUtil(flag,code,message);
        }

        //查询设定时间与当前时间对比
        boolean allowed = false;
        try {
            allowed = validateTimeWithSystem(token,"SWQHTZSXSJ");
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        if(!allowed){
            //批量中止零售订单
            int i = batchTerm(token, ids);
            //List<String> sourceBillNos = Stream.of(retailOrderBills).map(RetailOrderBill::getSourceBillNo).collect(Collectors.toList());
            //原始零售订单编号 sourceBillNo
            long termStatus = Long.parseLong(BillStatus.TERM);
            int j = originalRetailOrderBillService.updateOrderStatus(token,termStatus, sourceBillNos);
            if(i>0 && j>0){
                flag = "success";
                code = "0000";
                message= "成功";
            }else{
                code = "0001";
                message= "更改失败！";
            }
        }else{
            flag = "success";
            code = "0000";
            message= "成功";
        }
        return resultUtil(flag,code,message);
    }

    /**
     * 对比实物缺货时间与当前时间
     * @param token
     * @param key
     */
    private boolean validateTimeWithSystem(String token,String key) throws ParseException {
        SystemParameter systemParameter = consumerSystemParameterService.findByIkey(token, key);
        String parametrivalue = systemParameter.getParametrivalue();
        if(StringUtils.isBlank(parametrivalue)){
            parametrivalue = "17:00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //当前时间
        Date nowDate = sdf.parse(sdf.format(new Date()));
        //系统时间
        Date sysDate = sdf.parse(parametrivalue);

        return nowDate.getTime() < sysDate.getTime();
    }

    @Override
    public ServiceResult createOrders(String token, List<RetailOrderBill> orderBillList) {
        ServiceResult result = new ServiceResult();
        List<RetailOrderBill> newBillList = new ArrayList<>(orderBillList.size());
        List<RetailOrderGoodsDetail> newDetailList= new ArrayList<>(orderBillList.size());
        List<RetailOrderDistributionInfo> distributionInfoList= new ArrayList<>(orderBillList.size());
        for(RetailOrderBill orderBill : orderBillList){
            Long billId = idService.nextId();
            orderBill.setId(billId);
            orderBill.setCreateDate(new Date());
            orderBill.setCreateBy("liling-job");
            newBillList.add(orderBill);
            for(RetailOrderGoodsDetail detail : orderBill.getGoodsDetail()){
                detail.setId(idService.nextId());
                detail.setRetailOrderBillId(billId);
                newDetailList.add(detail);
            }
            for(RetailOrderDistributionInfo distributionInfo : orderBill.getAdvancedDistributionInfos()){
                if(distributionInfo.getId()==null){
                    distributionInfo.setId(idService.nextId());
                }
                distributionInfo.setRetailOrderBillId(billId);
                distributionInfoList.add(distributionInfo);
            }
        }
        retailOrderBillMapper.insertSelective(newBillList);
        detailMapper.insertSelective(newDetailList);
        distributionInfoMapper.insertSelective(distributionInfoList);
        return result;
    }

    /**
     * 封装ewms发货，出库的响应体
     * @param flag
     * @param code
     * @param message
     * @return
     */
    private String resultUtil(String flag, String code, String message){
        JSONObject obj = new JSONObject();
        JSONObject response = new JSONObject();
        response.put("flag",flag);
        response.put("code",code);
        response.put("message",message);
        obj.put("response",response);
        return obj.toJSONString();
    }

    /**
     * 批量中止
     * @param token
     * @param ids
     */
    private int batchTerm(String token,List<Long> ids){
        Session session = LoginUtil.checkUserLogin(token);
        Map<String,Object> param = new HashedMap<>();
        param.put("termStatus",BillStatus.TERM);
        param.put("distributionState","9");
        param.put("abolishBy", session.userName);
        param.put("abolishDate", new Date());
        param.put("ids",ids);
        return retailOrderBillMapper.batchTerm(param);
    }

    @Override
    public ServiceResult ewmsStockout(String token, String billNo) {
        // 查询零售订单
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("billNo", billNo));
        RetailOrderBill[] retailOrderBills = queryPage(token, selector, -1, -1);
        if (ArrayUtils.isEmpty(retailOrderBills)) {
            return new ServiceResult();
        }
        RetailOrderBill retailOrderBill = retailOrderBills[0];
        // 订单状态更新为未确认
        retailOrderBill.setStatus(0);
        // 发货状态更新为实物缺货
        retailOrderBill.setDistributionState("6");
        retailOrderBillMapper.updateByPrimaryKeySelective(Arrays.asList(retailOrderBills));

        // 释放锁定库存
        ServiceResult serviceResult = releaseBillLockStock(token, billNo);

        return serviceResult;
    }

    @Override
    public ServiceResult modifyDistributionState(String token, RetailOrderBill[] retailOrderBills) {
        ServiceResult result = new ServiceResult();
        if (ArrayUtils.isEmpty(retailOrderBills)) {
            return result;
        }

        for (RetailOrderBill retailOrderBill : retailOrderBills) {
            retailOrderBillMapper.updateDistributionStateById(retailOrderBill);
        }

        return result;
    }

}
