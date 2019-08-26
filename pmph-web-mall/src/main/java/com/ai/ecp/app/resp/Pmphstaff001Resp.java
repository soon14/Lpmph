/** 
 * File Name:DemoResp.java 
 * Date:2016-2-22下午6:53:17 
 * 
 */ 
package com.ai.ecp.app.resp;

import com.ailk.butterfly.app.model.IBody;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 用户注册出参<br>
 * Date:2016-2-22下午6:53:17  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class Pmphstaff001Resp extends IBody {
    
    private String tocken;
    
    private String success;
    
    private String message;
    
    private String staffCode;
    
    private String mobile;
    
    private Long staffId;
    
    public String getTocken() {
        return tocken;
    }

    public void setTocken(String tocken) {
        this.tocken = tocken;
    }

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}

