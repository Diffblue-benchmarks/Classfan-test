package com.baison.e3plus.basebiz.order.service.configuration;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerUnitConverRelationService;
import com.baison.e3plus.biz.support.api.manyunit.model.UnitConverRelation;
import com.baison.e3plus.biz.support.api.manyunit.service.IUnitConverRelationService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.orm.core.AbstractBean;

/**
 * 架构调整适配AbstractBaseService中IAuthService的实例化
 * 
 * @author xinghua.liu
 */
@Configuration
@ConditionalOnClass(IUnitConverRelationService.class)
public class UnitConverRelationConfiguration {

	@Autowired
	ConsumerUnitConverRelationService service;

	@Bean(name = "unitConverRelationService")
	@Primary
	public IUnitConverRelationService getIUnitConverRelationService() {
		return new IUnitConverRelationService() {

			@Override
			public ServiceResult importData(String token, String businessObjectMetaId, String templateId,
					Map<String, List<String[]>> dataMap) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult exportData(String token, String templateId, E3Selector selector) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult importDetailDatas(String token, String businessObjectMetaId, String billFormId,
					String templateId, Map<String, List<String[]>> dataMap) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult saveImportDatas(String token, AbstractBean[] saveBeans, String billId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult create(String token, List<UnitConverRelation> unitconverrelations) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult remove(String token, Object[] unitconverrelations) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public UnitConverRelation findById(String token, Object unitconverrelationid) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult modify(String token, List<UnitConverRelation> unitconverrelations) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult enable(String token, Object[] unitconverrelationids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult disable(String token, Object[] unitconverrelationids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getListCount(String token, E3Selector selector) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public UnitConverRelation[] query(String token, E3Selector selector) {
				return service.query(token, selector);
			}

			@Override
			public UnitConverRelation[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};

	}
}
