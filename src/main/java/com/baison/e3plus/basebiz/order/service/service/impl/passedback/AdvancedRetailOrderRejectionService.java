package com.baison.e3plus.basebiz.order.service.service.impl.passedback;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baison.e3plus.basebiz.order.api.model.AdvancedRetailOrderRejection;
import com.baison.e3plus.basebiz.order.api.passedback.IAdvancedRetailOrderRejectionService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.AdvancedRetailOrderRejectionMapper;

@Service
public class AdvancedRetailOrderRejectionService implements IAdvancedRetailOrderRejectionService {
//    @Resource(name = "adsSqlSessionFactory")
//    private SqlSessionFactory adsSqlSessionFactory;
//
//    @Resource(name = "rdsSqlSessionFactory")
//    private SqlSessionFactory rdsSqlSessionFactory;
//
//    private SqlSessionTemplate extracted(SqlSessionFactory sessionFactory) {
//        return new SqlSessionTemplate(sessionFactory);
//    }
//
//    @Autowired
//    private AdvancedRetailOrderRejectionMapper advancedRetailOrderRejectionMapper() {
//        return extracted(adsSqlSessionFactory).getMapper(AdvancedRetailOrderRejectionMapper.class);
//    }
//
//    @Override
//    public ServiceResult createObject(String token, AdvancedRetailOrderRejection[] advancedRetailOrderRejection) {
//        advancedRetailOrderRejectionMapper().insertbatch(advancedRetailOrderRejection);
//        return new ServiceResult();
//    }

    /**
     * 修改只针对于数据源的切换方案
     *
     * @author qiancheng.chen start
     * @since 2018-12-07
     */
    @Autowired
    private AdvancedRetailOrderRejectionMapper advancedRetailOrderRejectionMapper;
    @Autowired
    ConsumerIdService idService;

    @Override
    public ServiceResult createObject(String token, AdvancedRetailOrderRejection[] advancedRetailOrderRejection) {
        for (AdvancedRetailOrderRejection retailOrderRejection : advancedRetailOrderRejection) {
            retailOrderRejection.setId(idService.nextId());
        }
        advancedRetailOrderRejectionMapper.insertbatch(advancedRetailOrderRejection);
        return new ServiceResult();
    }
    /**
     * 修改只针对于数据源的切换方案
     * @author qiancheng.chen end
     * @since 2018-12-07
     */

}
