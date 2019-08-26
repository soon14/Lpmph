package com.ai.ecp.pmph.service.common.interfaces.dataimport;

import java.util.List;
import java.util.Map;

import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgReqDTO;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * 
 * Project Name:ecp-services-goods-server <br>
 * Description: 泽元电子书数字教材分类导入接口.<br>
 * Date:2015-10-27上午10:56:28  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IZYGdsCategoryImportSV {
	/**
	 * 
	 * executeImport:执行初始导入.
	 * 
	 * @param map
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void executeImport(Map<String, Object> map) throws BusinessException;
	/**
	 * 
	 * executeImport:(这里用一句话描述这个方法的作用). <br/> 
	 * 
	 * @param map
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void batchImport(List<Map<String, Object>> dataMaps) throws BusinessException;
	
	public void deleteCategory(GdsInterfaceCatgReqDTO interfaceCatgReqDTO);

}

