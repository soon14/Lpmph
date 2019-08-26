/** 
 * File Name:IImageSampleChapterSVImpl.java 
 * Date:2015-10-30下午3:10:55 
 * 
 */ 
package com.ai.ecp.pmph.service.busi.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;

import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description: 图片样章更新服务。<br>
 * Date:2015-10-30下午3:10:55  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IImageSampleChapterSVImpl {

	/**
	 * 
	 * executeUpdate:更新图片样章。
	 * 
	 * @param map
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void executeUpdate(Map<String, Object> map)throws BusinessException;

}

