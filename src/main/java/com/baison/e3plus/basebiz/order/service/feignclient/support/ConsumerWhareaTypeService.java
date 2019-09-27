
package com.baison.e3plus.basebiz.order.service.feignclient.support;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.whareatype.WhareaType;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IWhareaTypeService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerWhareaTypeService")
public interface ConsumerWhareaTypeService {
	
	@PostMapping("/providerWhareaTypeService/createWhareaType")
	public ServiceResult createWhareaType(@RequestParam(name = "token", required = true) String token, @RequestBody List<WhareaType> WhareaType);
	
	@PostMapping("/providerWhareaTypeService/modifyWhareaType")
	public ServiceResult modifyWhareaType(@RequestParam(name = "token", required = true) String token, @RequestBody List<WhareaType> WhareaType);
	
	@PostMapping("/providerWhareaTypeService/removeWhareaTypes")
	public ServiceResult removeWhareaTypes(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerWhareaTypeService/findWhareaTypeById")
	public WhareaType findWhareaTypeById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "id", required = false) Object id);
	
	@PostMapping("/providerWhareaTypeService/queryWhareaType")
	public WhareaType[] queryWhareaType(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerWhareaTypeService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerWhareaTypeService/queryPageWhareaType")
	public WhareaType[] queryPageWhareaType(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);
	
	@PostMapping("/providerWhareaTypeService/enableWhareaType")
	public ServiceResult enableWhareaType(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerWhareaTypeService/disableWhareaType")
	public ServiceResult disableWhareaType(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerWhareaTypeService/findObjectByCodeArry")
	public WhareaType[] findObjectByCodeArry(@RequestParam(name = "token", required = true) String token, @RequestBody String code);
	
}