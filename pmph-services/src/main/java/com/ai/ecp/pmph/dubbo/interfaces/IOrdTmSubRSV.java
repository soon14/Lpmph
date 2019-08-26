package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubResp;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * 
 * Project Name:ecp-services-order-server <br>
 * Description: <br>
 * Date:2016年2月23日下午4:40:33  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IOrdTmSubRSV {
    
    /**
     *  
     * queryOrderTmSubByOrderId:(天猫订单明细查询). <br/> 
     * 
     * @param rOrdTmSubReq
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public PageResponseDTO<ROrdTmSubResp> queryOrderTmSubByOrderId(ROrdTmSubReq rOrdTmSubReq) throws BusinessException;
}

