package com.ai.ecp.pmph.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseInfo;

public class OrdCompensateReq extends BaseInfo {

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 5431823921081646874L;
    
    private String orderId;
    private long backMoney;
    private String memo;
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public long getBackMoney() {
        return backMoney;
    }
    public void setBackMoney(long backMoney) {
        this.backMoney = backMoney;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
}

