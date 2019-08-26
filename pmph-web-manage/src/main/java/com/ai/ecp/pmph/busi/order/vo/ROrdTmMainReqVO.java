package com.ai.ecp.pmph.busi.order.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class ROrdTmMainReqVO extends EcpBasePageReqVO implements Serializable {
    
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
    
    //导入时间 
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date begDate;
    
    //导入结束时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    
  //导入结束时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;

    private static final long serialVersionUID = 1L; 
    
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

    public Date getBegDate() {
        return begDate;
    }

    public void setBegDate(Date begDate) {
        this.begDate = begDate;
    }

    public Date getEndDate() {
        return DateUtils.addDays(endDate, 1);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}