package com.ai.ecp.common.vo;

import java.sql.Timestamp;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class CustomerGdsRespVO extends EcpBasePageReqVO {

	private Long id; // 商品id
	private Long firstSkuId; // 单品id
	private String imageUrl; // 商品图片
	private Long defaultPrice; // 单品默认价格
	private Long discountPrice; // 单品折扣价格
	private Long discount; // 折扣率
	private String gdsName; // 商品名称
	private String gdsSubHead; // 商品副标题
	private String gdsDesc; // 商品描述
	private String shopName; // 店铺名称
	private Long shopId; // 店铺id
//	private Long sales; // 付款人数
//	private Long storage; // 库存量
	private Long gdsTypeId; // 商品类型
	
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Long getDefaultPrice() {
		return defaultPrice;
	}
	public void setDefaultPrice(Long defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	public Long getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Long discountPrice) {
		this.discountPrice = discountPrice;
	}
	public Long getDiscount() {
		return discount;
	}
	public void setDiscount(Long discount) {
		this.discount = discount;
	}
	public String getGdsName() {
		return gdsName;
	}
	public void setGdsName(String gdsName) {
		this.gdsName = gdsName;
	}
	public String getGdsSubHead() {
		return gdsSubHead;
	}
	public void setGdsSubHead(String gdsSubHead) {
		this.gdsSubHead = gdsSubHead;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getGdsTypeId() {
		return gdsTypeId;
	}
	public void setGdsTypeId(Long gdsTypeId) {
		this.gdsTypeId = gdsTypeId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFirstSkuId() {
		return firstSkuId;
	}
	public void setFirstSkuId(Long firstSkuId) {
		this.firstSkuId = firstSkuId;
	}
	public String getGdsDesc() {
		return gdsDesc;
	}
	public void setGdsDesc(String gdsDesc) {
		this.gdsDesc = gdsDesc;
	}

}

