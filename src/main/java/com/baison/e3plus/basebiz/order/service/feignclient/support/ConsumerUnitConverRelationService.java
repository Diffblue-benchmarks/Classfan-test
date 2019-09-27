package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.manyunit.model.UnitConverRelation;
import com.baison.e3plus.common.cncore.query.E3Selector;


/**
 * 消费者模块-IUnitConverRelationService
 * @author xinghua.liu
 * ConvertUtil api模块调用了该service，无法引用
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerUnitConverRelationService")
public interface ConsumerUnitConverRelationService{

	@PostMapping("/providerUnitConverRelationService/query")
	public UnitConverRelation[] query(@RequestParam(name="token",required=true)  String token, @RequestBody E3Selector selector);
}
