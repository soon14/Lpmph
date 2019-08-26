/** 
 * File Name:AipZYAuthSVImpl.java 
 * Date:2015-11-5下午8:09:48 
 * 
 */
package com.ai.ecp.pmph.aip.dubbo.impl;

import javax.annotation.Resource;

import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeResponse;
import com.ai.ecp.pmph.aip.dubbo.interfaces.IAipEEduNoticeRSV;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipEEduNoticeSV;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: <br>
 * Date:2015-11-5下午8:09:48 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class AipEEduNoticeRSVImpl implements IAipEEduNoticeRSV {

    @Resource
    private IAipEEduNoticeSV aipEEduNoticeSV;
    /**
     * TODO 简单描述该方法的实现功能（可选）. 
     * @see com.ai.ecp.pmph.aip.dubbo.interfaces.IAipEEduNoticeRSV#sendVNNoticeRequest(com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeRequest)
     */
    @Override
    public AipEEduVNNoticeResponse sendVNNoticeRequest(AipEEduVNNoticeRequest noticeRequest)
            throws BusinessException {
        return aipEEduNoticeSV.sendVNNoticeRequest(noticeRequest);
    }
}
