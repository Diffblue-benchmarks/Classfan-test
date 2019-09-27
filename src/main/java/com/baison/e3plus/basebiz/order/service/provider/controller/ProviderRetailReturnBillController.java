package com.baison.e3plus.basebiz.order.service.provider.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baison.e3plus.basebiz.order.api.model.RetailReturnBill;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnBillService;
import com.baison.e3plus.basebiz.order.api.thirdParty.RetailReturnBillEntity;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

/**
 * 生产者模块-IRetailReturnBillService
 * 
 * @author junyi.yue
 *
 */
@RestController
@RequestMapping(value = "providerRetailReturnBillService")
public class ProviderRetailReturnBillController {

	@Autowired
	private IRetailReturnBillService iRetailReturnBillService;
	
	@PostMapping(value = "create")
	public ServiceResult create(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnBill[] beans) {
		return iRetailReturnBillService.create(token, beans);
	}
	
	@PostMapping(value = "modify")
	public ServiceResult modify(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnBill[] beans) {
		return iRetailReturnBillService.modify(token, beans);
	}
	
	@PostMapping(value = "remove")
	public ServiceResult remove(@RequestParam(name = "token", required = true) String token, @RequestBody Object[] ids) {
		return iRetailReturnBillService.remove(token, ids);
	}
	
	@PostMapping(value = "findById")
	public RetailReturnBill[] findById(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "ids", required = false) Object[] ids,
			@RequestParam(name = "selectFields", required = false)  String[] selectFields) {
		return iRetailReturnBillService.findById(token, ids, selectFields);
	}
	
	@PostMapping(value = "getListCount")
	public long getListCount(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector) {
		return iRetailReturnBillService.getListCount(token, selector);
	}
	
	@PostMapping(value = "queryPage")
	public RetailReturnBill[] queryPage(@RequestParam(name = "token", required = true) String token, @RequestBody E3Selector selector, 
			@RequestParam(name = "pageSize", required = true) int pageSize, 
			@RequestParam(name = "pageIndex", required = true) int pageIndex) {
		return iRetailReturnBillService.queryPage(token, selector,pageSize , pageIndex);
	}
	
	@PostMapping(value = "submit")
	public ServiceResult submit(@RequestParam(name = "token", required = true) String token, @RequestBody Object billId) {
		return iRetailReturnBillService.submit(token, billId);
	}
	
	@PostMapping(value = "retreat")
	public ServiceResult retreat(@RequestParam(name = "token", required = true) String token, @RequestBody Object billId) {
		return iRetailReturnBillService.retreat(token, billId);
	}
	
	@PostMapping(value = "complete")
	public ServiceResult complete(@RequestParam(name = "token", required = true) String token, @RequestBody Object billId, @RequestParam(name = "wmsDate", required = true) Date wmsDate) {
		return iRetailReturnBillService.complete(token, billId, wmsDate);
	}
	
	@PostMapping(value = "term")
	public ServiceResult term(@RequestParam(name = "token", required = true) String token, @RequestBody Object billId) {
		return iRetailReturnBillService.term(token, billId);
	}
	
	@PostMapping(value = "queryObjectBySql")
	public RetailReturnBill[] queryObjectBySql(@RequestParam(name = "token", required = true) String token, @RequestParam(name = "sql", required = false) String sql) {
		return iRetailReturnBillService.queryObjectBySql(token, sql);
	}

	@PostMapping(value = "updateBatchByBillId")
	public int updateBatchByBillId(@RequestBody Map map) {
		return iRetailReturnBillService.updateBatchByBillId(map);
	}
	
	@PostMapping(value = "updateByPrimaryKeySelective")
	public ServiceResult updateByPrimaryKeySelective(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnBill[] beans) {
		return iRetailReturnBillService.updateByPrimaryKeySelective(token, beans);
	}
	
	@PostMapping(value = "savePasseBack")
	public ServiceResult savePasseBack(@RequestParam(name = "token", required = true) String token, 
			@RequestBody RetailReturnBillEntity beans) {
		return iRetailReturnBillService.savePasseBack(token, beans.getBillList(), beans.getBillDetail(), beans.getIds(), beans.getWmsDate());
	}
	
	@PostMapping(value = "updateByBillNo")
	public void updateByBillNo(@RequestParam(name = "billno", required = false) String billno, 
			@RequestParam(name = "receiverName", required = false) String receiverName, 
			@RequestParam(name = "receiverMobile", required = false)String receiverMobile, 
			@RequestParam(name = "receiverTel", required = false) String receiverTel, 
			@RequestParam(name = "key", required = false) String key) {
		iRetailReturnBillService.updateByBillNo(billno, receiverName, receiverMobile, receiverTel, key);
	}
	
	@PostMapping(value = "selectBySap")
	public RetailReturnBill[] selectBySap(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "selectfields", required = false) String selectfields) {
		return iRetailReturnBillService.selectBySap(token, selectfields);
	}
	
	@PostMapping(value = "updateIsConfirm")
	public int updateIsConfirm(@RequestBody Map map) {
		return iRetailReturnBillService.updateIsConfirm(map);
	}
	
	@PostMapping(value = "selectIdBySap")
	public RetailReturnBill[] selectIdBySap(@RequestParam(name = "token", required = true) String token, 
			@RequestParam(name = "selectfields", required = false) String selectfields) {
		return iRetailReturnBillService.selectIdBySap(token, selectfields);
	}
	

}
