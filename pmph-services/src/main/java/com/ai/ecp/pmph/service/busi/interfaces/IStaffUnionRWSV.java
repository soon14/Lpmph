package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.pmph.dubbo.dto.OrderBackMainRWReqDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.OrderBackMainReqDTO;
import com.ai.ecp.staff.dubbo.dto.OrderBackSubReqDTO;

/**
 * 
 * Project Name:pmph-services-server <br>
 * Description: 针对人卫需求改造的积分服务<br>
 * Date:2016年9月1日下午3:22:01  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IStaffUnionRWSV {
	
	 /**
	  * 
	  * selTotalScoreByBackOrder:(能过orderId，比例，查询此次退货需要扣除的积分). <br/> 
	  * 入参：orderId、退货金额比例scale、是否最后一笔退货lastFlag
	  * @param req
	  * @return
	  * @throws BusinessException 
	  * @since JDK 1.6
	  */
    public long selTotalScoreByBackOrderRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException;
    
    
    /**
     * 
     * saveScoreAddForOrderBack:(退货同意退款). <br/> 
     * 1、返还已使用的积分
     * 2、退货同意退款时，解冻之前冻结的积分，并把积分扣除
     * 3、返还已使用的资金账户
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public boolean saveScoreAcctForOrderBackRW(OrderBackMainRWReqDTO<OrderBackSubReqDTO> req,OrderBackSubReqDTO orderReq) throws BusinessException;
    
    
    /**
     * 
     * saveScoreFrozenForOrderBack:(同意退货：冻结子订单赠送的积分). <br/> 
     * 包括：1、子订单积分；2、主订单获得的积分按比较平摊到子订单的积分部分。
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public Long saveScoreFrozenForOrderBackRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException;
    
    /**
     * 
     * saveScoreFrozenModifyForOrderBackRW:(人卫调整金额之后，需要冻结的积分). <br/> 
     * 金额可能调高，也可能调低，调整之后，冻结金额也需要调整，可用积分也跟着变化
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public Long saveScoreFrozenModifyForOrderBackRW(OrderBackMainRWReqDTO<OrderBackSubReqDTO> req) throws BusinessException;
    

    /**
	  * 
	  * selTotalScoreByBackOrderAllRW:(通过orderId，比例，查询此次退货需要扣除的积分，不进行是否能扣为负的判断). <br/> 
	  * 入参：orderId、退货金额比例scale、是否最后一笔退货lastFlag
	  * @param req
	  * @return
	  * @throws BusinessException 
	  * @since JDK 1.6
	  */
   public long selTotalScoreByBackOrderAllRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException;
   
}

