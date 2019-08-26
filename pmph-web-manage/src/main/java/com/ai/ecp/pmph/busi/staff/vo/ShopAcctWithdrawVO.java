package com.ai.ecp.pmph.busi.staff.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;
/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: 提现一级、二级审核 VO<br>
 * Date:2018年6月4日下午2:04:25  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
public class ShopAcctWithdrawVO extends EcpBasePageReqVO {
	
	/** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.7
     */ 
    private static final long serialVersionUID = 1L;

    /** 
     * shopName:店铺名称. 
     */ 
	private String shopName;
	
	/** 
     * shopId:店铺ID. 
     */ 
    private Long shopId;
	
    /** 
     * begDate:开始时间. 
     */ 
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date begDate;

    /** 
     * endDate:截止时间. 
     */ 
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
	
    /** 
     * tabFlag:状态标志. 00:一级待审核  01：二级待审核
     */ 
    private String tabFlag;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public String getTabFlag() {
        return tabFlag;
    }

    public void setTabFlag(String tabFlag) {
        this.tabFlag = tabFlag;
    }

    @Override
    public String toString() {
        return "ShopAcctWithdrawVO [shopName=" + shopName + ", shopId=" + shopId + ", begDate="
                + begDate + ", endDate=" + endDate + ", tabFlag=" + tabFlag + "]";
    }
}
