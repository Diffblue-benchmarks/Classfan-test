package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baison.e3plus.basebiz.order.api.model.WareHousePriorityStrategyOnlineDetail;
import com.baison.e3plus.basebiz.order.api.service.IWareHousePriorityStrategyOnlineDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.WareHousePriorityStrategyOnlineDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.WareHousePriorityStrategyOnlineDetailExample;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.common.cncore.common.ResultUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.restful.result.Result;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

@Service
public class WareHousePriorityStrategyOnlineDetailService implements IWareHousePriorityStrategyOnlineDetailService {

	/**
	 * 特殊表示同省门店仓id
	 */
	public static final Long SAME_PROVINCE_WAREHOUSEID = -1L;
	
    @Autowired
    private ConsumerIdService idService;

    /**
     * 修改只针对于数据源的切换方案
     *
     * @author qiancheng.chen start
     * @since 2018-12-07
     */
    @Autowired
    private WareHousePriorityStrategyOnlineDetailMapper wareHousePriorityStrategyOnlineDetailMapper;

    @Override
    public ServiceResult create(String token, WareHousePriorityStrategyOnlineDetail[] objects) {
        ServiceResult result = new ServiceResult();
        if (objects == null || objects.length == 0) {
            result.addErrorObject("", "", "input data is null");
        }

        // id
        BatchIdVo batchIdVo = idService.batchId(objects.length);
        long startId = batchIdVo.getStartId();
        for (WareHousePriorityStrategyOnlineDetail object : objects) {
            object.setId(startId);
            startId += batchIdVo.getStep();
        }

        wareHousePriorityStrategyOnlineDetailMapper.batchInsertSelective(Arrays.asList(objects));

        result.addSuccessObject(objects);

        return result;
    }

    @Override
    public ServiceResult modify(String token, WareHousePriorityStrategyOnlineDetail[] objects) {
        ServiceResult result = new ServiceResult();
        if (objects == null || objects.length == 0) {
            return result;
        }

        //删除原有数据
        List<Long> warehousePriorityIdList = Arrays.stream(objects).map(t -> t.getWarehousePriorityId()).collect(Collectors.toList());
        if (warehousePriorityIdList != null && warehousePriorityIdList.size() > 0) {
            wareHousePriorityStrategyOnlineDetailMapper.deleteWarehousePriorityIdBatch(warehousePriorityIdList);
        }

        //id
        BatchIdVo batchIdVo = idService.batchId(objects.length);
        long startId = batchIdVo.getStartId();
        for (WareHousePriorityStrategyOnlineDetail object : objects) {
            object.setId(startId);
            startId += batchIdVo.getStep();
        }

        wareHousePriorityStrategyOnlineDetailMapper.batchInsertSelective(Arrays.asList(objects));

        result.addSuccessObject(objects);
        return result;
    }

    @Override
    public ServiceResult remove(String token, Long[] pkIds) {

        ServiceResult result = new ServiceResult();
        if (pkIds == null || pkIds.length == 0) {
            result.addErrorObject("", "", "input data is null");
        }
        wareHousePriorityStrategyOnlineDetailMapper.deleteByPrimaryKey(Arrays.asList(pkIds));

        return result;
    }

    @Override
    public WareHousePriorityStrategyOnlineDetail findById(String token, Long pkId) {
        return wareHousePriorityStrategyOnlineDetailMapper.selectByPrimaryKey(pkId);
    }

    @Override
    public WareHousePriorityStrategyOnlineDetail[] queryPage(String token, E3Selector selector, int pageSize,
                                                             int pageIndex) {

        WareHousePriorityStrategyOnlineDetailExample example = new WareHousePriorityStrategyOnlineDetailExample();

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

        List<WareHousePriorityStrategyOnlineDetail> datas = wareHousePriorityStrategyOnlineDetailMapper.selectByExample(example);

        if (datas != null) {
            return datas.toArray(new WareHousePriorityStrategyOnlineDetail[datas.size()]);
        }

        return null;
    }

    @Override
    public long getListCount(String token, E3Selector selector) {

        WareHousePriorityStrategyOnlineDetailExample example = new WareHousePriorityStrategyOnlineDetailExample();

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

        return wareHousePriorityStrategyOnlineDetailMapper.getListCount(example);
    }

    @Override
    public ServiceResult removeByStrategyId(String token, Long[] strategyIds) {
        ServiceResult result = new ServiceResult();
        if (strategyIds == null || strategyIds.length == 0) {
            result.addErrorObject("", "", "input data is null");
        }

        wareHousePriorityStrategyOnlineDetailMapper.removeByStrategyId(Arrays.asList(strategyIds));

        return result;
    }

	@Override
	public WareHousePriorityStrategyOnlineDetail[] queryWareHousePriorityByWareHouseAndArea(Long strategyId,
			List<Long> wareHouseIds, Long areaId) {

		if (areaId == null) {
			return null;
		}

		WareHousePriorityStrategyOnlineDetailExample example = new WareHousePriorityStrategyOnlineDetailExample();
		List<Integer> intValues = new ArrayList<>();
		for (Long value : wareHouseIds) {
			intValues.add(value.intValue());
		}

		example.createCriteria().andWarehousePriorityIdEqualTo(strategyId).andWarehouseIdIn(intValues)
				.andAreaIdEqualTo(areaId.intValue());

		List<WareHousePriorityStrategyOnlineDetail> datas = wareHousePriorityStrategyOnlineDetailMapper
				.selectByExample(example);

		if (datas != null) {
			return datas.toArray(new WareHousePriorityStrategyOnlineDetail[datas.size()]);
		}

		return null;
	}

    @Override
    public ServiceResult deleteByPrimaryKey(String token, Long pid) {
        ServiceResult result = new ServiceResult();
        if (pid == null) {
            result.addErrorObject("", "", "input data is null");
            return result;
        }

        wareHousePriorityStrategyOnlineDetailMapper.deleteByPrimayKey(pid);

        return result;
    }

    @Override
    @Transactional
    public String importData(String token, List<WareHousePriorityStrategyOnlineDetail> details, Long pid) {
        ServiceResult result = new ServiceResult();
        if (details == null || details.size() == 0) {
            result.addErrorObject("", "", "input data is null");
            return JSON.toJSONString(result);
        }

        this.deleteByPrimaryKey(token, pid);

        List<List<WareHousePriorityStrategyOnlineDetail>> partition = Lists.partition(details, 1000);
        for (List<WareHousePriorityStrategyOnlineDetail> list : partition) {
            this.create(token, list.toArray(new WareHousePriorityStrategyOnlineDetail[list.size()]));
        }

        return JSON.toJSONString(ResultUtil.handleResult(result, new Result()));
    }

    /**
     * 修改只针对于数据源的切换方案
     * @author qiancheng.chen end
     * @since 2018-12-07
     */
}
