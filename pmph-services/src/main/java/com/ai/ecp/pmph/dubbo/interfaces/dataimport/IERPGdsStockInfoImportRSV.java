package com.ai.ecp.pmph.dubbo.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.kettle.IReceiveData;

import java.util.Map;

public interface IERPGdsStockInfoImportRSV extends IReceiveData{
    public void saveGdsStockInfo(Map map) throws BusinessException ;
}

