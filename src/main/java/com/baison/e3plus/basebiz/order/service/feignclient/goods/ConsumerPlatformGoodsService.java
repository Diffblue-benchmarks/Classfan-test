package com.baison.e3plus.basebiz.order.service.feignclient.goods;


import com.baison.e3plus.basebiz.goods.api.business.advanced.model.PlatformGoods;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.basebiz.goods.api.config.BasebizGoodsApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 消费者模块-IPlatformGoodsService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BasebizGoodsApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerPlatformGoodsService")
public interface ConsumerPlatformGoodsService {

	@PostMapping("/providerPlatformGoodsService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token, @RequestBody PlatformGoods[] templates);
	
	@PostMapping("/providerPlatformGoodsService/modifyObject")
	public ServiceResult modifyObject(@RequestParam(name = "token", required = true) String token, @RequestBody PlatformGoods[] templates);
	
	@PostMapping("/providerPlatformGoodsService/removeObject")
	public ServiceResult removeObject(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerPlatformGoodsService/queryObject")
	public PlatformGoods[] queryObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerPlatformGoodsService/findObjectById")
	public PlatformGoods[] findObjectById(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerPlatformGoodsService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerPlatformGoodsService/queryPageObject")
	public PlatformGoods[] queryPageObject(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = false) int pageSize, 
			@RequestParam(name = "pageIndex", required = false) int pageIndex);
	
	@PostMapping("/providerPlatformGoodsService/queryPageGoods")
	public List<? extends PlatformGoods>  queryPageGoods(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = false) int pageSize, 
			@RequestParam(name = "pageIndex", required = false) int pageIndex);
	
	@PostMapping("/providerPlatformGoodsService/getListCountGoods")
	public long  getListCountGoods(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerPlatformGoodsService/synVipsGoods")
	public ServiceResult  synVipsGoods(@RequestParam(name = "token", required = true) String token);
	
}
