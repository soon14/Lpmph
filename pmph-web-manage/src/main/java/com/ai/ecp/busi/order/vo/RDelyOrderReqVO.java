package com.ai.ecp.busi.order.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.alibaba.fastjson.annotation.JSONField;

public class RDelyOrderReqVO extends EcpBasePageReqVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9109740382609490115L;

	/**
	 * 开始时间
	 */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="{order.orderdate.null.error}")
    private Date begDate;

	/**
     * 截止时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="{order.orderdate.null.error}")
    private Date endDate;

    /**
     * 订单编号
     */
    @NotNull(message="{order.orderid.null.error}")
    private String orderId;
    
    /**
     * 买家账号
     */
    private Long staffId;
    
    /**
     * 商品分类
     */
    private String categoryCode;
    
    /** 
     * status:订单状态. 
     * @since JDK 1.6 
     */ 
    private String status;
    
    /**
     * 站点信息 
     */
    private Long siteId;
    
    /**
     * 
     * 店铺ID
     */
    private Long shopId;
    
    /** 
     * contactName:收货人. 
     * @since JDK 1.6 
     */ 
    private String contactName;
    
    /** 
     * contactPhone:收货人手机. 
     * @since JDK 1.6 
     */ 
    private String contactPhone;
    
    /** 
     * invoiceType:是否开发票. 
     * @since JDK 1.6 
     */ 
    private String invoiceType;
    
    /** 
     * dispatchType:配送方式. 
     * @since JDK 1.6 
     */ 
    private String dispatchType;
    
    /** 
     * staffCode:登录工号. 
     * @since JDK 1.6 
     */ 
    private String staffCode;

    /**
     * payType 支付方式
     * @return
     */
    private String payType;
    
    /** 
     * payWay:支付通道. 
     * @since JDK 1.6 
     */ 
    private String payWay;
    
    /** 
     * payFlag:支付状态. 
     * @since JDK 1.6 
     */ 
    private String payFlag;
    
    /** 
     * payTranNo:商户订单号. 
     * @since JDK 1.6 
     */
    private String payTranNo;

    /**
     * 下载类型
     */
    private String exportType;
    
    /**
     * 排序规则
     */
    private String orderRull;
    
    /**
     * 订单来源:0.人卫 1.临床 2.用药 3.约健康 4.征订单
     */
    private String catgFlag;
    
    /**
     * 征订单编号
     */
    private String zdId;
    /**
     * 是否补偿性退款
     */
    private String isCompenstate;
    /**
     * 订单金额
     */
    private String realMoney;

	public String getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(String realMoney) {
		this.realMoney = realMoney;
	}

	public String getIsCompenstate() {
		return isCompenstate;
	}

	public void setIsCompenstate(String isCompenstate) {
		this.isCompenstate = isCompenstate;
	}

	public String getPayTranNo() {
        return payTranNo;
    }

    public void setPayTranNo(String payTranNo) {
        this.payTranNo = payTranNo;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

	public String getOrderRull() {
		return orderRull;
	}

	public void setOrderRull(String orderRull) {
		this.orderRull = orderRull;
	}

    public String getCatgFlag() {
        return catgFlag;
    }

    public void setCatgFlag(String catgFlag) {
        this.catgFlag = catgFlag;
    }

    public String getZdId() {
        return zdId;
    }

    public void setZdId(String zdId) {
        this.zdId = zdId;
    }
    
}
