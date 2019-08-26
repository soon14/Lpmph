package com.ai.ecp.pmph.dubbo.dto;

import java.sql.Timestamp;
import java.util.Date;

import com.ai.ecp.server.front.dto.BaseInfo;

public class ROrdTmMainReq extends BaseInfo {
    
    //商城会员ID
    private Long rwStaffId;
    
    //商城会员名
    private String rwStaffCode;

    //淘宝会员名
    private String tmStaffCode;
    
    //收货人姓名
    private String contractName;
    
    //订单编码
    private String orderCode;

    //订单状态
    private String status;
    
    //是否已增送积分
    private String rwScoreFlag;
    
    //赠送积分数 
    private Long rwScore;
    
    //导入开始时间 
    private Timestamp begDate;
    
    //导入结束时间
    private Timestamp endDate; 
    
    //创建时间
    private Timestamp createTime;
    
    //导入开始时间 
    private Date importTime;
    
    
    private static final long serialVersionUID = 1L; 
    
    public Long getRwStaffId() {
        return rwStaffId;
    }

    public void setRwStaffId(Long rwStaffId) {
        this.rwStaffId = rwStaffId;
    }
    
    public String getRwStaffCode() {
        return rwStaffCode;
    }

    public void setRwStaffCode(String rwStaffCode) {
        this.rwStaffCode = rwStaffCode;
    }

    public String getTmStaffCode() {
        return tmStaffCode;
    }

    public void setTmStaffCode(String tmStaffCode) {
        this.tmStaffCode = tmStaffCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRwScoreFlag() {
        return rwScoreFlag;
    }

    public void setRwScoreFlag(String rwScoreFlag) {
        this.rwScoreFlag = rwScoreFlag;
    }

    public Long getRwScore() {
        return rwScore;
    }

    public void setRwScore(Long rwScore) {
        this.rwScore = rwScore;
    }
    
    public Timestamp getBegDate() {
        return begDate;
    }

    public void setBegDate(Timestamp begDate) {
        this.begDate = begDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

}