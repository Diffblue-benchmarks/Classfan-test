
package com.baison.e3plus.basebiz.order.service.feignclient.goods;


import com.baison.e3plus.basebiz.goods.api.config.BasebizGoodsApiConfig;
import com.baison.e3plus.basebiz.goods.api.model.barcode.Barcode;
import com.baison.e3plus.common.cncore.query.E3Selector;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 消费者模块-IBarcodeService
 * 
 * @author junyi.yue
 *
 */
@FeignClient(name = BasebizGoodsApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerBarcodeService")
public interface ConsumerBarcodeService {

	@PostMapping("/providerBarcodeService/queryBarcode")
	public Barcode[] queryBarcode(@RequestParam(name = "token", required = true) String token,
			@RequestBody E3Selector selector);
	
	@PostMapping("/providerBarcodeService/barcodeQueryConditionConversion")
	public E3Selector barcodeQueryConditionConversion(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);
	
	@PostMapping("/providerBarcodeService/barcodeRecognizing")
	public Barcode barcodeRecognizing(@RequestParam(name = "token", required = true) String token,@RequestParam(name = "barcode", required = false) String barcode);

	@PostMapping("/providerBarcodeService/transBarcode")
	Map<String, Barcode> transBarcode(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "barcode") String barcode);

}