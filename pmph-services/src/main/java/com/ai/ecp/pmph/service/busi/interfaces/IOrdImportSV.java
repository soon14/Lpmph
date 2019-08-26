/**
 * 
 */
package com.ai.ecp.pmph.service.busi.interfaces;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.ROrdImportZYRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.pmph.dao.model.OrdMainTM;
import com.ai.ecp.pmph.dao.model.OrdSubTM;
import com.ai.ecp.server.service.interfaces.IGeneralSQLSV;



/**
 *
 */
public interface IOrdImportSV  extends IGeneralSQLSV{

    /**
     * 
     * saveOrdMainTM:(保存主订单信息). <br/>
     * 
     * @param ordMainTM 
     * @since JDK 1.6
     */
    public void saveOrdMainTM(OrdMainTM ordMainTM);
    
    /**
     * 
     * queryOrdMainTMRepeat:(根据订单号判断订单是否重复). <br/>
     * 
     * @param orderId
     * @param status
     * @return 
     * @since JDK 1.6
     */
    public OrdMainTM queryOrdMainTMRepeat(String orderId, String status);
    
    /**
     * 
     * saveOrdSubTM:(保存子订单信息). <br/>
     * 
     * @param ordSubTM 
     * @since JDK 1.6
     */
    public void saveOrdSubTM(OrdSubTM ordSubTM);
    
    /**
     * 
     * queryOrdMainTMRepeat:(根据订单号判断订单是否重复). <br/>
     * 
     * @param orderId
     * @param status
     * @return 
     * @since JDK 1.6
     */
    public OrdSubTM queryOrdSubTMRepeat(String orderId,String externalSysCode, String status,String title);
    
    public void saveOrdMainZY(ROrdImportZYRequest info);
    
    public void saveOrdSubZY(ROrdImportZYRequest rOrdImportZYRequest);
    
    
    /** 
     * saveBatchOrdMainTM:(批量导入). <br/> 
     * @param info
     * @param group 
     * @since JDK 1.6 
     */ 
    public void saveBatchOrdMainTM(RFileImportRequest info, List<List<Object>> group);
    
    /** 
     * saveOneOrdMainTM:(天猫订单一条一条导入). <br/> 
     * @param info
     * @param row 
     * @since JDK 1.6 
     */ 
    public void saveOneOrdMainTM(RFileImportRequest info, List<Object> row);
    
    /** 
     * saveBatchOrdSubTM:(子订单批量导入). <br/> 
     * @param ordSubTM 
     * @since JDK 1.6 
     */ 
    public void saveBatchOrdSubTM(RFileImportRequest info, List<List<Object>> group);
    /** 
     * saveOneOrdSubTM:(子订单单行处理). <br/> 
     * @param ordSubTM 
     * @since JDK 1.6 
     */ 
    public void saveOneOrdSubTM(RFileImportRequest info, List<Object> row);
    
    /** 
     * saveOneOrdSubZD:(征订单订单行处理). <br/> 
     * @param ordSubTM 
     * @since JDK 1.7
     */ 
    public RPreOrdMainsResponse saveOneOrdMainZD(RFileImportRequest info, List<Object> row);
}
