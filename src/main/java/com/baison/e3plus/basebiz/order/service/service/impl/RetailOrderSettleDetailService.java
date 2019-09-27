package com.baison.e3plus.basebiz.order.service.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baison.e3plus.basebiz.order.service.feignclient.support.ConsumerIdService;
import com.baison.e3plus.common.cncore.query.E3FilterField;
import com.baison.e3plus.common.cncore.query.E3FilterGroup;
import com.baison.e3plus.common.cncore.query.E3Selector;
import com.baison.e3plus.common.cncore.result.model.ServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baison.e3plus.basebiz.order.api.model.RetailOrderSettleDetail;
import com.baison.e3plus.basebiz.order.api.service.IRetailOrderSettleDetailService;
import com.baison.e3plus.basebiz.order.service.dao.mapper.rds.RetailOrderSettleDetailMapper;

/**
 * 
 * @author cong
 *
 */
@Service
public class RetailOrderSettleDetailService implements IRetailOrderSettleDetailService {

	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen start
	 * @since 2018-12-07
	 */
	@Autowired
	private ConsumerIdService idService;

	@Autowired
	private RetailOrderSettleDetailMapper retailOrderSettleDetailMapper;

	@Override
	@Transactional
	public ServiceResult create(String token, RetailOrderSettleDetail[] objects) {
		ServiceResult result = new ServiceResult();
		if (objects == null || objects.length == 0) {
			result.addErrorObject("", "", "no data input!");
		}

		if (result.hasError()) {
			return result;
		}

		// retailorderid不能为空
		for (RetailOrderSettleDetail detail : objects) {
			if (detail.getBillNo() == null) {
				result.addErrorObject("", "", "bill no is null!");
			}
		}

		if (result.hasError()) {
			return result;
		}

		for (RetailOrderSettleDetail detail : objects) {
			detail.setId(idService.nextId());
		}

		retailOrderSettleDetailMapper.insertSelective(Arrays.asList(objects));

		return result;
	}

	@Override
	@Transactional
	public ServiceResult modify(String token, RetailOrderSettleDetail[] objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ServiceResult remove(String token, Object[] pkIds) {
		ServiceResult result = new ServiceResult();

		if (pkIds == null || pkIds.length == 0) {
			result.addErrorObject("", "", "pkid is null!");

			return result;
		}

		retailOrderSettleDetailMapper.deleteByPrimaryKey(Arrays.asList(pkIds));

		return result;
	}

	@Override
	public RetailOrderSettleDetail[] findById(String token, Object[] pkIds, String[] selectFields) {
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

		List<RetailOrderSettleDetail> details = retailOrderSettleDetailMapper.selectByPrimaryKey(Arrays.asList(pkIds),
				fields);

		return details.toArray(new RetailOrderSettleDetail[details.size()]);
	}

	@Override
	public RetailOrderSettleDetail[] queryPage(String token, E3Selector selector, int pageSize, int pageIndex) {
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

		List<RetailOrderSettleDetail> datas = retailOrderSettleDetailMapper.queryPage(args);

		return datas.toArray(new RetailOrderSettleDetail[datas.size()]);
	}

	@Override
	public long getListCount(String token, E3Selector selector) {
		Map<String, Object> args = new HashMap<>();
		for (E3FilterField field : selector.getFilterFields()) {
			args.put(field.getFieldName(), field.getValue());
		}

		return retailOrderSettleDetailMapper.getListCount(args);
	}

	@Override
	@Transactional
	public ServiceResult removeByRetailOrderId(String token, Object[] retailOrderIds) {
		ServiceResult result = new ServiceResult();

		if (retailOrderIds == null || retailOrderIds.length == 0) {
			result.addErrorObject("", "", "pkid is null!");

			return result;
		}

		retailOrderSettleDetailMapper.deleteByRetailOrderId(Arrays.asList(retailOrderIds));

		return result;
	}
	
	@Override
	@Transactional
	public ServiceResult removeByRetailOrderNo(String token, List<Object> retailOrderNos) {
		ServiceResult result = new ServiceResult();

		if (retailOrderNos == null || retailOrderNos.size() == 0) {
			result.addErrorObject("", "", "remove billno is null!");

			return result;
		}

		retailOrderSettleDetailMapper.deleteByRetailOrderNo(retailOrderNos);

		return result;
	}
	
	/**
	 * 修改只针对于数据源的切换方案
	 * @author qiancheng.chen end
	 * @since 2018-12-07
	 */

}
