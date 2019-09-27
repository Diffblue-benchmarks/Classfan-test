package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.excel.ExcelMetadata;
import com.baison.e3plus.common.cncore.query.E3Selector;

/**
 * 消费者模块-ITemplateService
 * 
 * @author xinghua.liu
 *
 */

@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerTemplateService")
public interface ConsumerTemplateService{

	@PostMapping("/providerTemplateService/findExcelMetadataList")
	ExcelMetadata[] findExcelMetadataList(@RequestParam(name = "token", required = true) String token,@RequestBody E3Selector selector);
}
