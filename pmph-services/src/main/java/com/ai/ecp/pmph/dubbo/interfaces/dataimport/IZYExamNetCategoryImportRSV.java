package com.ai.ecp.pmph.dubbo.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.kettle.IReceiveData;

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
public interface IZYExamNetCategoryImportRSV  extends IReceiveData{

	public void executeImport(Map<String, Object> map)throws BusinessException;
	
}

