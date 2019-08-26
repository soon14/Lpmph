package com.ai.ecp.busi.rwgw.vo;

import java.io.Serializable;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

/**页面组件请求服务VO
 * Project Name:ecp-web-manage <br>
 * Description: <br>
 * Date:2015年11月20日下午5:49:47 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ComponentReqVO extends EcpBasePageReqVO implements Serializable {

    /** 
     * 站点ID. 
     */ 
    private Long siteId;
    /** 
     * 模板ID. 
     */ 
    private Long templateId;
    /** 
     * 内容位置ID. 
     */ 
    private Long placeId;
    /** 
     * 楼层广告内容位置ID. 
     */ 
    private Long adPlaceId;
    /** 
     * 楼层D . 
     */ 
    private Long floorId;
    /** 
     * 页签ID. 
     */ 
    private Long tabId;
    /** 
     * 内容位置中元素显示个数. 
     */ 
    private Integer placeSize;
    /** 
     * 楼层中商品显示个数. 
     */ 
    private Integer gdsSize;
    /** 
     * 楼层中页签显示个数. 
     */
    private Integer tabSize;
    /**
     * 楼层中图片显示个数. 
     */
    private Integer floorImgSize;
    /**
     *  楼层中图片显示宽度. 
     */
    private Long floorImgWidth;
    /**
     * 楼层中图片显示高度. 
     */
    private Long floorImgHeight;
    /** 
     * 内容位置宽. 
     */
    private Long placeWidth;
    /** 
     * 内容位置高. 
     */
    private Long placeHeight;
    /** 
     * 状态. 0，无效 1，有效 
     */
    private String status;
    /** 
     * 位置类型
     */
    private String placeType;
    /** 
     * 推荐类型
     */
    private String recommendType;
    
    /** 
     * 分类菜单类型
     */
    private String menuType;
    
    /** 
     * 分类ID
     */
    private String catalogId;
    
    /** 
     * 网站信息ID
     */
    private Long siteInfoId;
    
    /** 
     * 静态文件ID
     */
    private String staticId;
    /** 
     * 站点信息类型
     */
    private String siteInfoType;
    /** 
     * 文章ID
     */
    private String articleId;
    /** 
     * 栏目ID. 
     */ 
    private Long channelId;
    /** 
     * 首页是否显示
     */
    private String homepageIsShow;
    /** 
     * 平台类型
     */
    private String platformType;
    /** 
     * 栏目类型
     */
    private String channelType;
    
    /**
     * 返回的url
     */
    private String returnUrl;
    
    private String keyword;
    
    private static final long serialVersionUID = 1L;

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getTemplateId() {
        return templateId;
    }
    
    public Long getTabId() {
        return tabId;
    }

    public void setTabId(Long tabId) {
        this.tabId = tabId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getPlaceWidth() {
        return placeWidth;
    }

    public void setPlaceWidth(Long placeWidth) {
        this.placeWidth = placeWidth;
    }

    public Integer getPlaceSize() {
        return placeSize;
    }

    public void setPlaceSize(Integer placeSize) {
        this.placeSize = placeSize;
    }

    public Long getPlaceHeight() {
        return placeHeight;
    }

    public void setPlaceHeight(Long placeHeight) {
        this.placeHeight = placeHeight;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Integer getGdsSize() {
        return gdsSize;
    }

    public void setGdsSize(Integer gdsSize) {
        this.gdsSize = gdsSize;
    }

    public Integer getTabSize() {
        return tabSize;
    }

    public void setTabSize(Integer tabSize) {
        this.tabSize = tabSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    
    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }
    
    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }
    
    public Integer getFloorImgSize() {
        return floorImgSize;
    }

    public void setFloorImgSize(Integer floorImgSize) {
        this.floorImgSize = floorImgSize;
    }

    public Long getFloorImgWidth() {
        return floorImgWidth;
    }

    public void setFloorImgWidth(Long floorImgWidth) {
        this.floorImgWidth = floorImgWidth;
    }

    public Long getFloorImgHeight() {
        return floorImgHeight;
    }

    public void setFloorImgHeight(Long floorImgHeight) {
        this.floorImgHeight = floorImgHeight;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
    
    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }
    
    public Long getAdPlaceId() {
        return adPlaceId;
    }

    public void setAdPlaceId(Long adPlaceId) {
        this.adPlaceId = adPlaceId;
    }
    
    public Long getSiteInfoId() {
        return siteInfoId;
    }

    public void setSiteInfoId(Long siteInfoId) {
        this.siteInfoId = siteInfoId;
    }
    public String getStaticId() {
        return staticId;
    }

    public void setStaticId(String staticId) {
        this.staticId = staticId;
    }

    public String getSiteInfoType() {
        return siteInfoType;
    }

    public void setSiteInfoType(String siteInfoType) {
        this.siteInfoType = siteInfoType;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
    
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getHomepageIsShow() {
        return homepageIsShow;
    }

    public void setHomepageIsShow(String homepageIsShow) {
        this.homepageIsShow = homepageIsShow;
    }
    
    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }
    
    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
