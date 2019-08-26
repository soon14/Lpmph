/** 
 * File Name:IAipZYAuthRSV.java 
 * Date:2015-10-28下午7:51:23 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.interfaces;

import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthResponse;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: 泽元授权接口调用服务。<br>
 * Date:2015-10-28下午7:51:23  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IAipZYAuthRSV {
	/**
	 * 
	 * sendAuthRequest:向泽元提供数字教材/电子书授权服务发起授权请求。<br/> 
	 * 
	 * @param authRequest
	 * @param authUrl
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
    public AipZYAuthResponse sendAuthRequest(AipZYAuthRequest authRequest)throws BusinessException;
    
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

