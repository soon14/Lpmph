package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.general.order.dto.PayQuartzInfoRequest;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreISBNActiveReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreResultResDTO;

/**
 * 
 * Project Name:ecp-services-staff-server <br>
 * Description: 积分计算接口<br>
 * Date:2016-2-17下午4:04:07  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IScoreCalRSV {

    /**
     * 
     * saveScoreCal:(通用积分计算方法). <br/> 
     * 
     * @param pSourceType
     * @param pCustInfo
     * @param pOrderInfo
     * @return 
     * @since JDK 1.6
     */
    public long saveScoreCal(String pSourceType, CustInfoReqDTO pCustInfo, PayQuartzInfoRequest pOrderInfo); 
   
    /**
     * 
     * saveScoreCal:(一书一码激活赠送积分计算方法). <br/> 
     * 因为一书一码比较特殊，所以另写一个方法来处理
     * @param pSourceType
     * @param pCustInfo
     * @param pOrderInfo
     * @return 
     * @since JDK 1.6
     */
    public long saveScoreCalForIsbnActive(ScoreISBNActiveReqDTO req) throws BusinessException; 
   
   
    /**
     * 
     * updateScoreForE:(人卫E教兑换人卫商城积分). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author admin 
     * @param cust
     * @param scoreResult
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public void updateScoreForE(CustInfoReqDTO cust, ScoreResultResDTO scoreResult) throws BusinessException;
}

