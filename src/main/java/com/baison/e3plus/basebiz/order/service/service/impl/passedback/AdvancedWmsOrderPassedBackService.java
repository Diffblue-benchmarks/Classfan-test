package com.baison.e3plus.basebiz.order.service.service.impl.passedback;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baison.e3plus.basebiz.order.api.model.AdvancedWmsOrderPassedBack;
import com.baison.e3plus.basebiz.order.api.passedback.IAdvancedWmsOrderPassedBackService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.WmsOrderPassedBackMapper;

@Service
public class AdvancedWmsOrderPassedBackService implements IAdvancedWmsOrderPassedBackService {

    /**
     * 修改只针对于数据源的切换方案
     *
     * @author qiancheng.chen start
     * @since 2018-12-07
     */
    @Autowired
    private WmsOrderPassedBackMapper wmsOrderPassedBackMapper;

    @Autowired
    ConsumerIdService idService;

    @Override
    public ServiceResult createPassedBack(String token, String billType, String billNo, String wmsBillNo, String postData) {
        ServiceResult result = new ServiceResult();

        AdvancedWmsOrderPassedBack bean = new AdvancedWmsOrderPassedBack();
        bean.setId(idService.nextId());
        bean.setBillType(billType);
        bean.setStatus("0");
        bean.setBillNo(billNo);
        bean.setWmsBillNo(wmsBillNo);
        bean.setPostData(postData);
        bean.setCreateDate(new Date());
        bean.setModifyDate(new Date());
        wmsOrderPassedBackMapper.insertSelective(bean);
        return result;
    }

    @Override
    public long getListCount(String token, E3Selector selector) {

        Map<String, Object> map = new HashMap<String, Object>();
        for (E3FilterField field : selector.getFilterFields()) {
            map.put(field.getFieldName(), field.getValue());
        }
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -10);// 10分钟之前的时间
        Date beforeD = beforeTime.getTime();
        //处理查询10分钟之前状态为1的单子
        map.put("beforeDate", beforeD);
        return wmsOrderPassedBackMapper.getListCount(map);
    }

    @Override
    public AdvancedWmsOrderPassedBack[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (E3FilterField field : selector.getFilterFields()) {
            map.put(field.getFieldName(), field.getValue());
        }
        if (pageSize > 0 && pageIndex >= 0) {
            int stratRow = pageIndex * pageSize;

            map.put("stratRow", stratRow);
            map.put("endRow", pageSize);
        }
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -10);// 10分钟之前的时间
        Date beforeD = beforeTime.getTime();
        //处理查询10分钟之前状态为1的单子
        map.put("beforeDate", beforeD);
        return wmsOrderPassedBackMapper.querPage(map);
    }

    @Override
    public int saveBatch(AdvancedWmsOrderPassedBack[] bean) {
        for (AdvancedWmsOrderPassedBack advancedWmsOrderPassedBack : bean) {
            advancedWmsOrderPassedBack.setId(idService.nextId());
        }
        return wmsOrderPassedBackMapper.saveBatch(bean);
    }

    @Override
    public int updateBatch(List<AdvancedWmsOrderPassedBack> bean) {
        return wmsOrderPassedBackMapper.updateBatch(bean);
    }

    @Override
    public int updateBatchById(Map map) {
        return wmsOrderPassedBackMapper.updateBatchById(map);
    }
}
