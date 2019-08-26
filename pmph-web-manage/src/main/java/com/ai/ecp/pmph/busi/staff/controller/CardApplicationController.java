package com.ai.ecp.pmph.busi.staff.controller;



import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.staff.vo.CardApplicationVO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICardApplicationRSV;
import com.ai.ecp.pmph.dubbo.interfaces.ICardMgrRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ResourceMsgUtil;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:ecp-web-manage Maven Webapp <br>
 * Description: 会员卡申请<br>
 * Date:2015-10-27下午5:09:34  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value="/cardapplication")
public class CardApplicationController extends EcpBaseController {
    
    private static final String MODULE = CardApplicationController.class.getName();
    
    @Resource
    private ICardApplicationRSV cardApplicationRSV;
    
    @Resource
    private ICardMgrRSV cardMgrRsv;
    
    @Resource
    private ICustManageRSV custMgrRSV;
    /**
     * 
     * grid:(跳转到会员卡待审核列表). <br/> 
     * 
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value="/grid")
    public String grid(Model model){
        return "/staff/cardapplication/cardapplication-grid";
    }
    /**
     * 
     * gridList:(查询会员卡申请待审核列表). <br/> 
     * 
     * @param model
     * @param vo
     * @param cardApp
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping("/gridlist")
    @ResponseBody
    public Model gridList(Model model, EcpBasePageReqVO vo, @ModelAttribute CardApplicationVO cardApp) throws Exception {
        CardApplicationReqDTO req = vo.toBaseInfo(CardApplicationReqDTO.class);
        req.setCustLevelCode(cardApp.getCustLevelCode());//会员等级
        //查询开始时间
        if (StringUtil.isNotBlank(cardApp.getSelTimeFrom())) {
            req.setSelTimeFrom(DateUtil.getTimestamp(cardApp.getSelTimeFrom() + " 00:00:00","yyyy-MM-dd HH:mm:ss"));
        }
        //查询结束时间
        if (StringUtil.isNotBlank(cardApp.getSelTimeEnd())) {
            req.setSelTimeEnd(DateUtil.getTimestamp(cardApp.getSelTimeEnd() + " 23:59:59","yyyy-MM-dd HH:mm:ss"));
        }
        //查询状态
        if (StringUtil.isNotBlank(cardApp.getCheckStatus())) {
            req.setCheckStatus(cardApp.getCheckStatus());//待审核
        }
        
        PageResponseDTO<CardApplicationResDTO> pageInfo = cardApplicationRSV.listCardApplication(req);
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(pageInfo);
        
       return super.addPageToModel(model, respVO);

    }
    /**
     * 
     * nopass:(跳转到：会员卡审核不通过页面). <br/> 
     * 
     * @param model
     * @param id
     * @param staffId
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value="/nopass")
    public String nopass(Model model,@RequestParam(value = "id")Long id,@RequestParam(value = "staffId")Long staffId){
        model.addAttribute("id", id);
        model.addAttribute("staffId", staffId);
        return "/staff/cardapplication/check/cardapp-no-pass";
    }
    /**
     * 
     * nopassSave:(保存：会员卡申请审核不通过信息). <br/> 
     * 
     * @param cardVo
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping(value="/nopass/save")
    @ResponseBody
    public EcpBaseResponseVO nopassSave(@ModelAttribute CardApplicationVO cardVo) throws Exception{
        EcpBaseResponseVO result = new EcpBaseResponseVO();
        LogUtil.info(MODULE, "======== 审核不通过方法  开始   =======");
        CardApplicationReqDTO req = new CardApplicationReqDTO();
        req.setId(cardVo.getId());
        req.setRemark(cardVo.getRemark());//审核备注
        req.setStaffId(cardVo.getStaffId());
        req.setCheckStatus(StaffConstants.Card.CHECK_STATUS_2);//审核不通过
        cardApplicationRSV.updateCardApplication(req);
        result.setResultMsg(ResourceMsgUtil.getMessage("web.staff.101001", new String[]{}));
        LogUtil.info(MODULE, "======== 审核不通过方法  结束   =======");

        return result;
    }
    /**
     * 
     * pass:(跳转到会员卡申请审核通过后，绑定会员卡界面). <br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value="/pass")
    public String pass(Model model,@RequestParam("id") Long id,@RequestParam("staffId") String staffId,@RequestParam("custLevelCode") String custLevelCode){
        model.addAttribute("cardAppId", id);
        model.addAttribute("staffId", staffId);
        model.addAttribute("custLevelCode", custLevelCode);
        return "/staff/cardapplication/check/cardapp-pass-bind";
    }
    /**
     * 
     * bindList:(查询会员卡列表). <br/> 
     * 
     * @param model
     * @param vo
     * @param cardApp
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping("/bindlist")
    @ResponseBody
    public Model bindList(Model model, EcpBasePageReqVO vo, @ModelAttribute CardApplicationVO cardVo) throws Exception {
        CardInfoReqDTO req = vo.toBaseInfo(CardInfoReqDTO.class);
        req.setCustLevelCode(cardVo.getCustLevelCode());//会员等级
        req.setCardStatus(StaffConstants.Card.CUST_CARD_NO_SEND);//未发卡
        PageResponseDTO<CardInfoResDTO> pageInfo = cardMgrRsv.listCardInfo(req);
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(pageInfo);
        return super.addPageToModel(model, respVO);

    }
    
    /**
     * 
     * nopassSave:(保存：会员卡申请审核不通过信息). <br/> 
     * 
     * @param cardVo
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping(value="/passbind/save")
    @ResponseBody
    public EcpBaseResponseVO passBindSave(@ModelAttribute CardApplicationVO cardVo) throws Exception{
        EcpBaseResponseVO result = new EcpBaseResponseVO();
        LogUtil.info(MODULE, "======== 审核通过方法  开始   =======");
        if (cardVo.getStaffId() != null && cardVo.getStaffId() != 0L) {
            CustInfoResDTO custRes = custMgrRSV.findCustInfoById(cardVo.getStaffId());
            if (custRes != null && !custRes.getCustLevelCode().equals(cardVo.getCustLevelCode())) {
                result.setResultMsg("用户当前的会员等级与申请的会员卡等级不一致，不能审核通过");
                return result;
            }
        }
        /*设置会员卡申请审核参数*/
        CardApplicationReqDTO req = new CardApplicationReqDTO();
        req.setId(cardVo.getId());//申请记录的id
        req.setRemark("审核通过");//审核备注
        req.setStaffId(cardVo.getStaffId());//申请人id
        req.setCheckStatus(StaffConstants.Card.CHECK_STATUS_1);//审核通过
        req.setUpdateStaff(req.getStaff().getId());//更新人
        req.setCustCardId(cardVo.getCustCardId());
        
        /*设置会员卡绑定所需参数*/
        CardBindReqDTO bindReq = new CardBindReqDTO();
        bindReq.setBindCustLevelCode(cardVo.getCustLevelCode());//申请的会员等级
        bindReq.setCustCardId(cardVo.getCustCardId());//需要绑定的会员卡
        bindReq.setStaffId(cardVo.getStaffId());//申请人id
        bindReq.setCreateStaff(bindReq.getStaff().getId());//创建人
        bindReq.setUpdateStaff(bindReq.getStaff().getId());//更新人
        bindReq.setBindType(StaffConstants.Card.BIND_TYPE_1);
        /*调用审核方法*/
        cardApplicationRSV.saveCardAppPass(req,bindReq);
        result.setResultMsg("审核成功，并已绑定相应等级的会员卡！");
        LogUtil.info(MODULE, "======== 审核通过方法  结束   =======");

        return result;
    }
}

