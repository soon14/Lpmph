package com.ai.ecp.busi.order.vo;

import java.io.Serializable;

public class CoupCodeReqVo implements Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 7849406411026476579L;
    private String coupCode;
    private Long shopId;
    private String sourceKey;
    private String mainHashKey;
    private String shopIdList;
    public String getShopIdList() {
        return shopIdList;
    }
    public void setShopIdList(String shopIdList) {
        this.shopIdList = shopIdList;
    }
    public String getCoupCode() {
        return coupCode;
    }
    public Long getShopId() {
        return shopId;
    }
    public String getSourceKey() {
        return sourceKey;
    }
    public void setCoupCode(String coupCode) {
        this.coupCode = coupCode;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }
    public String getMainHashKey() {
        return mainHashKey;
    }
    public void setMainHashKey(String mainHashKey) {
        this.mainHashKey = mainHashKey;
    }
}

