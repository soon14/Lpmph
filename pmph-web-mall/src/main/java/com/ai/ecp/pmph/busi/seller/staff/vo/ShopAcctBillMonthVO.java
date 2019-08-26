package com.ai.ecp.pmph.busi.seller.staff.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class ShopAcctBillMonthVO extends EcpBasePageReqVO implements Serializable{

	/**
     * 结算月
     */
    private Integer billMonth;

    /**
     * 店铺ID
     */
    private Long shopId;
	
    /**
     * 账户ID
     */
    private Long acctId;

    /**
     * 月结商品码洋
     */
    private Long monBasicMoney;

    /**
     * 月结商品实洋
     */
    private Long monGdsMoney;

    /**
     * 月结实际运费
     */
    private Long monExpFee;

    /**
     * 月结订单实付
     */
    private Long monRealMoney;

    /**
     * 月结订单支付费用
     */
    private Long monPayFee;

    /**
     * 月结订单服务费用
     */
    private Long monServFee;

    /**
     * 月结订单手续费
     */
    private Long monInFee;

    /**
     * 月结订单收入
     */
    private Long inMoney;

    /**
     * 月结退款总额
     */
    private Long monBackMoney;

    /**
     * 月结退款支付费用
     */
    private Long monBackPayfee;

    /**
     * 月结退款服务费用
     */
    private Long monBackServfee;

    /**
     * 月结退款费用
     */
    private Long monBackBackfee;

    /**
     * 月结退款手续费
     */
    private Long monBackFee;

    /**
     * 月结退货退款支出
     */
    private Long backMoney;

    /**
     * 月结调账
     */
    private Long adjMoney;

    /**
     * 月结入账=月结订单收入+月结退货退款支出+月结调账
     */
    private Long money;

    /**
     * 月结提现
     */
    private Long withdrawMoney;

    /**
     * 统计时间
     */
    private Timestamp calTime;

    /**
     * 可提现日期
     */
    private Timestamp canWithdrawTime;

    /**
     * 状态：0：未对账 1：未提现  2：已提现
     */
    private String status;

    /**
     * 创建人
     */
    private Long createStaff;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新人
     */
    private Long updateStaff;

    /**
     * 更新时间
     */
    private Timestamp updateTime;
    
    /**
     * 系统当前时间：用于判断是否已到可提现时间
     */
    private Timestamp systemTime;
    
    private static final long serialVersionUID = 1L;
	
	public Timestamp getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Timestamp systemTime) {
        this.systemTime = systemTime;
    }

    public Integer getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(Integer billMonth) {
        this.billMonth = billMonth;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
	
    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public Long getMonBasicMoney() {
        return monBasicMoney;
    }

    public void setMonBasicMoney(Long monBasicMoney) {
        this.monBasicMoney = monBasicMoney;
    }

    public Long getMonGdsMoney() {
        return monGdsMoney;
    }

    public void setMonGdsMoney(Long monGdsMoney) {
        this.monGdsMoney = monGdsMoney;
    }

    public Long getMonExpFee() {
        return monExpFee;
    }

    public void setMonExpFee(Long monExpFee) {
        this.monExpFee = monExpFee;
    }

    public Long getMonRealMoney() {
        return monRealMoney;
    }

    public void setMonRealMoney(Long monRealMoney) {
        this.monRealMoney = monRealMoney;
    }

    public Long getMonPayFee() {
        return monPayFee;
    }

    public void setMonPayFee(Long monPayFee) {
        this.monPayFee = monPayFee;
    }

    public Long getMonServFee() {
        return monServFee;
    }

    public void setMonServFee(Long monServFee) {
        this.monServFee = monServFee;
    }

    public Long getMonInFee() {
        return monInFee;
    }

    public void setMonInFee(Long monInFee) {
        this.monInFee = monInFee;
    }

    public Long getInMoney() {
        return inMoney;
    }

    public void setInMoney(Long inMoney) {
        this.inMoney = inMoney;
    }

    public Long getMonBackMoney() {
        return monBackMoney;
    }

    public void setMonBackMoney(Long monBackMoney) {
        this.monBackMoney = monBackMoney;
    }

    public Long getMonBackPayfee() {
        return monBackPayfee;
    }

    public void setMonBackPayfee(Long monBackPayfee) {
        this.monBackPayfee = monBackPayfee;
    }

    public Long getMonBackServfee() {
        return monBackServfee;
    }

    public void setMonBackServfee(Long monBackServfee) {
        this.monBackServfee = monBackServfee;
    }

    public Long getMonBackBackfee() {
        return monBackBackfee;
    }

    public void setMonBackBackfee(Long monBackBackfee) {
        this.monBackBackfee = monBackBackfee;
    }

    public Long getMonBackFee() {
        return monBackFee;
    }

    public void setMonBackFee(Long monBackFee) {
        this.monBackFee = monBackFee;
    }

    public Long getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(Long backMoney) {
        this.backMoney = backMoney;
    }

    public Long getAdjMoney() {
        return adjMoney;
    }

    public void setAdjMoney(Long adjMoney) {
        this.adjMoney = adjMoney;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(Long withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public Timestamp getCalTime() {
        return calTime;
    }

    public void setCalTime(Timestamp calTime) {
        this.calTime = calTime;
    }

    public Timestamp getCanWithdrawTime() {
        return canWithdrawTime;
    }

    public void setCanWithdrawTime(Timestamp canWithdrawTime) {
        this.canWithdrawTime = canWithdrawTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(Long createStaff) {
        this.createStaff = createStaff;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateStaff() {
        return updateStaff;
    }

    public void setUpdateStaff(Long updateStaff) {
        this.updateStaff = updateStaff;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
		sb.append(", billMonth=").append(billMonth);
        sb.append(", shopId=").append(shopId);
        sb.append(", acctId=").append(acctId);
        sb.append(", monBasicMoney=").append(monBasicMoney);
        sb.append(", monGdsMoney=").append(monGdsMoney);
        sb.append(", monExpFee=").append(monExpFee);
        sb.append(", monRealMoney=").append(monRealMoney);
        sb.append(", monPayFee=").append(monPayFee);
        sb.append(", monServFee=").append(monServFee);
        sb.append(", monInFee=").append(monInFee);
        sb.append(", inMoney=").append(inMoney);
        sb.append(", monBackMoney=").append(monBackMoney);
        sb.append(", monBackPayfee=").append(monBackPayfee);
        sb.append(", monBackServfee=").append(monBackServfee);
        sb.append(", monBackBackfee=").append(monBackBackfee);
        sb.append(", monBackFee=").append(monBackFee);
        sb.append(", backMoney=").append(backMoney);
        sb.append(", adjMoney=").append(adjMoney);
        sb.append(", money=").append(money);
        sb.append(", withdrawMoney=").append(withdrawMoney);
        sb.append(", calTime=").append(calTime);
        sb.append(", aTime=").append(canWithdrawTime);
        sb.append(", status=").append(status);
        sb.append(", createStaff=").append(createStaff);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateStaff=").append(updateStaff);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", systemTime=").append(systemTime);
        sb.append("]");
        return sb.toString();
    }
}