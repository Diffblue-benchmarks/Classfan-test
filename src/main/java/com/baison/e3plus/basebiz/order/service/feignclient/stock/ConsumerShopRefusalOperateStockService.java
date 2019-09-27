
package com.baison.e3plus.basebiz.order.service.feignclient.stock;
import com.baison.e3plus.biz.sales.api.model.refusalstock.ShopRefusalOpeStockParam;
import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.stock.api.config.BizStockApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IVirtualWareHouseStockOperateService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizStockApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerShopRefusalOperateStockService")
public interface ConsumerShopRefusalOperateStockService {

	@PostMapping("/providerShopRefusalOperateStockService/querySkuAvailableAty")
	public ServiceResult operateStock(@RequestParam(name = "token", required = true) String token,
			@RequestBody ShopRefusalOpeStockParam[] refusalOpeStockParams);
			


}