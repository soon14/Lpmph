package com.ai.ecp.pmph.dubbo.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.pmph.dubbo.interfaces.IPayQuartzInfoForZYOrdRSV;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayZYDigitalSV;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayZYExaminationSV;
import com.ai.ecp.pmph.service.busi.interfaces.IPayQuartzInfoPmphSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class PayQuartzInfoForZYOrdRSVImpl implements IPayQuartzInfoForZYOrdRSV {
    
    @Resource
    private IPayZYDigitalSV payZYDigitalSV;
    
    @Resource
    private IPayZYExaminationSV payZYExaminationSV;
    
    @Resource
    private IPayQuartzInfoPmphSV payQuartzInfoPmphSV;
    
    private static final String MODULE = PayQuartzInfoForZYOrdRSVImpl.class.getName();

    @Override
    public void dealZYDigitalOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest)
            throws BusinessException {
        if(rPayQuartzInfoRequest == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.PayInputMsgCode.PAY_INPUT_300000); 
        }
        if(StringUtil.isBlank(rPayQuartzInfoRequest.getOrderId())){
            LogUtil.info(MODULE, "订单编码不能为空");
            throw new BusinessException(MsgConstants.PayInputMsgCode.PAY_INPUT_300001); 
        }
        if(StringUtil.isBlank(rPayQuartzInfoRequest.getSubOrder())){
            LogUtil.info(MODULE, "子订单编码不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311001); 
        }
        if(rPayQuartzInfoRequest.getStaffId()<=0){
            LogUtil.info(MODULE, "买家ID不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311003); 
        }
        LogUtil.info(MODULE, rPayQuartzInfoRequest.toString());
        try {
            PaySuccInfo paySuccInfo = new PaySuccInfo();
            paySuccInfo.setOrderId(rPayQuartzInfoRequest.getOrderId());
            paySuccInfo.setSubOrder(rPayQuartzInfoRequest.getSubOrder());
            paySuccInfo.setStaffId(rPayQuartzInfoRequest.getStaffId());
            payZYDigitalSV.deal(paySuccInfo);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常==="+be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常==="+e);
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310012);
        }
    }

    @Override
    public void dealZYExaminationOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest)
            throws BusinessException {
        if(rPayQuartzInfoRequest == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.PayInputMsgCode.PAY_INPUT_300000); 
        }
        if(StringUtil.isBlank(rPayQuartzInfoRequest.getOrderId())){
            LogUtil.info(MODULE, "订单编码不能为空");
            throw new BusinessException(MsgConstants.PayInputMsgCode.PAY_INPUT_300001); 
        }
        if(StringUtil.isBlank(rPayQuartzInfoRequest.getSubOrder())){
            LogUtil.info(MODULE, "子订单编码不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311001); 
        }
        if(rPayQuartzInfoRequest.getStaffId()<=0){
            LogUtil.info(MODULE, "买家ID不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311003); 
        }
        LogUtil.info(MODULE, rPayQuartzInfoRequest.toString());
        try {
            PaySuccInfo paySuccInfo = new PaySuccInfo();
            paySuccInfo.setOrderId(rPayQuartzInfoRequest.getOrderId());
            paySuccInfo.setSubOrder(rPayQuartzInfoRequest.getSubOrder());
            paySuccInfo.setStaffId(rPayQuartzInfoRequest.getStaffId());
            payZYExaminationSV.deal(paySuccInfo);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常==="+be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常==="+e);
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310013);
        }
    }

    @Override
    public List<RPayQuartzInfoResponse> queryNotDealZYDigitalOrder(
            RPayQuartzInfoRequest rPayQuartzInfoRequest) {
        if(rPayQuartzInfoRequest == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.PayInputMsgCode.PAY_INPUT_300000); 
        }
        LogUtil.info(MODULE, rPayQuartzInfoRequest.toString());
        try {
            return this.payQuartzInfoPmphSV.queryNotDealZYDigitalOrder(rPayQuartzInfoRequest);
            
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常==="+be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常==="+e);
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310014);
        }
    }

    @Override
    public List<RPayQuartzInfoResponse> queryNotDealZYExaminationOrder(
            RPayQuartzInfoRequest rPayQuartzInfoRequest) {
        if(rPayQuartzInfoRequest == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.PayInputMsgCode.PAY_INPUT_300000); 
        }
        LogUtil.info(MODULE, rPayQuartzInfoRequest.toString());
        try {
            return this.payQuartzInfoPmphSV.queryNotDealZYExaminationOrder(rPayQuartzInfoRequest);
            
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常==="+be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常==="+e);
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310014);
        }
    }

}

