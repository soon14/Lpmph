package com.ai.ecp.pmph.dubbo.interfaces;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;
import com.ai.ecp.server.front.exception.BusinessException;

public interface IPayQuartzInfoForExternalOrdRSV {
	/**
     * 
     * queryNotDealExternalMedicareOrder:获取未处理外系统授权的订单. <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param rPayQuartzInfoRequest
     * @return 
     * @since JDK 1.6
     */
	public List<RPayQuartzInfoResponse> queryNotDealExternalMedicareOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest);
    
	/**
     * 
     * dealZYDigitalOrder:处理外系统授权订单. <br/> 
     * @param rPayQuartzInfoRequest
     * @return 
     * @since JDK 1.6
     */
	public void dealExternalMedicareOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest) throws BusinessException;
    
}
