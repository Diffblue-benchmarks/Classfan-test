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

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedShopService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSystemOperateLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedAdapterShopDetail;
import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedExpressStrategy;
import com.baison.e3plus.basebiz.order.api.service.adapter.IAdvancedAdapterShopDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedAdapterShopDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedExpressStrategyMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedAdapterShopDetailExample;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.common.OperateType;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.operatelog.model.SystemOperateLog;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;

/**
 * 适配店铺
 *
 * @author tongyong.geng
 */
@Service
public class AdvancedAdapterShopDetailService implements IAdvancedAdapterShopDetailService {

    @Autowired
    private ConsumerSystemOperateLogService iSystemOperateLogService;
    @Autowired
    private AdvancedAdapterShopDetailMapper advancedAdapterShopDetailMapper;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private ConsumerAdvancedShopService advancedShopService;
    @Autowired
    private AdvancedExpressStrategyMapper advancedExpressStrategyMapper;

    private static Logger logger = LoggerFactory.getLogger(AdvancedAdapterShopDetailService.class);

    /**
     * 快递策略缓存标记
     */
    private static final String CACHE_FLAG = "OrderExpressStrategyVersion";

    @Override
    @Transactional
    public ServiceResult createObject(String token, AdvancedAdapterShopDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        //根据策略ID & 店铺ID 校验是否已存在
        if (checkStrategyAndShop(beans, result, OperateType.CREATE)) {
            return result;
        }
        
        if(!checkShopByWareHouseId(beans, result, OperateType.MODIFY)) {
        	return result;
        }

        BatchIdVo batchIdVo = idService.batchId(beans.length);
        long startId = batchIdVo.getStartId();
        for (AdvancedAdapterShopDetail bean : beans) {
            bean.setId(startId);
            startId += batchIdVo.getStep();
        }
        advancedAdapterShopDetailMapper.insertBatch(beans);

        doSystemOpetateLog(token, beans, OperateType.CREATE);
        result.addSuccessObject(beans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    private void doSystemOpetateLog(String token, AdvancedAdapterShopDetail[] bills, String operateType) {
            List<SystemOperateLog> logs = new ArrayList<>();
            if(bills != null && bills.length > 0){
                StringBuffer content = new StringBuffer("编辑适用店铺：");
                for (AdvancedAdapterShopDetail shopDetail : bills) {
                    if (OperateType.CREATE.equals(operateType)) {
                        content.append(" 新增店铺：" + shopDetail.getShopName());
                    } else if (OperateType.REMOVE.equals(operateType)) {
                        content.append(" 删除店铺：" + shopDetail.getShopName());
                    }
                }
                SystemOperateLog log = new SystemOperateLog();
                try {
                    log.setBillType(BillType.AdvancedExpressStrategy);
                    log.setOperateTime(new Date());
                    log.setOperateType(OperateType.MODIFY);
                    log.setOperatorId(LoginUtil.checkUserLogin(token).userId);
                    log.setPId(String.valueOf(bills[0].getExpressStrategyId()));
                    log.setOperateContent(content.toString());

                    if(StringUtil.isNotEmptyOrNull(log.getOperateContent())){
                        logs.add(log);
                    }
                    iSystemOperateLogService.createObject(token, logs.toArray(new SystemOperateLog[logs.size()]));
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
    }

    @Override
    @Transactional
    public ServiceResult modifyObject(String token, AdvancedAdapterShopDetail[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        //根据策略ID & 店铺ID 校验是否已存在
        if (checkStrategyAndShop(beans, result, OperateType.MODIFY)) {
            return result;
        }
        
        if(!checkShopByWareHouseId(beans, result, OperateType.MODIFY)) {
        	return result;
        }

        AdvancedAdapterShopDetail[] sortedBeans = Arrays.stream(beans).sorted(Comparator.comparing(t -> t.getId())).toArray(AdvancedAdapterShopDetail[]::new);

        advancedAdapterShopDetailMapper.updateByPrimaryKeySelectiveBatch(sortedBeans);

        doSystemOpetateLog(token, beans, OperateType.MODIFY);
        result.addSuccessObject(beans);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    /**
     * 根据策略ID & 店铺ID 校验是否已存在
     *
     * @author tongyong.geng
     */
    private boolean checkStrategyAndShop(AdvancedAdapterShopDetail[] beans, ServiceResult result, String operateType) {
        Map<String, String> map = new HashMap<>();
        for (AdvancedAdapterShopDetail bean : beans) {
            //校验批量数据中是否存在重复数据
            String key = bean.getExpressStrategyId() + "" + bean.getShopId();
            if (map.containsKey(key)) {
                result.addErrorObject(beans, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_014,
                        ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_014));
                return true;
            } else {
                map.put(key, "");
            }

            AdvancedAdapterShopDetailExample example = new AdvancedAdapterShopDetailExample();
            AdvancedAdapterShopDetailExample.Criteria criteria = example.createCriteria();
            criteria.andExpressStrategyIdEqualTo(bean.getExpressStrategyId());
            criteria.andShopIdEqualTo(bean.getShopId());
            //修改时排除自身数据
            if (OperateType.MODIFY.equals(operateType)) {
                criteria.andIdNotEqualTo(bean.getId());
            }
            long count = advancedAdapterShopDetailMapper.countByExample(example);
            if (count > 0) {
                result.addErrorObject(beans, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_014,
                        ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_014));
                return true;
            }
        }
        return false;
    }
    
    private boolean checkShopByWareHouseId(AdvancedAdapterShopDetail[] beans, ServiceResult result, String operateType) {
    	AdvancedExpressStrategy expressStrategy = advancedExpressStrategyMapper.selectByPrimaryKey(beans[0].getExpressStrategyId());
    	
    	for(AdvancedAdapterShopDetail bean : beans) {
    		if(expressStrategy.getWarehouseId() != null) {
    			List<Long> expressStrategys = advancedExpressStrategyMapper.selectByShopIdAndWareHouseId(bean.getShopId(), expressStrategy.getWarehouseId(), null);
    			//修改时排除自身数据
                if (OperateType.MODIFY.equals(operateType) && expressStrategys != null) {
                	expressStrategys.remove(beans[0].getExpressStrategyId());
                }
    			if(expressStrategys != null && expressStrategys.size() > 0) {
    				result.addErrorObject(beans, AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_020,
                            ResourceUtils.get(AdvancedOrderErrorCode.ADVANCEDEXPRESSSTRATEGY_020));
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }

    @Override
    @Transactional
    public ServiceResult removeObject(String token, Object[] ids) {
        ServiceResult result = new ServiceResult();
        if (ids == null || ids.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }

        AdvancedAdapterShopDetail[] shopDetails = findObjectById(token, ids);
        advancedAdapterShopDetailMapper.deleteByPrimaryKeyBatch(ids);
        if(shopDetails != null && shopDetails.length > 0){
            List<Long> shopIds = new ArrayList<Long>();
            Arrays.stream(shopDetails).forEach(s -> shopIds.add(s.getShopId()));

            E3Selector shopsSelector = new E3Selector();
            shopsSelector.addFilterField(new E3FilterField("shopId",shopIds));
            shopsSelector.addSelectFields("shopId");
            shopsSelector.addSelectFields("name");
            Shop[] shops = advancedShopService.findShop(token, shopsSelector);

            Map<Long, String> shopsMap = Arrays.stream(shops).collect(Collectors.toMap(t -> t.getShopId(), t -> t.getName(), (k1, k2) -> k2));
            for (AdvancedAdapterShopDetail shopDetail : shopDetails) {
                shopDetail.setShopName(shopsMap.get(shopDetail.getShopId()));
            }
        }
        doSystemOpetateLog(token, shopDetails, OperateType.REMOVE);
        ISupportServiceCache.updateVersion(CACHE_FLAG);
        return result;
    }

    @Override
    public AdvancedAdapterShopDetail[] findObjectById(String token, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        List<AdvancedAdapterShopDetail> shopDetailList = advancedAdapterShopDetailMapper.selectByPrimaryKeyBatch(idArr);
        if (shopDetailList == null || shopDetailList.size() == 0) {
            return null;
        }

        return shopDetailList.toArray(new AdvancedAdapterShopDetail[shopDetailList.size()]);
    }

    @Override
    public AdvancedAdapterShopDetail[] queryObject(String token, E3Selector selector) {
        // TODO
        return new AdvancedAdapterShopDetail[0];
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        AdvancedAdapterShopDetailExample example = new AdvancedAdapterShopDetailExample();
        return advancedAdapterShopDetailMapper.countByExample(example);
    }

    @Override
    public AdvancedAdapterShopDetail[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
        AdvancedAdapterShopDetailExample example = new AdvancedAdapterShopDetailExample();
        E3FilterField filterField = selector.getFilterFieldByFieldName("expressStrategyId");
        if (filterField != null) {
            Object value = filterField.getValue();
            if (value != null) {
                AdvancedAdapterShopDetailExample.Criteria criteria = example.createCriteria();
                criteria.andExpressStrategyIdEqualTo(Long.valueOf(String.valueOf(value)));
            }
        }
        if (pageIndex >= 0 && pageIndex >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize);
        }
        List<AdvancedAdapterShopDetail> details = advancedAdapterShopDetailMapper.selectByExample(example);
        if (details == null || details.size() == 0) {
            return null;
        }

        Set<Long> shopIdSet = details.stream().map(t -> t.getShopId()).collect(Collectors.toSet());
        if (shopIdSet != null && shopIdSet.size() > 0) {
            Shop[] shops = advancedShopService.findShopById(token, shopIdSet.toArray(new Long[shopIdSet.size()]));
            if (shops != null && shops.length > 0) {
                Map<String, Shop> shopMap = Arrays.stream(shops).collect(Collectors.toMap(t -> String.valueOf(t.getShopId()), t -> t, (t1, t2) -> t2));
                for (AdvancedAdapterShopDetail detail : details) {
                    String key = String.valueOf(detail.getShopId());
                    Shop shop = shopMap.get(key);
                    if (shop != null) {
                        detail.setShop(shop);
                        detail.setShopCode(shop.getCode());
                        detail.setShopName(shop.getName());
                    }
                }
            }
        }

        return details.toArray(new AdvancedAdapterShopDetail[details.size()]);
    }

}
