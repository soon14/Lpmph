package com.ai.ecp.pmph.busi.pub.vo;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class PubAllOrdVO extends EcpBasePageReqVO {

    /** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.7 
	 */ 
	private static final long serialVersionUID = 1L;

	/**
	 * 开始日期
	 */
	private Date begDate;

	/**
	 * 结束日期
	 */
    private Date endDate;

    /**
     * 公众号会员Id
     */
    private Long staffId;

    /**
     * 征订单状态    01：待支付   02：待发货  04：部分发货  05：全部发货  99：取消
     */
    private String pubStatus;

    /**
     * 征订单号
     */
    private String pubOrderId;

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

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getPubStatus() {
		return pubStatus;
	}

	public void setPubStatus(String pubStatus) {
		this.pubStatus = pubStatus;
	}

	public String getPubOrderId() {
		return pubOrderId;
	}

	public void setPubOrderId(String pubOrderId) {
		this.pubOrderId = pubOrderId;
	}
    
}

