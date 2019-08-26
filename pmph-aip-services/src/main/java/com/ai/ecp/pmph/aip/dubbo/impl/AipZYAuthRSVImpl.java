package com.ai.ecp.pmph.aip.dubbo.impl;

import com.ai.ecp.aip.dubbo.impl.AipAbstractRSVImpl;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthResponse;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipZYAuthSV;
import com.ai.ecp.server.front.exception.BusinessException;

import javax.annotation.Resource;

public class AipZYAuthRSVImpl extends AipAbstractRSVImpl implements com.ai.ecp.pmph.aip.dubbo.interfaces.IAipZYAuthRSV {
	
	// 远程授权接口。
	//private static final String REMOTE_AUTH_URL = "http://books123456789.ipmph.com/books/youxinAuth.zaction";
	@Resource
	private IAipZYAuthSV aipZYAuthSV;

	@Override
	public AipZYAuthResponse sendAuthRequest(AipZYAuthRequest authRequest)
			throws BusinessException {
		return aipZYAuthSV.sendAuthRequest(authRequest);
	}
	
	@Override
	public AipZYAuthResponse sendExaminationRequest(AipZYAuthRequest authRequest)
	        throws BusinessException {
	    return aipZYAuthSV.sendExaminationRequest(authRequest);
	}
	
}

