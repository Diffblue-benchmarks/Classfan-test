package com.baison.e3plus.basebiz.order.service.feignclient.support;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.business.advanced.api.model.delivery.AdvancedDeliveryType;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IAdvancedDeliveryTypeService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdvancedDeliveryTypeService")
public interface ConsumerAdvancedDeliveryTypeService {

	@PostMapping("/providerAdvancedDeliveryTypeService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody AdvancedDeliveryType[] beans);

	@PostMapping("/providerAdvancedDeliveryTypeService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody AdvancedDeliveryType[] beans);

	@PostMapping("/providerAdvancedDeliveryTypeService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerAdvancedDeliveryTypeService/findObjectById")
	public AdvancedDeliveryType[] findObjectById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] ids);

	@PostMapping("/providerAdvancedDeliveryTypeService/queryObject")
	public AdvancedDeliveryType[] queryObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerAdvancedDeliveryTypeService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);

	@PostMapping("/providerAdvancedDeliveryTypeService/queryPageObject")
	public AdvancedDeliveryType[] queryPageObject(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerAdvancedDeliveryTypeService/enableObjects")
	ServiceResult enableObjects(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] detailIds);

	@PostMapping("/providerAdvancedDeliveryTypeService/disableObjects")
	ServiceResult disableObjects(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] detailIds);

	@PostMapping("/providerAdvancedDeliveryTypeService/setMasterPic")
	ServiceResult setMasterPic(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object[] detailIds);
	
	@PostMapping("/providerAdvancedDeliveryTypeService/enable")
	ServiceResult enable(@RequestParam(name = "token", required = true) String token,
			@RequestBody  Object[] ids);
	@PostMapping("/providerAdvancedDeliveryTypeService/disable")
	ServiceResult disable(@RequestParam(name = "token", required = true) String token,
			@RequestBody  Object[] ids);
	@PostMapping("/providerAdvancedDeliveryTypeService/queryCodesById")
    List<String> queryCodesById(@RequestParam(name = "token", required = true) String token,
			@RequestBody  List<Long> deliveryTypeIdList);
	
	@PostMapping("/providerAdvancedDeliveryTypeService/checkVersion")
	boolean checkVersion();

}
