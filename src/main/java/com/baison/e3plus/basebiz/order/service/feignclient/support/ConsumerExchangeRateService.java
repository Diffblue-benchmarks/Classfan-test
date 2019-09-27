package com.baison.e3plus.basebiz.order.service.feignclient.support;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.manyunit.model.ExchangeRate;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IExchangeRateService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerExchangeRateService")
public interface ConsumerExchangeRateService {

	@PostMapping("/providerExchangeRateService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody ExchangeRate[] beans);

	@PostMapping("/providerExchangeRateService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody ExchangeRate[] beans);

	@PostMapping("/providerExchangeRateService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerExchangeRateService/findObjectById")
	public ExchangeRate[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerExchangeRateService/queryObject")
	public ExchangeRate[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerExchangeRateService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerExchangeRateService/queryPageObject")
	public ExchangeRate[] queryPageObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerExchangeRateService/enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] Ids);

	@PostMapping("/providerExchangeRateService/disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] Ids);

	@PostMapping("/providerExchangeRateService/query")
	public ExchangeRate[] query(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerExchangeRateService/queryPage")
	public ExchangeRate[] queryPage(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

}
