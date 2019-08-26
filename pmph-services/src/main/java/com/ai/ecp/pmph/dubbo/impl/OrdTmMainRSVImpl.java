package com.ai.ecp.pmph.dubbo.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.pmph.dao.model.OrdMainTM;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdTmMainRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdTmMainSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:ecp-services-order-server <br>
 * Description: <br>
 * Date:2016年2月23日下午4:44:59  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class OrdTmMainRSVImpl implements IOrdTmMainRSV {

    @Resource
    private IOrdTmMainSV ordTmMainSV;
    
    @Resource
    private IOrdImportLogSV ordImportLogSV;

    private static final String MODULE = OrdTmMainRSVImpl.class.getName();

    @Override
    public PageResponseDTO<ROrdTmMainResp> queryOrderTmMain(ROrdTmMainReq rOrdTmMainReq)
            throws BusinessException {
        if(rOrdTmMainReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        
        if(rOrdTmMainReq.getBegDate() == null){
            LogUtil.info(MODULE, "开始时间不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311004); 
        }
        if(rOrdTmMainReq.getEndDate() == null){
            LogUtil.info(MODULE, "结束时间不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311004); 
        }
        PageResponseDTO<ROrdTmMainResp> rdor = null;
        try {
            rdor = this.ordTmMainSV.queryTmOrderMain(rOrdTmMainReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310053);
        }
        return rdor;
    } 
    
    @Override
    public List<ROrdTmMainResp> queryOrderTmMainNoGift(ROrdTmMainReq rOrdTmMainReq)
            throws BusinessException {
        if(rOrdTmMainReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        
        if(rOrdTmMainReq.getCreateTime() == null){
            LogUtil.info(MODULE, "录入时间不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311004); 
        }
        List<ROrdTmMainResp> rdor = null;
        try {
            rOrdTmMainReq.setRwScoreFlag("0");
            rdor = this.ordTmMainSV.queryOrderTmMainNoGift(rOrdTmMainReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310053);
        }
        return rdor;
    }
    
    @Override
    public Boolean validOrderTmMain(ROrdTmMainReq rOrdTmMainReq) 
            throws BusinessException{
        if(rOrdTmMainReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrdTmMainReq.getOrderCode())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        } 
        try {
            OrdMainTM ordMainTM = this.ordTmMainSV.queryTmOrderMainInfo(rOrdTmMainReq);
            if(ordMainTM != null){
                return true;
            }
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310053);
        }
        return false;
        
    }
    
    @Override
    public void updateOrderTmMainScore(ROrdTmMainReq rOrdTmMainReq)
            throws BusinessException {
        if(rOrdTmMainReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrdTmMainReq.getOrderCode())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        try {
            this.ordTmMainSV.updateOrderTmMainScore(rOrdTmMainReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310051);
        }
    }
    
    @Override
    public void updateOrderTmMainStaff(ROrdTmMainReq rOrdTmMainReq)
            throws BusinessException {
        if(rOrdTmMainReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrdTmMainReq.getOrderCode())){
            LogUtil.info(MODULE, "订单编号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        try {
            this.ordTmMainSV.updateOrderTmMainStaff(rOrdTmMainReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310052);
        }
    } 

}
