package com.baison.e3plus.basebiz.order.service.dao.model.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RetailAdditionalRefundExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RetailAdditionalRefundExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNull() {
            addCriterion("shop_code is null");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNotNull() {
            addCriterion("shop_code is not null");
            return (Criteria) this;
        }

        public Criteria andShopCodeEqualTo(String value) {
            addCriterion("shop_code =", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotEqualTo(String value) {
            addCriterion("shop_code <>", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThan(String value) {
            addCriterion("shop_code >", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThanOrEqualTo(String value) {
            addCriterion("shop_code >=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThan(String value) {
            addCriterion("shop_code <", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThanOrEqualTo(String value) {
            addCriterion("shop_code <=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLike(String value) {
            addCriterion("shop_code like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotLike(String value) {
            addCriterion("shop_code not like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeIn(List<String> values) {
            addCriterion("shop_code in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotIn(List<String> values) {
            addCriterion("shop_code not in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeBetween(String value1, String value2) {
            addCriterion("shop_code between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotBetween(String value1, String value2) {
            addCriterion("shop_code not between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andRefundNoIsNull() {
            addCriterion("refund_no is null");
            return (Criteria) this;
        }

        public Criteria andRefundNoIsNotNull() {
            addCriterion("refund_no is not null");
            return (Criteria) this;
        }

        public Criteria andRefundNoEqualTo(String value) {
            addCriterion("refund_no =", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoNotEqualTo(String value) {
            addCriterion("refund_no <>", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoGreaterThan(String value) {
            addCriterion("refund_no >", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoGreaterThanOrEqualTo(String value) {
            addCriterion("refund_no >=", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoLessThan(String value) {
            addCriterion("refund_no <", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoLessThanOrEqualTo(String value) {
            addCriterion("refund_no <=", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoLike(String value) {
            addCriterion("refund_no like", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoNotLike(String value) {
            addCriterion("refund_no not like", value, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoIn(List<String> values) {
            addCriterion("refund_no in", values, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoNotIn(List<String> values) {
            addCriterion("refund_no not in", values, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoBetween(String value1, String value2) {
            addCriterion("refund_no between", value1, value2, "refundNo");
            return (Criteria) this;
        }

        public Criteria andRefundNoNotBetween(String value1, String value2) {
            addCriterion("refund_no not between", value1, value2, "refundNo");
            return (Criteria) this;
        }

        public Criteria andBillNoIsNull() {
            addCriterion("bill_no is null");
            return (Criteria) this;
        }

        public Criteria andBillNoIsNotNull() {
            addCriterion("bill_no is not null");
            return (Criteria) this;
        }

        public Criteria andBillNoEqualTo(String value) {
            addCriterion("bill_no =", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoNotEqualTo(String value) {
            addCriterion("bill_no <>", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoGreaterThan(String value) {
            addCriterion("bill_no >", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoGreaterThanOrEqualTo(String value) {
            addCriterion("bill_no >=", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoLessThan(String value) {
            addCriterion("bill_no <", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoLessThanOrEqualTo(String value) {
            addCriterion("bill_no <=", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoLike(String value) {
            addCriterion("bill_no like", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoNotLike(String value) {
            addCriterion("bill_no not like", value, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoIn(List<String> values) {
            addCriterion("bill_no in", values, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoNotIn(List<String> values) {
            addCriterion("bill_no not in", values, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoBetween(String value1, String value2) {
            addCriterion("bill_no between", value1, value2, "billNo");
            return (Criteria) this;
        }

        public Criteria andBillNoNotBetween(String value1, String value2) {
            addCriterion("bill_no not between", value1, value2, "billNo");
            return (Criteria) this;
        }

        public Criteria andTransNoIsNull() {
            addCriterion("trans_no is null");
            return (Criteria) this;
        }

        public Criteria andTransNoIsNotNull() {
            addCriterion("trans_no is not null");
            return (Criteria) this;
        }

        public Criteria andTransNoEqualTo(String value) {
            addCriterion("trans_no =", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoNotEqualTo(String value) {
            addCriterion("trans_no <>", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoGreaterThan(String value) {
            addCriterion("trans_no >", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoGreaterThanOrEqualTo(String value) {
            addCriterion("trans_no >=", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoLessThan(String value) {
            addCriterion("trans_no <", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoLessThanOrEqualTo(String value) {
            addCriterion("trans_no <=", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoLike(String value) {
            addCriterion("trans_no like", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoNotLike(String value) {
            addCriterion("trans_no not like", value, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoIn(List<String> values) {
            addCriterion("trans_no in", values, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoNotIn(List<String> values) {
            addCriterion("trans_no not in", values, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoBetween(String value1, String value2) {
            addCriterion("trans_no between", value1, value2, "transNo");
            return (Criteria) this;
        }

        public Criteria andTransNoNotBetween(String value1, String value2) {
            addCriterion("trans_no not between", value1, value2, "transNo");
            return (Criteria) this;
        }

        public Criteria andOperationDateIsNull() {
            addCriterion("operation_date is null");
            return (Criteria) this;
        }

        public Criteria andOperationDateIsNotNull() {
            addCriterion("operation_date is not null");
            return (Criteria) this;
        }

        public Criteria andOperationDateEqualTo(Date value) {
            addCriterion("operation_date =", value, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateNotEqualTo(Date value) {
            addCriterion("operation_date <>", value, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateGreaterThan(Date value) {
            addCriterion("operation_date >", value, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateGreaterThanOrEqualTo(Date value) {
            addCriterion("operation_date >=", value, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateLessThan(Date value) {
            addCriterion("operation_date <", value, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateLessThanOrEqualTo(Date value) {
            addCriterion("operation_date <=", value, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateIn(List<Date> values) {
            addCriterion("operation_date in", values, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateNotIn(List<Date> values) {
            addCriterion("operation_date not in", values, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateBetween(Date value1, Date value2) {
            addCriterion("operation_date between", value1, value2, "operationDate");
            return (Criteria) this;
        }

        public Criteria andOperationDateNotBetween(Date value1, Date value2) {
            addCriterion("operation_date not between", value1, value2, "operationDate");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyIsNull() {
            addCriterion("deal_with_money is null");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyIsNotNull() {
            addCriterion("deal_with_money is not null");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyEqualTo(Long value) {
            addCriterion("deal_with_money =", value, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyNotEqualTo(Long value) {
            addCriterion("deal_with_money <>", value, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyGreaterThan(Long value) {
            addCriterion("deal_with_money >", value, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("deal_with_money >=", value, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyLessThan(Long value) {
            addCriterion("deal_with_money <", value, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyLessThanOrEqualTo(Long value) {
            addCriterion("deal_with_money <=", value, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyIn(List<Long> values) {
            addCriterion("deal_with_money in", values, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyNotIn(List<Long> values) {
            addCriterion("deal_with_money not in", values, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyBetween(Long value1, Long value2) {
            addCriterion("deal_with_money between", value1, value2, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andDealWithMoneyNotBetween(Long value1, Long value2) {
            addCriterion("deal_with_money not between", value1, value2, "dealWithMoney");
            return (Criteria) this;
        }

        public Criteria andPayedAmountIsNull() {
            addCriterion("payed_amount is null");
            return (Criteria) this;
        }

        public Criteria andPayedAmountIsNotNull() {
            addCriterion("payed_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPayedAmountEqualTo(Long value) {
            addCriterion("payed_amount =", value, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountNotEqualTo(Long value) {
            addCriterion("payed_amount <>", value, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountGreaterThan(Long value) {
            addCriterion("payed_amount >", value, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("payed_amount >=", value, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountLessThan(Long value) {
            addCriterion("payed_amount <", value, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountLessThanOrEqualTo(Long value) {
            addCriterion("payed_amount <=", value, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountIn(List<Long> values) {
            addCriterion("payed_amount in", values, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountNotIn(List<Long> values) {
            addCriterion("payed_amount not in", values, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountBetween(Long value1, Long value2) {
            addCriterion("payed_amount between", value1, value2, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andPayedAmountNotBetween(Long value1, Long value2) {
            addCriterion("payed_amount not between", value1, value2, "payedAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountIsNull() {
            addCriterion("additional_refund_amount is null");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountIsNotNull() {
            addCriterion("additional_refund_amount is not null");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountEqualTo(Long value) {
            addCriterion("additional_refund_amount =", value, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountNotEqualTo(Long value) {
            addCriterion("additional_refund_amount <>", value, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountGreaterThan(Long value) {
            addCriterion("additional_refund_amount >", value, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("additional_refund_amount >=", value, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountLessThan(Long value) {
            addCriterion("additional_refund_amount <", value, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountLessThanOrEqualTo(Long value) {
            addCriterion("additional_refund_amount <=", value, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountIn(List<Long> values) {
            addCriterion("additional_refund_amount in", values, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountNotIn(List<Long> values) {
            addCriterion("additional_refund_amount not in", values, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountBetween(Long value1, Long value2) {
            addCriterion("additional_refund_amount between", value1, value2, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundAmountNotBetween(Long value1, Long value2) {
            addCriterion("additional_refund_amount not between", value1, value2, "additionalRefundAmount");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonIsNull() {
            addCriterion("additional_refund_reason is null");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonIsNotNull() {
            addCriterion("additional_refund_reason is not null");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonEqualTo(String value) {
            addCriterion("additional_refund_reason =", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonNotEqualTo(String value) {
            addCriterion("additional_refund_reason <>", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonGreaterThan(String value) {
            addCriterion("additional_refund_reason >", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonGreaterThanOrEqualTo(String value) {
            addCriterion("additional_refund_reason >=", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonLessThan(String value) {
            addCriterion("additional_refund_reason <", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonLessThanOrEqualTo(String value) {
            addCriterion("additional_refund_reason <=", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonLike(String value) {
            addCriterion("additional_refund_reason like", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonNotLike(String value) {
            addCriterion("additional_refund_reason not like", value, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonIn(List<String> values) {
            addCriterion("additional_refund_reason in", values, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonNotIn(List<String> values) {
            addCriterion("additional_refund_reason not in", values, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonBetween(String value1, String value2) {
            addCriterion("additional_refund_reason between", value1, value2, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andAdditionalRefundReasonNotBetween(String value1, String value2) {
            addCriterion("additional_refund_reason not between", value1, value2, "additionalRefundReason");
            return (Criteria) this;
        }

        public Criteria andOperatorByIsNull() {
            addCriterion("operator_by is null");
            return (Criteria) this;
        }

        public Criteria andOperatorByIsNotNull() {
            addCriterion("operator_by is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorByEqualTo(String value) {
            addCriterion("operator_by =", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByNotEqualTo(String value) {
            addCriterion("operator_by <>", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByGreaterThan(String value) {
            addCriterion("operator_by >", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByGreaterThanOrEqualTo(String value) {
            addCriterion("operator_by >=", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByLessThan(String value) {
            addCriterion("operator_by <", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByLessThanOrEqualTo(String value) {
            addCriterion("operator_by <=", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByLike(String value) {
            addCriterion("operator_by like", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByNotLike(String value) {
            addCriterion("operator_by not like", value, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByIn(List<String> values) {
            addCriterion("operator_by in", values, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByNotIn(List<String> values) {
            addCriterion("operator_by not in", values, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByBetween(String value1, String value2) {
            addCriterion("operator_by between", value1, value2, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andOperatorByNotBetween(String value1, String value2) {
            addCriterion("operator_by not between", value1, value2, "operatorBy");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}