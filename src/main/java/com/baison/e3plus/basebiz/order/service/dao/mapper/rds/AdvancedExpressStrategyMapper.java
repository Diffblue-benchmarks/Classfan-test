package com.baison.e3plus.basebiz.order.service.dao.mapper.rds;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baison.e3plus.basebiz.order.api.model.adapter.AdvancedExpressStrategy;
import com.baison.e3plus.basebiz.order.service.dao.model.example.AdvancedExpressStrategyExample;

public interface AdvancedExpressStrategyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdvancedExpressStrategy record);

    int insertSelective(AdvancedExpressStrategy record);

    List<AdvancedExpressStrategy> selectByExample(AdvancedExpressStrategyExample example);

    AdvancedExpressStrategy selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdvancedExpressStrategy record);

    int updateByPrimaryKey(AdvancedExpressStrategy record);

    int insertBatch(AdvancedExpressStrategy[] beans);

    int updateByPrimaryKeySelectiveBatch(AdvancedExpressStrategy[] beans);

    int deleteByPrimaryKeyBatch(Long[] idArr);

    List<AdvancedExpressStrategy> selectByPrimaryKeyBatch(Long[] idArr);
    
    List<AdvancedExpressStrategy> selectByPrimaryKeyAndStatusBatch(@Param("ids") Long[] ids, @Param("status") int status);

    long countByExample(AdvancedExpressStrategyExample example);
    
    List<Long> selectByShopIdAndWareHouseId(@Param("shopId")Long shopId, @Param("wareHouseId")Long wareHouseId, @Param("status")Integer status);
    
    List<Long> selectByWareHouseId(@Param("wareHouseId")Long wareHouseId, @Param("status")Integer status);

    List<AdvancedExpressStrategy> queryByWareHouseId(Long[] wareHouseIdSet);
}