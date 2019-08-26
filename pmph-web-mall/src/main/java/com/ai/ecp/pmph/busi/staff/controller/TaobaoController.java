/** 
 * File Name:DemoController.java 
 * Date:2015-8-5下午2:51:38 
 * 
 */ 
package com.ai.ecp.pmph.busi.staff.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.staff.buyer.vo.CustThirdCodeVO;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdTmMainRSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.ICustThirdCodeRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ObjectCopyUtil;


/**
 * 
 * Project Name:ecp-web-mall <br>
 * Description: <br>
 * Date:2016年2月24日上午10:16:11  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value="/taobao")
public class TaobaoController extends EcpBaseController {
    
    private static String MODULE = TaobaoController.class.getName();
    
    @Resource
    private ICustThirdCodeRSV custThirdCodeRSV;
    
    @Resource
    private IOrdTmMainRSV iOrdTmMainRSV;
    
    @Resource
    private ICustManageRSV custManageRSV;

    
    @RequestMapping(value="/index")
    public String index(Model model){
        CustThirdCodeReqDTO req = new CustThirdCodeReqDTO();
        req.setStaffId(req.getStaff().getId());
        req.setThirdCodeType(StaffConstants.custInfo.CUST_THIRD_CODE_TYPE_TMALL);
        CustThirdCodeResDTO res = custThirdCodeRSV.queryThirdCode(req);
        if(res!=null){
          model.addAttribute("taobaocode", res.getThirdCode());
        }
        return "staff/buyer/thirdcode/member-card-taobao";
    } 
 
    @RequestMapping(value="/savethirdcode")   
    @ResponseBody
    public EcpBaseResponseVO saveThirdCode(Model model,@Valid CustThirdCodeVO custThirdCodeVO)throws BusinessException{
        EcpBaseResponseVO res = new EcpBaseResponseVO();
        //先验证订单号
        ROrdTmMainReq ordTmMainReq = new ROrdTmMainReq();
        ordTmMainReq.setOrderCode(custThirdCodeVO.getOrderCode());
        ordTmMainReq.setTmStaffCode(custThirdCodeVO.getThirdCode());
        boolean flag  = iOrdTmMainRSV.validOrderTmMain(ordTmMainReq);
        if(!flag){
            //不通过，抛出异常
            res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION); 
            return res;
        }
        CustThirdCodeReqDTO req = new CustThirdCodeReqDTO();
        ObjectCopyUtil.copyObjValue(custThirdCodeVO, req, null, false);
        req.setThirdCodeType(StaffConstants.custInfo.CUST_THIRD_CODE_TYPE_TMALL);
        req.setStaffId(req.getStaff().getId());
        req.setCreateTime(DateUtil.getSysDate());
        custThirdCodeRSV.saveCustThirdCode(req);
        
        //绑定成功后修改订单数据
        CustInfoResDTO cust = custManageRSV.findCustInfoById(req.getStaff().getId());
        
        ordTmMainReq.setRwStaffId(req.getStaff().getId());
        ordTmMainReq.setRwStaffCode(cust.getStaffCode());
        ordTmMainReq.setOrderCode(custThirdCodeVO.getOrderCode());
        iOrdTmMainRSV.updateOrderTmMainStaff(ordTmMainReq);
        
        res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        return res;
    }
 
}


