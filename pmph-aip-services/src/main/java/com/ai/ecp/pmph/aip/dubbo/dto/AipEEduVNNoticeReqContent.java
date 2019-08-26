package com.ai.ecp.pmph.aip.dubbo.dto;


/**
 * 
 * Project Name:pmph-aip-services-server <br>
 * Description: 本版编号通知请求内容对象  VN:versionNumber<br>
 * Date:2017年8月23日下午5:23:11  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class AipEEduVNNoticeReqContent extends AipAbstractRequest{
    
    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -7247488729651459240L;
    
    /**
     * 通知类型 0:修改
     */
    private String noteicetype;
    
    /**
     * 本版编号
     */
    private String key;

    public String getNoteicetype() {
        return noteicetype;
    }

    public void setNoteicetype(String noteicetype) {
        this.noteicetype = noteicetype;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
}

