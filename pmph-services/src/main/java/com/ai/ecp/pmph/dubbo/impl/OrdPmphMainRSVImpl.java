package com.ai.ecp.pmph.dubbo.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.order.dao.model.OrdBackApply;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dubbo.dto.OrdMainCompensateResponse;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdPmphMainRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdPmphMainSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class OrdPmphMainRSVImpl implements IOrdPmphMainRSV {

    @Resource
    private IOrdPmphMainSV ordPmphMainSV;
    
    private static final String MODULE = OrdPmphMainRSVImpl.class.getName();
    
    @Override
    public PageResponseDTO<RCustomerOrdResponse> queryOrderByStaffId(
            RQueryOrderRequest rQueryOrderRequest) throws BusinessException {
        if(rQueryOrderRequest == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(rQueryOrderRequest.getStaffId() ==null || rQueryOrderRequest.getStaffId() < 1){
            LogUtil.info(MODULE, "买家id不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311003);
        }
        if(rQueryOrderRequest.getSiteId() == null ){
            LogUtil.info(MODULE, "所属站点不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300001);
        }
        
        rQueryOrderRequest.setSysType(OrdConstants.SysType.SYS_TYPE_BASE);
        PageResponseDTO<RCustomerOrdResponse> rcor = null;
        try {
            rcor = ordPmphMainSV.queryOrderByStaffIdPage(rQueryOrderRequest);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310001);
        }
        
        return rcor;
    }
    
    @Override
    public PageResponseDTO<OrdMainCompensateResponse> queryOrderByCompensatePage(
            RQueryOrderRequest rQueryOrderRequest) throws BusinessException {
        if(rQueryOrderRequest == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        PageResponseDTO<OrdMainCompensateResponse> rcor = null;
        try {
            rcor = ordPmphMainSV.queryOrderByCompensatePage(rQueryOrderRequest);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310001);
        }        
        return rcor;
    }
    
    @Override
    public boolean checkBackMoney(ROrdReturnRefundReq req) throws BusinessException{
    	if(req == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
    	
    	if(StringUtil.isBlank(req.getOrderId())){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
    	
    	return ordPmphMainSV.checkBackMoney(req);
    }
    
    @Override
    public ROrdMainResponse queryOrderMain(ROrderIdRequest req)throws BusinessException{
        if(req == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        
        if(StringUtil.isBlank(req.getOrderId())){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000);            
        }
        ROrdMainResponse resp = null;
        try {
            resp = ordPmphMainSV.queryOrderMain(req);
            return resp;
        }catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        }
    }
   
}

