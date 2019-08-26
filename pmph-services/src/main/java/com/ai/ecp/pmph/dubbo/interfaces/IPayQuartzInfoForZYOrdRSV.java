package com.ai.ecp.pmph.dubbo.interfaces;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;
import com.ai.ecp.server.front.exception.BusinessException;

public interface IPayQuartzInfoForZYOrdRSV {
    
    /**
     * 
     * queryNotDealPayScoreOrder:获取未处理泽元数字教材授权的订单. <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param rPayQuartzInfoRequest
     * @return 
     * @since JDK 1.6
     */
    public List<RPayQuartzInfoResponse> queryNotDealZYDigitalOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest);
    
    /**
     * 
     * queryNotDealPayScoreOrder:获取未处理考试网授权的订单. <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param rPayQuartzInfoRequest
     * @return 
     * @since JDK 1.6
     */
    public List<RPayQuartzInfoResponse> queryNotDealZYExaminationOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest);
    
    /**
     * 
     * queryNotDealPayScoreOrder:处理泽元数字教材授权订单. <br/> 
     * @param rPayQuartzInfoRequest
     * @return 
     * @since JDK 1.6
     */
    public void dealZYDigitalOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest) throws BusinessException;
    
    /**
     * 
     * queryNotDealPayScoreOrder:处理考试网授权订单. <br/> 
     * @param rPayQuartzInfoRequest
     * @return 
     * @since JDK 1.6
     */
    public void dealZYExaminationOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest) throws BusinessException;
}

