package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.api.model.OrderExemptReviewStrategyDetail;
import com.baison.e3plus.basebiz.order.api.service.IOrderExemptReviewStrategyDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OrderExemptReviewStrategyDetailMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.OrderExemptReviewStrategyDetailExample;
import com.baison.e3plus.common.bscore.utils.UUIDUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderExemptReviewStrategyDetailService implements IOrderExemptReviewStrategyDetailService {

    @Autowired
    private OrderExemptReviewStrategyDetailMapper orderExemptReviewStrategyDetailMapper;

    @Override
    public long getListCount(String token, E3Selector querySelector) {
        OrderExemptReviewStrategyDetailExample example = new OrderExemptReviewStrategyDetailExample();
        OrderExemptReviewStrategyDetailExample.Criteria criteria = example.createCriteria();
        E3FilterField typeFilterField = querySelector.getFilterFieldByFieldName("type");
        if (typeFilterField != null) {
            criteria.andTypeEqualTo(String.valueOf(typeFilterField.getValue()));
        }

        return orderExemptReviewStrategyDetailMapper.count(example);
    }

    @Override
    public OrderExemptReviewStrategyDetail[] queryObject(String token, E3Selector querySelector) {
        OrderExemptReviewStrategyDetailExample example = new OrderExemptReviewStrategyDetailExample();
        OrderExemptReviewStrategyDetailExample.Criteria criteria = example.createCriteria();
        E3FilterField typeFilterField = querySelector.getFilterFieldByFieldName("type");
        if (typeFilterField != null) {
            criteria.andTypeEqualTo(String.valueOf(typeFilterField.getValue()));
        }

        List<OrderExemptReviewStrategyDetail> list = orderExemptReviewStrategyDetailMapper.selectByExample(example);

        return list.toArray(new OrderExemptReviewStrategyDetail[0]);
    }

    @Override
    public OrderExemptReviewStrategyDetail[] queryPageObject(String token, E3Selector querySelector, int pageSize, int pageIndex) {
        OrderExemptReviewStrategyDetailExample example = new OrderExemptReviewStrategyDetailExample();
        OrderExemptReviewStrategyDetailExample.Criteria criteria = example.createCriteria();
        E3FilterField typeFilterField = querySelector.getFilterFieldByFieldName("type");
        if (typeFilterField != null) {
            criteria.andTypeEqualTo(String.valueOf(typeFilterField.getValue()));
        }
        if (pageIndex >= 0 && pageSize >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize);
        }

        List<OrderExemptReviewStrategyDetail> list = orderExemptReviewStrategyDetailMapper.selectByExample(example);

        return list.toArray(new OrderExemptReviewStrategyDetail[0]);
    }

    @Override
    public ServiceResult createObject(String token, OrderExemptReviewStrategyDetail[] beans) {
        ServiceResult serviceResult = new ServiceResult();
        if (ArrayUtils.isEmpty(beans)) {
            return serviceResult;
        }

        for (OrderExemptReviewStrategyDetail detail : beans) {
            detail.setOrderExemptReviewStrategyDetailId(UUIDUtil.generate());
            orderExemptReviewStrategyDetailMapper.insertSelective(detail);
        }

        return serviceResult;
    }

    @Override
    public ServiceResult modifyObject(String token, OrderExemptReviewStrategyDetail[] beans) {
        ServiceResult serviceResult = new ServiceResult();
        if (ArrayUtils.isEmpty(beans)) {
            return serviceResult;
        }

        for (OrderExemptReviewStrategyDetail detail : beans) {
            orderExemptReviewStrategyDetailMapper.updateByPrimaryKeySelectiveBatch(detail);
        }

        return serviceResult;
    }

    @Override
    public ServiceResult removeObject(String token, Object[] ids) {
        ServiceResult serviceResult = new ServiceResult();
        if (ArrayUtils.isEmpty(ids)) {
            return serviceResult;
        }

        String[] idArr = Arrays.stream(ids).map(t -> String.valueOf(t)).toArray(String[]::new);
        for (String id : idArr) {
            orderExemptReviewStrategyDetailMapper.deleteByPrimaryKey(id);
        }

        return serviceResult;
    }

}
