package com.ai.ecp.pmph.dubbo.dto.goods;

import com.ai.ecp.server.front.dto.BaseInfo;

public class GdsCatgCodeReqDTO extends BaseInfo{

    
    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 2864230131740486617L;
    /**
     * 分类编码id
     */
    private String catgCode;
    /**
     * 分类名称
     */
    private String catgName;
    /**
     * 是否选中 1：选择，0：未选中
     */
    private String checked;
    /**
     * 商品编码
     */
    private Long gdsId;
    /**
     * 单品编码
     */
    private Long skuId;
    
    public String getCatgName() {
        return catgName;
    }
    public void setCatgName(String catgName) {
        this.catgName = catgName;
    }
    public String getChecked() {
        return checked;
    }
    public void setChecked(String checked) {
        this.checked = checked;
    }
    public Long getGdsId() {
        return gdsId;
    }
    public void setGdsId(Long gdsId) {
        this.gdsId = gdsId;
    }
    public Long getSkuId() {
        return skuId;
    }
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
    public String getCatgCode() {
        return catgCode;
    }
    public void setCatgCode(String catgCode) {
        this.catgCode = catgCode;
    }
    
    
}

