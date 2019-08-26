package com.ai.ecp.busi.rwzw.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.format.annotation.DateTimeFormat;

import com.ai.ecp.base.vo.EcpBasePageReqVO;

public class ArticleVO  extends EcpBasePageReqVO implements Serializable {
	 
	  private Timestamp startPubTime;
	
	  private Timestamp endPubTime;
	  
	  private Timestamp startLostTime;
;
	  private Timestamp endLostTime;

	  private Timestamp thisTime;
	  
	  private String statusZH;
	  
	  private String istopZH;
	  
	  private String homepageIsShowZH;

	  private String channelZH;

	  private String siteZH;
	  
	  private String sourceZH;

	  private String thumbnailUrl;
	  
	  private String staticUrl;
	  
	  private String vfsUrl;

	  private Long id;
	  
	  private String articleTitle;

	  private String articleRemark;
	  
	  private String source;
	  
	  private String istop;
	
	  private String status;

	  private Timestamp pubTime;
	  
	  private Timestamp lostTime;

	  private String thumbnailName;

	  private String thumbnail;
	  
	  private String staticId;
	  
	  private String vfsName;

	  private String vfsId;

	  private Long siteId;

	  private Long channelId;
	  
	  private Long createStaff;
	  
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	  private Timestamp createTime;
	  
	  private Long updateStaff;

	  private Timestamp updateTime;

	  private String homepageIsShow;
	  
	  private String authorName;

	  private String qrCode;
	  
	  private String bigQrCode;
	 
	  
	  private static final long serialVersionUID = 1L;

	public Timestamp getStartPubTime() {
		return startPubTime;
	}

	public void setStartPubTime(Timestamp startPubTime) {
		this.startPubTime = startPubTime;
	}

	public Timestamp getEndPubTime() {
		return endPubTime;
	}

	public void setEndPubTime(Timestamp endPubTime) {
		this.endPubTime = endPubTime;
	}

	public Timestamp getStartLostTime() {
		return startLostTime;
	}

	public void setStartLostTime(Timestamp startLostTime) {
		this.startLostTime = startLostTime;
	}

	public Timestamp getEndLostTime() {
		return endLostTime;
	}

	public void setEndLostTime(Timestamp endLostTime) {
		this.endLostTime = endLostTime;
	}

	public Timestamp getThisTime() {
		return thisTime;
	}

	public void setThisTime(Timestamp thisTime) {
		this.thisTime = thisTime;
	}

	public String getStatusZH() {
		return statusZH;
	}

	public void setStatusZH(String statusZH) {
		this.statusZH = statusZH;
	}

	public String getIstopZH() {
		return istopZH;
	}

	public void setIstopZH(String istopZH) {
		this.istopZH = istopZH;
	}

	public String getHomepageIsShowZH() {
		return homepageIsShowZH;
	}

	public void setHomepageIsShowZH(String homepageIsShowZH) {
		this.homepageIsShowZH = homepageIsShowZH;
	}

	public String getChannelZH() {
		return channelZH;
	}

	public void setChannelZH(String channelZH) {
		this.channelZH = channelZH;
	}

	public String getSiteZH() {
		return siteZH;
	}

	public void setSiteZH(String siteZH) {
		this.siteZH = siteZH;
	}

	public String getSourceZH() {
		return sourceZH;
	}

	public void setSourceZH(String sourceZH) {
		this.sourceZH = sourceZH;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getStaticUrl() {
		return staticUrl;
	}

	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}

	public String getVfsUrl() {
		return vfsUrl;
	}

	public void setVfsUrl(String vfsUrl) {
		this.vfsUrl = vfsUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getArticleRemark() {
		return articleRemark;
	}

	public void setArticleRemark(String articleRemark) {
		this.articleRemark = articleRemark;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIstop() {
		return istop;
	}

	public void setIstop(String istop) {
		this.istop = istop;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getPubTime() {
		return pubTime;
	}

	public void setPubTime(Timestamp pubTime) {
		this.pubTime = pubTime;
	}

	public Timestamp getLostTime() {
		return lostTime;
	}

	public void setLostTime(Timestamp lostTime) {
		this.lostTime = lostTime;
	}

	public String getThumbnailName() {
		return thumbnailName;
	}

	public void setThumbnailName(String thumbnailName) {
		this.thumbnailName = thumbnailName;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getStaticId() {
		return staticId;
	}

	public void setStaticId(String staticId) {
		this.staticId = staticId;
	}

	public String getVfsName() {
		return vfsName;
	}

	public void setVfsName(String vfsName) {
		this.vfsName = vfsName;
	}

	public String getVfsId() {
		return vfsId;
	}

	public void setVfsId(String vfsId) {
		this.vfsId = vfsId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getCreateStaff() {
		return createStaff;
	}

	public void setCreateStaff(Long createStaff) {
		this.createStaff = createStaff;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateStaff() {
		return updateStaff;
	}

	public void setUpdateStaff(Long updateStaff) {
		this.updateStaff = updateStaff;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getHomepageIsShow() {
		return homepageIsShow;
	}

	public void setHomepageIsShow(String homepageIsShow) {
		this.homepageIsShow = homepageIsShow;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getBigQrCode() {
		return bigQrCode;
	}

	public void setBigQrCode(String bigQrCode) {
		this.bigQrCode = bigQrCode;
	}
	
}
