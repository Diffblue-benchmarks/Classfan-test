package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedShopService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyShopDetail;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyShopDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.WareHousePriorityStrategyShopDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyShopDetailExample;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.shop.AdvancedShop;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;

@Service
public class WareHousePriorityStrategyShopDetailService implements IWareHousePriorityStrategyShopDetailService {

	@Autowired
	private ConsumerAdvancedShopService shopService;

	@Autowired
	private ConsumerIdService idService;

	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen start
	 * @since 2018-12-07
	 */
	@Autowired
	private WareHousePriorityStrategyShopDetailMapper wareHousePriorityStrategyShopDetailMapper;

	@Override
	public ServiceResult create(String token, WareHousePriorityStrategyShopDetail[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}

		// id
		BatchIdVo batchIdVo = idService.batchId(objects.length);
		long startId = batchIdVo.getStartId();
		for (WareHousePriorityStrategyShopDetail object : objects) {
			object.setId(startId);
			startId += batchIdVo.getStep();
		}

		wareHousePriorityStrategyShopDetailMapper.batchInsertSelective(Arrays.asList(objects));

		result.addSuccessObject(objects);

		return result;
	}

	@Override
	public ServiceResult modify(String token, WareHousePriorityStrategyShopDetail[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}
		wareHousePriorityStrategyShopDetailMapper.batchUpdateByPrimaryKeySelective(Arrays.asList(objects));

		result.addSuccessObject(objects);

		return result;
	}

	@Override
	public ServiceResult remove(String token, Long[] pkIds) {

		ServiceResult result = new ServiceResult();
		if (pkIds == null || pkIds.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}
		wareHousePriorityStrategyShopDetailMapper.deleteByPrimaryKey(Arrays.asList(pkIds));

		return result;
	}

	@Override
	public WareHousePriorityStrategyShopDetail findById(String token, Long pkId) {
		return wareHousePriorityStrategyShopDetailMapper.selectByPrimaryKey(pkId);
	}

	@Override
	public WareHousePriorityStrategyShopDetail[] queryPage(String token, E3Selector selector, int pageSize,
			int pageIndex) {

		WareHousePriorityStrategyShopDetailExample example = new WareHousePriorityStrategyShopDetailExample();

		E3FilterField filterField = selector.getFilterFieldByFieldName("warehousePriorityId");
		if (filterField != null) {
			example.createCriteria().andWarehousePriorityIdEqualTo((Long) filterField.getValue());
		}

		filterField = selector.getFilterFieldByFieldName("shopId");
		if (filterField != null) {
			example.createCriteria().andShopIdEqualTo(Long.parseLong(filterField.getValue().toString()));
		}

		filterField = selector.getFilterFieldByFieldName("shopCode");
		if (filterField != null) {
			example.createCriteria().andShopCodeEqualTo((String) filterField.getValue());
		}

		if (pageIndex >= 0 && pageSize >= 0) {
			PageHelper.startPage(pageIndex + 1, pageSize, false);
		}

		List<WareHousePriorityStrategyShopDetail> datas = wareHousePriorityStrategyShopDetailMapper.selectByExample(example);

		if (datas != null) {

			// 查询店铺对象
			queryShopInfo(token, datas);

			return datas.toArray(new WareHousePriorityStrategyShopDetail[datas.size()]);
		}

		return null;
	}

	private void queryShopInfo(String token, List<WareHousePriorityStrategyShopDetail> datas) {

		if (datas == null || datas.size() == 0) {
			return;
		}
		
		List<Long> shopIds = new ArrayList<>();
		for (WareHousePriorityStrategyShopDetail detail : datas) {
			shopIds.add(detail.getShopId());
		}

		E3Selector selector = new E3Selector();
		selector.addFilterField(new E3FilterField("shopId", shopIds));
		selector.addSelectFields("code");
		selector.addSelectFields("name");
		Shop[] shops = shopService.queryPageShop(token, selector, -1, -1);
		Map<Long, Shop> shopMap = new HashMap<>();

		if (shops != null) {
			for (Shop shop : shops) {
				shopMap.put(shop.getShopId(), shop);
			}
		}

		for (WareHousePriorityStrategyShopDetail detail : datas) {
			detail.setShop((AdvancedShop) shopMap.get(detail.getShopId()));
		}

	}

	@Override
	public long getListCount(String token, E3Selector selector) {
		return 0;
	}

	@Override
	public ServiceResult removeByStrategyId(String token, Long[] strategyIds) {

		ServiceResult result = new ServiceResult();
		if (strategyIds == null || strategyIds.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}

		wareHousePriorityStrategyShopDetailMapper.removeByStrategyId(Arrays.asList(strategyIds));

		return result;
	}

	@Override
	public WareHousePriorityStrategyShopDetail queryByShopId(String token, Long shopId) {

		WareHousePriorityStrategyShopDetailExample example = new WareHousePriorityStrategyShopDetailExample();
		example.createCriteria().andShopIdEqualTo(shopId);

		List<WareHousePriorityStrategyShopDetail> datas = wareHousePriorityStrategyShopDetailMapper.selectByExample(example);

		queryShopInfo(token, datas);

		if (datas != null && datas.size() > 0) {
			return datas.get(0);
		}

		return null;
	}
	
	@Override
	public List<WareHousePriorityStrategyShopDetail> queryByShopId(String token, List<Long> shopIds) {

		if(shopIds != null && shopIds.size() > 0){
			WareHousePriorityStrategyShopDetailExample example = new WareHousePriorityStrategyShopDetailExample();
			example.createCriteria().andShopIdIn(shopIds);

			List<WareHousePriorityStrategyShopDetail> datas = wareHousePriorityStrategyShopDetailMapper.selectByExample(example);

			queryShopInfo(token, datas);
			return datas;
		}

		return null;
	}
	
	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen end
	 * @since 2018-12-07
	 */
}
