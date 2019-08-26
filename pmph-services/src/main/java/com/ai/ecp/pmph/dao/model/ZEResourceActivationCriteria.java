package com.ai.ecp.pmph.dao.model;

import com.ai.ecp.frame.vo.BaseCriteria;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ZEResourceActivationCriteria extends BaseCriteria implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected String suffix = "";

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public ZEResourceActivationCriteria() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPastecardidIsNull() {
            addCriterion("PASTECARDID is null");
            return (Criteria) this;
        }

        public Criteria andPastecardidIsNotNull() {
            addCriterion("PASTECARDID is not null");
            return (Criteria) this;
        }

        public Criteria andPastecardidEqualTo(Long value) {
            addCriterion("PASTECARDID =", value, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidNotEqualTo(Long value) {
            addCriterion("PASTECARDID <>", value, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidGreaterThan(Long value) {
            addCriterion("PASTECARDID >", value, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidGreaterThanOrEqualTo(Long value) {
            addCriterion("PASTECARDID >=", value, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidLessThan(Long value) {
            addCriterion("PASTECARDID <", value, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidLessThanOrEqualTo(Long value) {
            addCriterion("PASTECARDID <=", value, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidIn(List<Long> values) {
            addCriterion("PASTECARDID in", values, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidNotIn(List<Long> values) {
            addCriterion("PASTECARDID not in", values, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidBetween(Long value1, Long value2) {
            addCriterion("PASTECARDID between", value1, value2, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andPastecardidNotBetween(Long value1, Long value2) {
            addCriterion("PASTECARDID not between", value1, value2, "pastecardid");
            return (Criteria) this;
        }

        public Criteria andCardnoIsNull() {
            addCriterion("CARDNO is null");
            return (Criteria) this;
        }

        public Criteria andCardnoIsNotNull() {
            addCriterion("CARDNO is not null");
            return (Criteria) this;
        }

        public Criteria andCardnoEqualTo(String value) {
            addCriterion("CARDNO =", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoNotEqualTo(String value) {
            addCriterion("CARDNO <>", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoGreaterThan(String value) {
            addCriterion("CARDNO >", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoGreaterThanOrEqualTo(String value) {
            addCriterion("CARDNO >=", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoLessThan(String value) {
            addCriterion("CARDNO <", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoLessThanOrEqualTo(String value) {
            addCriterion("CARDNO <=", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoLike(String value) {
            addCriterion("CARDNO like", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoNotLike(String value) {
            addCriterion("CARDNO not like", value, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoIn(List<String> values) {
            addCriterion("CARDNO in", values, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoNotIn(List<String> values) {
            addCriterion("CARDNO not in", values, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoBetween(String value1, String value2) {
            addCriterion("CARDNO between", value1, value2, "cardno");
            return (Criteria) this;
        }

        public Criteria andCardnoNotBetween(String value1, String value2) {
            addCriterion("CARDNO not between", value1, value2, "cardno");
            return (Criteria) this;
        }

        public Criteria andMemoIsNull() {
            addCriterion("MEMO is null");
            return (Criteria) this;
        }

        public Criteria andMemoIsNotNull() {
            addCriterion("MEMO is not null");
            return (Criteria) this;
        }

        public Criteria andMemoEqualTo(String value) {
            addCriterion("MEMO =", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotEqualTo(String value) {
            addCriterion("MEMO <>", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThan(String value) {
            addCriterion("MEMO >", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThanOrEqualTo(String value) {
            addCriterion("MEMO >=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThan(String value) {
            addCriterion("MEMO <", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThanOrEqualTo(String value) {
            addCriterion("MEMO <=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLike(String value) {
            addCriterion("MEMO like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotLike(String value) {
            addCriterion("MEMO not like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoIn(List<String> values) {
            addCriterion("MEMO in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotIn(List<String> values) {
            addCriterion("MEMO not in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoBetween(String value1, String value2) {
            addCriterion("MEMO between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotBetween(String value1, String value2) {
            addCriterion("MEMO not between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andProp1IsNull() {
            addCriterion("PROP1 is null");
            return (Criteria) this;
        }

        public Criteria andProp1IsNotNull() {
            addCriterion("PROP1 is not null");
            return (Criteria) this;
        }

        public Criteria andProp1EqualTo(String value) {
            addCriterion("PROP1 =", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1NotEqualTo(String value) {
            addCriterion("PROP1 <>", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1GreaterThan(String value) {
            addCriterion("PROP1 >", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1GreaterThanOrEqualTo(String value) {
            addCriterion("PROP1 >=", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1LessThan(String value) {
            addCriterion("PROP1 <", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1LessThanOrEqualTo(String value) {
            addCriterion("PROP1 <=", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1Like(String value) {
            addCriterion("PROP1 like", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1NotLike(String value) {
            addCriterion("PROP1 not like", value, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1In(List<String> values) {
            addCriterion("PROP1 in", values, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1NotIn(List<String> values) {
            addCriterion("PROP1 not in", values, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1Between(String value1, String value2) {
            addCriterion("PROP1 between", value1, value2, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp1NotBetween(String value1, String value2) {
            addCriterion("PROP1 not between", value1, value2, "prop1");
            return (Criteria) this;
        }

        public Criteria andProp2IsNull() {
            addCriterion("PROP2 is null");
            return (Criteria) this;
        }

        public Criteria andProp2IsNotNull() {
            addCriterion("PROP2 is not null");
            return (Criteria) this;
        }

        public Criteria andProp2EqualTo(String value) {
            addCriterion("PROP2 =", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2NotEqualTo(String value) {
            addCriterion("PROP2 <>", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2GreaterThan(String value) {
            addCriterion("PROP2 >", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2GreaterThanOrEqualTo(String value) {
            addCriterion("PROP2 >=", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2LessThan(String value) {
            addCriterion("PROP2 <", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2LessThanOrEqualTo(String value) {
            addCriterion("PROP2 <=", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2Like(String value) {
            addCriterion("PROP2 like", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2NotLike(String value) {
            addCriterion("PROP2 not like", value, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2In(List<String> values) {
            addCriterion("PROP2 in", values, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2NotIn(List<String> values) {
            addCriterion("PROP2 not in", values, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2Between(String value1, String value2) {
            addCriterion("PROP2 between", value1, value2, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp2NotBetween(String value1, String value2) {
            addCriterion("PROP2 not between", value1, value2, "prop2");
            return (Criteria) this;
        }

        public Criteria andProp3IsNull() {
            addCriterion("PROP3 is null");
            return (Criteria) this;
        }

        public Criteria andProp3IsNotNull() {
            addCriterion("PROP3 is not null");
            return (Criteria) this;
        }

        public Criteria andProp3EqualTo(String value) {
            addCriterion("PROP3 =", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3NotEqualTo(String value) {
            addCriterion("PROP3 <>", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3GreaterThan(String value) {
            addCriterion("PROP3 >", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3GreaterThanOrEqualTo(String value) {
            addCriterion("PROP3 >=", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3LessThan(String value) {
            addCriterion("PROP3 <", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3LessThanOrEqualTo(String value) {
            addCriterion("PROP3 <=", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3Like(String value) {
            addCriterion("PROP3 like", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3NotLike(String value) {
            addCriterion("PROP3 not like", value, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3In(List<String> values) {
            addCriterion("PROP3 in", values, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3NotIn(List<String> values) {
            addCriterion("PROP3 not in", values, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3Between(String value1, String value2) {
            addCriterion("PROP3 between", value1, value2, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp3NotBetween(String value1, String value2) {
            addCriterion("PROP3 not between", value1, value2, "prop3");
            return (Criteria) this;
        }

        public Criteria andProp4IsNull() {
            addCriterion("PROP4 is null");
            return (Criteria) this;
        }

        public Criteria andProp4IsNotNull() {
            addCriterion("PROP4 is not null");
            return (Criteria) this;
        }

        public Criteria andProp4EqualTo(String value) {
            addCriterion("PROP4 =", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4NotEqualTo(String value) {
            addCriterion("PROP4 <>", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4GreaterThan(String value) {
            addCriterion("PROP4 >", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4GreaterThanOrEqualTo(String value) {
            addCriterion("PROP4 >=", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4LessThan(String value) {
            addCriterion("PROP4 <", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4LessThanOrEqualTo(String value) {
            addCriterion("PROP4 <=", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4Like(String value) {
            addCriterion("PROP4 like", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4NotLike(String value) {
            addCriterion("PROP4 not like", value, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4In(List<String> values) {
            addCriterion("PROP4 in", values, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4NotIn(List<String> values) {
            addCriterion("PROP4 not in", values, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4Between(String value1, String value2) {
            addCriterion("PROP4 between", value1, value2, "prop4");
            return (Criteria) this;
        }

        public Criteria andProp4NotBetween(String value1, String value2) {
            addCriterion("PROP4 not between", value1, value2, "prop4");
            return (Criteria) this;
        }

        public Criteria andAdduserIsNull() {
            addCriterion("ADDUSER is null");
            return (Criteria) this;
        }

        public Criteria andAdduserIsNotNull() {
            addCriterion("ADDUSER is not null");
            return (Criteria) this;
        }

        public Criteria andAdduserEqualTo(String value) {
            addCriterion("ADDUSER =", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserNotEqualTo(String value) {
            addCriterion("ADDUSER <>", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserGreaterThan(String value) {
            addCriterion("ADDUSER >", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserGreaterThanOrEqualTo(String value) {
            addCriterion("ADDUSER >=", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserLessThan(String value) {
            addCriterion("ADDUSER <", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserLessThanOrEqualTo(String value) {
            addCriterion("ADDUSER <=", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserLike(String value) {
            addCriterion("ADDUSER like", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserNotLike(String value) {
            addCriterion("ADDUSER not like", value, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserIn(List<String> values) {
            addCriterion("ADDUSER in", values, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserNotIn(List<String> values) {
            addCriterion("ADDUSER not in", values, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserBetween(String value1, String value2) {
            addCriterion("ADDUSER between", value1, value2, "adduser");
            return (Criteria) this;
        }

        public Criteria andAdduserNotBetween(String value1, String value2) {
            addCriterion("ADDUSER not between", value1, value2, "adduser");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNull() {
            addCriterion("ADDTIME is null");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNotNull() {
            addCriterion("ADDTIME is not null");
            return (Criteria) this;
        }

        public Criteria andAddtimeEqualTo(Timestamp value) {
            addCriterion("ADDTIME =", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotEqualTo(Timestamp value) {
            addCriterion("ADDTIME <>", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThan(Timestamp value) {
            addCriterion("ADDTIME >", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("ADDTIME >=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThan(Timestamp value) {
            addCriterion("ADDTIME <", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("ADDTIME <=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeIn(List<Timestamp> values) {
            addCriterion("ADDTIME in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotIn(List<Timestamp> values) {
            addCriterion("ADDTIME not in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("ADDTIME between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("ADDTIME not between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andModifyuserIsNull() {
            addCriterion("MODIFYUSER is null");
            return (Criteria) this;
        }

        public Criteria andModifyuserIsNotNull() {
            addCriterion("MODIFYUSER is not null");
            return (Criteria) this;
        }

        public Criteria andModifyuserEqualTo(String value) {
            addCriterion("MODIFYUSER =", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserNotEqualTo(String value) {
            addCriterion("MODIFYUSER <>", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserGreaterThan(String value) {
            addCriterion("MODIFYUSER >", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserGreaterThanOrEqualTo(String value) {
            addCriterion("MODIFYUSER >=", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserLessThan(String value) {
            addCriterion("MODIFYUSER <", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserLessThanOrEqualTo(String value) {
            addCriterion("MODIFYUSER <=", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserLike(String value) {
            addCriterion("MODIFYUSER like", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserNotLike(String value) {
            addCriterion("MODIFYUSER not like", value, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserIn(List<String> values) {
            addCriterion("MODIFYUSER in", values, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserNotIn(List<String> values) {
            addCriterion("MODIFYUSER not in", values, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserBetween(String value1, String value2) {
            addCriterion("MODIFYUSER between", value1, value2, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifyuserNotBetween(String value1, String value2) {
            addCriterion("MODIFYUSER not between", value1, value2, "modifyuser");
            return (Criteria) this;
        }

        public Criteria andModifytimeIsNull() {
            addCriterion("MODIFYTIME is null");
            return (Criteria) this;
        }

        public Criteria andModifytimeIsNotNull() {
            addCriterion("MODIFYTIME is not null");
            return (Criteria) this;
        }

        public Criteria andModifytimeEqualTo(Timestamp value) {
            addCriterion("MODIFYTIME =", value, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeNotEqualTo(Timestamp value) {
            addCriterion("MODIFYTIME <>", value, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeGreaterThan(Timestamp value) {
            addCriterion("MODIFYTIME >", value, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("MODIFYTIME >=", value, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeLessThan(Timestamp value) {
            addCriterion("MODIFYTIME <", value, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("MODIFYTIME <=", value, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeIn(List<Timestamp> values) {
            addCriterion("MODIFYTIME in", values, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeNotIn(List<Timestamp> values) {
            addCriterion("MODIFYTIME not in", values, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("MODIFYTIME between", value1, value2, "modifytime");
            return (Criteria) this;
        }

        public Criteria andModifytimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("MODIFYTIME not between", value1, value2, "modifytime");
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