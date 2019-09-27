package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;


/**
 * 消费者模块-IdService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerIdService")
public interface ConsumerIdService {

	@PostMapping("/providerIdService/nextId")
	public long nextId();

	@PostMapping("/providerIdService/batchId")
	public BatchIdVo batchId(@RequestParam(name = "size", required = true) int size);

}
