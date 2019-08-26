package com.ai.ecp.pmph.dubbo.impl;

import javax.annotation.Resource;

import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.order.dubbo.dto.RBackApplyOrdResp;
import com.ai.ecp.order.dubbo.dto.RBackApplyReq;
import com.ai.ecp.order.dubbo.dto.RBackApplyResp;
import com.ai.ecp.order.dubbo.dto.RBackConfirmReq;
import com.ai.ecp.order.dubbo.dto.RBackReviewReq;
import com.ai.ecp.order.dubbo.dto.RBackReviewResp;
import com.ai.ecp.order.dubbo.dto.ROrderBackReq;
import com.ai.ecp.order.dubbo.dto.ROrderBackResp;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.order.facade.interfaces.eventual.IBackPayOrderSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackApplySV;
import com.ai.ecp.pmph.dubbo.dto.CompensateBackResp;
import com.ai.ecp.pmph.dubbo.dto.OrdCompensateReq;
import com.ai.ecp.pmph.dubbo.dto.RBackApplyPmphOrdReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.pmph.dubbo.interfaces.IReturnBackRSV;
import com.ai.ecp.pmph.facade.interfaces.eventual.IBackPayPmphOrderSV;
import com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public  class ReturnBackRSVImpl implements IReturnBackRSV {
    
    private static final String MODULE = ReturnBackRSVImpl.class.getName();
    
    @Resource
    private IReturnBackSV returnBackSV;
    
    @Resource
    private IOrdBackApplySV ordBackApplySV;

    @Resource
    private IBackPayOrderSV backPayOrderSV;
    
    @Override
    public ROrdCartsChkResponse returnCheck(ROrderDetailsRequest info) throws BusinessException {
        if(info == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(info.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        if(StringUtil.isBlank(info.getOper())){
            LogUtil.info(MODULE, "操作类型不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311010);
        }
        ROrdCartsChkResponse rep = null;
        try {
            rep = this.returnBackSV.queryBackOperChk(info);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310000);
        }
        
        return rep;
   
    }
    
    
    @Override
    public void saveBackMoneyApply(RBackApplyReq rBackApplyReq) throws BusinessException {
        
        if(rBackApplyReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rBackApplyReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        try {
            rBackApplyReq.setApplyType(BackConstants.ApplyType.REFUND);
            this.ordBackApplySV.saveBackMoneyApply(rBackApplyReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310042);
        }
    }
    
    @Override
    public void saveBackGdsApply(RBackApplyReq rBackApplyReq) throws BusinessException {
        //orderId 不能为空
        //明细列表不能为空
        if(rBackApplyReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rBackApplyReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        try {
            rBackApplyReq.setApplyType(BackConstants.ApplyType.BACK_GDS);
            this.ordBackApplySV.saveBackGdsApply(rBackApplyReq);           
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310037);
        }
    }
    /**
     * 
     * TODO 构造订单优惠信息. 
     * @see com.ai.ecp.pmph.dubbo.interfaces.IReturnBackRSV#calCulateBackInfo(com.ai.ecp.pmph.dubbo.dto.RBackApplyPmphOrdReq)
     */
    @Override
    public ROrdReturnRefundResp calCulateBackInfo(RBackApplyPmphOrdReq resp)
            throws BusinessException {       
        if(StringUtil.isBlank(resp.getApplyType())){
            LogUtil.info(MODULE, "申请类型不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311010); 
        }
        try {
           return this.returnBackSV.calCulateBackInfo(resp);       
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;           
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310037);
        }
        
    }


    @Override
    public ROrdReturnRefundResp calCulateBackInfo(ROrderBackReq req) throws BusinessException {
        if(req == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(req.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        try {
           return this.returnBackSV.calCulateBackInfo(req);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;           
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310037);
        }
    }

    /**
     * 
     * TODO 修改退款金额后重新冻结积分（可选）. 
     * @see com.ai.ecp.pmph.dubbo.interfaces.IReturnBackRSV#modifyBackMoney(com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq)
     */
    @Override
    public boolean modifyBackMoney(ROrdReturnRefundReq req) throws BusinessException {
        if(req == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(req.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        try {
            return this.returnBackSV.modifyBackMoney(req);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;           
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310037);
        }
    }


    @Override
    public void saveCompensateBackMoney(OrdCompensateReq req) throws BusinessException {
        if(req == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(req.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        if(req.getBackMoney()<=0l){
            LogUtil.info(MODULE, "退款金额不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        if(StringUtil.isBlank(req.getOrderId())){
            LogUtil.info(MODULE, "备注不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311108); 
        }
        try {
             this.returnBackSV.saveCompensateBackMoney(req);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;           
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310037);
        }
    }


    @Override
    public void saveBackGdsPayed(RBackConfirmReq rBackConfirmReq) throws BusinessException {
        if(rBackConfirmReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rBackConfirmReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        if(rBackConfirmReq.getBackId() ==null){
            LogUtil.info(MODULE, "退款或退货申请单号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311011); 
        }
        try {
//            this.ordBackApplySV.saveBackGdsPayed(rBackConfirmReq);
            ROrderBackReq rOrderBackReq = new ROrderBackReq();
            rOrderBackReq.setOrderId(rBackConfirmReq.getOrderId());
            rOrderBackReq.setBackId(rBackConfirmReq.getBackId());
            RBackApplyResp  chkResp =this.ordBackApplySV.queryOrdBackApply(rOrderBackReq);
            if(chkResp.getApplyType().equals(BackConstants.ApplyType.BACK_GDS)){
                if(chkResp == null || !(BackConstants.ChkStatus.CHK_BACKGDS_REFUND.contains(chkResp.getStatus()))){
                    LogUtil.info(MODULE, "申请单状态不对"+rBackConfirmReq.getBackId()+chkResp.getStatus()+BackConstants.ChkStatus.CHK_BACKGDS_REVIEW);
                    throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_REFUND);
                }
            }else{
                if(chkResp == null || !(BackConstants.ChkStatus.CHK_REFUND_REFUND.contains(chkResp.getStatus()))){
                    LogUtil.info(MODULE, "申请单状态不对"+rBackConfirmReq.getBackId()+chkResp.getStatus()+BackConstants.ChkStatus.CHK_BACKGDS_REVIEW);
                    throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_REFUND);
                }
            }
            this.backPayOrderSV.dealMethod(rBackConfirmReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310041);
        }
    }
    
    @Override
    public PageResponseDTO<ROrderBackResp> queryBackMoneyByShop(ROrderBackReq rOrderBackReq)
            throws BusinessException {
        //ShopId 不能为空
        if(rOrderBackReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(rOrderBackReq.getShopId() ==null || rOrderBackReq.getShopId() < 1){
            LogUtil.info(MODULE, "店铺id");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311002);
        }
        PageResponseDTO<ROrderBackResp> resp = null;
        try {
            rOrderBackReq.setApplyType(BackConstants.ApplyType.REFUND);
            resp = this.ordBackApplySV.queryBackGdsByShop(rOrderBackReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310045);
        }
        return resp;
    }

    @Override
    public CompensateBackResp queryCompensateBackMoney(ROrderBackReq rOrderBackReq)
            throws BusinessException {
        if(rOrderBackReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrderBackReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        CompensateBackResp resp = null;
        try {
            rOrderBackReq.setApplyType(BackConstants.ApplyType.REFUND);
            
            resp = returnBackSV.queryCompensateBackMoney(rOrderBackReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310048);
        }
        return resp;
    }
    
    @Override
    public RBackReviewResp queryBackGdsReview(ROrderBackReq rOrderBackReq)
            throws BusinessException {
        if(rOrderBackReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrderBackReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        RBackReviewResp resp = null;
        try {
            rOrderBackReq.setApplyType(BackConstants.ApplyType.BACK_GDS);
            resp = this.ordBackApplySV.queryBackReview(rOrderBackReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310047);
        }
        return resp;
    }

    @Override
    public void saveBackGdsReview(RBackReviewReq rBackGdsReviewReq) throws BusinessException {
        //审核状态 值
        //orderId
        //backId
        //支付方式不能为空
        LogUtil.info(MODULE, "退货审核开始");
        if(rBackGdsReviewReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rBackGdsReviewReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        if(rBackGdsReviewReq.getBackId() ==null){
            LogUtil.info(MODULE, "退款或退货申请单号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311011); 
        }
        if(StringUtil.isBlank(rBackGdsReviewReq.getStatus())){
            LogUtil.info(MODULE, "审核状态值不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311112); 
        }
        if(rBackGdsReviewReq.getStatus().trim().equals(BackConstants.Status.REVIEW_PASS) 
                && StringUtil.isBlank(rBackGdsReviewReq.getPayType())){
            LogUtil.info(MODULE, "退款方式不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311012); 
        }
        try {
            rBackGdsReviewReq.setApplyType(BackConstants.ApplyType.BACK_GDS);
            ROrderBackReq rOrderBackReq = new ROrderBackReq();
            rOrderBackReq.setOrderId(rBackGdsReviewReq.getOrderId());
            rOrderBackReq.setBackId(rBackGdsReviewReq.getBackId());
            RBackApplyResp  chkResp =this.ordBackApplySV.queryOrdBackApply(rOrderBackReq);
            if(chkResp == null || !(BackConstants.ChkStatus.CHK_BACKGDS_REVIEW.contains(chkResp.getStatus()))){
                LogUtil.info(MODULE, "申请单状态不对"+rBackGdsReviewReq.getBackId()+chkResp.getStatus()+BackConstants.ChkStatus.CHK_BACKGDS_REVIEW);
                throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_REVIEW);
            }
            this.ordBackApplySV.saveBackReview(rBackGdsReviewReq);
            LogUtil.info(MODULE, "退货审核结束");
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310039);
        }
    }
    
    @Override
    public RBackReviewResp queryBackMoneyReview(ROrderBackReq rOrderBackReq)
            throws BusinessException {
        if(rOrderBackReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrderBackReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        RBackReviewResp resp = null;
        try {
            rOrderBackReq.setApplyType(BackConstants.ApplyType.REFUND);
            resp = this.ordBackApplySV.queryBackReview(rOrderBackReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310048);
        }
        return resp;
    }
    
    @Override
    public void saveBackMoneyReview(RBackReviewReq rBackGdsReviewReq) throws BusinessException {
        if(rBackGdsReviewReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rBackGdsReviewReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        if(rBackGdsReviewReq.getBackId() ==null){
            LogUtil.info(MODULE, "退款或退货申请单号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311011); 
        }
        if(StringUtil.isBlank(rBackGdsReviewReq.getStatus())){
            LogUtil.info(MODULE, "审核状态值不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311112); 
        }
        if(StringUtil.isBlank(rBackGdsReviewReq.getPayType())){
            LogUtil.info(MODULE, "退款方式不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311012); 
        }
        try {
            rBackGdsReviewReq.setApplyType(BackConstants.ApplyType.REFUND);
            
            //校验申请状态是否正确
            ROrderBackReq rOrderBackReq = new ROrderBackReq();
            rOrderBackReq.setOrderId(rBackGdsReviewReq.getOrderId());
            rOrderBackReq.setBackId(rBackGdsReviewReq.getBackId());
            RBackApplyResp  chkResp =this.ordBackApplySV.queryOrdBackApply(rOrderBackReq);
            if(chkResp == null || !(BackConstants.ChkStatus.CHK_REFUND_REVIEW.contains(chkResp.getStatus()))){
                LogUtil.info(MODULE, "申请单状态不对"+rBackGdsReviewReq.getBackId()+chkResp.getStatus()+BackConstants.ChkStatus.CHK_REFUND_REVIEW);
                throw new BusinessException(MsgConstants.ChkMsgCode.CHK_REFUND_REVIEW);
            }
            this.ordBackApplySV.saveBackReview(rBackGdsReviewReq);

        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310043);
        }
    }
    
    @Override
    public RBackApplyOrdResp queryBackOrderSub(ROrderBackReq rOrderBackReq)
            throws BusinessException {
        if(rOrderBackReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrderBackReq.getOrderId())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        RBackApplyOrdResp resp = null;
        try {
            resp = this.ordBackApplySV.queryBackOrderSub(rOrderBackReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310035);
        }
        return resp;
    }
    

}

