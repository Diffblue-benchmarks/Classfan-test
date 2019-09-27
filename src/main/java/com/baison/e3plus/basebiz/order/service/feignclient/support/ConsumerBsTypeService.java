package com.baison.e3plus.basebiz.order.service.feignclient.support;

import com.baison.e3plus.biz.support.api.config.BizSupportApiConfig;
import com.baison.e3plus.biz.support.api.publicrecord.model.businesstype.BsType;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 消费者模块-IBsTypeService
 * 
 * @author xinghua.liu
 *
 */
@FeignClient(name = BizSupportApiConfig.PLACE_HOLD_SERVICE_NAME, contextId="ConsumerBsTypeService")
public interface ConsumerBsTypeService {

	@PostMapping("/providerBsTypeService/createObject")
	public ServiceResult createObject(@RequestParam(name = "token", required = true) String token,
                                      @RequestBody List<BsType> beans);

	@PostMapping("/providerBsTypeService/remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

	@PostMapping("/providerBsTypeService/modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token,
                                @RequestBody List<BsType> beans);

	@PostMapping("/providerBsTypeService/findById")
	public BsType[] findById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "id", required = false) Object id);

	@PostMapping("/providerBsTypeService/enable")
	public ServiceResult enable(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids);

	@PostMapping("/providerBsTypeService/disable")
	public ServiceResult disable(@RequestParam(name = "token", required = true) String token,
                                 @RequestBody Object[] ids);

	@PostMapping("/providerBsTypeService/getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token,
                             @RequestBody E3Selector selector);

	@PostMapping("/providerBsTypeService/query")
	public BsType[] query(@RequestParam(name = "token", required = true) String token,
                          @RequestBody E3Selector selector);

	@PostMapping("/providerBsTypeService/queryPage")
	public BsType[] queryPage(@RequestParam(name = "token", required = true) String token,
                              @RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
                              @RequestParam(name = "pageIndex", required = true) int pageIndex);

	@PostMapping("/providerBsTypeService/queryPageForSub")
	public BsType[] queryPage(@RequestParam(name = "token", required = true) String token,
                              @RequestBody E3Selector selector, @RequestParam(name = "pageSize", required = true) int pageSize,
                              @RequestParam(name = "pageIndex", required = true) int pageIndex,
                              @RequestParam(name = "selectSub", required = true) boolean selectSub);

}
