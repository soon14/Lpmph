package com.ai.ecp.pmph.dubbo.dto;

import java.security.Timestamp;
import java.util.Date;

import com.ai.ecp.server.front.dto.BaseResponseDTO;

public class OrdMainCompensateResponse extends BaseResponseDTO{
    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -7494429999128596573L;

    private String id;

    private String orderCode;

    private Long orderAmount;

    private Long orderMoney;

    private Long realMoney;

    private Long realExpressFee;

    private Long shopId;

    private String shopName;

    private Long staffId;

    private Date orderTime;

    private Date payTime;

    private String deliverDate;

    private String status;

    private String orderTwoStatus;

    private String orderType;

    private String payType;

    private String payFlag;

    private String dispatchType;

    private String source;

    private String payLock;

    private String buyerMsg;

    private Date createTime;

    private Long createStaff;

    private Date updateTime;

    private Long updateStaff;

    private Long siteId;

    private String sysType;

    private String payWay;

    private Long orderScore;

    private String invoiceType;

    private String contactName;

    private String contactPhone;

    private String chnlAddress;

    private String postCode;

    private String contactNumber;

    private String province;

    private String cityCode;

    private String countyCode;

    private String bringName;

    private String cardType;

    private String cardId;

    private String bringNumber;

    private String bringPhone;

    private String billingFlag;

    private String sendInvoiceType;

    private String invoiceExpressNo;

    private Long basicMoney;

    private String isInAuditFile;
    
    private boolean hasBack;//是否有过退款退货
    
    private String staffName;

    public String getId() {
        return id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public Long getOrderMoney() {
        return orderMoney;
    }

    public Long getRealMoney() {
        return realMoney;
    }

    public Long getRealExpressFee() {
        return realExpressFee;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public Long getStaffId() {
        return staffId;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderTwoStatus() {
        return orderTwoStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getPayType() {
        return payType;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public String getDispatchType() {
        return dispatchType;
    }

    public String getSource() {
        return source;
    }

    public String getPayLock() {
        return payLock;
    }

    public String getBuyerMsg() {
        return buyerMsg;
    }

    public Long getCreateStaff() {
        return createStaff;
    }

    public Long getUpdateStaff() {
        return updateStaff;
    }

    public Long getSiteId() {
        return siteId;
    }

    public String getSysType() {
        return sysType;
    }

    public String getPayWay() {
        return payWay;
    }

    public Long getOrderScore() {
        return orderScore;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getChnlAddress() {
        return chnlAddress;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getProvince() {
        return province;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public String getBringName() {
        return bringName;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardId() {
        return cardId;
    }

    public String getBringNumber() {
        return bringNumber;
    }

    public String getBringPhone() {
        return bringPhone;
    }

    public String getBillingFlag() {
        return billingFlag;
    }

    public String getSendInvoiceType() {
        return sendInvoiceType;
    }

    public String getInvoiceExpressNo() {
        return invoiceExpressNo;
    }

    public Long getBasicMoney() {
        return basicMoney;
    }

    public String getIsInAuditFile() {
        return isInAuditFile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setOrderMoney(Long orderMoney) {
        this.orderMoney = orderMoney;
    }

    public void setRealMoney(Long realMoney) {
        this.realMoney = realMoney;
    }

    public void setRealExpressFee(Long realExpressFee) {
        this.realExpressFee = realExpressFee;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderTwoStatus(String orderTwoStatus) {
        this.orderTwoStatus = orderTwoStatus;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setPayLock(String payLock) {
        this.payLock = payLock;
    }

    public void setBuyerMsg(String buyerMsg) {
        this.buyerMsg = buyerMsg;
    }

    public void setCreateStaff(Long createStaff) {
        this.createStaff = createStaff;
    } 

    public void setUpdateStaff(Long updateStaff) {
        this.updateStaff = updateStaff;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public void setOrderScore(Long orderScore) {
        this.orderScore = orderScore;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setChnlAddress(String chnlAddress) {
        this.chnlAddress = chnlAddress;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setBringName(String bringName) {
        this.bringName = bringName;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setBringNumber(String bringNumber) {
        this.bringNumber = bringNumber;
    }

    public void setBringPhone(String bringPhone) {
        this.bringPhone = bringPhone;
    }

    public void setBillingFlag(String billingFlag) {
        this.billingFlag = billingFlag;
    }

    public void setSendInvoiceType(String sendInvoiceType) {
        this.sendInvoiceType = sendInvoiceType;
    }

    public void setInvoiceExpressNo(String invoiceExpressNo) {
        this.invoiceExpressNo = invoiceExpressNo;
    }

    public void setBasicMoney(Long basicMoney) {
        this.basicMoney = basicMoney;
    }

    public void setIsInAuditFile(String isInAuditFile) {
        this.isInAuditFile = isInAuditFile;
    }

    public boolean isHasBack() {
        return hasBack;
    }

    public void setHasBack(boolean hasBack) {
        this.hasBack = hasBack;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}

