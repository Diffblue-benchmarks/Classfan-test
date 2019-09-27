package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.api.model.*;
import com.baison.e3plus.basebiz.order.api.service.*;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NonReviewStrategyService implements INonReviewStrategyService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IRetailOrderBillService retailOrderBillService;
    //@Autowired
   // private IRetailOrderGoodsDetailService goodsDetailService;
    @Autowired
    private IOrderExemptReviewStrategyService orderExemptReviewStrategyService;
    @Autowired
    private IOrderExemptReviewStrategyDetailService orderExemptReviewStrategyDetailService;

    @Override
    public NonReviewStrategy nonReviewStrategy(String token, RetailOrderBill[] retailOrderBills) {
        //ServiceResult result = new ServiceResult();
        if (retailOrderBills == null && retailOrderBills.length == 0){
           // result.addErrorObject("","","input data is null");
            return null;
        }
        NonReviewStrategy nonReviewStrategy = new NonReviewStrategy();
        //启动订单免审&通知配货策略总开关
        boolean switchFlag = false;
        //订单总金额设置的范围
        boolean amountTotalFlag = false;
        Double amountTotalLimt1 = 0d;
        Double amountTotalLimt2 = 0d;
        //订单商品总数量上限
        boolean qtyLimtFlag = false;
        Double qtyLimt = 100d;
        //设置延迟免审商店
        boolean delayFlag = false;
        Integer minutes = 0;
        //“指定SKU不免审 ”是否被勾选
        boolean skuFlag = false;
        //指定SKU不免审
       // List<String> skuList = new ArrayList<>();
        Set<String> existedSku = new HashSet<>();

        //卖家（sellerMessage）是否有留言
        boolean sellMsgFlag = false;
        //消费者（buyerMessage）是否有留言
        boolean buyerMsgFlag = false;

        List<RetailOrderBill> successList = new ArrayList<>();
        List<RetailOrderBill> billsList = Arrays.asList(retailOrderBills);

        String[] codes = {"001","002","003","004","006","007","008"};
        OrderExemptReviewStrategy[] orderExemptReviewStrategies = orderExemptReviewStrategyService.findObjectByCode(token,codes);
        Map<Long, String> shopIdMap = new HashMap<>();
        for (OrderExemptReviewStrategy oers : orderExemptReviewStrategies) {
            String code ="";
            if (oers.getCode() != null){
                code = oers.getCode();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("===code="+code);
            }
            switch (code) {
                //启动订单免审&通知配货策略
                case "001" :
                    //1 开启 0 关闭

                    if ("1".equals(oers.getVal())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("===== 启动订单免审,总开关：ON ===");
                        }
                        switchFlag = true;
                    }
                    break;
                //免审订单金额范围
                case "002" :
                    if ("1".equals(oers.getVal())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("===== 启动订单免审订单金额：ON ===");
                        }
                        amountTotalFlag = true;
                        amountTotalLimt1 = Double.parseDouble(oers.getVal2());
                        amountTotalLimt2 = Double.parseDouble(oers.getVal3());
                    }
                    break;
                //免审订单商品总数量上限（包含设置的上限数量
                case "003" :
                    if ("1".equals(oers.getVal())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("===== 启动免审订单商品总数量上限：ON ===");
                        }
                        qtyLimtFlag = true;
                        qtyLimt = Double.parseDouble(oers.getVal2());
                    }
                    break;
                //订单延迟免审
                case "004" :
                    if ("1".equals(oers.getVal())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("===== 启动订单延迟免审：ON ===");
                        }
                        delayFlag = true;
                        minutes = Integer.parseInt(oers.getVal2());
                        //获取配置的免审商店id
                        E3Selector selector = new E3Selector();
                        selector.addFilterField(new E3FilterField("type", "3"));
                        OrderExemptReviewStrategyDetail[] orderExemptReviewStrategyDetails = orderExemptReviewStrategyDetailService.queryObject(token,selector);
                        if (ArrayUtils.isNotEmpty(orderExemptReviewStrategyDetails)) {
                            for (OrderExemptReviewStrategyDetail oersd : orderExemptReviewStrategyDetails) {
                                if (oersd.getShopId() != null && oersd.getShopName() != null ) {
                                    shopIdMap.put(oersd.getShopId(), oersd.getShopName());
                                }
                            }
                        }
                    }
                    break;
                //指定SKU不免审
                case "006" :
                    if ("1".equals(oers.getVal())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("===== 启动指定SKU不免审：ON ===");
                        }
                        skuFlag = true;
                        //获取不免审的SKU
                        E3Selector e3Selector = new E3Selector();
                        e3Selector.addFilterField(new E3FilterField("type", "1"));
                        OrderExemptReviewStrategyDetail[] orderExemptReviewStrategyDetails = orderExemptReviewStrategyDetailService.queryObject(token,e3Selector);
                        if (ArrayUtils.isNotEmpty(orderExemptReviewStrategyDetails)) {
                            for (OrderExemptReviewStrategyDetail oersd : orderExemptReviewStrategyDetails) {
                                if (oersd.getSingleProductCode() != null ) {
                                    existedSku.add(oersd.getSingleProductCode());
                                }
                            }
                        }
                    }
                    break;
                //卖家有留言则订单不免审
                case "007" :
                    if ("1".equals(oers.getVal())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("===== 启动卖家有留言则订单不免审：ON ===");
                        }
                        sellMsgFlag = true;
                    }
                    break;
                //消费者有留言则订单不免审
                case "008" :
                    if ("1".equals(oers.getVal())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("===== 启动消费者有留言则订单不免审：ON ===");
                        }
                        buyerMsgFlag = true;
                    }
                    break;
                default:
                    break;
            }
        }
        lableA:
        for (RetailOrderBill retailOrderBill : billsList) {
            //判断总开关是否开启
            boolean successFlag = false;
            if (!switchFlag){
                nonReviewStrategy.addFailureList(retailOrderBill);
                logger.info("订单：" + retailOrderBill.getBillNo() + " -> 总开关没开启，不进行自动免审");
                continue;
            }else {
                successFlag = true;
            }
            //判断订单总金额在设置的范围内
            if (amountTotalFlag) {
                if (retailOrderBill.getTotalTaxPrice() == null) {
                    nonReviewStrategy.addFailureList(retailOrderBill);
                    logger.info("订单：" + retailOrderBill.getBillNo() + " -> 订单总金额为空，不进行自动免审");
                    continue;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("==当前订单总金额:"+retailOrderBill.getTotalTaxPrice()+",下限金额："+amountTotalLimt1+",上限总金额："+amountTotalLimt2);
                }
                if (retailOrderBill.getTotalTaxPrice() < amountTotalLimt1 || retailOrderBill.getTotalTaxPrice() > amountTotalLimt2) {
                    nonReviewStrategy.addFailureList(retailOrderBill);
                    logger.info("订单：" + retailOrderBill.getBillNo() + " -> 订单总金额大于设置的范围，不进行自动免审");
                    continue;
                }else {
                    successFlag = true;
                }
            }
            //判断订单商品总数量是否大于设置的上限
            if (qtyLimtFlag) {
                if (retailOrderBill.getQty() == null) {
                    nonReviewStrategy.addFailureList(retailOrderBill);
                    logger.info("订单：" + retailOrderBill.getBillNo() + " -> 订单商品总数量为空，不进行自动免审");
                    continue;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("==当前订单总数量："+retailOrderBill.getQty()+",设置的上限："+qtyLimt);
                }
                if (retailOrderBill.getQty() > qtyLimt) {
                    nonReviewStrategy.addFailureList(retailOrderBill);
                    logger.info("订单：" + retailOrderBill.getBillNo() + " -> 订单商品总数量大于设置的范围，不进行自动免审");
                    continue;
                }else {
                    successFlag = true;
                }
            }

            if (skuFlag){
                //获取订单的SKU
                //RetailOrderGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token,result,retailOrderBill);
                //if (result.hasError()) {
                //    return result;
                // }

                if (retailOrderBill.getGoodsDetail() == null || retailOrderBill.getGoodsDetail().size() == 0) {
                    nonReviewStrategy.addFailureList(retailOrderBill);
                    logger.info("订单：" + retailOrderBill.getBillNo() + " -> 商品明细没有数据，不进行自动免审");
                    continue;
                }
                List<RetailOrderGoodsDetail> goodsDetails = retailOrderBill.getGoodsDetail();
                for (RetailOrderGoodsDetail goodsDetail : goodsDetails) {
                    if (goodsDetail.getSingleProduct().getCode() == null ) {
                        nonReviewStrategy.addFailureList(retailOrderBill);
                        logger.info("订单：" + retailOrderBill.getBillNo() + " -> 订单SKU为空，不自动免审");
                        continue lableA;
                    }
                    String sku = goodsDetail.getSingleProduct().getCode();
                    //判断订单的SKU是否包含在列表中，包含则订单不免审
                    if (logger.isDebugEnabled()) {
                        logger.debug("当前订单的SKU："+sku);
                    }
                    if (existedSku.contains(sku)){
                        nonReviewStrategy.addFailureList(retailOrderBill);
                        logger.info("订单：" + retailOrderBill.getBillNo() + " -> 指定SKU不免审");
                        continue lableA;
                    }else {
                        successFlag = true;
                    }
                }

            }
            //卖家（sellerMessage）是否有留言 - 设置页面是否勾选
            if (sellMsgFlag) {
                //判断卖家（sellerMessage）是否有留言，有则不免审
                if (retailOrderBill.getSellersMsg() != null ){
                    nonReviewStrategy.addFailureList(retailOrderBill);
                    logger.info("订单：" + retailOrderBill.getBillNo() + " -> 卖家有留言，不自动免审");
                    continue;
                }else {
                    successFlag = true;
                }
            }
            //消费者（buyerMessage）是否有留言 - 设置页面是否勾选
            if (buyerMsgFlag) {
                //判断消费者（buyerMessage）是否有留言，有则不免审
                if (retailOrderBill.getBuyersMsg() != null) {
                    nonReviewStrategy.addFailureList(retailOrderBill);
                    logger.info("订单：" + retailOrderBill.getBillNo() + " -> 买家有留言，不自动免审");
                    continue;
                }else {
                    successFlag = true;
                }
            }
            //判断订单是否延迟免审，根据商店id判断订单商店是否在延迟免审商店里
            if (delayFlag) {
                if (retailOrderBill.getShopId() != null ) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("当前订单商店id:"+retailOrderBill.getShopId());
                    }
                if (shopIdMap.containsKey(retailOrderBill.getShopId())) {
                    //转单时间+延迟时间 小于当前时间，还不能进行免审
                    Calendar ca = Calendar.getInstance();
                    if (retailOrderBill.getBillDate() == null ) {
                        nonReviewStrategy.addFailureList(retailOrderBill);
                        logger.info("当前订单转单时间为空,订单不免审" );
                        continue ;
                    }
                    Date billDate = retailOrderBill.getBillDate();
                    ca.setTime(billDate);
                    ca.add(Calendar.MINUTE, minutes);
                    Date auditTime = ca.getTime();
                    Date nowTime = Calendar.getInstance().getTime();
                    if (logger.isDebugEnabled()) {
                        logger.debug("当前订单转单时间：" + billDate + ",延迟免审时间：" + auditTime + ",当前时间：" + nowTime);
                    }
                    int res = nowTime.compareTo(auditTime);
                    if (res < 0) {
                        retailOrderBill.setExemptAuditDelayTime(auditTime);
                        nonReviewStrategy.addDelayList(retailOrderBill);
                        logger.info("订单：" + retailOrderBill.getBillNo() + " -> 延迟免审时间还没到，不进行自动免审");
                        continue;
                    } else {
                        retailOrderBill.setExemptAuditDelayTime(auditTime);
                        successFlag = true;
                    }
                }
                }else {
                    successFlag = true;
                }
            }
            if (successFlag) {
                successList.add(retailOrderBill);
            }
        }

        nonReviewStrategy.setSuccessList(successList);
        return nonReviewStrategy;
    }
//    private RetailOrderGoodsDetail[] findAndCheckGoodsDetails(String token, ServiceResult result,
//                                                              RetailOrderBill bill) {
//        E3Selector selector = new E3Selector();
//        selector.addFilterField(new E3FilterField("retailOrderBillId", bill.getId()));
//        if (bill.getBillNo() != null) {
//            selector.addFilterField(new E3FilterField("billNo", bill.getBillNo()));
//        }
//
//        RetailOrderGoodsDetail[] goodsDetails = goodsDetailService.queryPage(token, selector, -1, -1);
//        if (goodsDetails == null || goodsDetails.length == 0) {
//            result.addErrorObject("", AdvancedOrderErrorCode.STOCK_012, ResourceUtils.get("e3.errorCode.stock.99012"));
//            return null;
//        }
//
//        for (RetailOrderGoodsDetail retailOrderGoodsDetail : goodsDetails) {
//            if (retailOrderGoodsDetail.getSingleProductId() == null
//                    || (bill.getWhareatypeId() == null && bill.getShopId() == null)) {
//                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_015);
//            }
//            if (retailOrderGoodsDetail.getQty() == null || retailOrderGoodsDetail.getQty().equals(0)) {
//                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_048,
//                        ResourceUtils.get("e3.errorCode.stock.99048", retailOrderGoodsDetail.getGoodsId()));
//            }
//        }
//
//        return goodsDetails;
//    }
}
