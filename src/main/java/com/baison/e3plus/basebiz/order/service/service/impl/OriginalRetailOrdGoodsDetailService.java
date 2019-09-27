package com.baison.e3plus.basebiz.order.service.service.impl;

import com.baison.e3plus.basebiz.goods.api.model.barcode.Barcode;
import com.baison.e3plus.basebiz.order.api.model.OriginalRetailOrdGoodsDetail;
import com.baison.e3plus.basebiz.order.api.service.IOriginalRetailOrdGoodsDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.OriginalRetailOrdGoodsDetailMapper;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerBarcodeService;
import com.baison.e3plus.basebiz.order.service.feignclient.goods.ConsumerSingleProductService;
import com.baison.e3plus.common.bscore.utils.UUIDUtil;
import com.baison.e3plus.common.cncore.common.Status;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;
import com.baison.e3plus.common.orm.metadata.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OriginalRetailOrdGoodsDetailService implements IOriginalRetailOrdGoodsDetailService {

    @Autowired
    private OriginalRetailOrdGoodsDetailMapper originalRetailOrdGoodsDetailMapper;

    @Autowired
    private ConsumerBarcodeService barcodeService;

    @Autowired
    private ConsumerSingleProductService singleProductService;

    /**
     * 创建明细
     * @param token
     * @param detailList
     * @return
     */
    @Override
    public ServiceResult create(String token, List<OriginalRetailOrdGoodsDetail> detailList) {
        ServiceResult result = new ServiceResult();
        if (detailList == null || detailList.size() == 0) {
            result.addErrorObject("", "", "no data input!");
            return result;
        }

        // OrdOriginalRetailOrderId不能为空
        for (OriginalRetailOrdGoodsDetail detail : detailList) {
            if (detail.getOriginalRetailOrderId() == null) {
                result.addErrorObject("", "", "order id is null!");
            }
        }

        if (result.hasError()) {
            return result;
        }

        // barcode为空则返回错误的交易号
        List<String> billNos = new ArrayList<>();
        detailList.stream().filter(t -> "".equals(t.getCode())).forEach(item -> {
            billNos.add(item.getTradeNo());
        });

        if(billNos != null && billNos.size() > 0){
            result.addErrorObject(billNos, "","该订单明细" + billNos.toString() + "条码不能为空");
            return result;
        }

        Set<String> barcodes = new HashSet<>();
        for (OriginalRetailOrdGoodsDetail detail : detailList) {
            barcodes.add(detail.getCode());
        }
        List<Barcode> barcodeList = new ArrayList<Barcode>();

        if (barcodes.size() > 0) {
            E3Selector selector = new E3Selector();
            selector.addFilterField(new E3FilterField("barcode", barcodes));
            selector.addFilterField(new E3FilterField("status", Status.ENABLE));
            selector.addSelectFields("barcode");
            Barcode[] datas = barcodeService.queryBarcode(token, selector);
            barcodeList = Arrays.asList(datas);
        }
        Map<String, Object> barcodeMap = new HashMap<String, Object>();

        if(barcodeList != null && barcodeList.size() > 0){
            for(String barcode :barcodes){
                for (int i = 0; i < barcodeList.size(); i++){
                    if(barcode.equals(barcodeList.get(i).getBarcode())){
                        barcodeMap.put(barcodeList.get(i).getBarcode(), barcode);
                    }
                }
            }
        }else {
            result.addErrorObject(barcodes, "","barcode不能匹配");
            return result;
        }
        if(barcodeMap.size() != barcodes.size()){
            result.addErrorObject(barcodes, "","barcode不能匹配");
            return result;
        }

        for (OriginalRetailOrdGoodsDetail detail : detailList) {
            detail.setId(UUIDUtil.generate());
        }

        originalRetailOrdGoodsDetailMapper.insertSelective(detailList);

        return result;
    }

    /**
     * 分页查询
     *
     * @param token
     * @param selector
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @Override
    public OriginalRetailOrdGoodsDetail[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {

        Map<String, Object> args = createQueryArgs(selector);

        if (pageSize > 0 && pageIndex >= 0) {
            int stratRow = pageIndex * pageSize;

            args.put("stratRow", stratRow);
            args.put("endRow", pageSize);
        }

        List<OriginalRetailOrdGoodsDetail> datas = null;
        datas = originalRetailOrdGoodsDetailMapper.queryPage(args);

        return datas.toArray(new OriginalRetailOrdGoodsDetail[datas.size()]);
    }

    /**
     * 查询总数
     *
     * @param token
     * @param selector
     * @return
     */
    @Override
    public long getListCount(String token, E3Selector selector) {
        Map<String, Object> args = createQueryArgs(selector);

        return originalRetailOrdGoodsDetailMapper.getListCount(args);
    }

    /**
     * 查询条件封装
     *
     * @param selector
     * @return
     */
    private Map<String, Object> createQueryArgs(E3Selector selector){
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
        return args;
    }


    @Override
    public OriginalRetailOrdGoodsDetail[] queryGoodsDetailByBillNo(String token, List<String> billNo) {

        if (billNo == null || billNo.size() == 0) {
            return null;
        }

        List<OriginalRetailOrdGoodsDetail> goodsDetails = originalRetailOrdGoodsDetailMapper.selectByBillNo(billNo);

        if (goodsDetails != null) {
            return goodsDetails.toArray(new OriginalRetailOrdGoodsDetail[goodsDetails.size()]);
        }
        return null;
    }

}
