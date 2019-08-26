package com.ai.ecp.pmph.dao.model;

import com.ai.ecp.frame.vo.BaseCriteria;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrdMainTMCriteria extends BaseCriteria implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected String suffix = "";

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public OrdMainTMCriteria() {
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

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
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
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("ID like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("ID not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOrderCodeIsNull() {
            addCriterion("ORDER_CODE is null");
            return (Criteria) this;
        }

        public Criteria andOrderCodeIsNotNull() {
            addCriterion("ORDER_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andOrderCodeEqualTo(String value) {
            addCriterion("ORDER_CODE =", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeNotEqualTo(String value) {
            addCriterion("ORDER_CODE <>", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeGreaterThan(String value) {
            addCriterion("ORDER_CODE >", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ORDER_CODE >=", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeLessThan(String value) {
            addCriterion("ORDER_CODE <", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeLessThanOrEqualTo(String value) {
            addCriterion("ORDER_CODE <=", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeLike(String value) {
            addCriterion("ORDER_CODE like", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeNotLike(String value) {
            addCriterion("ORDER_CODE not like", value, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeIn(List<String> values) {
            addCriterion("ORDER_CODE in", values, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeNotIn(List<String> values) {
            addCriterion("ORDER_CODE not in", values, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeBetween(String value1, String value2) {
            addCriterion("ORDER_CODE between", value1, value2, "orderCode");
            return (Criteria) this;
        }

        public Criteria andOrderCodeNotBetween(String value1, String value2) {
            addCriterion("ORDER_CODE not between", value1, value2, "orderCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeIsNull() {
            addCriterion("TM_STAFF_CODE is null");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeIsNotNull() {
            addCriterion("TM_STAFF_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeEqualTo(String value) {
            addCriterion("TM_STAFF_CODE =", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeNotEqualTo(String value) {
            addCriterion("TM_STAFF_CODE <>", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeGreaterThan(String value) {
            addCriterion("TM_STAFF_CODE >", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeGreaterThanOrEqualTo(String value) {
            addCriterion("TM_STAFF_CODE >=", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeLessThan(String value) {
            addCriterion("TM_STAFF_CODE <", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeLessThanOrEqualTo(String value) {
            addCriterion("TM_STAFF_CODE <=", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeLike(String value) {
            addCriterion("TM_STAFF_CODE like", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeNotLike(String value) {
            addCriterion("TM_STAFF_CODE not like", value, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeIn(List<String> values) {
            addCriterion("TM_STAFF_CODE in", values, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeNotIn(List<String> values) {
            addCriterion("TM_STAFF_CODE not in", values, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeBetween(String value1, String value2) {
            addCriterion("TM_STAFF_CODE between", value1, value2, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andTmStaffCodeNotBetween(String value1, String value2) {
            addCriterion("TM_STAFF_CODE not between", value1, value2, "tmStaffCode");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountIsNull() {
            addCriterion("ALIPAY_ACCOUNT is null");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountIsNotNull() {
            addCriterion("ALIPAY_ACCOUNT is not null");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountEqualTo(String value) {
            addCriterion("ALIPAY_ACCOUNT =", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotEqualTo(String value) {
            addCriterion("ALIPAY_ACCOUNT <>", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountGreaterThan(String value) {
            addCriterion("ALIPAY_ACCOUNT >", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountGreaterThanOrEqualTo(String value) {
            addCriterion("ALIPAY_ACCOUNT >=", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountLessThan(String value) {
            addCriterion("ALIPAY_ACCOUNT <", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountLessThanOrEqualTo(String value) {
            addCriterion("ALIPAY_ACCOUNT <=", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountLike(String value) {
            addCriterion("ALIPAY_ACCOUNT like", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotLike(String value) {
            addCriterion("ALIPAY_ACCOUNT not like", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountIn(List<String> values) {
            addCriterion("ALIPAY_ACCOUNT in", values, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotIn(List<String> values) {
            addCriterion("ALIPAY_ACCOUNT not in", values, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountBetween(String value1, String value2) {
            addCriterion("ALIPAY_ACCOUNT between", value1, value2, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotBetween(String value1, String value2) {
            addCriterion("ALIPAY_ACCOUNT not between", value1, value2, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyIsNull() {
            addCriterion("ORDER_MONEY is null");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyIsNotNull() {
            addCriterion("ORDER_MONEY is not null");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyEqualTo(String value) {
            addCriterion("ORDER_MONEY =", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyNotEqualTo(String value) {
            addCriterion("ORDER_MONEY <>", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyGreaterThan(String value) {
            addCriterion("ORDER_MONEY >", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyGreaterThanOrEqualTo(String value) {
            addCriterion("ORDER_MONEY >=", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyLessThan(String value) {
            addCriterion("ORDER_MONEY <", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyLessThanOrEqualTo(String value) {
            addCriterion("ORDER_MONEY <=", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyLike(String value) {
            addCriterion("ORDER_MONEY like", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyNotLike(String value) {
            addCriterion("ORDER_MONEY not like", value, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyIn(List<String> values) {
            addCriterion("ORDER_MONEY in", values, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyNotIn(List<String> values) {
            addCriterion("ORDER_MONEY not in", values, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyBetween(String value1, String value2) {
            addCriterion("ORDER_MONEY between", value1, value2, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andOrderMoneyNotBetween(String value1, String value2) {
            addCriterion("ORDER_MONEY not between", value1, value2, "orderMoney");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeIsNull() {
            addCriterion("REAL_EXPRESS_FEE is null");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeIsNotNull() {
            addCriterion("REAL_EXPRESS_FEE is not null");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeEqualTo(String value) {
            addCriterion("REAL_EXPRESS_FEE =", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeNotEqualTo(String value) {
            addCriterion("REAL_EXPRESS_FEE <>", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeGreaterThan(String value) {
            addCriterion("REAL_EXPRESS_FEE >", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeGreaterThanOrEqualTo(String value) {
            addCriterion("REAL_EXPRESS_FEE >=", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeLessThan(String value) {
            addCriterion("REAL_EXPRESS_FEE <", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeLessThanOrEqualTo(String value) {
            addCriterion("REAL_EXPRESS_FEE <=", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeLike(String value) {
            addCriterion("REAL_EXPRESS_FEE like", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeNotLike(String value) {
            addCriterion("REAL_EXPRESS_FEE not like", value, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeIn(List<String> values) {
            addCriterion("REAL_EXPRESS_FEE in", values, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeNotIn(List<String> values) {
            addCriterion("REAL_EXPRESS_FEE not in", values, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeBetween(String value1, String value2) {
            addCriterion("REAL_EXPRESS_FEE between", value1, value2, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andRealExpressFeeNotBetween(String value1, String value2) {
            addCriterion("REAL_EXPRESS_FEE not between", value1, value2, "realExpressFee");
            return (Criteria) this;
        }

        public Criteria andOrderScoreIsNull() {
            addCriterion("ORDER_SCORE is null");
            return (Criteria) this;
        }

        public Criteria andOrderScoreIsNotNull() {
            addCriterion("ORDER_SCORE is not null");
            return (Criteria) this;
        }

        public Criteria andOrderScoreEqualTo(String value) {
            addCriterion("ORDER_SCORE =", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreNotEqualTo(String value) {
            addCriterion("ORDER_SCORE <>", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreGreaterThan(String value) {
            addCriterion("ORDER_SCORE >", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreGreaterThanOrEqualTo(String value) {
            addCriterion("ORDER_SCORE >=", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreLessThan(String value) {
            addCriterion("ORDER_SCORE <", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreLessThanOrEqualTo(String value) {
            addCriterion("ORDER_SCORE <=", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreLike(String value) {
            addCriterion("ORDER_SCORE like", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreNotLike(String value) {
            addCriterion("ORDER_SCORE not like", value, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreIn(List<String> values) {
            addCriterion("ORDER_SCORE in", values, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreNotIn(List<String> values) {
            addCriterion("ORDER_SCORE not in", values, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreBetween(String value1, String value2) {
            addCriterion("ORDER_SCORE between", value1, value2, "orderScore");
            return (Criteria) this;
        }

        public Criteria andOrderScoreNotBetween(String value1, String value2) {
            addCriterion("ORDER_SCORE not between", value1, value2, "orderScore");
            return (Criteria) this;
        }

        public Criteria andSumMoneyIsNull() {
            addCriterion("SUM_MONEY is null");
            return (Criteria) this;
        }

        public Criteria andSumMoneyIsNotNull() {
            addCriterion("SUM_MONEY is not null");
            return (Criteria) this;
        }

        public Criteria andSumMoneyEqualTo(String value) {
            addCriterion("SUM_MONEY =", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyNotEqualTo(String value) {
            addCriterion("SUM_MONEY <>", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyGreaterThan(String value) {
            addCriterion("SUM_MONEY >", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyGreaterThanOrEqualTo(String value) {
            addCriterion("SUM_MONEY >=", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyLessThan(String value) {
            addCriterion("SUM_MONEY <", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyLessThanOrEqualTo(String value) {
            addCriterion("SUM_MONEY <=", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyLike(String value) {
            addCriterion("SUM_MONEY like", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyNotLike(String value) {
            addCriterion("SUM_MONEY not like", value, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyIn(List<String> values) {
            addCriterion("SUM_MONEY in", values, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyNotIn(List<String> values) {
            addCriterion("SUM_MONEY not in", values, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyBetween(String value1, String value2) {
            addCriterion("SUM_MONEY between", value1, value2, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andSumMoneyNotBetween(String value1, String value2) {
            addCriterion("SUM_MONEY not between", value1, value2, "sumMoney");
            return (Criteria) this;
        }

        public Criteria andBackScoreIsNull() {
            addCriterion("BACK_SCORE is null");
            return (Criteria) this;
        }

        public Criteria andBackScoreIsNotNull() {
            addCriterion("BACK_SCORE is not null");
            return (Criteria) this;
        }

        public Criteria andBackScoreEqualTo(String value) {
            addCriterion("BACK_SCORE =", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreNotEqualTo(String value) {
            addCriterion("BACK_SCORE <>", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreGreaterThan(String value) {
            addCriterion("BACK_SCORE >", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreGreaterThanOrEqualTo(String value) {
            addCriterion("BACK_SCORE >=", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreLessThan(String value) {
            addCriterion("BACK_SCORE <", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreLessThanOrEqualTo(String value) {
            addCriterion("BACK_SCORE <=", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreLike(String value) {
            addCriterion("BACK_SCORE like", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreNotLike(String value) {
            addCriterion("BACK_SCORE not like", value, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreIn(List<String> values) {
            addCriterion("BACK_SCORE in", values, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreNotIn(List<String> values) {
            addCriterion("BACK_SCORE not in", values, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreBetween(String value1, String value2) {
            addCriterion("BACK_SCORE between", value1, value2, "backScore");
            return (Criteria) this;
        }

        public Criteria andBackScoreNotBetween(String value1, String value2) {
            addCriterion("BACK_SCORE not between", value1, value2, "backScore");
            return (Criteria) this;
        }

        public Criteria andRealMoneyIsNull() {
            addCriterion("REAL_MONEY is null");
            return (Criteria) this;
        }

        public Criteria andRealMoneyIsNotNull() {
            addCriterion("REAL_MONEY is not null");
            return (Criteria) this;
        }

        public Criteria andRealMoneyEqualTo(String value) {
            addCriterion("REAL_MONEY =", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyNotEqualTo(String value) {
            addCriterion("REAL_MONEY <>", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyGreaterThan(String value) {
            addCriterion("REAL_MONEY >", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyGreaterThanOrEqualTo(String value) {
            addCriterion("REAL_MONEY >=", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyLessThan(String value) {
            addCriterion("REAL_MONEY <", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyLessThanOrEqualTo(String value) {
            addCriterion("REAL_MONEY <=", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyLike(String value) {
            addCriterion("REAL_MONEY like", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyNotLike(String value) {
            addCriterion("REAL_MONEY not like", value, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyIn(List<String> values) {
            addCriterion("REAL_MONEY in", values, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyNotIn(List<String> values) {
            addCriterion("REAL_MONEY not in", values, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyBetween(String value1, String value2) {
            addCriterion("REAL_MONEY between", value1, value2, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealMoneyNotBetween(String value1, String value2) {
            addCriterion("REAL_MONEY not between", value1, value2, "realMoney");
            return (Criteria) this;
        }

        public Criteria andRealScoreIsNull() {
            addCriterion("REAL_SCORE is null");
            return (Criteria) this;
        }

        public Criteria andRealScoreIsNotNull() {
            addCriterion("REAL_SCORE is not null");
            return (Criteria) this;
        }

        public Criteria andRealScoreEqualTo(String value) {
            addCriterion("REAL_SCORE =", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreNotEqualTo(String value) {
            addCriterion("REAL_SCORE <>", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreGreaterThan(String value) {
            addCriterion("REAL_SCORE >", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreGreaterThanOrEqualTo(String value) {
            addCriterion("REAL_SCORE >=", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreLessThan(String value) {
            addCriterion("REAL_SCORE <", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreLessThanOrEqualTo(String value) {
            addCriterion("REAL_SCORE <=", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreLike(String value) {
            addCriterion("REAL_SCORE like", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreNotLike(String value) {
            addCriterion("REAL_SCORE not like", value, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreIn(List<String> values) {
            addCriterion("REAL_SCORE in", values, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreNotIn(List<String> values) {
            addCriterion("REAL_SCORE not in", values, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreBetween(String value1, String value2) {
            addCriterion("REAL_SCORE between", value1, value2, "realScore");
            return (Criteria) this;
        }

        public Criteria andRealScoreNotBetween(String value1, String value2) {
            addCriterion("REAL_SCORE not between", value1, value2, "realScore");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("STATUS is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("STATUS =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("STATUS <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("STATUS >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("STATUS >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("STATUS <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("STATUS <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("STATUS like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("STATUS not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("STATUS in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("STATUS not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("STATUS between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("STATUS not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgIsNull() {
            addCriterion("BUYER_MSG is null");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgIsNotNull() {
            addCriterion("BUYER_MSG is not null");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgEqualTo(String value) {
            addCriterion("BUYER_MSG =", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgNotEqualTo(String value) {
            addCriterion("BUYER_MSG <>", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgGreaterThan(String value) {
            addCriterion("BUYER_MSG >", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgGreaterThanOrEqualTo(String value) {
            addCriterion("BUYER_MSG >=", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgLessThan(String value) {
            addCriterion("BUYER_MSG <", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgLessThanOrEqualTo(String value) {
            addCriterion("BUYER_MSG <=", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgLike(String value) {
            addCriterion("BUYER_MSG like", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgNotLike(String value) {
            addCriterion("BUYER_MSG not like", value, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgIn(List<String> values) {
            addCriterion("BUYER_MSG in", values, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgNotIn(List<String> values) {
            addCriterion("BUYER_MSG not in", values, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgBetween(String value1, String value2) {
            addCriterion("BUYER_MSG between", value1, value2, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andBuyerMsgNotBetween(String value1, String value2) {
            addCriterion("BUYER_MSG not between", value1, value2, "buyerMsg");
            return (Criteria) this;
        }

        public Criteria andContractNameIsNull() {
            addCriterion("CONTRACT_NAME is null");
            return (Criteria) this;
        }

        public Criteria andContractNameIsNotNull() {
            addCriterion("CONTRACT_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andContractNameEqualTo(String value) {
            addCriterion("CONTRACT_NAME =", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotEqualTo(String value) {
            addCriterion("CONTRACT_NAME <>", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameGreaterThan(String value) {
            addCriterion("CONTRACT_NAME >", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameGreaterThanOrEqualTo(String value) {
            addCriterion("CONTRACT_NAME >=", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameLessThan(String value) {
            addCriterion("CONTRACT_NAME <", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameLessThanOrEqualTo(String value) {
            addCriterion("CONTRACT_NAME <=", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameLike(String value) {
            addCriterion("CONTRACT_NAME like", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotLike(String value) {
            addCriterion("CONTRACT_NAME not like", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameIn(List<String> values) {
            addCriterion("CONTRACT_NAME in", values, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotIn(List<String> values) {
            addCriterion("CONTRACT_NAME not in", values, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameBetween(String value1, String value2) {
            addCriterion("CONTRACT_NAME between", value1, value2, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotBetween(String value1, String value2) {
            addCriterion("CONTRACT_NAME not between", value1, value2, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractAddrIsNull() {
            addCriterion("CONTRACT_ADDR is null");
            return (Criteria) this;
        }

        public Criteria andContractAddrIsNotNull() {
            addCriterion("CONTRACT_ADDR is not null");
            return (Criteria) this;
        }

        public Criteria andContractAddrEqualTo(String value) {
            addCriterion("CONTRACT_ADDR =", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrNotEqualTo(String value) {
            addCriterion("CONTRACT_ADDR <>", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrGreaterThan(String value) {
            addCriterion("CONTRACT_ADDR >", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrGreaterThanOrEqualTo(String value) {
            addCriterion("CONTRACT_ADDR >=", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrLessThan(String value) {
            addCriterion("CONTRACT_ADDR <", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrLessThanOrEqualTo(String value) {
            addCriterion("CONTRACT_ADDR <=", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrLike(String value) {
            addCriterion("CONTRACT_ADDR like", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrNotLike(String value) {
            addCriterion("CONTRACT_ADDR not like", value, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrIn(List<String> values) {
            addCriterion("CONTRACT_ADDR in", values, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrNotIn(List<String> values) {
            addCriterion("CONTRACT_ADDR not in", values, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrBetween(String value1, String value2) {
            addCriterion("CONTRACT_ADDR between", value1, value2, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andContractAddrNotBetween(String value1, String value2) {
            addCriterion("CONTRACT_ADDR not between", value1, value2, "contractAddr");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeIsNull() {
            addCriterion("DISPATCH_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeIsNotNull() {
            addCriterion("DISPATCH_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeEqualTo(String value) {
            addCriterion("DISPATCH_TYPE =", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeNotEqualTo(String value) {
            addCriterion("DISPATCH_TYPE <>", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeGreaterThan(String value) {
            addCriterion("DISPATCH_TYPE >", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeGreaterThanOrEqualTo(String value) {
            addCriterion("DISPATCH_TYPE >=", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeLessThan(String value) {
            addCriterion("DISPATCH_TYPE <", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeLessThanOrEqualTo(String value) {
            addCriterion("DISPATCH_TYPE <=", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeLike(String value) {
            addCriterion("DISPATCH_TYPE like", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeNotLike(String value) {
            addCriterion("DISPATCH_TYPE not like", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeIn(List<String> values) {
            addCriterion("DISPATCH_TYPE in", values, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeNotIn(List<String> values) {
            addCriterion("DISPATCH_TYPE not in", values, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeBetween(String value1, String value2) {
            addCriterion("DISPATCH_TYPE between", value1, value2, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeNotBetween(String value1, String value2) {
            addCriterion("DISPATCH_TYPE not between", value1, value2, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andContractTelIsNull() {
            addCriterion("CONTRACT_TEL is null");
            return (Criteria) this;
        }

        public Criteria andContractTelIsNotNull() {
            addCriterion("CONTRACT_TEL is not null");
            return (Criteria) this;
        }

        public Criteria andContractTelEqualTo(String value) {
            addCriterion("CONTRACT_TEL =", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelNotEqualTo(String value) {
            addCriterion("CONTRACT_TEL <>", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelGreaterThan(String value) {
            addCriterion("CONTRACT_TEL >", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelGreaterThanOrEqualTo(String value) {
            addCriterion("CONTRACT_TEL >=", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelLessThan(String value) {
            addCriterion("CONTRACT_TEL <", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelLessThanOrEqualTo(String value) {
            addCriterion("CONTRACT_TEL <=", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelLike(String value) {
            addCriterion("CONTRACT_TEL like", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelNotLike(String value) {
            addCriterion("CONTRACT_TEL not like", value, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelIn(List<String> values) {
            addCriterion("CONTRACT_TEL in", values, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelNotIn(List<String> values) {
            addCriterion("CONTRACT_TEL not in", values, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelBetween(String value1, String value2) {
            addCriterion("CONTRACT_TEL between", value1, value2, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractTelNotBetween(String value1, String value2) {
            addCriterion("CONTRACT_TEL not between", value1, value2, "contractTel");
            return (Criteria) this;
        }

        public Criteria andContractNumIsNull() {
            addCriterion("CONTRACT_NUM is null");
            return (Criteria) this;
        }

        public Criteria andContractNumIsNotNull() {
            addCriterion("CONTRACT_NUM is not null");
            return (Criteria) this;
        }

        public Criteria andContractNumEqualTo(String value) {
            addCriterion("CONTRACT_NUM =", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumNotEqualTo(String value) {
            addCriterion("CONTRACT_NUM <>", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumGreaterThan(String value) {
            addCriterion("CONTRACT_NUM >", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumGreaterThanOrEqualTo(String value) {
            addCriterion("CONTRACT_NUM >=", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumLessThan(String value) {
            addCriterion("CONTRACT_NUM <", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumLessThanOrEqualTo(String value) {
            addCriterion("CONTRACT_NUM <=", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumLike(String value) {
            addCriterion("CONTRACT_NUM like", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumNotLike(String value) {
            addCriterion("CONTRACT_NUM not like", value, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumIn(List<String> values) {
            addCriterion("CONTRACT_NUM in", values, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumNotIn(List<String> values) {
            addCriterion("CONTRACT_NUM not in", values, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumBetween(String value1, String value2) {
            addCriterion("CONTRACT_NUM between", value1, value2, "contractNum");
            return (Criteria) this;
        }

        public Criteria andContractNumNotBetween(String value1, String value2) {
            addCriterion("CONTRACT_NUM not between", value1, value2, "contractNum");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNull() {
            addCriterion("ORDER_TIME is null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNotNull() {
            addCriterion("ORDER_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeEqualTo(Timestamp value) {
            addCriterion("ORDER_TIME =", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotEqualTo(Timestamp value) {
            addCriterion("ORDER_TIME <>", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThan(Timestamp value) {
            addCriterion("ORDER_TIME >", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("ORDER_TIME >=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThan(Timestamp value) {
            addCriterion("ORDER_TIME <", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("ORDER_TIME <=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIn(List<Timestamp> values) {
            addCriterion("ORDER_TIME in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotIn(List<Timestamp> values) {
            addCriterion("ORDER_TIME not in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("ORDER_TIME between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("ORDER_TIME not between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeIsNull() {
            addCriterion("PAY_TIME is null");
            return (Criteria) this;
        }

        public Criteria andPayTimeIsNotNull() {
            addCriterion("PAY_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andPayTimeEqualTo(Timestamp value) {
            addCriterion("PAY_TIME =", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotEqualTo(Timestamp value) {
            addCriterion("PAY_TIME <>", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThan(Timestamp value) {
            addCriterion("PAY_TIME >", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("PAY_TIME >=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThan(Timestamp value) {
            addCriterion("PAY_TIME <", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("PAY_TIME <=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeIn(List<Timestamp> values) {
            addCriterion("PAY_TIME in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotIn(List<Timestamp> values) {
            addCriterion("PAY_TIME not in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("PAY_TIME between", value1, value2, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("PAY_TIME not between", value1, value2, "payTime");
            return (Criteria) this;
        }

        public Criteria andBbTitleIsNull() {
            addCriterion("BB_TITLE is null");
            return (Criteria) this;
        }

        public Criteria andBbTitleIsNotNull() {
            addCriterion("BB_TITLE is not null");
            return (Criteria) this;
        }

        public Criteria andBbTitleEqualTo(String value) {
            addCriterion("BB_TITLE =", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleNotEqualTo(String value) {
            addCriterion("BB_TITLE <>", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleGreaterThan(String value) {
            addCriterion("BB_TITLE >", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleGreaterThanOrEqualTo(String value) {
            addCriterion("BB_TITLE >=", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleLessThan(String value) {
            addCriterion("BB_TITLE <", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleLessThanOrEqualTo(String value) {
            addCriterion("BB_TITLE <=", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleLike(String value) {
            addCriterion("BB_TITLE like", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleNotLike(String value) {
            addCriterion("BB_TITLE not like", value, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleIn(List<String> values) {
            addCriterion("BB_TITLE in", values, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleNotIn(List<String> values) {
            addCriterion("BB_TITLE not in", values, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleBetween(String value1, String value2) {
            addCriterion("BB_TITLE between", value1, value2, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTitleNotBetween(String value1, String value2) {
            addCriterion("BB_TITLE not between", value1, value2, "bbTitle");
            return (Criteria) this;
        }

        public Criteria andBbTypeIsNull() {
            addCriterion("BB_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andBbTypeIsNotNull() {
            addCriterion("BB_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andBbTypeEqualTo(String value) {
            addCriterion("BB_TYPE =", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeNotEqualTo(String value) {
            addCriterion("BB_TYPE <>", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeGreaterThan(String value) {
            addCriterion("BB_TYPE >", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeGreaterThanOrEqualTo(String value) {
            addCriterion("BB_TYPE >=", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeLessThan(String value) {
            addCriterion("BB_TYPE <", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeLessThanOrEqualTo(String value) {
            addCriterion("BB_TYPE <=", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeLike(String value) {
            addCriterion("BB_TYPE like", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeNotLike(String value) {
            addCriterion("BB_TYPE not like", value, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeIn(List<String> values) {
            addCriterion("BB_TYPE in", values, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeNotIn(List<String> values) {
            addCriterion("BB_TYPE not in", values, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeBetween(String value1, String value2) {
            addCriterion("BB_TYPE between", value1, value2, "bbType");
            return (Criteria) this;
        }

        public Criteria andBbTypeNotBetween(String value1, String value2) {
            addCriterion("BB_TYPE not between", value1, value2, "bbType");
            return (Criteria) this;
        }

        public Criteria andExpressNoIsNull() {
            addCriterion("EXPRESS_NO is null");
            return (Criteria) this;
        }

        public Criteria andExpressNoIsNotNull() {
            addCriterion("EXPRESS_NO is not null");
            return (Criteria) this;
        }

        public Criteria andExpressNoEqualTo(String value) {
            addCriterion("EXPRESS_NO =", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotEqualTo(String value) {
            addCriterion("EXPRESS_NO <>", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoGreaterThan(String value) {
            addCriterion("EXPRESS_NO >", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoGreaterThanOrEqualTo(String value) {
            addCriterion("EXPRESS_NO >=", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoLessThan(String value) {
            addCriterion("EXPRESS_NO <", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoLessThanOrEqualTo(String value) {
            addCriterion("EXPRESS_NO <=", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoLike(String value) {
            addCriterion("EXPRESS_NO like", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotLike(String value) {
            addCriterion("EXPRESS_NO not like", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoIn(List<String> values) {
            addCriterion("EXPRESS_NO in", values, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotIn(List<String> values) {
            addCriterion("EXPRESS_NO not in", values, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoBetween(String value1, String value2) {
            addCriterion("EXPRESS_NO between", value1, value2, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotBetween(String value1, String value2) {
            addCriterion("EXPRESS_NO not between", value1, value2, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyIsNull() {
            addCriterion("EXPRESS_COMPANY is null");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyIsNotNull() {
            addCriterion("EXPRESS_COMPANY is not null");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyEqualTo(String value) {
            addCriterion("EXPRESS_COMPANY =", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyNotEqualTo(String value) {
            addCriterion("EXPRESS_COMPANY <>", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyGreaterThan(String value) {
            addCriterion("EXPRESS_COMPANY >", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyGreaterThanOrEqualTo(String value) {
            addCriterion("EXPRESS_COMPANY >=", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyLessThan(String value) {
            addCriterion("EXPRESS_COMPANY <", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyLessThanOrEqualTo(String value) {
            addCriterion("EXPRESS_COMPANY <=", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyLike(String value) {
            addCriterion("EXPRESS_COMPANY like", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyNotLike(String value) {
            addCriterion("EXPRESS_COMPANY not like", value, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyIn(List<String> values) {
            addCriterion("EXPRESS_COMPANY in", values, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyNotIn(List<String> values) {
            addCriterion("EXPRESS_COMPANY not in", values, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyBetween(String value1, String value2) {
            addCriterion("EXPRESS_COMPANY between", value1, value2, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andExpressCompanyNotBetween(String value1, String value2) {
            addCriterion("EXPRESS_COMPANY not between", value1, value2, "expressCompany");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("REMARK is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("REMARK is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("REMARK =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("REMARK <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("REMARK >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("REMARK >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("REMARK <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("REMARK <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("REMARK like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("REMARK not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("REMARK in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("REMARK not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("REMARK between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("REMARK not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNull() {
            addCriterion("ORDER_AMOUNT is null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNotNull() {
            addCriterion("ORDER_AMOUNT is not null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountEqualTo(String value) {
            addCriterion("ORDER_AMOUNT =", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotEqualTo(String value) {
            addCriterion("ORDER_AMOUNT <>", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThan(String value) {
            addCriterion("ORDER_AMOUNT >", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThanOrEqualTo(String value) {
            addCriterion("ORDER_AMOUNT >=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThan(String value) {
            addCriterion("ORDER_AMOUNT <", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThanOrEqualTo(String value) {
            addCriterion("ORDER_AMOUNT <=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLike(String value) {
            addCriterion("ORDER_AMOUNT like", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotLike(String value) {
            addCriterion("ORDER_AMOUNT not like", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIn(List<String> values) {
            addCriterion("ORDER_AMOUNT in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotIn(List<String> values) {
            addCriterion("ORDER_AMOUNT not in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountBetween(String value1, String value2) {
            addCriterion("ORDER_AMOUNT between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotBetween(String value1, String value2) {
            addCriterion("ORDER_AMOUNT not between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNull() {
            addCriterion("SHOP_ID is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("SHOP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(String value) {
            addCriterion("SHOP_ID =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(String value) {
            addCriterion("SHOP_ID <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(String value) {
            addCriterion("SHOP_ID >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(String value) {
            addCriterion("SHOP_ID >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(String value) {
            addCriterion("SHOP_ID <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(String value) {
            addCriterion("SHOP_ID <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLike(String value) {
            addCriterion("SHOP_ID like", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotLike(String value) {
            addCriterion("SHOP_ID not like", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<String> values) {
            addCriterion("SHOP_ID in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<String> values) {
            addCriterion("SHOP_ID not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(String value1, String value2) {
            addCriterion("SHOP_ID between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(String value1, String value2) {
            addCriterion("SHOP_ID not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopNameIsNull() {
            addCriterion("SHOP_NAME is null");
            return (Criteria) this;
        }

        public Criteria andShopNameIsNotNull() {
            addCriterion("SHOP_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andShopNameEqualTo(String value) {
            addCriterion("SHOP_NAME =", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotEqualTo(String value) {
            addCriterion("SHOP_NAME <>", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameGreaterThan(String value) {
            addCriterion("SHOP_NAME >", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameGreaterThanOrEqualTo(String value) {
            addCriterion("SHOP_NAME >=", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLessThan(String value) {
            addCriterion("SHOP_NAME <", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLessThanOrEqualTo(String value) {
            addCriterion("SHOP_NAME <=", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLike(String value) {
            addCriterion("SHOP_NAME like", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotLike(String value) {
            addCriterion("SHOP_NAME not like", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameIn(List<String> values) {
            addCriterion("SHOP_NAME in", values, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotIn(List<String> values) {
            addCriterion("SHOP_NAME not in", values, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameBetween(String value1, String value2) {
            addCriterion("SHOP_NAME between", value1, value2, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotBetween(String value1, String value2) {
            addCriterion("SHOP_NAME not between", value1, value2, "shopName");
            return (Criteria) this;
        }

        public Criteria andCloseReasonIsNull() {
            addCriterion("CLOSE_REASON is null");
            return (Criteria) this;
        }

        public Criteria andCloseReasonIsNotNull() {
            addCriterion("CLOSE_REASON is not null");
            return (Criteria) this;
        }

        public Criteria andCloseReasonEqualTo(String value) {
            addCriterion("CLOSE_REASON =", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonNotEqualTo(String value) {
            addCriterion("CLOSE_REASON <>", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonGreaterThan(String value) {
            addCriterion("CLOSE_REASON >", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonGreaterThanOrEqualTo(String value) {
            addCriterion("CLOSE_REASON >=", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonLessThan(String value) {
            addCriterion("CLOSE_REASON <", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonLessThanOrEqualTo(String value) {
            addCriterion("CLOSE_REASON <=", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonLike(String value) {
            addCriterion("CLOSE_REASON like", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonNotLike(String value) {
            addCriterion("CLOSE_REASON not like", value, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonIn(List<String> values) {
            addCriterion("CLOSE_REASON in", values, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonNotIn(List<String> values) {
            addCriterion("CLOSE_REASON not in", values, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonBetween(String value1, String value2) {
            addCriterion("CLOSE_REASON between", value1, value2, "closeReason");
            return (Criteria) this;
        }

        public Criteria andCloseReasonNotBetween(String value1, String value2) {
            addCriterion("CLOSE_REASON not between", value1, value2, "closeReason");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeIsNull() {
            addCriterion("SHOP_SERVICE_FEE is null");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeIsNotNull() {
            addCriterion("SHOP_SERVICE_FEE is not null");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeEqualTo(String value) {
            addCriterion("SHOP_SERVICE_FEE =", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeNotEqualTo(String value) {
            addCriterion("SHOP_SERVICE_FEE <>", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeGreaterThan(String value) {
            addCriterion("SHOP_SERVICE_FEE >", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeGreaterThanOrEqualTo(String value) {
            addCriterion("SHOP_SERVICE_FEE >=", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeLessThan(String value) {
            addCriterion("SHOP_SERVICE_FEE <", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeLessThanOrEqualTo(String value) {
            addCriterion("SHOP_SERVICE_FEE <=", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeLike(String value) {
            addCriterion("SHOP_SERVICE_FEE like", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeNotLike(String value) {
            addCriterion("SHOP_SERVICE_FEE not like", value, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeIn(List<String> values) {
            addCriterion("SHOP_SERVICE_FEE in", values, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeNotIn(List<String> values) {
            addCriterion("SHOP_SERVICE_FEE not in", values, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeBetween(String value1, String value2) {
            addCriterion("SHOP_SERVICE_FEE between", value1, value2, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andShopServiceFeeNotBetween(String value1, String value2) {
            addCriterion("SHOP_SERVICE_FEE not between", value1, value2, "shopServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeIsNull() {
            addCriterion("STAFF_SERVICE_FEE is null");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeIsNotNull() {
            addCriterion("STAFF_SERVICE_FEE is not null");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeEqualTo(String value) {
            addCriterion("STAFF_SERVICE_FEE =", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeNotEqualTo(String value) {
            addCriterion("STAFF_SERVICE_FEE <>", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeGreaterThan(String value) {
            addCriterion("STAFF_SERVICE_FEE >", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeGreaterThanOrEqualTo(String value) {
            addCriterion("STAFF_SERVICE_FEE >=", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeLessThan(String value) {
            addCriterion("STAFF_SERVICE_FEE <", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeLessThanOrEqualTo(String value) {
            addCriterion("STAFF_SERVICE_FEE <=", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeLike(String value) {
            addCriterion("STAFF_SERVICE_FEE like", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeNotLike(String value) {
            addCriterion("STAFF_SERVICE_FEE not like", value, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeIn(List<String> values) {
            addCriterion("STAFF_SERVICE_FEE in", values, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeNotIn(List<String> values) {
            addCriterion("STAFF_SERVICE_FEE not in", values, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeBetween(String value1, String value2) {
            addCriterion("STAFF_SERVICE_FEE between", value1, value2, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andStaffServiceFeeNotBetween(String value1, String value2) {
            addCriterion("STAFF_SERVICE_FEE not between", value1, value2, "staffServiceFee");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleIsNull() {
            addCriterion("INVOICE_TITLE is null");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleIsNotNull() {
            addCriterion("INVOICE_TITLE is not null");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleEqualTo(String value) {
            addCriterion("INVOICE_TITLE =", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleNotEqualTo(String value) {
            addCriterion("INVOICE_TITLE <>", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleGreaterThan(String value) {
            addCriterion("INVOICE_TITLE >", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleGreaterThanOrEqualTo(String value) {
            addCriterion("INVOICE_TITLE >=", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleLessThan(String value) {
            addCriterion("INVOICE_TITLE <", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleLessThanOrEqualTo(String value) {
            addCriterion("INVOICE_TITLE <=", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleLike(String value) {
            addCriterion("INVOICE_TITLE like", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleNotLike(String value) {
            addCriterion("INVOICE_TITLE not like", value, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleIn(List<String> values) {
            addCriterion("INVOICE_TITLE in", values, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleNotIn(List<String> values) {
            addCriterion("INVOICE_TITLE not in", values, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleBetween(String value1, String value2) {
            addCriterion("INVOICE_TITLE between", value1, value2, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andInvoiceTitleNotBetween(String value1, String value2) {
            addCriterion("INVOICE_TITLE not between", value1, value2, "invoiceTitle");
            return (Criteria) this;
        }

        public Criteria andAppFlagIsNull() {
            addCriterion("APP_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andAppFlagIsNotNull() {
            addCriterion("APP_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andAppFlagEqualTo(String value) {
            addCriterion("APP_FLAG =", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagNotEqualTo(String value) {
            addCriterion("APP_FLAG <>", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagGreaterThan(String value) {
            addCriterion("APP_FLAG >", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagGreaterThanOrEqualTo(String value) {
            addCriterion("APP_FLAG >=", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagLessThan(String value) {
            addCriterion("APP_FLAG <", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagLessThanOrEqualTo(String value) {
            addCriterion("APP_FLAG <=", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagLike(String value) {
            addCriterion("APP_FLAG like", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagNotLike(String value) {
            addCriterion("APP_FLAG not like", value, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagIn(List<String> values) {
            addCriterion("APP_FLAG in", values, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagNotIn(List<String> values) {
            addCriterion("APP_FLAG not in", values, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagBetween(String value1, String value2) {
            addCriterion("APP_FLAG between", value1, value2, "appFlag");
            return (Criteria) this;
        }

        public Criteria andAppFlagNotBetween(String value1, String value2) {
            addCriterion("APP_FLAG not between", value1, value2, "appFlag");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgIsNull() {
            addCriterion("STAGE_ORDER_MSG is null");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgIsNotNull() {
            addCriterion("STAGE_ORDER_MSG is not null");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgEqualTo(String value) {
            addCriterion("STAGE_ORDER_MSG =", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgNotEqualTo(String value) {
            addCriterion("STAGE_ORDER_MSG <>", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgGreaterThan(String value) {
            addCriterion("STAGE_ORDER_MSG >", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgGreaterThanOrEqualTo(String value) {
            addCriterion("STAGE_ORDER_MSG >=", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgLessThan(String value) {
            addCriterion("STAGE_ORDER_MSG <", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgLessThanOrEqualTo(String value) {
            addCriterion("STAGE_ORDER_MSG <=", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgLike(String value) {
            addCriterion("STAGE_ORDER_MSG like", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgNotLike(String value) {
            addCriterion("STAGE_ORDER_MSG not like", value, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgIn(List<String> values) {
            addCriterion("STAGE_ORDER_MSG in", values, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgNotIn(List<String> values) {
            addCriterion("STAGE_ORDER_MSG not in", values, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgBetween(String value1, String value2) {
            addCriterion("STAGE_ORDER_MSG between", value1, value2, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andStageOrderMsgNotBetween(String value1, String value2) {
            addCriterion("STAGE_ORDER_MSG not between", value1, value2, "stageOrderMsg");
            return (Criteria) this;
        }

        public Criteria andDownRankIsNull() {
            addCriterion("DOWN_RANK is null");
            return (Criteria) this;
        }

        public Criteria andDownRankIsNotNull() {
            addCriterion("DOWN_RANK is not null");
            return (Criteria) this;
        }

        public Criteria andDownRankEqualTo(String value) {
            addCriterion("DOWN_RANK =", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankNotEqualTo(String value) {
            addCriterion("DOWN_RANK <>", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankGreaterThan(String value) {
            addCriterion("DOWN_RANK >", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankGreaterThanOrEqualTo(String value) {
            addCriterion("DOWN_RANK >=", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankLessThan(String value) {
            addCriterion("DOWN_RANK <", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankLessThanOrEqualTo(String value) {
            addCriterion("DOWN_RANK <=", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankLike(String value) {
            addCriterion("DOWN_RANK like", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankNotLike(String value) {
            addCriterion("DOWN_RANK not like", value, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankIn(List<String> values) {
            addCriterion("DOWN_RANK in", values, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankNotIn(List<String> values) {
            addCriterion("DOWN_RANK not in", values, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankBetween(String value1, String value2) {
            addCriterion("DOWN_RANK between", value1, value2, "downRank");
            return (Criteria) this;
        }

        public Criteria andDownRankNotBetween(String value1, String value2) {
            addCriterion("DOWN_RANK not between", value1, value2, "downRank");
            return (Criteria) this;
        }

        public Criteria andModitySkuIsNull() {
            addCriterion("MODITY_SKU is null");
            return (Criteria) this;
        }

        public Criteria andModitySkuIsNotNull() {
            addCriterion("MODITY_SKU is not null");
            return (Criteria) this;
        }

        public Criteria andModitySkuEqualTo(String value) {
            addCriterion("MODITY_SKU =", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuNotEqualTo(String value) {
            addCriterion("MODITY_SKU <>", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuGreaterThan(String value) {
            addCriterion("MODITY_SKU >", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuGreaterThanOrEqualTo(String value) {
            addCriterion("MODITY_SKU >=", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuLessThan(String value) {
            addCriterion("MODITY_SKU <", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuLessThanOrEqualTo(String value) {
            addCriterion("MODITY_SKU <=", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuLike(String value) {
            addCriterion("MODITY_SKU like", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuNotLike(String value) {
            addCriterion("MODITY_SKU not like", value, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuIn(List<String> values) {
            addCriterion("MODITY_SKU in", values, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuNotIn(List<String> values) {
            addCriterion("MODITY_SKU not in", values, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuBetween(String value1, String value2) {
            addCriterion("MODITY_SKU between", value1, value2, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModitySkuNotBetween(String value1, String value2) {
            addCriterion("MODITY_SKU not between", value1, value2, "moditySku");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrIsNull() {
            addCriterion("MODIFY_CONTRACT_ADDR is null");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrIsNotNull() {
            addCriterion("MODIFY_CONTRACT_ADDR is not null");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrEqualTo(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR =", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrNotEqualTo(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR <>", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrGreaterThan(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR >", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrGreaterThanOrEqualTo(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR >=", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrLessThan(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR <", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrLessThanOrEqualTo(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR <=", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrLike(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR like", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrNotLike(String value) {
            addCriterion("MODIFY_CONTRACT_ADDR not like", value, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrIn(List<String> values) {
            addCriterion("MODIFY_CONTRACT_ADDR in", values, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrNotIn(List<String> values) {
            addCriterion("MODIFY_CONTRACT_ADDR not in", values, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrBetween(String value1, String value2) {
            addCriterion("MODIFY_CONTRACT_ADDR between", value1, value2, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andModifyContractAddrNotBetween(String value1, String value2) {
            addCriterion("MODIFY_CONTRACT_ADDR not between", value1, value2, "modifyContractAddr");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgIsNull() {
            addCriterion("EXCEPTION_MSG is null");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgIsNotNull() {
            addCriterion("EXCEPTION_MSG is not null");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgEqualTo(String value) {
            addCriterion("EXCEPTION_MSG =", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgNotEqualTo(String value) {
            addCriterion("EXCEPTION_MSG <>", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgGreaterThan(String value) {
            addCriterion("EXCEPTION_MSG >", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgGreaterThanOrEqualTo(String value) {
            addCriterion("EXCEPTION_MSG >=", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgLessThan(String value) {
            addCriterion("EXCEPTION_MSG <", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgLessThanOrEqualTo(String value) {
            addCriterion("EXCEPTION_MSG <=", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgLike(String value) {
            addCriterion("EXCEPTION_MSG like", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgNotLike(String value) {
            addCriterion("EXCEPTION_MSG not like", value, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgIn(List<String> values) {
            addCriterion("EXCEPTION_MSG in", values, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgNotIn(List<String> values) {
            addCriterion("EXCEPTION_MSG not in", values, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgBetween(String value1, String value2) {
            addCriterion("EXCEPTION_MSG between", value1, value2, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andExceptionMsgNotBetween(String value1, String value2) {
            addCriterion("EXCEPTION_MSG not between", value1, value2, "exceptionMsg");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductIsNull() {
            addCriterion("TM_COUP_DEDUCT is null");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductIsNotNull() {
            addCriterion("TM_COUP_DEDUCT is not null");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductEqualTo(String value) {
            addCriterion("TM_COUP_DEDUCT =", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductNotEqualTo(String value) {
            addCriterion("TM_COUP_DEDUCT <>", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductGreaterThan(String value) {
            addCriterion("TM_COUP_DEDUCT >", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductGreaterThanOrEqualTo(String value) {
            addCriterion("TM_COUP_DEDUCT >=", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductLessThan(String value) {
            addCriterion("TM_COUP_DEDUCT <", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductLessThanOrEqualTo(String value) {
            addCriterion("TM_COUP_DEDUCT <=", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductLike(String value) {
            addCriterion("TM_COUP_DEDUCT like", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductNotLike(String value) {
            addCriterion("TM_COUP_DEDUCT not like", value, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductIn(List<String> values) {
            addCriterion("TM_COUP_DEDUCT in", values, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductNotIn(List<String> values) {
            addCriterion("TM_COUP_DEDUCT not in", values, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductBetween(String value1, String value2) {
            addCriterion("TM_COUP_DEDUCT between", value1, value2, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andTmCoupDeductNotBetween(String value1, String value2) {
            addCriterion("TM_COUP_DEDUCT not between", value1, value2, "tmCoupDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductIsNull() {
            addCriterion("JFB_DEDUCT is null");
            return (Criteria) this;
        }

        public Criteria andJfbDeductIsNotNull() {
            addCriterion("JFB_DEDUCT is not null");
            return (Criteria) this;
        }

        public Criteria andJfbDeductEqualTo(String value) {
            addCriterion("JFB_DEDUCT =", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductNotEqualTo(String value) {
            addCriterion("JFB_DEDUCT <>", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductGreaterThan(String value) {
            addCriterion("JFB_DEDUCT >", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductGreaterThanOrEqualTo(String value) {
            addCriterion("JFB_DEDUCT >=", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductLessThan(String value) {
            addCriterion("JFB_DEDUCT <", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductLessThanOrEqualTo(String value) {
            addCriterion("JFB_DEDUCT <=", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductLike(String value) {
            addCriterion("JFB_DEDUCT like", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductNotLike(String value) {
            addCriterion("JFB_DEDUCT not like", value, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductIn(List<String> values) {
            addCriterion("JFB_DEDUCT in", values, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductNotIn(List<String> values) {
            addCriterion("JFB_DEDUCT not in", values, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductBetween(String value1, String value2) {
            addCriterion("JFB_DEDUCT between", value1, value2, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andJfbDeductNotBetween(String value1, String value2) {
            addCriterion("JFB_DEDUCT not between", value1, value2, "jfbDeduct");
            return (Criteria) this;
        }

        public Criteria andO2oFlagIsNull() {
            addCriterion("O2O_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andO2oFlagIsNotNull() {
            addCriterion("O2O_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andO2oFlagEqualTo(String value) {
            addCriterion("O2O_FLAG =", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagNotEqualTo(String value) {
            addCriterion("O2O_FLAG <>", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagGreaterThan(String value) {
            addCriterion("O2O_FLAG >", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagGreaterThanOrEqualTo(String value) {
            addCriterion("O2O_FLAG >=", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagLessThan(String value) {
            addCriterion("O2O_FLAG <", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagLessThanOrEqualTo(String value) {
            addCriterion("O2O_FLAG <=", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagLike(String value) {
            addCriterion("O2O_FLAG like", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagNotLike(String value) {
            addCriterion("O2O_FLAG not like", value, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagIn(List<String> values) {
            addCriterion("O2O_FLAG in", values, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagNotIn(List<String> values) {
            addCriterion("O2O_FLAG not in", values, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagBetween(String value1, String value2) {
            addCriterion("O2O_FLAG between", value1, value2, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andO2oFlagNotBetween(String value1, String value2) {
            addCriterion("O2O_FLAG not between", value1, value2, "o2oFlag");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Timestamp value) {
            addCriterion("CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Timestamp value) {
            addCriterion("CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Timestamp> values) {
            addCriterion("CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Timestamp> values) {
            addCriterion("CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("CREATE_TIME not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateStaffIsNull() {
            addCriterion("CREATE_STAFF is null");
            return (Criteria) this;
        }

        public Criteria andCreateStaffIsNotNull() {
            addCriterion("CREATE_STAFF is not null");
            return (Criteria) this;
        }

        public Criteria andCreateStaffEqualTo(Long value) {
            addCriterion("CREATE_STAFF =", value, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffNotEqualTo(Long value) {
            addCriterion("CREATE_STAFF <>", value, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffGreaterThan(Long value) {
            addCriterion("CREATE_STAFF >", value, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffGreaterThanOrEqualTo(Long value) {
            addCriterion("CREATE_STAFF >=", value, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffLessThan(Long value) {
            addCriterion("CREATE_STAFF <", value, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffLessThanOrEqualTo(Long value) {
            addCriterion("CREATE_STAFF <=", value, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffIn(List<Long> values) {
            addCriterion("CREATE_STAFF in", values, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffNotIn(List<Long> values) {
            addCriterion("CREATE_STAFF not in", values, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffBetween(Long value1, Long value2) {
            addCriterion("CREATE_STAFF between", value1, value2, "createStaff");
            return (Criteria) this;
        }

        public Criteria andCreateStaffNotBetween(Long value1, Long value2) {
            addCriterion("CREATE_STAFF not between", value1, value2, "createStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Timestamp value) {
            addCriterion("UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Timestamp value) {
            addCriterion("UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Timestamp> values) {
            addCriterion("UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Timestamp> values) {
            addCriterion("UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffIsNull() {
            addCriterion("UPDATE_STAFF is null");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffIsNotNull() {
            addCriterion("UPDATE_STAFF is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffEqualTo(Long value) {
            addCriterion("UPDATE_STAFF =", value, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffNotEqualTo(Long value) {
            addCriterion("UPDATE_STAFF <>", value, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffGreaterThan(Long value) {
            addCriterion("UPDATE_STAFF >", value, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffGreaterThanOrEqualTo(Long value) {
            addCriterion("UPDATE_STAFF >=", value, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffLessThan(Long value) {
            addCriterion("UPDATE_STAFF <", value, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffLessThanOrEqualTo(Long value) {
            addCriterion("UPDATE_STAFF <=", value, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffIn(List<Long> values) {
            addCriterion("UPDATE_STAFF in", values, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffNotIn(List<Long> values) {
            addCriterion("UPDATE_STAFF not in", values, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffBetween(Long value1, Long value2) {
            addCriterion("UPDATE_STAFF between", value1, value2, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andUpdateStaffNotBetween(Long value1, Long value2) {
            addCriterion("UPDATE_STAFF not between", value1, value2, "updateStaff");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeIsNull() {
            addCriterion("RW_STAFF_CODE is null");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeIsNotNull() {
            addCriterion("RW_STAFF_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeEqualTo(String value) {
            addCriterion("RW_STAFF_CODE =", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeNotEqualTo(String value) {
            addCriterion("RW_STAFF_CODE <>", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeGreaterThan(String value) {
            addCriterion("RW_STAFF_CODE >", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeGreaterThanOrEqualTo(String value) {
            addCriterion("RW_STAFF_CODE >=", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeLessThan(String value) {
            addCriterion("RW_STAFF_CODE <", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeLessThanOrEqualTo(String value) {
            addCriterion("RW_STAFF_CODE <=", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeLike(String value) {
            addCriterion("RW_STAFF_CODE like", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeNotLike(String value) {
            addCriterion("RW_STAFF_CODE not like", value, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeIn(List<String> values) {
            addCriterion("RW_STAFF_CODE in", values, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeNotIn(List<String> values) {
            addCriterion("RW_STAFF_CODE not in", values, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeBetween(String value1, String value2) {
            addCriterion("RW_STAFF_CODE between", value1, value2, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffCodeNotBetween(String value1, String value2) {
            addCriterion("RW_STAFF_CODE not between", value1, value2, "rwStaffCode");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdIsNull() {
            addCriterion("RW_STAFF_ID is null");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdIsNotNull() {
            addCriterion("RW_STAFF_ID is not null");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdEqualTo(Long value) {
            addCriterion("RW_STAFF_ID =", value, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdNotEqualTo(Long value) {
            addCriterion("RW_STAFF_ID <>", value, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdGreaterThan(Long value) {
            addCriterion("RW_STAFF_ID >", value, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdGreaterThanOrEqualTo(Long value) {
            addCriterion("RW_STAFF_ID >=", value, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdLessThan(Long value) {
            addCriterion("RW_STAFF_ID <", value, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdLessThanOrEqualTo(Long value) {
            addCriterion("RW_STAFF_ID <=", value, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdIn(List<Long> values) {
            addCriterion("RW_STAFF_ID in", values, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdNotIn(List<Long> values) {
            addCriterion("RW_STAFF_ID not in", values, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdBetween(Long value1, Long value2) {
            addCriterion("RW_STAFF_ID between", value1, value2, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwStaffIdNotBetween(Long value1, Long value2) {
            addCriterion("RW_STAFF_ID not between", value1, value2, "rwStaffId");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagIsNull() {
            addCriterion("RW_SCORE_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagIsNotNull() {
            addCriterion("RW_SCORE_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagEqualTo(String value) {
            addCriterion("RW_SCORE_FLAG =", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagNotEqualTo(String value) {
            addCriterion("RW_SCORE_FLAG <>", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagGreaterThan(String value) {
            addCriterion("RW_SCORE_FLAG >", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagGreaterThanOrEqualTo(String value) {
            addCriterion("RW_SCORE_FLAG >=", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagLessThan(String value) {
            addCriterion("RW_SCORE_FLAG <", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagLessThanOrEqualTo(String value) {
            addCriterion("RW_SCORE_FLAG <=", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagLike(String value) {
            addCriterion("RW_SCORE_FLAG like", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagNotLike(String value) {
            addCriterion("RW_SCORE_FLAG not like", value, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagIn(List<String> values) {
            addCriterion("RW_SCORE_FLAG in", values, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagNotIn(List<String> values) {
            addCriterion("RW_SCORE_FLAG not in", values, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagBetween(String value1, String value2) {
            addCriterion("RW_SCORE_FLAG between", value1, value2, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreFlagNotBetween(String value1, String value2) {
            addCriterion("RW_SCORE_FLAG not between", value1, value2, "rwScoreFlag");
            return (Criteria) this;
        }

        public Criteria andRwScoreIsNull() {
            addCriterion("RW_SCORE is null");
            return (Criteria) this;
        }

        public Criteria andRwScoreIsNotNull() {
            addCriterion("RW_SCORE is not null");
            return (Criteria) this;
        }

        public Criteria andRwScoreEqualTo(Long value) {
            addCriterion("RW_SCORE =", value, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreNotEqualTo(Long value) {
            addCriterion("RW_SCORE <>", value, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreGreaterThan(Long value) {
            addCriterion("RW_SCORE >", value, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreGreaterThanOrEqualTo(Long value) {
            addCriterion("RW_SCORE >=", value, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreLessThan(Long value) {
            addCriterion("RW_SCORE <", value, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreLessThanOrEqualTo(Long value) {
            addCriterion("RW_SCORE <=", value, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreIn(List<Long> values) {
            addCriterion("RW_SCORE in", values, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreNotIn(List<Long> values) {
            addCriterion("RW_SCORE not in", values, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreBetween(Long value1, Long value2) {
            addCriterion("RW_SCORE between", value1, value2, "rwScore");
            return (Criteria) this;
        }

        public Criteria andRwScoreNotBetween(Long value1, Long value2) {
            addCriterion("RW_SCORE not between", value1, value2, "rwScore");
            return (Criteria) this;
        }

        public Criteria andImportTimeIsNull() {
            addCriterion("IMPORT_TIME is null");
            return (Criteria) this;
        }

        public Criteria andImportTimeIsNotNull() {
            addCriterion("IMPORT_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andImportTimeEqualTo(Timestamp value) {
            addCriterion("IMPORT_TIME =", value, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeNotEqualTo(Timestamp value) {
            addCriterion("IMPORT_TIME <>", value, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeGreaterThan(Timestamp value) {
            addCriterion("IMPORT_TIME >", value, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("IMPORT_TIME >=", value, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeLessThan(Timestamp value) {
            addCriterion("IMPORT_TIME <", value, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("IMPORT_TIME <=", value, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeIn(List<Timestamp> values) {
            addCriterion("IMPORT_TIME in", values, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeNotIn(List<Timestamp> values) {
            addCriterion("IMPORT_TIME not in", values, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("IMPORT_TIME between", value1, value2, "importTime");
            return (Criteria) this;
        }

        public Criteria andImportTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("IMPORT_TIME not between", value1, value2, "importTime");
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