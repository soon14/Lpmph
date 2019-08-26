package com.ai.ecp.pmph.busi.seller.order.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.util.ParamsTool;
import com.ai.ecp.busi.seller.order.vo.RBackGdsReviewReqVO;
import com.ai.ecp.busi.seller.order.vo.RDelyOrderReqVO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.order.dubbo.dto.RBackApplyInfoResp;
import com.ai.ecp.order.dubbo.dto.RBackReviewReq;
import com.ai.ecp.order.dubbo.dto.RBackReviewResp;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrderBackReq;
import com.ai.ecp.order.dubbo.dto.ROrderBackResp;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdBackMoneyRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdManageRSV;
import com.ai.ecp.order.dubbo.interfaces.pay.IPaymentRSV;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.pmph.busi.seller.order.vo.ROrdBackReqVO;
import com.ai.ecp.pmph.busi.seller.order.vo.ROrdReturnRefundReqVO;
import com.ai.ecp.pmph.dubbo.dto.CompensateBackResp;
import com.ai.ecp.pmph.dubbo.dto.OrdCompensateReq;
import com.ai.ecp.pmph.dubbo.dto.OrdMainCompensateResponse;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdPmphMainRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IReturnBackRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value="/seller/refundReview")
public class OrdRefundReviewController extends EcpBaseController {
    
    @Resource
    private IGdsCategoryRSV gdsCategoryRSV; 

    @Resource
    private IOrdBackMoneyRSV ordBackMoneyRSV;

    @Resource
    private IStaffUnionRSV staffUnionRSV;
    
    @Resource
    private IPaymentRSV paymentRSV;
    
    @Resource(name = "ordManageRSV")
    private IOrdManageRSV ordManageRSV;
    
    @Resource
    private IOrdPmphMainRSV ordPmphMainRSV;
    
    @Resource
    private IReturnBackRSV returnBackRSV;
    
    private static String MODULE = OrdRefundReviewController.class.getName();
    /** 
     * manageRefund:退款管理. <br/> 
     * @param model
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="/refund1")
    public String manageRefund1(Model model,@RequestParam(value="shopId", required=false)Long shopId){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
        model.addAttribute("shopId", shopId);
        return "/seller/order/ordback/review/refund1";
    }
    @RequestMapping(value="/refund2")
    public String manageRefund2(Model model,@RequestParam(value="shopId", required=false)Long shopId){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
        model.addAttribute("shopId", shopId);
        return "/seller/order/ordback/review/refund2";
    }
    @RequestMapping(value="/refund3")
    public String manageRefund3(Model model,@RequestParam(value="shopId", required=false)Long shopId){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
        model.addAttribute("shopId", shopId);
        return "/seller/order/ordback/review/refund3";
    }
    
    @RequestMapping("/queryOrder")
    public String queryOrderBack(Model model, ROrdBackReqVO rOrdBackReqVO) throws Exception {
        LogUtil.info(MODULE, JSON.toJSONString(rOrdBackReqVO).toString());
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = rOrdBackReqVO.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(rOrdBackReqVO, rOrderBackReq, "", false);
        rOrderBackReq.setEndDate(new Timestamp(DateUtils.addDays(rOrderBackReq.getEndDate(), 1).getTime()));
        PageResponseDTO<ROrderBackResp> resp = this.returnBackRSV.queryBackMoneyByShop(rOrderBackReq);

        LogUtil.info(MODULE,JSON.toJSONString(resp));

        model.addAttribute("resp", resp);
        return "/seller/order/ordbackgdslist/order-refund-todo";
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
        ROrderIdRequest orderIdRequest = new ROrderIdRequest();
        
        RBackReviewResp resp = this.returnBackRSV.queryBackMoneyReview(rOrderBackReq);  
        ROrdReturnRefundResp rOrdReturnRefundResp = returnBackRSV.calCulateBackInfo(rOrderBackReq);
        orderIdRequest.setOrderId(vo.getOrderId());
        ROrdMainResponse ordMainResp = ordPmphMainRSV.queryOrderMain(orderIdRequest);
        LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(resp).toString());
        model.addAttribute("rOrdReturnRefundResp", rOrdReturnRefundResp);
        model.addAttribute("ordMainResp", ordMainResp);
        model.addAttribute("rBackApplyResp", resp.getrBackApplyResp());
        model.addAttribute("orderDetailsMain", resp.getOrderDetailsMain());
        model.addAttribute("rBackPicResps", resp.getrBackPicResps());
        model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
        model.addAttribute("rBackApplyInfoResp",resp.getrBackApplyInfoResp());
        model.addAttribute("shareInfo", JSON.toJSONString(resp.getrBackApplyInfoResp()));
        model.addAttribute("rBackTrackResps", resp.getrBackTrackResps());
        return "/seller/order/ordback/backreview";
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
        ROrderIdRequest orderIdRequest = new ROrderIdRequest();
        
        RBackReviewResp resp = this.returnBackRSV.queryBackMoneyReview(rOrderBackReq);  
        ROrdReturnRefundResp rOrdReturnRefundResp = returnBackRSV.calCulateBackInfo(rOrderBackReq);
        orderIdRequest.setOrderId(vo.getOrderId());
        ROrdMainResponse ordMainResp = ordPmphMainRSV.queryOrderMain(orderIdRequest);
        LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(resp).toString());
        model.addAttribute("rOrdReturnRefundResp", rOrdReturnRefundResp);
        model.addAttribute("ordMainResp", ordMainResp);
        model.addAttribute("rBackApplyResp", resp.getrBackApplyResp());
        model.addAttribute("orderDetailsMain", resp.getOrderDetailsMain());
        model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
        model.addAttribute("rBackPicResps", resp.getrBackPicResps());
        model.addAttribute("rBackApplyInfoResp",resp.getrBackApplyInfoResp());
        model.addAttribute("shareInfo", JSON.toJSONString(resp.getrBackApplyInfoResp()));
        model.addAttribute("rBackTrackResps", resp.getrBackTrackResps());
        return "/seller/order/ordback/refunddetail";
    }
    
    /**
     * reviewCompensateBack:补偿性退款审核信息查询. <br/>
     * @param model
     * @param vo
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value="/reviewCompensateBack")
    public String reviewCompensateBack(Model model, @Valid RBackGdsReviewReqVO vo) {
        LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = vo.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(vo, rOrderBackReq, "", false);
        CompensateBackResp resp = returnBackRSV.queryCompensateBackMoney(rOrderBackReq);
        LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(resp).toString());
        model.addAttribute("compensateBackResp", resp);
        return "/seller/order/ordback/compensatereview";
    }
    
    /**
     * detailCompensateBack:补偿性退款详情信息查询. <br/>
     * @param model
     * @param vo
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value="/detailCompensateBack")
    public String detailCompensateBack(Model model, @Valid RBackGdsReviewReqVO vo) {
        LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq = vo.toBaseInfo(ROrderBackReq.class);
        ObjectCopyUtil.copyObjValue(vo, rOrderBackReq, "", false);
        CompensateBackResp resp = returnBackRSV.queryCompensateBackMoney(rOrderBackReq);
        LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(resp).toString());
        model.addAttribute("compensateBackResp", resp);
        return "/seller/order/ordback/compensatedetail";
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
            if(vo.getStatus().equals("0")) {
                rBackReviewReq.setStatus(BackConstants.Status.REFUSE);
            } else if(vo.getStatus().equals("1")){   //一级审核 通过
                rBackReviewReq.setStatus("06");
            } else if(vo.getStatus().equals("2")){ //二级级审核 通过
                rBackReviewReq.setStatus("07");
            } else if(vo.getStatus().equals("3")) { //三级审核 通过 与原有程序逻辑保持一致
                rBackReviewReq.setStatus(BackConstants.Status.REVIEW_PASS);
            }
            rBackReviewReq.setApplyType(BackConstants.ApplyType.REFUND);
            if(StringUtil.isNotBlank(vo.getShareInfo())){
                LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(vo.getShareInfo()).toString());
                RBackApplyInfoResp rBackApplyInfoResp = JSON.parseObject(vo.getShareInfo(), RBackApplyInfoResp.class);
                rBackReviewReq.setrBackApplyInfoResp(rBackApplyInfoResp);
                LogUtil.info(MODULE, "审核信息出参"+JSON.toJSONString(rBackApplyInfoResp).toString());
            }

            LogUtil.info(MODULE, "---------------"+JSON.toJSONString(rBackReviewReq));
            this.returnBackRSV.saveBackMoneyReview(rBackReviewReq);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
    
    /**
     * 
     * compensate:(进入补偿性退款页面)，带入店铺编号. 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/compensate/{shopId}")
    public String compensate(Model model,@PathVariable Long shopId){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
        model.addAttribute("shopId", shopId);
        return "/seller/order/ordback/review/compensate"; 
    }
    
    /**
     * 订单查询 根据店铺ID查询
     */
    @RequestMapping("/gridordlist")
    public String gridList(Model model, RDelyOrderReqVO vo) throws Exception {
        // 后场服务所需要的DTO；
        RQueryOrderRequest r = new RQueryOrderRequest();
        r = vo.toBaseInfo(RQueryOrderRequest.class);
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
        PageResponseDTO<OrdMainCompensateResponse>  resp = ordPmphMainRSV.queryOrderByCompensatePage(r);
        model.addAttribute("resp", resp);
        return "/seller/order/ordbackgdslist/order-refund-compensate";
    }
    

    // 避免空指针异常
    @SuppressWarnings("rawtypes")
    private EcpBasePageRespVO<Map> bulidPageResp(PageResponseDTO<OrdMainCompensateResponse> t)
            throws Exception {
        EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
        if (t != null) {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
        }
        return respVO;
    }
    
    @RequestMapping(value="/savecompenstate")
    @ResponseBody
    public EcpBaseResponseVO saveCompenstateBackMoney(ROrdReturnRefundReqVO vo){
        LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo));
        EcpBaseResponseVO resp = new EcpBaseResponseVO();
        try{
            OrdCompensateReq req = new OrdCompensateReq();
            ObjectCopyUtil.copyObjValue(vo, req, "", false);
            returnBackRSV.saveCompensateBackMoney(req);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping(value="/checkBackMoney")
    @ResponseBody
    public EcpBaseResponseVO checkBackMoney(ROrdReturnRefundReqVO vo){
        LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo));
        EcpBaseResponseVO resp = new EcpBaseResponseVO();
        try{
        	ROrdReturnRefundReq req = new ROrdReturnRefundReq();
            ObjectCopyUtil.copyObjValue(vo, req, "", false);
            boolean result = ordPmphMainRSV.checkBackMoney(req);
            if(result){
            	resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            }else{
            	 resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            }
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
}

