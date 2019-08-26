/** 
 * File Name:AipLMNetValueAddedDetail.java 
 * Date:2015-10-29下午2:37:39 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.dto;

import java.io.Serializable;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description:雷鸣网络增值服务明细。 <br>
 * Date:2015-10-29下午2:37:39  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipLMNetValueAddedDetail implements Serializable {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = -5193534295381324723L;
	/**
	 * 资源名称；中文
	 */
	private String resName;
	/**
	 * 格式；中文
	 */
	private String resFormat;
	/**
	 * 资源类型；中文
	 */
	private String resType;
	/**
	 * 资源图片路径
	 */
	private String imgPath;
	
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public String getResFormat() {
		return resFormat;
	}
	public void setResFormat(String resFormat) {
		this.resFormat = resFormat;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	
	
	
	

}

