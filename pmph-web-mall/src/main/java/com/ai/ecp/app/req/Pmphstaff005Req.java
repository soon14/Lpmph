package com.ai.ecp.app.req;

import com.ailk.butterfly.app.model.IBody;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 重置密码-入参<br>
 * Date:2016-2-22下午6:52:57  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class Pmphstaff005Req extends IBody {
  
	private String mobile;//手机号码
	
	private String checkCode;//短信验证码
	
	private String newPwd;//新密码

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	

	

}

