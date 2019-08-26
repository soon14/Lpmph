package com.ai.ecp.pmph.aip.service.common.interfaces;

import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthResponse;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:pmph-services-aip-server <br>
 * Description: <br>
 * Date:2018-06-21下午8:07:47  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public interface IAipExternalAuthSV {
	/**
	 * 
	 * sendAuthRequest:发送授权请求. 
	 * 
	 * @param authRequest
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.7
	 */
	public AipExternalAuthResponse sendAuthRequest(AipExternalAuthRequest authRequest)
			throws BusinessException;
}