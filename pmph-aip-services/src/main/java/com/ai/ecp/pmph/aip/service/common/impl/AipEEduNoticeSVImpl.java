/** 
 * File Name:AipZYAuthSVImpl.java 
 * Date:2015-11-5下午8:09:48 
 * 
 */
package com.ai.ecp.pmph.aip.service.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ai.ecp.pmph.aip.dubbo.constants.PmphConstants;
import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeReqContent;
import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeResponse;
import com.ai.ecp.pmph.aip.dubbo.utils.HttpUtil;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipEEduNoticeSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: <br>
 * Date:2015-11-5下午8:09:48 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class AipEEduNoticeSVImpl implements IAipEEduNoticeSV {

    private static final String MODULE = AipEEduNoticeSVImpl.class.getName();

    /**
     * 
     * TODO 简单描述该方法的实现功能（可选）. 
     * @see com.ai.ecp.pmph.aip.service.common.interfaces.IAipEEduNoticeSV#sendVNNoticeRequest(com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeRequest)
     */
    @Override
    public AipEEduVNNoticeResponse sendVNNoticeRequest(AipEEduVNNoticeRequest noticeRequest)
            throws BusinessException {
        LogUtil.info(MODULE, "通过versionNumber给人卫e教发通知，入参: "+ToStringBuilder.reflectionToString(noticeRequest));
        if (null == noticeRequest) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "noticeRequest" });
        }
        if (StringUtil.isEmpty(noticeRequest.getReqUrl())) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "reqUrl" });
        }
        if (StringUtil.isEmpty(noticeRequest.getAct())) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "act" });
        }
        noticeRequest.setContent(filterVNReqContent(noticeRequest.getContent()));
        if (CollectionUtils.isEmpty(noticeRequest.getContent())) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "content" });
        }
        AipEEduVNNoticeResponse resp = new AipEEduVNNoticeResponse();
        Map<String, String> dataMap = buildDataMap(noticeRequest);
        LogUtil.info(MODULE, "dataMap:" + dataMap);
        try {
            String responseJson = HttpUtil.postForm(noticeRequest.getReqUrl(), dataMap, null, null);
            LogUtil.info(MODULE, "调用人卫e教通知服务返回报文:" + responseJson);
            resp.setResponseMessage(responseJson);
            if(StringUtil.isBlank(responseJson)){//返回空则认为是无异常
                responseJson = AipEEduVNNoticeRequest._ZVING_STATUS_OK;
            }
            responseJson = responseJson.trim();
            //resp = JSONObject.parseObject(responseJson, AipEEduVNNoticeResponse.class);
            resp.setStatus(responseJson);//返回的body内容就是状态编码
            resp.setMessage(responseJson);
        } catch (Exception e) {
            LogUtil.error(MODULE, "调用人卫e教通知服务请求遇到异常!", e);
            BusinessException be = new BusinessException(PmphConstants.ErrorCode.AIP_A20002,
                    StringUtil.isNotBlank(e.getMessage())?new String[] { e.getMessage() } : new String[]{""});
            resp = new AipEEduVNNoticeResponse();
            resp.setStatus(AipEEduVNNoticeRequest._ZVING_STATUS_EXCE);
            resp.setMessage(be.getErrorMessage());
        }
        resp.setRequestMessage(JSONObject.toJSONString(dataMap));
        LogUtil.info(MODULE, "通过versionNumber给人卫e教发通知，出参: "+ToStringBuilder.reflectionToString(resp));
        return resp;
    }
    /**
     * 
     * filterVNReqContent:(过滤无效的请求内容). <br/> 
     * 
     * @param content
     * @return 
     * @since JDK 1.6
     */
    private List<AipEEduVNNoticeReqContent> filterVNReqContent(List<AipEEduVNNoticeReqContent> contentList){
        List<AipEEduVNNoticeReqContent> result = new ArrayList<AipEEduVNNoticeReqContent>();
        if(CollectionUtils.isNotEmpty(contentList)){
            for(AipEEduVNNoticeReqContent content : contentList){
                if(StringUtil.isNotBlank(content.getKey()) && StringUtil.isNotBlank(content.getNoteicetype())){
                    result.add(content);
                }
            }
        }
        contentList = null;
        return result;
    }
    /**
     * 
     * buildDataMap:(构建请求参数). <br/> 
     * 
     * @param request
     * @return 
     * @since JDK 1.6
     */
    private Map<String, String> buildDataMap(AipEEduVNNoticeRequest request){
        Map<String, String> map = new HashMap<String, String>();
        List<Map<String, String>> contentMapList = new ArrayList<Map<String,String>>();
        map.put("act", request.getAct());
        List<AipEEduVNNoticeReqContent> contentList = request.getContent();
        for(AipEEduVNNoticeReqContent content : contentList){
            Map<String, String> contentMap = new HashMap<String, String>();
            contentMap.put("noteicetype", content.getNoteicetype());
            contentMap.put("key", content.getKey());
            contentMapList.add(contentMap);
        }
        map.put("content", JSONObject.toJSONString(contentMapList));
        return map;
    }
    /*public static void main(String[] args) {
        //String enParam = CryptoUtil.encrypt("27jrWz3sxrVbR+pnyg6j", json);
        AipEEduNoticeSVImpl sv = new AipEEduNoticeSVImpl();
        AipEEduVNNoticeRequest request = new AipEEduVNNoticeRequest();
        request.setAct(PmphConstants.EEduActType.SHOP);
        request.setReqUrl("http://119.254.226.115/pmph_imesp/queryAction!index.action");
        request.setContent(PmphConstants.EEduNoteiceType.UPDATE, "20170101123456");
        AipEEduVNNoticeResponse resp= sv.sendVNNoticeRequest(request);
        LogUtil.info(MODULE ,JSONObject.toJSONString(resp));
    }*/
}
