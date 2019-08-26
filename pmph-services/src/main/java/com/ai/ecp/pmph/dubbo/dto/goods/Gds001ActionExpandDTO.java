package com.ai.ecp.pmph.dubbo.dto.goods;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * Project Name:pmph-services-server <br>
 * Description:产品001接口额外对象。 <br>
 * Date:2016年11月14日下午8:07:07  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class Gds001ActionExpandDTO implements Serializable{
    
    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 326141905234365125L;

    private boolean existOtherBook =false;
    
    private Long correspondingGdsId;
    
    private Long correspondingSkuId;
    /**
     * 对应商品分类描述。
     */
    private String coorespondingCatgName;
    
    /**
     * 最终折扣价。
     */
    private BigDecimal discountFinalPrice;

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

    public String getCoorespondingCatgName() {
        return coorespondingCatgName;
    }

    public void setCoorespondingCatgName(String coorespondingCatgName) {
        this.coorespondingCatgName = coorespondingCatgName;
    }

    public BigDecimal getDiscountFinalPrice() {
        return discountFinalPrice;
    }

    public void setDiscountFinalPrice(BigDecimal discountFinalPrice) {
        this.discountFinalPrice = discountFinalPrice;
    }


}

