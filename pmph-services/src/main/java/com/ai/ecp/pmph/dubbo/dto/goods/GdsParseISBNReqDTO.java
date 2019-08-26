package com.ai.ecp.pmph.dubbo.dto.goods;

import java.io.Serializable;

public class GdsParseISBNReqDTO implements Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 8658676274134630190L;
    /**
     * ISBN号 ,目前用来传的是1002的，即包含ISBN开头 和/R的
     */
    private String isbn;
    /**
     * 类型。1：纸质书，0：电子书
     */
    private String type;
    /**
     * 标准isbn号
     */
    private String biazhunisbn;
    /**
     * 商品编码
     */
    private Long gdsId;
    /**
     * 当前商品分类编码
     */
    private String catgCode;
    /**
     * 单品编码
     */
    private Long skuId;
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBiazhunisbn() {
        return biazhunisbn;
    }

    public void setBiazhunisbn(String biazhunisbn) {
        this.biazhunisbn = biazhunisbn;
    }

    public Long getGdsId() {
        return gdsId;
    }

    public void setGdsId(Long gdsId) {
        this.gdsId = gdsId;
    }

    public String getCatgCode() {
        return catgCode;
    }

    public void setCatgCode(String catgCode) {
        this.catgCode = catgCode;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
    
}

