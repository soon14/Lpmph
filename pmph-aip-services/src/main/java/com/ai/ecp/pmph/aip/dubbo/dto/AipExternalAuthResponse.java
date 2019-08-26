package com.ai.ecp.pmph.aip.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseResponseDTO;
import com.alibaba.fastjson.annotation.JSONField;

public class AipExternalAuthResponse extends BaseResponseDTO {

	private static final long serialVersionUID = 2284295564368205285L;
	
	/**
	 * code:接口执行结果状态：0=成功,1=系统错误,2=业务错误
	 */
	@JSONField(name="code")
	private String code;
	/**
	 * msg:接口执行结果信息，成功表示执行成功
	 */
	@JSONField(name="msg")
	private String msg;
	//请求报文 给调用者保存日志
    private String requestMessage;
    //响应报文 给调用者保存日志
    private String responseMessage;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
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
