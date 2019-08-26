package com.ai.ecp.pmph.dubbo.interfaces;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.pmph.dubbo.dto.ROrdZDImportLogResp;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2018年7月30日下午8:20:29  <br>
 * 
 * @version  
 * @since JDK 1.7
 */ 
public interface IOrdImportZDMainRSV {
	
	public List<ROrdZDImportLogResp> importZDMain(RFileImportRequest info) throws BusinessException;
	
	/**
     * 
     * queryFailZDOrdImport:(查询失败的天猫订单). <br/>
     * 
     * @param fileId
     * @param importType
     * @return 
     * @since JDK 1.7
     */
    public List<ROrdZDImportLogResp> queryFailZDOrdImport(String fileId,String importType);
}
