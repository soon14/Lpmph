package com.ai.ecp.busi.order.controller;

import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.vo.RQueryOrderReqVO;
import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.ROrdReceiptRequest;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdReceiptRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdPmphMainRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

@Controller
@RequestMapping(value="/order/recept")
public class ReceptOrdController extends EcpBaseController {

    private static final String MODULE = ReceptOrdController.class.getName();
    
    @Resource 
    private IOrdMainRSV ordMainRSV;
    
    @Resource
    private IOrdReceiptRSV ordReceiptRSV;
    
    @Resource
    private IOrdPmphMainRSV ordPmphMainRSV;   
    
    @RequestMapping()
    public String init(Model model,RQueryOrderReqVO vo) throws Exception{
        //后场服务所需要的DTO；
        LogUtil.debug(MODULE, vo.toString());
        //后场服务所需要的DTO；
        RQueryOrderRequest rdor = vo.toBaseInfo(RQueryOrderRequest.class);

        String status = OrdConstants.CustomerRequestStatus.REQUEST_STATUS_RECEPT;
        //rdor.setStaffId(ParamsTool.getStaffId());
        rdor.setStaffId(rdor.getStaff().getId());
        rdor.setSiteId(1l);
        rdor.setSysType("00");
        rdor.setStatus(status); // 
        
        ObjectCopyUtil.copyObjValue(vo, rdor, "", false);
        PageResponseDTO<RCustomerOrdResponse> rdors = ordPmphMainRSV.queryOrderByStaffId(rdor);
        if(rdors==null){
            rdors = new PageResponseDTO<RCustomerOrdResponse>();
            rdors.setPageSize(1);
        }
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        model.addAttribute("status", status);
        //返回时间
        Map<String,Timestamp> dates = ParamsTool.params(vo);
        model.addAllAttributes(dates);
        model.addAttribute("resp",rdors);
        return "/order/recept-list";
    }
    
    //买家确认收货
    @RequestMapping(value="/confirmord")
    @ResponseBody
    public EcpBaseResponseVO  confirmOrd(@RequestParam(value="orderId")String orderId,@RequestParam(value="oper")String oper){
        ROrdCartsChkResponse resp = new ROrdCartsChkResponse();
        EcpBaseResponseVO ecpResp = new EcpBaseResponseVO();
        ROrdReceiptRequest rdto = new ROrdReceiptRequest();
        try {
            ROrderDetailsRequest rdor = new ROrderDetailsRequest();
            rdor.setOrderId(orderId);
            rdor.setOper(oper);
            resp = ordMainRSV.queryOrdOperChk(rdor); 
            ecpResp.setResultFlag(resp.isStatus()+"");
            ecpResp.setResultMsg(resp.getMsg());
            if(resp.isStatus()==true){
                if(StringUtil.isBlank(orderId)){
                    throw new BusinessException("order.null.error");
                }
                rdto.setOrderId(orderId); 
                rdto.setStaffId(rdto.getStaff().getId()); 
                ordReceiptRSV.orderReceipt(rdto);
            }
        } catch (Exception e) {
            ecpResp.setResultFlag("false");
            ecpResp.setResultMsg(e.getMessage());
        } 
        return ecpResp;
    } 
}

