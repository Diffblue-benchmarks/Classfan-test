package com.baison.e3plus.basebiz.order.service.configuration;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerTemplateService;
import com.baison.e3plus.biz.support.api.excel.ExcelMetadata;
import com.baison.e3plus.biz.support.api.excel.ser.ITemplateService;
import com.baison.e3plus.biz.support.api.excel.template.Template;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.orm.core.AbstractBean;

/**
 * 架构调整适配stock模块中ITemplateService的实例化
 * 
 * @author xinghua.liu
 */
@Configuration
@ConditionalOnMissingBean(ITemplateService.class)
public class TemplateConfiguration {

	@Autowired
	ConsumerTemplateService service;

	@Bean(name = "templateService")
	@Primary
	public ITemplateService getIAuthService() {
		return new ITemplateService() {
			
			@Override
			public ExcelMetadata[] findExcelMetadataList(String token, E3Selector selector) {
				return service.findExcelMetadataList(token, selector);
			}

			@Override
			public ServiceResult createObject(String token, Template[] object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult modifyObject(String token, Template[] object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult removeObject(String token, Object[] ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Template[] findObjectById(String token, Object[] ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Template[] queryObject(String token, E3Selector selector) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getListCount(String token, E3Selector selector) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getListCountByADS(String token, E3Selector selector) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Template[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Template[] queryPageObjectByADS(String token, E3Selector selector, int pageSize, int pageIndex) {
				// TODO Auto-generated method stub
				return null;
			}

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
			public ServiceResult removeTemplate(String token, Object[] ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Template[] findTemplateById(String token, Object id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Template[] findTemplateList(String token, E3Selector selector) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Template[] queryPageTemplate(String token, E3Selector selector, int pageIndex, int pageSize) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceResult createDownloadTemplate(String token, Object id) {
				// TODO Auto-generated method stub
				return null;
			}

		};

	}
}
