package com.ai.ecp.pmph.busi.staff.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

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
import com.ai.ecp.pmph.busi.staff.vo.CarderInfoVO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICarderInfoRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;

@Controller
@RequestMapping(value="/carderinfo")
public class CarderInfoController  extends EcpBaseController{

    private static String MODULE = CustCardSendController.class.getName();
    
    private static String URL = "/staff/cardsend";
    
    @Resource
    private ICarderInfoRSV carderInfoRSV;
    
    @RequestMapping(value="/grid")
    public String grid(Model model)
    {
        return URL+"/carderinfo-list";
    }
    
    @RequestMapping(value="/griddata")
    @ResponseBody
    public Model cardGridList(Model model, EcpBasePageReqVO vo, @Valid CarderInfoVO paramVO) throws Exception
    {
        CardInformationReqDTO paramDto = vo.toBaseInfo(CardInformationReqDTO.class);
        ObjectCopyUtil.copyObjValue(paramVO, paramDto, null, false);
        
        PageResponseDTO<CardInformationResDTO>  pageResponseDTO = carderInfoRSV.listPageInfo(paramDto);
        
        EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(pageResponseDTO);
        
        return super.addPageToModel(model, pageinfo);
    }
    @RequestMapping(value="/edit")
    public String edit(Model model, @RequestParam("id") String paramId)
    {
        
        //填充数据
        CardInformationReqDTO _paramReqDTO = new CardInformationReqDTO();
        _paramReqDTO.setId(Long.valueOf(paramId));
        
        CardInformationResDTO resultResDTO = carderInfoRSV.findById(_paramReqDTO);
        
        model.addAttribute("infodto", resultResDTO);
        
        return URL + "/carderinfo-edit";
    }
    @RequestMapping(value="/add")
    public String add(Model model)
    {            
        model.addAttribute("infodto", null);

        return URL + "/carderinfo-edit";
    }
    @RequestMapping(value="/save")
    @ResponseBody
    public EcpBaseResponseVO saveUpdate(@Valid @ModelAttribute CarderInfoVO paramVO)
    {
        
        CardInformationReqDTO paramReqDTO = new CardInformationReqDTO();
        ObjectCopyUtil.copyObjValue(paramVO, paramReqDTO, null, false);
        
        EcpBaseResponseVO resultVo = new EcpBaseResponseVO();
        
        if(paramVO.getId() == null)
        {
            try {
				carderInfoRSV.insert(paramReqDTO);
			} catch (BusinessException e) {
				resultVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
				resultVo.setResultMsg(e.getMessage());
				return resultVo;
			}
        }
        else {
            carderInfoRSV.update(paramReqDTO);
        }
        
        resultVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);

        return resultVo;
    }

    @RequestMapping(value="/valid")
    @ResponseBody
    public EcpBaseResponseVO valid(@RequestParam("id")String id) throws Exception{
        LogUtil.info(MODULE, "======== 发卡人失效  开始   =======");
        CardInformationReqDTO req = new CardInformationReqDTO();
        req.setId(Long.parseLong(id));
        
        carderInfoRSV.deleteAtive(req);
        
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        
        LogUtil.info(MODULE, "======== 发卡人失效   结束   =======");
        return vo;
    }
    @RequestMapping(value="/active")
    @ResponseBody
    public EcpBaseResponseVO active(@RequestParam("id")String id) throws Exception{
        LogUtil.info(MODULE, "======== 发卡人生效  开始   =======");
        
        CardInformationReqDTO req = new CardInformationReqDTO();
        req.setId(Long.parseLong(id));

        carderInfoRSV.updateToAtive(req);
        
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        
        LogUtil.info(MODULE, "======== 发卡人生效  结束   =======");
        return vo;
    }
}

