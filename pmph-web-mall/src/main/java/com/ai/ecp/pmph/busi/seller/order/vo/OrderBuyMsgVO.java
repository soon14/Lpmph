package com.ai.ecp.pmph.busi.seller.order.vo;

import java.io.Serializable;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class OrderBuyMsgVO extends EcpBasePageReqVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String buyMsg;
	
	private String orderId;

	public String getBuyMsg() {
		return buyMsg;
	}

	public void setBuyMsg(String buyMsg) {
		this.buyMsg = buyMsg;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
