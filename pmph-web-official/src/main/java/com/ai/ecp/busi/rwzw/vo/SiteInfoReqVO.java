package com.ai.ecp.busi.rwzw.vo;

import java.io.Serializable;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

/**网站信息请求服务VO
 * Project Name:ecp-web-manage <br>
 * Description: <br>
 * Date:2015年12月80日下午10:49:47 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class SiteInfoReqVO extends EcpBasePageReqVO implements Serializable {

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -3176027921638674705L;

    /** 
     * 站点ID. 
     */ 
    private Long siteId;
    
    /** 
     * 状态. 0，无效 1，有效 
     */
    private String status;
    
    /**
     * 网站信息类型  01 静态文件  02 文章列表 
     */
    private String siteInfoType;

    /**
     * 组件指定返回的url
     */
    private String url ;
    
    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSiteInfoType() {
        return siteInfoType;
    }

    public void setSiteInfoType(String siteInfoType) {
        this.siteInfoType = siteInfoType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
}
