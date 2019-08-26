package com.ai.ecp.app.req;

import com.ailk.butterfly.app.model.IBody;

/**
 * 
 * Project Name:pmph-web-mall <br>
 * Description: 第三方登录：通过第三方帐号绑定商城用户入参<br>
 * Date:2017年2月16日下午3:08:33  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class Pmphstaff007Req extends IBody {
  
	//平台编码，目前我们就三种平台：
	//1、微信：Wechat；2、腾讯：QQ；3、新浪微博：Weibo
	private String platCode;
	
	private String platUID;//平台唯一id
	
	private String publicPlatUID;//公用平台用户唯一id

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

}

