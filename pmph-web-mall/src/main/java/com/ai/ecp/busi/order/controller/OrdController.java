package com.ai.ecp.busi.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.vo.ROrdAddressReqVO;
import com.ai.ecp.busi.order.vo.RQueryOrderReqVO;
import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.ROrdAddressRequest;
import com.ai.ecp.order.dubbo.dto.ROrdAddressResponse;
import com.ai.ecp.order.dubbo.dto.ROrdExpressDetailsResp;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsResponse;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdDetailsRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdReceiptRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrderExpressRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.interfaces.ICustAddrRSV;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

@Controller
@RequestMapping(value = "/ord")
public class OrdController extends EcpBaseController {

    private static final String MODULE = OrdController.class.getName();

    @Resource
    private IOrdMainRSV ordMainRSV;

    @Resource
    private IOrdReceiptRSV ordReceiptRSV;

    @Resource
    private IOrdDetailsRSV ordDetailsRSV;
    
    @Resource
    private ICustAddrRSV custaddrRSV;
    
    @Resource
    private IOrderExpressRSV orderExpressRSV;

    @RequestMapping()
    public String init(Model model, RQueryOrderReqVO vo,
            @RequestParam(value = "status") String status) throws Exception {
        LogUtil.debug(MODULE, vo.toString());
        // 后场服务所需要的DTO；
        RQueryOrderRequest rdor = new RQueryOrderRequest();

        if (StringUtil.isBlank(status)) {
            status = "01";
        }
        rdor.setPageNo(1);
        rdor.setPageSize(10);
        rdor.setStaffId(rdor.getStaff().getId());
        rdor.setSiteId(1l);
        rdor.setSysType("00");
        rdor.setStatus(status); // 全部订单

        rdor.setBegDate(ParamsTool.params(vo).get("begDate"));
        rdor.setEndDate(ParamsTool.params(vo).get("endDate"));
        PageResponseDTO<RCustomerOrdResponse> rdors = this.ordMainRSV.queryOrderByStaffId(rdor);
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        model.addAttribute("resp", rdors);
        model.addAttribute("status", status);
        return "/order/all-list";
    }

    @RequestMapping(value = "/myorder/comment")
    public String comment() {
        return "/order/order-comment";
    }
    
    // 订单详情
    /**
     * 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail")
    public String otherDomainQueryOrderDetails(Model model, @RequestParam("orderId") String orderid) {

        ROrderDetailsRequest rdto = new ROrderDetailsRequest();
        if (StringUtil.isBlank(orderid)) {
            throw new BusinessException("order.orderid.null.error");
        }
        rdto.setOrderId(orderid);
        rdto.setOper("01");
        ROrderDetailsResponse resp = new ROrderDetailsResponse();

        if(!ordMainRSV.queryChkStatus(rdto).isStatus()){

            return "redirect:/homepage";
        }
        resp = ordDetailsRSV.queryOrderDetails(rdto);

        // 订单id
        model.addAttribute("orderId", orderid);
        // 主订单相关字段
        model.addAttribute("sOrderDetailsMain", resp.getsOrderDetailsMain());
        // 子订单相关字段
        model.addAttribute("sOrderDetailsSubs", resp.getsOrderDetailsSubs());
        // 订单优惠相关字段
        model.addAttribute("sOrderDetailsDiscount", resp.getsOrderDetailsDiscount());
        // 订单跟踪相关字段
        model.addAttribute("sOrderDetailsTracks", resp.getsOrderDetailsTracks());
        // 订单收货地址相关字段
        model.addAttribute("sOrderDetailsAddr", resp.getsOrderDetailsAddr());
        // 订单普通发票相关字段
        model.addAttribute("sOrderDetailsComm", resp.getsOrderDetailsComm());
        // 订单增值税发票相关字段
        model.addAttribute("sOrderDetailsTax", resp.getsOrderDetailsTax());
        //物流信息相关字段
        model.addAttribute("ordExpressDetailsResps",resp.getOrdExpressDetailsResps());
        //赠品信息相关
        model.addAttribute("sOrderDetailsGift", resp.getsOrderDetailsGifts());
        // 进度条相关
        Map<String, Integer> status = ParamsTool.getStatusMap();
        List<String> statuslist = ParamsTool.getStatusList();
        model.addAttribute("status", status);
        model.addAttribute("statuslist", statuslist);

        return "/order/detail/otherDomain/order-detail";
    }

    // 订单详情
    /**
     * 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/{orderId}")
    public String queryOrderDetails(Model model, @PathVariable("orderId") String orderid) {

        ROrderDetailsRequest rdto = new ROrderDetailsRequest();
        if (StringUtil.isBlank(orderid)) {
            throw new BusinessException("order.orderid.null.error");
        }
        rdto.setOrderId(orderid);
        rdto.setOper("01");
        ROrderDetailsResponse resp = new ROrderDetailsResponse();
        if(!ordMainRSV.queryChkStatus(rdto).isStatus()){

            return "redirect:/homepage";
        }
        resp = ordDetailsRSV.queryOrderDetails(rdto);

        // 订单id
        model.addAttribute("orderId", orderid);
        // 主订单相关字段
        if(resp.getsOrderDetailsMain().getBuyerMsg() != null){
            resp.getsOrderDetailsMain().setBuyerMsg(resp.getsOrderDetailsMain().getBuyerMsg().replace("\n","<br />"));
        }
        model.addAttribute("sOrderDetailsMain", resp.getsOrderDetailsMain());
        // 子订单相关字段
        model.addAttribute("sOrderDetailsSubs", resp.getsOrderDetailsSubs());
        // 订单优惠相关字段
        model.addAttribute("sOrderDetailsDiscount", resp.getsOrderDetailsDiscount());
        // 订单跟踪相关字段
        model.addAttribute("sOrderDetailsTracks", resp.getsOrderDetailsTracks());
        // 订单收货地址相关字段
        model.addAttribute("sOrderDetailsAddr", resp.getsOrderDetailsAddr());
        // 订单普通发票相关字段
        model.addAttribute("sOrderDetailsComm", resp.getsOrderDetailsComm());
        // 订单增值税发票相关字段
        model.addAttribute("sOrderDetailsTax", resp.getsOrderDetailsTax());
        //物流信息相关字段
        model.addAttribute("ordExpressDetailsResps",resp.getOrdExpressDetailsResps());
        //赠品信息相关
        model.addAttribute("sOrderDetailsGifts", resp.getsOrderDetailsGifts());
        // 进度条相关
        Map<String, Integer> status = ParamsTool.getStatusMap();
        List<String> statuslist = ParamsTool.getStatusList();
        model.addAttribute("status", status);
        model.addAttribute("statuslist", statuslist);
        // 是否展示卖家备注（0表示不展示）
        model.addAttribute("isOpenSellerMsgOrNot", 0);
        return "/order/detail/order-detail";
    }
    /**
     * 订单详情
     */
    @RequestMapping(value = "/orderdetails/{orderId}")
    public String queryOrderDetailsNew(Model model, @PathVariable("orderId") String orderId) {

        ROrderDetailsRequest rdto = new ROrderDetailsRequest();
        if (StringUtil.isBlank(orderId)) {
            throw new BusinessException("order.orderid.null.error");
        }
        rdto.setOrderId(orderId);
        ROrderDetailsResponse resp = new ROrderDetailsResponse();
        resp = ordDetailsRSV.queryOrderDetails(rdto);

        // 订单id
        model.addAttribute("orderId", orderId);
        // 主订单相关字段
        if(resp.getsOrderDetailsMain().getBuyerMsg() != null){
            resp.getsOrderDetailsMain().setBuyerMsg(resp.getsOrderDetailsMain().getBuyerMsg().replace("\n","<br />"));
        }

        model.addAttribute("sOrderDetailsMain", resp.getsOrderDetailsMain());
        // 子订单相关字段
        model.addAttribute("sOrderDetailsSubs", resp.getsOrderDetailsSubs());
        // 订单优惠相关字段
        model.addAttribute("sOrderDetailsDiscount", resp.getsOrderDetailsDiscount());
        // 订单跟踪相关字段
        model.addAttribute("sOrderDetailsTracks", resp.getsOrderDetailsTracks());
        // 订单收货地址相关字段
        model.addAttribute("sOrderDetailsAddr", resp.getsOrderDetailsAddr());
        // 订单普通发票相关字段
        model.addAttribute("sOrderDetailsComm", resp.getsOrderDetailsComm());
        // 订单增值税发票相关字段
        model.addAttribute("sOrderDetailsTax", resp.getsOrderDetailsTax());
        //物流信息相关字段
        model.addAttribute("sOrderDetailsDeliverys", resp.getsOrderDetailsDeliverys());
        //物流信息相关字段
        model.addAttribute("ordExpressDetailsResps",resp.getOrdExpressDetailsResps());
        
        Map<String, Integer> status = ParamsTool.getStatusMap();
        List<String> statuslist = ParamsTool.getStatusList();
        model.addAttribute("status", status);
        model.addAttribute("statuslist", statuslist);

        return "/order/detail/order-detail";
    }
    /**
     * 
     * cancelOrd:(取消订单). <br/>  
     * 
     * @param orderId
     * @param oper
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/myorder/cancel")
    @ResponseBody
    public EcpBaseResponseVO cancelOrd(@RequestParam(value="orderId")String orderId,@RequestParam(value="oper")String oper){
    	ROrdCartsChkResponse resp = new ROrdCartsChkResponse();
        EcpBaseResponseVO ecpResp = new EcpBaseResponseVO(); 
        try {
            ROrderDetailsRequest rdor = new ROrderDetailsRequest();
            rdor.setOrderId(orderId);
            rdor.setOper(oper);
            //取消订单来源判断
            rdor.setDelFrom(OrdConstants.DealFrom.FROM_BUYER);
            resp = ordMainRSV.queryOrdOperChk(rdor);
            ecpResp.setResultFlag(resp.isStatus()+"");
            ecpResp.setResultMsg(resp.getMsg());
            if(resp.isStatus()==true){
                ROrderDetailsRequest rdetail = new ROrderDetailsRequest();
                if(StringUtil.isBlank(orderId)){
                    ecpResp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
                    ecpResp.setResultMsg("orderId不能为空");
                    throw new BusinessException("orderId不能为空");
                }
                rdetail.setOrderId(orderId);
                rdetail.setStaffId(rdetail.getStaff().getId());
                ordMainRSV.removeOrd(rdetail);
            }
        } catch (Exception e) {
            ecpResp.setResultFlag("false");
            ecpResp.setResultMsg(e.getMessage());
        }  
    	return ecpResp;
    } 
    
    /**
     * 
     * buyerAddrUpdate:(收货地址编辑弹出窗口). <br/> 
     * 
     * @param model
     * @param id
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/buyeraddrupdate")
    public String buyerAddrUpdate(Model model,@RequestParam(value="orderId")String orderId){
        //根据订单编号，查找地址信息 
        ROrderIdRequest rOrderIdRequest = new ROrderIdRequest(); 
        rOrderIdRequest.setOrderId(orderId);
        ROrdAddressResponse addr = ordMainRSV.queryOrderAddress(rOrderIdRequest);
        model.addAttribute("orderId", orderId);
        model.addAttribute("buyerAddr", addr);
        return "/order/openaddress/buyer-addressupdate";
    }
    
    /**
     * 
     * saveAddr:(收货地址保存). <br/> 
     * 
     * @param custaddr
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/saveaddr")
    @ResponseBody
    public Map<String,Object> saveAddr(@ModelAttribute ROrdAddressReqVO addrReq){
        Map<String, Object> cust = new HashMap<String, Object>();
        LogUtil.info(MODULE, "============== 保存店铺收货地址    开始  =============");
        
        ROrdAddressRequest addrDTO = new ROrdAddressRequest();
        ObjectCopyUtil.copyObjValue(addrReq, addrDTO, null, false);  
        try{
            ordMainRSV.updateOrderAddress(addrDTO);
            cust.put("resultFlag","ok");
        }catch(Exception e){
            LogUtil.error(MODULE, "保存地址出错");
            cust.put("resultFlag","expt");
            cust.put("msg",e.getMessage());
        } 
        return cust;
    }
    
    /**
     * 查询订单物流信息
     * @param orderId
     * @return
     */
    @RequestMapping(value="/queryExpress")
    @ResponseBody
    public List<ROrdExpressDetailsResp> queryOrderExpress(String orderId){
    	 List<ROrdExpressDetailsResp> list = null;
    	 try{
    		 list = orderExpressRSV.queryOrderExpressDetailList(orderId);
         }catch(Exception e){
             LogUtil.error(MODULE, "业务异常");
         }
    	 return list;
    }
    
}
