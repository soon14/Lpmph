/** 
 * File Name:AipZYAuthResponse.java 
 * Date:2015-10-30下午2:47:02 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseResponseDTO;

/**
 * 
 * Project Name:pmph-aip-services-server <br>
 * Description: 本版编号通知响应对象  VN:versionNumber<br>
 * Date:2017年8月23日下午5:22:53  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class AipEEduVNNoticeResponse extends BaseResponseDTO{
	
	private static final long serialVersionUID = 4253988212015320576L;
	/**
	 * 请求结果状态
	 */
	private String status;
	/**
	 * 请求结果信息
	 */
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

