package com.ai.ecp.pmph.service.busi.interfaces;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;




/**
 * 
 * Project Name:ecp-services-order-server <br>
 * Description: <br>
 * Date:2015年10月8日下午2:58:25  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IPayQuartzInfoPmphSV {
    
    /**
     * 
     * addZYDigitalInfo:添加泽元数字教材/电子书授权服务任务. <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param payQuartzInfo 
     * @since JDK 1.6
     */
    public void addZYDigitalInfo(RPayQuartzInfoRequest payQuartzInfo);
    
    /**
     * 
     * addZYExaminationInfo:添加泽元考试网授权服务任务. <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param payQuartzInfo 
     * @since JDK 1.6
     */
    public void addZYExaminationInfo(RPayQuartzInfoRequest payQuartzInfo);
    
    /**
     * 
     * addExternalMedicareInfo:添加外系统授权服务任务. <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param payQuartzInfo 
     * @since JDK 1.7
     */
    public void addExternalMedicareInfo(RPayQuartzInfoRequest payQuartzInfo);
    
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
     * queryNotDealExternalMedicareOrder:获取未处理外系统授权的订单. <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param rPayQuartzInfoRequest
     * @return 
     * @since JDK 1.7
     */
    public List<RPayQuartzInfoResponse> queryNotDealExternalMedicareOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest);
}
