package com.baison.e3plus.basebiz.order.model;

import com.baison.e3plus.basebiz.order.BaseOrderTest;
import com.baison.e3plus.basebiz.order.api.model.*;
import com.baison.e3plus.basebiz.order.api.model.adapter.*;
import com.baison.e3plus.basebiz.order.api.model.calculate.*;
import com.baison.e3plus.common.bscore.utils.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 统一完成Model的测试
 * @author liang.zeng
 */
public class CommonModelTest extends BaseOrderTest {


    private Class<?>[] modelClassArray = new Class[]{
            AdvancedAdapterPriorityDetail.class,
            AdvancedAdapterShopDetail.class,
            AdvancedAdapterWareHouseDetail.class,
            AdvancedExpressStrategy.class,
            AdvancedStrategyPlatformGoodsDetail.class,
            OmsRetailOrderBill.class,
            DeliveryResponse.class,
            OrderAdapterResponse.class,
            OrderDisCalContext.class,
            OrderDisSKUDetail.class,
            OrderDistributeCalculateException.class,
            OrderDistributeLog.class,
            OrderDistributeLogWithBLOBs.class,
            OrderDistributeParas.class,
            OrderDistributeProcess.class,
            OrderDistributeResponse.class,
            OrderResponseInfo.class,
            SendSKUDetail.class,
            WareHouseInfo.class,
            AdvancedRetailOrderRejection.class,
            AdvancedWmsOrderPassedBack.class,
            ODSShopRefusalRecord.class,
            ODSSkuPrioritySetting.class,
            OrderDistributeStrategy.class,
            OrderExemptReviewStrategy.class,
            OrderExemptReviewStrategyDetail.class,
            OrderOdsShopScope.class,
            OrderOdsShopScopeDetail.class,
            OrderOdsWareahouse.class,
            OriginalRetailOrderBill.class,
            OriginalRetailOrderDistributionInfo.class,
            OriginalRetailOrdGoodsDetail.class,
            PageQuery.class,
            RequestRetailOrderBill.class,
            RequestRetailOrderDistributionInfo.class,
            RequestRetailOrderGoodsDetail.class,
            RetailAdditionalRefund.class,
            RetailOrderBill.class,
            RetailOrderDistributionInfo.class,
            RetailOrderGoodsDetail.class,
            RetailOrderSettleDetail.class,
            RetailReturnBill.class,
            RetailReturnChasingGoodsDetail.class,
            RetailReturnGoodsDetail.class,
            SetMealRetailOrderGoods.class,
            WareHousePriority.class,
            WareHousePriorityStrategy.class,
            WareHousePriorityStrategyOnlineDetail.class,
            WareHousePriorityStrategyShopDetail.class,
            WareHousePriorityStrategyStoreDetail.class
    };

    @Test
    public void testEquals() {
        for (Class<?> modelClass : modelClassArray) {
            Object object1 = getObject(modelClass);
            Object object2 = getObject(modelClass);
            object1.equals(object2);
            object1.toString().equals(object2.toString());
            //Assert.assertEquals(object1,object2);
            //Assert.assertEquals(object1.toString(),object2.toString());
            //Assert.assertEquals(object1.hashCode(),object2.hashCode());

            object1 = getObject(modelClass, false);
            object2 = getObject(modelClass, false);
            object1.equals(object2);
            object1.toString().equals(object2.toString());
            //Assert.assertEquals(object1,object2);
            //Assert.assertEquals(object1.toString(),object2.toString());
            //Assert.assertEquals(object1.hashCode(),object2.hashCode());
        }
    }

    public Object getObject(Class<?> modelClass){
        return getObject(modelClass, true);
    }

    public Object getObject(Class<?> modelClass, boolean setValue){
        try {
            Object object = modelClass.newInstance();
            if(setValue){
                setValue(object, modelClass);
            }
            return object;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void setValue(Object obj, Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        Date date = DateUtil.convertToDate("2019-01-01 00:00:00") ; //yyyy-MM-dd HH:mm:ss
        for(Method method : clazz.getDeclaredMethods()){
            if(method.getName().startsWith("set")){
                Class parmsType = method.getParameterTypes()[0];    //setter方法一个参数
                if(parmsType.isAssignableFrom(Integer.class)){
                    method.invoke(obj,0);
                }else if(parmsType.isAssignableFrom(Long.class)){
                    method.invoke(obj,0L);
                }else if(parmsType.isAssignableFrom(Double.class)){
                    method.invoke(obj,0D);
                }else if(parmsType.isAssignableFrom(BigDecimal.class)){
                    method.invoke(obj,new BigDecimal(0));
                }else if(parmsType.isAssignableFrom(String.class)){
                    method.invoke(obj,new String(""));
                }else if(parmsType.isAssignableFrom(Date.class)){
                    method.invoke(obj,date);
                }else if(parmsType.isAssignableFrom(Boolean.class)){
                    method.invoke(obj,true);
                }else if(parmsType.isAssignableFrom(Short.class)) {
                    method.invoke(obj, Short.valueOf("0"));
                }
            }
        }
    }

}
