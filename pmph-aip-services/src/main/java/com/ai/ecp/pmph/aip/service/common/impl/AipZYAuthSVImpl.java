/** 
 * File Name:AipZYAuthSVImpl.java 
 * Date:2015-11-5下午8:09:48 
 * 
 */
package com.ai.ecp.pmph.aip.service.common.impl;

import com.ai.ecp.aip.service.common.utils.zy.CryptoUtil;
import com.ai.ecp.pmph.aip.dubbo.constants.PmphConstants;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthResponse;
import com.ai.ecp.pmph.aip.dubbo.utils.HttpUtil;
import com.ai.ecp.pmph.aip.service.common.interfaces.IAipZYAuthSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: <br>
 * Date:2015-11-5下午8:09:48 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class AipZYAuthSVImpl implements IAipZYAuthSV {

    private static final String MODULE = AipZYAuthSVImpl.class.getName();

    /**
     * TODO 简单描述该方法的实现功能（可选）.
     * 
     * @see com.ai.ecp.aip.service.common.interfaces.IAipZYAuthSV#sendAuthRequest(com.ai.ecp.aip.dubbo.dto.AipZYAuthRequest)
     */
    @Override
    public AipZYAuthResponse sendAuthRequest(AipZYAuthRequest authRequest) throws BusinessException {
        LogUtil.info(MODULE, "向泽元授权接口发送授权请求！入参:" + ToStringBuilder.reflectionToString(authRequest));

        if (null == authRequest) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "authRequest" });
        }

        if (StringUtil.isEmpty(authRequest.getAuthUrl())) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "authUrl" });
        }

        AipZYAuthResponse resp = null;
        try {
            Map<String, String> dataMap = buildDataMap(authRequest);
            LogUtil.info(MODULE, "dataMap:" + dataMap);
            //resp.setRequestMessage(JSONObject.toJSONString(dataMap));
            /*
             * if(StringUtil.isBlank(REMOTE_AUTH_URL)){ resp = new AipZYAuthResponse();
             * resp.setStatus(AipZYAuthRequest._ZVING_STATUS_OK); // return
             * "{_ZVING_STATUS:\""+AipZYAuthRequest._ZVING_STATUS_OK+"\"}"; // return
             * JSONObject.toJSONString(resp); }
             */
            String responseJson = HttpUtil.postForm(authRequest.getAuthUrl(), dataMap, null, null);
            // HttpRequestUtil.doGet(null, REMOTE_AUTH_URL, "UTF-8", dataMap);
            LogUtil.info(MODULE, "调用泽元提供数字教材/电子书授权服务返回报文:" + responseJson);
            resp = JSONObject.parseObject(responseJson, AipZYAuthResponse.class);
            resp.setResponseMessage(responseJson);
            resp.setRequestMessage(JSONObject.toJSONString(dataMap));
        } catch (Exception e) {
            LogUtil.error(MODULE, "向泽元提供数字教材/电子书授权服务发起授权请求遇到异常!", e);
            BusinessException be = new BusinessException(PmphConstants.ErrorCode.ZYAUTH_B20020,
                    StringUtil.isNotBlank(e.getMessage())?new String[] { e.getMessage() } : new String[]{""});
            resp = new AipZYAuthResponse();
            resp.setStatus(AipZYAuthRequest._ZVING_STATUS_FALT);
            resp.setMessage(be.getErrorMessage());
            // throw new BusinessException(PmphConstants.ErrorCode.ZYAUTH_B20020);
        }
        return resp;
    }

    private Map<String, String> buildDataMap(AipZYAuthRequest authRequest) {
        Map<String, String> formData = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtil.isNotBlank(authRequest.getUserName())) {
            map.put("UserName", authRequest.getUserName());
        }
        if (StringUtil.isNotBlank(authRequest.getGoodsId())) {
            map.put("GoodsID", authRequest.getGoodsId().toString());
        }
        if (null != authRequest.getTime()) {
            map.put("Time", authRequest.getTime().toString());
        }
        if (StringUtil.isNotBlank(authRequest.getOrderSN())) {
            map.put("OrderSN", authRequest.getOrderSN());
        }
        if (StringUtil.isNotBlank(authRequest.getReadType())) {
            map.put("ReadType", authRequest.getReadType());
        }
        if (StringUtil.isNotBlank(authRequest.getGoodsType())) {
            map.put("GoodsType", authRequest.getGoodsType());
        }
        if (MapUtils.isNotEmpty(map)) {
            String json = JSONObject.toJSONString(map);
            String enParam = CryptoUtil.encrypt("27jrWz3sxrVbR+pnyg6j", json);
            formData.put("json", enParam);
        }
        return formData;
    }

    @Override
    public AipZYAuthResponse sendExaminationRequest(AipZYAuthRequest authRequest)
            throws BusinessException {
        LogUtil.info(MODULE, "向泽元授权接口发送授权请求！入参:" + ToStringBuilder.reflectionToString(authRequest));

        if (null == authRequest) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "authRequest" });
        }

        if (StringUtil.isEmpty(authRequest.getAuthUrl())) {
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,
                    new String[] { "authUrl" });
        }

        AipZYAuthResponse resp = null;
        try {
            Map<String, String> dataMap = buildDataMap(authRequest);
            LogUtil.info(MODULE, "dataMap:" + dataMap);
            //resp.setRequestMessage(JSONObject.toJSONString(dataMap));
            /*
             * if(StringUtil.isBlank(REMOTE_AUTH_URL)){ resp = new AipZYAuthResponse();
             * resp.setStatus(AipZYAuthRequest._ZVING_STATUS_OK); // return
             * "{_ZVING_STATUS:\""+AipZYAuthRequest._ZVING_STATUS_OK+"\"}"; // return
             * JSONObject.toJSONString(resp); }
             */
            String responseJson = HttpUtil.postForm(authRequest.getAuthUrl(), dataMap, null, null);
            // HttpRequestUtil.doGet(null, REMOTE_AUTH_URL, "UTF-8", dataMap);
            LogUtil.info(MODULE, "调用泽元提供考试网授权服务返回报文:" + responseJson);
            resp = JSONObject.parseObject(responseJson, AipZYAuthResponse.class);
            resp.setResponseMessage(responseJson);
            resp.setRequestMessage(JSONObject.toJSONString(dataMap));
        } catch (Exception e) {
            LogUtil.error(MODULE, "向泽元提供考试网授权服务发起授权请求遇到异常!", e);
            BusinessException be = new BusinessException(PmphConstants.ErrorCode.ZYAUTH_B20020,
                    StringUtil.isNotBlank(e.getMessage())?new String[] { e.getMessage() } : new String[]{""}
                    );
            resp = new AipZYAuthResponse();
            resp.setStatus(AipZYAuthRequest._ZVING_STATUS_FALT);
            resp.setMessage(be.getErrorMessage());
            // throw new BusinessException(PmphConstants.ErrorCode.ZYAUTH_B20020);
        }
        return resp;
    }
    
   /* public static void main(String[] args) {
        //String enParam = CryptoUtil.encrypt("27jrWz3sxrVbR+pnyg6j", json);
        AipZYAuthSVImpl sv = new AipZYAuthSVImpl();
        AipZYAuthRequest authRequest = new AipZYAuthRequest();
        authRequest.setGoodsId("3526");
        authRequest.setOrderSN("SRW16061500426738");
        authRequest.setReadType("1");
        authRequest.setUserName("1201140125");
        authRequest.setAuthUrl("http://192.168.2.5/books/youxinAuth.zaction");
        sv.sendExaminationRequest(authRequest);
        System.out.println("end");
        //{"GoodsID":"3526","OrderSN":"SRW16061500426738","ReadType":"1","UserName":"1201140125","authUrl":"http://192.168.2.5/books/youxinAuth.zaction"}
    }
*/
}
