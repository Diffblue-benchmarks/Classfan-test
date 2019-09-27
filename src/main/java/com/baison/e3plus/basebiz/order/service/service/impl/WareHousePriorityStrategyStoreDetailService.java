package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdministrationAreaWithFashionService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyStoreDetail;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyStoreDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.WareHousePriorityStrategyStoreDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyStoreDetailExample;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyStoreDetailExample.Criteria;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.biz.support.api.publicrecord.model.administrationarea.AdministrationArea;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.cncore.common.ResultUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.restful.result.Result;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.session.Session;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

@Service
public class WareHousePriorityStrategyStoreDetailService implements IWareHousePriorityStrategyStoreDetailService {

	@Autowired
	private ConsumerIdService idService;

	/**
	 * 修改只针对于数据源的切换方案
	 *
	 * @author qiancheng.chen start
	 * @since 2018-12-07
	 */
	@Autowired
	private WareHousePriorityStrategyStoreDetailMapper wareHousePriorityStrategyStoreDetailMapper;

	@Autowired
	private ConsumerAdministrationAreaWithFashionService administrateService;

	@Autowired
	private ConsumerAdvancedWareHouseService warehouseService;

	@Override
	public ServiceResult create(String token, WareHousePriorityStrategyStoreDetail[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}

		// id
		BatchIdVo batchIdVo = idService.batchId(objects.length);
		long startId = batchIdVo.getStartId();
		for (WareHousePriorityStrategyStoreDetail object : objects) {
			if (validUnique(object)) {
				String errInfo = ResourceUtils.get(AdvancedOrderErrorCode.WAREHOUSE_PRIORITY_STORE_VALID_UNIQUE);
				result.addErrorObject("", AdvancedOrderErrorCode.WAREHOUSE_PRIORITY_STORE_VALID_UNIQUE, errInfo);
				return result;
			}
			object.setId(startId);
			startId += batchIdVo.getStep();
		}
		wareHousePriorityStrategyStoreDetailMapper.batchInsertSelective(Arrays.asList(objects));
		result.addSuccessObject(objects);

		return result;
	}

	private boolean validUnique(WareHousePriorityStrategyStoreDetail wareHousePriorityStrategyStoreDetail) {
		int result = wareHousePriorityStrategyStoreDetailMapper.validUnique(wareHousePriorityStrategyStoreDetail);
		return result > 0;
	}

	@Override
	public ServiceResult modify(String token, WareHousePriorityStrategyStoreDetail[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}
		wareHousePriorityStrategyStoreDetailMapper.batchUpdateByPrimaryKeySelective(Arrays.asList(objects));

		result.addSuccessObject(objects);

		return result;
	}

	@Override
	public ServiceResult remove(String token, Long[] pkIds) {
		ServiceResult result = new ServiceResult();
		if (pkIds == null || pkIds.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}
		wareHousePriorityStrategyStoreDetailMapper.deleteByPrimaryKey(Arrays.asList(pkIds));

		return result;
	}

	@Override
	public WareHousePriorityStrategyStoreDetail findById(String token, Long pkId) {
		return wareHousePriorityStrategyStoreDetailMapper.selectByPrimaryKey(pkId);
	}

	@Override
	public WareHousePriorityStrategyStoreDetail[] queryPage(String token, E3Selector selector, int pageSize,
			int pageIndex) {
		WareHousePriorityStrategyStoreDetailExample example = new WareHousePriorityStrategyStoreDetailExample();

		E3FilterField filterField = selector.getFilterFieldByFieldName("warehousePriorityId");
		if (filterField != null) {
			example.createCriteria().andWarehousePriorityIdEqualTo(Long.parseLong(filterField.getValue().toString()));
		}

		filterField = selector.getFilterFieldByFieldName("warehouseId");
		if (filterField != null) {
			example.createCriteria().andWarehouseIdEqualTo(Integer.parseInt(filterField.getValue().toString()));
		}

		filterField = selector.getFilterFieldByFieldName("areaId");
		if (filterField != null) {
			example.createCriteria().andAreaIdEqualTo(Integer.parseInt(filterField.getValue().toString()));
		}

		if (pageIndex >= 0 && pageSize >= 0) {
			PageHelper.startPage(pageIndex + 1, pageSize, false);
		}

		List<WareHousePriorityStrategyStoreDetail> datas = wareHousePriorityStrategyStoreDetailMapper
				.selectByExample(example);

		queryWareHouseAndArea(token, datas);

		if (datas != null) {
			return datas.toArray(new WareHousePriorityStrategyStoreDetail[datas.size()]);
		}

		return null;
	}

	private void queryWareHouseAndArea(String token, List<WareHousePriorityStrategyStoreDetail> datas) {

		List<Long> wareHouseIds = new ArrayList<>();
		List<Long> administrationIds = new ArrayList<>();

		for (WareHousePriorityStrategyStoreDetail detail : datas) {
			if (detail.getWarehouseId() != null) {
				wareHouseIds.add(detail.getWarehouseId());
			}
			if (detail.getCountryId() != null) {
				administrationIds.add(detail.getCountryId());
			}
			if (detail.getProvinceId() != null) {
				administrationIds.add(detail.getProvinceId());
			}
			if (detail.getCityId() != null) {
				administrationIds.add(detail.getCityId());
			}
			if (detail.getAreaId() != null) {
				administrationIds.add(detail.getAreaId());
			}
		}

		Map<Long, WareHouse> warehouseMap = new HashMap<>();
		if (wareHouseIds.size() > 0) {
			E3Selector selector = new E3Selector();
			selector.addFilterField(new E3FilterField("wareHouseId", wareHouseIds));
			selector.addSelectFields("code");
			selector.addSelectFields("name");
			WareHouse[] warehouses = warehouseService.queryObject(token, selector);
			if (warehouses != null) {
				for (WareHouse warehouse : warehouses) {
					warehouseMap.put(warehouse.getWareHouseId(), warehouse);
				}
			}
		}

		Map<Long, AdministrationArea> administrationMap = new HashMap<>();
		if (administrationIds.size() > 0) {
			E3Selector selector = new E3Selector();
			selector.addFilterField(new E3FilterField("administrationAreaId", administrationIds));
			selector.addSelectFields("code");
			selector.addSelectFields("name");
			AdministrationArea[] administrationAreas = administrateService.queryPageAdministrationArea(token, selector,
					-1, -1);
			if (administrationAreas != null) {
				for (AdministrationArea area : administrationAreas) {
					administrationMap.put(area.getAdministrationAreaId(), area);
				}
			}
		}

		for (WareHousePriorityStrategyStoreDetail detail : datas) {
			if (warehouseMap.containsKey(detail.getWarehouseId())) {
				detail.setWarehouse(warehouseMap.get(detail.getWarehouseId()));
			}
			if (administrationMap.containsKey(detail.getCountryId())) {
				detail.setCountry(administrationMap.get(detail.getCountryId()));
			}
			if (administrationMap.containsKey(detail.getProvinceId())) {
				detail.setProvince(administrationMap.get(detail.getProvinceId()));
			}
			if (administrationMap.containsKey(detail.getCityId())) {
				detail.setCity(administrationMap.get(detail.getCityId()));
			}
			if (administrationMap.containsKey(detail.getAreaId())) {
				detail.setArea(administrationMap.get(detail.getAreaId()));
			}
		}

	}

	@Override
	public long getListCount(String token, E3Selector selector) {
		WareHousePriorityStrategyStoreDetailExample example = new WareHousePriorityStrategyStoreDetailExample();

		E3FilterField filterField = selector.getFilterFieldByFieldName("warehousePriorityId");
		if (filterField != null) {
			example.createCriteria().andWarehousePriorityIdEqualTo(Long.parseLong(filterField.getValue().toString()));
		}

		filterField = selector.getFilterFieldByFieldName("warehouseId");
		if (filterField != null) {
			example.createCriteria().andWarehouseIdEqualTo(Integer.parseInt(filterField.getValue().toString()));
		}

		filterField = selector.getFilterFieldByFieldName("areaId");
		if (filterField != null) {
			example.createCriteria().andAreaIdEqualTo(Integer.parseInt(filterField.getValue().toString()));
		}

		return wareHousePriorityStrategyStoreDetailMapper.getListCount(example);
	}

	@Override
	public ServiceResult removeByStrategyId(String token, Long[] strategyIds) {
		ServiceResult result = new ServiceResult();
		if (strategyIds == null || strategyIds.length == 0) {
			result.addErrorObject("", "", "input data is null");
		}

		wareHousePriorityStrategyStoreDetailMapper.removeByStrategyId(Arrays.asList(strategyIds));

		return result;
	}

	@Override
	public WareHousePriorityStrategyStoreDetail[] queryWareHousePriorityByWareHouseAndArea(Long strategyId,
			List<Long> wareHouseIds, Long proviceId, Long cityId, Long areaId) {

		WareHousePriorityStrategyStoreDetailExample example = new WareHousePriorityStrategyStoreDetailExample();

		List<Integer> intValues = new ArrayList<>();
		for (Long value : wareHouseIds) {
			intValues.add(value.intValue());
		}

		Criteria andWarehouseIdIn = example.createCriteria().andWarehousePriorityIdEqualTo(strategyId).andWarehouseIdIn(intValues);
				
		if (areaId != null) {
			andWarehouseIdIn.andAreaIdEqualTo(areaId.intValue());
		}
		if (cityId != null) {
			andWarehouseIdIn.andCityIdEqualTo(cityId.intValue());
		}
		if (proviceId != null) {
			andWarehouseIdIn.andProvinceIdEqualTo(proviceId.intValue());
		}

		List<WareHousePriorityStrategyStoreDetail> datas = wareHousePriorityStrategyStoreDetailMapper
				.selectByExample(example);

		if (datas != null) {

			queryWareHouseAndArea(Session.ADMINTOKEN, datas);

			return datas.toArray(new WareHousePriorityStrategyStoreDetail[datas.size()]);
		}

		return null;
	}

	@Override
	@Transactional
	public ServiceResult deleteByPrimayKey(String token, Long pid) {
		ServiceResult result = new ServiceResult();
		if (pid == null) {
			result.addErrorObject("", "", "input data is null");
			return result;
		}

		wareHousePriorityStrategyStoreDetailMapper.deleteByPrimayKey(pid);

		return result;
	}

	@Override
	@Transactional
	public String importData(String token, List<WareHousePriorityStrategyStoreDetail> details, Long pid) {
		ServiceResult result = new ServiceResult();
		if (details == null || details.size() == 0) {
			result.addErrorObject("", "", "input data is null");
			return JSON.toJSONString(result);
		}

		// 保存
		List<List<WareHousePriorityStrategyStoreDetail>> lists = Lists.partition(details, 1000);
		for (List<WareHousePriorityStrategyStoreDetail> list : lists) {
			this.create(token, list.toArray(new WareHousePriorityStrategyStoreDetail[list.size()]));
		}

		return JSON.toJSONString(ResultUtil.handleResult(result, new Result()));
	}
}
