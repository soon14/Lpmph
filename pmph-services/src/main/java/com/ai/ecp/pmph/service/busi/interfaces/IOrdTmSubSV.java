package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubResp;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.interfaces.IGeneralSQLSV;

public interface IOrdTmSubSV  extends IGeneralSQLSV{ 
    
    /**
     * 
     * queryOrdeTmSubByOrderId:(天猫订单明细查询). <br/>
     * 
     * @param rOrdTmSubReq
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public PageResponseDTO<ROrdTmSubResp> queryOrderTmSubByOrderId(ROrdTmSubReq rOrdTmSubReq) throws BusinessException;


}
