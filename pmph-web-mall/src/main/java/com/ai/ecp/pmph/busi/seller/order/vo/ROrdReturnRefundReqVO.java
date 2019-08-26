package com.ai.ecp.pmph.busi.seller.order.vo;

import java.io.Serializable;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class ROrdReturnRefundReqVO extends EcpBasePageReqVO implements Serializable{
    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 6363428694292005636L;

    private String orderId;//订单编号   
    private long scale; //比例
    private long backMoney; //退款金额
    private long modifyBackMoney;//一级审核调整的退款金额
    private long useScore;//下单时使用积分
    private long reduCulateScore;//订单退款退货时需退回的总积分 
    private long curScore;//用户当前剩余积分      
    private long backId;//退款退货遍号
    private String memo;//调整退款金额原因
    private long staffId;
    private boolean isLastFlag;
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public long getScale() {
        return scale;
    }
    public void setScale(long scale) {
        this.scale = scale;
    }
    public long getBackMoney() {
        return backMoney;
    }
    public void setBackMoney(long backMoney) {
        this.backMoney = backMoney;
    }
    public long getModifyBackMoney() {
        return modifyBackMoney;
    }
    public void setModifyBackMoney(long modifyBackMoney) {
        this.modifyBackMoney = modifyBackMoney;
    }
    public long getUseScore() {
        return useScore;
    }
    public void setUseScore(long useScore) {
        this.useScore = useScore;
    }
    public long getReduCulateScore() {
        return reduCulateScore;
    }
    public void setReduCulateScore(long reduCulateScore) {
        this.reduCulateScore = reduCulateScore;
    }
    public long getCurScore() {
        return curScore;
    }
    public void setCurScore(long curScore) {
        this.curScore = curScore;
    }
    public long getBackId() {
        return backId;
    }
    public void setBackId(long backId) {
        this.backId = backId;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public long getStaffId() {
        return staffId;
    }
    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }
    public boolean isLastFlag() {
        return isLastFlag;
    }
    public void setLastFlag(boolean isLastFlag) {
        this.isLastFlag = isLastFlag;
    }
}

