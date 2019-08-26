package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.pmph.dubbo.dto.OrdMainCompensateResponse;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.server.front.dto.PageResponseDTO;

public interface IOrdPmphMainSV {

    /** 
     * queryOrderByStaffIdPage:买家查询订单，支付分页查询. <br/> 
     * @param rCustomerOrdRequest
     * @return 
     * @since JDK 1.6 
     */ 
    public PageResponseDTO<RCustomerOrdResponse> queryOrderByStaffIdPage(RQueryOrderRequest rQueryOrderRequest);
    
    /** 
     * queryOrderByCompensatePage:可进行补偿性退款数据的分页查询. <br/> 
     * @param rCustomerOrdRequest
     * @return 
     * @since JDK 1.6 
     */ 
    public PageResponseDTO<OrdMainCompensateResponse> queryOrderByCompensatePage(RQueryOrderRequest rQueryOrderRequest);
    
    /** 
     * queryOrderByCompensatePage:调整金额判断. <br/> 
     * @param rCustomerOrdRequest
     * @return 
     * @since JDK 1.7 
     */
    public boolean checkBackMoney(ROrdReturnRefundReq req);
    
    public ROrdMainResponse queryOrderMain(ROrderIdRequest req);
}

