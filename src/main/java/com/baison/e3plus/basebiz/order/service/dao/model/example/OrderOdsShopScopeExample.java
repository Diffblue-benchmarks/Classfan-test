package com.baison.e3plus.basebiz.order.service.dao.model.example;

import java.util.ArrayList;
import java.util.List;

public class OrderOdsShopScopeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderOdsShopScopeExample() {
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

        public Criteria andShopScopeIdIsNull() {
            addCriterion("shop_scope_id is null");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdIsNotNull() {
            addCriterion("shop_scope_id is not null");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdEqualTo(Long value) {
            addCriterion("shop_scope_id =", value, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdNotEqualTo(Long value) {
            addCriterion("shop_scope_id <>", value, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdGreaterThan(Long value) {
            addCriterion("shop_scope_id >", value, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("shop_scope_id >=", value, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdLessThan(Long value) {
            addCriterion("shop_scope_id <", value, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdLessThanOrEqualTo(Long value) {
            addCriterion("shop_scope_id <=", value, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdIn(List<Long> values) {
            addCriterion("shop_scope_id in", values, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdNotIn(List<Long> values) {
            addCriterion("shop_scope_id not in", values, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdBetween(Long value1, Long value2) {
            addCriterion("shop_scope_id between", value1, value2, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andShopScopeIdNotBetween(Long value1, Long value2) {
            addCriterion("shop_scope_id not between", value1, value2, "shopScopeId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdIsNull() {
            addCriterion("order_distribute_strategy_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdIsNotNull() {
            addCriterion("order_distribute_strategy_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdEqualTo(Long value) {
            addCriterion("order_distribute_strategy_id =", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdNotEqualTo(Long value) {
            addCriterion("order_distribute_strategy_id <>", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdGreaterThan(Long value) {
            addCriterion("order_distribute_strategy_id >", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdGreaterThanOrEqualTo(Long value) {
            addCriterion("order_distribute_strategy_id >=", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdLessThan(Long value) {
            addCriterion("order_distribute_strategy_id <", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdLessThanOrEqualTo(Long value) {
            addCriterion("order_distribute_strategy_id <=", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdIn(List<Long> values) {
            addCriterion("order_distribute_strategy_id in", values, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdNotIn(List<Long> values) {
            addCriterion("order_distribute_strategy_id not in", values, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdBetween(Long value1, Long value2) {
            addCriterion("order_distribute_strategy_id between", value1, value2, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdNotBetween(Long value1, Long value2) {
            addCriterion("order_distribute_strategy_id not between", value1, value2, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andScopeTypeIsNull() {
            addCriterion("scope_type is null");
            return (Criteria) this;
        }

        public Criteria andScopeTypeIsNotNull() {
            addCriterion("scope_type is not null");
            return (Criteria) this;
        }

        public Criteria andScopeTypeEqualTo(Byte value) {
            addCriterion("scope_type =", value, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeNotEqualTo(Byte value) {
            addCriterion("scope_type <>", value, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeGreaterThan(Byte value) {
            addCriterion("scope_type >", value, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("scope_type >=", value, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeLessThan(Byte value) {
            addCriterion("scope_type <", value, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeLessThanOrEqualTo(Byte value) {
            addCriterion("scope_type <=", value, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeIn(List<Byte> values) {
            addCriterion("scope_type in", values, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeNotIn(List<Byte> values) {
            addCriterion("scope_type not in", values, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeBetween(Byte value1, Byte value2) {
            addCriterion("scope_type between", value1, value2, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("scope_type not between", value1, value2, "scopeType");
            return (Criteria) this;
        }

        public Criteria andScopeConditionIsNull() {
            addCriterion("scope_condition is null");
            return (Criteria) this;
        }

        public Criteria andScopeConditionIsNotNull() {
            addCriterion("scope_condition is not null");
            return (Criteria) this;
        }

        public Criteria andScopeConditionEqualTo(String value) {
            addCriterion("scope_condition =", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionNotEqualTo(String value) {
            addCriterion("scope_condition <>", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionGreaterThan(String value) {
            addCriterion("scope_condition >", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionGreaterThanOrEqualTo(String value) {
            addCriterion("scope_condition >=", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionLessThan(String value) {
            addCriterion("scope_condition <", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionLessThanOrEqualTo(String value) {
            addCriterion("scope_condition <=", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionLike(String value) {
            addCriterion("scope_condition like", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionNotLike(String value) {
            addCriterion("scope_condition not like", value, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionIn(List<String> values) {
            addCriterion("scope_condition in", values, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionNotIn(List<String> values) {
            addCriterion("scope_condition not in", values, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionBetween(String value1, String value2) {
            addCriterion("scope_condition between", value1, value2, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeConditionNotBetween(String value1, String value2) {
            addCriterion("scope_condition not between", value1, value2, "scopeCondition");
            return (Criteria) this;
        }

        public Criteria andScopeDescIsNull() {
            addCriterion("scope_desc is null");
            return (Criteria) this;
        }

        public Criteria andScopeDescIsNotNull() {
            addCriterion("scope_desc is not null");
            return (Criteria) this;
        }

        public Criteria andScopeDescEqualTo(String value) {
            addCriterion("scope_desc =", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescNotEqualTo(String value) {
            addCriterion("scope_desc <>", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescGreaterThan(String value) {
            addCriterion("scope_desc >", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescGreaterThanOrEqualTo(String value) {
            addCriterion("scope_desc >=", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescLessThan(String value) {
            addCriterion("scope_desc <", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescLessThanOrEqualTo(String value) {
            addCriterion("scope_desc <=", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescLike(String value) {
            addCriterion("scope_desc like", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescNotLike(String value) {
            addCriterion("scope_desc not like", value, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescIn(List<String> values) {
            addCriterion("scope_desc in", values, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescNotIn(List<String> values) {
            addCriterion("scope_desc not in", values, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescBetween(String value1, String value2) {
            addCriterion("scope_desc between", value1, value2, "scopeDesc");
            return (Criteria) this;
        }

        public Criteria andScopeDescNotBetween(String value1, String value2) {
            addCriterion("scope_desc not between", value1, value2, "scopeDesc");
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