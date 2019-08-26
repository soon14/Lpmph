package com.ai.ecp.pmph.busi.staff.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class ShopAcctIncomeReqVO extends EcpBasePageReqVO implements Serializable{
	
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
    private Date payBegDate;

    /**
     * 截止时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date payEndDate;
    
    /**
     * 店铺ID
     */
    private Long inShopId;

    /**
     * 订单编号
     */
    private String inOrderId;
    
    /**
     * 商户订单号
     */
    private String inPayTranNo;
    
    /**
     * 下单人
     */
    private String orderStaffCode;
    
    /**
     * 收入流水号
     */
    private Long incomeId;
    
    /**
     * 支付通道代码
     */
    private String inPayWay;

    /**
     * 0为在线支付，1为到店支付，2为邮局汇款 3为银行转账
     */
    private String inPayType;

	public Date getPayBegDate() {
		return payBegDate;
	}

	public void setPayBegDate(Date payBegDate) {
		this.payBegDate = payBegDate;
	}

	public Date getPayEndDate() {
		return payEndDate;
	}

	public void setPayEndDate(Date payEndDate) {
		this.payEndDate = payEndDate;
	}

	public Long getInShopId() {
		return inShopId;
	}

	public void setInShopId(Long inShopId) {
		this.inShopId = inShopId;
	}

	public String getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(String inOrderId) {
		this.inOrderId = inOrderId;
	}

	public String getInPayTranNo() {
		return inPayTranNo;
	}

	public void setInPayTranNo(String inPayTranNo) {
		this.inPayTranNo = inPayTranNo;
	}

	public String getOrderStaffCode() {
		return orderStaffCode;
	}

	public void setOrderStaffCode(String orderStaffCode) {
		this.orderStaffCode = orderStaffCode;
	}

	public Long getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(Long incomeId) {
		this.incomeId = incomeId;
	}

	public String getInPayWay() {
		return inPayWay;
	}

	public void setInPayWay(String inPayWay) {
		this.inPayWay = inPayWay;
	}

	public String getInPayType() {
		return inPayType;
	}

	public void setInPayType(String inPayType) {
		this.inPayType = inPayType;
	}

}

