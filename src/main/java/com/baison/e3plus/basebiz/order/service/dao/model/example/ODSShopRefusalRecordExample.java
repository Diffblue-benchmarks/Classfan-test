package com.baison.e3plus.basebiz.order.service.dao.model.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ODSShopRefusalRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ODSShopRefusalRecordExample() {
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

        public Criteria andOrderBillNoIsNull() {
            addCriterion("order_bill_no is null");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoIsNotNull() {
            addCriterion("order_bill_no is not null");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoEqualTo(String value) {
            addCriterion("order_bill_no =", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoNotEqualTo(String value) {
            addCriterion("order_bill_no <>", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoGreaterThan(String value) {
            addCriterion("order_bill_no >", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoGreaterThanOrEqualTo(String value) {
            addCriterion("order_bill_no >=", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoLessThan(String value) {
            addCriterion("order_bill_no <", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoLessThanOrEqualTo(String value) {
            addCriterion("order_bill_no <=", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoLike(String value) {
            addCriterion("order_bill_no like", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoNotLike(String value) {
            addCriterion("order_bill_no not like", value, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoIn(List<String> values) {
            addCriterion("order_bill_no in", values, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoNotIn(List<String> values) {
            addCriterion("order_bill_no not in", values, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoBetween(String value1, String value2) {
            addCriterion("order_bill_no between", value1, value2, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andOrderBillNoNotBetween(String value1, String value2) {
            addCriterion("order_bill_no not between", value1, value2, "orderBillNo");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdIsNull() {
            addCriterion("ware_house_id is null");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdIsNotNull() {
            addCriterion("ware_house_id is not null");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdEqualTo(Integer value) {
            addCriterion("ware_house_id =", value, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdNotEqualTo(Integer value) {
            addCriterion("ware_house_id <>", value, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdGreaterThan(Integer value) {
            addCriterion("ware_house_id >", value, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ware_house_id >=", value, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdLessThan(Integer value) {
            addCriterion("ware_house_id <", value, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdLessThanOrEqualTo(Integer value) {
            addCriterion("ware_house_id <=", value, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdIn(List<Integer> values) {
            addCriterion("ware_house_id in", values, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdNotIn(List<Integer> values) {
            addCriterion("ware_house_id not in", values, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdBetween(Integer value1, Integer value2) {
            addCriterion("ware_house_id between", value1, value2, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ware_house_id not between", value1, value2, "wareHouseId");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeIsNull() {
            addCriterion("ware_house_code is null");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeIsNotNull() {
            addCriterion("ware_house_code is not null");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeEqualTo(String value) {
            addCriterion("ware_house_code =", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeNotEqualTo(String value) {
            addCriterion("ware_house_code <>", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeGreaterThan(String value) {
            addCriterion("ware_house_code >", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ware_house_code >=", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeLessThan(String value) {
            addCriterion("ware_house_code <", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeLessThanOrEqualTo(String value) {
            addCriterion("ware_house_code <=", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeLike(String value) {
            addCriterion("ware_house_code like", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeNotLike(String value) {
            addCriterion("ware_house_code not like", value, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeIn(List<String> values) {
            addCriterion("ware_house_code in", values, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeNotIn(List<String> values) {
            addCriterion("ware_house_code not in", values, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeBetween(String value1, String value2) {
            addCriterion("ware_house_code between", value1, value2, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andWareHouseCodeNotBetween(String value1, String value2) {
            addCriterion("ware_house_code not between", value1, value2, "wareHouseCode");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeIsNull() {
            addCriterion("single_product_code is null");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeIsNotNull() {
            addCriterion("single_product_code is not null");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeEqualTo(String value) {
            addCriterion("single_product_code =", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeNotEqualTo(String value) {
            addCriterion("single_product_code <>", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeGreaterThan(String value) {
            addCriterion("single_product_code >", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeGreaterThanOrEqualTo(String value) {
            addCriterion("single_product_code >=", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeLessThan(String value) {
            addCriterion("single_product_code <", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeLessThanOrEqualTo(String value) {
            addCriterion("single_product_code <=", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeLike(String value) {
            addCriterion("single_product_code like", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeNotLike(String value) {
            addCriterion("single_product_code not like", value, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeIn(List<String> values) {
            addCriterion("single_product_code in", values, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeNotIn(List<String> values) {
            addCriterion("single_product_code not in", values, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeBetween(String value1, String value2) {
            addCriterion("single_product_code between", value1, value2, "singleProductCode");
            return (Criteria) this;
        }

        public Criteria andSingleProductCodeNotBetween(String value1, String value2) {
            addCriterion("single_product_code not between", value1, value2, "singleProductCode");
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