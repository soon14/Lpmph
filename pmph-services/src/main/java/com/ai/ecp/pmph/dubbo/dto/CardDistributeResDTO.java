package com.ai.ecp.pmph.dubbo.dto;

import java.sql.Timestamp;

import com.ai.ecp.server.front.dto.BaseResponseDTO;

public class CardDistributeResDTO extends BaseResponseDTO{
    
    private Long id;

    private Long disId;
    
    private String disName;

    private String custLevelCode;

    private String custLevelCodeName;

    private String custCardId;

    private String remark;

    private Timestamp sendCard;

    private String status;

    private Timestamp createTime;

    private Long createStaff;

    private Timestamp updateTime;

    private Long updateStaff;

    private static final long serialVersionUID = 1L;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDisId() {
        return disId;
    }

    public void setDisId(Long disId) {
        this.disId = disId;
    }
    
    public String getCustLevelCode() {
        return custLevelCode;
    }

    public void setCustLevelCode(String custLevelCode) {
        this.custLevelCode = custLevelCode == null ? null : custLevelCode.trim();
    }

    public String getCustCardId() {
        return custCardId;
    }

    public void setCustCardId(String custCardId) {
        this.custCardId = custCardId == null ? null : custCardId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Timestamp getSendCard() {
        return sendCard;
    }

    public void setSendCard(Timestamp sendCard) {
        this.sendCard = sendCard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public String getDisName() {
        return disName;
    }

    public void setDisName(String disName) {
        this.disName = disName;
    }

    public String getCustLevelCodeName() {
        return custLevelCodeName;
    }

    public void setCustLevelCodeName(String custLevelCodeName) {
        this.custLevelCodeName = custLevelCodeName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CardDistributeResDTO [id=");
        builder.append(id);
        builder.append(", disId=");
        builder.append(disId);
        builder.append(", custLevelCode=");
        builder.append(custLevelCode);
        builder.append(", custCareId=");
        builder.append(custCardId);
        builder.append(", remark=");
        builder.append(remark);
        builder.append(", sendCare=");
        builder.append(sendCard);
        builder.append(", status=");
        builder.append(status);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", createStaff=");
        builder.append(createStaff);
        builder.append(", updateTime=");
        builder.append(updateTime);
        builder.append(", updateStaff=");
        builder.append(updateStaff);
        builder.append("]");
        return builder.toString();
    }



}

