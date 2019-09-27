package com.baison.e3plus.basebiz.order.service.service.adapter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterPriorityDetail;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedExpressStrategy;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedStrategyPlatformGoodsDetail;
import com.baison.e3plus.basebiz.order.api.model.adapter.OmsRetailOrderBill;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedExpressStrategyService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedAdapterPriorityDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedAdapterShopDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedAdapterWareHouseDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedExpressStrategyMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedStrategyPlatformGoodsDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedExpressStrategyExample;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedDeliveryTypeService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedShopService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSystemOperateLogService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSystemParameterService;
import com.baison.e3plus.basebiz.order.service.redis.calculate.AdvancedExpressStrategyManage;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.delivery.AdvancedDeliveryAreaDetail;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.delivery.AdvancedDeliveryType;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.shop.AdvancedShop;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedDeliveryTypeDetail;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedWareHouse;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.common.OperateType;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.operatelog.model.SystemOperateLog;
import com.baison.e3plus.biz.support.api.parameter.SystemParameter;
import com.baison.e3plus.biz.support.api.publicrecord.model.channel.Channel;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.bscore.utils.CollectionUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.common.Status;
import com.baison.e3plus.common.cncore.log.BusinessTraceUtils;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.session.Session;
import com.github.pagehelper.PageHelper;

/**
 * 快递适配策略
 *
 * @author tongyong.geng
 */
@Service
public class AdvancedExpressStrategyService implements IAdvancedExpressStrategyService {
    @Autowired
    private ConsumerAdvancedWareHouseService iAdvancedWareHouseService;
    @Autowired
    private ConsumerAdvancedDeliveryTypeService iAdvancedDeliveryTypeService;
    @Autowired
    private ConsumerSystemParameterService iSystemParameterService;
    @Autowired
    private ConsumerAdvancedShopService iAdvancedShopService;
    @Autowired
    private ConsumerSystemOperateLogService iSystemOperateLogService;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private AdvancedExpressStrategyMapper advancedExpressStrategyMapper;
    @Autowired
    private AdvancedAdapterPriorityDetailMapper advancedAdapterPriorityDetailMapper;
    @Autowired
    private AdvancedAdapterShopDetailMapper advancedAdapterShopDetailMapper;
    @Autowired
    private AdvancedAdapterWareHouseDetailMapper advancedAdapterWareHouseDetailMapper;
    @Autowired
    private AdvancedStrategyPlatformGoodsDetailMapper advancedStrategyPlatformGoodsDetailMapper;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String JINGDONG = "jingdong";

    /**
     * 快递策略缓存标记
     */
    private static final String CACHE_FLAG = "OrderExpressStrategyVersion";

    /**
     * 快递策略缓存
     */
    private static final Map<Long, AdvancedExpressStrategy> expressStrategyCache = new ConcurrentHashMap<>();

    /**
     * warehouse wareHouse-id 缓存
     */
    private Map<Long, AdvancedWareHouse> wareHouseIdMapCache = new ConcurrentHashMap<>();

    /**
     * 配送方式缓存
     */
    private Map<Long, AdvancedDeliveryType> deliveryTypeIdMapCache = new ConcurrentHashMap<>();

    /**
     * 商店+仓库->策略缓存
     */
    private Map<String, List<Long>> shopWareHouseExpressStrategyMapCache = new ConcurrentHashMap<>();
    /**
     * 仓库->策略缓存
     */
    private Map<String, List<Long>> wareHouseExpressStrategyMapCache = new ConcurrentHashMap<>();
    /**
     * 商店->策略缓存
     */
    private Map<String, List<Long>> shopExpressStrategyMapCache = new ConcurrentHashMap<>();

    /**
     * 缓存标记
     */
    private boolean enableCache = true;

    @Override
    @Transactional
    public ServiceResult createObject(String token, AdvancedExpressStrategy[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        BatchIdVo batchIdVo = idService.batchId(beans.length);
        long startId = batchIdVo.getStartId();
        for (AdvancedExpressStrategy bean : beans) {
            Integer priority = bean.getPriority();
            if (priority == null) {
                result.addErrorObject(bean, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_019,
                        ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_019));
                return result;
            }
            if (priority < 0) {
                result.addErrorObject(bean, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_018,
                        ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_018));
                return result;
            }
            bean.setId(startId);
            startId += batchIdVo.getStep();
        }

        advancedExpressStrategyMapper.insertBatch(beans);

        doSystemOperateLog(token, beans, OperateType.CREATE);
        result.addSuccessObject(beans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    private void doSystemOperateLog(String token, AdvancedExpressStrategy[] bills, String operateType) {
        try {
            List<SystemOperateLog> logs = new ArrayList<>();
            for (AdvancedExpressStrategy bill : bills) {
                SystemOperateLog log = new SystemOperateLog();
                log.setBillType(BillType.AdvancedExpressStrategy);
                log.setOperateType(operateType);
                log.setOperateTime(new Date());
                log.setOperatorId(LoginUtil.checkUserLogin(token).userId);
                log.setPId(bill.getId().toString());
                if (OperateType.CREATE.equals(operateType)) {
                    log.setOperateContent("创建 " + bill.getName());
                } else if (OperateType.ENABLE.equals(operateType)) {
                    log.setOperateContent("启用 " + bill.getName());
                } else if (OperateType.DISABLE.equals(operateType)) {
                    log.setOperateContent("停用 " + bill.getName());
                } else if (OperateType.MODIFY.equals(operateType)) {
                    StringBuffer modifyContent = new StringBuffer();
                    if (bill.getName() != null) {
                        modifyContent.append(" 修改 名称:" + bill.getName() + ";");
                    }
                    if (bill.getCode() != null) {
                        modifyContent.append(" 修改 代码:" + bill.getCode() + ";");
                    }
                    if (bill.getDeliveryType() != null) {
                        modifyContent.append(" 修改 默认配送方式:" + bill.getDeliveryType().getName() + ";");
                    }
                    if (bill.getWareHouse() != null) {
                        modifyContent.append(" 修改 适用仓库:" + bill.getWareHouse().getName() + ";");
                    }
                    if (bill.getChannel() != null) {
                        modifyContent.append(" 修改 所属组织:" + bill.getChannel().getName() + ";");
                    }
                    if (bill.getPriority() != null) {
                        modifyContent.append(" 修改 优先级:" + bill.getPriority() + ";");
                    }
                    log.setOperateContent(modifyContent.toString());
                }
                if (StringUtil.isNotEmptyOrNull(log.getOperateContent())) {
                    logs.add(log);
                }
            }

            if (logs.size() > 0) {
                iSystemOperateLogService.createObject(token, logs.toArray(new SystemOperateLog[logs.size()]));
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Override
    @Transactional
    public ServiceResult modifyObject(String token, AdvancedExpressStrategy[] beans) {

        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        List<Long> idsArr = new ArrayList<Long>();
        for (AdvancedExpressStrategy bean : beans) {
            idsArr.add(bean.getId());
            if (bean.getPriority() != null && bean.getPriority() < 0) {
                result.addErrorObject(bean, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_018,
                        ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_018));
                return result;
            }
        }

        //排序，防止死锁
        AdvancedExpressStrategy[] sortedBens = Arrays.stream(beans).sorted(Comparator.comparing(t -> t.getId())).toArray(AdvancedExpressStrategy[]::new);

        AdvancedExpressStrategy[] modifyAdvancedExpressStrategys = new AdvancedExpressStrategy[0];
        try {
            //防止启用停用，详情字段未修改
            modifyAdvancedExpressStrategys = getModifyAdvancedExpressStrategys(idsArr, sortedBens);
        } catch (Exception e) {
            modifyAdvancedExpressStrategys = sortedBens;
        }

        advancedExpressStrategyMapper.updateByPrimaryKeySelectiveBatch(sortedBens);

        doSystemOperateLog(token, modifyAdvancedExpressStrategys, OperateType.MODIFY);
        result.addSuccessObject(sortedBens);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    @Transactional
    public ServiceResult removeObject(String token, Object[] ids) {
        ServiceResult result = new ServiceResult();
        if (ids == null || ids.length == 0) {
            return result;
        }

        AdvancedExpressStrategy[] datas = this.findObjectById(token, ids);

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        advancedExpressStrategyMapper.deleteByPrimaryKeyBatch(idArr);
        advancedAdapterPriorityDetailMapper.deleteByExpressStrategyIdBatch(idArr);
        advancedAdapterShopDetailMapper.deleteByExpressStrategyIdBatch(idArr);
        advancedAdapterWareHouseDetailMapper.deleteByExpressStrategyIdBatch(idArr);
        advancedStrategyPlatformGoodsDetailMapper.deleteByExpressStrategyIdBatch(idArr);

        doSystemOperateLog(token, datas, OperateType.REMOVE);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    public AdvancedExpressStrategy[] findObjectById(String token, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);

        List<AdvancedExpressStrategy> datas = advancedExpressStrategyMapper.selectByPrimaryKeyBatch(idArr);

        if (datas == null || datas.size() == 0) {
            return null;
        }
        return datas.toArray(new AdvancedExpressStrategy[datas.size()]);
    }

    @Override
    public AdvancedExpressStrategy[] queryObject(String token, E3Selector selector) {
        return new AdvancedExpressStrategy[0];
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        AdvancedExpressStrategyExample example = new AdvancedExpressStrategyExample();
        //添加搜索条件
        addFilterCriteria(selector, example);
        return advancedExpressStrategyMapper.countByExample(example);
    }

    @Override
    public AdvancedExpressStrategy[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {

        AdvancedExpressStrategyExample example = new AdvancedExpressStrategyExample();

        //添加搜索条件
        addFilterCriteria(selector, example);

        //分页
        if (pageIndex >= 0 && pageIndex >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize, false);
        }

        List<AdvancedExpressStrategy> datas = advancedExpressStrategyMapper.selectByExample(example);
        if (datas == null || datas.size() == 0) {
            return null;
        }

        return datas.toArray(new AdvancedExpressStrategy[datas.size()]);
    }

    @Override
    public Map<Long, List<Long>> queryStrategyIdByWareHouseId(String token, Set<Long> wareHouseIdSet) {
        // key为仓库id，value为策略id
        Map<Long, List<Long>> map = new HashMap<>();

        if (!CollectionUtil.isEmpty(wareHouseIdSet)) {
            List<AdvancedExpressStrategy> expressStrategies = advancedExpressStrategyMapper.queryByWareHouseId(wareHouseIdSet.toArray(new Long[0]));
            if (!CollectionUtil.isEmpty(expressStrategies)) {
                for (AdvancedExpressStrategy expressStrategy : expressStrategies) {
                    Long warehouseId = expressStrategy.getWarehouseId();
                    if (map.containsKey(warehouseId)) {
                        map.get(warehouseId).add(expressStrategy.getId());
                    } else {
                        List<Long> list = new ArrayList<>();
                        list.add(expressStrategy.getId());
                        map.put(warehouseId, list);
                    }
                }
            }
        }

        return map;
    }

    private void addFilterCriteria(E3Selector selector, AdvancedExpressStrategyExample example) {
        AdvancedExpressStrategyExample.Criteria criteria = example.createCriteria();
        E3FilterField codeFilter = selector.getFilterFieldByFieldName("code");
        if (codeFilter != null) {
            criteria.andCodeLike(String.valueOf(codeFilter.getValue()));
        }
        E3FilterField nameFilter = selector.getFilterFieldByFieldName("name");
        if (nameFilter != null) {
            criteria.andNameLike(String.valueOf(nameFilter.getValue()));
        }
        E3FilterField priorityFilter = selector.getFilterFieldByFieldName("priority");
        if (priorityFilter != null) {
            criteria.andPriorityEqualTo(Integer.valueOf(String.valueOf(priorityFilter.getValue())));
        }
        E3FilterField channelFilter = selector.getFilterFieldByFieldName("channelId");
        if (channelFilter != null&&channelFilter.getValue()!=null) {
            criteria.andChannelIdEqualTo(Long.valueOf(String.valueOf(channelFilter.getValue())));
        }
        
        E3FilterField groupFilter = selector.getFilterFieldByFieldName("FILTER-GROUP");
        if(groupFilter != null) {
        	List<E3FilterField> e3FilterFieldList = ((E3FilterGroup)groupFilter).getFilterFields();
        	for (E3FilterField e3FilterField : e3FilterFieldList) {
        		if("channelId".equalsIgnoreCase(e3FilterField.getFieldName())) {
        			List<Long> channelIds = (List<Long>)e3FilterField.getValue();
        			criteria.andChannelIdIn(channelIds);
        		}
        	}
        }
        
        E3FilterField statusFilter = selector.getFilterFieldByFieldName("status");
        if (statusFilter != null) {
            Object[] value = (Object[]) statusFilter.getValue();
            if (value != null && value.length > 0) {
                List<Byte> byteList = Arrays.stream(value).map(t -> Byte.valueOf(String.valueOf(t))).collect(Collectors.toList());
                criteria.andStatusIn(byteList);
            }
        }
    }

    @Override
    @Transactional
    public ServiceResult enable(String token, Object[] ids) {
        ServiceResult result = new ServiceResult();
        if (ids == null || ids.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }
        Session session = LoginUtil.checkUserLogin(token);
        AdvancedExpressStrategy[] datas = findObjectById(token, ids);

        if (null != datas) {
            for (AdvancedExpressStrategy advancedExpressStrategy : datas) {
                advancedExpressStrategy.setEnableBy(session.userName);
                advancedExpressStrategy.setEnableTime(new Date());
                advancedExpressStrategy.setStatus(Byte.valueOf(Status.ENABLE));
            }
        }

        this.modifyObject(token, datas);

        doSystemOperateLog(token, datas, OperateType.ENABLE);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    @Transactional
    public ServiceResult disable(String token, Object[] ids) {
        ServiceResult result = new ServiceResult();
        if (ids == null || ids.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }

        Session session = LoginUtil.checkUserLogin(token);
        AdvancedExpressStrategy[] datas = findObjectById(token, ids);

        if (null != datas) {
            for (AdvancedExpressStrategy advancedExpressStrategy : datas) {
                advancedExpressStrategy.setEnableBy(session.userName);
                advancedExpressStrategy.setEnableTime(new Date());
                advancedExpressStrategy.setStatus(Byte.valueOf(Status.DISABLE));
            }
        }

        this.modifyObject(token, datas);

        doSystemOperateLog(token, datas, OperateType.DISABLE);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;

    }

    @Override
    public ServiceResult filterExpress(String token, OmsRetailOrderBill bill) {
        ServiceResult result = new ServiceResult();
        
        StringBuilder process = new StringBuilder();
        process.append("单据[" + bill.getBillNo() + "]开始适配快递策略:\n");
        try {
            //记录运维日志
            BusinessTraceUtils.writeLog(
                    BusinessTraceUtils.NODE_SO, bill.getBillNo(),
                    BusinessTraceUtils.NODE_ES, null,
                    "单据[" + bill.getBillNo() + "]开始适配快递策略");
            //正在处理中的单据不允许重复处理
            if (!AdvancedExpressStrategyManage.markOrderStatusInitail(bill.getBillNo())) {
            	process.append("单据正在适配快递策略.");
                result.addErrorObjectNotThrow(null, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_017, ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_017, bill.getBillNo()));
                return result;
            }

            //参数校验
            result = validateReqParam(bill);
            if (result.hasError()) {
                return result;
            }

            //清空缓存
            cleanCache(process);

            //预售订单处理
            if ("1".equals(bill.getOrderType())) {
                SystemParameter ysBtn = iSystemParameterService.findByIkey(token, "TQFYSDD");
                //开启预售发货参数
                if (ysBtn != null && "1".equals(ysBtn.getParametrivalue())) {
                	process.append("开启预售发货参数,走预售逻辑,获取预发可达的配送方式.\n");
                    result = presellOrderHandle(token, bill);
                } else {
                    //未开启预售发货参数则走适配快递策略
                	process.append("未开启预售发货参数则走适配快递策略:\n");
                    result = adaptExpressStrategy(token, bill, process);
                }
            }

            //非预售订单则走适配快递策略
            if (!"1".equals(bill.getOrderType())) {
            	process.append("非预售订单则走适配快递策略:\n");
                result = adaptExpressStrategy(token, bill, process);
            }
        } catch (Exception e) {
            log.error("处理快递策略出现异常：" + e.getMessage());
        } finally {
            //删除单据处理标记
            AdvancedExpressStrategyManage.deleteOrderStatus(bill.getBillNo());
        }

        if (!result.hasError()) {
            AdvancedDeliveryType deliveryType = null;
            if (result.getSuccessObject().size() > 0) {
                deliveryType = (AdvancedDeliveryType) result.getSuccessObject().get(0);
            }
            if (deliveryType == null) {
                result.addErrorObject(null, AdvancedOrderErrorCode.NO_DELIVERY_TYPE, ResourceUtils.get(AdvancedOrderErrorCode.NO_DELIVERY_TYPE));
                return result;
            }
            result = new ServiceResult();
            AdvancedDeliveryType returnDeliveryType = new AdvancedDeliveryType();
            returnDeliveryType.setCode(deliveryType.getCode());
            returnDeliveryType.setName(deliveryType.getName());
            returnDeliveryType.setId(deliveryType.getId());
            process.append("处理快递策略[" + bill.getBillNo() + "]结束.");
            if(!StringUtil.isEmptyOrNull(bill.getIsDebug())) {
            	returnDeliveryType.setRemark(process.toString());
            }
            result.addSuccessObject(returnDeliveryType);
            //记录运维日志
            BusinessTraceUtils.writeLog(
                    BusinessTraceUtils.NODE_SO, bill.getBillNo(),
                    BusinessTraceUtils.NODE_ES, deliveryType.getCode(),
                    process.toString());
            return result;
        }

        return result;
    }

    /***
     * 预售订单处理
     * @return
     */
    private ServiceResult presellOrderHandle(String token, OmsRetailOrderBill bill) {
        ServiceResult result = new ServiceResult();

        // 是否指定预发货快递
        E3Selector selector = new E3Selector();
        selector.addSelectFields("id");
        selector.addSelectFields("code");
        selector.addSelectFields("name");
        selector.addSelectFields("deliveryAreaDetails.areaId");
        selector.addSelectFields("deliveryAreaDetails.accessible");
        selector.addSelectFields("preconsignmentExpressPri");

        selector.addFilterField(new E3FilterField("preconsignmentExpress", "1"));//预发货快递
        selector.addFilterField(new E3FilterField("status", "1"));
        AdvancedDeliveryType[] deliveryTypes = iAdvancedDeliveryTypeService.queryObject(token, selector);
        // 未指定预发快递
        if (deliveryTypes == null || deliveryTypes.length == 0) {
            result.addErrorObjectNotThrow(null, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_015, ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_015));
            return result;
        }

        if (enableCache) {
            for (AdvancedDeliveryType deliveryType : deliveryTypes) {
                deliveryTypeIdMapCache.put(deliveryType.getId(), deliveryType);
            }
        }

        // 配送方式按照优先排序
        List<AdvancedDeliveryType> deliveryTypeList = Arrays.asList(deliveryTypes);
        deliveryTypeList.forEach(s -> {
            if (s.getPreconsignmentExpressPri() == null) {
                s.setPreconsignmentExpressPri(Integer.MAX_VALUE);
            }
        });
        List<AdvancedDeliveryType> tmpDeliveryTypes = deliveryTypeList.stream().sorted(Comparator.comparing(AdvancedDeliveryType::getPreconsignmentExpressPri)).collect(Collectors.toList());

        // 配送方式ID
        List<Long> tmpDeliveryTypeIds = LinqUtil.select(tmpDeliveryTypes, s -> s.getId());

        // 判断快递方式可达不可达
        List<AdvancedDeliveryType> deliveryType = findAccessibleArea(token, bill, tmpDeliveryTypeIds);

        if (deliveryType.isEmpty()) {
            result.addErrorObjectNotThrow(null, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_016, ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_016));
            return result;
        }

        result.addSuccessObject(deliveryType.get(0));

        return result;
    }

    /***
     * 适配快递策略
     * @return
     */
    private ServiceResult adaptExpressStrategy(String token, OmsRetailOrderBill bill, StringBuilder process) {
        ServiceResult result = new ServiceResult();
        // 仓库+商店维度获取快递策略
        List<AdvancedExpressStrategy> expressStrategys = getExpressStrategy(bill.getShopId(), bill.getWareHouseId());

        if (expressStrategys == null) {
            // 根据仓库获取快递策略
            expressStrategys = getExpressStrategyByWarehouseId(bill.getWareHouseId());
        }

        if (expressStrategys == null) {
            // 获取商店指定的快递策略
            expressStrategys = getExpressStrategyByShopId(token, bill.getShopId());
        }

        AdvancedDeliveryType deliveryType = handleExpressStrategy(token, bill, expressStrategys, process);

        if (deliveryType != null) {
            result.addSuccessObject(deliveryType);
        } else {
            result.addErrorObjectNotThrow(deliveryType, AdvancedOrderErrorCode.NO_DELIVERY_TYPE, ResourceUtils.get(AdvancedOrderErrorCode.NO_DELIVERY_TYPE));
        }

        return result;
    }

    /***
     * 根据策略获取配送方式
     * @param token
     * @param bill
     * @param expressStrategys
     * @return
     */
    private AdvancedDeliveryType handleExpressStrategy(String token, OmsRetailOrderBill bill, List<AdvancedExpressStrategy> expressStrategys, StringBuilder process) {
        AdvancedDeliveryType deliveryType = null;
        //仓库指定的配送方式
        List<AdvancedDeliveryType> warehouseDeliveryTypes = findWarehouseDeliveryType(token, bill);
        //仓库是否指定了配送方式
        Boolean notDesignationDeliveryTypes = (warehouseDeliveryTypes == null || warehouseDeliveryTypes.size() == 0);
        //仓库指定的可达的配送方式
        List<AdvancedDeliveryType> accessibleDeliveryType = new ArrayList<>();
        if (warehouseDeliveryTypes != null && warehouseDeliveryTypes.size() > 0) {
            accessibleDeliveryType = findAccessibleArea(token, bill, warehouseDeliveryTypes.stream().map(t -> t.getId()).collect(Collectors.toList()));
        }

        // 如果没有找到快递策略，按仓库指定快递策略处理
        if (expressStrategys == null || expressStrategys.size() == 0) {
            process.append("没有找到快递策略\n");
            if (!notDesignationDeliveryTypes) { //仓库有指定配送方式
                if (accessibleDeliveryType != null && accessibleDeliveryType.size() > 0) {
                	process.append("仓库有指定配送方式,取指定配送方式中可达优先级最高的:" + warehouseDeliveryTypes.get(0).getName() + "\n");
                    return warehouseDeliveryTypes.get(0);
                } else {
                	process.append("仓库有指定配送方式,没有可达的\n");
                    return null;
                }
            } else { //仓库没有指定配送方式
                // 没有匹配到配送方式 获取系统默认配送方式
                AdvancedDeliveryType systemDefaultDeliveryType = getSystemDefaultDeliveryType(token);
                process.append("没有匹配到配送方式 获取系统默认配送方式:" + systemDefaultDeliveryType.getName() + "\n");
                return systemDefaultDeliveryType;
            }
        } else {
            // 将策略按照优先级进行排序
            expressStrategys.forEach(s -> {
                if (s.getPriority() == null) {
                    s.setPriority(Integer.MAX_VALUE);
                }
            });
            expressStrategys = expressStrategys.stream().sorted(Comparator.comparing(AdvancedExpressStrategy::getPriority)).collect(Collectors.toList());
            // 轮询策略找出配送方式
            process.append("存在"+expressStrategys.size()+"个策略,策略需要轮询:\n");
            for (AdvancedExpressStrategy expressStrategy : expressStrategys) {
            	process.append("\t策略:" + expressStrategy.getName() + "\n");
                List<Long> expressStrategyDeliveryTypes = findDeliveryTypeFromExpressStrategy(token, bill, expressStrategy);
                // 将策略的默认快递方式也放入区域适配优先级中校验可达不可达
                expressStrategyDeliveryTypes.add(expressStrategy.getDeliveryTypeId());
                // 取出配送方式中可达的配送方式
                List<AdvancedDeliveryType> deliveryTypeTmps = findAccessibleArea(token, bill, expressStrategyDeliveryTypes);
                // 不可达需要继续下一条策略
                if (deliveryTypeTmps.isEmpty()) {
                	process.append("\t策略:" + expressStrategy.getName() + " 中没有可达的配送方式，轮询下一条\n");
                    continue;
                }
                // 如果仓库档案没有配置指定配送方式 则所有配送方式可用 以策略上的优先级最高的配送方式为主
                if (notDesignationDeliveryTypes) {
                	process.append("\t仓库档案没有配置指定配送方式则所有配送方式可用，取策略:" + expressStrategy.getName() + " 中优先级最高的配送方式:" + deliveryTypeTmps.get(0).getName() +"\n");
                    return deliveryTypeTmps.get(0);
                }
                // 获取策略中在仓库档案支持的配送方式，没有支持的轮询下一条策略
                deliveryType = getWareHouseSupportDeliveryTypeFromExpressStrategy(accessibleDeliveryType, deliveryTypeTmps);
                if (deliveryType != null) {
                	process.append("\t获取策略中在仓库档案支持的配送方式，取策略:" + expressStrategy.getName() + " 中配送方式:" + deliveryType.getName() +"\n");
                    return deliveryType;
                }
            }

            // 仓库档案不支持策略中的配送方式时，取仓库档案优先级最高的配送方式
            process.append("轮询策略结束，策略中没有仓库档案支持的配送方式\\n");
            if (accessibleDeliveryType == null || accessibleDeliveryType.size() == 0) {
                if (notDesignationDeliveryTypes) {
                    AdvancedDeliveryType systemDefaultDeliveryType = getSystemDefaultDeliveryType(token);
                    process.append("仓库档案没有配送方式，取系统默认配送方式" + systemDefaultDeliveryType.getName() + "\n");
                    return systemDefaultDeliveryType;
                } else {
                    return null;
                }
            } else {
            	process.append("取仓库档案优先级最高的配送方式:" + accessibleDeliveryType.get(0).getName() + "\n");
                return accessibleDeliveryType.get(0);
            }

        }
    }

    /***
     * 获取策略中最优的配送方式
     * @param token
     * @param bill
     * @param expressStrategy
     * @return
     */
    private List<Long> findDeliveryTypeFromExpressStrategy(String token, OmsRetailOrderBill bill, AdvancedExpressStrategy expressStrategy) {
        List<Long> deliveryTypes = new ArrayList<>();
        // 校验平台商品是否在策略范围之内
        List<Long> palteFromGoodsDeliveryTypes = getPalteFromGoodsDeliveryTypes(bill.getPlatformGoodsIds(), expressStrategy.getPlatformGoodsDetails());
        if (palteFromGoodsDeliveryTypes != null && palteFromGoodsDeliveryTypes.size() > 0) {
            palteFromGoodsDeliveryTypes.stream().sorted(Comparator.comparing(t -> t));
            deliveryTypes.addAll(palteFromGoodsDeliveryTypes);
            return deliveryTypes;
        }

        // 匹配区域适配优先级
        List<AdvancedAdapterPriorityDetail> adapterPriorityDetails = expressStrategy.getAdapterPriorityDetails();
        if (adapterPriorityDetails == null || adapterPriorityDetails.size() == 0)

        {
            return deliveryTypes;
        }

        // 提取出收货地址所在省的优先级
        List<AdvancedAdapterPriorityDetail> adapterPriorityProvinceDetails = LinqUtil.where(adapterPriorityDetails, s -> s.getProvinceId().equals(bill.getProvinceId()));
        if (adapterPriorityProvinceDetails == null || adapterPriorityProvinceDetails.size() == 0)

        {
            return deliveryTypes;
        }
        // 按照优先级排序
        adapterPriorityProvinceDetails.forEach(s ->

        {
            if (s.getPriority() == null) {
                s.setPriority(Integer.MAX_VALUE);
            }
        });
        adapterPriorityProvinceDetails = adapterPriorityProvinceDetails.stream().

                sorted(Comparator.comparing(AdvancedAdapterPriorityDetail::getPriority)).

                collect(Collectors.toList());
        deliveryTypes = new ArrayList<>();
        for (AdvancedAdapterPriorityDetail detail : adapterPriorityProvinceDetails) {
            if (checkWeightAndAmount(detail.getValue(), bill)) {
                deliveryTypes.add(detail.getDeliveryTypeId());
            }
        }

        return deliveryTypes;
    }

    /**
     * 查找仓库指定的可达配送方式
     *
     * @param token
     * @param bill
     */
    private List<AdvancedDeliveryType> findWarehouseDeliveryType(String token, OmsRetailOrderBill bill) {
        List<AdvancedDeliveryType> deliveryTypes = null;

        AdvancedWareHouse advancedWareHouse = null;

        Long wareHouseId = bill.getWareHouseId();

        if (wareHouseIdMapCache.containsKey(wareHouseId) && enableCache) {
            advancedWareHouse = wareHouseIdMapCache.get(wareHouseId);
        } else {
            E3Selector wareHouseSelector = new E3Selector();
            wareHouseSelector.addFilterField(new E3FilterField("wareHouseId", wareHouseId));
            wareHouseSelector.addSelectFields("deliveryTypeDetails.priority");
            wareHouseSelector.addSelectFields("deliveryTypeDetails.deliveryTypeId");
            wareHouseSelector.addSelectFields("outWareHouseType");
            WareHouse[] advancedWareHouseArr = iAdvancedWareHouseService.queryObject(token, wareHouseSelector);
            if (null != advancedWareHouseArr && advancedWareHouseArr.length > 0) {
                advancedWareHouse = (AdvancedWareHouse) advancedWareHouseArr[0];
                wareHouseIdMapCache.put(wareHouseId, advancedWareHouse);
            }
        }

        List<Long> tmpDeliveryTypes = new ArrayList<>();
        // 京东仓需要从配送方式档案中获取
        if ("2".equals(advancedWareHouse.getOutWareHouseType())) {
            tmpDeliveryTypes = getJingDongDeliveryType(token, bill.getPlatformCode());
            if (tmpDeliveryTypes == null) {
                return deliveryTypes;
            }
        } else {
            // 非京东仓需要从配送方式明细中获取
            List<AdvancedDeliveryTypeDetail> deliveryTypeDetails = advancedWareHouse.getDeliveryTypeDetails();
            if (deliveryTypeDetails == null || deliveryTypeDetails.isEmpty()) {
                return deliveryTypes;
            }
            deliveryTypeDetails.forEach(s -> {
                if (s.getPriority() == null) {
                    s.setPriority(Integer.MAX_VALUE);
                }
            });
            List<AdvancedDeliveryTypeDetail> tmpDeliveryTypeDetails = deliveryTypeDetails.stream().sorted(Comparator.comparing(AdvancedDeliveryTypeDetail::getPriority)).collect(Collectors.toList());
            for (AdvancedDeliveryTypeDetail deliveryTypeDetail : tmpDeliveryTypeDetails) {
                tmpDeliveryTypes.add(deliveryTypeDetail.getDeliveryTypeId());
            }
        }

        // 返回仓库指定配送方式
        return setDeliveryTypeIdMapCache(token, tmpDeliveryTypes);
    }

    /***
     * 获取京东仓配送方式
     * 需要区分是否是京东平台订单
     * @param token
     * @param PlatformCode
     * @return
     */
    private List<Long> getJingDongDeliveryType(String token, String PlatformCode) {
        List<Long> tmpDeliveryTypes = null;
        E3Selector selector = new E3Selector();
        selector.addSelectFields("id");
        selector.addSelectFields("code");
        selector.addSelectFields("name");
        selector.addSelectFields("deliveryAreaDetails.areaId");
        selector.addSelectFields("deliveryAreaDetails.accessible");

        if (!JINGDONG.equals(PlatformCode)) {
            selector.addSelectFields("nonJDorderWareHousePri");
            selector.addFilterField(new E3FilterField("nonJDorderWareHouse", "1"));//是否非京东订单京东仓配送
        } else {
            selector.addSelectFields("isJDorderWareHousePri");
            selector.addFilterField(new E3FilterField("isJDorderWareHouse", "1"));//是否京东订单京东仓配送

        }
        selector.addFilterField(new E3FilterField("status", "1"));
        AdvancedDeliveryType[] deliveryTypeArr = iAdvancedDeliveryTypeService.queryObject(token, selector);
        if (deliveryTypeArr != null && deliveryTypeArr.length > 0) {
            tmpDeliveryTypes = new ArrayList<>();
            List<AdvancedDeliveryType> deliveryTypeListTmp = null;
            List<AdvancedDeliveryType> deliveryTypeDetails = Arrays.asList(deliveryTypeArr);
            if (!JINGDONG.equals(PlatformCode)) {
                deliveryTypeDetails.forEach(s -> {
                    if (s.getNonJDorderWareHousePri() == null) {
                        s.setNonJDorderWareHousePri(Integer.MAX_VALUE);
                    }
                });
                deliveryTypeListTmp = deliveryTypeDetails.stream().sorted(Comparator.comparing(AdvancedDeliveryType::getNonJDorderWareHousePri)).collect(Collectors.toList());
            } else {
                deliveryTypeDetails.forEach(s -> {
                    if (s.getIsJDorderWareHousePri() == null) {
                        s.setIsJDorderWareHousePri(Integer.MAX_VALUE);
                    }
                });
                deliveryTypeListTmp = deliveryTypeDetails.stream().sorted(Comparator.comparing(AdvancedDeliveryType::getIsJDorderWareHousePri)).collect(Collectors.toList());
            }
            for (AdvancedDeliveryType deliveryTypeTmp : deliveryTypeListTmp) {
                deliveryTypeIdMapCache.put(deliveryTypeTmp.getId(), deliveryTypeTmp);
                tmpDeliveryTypes.add(deliveryTypeTmp.getId());
            }
        }
        return tmpDeliveryTypes;
    }

    /***
     * 在配送方式中找出可达的配送方式
     * @param token
     * @param bill
     * @param deliveryType
     * @return
     */
    private List<AdvancedDeliveryType> findAccessibleArea(String token, OmsRetailOrderBill bill, List<Long> srcDeliveryTypes) {
        List<AdvancedDeliveryType> deliveryType = new ArrayList<>();
        if (srcDeliveryTypes == null) {
            return null;
        }

        List<AdvancedDeliveryType> deliveryTypes = setDeliveryTypeIdMapCache(token, srcDeliveryTypes);

        for (AdvancedDeliveryType deliveryTypeTmp : deliveryTypes) {
            List<AdvancedDeliveryAreaDetail> details = deliveryTypeTmp.getDeliveryAreaDetails();
            // 未配置可达、不可达地区时，默认全部可达
            if (details == null || details.size() == 0) {
                deliveryType.add(deliveryTypeTmp);
                continue;
            }
            if (details != null && details.size() > 0) {
                List<Long> notReachableList = new ArrayList<>();
                List<Long> reachableList = new ArrayList<>();
                details.forEach(adArea -> {
                    if (("0").equals(adArea.getAccessible())) {
                        notReachableList.add(adArea.getAreaId());
                    } else {
                        reachableList.add(adArea.getAreaId());
                    }
                });

                // 未配置不可达、配置可达
                if (notReachableList.isEmpty() && !reachableList.isEmpty()) {
                    // 可达
                    if (reachableList.contains(bill.getProvinceId()) || reachableList.contains(bill.getCityId()) || reachableList.contains(bill.getDistrictId())) {
                        deliveryType.add(deliveryTypeTmp);
                        continue;
                    }
                }

                // 配置不可达、未配置可达
                if (!notReachableList.isEmpty() && reachableList.isEmpty()) {
                    // 不可达
                    if (notReachableList.contains(bill.getProvinceId()) || notReachableList.contains(bill.getCityId()) || notReachableList.contains(bill.getDistrictId())) {
                        continue;
                    }
                    //可达
                    deliveryType.add(deliveryTypeTmp);
                }

                // 配置不可达、可达
                if (!notReachableList.isEmpty() && !reachableList.isEmpty()) {
                    // 不可达
                    if (notReachableList.contains(bill.getProvinceId()) || notReachableList.contains(bill.getCityId()) || notReachableList.contains(bill.getDistrictId())) {
                        continue;
                    }
                    // 可达
                    if (reachableList.contains(bill.getProvinceId()) || reachableList.contains(bill.getCityId()) || reachableList.contains(bill.getDistrictId())) {
                        deliveryType.add(deliveryTypeTmp);
                        continue;
                    }
                }
            }
        }

        return deliveryType;
    }

    /***
     * 缓存设置配送方式的值
     * @param token
     * @param srcDeliveryTypes
     * @return
     */
    private List<AdvancedDeliveryType> setDeliveryTypeIdMapCache(String token, List<Long> srcDeliveryTypes) {
        List<AdvancedDeliveryType> deliveryTypes = new ArrayList<>();

        List<Long> deliveryTypeIds = new ArrayList<>();

        E3Selector selector = new E3Selector();
        selector.addSelectFields("id");
        selector.addSelectFields("code");
        selector.addSelectFields("name");
        selector.addSelectFields("deliveryAreaDetails.areaId");
        selector.addSelectFields("deliveryAreaDetails.accessible");

        if (enableCache) {
            // 取出缓存中不存在的ID
            for (Long deliveryTypeTmp : srcDeliveryTypes) {
                if (!deliveryTypeIdMapCache.containsKey(deliveryTypeTmp)) {
                    deliveryTypeIds.add(deliveryTypeTmp);
                }
            }
            if (!deliveryTypeIds.isEmpty()) {
                // 数据库中查出数据赋予缓存
                selector.addFilterField(new E3FilterField("id", deliveryTypeIds));
                AdvancedDeliveryType[] tmpDeliveryTypes = iAdvancedDeliveryTypeService.queryObject(token, selector);
                for (AdvancedDeliveryType tmpDeliveryType : tmpDeliveryTypes) {
                    deliveryTypeIdMapCache.put(tmpDeliveryType.getId(), tmpDeliveryType);
                }
            }

            // 缓存中取值
            for (Long deliveryTypeTmp : srcDeliveryTypes) {
                deliveryTypes.add(deliveryTypeIdMapCache.get(deliveryTypeTmp));
            }

        } else {
            selector.addFilterField(new E3FilterField("id", srcDeliveryTypes));
            AdvancedDeliveryType[] tmpDeliveryTypes = iAdvancedDeliveryTypeService.queryObject(token, selector);
            for (Long deliveryTypeTmp : srcDeliveryTypes) {
                deliveryTypes.add(LinqUtil.first(Arrays.asList(tmpDeliveryTypes), s -> s.getId().equals(deliveryTypeTmp)));
            }
        }
        return deliveryTypes;
    }

    /***
     * 获取订单商品配送方式
     *
     * @param singleProductCodes
     * @param platformGoodsDetails
     * @return
     */
    private List<Long> getPalteFromGoodsDeliveryTypes(List<String> singleProductCodes, List<AdvancedStrategyPlatformGoodsDetail> platformGoodsDetails) {
        if (platformGoodsDetails == null || platformGoodsDetails.size() == 0) {
            return null;
        }

        if (singleProductCodes == null || singleProductCodes.size() == 0) {
            return null;
        }

        Map<String, AdvancedStrategyPlatformGoodsDetail> map = platformGoodsDetails.stream().collect(Collectors.toMap(t -> String.valueOf(t.getPlatformGoodsId()), t -> t, (v1, v2) -> v2));
        List<AdvancedStrategyPlatformGoodsDetail> platformGoodsDetailList = new ArrayList<>();
        for (String singleProduct : singleProductCodes) {
            AdvancedStrategyPlatformGoodsDetail detail = map.get(singleProduct);
            if (detail != null) {
                platformGoodsDetailList.add(detail);
            }
        }

        if (platformGoodsDetailList.size() == 0) {
            return null;
        }

        //返回按优先级排序后的配送方式ID
        return platformGoodsDetailList.stream().sorted(Comparator.comparing(AdvancedStrategyPlatformGoodsDetail::getPriority)).map(AdvancedStrategyPlatformGoodsDetail::getDeliveryTypeId).collect(Collectors.toList());
    }

    /***
     * 校验金额、重量
     * @param value
     * @param bill
     * @return
     */
    private boolean checkWeightAndAmount(String value, OmsRetailOrderBill bill) {
        String[] vals = value.split("\\|");
        if (vals.length == 1) {
            return true;
        }
        if (vals.length == 2) {
            if (vals[1].contains("W")) {
                return validate(bill.getTotalWeigh(), vals[1]);
            }
            if (vals[1].contains("J")) {
                return validate(bill.getAmountTotal(), vals[1]);
            }
        }

        if (vals.length == 3) {
            boolean w = true;
            boolean j = true;

            if (vals[1].toUpperCase().contains("W") && vals[1].toUpperCase().contains("J")) {
                w = validate(bill.getTotalWeigh(), vals[1]);
                j = validate(bill.getAmountTotal(), vals[2]);
            }

            if (vals[2].toUpperCase().contains("J") && vals[3].toUpperCase().contains("W")) {
                w = validate(bill.getTotalWeigh(), vals[2]);
                j = validate(bill.getAmountTotal(), vals[1]);
            }
            return w && j;
        }

        return true;
    }


    /***
     * 获取仓库支持的策略配送方式
     * @param wareHouseDeliveryTypes
     * @param expressStrategyDeliveryTypes
     * @return
     */
    private AdvancedDeliveryType getWareHouseSupportDeliveryTypeFromExpressStrategy(List<AdvancedDeliveryType> wareHouseDeliveryTypes, List<AdvancedDeliveryType> expressStrategyDeliveryTypes) {
        AdvancedDeliveryType deliveryType = null;
        List<Long> wareHouseDeliveryTypeIds = LinqUtil.select(wareHouseDeliveryTypes, s -> s.getId());
        for (AdvancedDeliveryType expressStrategyDeliveryType : expressStrategyDeliveryTypes) {
            if (wareHouseDeliveryTypeIds.contains(expressStrategyDeliveryType.getId())) {
                return expressStrategyDeliveryType;
            }
        }
        return deliveryType;
    }

    private AdvancedDeliveryType getSystemDefaultDeliveryType(String token) {
        SystemParameter systemParameter = iSystemParameterService.findByIkey(token, "MRPSFS");
        if (StringUtils.isNotEmpty(systemParameter.getParametrivalue())) {
            Long deliveryTypeId = Long.parseLong(systemParameter.getParametrivalue());
            if (deliveryTypeIdMapCache.containsKey(deliveryTypeId)) {
                return deliveryTypeIdMapCache.get(deliveryTypeId);
            } else {
                E3Selector selector = new E3Selector();
                selector.addSelectFields("id");
                selector.addSelectFields("code");
                selector.addSelectFields("name");
                selector.addSelectFields("deliveryAreaDetails.areaId");
                selector.addSelectFields("deliveryAreaDetails.accessible");
                selector.addFilterField(new E3FilterField("id", deliveryTypeId));
                AdvancedDeliveryType[] defaultDeliveryTypeList = iAdvancedDeliveryTypeService.queryObject(token, selector);
                if (null != defaultDeliveryTypeList && defaultDeliveryTypeList.length > 0) {
                    deliveryTypeIdMapCache.put(deliveryTypeId, defaultDeliveryTypeList[0]);
                    return defaultDeliveryTypeList[0];
                }
            }
        }
        return null;
    }

    private List<Long> getShopExpressStrategyIdByShopId(String token, Long shopId) {
        List<Long> expressStrategys = new ArrayList<>();
        //店铺的默认快递策略 ->  根据店铺ID 获取默认快递策略
        E3Selector shopSelector = new E3Selector();
        shopSelector.addFilterField(new E3FilterField("shopId", shopId));
        shopSelector.addSelectFields("expressStrategyId");
        Shop[] shops = iAdvancedShopService.queryPageShop(token, shopSelector, -1, -1);
        if (null != shops && shops.length > 0) {
            AdvancedShop advancedShop = (AdvancedShop) shops[0];
            if (advancedShop.getExpressStrategyId() != null) {
                expressStrategys.add(advancedShop.getExpressStrategyId());
            }
        }
        return expressStrategys;
    }

    /***
     * 获取商店指定的快递适配策略
     * @param token
     * @param shopId
     * @return
     */
    private List<AdvancedExpressStrategy> getExpressStrategyByShopId(String token, Long shopId) {

        List<Long> expressStrategys = null;
        if (enableCache) {
            String key = "" + shopId;
            if (shopExpressStrategyMapCache.containsKey(key)) {
                expressStrategys = shopExpressStrategyMapCache.get(key);
            } else {
                expressStrategys = getShopExpressStrategyIdByShopId(token, shopId);
                shopExpressStrategyMapCache.put(key, expressStrategys);
            }
        } else {
            expressStrategys = getShopExpressStrategyIdByShopId(token, shopId);
        }

        if (expressStrategys == null || expressStrategys.size() == 0) {
            return null;
        }

        return getExpressStrategyFromCache(expressStrategys.toArray(new Long[expressStrategys.size()]));
    }

    /**
     * 根据明细仓库ID获取策略
     *
     * @param wareHouseId
     * @return
     */
    private List<AdvancedExpressStrategy> getExpressStrategyByWarehouseId(Long wareHouseId) {

        List<Long> expressStrategys = null;
        if (enableCache) {
            String key = "" + wareHouseId;
            if (wareHouseExpressStrategyMapCache.containsKey(key)) {
                expressStrategys = wareHouseExpressStrategyMapCache.get(key);
            } else {
                expressStrategys = advancedExpressStrategyMapper.selectByWareHouseId(wareHouseId, 1);
                wareHouseExpressStrategyMapCache.put(key, expressStrategys);
            }
        } else {
            expressStrategys = advancedExpressStrategyMapper.selectByWareHouseId(wareHouseId, 1);
        }
        if (expressStrategys == null || expressStrategys.size() == 0) {
            return null;
        }

        return getExpressStrategyFromCache(expressStrategys.toArray(new Long[expressStrategys.size()]));
    }

    /***
     * 根据明细商店ID、仓库ID获取策略
     * @param shopId
     * @param wareHouseId
     * @return
     */
    private List<AdvancedExpressStrategy> getExpressStrategy(Long shopId, Long wareHouseId) {
        List<Long> expressStrategys = null;
        if (enableCache) {
            String key = shopId + "" + wareHouseId;
            if (shopWareHouseExpressStrategyMapCache.containsKey(key)) {
                expressStrategys = shopWareHouseExpressStrategyMapCache.get(key);
            } else {
                expressStrategys = advancedExpressStrategyMapper.selectByShopIdAndWareHouseId(shopId, wareHouseId, 1);
                shopWareHouseExpressStrategyMapCache.put(key, expressStrategys);
            }
        } else {
            expressStrategys = advancedExpressStrategyMapper.selectByShopIdAndWareHouseId(shopId, wareHouseId, 1);
        }
        if (expressStrategys == null || expressStrategys.size() == 0) {
            return null;
        }
        return getExpressStrategyFromCache(expressStrategys.toArray(new Long[expressStrategys.size()]));
    }

    /***
     * 缓存中获取快递适配策略
     * @param idArr
     * @return
     */
    private List<AdvancedExpressStrategy> getExpressStrategyFromCache(Long[] idArr) {
        List<AdvancedExpressStrategy> expressStrategys = null;
        Long localVersion = ISupportServiceCache.getLocalVersionCache(CACHE_FLAG);
        if (localVersion == null) {
            localVersion = 0l;
        }
        Long remoteVersion = ISupportServiceCache.getRemoteVersion(CACHE_FLAG);

        if (remoteVersion.equals(localVersion)) {
            expressStrategys = new ArrayList<>();
            List<Long> idArrTmp = null;
            for (Long id : idArr) {
                if (!expressStrategyCache.containsKey(id)) {
                    if (idArrTmp == null) {
                        idArrTmp = new ArrayList<>();
                    }
                    idArrTmp.add(id);
                } else {
                    expressStrategys.add(expressStrategyCache.get(id));
                }
            }
            if (idArrTmp != null) {
                List<AdvancedExpressStrategy> expressStrategyList = advancedExpressStrategyMapper.selectByPrimaryKeyAndStatusBatch(idArrTmp.toArray(new Long[idArrTmp.size()]), 1);
                if (expressStrategyList != null && !expressStrategyList.isEmpty()) {
                    for (AdvancedExpressStrategy expressStrategy : expressStrategyList) {
                        expressStrategyCache.put(expressStrategy.getId(), expressStrategy);
                    }
                    expressStrategys.addAll(expressStrategyList);
                }
            }
        } else {
            expressStrategys = advancedExpressStrategyMapper.selectByPrimaryKeyAndStatusBatch(idArr, 1);
            if (expressStrategys != null && !expressStrategys.isEmpty()) {
            	expressStrategyCache.clear();
                for (AdvancedExpressStrategy expressStrategy : expressStrategys) {
                    expressStrategyCache.put(expressStrategy.getId(), expressStrategy);
                }
            }
            ISupportServiceCache.setLocalVersionCache(remoteVersion, CACHE_FLAG);
        }
        return expressStrategys;
    }

    /***
     * 清空配送方式和仓库档案的缓存数据
     */
    private void cleanCache(StringBuilder process) {

        // 仓库档案
        if (!iAdvancedWareHouseService.checkVersion()) {
            process.append("仓库档案发生变动，快递策略清理仓库档案缓存\n");
            this.wareHouseIdMapCache.clear();
        }

        // 配送方式
        if (!iAdvancedDeliveryTypeService.checkVersion()) {
            process.append("配送方式发生变动，快递策略清理配送方式缓存\n");
            this.deliveryTypeIdMapCache.clear();
        }

        Long localVersion = ISupportServiceCache.getLocalVersionCache(CACHE_FLAG);
        if (localVersion == null) {
            localVersion = 0l;
        }
        Long remoteVersion = ISupportServiceCache.getRemoteVersion(CACHE_FLAG);
        
//		try {
//			InetAddress addr = InetAddress.getLocalHost();
//			String ip=addr.getHostAddress().toString();
//			process.append("ip地址:" + ip + "\n");
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}  
        
        // 商店/仓库->策略缓存
        process.append("本地缓存版本:" + localVersion + ", 远程缓存版本:" + remoteVersion + "\n");
        if (!remoteVersion.equals(localVersion)) {
            process.append("快递策略发生变动，清空快递策略清理商店/仓库->策略缓存缓存\n");
            this.shopWareHouseExpressStrategyMapCache.clear();
            this.wareHouseExpressStrategyMapCache.clear();
            this.shopExpressStrategyMapCache.clear();
        }

    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 验证请求参数
     *
     * @param bill
     * @return
     */
    private ServiceResult validateReqParam(OmsRetailOrderBill bill) {

        ServiceResult serviceResult = new ServiceResult();
        if (null == bill) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_001,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_001));
            return serviceResult;
        }
        if (null == bill.getAmountTotal()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_003,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_003));
            return serviceResult;
        }
        if (null == bill.getPlatformCode()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_005,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_005));
            return serviceResult;
        }
        if (null == bill.getPayState()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_004,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_004));
            return serviceResult;
        }
        if (null == bill.getShopId() || 0 == bill.getShopId()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_001,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_001));
            return serviceResult;
        }
//        if (null == bill.getTotalWeigh()) {
//            serviceResult.addErrorObjectNotThrowNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_007,
//                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_007));
//            return serviceResult;
//        }
        if (null == bill.getWareHouseId() || 0 == bill.getWareHouseId()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_006,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_006));
            return serviceResult;
        }
        if (null == bill.getProvinceId()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_008,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_008));
            return serviceResult;
        }
        if (null == bill.getDistrictId()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_009,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_009));
            return serviceResult;
        }
        if (null == bill.getCityId()) {
            serviceResult.addErrorObjectNotThrow(bill, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_010,
                    ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_010));
            return serviceResult;
        }

        return serviceResult;
    }

    /**
     * 验证参数是否在指定范围区间
     *
     * @param param 订单重量 或 金额
     * @param val
     * @return
     */
    private boolean validate(Double param, String val) {
        String[] vals = val.replace("W", "").replace("J", "").split("-");
        if (vals.length == 2) {
            Double lowVal = null;
            Double highVal = null;
            if (vals[0].matches("^[0-9]*$")) {
                lowVal = Double.valueOf(vals[0]);
            }
            if (vals[1].matches("^[0-9]*$")) {
                highVal = Double.valueOf(vals[1]);
            }
            if (null == lowVal && null != highVal) {
                return (Double.doubleToLongBits(param) <= Double.doubleToLongBits(highVal));
            } else if (null == highVal && null != lowVal) {
                return (Double.doubleToLongBits(param) >= Double.doubleToLongBits(lowVal));
            } else if (null != lowVal && null != highVal) {
                return (Double.doubleToLongBits(param) >= Double.doubleToLongBits(lowVal)) && (Double.doubleToLongBits(param) <= Double.doubleToLongBits(highVal));
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 对比两次操作，拿出有修改的数据
     */
    private AdvancedExpressStrategy[] getModifyAdvancedExpressStrategys(List<Long> idsArr, AdvancedExpressStrategy[] beans) {
        List<AdvancedExpressStrategy> billDbs = new ArrayList<AdvancedExpressStrategy>();
        if (idsArr != null && !idsArr.isEmpty()) {
            //从数据库取出原来的数据
            billDbs = advancedExpressStrategyMapper.selectByPrimaryKeyBatch(idsArr.toArray(new Long[idsArr.size()]));
        }
        Map<Long, AdvancedExpressStrategy> billDbsMap = new HashMap<Long, AdvancedExpressStrategy>();
        if (!billDbs.isEmpty()) {
            //将原来的数据转换成map，key为id
            billDbsMap = billDbs.stream().collect(Collectors.toMap(AdvancedExpressStrategy::getId, t -> t, (k1, k2) -> k2));
        }
        //定义一个装修改过数据的集合
        List<AdvancedExpressStrategy> modifyAdvancedExpressStrategys = new ArrayList<AdvancedExpressStrategy>();
        //遍历新的传入数据，并且用ID查找旧数据是否和新数据相等
        for (AdvancedExpressStrategy newBean : beans) {

            AdvancedExpressStrategy oldBean = billDbsMap.get(newBean.getId());
            if (oldBean == null) {
                continue;
            }
            AdvancedExpressStrategy modifyBean = new AdvancedExpressStrategy();
            modifyBean.setId(newBean.getId());
            if (!newBean.getName().equals(oldBean.getName())) {
                modifyBean.setName(newBean.getName());
            }
            if (!newBean.getCode().equals(oldBean.getCode())) {
                modifyBean.setCode(newBean.getCode());
            }

            if (!newBean.getDeliveryTypeId().equals(oldBean.getDeliveryTypeId())) {
                modifyBean.setDeliveryType(new AdvancedDeliveryType());
                modifyBean.getDeliveryType().setName(newBean.getDeliveryType().getName());
            }

            if (!newBean.getWarehouseId().equals(oldBean.getWarehouseId())) {
                modifyBean.setWareHouse(new WareHouse());
                modifyBean.getWareHouse().setName(newBean.getWareHouse().getName());
            }

            if (!newBean.getChannelId().equals(oldBean.getChannelId())) {
                modifyBean.setChannel(new Channel());
                modifyBean.getChannel().setName(newBean.getChannel().getName());
            }

            if (!newBean.getPriority().equals(oldBean.getPriority())) {
                modifyBean.setPriority(newBean.getPriority());
            }

            modifyAdvancedExpressStrategys.add(modifyBean);
        }
        return modifyAdvancedExpressStrategys.toArray(new AdvancedExpressStrategy[modifyAdvancedExpressStrategys.size()]);
    }


}
