package com.ai.ecp.pmph.dubbo.dto;

import java.io.Serializable;

import com.ai.ecp.server.front.dto.BaseInfo;

public class ROrdReturnRefundReq extends BaseInfo implements Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -5688245127918055099L;

    private String orderId;//订单编号
    
    private long scale; //比例
    private long backMoney; //退款金额
    private long modifyBackMoney;//一级审核调整的退款金额
    private long useScore;//下单时使用积分
    private long reduCulateScore;//订单退款退货时需退回的总积分 
    private long curScore;//用户当前剩余积分      
    private long backId;//退款退货遍号
    private long staffId;//买家编号
    private long createStaff;//当前操作人
    private boolean isLastFlag;//是否最后一笔
    private String memo;
    
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
    public long getCreateStaff() {
        return createStaff;
    }
    public void setCreateStaff(long createStaff) {
        this.createStaff = createStaff;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
}

