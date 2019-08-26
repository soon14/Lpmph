package com.ai.ecp.common.vo;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class CustInfoReqVO extends EcpBasePageReqVO {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.7
	 */ 
	private static final long serialVersionUID = 6089851314763993784L;
	
	    private String staffCode;
	    
	    private Long id;

	    private String custCode;

	    private Integer custType;

	    private String custCardId;

	    private Long custGrowValue;

	    private String custLevelCode;

	    private String goodsId;

	    private String orderId;

	    private Long shopId;

	    private Long businessType;

	    private Long reqTime;

	    private String ofStaffCode;

	    private String custLevel;

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public Integer getCustType() {
		return custType;
	}

	public void setCustType(Integer custType) {
		this.custType = custType;
	}

	public String getCustCardId() {
		return custCardId;
	}

	public void setCustCardId(String custCardId) {
		this.custCardId = custCardId;
	}

	public Long getCustGrowValue() {
		return custGrowValue;
	}

	public void setCustGrowValue(Long custGrowValue) {
		this.custGrowValue = custGrowValue;
	}

	public String getCustLevelCode() {
		return custLevelCode;
	}

	public void setCustLevelCode(String custLevelCode) {
		this.custLevelCode = custLevelCode;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Long businessType) {
		this.businessType = businessType;
	}

	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}

	public String getOfStaffCode() {
		return ofStaffCode;
	}

	public void setOfStaffCode(String ofStaffCode) {
		this.ofStaffCode = ofStaffCode;
	}

	public String getCustLevel() {
		return custLevel;
	}

	public void setCustLevel(String custLevel) {
		this.custLevel = custLevel;
	}
	
	
	
}
