package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baison.e3plus.basebiz.order.api.model.RetailAdditionalRefund;
import com.baison.e3plus.basebiz.order.api.service.IRetailAdditionalRefundService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailAdditionalRefundMapper;

/**
 * 额外退款报表接口实现
 *
 * @author peng.liao
 */
@Service
public class RetailAdditionalRefundService implements IRetailAdditionalRefundService {

    @Autowired
    private ConsumerIdService idService;

    @Autowired
    private RetailAdditionalRefundMapper retailAdditionalRefundMapper;

    @Override
    public ServiceResult createObject(String token, RetailAdditionalRefund[] beans) {
        ServiceResult result = new ServiceResult();

        RetailAdditionalRefund[] dataArray = new RetailAdditionalRefund[0];

        if (beans != null && beans.length > 0) {
            dataArray = beans;
        }

        // 生成ID
        for (RetailAdditionalRefund refund : dataArray) {
            refund.setId(idService.nextId());
        }

        List<RetailAdditionalRefund> dataList = Arrays.asList(dataArray);

        retailAdditionalRefundMapper.batchInsert(dataList);
        result.getSuccessObject().addAll(dataList);

        return result;
    }

}
