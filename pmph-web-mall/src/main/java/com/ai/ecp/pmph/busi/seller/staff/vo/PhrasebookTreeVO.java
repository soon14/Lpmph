package com.ai.ecp.pmph.busi.seller.staff.vo;

import java.util.List;

public class PhrasebookTreeVO {
	private String name; //展示名称：分组名/常用语文本
	private String id; //id ：分组ID/常用语ID
	private Boolean open;//展示是否展开
	private Boolean isParent; //展示是否父节点
	private List<PhrasebookTreeVO> children; //子集
	private String tClass; //所属类型  10：公共；20：私有
	private String shopId; //所属店铺
	private String sortNo; //排序
	private String status; //是否有效
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	public List<PhrasebookTreeVO> getChildren() {
		return children;
	}
	public void setChildren(List<PhrasebookTreeVO> children) {
		this.children = children;
	}
	public String gettClass() {
		return tClass;
	}
	public void settClass(String tClass) {
		this.tClass = tClass;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getSortNo() {
		return sortNo;
	}
	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
