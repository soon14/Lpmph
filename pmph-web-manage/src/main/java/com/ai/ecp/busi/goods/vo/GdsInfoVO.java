package com.ai.ecp.busi.goods.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * Project Name:ecp-web-manage <br>
 * Description: 新增保存商品时候的用于接收页面传过来的入参参数VO<br>
 * Date:2015年8月31日下午9:49:44  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class GdsInfoVO  implements Serializable{
    /** 
     * serialVersionUID:TODO(商品录入、编辑的时候用于接收接收js传参). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 3180590697904204126L;
    /**
     * 商品Id
     */
    private Long id;
    /**
     * 快照编码
     */
    private Long snapId;
    /**
     * 店铺id
     */
    private Long shopIdVal;
    
    /**
     * 商品名称
     */
    @NotBlank(message="{goods.gdsInfoVO.gdsName.null.error}")
    @Length(min=0,max=128, message="{goods.gdsInfoVO.gdsName.length.error}")
    private String gdsName;
    /**
     * 商品副标题
     */
    @Length(min=0,max=100, message="{goods.gdsInfoVO.gdsSubHead.length.error}")
    private String gdsSubHead;
    /**
     * 商品知道价(产品价格)
     */
    @NotNull(message="{goods.gdsInfoVO.guidePrice.null.error}")
//    @DecimalMin(value="0",message="{goods.gdsInfoVO.minBuyCnt.0.error}")
//    @Pattern(regexp="/^\\d+(\\.\\d{1,2})?$/",message="价格格式不合法，请保留小数点后两位")
    private String guidePrice;
    /**
     * 商品描述
     */
    private String gdsDesc;
    /**
     * 包装清单
     */
    private String gdsPartlist;
    /**
     * 商品类型
     */
    @NotNull(message="{goods.gdsInfoVO.gdsTypeId.null.error}")
    private Long gdsTypeId;
    
    private String gdsTypeName;
    
    /**
     * 店铺编码
     */
    private Long  shopId;
    /**
     * 商品标签
     */
    private String gdsLabel;
    /**
     * 自动上架时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date putonTime;
    
    /**
     * 自动下架时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date putoffTime;

    /**
     * 是否赠送积分
     */
    private String ifSendscore;

    /**
     * 是否单独销售
     */
    private String ifSalealone;

    /**
     * 是否推荐
     */
    private String ifRecomm;
    /**
     * 是否新品
     */
    private String ifNew;
    /**
     * 是否开启存量过低提醒
     */
    private String ifStocknotice;
    /**
     * 是否免邮
     */
    private String ifFree;
    /**
     * 是否开启分仓库存
     */
    private String ifDisperseStock;
    /**
     * 是否开启高级价格
     */
    private String ifSeniorPrice;
    /**
     * 是否阶梯价
     */
    private String ifLadderPrice;
    /**
     * 实体编码策略
     */
    private String ifEntityCode;
    /**
     * 运费模板编码
     */
    private Long shipTemplateId;
    /**
     * 供货商
     */
    private Long supplierId;
    /**
     * 平台分类
     */
    private String getCategoryParam;
    /**
     * 店铺分类
     */
    private String getShopCategoryParam;
    /**
     * 自动加载的参数（basics）
     */
    private String autoParam;
    /**
     * 单品参数（包含价格、库存、单品图片）
     */
    private String skuParam;
    /**
     * 商品图片
     */
    private String pictrueParam;
    /**
     * 阶梯价格
     */
    private String ladderPriceParam;
    /**
     * 选中的属性
     */
    private String checkedParam;
    /**
     * 企业编码
     */
    private Long companyId;
    /**
     * 复制商品的标识。1 是复制
     */
    private String copyFlag;
    /**
     * 积分商城的价格方式参数
     */
    private String scorePriceParam;
    /**
     * 分类编码
     */
    private String catgCode;
    /**
     * 分类编码名称
     */
    private String catgName;
    /**
     * ISBN号，在核心版本中被定义为商品编号。
     */
    private String isbn;
    /**
     * 商品状态
     */
    private String gdsStatus;
    /**
     * 扩展字段。
     */
    private String ext1;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSnapId() {
        return snapId;
    }
    public void setSnapId(Long snapId) {
        this.snapId = snapId;
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
    
    public String getGuidePrice() {
        return guidePrice;
    }
    public void setGuidePrice(String guidePrice) {
        this.guidePrice = guidePrice;
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
    public Long getGdsTypeId() {
        return gdsTypeId;
    }
    public void setGdsTypeId(Long gdsTypeId) {
        this.gdsTypeId = gdsTypeId;
    }
    
    public String getGdsTypeName() {
        return gdsTypeName;
    }
    public void setGdsTypeName(String gdsTypeName) {
        this.gdsTypeName = gdsTypeName;
    }
    public Long getShopId() {
        return shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public String getGdsLabel() {
        return gdsLabel;
    }
    public void setGdsLabel(String gdsLabel) {
        this.gdsLabel = gdsLabel;
    }
    
    public Date getPutonTime() {
        return putonTime;
    }
    public void setPutonTime(Date putonTime) {
        this.putonTime = putonTime;
    }
    public Date getPutoffTime() {
        return putoffTime;
    }
    public void setPutoffTime(Date putoffTime) {
        this.putoffTime = putoffTime;
    }
    public String getIfSendscore() {
        return ifSendscore;
    }
    public void setIfSendscore(String ifSendscore) {
        this.ifSendscore = ifSendscore;
    }
    public String getIfSalealone() {
        return ifSalealone;
    }
    public void setIfSalealone(String ifSalealone) {
        this.ifSalealone = ifSalealone;
    }
    public String getIfRecomm() {
        return ifRecomm;
    }
    public void setIfRecomm(String ifRecomm) {
        this.ifRecomm = ifRecomm;
    }
    public String getIfNew() {
        return ifNew;
    }
    public void setIfNew(String ifNew) {
        this.ifNew = ifNew;
    }
    public String getIfStocknotice() {
        return ifStocknotice;
    }
    public void setIfStocknotice(String ifStocknotice) {
        this.ifStocknotice = ifStocknotice;
    }
    public String getIfFree() {
        return ifFree;
    }
    public void setIfFree(String ifFree) {
        this.ifFree = ifFree;
    }
    public String getIfDisperseStock() {
        return ifDisperseStock;
    }
    public void setIfDisperseStock(String ifDisperseStock) {
        this.ifDisperseStock = ifDisperseStock;
    }
    public String getIfLadderPrice() {
        return ifLadderPrice;
    }
    public void setIfLadderPrice(String ifLadderPrice) {
        this.ifLadderPrice = ifLadderPrice;
    }
    public String getIfEntityCode() {
        return ifEntityCode;
    }
    public void setIfEntityCode(String ifEntityCode) {
        this.ifEntityCode = ifEntityCode;
    }
    public Long getShipTemplateId() {
        return shipTemplateId;
    }
    public void setShipTemplateId(Long shipTemplateId) {
        this.shipTemplateId = shipTemplateId;
    }
    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    public String getGetCategoryParam() {
        return getCategoryParam;
    }
    public void setGetCategoryParam(String getCategoryParam) {
        this.getCategoryParam = getCategoryParam;
    }
    public String getGetShopCategoryParam() {
        return getShopCategoryParam;
    }
    public void setGetShopCategoryParam(String getShopCategoryParam) {
        this.getShopCategoryParam = getShopCategoryParam;
    }
    public String getAutoParam() {
        return autoParam;
    }
    public void setAutoParam(String autoParam) {
        this.autoParam = autoParam;
    }
    public String getSkuParam() {
        return skuParam;
    }
    public void setSkuParam(String skuParam) {
        this.skuParam = skuParam;
    }
    public String getPictrueParam() {
        return pictrueParam;
    }
    public void setPictrueParam(String pictrueParam) {
        this.pictrueParam = pictrueParam;
    }
    public String getLadderPriceParam() {
        return ladderPriceParam;
    }
    public void setLadderPriceParam(String ladderPriceParam) {
        this.ladderPriceParam = ladderPriceParam;
    }
    public String getIfSeniorPrice() {
        return ifSeniorPrice;
    }
    public void setIfSeniorPrice(String ifSeniorPrice) {
        this.ifSeniorPrice = ifSeniorPrice;
    }
    public Long getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
    public String getCopyFlag() {
        return copyFlag;
    }
    public void setCopyFlag(String copyFlag) {
        this.copyFlag = copyFlag;
    }
    public String getCheckedParam() {
        return checkedParam;
    }
    public void setCheckedParam(String checkedParam) {
        this.checkedParam = checkedParam;
    }
    public String getScorePriceParam() {
        return scorePriceParam;
    }
    public void setScorePriceParam(String scorePriceParam) {
        this.scorePriceParam = scorePriceParam;
    }
    public String getCatgCode() {
        return catgCode;
    }
    public void setCatgCode(String catgCode) {
        this.catgCode = catgCode;
    }
    public String getCatgName() {
        return catgName;
    }
    public void setCatgName(String catgName) {
        this.catgName = catgName;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
	public Long getShopIdVal() {
		return shopIdVal;
	}
	public void setShopIdVal(Long shopIdVal) {
		this.shopIdVal = shopIdVal;
	}
	public String getGdsStatus() {
		return gdsStatus;
	}
	public void setGdsStatus(String gdsStatus) {
		this.gdsStatus = gdsStatus;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
    
    
}

