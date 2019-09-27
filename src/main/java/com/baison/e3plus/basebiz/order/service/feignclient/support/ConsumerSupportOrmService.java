package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.baison.e3plus.common.orm.core.AbstractBean;
import com.baison.e3plus.common.orm.oosql.OOSQLObject;

/**
 * 消费者模块-ISupportOrmService
 * 
 * @author xinghua.liu
 *
 */

@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerSupportOrmService")
public interface ConsumerSupportOrmService {

	@PostMapping("/providerSupportOrmService/query")
	public AbstractBean[] query(@RequestBody OOSQLObject ooSqlObj);
}
