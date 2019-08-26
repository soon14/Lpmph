package com.ai.ecp.app.resp.gds;

import java.util.List;

import com.ailk.butterfly.app.model.IBody;

public class GdsDetailBaseInfo extends IBody{


	private String shopName;

	private String stockStatus;

	private String stockStatusDesc;
	
	private Long stockLackNum;

	private Boolean ifBrowse;

	private Long saleCount;

	private String digitsPrinPrice;

	private String mainPicUrl;
	
	private String gdsName;
	//收藏id，如果被当前用户收藏，则返回
	private Long collectId;
	//0是显示电子书, 1是显示数字教材
	private String edbook;
	//出版日期
	private String appearDate;
	//分类路径
	List<GdscatgsCodeAndNameVO> cateList;
	
	
    private boolean existOtherBook =false;
    
    private Long correspondingGdsId;
    
    private Long correspondingSkuId;
    /**
     * 对应商品分类描述。
     */
    private String correspondingCatgName;
    
    /**
     * 最终折扣价。
     */
    private Long corrDiscountFinalPrice;
    
    private String author;
    /**
     * 分享地址
     */
    private String shareUrl;
    
	/**
	 * 商品副标题
	 */
	private String gdsSubHead;

	/**
	 * 商品知道价
	 */
	private Long guidePrice;

	private List<String> imageUrlList;

	/**
	 * 商品类型
	 */
	private Long gdsTypeId;

	/**
	 * 商品状态
	 */
	private String gdsStatus;
	
	/**
	 * 是否虚拟商品.
	 */
	private boolean isVirtualProduct;

	/**
	 * 店铺编码
	 */
	private Long shopId;

	private String isbn;
	/**
	 * 分类编码
	 */
	private String mainCatgs;
	/**
	 * 单品信息
	 */
	private GdsSkuBaseInfo gdsSkuBaseInfo;
	
    /**
     * 商品描述
     */
    private String gdsDesc;

    /**
     * 包装清单
     */
    private String gdsPartlist;
    
	/**
	 * 参数属性
	 */
	private List<GdsPropBaseInfo> params;
	/**
	 * 基础信息URL地址。
	 */
	private String baseInfoUrl;
	
	private String contentInfoUrl;
	
	
	private String addToCartPromp;
	
	private boolean enableAddToCart = true;

	/**
     * 是否有增值服务
     */
	private boolean hasValueAdded = false;
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getStockStatusDesc() {
		return stockStatusDesc;
	}

	public void setStockStatusDesc(String stockStatusDesc) {
		this.stockStatusDesc = stockStatusDesc;
	}

	public Boolean getIfBrowse() {
		return ifBrowse;
	}

	public void setIfBrowse(Boolean ifBrowse) {
		this.ifBrowse = ifBrowse;
	}

	public Long getSaleCount() {
		return saleCount;
	}

	public Long getCollectId() {
        return collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }
	
	public void setSaleCount(Long saleCount) {
		this.saleCount = saleCount;
	}

	public String getDigitsPrinPrice() {
		return digitsPrinPrice;
	}

	public void setDigitsPrinPrice(String digitsPrinPrice) {
		this.digitsPrinPrice = digitsPrinPrice;
	}

	public String getMainPicUrl() {
		return mainPicUrl;
	}

	public void setMainPicUrl(String mainPicUrl) {
		this.mainPicUrl = mainPicUrl;
	}

	public String getGdsSubHead() {
		return gdsSubHead;
	}

	public void setGdsSubHead(String gdsSubHead) {
		this.gdsSubHead = gdsSubHead;
	}

	public Long getGuidePrice() {
		return guidePrice;
	}

	public void setGuidePrice(Long guidePrice) {
		this.guidePrice = guidePrice;
	}

	public List<String> getImageUrlList() {
		return imageUrlList;
	}

	public void setImageUrlList(List<String> imageUrlList) {
		this.imageUrlList = imageUrlList;
	}

	public Long getGdsTypeId() {
		return gdsTypeId;
	}

	public void setGdsTypeId(Long gdsTypeId) {
		this.gdsTypeId = gdsTypeId;
	}

	public String getGdsStatus() {
		return gdsStatus;
	}

	public void setGdsStatus(String gdsStatus) {
		this.gdsStatus = gdsStatus;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public GdsSkuBaseInfo getGdsSkuBaseInfo() {
		return gdsSkuBaseInfo;
	}

	public void setGdsSkuBaseInfo(GdsSkuBaseInfo gdsSkuBaseInfo) {
		this.gdsSkuBaseInfo = gdsSkuBaseInfo;
	}

	public List<GdsPropBaseInfo> getParams() {
		return params;
	}

	public void setParams(List<GdsPropBaseInfo> params) {
		this.params = params;
	}

	public String getGdsName() {
		return gdsName;
	}

	public void setGdsName(String gdsName) {
		this.gdsName = gdsName;
	}

	public String getMainCatgs() {
		return mainCatgs;
	}

	public void setMainCatgs(String mainCatgs) {
		this.mainCatgs = mainCatgs;
	}

	public String getGdsDesc() {
		return gdsDesc;
	}

	public void setGdsDesc(String gdsDesc) {
		this.gdsDesc = gdsDesc;
	}

	public String getGdsPartlist() {
		return gdsPartlist;
	}

	public void setGdsPartlist(String gdsPartlist) {
		this.gdsPartlist = gdsPartlist;
	}

    public Long getStockLackNum() {
        return stockLackNum;
    }

    public void setStockLackNum(Long stockLackNum) {
        this.stockLackNum = stockLackNum;
    }

    public boolean isVirtualProduct() {
        return isVirtualProduct;
    }

    public void setVirtualProduct(boolean isVirtualProduct) {
        this.isVirtualProduct = isVirtualProduct;
    }

    public boolean isExistOtherBook() {
        return existOtherBook;
    }

    public void setExistOtherBook(boolean existOtherBook) {
        this.existOtherBook = existOtherBook;
    }

    public Long getCorrespondingGdsId() {
        return correspondingGdsId;
    }

    public void setCorrespondingGdsId(Long correspondingGdsId) {
        this.correspondingGdsId = correspondingGdsId;
    }

    public Long getCorrespondingSkuId() {
        return correspondingSkuId;
    }

    public void setCorrespondingSkuId(Long correspondingSkuId) {
        this.correspondingSkuId = correspondingSkuId;
    }

    public String getCorrespondingCatgName() {
        return correspondingCatgName;
    }

    public void setCorrespondingCatgName(String correspondingCatgName) {
        this.correspondingCatgName = correspondingCatgName;
    }

    public Long getCorrDiscountFinalPrice() {
        return corrDiscountFinalPrice;
    }

    public void setCorrDiscountFinalPrice(Long corrDiscountFinalPrice) {
        this.corrDiscountFinalPrice = corrDiscountFinalPrice;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBaseInfoUrl() {
        return baseInfoUrl;
    }

    public void setBaseInfoUrl(String baseInfoUrl) {
        this.baseInfoUrl = baseInfoUrl;
    }

	public String getEdbook() {
		return edbook;
	}

	public void setEdbook(String edbook) {
		this.edbook = edbook;
	}

	public String getAppearDate() {
		return appearDate;
	}

	public void setAppearDate(String appearDate) {
		this.appearDate = appearDate;
	}

	public List<GdscatgsCodeAndNameVO> getCateList() {
		return cateList;
	}

	public void setCateList(List<GdscatgsCodeAndNameVO> cateList) {
		this.cateList = cateList;
	}

	public String getContentInfoUrl() {
		return contentInfoUrl;
	}

	public void setContentInfoUrl(String contentInfoUrl) {
		this.contentInfoUrl = contentInfoUrl;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

    public boolean isHasValueAdded() {
        return hasValueAdded;
    }

    public void setHasValueAdded(boolean hasValueAdded) {
        this.hasValueAdded = hasValueAdded;
    }

    public String getAddToCartPromp() {
        return addToCartPromp;
    }

    public void setAddToCartPromp(String addToCartPromp) {
        this.addToCartPromp = addToCartPromp;
    }

    public boolean isEnableAddToCart() {
        return enableAddToCart;
    }

    public void setEnableAddToCart(boolean enableAddToCart) {
        this.enableAddToCart = enableAddToCart;
    }
    
    
}

