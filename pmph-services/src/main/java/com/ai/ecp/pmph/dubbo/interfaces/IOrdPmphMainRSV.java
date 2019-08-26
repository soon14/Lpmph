package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.pmph.dubbo.dto.OrdMainCompensateResponse;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

public interface IOrdPmphMainRSV {

    /** 
     * queryOrderByStaffId:买家订单查询. <br/> 
     * @param rCustomerOrdRequest
     * @return
     * @throws BusinessException 
     * @since JDK 1.6 
     */ 
    public PageResponseDTO<RCustomerOrdResponse> queryOrderByStaffId(RQueryOrderRequest rQueryOrderRequest) throws BusinessException;
      
    /**
     * 
     * queryOrderByCompensatePage:(可进行补偿性退款数据的分页查询). <br/> 
     * 
     * @param rQueryOrderRequest
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public PageResponseDTO<OrdMainCompensateResponse> queryOrderByCompensatePage(RQueryOrderRequest rQueryOrderRequest) throws BusinessException;

	public boolean checkBackMoney(ROrdReturnRefundReq req) throws BusinessException;
	
	 public ROrdMainResponse queryOrderMain(ROrderIdRequest req)throws BusinessException;
}

