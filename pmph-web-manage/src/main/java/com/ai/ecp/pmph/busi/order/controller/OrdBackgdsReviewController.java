package com.ai.ecp.pmph.busi.order.controller;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.ordback.vo.RBackGdsReviewReqVO;
import com.ai.ecp.busi.order.ordback.vo.ROrdBackReqVO;
import com.ai.ecp.order.dubbo.dto.*;
import com.ai.ecp.order.dubbo.interfaces.IOrdBackGdsRSV;
import com.ai.ecp.order.dubbo.interfaces.pay.IPaymentRSV;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.pmph.busi.order.vo.ROrdReturnRefundReqVO;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdPmphMainRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IReturnBackRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value="/backReview")
public class OrdBackgdsReviewController extends EcpBaseController {
    
    @Resource
    private IOrdBackGdsRSV ordBackGdsRSV;
    
    @Resource
    private IPaymentRSV paymentRSV;
        
    @Resource
    private IReturnBackRSV returnBackRSV;
    
    @Resource
    private IOrdPmphMainRSV ordPmphMainRSV;
    
	private static String MODULE = OrdBackgdsReviewController.class.getName();
	
	/** 
	 * manageBackGds:退货管理. <br/> 
	 * @param model
	 * @return 
	 * @since JDK 1.6 
	 */ 
	@RequestMapping(value="/backgds1")
	public String manageBackGds1(Model model){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
		return "/order/ordback/review/backgds1";
	}
	@RequestMapping(value="/backgds2")
	public String manageBackGds2(Model model){
		model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
		model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
		return "/order/ordback/review/backgds2";
	}
	@RequestMapping(value="/backgds3")
	public String manageBackGds3(Model model){
		model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
		model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
		return "/order/ordback/review/backgds3";
	}
	
	/** 
	 * queryOrderBack:退货管理查询页面. <br/> 
	 * @param model
	 * @param rOrdBackReqVO
	 * @return
	 * @throws Exception 
	 * @since JDK 1.6 
	 */ 
	@RequestMapping("/queryOrder")
    @ResponseBody
    public Model queryOrdergBack(Model model, ROrdBackReqVO rOrdBackReqVO) throws Exception {
	    LogUtil.info(MODULE, JSON.toJSONString(rOrdBackReqVO).toString());
//	    String val = "{\"count\":9,\"endRowIndex\":10,\"pageCount\":1,\"pageNo\":1,\"pageSize\":10,\"result\":[{\"chnlAddress\":\"北京市辖区东城区werwrewer\",\"contactName\":\"44444\",\"contactPhone\":\"13423423423\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":1,\"orderDate\":1448503519000,\"orderId\":\"RW15112600002072\",\"orderScore\":0,\"orderTime\":1448503519000,\"orderType\":\"01\",\"payTime\":1448508033000,\"payType\":\"1\",\"realMoney\":32100,\"remainAmounts\":1,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":180,\"staffName\":\"wangxq\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区werwrewer\",\"contactName\":\"44444\",\"contactPhone\":\"13423423423\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":2,\"orderDate\":1448509103000,\"orderId\":\"RW15112600002088\",\"orderScore\":0,\"orderTime\":1448509103000,\"orderType\":\"01\",\"payTime\":1448522166000,\"payType\":\"1\",\"realMoney\":242222,\"remainAmounts\":2,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":180,\"staffName\":\"wangxq\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":5,\"orderDate\":1448522575000,\"orderId\":\"RW15112600002093\",\"orderScore\":0,\"orderTime\":1448522575000,\"orderType\":\"01\",\"payTime\":1448522825000,\"payType\":\"3\",\"realMoney\":10000,\"remainAmounts\":5,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":1501,\"dispatchType\":\"1\",\"invoiceType\":\"2\",\"orderAmounts\":1501,\"orderDate\":1449025234000,\"orderId\":\"RW15120200002117\",\"orderScore\":0,\"orderTime\":1449025234000,\"orderType\":\"01\",\"payTime\":1449025467000,\"payType\":\"3\",\"realMoney\":3666100,\"remainAmounts\":0,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"04\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":0,\"dispatchType\":\"1\",\"invoiceType\":\"2\",\"orderAmounts\":99,\"orderDate\":1449026285000,\"orderId\":\"RW15120200002123\",\"orderScore\":0,\"orderTime\":1449026285000,\"orderType\":\"01\",\"payTime\":1449026449000,\"payType\":\"3\",\"realMoney\":197500,\"remainAmounts\":99,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"02\"},{\"chnlAddress\":\"北京市辖区东城区000000000000000000000000\",\"contactName\":\"保兰兄弟\",\"contactPhone\":\"18060479649\",\"deliverAmounts\":200,\"dispatchType\":\"1\",\"invoiceType\":\"2\",\"orderAmounts\":200,\"orderDate\":1449026332000,\"orderId\":\"RW15120200002124\",\"orderScore\":0,\"orderTime\":1449026332000,\"orderType\":\"01\",\"payTime\":1449026453000,\"payType\":\"3\",\"realMoney\":559500,\"remainAmounts\":0,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":182,\"staffName\":\"chengbl\",\"status\":\"04\"},{\"chnlAddress\":\"福建福州市仓山区金工路\",\"contactName\":\"李四\",\"contactNumber\":\"1234567\",\"contactPhone\":\"18513245681\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"0\",\"orderAmounts\":1,\"orderDate\":1449109356000,\"orderId\":\"RW15120300002399\",\"orderScore\":0,\"orderTime\":1449109356000,\"orderType\":\"01\",\"payTime\":1449109368000,\"payType\":\"1\",\"realMoney\":3200,\"remainAmounts\":1,\"sOrderDetailsComm\":{\"detailFlag\":\"0\",\"invoiceContent\":\"图书\",\"invoiceTitle\":\"李四\",\"invoiceType\":\"0\"},\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":241,\"staffName\":\"panjs3\",\"status\":\"02\"},{\"chnlAddress\":\"福建福州市仓山区金工路\",\"contactName\":\"李四\",\"contactNumber\":\"1234567\",\"contactPhone\":\"18513245681\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"2\",\"orderAmounts\":1,\"orderDate\":1450317615000,\"orderId\":\"RW15121700005341\",\"orderScore\":0,\"orderTime\":1450317615000,\"orderType\":\"01\",\"payTime\":1450317462000,\"payType\":\"1\",\"realMoney\":3200,\"remainAmounts\":1,\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":241,\"staffName\":\"panjs3\",\"status\":\"02\"},{\"chnlAddress\":\"福建福州市仓山区金工路\",\"contactName\":\"李四\",\"contactNumber\":\"1234567\",\"contactPhone\":\"18513245681\",\"deliverAmounts\":0,\"dispatchType\":\"2\",\"invoiceType\":\"0\",\"orderAmounts\":1,\"orderDate\":1450317716000,\"orderId\":\"RW15121700005342\",\"orderScore\":0,\"orderTime\":1450317716000,\"orderType\":\"01\",\"payTime\":1450317463000,\"payType\":\"1\",\"realMoney\":800,\"remainAmounts\":1,\"sOrderDetailsComm\":{\"detailFlag\":\"1\",\"invoiceContent\":\"图书\",\"invoiceTitle\":\"李四\",\"invoiceType\":\"0\"},\"shopName\":\"测试店铺0011\",\"siteId\":1,\"staffId\":241,\"staffName\":\"panjs3\",\"status\":\"02\"}],\"startRowIndex\":0}";
//	    PageResponseDTO<RShopOrderResponse> resp = JSON.parseObject(val,new TypeReference<PageResponseDTO<RShopOrderResponse>>(){});
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
	    ROrderBackReq rOrderBackReq = new ROrderBackReq();
	    rOrderBackReq = rOrdBackReqVO.toBaseInfo(ROrderBackReq.class);
	    ObjectCopyUtil.copyObjValue(rOrdBackReqVO, rOrderBackReq, "", false);
	    rOrderBackReq.setEndDate(new Timestamp(DateUtils.addDays(rOrderBackReq.getEndDate(), 1).getTime()));
	    PageResponseDTO<ROrderBackResp> resp = this.ordBackGdsRSV.queryBackGdsByShop(rOrderBackReq);;

		LogUtil.info(MODULE,JSON.toJSONString(resp));

	    EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
        if (resp != null) {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(resp);
        }
        return super.addPageToModel(model, respVO);
    }
	/**
	 * backGdsReview:退货审核信息查询. <br/>
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
		RBackReviewResp resp = this.returnBackRSV.queryBackGdsReview(rOrderBackReq);
		ROrdReturnRefundResp rOrdReturnRefundResp = returnBackRSV.calCulateBackInfo(rOrderBackReq);   
		orderIdRequest.setOrderId(vo.getOrderId());
		ROrdMainResponse ordMainResp = ordPmphMainRSV.queryOrderMain(orderIdRequest);
		LogUtil.info(MODULE, JSON.toJSONString(resp).toString());
		model.addAttribute("ordMainResp", ordMainResp);
		model.addAttribute("rOrdReturnRefundResp", rOrdReturnRefundResp);
		model.addAttribute("orderDetailsMain", resp.getOrderDetailsMain());
		model.addAttribute("rBackApplyResp", resp.getrBackApplyResp());
		model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
		model.addAttribute("rBackApplyInfoResp", resp.getrBackApplyInfoResp());
		model.addAttribute("rBackPicResps", resp.getrBackPicResps());
		model.addAttribute("shareInfo", JSON.toJSONString(resp.getrBackApplyInfoResp()).toString());
		model.addAttribute("rBackTrackResps", resp.getrBackTrackResps());
		return "/order/ordback/backreview";
	}
	
	/**
	 * backGdsReview:退货详情信息. <br/>
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
		RBackReviewResp resp = this.returnBackRSV.queryBackGdsReview(rOrderBackReq);
		ROrdReturnRefundResp rOrdReturnRefundResp = returnBackRSV.calCulateBackInfo(rOrderBackReq);   
		orderIdRequest.setOrderId(vo.getOrderId());
		ROrdMainResponse ordMainResp = ordPmphMainRSV.queryOrderMain(orderIdRequest);
		LogUtil.info(MODULE, JSON.toJSONString(resp).toString());
		model.addAttribute("ordMainResp", ordMainResp);
		model.addAttribute("rOrdReturnRefundResp", rOrdReturnRefundResp);
		model.addAttribute("orderDetailsMain", resp.getOrderDetailsMain());
		model.addAttribute("rBackApplyResp", resp.getrBackApplyResp());
		model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
		model.addAttribute("rBackApplyInfoResp", resp.getrBackApplyInfoResp());
		model.addAttribute("rBackPicResps", resp.getrBackPicResps());
		model.addAttribute("shareInfo", JSON.toJSONString(resp.getrBackApplyInfoResp()).toString());
		model.addAttribute("rBackTrackResps", resp.getrBackTrackResps());
		return "/order/ordback/backdetail";
	}
	
	/**
	 * confirmReview:退货提交审核. <br/>
	 * @param vo
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value="/confirmReview")
	@ResponseBody
	public EcpBaseResponseVO confirmgReview(@Valid RBackGdsReviewReqVO vo) {
		LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo).toString());
		EcpBaseResponseVO resp = new EcpBaseResponseVO();
		try {
			RBackReviewReq rBackReviewReq = new RBackReviewReq();
			rBackReviewReq = vo.toBaseInfo(RBackReviewReq.class);
			ObjectCopyUtil.copyObjValue(vo, rBackReviewReq, "", false);
			RBackApplyInfoResp rBackApplyInfoResp = null;
			if(vo.getStatus().equals("0")) {   //审核不通过
				rBackReviewReq.setStatus(BackConstants.Status.REFUSE);
			} else if(vo.getStatus().equals("1")){   //一级审核 通过
				rBackReviewReq.setStatus("06");
				rBackApplyInfoResp = JSON.parseObject(vo.getShareInfo(), RBackApplyInfoResp.class);
			} else if(vo.getStatus().equals("2")){ //二级级审核 通过
				rBackReviewReq.setStatus("07");
				rBackApplyInfoResp = JSON.parseObject(vo.getShareInfo(), RBackApplyInfoResp.class);
			} else if(vo.getStatus().equals("3")) { //三级审核 通过 与原有程序逻辑保持一致
				rBackReviewReq.setStatus(BackConstants.Status.REVIEW_PASS);
				rBackApplyInfoResp = JSON.parseObject(vo.getShareInfo(), RBackApplyInfoResp.class);
			}
			rBackReviewReq.setApplyType(BackConstants.ApplyType.BACK_GDS);
			rBackReviewReq.setrBackApplyInfoResp(rBackApplyInfoResp);
			this.returnBackRSV.saveBackGdsReview(rBackReviewReq);
			resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		}catch(Exception e){
			LogUtil.error(MODULE, "============出错了============="+e.getMessage());
			resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
			resp.setResultMsg(e.getMessage());
		}
		return resp;
	}
	
	/**
     * confirmReview:退货提交审核. <br/>
     * @param vo
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value="/modifyBackMoney")
    @ResponseBody
    public EcpBaseResponseVO modifyBackMoney(@Valid ROrdReturnRefundReqVO vo) {
        EcpBaseResponseVO resp = new EcpBaseResponseVO();
        ROrdReturnRefundReq req = new ROrdReturnRefundReq();
        ObjectCopyUtil.copyObjValue(vo, req, "", false);
        try{
            returnBackRSV.modifyBackMoney(req);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);           
        }catch(Exception e){
            e.printStackTrace();
            LogUtil.error(MODULE, "============出错了============="+e.getMessage());
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
    
 
    @RequestMapping(value="/goModifyMoney")
    public String goModifyMoney(Model model, @Valid ROrdReturnRefundReqVO vo) {
         LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
         model.addAttribute("ordReturnRefundReqVO",vo);
         return "/order/ordback/modify/modifymoney";
    }
	
}


