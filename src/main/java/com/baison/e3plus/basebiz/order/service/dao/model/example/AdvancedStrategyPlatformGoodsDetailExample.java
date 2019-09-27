package com.baison.e3plus.basebiz.order.service.dao.model.example;

import java.util.ArrayList;
import java.util.List;

public class AdvancedStrategyPlatformGoodsDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AdvancedStrategyPlatformGoodsDetailExample() {
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

        public Criteria andExpressStrategyIdIsNull() {
            addCriterion("express_strategy_id is null");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdIsNotNull() {
            addCriterion("express_strategy_id is not null");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdEqualTo(Long value) {
            addCriterion("express_strategy_id =", value, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdNotEqualTo(Long value) {
            addCriterion("express_strategy_id <>", value, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdGreaterThan(Long value) {
            addCriterion("express_strategy_id >", value, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdGreaterThanOrEqualTo(Long value) {
            addCriterion("express_strategy_id >=", value, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdLessThan(Long value) {
            addCriterion("express_strategy_id <", value, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdLessThanOrEqualTo(Long value) {
            addCriterion("express_strategy_id <=", value, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdIn(List<Long> values) {
            addCriterion("express_strategy_id in", values, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdNotIn(List<Long> values) {
            addCriterion("express_strategy_id not in", values, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdBetween(Long value1, Long value2) {
            addCriterion("express_strategy_id between", value1, value2, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andExpressStrategyIdNotBetween(Long value1, Long value2) {
            addCriterion("express_strategy_id not between", value1, value2, "expressStrategyId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdIsNull() {
            addCriterion("platform_goods_id is null");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdIsNotNull() {
            addCriterion("platform_goods_id is not null");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdEqualTo(Long value) {
            addCriterion("platform_goods_id =", value, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdNotEqualTo(Long value) {
            addCriterion("platform_goods_id <>", value, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdGreaterThan(Long value) {
            addCriterion("platform_goods_id >", value, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdGreaterThanOrEqualTo(Long value) {
            addCriterion("platform_goods_id >=", value, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdLessThan(Long value) {
            addCriterion("platform_goods_id <", value, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdLessThanOrEqualTo(Long value) {
            addCriterion("platform_goods_id <=", value, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdIn(List<Long> values) {
            addCriterion("platform_goods_id in", values, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdNotIn(List<Long> values) {
            addCriterion("platform_goods_id not in", values, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdBetween(Long value1, Long value2) {
            addCriterion("platform_goods_id between", value1, value2, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andPlatformGoodsIdNotBetween(Long value1, Long value2) {
            addCriterion("platform_goods_id not between", value1, value2, "platformGoodsId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdIsNull() {
            addCriterion("delivery_type_id is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdIsNotNull() {
            addCriterion("delivery_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdEqualTo(Long value) {
            addCriterion("delivery_type_id =", value, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdNotEqualTo(Long value) {
            addCriterion("delivery_type_id <>", value, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdGreaterThan(Long value) {
            addCriterion("delivery_type_id >", value, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("delivery_type_id >=", value, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdLessThan(Long value) {
            addCriterion("delivery_type_id <", value, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdLessThanOrEqualTo(Long value) {
            addCriterion("delivery_type_id <=", value, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdIn(List<Long> values) {
            addCriterion("delivery_type_id in", values, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdNotIn(List<Long> values) {
            addCriterion("delivery_type_id not in", values, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdBetween(Long value1, Long value2) {
            addCriterion("delivery_type_id between", value1, value2, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeIdNotBetween(Long value1, Long value2) {
            addCriterion("delivery_type_id not between", value1, value2, "deliveryTypeId");
            return (Criteria) this;
        }

        public Criteria andPriorityIsNull() {
            addCriterion("priority is null");
            return (Criteria) this;
        }

        public Criteria andPriorityIsNotNull() {
            addCriterion("priority is not null");
            return (Criteria) this;
        }

        public Criteria andPriorityEqualTo(Integer value) {
            addCriterion("priority =", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityNotEqualTo(Integer value) {
            addCriterion("priority <>", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityGreaterThan(Integer value) {
            addCriterion("priority >", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityGreaterThanOrEqualTo(Integer value) {
            addCriterion("priority >=", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityLessThan(Integer value) {
            addCriterion("priority <", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityLessThanOrEqualTo(Integer value) {
            addCriterion("priority <=", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityIn(List<Integer> values) {
            addCriterion("priority in", values, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityNotIn(List<Integer> values) {
            addCriterion("priority not in", values, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityBetween(Integer value1, Integer value2) {
            addCriterion("priority between", value1, value2, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityNotBetween(Integer value1, Integer value2) {
            addCriterion("priority not between", value1, value2, "priority");
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