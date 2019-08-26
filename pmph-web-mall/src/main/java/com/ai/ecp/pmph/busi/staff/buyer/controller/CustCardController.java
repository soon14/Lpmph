package com.ai.ecp.pmph.busi.staff.buyer.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.staff.buyer.vo.CustCardApplyVO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICardApplicationRSV;
import com.ai.ecp.pmph.dubbo.interfaces.ICardMgrRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustInfoRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
/**
 * 
 * Project Name:ecp-web-mall <br>
 * Description: <br>
 * Date:2015-10-30下午7:22:11  <br>
 * 
 * @version  
 * @since JDK 1.6
 * 
 * 会员卡管理
 */
@Controller
@RequestMapping(value="/custcard")
public class CustCardController  extends EcpBaseController{
    public static String MODULE = CustCardController.class.getName();
    public static String URL = "/pmph/staff/buyer/custinformation";
    
    @Resource
    private ICustInfoRSV custInfoRSV;
    
    @Resource
    private ICardApplicationRSV cardApplicationRSV;
    
    @Resource
    private ICardMgrRSV cardMgrRSV;
    
    
    @RequestMapping(value="/index")
    public String index(Model model)
    {
        CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
        custInfoReqDTO.setId(custInfoReqDTO.getStaff().getId());
//        custInfoReqDTO.setId(808L);
        
        CustInfoResDTO custInfoResDTO = custInfoRSV.getCustInfoById(custInfoReqDTO);
        
        CardBindReqDTO cardBindReqDTO = new CardBindReqDTO();
        cardBindReqDTO.setStaffId(cardBindReqDTO.getStaff().getId());
        cardBindReqDTO.setPageNo(1);
        cardBindReqDTO.setPageSize(10);
        
        PageResponseDTO<CardBindResDTO> cardbindPage = cardMgrRSV.listCardBindPageByStaff(cardBindReqDTO);
        List<CardBindResDTO> cardbinglist = cardbindPage.getResult();
        
        CardApplicationReqDTO cardApplicationReqDTO = new CardApplicationReqDTO();
        cardApplicationReqDTO.setStaffId(cardApplicationReqDTO.getStaff().getId());
        cardApplicationReqDTO.setPageNo(1);
        cardApplicationReqDTO.setPageSize(5);   
        
        PageResponseDTO<CardApplicationResDTO> cardapplicationPage = cardApplicationRSV.listCardApplication(cardApplicationReqDTO);
        List<CardApplicationResDTO> cardapplicationlist = cardapplicationPage.getResult();
        
        model.addAttribute("custinfo", custInfoResDTO);
        model.addAttribute("cardbinglist", cardbinglist);
        model.addAttribute("cardapplicationlist", cardapplicationlist);


        return URL+"/custcard";
    }

    @RequestMapping(value="saveapply")
    @ResponseBody
    public EcpBaseResponseVO saveCardApplyRequest(Model model, @Valid CustCardApplyVO custcardVO)
    {
        CardApplicationReqDTO cardAppReqDTO = new CardApplicationReqDTO();
        //只能申请同等级的会员卡
        if (!cardAppReqDTO.getStaff().getStaffLevelCode().equals(custcardVO.getCustLevelCode())) {
            throw new BusinessException("对不起，只能申请同等级的会员卡！");
        }
        cardAppReqDTO.setCustLevelCode(custcardVO.getCustLevelCode());
        cardAppReqDTO.setContactName(custcardVO.getContactName());
        cardAppReqDTO.setContactPhone(custcardVO.getContactPhone());
        cardAppReqDTO.setContactAddress(custcardVO.getContactAddress());
        cardAppReqDTO.setRemark(custcardVO.getRemark());
        cardAppReqDTO.setStaffId(cardAppReqDTO.getStaff().getId());
        
        cardApplicationRSV.applyCustCardOnLine(cardAppReqDTO);
        
        EcpBaseResponseVO responseVO = new EcpBaseResponseVO();
        responseVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        
        return responseVO;
    }
    @RequestMapping(value="bindcard")
    @ResponseBody
    public EcpBaseResponseVO saveCardBindRequest(Model model, @Valid @RequestParam(value="cardid")String cardid)
    {

        CardBindReqDTO cardBindReqDTO = new CardBindReqDTO();
        cardBindReqDTO.setBindType(StaffConstants.Card.BIND_TYPE_0);
        cardBindReqDTO.setCustCardId(cardid);
        cardBindReqDTO.setStaffId(cardBindReqDTO.getStaff().getId());
        cardBindReqDTO.setCreateStaff(cardBindReqDTO.getStaff().getId());
        cardBindReqDTO.setUpdateStaff(cardBindReqDTO.getStaff().getId());
        
        cardMgrRSV.saveCardBindOpt(cardBindReqDTO);
        
        EcpBaseResponseVO responseVO = new EcpBaseResponseVO();
        responseVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        responseVO.setResultMsg("恭喜！成功绑定会员卡："+cardid);
        
        return responseVO;
    }
}

