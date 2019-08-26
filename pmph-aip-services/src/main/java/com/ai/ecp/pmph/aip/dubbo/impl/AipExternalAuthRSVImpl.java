package com.ai.ecp.pmph.aip.dubbo.impl;

import javax.annotation.Resource;

import com.ai.ecp.aip.dubbo.impl.AipAbstractRSVImpl;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthResponse;
import com.ai.ecp.pmph.aip.dubbo.interfaces.IAipExternalAuthRSV;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipExternalAuthSV;
import com.ai.ecp.server.front.exception.BusinessException;

public class AipExternalAuthRSVImpl extends AipAbstractRSVImpl implements IAipExternalAuthRSV {
	
	// 远程授权接口。
	@Resource
	private IAipExternalAuthSV aipExternalAuthSV;
	
	@Override
	public AipExternalAuthResponse sendAuthRequest(AipExternalAuthRequest authRequest) 
			throws BusinessException {
		return aipExternalAuthSV.sendAuthRequest(authRequest);
	}
}
