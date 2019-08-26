/** 
 * File Name:AipLMNetValueAddedRSVImpl.java 
 * Date:2015-10-29下午3:15:51 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.impl;

import com.ai.ecp.aip.dubbo.impl.AipAbstractRSVImpl;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipLMNetValueAddedSV;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedResponse;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMTONetValueAddedResponse;
import com.ai.ecp.server.front.exception.BusinessException;

import javax.annotation.Resource;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: 获取雷鸣网络增值服务服务实现类。<br>
 * Date:2015-10-29下午3:15:51  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipLMNetValueAddedRSVImpl extends AipAbstractRSVImpl implements com.ai.ecp.pmph.aip.dubbo.interfaces.IAipLMNetValueAddedRSV {
	
	@Resource
	private IAipLMNetValueAddedSV aipLMNetValueAddedSV;

	/** 
	 * TODO 简单描述该方法的实现功能（可选）. 
	 * @see com.ai.ecp.pmph.aip.dubbo.interfaces.IAipLMNetValueAddedRSV#requestResource(com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedRequest)
	 */
	@Override
	public AipLMNetValueAddedResponse requestResource(
			AipLMNetValueAddedRequest request) throws BusinessException {
		return aipLMNetValueAddedSV.requestResource(request);
	}

	/**
	 * 获取详情页接口
	 */
	@Override
	public AipLMTONetValueAddedResponse requestLink(AipLMNetValueAddedRequest request) throws BusinessException {
		return aipLMNetValueAddedSV.requestLink(request);
	}
	
	
}

