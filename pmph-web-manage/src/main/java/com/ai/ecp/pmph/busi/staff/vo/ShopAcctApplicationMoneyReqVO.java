package com.ai.ecp.pmph.busi.staff.vo;

import java.io.Serializable;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: 提现申请VO<br>
 * Date:2018年5月24日上午9:41:41  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author myf
 * @version  
 * @since JDK 1.7
 */
public class ShopAcctApplicationMoneyReqVO extends EcpBasePageReqVO implements Serializable{
    
    /**
     * 店铺ID
     */
    private Long shopId;
    
    /**
     * 提现申请编码
     */
    private Long applyId;
    
    /**
     * 店铺全称
     */
    private String shopFullName;
    
    /**
     * 提现前账户总额
     */
    private Long prevAcctTotal;
    
    /**
     * 提现后账户总额=提现前账户总额-申请提现金额
     */
    private Long alreadyAcctTotal;
    
    /**
     * 申请提现金额
     */
    private Long applicationMoney;
    
    private static final long serialVersionUID = 1L;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public String getShopFullName() {
        return shopFullName;
    }

    public void setShopFullName(String shopFullName) {
        this.shopFullName = shopFullName;
    }
    
    public Long getPrevAcctTotal() {
        return prevAcctTotal;
    }

    public void setPrevAcctTotal(Long prevAcctTotal) {
        this.prevAcctTotal = prevAcctTotal;
    }

    public Long getAlreadyAcctTotal() {
        return alreadyAcctTotal;
    }

    public void setAlreadyAcctTotal(Long alreadyAcctTotal) {
        this.alreadyAcctTotal = alreadyAcctTotal;
    }

    public Long getApplicationMoney() {
        return applicationMoney;
    }

    public void setApplicationMoney(Long applicationMoney) {
        this.applicationMoney = applicationMoney;
    }

}

