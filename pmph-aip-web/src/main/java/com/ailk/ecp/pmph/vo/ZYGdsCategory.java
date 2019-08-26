/** 
 * File Name:ZYGdsCategoryModel.java 
 * Date:2015-11-2下午1:55:08 
 * 
 */ 
package com.ailk.ecp.pmph.vo;

/**
 * Project Name:ecp-aip-web <br>
 * Description:泽元数字教材/电子书分类同步接口分类对象. <br>
 * Date:2015-11-2下午1:55:08  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class ZYGdsCategory {
	/**
	 * 分类编码.
	 */
	private Long id;
	/**
	 * 分类名称.
	 */
	private String name;
	/**
	 * 排序.
	 */
	private Long sort;
	/**
	 * 上级分类编码.如果为顶级分类，则为0.
	 */
	private Long parentId;
	/**
	 * 分类类型，1-数字教材，2-电子书
	 */
	private Integer catgType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getCatgType() {
		return catgType;
	}
	public void setCatgType(Integer catgType) {
		this.catgType = catgType;
	}
	
	

}

