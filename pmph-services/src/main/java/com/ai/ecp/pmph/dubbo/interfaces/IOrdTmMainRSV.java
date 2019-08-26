package com.ai.ecp.pmph.dubbo.interfaces;

import java.util.List;

import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainResp;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * 
 * Project Name:ecp-services-order-server <br>
 * Description: <br>
 * Date:2016年2月23日下午4:38:38  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IOrdTmMainRSV {
    
   /**
    *  
    * queryOrderTmMain:(天猫订单查询). <br/> 
    * 
    * @param rOrdTmMainReq
    * @return
    * @throws BusinessException 
    * @since JDK 1.6
    */
   public PageResponseDTO<ROrdTmMainResp> queryOrderTmMain(ROrdTmMainReq rOrdTmMainReq) throws BusinessException;
   
   /**
    *  
    * queryOrderTmMainNoGift:(查询未赠送积分的天猫订单). <br/> 
    * 
    * @param rOrdTmMainReq
    * @return
    * @throws BusinessException 
    * @since JDK 1.6
    */
   public List<ROrdTmMainResp> queryOrderTmMainNoGift(ROrdTmMainReq rOrdTmMainReq) throws BusinessException;
   
   /**
    *  
    * validOrderTmMain:(验证天猫订单是否成功). <br/> 
    * 
    * @param rOrdTmMainReq
    * @return
    * @throws BusinessException 
    * @since JDK 1.6
    */
   public Boolean validOrderTmMain(ROrdTmMainReq rOrdTmMainReq) throws BusinessException;
   
   /**
    *  
    * updateOrderTmMainScore:(更新天猫订单主表赠送积分). <br/>
    * 
    * @param rBackApplyReq
    * @throws BusinessException 
    * @since JDK 1.6
    */
   public void updateOrderTmMainScore(ROrdTmMainReq rOrdTmMainReq) throws BusinessException;
   
   /**
    * 
    * updateOrderTmMainStaff:(更新天猫订单主表商城会员id). <br/> 
    * 
    * @param rBackApplyReq
    * @throws BusinessException 
    * @since JDK 1.6
    */
   public void updateOrderTmMainStaff(ROrdTmMainReq rOrdTmMainReq) throws BusinessException;
}



