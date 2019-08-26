package com.ai.ecp.pmph.dubbo.dto;

import java.sql.Timestamp;

import com.ai.ecp.server.front.dto.BaseResponseDTO;

public class ROrdTmImportLogResp extends BaseResponseDTO {
    
    private String id;

    private String importType;

    private Timestamp importTime;

    private String oldOrderId;

    private String newOrderId;

    private String exceptionMsg;

    private String status;

    private Timestamp createTime;

    private Long createStaff;

    private Timestamp updateTime;

    private Long updateStaff;

    private Long newStaffId;

    private String oldStaffId;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType == null ? null : importType.trim();
    }

    public Timestamp getImportTime() {
        return importTime;
    }

    public void setImportTime(Timestamp importTime) {
        this.importTime = importTime;
    }

    public String getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(String oldOrderId) {
        this.oldOrderId = oldOrderId == null ? null : oldOrderId.trim();
    }

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId == null ? null : newOrderId.trim();
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg == null ? null : exceptionMsg.trim();
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

    public Long getNewStaffId() {
        return newStaffId;
    }

    public void setNewStaffId(Long newStaffId) {
        this.newStaffId = newStaffId;
    }

    public String getOldStaffId() {
        return oldStaffId;
    }

    public void setOldStaffId(String oldStaffId) {
        this.oldStaffId = oldStaffId == null ? null : oldStaffId.trim();
    }
}