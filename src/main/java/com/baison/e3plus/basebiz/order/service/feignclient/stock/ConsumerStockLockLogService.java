
package com.baison.e3plus.basebiz.order.service.feignclient.stock;
import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.stock.api.config.BizStockApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.stock.api.model.stock.StockLockLog;
import com.baison.e3plus.common.cncore.query.E3Selector;

/**
 * 消费者模块-IStockLockLogService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizStockApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerStockLockLogService")
public interface ConsumerStockLockLogService {

	@PostMapping("/providerStockLockLogService/queryObject")
	public StockLockLog[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
			


}