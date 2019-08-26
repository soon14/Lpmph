package com.ai.ecp.pmph.dubbo.impl.dataimport;

import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IERPGdsCategoryImportRSV;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IERPGdsCategoryImportSV;
import com.ai.ecp.server.front.exception.BusinessException;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 
 * Project Name:ecp-services-goods-server <br>
 * Description: <br>
 * Date:2015-10-26下午8:48:43  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class ERPGdsCategoryImportRSVImpl implements IERPGdsCategoryImportRSV {

	@Resource(name="erpGdsCategoryImportSV")
	private IERPGdsCategoryImportSV gdsCategoryImportSV;
	
	
	@Override
	public void receive(Map<String, Object> data) {
		
		this.executeImport(data);
	}





	@Override
	public void executeImport(Map<String, Object> map)
			throws BusinessException {
		gdsCategoryImportSV.executeImport(map);
	}

	
	
}

