package com.ai.ecp.pmph.busi.seller.staff.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class ShopAcctWithdrawReqVO2 extends EcpBasePageReqVO implements Serializable{
	
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
    private Date withdrawBegDate;

    /**
     * 截止时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date withdrawEndDate;
    
    /**
     * 店铺ID
     */
    private Long withdrawShopId;
    
    /**
     * 提现流水号
     */
    private Long withdrawId;
    
	public Date getWithdrawBegDate() {
		return withdrawBegDate;
	}

	public void setWithdrawBegDate(Date withdrawBegDate) {
		this.withdrawBegDate = withdrawBegDate;
	}

	public Date getWithdrawEndDate() {
		return withdrawEndDate;
	}

	public void setWithdrawEndDate(Date withdrawEndDate) {
		this.withdrawEndDate = withdrawEndDate;
	}

	public Long getWithdrawShopId() {
		return withdrawShopId;
	}

	public void setWithdrawShopId(Long withdrawShopId) {
		this.withdrawShopId = withdrawShopId;
	}

	public Long getWithdrawId() {
		return withdrawId;
	}

	public void setWithdrawId(Long withdrawId) {
		this.withdrawId = withdrawId;
	}

}

