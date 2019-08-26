package com.ai.ecp.app.req;

import com.ailk.butterfly.app.model.IBody;

/**
 * 
 * Project Name:pmph-web-mall <br>
 * Description: 第三方登录：通过第三方帐号查询或生成平台的用户入参<br>
 * Date:2017年2月16日下午3:08:33  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class Pmphstaff006Req extends IBody {
  
	//平台编码，目前我们就三种平台：
	//1、微信：Wechat；2、腾讯：QQ；3、新浪微博：Weibo
	private String platCode;
	
	private String platUID;//平台唯一id
	
	private String publicPlatUID;//公用平台用户唯一id
	
	//一个新的第三方帐号进来，没有在SSO建用户时，如果操作
	//AutoCreate:自动创建，此参数适用于不绑定，直接登录
	//BindExistUser:绑定已有商城用户，此参数适用于绑定商城用户，这里需要输入用户密码
	private String noUserAction;
	
	private String loginId;//要绑定的商城的用户id
	
	private String password;//要绑定的商城的用户密码

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	public String getPlatUID() {
		return platUID;
	}

	public void setPlatUID(String platUID) {
		this.platUID = platUID;
	}

	public String getPublicPlatUID() {
		return publicPlatUID;
	}

	public void setPublicPlatUID(String publicPlatUID) {
		this.publicPlatUID = publicPlatUID;
	}

	public String getNoUserAction() {
		return noUserAction;
	}

	public void setNoUserAction(String noUserAction) {
		this.noUserAction = noUserAction;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

