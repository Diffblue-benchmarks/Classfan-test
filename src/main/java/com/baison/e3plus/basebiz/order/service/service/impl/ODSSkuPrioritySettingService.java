package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerAdvancedGoodsService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerBrandService;
import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleGoods;
import com.baison.e3plus.basebiz.goods.api.business.advanced.model.AdvancedSimpleSingleProduct;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleGoods;
import com.baison.e3plus.basebiz.goods.api.model.product.SimpleSingleProduct;
import com.baison.e3plus.basebiz.order.api.model.ODSSkuPrioritySetting;
import com.baison.e3plus.basebiz.order.api.service.IODSSkuPrioritySettingService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.ODSSkuPrioritySettingMapper;
import com.baison.e3plus.basebiz.order.service.dao.model.example.ODSSkuPrioritySettingExample;
import com.baison.e3plus.biz.support.api.goods.model.brand.Brand;
import com.baison.e3plus.biz.support.api.id.model.BatchIdVo;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.github.pagehelper.PageHelper;

@Service
public class ODSSkuPrioritySettingService implements IODSSkuPrioritySettingService {

    @Autowired
    private ODSSkuPrioritySettingMapper odsSkuPrioritySettingMapper;
    @Autowired
    private ConsumerIdService idService;
    @Autowired
    private ConsumerSingleProductService singleProductService;
    @Autowired
    private ConsumerAdvancedGoodsService advancedGoodsService;
    @Autowired
    private ConsumerBrandService brandService;

    @Override
    @Transactional
    public ServiceResult createObject(String token, ODSSkuPrioritySetting[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return result;
        }

        BatchIdVo batchIdVo = idService.batchId(beans.length);
        long startId = batchIdVo.getStartId();
        for (ODSSkuPrioritySetting bean : beans) {
            bean.setId(startId);
            startId += batchIdVo.getStep();
        }

        odsSkuPrioritySettingMapper.insertBatch(beans);

        result.addSuccessObject(beans);
        return result;
    }

    @Override
    @Transactional
    public ServiceResult modifyObject(String token, ODSSkuPrioritySetting[] beans) {
        ServiceResult result = new ServiceResult();
        if (beans == null || beans.length == 0) {
            return null;
        }

        ODSSkuPrioritySetting[] sortedBeans = Arrays.stream(beans).sorted(Comparator.comparing(t -> t.getId())).toArray(ODSSkuPrioritySetting[]::new);
        odsSkuPrioritySettingMapper.updateByPrimaryKeySelectiveBatch(sortedBeans);

        result.addSuccessObject(sortedBeans);
        return result;
    }

    @Override
    @Transactional
    public ServiceResult removeObject(String token, Object[] ids) {
        ServiceResult result = new ServiceResult();
        if (ids == null || ids.length == 0) {
            result.addErrorObject("", "", "no input data");
            return result;
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        odsSkuPrioritySettingMapper.deleteByPrimaryKeyBatch(idArr);

        result.addSuccessObject(ids);
        return result;
    }

    @Override
    public ODSSkuPrioritySetting[] findObjectById(String token, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        Long[] idArr = Arrays.stream(ids).map(t -> Long.valueOf(String.valueOf(t))).toArray(Long[]::new);
        List<ODSSkuPrioritySetting> datas = odsSkuPrioritySettingMapper.selectByPrimaryKeyBatch(idArr);

        if (datas == null || datas.size() == 0) {
            return null;
        }

        fillDatas(token, datas);

        return datas.toArray(new ODSSkuPrioritySetting[datas.size()]);
    }

    private void fillDatas(String token, List<ODSSkuPrioritySetting> datas) {
        Set<Long> productIdSet = datas.stream().map(t -> Long.valueOf(String.valueOf(t.getSingleProductId()))).collect(Collectors.toSet());
        if (productIdSet != null && productIdSet.size() > 0) {
            E3Selector e3Selector = new E3Selector();
            E3FilterField e3FilterField = new E3FilterField("singleProductId", "in", productIdSet.toArray(new Long[productIdSet.size()]), E3FilterField.ANDOperator);
            e3Selector.addFilterField(e3FilterField);
            SimpleSingleProduct[] simpleSingleProducts = singleProductService.querySimpleSingleProduct(token, e3Selector);
            List<AdvancedSimpleSingleProduct> simpleSingleProductList = new ArrayList<>();
            for (SimpleSingleProduct simpleSingleProduct : simpleSingleProducts) {
                if (simpleSingleProduct instanceof AdvancedSimpleSingleProduct) {
                    AdvancedSimpleSingleProduct temp = (AdvancedSimpleSingleProduct) simpleSingleProduct;
                    simpleSingleProductList.add(temp);
                }
            }

            if (simpleSingleProductList != null && simpleSingleProductList.size() > 0) {
                Map<String, AdvancedSimpleSingleProduct> singleProductMap = simpleSingleProductList.stream().collect(Collectors.toMap(t -> String.valueOf(t.getSingleProductId()), t -> t, (t1, t2) -> t2));
                for (ODSSkuPrioritySetting data : datas) {
                    String key = String.valueOf(data.getSingleProductId());
                    AdvancedSimpleSingleProduct singleProduct = singleProductMap.get(key);
                    if (singleProduct != null) {
                        data.setSingleProduct(singleProduct);
                        data.setSingleProductCode(singleProduct.getCode());
                    }

                }
            }
        }

        Set<Long> goodsIdSer = datas.stream().map(t -> Long.valueOf(String.valueOf(t.getGoodsId()))).collect(Collectors.toSet());
        if (goodsIdSer != null && goodsIdSer.size() > 0) {
            E3Selector e3Selector = new E3Selector();
            E3FilterField e3FilterField = new E3FilterField("goodsId", "in", goodsIdSer.toArray(new Long[goodsIdSer.size()]), E3FilterField.ANDOperator);
            e3Selector.addFilterField(e3FilterField);
            SimpleGoods[] simpleGoods = advancedGoodsService.queryAdvancedSimpleGoods(token, e3Selector);
            List<AdvancedSimpleGoods> simpleGoodsList = new ArrayList<>();
            for (SimpleGoods simpleGood : simpleGoods) {
                if (simpleGood instanceof AdvancedSimpleGoods) {
                    AdvancedSimpleGoods temp = (AdvancedSimpleGoods) simpleGood;
                    simpleGoodsList.add(temp);
                }
            }

            if (simpleGoodsList != null && simpleGoodsList.size() > 0) {
                Map<String, AdvancedSimpleGoods> singleProductMap = simpleGoodsList.stream().collect(Collectors.toMap(t -> String.valueOf(t.getGoodsId()), t -> t, (v1, v2) -> v2));
                for (ODSSkuPrioritySetting data : datas) {
                    String key = String.valueOf(data.getGoodsId());
                    AdvancedSimpleGoods singleGoods = singleProductMap.get(key);
                    if (singleGoods != null) {
                        data.setGoods(singleGoods);
                        data.setGoodsCode(singleGoods.getCode());
                    }

                }
            }
        }


        Set<Long> brandIdSet = datas.stream().map(t -> Long.valueOf(String.valueOf(t.getBrandId()))).collect(Collectors.toSet());
        if (brandIdSet != null && brandIdSet.size() > 0) {
            E3Selector e3Selector = new E3Selector();
            E3FilterField e3FilterField = new E3FilterField("brandId", "in", brandIdSet.toArray(new Long[brandIdSet.size()]), E3FilterField.ANDOperator);
            e3Selector.addFilterField(e3FilterField);
            Brand[] brandArr = brandService.queryBrand(token, e3Selector);

            if (brandArr != null && brandArr.length > 0) {
                Map<String, Brand> beandMap = Arrays.asList(brandArr).stream().collect(Collectors.toMap(t -> String.valueOf(t.getBrandId()), t -> t, (v1, v2) -> v2));
                for (ODSSkuPrioritySetting data : datas) {
                    String key = String.valueOf(data.getBrandId());
                    Brand brand = beandMap.get(key);
                    if (brand != null) {
                        data.setBrand(brand);
                        data.setBrandCode(brand.getCode());
                    }

                }
            }
        }
    }

    @Override
    public List<ODSSkuPrioritySetting> queryBySingleProductId(String token, List<Integer> singleProductIds) {
    	ODSSkuPrioritySettingExample example = new ODSSkuPrioritySettingExample();
    	example.createCriteria().andSingleProductIdIn(singleProductIds);
    	return odsSkuPrioritySettingMapper.selectByExample(example);
    }

    @Override
    public long getListCount(String token, E3Selector selector) {
        ODSSkuPrioritySettingExample example = new ODSSkuPrioritySettingExample();
        E3FilterField fieldName = selector.getFilterFieldByFieldName("code");
        return odsSkuPrioritySettingMapper.countByExample(example);
    }

    @Override
    public ODSSkuPrioritySetting[] queryPageObject(String token, E3Selector selector, int pageSize, int pageIndex) {
        ODSSkuPrioritySettingExample example = new ODSSkuPrioritySettingExample();
        if (pageIndex >= 0 && pageSize >= 0) {
            PageHelper.startPage(pageIndex + 1, pageSize, false);
        }
        List<ODSSkuPrioritySetting> datas = odsSkuPrioritySettingMapper.selectByExample(example);

        if (datas == null || datas.size() == 0) {
            return null;
        }

        fillDatas(token, datas);

        return datas.toArray(new ODSSkuPrioritySetting[datas.size()]);
    }

}
