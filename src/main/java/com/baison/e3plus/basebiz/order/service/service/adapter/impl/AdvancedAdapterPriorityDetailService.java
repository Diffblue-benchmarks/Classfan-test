package com.baison.e3plus.basebiz.order.service.service.adapter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdministrationAreaWithFashionService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedDeliveryTypeService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSystemOperateLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterPriorityDetail;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedAdapterPriorityDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedAdapterPriorityDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedAdapterPriorityDetailExample;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.delivery.AdvancedDeliveryType;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.common.OperateType;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.operatelog.model.SystemOperateLog;
import com.baison.e3plus.biz.support.api.publicrecord.model.administrationarea.AdministrationArea;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.utils.CollectionUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.common.ResultUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.restful.result.Result;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

/**
 * 区域适配优先级
 *
 * @author tongyong.geng
 */
@Service
public class AdvancedAdapterPriorityDetailService implements IAdvancedAdapterPriorityDetailService {

    private static Logger logger = LoggerFactory.getLogger(AdvancedAdapterPriorityDetailService.class);

    @Autowired
    private ConsumerSystemOperateLogService iSystemOperateLogService;
    @Autowired
    private AdvancedAdapterPriorityDetailMapper advancedAdapterPriorityDetailMapper;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private ConsumerAdvancedDeliveryTypeService deliveryTypeService;
    @Autowired
    private ConsumerAdministrationAreaWithFashionService areaService;

    /**
     * 快递策略缓存标记
     */
    private static final String CACHE_FLAG = "OrderExpressStrategyVersion";

    @Override
    @Transactional
    public ServiceResult createObject(String token, AdvancedAdapterPriorityDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return null;
        }

        List<String> errList = new ArrayList<>();
        for (AdvancedAdapterPriorityDetail bean : beans) {
            String value = bean.getValue();
            if (!value.matches("^[1-9][0-9]*(\\|[J][0-9]*-[0-9]*)?(\\|[W][0-9]*-[0-9]*)?")) {
                errList.add(value);
            }
        }

        if (errList.size() > 0) {
            result.addErrorObject(errList, AdvancedOrderErrorCode.ERROR_PRIORITY_VALUE, ResourceUtils.get(AdvancedOrderErrorCode.ERROR_PRIORITY_VALUE, String.join("、", errList)));
            return result;
        }

        Set<Long> strategyIdSet = Arrays.stream(beans).map(t -> t.getExpressStrategyId()).collect(Collectors.toSet());
        if (strategyIdSet != null && strategyIdSet.size() > 0) {
            advancedAdapterPriorityDetailMapper.deleteByExpressStrategyIdBatch(strategyIdSet.toArray(new Long[strategyIdSet.size()]));
        }

        BatchIdVo batchIdVo = idService.batchId(beans.length);
        long startId = batchIdVo.getStartId();
        Set<Long> deliveryTypeIdSet = new HashSet<>();
        Set<Long> areaIdSet = new HashSet<>();
        for (AdvancedAdapterPriorityDetail bean : beans) {
            bean.setId(startId);
            startId += batchIdVo.getStep();

            deliveryTypeIdSet.add(bean.getDeliveryTypeId());
            areaIdSet.add(bean.getProvinceId());
        }
        advancedAdapterPriorityDetailMapper.insertBatch(beans);
        if (!deliveryTypeIdSet.isEmpty() || !areaIdSet.isEmpty()) {
            E3Selector deliveryTypeSelector = new E3Selector();
            deliveryTypeSelector.addFilterField(new E3FilterField("id", deliveryTypeIdSet));
            deliveryTypeSelector.addSelectFields("id");
            deliveryTypeSelector.addSelectFields("name");
            AdvancedDeliveryType[] deliveryTypes = deliveryTypeService.queryObject(token, deliveryTypeSelector);
            Map<Long, String> deliveryTypeMaps = Arrays.stream(deliveryTypes).collect(Collectors.toMap(i -> i.getId(), i -> i.getName()));

            E3Selector provinceSelector = new E3Selector();
            provinceSelector.addFilterField(new E3FilterField("administrationAreaId", areaIdSet));
            provinceSelector.addSelectFields("administrationAreaId");
            provinceSelector.addSelectFields("name");
            AdministrationArea[] areas = areaService.findAdministrationAreaList(token, provinceSelector);
            Map<Long, String> areaMaps = Arrays.stream(areas).collect(Collectors.toMap(i -> i.getAdministrationAreaId(), i -> i.getName()));

            for (AdvancedAdapterPriorityDetail bean : beans) {
                if (bean.getDeliveryType() == null) {
                    bean.setDeliveryType(new AdvancedDeliveryType());
                }
                bean.getDeliveryType().setName(deliveryTypeMaps.get(bean.getDeliveryTypeId()));

                if (bean.getProvince() == null) {
                    bean.setProvince(new AdministrationArea());
                }
                bean.getProvince().setName(areaMaps.get(bean.getProvinceId()));
            }
        }

        doSystemOperateLog(token, beans, OperateType.CREATE);
        result.addSuccessObject(beans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    private void doSystemOperateLog(String token, AdvancedAdapterPriorityDetail[] bills, String operateType) {
        try {
            List<SystemOperateLog> logs = new ArrayList<>();
            StringBuffer content = new StringBuffer();
            for (AdvancedAdapterPriorityDetail priorityDetail : bills) {
                if (OperateType.CREATE.equals(operateType)) {
                    content.append(" 编辑区域适配优先级：");
                    content.append(" 新增 省份：" + priorityDetail.getProvince().getName() + "; 配送方式："
                            + priorityDetail.getDeliveryType().getName() + "; 值：" + priorityDetail.getValue());
                }
            }
            SystemOperateLog log = new SystemOperateLog();
            log.setBillType(BillType.AdvancedExpressStrategy);
            log.setPId(bills[0].getExpressStrategyId().toString());
            log.setOperateTime(new Date());
            log.setOperateType(OperateType.MODIFY);
            log.setOperatorId(LoginUtil.checkUserLogin(token).userId);
            log.setOperateContent(content.toString());
            if (StringUtil.isNotEmptyOrNull(log.getOperateContent())) {
                logs.add(log);
            }
            iSystemOperateLogService.createObject(token, logs.toArray(new SystemOperateLog[logs.size()]));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ServiceResult modifyObject(String token, AdvancedAdapterPriorityDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return null;
        }

        AdvancedAdapterPriorityDetail[] sortedBeans = Arrays.stream(beans).sorted(Comparator.comparing(t -> t.getId())).toArray(AdvancedAdapterPriorityDetail[]::new);
        advancedAdapterPriorityDetailMapper.updateByPrimaryKeySelectiveBatch(sortedBeans);

        doSystemOperateLog(token, sortedBeans, OperateType.MODIFY);
        result.addSuccessObject(sortedBeans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    @Transactional
    public ServiceResult removeObject(String token, Object[] ids) {
        ServiceResult result = new ServiceResult();
        if (ids == null || ids.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        advancedAdapterPriorityDetailMapper.deleteByPrimaryKeyBatch(idArr);

        result.addSuccessObject(ids);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    @Transactional
    public String importData(String token, List<AdvancedAdapterPriorityDetail> details, Long pid) {
        ServiceResult result = new ServiceResult();
        if (details == null || details.size() == 0) {
            result.addErrorObject("", "", "input data is null");
            return JSON.toJSONString(result);
        }

        advancedAdapterPriorityDetailMapper.deleteByExpressStrategyIdBatch(new Long[]{pid});

        List<List<AdvancedAdapterPriorityDetail>> partition = Lists.partition(details, 1000);
        for (List<AdvancedAdapterPriorityDetail> list : partition) {
            this.createObject(token, list.toArray(new AdvancedAdapterPriorityDetail[list.size()]));
        }

        return JSON.toJSONString(ResultUtil.handleResult(result, new Result()));
    }

    @Override
    public AdvancedAdapterPriorityDetail[] findObjectById(String token, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        Long[] idsArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        List<AdvancedAdapterPriorityDetail> details = advancedAdapterPriorityDetailMapper.selectByPrimaryKeyBatch(idsArr);

        if (details == null || details.size() == 0) {
            return null;
        }
        return details.toArray(new AdvancedAdapterPriorityDetail[details.size()]);
    }

    @Override
    public AdvancedAdapterPriorityDetail[] queryObject(String token, E3Selector selector) {
        // TODO
        return new AdvancedAdapterPriorityDetail[0];
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        AdvancedAdapterPriorityDetailExample example = new AdvancedAdapterPriorityDetailExample();
        return advancedAdapterPriorityDetailMapper.countByExample(example);
    }

    @Override
    public AdvancedAdapterPriorityDetail[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
        AdvancedAdapterPriorityDetailExample example = new AdvancedAdapterPriorityDetailExample();
        E3FilterField filterField = selector.getFilterFieldByFieldName("expressStrategyId");
        if (filterField != null) {
            Object value = filterField.getValue();
            if (value != null) {
                AdvancedAdapterPriorityDetailExample.Criteria criteria = example.createCriteria();
                criteria.andExpressStrategyIdEqualTo(Long.valueOf(String.valueOf(value)));
            }
        }
        if (pageIndex >= 0 && pageSize >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize);
        }
        List<AdvancedAdapterPriorityDetail> details = advancedAdapterPriorityDetailMapper.selectByExample(example);

        if (details == null || details.size() == 0) {
            return null;
        }

        return details.toArray(new AdvancedAdapterPriorityDetail[details.size()]);
    }

    @Override
    public Map<Long, List<Long>> queryAvailableDeliveryType(String token, Map<Long, List<Long>> strategyIds) {
        if (CollectionUtil.isEmpty(strategyIds)) {
            return null;
        }

        Map<Long, List<Long>> map = new HashMap<>();
        List<Long> strategyIdList = new ArrayList<>();

        for (Map.Entry<Long, List<Long>> longListEntry : strategyIds.entrySet()) {
            if (!CollectionUtil.isEmpty(longListEntry.getValue())) {
                strategyIdList.addAll(longListEntry.getValue());
            }
        }

        if (!CollectionUtil.isEmpty(strategyIdList)) {
            List<AdvancedAdapterPriorityDetail> details = advancedAdapterPriorityDetailMapper.queryAvailableDeliveryType(strategyIdList.toArray(new Long[0]));

            for (AdvancedAdapterPriorityDetail detail : details) {
                Long expressStrategyId = detail.getExpressStrategyId();
                for (Map.Entry<Long, List<Long>> longListEntry : strategyIds.entrySet()) {
                    if (longListEntry.getValue().contains(expressStrategyId)) {
                        if (map.containsKey(longListEntry.getKey())) {
                            map.get(longListEntry.getKey()).add(detail.getDeliveryTypeId());
                        } else {
                            List<Long> list = new ArrayList<>();
                            list.add(detail.getDeliveryTypeId());
                            map.put(longListEntry.getKey(), list);
                        }
                    }
                }
            }
        }

        return map;
    }

}
