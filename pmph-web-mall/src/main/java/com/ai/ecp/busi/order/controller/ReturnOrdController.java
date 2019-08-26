package com.ai.ecp.busi.order.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.mvc.annotation.NativeJson;
import com.ai.ecp.base.velocity.AiToolUtil;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.goods.gdsdetail.vo.GdsSkuMediaVO;
import com.ai.ecp.busi.order.OrdConstant;
import com.ai.ecp.busi.order.vo.RBackApplyOrdSubReqVO;
import com.ai.ecp.busi.order.vo.RBackApplyReqVO;
import com.ai.ecp.busi.order.vo.RBackConfirmReqVO;
import com.ai.ecp.busi.order.vo.ROrderBackReqVO;
import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.order.dubbo.dto.RBackApplyOrdResp;
import com.ai.ecp.order.dubbo.dto.RBackApplyOrdSubResp;
import com.ai.ecp.order.dubbo.dto.RBackApplyReq;
import com.ai.ecp.order.dubbo.dto.RBackConfirmReq;
import com.ai.ecp.order.dubbo.dto.ROrderBackDetailResp;
import com.ai.ecp.order.dubbo.dto.ROrderBackReq;
import com.ai.ecp.order.dubbo.dto.ROrderBackResp;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsResponse;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsMain;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsSub;
import com.ai.ecp.order.dubbo.interfaces.IOrdBackGdsRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdBackMoneyRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdDetailsRSV;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dubbo.dto.CompensateBackResp;
import com.ai.ecp.pmph.dubbo.dto.RBackApplyPmphOrdReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphYsymZhekouRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IReturnBackRSV;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value="/order/return")
public class ReturnOrdController extends EcpBaseController {

    private static final String MODULE = ReturnOrdController.class.getName();
    
    @Resource
    private IOrdBackMoneyRSV ordBackMoneyRSV;
    
    @Resource
    private IOrdBackGdsRSV ordBackGdsRSV;
    
    @Resource
    private IShopInfoRSV shopInfoRSV;
    
    @Resource
    private IReturnBackRSV returnBackRSV;
    
    @Resource
    private IGdsPmphYsymZhekouRSV gdsPmphYsymZhekouRSV;
    
    @Resource
    private IOrdDetailsRSV ordDetailsRSV;
    
    /**
     * 
     * moneyList:退款列表获取. <br/> 
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/moneyList")
    public String moneyList(Model model,ROrderBackReqVO vo) throws Exception{
    	 LogUtil.debug(MODULE, vo.toString());
         //后场服务所需要的DTO；
    	 ROrderBackReq dto = vo.toBaseInfo(ROrderBackReq.class);
         ObjectCopyUtil.copyObjValue(vo, dto, "", false);
    	 PageResponseDTO<ROrderBackResp> resp = ordBackMoneyRSV.queryBackMoneyByStaff(dto);
    	 if(resp==null){
    		 resp = new PageResponseDTO<ROrderBackResp>();
    		 resp.setPageSize(1);
         } 
    	model.addAttribute("resp",resp);
    	model.addAttribute("status", "01");
    	return "/order/return/list/money-list";
    }
    
    /**
     * 
     * returnList:退货列表获取. <br/> 
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnList")
    public String returnList(Model model,ROrderBackReqVO vo) throws Exception{
    	 LogUtil.debug(MODULE, vo.toString());
         //后场服务所需要的DTO；
    	 ROrderBackReq dto = vo.toBaseInfo(ROrderBackReq.class);
         ObjectCopyUtil.copyObjValue(vo, dto, "", false);
         PageResponseDTO<ROrderBackResp> resp = ordBackGdsRSV.queryBackGdsByStaff(dto);
    	 if(resp==null){
    		 resp = new PageResponseDTO<ROrderBackResp>();
    		resp.setPageSize(1);
         }
    	model.addAttribute("resp",resp);
    	model.addAttribute("status", "02");
    	return "/order/return/list/return-list";
    }
    
    /**
     * returnMoney:(退款详情页面). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnMoney")
    public String returnMoney(Model model,RBackConfirmReqVO vo) {	//后场服务所需要的DTO；
    	ROrderBackReq dto = new ROrderBackReq();
    	dto.setBackId(vo.getBackId());
    	dto.setOrderId(vo.getOrderId());
    	ROrderBackDetailResp resp  = ordBackMoneyRSV.queryBackMoneyDetails(dto);
    	//订单信息
    	model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
    	//订单优惠信息 （金额、资金账户、积分相关信息）
    	model.addAttribute("rBackDiscountResps", resp.getrBackDiscountResps());
    	//订单优惠信息 （优惠相关信息）
    	model.addAttribute("rBackCouponResps", resp.getBackApllyCoupList());
    	//订单优惠信息 （赠品相关信息）
    	model.addAttribute("rBackGiftResps", resp.getrBackGiftResps());
    	//订单优惠信息 （图片作证相关信息）
    	model.addAttribute("rBackPicResps", resp.getrBackPicResps());
    	//退款日志信息
    	model.addAttribute("rBackTrackResps", resp.getrBackTrackResps());
    	//退货申请信息
    	model.addAttribute("rBackApplyResp", resp.getrBackApplyResp()); 
    	
        return "/order/return/return-money";
    } 
    
    /**
     * returnCompensateMoney:(补偿性退款详情页面). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnCompensateMoney")
    public String returnCompensateMoney(Model model,RBackConfirmReqVO vo) {
        //后场服务所需要的DTO；
        ROrderBackReq dto = new ROrderBackReq();
        dto.setBackId(vo.getBackId());
        dto.setOrderId(vo.getOrderId());
        CompensateBackResp resp  = returnBackRSV.queryCompensateBackMoney(dto);      
        //退货申请信息
        model.addAttribute("compensateBackResp", resp);         
        return "/order/return/return-compensate-money";
    } 
    
    /**
     * returnDetail:(退货详情页面). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnDetail")
    public String returnDetail(Model model,RBackConfirmReqVO vo) {
    	//后场服务所需要的DTO；
    	ROrderBackReq dto = new ROrderBackReq();
    	 dto.setBackId(vo.getBackId());
         dto.setOrderId(vo.getOrderId());
    	ROrderBackDetailResp resp  = ordBackGdsRSV.queryBackGdsDetails(dto);
    	//订单信息
    	model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
    	//订单优惠信息 （金额、资金账户、积分相关信息）
    	model.addAttribute("rBackDiscountResps", resp.getrBackDiscountResps());
    	//订单优惠信息 （优惠券相关信息）
    	model.addAttribute("rBackCouponResps", resp.getBackApllyCoupList());
    	//订单优惠信息 （赠品相关信息）
    	model.addAttribute("rBackGiftResps", resp.getrBackGiftResps());
    	//订单优惠信息 （图片作证相关信息）
    	model.addAttribute("rBackPicResps", resp.getrBackPicResps());
    	//退款日志信息
    	model.addAttribute("rBackTrackResps", resp.getrBackTrackResps());
    	//退货申请信息
    	model.addAttribute("rBackApplyResp", resp.getrBackApplyResp()); 
    	
        return "/order/return/return-detail";
    } 
    /**
     * returnDetail:(退货详情页面). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnDetail/{backId}/{orderId}")
    public String returnDetailNew(Model model,@PathVariable("backId") String backId, @PathVariable("orderId") String orderId) {
    	RBackConfirmReqVO vo=new RBackConfirmReqVO();
    	vo.setBackId(Long.parseLong(backId));
    	vo.setOrderId(orderId);
    	//后场服务所需要的DTO；
    	ROrderBackReq dto = new ROrderBackReq();
    	dto.setBackId(vo.getBackId());
    	dto.setOrderId(vo.getOrderId());
    	ROrderBackDetailResp resp  = ordBackGdsRSV.queryBackGdsDetails(dto);
    	//订单信息
    	model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
    	//订单优惠信息 （金额、资金账户、积分相关信息）
    	model.addAttribute("rBackDiscountResps", resp.getrBackDiscountResps());
    	//订单优惠信息 （优惠券相关信息）
    	model.addAttribute("rBackCouponResps", resp.getBackApllyCoupList());
    	//订单优惠信息 （赠品相关信息）
    	model.addAttribute("rBackGiftResps", resp.getrBackGiftResps());
    	//订单优惠信息 （图片作证相关信息）
    	model.addAttribute("rBackPicResps", resp.getrBackPicResps());
    	//退款日志信息
    	model.addAttribute("rBackTrackResps", resp.getrBackTrackResps());
    	//退货申请信息
    	model.addAttribute("rBackApplyResp", resp.getrBackApplyResp()); 
    	
    	return "/order/return/return-detail";
    } 
    
    /**
     * checkReturn:(验证退货、退款申请及确认发货). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkReturn")
    public Map<String,Object> checkReturn(Model model, @RequestParam("backId") String backId, @RequestParam("orderId") String orderId,@RequestParam("oper") String oper) {
        Map<String, Object> map = new HashMap<String, Object>(); 
        try {
            ROrderDetailsRequest dto = new ROrderDetailsRequest(); 
            dto.setOrderId(orderId); 
            dto.setOper(oper);
            if(oper.equals("02")){
                dto.setBackId(Long.valueOf(backId));
            }
            ROrdCartsChkResponse  resp = new ROrdCartsChkResponse();
            resp = returnBackRSV.returnCheck(dto);
            map.put("flag",resp.isStatus());
            map.put("msg",resp.getMsg());
        } catch (Exception e) {
            LogUtil.error(MODULE, e.getMessage(),e);
            map.put("flag", false);
            map.put("msg",e.getMessage());
        }
        return map; 
    } 
    
    /**
     * returnApply:(退款申请提交页面). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnApply/{orderId}")
    public String returnApply(Model model, @PathVariable("orderId") String orderId) {
        ROrderBackReq dto = new ROrderBackReq();
        long staffId = dto.getStaff().getId();
        if (StringUtils.isBlank(orderId)) {
            throw new BusinessException("order.orderid.null.error");
        }
        dto.setOrderId(orderId);
        RBackApplyOrdResp resp = new RBackApplyOrdResp();
        resp = ordBackGdsRSV.queryBackOrderSub(dto);
        
        ROrderDetailsRequest reqOrd = new ROrderDetailsRequest();
        reqOrd.setOrderId(orderId);
        ROrderDetailsResponse respOrdDto = ordDetailsRSV.queryOrderDetails(reqOrd);
        List<SOrderDetailsSub> subList=respOrdDto.getsOrderDetailsSubs();
        if(!CollectionUtils.isEmpty(subList)){
        	for(SOrderDetailsSub ordSub:subList){
            	//调用商品接口判断是否存在电子书或者数字教材
                if(gdsPmphYsymZhekouRSV.ifDigitalTeachOrDigitalBook(ordSub.getSkuId())){
                	throw new BusinessException("该订单的商品含有数字产品，不允许退款！");
                } 
        	}
        }
        long createStaffId = resp.getSOrderDetailsMain().getCreateStaff();
        if(staffId!=createStaffId){
            //创建人和退货人不一致
            throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_PERMISSION);                     
        } 
        if(!resp.getSOrderDetailsMain().getStatus().equals(OrdConstants.OrderStatus.ORDER_STATUS_PAID)){
            //不在可退款状态
            throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_PERMISSION);                     
        } 
      //判断是否在退货流程中
        List<RBackApplyOrdSubResp> baos =  resp.getrBackApplyOrdSubResps();
        String status = "";
        for(RBackApplyOrdSubResp ordSubResp:baos){
            if(ordSubResp.getBackStatus().equals(BackConstants.IsProcess.YES)){
                status = BackConstants.IsProcess.YES;
                break;
            }
        }        
        if(StringUtils.isNotBlank(status)&&status.equals(BackConstants.IsProcess.YES)){
            //退货流程中
            throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_APPLY);   
        }else{      
            RBackApplyPmphOrdReq backApplyPmphOrdReq = new RBackApplyPmphOrdReq();
            ObjectCopyUtil.copyObjValue(resp, backApplyPmphOrdReq, "", false);
            backApplyPmphOrdReq.setApplyType(BackConstants.ApplyType.REFUND);
            ROrdReturnRefundResp  ordReturnRefundResp = returnBackRSV.calCulateBackInfo(backApplyPmphOrdReq);
            //订单优惠信息
            model.addAttribute("ordReturnRefundResp",ordReturnRefundResp);
            // 订单id
            model.addAttribute("orderId", orderId);
            // 子订单相关字段
            model.addAttribute("backApplyOrdSubResps", resp.getrBackApplyOrdSubResps());
            long backMoney = resp.getSOrderDetailsMain().getRealMoney();
            model.addAttribute("backMoney", backMoney);
            return "/order/return/return-apply";
        }
    } 
    
    /**
     * returnApply:(退款申请保存). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/saveBackMoneyApply")
    public EcpBaseResponseVO saveBackMoneyApply(Model model, RBackApplyReqVO vo) {
        LogUtil.info(MODULE, "===========================退款申请开始===============================");
        EcpBaseResponseVO resp = new EcpBaseResponseVO(); 
        try {
            //后场服务所需要的DTO；
            RBackApplyReq dto = vo.toBaseInfo(RBackApplyReq.class);
            ObjectCopyUtil.copyObjValue(vo, dto, "", false); 
            dto.setrBackOrdSubReqs(vo.getrBackOrdSubReqs());
            try{
                returnBackRSV.saveBackMoneyApply(dto);
            }catch(Exception e){
                e.printStackTrace();
            }
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        } catch(Exception e){
            e.printStackTrace();
            LogUtil.error(MODULE, "退款申请失败");
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage()!=null?e.getMessage():"系统异常");
        }
        LogUtil.info(MODULE, "========================退款申请结束=============================");
        return resp; 
    } 
    
    /**
     * returnChild:(子订单申请退货页面页面). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnChild/{orderId}")
    public String returnChild(Model model, @PathVariable("orderId") String orderId) { 
    	ROrderBackReq dto = new ROrderBackReq();
        if (StringUtil.isBlank(orderId)) {
            throw new BusinessException("order.orderid.null.error");
        }
        long staffId = dto.getStaff().getId();
        dto.setOrderId(orderId);
        RBackApplyOrdResp resp = new RBackApplyOrdResp();
        resp = returnBackRSV.queryBackOrderSub(dto);       
        long createStaffId = resp.getSOrderDetailsMain().getCreateStaff();
        if(staffId!=createStaffId){
            //创建人和退货人不一致
            throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_PERMISSION);                     
        } 
        //判断是否在退货流程中
        List<RBackApplyOrdSubResp> baos =  resp.getrBackApplyOrdSubResps();
        String status = "";
        for(RBackApplyOrdSubResp ordSubResp:baos){
            if(ordSubResp.getBackStatus().equals(BackConstants.IsProcess.YES)){
                status = BackConstants.IsProcess.YES;
                break;
            }
        }        
        if(StringUtils.isNotBlank(status)&&status.equals(BackConstants.IsProcess.YES)){
            //退货流程中
            throw new BusinessException(MsgConstants.ChkMsgCode.CHK_BACKGDS_APPLY);   
        }else{ 
            // 订单id
            model.addAttribute("orderId", orderId);
            //主订单相关字段
            model.addAttribute("sOrderDetailsMain", resp.getSOrderDetailsMain());
            // 子订单相关字段
            model.addAttribute("backApplyOrdSubResps", resp.getrBackApplyOrdSubResps());
           
            return "/order/return/return-child";       
        }             
    }
    
    /**
     * saveSessionSub:(保存session子订单信息). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveSessionSub/{orderId}")
    @ResponseBody
    public EcpBaseResponseVO saveSessionSub(@PathVariable("orderId") String orderId,Model model,RBackApplyReqVO vo,HttpServletRequest request) { 
    	EcpBaseResponseVO respVO = new EcpBaseResponseVO();
    	//行选中属性
		String itemCheck = "checked";
    	try {
    		LogUtil.info(MODULE, "===========================封装参数================================");
    		List<RBackApplyOrdSubResp> resp = new ArrayList<RBackApplyOrdSubResp>();
			if(!CollectionUtils.isEmpty(vo.getrBackApplyOrdSubResps())){
				for(RBackApplyOrdSubReqVO rBackApplyOrdSubReqVO : vo.getrBackApplyOrdSubResps()){				
					//是否明细项目被选中或者全部退货
					if((StringUtils.isNotEmpty(vo.getCheckedAll())&&vo.getCheckedAll().equals("1")&&rBackApplyOrdSubReqVO.getNum()>0)||(StringUtils.isNotEmpty(rBackApplyOrdSubReqVO.getItemCheck()) && rBackApplyOrdSubReqVO.getItemCheck().equals(itemCheck))){
						RBackApplyOrdSubResp rBackApplyOrdSubResp = new RBackApplyOrdSubResp();
						ObjectCopyUtil.copyObjValue(rBackApplyOrdSubReqVO, rBackApplyOrdSubResp, "", false);
						resp.add(rBackApplyOrdSubResp);
					}
				}
			} 
			respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            RBackApplyPmphOrdReq backApplyPmphOrdReq = new RBackApplyPmphOrdReq();
            backApplyPmphOrdReq.setrBackApplyOrdSubResps(resp);
            
            backApplyPmphOrdReq.setApplyType(BackConstants.ApplyType.BACK_GDS);
            SOrderDetailsMain orderDetailsMain= new SOrderDetailsMain();
            orderDetailsMain.setId(orderId);
            backApplyPmphOrdReq.setSOrderDetailsMain(orderDetailsMain);
            ROrdReturnRefundResp ordReturnRefundResp = returnBackRSV.calCulateBackInfo(backApplyPmphOrdReq);
            
			LogUtil.info(MODULE, "==========================保存session信息开始===============================================");
			//把子订单列表信息缓存起来 时间为半小时
			HttpSession session = request.getSession(); 
			Long staffId = new BaseInfo().getStaff().getId();
			session.setAttribute("refund"+orderId,ordReturnRefundResp);
			session.setAttribute(OrdConstant.ORDER_SESSION_KEY_SUB+staffId, resp);			
            //订单优惠信息          
            session.setMaxInactiveInterval(OrdConstant.ORDER_SESSION_TIME);            
            LogUtil.info(MODULE, "==========================保存session信息完毕===============================================");
		}catch(Exception e){
			LogUtil.error(MODULE, e.getMessage(),e);
			respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
			if(e.getMessage()==null){
				respVO.setResultMsg("请选择至少一项商品");
			}else{
				respVO.setResultMsg(e.getMessage());
			}
		}
		
		return respVO;
    }
    
    /**
     * returnApplySub:(退货申请提交页面). <br/> 
     * @param model
     * @param orderid
     * @param checkedAll
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnApplySub/{orderId}/{checkedAll}")
    public String returnApplySub(Model model, @PathVariable("orderId") String orderId,@PathVariable("checkedAll") String checkedAll,HttpServletRequest request) { 
    	try {
    		HttpSession session = request.getSession();
    		RBackApplyOrdResp resp = new RBackApplyOrdResp();
    		Long staffId = new BaseInfo().getStaff().getId();
    		LogUtil.info(MODULE, "==========================获取session信息===============================================");
    		List<RBackApplyOrdSubResp> rBackApplyOrdSubResps = (List<RBackApplyOrdSubResp>)session.getAttribute(OrdConstant.ORDER_SESSION_KEY_SUB+staffId); 
    		resp.setrBackApplyOrdSubResps(rBackApplyOrdSubResps);
    		ROrdReturnRefundResp ordReturnRefundResp = (ROrdReturnRefundResp)session.getAttribute("refund"+orderId);
    		if(resp == null){
                return "redirect:/homepage";
            }
			// 订单id
	        model.addAttribute("orderId", orderId);
	        model.addAttribute("checkedAll",checkedAll);
	        // 子订单相关字段
	        model.addAttribute("backApplyOrdSubResps", resp.getrBackApplyOrdSubResps());
	        model.addAttribute("ordReturnRefundResp",ordReturnRefundResp);
		} catch (Exception e) {
			LogUtil.error(MODULE, "==================================展示退货申请提交页面出错============================");
			LogUtil.error(MODULE, e.getMessage(),e);
		}  
        return "/order/return/return-apply-sub";
    }
    
    /**
     * returnApply:(退货申请保存). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveBackGdsApply")
    @ResponseBody
    public EcpBaseResponseVO saveBackGdsApply(Model model, RBackApplyReqVO vo) {
    	LogUtil.info(MODULE, "===========================退货申请开始===============================");
    	EcpBaseResponseVO resp = new EcpBaseResponseVO(); 
        try {
        	//后场服务所需要的DTO；
        	RBackApplyReq dto = vo.toBaseInfo(RBackApplyReq.class);
            ObjectCopyUtil.copyObjValue(vo, dto, "", false); 
            dto.setrBackOrdSubReqs(vo.getrBackOrdSubReqs());
            returnBackRSV.saveBackGdsApply(dto);
        	resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch(Exception e){
		    e.printStackTrace();
		    LogUtil.error(MODULE, "退货申请失败");
		    resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
		    resp.setResultMsg(e.getMessage()!=null?e.getMessage():"系统异常");
		}
		LogUtil.info(MODULE, "========================退货申请结束=============================");
		return resp; 
    }
    
    /**
     * returnBack:(退货发货页面). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/returnBack/{backId}/{orderId}")
    public String returnBack(Model model, @PathVariable("backId") String backId, @PathVariable("orderId") String orderId) {
    	//后场服务所需要的DTO；
    	ROrderBackReq dto = new ROrderBackReq();
    	dto.setBackId(Long.valueOf(backId));
    	dto.setOrderId(orderId);
    	ROrderBackDetailResp resp  = ordBackGdsRSV.queryBackGdsDetails(dto);
        model.addAttribute("rBackApplyResp", resp.getrBackApplyResp());
        model.addAttribute("rBackGdsResps", resp.getrBackGdsResps());
        return "/order/return/return-back";
    }
    
    /**
     * returnBack:(退货发货保存). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveBackGdsSend")
    @ResponseBody
    public EcpBaseResponseVO saveBackGdsSend(Model model, RBackConfirmReqVO vo) {
    	LogUtil.info(MODULE, "===========================退货发货开始===============================");
    	EcpBaseResponseVO resp = new EcpBaseResponseVO(); 
        try {
        	//后场服务所需要的DTO；
        	RBackConfirmReq dto = vo.toBaseInfo(RBackConfirmReq.class);
            ObjectCopyUtil.copyObjValue(vo, dto, "", false); 
            ordBackGdsRSV.saveBackGdsSend(dto);
        	resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch(Exception e){
		    e.printStackTrace();
		    LogUtil.error(MODULE, "退货发货失败");
		    resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
		    resp.setResultMsg(e.getMessage()!=null?e.getMessage():"系统异常");
		}
		LogUtil.info(MODULE, "========================退货发货结束=============================");
		return resp; 
    } 
    
    
    /**
     * uploadImage:(上传图片). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadimage")
    @ResponseBody
    @NativeJson(true)
    private String uploadImage(Model model,HttpServletRequest req, HttpServletResponse rep) {
        JSONObject obj = new JSONObject();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
        // 获取图片
        Iterator<MultipartFile> files = multipartRequest.getFileMap().values().iterator();
        MultipartFile file = null;
        if (files.hasNext()) {
            file = files.next();
        }
        Iterator<String> ids = multipartRequest.getFileMap().keySet().iterator();
        String id = null;
        if (ids.hasNext()) {
            id = ids.next();
        }
        try {
            if (file == null) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择上传文件！");
                LogUtil.error(MODULE, "请选择上传文件！");
                return obj.toJSONString();
            }
            String fileName = file.getOriginalFilename();
            String extensionName = "." + getExtensionName(fileName);

            /** 支持文件拓展名：.jpg,.png,.jpeg,.gif,.bmp */
            boolean flag = Pattern.compile("\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$")
                    .matcher(extensionName.toLowerCase()).find();
            if (!flag) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择图片文件(.jpg,.png,.jpeg,.gif)!");
                LogUtil.error(MODULE, "上传图片失败,原因---请选择图片文件(.jpg,.png,.jpeg,.gif)!");
                return obj.toJSONString();
            }
            
            byte[] datas = inputStream2Bytes(file.getInputStream());
            if(datas.length>5*1024*1024){
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "上传的图片不能大于5M！");
                LogUtil.error(MODULE, "图片上传失败，上传的图片必须小于5M！");
                return obj.toJSONString();
            }
            fileName = Math.random()+"";
            String vfsId = ImageUtil.upLoadImage(datas, fileName);
            resultMap.put("vfsId", vfsId);
            resultMap.put("imageName", fileName);
            resultMap.put("id", id);
            resultMap.put("imagePath", new AiToolUtil().genImageUrl(vfsId,"150x150!"));
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            obj.put("message", "保存成功!");
            obj.put("map", resultMap);
        } catch (BusinessException e) {
            LogUtil.error(MODULE,"上传图片出错,原因---"+e.getMessage(), e);
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
        } catch (IOException e) {
            LogUtil.error(MODULE,"上传图片出错,原因---"+e.getMessage(), e);
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
        }
        return obj.toJSONString();
    } 
   
    /**
     * getExtensionName:(获取文件拓展名). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    private String getExtensionName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }
    
    /**
     * inputStream2Bytes:(将InputStream转换成byte数组). <br/> 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    private byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count = -1;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return outStream.toByteArray();
    }
    
    
    /**
	 * 
	 * :(获取图片展示). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * 
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/querygdspictrue")
	@ResponseBody
	public Map<String, Object> queryGdsPictrue(
			@RequestParam("pictrueNum") String pictrueNum,
			@RequestParam("pictrueHeight") String height,
			@RequestParam("pictrueWidth") String width,
			@RequestParam("pictrueMoreHeight") String moreHeight,
			@RequestParam("pictrueMoreWidth") String moreWidth,
			@RequestParam("vfsId") String vfsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GdsSkuMediaVO> resultList = new ArrayList<GdsSkuMediaVO>();
		String [] vfsIds = vfsId.split(","); 
		for(int i=0;i<vfsIds.length;i++){
			if(StringUtil.isNotEmpty(vfsIds[i])){
				GdsSkuMediaVO gdsSkuMediaVO = new GdsSkuMediaVO();
				gdsSkuMediaVO.setBigUrl(new AiToolUtil().genImageUrl(vfsIds[i],moreHeight + "x" + moreWidth + "!"));
				gdsSkuMediaVO.setMiddleUrl(new AiToolUtil().genImageUrl(
						vfsIds[i],Integer.parseInt(moreHeight) / 2 + "x"+ Integer.parseInt(moreWidth) / 2+ "!"));
				gdsSkuMediaVO.setUrl(new AiToolUtil().genImageUrl(
						vfsIds[i], height+ "x" + width + "!"));
				resultList.add(gdsSkuMediaVO);
			}
		} 
		map.put("bigUrl", new AiToolUtil().genImageUrl("", moreHeight + "x" + moreWidth + "!"));
        map.put("middleUrl", new AiToolUtil().genImageUrl("",Integer.parseInt(moreHeight) / 2 + "x"+ Integer.parseInt(moreWidth) / 2+"!"));
		map.put("result", resultList);
		map.put("pictrueHeight", height);
		map.put("pictrueWidth", width);
		map.put("pictrueMoreHeight", moreHeight);
		map.put("pictrueMoreWidth", moreWidth);
		map.put("pictrueNum", pictrueNum);
		return map;
	}
}

