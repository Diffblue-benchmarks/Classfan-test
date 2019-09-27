package com.baison.e3plus.basebiz.order.service.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSupportOrmService;
import com.baison.e3plus.biz.support.api.orm.service.ISupportOrmService;

import com.baison.e3plus.common.orm.core.AbstractBean;
import com.baison.e3plus.common.orm.oosql.OOSQLObject;


/**
 * 架构调整适配stock模块中ISupportOrmService的实例化
 * 
 * @author xinghua.liu
 */
@Configuration
@ConditionalOnMissingBean(ISupportOrmService.class)
public class SupportOrmConfiguration {

	@Autowired
	ConsumerSupportOrmService service;

	@Bean(name = "supportOrmService")
	@Primary
	public ISupportOrmService getIAuthService() {
		return new ISupportOrmService() {

			@Override
			public void execute(OOSQLObject... oosqlObjs) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void executeBatch(OOSQLObject... oosqlObjs) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public AbstractBean[] query(OOSQLObject ooSqlObj) {
				return service.query(ooSqlObj);
			}

			@Override
			public int count(OOSQLObject ooSqlObj) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public AbstractBean[] next(OOSQLObject ooSqlObj, int batchSize, int page) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AbstractBean[] filter(List<Object> ids, OOSQLObject ooSqlObj) {
				// TODO Auto-generated method stub
				return null;
			}


		};

	}
}
