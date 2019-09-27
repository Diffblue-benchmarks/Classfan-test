package com.baison.e3plus.basebiz.order.service.service.adapter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSystemOperateLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterWareHouseDetail;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedAdapterWareHouseDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedAdapterWareHouseDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedAdapterWareHouseDetailExample;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.common.OperateType;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.operatelog.model.SystemOperateLog;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;

@Service
public class AdvancedAdapterWareHouseDetailService implements IAdvancedAdapterWareHouseDetailService {
    @Autowired
    private ConsumerSystemOperateLogService iSystemOperateLogService;
    @Autowired
    private AdvancedAdapterWareHouseDetailMapper advancedAdapterWareHouseDetailMapper;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private ConsumerAdvancedWareHouseService advancedWareHouseService;

    private static Logger logger = LoggerFactory.getLogger(AdvancedAdapterWareHouseDetailService.class);

    /**
     * 快递策略缓存标记
     */
    private static final String CACHE_FLAG = "OrderExpressStrategyVersion";

    @Override
    @Transactional
    public ServiceResult createObject(String token, AdvancedAdapterWareHouseDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        BatchIdVo batchIdVo = idService.batchId(beans.length);
        long startId = batchIdVo.getStartId();
        for (AdvancedAdapterWareHouseDetail bean : beans) {
            bean.setId(startId);
            startId += batchIdVo.getStep();
        }

        advancedAdapterWareHouseDetailMapper.insertBatch(beans);

        doSystemOpetateLog(token, beans, OperateType.CREATE);
        result.addSuccessObject(beans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    /**
     * 根据策略ID & 仓库ID 校验是否已存在
     *
     * @author tongyong.geng
     */
    private boolean checkStrategyAndShop(AdvancedAdapterWareHouseDetail[] beans, ServiceResult result, String operateType) {
        Map<String, String> map = new HashMap<>();
        for (AdvancedAdapterWareHouseDetail bean : beans) {
            //校验批量数据中是否存在重复数据
            String key = bean.getExpressStrategyId() + "" + bean.getWarehouseId();
            if (map.containsKey(key)) {
                result.addErrorObject(beans, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_013,
                        ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_013));
                return true;
            } else {
                map.put(key, "");
            }

            AdvancedAdapterWareHouseDetailExample example = new AdvancedAdapterWareHouseDetailExample();
            AdvancedAdapterWareHouseDetailExample.Criteria criteria = example.createCriteria();
            criteria.andExpressStrategyIdEqualTo(bean.getExpressStrategyId());
            criteria.andWarehouseIdEqualTo(bean.getWarehouseId());
            //修改时排除自身数据
            if (OperateType.MODIFY.equals(operateType)) {
                criteria.andIdNotEqualTo(bean.getId());
            }
            long count = advancedAdapterWareHouseDetailMapper.countByExample(example);
            if (count > 0) {
                result.addErrorObject(beans, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_013,
                        ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_013));
                return true;
            }
        }
        return false;
    }

    private void doSystemOpetateLog(String token, AdvancedAdapterWareHouseDetail[] bills, String operateType) {
        try {
            List<SystemOperateLog> logs = new ArrayList<>();
            for (AdvancedAdapterWareHouseDetail shopDetail : bills) {
                SystemOperateLog log = new SystemOperateLog();
                log.setBillType(BillType.AdvancedExpressStrategy);
                log.setOperateTime(new Date());
                log.setOperateType(OperateType.MODIFY);
                log.setOperatorId(LoginUtil.checkUserLogin(token).userId);
                log.setPId(shopDetail.getExpressStrategyId().toString());

                StringBuffer content = new StringBuffer("编辑适用仓库：");
                if (OperateType.CREATE.equals(operateType)) {
                    content.append(" 新增仓库：" + shopDetail.getWareHouse().getName());
                } else if (OperateType.REMOVE.equals(operateType)) {
                    content.append(" 删除仓库：" + shopDetail.getWareHouse().getName());
                }
                log.setOperateContent(content.toString());

                logs.add(log);
            }
            iSystemOperateLogService.createObject(token, logs.toArray(new SystemOperateLog[logs.size()]));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ServiceResult modifyObject(String token, AdvancedAdapterWareHouseDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        AdvancedAdapterWareHouseDetail[] sortedBeans = Arrays.stream(beans).sorted(Comparator.comparing(t -> t.getId())).toArray(AdvancedAdapterWareHouseDetail[]::new);
        advancedAdapterWareHouseDetailMapper.updateByPrimaryKeySelectiveBatch(sortedBeans);

        doSystemOpetateLog(token, sortedBeans, OperateType.MODIFY);
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

        Long[] idsArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        advancedAdapterWareHouseDetailMapper.deleteByPrimaryKeyBatch(idsArr);

        AdvancedAdapterWareHouseDetail[] shopDetails = findObjectById(token, ids);

        doSystemOpetateLog(token, shopDetails, OperateType.REMOVE);
        result.addSuccessObject(ids);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    public AdvancedAdapterWareHouseDetail[] findObjectById(String token, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        List<AdvancedAdapterWareHouseDetail> detailList = advancedAdapterWareHouseDetailMapper.selectByPrimaryKeyBatch(idArr);
        if (detailList == null || detailList.size() == 0) {
            return null;
        }

        return detailList.toArray(new AdvancedAdapterWareHouseDetail[detailList.size()]);
    }

    @Override
    public AdvancedAdapterWareHouseDetail[] queryObject(String token, E3Selector selector) {
        //TODO
        return new AdvancedAdapterWareHouseDetail[0];
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        AdvancedAdapterWareHouseDetailExample example = new AdvancedAdapterWareHouseDetailExample();
        return advancedAdapterWareHouseDetailMapper.countByExample(example);
    }

    @Override
    public AdvancedAdapterWareHouseDetail[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
        AdvancedAdapterWareHouseDetailExample example = new AdvancedAdapterWareHouseDetailExample();
        E3FilterField filterField = selector.getFilterFieldByFieldName("expressStrategyId");
        if (filterField != null) {
            Object value = filterField.getValue();
            if (value != null) {
                AdvancedAdapterWareHouseDetailExample.Criteria criteria = example.createCriteria();
                criteria.andExpressStrategyIdEqualTo(Long.valueOf(String.valueOf(value)));
            }
        }
        if (pageSize >= 0 && pageIndex >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize);
        }
        List<AdvancedAdapterWareHouseDetail> detailList = advancedAdapterWareHouseDetailMapper.selectByExample(example);
        if (detailList == null || detailList.size() == 0) {
            return null;
        }

        Set<Long> warehouseIdSet = detailList.stream().map(t -> t.getWarehouseId()).collect(Collectors.toSet());
        if (warehouseIdSet != null && warehouseIdSet.size() > 0) {
            WareHouse[] wareHouses = advancedWareHouseService.findObjectById(token, warehouseIdSet.toArray(new Long[warehouseIdSet.size()]));
            if (wareHouses != null && wareHouses.length > 0) {
                Map<String, WareHouse> wareHouseMap = Arrays.stream(wareHouses).collect(Collectors.toMap(t -> String.valueOf(t.getWareHouseId()), t -> t, (t1, t2) -> t2));
                for (AdvancedAdapterWareHouseDetail detail : detailList) {
                    String key = String.valueOf(detail.getWarehouseId());
                    WareHouse wareHouse = wareHouseMap.get(key);
                    if (wareHouse != null) {
                        detail.setWareHouse(wareHouse);
                        detail.setWareHouseCode(wareHouse.getCode());
                        detail.setWareHouseName(wareHouse.getName());
                    }
                }
            }
        }

        return detailList.toArray(new AdvancedAdapterWareHouseDetail[detailList.size()]);
    }

}
