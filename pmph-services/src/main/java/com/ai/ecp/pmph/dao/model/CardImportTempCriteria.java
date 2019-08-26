package com.ai.ecp.pmph.dao.model;

import com.ai.ecp.frame.vo.BaseCriteria;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CardImportTempCriteria extends BaseCriteria implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected String suffix = "";

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public CardImportTempCriteria() {
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

        public Criteria andImportIdIsNull() {
            addCriterion("IMPORT_ID is null");
            return (Criteria) this;
        }

        public Criteria andImportIdIsNotNull() {
            addCriterion("IMPORT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andImportIdEqualTo(Long value) {
            addCriterion("IMPORT_ID =", value, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdNotEqualTo(Long value) {
            addCriterion("IMPORT_ID <>", value, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdGreaterThan(Long value) {
            addCriterion("IMPORT_ID >", value, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdGreaterThanOrEqualTo(Long value) {
            addCriterion("IMPORT_ID >=", value, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdLessThan(Long value) {
            addCriterion("IMPORT_ID <", value, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdLessThanOrEqualTo(Long value) {
            addCriterion("IMPORT_ID <=", value, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdIn(List<Long> values) {
            addCriterion("IMPORT_ID in", values, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdNotIn(List<Long> values) {
            addCriterion("IMPORT_ID not in", values, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdBetween(Long value1, Long value2) {
            addCriterion("IMPORT_ID between", value1, value2, "importId");
            return (Criteria) this;
        }

        public Criteria andImportIdNotBetween(Long value1, Long value2) {
            addCriterion("IMPORT_ID not between", value1, value2, "importId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdIsNull() {
            addCriterion("CUST_CARD_ID is null");
            return (Criteria) this;
        }

        public Criteria andCustCardIdIsNotNull() {
            addCriterion("CUST_CARD_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCustCardIdEqualTo(String value) {
            addCriterion("CUST_CARD_ID =", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdNotEqualTo(String value) {
            addCriterion("CUST_CARD_ID <>", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdGreaterThan(String value) {
            addCriterion("CUST_CARD_ID >", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdGreaterThanOrEqualTo(String value) {
            addCriterion("CUST_CARD_ID >=", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdLessThan(String value) {
            addCriterion("CUST_CARD_ID <", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdLessThanOrEqualTo(String value) {
            addCriterion("CUST_CARD_ID <=", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdLike(String value) {
            addCriterion("CUST_CARD_ID like", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdNotLike(String value) {
            addCriterion("CUST_CARD_ID not like", value, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdIn(List<String> values) {
            addCriterion("CUST_CARD_ID in", values, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdNotIn(List<String> values) {
            addCriterion("CUST_CARD_ID not in", values, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdBetween(String value1, String value2) {
            addCriterion("CUST_CARD_ID between", value1, value2, "custCardId");
            return (Criteria) this;
        }

        public Criteria andCustCardIdNotBetween(String value1, String value2) {
            addCriterion("CUST_CARD_ID not between", value1, value2, "custCardId");
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

        public Criteria andErrorMessIsNull() {
            addCriterion("ERROR_MESS is null");
            return (Criteria) this;
        }

        public Criteria andErrorMessIsNotNull() {
            addCriterion("ERROR_MESS is not null");
            return (Criteria) this;
        }

        public Criteria andErrorMessEqualTo(String value) {
            addCriterion("ERROR_MESS =", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessNotEqualTo(String value) {
            addCriterion("ERROR_MESS <>", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessGreaterThan(String value) {
            addCriterion("ERROR_MESS >", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessGreaterThanOrEqualTo(String value) {
            addCriterion("ERROR_MESS >=", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessLessThan(String value) {
            addCriterion("ERROR_MESS <", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessLessThanOrEqualTo(String value) {
            addCriterion("ERROR_MESS <=", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessLike(String value) {
            addCriterion("ERROR_MESS like", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessNotLike(String value) {
            addCriterion("ERROR_MESS not like", value, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessIn(List<String> values) {
            addCriterion("ERROR_MESS in", values, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessNotIn(List<String> values) {
            addCriterion("ERROR_MESS not in", values, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessBetween(String value1, String value2) {
            addCriterion("ERROR_MESS between", value1, value2, "errorMess");
            return (Criteria) this;
        }

        public Criteria andErrorMessNotBetween(String value1, String value2) {
            addCriterion("ERROR_MESS not between", value1, value2, "errorMess");
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