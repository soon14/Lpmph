package com.ai.ecp.pmph.facade.impl.eventual;

import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dubbo.dto.*;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.order.facade.impl.eventual.BackPayStaffSVImpl;
import com.ai.ecp.order.facade.interfaces.eventual.IBackPayStaffSV;
import com.ai.ecp.order.service.busi.interfaces.*;
import com.ai.ecp.pmph.dubbo.dto.OrderBackMainRWReqDTO;
import com.ai.ecp.pmph.dubbo.dto.RBackApplyPmphOrdReq;
import com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV;
import com.ai.ecp.pmph.service.busi.interfaces.IStaffUnionRWSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.OrderBackSubReqDTO;
import com.ai.paas.utils.LogUtil;
import com.alibaba.fastjson.JSON;
import com.distribute.tx.common.TransactionStatus;
import net.sf.json.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class BackPayStaffPmphSVImpl extends BackPayStaffSVImpl {
    
    @Resource
    private IStaffUnionRWSV staffUnionRWSV;

    @Resource
    private IOrdBackApplySV ordBackApplySV;

    @Resource
    private IReturnBackSV returnBackSV;

    @Resource
    private IOrdBackDiscountSV ordBackDiscountSV;

    @Resource
    private IOrdMainSV  ordMainSV;

    private static final String MODULE = BackPayStaffPmphSVImpl.class.getName();


    @Override
    public void dealMethod(RBackConfirmReq rBackConfirmReq) {

        LogUtil.error(MODULE, "人卫子事物开始");
        ROrderBackReq rOrderBackReq  = new ROrderBackReq();
        rOrderBackReq.setOrderId(rBackConfirmReq.getOrderId());
        rOrderBackReq.setBackId(rBackConfirmReq.getBackId());
        RBackReviewResp rBackReviewResp = this.ordBackApplySV.queryBackIdInfo(rOrderBackReq);
        
        List<OrderBackSubReqDTO> orderBackSubReqDTOs = new ArrayList<OrderBackSubReqDTO>();
        for(RBackGdsResp rBackGdsResp: rBackReviewResp.getrBackGdsResps()){
            OrderBackSubReqDTO orderBackSubReqDTO = new OrderBackSubReqDTO();
            orderBackSubReqDTO.setOrderId(rBackConfirmReq.getOrderId());
            orderBackSubReqDTO.setStaffId(rBackReviewResp.getrBackApplyResp().getStaffId());
            orderBackSubReqDTO.setSubOrderId(rBackGdsResp.getOrderSubId());
            SBaseAndSubInfo sOrderAOrderSubInfo = new SBaseAndSubInfo();
            sOrderAOrderSubInfo.setOrderSubId(rBackGdsResp.getOrderSubId());
            sOrderAOrderSubInfo.setOrderId(rBackGdsResp.getOrderId());            
            orderBackSubReqDTOs.add(orderBackSubReqDTO);
        }
        
        //积分相关信息
        OrderBackMainRWReqDTO<OrderBackSubReqDTO> socreOrderBackMainReqDTO = new OrderBackMainRWReqDTO<OrderBackSubReqDTO>();
        socreOrderBackMainReqDTO.setOrderId(rBackReviewResp.getrBackApplyResp().getOrderId());//订单号
        socreOrderBackMainReqDTO.setBackId(rBackReviewResp.getrBackApplyResp().getBackId());//退款退货编号
        socreOrderBackMainReqDTO.setList(orderBackSubReqDTOs);
        RBackApplyPmphOrdReq rBackApplyPmphOrdReq = new RBackApplyPmphOrdReq();
        rBackApplyPmphOrdReq.setApplyType(rBackReviewResp.getrBackApplyResp().getApplyType());

        OrdMain ordMain =this.ordMainSV.queryOrderByOrderId(socreOrderBackMainReqDTO.getOrderId());
        SOrderDetailsMain  sOrderDetailsMain = new SOrderDetailsMain();
        sOrderDetailsMain.setId(rBackConfirmReq.getOrderId());
        sOrderDetailsMain.setBasicMoney(ordMain.getBasicMoney());
        rBackApplyPmphOrdReq.setSOrderDetailsMain(sOrderDetailsMain);
        List<RBackApplyOrdSubResp> rBackApplyOrdSubRespList = new ArrayList<>();
        for(RBackGdsResp rBackGdsResp: rBackReviewResp.getrBackGdsResps()){
            RBackApplyOrdSubResp rBackApplyOrdSubResp = new RBackApplyOrdSubResp();
            rBackApplyOrdSubResp.setOrderSubId(rBackGdsResp.getOrderSubId());
            rBackApplyOrdSubResp.setNum(rBackGdsResp.getNum());
            rBackApplyOrdSubRespList.add(rBackApplyOrdSubResp);
        }
        rBackApplyPmphOrdReq.setrBackApplyOrdSubResps(rBackApplyOrdSubRespList);
        socreOrderBackMainReqDTO.setScale(this.returnBackSV.calCulateScaleApply(rBackApplyPmphOrdReq));
        ROrderBackReq orderBackReq = new ROrderBackReq();
        orderBackReq.setBackId(rBackReviewResp.getrBackApplyResp().getBackId());
        orderBackReq.setOrderId(rBackReviewResp.getrBackApplyResp().getOrderId());
        List<RBackDiscountResp> backDiscountResps = ordBackDiscountSV.queryOrdBackDiscount(orderBackReq);
        long backScore = 0l;
        if(backDiscountResps!=null&&backDiscountResps.size()>0){
            for(RBackDiscountResp backDiscountResp:backDiscountResps){
                if(backDiscountResp.getDiscountType().equals("02")){
                    backScore=backScore+backDiscountResp.getAmount();
                }
            }
        }
        socreOrderBackMainReqDTO.setBackScore(backScore);
        socreOrderBackMainReqDTO.setModifyBackSocre(backScore);
        socreOrderBackMainReqDTO.setStaffId(rBackReviewResp.getrBackApplyResp().getStaffId());//买家
        socreOrderBackMainReqDTO.setLastFlag("1".equals(rBackReviewResp.getrBackApplyResp().getIsEntire()));//是否最后一笔      
        socreOrderBackMainReqDTO.setRefundOrBack(rBackReviewResp.getrBackApplyResp().getApplyType());
        
        OrderBackSubReqDTO orderBackSubReqDTO = new OrderBackSubReqDTO();
        orderBackSubReqDTO.setStaffId(rBackReviewResp.getrBackApplyResp().getStaffId());
        orderBackSubReqDTO.setOrderId(rBackReviewResp.getrBackApplyResp().getOrderId());
        orderBackSubReqDTO.setLastFlag(socreOrderBackMainReqDTO.isLastFlag());
        orderBackSubReqDTO.setScale(socreOrderBackMainReqDTO.getScale());
        orderBackSubReqDTO.setRefundOrBack(rBackReviewResp.getrBackApplyResp().getApplyType());
        orderBackSubReqDTO.setBackId(rBackReviewResp.getrBackApplyResp().getBackId());
        staffUnionRWSV.saveScoreAcctForOrderBackRW(socreOrderBackMainReqDTO,orderBackSubReqDTO); 
        
        LogUtil.error(MODULE, "调用客户域入参："+JSON.toJSONString(socreOrderBackMainReqDTO)+"===="+JSON.toJSONString(orderBackSubReqDTO));

    }

}

