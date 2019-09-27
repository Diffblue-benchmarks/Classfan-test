package com.baison.e3plus.basebiz.order.service.service.adapter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedDeliveryTypeService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerPlatformGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSystemOperateLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.goods.api.business.advanced.model.PlatformGoods;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedStrategyPlatformGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedStrategyPlatformGoodsDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedStrategyPlatformGoodsDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedStrategyPlatformGoodsDetailExample;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.delivery.AdvancedDeliveryType;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.common.OperateType;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.operatelog.model.SystemOperateLog;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;

/**
 * 平台商品
 *
 * @author tongyong.geng
 */

@Service
public class AdvancedStrategyPlatformGoodsDetailService implements IAdvancedStrategyPlatformGoodsDetailService {
    @Autowired
    private ConsumerSystemOperateLogService iSystemOperateLogService;
    @Autowired
    private AdvancedStrategyPlatformGoodsDetailMapper advancedStrategyPlatformGoodsDetailMapper;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private ConsumerPlatformGoodsService platformGoodsService;
    @Autowired
    private ConsumerAdvancedDeliveryTypeService deliveryTypeService;

    private static Logger logger = LoggerFactory.getLogger(AdvancedStrategyPlatformGoodsDetailService.class);

    /**
     * 快递策略缓存标记
     */
    private static final String CACHE_FLAG = "OrderExpressStrategyVersion";

    @Override
    @Transactional
    public ServiceResult createObject(String token, AdvancedStrategyPlatformGoodsDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        BatchIdVo batchIdVo = idService.batchId(beans.length);
        long startId = batchIdVo.getStartId();
        for (AdvancedStrategyPlatformGoodsDetail bean : beans) {
            bean.setId(startId);
            startId += batchIdVo.getStep();
        }
        advancedStrategyPlatformGoodsDetailMapper.insertBatch(beans);

        doSystemOperateLog(token, beans, OperateType.CREATE);
        result.addSuccessObject(beans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    private void doSystemOperateLog(String token, AdvancedStrategyPlatformGoodsDetail[] bills, String operateType) {
        try {
            List<SystemOperateLog> logs = new ArrayList<>();
            for (AdvancedStrategyPlatformGoodsDetail goodsDetail : bills) {
                SystemOperateLog log = new SystemOperateLog();
                log.setBillType(BillType.AdvancedExpressStrategy);
                log.setPId(goodsDetail.getExpressStrategyId().toString());
                log.setOperateTime(new Date());
                log.setOperateType(OperateType.MODIFY);
                log.setOperatorId(LoginUtil.checkUserLogin(token).userId);

                StringBuffer content = new StringBuffer("编辑平台商品：");
                if (OperateType.CREATE.equals(operateType)) {
                    content.append("新增 平台商品：" + goodsDetail.getPlatformGoodsName() + "; 配送方式："
                            + goodsDetail.getDeliveryTypeName() + "; 优先级：" + goodsDetail.getPriority());
                } else if (OperateType.MODIFY.equals(operateType)){
                    content.append("修改 平台商品：" + goodsDetail.getPlatformGoodsName() + "; 配送方式："
                            + goodsDetail.getDeliveryTypeName() + "; 优先级：" + goodsDetail.getPriority());
                } else if (OperateType.REMOVE.equals(operateType)) {
                    content.append("删除 平台商品：" + goodsDetail.getPlatformGoodsName() + "; 配送方式："
                            + goodsDetail.getDeliveryTypeName() + "; 优先级：" + goodsDetail.getPriority());
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
    public ServiceResult modifyObject(String token, AdvancedStrategyPlatformGoodsDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        AdvancedStrategyPlatformGoodsDetail[] sortedBeans = Arrays.stream(beans).sorted(Comparator.comparing(t -> t.getId())).toArray(AdvancedStrategyPlatformGoodsDetail[]::new);

        advancedStrategyPlatformGoodsDetailMapper.updateByPrimaryKeySelectiveBatch(sortedBeans);

        doSystemOperateLog(token, beans, OperateType.MODIFY);
        result.addSuccessObject(beans);
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

        AdvancedStrategyPlatformGoodsDetail[] details = this.findObjectById(token, ids);
        advancedStrategyPlatformGoodsDetailMapper.deleteByPrimaryKeyBatch(idArr);

        if(details != null && details.length > 0){

            List<String> goodsIds = new ArrayList<String>();
            List<Long> deliverTypeIds = new ArrayList<Long>();

            Arrays.stream(details).forEach(i -> {goodsIds.add(i.getPlatformGoodsId());deliverTypeIds.add(i.getDeliveryTypeId());});

            E3Selector goodsSelector = new E3Selector();
            goodsSelector.addFilterField(new E3FilterField("platformGoodsId",goodsIds));
            goodsSelector.addSelectFields("platformGoodsId");
            goodsSelector.addSelectFields("name");
            PlatformGoods[] platformGoods = platformGoodsService.queryObject(token, goodsSelector);

            E3Selector deliverTypeSelector = new E3Selector();
            deliverTypeSelector.addFilterField(new E3FilterField("id",deliverTypeIds));
            deliverTypeSelector.addSelectFields("id");
            deliverTypeSelector.addSelectFields("name");
            AdvancedDeliveryType[] deliverTypes = deliveryTypeService.queryObject(token, deliverTypeSelector);

            Map<String, String> platformGoodMaps = Arrays.stream(platformGoods).collect(Collectors.toMap(k -> k.getPlatformGoodsId(), v -> v.getName(), (k1, k2) -> k2));
            Map<Long, String> deliverTypeMaps = Arrays.stream(deliverTypes).collect(Collectors.toMap(k -> k.getId(), v -> v.getName(), (k1, k2) -> k2));

            Arrays.stream(details).forEach(i -> {
                i.setDeliveryTypeName(deliverTypeMaps.get(i.getDeliveryTypeId()));
                i.setPlatformGoodsName(platformGoodMaps.get(i.getPlatformGoodsId()));
            });
        }

        doSystemOperateLog(token, details, OperateType.REMOVE);
        result.addSuccessObject(ids);

        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    public AdvancedStrategyPlatformGoodsDetail[] findObjectById(String token, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        List<AdvancedStrategyPlatformGoodsDetail> detailList = advancedStrategyPlatformGoodsDetailMapper.selectByPrimaryKeyBatch(ids);

        if (detailList == null || detailList.size() == 0) {
            return null;
        }
        return detailList.toArray(new AdvancedStrategyPlatformGoodsDetail[detailList.size()]);
    }

    @Override
    public AdvancedStrategyPlatformGoodsDetail[] queryObject(String token, E3Selector selector) {
        // TODO
        return new AdvancedStrategyPlatformGoodsDetail[0];
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        AdvancedStrategyPlatformGoodsDetailExample example = new AdvancedStrategyPlatformGoodsDetailExample();
        return advancedStrategyPlatformGoodsDetailMapper.countByExample(example);
    }

    @Override
    public AdvancedStrategyPlatformGoodsDetail[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
        AdvancedStrategyPlatformGoodsDetailExample example = new AdvancedStrategyPlatformGoodsDetailExample();
        E3FilterField filterField = selector.getFilterFieldByFieldName("expressStrategyId");
        if (filterField != null) {
            Object value = filterField.getValue();
            if (value != null) {
                AdvancedStrategyPlatformGoodsDetailExample.Criteria criteria = example.createCriteria();
                criteria.andExpressStrategyIdEqualTo(Long.valueOf(String.valueOf(value)));
            }
        }
        if (pageIndex >= 0 && pageIndex >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize);
        }
        List<AdvancedStrategyPlatformGoodsDetail> detailList = advancedStrategyPlatformGoodsDetailMapper.selectByExample(example);
        if (detailList == null || detailList.size() == 0) {
            return null;
        }

        Set<String> platformGoodsIdSet = detailList.stream().map(t -> t.getPlatformGoodsId()).collect(Collectors.toSet());
        if (platformGoodsIdSet != null && platformGoodsIdSet.size() > 0) {
            //平台商品
            E3Selector goodsDetailSelector = new E3Selector();
            E3FilterField e3FilterField = new E3FilterField("platformGoodsId", "in", platformGoodsIdSet.toArray(new String[platformGoodsIdSet.size()]), E3FilterField.ANDOperator);
            goodsDetailSelector.addFilterField(e3FilterField);

            PlatformGoods[] goods = platformGoodsService.queryObject(token, goodsDetailSelector);
            if (goods != null && goods.length > 0) {
                Map<String, PlatformGoods> platformGoodsMap = Arrays.stream(goods).collect(Collectors.toMap(t -> String.valueOf(t.getPlatformGoodsId()), t -> t, (t1, t2) -> t2));
                for (AdvancedStrategyPlatformGoodsDetail detail : detailList) {
                    String key = String.valueOf(detail.getPlatformGoodsId());
                    PlatformGoods platformGoods = platformGoodsMap.get(key);
                    if (platformGoods != null) {
                        detail.setPlatformGoods(platformGoods);
                        detail.setPlatformGoodsCode(platformGoods.getCode());
                        detail.setPlatformGoodsName(platformGoods.getName());
                    }
                }
            }


            Set<Long> deliveryTypeIdSet = detailList.stream().map(t -> t.getDeliveryTypeId()).collect(Collectors.toSet());
            if (platformGoodsIdSet != null && platformGoodsIdSet.size() > 0) {
                //配送方式
                AdvancedDeliveryType[] deliveryTypes = deliveryTypeService.findObjectById(token, deliveryTypeIdSet.toArray(new Long[deliveryTypeIdSet.size()]));
                if (deliveryTypes != null && deliveryTypes.length > 0) {
                    Map<String, AdvancedDeliveryType> deliveryTypesMap = Arrays.stream(deliveryTypes).collect(Collectors.toMap(t -> String.valueOf(t.getId()), t -> t, (v1, v2) -> v2));
                    for (AdvancedStrategyPlatformGoodsDetail detail : detailList) {
                        String key = String.valueOf(detail.getDeliveryTypeId());
                        AdvancedDeliveryType deliveryType = deliveryTypesMap.get(key);
                        if (deliveryType != null) {
                            detail.setDeliveryType(deliveryType);
                            detail.setDeliveryTypeCode(deliveryType.getCode());
                            detail.setDeliveryTypeName(deliveryType.getName());
                        }
                    }
                }
            }
        }

        return detailList.toArray(new AdvancedStrategyPlatformGoodsDetail[detailList.size()]);
    }
}
