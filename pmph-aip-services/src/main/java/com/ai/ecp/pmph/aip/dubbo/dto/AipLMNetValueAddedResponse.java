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
 * Description: 根据ISBN号获取网络增值服务消息响应对象。<br>
 * Date:2015-10-29上午11:09:08  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipLMNetValueAddedResponse extends BaseResponseDTO {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = -1717009755236423539L;
	
	private String isbn;
	private int  cnt;
	private String content;
	private List<AipLMNetValueAddedDetail> resources;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public List<AipLMNetValueAddedDetail> getResources() {
		return resources;
	}

	public void setResources(List<AipLMNetValueAddedDetail> resources) {
		this.resources = resources;
	}
	
	
	

}

