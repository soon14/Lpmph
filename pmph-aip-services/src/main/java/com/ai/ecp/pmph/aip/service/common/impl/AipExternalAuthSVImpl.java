package com.ai.ecp.pmph.aip.service.common.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ai.ecp.pmph.aip.dubbo.constants.PmphConstants;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthResponse;
import com.ai.ecp.pmph.aip.dubbo.utils.HttpUtil;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipExternalAuthSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Project Name:pmph-services-aip-server <br>
 * Description: <br>
 * Date:2018-06-21下午8:09:48 <br>
 * 
 * @version
 * @since JDK 1.7
 */
public class AipExternalAuthSVImpl implements IAipExternalAuthSV {
	
	private static final String MODULE = AipExternalAuthSVImpl.class.getName();
	
	/**
     * TODO 简单描述该方法的实现功能（可选）.
     * 
     * @see com.ai.ecp.aip.service.common.interfaces.IAipExternalAuthSV#sendAuthRequest(com.ai.ecp.aip.dubbo.dto.AipExternalAuthRequest)
     */
	@Override
	public AipExternalAuthResponse sendAuthRequest(AipExternalAuthRequest authRequest) throws BusinessException {
		LogUtil.info(MODULE, "向外部系统授权接口发送授权请求！入参:" + ToStringBuilder.reflectionToString(authRequest));
		
		if (null == authRequest) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "authRequest" });
        }

        if (StringUtil.isEmpty(authRequest.getAuthUrl())) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "authUrl" });
        }
        
        AipExternalAuthResponse resp = null;
        try{
        	Map<String, String> dataMap = buildDataMap(authRequest);
        	LogUtil.info(MODULE, "dataMap:" + dataMap);
        	
        	String responseJson = HttpUtil.postForm(authRequest.getAuthUrl(), dataMap, null, null);
        	LogUtil.info(MODULE, "调用外部系统提供临床/用药/约健康授权服务返回报文:" + responseJson);
        	resp = JSONObject.parseObject(responseJson, AipExternalAuthResponse.class);
        	resp.setResponseMessage(responseJson);
            resp.setRequestMessage(JSONObject.toJSONString(dataMap));
        }catch(BusinessException be){
        	throw new BusinessException(be.getErrorMessage());
        }catch(Exception e){
        	throw new BusinessException(PmphConstants.ErrorCode.EXTERNALAUTH_C30300);
        }
		return resp;
	}
	
	private Map<String, String> buildDataMap(AipExternalAuthRequest authRequest){
		Map<String, String> formData = new HashMap<String, String>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtil.isNotBlank(authRequest.getStaff_code())) {
            map.put("staff_code", authRequest.getStaff_code());
        }
        if (StringUtil.isNotBlank(authRequest.getGds_detail())) {
        	String gds_detail =authRequest.getGds_detail();
        	Object gdsDetail = JSONArray.parse(gds_detail);
            map.put("gds_detail", gdsDetail);
        }
        if (StringUtil.isNotBlank(authRequest.getOrder_code())) {
            map.put("order_code", authRequest.getOrder_code());
        }
        if (StringUtil.isNotBlank(authRequest.getOrder_time())) {
            map.put("order_time", authRequest.getOrder_time());
        }
        if (StringUtil.isNotBlank(authRequest.getPay_time())) {
            map.put("pay_time", authRequest.getPay_time());
        }
        if (StringUtil.isNotBlank(authRequest.getPay_way())) {
            map.put("pay_way", authRequest.getPay_way());
        }
        if (MapUtils.isNotEmpty(map)) {
        	String biz_content = JSONObject.toJSONString(map);
        	formData.put("biz_content", biz_content);
        }
		return formData;
	}
}
