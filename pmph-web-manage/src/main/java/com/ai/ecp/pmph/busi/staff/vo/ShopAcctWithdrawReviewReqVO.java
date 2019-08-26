package com.ai.ecp.pmph.busi.staff.vo;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: 提现申请确认审核通过或拒绝提现申请VO<br>
 * Date:2018年6月5日下午5:39:06  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
public class ShopAcctWithdrawReviewReqVO extends EcpBasePageReqVO {
	 
    private static final long serialVersionUID = 1L;
    
    /**
     * 提现申请店铺ID
     */
    private Long shopId;
	
    /**
     * 提现申请ID
     */
	private Long applyId;
	
	/**
	 * 审核前该审核单的状态
	 */
	private String status;
	
	/**
	 * 审核人备注信息
	 */
	private String checkDesc;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckDesc() {
        return checkDesc;
    }

    public void setCheckDesc(String checkDesc) {
        this.checkDesc = checkDesc;
    }

    @Override
    public String toString() {
        return "ShopAcctWithdrawReviewReqVO [shopId=" + shopId + ", applyId=" + applyId
                + ", status=" + status + ", checkDesc=" + checkDesc + "]";
    }
}
