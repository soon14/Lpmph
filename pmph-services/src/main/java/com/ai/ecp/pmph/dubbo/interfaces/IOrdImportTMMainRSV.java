package com.ai.ecp.pmph.dubbo.interfaces;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmImportLogResp;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月12日下午8:20:29  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */  
public interface IOrdImportTMMainRSV {
    
    public List<ROrdTmImportLogResp> importTMMain(RFileImportRequest info) throws BusinessException;
    
    /**
     * 
     * queryFailTmOrdImport:(查询失败的天猫订单). <br/>
     * 
     * @param oldOrderCode
     * @param importType
     * @return 
     * @since JDK 1.6
     */
    public List<ROrdTmImportLogResp> queryFailTmOrdImport(String fileId,String importType);

}

