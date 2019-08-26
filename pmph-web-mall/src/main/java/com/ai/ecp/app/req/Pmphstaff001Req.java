/** 
 * File Name:DemoReq.java 
 * Date:2016-2-22下午6:52:57 
 * 
 */ 
package com.ai.ecp.app.req;

import java.sql.Timestamp;

import com.ailk.butterfly.app.model.IBody;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 用户登录入参<br>
 * Date:2016-2-22下午6:52:57  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class Pmphstaff001Req extends IBody {
    
	   private String loginCode;

	    private String password;

	    public String getLoginCode() {
	        return loginCode;
	    }

	    public void setLoginCode(String loginCode) {
	        this.loginCode = loginCode;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
    
}

