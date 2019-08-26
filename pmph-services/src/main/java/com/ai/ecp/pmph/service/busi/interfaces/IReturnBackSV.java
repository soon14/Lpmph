package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.order.dao.model.OrdBackApply;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dubbo.dto.*;
import com.ai.ecp.pmph.dubbo.dto.CompensateBackResp;
import com.ai.ecp.pmph.dubbo.dto.OrdCompensateReq;
import com.ai.ecp.pmph.dubbo.dto.RBackApplyPmphOrdReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

public interface IReturnBackSV {
    
    public ROrdCartsChkResponse queryBackOperChk(ROrderDetailsRequest info);
    
    public RBackApplyInfoResp calCulateShareApply(RBackApplyReq rBackApplyReq, OrdBackApply ordBackApply) throws BusinessException;
    
    public long calCulateScaleApply(RBackApplyReq rBackApplyReq, OrdBackApply ordBackApply,OrdMain ordMain) throws BusinessException ;
    
    public ROrdReturnRefundResp calCulateBackInfo(RBackApplyPmphOrdReq resp);
    
    /**
     * 
     * calCulateBackInfo:(退货审核时查询当前退货积分和退款信息). <br/> 
     * 
     * @param req
     * @return 
     * @since JDK 1.6
     */
    public ROrdReturnRefundResp calCulateBackInfo (ROrderBackReq req);
    
    public boolean modifyBackMoney(ROrdReturnRefundReq req);
    
    /**
     * 
     * saveCompensateBackMoney:(补偿性退款). <br/> 
     * 
     * @param req 
     * @since JDK 1.6
     */
    public void saveCompensateBackMoney(OrdCompensateReq req);
    
    /**
     * 
     * queryCompensateBackMoney:(查询补偿性退款信息). <br/> 
     * 
     * @param rOrderBackReq
     * @return 
     * @since JDK 1.6
     */
    public CompensateBackResp queryCompensateBackMoney(ROrderBackReq rOrderBackReq);


    /**
     *  计算比例
     * @param resp
     * @return
     */
    public long calCulateScaleApply(RBackApplyPmphOrdReq resp) throws BusinessException;

}

