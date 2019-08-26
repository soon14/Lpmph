package com.ai.ecp.pmph.busi.seller.staff.vo;

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
    private Date begDate;

    /**
     * 截止时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    
    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 订单编号
     */
    private String orderId;
    
    /**
     * 商户订单号
     */
    private String payTranNo;
    
    /**
     * 下单人
     */
    private String orderStaffCode;
    
    /**
     * 收入流水号
     */
    private Long id;
    
    /**
     * 支付通道代码
     */
    private String payWay;

    /**
     * 0为在线支付，1为到店支付，2为邮局汇款 3为银行转账
     */
    private String payType;

	public Date getBegDate() {
		return begDate;
	}

	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayTranNo() {
		return payTranNo;
	}

	public void setPayTranNo(String payTranNo) {
		this.payTranNo = payTranNo;
	}

	public String getOrderStaffCode() {
		return orderStaffCode;
	}

	public void setOrderStaffCode(String orderStaffCode) {
		this.orderStaffCode = orderStaffCode;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
}

