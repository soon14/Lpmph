package com.ai.ecp.pmph.service.busi.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;

import java.util.Map;

public interface IERPGdsStockInfoImportSV {

    void saveGdsStockInfo(Map map)throws BusinessException;
    
    
}

