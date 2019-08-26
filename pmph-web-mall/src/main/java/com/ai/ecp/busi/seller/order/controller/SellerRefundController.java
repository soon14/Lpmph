package com.ai.ecp.busi.seller.order.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.vo.RBackConfirmReqVO;
import com.ai.ecp.busi.seller.order.vo.RBackGdsReviewReqVO;
import com.ai.ecp.order.dubbo.dto.RBackApplyInfoResp;
import com.ai.ecp.order.dubbo.dto.RBackConfirmReq;
import com.ai.ecp.order.dubbo.dto.RBackPayInfoResp;
import com.ai.ecp.order.dubbo.dto.RBackReviewReq;
import com.ai.ecp.order.dubbo.dto.RBackReviewResp;
import com.ai.ecp.order.dubbo.dto.ROrderBackReq;
import com.ai.ecp.order.dubbo.dto.ROrderBackResp;
import com.ai.ecp.order.dubbo.dto.pay.RPayRefundRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayRefundResponse;
import com.ai.ecp.order.dubbo.interfaces.IOrdBackMoneyRSV;
import com.ai.ecp.order.dubbo.interfaces.pay.IPaymentRSV;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.busi.seller.order.vo.ROrdBackReqVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value = "/seller/order/refund")
public class SellerRefundController extends EcpBaseController{
    
    private static String MODULE = SellerRefundController.class.getName();
    
    @Resource
    private IOrdBackMoneyRSV ordBackMoneyRSV;
    
    @Resource
    private IPaymentRSV paymentRSV;
    
    private static final String SELLER_ORDER_DELIVERY_VM_PATH = "/seller/order";

    @RequestMapping(value="index")
    public String index(Model model, @RequestParam(value="shopId", required=false)Long shopId) throws Exception
    {
    	model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
    	model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
        model.addAttribute("shopId", shopId);
        return SELLER_ORDER_DELIVERY_VM_PATH + "/seller-refund";
    }
    /** 
     * queryOrderTodo:待处理订单查询. <br/> 
     * @param model
     * @param rOrdBackReqVO
     * @return
     * @throws Exception 
     * @since JDK 1.6 
     */ 
    @RequestMapping("/querytodo")
    public String queryOrderTodo(Model model, ROrdBackReqVO rOrdBackReqVO) throws Exception {
        LogUtil.info(MODULE, JSON.toJSONString(rOrdBackReqVO).toString());
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = rOrdBackReqVO.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(rOrdBackReqVO, rOrderBackReq, "", false);
        rOrderBackReq.setEndDate(new Timestamp(DateUtils.addDays(rOrderBackReq.getEndDate(), 1).getTime()));
        rOrderBackReq.setTabFlag("00"); //待处理
        PageResponseDTO<ROrderBackResp> resp = this.ordBackMoneyRSV.queryBackMoneyByShop(rOrderBackReq);;
        model.addAttribute("resp", resp);
        return SELLER_ORDER_DELIVERY_VM_PATH + "/refundlist/seller-refund-todo";
    }
    /** 
     * queryOrderHandled:已处理订单查询. <br/> 
     * @param model
     * @param rOrdBackReqVO
     * @return
     * @throws Exception 
     * @since JDK 1.6 
     */ 
    @RequestMapping("/queryhandled")
    public String queryOrderHandled(Model model, ROrdBackReqVO rOrdBackReqVO) throws Exception {
        LogUtil.info(MODULE, JSON.toJSONString(rOrdBackReqVO).toString());
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = rOrdBackReqVO.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(rOrdBackReqVO, rOrderBackReq, "", false);
        rOrderBackReq.setEndDate(new Timestamp(DateUtils.addDays(rOrderBackReq.getEndDate(), 1).getTime()));
        rOrderBackReq.setTabFlag("01"); //已处理
        PageResponseDTO<ROrderBackResp> resp = this.ordBackMoneyRSV.queryBackMoneyByShop(rOrderBackReq);;
        model.addAttribute("resp", resp);
        return SELLER_ORDER_DELIVERY_VM_PATH + "/refundlist/seller-refund-handled";
    }
    /** 
     * backGdsReview:审核. <br/> 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/review")
    public String backGdsReview(Model model, @Valid RBackGdsReviewReqVO vo) {
        LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = vo.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(vo, rOrderBackReq, "", false);
        RBackReviewResp resp = this.ordBackMoneyRSV.queryBackMoneyReview(rOrderBackReq);
        LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(resp).toString());
        model.addAttribute("rBackApplyResp", resp.getrBackApplyResp());
        model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
        model.addAttribute("rBackApplyInfoResp",resp.getrBackApplyInfoResp());
        model.addAttribute("shareInfo", JSON.toJSONString(resp.getrBackApplyInfoResp()));
        return SELLER_ORDER_DELIVERY_VM_PATH + "/seller-refund-review";
    }
    /** 
     * confirmReview:退款提交审核. <br/> 
     * @param vo
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/confirmReview")
    @ResponseBody
    public EcpBaseResponseVO confirmReview(@Valid RBackGdsReviewReqVO vo) {
        LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo));
        EcpBaseResponseVO resp = new EcpBaseResponseVO();  
        try { 
            RBackReviewReq rBackReviewReq = new RBackReviewReq();
            rBackReviewReq = vo.toBaseInfo(RBackReviewReq.class);
            ObjectCopyUtil.copyObjValue(vo, rBackReviewReq, "", false);
            if(vo.getStatus().equals("1")) {
                rBackReviewReq.setStatus(BackConstants.Status.REVIEW_PASS);
            } else if(vo.getStatus().equals("0")){
                rBackReviewReq.setStatus(BackConstants.Status.REFUSE);
            } 
            rBackReviewReq.setApplyType(BackConstants.ApplyType.REFUND);
            if(StringUtil.isNotBlank(vo.getShareInfo())){
                LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(vo.getShareInfo()).toString());
                RBackApplyInfoResp rBackApplyInfoResp = JSON.parseObject(vo.getShareInfo(), RBackApplyInfoResp.class);
                rBackReviewReq.setrBackApplyInfoResp(rBackApplyInfoResp);
                LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(rBackApplyInfoResp).toString());
            }
            
            LogUtil.info(MODULE, "---------------"+JSON.toJSONString(rBackReviewReq));
            this.ordBackMoneyRSV.saveBackMoneyReview(rBackReviewReq);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
    /** 
     * queryRefund:确认退款信息查询. <br/> 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/queryRefund")
    public String queryRefund(Model model,@Valid RBackConfirmReqVO vo) {
        model.addAttribute("vo", vo);
        model.addAttribute("applyType", "0");
        return SELLER_ORDER_DELIVERY_VM_PATH + "/ordback/confirmrefund/confirmrefund";
    }
    /** 
     * confirmRefund:线下确认退款. <br/> 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/confirmRefund")
    @ResponseBody
    public EcpBaseResponseVO confirmRefund(Model model, @Valid RBackConfirmReqVO vo) {
        LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
        EcpBaseResponseVO resp = new EcpBaseResponseVO();  
        try { 
            RBackConfirmReq rBackConfirmReq = new RBackConfirmReq();
            rBackConfirmReq = vo.toBaseInfo(RBackConfirmReq.class);
            ObjectCopyUtil.copyObjValue(vo, rBackConfirmReq, "", false);
            this.ordBackMoneyRSV.saveBackGdsPayed(rBackConfirmReq);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
    /** 
     * onlineRefund:线上退款. <br/> 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/onlineRefund")
    @ResponseBody
    public RPayRefundResponse onlineRefund(Model model, @Valid RBackConfirmReqVO vo) {
        LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
        RPayRefundResponse rPayRefundResponse = null;
        try { 
            RBackConfirmReq rBackConfirmReq = new RBackConfirmReq();
            rBackConfirmReq = vo.toBaseInfo(RBackConfirmReq.class);
            ObjectCopyUtil.copyObjValue(vo, rBackConfirmReq, "", false);
            ROrderBackReq rOrderBackReq = new ROrderBackReq();
            rOrderBackReq.setBackId(rBackConfirmReq.getBackId());
            rOrderBackReq.setOrderId(rBackConfirmReq.getOrderId());
            RBackPayInfoResp  rBackPayInfoResp  = this.ordBackMoneyRSV.queryBackPayInfo(rOrderBackReq);
            RPayRefundRequest rPayRefundRequest = new RPayRefundRequest();
            rPayRefundRequest.setBackId(rBackPayInfoResp.getrBackApplyResp().getBackId());
            rPayRefundRequest.setOrderId(rBackPayInfoResp.getrBackApplyResp().getOrderId());
            rPayRefundRequest.setPayWay(rBackPayInfoResp.getsOrderDetailsMain().getPayWay());
            rPayRefundRequest.setRefundAmount(rBackPayInfoResp.getrBackApplyResp().getBackMoney());
            Map<String,String> val = new HashMap<String, String>();
            rPayRefundResponse  =  this.paymentRSV.refund(rPayRefundRequest, val);
            if(rPayRefundResponse == null ){
                rPayRefundResponse = new RPayRefundResponse();
                rPayRefundResponse.setRefundMethod(OrdConstants.PayStatus.PAY_REFUND_METHOD_01);
                rPayRefundResponse.setFlag(false);
                rPayRefundResponse.setMessage("支付报错,请联系管理员");
            }
            
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            rPayRefundResponse = new RPayRefundResponse();
            rPayRefundResponse.setRefundMethod(OrdConstants.PayStatus.PAY_REFUND_METHOD_01);
            rPayRefundResponse.setFlag(false);
            rPayRefundResponse.setMessage("支付报错,请联系管理员");
        }
        return rPayRefundResponse;
    }
    /**
     * 
     * printList:(打印清单页面). <br/> 
     * 
     * @param vo
     * @param model
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value="/printList")
    public String printList(Model model, ROrdBackReqVO rOrdBackReqVO){
        LogUtil.info(MODULE, JSON.toJSONString(rOrdBackReqVO).toString());
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = rOrdBackReqVO.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(rOrdBackReqVO, rOrderBackReq, "", false);
        rOrderBackReq.setEndDate(new Timestamp(DateUtils.addDays(rOrderBackReq.getEndDate(), 1).getTime()));
        PageResponseDTO<ROrderBackResp> orderBacks = this.ordBackMoneyRSV.queryBackMoneyByShop(rOrderBackReq);
        List<ROrderBackResp> orderBackList = orderBacks.getResult(); 
        model.addAttribute("orderBackList", orderBackList); 
        return "/seller/order/ordback/print/refund-print"; 
    }

}

