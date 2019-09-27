package com.baison.e3plus.basebiz.order.service.configuration;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerCodeRuleService;
import com.baison.e3plus.biz.support.api.publicrecord.model.coderule.CodeRule;
import com.baison.e3plus.biz.support.api.publicrecord.service.ICodeRuleService;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.orm.core.AbstractBean;

/**
 * 架构调整适配AbstractBaseService中IAuthService的实例化
 * 
 * @author xinghua.liu
 */
@Configuration
@ConditionalOnClass(ICodeRuleService.class)
public class CodeRuleConfiguration {

	@Autowired
	ConsumerCodeRuleService service;

	@Bean(name = "codeRuleService")
	@Primary
	public ICodeRuleService getICodeRuleService() {
		return new ICodeRuleService() {

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
			public ServiceResult createCodeRules(String token, List<CodeRule> codeRules) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult modifyCodeRules(String token, List<CodeRule> codeRules) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CodeRule[] findById(String token, Object codeRuleId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CodeRule[] findCodeRules(String token, E3Selector selector) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getListCount(String token, E3Selector selector) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public CodeRule[] queryPageCodeRules(String token, E3Selector selector, int pageSize, int pageIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult removeCodeRules(String token, Object[] ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult enableCodeRules(String token, Object[] ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult disableCodeRules(String token, Object[] ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String generateCode(String token, String javaBeanName, Object object) {
				return service.generateCode(token, javaBeanName, object);
			}

			@Override
			public String createBillNo(String token, Object object, CodeRule[] codeRules, String simpleClassName) {
				// TODO Auto-generated method stub
				return null;
			}

		};

	}
}
