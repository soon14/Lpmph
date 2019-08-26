/** 
 * File Name:AipLMNetValueAddedRequest.java 
 * Date:2015-10-29上午10:59:28 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.dto;


import com.ai.ecp.server.front.dto.BaseInfo;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description:根据ISBN号获取雷鸣网络增值服务请求 <br>
 * Date:2015-10-29上午10:59:28  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipLMNetValueAddedRequest extends BaseInfo {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = -7381663623019946162L;
	/**
	 * 书本ISBN号。
	 */
	private String isbn;
	/**
	 * 获取增值服务数量。
	 */
	private Integer cnt;
	/**
	 * 网络增值服务访问地址。
	 */
	private String reqUrl;
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public String getReqUrl() {
		return reqUrl;
	}
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

}

