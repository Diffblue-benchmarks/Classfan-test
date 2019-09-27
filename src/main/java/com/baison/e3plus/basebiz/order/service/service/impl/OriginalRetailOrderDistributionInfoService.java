package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrderDistributionInfo;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrderDistributionInfoService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OriginalRetailOrderDistributionInfoMapper;
import com.baison.e3plus.common.bscore.utils.UUIDUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OriginalRetailOrderDistributionInfoService implements IOriginalRetailOrderDistributionInfoService {

    @Autowired
    private OriginalRetailOrderDistributionInfoMapper originalRetailOrderDistributionInfoMapper;

    @Override
    @Transactional
    public ServiceResult create(String token, List<OriginalRetailOrderDistributionInfo> distributionInfoList) {
        ServiceResult result = new ServiceResult();
        if (distributionInfoList == null || distributionInfoList.size() == 0) {
            result.addErrorObject("", "", "no data input!");
            return result;
        }

        // 省，市，手机号，收件人为空则返回错误的交易号
        List<String> billNos = new ArrayList<>();
        distributionInfoList.stream().filter(t -> t.getProvinceId() == null  ||
                t.getCityId()==null || t.getAddressDesc() == null || "".equals(t.getAddressDesc())
                || t.getReceiver() == null || "".equals(t.getReceiver())
                || t.getTel() == null || "".equals(t.getTel())
         ).forEach(item -> {
            billNos.add(item.getTradeNo());
        });

        if(billNos != null && billNos.size() > 0){
            result.addErrorObject(billNos, "","该订单收件人信息" + billNos.toString() + "不能为空");
            return result;
        }

        // OriginalRetailOrderId不能为空
        for (OriginalRetailOrderDistributionInfo detail : distributionInfoList) {
            if (detail.getOriginalRetailOrderId() == null) {
                result.addErrorObject(detail, "", "order id is null!");
            }
        }

        if (result.hasError()) {
            return result;
        }

        for (OriginalRetailOrderDistributionInfo detail : distributionInfoList) {
            detail.setId(UUIDUtil.generate());
        }

        originalRetailOrderDistributionInfoMapper.insertSelective(distributionInfoList);

        return result;
    }


    @Override
    public OriginalRetailOrderDistributionInfo[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {
        Map<String, Object> args = new HashMap<>();
        for (E3FilterField field : selector.getFilterFields()) {
            args.put(field.getFieldName(), field.getValue());
        }

        if (pageSize > 0 && pageIndex >= 0) {
            int stratRow = pageIndex * pageSize;

            args.put("stratRow", stratRow);
            args.put("endRow", pageSize);
        }

        List<OriginalRetailOrderDistributionInfo> datas = originalRetailOrderDistributionInfoMapper.queryPage(args);

        return datas.toArray(new OriginalRetailOrderDistributionInfo[datas.size()]);
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        Map<String, Object> args = new HashMap<>();
        for (E3FilterField field : selector.getFilterFields()) {
            args.put(field.getFieldName(), field.getValue());
        }

        return originalRetailOrderDistributionInfoMapper.getListCount(args);
    }


    @Override
    public OriginalRetailOrderDistributionInfo[] queryGoodsDetailByBillNo(String token, List<String> billNo) {

        if (billNo == null || billNo.size() == 0) {
            return null;
        }

        List<OriginalRetailOrderDistributionInfo> goodsDetails = originalRetailOrderDistributionInfoMapper.selectByBillNo(billNo);

        if (goodsDetails != null) {
            return goodsDetails.toArray(new OriginalRetailOrderDistributionInfo[goodsDetails.size()]);
        }
        return null;
    }
}
