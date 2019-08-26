/**
 * 
 */
package com.ai.ecp.pmph.service.busi.interfaces;

import java.util.List;

import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmImportLogResp;
import com.ai.ecp.pmph.dubbo.dto.ROrdZDImportLogResp;
import com.ai.ecp.server.service.interfaces.IGeneralSQLSV;



/**
 *
 */
public interface IOrdImportLogSV  extends IGeneralSQLSV{

    public void saveOrdImportLog(OrdImportLog info);
  
    public OrdImportLog queryOrdImport(String oldOrderCode,String importType,String status);
    
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
    
    /**
     * 
     * queryFailZDOrdImport:(查询失败的征订单). <br/>
     * 
     * @param fileId
     * @param importType
     * @return 
     * @since JDK 1.6
     */
    public List<ROrdZDImportLogResp> queryFailZDOrdImport(String fileId,String importType);
    
}
