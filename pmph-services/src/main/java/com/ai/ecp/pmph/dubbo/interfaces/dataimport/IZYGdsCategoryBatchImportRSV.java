package com.ai.ecp.pmph.dubbo.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;

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
public interface IZYGdsCategoryBatchImportRSV{

	public void batchImport(List<Map<String, Object>> dataMaps)throws BusinessException;
}

