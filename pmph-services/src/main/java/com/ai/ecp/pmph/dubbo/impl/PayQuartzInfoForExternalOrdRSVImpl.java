package com.ai.ecp.pmph.dubbo.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.pmph.dubbo.interfaces.IPayQuartzInfoForExternalOrdRSV;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayExternalMedicareSV;
import com.ai.ecp.pmph.service.busi.interfaces.IPayQuartzInfoPmphSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class PayQuartzInfoForExternalOrdRSVImpl implements IPayQuartzInfoForExternalOrdRSV {
	
	@Resource
	private IPayExternalMedicareSV payExternalMedicareSV;
	
	@Resource
    private IPayQuartzInfoPmphSV payQuartzInfoPmphSV;
	
	private static final String MODULE = PayQuartzInfoForExternalOrdRSVImpl.class.getName();
	
	@Override
	public List<RPayQuartzInfoResponse> queryNotDealExternalMedicareOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest) {
		if(rPayQuartzInfoRequest == null){
            LogUtil.info(MODULE, "入参对象不能为空");
            throw new BusinessException(MsgConstants.PayInputMsgCode.PAY_INPUT_300000); 
        }
        LogUtil.info(MODULE, rPayQuartzInfoRequest.toString());
        try {
        	return this.payQuartzInfoPmphSV.queryNotDealExternalMedicareOrder(rPayQuartzInfoRequest);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常==="+be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常==="+e);
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310014);
        }
	}

	@Override
	public void dealExternalMedicareOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest) throws BusinessException {
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
            payExternalMedicareSV.deal(paySuccInfo);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常==="+be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常==="+e);
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310012);
        }
	}

}
