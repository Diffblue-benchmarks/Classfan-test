package com.baison.e3plus.basebiz.order.service.feignclient.goods;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.basebiz.goods.api.config.BasebizGoodsApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.basebiz.goods.api.model.product.E3SingleProduct;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.common.cncore.query.E3ListFieldsInfo;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-ISingleProductService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BasebizGoodsApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerSingleProductService")
public interface ConsumerSingleProductService {

	@PostMapping("/providerSingleProductService/createSingleProduct")
	public ServiceResult createSingleProduct(@RequestParam(name = "token", required = true) String token, @RequestBody List<E3SingleProduct> singleProducts);
	
	@PostMapping("/providerSingleProductService/modifySingleProduct")
	public ServiceResult modifySingleProduct(@RequestParam(name = "token", required = true) String token, @RequestBody List<E3SingleProduct> singleProducts);
	
	@PostMapping("/providerSingleProductService/removeSingleProduct")
	public ServiceResult removeSingleProduct(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerSingleProductService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerSingleProductService/queryPageSingleProduct")
	public E3SingleProduct[] queryPageSingleProduct(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = false) int pageSize, 
			@RequestParam(name = "pageIndex", required = false) int pageIndex);
	
	@PostMapping("/providerSingleProductService/findById")
	public E3SingleProduct findById(@RequestParam(name = "token", required = true) String token,
			@RequestBody Object singleProductIds);
	
	@PostMapping("/providerSingleProductService/findSingleProduct")
	public E3SingleProduct[] findSingleProduct(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerSingleProductService/enbaleSingleProduct")
	public void enbaleSingleProduct();
	
	@PostMapping("/providerSingleProductService/enableSingleProduct")
	public ServiceResult enableSingleProduct(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerSingleProductService/disableSingleProduct")
	public ServiceResult disableSingleProduct(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);
	
	@PostMapping("/providerSingleProductService/save")
	public ServiceResult save(@RequestParam(name = "token", required = true) String token, @RequestBody List<E3SingleProduct> singleProducts);
	
	@PostMapping("/providerSingleProductService/getQueryFields")
	public E3ListFieldsInfo getQueryFields(@RequestParam(name = "token", required = true) String token, @RequestBody Long categoryTreeId);
	
	@PostMapping("/providerSingleProductService/querySimpleSingleProduct")
	SimpleSingleProduct[] querySimpleSingleProduct(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
	@PostMapping("/providerSingleProductService/findByIdsThree")
	public E3SingleProduct[] findByIdsThree(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector,
			@RequestParam(name = "singleProductIds", required = true) Object[] singleProductIds);
	
}
