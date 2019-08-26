package com.ai.ecp.common.vo;

import java.sql.Timestamp;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class SessionReqVO extends EcpBasePageReqVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String csaCode;
	
	private String userCode;
	
	private Long shopId;
	
	private String gdsId;
	
	private String ordId;
	
	private String issueType;
	
	private String status;
	
	private Timestamp sessionTime;
	
	private String source; //会话来源

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCsaCode() {
		return csaCode;
	}

	public void setCsaCode(String csaCode) {
		this.csaCode = csaCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getGdsId() {
		return gdsId;
	}

	public void setGdsId(String gdsId) {
		this.gdsId = gdsId;
	}

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(Timestamp sessionTime) {
		this.sessionTime = sessionTime;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
}
