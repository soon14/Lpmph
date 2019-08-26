package com.ai.ecp.pmph.busi.staff.vo;

import java.io.Serializable;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class ShopAcctBillMonthVO extends EcpBasePageReqVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

    /**
     * 店铺名称
     */
    private String shopName;
	
	/**
     * 结算开始月份
     */
    private String begDate;

    /**
     * 结算截止月份
     */
    private String endDate;
    
    /**
     * 店铺ID
     */
    private Long shopId;
    
    /**
     * 结算月
     */
    private Integer billMonth;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBegDate() {
        return begDate;
    }

    public void setBegDate(String begDate) {
        this.begDate = begDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(Integer billMonth) {
        this.billMonth = billMonth;
    }

    @Override
    public String toString() {
        return "ShopAcctBillMonthVO [shopName=" + shopName + ", begDate=" + begDate + ", endDate="
                + endDate + ", shopId=" + shopId + ", billMonth=" + billMonth + "]";
    }
}

