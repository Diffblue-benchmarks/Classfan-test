package com.baison.e3plus.basebiz.order.service.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerAdvancedGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerBarcodeService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleSingleProduct;
import com.baison.e3plus.basebiz.goods.api.model.barcode.Barcode;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleGoods;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.basebiz.order.api.model.RetailReturnGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnGoodsDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailReturnGoodsDetailMapper;
import com.baison.e3plus.biz.support.api.publicrecord.model.owner.Owner;
import com.baison.e3plus.common.bscore.linq.LinqUtil;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

@Service
public class RetailReturnGoodsDetailService implements IRetailReturnGoodsDetailService {

	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen start
	 * @since 2018-12-07
	 * 
	 */
	@Autowired
	private ConsumerIdService idService;
	@Autowired
	private ConsumerAdvancedGoodsService goodsService;
	@Autowired
	private ConsumerSingleProductService skuService;
	@Autowired
	private ConsumerOwnerService ownerService;
	@Autowired
	private ConsumerBarcodeService barcodeService;
	@Autowired
	private RetailReturnGoodsDetailMapper retailReturnGoodsDetailMapper;

	@Override
	@Transactional
	public ServiceResult create(String token, RetailReturnGoodsDetail[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "no data input!");
		}

		if (result.hasError()) {
			return result;
		}

		// return id不能为空
		for (RetailReturnGoodsDetail detail : objects) {
			if (detail.getReturnBillId()==null) {
				result.addErrorObject("", "", "return id is null!");
			}
		}

		if (result.hasError()) {
			return result;
		}

		/**
		 * 如果只传入了条码，则需要根据条码找到商品id和单品id
		 */
		List<String> barcodes = new ArrayList<>();
		for (RetailReturnGoodsDetail detail : objects) {
			if (detail.getSingleProductId() == null || detail.getGoodsId() == null) {
				barcodes.add(detail.getBarcode());
			}
		}
		if (barcodes.size() > 0) {
			E3Selector selector = new E3Selector();
			selector.addFilterField(new E3FilterField("barcode", barcodes));
			selector.addSelectFields("barcode");
			selector.addSelectFields("singleProductId");
			selector.addSelectFields("goodsId");
			Barcode[] datas = barcodeService.queryBarcode(token, selector);
			Map<String, Barcode> dataMap = new HashMap<>();
			for (Barcode data : datas) {
				dataMap.put(data.getBarcode(), data);
			}

			selector = new E3Selector();
			selector.addFilterField(new E3FilterField("code", barcodes));
			selector.addSelectFields("code");
			selector.addSelectFields("singleProductId");
			selector.addSelectFields("goodsId");
			SimpleSingleProduct[] skus = skuService.querySimpleSingleProduct(token, selector);
			Map<String, SimpleSingleProduct> skuMap = new HashMap<>();
			for (SimpleSingleProduct data : skus) {
				skuMap.put(data.getCode(), data);
			}

			for (RetailReturnGoodsDetail detail : objects) {
				Long goodsId = null;
				Long skuId = null;
				Barcode barcode = dataMap.get(detail.getBarcode());
				if (barcode == null) {
					SimpleSingleProduct sku = skuMap.get(detail.getBarcode());
					if (sku == null) {
						result.addErrorObject("", detail.getBarcode(),
								"barcode or sku is not exist! barcode : " + detail.getBarcode());
						return result;
					}
					goodsId = sku.getGoodsId();
					skuId = sku.getSingleProductId();
				} else {
					goodsId = barcode.getGoodsId();
					skuId = barcode.getSingleProductId();
				}

				if (detail.getGoodsId() == null) {
					detail.setGoodsId(goodsId);
				}
				if (detail.getSingleProductId() == null) {
					detail.setSingleProductId(skuId);
				}
			}
		}

		// 取吊牌价，商品档案上price
		List<Long> goodsIds = LinqUtil.select(Arrays.asList(objects), s -> s.getGoodsId());
		E3Selector selector = new E3Selector();
		selector.addFilterField(new E3FilterField("goodsId", goodsIds));
		selector.addSelectFields("price");
		selector.addSelectFields("goodsId");
		SimpleGoods[] goods = goodsService.queryAdvancedSimpleGoods(token, selector);
		if (goods != null && goods.length > 0) {
			Map<Long, BigDecimal> mapDatas = new HashMap<>();
			for (SimpleGoods g : goods) {
				mapDatas.put(g.getGoodsId(), g.getPrice() == null ? new BigDecimal(0) : g.getPrice());
			}
			Arrays.asList(objects).forEach(c -> {
				c.setMarketPrice(mapDatas.get(c.getGoodsId()).doubleValue());
			});
		}
		
		
		for (RetailReturnGoodsDetail detail : objects) {
			detail.setId(idService.nextId());
		}

		retailReturnGoodsDetailMapper.insertSelective(Arrays.asList(objects));

		return result;
	}

	@Override
	@Transactional
	public ServiceResult modify(String token, RetailReturnGoodsDetail[] objects) {
		// TODO 主键一定要有，怎么判断字段是否要更新？如果要把字段设置为null怎么处理

		ServiceResult result = new ServiceResult();

		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				retailReturnGoodsDetailMapper.updateByPrimaryKey(objects[i]);
			}
		}

		return result;
	}

	@Override
	@Transactional
	public ServiceResult remove(String token, Object[] pkIds) {

		ServiceResult result = new ServiceResult();

		if (pkIds != null && pkIds.length > 0) {
			retailReturnGoodsDetailMapper.deleteByPrimaryKey(Arrays.asList(pkIds));
		}

		return result;
	}

	@Override
	public RetailReturnGoodsDetail[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {

		try {
			
			Map<String, Object> args = new HashMap<>();
			for (E3FilterField field : selector.getFilterFields()) {
				if (field instanceof E3FilterGroup) {
					for (E3FilterField grupField : ((E3FilterGroup) field).getFilterFields()) {
						if (args.containsKey(grupField.getFieldName())) {
							args.put(grupField.getFieldName() + grupField.getFieldName(), grupField.getOriginValue());
						} else {
							args.put(grupField.getFieldName(), grupField.getOriginValue());
						}
					}
				} else {
					args.put(field.getFieldName(), field.getValue());
				}
			}
			
			if (pageSize > 0 && pageIndex >= 0) {
				int stratRow = pageIndex * pageSize;
				
				args.put("stratRow", stratRow);
				args.put("endRow", pageSize);
			}
			List<RetailReturnGoodsDetail> datas = null;
			if(selector.getSelectFields() != null && selector.getSelectFields().size() > 0 && selector.getSelectFields().get(0).contains("sum")){
				datas = retailReturnGoodsDetailMapper.sum(args);				
			}else {
				datas = retailReturnGoodsDetailMapper.queryPage(args);
			}
			
			queryBaseFieldDatas(token, datas);
			
			return datas.toArray(new RetailReturnGoodsDetail[datas.size()]);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

	@Override
	public long getListCount(String token, E3Selector selector) {
		Map<String, Object> args = new HashMap<>();
		for (E3FilterField field : selector.getFilterFields()) {
			args.put(field.getFieldName(), field.getValue());
		}
		return retailReturnGoodsDetailMapper.getListCount(args);
	}

	private void queryBaseFieldDatas(String token, List<RetailReturnGoodsDetail> datas) {

		// 货主id
		List<Long> ownerIds = new ArrayList<>();
		// skuid
		List<Long> singleproductIds = new ArrayList<>();
		// 商品id
		List<Long> goodsIds = new ArrayList<>();

		for (RetailReturnGoodsDetail detail : datas) {
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

		E3Selector selector = new E3Selector();
		E3FilterField field = new E3FilterField("", "");
		selector.addFilterField(field);
		selector.addSelectFields("code");
		selector.addSelectFields("name");

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
				datas.forEach(c -> {
					c.setOwner(mapDatas.get(c.getOwnerId()));
				});
			}
		}

		// sku
		if (singleproductIds.size() > 0) {
			E3Selector skuSelector = new E3Selector();
			skuSelector.addFilterField(new E3FilterField("singleProductId", singleproductIds));
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
			skuSelector.addSelectFields("e3Goods.name");
			skuSelector.addSelectFields("e3Goods.code");
			skuSelector.addSelectFields("e3Goods.baseUnit.code");
			skuSelector.addSelectFields("e3Goods.baseUnit.name");
			SimpleSingleProduct[] skus = skuService.querySimpleSingleProduct(token, skuSelector);
			if (skus != null && skus.length > 0) {
				Map<Long, SimpleSingleProduct> mapDatas = new HashMap<>();
				for (SimpleSingleProduct sku : skus) {
					mapDatas.put(sku.getSingleProductId(), sku);
				}
				datas.forEach(c -> {
					c.setSingleProduct((AdvancedSimpleSingleProduct) mapDatas.get(c.getSingleProductId()));
				});
			}
		}

		// 商品
		if (goodsIds.size() > 0) {
			field.setFieldName("goodsId");
			field.setValue(goodsIds);
			SimpleGoods[] goods = goodsService.queryAdvancedSimpleGoods(token, selector);
			if (goods != null && goods.length > 0) {
				Map<Long, SimpleGoods> mapDatas = new HashMap<>();
				for (SimpleGoods g : goods) {
					mapDatas.put(g.getGoodsId(), g);
				}
				datas.forEach(c -> {
					c.setGoods(mapDatas.get(c.getGoodsId()));
				});
			}
		}
	}

	@Override
	@Transactional
	public ServiceResult removeByRetailReturnId(String token, Object[] returnBillIds) {
		ServiceResult result = new ServiceResult();

		if (returnBillIds == null || returnBillIds.length == 0) {
			result.addErrorObject("", "", "returnBillIds is null!");

			return result;
		}

		retailReturnGoodsDetailMapper.deleteByReturnId(Arrays.asList(returnBillIds));

		return result;
	}

	@Override
	public RetailReturnGoodsDetail[] findById(String token, Long[] pkIds, String[] selectFields) {
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

		List<RetailReturnGoodsDetail> details = new ArrayList<>();
		for (Long id : pkIds) {
			RetailReturnGoodsDetail detail = retailReturnGoodsDetailMapper.selectByPrimaryKey(id, fields);
			details.add(detail);
		}

		queryBaseFieldDatas(token, details);

		return details.toArray(new RetailReturnGoodsDetail[details.size()]);
	}
	
	
	@Override
	public int updateBarcode(String newBarcode,  String oldBarcode){
		 return retailReturnGoodsDetailMapper.updateBarcode(newBarcode, oldBarcode);
	}
	
}
