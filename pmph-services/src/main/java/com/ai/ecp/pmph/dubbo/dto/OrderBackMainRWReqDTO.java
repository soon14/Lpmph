package com.ai.ecp.pmph.dubbo.dto;

import java.util.List;

import com.ai.ecp.server.front.dto.BaseInfo;

public class OrderBackMainRWReqDTO<T extends BaseInfo> extends BaseInfo {

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -338996087612618315L;
    
    
    private List<T> list;
    
    private String orderId;
    
    private boolean lastFlag;//是否为退货的最后一笔
    
    private long scale;
    
    private Long staffId;
    
    private String type;//是否可以扣为负值（积分、资金账户）
    
    private Long siteId;//站点id
    
    private String refundOrBack;//退款退货标识：0：退款；1：退货；
    
    private Long backId;//订单退款退货申请id
    
    private Long backScore;//此次退货，需要退的积分
    
    private Long modifyBackSocre;//此次退货，调整后，需要退的积分


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isLastFlag() {
        return lastFlag;
    }

    public void setLastFlag(boolean lastFlag) {
        this.lastFlag = lastFlag;
    }

    public long getScale() {
        return scale;
    }

    public void setScale(long scale) {
        this.scale = scale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

	public String getRefundOrBack() {
		return refundOrBack;
	}

	public void setRefundOrBack(String refundOrBack) {
		this.refundOrBack = refundOrBack;
	}

    public Long getBackId() {
        return backId;
    }

    public void setBackId(Long backId) {
        this.backId = backId;
    }

	public Long getBackScore() {
		return backScore;
	}

	public void setBackScore(Long backScore) {
		this.backScore = backScore;
	}

	public Long getModifyBackSocre() {
		return modifyBackSocre;
	}

	public void setModifyBackSocre(Long modifyBackSocre) {
		this.modifyBackSocre = modifyBackSocre;
	}
    
}

