package com.ailk.ecp.pmph.vo;

import java.io.Serializable;

public class BizContentVO implements Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 1L;
    
    /**
     * 入参集合，JSON形式
     */
    private String biz_content;

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }
}

