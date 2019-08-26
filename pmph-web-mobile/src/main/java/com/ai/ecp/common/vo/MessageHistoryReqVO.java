package com.ai.ecp.common.vo;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class MessageHistoryReqVO extends EcpBasePageReqVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String sessionId;
	
	@NotNull(message="消息接收方不能为空")
	private String to;
	
	@NotNull(message="消息发送方不能为空")
	private String from;
	
	@NotNull(message="消息内容不能为空")
	private String body;
	
	private Long shopId;
	
	private String status;
	
	@NotNull(message="消息来源不能为空")
	private String toResource;
	
	@NotNull(message="消息类型不能为空")
	private String messageType;
	
	private String contentType;
	
	private String userCode;
	
	private String csaCode;
	
	private Timestamp beginDate;
	
	private Timestamp arriveDate;
	
	private Timestamp ofDate;
	
	private String gdsId;
	
	private String ordId;
	
	private String issueType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getToResource() {
		return toResource;
	}

	public void setToResource(String toResource) {
		this.toResource = toResource;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Timestamp getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
	}

	public Timestamp getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Timestamp arriveDate) {
		this.arriveDate = arriveDate;
	}

	public Timestamp getOfDate() {
		return ofDate;
	}

	public void setOfDate(Timestamp ofDate) {
		this.ofDate = ofDate;
	}
	
    public int getStartRowIndex() {
        return (this.getPageNumber() - 1) * this.getPageSize();
    }

    public int getEndRowIndex() {
        return this.getPageSize();
    }

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getCsaCode() {
		return csaCode;
	}

	public void setCsaCode(String csaCode) {
		this.csaCode = csaCode;
	}

	public String getGdsId() {
		return gdsId;
	}

	public void setGdsId(String gdsId) {
		this.gdsId = gdsId;
	}

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
    
}
