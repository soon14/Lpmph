package com.ailk.ecp.pmph.vo;

import java.io.Serializable;
/**
 * 
 * Project Name:pmph-aip-web <br>
 * Description: 电子图书馆图书编号更新操作。<br>
 * Date:2016年8月12日下午2:26:14  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class EBookIDSyncVO implements Serializable{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 333720537100160189L;
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

