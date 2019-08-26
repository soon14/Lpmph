package com.ai.ecp.pmph.dubbo.impl;

import javax.annotation.Resource;

import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdTmSubRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdTmSubSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class OrdTmSubRSVImpl implements IOrdTmSubRSV {

    private static final String MODULE = OrdTmSubRSVImpl.class.getName();
    
    @Resource
    private IOrdTmSubSV ordTmSubSV;

    @Override
    public PageResponseDTO<ROrdTmSubResp> queryOrderTmSubByOrderId(ROrdTmSubReq rOrdTmSubReq)
            throws BusinessException {
        if(rOrdTmSubReq == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_300000); 
        }
        if(StringUtil.isBlank(rOrdTmSubReq.getOrderId())){
            LogUtil.info(MODULE, "订单号不能为空");
            throw new BusinessException(MsgConstants.InputMsgCode.ORD_INPUT_311000); 
        }
        PageResponseDTO<ROrdTmSubResp> rdors = null;
        try {
            rdors = this.ordTmSubSV.queryOrderTmSubByOrderId(rOrdTmSubReq);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===",be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===",e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310000);
        }
        
        return rdors;
    }
    
    

}

