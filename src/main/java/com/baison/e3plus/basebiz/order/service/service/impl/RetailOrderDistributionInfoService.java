package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.order.api.model.RetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderDistributionInfoService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailOrderDistributionInfoMapper;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdministrationAreaWithFashionService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerDeliveryTypeService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerVirtualWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerWhareaTypeService;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.virtualwarehouse.VirtualWareHouse;
import com.baison.e3plus.biz.support.api.delivery.DeliveryType;
import com.baison.e3plus.biz.support.api.publicrecord.model.administrationarea.AdministrationArea;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.biz.support.api.publicrecord.model.whareatype.WhareaType;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 
 * @author cong
 *
 */
@Service
public class RetailOrderDistributionInfoService implements IRetailOrderDistributionInfoService {

	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen start
	 * @since 2018-12-07
	 */
	@Autowired
	private ConsumerIdService idService;

	@Autowired
	private ConsumerAdvancedWareHouseService wareHouseService;

	@Autowired
	private ConsumerWhareaTypeService whareaTypeService;

	@Autowired
	private ConsumerDeliveryTypeService deliveryTypeService;

	@Autowired
	private ConsumerAdministrationAreaWithFashionService areaService;

	@Autowired
	private ConsumerVirtualWareHouseService virtualWareHouseService;

	@Autowired
	private RetailOrderDistributionInfoMapper retailOrderDistributionInfoMapper;

	@Override
	@Transactional
	public ServiceResult create(String token, RetailOrderDistributionInfo[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "no data input!");
		}

		if (result.hasError()) {
			return result;
		}

		// retailorderid不能为空
		for (RetailOrderDistributionInfo detail : objects) {
			if (detail.getRetailOrderBillId() == null) {
				result.addErrorObject("", "", "order id is null!");
			}
		}

		if (result.hasError()) {
			return result;
		}

		for (RetailOrderDistributionInfo detail : objects) {
			detail.setId(idService.nextId());
		}

		retailOrderDistributionInfoMapper.insertSelective(Arrays.asList(objects));

		return result;
	}

	@Override
	@Transactional
	public ServiceResult modify(String token, RetailOrderDistributionInfo[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "no data input!");
		}

		if (result.hasError()) {
			return result;
		}
		
		retailOrderDistributionInfoMapper.updateByPrimaryKeySelective(Arrays.asList(objects));
		return result;
	}
	
	@Override
	@Transactional
	public ServiceResult saveAndmodify(String token, List<RetailOrderDistributionInfo> objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.size() == 0) {
			result.addErrorObject("", "", "no data input!");
		}
		
		if (result.hasError()) {
			return result;
		}
		
		List<String> billNoList = LinqUtil.select(objects, s->s.getBillNo());
		List<RetailOrderDistributionInfo> dbInfos = retailOrderDistributionInfoMapper.selectByBillNo(billNoList);
		
		List<RetailOrderDistributionInfo> createObjecs = new ArrayList<>(); 
		List<RetailOrderDistributionInfo> updateObjecs = new ArrayList<>(); 
		
		for(RetailOrderDistributionInfo object:objects){
			RetailOrderDistributionInfo info = LinqUtil.first(dbInfos, f->f.getBillNo().equals(object.getBillNo()));
			if(info == null) {
				object.setRetailOrderBillId(idService.nextId());
				createObjecs.add(object);
			} else {
				object.setRetailOrderBillId(info.getRetailOrderBillId());
				updateObjecs.add(object);
			}
		}
		
		if(!createObjecs.isEmpty()) {
			this.create(token, createObjecs.toArray(new RetailOrderDistributionInfo[0]));
		}
		
		if(!updateObjecs.isEmpty()) {
			this.modify(token, updateObjecs.toArray(new RetailOrderDistributionInfo[0]));
		}
		
		return result;
	}
	
	@Override
	@Transactional
	public ServiceResult remove(String token, Object[] pkIds) {
		ServiceResult result = new ServiceResult();

		if (pkIds == null || pkIds.length == 0) {
			result.addErrorObject("", "", "pkid is null!");

			return result;
		}

		retailOrderDistributionInfoMapper.deleteByPrimaryKey(Arrays.asList(pkIds));

		return result;
	}

	@Override
	public RetailOrderDistributionInfo[] findById(String token, Object[] pkIds, String[] selectFields) {
		if (pkIds == null || pkIds.length == 0) {
			return null;
		}

		String fields = null;
		if (selectFields != null) {
			StringBuilder sb = new StringBuilder("id");
			for (String field : selectFields) {
				sb.append(",").append(field);
			}
			fields = sb.toString();
		}

		List<RetailOrderDistributionInfo> details = retailOrderDistributionInfoMapper
				.selectByPrimaryKey(Arrays.asList(pkIds), fields);

		queryBaseField(token, details);

		return details.toArray(new RetailOrderDistributionInfo[details.size()]);
	}

	@Override
	public RetailOrderDistributionInfo[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {
		Map<String, Object> args = new HashMap<>();
		for (E3FilterField field : selector.getFilterFields()) {
			args.put(field.getFieldName(), field.getValue());
		}

		if (pageSize > 0 && pageIndex >= 0) {
			int stratRow = pageIndex * pageSize;

			args.put("stratRow", stratRow);
			args.put("endRow", pageSize);
		}

		List<RetailOrderDistributionInfo> datas = retailOrderDistributionInfoMapper.queryPage(args);

		queryBaseField(token, datas);

		return datas.toArray(new RetailOrderDistributionInfo[datas.size()]);
	}

	private void queryBaseField(String token, List<RetailOrderDistributionInfo> datas) {

		// 仓库id
		List<Long> warehouseIds = new ArrayList<>();
		// 库位id
		List<Long> whareaTypeIds = new ArrayList<>();
		// 区域id
		List<Long> areaIds = new ArrayList<>();
		// 配送方式id
		List<Long> deliveryTypeIds = new ArrayList<>();
		// 分配池id
		List<Long> virtualWareHouseIds = new ArrayList<>();

		for (RetailOrderDistributionInfo info : datas) {
			if (info.getCityId() != null) {
				areaIds.add(info.getCityId());
			}
			if (info.getDistrictId() != null) {
				areaIds.add(info.getDistrictId());
			}
			if (info.getCountryId() != null) {
				areaIds.add(info.getCountryId());
			}
			if (info.getProvinceId() != null) {
				areaIds.add(info.getProvinceId());
			}
			if (info.getDeliveryTypeId() != null) {
				deliveryTypeIds.add(info.getDeliveryTypeId());
			}
			if (info.getWarehouseOutId() != null) {
				warehouseIds.add(info.getWarehouseOutId());
			}
			if (info.getWareHouseDefaultId() != null) {
				warehouseIds.add(info.getWareHouseDefaultId());
			}
			if (info.getWareHouseRealId() != null) {
				warehouseIds.add(info.getWareHouseRealId());
			}
			if (info.getWhAreaTypeOutId() != null) {
				whareaTypeIds.add(info.getWhAreaTypeOutId());
			}
			if (info.getVirtualWareHouseId() != null) {
				virtualWareHouseIds.add(info.getVirtualWareHouseId());
			}
		}

		E3Selector selector = new E3Selector();
		E3FilterField field = new E3FilterField("", "");
		selector.addFilterField(field);
		selector.addSelectFields("code");
		selector.addSelectFields("name");

		// 仓库
		if (warehouseIds.size() > 0) {
			field.setFieldName("warehouseId");
			field.setValue(warehouseIds);
			WareHouse[] warehouses = wareHouseService.queryObject(token, selector);
			if (warehouses != null && warehouses.length > 0) {
				Map<Long, WareHouse> mapDatas = new HashMap<>();
				for (WareHouse warehouse : warehouses) {
					mapDatas.put(warehouse.getWareHouseId(), warehouse);
				}
				datas.forEach(c -> {
					c.setWarehouseOut(mapDatas.get(c.getWarehouseOutId()));
					c.setWareHouseDefault(mapDatas.get(c.getWareHouseDefaultId()));
					c.setWareHouseReal(mapDatas.get(c.getWareHouseRealId()));
				});
			}
		}

		// 库位
		if (whareaTypeIds.size() > 0) {
			field.setFieldName("whareaTypeId");
			field.setValue(whareaTypeIds);
			WhareaType[] whareaTypes = whareaTypeService.queryWhareaType(token, selector);
			if (whareaTypes != null && whareaTypes.length > 0) {
				Map<Long, WhareaType> mapDatas = new HashMap<>();
				for (WhareaType whareaType : whareaTypes) {
					mapDatas.put(whareaType.getWhareaTypeId(), whareaType);
				}
				datas.forEach(c -> {
					c.setWhAreaTypeOut(mapDatas.get(c.getWhAreaTypeOutId()));
				});
			}
		}

		// 库位
		if (areaIds.size() > 0) {
			field.setFieldName("administrationAreaId");
			field.setValue(areaIds);
			AdministrationArea[] areas = areaService.queryPageAdministrationArea(token, selector, -1, -1);
			if (areas != null && areas.length > 0) {
				Map<Long, AdministrationArea> mapDatas = new HashMap<>();
				for (AdministrationArea area : areas) {
					mapDatas.put(area.getAdministrationAreaId(), area);
				}
				datas.forEach(c -> {
					c.setCity(mapDatas.get(c.getCityId()));
					c.setCountry(mapDatas.get(c.getCountryId()));
					c.setProvince(mapDatas.get(c.getProvinceId()));
					c.setDistrict(mapDatas.get(c.getDistrictId()));
				});
			}
		}

		// 配送方式
		if (deliveryTypeIds.size() > 0) {
			field.setFieldName("id");
			field.setValue(deliveryTypeIds);
			DeliveryType[] deliveryTypes = deliveryTypeService.queryObject(token, selector);
			if (deliveryTypes != null && deliveryTypes.length > 0) {
				Map<Long, DeliveryType> mapDatas = new HashMap<>();
				for (DeliveryType deliveryType : deliveryTypes) {
					mapDatas.put(deliveryType.getId(), deliveryType);
				}
				datas.forEach(c -> {
					c.setDeliveryType(mapDatas.get(c.getDeliveryTypeId()));
				});
			}
		}

		// 分配池
		if (virtualWareHouseIds.size() > 0) {
			field.setFieldName("virtualWareHouseId");
			field.setValue(virtualWareHouseIds);
			VirtualWareHouse[] virtualWareHouses = virtualWareHouseService.queryObject(token, selector);
			if (virtualWareHouses != null && virtualWareHouses.length > 0) {
				Map<Long, VirtualWareHouse> mapDatas = new HashMap<>();
				for (VirtualWareHouse virtualWareHouse : virtualWareHouses) {
					mapDatas.put(virtualWareHouse.getVirtualWareHouseId(), virtualWareHouse);
				}
				datas.forEach(c -> {
					c.setVirtualWareHouse(mapDatas.get(c.getVirtualWareHouseId()));
				});
			}
		}
	}

	@Override
	public long getListCount(String token, E3Selector selector) {
		Map<String, Object> args = new HashMap<>();
		for (E3FilterField field : selector.getFilterFields()) {
			args.put(field.getFieldName(), field.getValue());
		}

		return retailOrderDistributionInfoMapper.getListCount(args);
	}

	@Override
	@Transactional
	public ServiceResult removeByRetailOrderId(String token, Object[] retailOrderIds) {
		ServiceResult result = new ServiceResult();

		if (retailOrderIds == null || retailOrderIds.length == 0) {
			result.addErrorObject("", "", "pkid is null!");

			return result;
		}

		retailOrderDistributionInfoMapper.deleteByRetailOrderId(Arrays.asList(retailOrderIds));

		return result;
	}

	@Override
	@Transactional
	public void updateByBillNo(String billNo,String receiver, String tel, String receiverTel,String key) {
		retailOrderDistributionInfoMapper.updateByBillNo(billNo, receiver, tel, receiverTel, key);
	}
	

}
