package com.ai.ecp.pmph.busi.staff.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class ShopAcctExpendReqVO extends EcpBasePageReqVO implements Serializable{
	
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
    private Date backBegDate;

    /**
     * 截止时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date backEndDate;
    
    /**
     * 店铺ID
     */
    private Long backShopId;

    /**
     * 订单编号
     */
    private String backOrderId;
    
    /**
     * 商户订单号
     */
    private String backPayTranNo;
    
    /**
     * 退款人
     */
    private String applyStaffCode;
    
    /**
     * 支出流水号
     */
    private Long backDetailId;
    
    /**
     * 退款通道代码
     */
    private String backWay;

    /**
     * 1为线上退款  0为线下退款
     */
    private String backType;
    
    /**
     * 支出类型 21:退款支出  22:退货支出
     */
    private String backOptType;

	public Date getBackBegDate() {
		return backBegDate;
	}

	public void setBackBegDate(Date backBegDate) {
		this.backBegDate = backBegDate;
	}

	public Date getBackEndDate() {
		return backEndDate;
	}

	public void setBackEndDate(Date backEndDate) {
		this.backEndDate = backEndDate;
	}

	public Long getBackShopId() {
		return backShopId;
	}

	public void setBackShopId(Long backShopId) {
		this.backShopId = backShopId;
	}

	public String getBackOrderId() {
		return backOrderId;
	}

	public void setBackOrderId(String backOrderId) {
		this.backOrderId = backOrderId;
	}

	public String getBackPayTranNo() {
		return backPayTranNo;
	}

	public void setBackPayTranNo(String backPayTranNo) {
		this.backPayTranNo = backPayTranNo;
	}

	public String getApplyStaffCode() {
		return applyStaffCode;
	}

	public void setApplyStaffCode(String applyStaffCode) {
		this.applyStaffCode = applyStaffCode;
	}

	public Long getBackDetailId() {
		return backDetailId;
	}

	public void setBackDetailId(Long backDetailId) {
		this.backDetailId = backDetailId;
	}

	public String getBackWay() {
		return backWay;
	}

	public void setBackWay(String backWay) {
		this.backWay = backWay;
	}

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public String getBackOptType() {
		return backOptType;
	}

	public void setBackOptType(String backOptType) {
		this.backOptType = backOptType;
	}
    
}

