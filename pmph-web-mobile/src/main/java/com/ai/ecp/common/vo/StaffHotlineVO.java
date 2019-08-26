package com.ai.ecp.common.vo;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class StaffHotlineVO extends EcpBasePageReqVO {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = 1L;

	private String csaCode;
	
	private String ofStaffCode;
	
	private Long shopId;
	
	private Long staffId;
	
	private String sessionId;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getCsaCode() {
		return csaCode;
	}

	public void setCsaCode(String csaCode) {
		this.csaCode = csaCode;
	}

	public String getOfStaffCode() {
		return ofStaffCode;
	}

	public void setOfStaffCode(String ofStaffCode) {
		this.ofStaffCode = ofStaffCode;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
