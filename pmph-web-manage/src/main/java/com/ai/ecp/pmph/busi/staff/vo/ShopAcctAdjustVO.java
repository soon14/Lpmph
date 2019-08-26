package com.ai.ecp.pmph.busi.staff.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class ShopAcctAdjustVO extends EcpBasePageReqVO implements Serializable{
	/**
     * 提现申请ID:SEQ_SHOP_ACCT_APPLY_ID
     */
    private Long id;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 账户ID
     */
    private Long acctId;

    /**
     * 调账类型：31：调账收入  32：调账支出   
     */
    private String optType;

    /**
     * 调账金额
     */
    private Long money;

    /**
     * 调账结算日
     */
    private Integer billDay;

    /**
     * 调账结算月
     */
    private Integer billMonth;

    /**
     * 申请人
     */
    private Long applyStaff;

    /**
     * 申请时间
     */
    private Timestamp applyTime;

    /**
     * 申请人备注
     */
    private String applyDesc;

    /**
     * 申请单状态：00 :一级待审核  01：二级待审核  02：同意调账  03：已调账   04：拒绝调账 05:撤销
     */
    private String status;

    /**
     * 审核意见
     */
    private String checkDesc;

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
     * 申请开始日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date begDate;

    /**
     * 申请截止日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    
    /**
     * 店铺名称
     */
    private String shopName;
    private static final long serialVersionUID = 1L;

    public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Date getBegDate() {
		return begDate;
	}

	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType == null ? null : optType.trim();
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getBillDay() {
        return billDay;
    }

    public void setBillDay(Integer billDay) {
        this.billDay = billDay;
    }

    public Integer getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(Integer billMonth) {
        this.billMonth = billMonth;
    }

    public Long getApplyStaff() {
        return applyStaff;
    }

    public void setApplyStaff(Long applyStaff) {
        this.applyStaff = applyStaff;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyDesc() {
        return applyDesc;
    }

    public void setApplyDesc(String applyDesc) {
        this.applyDesc = applyDesc == null ? null : applyDesc.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCheckDesc() {
        return checkDesc;
    }

    public void setCheckDesc(String checkDesc) {
        this.checkDesc = checkDesc == null ? null : checkDesc.trim();
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
        sb.append(", id=").append(id);
        sb.append(", shopId=").append(shopId);
        sb.append(", acctId=").append(acctId);
        sb.append(", optType=").append(optType);
        sb.append(", money=").append(money);
        sb.append(", billDay=").append(billDay);
        sb.append(", billMonth=").append(billMonth);
        sb.append(", applyStaff=").append(applyStaff);
        sb.append(", applyTime=").append(applyTime);
        sb.append(", applyDesc=").append(applyDesc);
        sb.append(", status=").append(status);
        sb.append(", checkDesc=").append(checkDesc);
        sb.append(", createStaff=").append(createStaff);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateStaff=").append(updateStaff);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", begDate=").append(begDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", shopName=").append(shopName);
        sb.append("]");
        return sb.toString();
    }
}

