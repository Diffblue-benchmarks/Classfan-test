package com.baison.e3plus.basebiz.order.service.feignclient.goods;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.basebiz.goods.api.config.BasebizGoodsApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.basebiz.goods.api.model.product.E3Goods;
import com.baison.e3plus.basebiz.goods.api.model.product.E3GoodsExt;
import com.baison.e3plus.basebiz.goods.api.model.product.ListField;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleGoods;
import com.baison.e3plus.common.cncore.query.E3ListFieldsInfo;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-IAdvancedGoodsService
 * 
 * @author junyi.le
 *
 */
@FeignClient(name = BasebizGoodsApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerAdvancedGoodsService")
public interface ConsumerAdvancedGoodsService {

	@PostMapping("/providerAdvancedGoodsService/createGoods")
	public ServiceResult createGoods(@RequestParam(name = "token", required = true) String token, @RequestBody List<E3Goods> simpleGoods);
	
	@PostMapping("/providerAdvancedGoodsService/modifyGoods")
	public ServiceResult modifyGoods(@RequestParam(name = "token", required = true) String token, @RequestBody List<E3Goods> simpleGoods);
	
	@PostMapping("/providerAdvancedGoodsService/removeGoods")
	public ServiceResult removeGoods(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerAdvancedGoodsService/findById")
	public E3Goods findById(@RequestParam(name = "token", required = true) String token, @RequestBody Object ids);

	@PostMapping("/providerAdvancedGoodsService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerAdvancedGoodsService/queryPageGoods")
	public E3Goods[] queryPageGoods(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerAdvancedGoodsService/findGoods")
	public E3Goods[] findGoods(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerAdvancedGoodsService/mountGoods")
	ServiceResult mountGoods(@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "categoryTreeId", required = false) String categoryTreeId, 
			@RequestParam(name = "categoryTreeNodeId", required = false) String categoryTreeNodeId, 
			@RequestParam(name = "goodsIds", required = false) List<Object> goodsIds);
	
	@PostMapping("/providerAdvancedGoodsService/getQueryFields")
	public E3ListFieldsInfo getQueryFields(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "categoryTreeId", required = false) Long categoryTreeId);
	
	@PostMapping("/providerAdvancedGoodsService/getGoodsId")
	public Long getGoodsId();
	
	@PostMapping("/providerAdvancedGoodsService/enableGoods")
	public ServiceResult enableGoods(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerAdvancedGoodsService/disableGoods")
	public ServiceResult disableGoods(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerAdvancedGoodsService/modifyGoodsAttrValues")
	public ServiceResult modifyGoodsAttrValues(@RequestParam(name = "token", required = true) String token, @RequestBody List<E3GoodsExt> goodsExts);
	
	@PostMapping("/providerAdvancedGoodsService/saveGoodsListTemplate")
	public ServiceResult saveGoodsListTemplate(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "categoryTreeId", required = false) Long categoryTreeId, 
			@RequestBody List<ListField> listFields);
	
	@PostMapping("/providerAdvancedGoodsService/queryGoodsListTemplate")
	public ServiceResult queryGoodsListTemplate(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "categoryTreeId", required = false) Long categoryTreeId);
	
	@PostMapping("/providerAdvancedGoodsService/save")
	public ServiceResult save(@RequestParam(name = "token", required = true) String token, @RequestBody List<E3Goods> goods);
	
	@PostMapping("/providerAdvancedGoodsService/findEkgrp")
	public String findEkgrp(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "classId", required = false) String classId);
	
	@PostMapping("/providerAdvancedGoodsService/queryAdvancedSimpleGoods")
	SimpleGoods[] queryAdvancedSimpleGoods(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
}
