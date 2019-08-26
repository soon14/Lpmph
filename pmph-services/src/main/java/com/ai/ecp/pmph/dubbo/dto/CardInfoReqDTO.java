package com.ai.ecp.pmph.dubbo.dto;

import java.sql.Timestamp;
import java.util.List;

import com.ai.ecp.server.front.dto.BaseInfo;

public class CardInfoReqDTO extends BaseInfo {
    private String custCardId;

    private String custLevelCode;

    private String cardStatus;
    
    private String cardGroup;
    
    public Long getRow() {
        return row;
    }
    
    

    public String getCardGroup() {
        return cardGroup;
    }



    public void setCardGroup(String cardGroup) {
        this.cardGroup = cardGroup;
    }



    public void setRow(Long row) {
        this.row = row;
    }

    private Long row;

    private Timestamp createTime;

    private Long createStaff;

    private Timestamp updateTime;

    private Long updateStaff;
    
    private List<CardInfoReqDTO> list;
    
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    private String fileId;
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileName;

    public List<CardInfoReqDTO> getList() {
        return list;
    }

    public void setList(List<CardInfoReqDTO> list) {
        this.list = list;
    }

    private static final long serialVersionUID = 1L;

    public String getCustCardId() {
        return custCardId;
    }

    public void setCustCardId(String custCardId) {
        this.custCardId = custCardId == null ? null : custCardId.trim();
    }

    public String getCustLevelCode() {
        return custLevelCode;
    }

    public void setCustLevelCode(String custLevelCode) {
        this.custLevelCode = custLevelCode == null ? null : custLevelCode.trim();
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus == null ? null : cardStatus.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custCardId=").append(custCardId);
        sb.append(", custLevelCode=").append(custLevelCode);
        sb.append(", cardStatus=").append(cardStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", createStaff=").append(createStaff);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateStaff=").append(updateStaff);
        sb.append(", row=").append(row);
        sb.append(", cardGroup=").append(cardGroup);
        sb.append("]");
        return sb.toString();
    }
}