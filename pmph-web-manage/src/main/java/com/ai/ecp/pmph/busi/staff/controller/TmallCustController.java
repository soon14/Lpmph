/** 
 * File Name:DemoController.java 
 * Date:2015-8-5下午2:51:38 
 * 
 */
package com.ai.ecp.pmph.busi.staff.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.staff.vo.CustInfoListVO;
import com.ai.ecp.busi.staff.vo.CustInfoVO;
import com.ai.ecp.busi.staff.vo.CustThirdCodeVO;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdTmMainRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.staff.dubbo.dto.AuthStaffReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.ICustThirdCodeRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.ResourceMsgUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * Project Name:ecp-web-demo <br>
 * Description: <br>
 * Date:2015-8-5下午2:51:38 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "/custtm")
public class TmallCustController extends EcpBaseController {

    private static String MODULE = TmallCustController.class.getName();

    @Resource
    private ICustManageRSV custManageRSV;
    
    @Resource
    private ICustThirdCodeRSV custThirdCodeRSV;
    
    @Resource
    private IOrdTmMainRSV iOrdTmMainRSV;

    
    @RequestMapping("/bindthirdcode")
    @ResponseBody
    public EcpBaseResponseVO bindThirdCode(@Valid CustThirdCodeVO custThirdCodeVO)throws Exception{
        EcpBaseResponseVO result = new EcpBaseResponseVO();
        //先验证订单号
        ROrdTmMainReq ordTmMainReq = new ROrdTmMainReq();
        ordTmMainReq.setOrderCode(custThirdCodeVO.getOrderCode());
        ordTmMainReq.setTmStaffCode(custThirdCodeVO.getThirdCode());
        boolean flag  = iOrdTmMainRSV.validOrderTmMain(ordTmMainReq);
        if(!flag){
            //不通过，抛出异常
            result.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION); 
            result.setResultMsg("对不起，此会员名、订单号无效或已被绑定");
            return result;
        }
        
        CustThirdCodeReqDTO custThirdCodeReqDTO = new CustThirdCodeReqDTO();
        ObjectCopyUtil.copyObjValue(custThirdCodeVO, custThirdCodeReqDTO, null, false);
        custThirdCodeReqDTO.setThirdCodeType(StaffConstants.custInfo.CUST_THIRD_CODE_TYPE_TMALL);
        custThirdCodeRSV.saveCustThirdCode(custThirdCodeReqDTO);
        result.setResultMsg(ResourceMsgUtil.getMessage("web.staff.101001", null));
        result.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        //绑定成功
        CustInfoResDTO cust = custManageRSV.findCustInfoById(custThirdCodeVO.getStaffId());
        
        ordTmMainReq.setRwStaffId(custThirdCodeVO.getStaffId());
        ordTmMainReq.setRwStaffCode(cust.getStaffCode());
        ordTmMainReq.setOrderCode(custThirdCodeVO.getOrderCode());
        iOrdTmMainRSV.updateOrderTmMainStaff(ordTmMainReq);
        return result;
    }
    
    @RequestMapping("/bindpage")
    public String bindCodePage(Model model)throws Exception{
        
        return "/staff/custmanage/remark/bindcode";
    }
    
}
