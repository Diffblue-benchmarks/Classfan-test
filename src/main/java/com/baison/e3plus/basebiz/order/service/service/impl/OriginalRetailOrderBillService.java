package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.api.common.OrderStatus;
import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrdGoodsDetail;
import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrdGoodsDetailService;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrderBillService;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrderDistributionInfoService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OriginalRetailOrdGoodsDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OriginalRetailOrderBillMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OriginalRetailOrderDistributionInfoMapper;
import com.baison.e3plus.basebiz.order.service.feignclient.support.*;
import com.baison.e3plus.biz.support.api.publicrecord.model.platform.Platform;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.user.model.UserInfo;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.bscore.utils.UUIDUtil;
import com.baison.e3plus.common.cncore.common.BillStatus;
import com.baison.e3plus.common.cncore.common.Status;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.session.Session;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OriginalRetailOrderBillService implements IOriginalRetailOrderBillService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OriginalRetailOrderBillMapper billMapper;

    @Autowired
    private OriginalRetailOrdGoodsDetailMapper detailMapper;

    @Autowired
    private OriginalRetailOrderDistributionInfoMapper distributionInfoMapper;

    @Autowired
    private IOriginalRetailOrdGoodsDetailService originalRetailOrdGoodsDetailService;

    @Autowired
    private IOriginalRetailOrderDistributionInfoService  originalRetailOrderDistributionInfoService;

    @Autowired
    public ConsumerAuthObjectService authObjectService;

    @Autowired
    public ConsumerDataAuthService dataAuthService;

    @Autowired
    private ConsumerCodeRuleService codeRuleService;

    @Autowired
    private ConsumerAdvancedShopService shopService;

    @Autowired
    private ConsumerPlatformService platformService;

    @Override
    public List<OriginalRetailOrderBill> queryOrderBill(String token){
        Map<String,Object> billQueryParamMap = new HashMap<>();
        billQueryParamMap.put("orderStatus", Status.INITIAL);
        List<OriginalRetailOrderBill> list = billMapper.queryPage(billQueryParamMap);
        queryDetails(list);
        queryDistributionInfos(list);
        return list;
    }

    private void queryDetails(List<OriginalRetailOrderBill> billList){
        List<String> billIdList = LinqUtil.select(billList, s -> s.getId());
        List<OriginalRetailOrdGoodsDetail> detailList = detailMapper.selectByBillId(billIdList);
        Map<String,List<OriginalRetailOrdGoodsDetail>> map = new HashMap<>();
        for (OriginalRetailOrdGoodsDetail detail : detailList){
            String tempId = detail.getOriginalRetailOrderId();
            List<OriginalRetailOrdGoodsDetail> tempList = map.get(tempId);
            if(CollectionUtils.isNotEmpty(tempList)){
                tempList.add(detail);
            } else {
                tempList = new ArrayList<>();
                tempList.add(detail);
                map.put(tempId,tempList);
            }
        }
        for(OriginalRetailOrderBill bill : billList){
            bill.setOriginalRetailOrdGoodsDetailList(map.get(bill.getId()));
        }
    }

    private void queryDistributionInfos(List<OriginalRetailOrderBill> billList){
        List<String> billNoList = LinqUtil.select(billList, s -> s.getOriginalOrderBillNo());
        List<OriginalRetailOrderDistributionInfo> distributionInfos = distributionInfoMapper.selectByBillNo(billNoList);
        Map<String,List<OriginalRetailOrderDistributionInfo>> map = new HashMap<>();
        for (OriginalRetailOrderDistributionInfo distributionInfo : distributionInfos){
            String tempNo = distributionInfo.getOriginalOrderBillNo();
            List<OriginalRetailOrderDistributionInfo> tempList = map.get(tempNo);
            if(CollectionUtils.isNotEmpty(tempList)){
                tempList.add(distributionInfo);
            } else {
                tempList = new ArrayList<>();
                tempList.add(distributionInfo);
                map.put(tempNo,tempList);
            }
        }
        for(OriginalRetailOrderBill bill : billList){
            bill.setDistributionInfos(map.get(bill.getOriginalOrderBillNo()));
        }
    }

    @Override
    public OriginalRetailOrderBill[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {

        Map<String, Object> args = getQueryPageSelectorConditionMap(token,selector);

        if (pageSize > 0 && pageIndex >= 0) {
            int stratRow = pageIndex * pageSize;

            args.put("stratRow", stratRow);
            args.put("endRow", pageSize);
        }

        List<OriginalRetailOrderBill> datas = null;
        try {
            datas = billMapper.queryPage(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return datas.toArray(new OriginalRetailOrderBill[datas.size()]);
    }


    @Override
    public List<OriginalRetailOrderBill> queryObject(String token, E3Selector selector) {
        ServiceResult result = new ServiceResult();
        List<OriginalRetailOrderBill> datas = null;
        OriginalRetailOrderBill[] originalRetailOrderBills = this.queryPage(token, selector, -1, -1);
        if(originalRetailOrderBills == null){
            return null;
        }
        datas = Arrays.asList(originalRetailOrderBills);
        List<String> billNos = new ArrayList<>();
        for (OriginalRetailOrderBill originalRetailOrderBill: datas) {
            billNos.add(originalRetailOrderBill.getOriginalOrderBillNo());
        }
        OriginalRetailOrdGoodsDetail[] originalRetailOrdGoodsDetails =originalRetailOrdGoodsDetailService.queryGoodsDetailByBillNo(token, billNos);
        OriginalRetailOrderDistributionInfo[] originalRetailOrderDistributionInfos = originalRetailOrderDistributionInfoService.queryGoodsDetailByBillNo(token, billNos);

        for (OriginalRetailOrderBill originalRetailOrderBill: datas) {
            for(OriginalRetailOrdGoodsDetail originalRetailOrdGoodsDetail : originalRetailOrdGoodsDetails){
                if(originalRetailOrderBill.getOriginalOrderBillNo().equals(originalRetailOrdGoodsDetail.getOriginalOrderBillNo())){
                    List<OriginalRetailOrdGoodsDetail> originalRetailOrdGoodsDetailList = new ArrayList<>();
                    originalRetailOrdGoodsDetailList.add(originalRetailOrdGoodsDetail);
                    originalRetailOrderBill.setOriginalRetailOrdGoodsDetailList(originalRetailOrdGoodsDetailList);
                }
            }

            for(OriginalRetailOrderDistributionInfo distributionInfo : originalRetailOrderDistributionInfos){
                if(originalRetailOrderBill.getOriginalOrderBillNo().equals(distributionInfo.getOriginalOrderBillNo())){
                    List<OriginalRetailOrderDistributionInfo> distributionInfos = new ArrayList<>();
                    distributionInfos.add(distributionInfo);
                    originalRetailOrderBill.setDistributionInfos(distributionInfos);
                }
            }
        }
        return datas;
    }

    @Override
    public long getListCount(String token, E3Selector selector) {

        Map<String, Object> args = getQueryPageSelectorConditionMap(token,selector);

        return billMapper.getListCount(args);
    }

    /**
     * 更新订单
     *
     * @param orderStatus
     * @param billNos
     * @return
     */
    @Override
    public int updateOrderStatus(String token, Long orderStatus, List<String> billNos){
        Session session = LoginUtil.checkUserLogin(token);
        Map<String, Object> map = new HashMap<>();
        map.put("orderStatus", orderStatus);
        map.put("billNos", billNos);
        Date date = new Date();
        String status = String.valueOf(orderStatus);
        if(BillStatus.SUBMIT.equals(status)){
            map.put("transferOrderBy", Session.ADMINTOKEN);
            map.put("transferOrderDate", date);
        }else if(BillStatus.COMPLETE.equals(status)){
            map.put("completeBy", session.userName);
            map.put("completeDate", date);
        }else if(BillStatus.TERM.equals(status)){
            map.put("abolishBy", session.userName);
            map.put("abolishDate", date);
        }
        int result = billMapper.updateOrderStatus(map);
        return result;
    }


    @Override
    @Transactional
    public ServiceResult create(String token, List<OriginalRetailOrderBill> beans) {

        try {
            ServiceResult result = new ServiceResult();
            Session session = LoginUtil.checkUserLogin(token);

            if (beans == null || beans.size() == 0) {
                result.addErrorObject(null, "", "");
                return result;
            }

            //校验商店与平台
            verifyShopAndPlatform(token, result, beans);
            if(result.hasError()){
                return result;
            }

            // 判断传入的单据编号是否存在重复，有则提示
            // 本身单据编号重复集合
            List<String> billNoList = new ArrayList<>();
            // 与数据库单据编号重复集合
            List<String> billNoListInDb = new ArrayList<>();

            String[] sourceBillNos = beans.stream().map(t -> t.getSourceBillNo()).toArray(String[]::new);

            E3Selector selector = new E3Selector();
            selector.addSelectFields("sourceBillNo");
            selector.addFilterField(new E3FilterField("sourceBillNo", "in", sourceBillNos, E3FilterField.ANDOperator));
            OriginalRetailOrderBill[] retailOrderBills = queryPage(token, selector, -1, -1);
            for (int i = 0; i < beans.size(); i++) {
                for (int j = i + 1; j < beans.size(); j++) {
                    if (beans.get(i).getSourceBillNo() != null && beans.get(i).getSourceBillNo().equals(beans.get(j).getSourceBillNo())) {
                        billNoList.add(beans.get(i).getSourceBillNo());
                        break;
                    }
                }
                for (OriginalRetailOrderBill retailOrderBill : retailOrderBills) {
                        if (beans.get(i).getSourceBillNo() != null && beans.get(i).getSourceBillNo().equals(retailOrderBill.getSourceBillNo())) {
                            if(retailOrderBill.getOrderStatus() == 9){
                                continue;
                            }
                            billNoListInDb.add(beans.get(i).getSourceBillNo());
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
            for (OriginalRetailOrderBill bill : beans) {
                String billNo = bill.getOriginalOrderBillNo();
                if (StringUtil.isEmptyOrNull(billNo)) {
                    billNo = codeRuleService.generateCode(token, "OriginalRetailOrderBill", bill);
                    bill.setOriginalOrderBillNo(billNo);
                }
                bill.setId(UUIDUtil.generate());
                bill.setMakingOrderBy(session.userName);
                bill.setMakingOrderDate(new Date());
                bill.setOrderStatus(OrderStatus.INITIAL);
            }

            // 给明细的单据编号赋值
            setOriginalRetailOrderBillNo(result, token, beans);
            if(result.hasError()){
                return result;
            }

            // 保存明细
            saveDetails(token, beans);

            billMapper.insertSelective(beans);
            result.getSuccessObject().addAll(beans);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    private void verifyShopAndPlatform(String token,ServiceResult result, List<OriginalRetailOrderBill> beans){
        //校验店铺是否符合
        Set<String> shopCodes = new HashSet<>();
        for (OriginalRetailOrderBill originalRetailOrderBill: beans) {
            if(originalRetailOrderBill.getShopCode() == null){
                result.addErrorObject(originalRetailOrderBill.getSourceBillNo(),"","订单存在shopId为空值");
                break;
            }
            shopCodes.add(originalRetailOrderBill.getShopCode());
        }
        E3Selector selectorShop = new E3Selector();
        selectorShop.addSelectFields("shopId");
        selectorShop.addSelectFields("code");
        selectorShop.addSelectFields("name");
        selectorShop.addFilterField(new E3FilterField("code", shopCodes));
        selectorShop.addFilterField(new E3FilterField("status", Status.ENABLE));
        Shop[] shops = shopService.queryPageShop(token, selectorShop, -1, -1);
        if(shopCodes.size() != shops.length){
            result.addErrorObject("","","有shopCode未适配到商店档案");
        }

        //校验平台是否符合
        Set<String> platformCodes = new HashSet<>();
        for (OriginalRetailOrderBill originalRetailOrderBill: beans) {
            if(originalRetailOrderBill.getPlatfromCode() == null){
                result.addErrorObject(originalRetailOrderBill.getSourceBillNo(),"","订单存在PlatformId为空值");
                break;
            }
            platformCodes.add(originalRetailOrderBill.getPlatfromCode());
        }
        E3Selector selectorPlatform = new E3Selector();
        selectorPlatform.addSelectFields("platformId");
        selectorPlatform.addSelectFields("name");
        selectorPlatform.addSelectFields("code");
        selectorPlatform.addFilterField(new E3FilterField("code", platformCodes));
        selectorPlatform.addFilterField(new E3FilterField("status", Status.ENABLE));
        Platform[] platforms = platformService.queryPageObject(token, selectorPlatform, -1, -1);
        if(platformCodes.size() != platforms.length){
            result.addErrorObject("","","有platformCode未适配到商店档案");
        }
        //把商店id,name,平牌id，name存入原始零售订单
        for (OriginalRetailOrderBill originalRetailOrderBill: beans){
            for(Shop shop : shops){
                if(originalRetailOrderBill.getShopCode().equals(shop.getCode())){
                    originalRetailOrderBill.setShopId(shop.getShopId());
                    originalRetailOrderBill.setShopName(shop.getName());
                }
            }

            for(Platform platform1 : platforms){
                if(originalRetailOrderBill.getPlatfromCode().equals(platform1.getCode())){
                    originalRetailOrderBill.setPlatformId(platform1.getPlatformId());
                    originalRetailOrderBill.setPlatfromName(platform1.getName());
                }
            }
        }
    }

    /**
     * 明细添加主表信息
     * @param token
     * @param dataArray
     */
    private void setOriginalRetailOrderBillNo(ServiceResult result, String token, List<OriginalRetailOrderBill> dataArray) {

        for (OriginalRetailOrderBill saveBean : dataArray) {
            String billNo = saveBean.getOriginalOrderBillNo();
            String retailBillId = saveBean.getId();
            String tradeNo = saveBean.getTradeNo();
            List<OriginalRetailOrdGoodsDetail> goodsDetails = saveBean.getOriginalRetailOrdGoodsDetailList();

            if(goodsDetails.size() > 1){
                result.addErrorObject(saveBean, "","该订单明细" + tradeNo + "不能有多个");
                break;
            }

            if (goodsDetails != null && goodsDetails.size() > 0) {
                for (OriginalRetailOrdGoodsDetail goodsDetail : goodsDetails) {
                    goodsDetail.setOriginalOrderBillNo(billNo);
                    goodsDetail.setOriginalRetailOrderId(retailBillId);
                    goodsDetail.setTradeNo(tradeNo);
                }
            }

            List<OriginalRetailOrderDistributionInfo> distributionInfos = saveBean.getDistributionInfos();
            if(distributionInfos.size() > 1){
                result.addErrorObject(saveBean, "","该订单收件人信息" + tradeNo + "不能有多个");
                break;
            }

            if (distributionInfos != null && distributionInfos.size() > 0) {
                for (OriginalRetailOrderDistributionInfo distributionInfo : distributionInfos) {
                    distributionInfo.setOriginalOrderBillNo(billNo);
                    distributionInfo.setOriginalRetailOrderId(retailBillId);
                    distributionInfo.setTradeNo(tradeNo);
                }
            }

        }
    }

    /**
     * 保存明细
     *
     * @param token
     * @param dataArray
     */
    private void saveDetails(String token, List<OriginalRetailOrderBill> dataArray) {

        // 商品明细
        List<OriginalRetailOrdGoodsDetail> goodsDetails = new ArrayList<>();

        // 配送信息明细
        List<OriginalRetailOrderDistributionInfo> distributeInfoList = new ArrayList<>();
        for (OriginalRetailOrderBill bill : dataArray) {
            if (bill.getOriginalRetailOrdGoodsDetailList() != null) {
                for (OriginalRetailOrdGoodsDetail detail : bill.getOriginalRetailOrdGoodsDetailList()) {
                    goodsDetails.add(detail);
                }
            }

            if (bill.getDistributionInfos() != null) {
                for (OriginalRetailOrderDistributionInfo detail : bill.getDistributionInfos()) {
                    distributeInfoList.add(detail);
                }
            }
        }

        if (goodsDetails != null && goodsDetails.size() > 0) {
            originalRetailOrdGoodsDetailService.create(token, goodsDetails);
        }

        if (distributeInfoList.size() > 0) {
            originalRetailOrderDistributionInfoService.create(token, distributeInfoList);
        }
    }

    /**
     * 查询条件封装
     *
     * @param token
     * @param selector
     * @return
     */
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
                if (field.getFieldName().toLowerCase().indexOf("date") > 0) {
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
                } else {
                    args.put(field.getFieldName(), field.getOriginValue());
                }
            }
        }

//        Session session = LoginUtil.checkUserLogin(token);
//        if (!isSuperAdmin(session.userRole)) {
//            // 处理权限
//            verifyPermission(token, session, args);
//        }
        return args;
    }

    /**
     * 验证权限
     */
//    private void verifyPermission(String token,Session session,Map<String, Object> args){
//        // 处理权限
//        E3Selector selector = new E3Selector();
//        selector.addFilterField(new E3FilterField("code", "RetailOrderBill"));
//        AuthObject[] authObjects = authObjectService.queryObject(token, selector);
//        if (authObjects != null && authObjects.length >= 0) {
//            List<Condition> authConditions = dataAuthService.getFilterCondition(session, "",
//                    authObjects[0].getAuthObjectId(), "view");
//            if (authConditions.size() > 0) {
//                addAuthCondition(args, authConditions);
//            }
//        }
//    }

//    private void addAuthCondition(Map<String, Object> args, List<Condition> authConditions) {
//
//        for (Condition condition : authConditions) {
//            List<Condition> childConditions = condition.getChildConditions();
//            if (childConditions == null || childConditions.size() == 0) {
//                String fieldName = condition.getExpression();
//
//                if (fieldName.indexOf(".") != -1) {
//                    fieldName = fieldName.split("\\.")[1];
//                }
//
//                String conditionValue = condition.getValue();
//                String operator = condition.getOperation();
//
//                List<Object> authValues = new ArrayList<>();
//                if (Condition.IN.equals(operator)) {
//                    String[] splitData = conditionValue.split(Condition.IN_SPLIT);
//                    for (String data : splitData) {
//                        authValues.add(data);
//                    }
//                } else {
//                    authValues.add(conditionValue);
//                }
//
//                Object value = args.get(fieldName);
//                List<Object> values = null;
//                if (value == null) {
//                    values = authValues;
//                } else {
//                    List<Object> existValues = new ArrayList<>();
//                    if (value instanceof List) {
//                        for (Object v : (List<Object>) value) {
//                            existValues.add(v.toString());
//                        }
//                    } else if (value.getClass().isArray()) {
//                        for (Object v : (Object[]) value) {
//                            existValues.add(v.toString());
//                        }
//                    } else {
//                        existValues.add(value.toString());
//                    }
//                    // 取交集
//                    values = LinqUtil.intersect(authValues, existValues);
//                    if (values.size() == 0) {
//                        //如果没有交集，则不可能查出数据，将当前条件字段给出一个不存在的值
//                        values.add(0);
//                    }
//                }
//                args.put(fieldName, values);
//            } else {
//                addAuthCondition(args, childConditions);
//            }
//        }
//    }

//    private boolean isSuperAdmin(String userRole) {
//        return UserInfo.SUPER_ADMIN.equals(userRole);
//    }

}
