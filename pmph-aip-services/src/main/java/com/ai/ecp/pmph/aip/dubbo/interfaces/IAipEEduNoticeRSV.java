/** 
 * File Name:IAipZYAuthSV.java 
 * Date:2015-11-5下午8:07:47 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.interfaces;

import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeResponse;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * 
 * Project Name:pmph-aip-services-server <br>
 * Description: 人卫e教 通知接口<br>
 * Date:2017年8月23日下午2:05:20  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IAipEEduNoticeRSV {
	/**
	 * 
	 * sendAuthRequest:通过版本编号发送商品通知请求. 
	 * 
	 * @param authRequest
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public AipEEduVNNoticeResponse sendVNNoticeRequest(AipEEduVNNoticeRequest noticeRequest)
			throws BusinessException ;
}

