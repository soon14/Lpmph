package com.ai.ecp.busi.order.ordback.controller;

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
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.ordback.vo.RBackConfirmReqVO;
import com.ai.ecp.busi.order.ordback.vo.RBackGdsReviewReqVO;
import com.ai.ecp.busi.order.ordback.vo.ROrdBackReqVO;
import com.ai.ecp.busi.order.util.OrdConstant;
import com.ai.ecp.busi.order.util.ParamsTool;
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
import com.ai.ecp.pmph.dubbo.interfaces.IReturnBackRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;
@Controller
@RequestMapping(value="/ordrefund")
public class OrdRefundController extends EcpBaseController {

    @Resource
    private IOrdBackMoneyRSV ordBackMoneyRSV;
    
    @Resource
    private IPaymentRSV paymentRSV;
    
    @Resource
    private IReturnBackRSV returnBackRSV;
    
    private static String MODULE = OrdRefundController.class.getName();
    /** 
     * manageRefund:退款管理. <br/> 
     * @param model
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/refund")
    public String manageRefund(Model model){
        model.addAllAttributes(ParamsTool.params());
        return "/order/ordback/refund";
    }
    
    @RequestMapping("/queryOrder")
    @ResponseBody
    public Model queryOrderBack(Model model, ROrdBackReqVO rOrdBackReqVO) throws Exception {
        LogUtil.info(MODULE, JSON.toJSONString(rOrdBackReqVO).toString());
//      String val = "{\"count\":9,\"endRowIndex\":10,\"pageCount\":1,\"pageNo\":1,\"pageSize\":10,\"result\":[{\"chnlAddress\":\"北京市辖区东城区werwrewer\",\"contactName\":\"44444\",\"contactPhone\":\"13423423423\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":1,\"orderDate\":1448503519000,\"orderId\":\"RW15112600002072\",\"orderScore\":0,\"orderTime\":1448503519000,\"orderType\":\"01\",\"payTime\":1448508033000,\"payType\":\"1\",\"realMoney\":32100,\"remainAmounts\":1,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":180,\"staffName\":\"wangxq\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区werwrewer\",\"contactName\":\"44444\",\"contactPhone\":\"13423423423\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":2,\"orderDate\":1448509103000,\"orderId\":\"RW15112600002088\",\"orderScore\":0,\"orderTime\":1448509103000,\"orderType\":\"01\",\"payTime\":1448522166000,\"payType\":\"1\",\"realMoney\":242222,\"remainAmounts\":2,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":180,\"staffName\":\"wangxq\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":5,\"orderDate\":1448522575000,\"orderId\":\"RW15112600002093\",\"orderScore\":0,\"orderTime\":1448522575000,\"orderType\":\"01\",\"payTime\":1448522825000,\"payType\":\"3\",\"realMoney\":10000,\"remainAmounts\":5,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":1501,\"dispatchType\":\"1\",\"invoiceType\":\"2\",\"orderAmounts\":1501,\"orderDate\":1449025234000,\"orderId\":\"RW15120200002117\",\"orderScore\":0,\"orderTime\":1449025234000,\"orderType\":\"01\",\"payTime\":1449025467000,\"payType\":\"3\",\"realMoney\":3666100,\"remainAmounts\":0,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"04\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":0,\"dispatchType\":\"1\",\"invoiceType\":\"2\",\"orderAmounts\":99,\"orderDate\":1449026285000,\"orderId\":\"RW15120200002123\",\"orderScore\":0,\"orderTime\":1449026285000,\"orderType\":\"01\",\"payTime\":1449026449000,\"payType\":\"3\",\"realMoney\":197500,\"remainAmounts\":99,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":200,\"dispatchType\":\"1\",\"invoiceType\":\"2\",\"orderAmounts\":200,\"orderDate\":1449026332000,\"orderId\":\"RW15120200002124\",\"orderScore\":0,\"orderTime\":1449026332000,\"orderType\":\"01\",\"payTime\":1449026453000,\"payType\":\"3\",\"realMoney\":559500,\"remainAmounts\":0,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"04\"},{\"chnlAddress\":\"福建福州市仓山区金工路\",\"contactName\":\"李四\",\"contactNumber\":\"1234567\",\"contactPhone\":\"18513245681\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"0\",\"orderAmounts\":1,\"orderDate\":1449109356000,\"orderId\":\"RW15120300002399\",\"orderScore\":0,\"orderTime\":1449109356000,\"orderType\":\"01\",\"payTime\":1449109368000,\"payType\":\"1\",\"realMoney\":3200,\"remainAmounts\":1,\"sOrderDetailsComm\":{\"detailFlag\":\"0\",\"invoiceContent\":\"图书\",\"invoiceTitle\":\"李四\",\"invoiceType\":\"0\"},\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":241,\"staffName\":\"panjs3\",\"status\":\"02\"},{\"chnlAddress\":\"福建福州市仓山区金工路\",\"contactName\":\"李四\",\"contactNumber\":\"1234567\",\"contactPhone\":\"18513245681\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":1,\"orderDate\":1450317615000,\"orderId\":\"RW15121700005341\",\"orderScore\":0,\"orderTime\":1450317615000,\"orderType\":\"01\",\"payTime\":1450317462000,\"payType\":\"1\",\"realMoney\":3200,\"remainAmounts\":1,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":241,\"staffName\":\"panjs3\",\"status\":\"02\"},{\"chnlAddress\":\"福建福州市仓山区金工路\",\"contactName\":\"李四\",\"contactNumber\":\"1234567\",\"contactPhone\":\"18513245681\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"0\",\"orderAmounts\":1,\"orderDate\":1450317716000,\"orderId\":\"RW15121700005342\",\"orderScore\":0,\"orderTime\":1450317716000,\"orderType\":\"01\",\"payTime\":1450317463000,\"payType\":\"1\",\"realMoney\":800,\"remainAmounts\":1,\"sOrderDetailsComm\":{\"detailFlag\":\"1\",\"invoiceContent\":\"图书\",\"invoiceTitle\":\"李四\",\"invoiceType\":\"0\"},\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":241,\"staffName\":\"panjs3\",\"status\":\"02\"}],\"startRowIndex\":0}";
//      PageResponseDTO<RShopOrderResponse> resp = JSON.parseObject(val,new TypeReference<PageResponseDTO<RShopOrderResponse>>(){});
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = rOrdBackReqVO.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(rOrdBackReqVO, rOrderBackReq, "", false);
        rOrderBackReq.setEndDate(new Timestamp(DateUtils.addDays(rOrderBackReq.getEndDate(), 1).getTime()));
        PageResponseDTO<ROrderBackResp> resp = this.returnBackRSV.queryBackMoneyByShop(rOrderBackReq);;
        
        EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
        if (resp != null) {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(resp);
        }
        return super.addPageToModel(model, respVO);
    }
    
    /** 
     * backGdsReview:退款审核信息查询. <br/> 
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
        model.addAttribute("orderDetailsMain", resp.getOrderDetailsMain());
        model.addAttribute("rBackApplyInfoResp",resp.getrBackApplyInfoResp());
        model.addAttribute("shareInfo", JSON.toJSONString(resp.getrBackApplyInfoResp()));
        return "/order/ordback/backreview";
    }
    
    /** 
     * backGdsReview:退款详情信息. <br/> 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/detail")
    public String backDetail(Model model, @Valid RBackGdsReviewReqVO vo) {
        LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = vo.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(vo, rOrderBackReq, "", false);
        RBackReviewResp resp = this.ordBackMoneyRSV.queryBackMoneyReview(rOrderBackReq);
        LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(resp).toString());
        model.addAttribute("rBackApplyResp", resp.getrBackApplyResp());
        model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
        model.addAttribute("orderDetailsMain", resp.getOrderDetailsMain());
        model.addAttribute("rBackApplyInfoResp",resp.getrBackApplyInfoResp());
        model.addAttribute("shareInfo", JSON.toJSONString(resp.getrBackApplyInfoResp()));
        return "/order/ordback/refunddetail";
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
        return "/order/ordback/confirmrefund/confirmrefund";
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
            returnBackRSV.saveBackGdsPayed(rBackConfirmReq);
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
                rPayRefundResponse.setMessage("退款报错,请联系管理员");
            }else{
                //线上退款
                if(!"01".equals(rPayRefundResponse.getRefundMethod())){
                    //把退款信息缓存起来 时间为半小时 
                    String key = ParamsTool.MD5(rBackConfirmReq.getStaff().getId() + (new Date().toString())).toLowerCase();
                    rPayRefundResponse.setSourceKey(key);
                    CacheUtil.addItem(OrdConstant.ORDER_SESSION_KEY_PAY_REFUND + key, rPayRefundResponse, OrdConstant.ORDER_SESSION_TIME);
                }
            }
            
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            rPayRefundResponse = new RPayRefundResponse();
            rPayRefundResponse.setRefundMethod(OrdConstants.PayStatus.PAY_REFUND_METHOD_01);
            rPayRefundResponse.setFlag(false);
            rPayRefundResponse.setMessage("退款报错,请联系管理员");
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
        return "/order/ordback/print/refund-print"; 
    }
    
    /**
     * 
     * refundReview:(线上退款页面). <br/> 
     * 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/refundReview")
    public String refundReview(Model model,@RequestParam(value = "sourceKey") String sourceKey) { 
        //获取缓存中退款信息
        RPayRefundResponse rPayRefundResponse = (RPayRefundResponse) CacheUtil.getItem(OrdConstant.ORDER_SESSION_KEY_PAY_REFUND+sourceKey);
        model.addAttribute("payData",rPayRefundResponse.getPayRequestData());
        Map<String, String> formData = rPayRefundResponse.getPayRequestData().getFormData();
        model.addAttribute("formData",JSON.toJSONString(formData).toString());
        //删除缓存
        CacheUtil.delItem(OrdConstant.ORDER_SESSION_KEY_PAY_REFUND+sourceKey);
        return "/order/ordback/refundreview/refund-review";
    }
}

