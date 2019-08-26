package com.ai.ecp.busi.order.controller;

import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.server.front.util.SiteLocaleUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.busi.order.vo.RQueryOrderReqVO;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdPmphMainRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;

@Controller
@RequestMapping(value="/order/send")
public class SendOrdController extends EcpBaseController {

    private static final String MODULE = SendOrdController.class.getName();
    
    @Resource
    private IOrdPmphMainRSV ordPmphMainRSV;
    
    @RequestMapping()
    public String init(Model model,RQueryOrderReqVO vo) throws Exception{

        LogUtil.debug(MODULE, vo.toString());
        //后场服务所需要的DTO；
        RQueryOrderRequest rdor = vo.toBaseInfo(RQueryOrderRequest.class);

        String status = OrdConstants.CustomerRequestStatus.REQUEST_STATUS_SEND;
        rdor.setStaffId(rdor.getStaff().getId());
        rdor.setSiteId(SiteLocaleUtil.getSite());
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
        return "/order/send-list";
    }
    
}

