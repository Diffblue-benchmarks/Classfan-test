package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedShopService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSystemOperateLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.OrderDistributeStrategy;
import com.baison.e3plus.basebiz.order.api.model.OrderOdsShopScope;
import com.baison.e3plus.basebiz.order.api.model.OrderOdsShopScopeDetail;
import com.baison.e3plus.basebiz.order.api.service.odsbase.IOrderDistributeStrategyService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrdDistributeStrategyMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderOdsShopScopeMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.OrdDistributeStrategyExample;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.common.OperateType;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.operatelog.model.SystemOperateLog;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.bscore.utils.BooleanUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.common.Status;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;

@Service
public class OrderDistributeStrategyServiceImpl implements IOrderDistributeStrategyService {
    private static final String CACHE_FLAG = "OrderDistributeStrategyVersion";
    private static final Map<Long, OrderDistributeStrategy> strategyCache = new ConcurrentHashMap<>();
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConsumerSystemOperateLogService iSystemOperateLogService;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private OrdDistributeStrategyMapper ordDistributeStrategyMapper;
    @Autowired
    private OrderOdsShopScopeMapper orderOdsShopScopeMapper;
    @Autowired
    private ConsumerAdvancedShopService advancedShopService;

    @Override
    public OrderDistributeStrategy[] findObjectById(String token, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        Long[] idsArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        List<OrderDistributeStrategy> orderDistributeStrategies = ordDistributeStrategyMapper
                .selectByPrimaryKeyBatch(idsArr);

        // 分单策略店铺范围明细
        List<OrderOdsShopScope> shopScopeList = orderOdsShopScopeMapper.selectByOdsIdBatch(idsArr);
        if (shopScopeList != null && shopScopeList.size() > 0) {
            List<List<OrderOdsShopScopeDetail>> shopScopeDetailListList = shopScopeList.stream()
                    .map(t -> t.getShopDetails()).collect(Collectors.toList());
            List<OrderOdsShopScopeDetail> billShopScopeDetailList = new ArrayList<>();
            if (shopScopeDetailListList != null && shopScopeDetailListList.size() > 0) {
                for (List<OrderOdsShopScopeDetail> orderOdsShopScopeDetails : shopScopeDetailListList) {
                    if (orderOdsShopScopeDetails != null && orderOdsShopScopeDetails.size() > 0) {
                        billShopScopeDetailList.addAll(orderOdsShopScopeDetails);
                    }
                }
            }

            // 填充显示需要的字段
            Set<Long> idArr = billShopScopeDetailList.stream().map(t -> t.getShopId()).collect(Collectors.toSet());
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("shopId", idArr));
            selector.addSelectFields("code");
            selector.addSelectFields("name");
            Shop[] shopArr = advancedShopService.queryPageShop(token, selector, -1, -1);
            if (shopArr != null && shopArr.length > 0) {
                Map<String, Shop> collect = Arrays.stream(shopArr)
                        .collect(Collectors.toMap(t -> String.valueOf(t.getShopId()), t -> t, (t1, t2) -> t2));
                for (OrderOdsShopScopeDetail orderOdsShopScopeDetail : billShopScopeDetailList) {
                    String key = String.valueOf(orderOdsShopScopeDetail.getShopId());
                    Shop shop = collect.get(key);
                    if (shop != null) {
                        orderOdsShopScopeDetail.setShop(shop);
                        orderOdsShopScopeDetail.setShopCode(shop.getCode());
                        orderOdsShopScopeDetail.setShopName(shop.getName());
                    }
                }

            }

            // 填充分单策略店铺范围明细
            Map<String, List<OrderOdsShopScope>> longListMap = shopScopeList.stream()
                    .collect(Collectors.groupingBy(t -> String.valueOf(t.getOrderDistributeStrategyId())));
            for (OrderDistributeStrategy orderDistributeStrategy : orderDistributeStrategies) {
                String key = String.valueOf(orderDistributeStrategy.getOrderDistributeStrategyId());
                List<OrderOdsShopScope> odsShopScopes = longListMap.get(key);
                if (odsShopScopes != null && odsShopScopes.size() > 0) {
                    orderDistributeStrategy.setShopScope(odsShopScopes);
                }
            }
        }

        return orderDistributeStrategies.toArray(new OrderDistributeStrategy[orderDistributeStrategies.size()]);
    }

    @Override
    @Transactional
    public ServiceResult createObject(String token, OrderDistributeStrategy[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }

        BatchIdVo batchIdVo = idService.batchId(beans.length);
        long startId = batchIdVo.getStartId();
        for (OrderDistributeStrategy bean : beans) {
            bean.setOrderDistributeStrategyId(startId);
            bean.setStatus(Integer.valueOf(Status.INITIAL));
            startId += batchIdVo.getStep();
        }

        ordDistributeStrategyMapper.insertBatch(beans);

        doSystemOperateLog(token, beans, OperateType.CREATE);
        result.addSuccessObject(beans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    private void doSystemOperateLog(String token, OrderDistributeStrategy[] bills, String operateType) {
        try {
            List<SystemOperateLog> logs = new ArrayList<>();
            for (OrderDistributeStrategy strategy : bills) {
                SystemOperateLog log = new SystemOperateLog();
                log.setBillType(BillType.OrderDistributeStrategy);
                log.setPId(String.valueOf(strategy.getOrderDistributeStrategyId()));
                log.setOperateTime(new Date());
                log.setOperateType(operateType);
                log.setOperatorId(LoginUtil.checkUserLogin(token).userId);
                if (OperateType.CREATE.equals(operateType)) {
                    log.setOperateContent("新增分单策略：" + strategy.getName());
                } else if (OperateType.MODIFY.equals(operateType)) {
                    log.setOperateContent("修改分担策略：" + (strategy));
                } else if (OperateType.ENABLE.equals(operateType)) {
                    log.setOperateContent("启用分单策略：" + strategy.getName());
                } else if (OperateType.DISABLE.equals(operateType)) {
                    log.setOperateContent("停用分单策略：" + strategy.getName());
                }

                if (StringUtil.isNotEmptyOrNull(log.getOperateContent())) {
                    logs.add(log);
                }
            }

            if (logs.size() > 0) {
                iSystemOperateLogService.createObject(token, logs.toArray(new SystemOperateLog[logs.size()]));
            }
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
    }

    @Override
    @Transactional
    public ServiceResult modifyObject(String token, OrderDistributeStrategy[] beans) {
    	
    	ServiceResult result = new ServiceResult();
    	
        // 排序，防止死锁
        OrderDistributeStrategy[] sortedBeans = Arrays.stream(beans)
                .sorted(Comparator.comparing(t -> t.getOrderDistributeStrategyId()))
                .toArray(OrderDistributeStrategy[]::new);
        Long[] strategyIdArr = Arrays.stream(sortedBeans).map(t -> t.getOrderDistributeStrategyId())
                .toArray(Long[]::new);

        // 更新分单策略
        ordDistributeStrategyMapper.updateByPrimaryKeySelectiveBatch(sortedBeans);

        // 更新分单策略店铺范围明细
        List<List<OrderOdsShopScope>> shopScopes = Arrays.stream(beans).map(t -> t.getShopScope())
                .collect(Collectors.toList());

        for (OrderDistributeStrategy strategy : beans) {
            for (OrderOdsShopScope scope : strategy.getShopScope()) {
                scope.setOrderDistributeStrategyId(strategy.getOrderDistributeStrategyId());
            }
        }

        List<OrderOdsShopScope> saveShopScopes = new ArrayList<>();
        if (shopScopes != null && shopScopes.size() > 0) {
            for (List<OrderOdsShopScope> shopScope : shopScopes) {
                if (shopScope != null && shopScope.size() > 0) {
                    saveShopScopes.addAll(shopScope);
                }
            }

            BatchIdVo batchIdVo = idService.batchId(saveShopScopes.size());
            long startId = batchIdVo.getStartId();
            for (OrderOdsShopScope saveShopScope : saveShopScopes) {
                saveShopScope.setShopScopeId(startId);
                startId += batchIdVo.getStep();
            }
            orderOdsShopScopeMapper.deleteByOdsIdBatch(strategyIdArr);
            orderOdsShopScopeMapper.deleteDetailByOdsIdBatch(strategyIdArr);
            orderOdsShopScopeMapper.insertBatch(saveShopScopes);

            List<List<OrderOdsShopScopeDetail>> collect = saveShopScopes.stream().map(t -> {

                for (OrderOdsShopScopeDetail shopDetail : t.getShopDetails()) {
                    shopDetail.setOrderDistributeStrategyId(t.getOrderDistributeStrategyId());
                    shopDetail.setShopScopeId(t.getShopScopeId());
                }
                return t.getShopDetails();

            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                List<OrderOdsShopScopeDetail> saveDetailList = new ArrayList<>();
                if (collect != null && collect.size() > 0) {
                    for (List<OrderOdsShopScopeDetail> orderOdsShopScopeDetails : collect) {
                        if (orderOdsShopScopeDetails != null && orderOdsShopScopeDetails.size() > 0) {
                            saveDetailList.addAll(orderOdsShopScopeDetails);
                        }
                    }
                    
                    if (saveDetailList.size() > 0) {
                    	// 不能存在相同的店铺
                    	Map<Long,Integer> shopCount = new HashMap<>();
						for (OrderOdsShopScopeDetail saveShopDetail : saveDetailList) {
							Integer count = shopCount.get(saveShopDetail.getShopId());
							if (count == null) {
								count = 0;
							}
							shopCount.put(saveShopDetail.getShopId(), ++count);
						}
						List<Long> repeatShopIds = new ArrayList<>();
						for (Entry<Long, Integer> entry : shopCount.entrySet()) {
							if(entry.getValue()>1) {
								repeatShopIds.add(entry.getKey());
							}
						}
						if (repeatShopIds.size() > 0) {
							E3Selector selector = new E3Selector();
							selector.addFilterField(new E3FilterField("shopId", repeatShopIds));
							selector.addSelectFields("code");
							selector.addSelectFields("name");
							Shop[] shops = advancedShopService.queryPageShop(token, selector, -1, -1);
							
							StringBuilder errorBuilder = new StringBuilder();
							for (Shop shop : shops) {
								errorBuilder.append(ResourceUtils.get(AdvancedOrderErrorCode.SHOP_REPEAT, shop.getName()))
										.append(" \n ");
							}
							result.addErrorObject("", "",errorBuilder.toString());
						}
                    	
                        BatchIdVo detailBatchIdVo = idService.batchId(saveDetailList.size());
                        long detailStartId = detailBatchIdVo.getStartId();
                        for (OrderOdsShopScopeDetail saveShopDetail : saveDetailList) {
                            saveShopDetail.setShopDetailId(detailStartId);
                            detailStartId += detailBatchIdVo.getStep();
                        }

                        orderOdsShopScopeMapper.insertDetailBatch(saveDetailList);
                    }

                }
            }

        }

        result.addSuccessObject(sortedBeans);
        doSystemOperateLog(token, sortedBeans, OperateType.MODIFY);
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
        ordDistributeStrategyMapper.deleteByPrimaryKeyBatch(idArr);
        orderOdsShopScopeMapper.deleteByOdsIdBatch(idArr);

        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    public OrderDistributeStrategy[] queryObjectByOrderDisCalContext(String token, String isAllowPart) {
        Long localVersion = ISupportServiceCache.getLocalVersionCache(CACHE_FLAG);
        if (localVersion == null) {
            localVersion = 0l;
        }
        Long remoteVersion = ISupportServiceCache.getRemoteVersion(CACHE_FLAG);

        List<OrderDistributeStrategy> resultDatas = new ArrayList<>();

        OrdDistributeStrategyExample example = new OrdDistributeStrategyExample();
        OrdDistributeStrategyExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(Integer.valueOf(Status.ENABLE));
        criteria.andIsAllWareHouseDistributeNotEqualTo(true);
        if (StringUtil.isEmptyOrNull(isAllowPart)) {
            isAllowPart = "1";
        }
        criteria.andDistributeRuleEqualTo(BooleanUtil.parse(isAllowPart));

        List<Long> idList = ordDistributeStrategyMapper.selectIdsByExample(example);
        if (remoteVersion.equals(localVersion)) {
            List<Long> notCacheStrategyIds = new ArrayList<>();
            for (Long id : idList) {
                if (strategyCache.containsKey(id)) {
                    resultDatas.add(strategyCache.get(id));
                } else {
                    notCacheStrategyIds.add(id);
                }
            }

            if (notCacheStrategyIds.size() > 0) {
                OrderDistributeStrategy[] dbDatas = findObjectById(token,
                        notCacheStrategyIds.toArray(new Long[notCacheStrategyIds.size()]));

                if (dbDatas != null && dbDatas.length > 0) {
                    for (OrderDistributeStrategy strategy : dbDatas) {
                        strategyCache.put(strategy.getOrderDistributeStrategyId(), strategy);
                    }
                    resultDatas.addAll(Arrays.asList(dbDatas));
                }

            }

        } else {

            OrderDistributeStrategy[] dbDatas = findObjectById(token, idList.toArray(new Long[idList.size()]));

            if (dbDatas != null && dbDatas.length > 0) {
                for (OrderDistributeStrategy strategy : dbDatas) {
                    strategyCache.put(strategy.getOrderDistributeStrategyId(), strategy);
                }
                resultDatas.addAll(Arrays.asList(dbDatas));
            }
            ISupportServiceCache.setLocalVersionCache(remoteVersion, CACHE_FLAG);
        }

        return resultDatas.toArray(new OrderDistributeStrategy[resultDatas.size()]);
    }

    @Override
    public OrderDistributeStrategy[] queryObject(String token, E3Selector querySelector) {
        OrdDistributeStrategyExample example = new OrdDistributeStrategyExample();
        OrdDistributeStrategyExample.Criteria criteria = example.createCriteria();
        E3FilterField codeFilterField = querySelector.getFilterFieldByFieldName("code");
        if (codeFilterField != null) {
            criteria.andCodeLike(String.valueOf(codeFilterField.getValue()));
        }
        E3FilterField nameFilterField = querySelector.getFilterFieldByFieldName("name");
        if (nameFilterField != null) {
            criteria.andNameLike(String.valueOf(nameFilterField.getValue()));
        }

        E3FilterField statusFilterField = querySelector.getFilterFieldByFieldName("status");
        if (statusFilterField != null) {
            criteria.andStatusEqualTo((Integer) statusFilterField.getValue());
        }

        E3FilterField distributeruleFilterField = querySelector.getFilterFieldByFieldName("distributerule");
        if (distributeruleFilterField != null) {
            criteria.andDistributeRuleEqualTo((Boolean) distributeruleFilterField.getValue());
        }

        List<OrderDistributeStrategy> orderDistributeStrategies = ordDistributeStrategyMapper.selectByExample(example);
        if (orderDistributeStrategies.isEmpty()) {
            return null;
        }

        List<Long> orderDistributeStrategyIds = LinqUtil.select(orderDistributeStrategies,
                s -> s.getOrderDistributeStrategyId());
        List<OrderOdsShopScope> shopDetails = orderOdsShopScopeMapper
                .selectByOdsIdBatch(orderDistributeStrategyIds.toArray(new Long[orderDistributeStrategyIds.size()]));

        for (OrderDistributeStrategy strategy : orderDistributeStrategies) {
            List<OrderOdsShopScope> details = LinqUtil.where(shopDetails,
                    f -> f.getOrderDistributeStrategyId().equals(strategy.getOrderDistributeStrategyId()));
            strategy.setShopScope(details);
        }

        return orderDistributeStrategies.toArray(new OrderDistributeStrategy[orderDistributeStrategies.size()]);
    }

    @Override
    public OrderDistributeStrategy[] queryPageObject(String token, E3Selector querySelector, int pageSize,
                                                     int pageIndex) {
        OrdDistributeStrategyExample example = new OrdDistributeStrategyExample();
        OrdDistributeStrategyExample.Criteria criteria = example.createCriteria();
        E3FilterField codeFilterField = querySelector.getFilterFieldByFieldName("code");
        if (codeFilterField != null) {
            criteria.andCodeLike(String.valueOf(codeFilterField.getValue()));
        }
        E3FilterField nameFilterField = querySelector.getFilterFieldByFieldName("name");
        if (nameFilterField != null) {
            criteria.andNameLike(String.valueOf(nameFilterField.getValue()));
        }

        E3FilterField statusFilterField = querySelector.getFilterFieldByFieldName("status");
        if (statusFilterField != null) {
            criteria.andStatusEqualTo((Integer) statusFilterField.getValue());
        }

        E3FilterField distributeruleFilterField = querySelector.getFilterFieldByFieldName("distributerule");
        if (distributeruleFilterField != null) {
            criteria.andDistributeRuleEqualTo((Boolean) distributeruleFilterField.getValue());
        }

        if (pageIndex >= 0 && pageSize >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize);
        }
        List<OrderDistributeStrategy> orderDistributeStrategies = ordDistributeStrategyMapper.selectByExample(example);
        if (orderDistributeStrategies.isEmpty()) {
            return null;
        }

        List<Long> orderDistributeStrategyIds = LinqUtil.select(orderDistributeStrategies,
                s -> s.getOrderDistributeStrategyId());
        List<OrderOdsShopScope> shopDetails = orderOdsShopScopeMapper
                .selectByOdsIdBatch(orderDistributeStrategyIds.toArray(new Long[orderDistributeStrategyIds.size()]));

        for (OrderDistributeStrategy strategy : orderDistributeStrategies) {
            List<OrderOdsShopScope> details = LinqUtil.where(shopDetails,
                    f -> f.getOrderDistributeStrategyId().equals(strategy.getOrderDistributeStrategyId()));
            strategy.setShopScope(details);
        }

        return orderDistributeStrategies.toArray(new OrderDistributeStrategy[orderDistributeStrategies.size()]);
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        return ordDistributeStrategyMapper.count();
    }

    @Override
    @Transactional
    public ServiceResult enable(String token, Object[] ids) {
        ServiceResult result = new ServiceResult();
        if (ids == null || ids.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }

        OrderDistributeStrategy[] datas = findObjectById(token, ids);
        for (OrderDistributeStrategy data : datas) {
            data.setStatus(Integer.valueOf(Status.ENABLE));
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        ordDistributeStrategyMapper.enableByPrimaryKeyBatch(idArr);

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

        OrderDistributeStrategy[] datas = findObjectById(token, ids);
        for (OrderDistributeStrategy data : datas) {
            data.setStatus(Integer.valueOf(Status.DISABLE));
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        ordDistributeStrategyMapper.disableByPrimaryKeyBatch(idArr);

        doSystemOperateLog(token, datas, OperateType.DISABLE);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }
}
