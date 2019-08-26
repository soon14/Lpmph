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
public interface IOrdImportTMSubRSV{

    public List<ROrdTmImportLogResp> importTMSub(final RFileImportRequest info) throws BusinessException;
    
}

