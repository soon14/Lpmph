package com.ai.ecp.pmph.dubbo.interfaces.dataimport;

import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgReqDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.kettle.IReceiveData;

import java.util.List;
import java.util.Map;

/**
 * 
 * Project Name:ecp-services-goods-server <br>
 * Description:泽云分类信息导入 <br>
 * Date:2015-10-27上午10:52:37  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IZYGdsCategoryImportRSV  extends IReceiveData{

	public void executeImport(Map<String, Object> map)throws BusinessException;
	
	public void batchImport(List<Map<String, Object>> dataMaps)throws BusinessException;
	/**
	 * 
	 * deleteCategory:根据分类映射关系删除分类. <br/> 
	 * 
	 * @param interfaceCatgReqDTO catgCode必传。
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void deleteCategory(GdsInterfaceCatgReqDTO interfaceCatgReqDTO)throws BusinessException;
}

