package com.baison.e3plus.basebiz.order.service.service.impl.odsbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerShopRefusalOperateStockService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.biz.sales.api.model.refusalstock.ShopRefusalOpeStockParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.order.api.model.ODSShopRefusalRecord;
import com.baison.e3plus.basebiz.order.api.service.odsbase.IODSShopRefusalRecordService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.ODSShopRefusalRecordMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.ODSShopRefusalRecordExample;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;

@Service
public class ODSShopRefusalRecordService implements IODSShopRefusalRecordService {
	@Autowired
	private ConsumerIdService idService;
	@Autowired
	private ODSShopRefusalRecordMapper odsShopRefusalRecordMapper;
	@Autowired
	private ConsumerShopRefusalOperateStockService shopRefusalOperateStockService;

	@Override
	@Transactional
	public ServiceResult createObject(String token, ODSShopRefusalRecord[] beans) {
		ServiceResult result = new ServiceResult();
		if (beans == null || beans.length == 0) {
			result.addErrorObject("", "", "no input data");
			return result;
		}

		BatchIdVo batchIdVo = idService.batchId(beans.length);
		long startId = batchIdVo.getStartId();
		for (ODSShopRefusalRecord bean : beans) {
			bean.setId(startId);
			// 给创建时间赋值
			bean.setCreateDate(new Date());
			startId += batchIdVo.getStep();
		}
		odsShopRefusalRecordMapper.insertBatch(beans);

		List<ShopRefusalOpeStockParam> stockParams = new ArrayList<>();
		for (ODSShopRefusalRecord bean : beans) {
			ShopRefusalOpeStockParam param = new ShopRefusalOpeStockParam(bean.getOrderBillNo(), bean.getWareHouseId(),
					bean.getWareHouseCode(), bean.getSingleProductCode());
			stockParams.add(param);
		}
		ServiceResult rs = shopRefusalOperateStockService.operateStock(token,
				stockParams.toArray(new ShopRefusalOpeStockParam[stockParams.size()]));
		if (rs.hasError()) {
			return rs;
		}

		result.addSuccessObject(beans);
		return result;

	}

	@Override
	@Transactional
	public ServiceResult modifyObject(String token, ODSShopRefusalRecord[] beans) {
		ServiceResult result = new ServiceResult();
		if (beans == null || beans.length == 0) {
			result.addErrorObject("", "", "no input data");
			return result;
		}
		ODSShopRefusalRecord[] odsShopRefusalRecord = Arrays.stream(beans).sorted(Comparator.comparing(t -> t.getId()))
				.toArray(ODSShopRefusalRecord[]::new);
		odsShopRefusalRecordMapper.updateByPrimaryKeySelectiveBatch(odsShopRefusalRecord);
		result.addSuccessObject(odsShopRefusalRecord);
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
		odsShopRefusalRecordMapper.deleteByPrimaryKeyBatch(idArr);
		return result;
	}

	@Override
	public ODSShopRefusalRecord[] findObjectById(String token, Object[] ids) {
		if (ids == null || ids.length == 0) {
			return null;
		}
		Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
		List<ODSShopRefusalRecord> odsShopRefusalRecords = odsShopRefusalRecordMapper.selectByPrimaryKeyBatch(idArr);
		return odsShopRefusalRecords.toArray(new ODSShopRefusalRecord[odsShopRefusalRecords.size()]);
	}

	@Override
	public ODSShopRefusalRecord[] queryObject(String token, E3Selector selector) {
		// TODO
		return null;
	}

	@Override
	public long getListCount(String token, E3Selector selector) {
		ODSShopRefusalRecordExample example = new ODSShopRefusalRecordExample();
		// TODO 无前端页面 暂不解析E3Selector
		return odsShopRefusalRecordMapper.countByExample(example);
	}

	@Override
	public ODSShopRefusalRecord[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
		// TODO 无前端页面 暂不解析E3Selector
		ODSShopRefusalRecordExample example = new ODSShopRefusalRecordExample();
		if (pageSize > 0 && pageIndex >= 0) {
			PageHelper.startPage(pageIndex + 1, pageSize);
			List<ODSShopRefusalRecord> odsShopRefusalRecords = odsShopRefusalRecordMapper.selectByExample(example);
			return odsShopRefusalRecords.toArray(new ODSShopRefusalRecord[odsShopRefusalRecords.size()]);
		}
		return null;
	}

	private ODSShopRefusalRecordExample getOdsShopRefusalRecordExample(E3Selector selector) {
		// TODO 无前端页面 暂不解析E3Selector
		E3FilterField odsShopRefusalRecordFilterField = selector.getFilterFieldByFieldName("id");
		ODSShopRefusalRecordExample example = new ODSShopRefusalRecordExample();
		if (odsShopRefusalRecordFilterField != null) {
			Object value = odsShopRefusalRecordFilterField.getValue();
			if (value != null) {
				example.createCriteria().andIdEqualTo(Long.valueOf(String.valueOf(value)));
			}
		}
		return example;
	}
}
