package com.ailk.ecp.pmph.vo;

import java.io.Serializable;

public class IsbnScoreVO implements Serializable{

    /** 
     * serialVersionUID:
     */ 
    private static final long serialVersionUID = -1045773240308027927L;
    
    private String staffCode;
    
    private String isbn;
    
    private String bookCode;
    
    private String bbCode;//本版编码

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

	public String getBbCode() {
		return bbCode;
	}

	public void setBbCode(String bbCode) {
		this.bbCode = bbCode;
	}
    
}

