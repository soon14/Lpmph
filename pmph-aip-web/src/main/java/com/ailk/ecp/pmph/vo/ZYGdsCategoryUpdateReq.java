/** 
 * File Name:ZYGdsCategoryUpdateReq.java 
 * Date:2015-11-2下午1:53:47 
 * 
 */
package com.ailk.ecp.pmph.vo;

/**
 * Project Name:ecp-aip-web <br>
 * Description: 泽元数字教材/电子书分类全量同步接口更新请求.<br>
 * Date:2015-11-2下午1:53:47 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ZYGdsCategoryUpdateReq {

	/**
	 * 数字教材/电子书分类全量同步分类信息.
	 */
	/*private List<ZYGdsCategory> categorys;

	public List<ZYGdsCategory> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<ZYGdsCategory> categorys) {
		this.categorys = categorys;
	}*/
	
	private String categorys;

	public String getCategorys() {
		return categorys;
	}

	public void setCategorys(String categorys) {
		this.categorys = categorys;
	}
	
	

}
