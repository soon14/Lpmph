package com.ai.ecp.pmph.busi.pub.controller;


import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.order.dubbo.dto.PubOrderZdRequsetDTO;
import com.ai.ecp.order.dubbo.dto.PubOrderZdResponseDTO;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.SPubOrderZdResponseDTO;
import com.ai.ecp.order.dubbo.interfaces.IPubOrderZdRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.dubbo.util.PubOrdConstants;
import com.ai.ecp.pmph.busi.pub.vo.PubAllOrdVO;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.BaseStaff;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.PubUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.IPubRSV;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ObjectCopyUtil;

@Controller
@RequestMapping(value = "/pubord/send")
public class PubSendOrdController extends EcpBaseController {

	@Resource
    private IPubRSV pubRSV;
	
	@Resource
	private IPubOrderZdRSV pubOrderZdRSV;

    @RequestMapping()
    public String init(Model model,PubAllOrdVO vo) throws Exception {
    	// 后场服务所需得DTO
    	PubOrderZdRequsetDTO rdor = vo.toBaseInfo(PubOrderZdRequsetDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, rdor, "", false);
    	String status = PubOrdConstants.CustomerRequestStatus.REQUEST_STATUS_SEND;
        rdor.setStaffId(rdor.getStaff().getId());
        rdor.setSiteId(1l);
        rdor.setSysType("00");
        // 征订单查询状态 
        rdor.setStatus(status);
        if(rdor.getBegDate() == null) {
        	rdor.setBegDate(new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        }else {
        	rdor.setBegDate(new Timestamp(rdor.getBegDate().getTime()));
        }
        if(rdor.getEndDate() == null) {
        	rdor.setEndDate(new Timestamp(DateUtils.addDays(new Date(), 1).getTime()));
        }else {
        	rdor.setEndDate(new Timestamp(DateUtils.addDays(rdor.getEndDate(), 1).getTime()));
        }
        /*rdor.setBegDate(new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        rdor.setEndDate(new Timestamp(new Date().getTime()));
        ObjectCopyUtil.copyObjValue(vo, rdor, "", false);*/
        
        // 查询待支付得征订单信息
        PubOrderZdRequsetDTO pubReq = new PubOrderZdRequsetDTO();
        pubReq.setStaffId(rdor.getStaffId());
        pubReq.setStatus(PubOrdConstants.CustomerRequestStatus.REQUEST_STATUS_PAY);
        SPubOrderZdResponseDTO sPubOrder = pubOrderZdRSV.sumPayPubOrderByStaffId(pubReq);
        
        PageResponseDTO<PubOrderZdResponseDTO> rdors = pubOrderZdRSV.queryPubOrderByStaffId(rdor);
        if(rdors==null){
            rdors = new PageResponseDTO<PubOrderZdResponseDTO>();
            rdors.setPageSize(1);
        }
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        model.addAttribute("status", status);
        model.addAttribute("sPubOrder", sPubOrder);
        model.addAttribute("resp",rdors);
        model.addAttribute("begDate", vo.getBegDate());
        model.addAttribute("endDate", vo.getEndDate());
        return "/pub/send-list";
    }
}
