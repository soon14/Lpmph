package com.ai.ecp.pmph.service.busi.impl;

import javax.annotation.Resource;

import com.ai.ecp.pmph.dubbo.dto.OrderBackMainRWReqDTO;
import com.ai.ecp.pmph.service.busi.interfaces.IScoreToOrderRWSV;
import com.ai.ecp.pmph.service.busi.interfaces.IStaffUnionRWSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.OrderBackCheckReqDTO;
import com.ai.ecp.staff.dubbo.dto.OrderBackMainReqDTO;
import com.ai.ecp.staff.dubbo.dto.OrderBackSubReqDTO;
import com.ai.ecp.staff.service.busi.acct.interfaces.IAcctToOrderSV;
import com.ai.ecp.staff.service.busi.score.interfaces.IScoreToOrderOtherSV;
import com.ai.ecp.staff.service.busi.union.interfaces.IOrdBackCheckSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;

public class StaffUnionRWSVImpl implements IStaffUnionRWSV{
	
	@Resource
    private IAcctToOrderSV acctToOrderSV;
	
	@Resource
	private IScoreToOrderRWSV scoreToOrderRWSV;
	
	@Resource
    private IOrdBackCheckSV ordBackCheckSV;
	
	@Resource
    private IScoreToOrderOtherSV scoreToOrderOtherSV;

	@Override
	public long selTotalScoreByBackOrderRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException {
		return scoreToOrderRWSV.selTotalScoreByBackOrderRW(req);
	}

	@Override
	public boolean saveScoreAcctForOrderBackRW(OrderBackMainRWReqDTO<OrderBackSubReqDTO> rwreq, OrderBackSubReqDTO orderReq)
			throws BusinessException {
		OrderBackMainReqDTO<OrderBackSubReqDTO> req = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
		ObjectCopyUtil.copyObjValue(rwreq, req, null, true);
		boolean isOpt = false;
        Long backId = 0L;
        //校验该笔退款退货订单是否已经操作过了。
        if (req.getBackId() != null && req.getBackId() != 0L) {
            backId = req.getBackId();
            isOpt = ordBackCheckSV.checkOrdBack(req.getBackId());
        } else if (orderReq != null && orderReq.getBackId() != null && orderReq.getBackId() != 0L) {
            backId = orderReq.getBackId();
            isOpt = ordBackCheckSV.checkOrdBack(orderReq.getBackId());
        }
        //已经操作过，则直接返回true，记录一下日志
        if (isOpt) {
            LogUtil.info("StaffUnionSVImpl", "该笔退款/退货订单已经处理过了。backId为：" + backId);
            return true;
        }
        if (req != null) {
            /*1、返还使用的积分，这个方法目前没用，积分订单在订单域有控制，不让退款退货的*/
          /*  for (OrderBackSubReqDTO orderBack : req.getList()) {
                scoreToOrderOtherSV.saveScoreUseForOrderRefund(orderBack);
            }*/
            /*2、解冻订单获得的积分，并扣除*/
            scoreToOrderRWSV.saveScoreAddForOrderBackRW(rwreq);
        }
        if (orderReq != null) {
        	/*4、返还资金账户使用的金额*/
            acctToOrderSV.saveAcctUseForOrderBack(orderReq);
        }
        /*5、业务处理完成后，记录此次退款退货申请id，以免下次重复处理*/
        OrderBackCheckReqDTO checkReq = new OrderBackCheckReqDTO();
        checkReq.setBackId(backId);
        ordBackCheckSV.saveOraBackCheck(checkReq);
        return true;
	}

	@Override
	public Long saveScoreFrozenForOrderBackRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException {
		return scoreToOrderRWSV.saveScoreFrozenForOrderBackRW(req);
	}

	@Override
	public Long saveScoreFrozenModifyForOrderBackRW(OrderBackMainRWReqDTO<OrderBackSubReqDTO> req)
			throws BusinessException {
		return scoreToOrderRWSV.saveScoreFrozenModifyForOrderBackRW(req);
	}

	@Override
	public long selTotalScoreByBackOrderAllRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException {
		return scoreToOrderRWSV.selTotalScoreByBackOrderAllRW(req);
	}

}

