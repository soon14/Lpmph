package com.ai.ecp.pmph.dubbo.dto;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.RBackCouponResp;
import com.ai.ecp.order.dubbo.dto.RBackDiscountResp;

public class ROrdReturnRefundResp implements java.io.Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 3414453693452618641L;
    
    private String orderId;//订单编号
    private long scale; //比例
    private long num;//退货数量
    private long backMoney; //退款金额
    private long modifyBackMoney;//一级审核调整的退款金额
    private long useScore;//下单时使用积分
    private long reduCulateScore;//订单退款退货时需退回的总积分 
    private long curScore;//用户当前剩余积分   
    private long staffId;//买家编号
    private boolean isLastFlag;//是否最后一笔
   
  
    /** 
     * rBackDiscountResps:资金账户、积分相关信息. 
     * @since JDK 1.6 
     */ 
    private List<RBackDiscountResp> rBackDiscountResps;    
    /** 
     * rBackCouponResps:优惠卷相关信息. 
     * @since JDK 1.6 
     */ 
    private List<RBackCouponResp> rBackCouponResps;   
    
    
    
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
    public List<RBackDiscountResp> getrBackDiscountResps() {
        return rBackDiscountResps;
    }
    public void setrBackDiscountResps(List<RBackDiscountResp> rBackDiscountResps) {
        this.rBackDiscountResps = rBackDiscountResps;
    }
    public List<RBackCouponResp> getrBackCouponResps() {
        return rBackCouponResps;
    }
    public void setrBackCouponResps(List<RBackCouponResp> rBackCouponResps) {
        this.rBackCouponResps = rBackCouponResps;
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
    public long getModifyBackMoney() {
        return modifyBackMoney;
    }
    public void setModifyBackMoney(long modifyBackMoney) {
        this.modifyBackMoney = modifyBackMoney;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public long getCurScore() {
        return curScore;
    }
    public void setCurScore(long curScore) {
        this.curScore = curScore;
    }
    public long getNum() {
        return num;
    }
    public void setNum(long num) {
        this.num = num;
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

