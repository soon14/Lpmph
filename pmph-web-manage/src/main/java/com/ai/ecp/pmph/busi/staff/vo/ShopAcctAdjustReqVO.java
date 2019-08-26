package com.ai.ecp.pmph.busi.staff.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class ShopAcctAdjustReqVO extends EcpBasePageReqVO implements Serializable{
	
	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = 1L;

	/**
     * 开始时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date adjBegDate;

    /**
     * 截止时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date adjEndDate;
    
    /**
     * 店铺ID
     */
    private Long adjShopId;
    
    /**
     * 调账类型 31:调账收入 32:调账支出
     */
    private String adjOptType;
    
    /**
     * 调账流水号
     */
    private Long adjDetailId;

	public Date getAdjBegDate() {
		return adjBegDate;
	}

	public void setAdjBegDate(Date adjBegDate) {
		this.adjBegDate = adjBegDate;
	}

	public Date getAdjEndDate() {
		return adjEndDate;
	}

	public void setAdjEndDate(Date adjEndDate) {
		this.adjEndDate = adjEndDate;
	}

	public Long getAdjShopId() {
		return adjShopId;
	}

	public void setAdjShopId(Long adjShopId) {
		this.adjShopId = adjShopId;
	}

	public String getAdjOptType() {
		return adjOptType;
	}

	public void setAdjOptType(String adjOptType) {
		this.adjOptType = adjOptType;
	}

	public Long getAdjDetailId() {
		return adjDetailId;
	}

	public void setAdjDetailId(Long adjDetailId) {
		this.adjDetailId = adjDetailId;
	}

}

