/** 
 * File Name:IExamNetCategoryImportSV.java 
 * Date:2015-11-6上午11:01:46 
 * 
 */ 
package com.ai.ecp.pmph.service.common.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;

import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description:考试网分类导入服务。 <br>
 * Date:2015-11-6上午11:01:46  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IZYExamNetCategoryImportSV {
	/**
	 * 
	 * executeImport:执行导入. <br/> 
	 * 
	 * @param map
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void executeImport(Map<String, Object> map) throws BusinessException;

}

