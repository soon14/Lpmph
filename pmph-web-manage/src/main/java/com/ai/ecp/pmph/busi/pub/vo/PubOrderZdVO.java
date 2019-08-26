package com.ai.ecp.pmph.busi.pub.vo;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage 征订单查询VO<br>
 * Description: <br>
 * Date:2018年8月6日下午1:37:27  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
public class PubOrderZdVO extends EcpBasePageReqVO implements Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.7
     */ 
    private static final long serialVersionUID = 1L;
    
    /**
     * 征订单号
     */
    private String pubOrderId;
    
    /**
     * 开始日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date begDate;
    
    /**
     * 截止日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    
    /**
     * 公众号名
     */
    private String pubCode;
    
    /**
     * 联系人
     */
    private String staffName;
    
    /**
     * 负责人
     */
    private String resName;
    
    /**
     * 会员名
     */
    private String staffCode;
    
    /**
     * 征订单状态    01：待支付   02：待发货  04：部分发货  05：全部发货  99：取消
     */
    private String pubStatus;

    public String getPubOrderId() {
        return pubOrderId;
    }

    public void setPubOrderId(String pubOrderId) {
        this.pubOrderId = pubOrderId;
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

    public String getPubCode() {
        return pubCode;
    }

    public void setPubCode(String pubCode) {
        this.pubCode = pubCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(String pubStatus) {
        this.pubStatus = pubStatus;
    }

    @Override
    public String toString() {
        return "PubOrderZdVO [pubOrderId=" + pubOrderId + ", begDate=" + begDate + ", endDate="
                + endDate + ", pubCode=" + pubCode + ", staffName=" + staffName + ", resName="
                + resName + ", staffCode=" + staffCode + ", pubStatus=" + pubStatus + "]";
    }
}

