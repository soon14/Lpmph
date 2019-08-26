package com.ai.ecp.pmph.dubbo.impl.dataimport;

import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IZYGdsCategoryBatchImportRSV;
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
public class ZYGdsCategoryBatchImportRSVImpl implements IZYGdsCategoryBatchImportRSV {

	@Resource(name="zyGdsCategoryImportSV")
	private IZYGdsCategoryImportSV zyGdsCategoryImportSV;
	
	
	@Override
	public void batchImport(List<Map<String, Object>> dataMaps)
			throws BusinessException {
		zyGdsCategoryImportSV.batchImport(dataMaps);
		
	}

	
	
}

