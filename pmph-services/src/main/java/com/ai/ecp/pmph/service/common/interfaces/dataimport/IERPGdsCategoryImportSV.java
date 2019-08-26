/** 
 * File Name:IGdsCategoryImportSV.java 
 * Date:2015-10-24下午4:21:21 
 * 
 */ 
package com.ai.ecp.pmph.service.common.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;

import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description: <br>
 * Date:2015-10-24下午4:21:21  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IERPGdsCategoryImportSV {
	/**
	 * 
	 * 执行Map结构类型分类同步.
	 * 
	 * @param map
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void executeImport(Map<String, Object> map) throws BusinessException;
	

}

