
package com.baison.e3plus.basebiz.order.service.feignclient.support;

import com.baison.e3plus.biz.support.api.publicrecord.model.customer.CustomerSku;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baison.e3plus.biz.support.api.publicrecord.model.customer.Customer;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 消费者模块-ICustomerService
 *
 * @author junyi.yue
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerCustomerService")
public interface ConsumerCustomerService {

	@PostMapping("/providerCustomerService/queryCustomer")
	public Customer[] queryCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);

	@PostMapping("/providerCustomerService/saveCustomer")
	public ServiceResult saveCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody List<Customer> Customers);

	@PostMapping("/providerCustomerService/createCustomer")
	public ServiceResult createCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody List<Customer> Customers);

	@PostMapping("/providerCustomerService/modifyCustomer")
	public ServiceResult modifyCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody List<Customer> Customers);

	@PostMapping("/providerCustomerService/removeCustomer")
	public ServiceResult removeCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

	@PostMapping("/providerCustomerService/removeCustomerAddress")
	public ServiceResult removeCustomerAddress(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

	@PostMapping("/providerCustomerService/findById")
	public Customer[] findById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "id", required = false) Object id);

	@PostMapping("/providerCustomerService/findCustomerById")
	public Customer findCustomerById(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] id);

	@PostMapping("/providerCustomerService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector);

	@PostMapping("/providerCustomerService/queryPageCustomer")
	public Customer[] queryPageCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = true) int pageSize,
			@RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerCustomerService/enableCustomer")
	public ServiceResult enableCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

	@PostMapping("/providerCustomerService/disableCustomer")
	public ServiceResult disableCustomer(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

	@PostMapping(value = "/providerCustomerService/queryCustomerSku")
	public CustomerSku[] queryCustomerSku(@RequestParam(name = "token", required = true) String token,
		@RequestBody E3Selector selector) ;
}