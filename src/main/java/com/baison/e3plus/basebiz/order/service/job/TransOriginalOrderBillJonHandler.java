package com.baison.e3plus.basebiz.order.service.job;

import com.baison.e3plus.basebiz.order.service.service.impl.RetailOrderBillService;
import com.baison.e3plus.common.cncore.session.Session;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@JobHandler(value = "transOriginalOrderBillJobHandler")
@Component
public class TransOriginalOrderBillJonHandler extends IJobHandler{

    @Autowired
    RetailOrderBillService retailOrderBillService;
    @Override
    public ReturnT<String> execute(String s) {
        retailOrderBillService.createRetailOrderBillFromOrignalBill(Session.ADMINTOKEN);
        return ReturnT.SUCCESS;
    }
}
