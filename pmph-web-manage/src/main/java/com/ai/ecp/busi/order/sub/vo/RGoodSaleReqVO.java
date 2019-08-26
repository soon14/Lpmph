package com.ai.ecp.busi.order.sub.vo;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 */
public class RGoodSaleReqVO  extends EcpBasePageReqVO implements Serializable {
    private static final long serialVersionUID = -3299110019834177559L;
    /**
     * 平台分类
     */
    private List<String> categoryCodes;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 商品分类
     */
    private Long gdsType;
    /**
     * 商品名称
     */
    private String gdsName;
    /**
     * ISBN号
     */
    private String isbn;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 会员
     */
    private String staffCode;
    /**
     * 起始日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date begDate;
    /**
     * 截止日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    /**
     * 站点
     */
    private Long siteId;
    /**
     * 店铺Id
     */
    private Long shopId;
    
    /**
     * 支付方式
     */
    private String payType;
    
    /**
     * 排序规则
     */
    private String orderRull;

    public List<String> getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(List<String> categoryCodes) {
        this.categoryCodes = categoryCodes;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGdsName() {
        return gdsName;
    }

    public void setGdsName(String gdsName) {
        this.gdsName = gdsName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Date getBegDate() {
        return begDate;
    }

    public void setBegDate(Date begDate) {
        this.begDate = begDate;
    }

    public Date getEndDate() {
       return DateUtils.addDays(endDate, 1);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getGdsType() {
        return gdsType;
    }

    public void setGdsType(Long gdsType) {
        this.gdsType = gdsType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getOrderRull() {
		return orderRull;
	}

	public void setOrderRull(String orderRull) {
		this.orderRull = orderRull;
	}
	
}
