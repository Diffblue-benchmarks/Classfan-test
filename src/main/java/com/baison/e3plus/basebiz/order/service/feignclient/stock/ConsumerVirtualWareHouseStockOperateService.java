
package com.baison.e3plus.basebiz.order.service.feignclient.stock;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.stock.api.config.BizStockApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.stock.api.model.stock.VirtualStockAvailableQty;

/**
 * 消费者模块-IVirtualWareHouseStockOperateService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizStockApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerVirtualWareHouseStockOperateService")
public interface ConsumerVirtualWareHouseStockOperateService {

	@PostMapping("/providerVirtualWareHouseStockOperateService/querySkuAvailableAty")
	public List<VirtualStockAvailableQty> querySkuAvailableAty(@RequestParam(name = "token", required = true) String token, 
			@RequestBody List<Long> virtualWareHouseIds,
			@RequestParam(name = "skuIds", required = false) List<Long> skuIds);
			


}