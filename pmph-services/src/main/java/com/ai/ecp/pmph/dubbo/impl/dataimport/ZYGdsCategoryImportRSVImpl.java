package com.ai.ecp.pmph.dubbo.impl.dataimport;

import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgReqDTO;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IZYGdsCategoryImportRSV;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IZYGdsCategoryImportSV;
import com.ai.ecp.server.front.exception.BusinessException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 
 * Project Name:ecp-services-goods-server <br>
 * Description:泽元全量数据导入接口.(可以一条一条处理.) <br>
 * Date:2015-10-26下午8:48:43  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class ZYGdsCategoryImportRSVImpl implements IZYGdsCategoryImportRSV {

	@Resource(name="zyGdsCategoryImportSV")
	private IZYGdsCategoryImportSV zyCategoryImportSV;
	
	
	
	
	
	@Override
	public void receive(Map<String, Object> data) {
		this.executeImport(data);
	}





	@Override
	public void executeImport(Map<String, Object> map)
			throws BusinessException {
		zyCategoryImportSV.executeImport(map);
	}
	
	@Deprecated
	@Override
	public void batchImport(List<Map<String, Object>> dataMaps)
			throws BusinessException {
		//zyCategoryImportSV.executeImport(map);
	    zyCategoryImportSV.batchImport(dataMaps);
		
	}





	@Override
	public void deleteCategory(GdsInterfaceCatgReqDTO interfaceCatgReqDTO)
			throws BusinessException {
		zyCategoryImportSV.deleteCategory(interfaceCatgReqDTO);
	}
	
	
	

	
	
}

