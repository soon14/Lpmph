package com.ai.ecp.pmph.busi.pub.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: 征订单线下支付审核VO<br>
 * Date:2018年8月8日下午5:34:53  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
public class PubOrdOffReviewReqVO extends EcpBasePageReqVO implements Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 1L;
    
    /**
     * 征订单编号
     */
    private String pubOrderId;
    
    /**
     * 审核意见
     */
    @NotNull(message="备注不能为空!")
    private String checkCont;
    
    public String getPubOrderId() {
        return pubOrderId;
    }

    public void setPubOrderId(String pubOrderId) {
        this.pubOrderId = pubOrderId;
    }

    public String getCheckCont() {
        return checkCont;
    }

    public void setCheckCont(String checkCont) {
        this.checkCont = checkCont;
    }
}

