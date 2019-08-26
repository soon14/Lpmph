/** 
 * File Name:IImageSampleChapterSVImpl.java 
 * Date:2015-10-30下午3:10:55 
 * 
 */ 
package com.ai.ecp.pmph.service.busi.interfaces.dataimport;

import java.util.Map;

import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description: 图片样章更新服务。<br>
 * Date:2015-10-30下午3:10:55  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IImageSampleChapterSV {

	/**
	 * 
	 * executeUpdate:更新图片样章。
	 * 
	 * @param map
	 * @param catgCode
	 * @param sendIdxMsg 是否发送索引更新消息.仅在值有true时才会发送索引更新消息.
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void executeUpdate(Map<String, Object> map, String catgCode, Boolean sendIdxMsg)throws BusinessException;
	/**
	 * 
	 * executeUpdateCheck:更新前检查,用于通用图片上传组件在图片上传前的检测.检测系统中是否有商品与该图片匹配.
	 * 
	 * @param fileName
	 * @param catgCode
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public boolean executeUpdateCheck(String fileName, String catgCode) throws BusinessException;

}

