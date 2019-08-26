/** 
 * File Name:EBookIDSyncReqDTO.java 
 * Date:2016年8月12日下午2:51:11 
 * 
 */ 
package com.ai.ecp.pmph.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseInfo;

/**
 * Project Name:pmph-services-server <br>
 * Description: 数字图书馆电子书编号更新请求对象。<br>
 * Date:2016年8月12日下午2:51:11  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class EBookIDSyncReqDTO extends BaseInfo {
    

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -6233434604087245986L;
    /**
     * 电子图书ID。
     */
    private String ebook_id;
    /**
     * 电子书ISBN。
     */
    private String isbn;
    /**
     * 1.新增 2.修改 3.删除.
     */
    private String operate;
    
    public String getEbook_id() {
        return ebook_id;
    }
    public void setEbook_id(String ebook_id) {
        this.ebook_id = ebook_id;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getOperate() {
        return operate;
    }
    public void setOperate(String operate) {
        this.operate = operate;
    }
    

}

