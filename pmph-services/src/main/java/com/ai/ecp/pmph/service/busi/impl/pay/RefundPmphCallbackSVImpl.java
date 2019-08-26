package com.ai.ecp.pmph.service.busi.impl.pay;

import javax.annotation.Resource;
import com.ai.ecp.order.dao.model.OrdBackApply;
import com.ai.ecp.order.dao.model.PayRefundResult;
import com.ai.ecp.order.dubbo.dto.RBackConfirmReq;
import com.ai.ecp.order.dubbo.dto.pay.PayRefundSuccInfo;
import com.ai.ecp.order.dubbo.util.MsgConstants.PayServiceMsgCode;
import com.ai.ecp.order.facade.interfaces.eventual.IBackPayOrderSV;
import com.ai.ecp.order.service.busi.impl.pay.RefundCallbackSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackApplySV;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayRefundResultSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class RefundPmphCallbackSVImpl extends RefundCallbackSV{

    public static final String module = RefundPmphCallbackSVImpl.class.getName();

    @Resource
    protected IPayRefundResultSV payRefundResultSV;
    
    @Resource
    protected IBackPayOrderSV backPayOrderSV;
    
    @Resource
    private IOrdBackApplySV ordBackApplySV;
    
    @Override
    public void saveRefundCallback(PayRefundSuccInfo payRefundSuccInfo) {
        try{
            //
            PayRefundResult bean = payRefundResultSV.getPayRefundResultByPaywayBackId(payRefundSuccInfo.getPayWay(), payRefundSuccInfo.getBackId());
            PayRefundResult payRefundResult = new PayRefundResult();
            payRefundResult.setId(bean.getId());
            payRefundResult.setRefundStatus(payRefundSuccInfo.getFlag());
            int dealCount = payRefundResultSV.updateRefund(payRefundResult);
            //过滤已经处理过的
            if(dealCount==0){
                throw new BusinessException(PayServiceMsgCode.PAY_SERVER_310020);
            }else{
                OrdBackApply ordBackApply = ordBackApplySV.queryOrdBackApplyByBackId(payRefundSuccInfo.getBackId());
                RBackConfirmReq rBackConfirmReq = new RBackConfirmReq();
                rBackConfirmReq.setBackId(payRefundSuccInfo.getBackId());
                rBackConfirmReq.setOrderId(payRefundSuccInfo.getOrderId());
                rBackConfirmReq.getStaff().setId(payRefundSuccInfo.getStaffId());
                if(StringUtil.isNotBlank(ordBackApply.getIsCompenstate())&&!ordBackApply.getIsCompenstate().equals("0")){
                    //正常退款退货处理事务订单逻辑                  
                    backPayOrderSV.dealMethod(rBackConfirmReq);
                }else{
                    //补偿性退款不走子事务             
                    ordBackApplySV.saveBackGdsPayed(rBackConfirmReq);
                }
            }
        }catch(Exception e){
            LogUtil.error(module, "严重错误：退款回调后订单状态变更失败！",e);
            throw new BusinessException(PayServiceMsgCode.PAY_SERVER_310018);
        }
    }
}

