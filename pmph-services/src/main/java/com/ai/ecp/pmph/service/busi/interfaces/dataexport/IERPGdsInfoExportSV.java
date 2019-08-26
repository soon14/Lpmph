package com.ai.ecp.pmph.service.busi.interfaces.dataexport;

import java.util.List;
import java.util.Map;

/**
 */
public interface IERPGdsInfoExportSV {

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
