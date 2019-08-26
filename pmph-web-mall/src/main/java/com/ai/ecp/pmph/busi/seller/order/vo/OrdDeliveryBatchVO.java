package com.ai.ecp.pmph.busi.seller.order.vo;

import java.io.Serializable;
/**
 * Title: SHOP <br>
 * Project Name:pmph-web-mall <br>
 * Description: 修改物流信息VO<br>
 * Date:2018年6月22日下午4:04:17  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
public class OrdDeliveryBatchVO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private Long id;
    
    private String deliveryType;
    
    private Long expressId;
    
    private String expressNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getExpressId() {
        return expressId;
    }

    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    } 
}