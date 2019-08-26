package com.ai.ecp.pmph.busi.seller.staff.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class ShopAcctInfoVO extends EcpBasePageReqVO implements Serializable {
    /**
     * 账户ID:SEQ_SHOP_ACCT_ID
     */
    private Long id;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 账户总额=账户可用余额+账户冻结金额
     */
    private Long acctTotal;

    /**
     * 账户可用余额，可提现额度
     */
    private Long acctBalance;

    /**
     * 账户冻结金额、押金
     */
    private Long acctFrozen;

    /**
     * 账户提现总额
     */
    private Long totalWithdraw;

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

    private static final long serialVersionUID = 1L;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getAcctTotal() {
        return acctTotal;
    }

    public void setAcctTotal(Long acctTotal) {
        this.acctTotal = acctTotal;
    }

    public Long getAcctBalance() {
        return acctBalance;
    }

    public void setAcctBalance(Long acctBalance) {
        this.acctBalance = acctBalance;
    }

    public Long getAcctFrozen() {
        return acctFrozen;
    }

    public void setAcctFrozen(Long acctFrozen) {
        this.acctFrozen = acctFrozen;
    }

    public Long getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(Long totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
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
        sb.append(", companyId=").append(companyId);
        sb.append(", acctTotal=").append(acctTotal);
        sb.append(", acctBalance=").append(acctBalance);
        sb.append(", acctFrozen=").append(acctFrozen);
        sb.append(", totalWithdraw=").append(totalWithdraw);
        sb.append(", createStaff=").append(createStaff);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateStaff=").append(updateStaff);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}