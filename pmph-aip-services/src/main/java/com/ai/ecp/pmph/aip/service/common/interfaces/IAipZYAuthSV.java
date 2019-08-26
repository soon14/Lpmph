/** 
 * File Name:IAipZYAuthSV.java 
 * Date:2015-11-5下午8:07:47 
 * 
 */ 
package com.ai.ecp.pmph.aip.service.common.interfaces;

import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthResponse;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: <br>
 * Date:2015-11-5下午8:07:47  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IAipZYAuthSV {
	/**
	 * 
	 * sendAuthRequest:发送授权请求. 
	 * 
	 * @param authRequest
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public AipZYAuthResponse sendAuthRequest(AipZYAuthRequest authRequest)
			throws BusinessException ;

	/**
     * 
     * sendExaminationRequest:向泽元提考试网服务发起授权请求。 <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param authRequest
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public AipZYAuthResponse sendExaminationRequest(AipZYAuthRequest authRequest)throws BusinessException;
}

