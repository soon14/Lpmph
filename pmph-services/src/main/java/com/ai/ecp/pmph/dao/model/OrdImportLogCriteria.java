package com.ai.ecp.pmph.dao.model;

import com.ai.ecp.frame.vo.BaseCriteria;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrdImportLogCriteria extends BaseCriteria implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected String suffix = "";

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public OrdImportLogCriteria() {
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

        public Criteria andImportTypeIsNull() {
            addCriterion("IMPORT_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andImportTypeIsNotNull() {
            addCriterion("IMPORT_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andImportTypeEqualTo(String value) {
            addCriterion("IMPORT_TYPE =", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotEqualTo(String value) {
            addCriterion("IMPORT_TYPE <>", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeGreaterThan(String value) {
            addCriterion("IMPORT_TYPE >", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeGreaterThanOrEqualTo(String value) {
            addCriterion("IMPORT_TYPE >=", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeLessThan(String value) {
            addCriterion("IMPORT_TYPE <", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeLessThanOrEqualTo(String value) {
            addCriterion("IMPORT_TYPE <=", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeLike(String value) {
            addCriterion("IMPORT_TYPE like", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotLike(String value) {
            addCriterion("IMPORT_TYPE not like", value, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeIn(List<String> values) {
            addCriterion("IMPORT_TYPE in", values, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotIn(List<String> values) {
            addCriterion("IMPORT_TYPE not in", values, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeBetween(String value1, String value2) {
            addCriterion("IMPORT_TYPE between", value1, value2, "importType");
            return (Criteria) this;
        }

        public Criteria andImportTypeNotBetween(String value1, String value2) {
            addCriterion("IMPORT_TYPE not between", value1, value2, "importType");
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

        public Criteria andOldOrderIdIsNull() {
            addCriterion("OLD_ORDER_ID is null");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdIsNotNull() {
            addCriterion("OLD_ORDER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdEqualTo(String value) {
            addCriterion("OLD_ORDER_ID =", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdNotEqualTo(String value) {
            addCriterion("OLD_ORDER_ID <>", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdGreaterThan(String value) {
            addCriterion("OLD_ORDER_ID >", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("OLD_ORDER_ID >=", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdLessThan(String value) {
            addCriterion("OLD_ORDER_ID <", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdLessThanOrEqualTo(String value) {
            addCriterion("OLD_ORDER_ID <=", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdLike(String value) {
            addCriterion("OLD_ORDER_ID like", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdNotLike(String value) {
            addCriterion("OLD_ORDER_ID not like", value, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdIn(List<String> values) {
            addCriterion("OLD_ORDER_ID in", values, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdNotIn(List<String> values) {
            addCriterion("OLD_ORDER_ID not in", values, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdBetween(String value1, String value2) {
            addCriterion("OLD_ORDER_ID between", value1, value2, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andOldOrderIdNotBetween(String value1, String value2) {
            addCriterion("OLD_ORDER_ID not between", value1, value2, "oldOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdIsNull() {
            addCriterion("NEW_ORDER_ID is null");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdIsNotNull() {
            addCriterion("NEW_ORDER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdEqualTo(String value) {
            addCriterion("NEW_ORDER_ID =", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdNotEqualTo(String value) {
            addCriterion("NEW_ORDER_ID <>", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdGreaterThan(String value) {
            addCriterion("NEW_ORDER_ID >", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("NEW_ORDER_ID >=", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdLessThan(String value) {
            addCriterion("NEW_ORDER_ID <", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdLessThanOrEqualTo(String value) {
            addCriterion("NEW_ORDER_ID <=", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdLike(String value) {
            addCriterion("NEW_ORDER_ID like", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdNotLike(String value) {
            addCriterion("NEW_ORDER_ID not like", value, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdIn(List<String> values) {
            addCriterion("NEW_ORDER_ID in", values, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdNotIn(List<String> values) {
            addCriterion("NEW_ORDER_ID not in", values, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdBetween(String value1, String value2) {
            addCriterion("NEW_ORDER_ID between", value1, value2, "newOrderId");
            return (Criteria) this;
        }

        public Criteria andNewOrderIdNotBetween(String value1, String value2) {
            addCriterion("NEW_ORDER_ID not between", value1, value2, "newOrderId");
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

        public Criteria andNewStaffIdIsNull() {
            addCriterion("NEW_STAFF_ID is null");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdIsNotNull() {
            addCriterion("NEW_STAFF_ID is not null");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdEqualTo(Long value) {
            addCriterion("NEW_STAFF_ID =", value, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdNotEqualTo(Long value) {
            addCriterion("NEW_STAFF_ID <>", value, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdGreaterThan(Long value) {
            addCriterion("NEW_STAFF_ID >", value, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdGreaterThanOrEqualTo(Long value) {
            addCriterion("NEW_STAFF_ID >=", value, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdLessThan(Long value) {
            addCriterion("NEW_STAFF_ID <", value, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdLessThanOrEqualTo(Long value) {
            addCriterion("NEW_STAFF_ID <=", value, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdIn(List<Long> values) {
            addCriterion("NEW_STAFF_ID in", values, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdNotIn(List<Long> values) {
            addCriterion("NEW_STAFF_ID not in", values, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdBetween(Long value1, Long value2) {
            addCriterion("NEW_STAFF_ID between", value1, value2, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andNewStaffIdNotBetween(Long value1, Long value2) {
            addCriterion("NEW_STAFF_ID not between", value1, value2, "newStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdIsNull() {
            addCriterion("OLD_STAFF_ID is null");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdIsNotNull() {
            addCriterion("OLD_STAFF_ID is not null");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdEqualTo(String value) {
            addCriterion("OLD_STAFF_ID =", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdNotEqualTo(String value) {
            addCriterion("OLD_STAFF_ID <>", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdGreaterThan(String value) {
            addCriterion("OLD_STAFF_ID >", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdGreaterThanOrEqualTo(String value) {
            addCriterion("OLD_STAFF_ID >=", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdLessThan(String value) {
            addCriterion("OLD_STAFF_ID <", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdLessThanOrEqualTo(String value) {
            addCriterion("OLD_STAFF_ID <=", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdLike(String value) {
            addCriterion("OLD_STAFF_ID like", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdNotLike(String value) {
            addCriterion("OLD_STAFF_ID not like", value, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdIn(List<String> values) {
            addCriterion("OLD_STAFF_ID in", values, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdNotIn(List<String> values) {
            addCriterion("OLD_STAFF_ID not in", values, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdBetween(String value1, String value2) {
            addCriterion("OLD_STAFF_ID between", value1, value2, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andOldStaffIdNotBetween(String value1, String value2) {
            addCriterion("OLD_STAFF_ID not between", value1, value2, "oldStaffId");
            return (Criteria) this;
        }

        public Criteria andFileIdIsNull() {
            addCriterion("FILE_ID is null");
            return (Criteria) this;
        }

        public Criteria andFileIdIsNotNull() {
            addCriterion("FILE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andFileIdEqualTo(String value) {
            addCriterion("FILE_ID =", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotEqualTo(String value) {
            addCriterion("FILE_ID <>", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThan(String value) {
            addCriterion("FILE_ID >", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThanOrEqualTo(String value) {
            addCriterion("FILE_ID >=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThan(String value) {
            addCriterion("FILE_ID <", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThanOrEqualTo(String value) {
            addCriterion("FILE_ID <=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLike(String value) {
            addCriterion("FILE_ID like", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotLike(String value) {
            addCriterion("FILE_ID not like", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdIn(List<String> values) {
            addCriterion("FILE_ID in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotIn(List<String> values) {
            addCriterion("FILE_ID not in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdBetween(String value1, String value2) {
            addCriterion("FILE_ID between", value1, value2, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotBetween(String value1, String value2) {
            addCriterion("FILE_ID not between", value1, value2, "fileId");
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