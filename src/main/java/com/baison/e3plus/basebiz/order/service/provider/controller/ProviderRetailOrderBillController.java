package com.baison.e3plus.basebiz.order.service.provider.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.SetMealRetailOrderGoods;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderBillService;
import com.baison.e3plus.basebiz.order.api.thirdParty.RetailOrderBillEntity;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IRetailOrderBillService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerRetailOrderBillService")
public class ProviderRetailOrderBillController {

	@Autowired
	private IRetailOrderBillService iRetailOrderBillService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderBill[] beans) {
		return iRetailOrderBillService.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderBill[] beans) {
		return iRetailOrderBillService.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iRetailOrderBillService.remove(token, ids);
	}
	
	@PostMapping(value = "findById")
	public RetailOrderBill[] findById(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billNos", required = false) Object[] billNos, 
			@RequestParam(name = "selectFields", required = false) String[] selectFields) {
		return iRetailOrderBillService.findById(token, billNos, selectFields);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iRetailOrderBillService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public RetailOrderBill[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iRetailOrderBillService.queryPage(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "submit")
	public ServiceResult submit(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billNo", required = false) String billNo) {
		return iRetailOrderBillService.submit(token, billNo);
	}
	
	@PostMapping(value = "retreat")
	public ServiceResult retreat(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billNo", required = false) String billNo) {
		return iRetailOrderBillService.retreat(token, billNo);
	}
	
	@PostMapping(value = "complete")
	public ServiceResult complete(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billNo", required = false) String billNo,
			@RequestParam(name = "wmsDate", required = false) Date wmsDate) {
		return iRetailOrderBillService.complete(token, billNo, wmsDate);
	}
	
	@PostMapping(value = "term")
	public ServiceResult term(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billNo", required = false) String billNo) {
		return iRetailOrderBillService.term(token, billNo);
	}
	
	@PostMapping(value = "releaseBillLockStock")
	public ServiceResult releaseBillLockStock(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billNo", required = false) String billNo) {
		return iRetailOrderBillService.releaseBillLockStock(token, billNo);
	}
	
	@PostMapping(value = "updateBySql")
	public void updateBySql(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "billNo", required = false) String sql) {
		iRetailOrderBillService.updateBySql(token, sql);
	}
	
	@PostMapping(value = "queryRetailOrderBillMapToOms")
	public List<Map<String, Object>> queryRetailOrderBillMapToOms(@RequestParam(name = "token", required = true) String token, 
			@RequestBody E3Selector selector,
			@RequestParam(name = "pageSize", required = false) int pageSize, 
			@RequestParam(name = "pageIndex", required = false) int pageIndex,
			@RequestParam(name = "isADS", required = false) boolean isADS) {
		return iRetailOrderBillService.queryRetailOrderBillMapToOms(token, selector, pageSize, pageIndex, isADS);
	}
	
	@PostMapping(value = "getRetailOrderBillToOmsCount")
	public long getRetailOrderBillToOmsCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector,
			@RequestParam(name = "isADS", required = false) boolean isADS) {
		return iRetailOrderBillService.getRetailOrderBillToOmsCount(token, selector, isADS);
	}
	
	@PostMapping(value = "queryPageBySetMealRetailOrderGoods")
	public SetMealRetailOrderGoods[] queryPageBySetMealRetailOrderGoods(@RequestParam(name = "token", required = true) String token, 
			@RequestBody Map<String,Object> filterMaps, 
			@RequestParam(name = "pageSize", required = false) int pageSize,
			@RequestParam(name = "pageIndex", required = false) int pageIndex) {
		return iRetailOrderBillService.queryPageBySetMealRetailOrderGoods(token, filterMaps, pageSize, pageIndex);
	}
	
	@PostMapping(value = "getSetMealRetailOrderGoodsCount")
	public long getSetMealRetailOrderGoodsCount(@RequestParam(name = "token", required = true) String token, 
			@RequestBody Map<String,Object> filterMaps) {
		return iRetailOrderBillService.getSetMealRetailOrderGoodsCount(token, filterMaps);
	}
	
	@PostMapping(value = "updateByPrimaryKeySelective")
	public ServiceResult updateByPrimaryKeySelective(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderBill[] beans) {
		return iRetailOrderBillService.updateByPrimaryKeySelective(token, beans);
	}
	
	@PostMapping(value = "queryObjectBySql")
	public RetailOrderBill[] queryObjectBySql(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "sql", required = false) String sql) {
		return iRetailOrderBillService.queryObjectBySql(token, sql);
	}
	
	@PostMapping(value = "savePassedBackBill")
	public ServiceResult savePassedBackBill(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailOrderBillEntity retailOrderBillEntity) {
		return iRetailOrderBillService.savePassedBackBill(token, retailOrderBillEntity.getBean(), retailOrderBillEntity.getInfo(), retailOrderBillEntity.getBillNos(), retailOrderBillEntity.getWmsDate());
	}
	
	@PostMapping(value = "selectBySap")
	public RetailOrderBill[] selectBySap(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "selectfields", required = false) String selectfields) {
		return iRetailOrderBillService.selectBySap(token, selectfields);
	}
	
	@PostMapping(value = "updateIsConfirm")
	public int updateIsConfirm(@RequestBody Map map) {
		return iRetailOrderBillService.updateIsConfirm(map);
	}
	
	@PostMapping(value = "selectBillNoBySap")
	public RetailOrderBill[] selectBillNoBySap(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "selectfields", required = false) String selectfields) {
		return iRetailOrderBillService.selectBillNoBySap(token, selectfields);
	}

	/**
	 * eWMS发货回传，修改订单发货状态为“”已发货“”；
	 * eWMS出库回传，修改订单状态为“”已完成“”、发货状态为“”已出库“”，释放锁定库存扣减在库库存；
	 * @param token
	 * @param param
	 * @return
	 */
	@PostMapping(value = "ewmsCheckOutAndSendOut")
	public String ewmsCheckOutAndSendOut(@RequestParam(name = "token", required = false) String token,
											   @RequestBody String param) {
		return iRetailOrderBillService.ewmsCheckOutAndSendOut(token, param);
	}

	/**
	 * 重试到“实物缺货停止刷新时间”将当日所有发货状态为“”实物缺货“”’状态订单返回给ECIP缺货，并终止零售订单和原始零售订单
	 * @param token
	 * @param param
	 * @return
	 */
	@PostMapping(value = "pushBillsToewms")
	public String pushBillsToewms(@RequestParam(name = "token", required = false) String token,
								  @RequestParam(name = "param", required = false) String param) {
		return iRetailOrderBillService.pushBillsToewms(token, param);
	}

	/**
	 * JOB专用新增接口
	 * @param token
	 * @param orderList
	 * @return
	 */
	@PostMapping(value = "createOrderList")
	public ServiceResult createOrderList(@RequestParam(name = "token", required = true) String token,
								  @RequestBody List<RetailOrderBill> orderList) {
		return iRetailOrderBillService.createOrders(token, orderList);
	}

	@PostMapping(value = "ewmsStockout")
	public ServiceResult ewmsStockout(@RequestParam(name = "token", required = true) String token,
								  @RequestParam(name = "billNo", required = true) String billNo) {
		return iRetailOrderBillService.ewmsStockout(token, billNo);
	}

	@PostMapping(value = "modifyDistributionState")
	public ServiceResult modifyDistributionState(@RequestParam(name = "token", required = true) String token,
												 @RequestBody RetailOrderBill[] retailOrderBills) {
		return iRetailOrderBillService.modifyDistributionState(token, retailOrderBills);
	}

}
