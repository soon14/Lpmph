package com.ailk.ecp.pmph.vo;

import java.io.Serializable;

public class ScoreCalVO implements Serializable{

    /** 
     * serialVersionUID:
     */ 
    private static final long serialVersionUID = -1045773240308027927L;
    
    private String staffCode;
    
    private Long score;//兑换积分

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

}

