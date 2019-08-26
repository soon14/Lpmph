package com.ai.ecp.pmph.aip.dubbo.interfaces;

import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthResponse;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:pmph-services-aip-server <br>
 * Description: 外系统授权接口调用服务。<br>
 * Date:2018-06-21下午7:51:23  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public interface IAipExternalAuthRSV {
	/**
	 * 
	 * sendAuthRequest:向外系统提供临床/用药/约健康 授权服务发起授权请求。<br/> 
	 * 
	 * @param authRequest
	 * @param authUrl
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.7
	 */
	public AipExternalAuthResponse sendAuthRequest(AipExternalAuthRequest authRequest)throws BusinessException;
    
}
