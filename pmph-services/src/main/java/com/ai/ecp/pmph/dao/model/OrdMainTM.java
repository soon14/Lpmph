package com.ai.ecp.pmph.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrdMainTM implements Serializable {
    private String id;

    private String orderCode;

    private String tmStaffCode;

    private String alipayAccount;

    private String orderMoney;

    private String realExpressFee;

    private String orderScore;

    private String sumMoney;

    private String backScore;

    private String realMoney;

    private String realScore;

    private String status;

    private String buyerMsg;

    private String contractName;

    private String contractAddr;

    private String dispatchType;

    private String contractTel;

    private String contractNum;

    private Timestamp orderTime;

    private Timestamp payTime;

    private String bbTitle;

    private String bbType;

    private String expressNo;

    private String expressCompany;

    private String remark;

    private String orderAmount;

    private String shopId;

    private String shopName;

    private String closeReason;

    private String shopServiceFee;

    private String staffServiceFee;

    private String invoiceTitle;

    private String appFlag;

    private String stageOrderMsg;

    private String downRank;

    private String moditySku;

    private String modifyContractAddr;

    private String exceptionMsg;

    private String tmCoupDeduct;

    private String jfbDeduct;

    private String o2oFlag;

    private Timestamp createTime;

    private Long createStaff;

    private Timestamp updateTime;

    private Long updateStaff;

    private String rwStaffCode;

    private Long rwStaffId;

    private String rwScoreFlag;

    private Long rwScore;

    private Timestamp importTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public String getTmStaffCode() {
        return tmStaffCode;
    }

    public void setTmStaffCode(String tmStaffCode) {
        this.tmStaffCode = tmStaffCode == null ? null : tmStaffCode.trim();
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount == null ? null : alipayAccount.trim();
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney == null ? null : orderMoney.trim();
    }

    public String getRealExpressFee() {
        return realExpressFee;
    }

    public void setRealExpressFee(String realExpressFee) {
        this.realExpressFee = realExpressFee == null ? null : realExpressFee.trim();
    }

    public String getOrderScore() {
        return orderScore;
    }

    public void setOrderScore(String orderScore) {
        this.orderScore = orderScore == null ? null : orderScore.trim();
    }

    public String getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(String sumMoney) {
        this.sumMoney = sumMoney == null ? null : sumMoney.trim();
    }

    public String getBackScore() {
        return backScore;
    }

    public void setBackScore(String backScore) {
        this.backScore = backScore == null ? null : backScore.trim();
    }

    public String getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(String realMoney) {
        this.realMoney = realMoney == null ? null : realMoney.trim();
    }

    public String getRealScore() {
        return realScore;
    }

    public void setRealScore(String realScore) {
        this.realScore = realScore == null ? null : realScore.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBuyerMsg() {
        return buyerMsg;
    }

    public void setBuyerMsg(String buyerMsg) {
        this.buyerMsg = buyerMsg == null ? null : buyerMsg.trim();
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr == null ? null : contractAddr.trim();
    }

    public String getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType == null ? null : dispatchType.trim();
    }

    public String getContractTel() {
        return contractTel;
    }

    public void setContractTel(String contractTel) {
        this.contractTel = contractTel == null ? null : contractTel.trim();
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum == null ? null : contractNum.trim();
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getBbTitle() {
        return bbTitle;
    }

    public void setBbTitle(String bbTitle) {
        this.bbTitle = bbTitle == null ? null : bbTitle.trim();
    }

    public String getBbType() {
        return bbType;
    }

    public void setBbType(String bbType) {
        this.bbType = bbType == null ? null : bbType.trim();
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo == null ? null : expressNo.trim();
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany == null ? null : expressCompany.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount == null ? null : orderAmount.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason == null ? null : closeReason.trim();
    }

    public String getShopServiceFee() {
        return shopServiceFee;
    }

    public void setShopServiceFee(String shopServiceFee) {
        this.shopServiceFee = shopServiceFee == null ? null : shopServiceFee.trim();
    }

    public String getStaffServiceFee() {
        return staffServiceFee;
    }

    public void setStaffServiceFee(String staffServiceFee) {
        this.staffServiceFee = staffServiceFee == null ? null : staffServiceFee.trim();
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle == null ? null : invoiceTitle.trim();
    }

    public String getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(String appFlag) {
        this.appFlag = appFlag == null ? null : appFlag.trim();
    }

    public String getStageOrderMsg() {
        return stageOrderMsg;
    }

    public void setStageOrderMsg(String stageOrderMsg) {
        this.stageOrderMsg = stageOrderMsg == null ? null : stageOrderMsg.trim();
    }

    public String getDownRank() {
        return downRank;
    }

    public void setDownRank(String downRank) {
        this.downRank = downRank == null ? null : downRank.trim();
    }

    public String getModitySku() {
        return moditySku;
    }

    public void setModitySku(String moditySku) {
        this.moditySku = moditySku == null ? null : moditySku.trim();
    }

    public String getModifyContractAddr() {
        return modifyContractAddr;
    }

    public void setModifyContractAddr(String modifyContractAddr) {
        this.modifyContractAddr = modifyContractAddr == null ? null : modifyContractAddr.trim();
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg == null ? null : exceptionMsg.trim();
    }

    public String getTmCoupDeduct() {
        return tmCoupDeduct;
    }

    public void setTmCoupDeduct(String tmCoupDeduct) {
        this.tmCoupDeduct = tmCoupDeduct == null ? null : tmCoupDeduct.trim();
    }

    public String getJfbDeduct() {
        return jfbDeduct;
    }

    public void setJfbDeduct(String jfbDeduct) {
        this.jfbDeduct = jfbDeduct == null ? null : jfbDeduct.trim();
    }

    public String getO2oFlag() {
        return o2oFlag;
    }

    public void setO2oFlag(String o2oFlag) {
        this.o2oFlag = o2oFlag == null ? null : o2oFlag.trim();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(Long createStaff) {
        this.createStaff = createStaff;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateStaff() {
        return updateStaff;
    }

    public void setUpdateStaff(Long updateStaff) {
        this.updateStaff = updateStaff;
    }

    public String getRwStaffCode() {
        return rwStaffCode;
    }

    public void setRwStaffCode(String rwStaffCode) {
        this.rwStaffCode = rwStaffCode == null ? null : rwStaffCode.trim();
    }

    public Long getRwStaffId() {
        return rwStaffId;
    }

    public void setRwStaffId(Long rwStaffId) {
        this.rwStaffId = rwStaffId;
    }

    public String getRwScoreFlag() {
        return rwScoreFlag;
    }

    public void setRwScoreFlag(String rwScoreFlag) {
        this.rwScoreFlag = rwScoreFlag == null ? null : rwScoreFlag.trim();
    }

    public Long getRwScore() {
        return rwScore;
    }

    public void setRwScore(Long rwScore) {
        this.rwScore = rwScore;
    }

    public Timestamp getImportTime() {
        return importTime;
    }

    public void setImportTime(Timestamp importTime) {
        this.importTime = importTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderCode=").append(orderCode);
        sb.append(", tmStaffCode=").append(tmStaffCode);
        sb.append(", alipayAccount=").append(alipayAccount);
        sb.append(", orderMoney=").append(orderMoney);
        sb.append(", realExpressFee=").append(realExpressFee);
        sb.append(", orderScore=").append(orderScore);
        sb.append(", sumMoney=").append(sumMoney);
        sb.append(", backScore=").append(backScore);
        sb.append(", realMoney=").append(realMoney);
        sb.append(", realScore=").append(realScore);
        sb.append(", status=").append(status);
        sb.append(", buyerMsg=").append(buyerMsg);
        sb.append(", contractName=").append(contractName);
        sb.append(", contractAddr=").append(contractAddr);
        sb.append(", dispatchType=").append(dispatchType);
        sb.append(", contractTel=").append(contractTel);
        sb.append(", contractNum=").append(contractNum);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", payTime=").append(payTime);
        sb.append(", bbTitle=").append(bbTitle);
        sb.append(", bbType=").append(bbType);
        sb.append(", expressNo=").append(expressNo);
        sb.append(", expressCompany=").append(expressCompany);
        sb.append(", remark=").append(remark);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", shopId=").append(shopId);
        sb.append(", shopName=").append(shopName);
        sb.append(", closeReason=").append(closeReason);
        sb.append(", shopServiceFee=").append(shopServiceFee);
        sb.append(", staffServiceFee=").append(staffServiceFee);
        sb.append(", invoiceTitle=").append(invoiceTitle);
        sb.append(", appFlag=").append(appFlag);
        sb.append(", stageOrderMsg=").append(stageOrderMsg);
        sb.append(", downRank=").append(downRank);
        sb.append(", moditySku=").append(moditySku);
        sb.append(", modifyContractAddr=").append(modifyContractAddr);
        sb.append(", exceptionMsg=").append(exceptionMsg);
        sb.append(", tmCoupDeduct=").append(tmCoupDeduct);
        sb.append(", jfbDeduct=").append(jfbDeduct);
        sb.append(", o2oFlag=").append(o2oFlag);
        sb.append(", createTime=").append(createTime);
        sb.append(", createStaff=").append(createStaff);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateStaff=").append(updateStaff);
        sb.append(", rwStaffCode=").append(rwStaffCode);
        sb.append(", rwStaffId=").append(rwStaffId);
        sb.append(", rwScoreFlag=").append(rwScoreFlag);
        sb.append(", rwScore=").append(rwScore);
        sb.append(", importTime=").append(importTime);
        sb.append("]");
        return sb.toString();
    }
}