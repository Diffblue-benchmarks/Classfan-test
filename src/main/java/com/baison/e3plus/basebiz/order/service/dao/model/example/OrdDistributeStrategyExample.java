package com.baison.e3plus.basebiz.order.service.dao.model.example;

import java.util.ArrayList;
import java.util.List;

public class OrdDistributeStrategyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrdDistributeStrategyExample() {
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

        public Criteria andOrderDistributeStrategyIdIsNull() {
            addCriterion("order_distribute_strategy_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdIsNotNull() {
            addCriterion("order_distribute_strategy_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdEqualTo(Integer value) {
            addCriterion("order_distribute_strategy_id =", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdNotEqualTo(Integer value) {
            addCriterion("order_distribute_strategy_id <>", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdGreaterThan(Integer value) {
            addCriterion("order_distribute_strategy_id >", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_distribute_strategy_id >=", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdLessThan(Integer value) {
            addCriterion("order_distribute_strategy_id <", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdLessThanOrEqualTo(Integer value) {
            addCriterion("order_distribute_strategy_id <=", value, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdIn(List<Integer> values) {
            addCriterion("order_distribute_strategy_id in", values, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdNotIn(List<Integer> values) {
            addCriterion("order_distribute_strategy_id not in", values, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdBetween(Integer value1, Integer value2) {
            addCriterion("order_distribute_strategy_id between", value1, value2, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andOrderDistributeStrategyIdNotBetween(Integer value1, Integer value2) {
            addCriterion("order_distribute_strategy_id not between", value1, value2, "orderDistributeStrategyId");
            return (Criteria) this;
        }

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleIsNull() {
            addCriterion("distribute_rule is null");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleIsNotNull() {
            addCriterion("distribute_rule is not null");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleEqualTo(Boolean value) {
            addCriterion("distribute_rule =", value, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleNotEqualTo(Boolean value) {
            addCriterion("distribute_rule <>", value, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleGreaterThan(Boolean value) {
            addCriterion("distribute_rule >", value, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleGreaterThanOrEqualTo(Boolean value) {
            addCriterion("distribute_rule >=", value, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleLessThan(Boolean value) {
            addCriterion("distribute_rule <", value, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleLessThanOrEqualTo(Boolean value) {
            addCriterion("distribute_rule <=", value, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleIn(List<Boolean> values) {
            addCriterion("distribute_rule in", values, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleNotIn(List<Boolean> values) {
            addCriterion("distribute_rule not in", values, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleBetween(Boolean value1, Boolean value2) {
            addCriterion("distribute_rule between", value1, value2, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andDistributeRuleNotBetween(Boolean value1, Boolean value2) {
            addCriterion("distribute_rule not between", value1, value2, "distributeRule");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
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

        public Criteria andIsAllWareHouseDistributeIsNull() {
            addCriterion("is_all_ware_house_distribute is null");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeIsNotNull() {
            addCriterion("is_all_ware_house_distribute is not null");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeEqualTo(Boolean value) {
            addCriterion("is_all_ware_house_distribute =", value, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeNotEqualTo(Boolean value) {
            addCriterion("is_all_ware_house_distribute <>", value, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeGreaterThan(Boolean value) {
            addCriterion("is_all_ware_house_distribute >", value, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_all_ware_house_distribute >=", value, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeLessThan(Boolean value) {
            addCriterion("is_all_ware_house_distribute <", value, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeLessThanOrEqualTo(Boolean value) {
            addCriterion("is_all_ware_house_distribute <=", value, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeIn(List<Boolean> values) {
            addCriterion("is_all_ware_house_distribute in", values, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeNotIn(List<Boolean> values) {
            addCriterion("is_all_ware_house_distribute not in", values, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeBetween(Boolean value1, Boolean value2) {
            addCriterion("is_all_ware_house_distribute between", value1, value2, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andIsAllWareHouseDistributeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_all_ware_house_distribute not between", value1, value2, "isAllWareHouseDistribute");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstIsNull() {
            addCriterion("e_commerce_first is null");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstIsNotNull() {
            addCriterion("e_commerce_first is not null");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstEqualTo(Boolean value) {
            addCriterion("e_commerce_first =", value, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstNotEqualTo(Boolean value) {
            addCriterion("e_commerce_first <>", value, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstGreaterThan(Boolean value) {
            addCriterion("e_commerce_first >", value, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstGreaterThanOrEqualTo(Boolean value) {
            addCriterion("e_commerce_first >=", value, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstLessThan(Boolean value) {
            addCriterion("e_commerce_first <", value, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstLessThanOrEqualTo(Boolean value) {
            addCriterion("e_commerce_first <=", value, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstIn(List<Boolean> values) {
            addCriterion("e_commerce_first in", values, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstNotIn(List<Boolean> values) {
            addCriterion("e_commerce_first not in", values, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstBetween(Boolean value1, Boolean value2) {
            addCriterion("e_commerce_first between", value1, value2, "eCommerceFirst");
            return (Criteria) this;
        }

        public Criteria andECommerceFirstNotBetween(Boolean value1, Boolean value2) {
            addCriterion("e_commerce_first not between", value1, value2, "eCommerceFirst");
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