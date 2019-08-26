package com.ai.ecp.pmph.aip.dubbo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Project Name:pmph-aip-services-server <br>
 * Description: 本版编号通知请求对象  VN:versionNumber<br>
 * Date:2017年8月23日下午5:23:11  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class AipEEduVNNoticeRequest extends AipAbstractRequest{
    
    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -3525310903666323336L;
    
    /**
     * 失败!
     */
    public static final String _ZVING_STATUS_FALT = "fail";
    /**
     * 成功!
     */
    public static final String _ZVING_STATUS_OK = "success";
    /**
     * 异常!
     */
    public static final String _ZVING_STATUS_EXCE = "exception";
    /**
     * 请求模块
     */
    private String act;
    /**
     * 请求内容数据
     */
    private List<AipEEduVNNoticeReqContent> content;
    
    /**
     * 请求地址
     */
    private String reqUrl;

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public List<AipEEduVNNoticeReqContent> getContent() {
        return content;
    }

    public void setContent(List<AipEEduVNNoticeReqContent> content) {
        this.content = content;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
    /**
     * 
     * setContent:(添加请求内容数据方法，无需手工创建对象). <br/> 
     * 
     * @param noteicetype
     * @param key 
     * @since JDK 1.6
     */
    public void setContent(String noteicetype , String key) {
        if(null == this.content){
            this.content = new ArrayList<AipEEduVNNoticeReqContent>();
        }
        AipEEduVNNoticeReqContent con = new AipEEduVNNoticeReqContent();
        con.setNoteicetype(noteicetype);
        con.setKey(key);
        this.content.add(con);
    }
}

