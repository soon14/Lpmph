/** 
 * File Name:AipLMNetValueAddedSVImpl.java 
 * Date:2015-11-5下午10:09:12 
 * 
 */ 
package com.ai.ecp.pmph.aip.service.common.impl;

import com.ai.ecp.pmph.aip.dubbo.constants.PmphConstants;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedDetail;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedResponse;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMTONetValueAddedResponse;
import com.ai.ecp.pmph.aip.dubbo.utils.AipUtil;
import com.ai.ecp.pmph.aip.dubbo.utils.HttpUtil;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipLMNetValueAddedSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: <br>
 * Date:2015-11-5下午10:09:12  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipLMNetValueAddedSVImpl implements IAipLMNetValueAddedSV {
	
	private static final String MODULE =AipLMNetValueAddedSVImpl.class.getName();

	/** 
	 * TODO 简单描述该方法的实现功能（可选）. 
	 * @see com.ai.ecp.aip.service.common.interfaces.IAipLMNetValueAddedSV#requestResource(com.ai.ecp.aip.dubbo.dto.AipLMNetValueAddedRequest)
	 */
	@Override
	public AipLMNetValueAddedResponse requestResource(
			AipLMNetValueAddedRequest request) throws BusinessException {
		paramCheck(request);
		AipLMNetValueAddedResponse response = null;
		LogUtil.info(MODULE, "获取雷鸣网络增值服务资源,入参:"+ToStringBuilder.reflectionToString(request));
		
		try {
			Map<String, String> dataMap = buildDataMap(request);
			// String url = HttpUtil.appendQueryParams(PmphConstants.RemoteURL.LM_REMOTE_NET_VALUE_ADDED_URL, dataMap);
			//LogUtil.info(MODULE, "url:"+url);
			// String json = HttpUtil.get(url, null, null);
			String json = HttpUtil.postForm(request.getReqUrl(), dataMap, null, null);
			if(StringUtil.isNotBlank(json)){
				LogUtil.info(MODULE, "雷鸣网络增值服务请求返回增值JSON信息:"+json);
				// 这种方式不不能将resources里边的数组转换成list
				response = JSONObject.parseObject(json, AipLMNetValueAddedResponse.class);
				/*// 获取JSONObject对象
				JSONObject obj = JSONObject.parseObject(json);
				String re = obj.getString("resources");
				JSONArray arr = JSONObject.parseArray(re);
				// 将对象转换成JSONArray数组
//				JSONArray arr = obj.getJSONArray("resources");
				// 将数组转换成字符串
				String js = JSONObject.toJSONString(arr);
				// 将字符串转换成list集合
				List<AipLMNetValueAddedDetail>  resources = JSONObject.parseArray(js, AipLMNetValueAddedDetail.class);
				response.setResources(resources);*/
			}
		} catch (IOException e) {
			LogUtil.error(MODULE, "向雷鸣网络增值服务接口发起请求遇到异常!",e);
			throw new BusinessException(PmphConstants.ErrorCode.LMREC_B20021,new String[]{e.getMessage()});
		}
		return response;
	}
	
	private Map<String, String> buildDataMap(AipLMNetValueAddedRequest request){
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtil.isNotBlank(request.getIsbn())){
			map.put("isbn", request.getIsbn());
		}
		if(null != request.getCnt()){
			map.put("cnt", request.getCnt().toString());
		}
		return map;
	}
	
	private void paramCheck(AipLMNetValueAddedRequest request){
		AipUtil.paramCheck(new Object[]{request}, new String[]{"request"});
	    AipUtil.paramCheck(new Object[]{request.getIsbn()}, new String[]{"isbn"});
		AipUtil.paramCheck(new Object[]{request.getReqUrl()}, new String[]{"authUrl"});
	}

	/**
	 * 获取详情页接口
	 */
	@Override
	public AipLMTONetValueAddedResponse requestLink(AipLMNetValueAddedRequest request) {
		paramCheck(request);
		AipLMTONetValueAddedResponse response = null;
		LogUtil.info(MODULE, "获取雷鸣网络增值服务详情资源,入参:"+ToStringBuilder.reflectionToString(request));
		
		try {
			Map<String, String> dataMap = buildDataMap(request);
			String json = HttpUtil.postForm(request.getReqUrl(), dataMap, null, null);
			if(StringUtil.isNotBlank(json)){
				LogUtil.info(MODULE, "雷鸣网络增值服务请求返回增值JSON信息:"+json);
				// 这种方式直接获取，不知道为什么，_ZVING_STATUS  _ZVING_MESSAGE 这两个字段的值不能转换
				response = JSONObject.parseObject(json, AipLMTONetValueAddedResponse.class);
				JSONObject jsonx = JSONObject.parseObject(json);
				String status = jsonx.getString("_ZVING_STATUS");
				String message = jsonx.getString("_ZVING_MESSAGE");
				response.set_ZVING_MESSAGE(message);
				response.set_ZVING_STATUS(status);
			}
		} catch (IOException e) {
			LogUtil.error(MODULE, "向雷鸣网络增值服务接口发起请求遇到异常!",e);
			throw new BusinessException(PmphConstants.ErrorCode.LMREC_B20021,new String[]{e.getMessage()});
		}
		return response;
	}
	
	/*
	public static void main(String[] args) {
		AipLMNetValueAddedRequest  req = new AipLMNetValueAddedRequest();
		req.setIsbn("978-7-117-24395-7");
		req.setCnt(100);
		
		IAipLMNetValueAddedRSV rsv = new AipLMNetValueAddedRSVImpl();
		
		AipLMNetValueAddedResponse response = rsv.requestResource(req);
		String json = "{\"isbn\" : \"\",\"cnt\" : 1,resources : [{\"resName\" : \"\",\"resSrc\" : \"\",\"resFormat\" : \"\",\"resType\" : \"\""+
					"}, {\"resName\" : \"\",\"resSrc\" : \"\",\"resFormat\" : \"\",\"resType\" : \"\"}]}";
		
		AipLMNetValueAddedResponse resp = JSONObject.parseObject(json, AipLMNetValueAddedResponse.class);
		System.out.println(ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));

	}*/

}

