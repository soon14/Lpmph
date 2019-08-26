package com.ai.ecp.pmph.service.busi.interfaces;

import java.util.List;

import com.ai.ecp.pmph.dao.model.OrdMainTM;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainResp;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.interfaces.IGeneralSQLSV;

public interface IOrdTmMainSV  extends IGeneralSQLSV{
    
    /**
     * 
     * queryTmOrderMain:(天猫订单查询). <br/> 
     * 
     * @param rOrdTmMainReq
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public PageResponseDTO<ROrdTmMainResp> queryTmOrderMain(ROrdTmMainReq rOrdTmMainReq) throws BusinessException;
    
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
     * queryTmOrderMainInfo:(天猫订单查询). <br/> 
     * 
     * @param rOrdTmMainReq
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public OrdMainTM queryTmOrderMainInfo(ROrdTmMainReq rOrdTmMainReq)throws BusinessException;
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
    
    /** 
     * queryTMOrderByOrderId:(根据订单号查询订单信息). <br/> 
     * @param orderId
     * @throws BusinessException 
     * @since JDK 1.6 
     */ 
    public OrdMainTM queryTMOrderByOrderId(String orderId) throws BusinessException;
	
}
