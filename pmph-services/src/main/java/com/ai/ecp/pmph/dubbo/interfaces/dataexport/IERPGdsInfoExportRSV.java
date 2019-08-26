package com.ai.ecp.pmph.dubbo.interfaces.dataexport;

import com.ai.ecp.server.front.kettle.IExportData;

import java.util.List;
import java.util.Map;

/**
 */
public interface IERPGdsInfoExportRSV extends IExportData{

    /**
     * 全量导出
     * @return
     */
    List<Map<String,Object>> fullImport();

    /**
     * 增量同步
     * @return
     */
    List<Map<String,Object>> deltaImport() throws Exception;

}
