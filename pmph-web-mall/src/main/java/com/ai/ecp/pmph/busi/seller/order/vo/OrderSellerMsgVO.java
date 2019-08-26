package com.ai.ecp.pmph.busi.seller.order.vo;

import java.io.Serializable;

public class OrderSellerMsgVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String orderId;

	private String sellerMsg;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSellerMsg() {
		return sellerMsg;
	}

	public void setSellerMsg(String sellerMsg) {
		this.sellerMsg = sellerMsg;
	}

	
}
