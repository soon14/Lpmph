/** 
 * File Name:AipLMNetValueAddedResponse.java 
 * Date:2015-10-29上午11:09:08 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseResponseDTO;

import java.util.List;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: 根据ISBN号跳转到网络增值服务详情页面消息响应对象。<br>
 * Date:2015-10-29上午11:09:08  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipLMTONetValueAddedResponse extends BaseResponseDTO {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = -1717009755236423539L;
	
	private String Link; // 增值服务图书详情URL
	private String _ZVING_STATUS;
	private String _ZVING_MESSAGE;
	
	public String getLink() {
		return Link;
	}
	public void setLink(String link) {
		Link = link;
	}
	public String get_ZVING_STATUS() {
		return _ZVING_STATUS;
	}
	public void set_ZVING_STATUS(String _ZVING_STATUS) {
		this._ZVING_STATUS = _ZVING_STATUS;
	}
	public String get_ZVING_MESSAGE() {
		return _ZVING_MESSAGE;
	}
	public void set_ZVING_MESSAGE(String _ZVING_MESSAGE) {
		this._ZVING_MESSAGE = _ZVING_MESSAGE;
	}
	
	
}

