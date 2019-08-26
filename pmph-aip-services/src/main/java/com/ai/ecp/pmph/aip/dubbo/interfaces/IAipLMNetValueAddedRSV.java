/** 
 * File Name:IAipLMNetValueAddedRSV.java 
 * Date:2015-10-29上午10:56:52 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.interfaces;

import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedResponse;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMTONetValueAddedResponse;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: 雷鸣提供网络增值服务。(根据ISBN号获取该书对应的N条网络增值服务列表信息)<br>
 * Date:2015-10-29上午10:56:52  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IAipLMNetValueAddedRSV {
	
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
      * 获取雷鸣网络增值服务详情页接口。
      * @return
      * @throws BusinessException
      */
     public AipLMTONetValueAddedResponse requestLink(AipLMNetValueAddedRequest request) throws BusinessException;
}

