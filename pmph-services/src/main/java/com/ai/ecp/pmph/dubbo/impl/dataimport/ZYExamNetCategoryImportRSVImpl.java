package com.ai.ecp.pmph.dubbo.impl.dataimport;

import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IZYExamNetCategoryImportRSV;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IZYExamNetCategoryImportSV;
import com.ai.ecp.server.front.exception.BusinessException;

import javax.annotation.Resource;
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
public class ZYExamNetCategoryImportRSVImpl implements IZYExamNetCategoryImportRSV {

	@Resource(name="zyExamNetCategoryImportSV")
	private IZYExamNetCategoryImportSV zyExamNetCategoryImportSV;
	
	
	
	
	
	@Override
	public void receive(Map<String, Object> data) {
		this.executeImport(data);
	}





	@Override
	public void executeImport(Map<String, Object> map)
			throws BusinessException {
		zyExamNetCategoryImportSV.executeImport(map);
	}
	
}

