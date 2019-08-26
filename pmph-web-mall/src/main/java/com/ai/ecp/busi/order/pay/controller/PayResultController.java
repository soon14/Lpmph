package com.ai.ecp.busi.order.pay.controller;

import java.sql.Timestamp;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.ecp.order.dubbo.dto.ROrdPayRelReq;
import com.ai.ecp.order.dubbo.dto.ROrdPayRelResp;
import com.ai.ecp.order.dubbo.interfaces.IOrdPayRelRSV;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.busi.order.HtmlEscape;
import com.ai.ecp.busi.order.RequestUtil;
import com.ai.ecp.busi.order.pay.vo.WeiXinVO;
import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsResponse;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsSub;
import com.ai.ecp.order.dubbo.dto.pay.OrderPayStatusVO;
import com.ai.ecp.order.dubbo.interfaces.IOrdDetailsRSV;
import com.ai.ecp.order.dubbo.interfaces.pay.IPaymentRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphExRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphYsymZhekouRSV;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;

 

/**
 * 
 * 用于解析支付通道给我们的参数
 * Description: <br>
 * Date:2015年10月19日下午8:27:13  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/payresult")
public class PayResultController extends EcpBaseController {
	public static final String module = PayResultController.class.getName();
 
	@Resource
    private IPaymentRSV paymentRSV;
	
	@Resource
    private IOrdDetailsRSV ordDetailsRSV;

    @Resource
    private IOrdPayRelRSV iOrdPayRelRSV;
    
    @Resource
    private IGdsPmphYsymZhekouRSV gdsPmphYsymZhekouRSV;
    
    @Resource
    public IGdsPmphExRSV gdsPmphExRSV;
    
    @Resource
    public IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
	
	/**
	 * 鸿支付在线支付结果展示
	 */
	@RequestMapping(value = "/9002")
	public String hongpay(Model model,HttpServletRequest request,HttpServletResponse response) {
		return this.showResultPage(model,"9002", this.getParamMap(request),request,response);
	}

	/**
	 * 支付宝在线支付结果解析
	 */
	@RequestMapping(value = "/9003")
	public String alipay(Model model,HttpServletRequest request,HttpServletResponse response) {
		return this.showResultPage(model,"9003", this.getParamMap(request),request,response);
	}

	/**
	 * 农行在线支付结果解析
	 */
	@RequestMapping(value = "/9004")
	public String abcpay(Model model,HttpServletRequest request,HttpServletResponse response) {
		return this.showResultPage(model,"9004", this.getParamMap(request),request,response);
	}
	
	/**
     * 微信扫码在线支付结果解析
     */
    @RequestMapping(value = "/9007/{joinOrderId}")
    public String weixinpay(Model model,@PathVariable String joinOrderId,HttpServletRequest request,HttpServletResponse response) {
        LogUtil.info(module,"支付成功合并后的订单号:"+joinOrderId);
        
        try{
          //合并支付获取合并支付的订单
            ROrdPayRelReq rOrdPayRelReq = new ROrdPayRelReq();
            rOrdPayRelReq.setJoinOrderid(joinOrderId);
            List<ROrdPayRelResp> ordList = new ArrayList<ROrdPayRelResp>();
            ordList = iOrdPayRelRSV.queryOrdPayRelByOption(rOrdPayRelReq);
            Timestamp time = new Timestamp(0l);
            Long siteId = 0l;
            String showOrderIds = "";
            String orderId = "";
/*            if(ordList != null && ordList.size() >=1){
                time = ordList.get(0).getCreateTime();
                ROrderDetailsRequest req = new ROrderDetailsRequest();
                req.setOrderId(ordList.get(0).getOrderId());
                ROrderDetailsResponse respDto = ordDetailsRSV.queryOrderDetails(req);
                if(respDto != null){
                    siteId = respDto.getsOrderDetailsMain().getSiteId();
                }
                orderId = ordList.get(0).getOrderId();
            }
            for(int i = 0;i<ordList.size();i++){
                ROrdPayRelResp rOrdPayRelResp = ordList.get(i);
                if(i == 0){
                    showOrderIds = rOrdPayRelResp.getOrderId();
                }else{
                    showOrderIds = showOrderIds + "," + rOrdPayRelResp.getOrderId();
                }
            }*/
            if(ordList != null && ordList.size() >=1){
                time = ordList.get(0).getCreateTime();
                ROrderDetailsRequest req = new ROrderDetailsRequest();
                req.setOrderId(ordList.get(0).getOrderId());
                ROrderDetailsResponse respDto = ordDetailsRSV.queryOrderDetails(req);
                if(respDto != null){
                    siteId = respDto.getsOrderDetailsMain().getSiteId();
                }
                orderId = ordList.get(0).getOrderId();
            }else{
            	orderId = joinOrderId;
                ROrdPayRelResp rOrdPayRelResp = new ROrdPayRelResp();
                rOrdPayRelResp.setOrderId(orderId);
                ordList = new ArrayList<ROrdPayRelResp>();
                ordList.add(rOrdPayRelResp);
            }                        
            String showAppUrlFlag="0";
            String showChargeUrlFlag="0";
            String dispatchType = "";
            String showZzsFlag = "0";
            String showXfkFlag = "0";
            String containZCFlag = "0";
            String showLCFlag = "0";
            String showYYFlag = "0";
            String showYJKFlag = "0";
            for(int index = 0,size = ordList.size(); index < size; index++){
            	ROrdPayRelResp rOrdPayRelResp = ordList.get(index);
            	if(index == 0){
            		showOrderIds = rOrdPayRelResp.getOrderId();
            	}else{
            		showOrderIds = showOrderIds + "," + rOrdPayRelResp.getOrderId();
            	}
                ROrderDetailsRequest reqOrd = new ROrderDetailsRequest();
                reqOrd.setOrderId(rOrdPayRelResp.getOrderId());
                ROrderDetailsResponse respOrdDto = ordDetailsRSV.queryOrderDetails(reqOrd);
                dispatchType = respOrdDto.getsOrderDetailsMain().getDispatchType();
                List<SOrderDetailsSub> subList=respOrdDto.getsOrderDetailsSubs();
                if(!CollectionUtils.isEmpty(subList)){
                	for(SOrderDetailsSub ordSub:subList){
                	    String zcFlag = "0";
                    	//调用商品接口判断是否存在电子书或者数字教材
                        GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
                        gdsSkuInfoReqDTO.setId(ordSub.getSkuId());
                        GdsSkuInfoRespDTO gdsSkuInfo = gdsSkuInfoQueryRSV.queryGdsSkuInfoResp(gdsSkuInfoReqDTO);
                        //判断是否数字教材/电子书
                        if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1200>")||StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1201>")){
                            showAppUrlFlag="1";
                            zcFlag = "1";
                        }else{
                            showZzsFlag = "1";
                        }
                        
                        String lcPlatCatg = SysCfgUtil.fetchSysCfg("GDS_LC_CAT_CODE").getParaValue();
                        String yyPlatCatg = SysCfgUtil.fetchSysCfg("GDS_YY_CAT_CODE").getParaValue();
                        String yjkPlatCatg = SysCfgUtil.fetchSysCfg("GDS_YJK_CAT_CODE").getParaValue();
                        if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(), new StringBuilder("<").append(lcPlatCatg).append(">").toString())){
                        	//判断是否是临床助手
                        	showLCFlag = lcPlatCatg;
                        }else if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(), new StringBuilder("<").append(yyPlatCatg).append(">").toString())){
                        	//判断是否是用药助手
                        	showYYFlag = yyPlatCatg;
                        }else if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(), new StringBuilder("<").append(yjkPlatCatg).append(">").toString())){
                        	//判断是否是约健康
                        	showYJKFlag = yjkPlatCatg;
                        }
                        
                        //判断是否数字图书馆消费卡
                        if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1207>")){
                            showXfkFlag = "1";
                            zcFlag = "1";
                        }
                    	//调用商品接口判断是否存在充值卡             
                        LongReqDTO longReqDTO = new LongReqDTO();
                        //加载参数
                        longReqDTO.setId(ordSub.getGdsType());    
                        if(gdsPmphExRSV.isVirtualCard(longReqDTO)){ 
                        	showChargeUrlFlag="1";
                            zcFlag = "1";
                        }
                        if("0".equals(zcFlag)){
                            containZCFlag = "1";    //包含正常的商品
                        }
                	}
                }
            }            
            model.addAttribute("orderIdList", ordList);
            model.addAttribute("orderId", orderId);
            model.addAttribute("showOrderIds", showOrderIds);
            model.addAttribute("payTime", time);
            model.addAttribute("siteId", siteId);
            model.addAttribute("showChargeUrlFlag", showChargeUrlFlag);  
            model.addAttribute("showAppUrlFlag", showAppUrlFlag); 
            model.addAttribute("dispatchType", dispatchType);
            model.addAttribute("showZzsFlag", showZzsFlag);
            model.addAttribute("showXfkFlag", showXfkFlag);
            model.addAttribute("containZCFlag", containZCFlag);
            model.addAttribute("showLCFlag", showLCFlag);
            model.addAttribute("showYYFlag", showYYFlag);
            model.addAttribute("showYJKFlag", showYJKFlag);
        } catch (Exception e) {
            LogUtil.error(module, e.getMessage(), e);
            return "/order/pay/pay-failure";
        }
        return "/order/pay/pay-success";
    }
    
    /**
     * 
     * getPayStatus:(查询支付结果状态). <br/> 
     * 
     * @param model
     * @param vo
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value="/getPayStatus")
    @ResponseBody
    public Map<String, Object> getPayStatus(Model model, WeiXinVO vo) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>(); 
        map.put("payFlag", "");
        try {
            String [] orderIds = vo.getOrderIds().split(",");  
            ROrdPayRelReq rOrdPayRelReq = new ROrdPayRelReq();
            rOrdPayRelReq.setOrderId(orderIds[0]);
            rOrdPayRelReq.setPayFlag(OrdConstants.Order.ORDER_PAY_FLAG_1);
            List<ROrdPayRelResp> ordList = iOrdPayRelRSV.queryOrdPayRelByOption(rOrdPayRelReq);
            if(CollectionUtils.isNotEmpty(ordList)){ 
                map.put("payFlag", ordList.get(0).getPayFlag());
            }
        } catch (Exception e) {
            map.put("payFlag","");
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /**
     * 线上支付
     * showResultPage:(支付成功或失败). <br/>  
     *
     * @param payWay
     * @param paramMap
     * @return
     * @since JDK 1.6
     */
    private String showResultPage(Model model,String payWay,Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response) {
        OrderPayStatusVO orderPayStatusVO = new OrderPayStatusVO();
        try {
            orderPayStatusVO = paymentRSV.parsePayStatus(payWay, paramMap);
            if(orderPayStatusVO.getFlag().equals("0")){
                //合并支付获取合并支付的订单
                ROrdPayRelReq rOrdPayRelReq = new ROrdPayRelReq();
                rOrdPayRelReq.setJoinOrderid(orderPayStatusVO.getOrderId());
                List<ROrdPayRelResp> ordList = iOrdPayRelRSV.queryOrdPayRelByOption(rOrdPayRelReq);
                Timestamp time = new Timestamp(0l);
                Long siteId = 0l;
                String orderId = "";
                String showOrderIds = "";
                if(ordList != null && ordList.size() >=1){
                    time = ordList.get(0).getCreateTime();
                    ROrderDetailsRequest req = new ROrderDetailsRequest();
                    req.setOrderId(ordList.get(0).getOrderId());
                    ROrderDetailsResponse respDto = ordDetailsRSV.queryOrderDetails(req);
                    if(respDto != null){
                        siteId = respDto.getsOrderDetailsMain().getSiteId();
                    }
                    orderId = ordList.get(0).getOrderId();
                }else{
                	orderId = orderPayStatusVO.getOrderId();
                    ROrdPayRelResp rOrdPayRelResp = new ROrdPayRelResp();
                    rOrdPayRelResp.setOrderId(orderId);
                    ordList = new ArrayList<ROrdPayRelResp>();
                    ordList.add(rOrdPayRelResp);
                }
                String showAppUrlFlag="0";
                String showChargeUrlFlag="0";
                String dispatchType = "";
                String showZzsFlag = "0";
                String showXfkFlag = "0";
                String containZCFlag = "0";
                String showLCFlag = "0";
                String showYYFlag = "0";
                String showYJKFlag = "0";
                for(int index = 0,size = ordList.size(); index < size; index++){
                	ROrdPayRelResp rOrdPayRelResp = ordList.get(index);
                	if(index == 0){
                		showOrderIds = rOrdPayRelResp.getOrderId();
                	}else{
                		showOrderIds = showOrderIds + "," + rOrdPayRelResp.getOrderId();
                	}
                    ROrderDetailsRequest reqOrd = new ROrderDetailsRequest();
                    reqOrd.setOrderId(rOrdPayRelResp.getOrderId());
                    ROrderDetailsResponse respOrdDto = ordDetailsRSV.queryOrderDetails(reqOrd);
                    dispatchType = respOrdDto.getsOrderDetailsMain().getDispatchType();
                    List<SOrderDetailsSub> subList=respOrdDto.getsOrderDetailsSubs();
                    if(!CollectionUtils.isEmpty(subList)){
                    	for(SOrderDetailsSub ordSub:subList){
                    	    String zcFlag = "0";
                        	//调用商品接口判断是否存在电子书或者数字教材
                            GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
                            gdsSkuInfoReqDTO.setId(ordSub.getSkuId());
                            GdsSkuInfoRespDTO gdsSkuInfo = gdsSkuInfoQueryRSV.queryGdsSkuInfoResp(gdsSkuInfoReqDTO);
                            //判断是否数字教材/电子书
                            if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1200>")||StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1201>")){
                                showAppUrlFlag="1";
                                zcFlag = "1";
                            }else{
                                showZzsFlag = "1";
                            }
                            
                            String lcPlatCatg = SysCfgUtil.fetchSysCfg("GDS_LC_CAT_CODE").getParaValue();
                            String yyPlatCatg = SysCfgUtil.fetchSysCfg("GDS_YY_CAT_CODE").getParaValue();
                            String yjkPlatCatg = SysCfgUtil.fetchSysCfg("GDS_YJK_CAT_CODE").getParaValue();
                            if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(), new StringBuilder("<").append(lcPlatCatg).append(">").toString())){
                            	//判断是否是临床助手
                            	showLCFlag = lcPlatCatg;
                            }else if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(), new StringBuilder("<").append(yyPlatCatg).append(">").toString())){
                            	//判断是否是用药助手
                            	showYYFlag = yyPlatCatg;
                            }else if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(), new StringBuilder("<").append(yjkPlatCatg).append(">").toString())){
                            	//判断是否是约健康
                            	showYJKFlag = yjkPlatCatg;
                            }
                            
                            //判断是否数字图书馆消费卡
                            if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1207>")){
                                showXfkFlag = "1";
                                zcFlag = "1";
                            }
                        	//调用商品接口判断是否存在充值卡             
                            LongReqDTO longReqDTO = new LongReqDTO();
                            //加载参数
                            longReqDTO.setId(ordSub.getGdsType());    
                            if(gdsPmphExRSV.isVirtualCard(longReqDTO)){ 
                            	showChargeUrlFlag="1";
                                zcFlag = "1";
                            }
                            if("0".equals(zcFlag)){
                                containZCFlag = "1";    //包含正常的商品
                            }
                    	}
                    }
                }

                model.addAttribute("orderIdList", ordList);
                model.addAttribute("orderId", orderId);
                model.addAttribute("showOrderIds", showOrderIds);
                model.addAttribute("payTime", time);
                model.addAttribute("siteId", siteId);
                model.addAttribute("showChargeUrlFlag", showChargeUrlFlag);  
                model.addAttribute("showAppUrlFlag", showAppUrlFlag); 
                model.addAttribute("dispatchType", dispatchType);
                model.addAttribute("showZzsFlag", showZzsFlag);
                model.addAttribute("showXfkFlag", showXfkFlag);
                model.addAttribute("containZCFlag", containZCFlag);
                model.addAttribute("showLCFlag", showLCFlag);
                model.addAttribute("showYYFlag", showYYFlag);
                model.addAttribute("showYJKFlag", showYJKFlag);
            }else{
                if("9004".equals(payWay)){
                    response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
                return "/order/pay/pay-failure";
            }

        } catch (Exception e) {
            LogUtil.error(module, e.getMessage(),e);
            if("9004".equals(payWay)){
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            return "/order/pay/pay-failure";
        }
        return "/order/pay/pay-success";
    }

    /**
	 *    
	 * 从Request获取参数
	 * @return 
	 * @since JDK 1.6
	 */
    private Map<String,String> getParamMap(HttpServletRequest request){
        Map<String, String> m = new HashMap<String, String>();
        Iterator<String> keys = request.getParameterMap().keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            m.put(key, getParam(key,request));
        }
        return m;
    }
    
    /**
     * 从Request获取参数
     * @param name
     * @return
     */
    private String getParam(String name,HttpServletRequest request){
        if(RequestUtil.isAjaxRequest(request)){
            return request.getParameter(name);
        } else {
            return HtmlEscape.htmlEncode(request.getParameter(name));
        }
    }
    

}
