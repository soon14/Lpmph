/** 
 * File Name:AipZYAuthResponse.java 
 * Date:2015-10-30下午2:47:02 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseResponseDTO;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description:数字教材/电子书提供（泽元提供）授权（试用、正式）响应. <br>
 * Date:2015-10-30下午2:47:02  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipZYAuthResponse extends BaseResponseDTO{
	
	private static final long serialVersionUID = 4253988212015320576L;
	/**
	 * _ZVING_STATUS:接口执行结果状态：FAIL=失败，OK=成功
	 */
	@JSONField(name="_ZVING_STATUS")
	private String status; 
	/**
	 * _ZVING_MESSAGE:接口执行结果信息，状态为FAIL时返回结果
	 */
	@JSONField(name="_ZVING_MESSAGE")
	private String message;
	//请求报文 给调用者保存日志
    private String requestMessage;
    //响应报文 给调用者保存日志
    private String responseMessage;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    public String getRequestMessage() {
        return requestMessage;
    }
    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
	
	

}

