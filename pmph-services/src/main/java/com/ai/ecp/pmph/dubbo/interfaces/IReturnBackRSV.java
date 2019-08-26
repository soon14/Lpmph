package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.order.dubbo.dto.RBackApplyOrdResp;
import com.ai.ecp.order.dubbo.dto.RBackApplyReq;
import com.ai.ecp.order.dubbo.dto.RBackConfirmReq;
import com.ai.ecp.order.dubbo.dto.RBackReviewReq;
import com.ai.ecp.order.dubbo.dto.RBackReviewResp;
import com.ai.ecp.order.dubbo.dto.ROrderBackReq;
import com.ai.ecp.order.dubbo.dto.ROrderBackResp;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.pmph.dubbo.dto.CompensateBackResp;
import com.ai.ecp.pmph.dubbo.dto.OrdCompensateReq;
import com.ai.ecp.pmph.dubbo.dto.RBackApplyPmphOrdReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

public interface IReturnBackRSV {

    public ROrdCartsChkResponse returnCheck(ROrderDetailsRequest info) throws BusinessException;
    
    public void saveBackMoneyApply(RBackApplyReq rBackApplyReq) throws BusinessException;
    
    public void saveBackGdsApply(RBackApplyReq rBackApplyReq) throws BusinessException;
    
    public ROrdReturnRefundResp calCulateBackInfo(RBackApplyPmphOrdReq resp)throws BusinessException;
  
    public ROrdReturnRefundResp calCulateBackInfo (ROrderBackReq req)throws BusinessException;
    
    public boolean modifyBackMoney(ROrdReturnRefundReq req)throws BusinessException;
    
    public void saveCompensateBackMoney(OrdCompensateReq req)throws BusinessException;
    
    public void saveBackGdsPayed(RBackConfirmReq rBackConfirmReq) throws BusinessException;
        
    public PageResponseDTO<ROrderBackResp> queryBackMoneyByShop(ROrderBackReq rOrderBackReq)throws BusinessException ;
    
    public CompensateBackResp queryCompensateBackMoney(ROrderBackReq rOrderBackReq)throws BusinessException ;
    
    public RBackReviewResp queryBackGdsReview(ROrderBackReq rOrderBackReq) throws BusinessException;
    
    public void saveBackGdsReview(RBackReviewReq rBackGdsReviewReq) throws BusinessException;
    
    public RBackReviewResp queryBackMoneyReview(ROrderBackReq rOrderBackReq)  throws BusinessException ;
    
    public void saveBackMoneyReview(RBackReviewReq rBackGdsReviewReq) throws BusinessException;
    
    public RBackApplyOrdResp queryBackOrderSub(ROrderBackReq rOrderBackReq)
            throws BusinessException;
}

