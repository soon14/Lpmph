/** 
 * File Name:IAipLMNetValueAddedRSV.java 
 * Date:2015-11-5下午8:39:15 
 * 
 */ 
package com.ai.ecp.pmph.aip.service.common.interfaces;

import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedResponse;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMTONetValueAddedResponse;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: 网络增值服务。<br>
 * Date:2015-11-5下午8:39:15  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IAipLMNetValueAddedSV {
        
	/**
	 * 
	 * 获取雷鸣网络增值服务服务接口。 
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
     public AipLMNetValueAddedResponse requestResource(AipLMNetValueAddedRequest request) throws BusinessException;

     /**
      * 获取雷鸣网络增值服务详情服务接口
      * @param request
      * @return
      */
	public AipLMTONetValueAddedResponse requestLink(AipLMNetValueAddedRequest request) throws BusinessException;
}

