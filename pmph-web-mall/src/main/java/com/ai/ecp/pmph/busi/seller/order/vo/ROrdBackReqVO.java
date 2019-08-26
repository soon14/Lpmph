package com.ai.ecp.pmph.busi.seller.order.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class ROrdBackReqVO extends EcpBasePageReqVO {

	/** 
	 * serialVersionUID:. 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = -1025452233121412401L;
	
    /** 
     * begDate:开始时间. 
     * @since JDK 1.6 
     */ 
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="{order.orderdate.null.error}")
    private Date begDate;

    /** 
     * endDate:截止时间. 
     * @since JDK 1.6 
     */ 
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="{order.orderdate.null.error}")
    private Date endDate;
    
    /** 
     * orderId:订单编号. 
     * @since JDK 1.6 
     */ 
    @NotNull(message="{order.orderid.null.error}")
    private String orderId;
    
    /** 
     * siteId:站点信息 . 
     * @since JDK 1.6 
     */ 
    private Long siteId;
    
    /** 
     * shopId:卖家ID. 
     * @since JDK 1.6 
     */ 
    private Long shopId;
    
    /** 
     * tabFlag:状态标志. 
     * @since JDK 1.6 
     */ 
    private String tabFlag;
    
    /** 
     * status:申请单状态. 
     * @since JDK 1.6 
     */ 
    private String status;
    
    /**
     * isCompensate:是否补偿性退款
     */
    private String isCompensate;

    /** 
     * payTranNo:商户订单号. 
     * @since JDK 1.6 
     */
    private String payTranNo;
    /** 
     * payWay:支付通道. 
     * @since JDK 1.6 
     */ 
    private String payWay;
    
    public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
    public String getPayTranNo() {
    	return payTranNo;
    }
    
    public void setPayTranNo(String payTranNo) {
    	this.payTranNo = payTranNo;
    }
    public String getIsCompensate() {
		return isCompensate;
	}

	public void setIsCompensate(String isCompensate) {
		this.isCompensate = isCompensate;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getTabFlag() {
        return tabFlag;
    }

    public void setTabFlag(String tabFlag) {
        this.tabFlag = tabFlag;
    }

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

}
