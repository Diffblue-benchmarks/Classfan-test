package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedShopService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriority;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategy;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyOnlineDetail;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyShopDetail;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyStoreDetail;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeCalculateException;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyOnlineDetailService;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyService;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyShopDetailService;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyStoreDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.WareHousePriorityStrategyMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyExample;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyExample.Criteria;
import com.baison.e3plus.biz.support.api.cache.ISupportServiceCache;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.cncore.common.exception.ServiceResultErrorException;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.session.Session;
import com.github.pagehelper.PageHelper;

@Service
public class WareHousePriorityStrategyService implements IWareHousePriorityStrategyService {

	@Autowired
	private IWareHousePriorityStrategyShopDetailService shopDetailService;

	@Autowired
	private IWareHousePriorityStrategyOnlineDetailService onlineDetailService;

	@Autowired
	private IWareHousePriorityStrategyStoreDetailService storeDetailService;

	@Autowired
	private ConsumerIdService idService;

	@Autowired
	private WareHousePriorityStrategyMapper wareHousePriorityStrategyMapper;

	@Autowired
	private ConsumerAdvancedShopService shopService;

	/**
	 * 店铺关联仓库优先级缓存
	 */
	private Map<Long, Map<Long, WareHousePriority>> shopWareHousePriorityMapCache = new ConcurrentHashMap<>();

	/**
	 * 店铺没有优先级缓存仓库集合
	 */
	private Map<Long, List<Long>> shopWareHousePriorityEmptyMapCache = new ConcurrentHashMap<>();

	private static final String CACHE_FLAG = "WareHousePriorityStrategyServiceCache";

	@Override
	@Transactional
	public ServiceResult create(String token, WareHousePriorityStrategy[] objects) {
		Session session = new Session();
		ServiceResult result = new ServiceResult();
		try {
			session = LoginUtil.checkUserLogin(token);
			if (objects == null || objects.length == 0) {
				result.addErrorObject("", "", "input data is null");
			}

			// id
			BatchIdVo batchIdVo = idService.batchId(objects.length);
			long startId = batchIdVo.getStartId();
			for (WareHousePriorityStrategy object : objects) {
				object.setId(startId);
				startId += batchIdVo.getStep();
			}

			// 店铺明细需要跟主表一起保存
			List<WareHousePriorityStrategyShopDetail> shopDetails = new ArrayList<>();
			for (WareHousePriorityStrategy strategy : objects) {
				strategy.setCreateBy(session.userCode);
				strategy.setCreateDate(new Date());
				strategy.setStatus(0);

				if (strategy.getShopDetails() != null) {
					for (WareHousePriorityStrategyShopDetail detail : strategy.getShopDetails()) {
						detail.setWarehousePriorityId(strategy.getId());
						shopDetails.add(detail);
					}
				}
			}

			handleShopUnique(shopDetails, result);
			if (result.hasError()) {
				return result;
			}

			wareHousePriorityStrategyMapper.batchInsertSelective(Arrays.asList(objects));

			if (shopDetails.size() > 0) {
				shopDetailService.create(token,
						shopDetails.toArray(new WareHousePriorityStrategyShopDetail[shopDetails.size()]));
			}

			result.addSuccessObject(objects);

		} catch (Exception e) {
			e.printStackTrace();
			result.addErrorObject(e, "", e.getMessage());
			throw new ServiceResultErrorException(result);
		}
		ISupportServiceCache.updateVersion(CACHE_FLAG);
		return result;
	}

	@Override
	@Transactional
	public ServiceResult modify(String token, WareHousePriorityStrategy[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}
		// 店铺明细需要跟主表一起保存
		List<WareHousePriorityStrategyShopDetail> addShopDetails = new ArrayList<>();
		List<Long> removeShopDetails = new ArrayList<>();
		for (WareHousePriorityStrategy strategy : objects) {

			E3Selector selector = new E3Selector();
			selector.addFilterField(new E3FilterField("warehousePriorityId", strategy.getId()));
			WareHousePriorityStrategyShopDetail[] shopDetails = shopDetailService.queryPage(token, selector, -1, -1);
			List<Long> shopDetailIds = new ArrayList<>();
			if (shopDetails != null) {
				for (WareHousePriorityStrategyShopDetail detail : shopDetails) {
					shopDetailIds.add(detail.getId());
				}
			}

			List<Long> notDeleteShopIds = new ArrayList<>();
			if (strategy.getShopDetails() != null) {
				for (WareHousePriorityStrategyShopDetail inputDetail : strategy.getShopDetails()) {
					if (inputDetail.getId() == null) {
						inputDetail.setWarehousePriorityId(strategy.getId());
						addShopDetails.add(inputDetail);
						continue;
					}

					if (shopDetailIds.contains(inputDetail.getId())) {
						notDeleteShopIds.add(inputDetail.getId());
					}
				}
			}

			shopDetailIds.removeAll(notDeleteShopIds);

			if (shopDetailIds.size() > 0) {
				removeShopDetails.addAll(shopDetailIds);
			}
		}

		// 店铺明细只能存在一条优先级数据中
		List<Long> shopIds = LinqUtil.select(addShopDetails, s -> s.getShopId());
		List<WareHousePriorityStrategyShopDetail> shopDetails = shopDetailService.queryByShopId(token, shopIds);

		handleShopUnique(shopDetails, result);
		if (result.hasError()) {
			return result;
		}

		wareHousePriorityStrategyMapper.batchUpdateByPrimaryKeySelective(Arrays.asList(objects));
		if (addShopDetails.size() > 0) {
			shopDetailService.create(token,
					addShopDetails.toArray(new WareHousePriorityStrategyShopDetail[addShopDetails.size()]));
		}

		if (removeShopDetails.size() > 0) {
			shopDetailService.remove(token, removeShopDetails.toArray(new Long[removeShopDetails.size()]));
		}

		result.addSuccessObject(objects);

		ISupportServiceCache.updateVersion(CACHE_FLAG);

		return result;
	}

	private void handleShopUnique(List<WareHousePriorityStrategyShopDetail> shopDetails, ServiceResult result) {
		if (shopDetails != null && shopDetails.size() > 0) {
			List<String> errorInfo = new ArrayList<>();
			for (WareHousePriorityStrategyShopDetail shopDetail : shopDetails) {
				WareHousePriorityStrategy data = wareHousePriorityStrategyMapper
						.selectByPrimaryKey(shopDetail.getWarehousePriorityId());
				if (data != null) {
					errorInfo.add(ResourceUtils.get(AdvancedOrderErrorCode.SHOP_EXIST_IN_OTHER_PRIORITY,
							shopDetail.getShopCode(), data.getName()));
				}
			}

			if (errorInfo.size() > 0) {
				result.addErrorObject("", "", errorInfo.toString());
			}
		}
	}

	@Override
	@Transactional
	public ServiceResult remove(String token, Long[] pkIds) {
		ServiceResult result = new ServiceResult();
		if (pkIds == null || pkIds.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}

		wareHousePriorityStrategyMapper.deleteByPrimaryKey(Arrays.asList(pkIds));

		shopDetailService.removeByStrategyId(token, pkIds);
		onlineDetailService.removeByStrategyId(token, pkIds);
		storeDetailService.removeByStrategyId(token, pkIds);

		ISupportServiceCache.updateVersion(CACHE_FLAG);

		return result;
	}

	@Override
	public WareHousePriorityStrategy findById(String token, Long pkId) {

		if (pkId == null) {
			return null;
		}

		WareHousePriorityStrategy data = wareHousePriorityStrategyMapper.selectByPrimaryKey(pkId);

		// 查询店铺明细
		E3Selector selector = new E3Selector();
		selector.addFilterField(new E3FilterField("warehousePriorityId", pkId));
		WareHousePriorityStrategyShopDetail[] shopDetails = shopDetailService.queryPage(token, selector, -1, -1);

		if (shopDetails != null && shopDetails.length > 0) {
			data.setShopDetails(Arrays.asList(shopDetails));
		}

		return data;
	}

	@Override
	public WareHousePriorityStrategy[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {

		WareHousePriorityStrategyExample example = new WareHousePriorityStrategyExample();
		E3FilterField filterField = selector.getFilterFieldByFieldName("name");
		Criteria createCriteria = example.createCriteria();
		if (filterField != null) {
			createCriteria.andNameLike(filterField.getValue().toString());
		}

		filterField = selector.getFilterFieldByFieldName("status");

		if (filterField != null) {
			Object value = filterField.getValue();
			List<Integer> statuList = new ArrayList<>();
			if (value instanceof Object[]) {
				Object[] valueArr = (Object[]) value;
				for (Object o : valueArr) {
					statuList.add(Integer.valueOf(String.valueOf(o)));
				}
			}

			if (statuList.size() > 0) {
				createCriteria.andStatusIn(statuList);
			}
		}

		if (pageIndex >= 0 && pageSize >= 0) {
			PageHelper.startPage(pageIndex + 1, pageSize, false);
		}

		List<WareHousePriorityStrategy> datas = wareHousePriorityStrategyMapper.selectByExample(example);

		List<Long> strategyIds = new ArrayList<>();
		for (WareHousePriorityStrategy data : datas) {
			strategyIds.add(data.getId());
		}

		WareHousePriorityStrategyShopDetail[] shopDetails = shopDetailService.queryPage(token, selector, -1, -1);
		if (shopDetails != null && shopDetails.length > 0) {
			List<WareHousePriorityStrategyShopDetail> shopDetailList = Arrays.asList(shopDetails);
			for (WareHousePriorityStrategy data : datas) {
				data.setShopDetails(
						LinqUtil.where(shopDetailList, f -> f.getWarehousePriorityId().equals(data.getId())));
			}
		}

		return datas.toArray(new WareHousePriorityStrategy[datas.size()]);
	}

	@Override
	public long getListCount(String token, E3Selector selector) {

		WareHousePriorityStrategyExample example = new WareHousePriorityStrategyExample();
		E3FilterField filterField = selector.getFilterFieldByFieldName("name");
		if (filterField != null) {
			example.createCriteria().andNameLike(filterField.getValue().toString());
		}

		filterField = selector.getFilterFieldByFieldName("status");

		if (filterField != null) {
			Object value = filterField.getValue();
			List<Integer> statuList = new ArrayList<>();
			if (value instanceof Object[]) {
				Object[] valueArr = (Object[]) value;
				for (Object o : valueArr) {
					statuList.add(Integer.valueOf(String.valueOf(o)));
				}
			}

			if (statuList.size() > 0) {
				example.createCriteria().andStatusIn(statuList);
			}
		}

		return wareHousePriorityStrategyMapper.getListCount(example);
	}

	@Override
	public WareHousePriority[] queryPriorityByWareHouseAndArea(String token, Long shopId, Long[] wareHouseIds,
			Long areaId, Long cityId, Long proviceId) {

		List<WareHousePriority> resultDatas = new ArrayList<>();
		List<Long> queryWareHouseIds = new ArrayList<>();

		if (areaId == null && cityId == null && proviceId == null) {
			return new WareHousePriority[0];
		}

		Long localVersion = ISupportServiceCache.getLocalVersionCache(CACHE_FLAG);
		if (ISupportServiceCache.checkVersion(localVersion, CACHE_FLAG)) {

			List<Long> emptyWareHouseIds = shopWareHousePriorityEmptyMapCache.get(shopId);
			// 缓存获取
			if (shopWareHousePriorityMapCache.containsKey(shopId)) {
				Map<Long, WareHousePriority> wareHousePriorityMapCache = shopWareHousePriorityMapCache.get(shopId);
				for (Long wareHouseId : wareHouseIds) {
					if (wareHousePriorityMapCache.containsKey(wareHouseId)) {
						resultDatas.add(wareHousePriorityMapCache.get(wareHouseId));
					} else {
						if (emptyWareHouseIds != null && emptyWareHouseIds.contains(wareHouseId)) {
							continue;
						}
						queryWareHouseIds.add(wareHouseId);
					}
				}

				if (wareHousePriorityMapCache
						.containsKey(WareHousePriorityStrategyOnlineDetailService.SAME_PROVINCE_WAREHOUSEID)) {
					resultDatas.add(wareHousePriorityMapCache
							.get(WareHousePriorityStrategyOnlineDetailService.SAME_PROVINCE_WAREHOUSEID));
				}

			} else {
				for (Long wareHouseId : wareHouseIds) {
					if (emptyWareHouseIds != null && emptyWareHouseIds.contains(wareHouseId)) {
						continue;
					}
					queryWareHouseIds.add(wareHouseId);
				}
			}

		} else {
			queryWareHouseIds.addAll(Arrays.asList(wareHouseIds));
		}

		if (queryWareHouseIds.size() == 0) {
			return resultDatas.toArray(new WareHousePriority[resultDatas.size()]);
		}

		Map<Long, WareHousePriority> wareHousePriorityMap = new HashMap<>();
		// 根据商店id找到策略id
		WareHousePriorityStrategyShopDetail shopDetail = shopDetailService.queryByShopId(token, shopId);
		Long strategyId = null;
		if (shopDetail != null) {
			strategyId = shopDetail.getWarehousePriorityId();
			// 策略是否启用
			WareHousePriorityStrategyExample example = new WareHousePriorityStrategyExample();
			example.createCriteria().andIdEqualTo(strategyId).andStatusEqualTo(1);
			Long listCount = wareHousePriorityStrategyMapper.getListCount(example);
			if (listCount == 0) {
				strategyId = null;
			}
		}

		if (strategyId == null) {
			WareHousePriorityStrategyExample example = new WareHousePriorityStrategyExample();
			WareHousePriorityStrategyExample.Criteria criteria = example.createCriteria();
			criteria.andIsAllShopEqualTo(1);
			criteria.andStatusEqualTo(1);
			List<WareHousePriorityStrategy> strategys = wareHousePriorityStrategyMapper.selectByExample(example);
			if (strategys != null && strategys.size() > 0) {
				strategyId = strategys.get(0).getId();
			}
		}

		if (strategyId == null) {
			E3Selector selector = new E3Selector();
			selector.addFilterField(new E3FilterField("shopId", shopId));
			selector.addSelectFields("code");
			Shop[] shop = shopService.queryPageShop(token, selector, -1, -1);
			throw new OrderDistributeCalculateException(AdvancedOrderErrorCode.WAREHOUSE_PRIORITY_NO_SHOP,
					shop[0].getCode());
		}

		// 先按照区域id来查询优先级
		findWareHousePriority(wareHousePriorityMap, strategyId, areaId, 0, queryWareHouseIds);

		// 按照市id来查询优先级
		List<Long> notFindWareHouseIds = findNotFitWareHouseIds(wareHousePriorityMap, queryWareHouseIds);
		if (notFindWareHouseIds.size() > 0) {
			findWareHousePriority(wareHousePriorityMap, strategyId, cityId, 1, notFindWareHouseIds);
		}

		// 按照省id来查询优先级
		notFindWareHouseIds = findNotFitWareHouseIds(wareHousePriorityMap, notFindWareHouseIds);
		if (notFindWareHouseIds.size() > 0) {
			findWareHousePriority(wareHousePriorityMap, strategyId, proviceId, 2, notFindWareHouseIds);
		}

		// 还存在没找到的仓库优先级
		notFindWareHouseIds = findNotFitWareHouseIds(wareHousePriorityMap, notFindWareHouseIds);

		if (!notFindWareHouseIds.isEmpty()) {
			List<Long> emptyWareHouseIds = shopWareHousePriorityEmptyMapCache.get(shopId);
			if (emptyWareHouseIds == null) {
				emptyWareHouseIds = new ArrayList<>();
				shopWareHousePriorityEmptyMapCache.put(shopId, emptyWareHouseIds);
			}
			emptyWareHouseIds.addAll(notFindWareHouseIds);
		}

		// 刷新缓存
		Map<Long, WareHousePriority> wareHousePriorityMapCache = shopWareHousePriorityMapCache.get(shopId);
		if (wareHousePriorityMapCache == null) {
			wareHousePriorityMapCache = new HashMap<>();
			shopWareHousePriorityMapCache.put(shopId, wareHousePriorityMap);
		}

		if (!wareHousePriorityMapCache
				.containsKey(WareHousePriorityStrategyOnlineDetailService.SAME_PROVINCE_WAREHOUSEID)) {
			// 查询同省门店优先级
			List<Long> sameProviceWareHouseIds = new ArrayList<>();
			sameProviceWareHouseIds.add(WareHousePriorityStrategyOnlineDetailService.SAME_PROVINCE_WAREHOUSEID);
			WareHousePriorityStrategyOnlineDetail[] onlineDetails = onlineDetailService
					.queryWareHousePriorityByWareHouseAndArea(strategyId, sameProviceWareHouseIds, proviceId);
			if (onlineDetails != null && onlineDetails.length > 0) {
				WareHousePriority priority = new WareHousePriority();
				priority.setAreaId(areaId);
				priority.setWarehouseId(WareHousePriorityStrategyOnlineDetailService.SAME_PROVINCE_WAREHOUSEID);
				priority.setPriority(onlineDetails[0].getPriority());
				wareHousePriorityMap.put(WareHousePriorityStrategyOnlineDetailService.SAME_PROVINCE_WAREHOUSEID,
						priority);
			}
		}

		wareHousePriorityMapCache.putAll(wareHousePriorityMap);
		ISupportServiceCache.setLocalVersionCache(ISupportServiceCache.getRemoteVersion(CACHE_FLAG), CACHE_FLAG);

		resultDatas.addAll(wareHousePriorityMap.values());

		return resultDatas.toArray(new WareHousePriority[resultDatas.size()]);
	}

	private void findWareHousePriority(Map<Long, WareHousePriority> wareHousePriorityMap, Long strategyId, Long areaId,
			Integer type, List<Long> wareHouseIds) {

		WareHousePriorityStrategyOnlineDetail[] onlineDetails = onlineDetailService
				.queryWareHousePriorityByWareHouseAndArea(strategyId, wareHouseIds, areaId);

		List<Long> notFindWareHouseIds = new ArrayList<>();
		if (onlineDetails != null) {
			for (Long wareHouseId : wareHouseIds) {
				WareHousePriorityStrategyOnlineDetail detail = findWareHousePriorityDetail(onlineDetails, wareHouseId);
				if (detail != null) {
					WareHousePriority priority = new WareHousePriority();
					priority.setAreaId(areaId);
					priority.setWarehouseId(wareHouseId);
					priority.setPriority(detail.getPriority());
					wareHousePriorityMap.put(wareHouseId, priority);
				} else {
					notFindWareHouseIds.add(wareHouseId);
				}
			}
		} else {
			notFindWareHouseIds.addAll(wareHouseIds);
		}

		if (notFindWareHouseIds.size() > 0) {
			WareHousePriorityStrategyStoreDetail[] storeDetails = null;

			switch (type) {
			case 0:
				storeDetails = storeDetailService.queryWareHousePriorityByWareHouseAndArea(strategyId,
						notFindWareHouseIds, null, null, areaId);
				break;
			case 1:
				storeDetails = storeDetailService.queryWareHousePriorityByWareHouseAndArea(strategyId,
						notFindWareHouseIds, null, areaId, null);
				break;
			case 2:
				storeDetails = storeDetailService.queryWareHousePriorityByWareHouseAndArea(strategyId,
						notFindWareHouseIds, areaId, null, null);
			}

			if (storeDetails != null) {
				for (Long wareHouseId : notFindWareHouseIds) {
					WareHousePriorityStrategyStoreDetail detail = findWareHousePriorityDetail(storeDetails,
							wareHouseId);
					if (detail != null) {
						WareHousePriority priority = new WareHousePriority();
						priority.setAreaId(areaId);
						priority.setWarehouseId(wareHouseId);
						priority.setPriority(detail.getPriority());
						wareHousePriorityMap.put(wareHouseId, priority);
					}
				}
			}
		}

	}

	private WareHousePriorityStrategyStoreDetail findWareHousePriorityDetail(
			WareHousePriorityStrategyStoreDetail[] storeDetails, Long wareHouseId) {

		for (WareHousePriorityStrategyStoreDetail detail : storeDetails) {
			if (detail.getWarehouseId().equals(wareHouseId)) {
				return detail;
			}
		}

		return null;
	}

	private WareHousePriorityStrategyOnlineDetail findWareHousePriorityDetail(
			WareHousePriorityStrategyOnlineDetail[] onlineDetails, Long wareHouseId) {

		for (WareHousePriorityStrategyOnlineDetail detail : onlineDetails) {
			if (detail.getWarehouseId().equals(wareHouseId.intValue())) {
				return detail;
			}
		}

		return null;
	}

	private List<Long> findNotFitWareHouseIds(Map<Long, WareHousePriority> wareHousePriorityMap,
			List<Long> wareHouseIds) {

		List<Long> result = new ArrayList<>();
		for (Long wareHouseId : wareHouseIds) {
			if (!wareHousePriorityMap.containsKey(wareHouseId)) {
				result.add(wareHouseId);
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baison.e3.middleware.order.api.service.
	 * IWareHousePriorityStrategyService#enable(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public ServiceResult enable(String token, Long strategyId) {
		ServiceResult result = new ServiceResult();
		if (strategyId == null) {
			result.addErrorObject("", "", "data is empty");
			return result;
		}

		WareHousePriorityStrategy strategy = wareHousePriorityStrategyMapper.selectByPrimaryKey(strategyId);
		if (strategy != null) {
			if (strategy.getStatus() == 1) { // 已启用，不再启用
				return result;
			}
			// 如果是全部商店策略，则只允许一个被启用
			if (strategy.getIsAllShop() != null && strategy.getIsAllShop() == 1) {
				WareHousePriorityStrategyExample example = new WareHousePriorityStrategyExample();
				example.createCriteria().andIsAllShopEqualTo(1).andStatusEqualTo(1);
				Long count = wareHousePriorityStrategyMapper.getListCount(example);
				if (count > 0) {
					result.addErrorObject("", "", ResourceUtils.get(AdvancedOrderErrorCode.WAREHOUSE_PRIORITY_EXIST));
					return result;
				}
			}

			strategy.setStatus(1);

			wareHousePriorityStrategyMapper.updateByPrimaryKeySelective(strategy);

		}

		ISupportServiceCache.updateVersion(CACHE_FLAG);
		return result;
	}

	@Override
	public ServiceResult disable(String token, Long strategyId) {
		ServiceResult result = new ServiceResult();
		if (strategyId == null) {
			result.addErrorObject("", "", "data is empty");
			return result;
		}

		WareHousePriorityStrategy strategy = wareHousePriorityStrategyMapper.selectByPrimaryKey(strategyId);
		if (strategy != null) {
			strategy.setStatus(2);

			wareHousePriorityStrategyMapper.updateByPrimaryKeySelective(strategy);

		}

		ISupportServiceCache.updateVersion(CACHE_FLAG);
		return result;
	}
}
