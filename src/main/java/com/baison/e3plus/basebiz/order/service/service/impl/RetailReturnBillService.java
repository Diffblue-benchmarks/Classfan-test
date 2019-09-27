package com.baison.e3plus.basebiz.order.service.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerAdvancedGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.basebiz.order.service.feignclient.stock.ConsumerAdvancedOrderOperateStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleSingleProduct;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleGoods;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.basebiz.order.api.common.OrderStatus;
import com.baison.e3plus.basebiz.order.api.errorcode.AdvancedOrderErrorCode;
import com.baison.e3plus.basebiz.order.api.model.RetailOrderBill;
import com.baison.e3plus.basebiz.order.api.model.RetailReturnBill;
import com.baison.e3plus.basebiz.order.api.model.RetailReturnChasingGoodsDetail;
import com.baison.e3plus.basebiz.order.api.model.RetailReturnGoodsDetail;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDisSKUDetail;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeParas;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderDistributeResponse;
import com.baison.e3plus.basebiz.order.api.model.calculate.OrderResponseInfo;
import com.baison.e3plus.basebiz.order.api.model.calculate.SendSKUDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderBillService;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnBillService;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnChasingGoodsDetailService;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnGoodsDetailService;
import com.baison.e3plus.basebiz.order.api.service.calculate.IOrderDistributeCalculateService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailReturnBillMapper;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedChannelService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedShopService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAdvancedWareHouseService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerAuthObjectService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerCloseBillStrategyService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerCodeRuleService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerCurrencyTypeService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerDataAuthService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerExchangeRateService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerJdWmsBillRelationService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerOwnerService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerSettleMethodService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerWhareaTypeService;
import com.baison.e3plus.biz.stock.api.model.stock.AreaGoodsStockParas;
import com.baison.e3plus.biz.stock.api.model.stock.GoodsStockParas;
import com.baison.e3plus.biz.sales.api.model.thirdParty.RetailTeturnBillEntity;
import com.baison.e3plus.biz.sales.api.model.thirdParty.RetailTeturnBillsEntity;
import com.baison.e3plus.biz.stock.api.model.virtualwarehouse.VirtualDistributeParam;
import com.baison.e3plus.biz.stock.api.model.virtualwarehouse.VirtualDistributeParamDetail;
import com.baison.e3plus.biz.support.api.business.advanced.api.model.AdvancedChannel;
import com.baison.e3plus.biz.support.api.business.advanced.model.JdWmsBillRelation;
import com.baison.e3plus.biz.support.api.business.advanced.model.closebill.CloseBillStrategy;
import com.baison.e3plus.biz.support.api.business.advanced.publicrecord.model.warehouse.AdvancedWareHouse;
import com.baison.e3plus.biz.support.api.manyunit.model.ExchangeRate;
import com.baison.e3plus.biz.support.api.publicrecord.model.channel.Channel;
import com.baison.e3plus.biz.support.api.publicrecord.model.currencytype.CurrencyType;
import com.baison.e3plus.biz.support.api.publicrecord.model.owner.Owner;
import com.baison.e3plus.biz.support.api.publicrecord.model.settlemethod.SettleMethod;
import com.baison.e3plus.biz.support.api.publicrecord.model.shop.Shop;
import com.baison.e3plus.biz.support.api.publicrecord.model.warehouse.WareHouse;
import com.baison.e3plus.biz.support.api.publicrecord.model.whareatype.WhareaType;
import com.baison.e3plus.biz.support.api.publicrecord.service.OrgUtil;
import com.baison.e3plus.biz.support.api.user.model.UserInfo;
import com.baison.e3plus.biz.support.api.userauth.model.AuthObject;
import com.baison.e3plus.biz.support.api.util.LoginUtil;
import com.google.common.collect.Lists;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.bscore.other.ServiceUtils;
import com.baison.e3plus.common.bscore.utils.ObjectUtil;
import com.baison.e3plus.common.bscore.utils.ResourceUtils;
import com.baison.e3plus.common.bscore.utils.StringUtil;
import com.baison.e3plus.common.cncore.BillType;
import com.baison.e3plus.common.cncore.common.Status;
import com.baison.e3plus.common.cncore.common.exception.ServiceResultErrorException;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.cncore.session.Session;
import com.baison.e3plus.common.orm.metadata.Condition;

@Service
public class RetailReturnBillService implements IRetailReturnBillService {
	
	private Logger logger = LoggerFactory.getLogger(RetailReturnBillService.class);

    @Autowired
    private IRetailReturnGoodsDetailService goodsDetailService;
    @Autowired
    private IRetailReturnChasingGoodsDetailService chasingGoodsDetailService;
    @Autowired
    private ConsumerAdvancedWareHouseService wareHouseService;
    @Autowired
    private ConsumerCodeRuleService codeRuleService;
    @Autowired
    private ConsumerAdvancedShopService shopService;
    @Autowired
    private ConsumerAdvancedOrderOperateStockService advancedOrderOperateStockService;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private ConsumerWhareaTypeService whareaTypeService;
    @Autowired
    private ConsumerAdvancedChannelService channelService;
    @Autowired
    private ConsumerCurrencyTypeService currencyTypeService;
    @Autowired
    private ConsumerSettleMethodService settleMethodService;
    @Autowired
    private ConsumerAdvancedGoodsService goodsService;
    @Autowired
    private ConsumerSingleProductService skuService;
    @Autowired
    private ConsumerOwnerService ownerService;
    @Autowired
    private ConsumerCloseBillStrategyService closeBillStrategyService;

    @Autowired
    private RetailReturnBillMapper retailReturnBillMapper;

    @Autowired
    public ConsumerAuthObjectService authObjectService;

    @Autowired
    public ConsumerDataAuthService dataAuthService;
    @Autowired
    private ConsumerExchangeRateService exchangeRateService;
    
    @Autowired
    private IOrderDistributeCalculateService orderDistributeCalculateService;
    @Autowired
    private IRetailOrderBillService retailOrderBillService;

    @Autowired
    private ConsumerJdWmsBillRelationService getJdwmsBillRelationService;

    @Override
    @Transactional
	public ServiceResult submit(String token, Object billId) {
		ServiceResult result = new ServiceResult();

		try {
			RetailReturnBill dbOrder = findAndCheckOrder(token, result, billId, OrderStatus.SUBMIT);
			if (result.hasError()) {
				return result;
			}

			if (OrderStatus.SUBMIT.equals(dbOrder.getStatus())) {
				return result;
			}

			findAndCheckGoodsDetails(token, result, dbOrder);
			if (result.hasError()) {
				return result;
			}
			RetailReturnChasingGoodsDetail[] chasingGoodsDetails = findAndCheckChasingGoodsDetails(token, result,
					dbOrder);
			if (result.hasError()) {
				return result;
			}

			/**
			 * 处理追单仓库问题：如果没有传入追单仓库，则需要调用分单接口，找到分单仓库。如果有拆单，则将仓库信息写到追单明细上面
			 */
			boolean isFitWareHouse = false;
			if (dbOrder.getChasingWarehouseId() == null && chasingGoodsDetails != null
					&& chasingGoodsDetails.length > 0) {
				List<RetailReturnChasingGoodsDetail> fitWareHouseChasingGoosDetails = fitChasingGoodsWareHouse(token,
						dbOrder, chasingGoodsDetails, result);
				if (fitWareHouseChasingGoosDetails.size() > 0) {

					List<Object> pkIdList = new ArrayList<>();
					for (RetailReturnChasingGoodsDetail goodsDetail : chasingGoodsDetails) {
						pkIdList.add(goodsDetail.getId());
					}

					// 有拆单，删除旧明细，生成新明细
					chasingGoodsDetailService.remove(token, pkIdList.toArray());

					chasingGoodsDetailService.create(token, fitWareHouseChasingGoosDetails
							.toArray(new RetailReturnChasingGoodsDetail[fitWareHouseChasingGoosDetails.size()]));
				}
				isFitWareHouse = true;
			}

			setBeanValue(token, dbOrder, OrderStatus.SUBMIT);
			retailReturnBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailReturnBill[] { dbOrder }));

			if (!isFitWareHouse) {
				if (chasingGoodsDetails != null && chasingGoodsDetails.length > 0) {
					dbOrder.setChasingDetails(Arrays.asList(chasingGoodsDetails));
				}
				// 更新库存
				result = updateLockStock(token, dbOrder, true);
			}
		} catch (Exception e) {
			result.addErrorObjectNotThrow(billId, "", e.getMessage());
			throw new ServiceResultErrorException(result);
		}

		return result;
	}

    private RetailReturnChasingGoodsDetail[] findAndCheckChasingGoodsDetails(String token, ServiceResult result,
                                                                             RetailReturnBill bill) {
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("returnBillId", bill.getId()));

        RetailReturnChasingGoodsDetail[] chasingGoodsDetails = chasingGoodsDetailService.queryPage(token, selector, -1,
                -1);
        if (chasingGoodsDetails == null || chasingGoodsDetails.length == 0) {
            return null;
        }

        for (RetailReturnChasingGoodsDetail goodsDetail : chasingGoodsDetails) {
            if (goodsDetail.getSingleProductId() == null
                    || (bill.getChasingWhAreaTypeId() == null && bill.getShopId() == null)) {
                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_015);
            }
            if (goodsDetail.getGoodsQty() == null || goodsDetail.getGoodsQty().equals(0)) {
                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_048,
                        ResourceUtils.get("e3.errorCode.stock.99048", goodsDetail.getGoodsId()));
            }
        }

        return chasingGoodsDetails;
    }

    private RetailReturnGoodsDetail[] findAndCheckGoodsDetails(String token, ServiceResult result,
                                                               RetailReturnBill bill) {
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("returnBillId", bill.getId()));

        RetailReturnGoodsDetail[] goodsDetails = goodsDetailService.queryPage(token, selector, -1, -1);
        if (goodsDetails == null || goodsDetails.length == 0) {
            result.addErrorObject("", AdvancedOrderErrorCode.STOCK_012, ResourceUtils.get("e3.errorCode.stock.99012"));
            return null;
        }

        for (RetailReturnGoodsDetail goodsDetail : goodsDetails) {
            if (goodsDetail.getSingleProductId() == null
                    || (bill.getReturnWarehouseId() == null && bill.getShopId() == null)) {
                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_015,
                        ResourceUtils.get(AdvancedOrderErrorCode.STOCK_015, goodsDetail.getGoodsId()));
            }
            if (goodsDetail.getQty() == null || goodsDetail.getQty().equals(0)) {
                result.addErrorObject("", AdvancedOrderErrorCode.STOCK_048,
                        ResourceUtils.get("e3.errorCode.stock.99048", goodsDetail.getGoodsId()));
            }
        }

        return goodsDetails;
    }

    private RetailReturnBill findAndCheckOrder(String token, ServiceResult result, Object billId,
                                               Integer targetStatus) {
        if (billId == null) {
            result.addErrorObject("", "", "billId is null!");
            return null;
        }

        Object[] objects;
        if (billId instanceof ArrayList) {
            objects = ((ArrayList) billId).toArray(new Object[0]);
        } else {
            objects = new Object[]{billId};
        }
        RetailReturnBill[] dbOrders = findById(token, objects, null);
        if (dbOrders == null || dbOrders.length == 0) {
            result.addErrorObject("", "", "data not exist in db!");
            return null;
        }

        RetailReturnBill dbOrder = dbOrders[0];
        // 校验状态是否合法
        if (!getPreStatus().get(targetStatus).contains(dbOrder.getStatus())) {
            result.addErrorObject("", "e3.errorCode.publicrecord.44001", StringUtil.format(
                    ResourceUtils.get("e3.errorCode.publicrecord.44001"), OrderStatus.getStatusDesc(targetStatus)));
            return null;
        }
        return dbOrder;
    }

    private ServiceResult updateLockStock(String token, RetailReturnBill bill, boolean isIncrese) {

        ServiceResult result = new ServiceResult();
        // 如果有追单明细，需要更新库存
        List<RetailReturnChasingGoodsDetail> chasingDetails = bill.getChasingDetails();
        if (chasingDetails != null && chasingDetails.size() > 0) {
            List<VirtualDistributeParam> vdparamList = getVdparamList(token, bill);

            GoodsStockParas[] paras = createParas(bill, chasingDetails);

            RetailTeturnBillEntity retailTeturnBillEntity = new RetailTeturnBillEntity();
            retailTeturnBillEntity.setParas(paras);
            retailTeturnBillEntity.setVdparamList(vdparamList);
            if (isIncrese) {
                result = advancedOrderOperateStockService.retailOrderSubmitLockStock(token, retailTeturnBillEntity);
            } else {
                result = advancedOrderOperateStockService.retailOrderReleaseLockStock(token, retailTeturnBillEntity);
            }
            if (result.hasError()) {
                throw new ServiceResultErrorException(result);
            }
        }

        return result;
    }

    private GoodsStockParas[] createParas(RetailReturnBill bill, List<RetailReturnChasingGoodsDetail> chasingDetails) {

        List<GoodsStockParas> paras = new ArrayList<>();

		for (RetailReturnChasingGoodsDetail detail : chasingDetails) {
			GoodsStockParas para = new GoodsStockParas();
			para.setBillId(bill.getId().toString());
			para.setBillNo(bill.getBillno());
			para.setBillDate(bill.getBillDate());
			para.setGoodsId(detail.getGoodsId());
			para.setGoodsCode(detail.getGoods().getCode());
			para.setSingleProductId(detail.getSingleProductId());
			para.setSingleProductCode(detail.getSingleProduct().getCode());
			para.setWareHouseId(
					detail.getWareHouseId() == null ? bill.getChasingWarehouseId() : detail.getWareHouseId());
			para.setWhareaTypeId(
					detail.getWhareaTypeId() == null ? bill.getChasingWhAreaTypeId() : detail.getWhareaTypeId());
			para.setQuantity(detail.getGoodsQty());
			para.setOwnerId(bill.getOwnerId());
			paras.add(para);
		}

        return paras.toArray(new GoodsStockParas[paras.size()]);
    }

    private List<VirtualDistributeParam> getVdparamList(String token, RetailReturnBill bill) {
        List<VirtualDistributeParam> vdparamList = new ArrayList<>();

        List<RetailReturnChasingGoodsDetail> chasingDetails = bill.getChasingDetails();

        if (chasingDetails == null || chasingDetails.size() == 0) {
            return vdparamList;
        }

        Long wareHouseId = bill.getChasingWarehouseId();
        Long shopId = bill.getShopId();
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("shopId", shopId));
        selector.addSelectFields("code");
        Shop[] shops = shopService.findShop(token, selector);
        String shopCode = shops[0].getCode();

        // 操作分配池库存
        VirtualDistributeParam vdparam = new VirtualDistributeParam();
        vdparam.setShopCode(shopCode);
        vdparam.setBillNo(bill.getBillno());

        List<VirtualDistributeParamDetail> paraDetailList = new ArrayList<>();
        for (RetailReturnChasingGoodsDetail goodsDetail : chasingDetails) {
            VirtualDistributeParamDetail paraDetail = new VirtualDistributeParamDetail();
            paraDetail.setSingleId(goodsDetail.getSingleProductId());
            paraDetail.setWareHouseId(wareHouseId);
            paraDetail.setWhareaTypeId(bill.getChasingWhAreaTypeId());
            Integer quantity = goodsDetail.getGoodsQty();
            if (quantity == null || quantity == 0) {
                continue;
            }
            paraDetail.setQty(quantity);
            paraDetailList.add(paraDetail);
        }
        vdparam.setDetails(paraDetailList);
        vdparamList.add(vdparam);

        return vdparamList;
    }

    private void setBeanValue(String token, RetailReturnBill retailReturnBill, Integer targetStatus) {
        Session session = LoginUtil.checkUserLogin(token);
        if (targetStatus.equals(OrderStatus.TERM)) {// 作废
            retailReturnBill.setTermBy(session.userName);
            retailReturnBill.setTermDate(new Date());
        }
        if (targetStatus.equals(OrderStatus.COMPLETE)) {// 完成
            retailReturnBill.setCompleteBy(session.userName);
            retailReturnBill.setCompleteDate(new Date());
        }
        if (targetStatus.equals(OrderStatus.SUBMIT)) {// 确认
            retailReturnBill.setConfirmBy(session.userName);
            retailReturnBill.setConfirmDate(new Date());
        }
        retailReturnBill.setStatus(targetStatus);
    }

    @Override
    @Transactional
    public ServiceResult complete(String token, Object billId,Date wmsDate) {
        ServiceResult result = new ServiceResult();
        RetailReturnBill dbOrder = findAndCheckOrder(token, result, billId, OrderStatus.COMPLETE);
        if (result.hasError()) {
            return result;
        }
        if (OrderStatus.COMPLETE.equals(dbOrder.getStatus())) {
            return result;
        }
        RetailReturnGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token, result, dbOrder);
        if (result.hasError()) {
            return result;
        }
        RetailReturnChasingGoodsDetail[] chasingGoodsDetails = findAndCheckChasingGoodsDetails(token, result, dbOrder);
        if (result.hasError()) {
            return result;
        }
        
        dbOrder.setDetails(Arrays.asList(goodsDetails));
        if (chasingGoodsDetails != null && chasingGoodsDetails.length > 0) {
            dbOrder.setChasingDetails(Arrays.asList(chasingGoodsDetails));
        }
        
        setBeanValue(token, dbOrder, OrderStatus.COMPLETE);
        //wms 回传时间
        dbOrder.setWmsDate(wmsDate);

        Shop shop = findShopById(token, dbOrder.getShopId());

        result = closeBill(token, dbOrder, BillType.RETAILRETURNBILL, shop.getChannelId());

        if (result.hasError()) {
            return result;
        }

        retailReturnBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailReturnBill[]{dbOrder}));

        List<RetailReturnBill> bills = new ArrayList<>();
        bills.add(dbOrder);
        List<VirtualDistributeParam> vdparamList = getVdparamList(token, bills.get(0));

        GoodsStockParas[] chasingParas = new GoodsStockParas[0];
        AreaGoodsStockParas[] paras = new AreaGoodsStockParas[0];

        for (RetailReturnBill advancedBill : bills) {
            // 封装参数
            List<RetailReturnGoodsDetail> details = advancedBill.getDetails();
            paras = createGoodsParas(token, dbOrder, details);

            List<RetailReturnChasingGoodsDetail> chasingDetails = advancedBill.getChasingDetails();
            if (chasingDetails != null && chasingDetails.size() > 0) {
                chasingParas = createParas(dbOrder, chasingDetails);
            }
        }
        RetailReturnBill[] dataArray = new RetailReturnBill[0];
        if (bills != null) {
            dataArray = bills.toArray(dataArray);
        }

        // 验收时需要回退信用额度 ，等直营上线时再添加 TODO
        RetailTeturnBillsEntity retailTeturnBillsEntity = new RetailTeturnBillsEntity();
        retailTeturnBillsEntity.setParas(paras);
        retailTeturnBillsEntity.setChasingParas(chasingParas);
        retailTeturnBillsEntity.setVdparamList(vdparamList);
        ServiceResult updateStockResult = advancedOrderOperateStockService.retailReturnOrderCheck(token, retailTeturnBillsEntity);

        if (updateStockResult.hasError()) {
            throw new ServiceResultErrorException(updateStockResult);
        }

        return result;
    }
    
	private List<RetailReturnChasingGoodsDetail> fitChasingGoodsWareHouse(String token, RetailReturnBill dbOrder,
			RetailReturnChasingGoodsDetail[] chasingGoodsDetails, ServiceResult result) {

		List<RetailReturnChasingGoodsDetail> returnDetails = new ArrayList<>();
		
		List<RetailReturnBill> dataList = Arrays.asList(dbOrder);
		queryBaseFieldDatas(token, dataList);
		OrderDistributeParas paras = createOrderDistributeParas(token, dbOrder, chasingGoodsDetails);

		OrderDistributeResponse response = orderDistributeCalculateService.distributeOrder(token, paras);

		if (response.getIsSuccess()) {
			List<OrderResponseInfo> orderResponseInfo = response.getOrderResponseInfo();
			if (orderResponseInfo.size() > 1) {// 有拆单
				for (OrderResponseInfo info : orderResponseInfo) {
					for(SendSKUDetail detail : info.getSkuDetail()) {
						RetailReturnChasingGoodsDetail goodsDetail = findChasingGoodsDetail(chasingGoodsDetails, detail);
						RetailReturnChasingGoodsDetail cloneDetail = ObjectUtil.clone(goodsDetail);
						cloneDetail.setId(null);
						cloneDetail.setWareHouseId(detail.getWareHouseId());
						cloneDetail.setWhareaTypeId(detail.getWhareaTypeId());
						cloneDetail.setGoodsQty(detail.getQty());
						
						returnDetails.add(cloneDetail);
					}
				}
			} else {
				// 无拆单
				dbOrder.setChasingWarehouseId(Long.parseLong(orderResponseInfo.get(0).getWareHouseId()));
				for (SendSKUDetail detail : orderResponseInfo.get(0).getSkuDetail()) {
					dbOrder.setChasingWhAreaTypeId(detail.getWhareaTypeId());
					RetailReturnChasingGoodsDetail goodsDetail = findChasingGoodsDetail(chasingGoodsDetails, detail);
					if (goodsDetail != null) {
						goodsDetail.setWareHouseId(detail.getWareHouseId());
						goodsDetail.setWhareaTypeId(detail.getWhareaTypeId());
					}
				}
			}
		} else {
			result.addErrorObject("", response.getErrorCode(), response.getDesc());
			throw new ServiceResultErrorException(result);
		}
		
		return returnDetails;
	}

    private RetailReturnChasingGoodsDetail findChasingGoodsDetail(RetailReturnChasingGoodsDetail[] chasingGoodsDetails,
			SendSKUDetail detail) {

		for (RetailReturnChasingGoodsDetail goodsDetail : chasingGoodsDetails) {
			if (goodsDetail.getSku().equals(detail.getSku()) && goodsDetail.getGiftFlg().equals(detail.getIsGift())) {
				return goodsDetail;
			}
		}
		return null;
	}

	private OrderDistributeParas createOrderDistributeParas(String token,RetailReturnBill dbOrder,
			RetailReturnChasingGoodsDetail[] chasingGoodsDetails) {
    	
    	OrderDistributeParas paras = new OrderDistributeParas();
    	paras.setOrderBillNo(dbOrder.getBillno());
    	paras.setShopCode(dbOrder.getShop().getCode());
    	paras.setContryCode(dbOrder.getReceiverCountry());
    	paras.setProvinceCode(dbOrder.getReceiverProvince());
    	paras.setCityCode(dbOrder.getReceiverCity());
    	paras.setAreaCode(dbOrder.getReceiverDistrict());
    	paras.setIsAllowPart("1");
    	paras.setIsCreateNewBillNo("0");// 不用生成新单号
    	
    	// sku
    	List<OrderDisSKUDetail> skuDetails = new ArrayList<>();
    	for(RetailReturnChasingGoodsDetail detail:chasingGoodsDetails) {
    		OrderDisSKUDetail skuDetail = new OrderDisSKUDetail();
    		skuDetail.setSku(detail.getSingleProduct().getCode());
    		skuDetail.setQty(detail.getGoodsQty());
    		skuDetail.setIsGift(detail.getGiftFlg());
    		skuDetails.add(skuDetail);
    	}
    	
    	paras.setSkuDetail(skuDetails.toArray(new OrderDisSKUDetail[skuDetails.size()]));
    	
    	return paras;
	}

	private ServiceResult closeBill(String token, RetailReturnBill returnBill, String billType, Long channelId) {
        ServiceResult result = new ServiceResult();

        LocalDate localDate = LocalDate.now();
        // 查询品牌信息
        Long brandId = null;
        E3Selector brandselector = new E3Selector();
        brandselector.addSelectFields("brandId");

        if (returnBill.getDetails() == null || returnBill.getDetails().get(0) == null || returnBill.getDetails().get(0).getGoodsId() == null) {
            result.addErrorObject(null, AdvancedOrderErrorCode.CLOSE_BILL_003, ResourceUtils.get(AdvancedOrderErrorCode.CLOSE_BILL_003));
            return result;
        }

        brandselector.addFilterField(new E3FilterField("goodsId", returnBill.getDetails().get(0).getGoodsId()));
        SimpleGoods[] e3goods = goodsService.queryAdvancedSimpleGoods(token, brandselector);
        if (null != e3goods && e3goods.length > 0) {
            brandId = e3goods[0].getBrandId();
        }

        // 验证单据的
        E3Selector closeBillSelector = new E3Selector();
        if (brandId == null) {
            result.addErrorObject(null, AdvancedOrderErrorCode.CLOSE_BILL_001, ResourceUtils.get(AdvancedOrderErrorCode.CLOSE_BILL_001));
            return result;
        }

        if (channelId == null) {
            result.addErrorObject(null, AdvancedOrderErrorCode.CLOSE_BILL_002, ResourceUtils.get(AdvancedOrderErrorCode.CLOSE_BILL_002));
            return result;
        }
        closeBillSelector.addFilterField(new E3FilterField("brandId", brandId));
        closeBillSelector.addFilterField(new E3FilterField("channelId", channelId));
        closeBillSelector.addFilterField(new E3FilterField("billType", billType));
        closeBillSelector.addFilterField(new E3FilterField("status", Status.ENABLE));

        // 符合的记录只有一条
        CloseBillStrategy[] strategys = closeBillStrategyService.queryObject(token, closeBillSelector);
        if (null != strategys && strategys.length >= 1) {
            CloseBillStrategy strategy = strategys[0];
            Date closeDate = strategy.getCloseDate();

            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            // 将时分秒,毫秒域清零
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            if (null != closeDate && (cal.getTime().after(closeDate))) {
                LocalDate nextMonth = localDate.plusMonths(1);
                LocalDate nextMonthFirstDay = LocalDate.of(nextMonth.getYear(), nextMonth.getMonthValue(), 1);
                Date newBillDate = Date.from(nextMonthFirstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
                returnBill.setBillDate(newBillDate);
            }
        }
        return result;
    }


    /**
     * 根据店铺ID获取与上级组织的关系
     *
     * @param token
     * @param shopId
     * @return
     */
    private Shop findShopById(String token, Long shopId) {
        Shop shop = null;
        if (null == shopId) {
            return shop;
        }
        E3Selector shopE3Selector = new E3Selector();
        shopE3Selector.addFilterField(new E3FilterField("shopId", shopId));
        shopE3Selector.addSelectFields("channelId");
        Shop[] shops = shopService.findShop(token, shopE3Selector);
        if (null != shops && shops.length > 0) {
            shop = shops[0];
        }
        return shop;
    }

    @Override
    @Transactional
    public ServiceResult term(String token, Object billId) {
        ServiceResult result = new ServiceResult();
        RetailReturnBill dbOrder = findAndCheckOrder(token, result, billId, OrderStatus.TERM);
        if (result.hasError()) {
            return result;
        }
        if (OrderStatus.TERM.equals(dbOrder.getStatus())) {
            return result;
        }
        RetailReturnGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token, result, dbOrder);
        if (result.hasError()) {
            return result;
        }
        RetailReturnChasingGoodsDetail[] chasingGoodsDetails = findAndCheckChasingGoodsDetails(token, result, dbOrder);
        if (result.hasError()) {
            return result;
        }
        Integer status = dbOrder.getStatus();// 原单据状态
        setBeanValue(token, dbOrder, OrderStatus.TERM);
        retailReturnBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailReturnBill[]{dbOrder}));

        dbOrder.setDetails(Arrays.asList(goodsDetails));
        if (chasingGoodsDetails != null && chasingGoodsDetails.length > 0) {
            dbOrder.setChasingDetails(Arrays.asList(chasingGoodsDetails));
        }
        boolean needUpdateStock = OrderStatus.SUBMIT.equals(status) || OrderStatus.COMPLETE.equals(status);

        if (needUpdateStock) {
            updateLockStock(token, dbOrder, false);
        }

        //京东仓库 需要更新 JdWmsBillRelation  表状态
        E3Selector selector = new E3Selector();
        selector.addSelectFields("outWareHouseType");
        selector.addFilterField(new E3FilterField("wareHouseId",dbOrder.getReturnWarehouse().getWareHouseId()));
        WareHouse[]  wareHouses= wareHouseService.queryWareHouse(token,selector);
        if (null!=wareHouses &&wareHouses.length>0){
            //京东云仓
            if (((AdvancedWareHouse)wareHouses[0]).getOutWareHouseType().equals("2")){
                selector = new E3Selector();
                selector.addFilterField(new E3FilterField("e3",dbOrder.getBillno()));
                selector.addSelectFields("id");
                selector.addSelectFields("status");
                JdWmsBillRelation[]  jdWmsBillRelations = getJdwmsBillRelationService.queryObject(token,selector);
                if (null !=jdWmsBillRelations && jdWmsBillRelations.length>0){
                    for (JdWmsBillRelation jdWmsBillRelation : jdWmsBillRelations) {
                        jdWmsBillRelation.setStatus("4");//已取消
                    }
                    result =getJdwmsBillRelationService.modifyObject(token,jdWmsBillRelations);
                }

            }
        }
        return result;
    }

    @Override
    @Transactional
    public ServiceResult retreat(String token, Object billId) {
        ServiceResult result = new ServiceResult();
        RetailReturnBill dbOrder = findAndCheckOrder(token, result, billId, OrderStatus.INITIAL);
        if (result.hasError()) {
            return result;
        }

        RetailReturnGoodsDetail[] goodsDetails = findAndCheckGoodsDetails(token, result, dbOrder);
        if (result.hasError()) {
            return result;
        }
        RetailReturnChasingGoodsDetail[] chasingGoodsDetails = findAndCheckChasingGoodsDetails(token, result, dbOrder);
        if (result.hasError()) {
            return result;
        }
        dbOrder.setDetails(Arrays.asList(goodsDetails));
        if (chasingGoodsDetails != null && chasingGoodsDetails.length > 0) {
            dbOrder.setChasingDetails(Arrays.asList(chasingGoodsDetails));
        }
        setBeanValue(token, dbOrder, OrderStatus.INITIAL);
        retailReturnBillMapper.updateByPrimaryKeySelective(Arrays.asList(new RetailReturnBill[]{dbOrder}));

        // 撤销退单，将分单结果数据设为废除，以便可以重新分单
        IOrderDistributeCalculateService calculateService = ServiceUtils
                .getService(IOrderDistributeCalculateService.class);
        calculateService.disableOrderDistributeLog(token, dbOrder.getBillno());
        
        // 更新库存
        updateLockStock(token, dbOrder, false);
        return new ServiceResult();
    }

    @Override
    @Transactional
    public ServiceResult create(String token, RetailReturnBill[] objects) {
    	try {
    		Session session = LoginUtil.checkUserLogin(token);
            ServiceResult result = new ServiceResult();

            // 其他赋值
            setOtherBeanValue(token, objects);
            // 明细保存
            saveDetailsWhenCreate(token, objects);
            
            // 判断传入的单据编号是否存在重复，有则提示
            // 本身单据编号重复集合
            List<String> billNoList = new ArrayList<>();
            // 与数据库单据编号重复集合
            List<String> billNoListInDb = new ArrayList<>();
            //关联单据编号集合-关联订单主表 relateOrderBillNo
            Set<String> relateBillNoSet = new HashSet<>();

            String[] billNos = Arrays.stream(objects).map(t -> t.getBillno()).toArray(String[]::new);
            E3Selector selector = new E3Selector();
            selector.addSelectFields("billno");
            selector.addFilterField(new E3FilterField("billno", "in", billNos, E3FilterField.ANDOperator));
            RetailReturnBill[] retailReturnBills = queryPage(token, selector, -1, -1);
            for (int i = 0; i < objects.length; i++) {
                for (int j = i + 1; j < objects.length; j++) {
                    if (objects[i].getBillno() != null && objects[i].getBillno().equals(objects[j].getBillno())) {
                        billNoList.add(objects[i].getBillno());
                        break;
                    }
                }
                for (RetailReturnBill retailReturnBill : retailReturnBills) {
                    if (objects[i].getBillno() != null && objects[i].getBillno().equals(retailReturnBill.getBillno())) {
                        billNoListInDb.add(objects[i].getBillno());
                    }
                }
                if(objects[i].getRelateOrderBillNo() != null){
                    relateBillNoSet.add(objects[i].getRelateOrderBillNo());
                }
            }
            if (!billNoList.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(ResourceUtils.get("e3.errorCode.order.clothes.08004"));
                for (String billNoStr : billNoList) {
                    stringBuffer.append(billNoStr + ",");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                result.addErrorObject(null, "", stringBuffer.toString());
                return result;
            }
            if (!billNoListInDb.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(ResourceUtils.get("e3.errorCode.order.clothes.08004"));
                for (String billNoStr : billNoListInDb) {
                    stringBuffer.append(billNoStr + ",");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                result.addErrorObject(null, "", stringBuffer.toString());
                return result;
            }
            for (RetailReturnBill bill : objects) {
                String billNo = bill.getBillno();
                if (billNo == null || "".equals(billNo)) {
                    billNo = generateBillNo(token, bill);
                    bill.setBillno(billNo);
                }
                Integer status = bill.getStatus();
                if (status==null) {
                	 bill.setStatus(OrderStatus.INITIAL);
                     bill.setCreateBy(session.userName);
                     bill.setCreateDate(new Date());
				}else {
					if (status == 0) {
						bill.setCreateBy(session.userName);
						bill.setCreateDate(new Date());
					} else if (status == 1) {
						bill.setCreateBy(session.userName);
						bill.setCreateDate(new Date());
						bill.setConfirmBy(session.userName);
						bill.setConfirmDate(new Date());
					}
				}
            }
            
            // 给本位币、汇率类型、汇率赋值
            setRetailBaseCurrency(token, objects);
            retailReturnBillMapper.insertSelective(Arrays.asList(objects));

            //更新销售订单表 - 更新为有退单记录
            List<RetailOrderBill> updateOrderBillList = new ArrayList<>();
            for (String billNo : relateBillNoSet) {
                RetailOrderBill retailOrderBill = new RetailOrderBill();
                if(StringUtil.isNotEmptyOrNull(billNo)){
                    //1为有退单记录
                    Integer hasReturn = 1;
                    retailOrderBill.setBillNo(billNo);
                    retailOrderBill.setHasReturn(hasReturn);
                    updateOrderBillList.add(retailOrderBill);
                }
            }
            retailOrderBillService.batchUpdateHasReturnByBillNo(updateOrderBillList);
            result.getSuccessObject().addAll(Arrays.asList(objects));
            return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
    }

    private void setRetailBaseCurrency(String token, RetailReturnBill[] dataArray) {
        List<RetailReturnBill> dataList = Arrays.asList(dataArray);
        List<Long> channelIds = LinqUtil.select(dataList, s -> s.getChannelId());
        List<Long> standardCurrencyIds = new ArrayList<>();// 本位币ids
        Map<Long, AdvancedChannel> channelMap = getChannelMap(token, channelIds, standardCurrencyIds);
        for (RetailReturnBill bill : dataArray) {
            Long currencyTypeId = bill.getCurrencyTypeId();// 结算币
            Long baseCurrencyId = bill.getBaseCurrencyId();// 本位币
            if (baseCurrencyId != null) {
                setAmountTax(token, currencyTypeId, baseCurrencyId, bill);
                continue;
            }
            // 本位币取值逻辑：取商店所属上级销售组织本位币
            Long channelId = bill.getChannelId();
            AdvancedChannel channel = channelMap.get(channelId);
            if (channel == null) {
                continue;
            }
            baseCurrencyId = channel.getStandardCurrencyId();
            bill.setBaseCurrencyId(baseCurrencyId);

            // 设置明细上的含税金额本位币等
            setAmountTax(token, currencyTypeId, baseCurrencyId, bill);
        }

    }

    private void setAmountTax(String token, Long currencyTypeId, Long baseCurrencyId, RetailReturnBill bill) {
        ExchangeRate exchangeRate = getExchangeRateMap(token, currencyTypeId, baseCurrencyId);
        if (exchangeRate != null) {
            bill.setExchangeratetype(exchangeRate.getExchangeratetype());// 汇率类型
            bill.setExchangerate(exchangeRate.getExchangerate());
        }

        // 设置明细上的含税金额本位币=含税金额*汇率
        Double totalTaxPrice = bill.getTotalTaxPrice() == null ? 0 : bill.getTotalTaxPrice();// 含税金额
        Double exchangerate2 = bill.getExchangerate() == null ? 0 : bill.getExchangerate();// 汇率
        Double amountStandardMoney = totalTaxPrice * exchangerate2;
        bill.setTotalTaxPriceStandardMoney(amountStandardMoney);// 含税金额本位币

        List<RetailReturnGoodsDetail> goodsDetails = bill.getDetails();
        if (goodsDetails == null || goodsDetails.size() == 0) {
            return;
        }

        for (RetailReturnGoodsDetail goodsDetail : goodsDetails) {
            Double amount = goodsDetail.getPayedAmount() == null ? 0d : goodsDetail.getPayedAmount();
            goodsDetail.setTotalTaxPrice(amount);// 含税金额
            goodsDetail.setTotalTaxPriceStandardMoney(amount * exchangerate2);// 含税金额本位币
        }
    }

    private ExchangeRate getExchangeRateMap(String token, Long currencyTypeId, Long standardCurrencyId) {
        if (currencyTypeId == null || standardCurrencyId == null) {
            return null;
        }
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("status", Status.ENABLE));
        selector.addFilterField(new E3FilterField("basecurrency", currencyTypeId));// 原币-结算币
        selector.addFilterField(new E3FilterField("exchangecurrency", standardCurrencyId));// 目标币-本位币
        selector.addSelectFields("exchangeratetype");
        selector.addSelectFields("exchangerate");
        ExchangeRate[] exchangeRates = exchangeRateService.query(token, selector);
        if (exchangeRates == null || exchangeRates.length == 0) {
            return null;
        }
        return exchangeRates[0];
    }

    private Map<Long, AdvancedChannel> getChannelMap(String token, List<Long> channelIds,
                                                 List<Long> standardCurrencyIds) {
        Map<Long, AdvancedChannel> channelMap = new HashMap<>();
        if (channelIds == null || channelIds.size() == 0) {
            return channelMap;
        }
        E3Selector selector = new E3Selector();
        selector.addSelectFields("channelId");
        selector.addSelectFields("code");
        selector.addSelectFields("name");
        selector.addSelectFields("standardCurrencyId");
        selector.addFilterField(new E3FilterField("channelId", channelIds));
        Channel[] channels = channelService.queryPageChannel(token, selector, -1, -1);
        if (channels == null || channels.length == 0) {
            return channelMap;
        }

        Long localCurrencyId = null;
        for (Channel object : channels) {
            if (object instanceof AdvancedChannel) {
                AdvancedChannel channel = (AdvancedChannel) object;
                Long standardCurrencyId = channel.getStandardCurrencyId();
                if (standardCurrencyId == null) {
                    if (localCurrencyId == null) {
                        localCurrencyId = getLocalCurrencyId(token);
                    }
                    standardCurrencyId = localCurrencyId;
                    channel.setStandardCurrencyId(standardCurrencyId);
                }
                // 本位币
                standardCurrencyIds.add(standardCurrencyId);

                // 如果组织档案上本位币为空，取币别档案上勾选本位币那条记录
                channelMap.put(channel.getChannelId(), channel);
            }
        }

        return channelMap;
    }

    private Long getLocalCurrencyId(String token) {
        E3Selector selector = new E3Selector();
        selector.addFilterField(new E3FilterField("localcurrency", "1"));
        selector.addSelectFields("CurrencyId");
        CurrencyType[] currencyTypeList = currencyTypeService.findCurrencyTypeList(token, selector);
        if (currencyTypeList == null || currencyTypeList.length == 0) {
            return null;
        }
        return currencyTypeList[0].getCurrencyId();
    }

    @Override
    @Transactional
    public ServiceResult modify(String token, RetailReturnBill[] objects) {
        LoginUtil.checkUserLogin(token);
        ServiceResult result = new ServiceResult();
        setOtherBeanValue(token, objects);// 其他赋值

        retailReturnBillMapper.updateByPrimaryKeySelective(Arrays.asList(objects));
        
        // 根据退单id删除所有明细，再新建
        List<Object> billIds = new ArrayList<>();
        for (RetailReturnBill bill : objects) {
			billIds.add(bill.getId());
		}
        goodsDetailService.removeByRetailReturnId(token, billIds.toArray());
        
        // 重新创建
        List<RetailReturnGoodsDetail> goodsDetails = new ArrayList<>();
        for (RetailReturnBill bill : objects) {
            List<RetailReturnGoodsDetail> goodsDetail = bill.getDetails();
            if (goodsDetail != null && goodsDetail.size() > 0) {
                goodsDetail.forEach(c -> {
                    c.setReturnBillId(bill.getId());
                });
                goodsDetails.addAll(goodsDetail);
            }
        }
        if (goodsDetails.size() > 0) {
            goodsDetailService.create(token, goodsDetails.toArray(new RetailReturnGoodsDetail[goodsDetails.size()]));
        }
        
        // 追单明细
        chasingGoodsDetailService.removeByRetailReturnId(token, billIds.toArray());
        
        List<RetailReturnChasingGoodsDetail> chasingGoodsDetails = new ArrayList<>();
        for (RetailReturnBill bill : objects) {
            List<RetailReturnChasingGoodsDetail> chasingGoodsDetail = bill.getChasingDetails();
            if (chasingGoodsDetail != null && chasingGoodsDetail.size() > 0) {
                chasingGoodsDetail.forEach(c -> {
                    c.setReturnBillId(bill.getId());
                });
                chasingGoodsDetails.addAll(chasingGoodsDetail);
            }
        }
		if (chasingGoodsDetails.size() > 0) {
			chasingGoodsDetailService.create(token,
					chasingGoodsDetails.toArray(new RetailReturnChasingGoodsDetail[chasingGoodsDetails.size()]));
		}
        
        return result;
    }

    private void setOtherBeanValue(String token, RetailReturnBill[] dataArray) {
        for (RetailReturnBill bill : dataArray) {
            Double marketGoodsAmount = bill.getMarketGoodsAmount() == null ? 0 : bill.getMarketGoodsAmount();
            Double discountFee = bill.getDiscountFee() == null ? 0 : bill.getDiscountFee();
            Double otherDiscountFee = bill.getOtherDiscountFee() == null ? 0 : bill.getOtherDiscountFee();
            Double shippingFee = bill.getShippingFee() == null ? 0 : bill.getShippingFee();
            Double shippingExtralFee = bill.getShippingExtRalFee() == null ? 0 : bill.getShippingExtRalFee();
            Double billReturnAmount = marketGoodsAmount - discountFee - otherDiscountFee + shippingFee
                    + shippingExtralFee;
            bill.setBillReturnAmount(billReturnAmount);
            bill.setActualReturnAmount(billReturnAmount);
            if (bill.getReturnWarehouseId() != null) {
                E3Selector selector = new E3Selector();
                selector.addFilterField(new E3FilterField("wareHouseId", bill.getReturnWarehouseId()));
                selector.addSelectFields("channelId");
                // 记录推送状态
                WareHouse[] load = wareHouseService.queryObject(token, selector);
                if(load != null && load.length > 0){
                    // 获取核算组织的货主
                    Owner owner = OrgUtil.getOwnerByChannel(token,
                            OrgUtil.getAccountChannelId(token, load[0].getChannelId()));
                    bill.setOwnerId(owner.getId());// 货主
                }
            }
        }
    }

    @Override
    public ServiceResult updateByPrimaryKeySelective(String token, RetailReturnBill[] objects) {
        ServiceResult result = new ServiceResult();
        if (objects == null || objects.length == 0) {
            result.addErrorObject("", "no input data");
            return result;
        }

        List<RetailReturnBill> dataList = Arrays.asList(objects);
        List<List<RetailReturnBill>> partition = Lists.partition(dataList, 200);
        for (List<RetailReturnBill> retailReturnBills : partition) {
        	retailReturnBillMapper.updateByPrimaryKeySelective(retailReturnBills);
        }

        return result;
    }

    @Override
    @Transactional
    public ServiceResult remove(String token, Object[] pkIds) {

        ServiceResult result = new ServiceResult();

        if (pkIds != null && pkIds.length > 0) {
            List<Object> returnIds = Arrays.asList(pkIds);
            Map<String, Object> relateOrderBillsMap = new HashMap<>();
            //根据id找到关联单据编号
            List<RetailReturnBill> relateOrderBills = retailReturnBillMapper.selectByPrimaryKey(returnIds, "relate_order_bill_no");
            if(relateOrderBills != null && relateOrderBills.size() > 0){
                relateOrderBillsMap.put("relateOrderBillNo", relateOrderBills.get(0).getRelateOrderBillNo());
            }
            goodsDetailService.removeByRetailReturnId(token, pkIds);
            chasingGoodsDetailService.removeByRetailReturnId(token, pkIds);
            retailReturnBillMapper.deleteByPrimaryKey(returnIds);
            if(relateOrderBillsMap != null && relateOrderBillsMap.size() > 0){
                //根据关联单号查询，如果无数据，用户取消退单
                long removeResult = retailReturnBillMapper.getListCount(relateOrderBillsMap);
                if(removeResult == 0L){
                    List<RetailOrderBill> retailOrderBills = new ArrayList<>();
                    RetailOrderBill retailOrderBill = new RetailOrderBill();
                    retailOrderBill.setHasReturn(0);
                    retailOrderBill.setBillNo(relateOrderBills.get(0).getRelateOrderBillNo());
                    retailOrderBills.add(retailOrderBill);
                    retailOrderBillService.batchUpdateHasReturnByBillNo(retailOrderBills);
                }
            }
        }

        return result;
    }

    @Override
    public RetailReturnBill[] findById(String token, Object[] pkIds, String[] selectFields) {
        try {
            if (pkIds == null || pkIds.length == 0) {
                return null;
            }
            String fields = null;
            if (selectFields != null) {
                StringBuilder sb = new StringBuilder("id");
                for (String field : selectFields) {
                    sb.append(",").append(field);
                }
                fields = sb.toString();
            }

            List<RetailReturnBill> bills = retailReturnBillMapper.selectByPrimaryKey(Arrays.asList(pkIds),
                    fields);

            if (bills == null || bills.size() == 0) {
                return new RetailReturnBill[0];
            }
            //根据settleMethodId找到 结算方式
            List<Long> ids = new ArrayList<Long>();
            bills.forEach(i -> ids.add(i.getSettleMethodId()));
            if(ids != null && !ids.isEmpty()){
                SettleMethod[] settleMethods = settleMethodService.findObjectById(token, ids.toArray(new Long[bills.size()]));
                if(settleMethods !=null && settleMethods.length > 0){
                    for (int i = 0;i < bills.size();i ++){
                        if(bills.get(i).getSettleMethodId().equals(settleMethods[i].getSettleMethodId()) ){
                            bills.get(i).setSettleMethod(settleMethods[i]);
                        }
                    }
                }
            }
            queryBaseFieldDatas(token, bills);

            return bills.toArray(new RetailReturnBill[bills.size()]);
        } catch (Exception e) {
            ServiceResult result = new ServiceResult();
            result.addErrorObjectNotThrow(pkIds, "", e.getMessage());
            throw new ServiceResultErrorException(result);
        }
    }

    @Override
    public RetailReturnBill[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {

        Map<String, Object> args = getQueryPageSelectorConditionMap(token,selector);

        if (pageSize > 0 && pageIndex >= 0) {
            int stratRow = pageIndex * pageSize;

            args.put("stratRow", stratRow);
            args.put("endRow", pageSize);
        }

        List<RetailReturnBill> datas = new ArrayList<>();
        datas = retailReturnBillMapper.queryPage(args);
        if (datas == null) {
            return new RetailReturnBill[0];
        }

        queryBaseFieldDatas(token, datas);

        return datas.toArray(new RetailReturnBill[datas.size()]);
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        Map<String, Object> args = getQueryPageSelectorConditionMap(token,selector);

        return retailReturnBillMapper.getListCount(args);
    }

    private Map<String, Object> getQueryPageSelectorConditionMap(String token,E3Selector selector){
        Map<String, Object> args = new HashMap<>();
        for (E3FilterField field : selector.getFilterFields()) {
            if (field instanceof E3FilterGroup) {
                for (E3FilterField grupField : ((E3FilterGroup) field).getFilterFields()) {
                    if (grupField.getFieldName().toLowerCase().indexOf("date") > 0) {
                        if (grupField.getConditionOperator().equals(">=")) {
                            args.put(grupField.getFieldName() + "_start", grupField.getValue());
                        } else {
                            args.put(grupField.getFieldName() + "_end", grupField.getValue());
                        }
                    } else {
                        if("billno".equals(grupField.getFieldName().toLowerCase()) || "sourcebillno".equals(grupField.getFieldName().toLowerCase())){
                            args.put(grupField.getFieldName(), String.valueOf(grupField.getOriginValue()).toUpperCase());
                        } else {
                            args.put(grupField.getFieldName(), grupField.getOriginValue());
                        }

                    }
                }
            } else{
                if (field.getFieldName().toLowerCase().indexOf("date") > 0) {
                    if (field.getConditionOperator().equals(">=")) {
                        args.put(field.getFieldName() + "_start", field.getValue());
                    } else {
                        args.put(field.getFieldName() + "_end", field.getValue());
                    }
                } else if (field.getFieldName().toLowerCase().equals("status")) {// 状态全部改为in for
                    if (field.getValue() instanceof Object[]) {
                        args.put(field.getFieldName(), field.getValue());
                    } else {
                        args.put(field.getFieldName(), new Object[]{field.getValue()});
                    }
                } else {
                    args.put(field.getFieldName(), field.getOriginValue());
                }
            }
        }

        Session session = LoginUtil.checkUserLogin(token);
        if (!isSuperAdmin(session.userRole)) {
            // 处理权限
            selector = new E3Selector();
            selector.addFilterField(new E3FilterField("code", "RetailReturnBill"));
            AuthObject[] authObjects = authObjectService.queryObject(token, selector);
            if (authObjects != null && authObjects.length >= 0) {
                List<Condition> authConditions = dataAuthService.getFilterCondition(session, "",
                        authObjects[0].getAuthObjectId(), "view");
                if (authConditions.size() > 0) {
                    addAuthCondition(args, authConditions);
                }
            }
        }
        return args;
    }

    private boolean isSuperAdmin(String userRole) {
        return UserInfo.SUPER_ADMIN.equals(userRole);
    }

    @SuppressWarnings("unchecked")
    private void addAuthCondition(Map<String, Object> args, List<Condition> authConditions) {

        for (Condition condition : authConditions) {
            List<Condition> childConditions = condition.getChildConditions();
            if (childConditions == null || childConditions.size() == 0) {
                String fieldName = condition.getExpression();

                if (fieldName.indexOf(".") != -1) {
                    fieldName = fieldName.split("\\.")[1];
                }

                String conditionValue = condition.getValue();
                String operator = condition.getOperation();
                
                List<Object> authValues = new ArrayList<>();
                if (Condition.IN.equals(operator)) {
                    String[] splitData = conditionValue.split(Condition.IN_SPLIT);
                    for (String data : splitData) {
                    	authValues.add(data);
                    }
                } else {
                	authValues.add(conditionValue);
                }

                Object value = args.get(fieldName);
                List<Object> values = null;
				if (value == null) {
					values = authValues;
				} else {
					List<Object> existValues = new ArrayList<>();
					if(value instanceof List) {
						for(Object v : (List<Object>)value) {
							existValues.add(v.toString());
						}
					} else if (value.getClass().isArray()) {
						for (Object v : (Object[]) value) {
							existValues.add(v.toString());
						}
					} else {
						existValues.add(value.toString());
					}
					// 取交集
					values = LinqUtil.intersect(authValues, existValues);
					if(values.size()==0) {
						//如果没有交集，则不可能查出数据，将当前条件字段给出一个不存在的值
						values.add(0);
					}
				}
                
                args.put(fieldName, values);
            } else {
                addAuthCondition(args, childConditions);
            }
        }
    }

    public String ConditionIn(E3FilterField field) {
        StringBuilder sb = new StringBuilder();
        if (field.getValue() instanceof String[]) {
            String[] status = (String[]) field.getValue();
            for (String s : status) {
                sb.append(s).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return null;
    }

    /**
     * 查询销售退单中基础档案字段的数据
     *
     * @param token
     * @param bills
     */
    private void queryBaseFieldDatas(String token, List<RetailReturnBill> bills) {

        // 结算方式id
        List<Long> settleMethodIds = new ArrayList<>();
        // 组织id
        List<Long> channelIds = new ArrayList<>();
        // 币别id
        List<Long> currencyTypeIds = new ArrayList<>();
        // 仓库id
        List<Long> warehouseIds = new ArrayList<>();
        // 商店id
        List<Long> shopIds = new ArrayList<>();
        // 库位id
        List<Long> whareatypeIds = new ArrayList<>();
        // 货主id
        List<Long> ownerIds = new ArrayList<>();

        for (RetailReturnBill bill : bills) {
            if (bill.getChannelId() != null) {
                channelIds.add(bill.getChannelId());
            }
            if (bill.getCurrencyTypeId() != null) {
                currencyTypeIds.add(bill.getCurrencyTypeId());
            }
            if (bill.getBaseCurrencyId() != null) {
                currencyTypeIds.add(bill.getBaseCurrencyId());
            }
            if (bill.getChasingWarehouseId() != null) {
                warehouseIds.add(bill.getChasingWarehouseId());
            }
            if (bill.getReturnWarehouseId() != null) {
                warehouseIds.add(bill.getReturnWarehouseId());
            }
            if (bill.getChasingWhAreaTypeId() != null) {
                whareatypeIds.add(bill.getChasingWhAreaTypeId());
            }
            if (bill.getReturnWhAreaTypeId() != null) {
                whareatypeIds.add(bill.getReturnWhAreaTypeId());
            }
            if (bill.getShopId() != null) {
                shopIds.add(bill.getShopId());
            }
            if (bill.getSettleMethodId() != null) {
                settleMethodIds.add(bill.getSettleMethodId());
            }
            if (bill.getOwnerId() != null) {
                ownerIds.add(bill.getOwnerId());
            }
        }

        E3Selector selector = new E3Selector();
        E3FilterField field = new E3FilterField("", "");
        selector.addFilterField(field);
        selector.addSelectFields("code");
        selector.addSelectFields("name");
        // 结算方式
        if (settleMethodIds.size() > 0) {
            field.setFieldName("settleMethodId");
            field.setValue(settleMethodIds);
            SettleMethod[] settleMethods = settleMethodService.queryObject(token, selector);
            if (settleMethodIds != null && settleMethodIds.size() > 0) {
                Map<Long, SettleMethod> mapDatas = new HashMap<>();
                for (SettleMethod settleMethod : settleMethods) {
                    mapDatas.put(settleMethod.getSettleMethodId(), settleMethod);
                }
                bills.forEach(c -> {
                    SettleMethod settleMethod = mapDatas.get(c.getSettleMethodId());
                    c.setSettleMethod(settleMethod);

                });
            }
        }

        // 组织
        if (channelIds.size() > 0) {
            field.setFieldName("channelId");
            field.setValue(channelIds);
            Channel[] channels = channelService.queryPageChannel(token, selector, -1, -1);
            if (channels != null && channels.length > 0) {
                Map<Long, Channel> mapDatas = new HashMap<>();
                for (Channel channel : channels) {
                    mapDatas.put(channel.getChannelId(), channel);
                }
                bills.forEach(c -> {
                    Channel channel = mapDatas.get(c.getChannelId());
                    c.setChannel(channel);
                });
            }
        }
        // 币别
        if (currencyTypeIds.size() > 0) {
            E3Selector selector1 = new E3Selector();
            selector1.addFilterField(new E3FilterField("currencyId", currencyTypeIds));
            selector1.addSelectFields("currencyCode");
            selector1.addSelectFields("currencyName");
            CurrencyType[] currencyTypes = currencyTypeService.queryPageCurrencyType(token, selector1, -1, -1);
            if (currencyTypes != null && currencyTypes.length > 0) {
                Map<Long, CurrencyType> mapDatas = new HashMap<>();
                for (CurrencyType currencyType : currencyTypes) {
                    mapDatas.put(currencyType.getCurrencyId(), currencyType);
                }
                bills.forEach(c -> {
                    CurrencyType currencyType = mapDatas.get(c.getCurrencyTypeId());
                    c.setCurrencyType(currencyType);

                    c.setBaseCurrency(mapDatas.get(c.getBaseCurrencyId()));
                });
            }
        }
        // 仓库
        if (warehouseIds.size() > 0) {
            E3Selector selector2 = new E3Selector();
            E3FilterField field2 = new E3FilterField("", "");
            selector.addFilterField(field);
            selector2.addSelectFields("code");
            selector2.addSelectFields("name");
            selector2.addSelectFields("channelId");

            field2.setFieldName("warehouseId");
            field2.setValue(warehouseIds);
            WareHouse[] warehouses = wareHouseService.queryObject(token, selector2);
            if (warehouses != null && warehouses.length > 0) {
                Map<Long, WareHouse> mapDatas = new HashMap<>();
                for (WareHouse warehouse : warehouses) {
                    mapDatas.put(warehouse.getWareHouseId(), warehouse);
                }
                bills.forEach(c -> {
                    c.setChasingWareHouse((AdvancedWareHouse) (mapDatas.get(c.getChasingWarehouseId())));
                });
                bills.forEach(c -> {
                    c.setReturnWarehouse((AdvancedWareHouse) (mapDatas.get(c.getReturnWarehouseId())));
                });
            }
        }
        // 商店
        if (shopIds.size() > 0) {
            E3Selector selector1 = new E3Selector();
            selector1.addFilterField(new E3FilterField("shopId", shopIds));
            selector1.addSelectFields("code");
            selector1.addSelectFields("name");
            selector1.addSelectFields("channelId");
            Shop[] shops = shopService.queryPageShop(token, selector1, -1, -1);
            if (shops != null && shops.length > 0) {
                Map<Long, Shop> mapDatas = new HashMap<>();
                for (Shop shop : shops) {
                    mapDatas.put(shop.getShopId(), shop);
                }
                bills.forEach(c -> {
                    c.setShop(mapDatas.get(c.getShopId()));
                });
            }
        }

        // 库位
        if (whareatypeIds.size() > 0) {
            field.setFieldName("whareatypeId");
            field.setValue(whareatypeIds);
            WhareaType[] whareaTypes = whareaTypeService.queryWhareaType(token, selector);
            if (whareaTypes != null && whareaTypes.length > 0) {
                Map<Long, WhareaType> mapDatas = new HashMap<>();
                for (WhareaType whareaType : whareaTypes) {
                    mapDatas.put(whareaType.getWhareaTypeId(), whareaType);
                }
                bills.forEach(c -> {
                    c.setChasingWhareaType(mapDatas.get(c.getChasingWhAreaTypeId()));
                });
                bills.forEach(c -> {
                    c.setReturnWhAreaType(mapDatas.get(c.getReturnWhAreaTypeId()));
                });
            }
        }

        // 货主
        if (ownerIds.size() > 0) {
            field.setFieldName("id");
            field.setValue(ownerIds);
            Owner[] owners = ownerService.queryObject(token, selector);
            if (owners != null && owners.length > 0) {
                Map<Long, Owner> mapDatas = new HashMap<>();
                for (Owner owner : owners) {
                    mapDatas.put(owner.getId(), owner);
                }
                bills.forEach(c -> {
                    c.setOwner(mapDatas.get(c.getOwnerId()));
                });
            }
        }
    }

    private void queryBaseDetailFieldDatas(String token, List<RetailReturnBill> bills) {

        // 货主id
        List<Long> ownerIds = new ArrayList<>();
        // skuid
        List<Long> singleproductIds = new ArrayList<>();
        // 商品id
        List<Long> goodsIds = new ArrayList<>();
        
        for(RetailReturnBill bill:bills) {
        	for (RetailReturnGoodsDetail detail : bill.getDetails()) {
        		if (detail.getOwnerId() != null) {
        			ownerIds.add(detail.getOwnerId());
        		}
        		if (detail.getSingleProductId() != null) {
        			singleproductIds.add(detail.getSingleProductId());
        		}
        		if (detail.getGoodsId() != null) {
        			goodsIds.add(detail.getGoodsId());
        		}
        	}
        }

        E3Selector selector = new E3Selector();
        E3FilterField field = new E3FilterField("", "");
        selector.addFilterField(field);
        selector.addSelectFields("code");
        selector.addSelectFields("name");

        // 货主
        Map<Long, Owner> ownerMapDatas = new HashMap<>();
        if (ownerIds.size() > 0) {
            field.setFieldName("id");
            field.setValue(ownerIds);
            Owner[] owners = ownerService.queryObject(token, selector);
            if (owners != null && owners.length > 0) {
                for (Owner owner : owners) {
                	ownerMapDatas.put(owner.getId(), owner);
                }
            }
        }

        // sku
        Map<Long, SimpleSingleProduct> skuMapDatas = new HashMap<>();
        if (singleproductIds.size() > 0) {
            E3Selector skuSelector = new E3Selector();
            skuSelector.addFilterField(new E3FilterField("singleProductId", singleproductIds));
            skuSelector.addSelectFields("singleProductId");
            skuSelector.addSelectFields("code");
            skuSelector.addSelectFields("name");
            skuSelector.addSelectFields("spec1");
            skuSelector.addSelectFields("attspec1.code");
            skuSelector.addSelectFields("attspec1.name");
            skuSelector.addSelectFields("spec2");
            skuSelector.addSelectFields("attspec2.code");
            skuSelector.addSelectFields("attspec2.name");
            skuSelector.addSelectFields("spec3");
            skuSelector.addSelectFields("attspec3.code");
            skuSelector.addSelectFields("attspec3.name");
            skuSelector.addSelectFields("e3Goods.code");
            skuSelector.addSelectFields("e3Goods.baseUnit.code");
            SimpleSingleProduct[] skus = skuService.querySimpleSingleProduct(token, skuSelector);
            if (skus != null && skus.length > 0) {
                for (SimpleSingleProduct sku : skus) {
                	skuMapDatas.put(sku.getSingleProductId(), sku);
                }
            }
        }

        // 商品
        Map<Long, SimpleGoods> goodsMapDatas = new HashMap<>();
        if (goodsIds.size() > 0) {
            selector.addSelectFields("goodsId");
            selector.addSelectFields("brandId");
            selector.addSelectFields("brand.code");
            selector.addSelectFields("brand.name");
            field.setFieldName("goodsId");
            field.setValue(goodsIds);
            SimpleGoods[] goods = goodsService.queryAdvancedSimpleGoods(token, selector);
            if (goods != null && goods.length > 0) {
                for (SimpleGoods g : goods) {
                	goodsMapDatas.put(g.getGoodsId(), g);
                }
                
            }
        }
        
        for(RetailReturnBill bill:bills) {
        	List<RetailReturnGoodsDetail> datas = bill.getDetails();
        	datas.forEach(c -> {
        		c.setOwner(ownerMapDatas.get(c.getOwnerId()));
        		c.setSingleProduct((AdvancedSimpleSingleProduct) skuMapDatas.get(c.getSingleProductId()));
                c.setGoods(goodsMapDatas.get(c.getGoodsId()));
            });
        }
    }

    private AreaGoodsStockParas[] createGoodsParas(String token, RetailReturnBill bill,
                                                   List<RetailReturnGoodsDetail> details) {
        List<AreaGoodsStockParas> paras = new ArrayList<>();

        AdvancedWareHouse wareHouse = bill.getReturnWarehouse();

        for (RetailReturnGoodsDetail detail : details) {
            AreaGoodsStockParas para = new AreaGoodsStockParas();
            para.setBillId(bill.getId().toString());
            para.setBillNo(bill.getBillno());
            para.setBillDate(bill.getBillDate());
            para.setGoodsId(detail.getGoodsId());
            para.setGoodsCode(detail.getGoods().getCode());
            para.setSingleProductId(detail.getSingleProductId());
            para.setSingleProductCode(detail.getSingleProduct().getCode());
            para.setWareHouseId(bill.getReturnWarehouseId());
            para.setWhareaTypeId(bill.getReturnWhAreaTypeId());
            para.setQuantity(detail.getQty());
            para.setOwnerId(bill.getOwnerId());

            if (wareHouse == null) {
                E3Selector selector = new E3Selector();
                selector.addFilterField(new E3FilterField("wareHouseId", bill.getReturnWarehouseId()));
                WareHouse[] wareHouses = wareHouseService.queryObject(token, selector);
                wareHouse = (AdvancedWareHouse) wareHouses[0];
            }
            String wareHouseType = wareHouse.getType();
            para.setWareHouseType(wareHouseType);
            if ("1".equals(wareHouseType)) {
                para.setAreaId(wareHouse.getProvinceId().toString());
            } else {
                para.setAreaId(wareHouse.getCode());
            }
            para.setShopCode(bill.getShop().getCode());

            paras.add(para);
        }

        return paras.toArray(new AreaGoodsStockParas[paras.size()]);
    }

    private void saveDetailsWhenCreate(String token, RetailReturnBill[] dataArray) {
        List<RetailReturnGoodsDetail> goodsList = new ArrayList<>();
        List<RetailReturnChasingGoodsDetail> chasingList = new ArrayList<>();
        for (RetailReturnBill bill : dataArray) {
            Long returnBillId = idService.nextId();
            bill.setId(returnBillId);
            // 商品明细
            List<RetailReturnGoodsDetail> details = bill.getDetails();
            if (details != null && details.size() > 0) {
                for (RetailReturnGoodsDetail goodsDetail : details) {
                    goodsDetail.setReturnBillId(returnBillId);
                }
                goodsList.addAll(details);
            }
            // 追单明细
            List<RetailReturnChasingGoodsDetail> chasingDetails = bill.getChasingDetails();
            if (chasingDetails != null && chasingDetails.size() > 0) {
                for (RetailReturnChasingGoodsDetail chasingGoodsDetail : chasingDetails) {
                    chasingGoodsDetail.setReturnBillId(returnBillId);
                }
                chasingList.addAll(chasingDetails);
            }
        }

        if (goodsList.size() > 0) {
            goodsDetailService.create(token, goodsList.toArray(new RetailReturnGoodsDetail[goodsList.size()]));
        }

        if (chasingList.size() > 0) {
            chasingGoodsDetailService.create(token,
                    chasingList.toArray(new RetailReturnChasingGoodsDetail[chasingList.size()]));
        }
    }

    private void setBeanValueWhenModify(String token, RetailReturnBill[] dataArray) {
        List<RetailReturnGoodsDetail> addGoodsDetails = new ArrayList<>();
        List<RetailReturnGoodsDetail> modifyGoodsDetails = new ArrayList<>();

        List<RetailReturnChasingGoodsDetail> addChasingGoodsDetails = new ArrayList<>();
        List<RetailReturnChasingGoodsDetail> modifyChasingGoodsDetails = new ArrayList<>();

        for (RetailReturnBill bill : dataArray) {
            Long returnId = bill.getId();
            // 商品明细
            List<RetailReturnGoodsDetail> details = bill.getDetails();
            if (details != null && details.size() > 0) {
                for (RetailReturnGoodsDetail goodsDetail : details) {
                    Long id = goodsDetail.getId();
                    if (id == null) {
                        goodsDetail.setReturnBillId(returnId);
                        addGoodsDetails.add(goodsDetail);
                    } else {
                        modifyGoodsDetails.add(goodsDetail);
                    }
                }
            }
            // 追单明细
            List<RetailReturnChasingGoodsDetail> chasingDetails = bill.getChasingDetails();
            if (chasingDetails != null && chasingDetails.size() > 0) {
                for (RetailReturnChasingGoodsDetail chasingGoodsDetail : chasingDetails) {
                    Long id = chasingGoodsDetail.getId();
                    if (id == null) {
                        chasingGoodsDetail.setReturnBillId(returnId);
                        addChasingGoodsDetails.add(chasingGoodsDetail);
                    } else {
                        modifyChasingGoodsDetails.add(chasingGoodsDetail);
                    }
                }
            }
        }
        if (addGoodsDetails.size() > 0) {
            goodsDetailService.create(token,
                    addGoodsDetails.toArray(new RetailReturnGoodsDetail[addGoodsDetails.size()]));
        }

        if (modifyGoodsDetails.size() > 0) {
            goodsDetailService.modify(token,
                    modifyGoodsDetails.toArray(new RetailReturnGoodsDetail[modifyGoodsDetails.size()]));
        }

        if (addChasingGoodsDetails.size() > 0) {
            chasingGoodsDetailService.create(token,
                    addChasingGoodsDetails.toArray(new RetailReturnChasingGoodsDetail[addChasingGoodsDetails.size()]));
        }

        if (modifyChasingGoodsDetails.size() > 0) {
            chasingGoodsDetailService.modify(token, modifyChasingGoodsDetails
                    .toArray(new RetailReturnChasingGoodsDetail[modifyChasingGoodsDetails.size()]));
        }
    }

    @Override
    public RetailReturnBill[] queryObjectBySql(String token, String sql) {
        List<RetailReturnBill> bills = retailReturnBillMapper.queryObjectBySql(sql);
        if (bills != null && !bills.isEmpty()) {
            queryBaseFieldDatas(token, bills);
            queryBaseDetailFieldDatas(token, bills);
            return bills.toArray(new RetailReturnBill[bills.size()]);
        }
        return null;
    }

    @Override
    @Transactional
	public ServiceResult savePasseBack(String token, RetailReturnBill[] bills, RetailReturnGoodsDetail[] details,
			List<Long> ids,Date wmsDate) {
		ServiceResult serviceResult = new ServiceResult();
		
		if (ids.isEmpty()) {
			serviceResult.addErrorObject(null, "no datas");
			throw new ServiceResultErrorException(serviceResult);
		}
		
		// 更新出库单号，货主 入库日期 快递信息
		this.updateByPrimaryKeySelective(token, bills);
		if (details.length > 0)
			goodsDetailService.modify(token, details);

		serviceResult = this.complete(token, ids,wmsDate);

		return serviceResult;
	}

    @Override
    public int updateBatchByBillId(Map map) {
        return retailReturnBillMapper.updateBatchByBillId(map);
    }


    private String generateBillNo(String token, RetailReturnBill bill) {
        return codeRuleService.generateCode(token, RetailReturnBill.class.getSimpleName(), bill);
    }

    private Map<Integer, List<Integer>> getPreStatus() {
        Map<Integer, List<Integer>> result = new HashMap<>();

        // 提交状态的前置状态
        result.put(OrderStatus.SUBMIT, Arrays.asList(OrderStatus.INITIAL));

        // 完成状态的前置状态
        result.put(OrderStatus.COMPLETE, Arrays.asList(OrderStatus.SUBMIT));

        // 初始状态的前置状态
        result.put(OrderStatus.INITIAL, Arrays.asList(OrderStatus.SUBMIT));

        // 终止状态的前置状态
        result.put(OrderStatus.TERM, Arrays.asList(OrderStatus.INITIAL, OrderStatus.SUBMIT));

        return result;
    }

    @Override
    public RetailReturnBill[] selectBySap(String token, String selectfields) {
        List<RetailReturnBill>  bills=retailReturnBillMapper.selectBySap(selectfields);
        queryBaseFieldDatas(token, bills);
        queryBaseDetailFieldDatas(token,bills);
        return bills.toArray(new RetailReturnBill[0]);
    }

    @Override
    public RetailReturnBill[] selectIdBySap(String token, String selectfields) {
        List<RetailReturnBill>  bills=retailReturnBillMapper.selectBySap(selectfields);
        return bills.toArray(new RetailReturnBill[0]);
    }

    @Override
    public int updateIsConfirm(Map map) {
        return retailReturnBillMapper.updateIsConfirm(map);
    }
    
    @Override
    @Transactional
    public void updateByBillNo(String billno, String receiverName, String receiverMobile, String receiverTel, String key){
    	retailReturnBillMapper.updateByBillNo(billno, receiverName, receiverMobile, receiverTel, key);
    }
}
