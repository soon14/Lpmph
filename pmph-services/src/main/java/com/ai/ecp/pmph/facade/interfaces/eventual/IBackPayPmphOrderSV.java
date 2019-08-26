package com.ai.ecp.pmph.facade.interfaces.eventual;

import com.ai.ecp.order.dubbo.dto.RBackConfirmReq;

public interface IBackPayPmphOrderSV {

    /** 
     * dealMethod:退货退款主方法. <br/> 
     * @param rBackConfirmReq 
     * @since JDK 1.6 
     */ 
    public void dealMethod(RBackConfirmReq rBackConfirmReq);
}

