package com.ai.ecp.app.resp;

import com.ailk.butterfly.app.model.IBody;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 重置密码-出参<br>
 * Date:2016-2-22下午6:53:17  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class Pmphstaff005Resp extends IBody {
    
	private String success;
	
	private String Message;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	
}

