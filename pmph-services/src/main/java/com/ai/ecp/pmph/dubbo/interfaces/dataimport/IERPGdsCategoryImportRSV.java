package com.ai.ecp.pmph.dubbo.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.kettle.IReceiveData;

import java.util.Map;

/**
 * 
 * Project Name:ecp-services-goods-server <br>
 * Description:人卫ERP分类信息导入服务 <br>
 * Date:2015-10-26下午5:34:36  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IERPGdsCategoryImportRSV extends IReceiveData{
	
	
	public void executeImport(Map<String, Object> map)throws BusinessException;

}

