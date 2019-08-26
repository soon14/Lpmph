package com.ai.ecp.common.vo;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.JSON;

public class CustServSatisfyReqVO extends EcpBasePageReqVO {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.7
	 */ 
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long shopId;

    private String ofStaffCode;

    private String csaCode;

    private String sessionId;

    private String satisfyType;

    private String notSatisfyType;

    private String notSatisfyReason;

    private Long beginTime;

    private Long endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getOfStaffCode() {
        return ofStaffCode;
    }

    public void setOfStaffCode(String ofStaffCode) {
        this.ofStaffCode = ofStaffCode;
    }

    public String getCsaCode() {
        return csaCode;
    }

    public void setCsaCode(String csaCode) {
        this.csaCode = csaCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSatisfyType() {
        return satisfyType;
    }

    public void setSatisfyType(String satisfyType) {
        this.satisfyType = satisfyType;
    }

    public String getNotSatisfyType() {
        return notSatisfyType;
    }

    public void setNotSatisfyType(String notSatisfyType) {
        this.notSatisfyType = notSatisfyType;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getNotSatisfyReason() {
        return notSatisfyReason;
    }

    public void setNotSatisfyReason(String notSatisfyReason) {
        this.notSatisfyReason = notSatisfyReason;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }  
}
