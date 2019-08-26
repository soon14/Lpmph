package com.ai.ecp.busi.order.vo;

import java.io.Serializable;

public class CoupPaperReqVo implements Serializable {

	/** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
	private static final long serialVersionUID = -4292039482483476704L;
	
	private String sourceKey;
	
	private String mainHashKey;
	
	private String shopIdList;
	
	private String skuIds;
	
	public String getSkuIds() {
		return skuIds;
	}
	public void setSkuIds(String skuIds) {
		this.skuIds = skuIds;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public String getShopIdList() {
		return shopIdList;
	}
	public void setShopIdList(String shopIdList) {
		this.shopIdList = shopIdList;
	}
	public String getMainHashKey() {
		return mainHashKey;
	}
	public void setMainHashKey(String mainHashKey) {
		this.mainHashKey = mainHashKey;
	}
	
}
