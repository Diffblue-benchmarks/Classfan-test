package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerAdvancedGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleSingleProduct;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleGoods;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.basebiz.order.api.model.RetailReturnChasingGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailReturnChasingGoodsDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailReturnChasingGoodsDetailMapper;

@Service
public class RetailReturnChasingGoodsDetailService implements IRetailReturnChasingGoodsDetailService {

	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen start
	 * @since 2018-12-07
	 */
	@Autowired
	private ConsumerIdService idService;
	@Autowired
	private ConsumerAdvancedGoodsService goodsService;

	@Autowired
	private ConsumerSingleProductService skuService;

	@Autowired
	private RetailReturnChasingGoodsDetailMapper retailReturnChasingGoodsDetailMapper;
	
	@Override
	@Transactional
	public ServiceResult create(String token, RetailReturnChasingGoodsDetail[] objects) {
		ServiceResult result = new ServiceResult();
		for (RetailReturnChasingGoodsDetail chasingGoodsDetail : objects) {
			chasingGoodsDetail.setId(idService.nextId());
		}
		retailReturnChasingGoodsDetailMapper.insertSelective(Arrays.asList(objects));
		result.addSuccessObject(objects);
		return result;
	}

	@Override
	@Transactional
	public ServiceResult modify(String token, RetailReturnChasingGoodsDetail[] objects) {
		ServiceResult result = new ServiceResult();

		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				retailReturnChasingGoodsDetailMapper.updateByPrimaryKey(objects[i]);
			}
		}

		return result;
	}

	@Override
	@Transactional
	public ServiceResult remove(String token, Object[] pkIds) {

		ServiceResult result = new ServiceResult();

		if (pkIds != null && pkIds.length > 0) {
			retailReturnChasingGoodsDetailMapper.deleteByPrimaryKey(Arrays.asList(pkIds));
		}

		return result;
	}

	@Override
	public RetailReturnChasingGoodsDetail[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {

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

		List<RetailReturnChasingGoodsDetail> datas = retailReturnChasingGoodsDetailMapper.queryPage(args);

		queryBaseFieldDatas(token, datas);

		return datas.toArray(new RetailReturnChasingGoodsDetail[datas.size()]);
	}

	private void queryBaseFieldDatas(String token, List<RetailReturnChasingGoodsDetail> datas) {

		// skuid
		List<Long> singleproductIds = new ArrayList<>();
		// 商品id
		List<Long> goodsIds = new ArrayList<>();

		for (RetailReturnChasingGoodsDetail detail : datas) {
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
	public long getListCount(String token, E3Selector selector) {
		Map<String, Object> args = new HashMap<>();
		for (E3FilterField field : selector.getFilterFields()) {
			args.put(field.getFieldName(), field.getValue());
		}
		return retailReturnChasingGoodsDetailMapper.getListCount(args);
	}

	@Override
	public RetailReturnChasingGoodsDetail[] findById(String token, Long[] pkIds, String[] selectFields) {
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

		List<RetailReturnChasingGoodsDetail> details = new ArrayList<>();
		for (Long id : pkIds) {
			RetailReturnChasingGoodsDetail detail = retailReturnChasingGoodsDetailMapper.selectByPrimaryKey(id, fields);
			details.add(detail);
		}

		queryBaseFieldDatas(token, details);

		return details.toArray(new RetailReturnChasingGoodsDetail[details.size()]);
	}

	@Override
	@Transactional
	public ServiceResult removeByRetailReturnId(String token, Object[] returnBillIds) {
		ServiceResult result = new ServiceResult();

		if (returnBillIds == null || returnBillIds.length == 0) {
			result.addErrorObject("", "", "returnBillIds is null!");

			return result;
		}

		retailReturnChasingGoodsDetailMapper.deleteByReturnId(Arrays.asList(returnBillIds));

		return result;
	}
	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen end
	 * @since 2018-12-07
	 */
}
