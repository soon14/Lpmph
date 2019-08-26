package com.ai.ecp.pmph.busi.staff.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.ai.ecp.pmph.busi.staff.vo.CardDistributeSelVO;
import com.ai.ecp.pmph.busi.staff.vo.CardDistributeVO;
import com.ai.ecp.pmph.dubbo.dto.CardDistributeReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardDistributeResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICarderInfoRSV;
import com.ai.ecp.pmph.dubbo.interfaces.ICustCardDistributeRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.FileImportReqDTO;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

@Controller
@RequestMapping(value="/custcardsend")
public class CustCardSendController extends EcpBaseController {

    private static String MODULE = CustCardSendController.class.getName();
    
    private static String URL = "/staff/cardsend";
    
    @Resource
    private ICustCardDistributeRSV custCardDistributeRSV;
    
    @Resource
    private ICarderInfoRSV carderInfoRSV;
    
    /**
     * 
     * cardsendmain:(发卡管理主菜单入口). <br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/main")
    public String cardsendmain(Model model,@RequestParam(required = false) String tab)
    {
        CardInformationReqDTO carderParamDto = new CardInformationReqDTO();
        carderParamDto.setPageNo(0);

        PageResponseDTO<CardInformationResDTO>  pageResponseDTO = carderInfoRSV.listPageInfo(carderParamDto);
        List<CardInformationResDTO> carderList = pageResponseDTO.getResult();
        //控制页签跳转:默认跳到第一页
        if (StringUtil.isNotBlank(tab)) {
            model.addAttribute("tab", tab);
        } else {
            model.addAttribute("tab", 1);
        }
        model.addAttribute("carderList", carderList);
        return URL+"/cardsend-main";
    }
    
    @RequestMapping(value="/grid")
    public String grid(Model model)
    {
        CardInformationReqDTO carderParamDto = new CardInformationReqDTO();
        carderParamDto.setPageNo(0);

        PageResponseDTO<CardInformationResDTO>  pageResponseDTO = carderInfoRSV.listPageInfo(carderParamDto);
        List<CardInformationResDTO> carderList = pageResponseDTO.getResult();
//        Map<Long, String> carderMap =  carderInfoRSV.listCarderInfoMap();
        
        model.addAttribute("carderList", carderList);
        return URL+"/cardsend-list";
    }
    
    @RequestMapping(value="/griddata")
    @ResponseBody
    public Model cardGridList(Model model, EcpBasePageReqVO vo, @Valid CardDistributeSelVO paramVO) throws Exception
    {
        CardDistributeReqDTO paramDto = vo.toBaseInfo(CardDistributeReqDTO.class);
        ObjectCopyUtil.copyObjValue(paramVO, paramDto, null, false);
        
        PageResponseDTO<CardDistributeResDTO>  pageResponseDTO = custCardDistributeRSV.listPageInfo(paramDto);
        
        EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(pageResponseDTO);
        
        return super.addPageToModel(model, pageinfo);
    }
    @RequestMapping(value="/edit")
    public String edit(Model model, @RequestParam("recordId") String paramId, @RequestParam("recordDisId") String paramDisId)
    {
        
        //填充数据
        CardDistributeReqDTO _paramReqDTO = new CardDistributeReqDTO();
        _paramReqDTO.setId(Long.valueOf(paramId));
        _paramReqDTO.setDisId(Long.valueOf(paramDisId));
        
        CardDistributeResDTO resultResDTO = custCardDistributeRSV.findById(_paramReqDTO);
        //
        CardInformationReqDTO carderParamDto = new CardInformationReqDTO();
        carderParamDto.setPageNo(0);
        PageResponseDTO<CardInformationResDTO>  pageResponseDTO = carderInfoRSV.listPageInfo(carderParamDto);
        List<CardInformationResDTO> carderList = pageResponseDTO.getResult();
//        Map<Long, String> carderMap =  carderInfoRSV.listCarderInfoMap();
        
        model.addAttribute("infodto", resultResDTO);
        model.addAttribute("carderList", carderList);
        
        return URL + "/cardsend-edit";
    }
    @RequestMapping(value="/add")
    public String add(Model model)
    {    
        CardInformationReqDTO carderParamDto = new CardInformationReqDTO();
        carderParamDto.setPageNo(0);
        carderParamDto.setStatus("1");//有效的发卡人

        PageResponseDTO<CardInformationResDTO>  pageResponseDTO = carderInfoRSV.listPageInfo(carderParamDto);
        List<CardInformationResDTO> carderList = pageResponseDTO.getResult();
        
        model.addAttribute("infodto", null);
        model.addAttribute("carderList", carderList);

        return URL + "/cardsend-edit";
    }
    @RequestMapping(value="/save")
    @ResponseBody
    public EcpBaseResponseVO saveUpdate(@Valid @ModelAttribute CardDistributeVO paramVO)
    {
        
        CardDistributeReqDTO paramReqDTO = new CardDistributeReqDTO();
        ObjectCopyUtil.copyObjValue(paramVO, paramReqDTO, null, false);
        
        if(paramVO.getId() == null)
        {
            custCardDistributeRSV.insert(paramReqDTO);
        }
        else {
            custCardDistributeRSV.update(paramReqDTO);
        }
        
        EcpBaseResponseVO resultVo = new EcpBaseResponseVO();
        
        resultVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);

        return resultVo;
    }
    
    /**
     * 
     * importPage:(导入excel文件). <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/importpage")
    public String importPage(){
        return "/staff/cardsend/importdata/cardsend-record-import";
    }    
   
    @RequestMapping(value="/importdata")
    @ResponseBody
    public String importData(@RequestParam(value = "fileId", required = true) String fileId,
            Model model,HttpServletRequest request, HttpServletResponse response){
        if(StringUtil.isBlank(fileId)){
            LogUtil.info(MODULE, "发卡信息记录导入文件不存在");
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{ "文件不存在" });
        }
        String result = "导入成功！";
        FileImportReqDTO excelReqDto = new FileImportReqDTO();
        excelReqDto.setFileId(fileId);
        excelReqDto.setFileName(FileUtil.getFileName(fileId));
        EcpBaseResponseVO ebResVO = new EcpBaseResponseVO();
        try {
            custCardDistributeRSV.importExcelData(excelReqDto);
        } catch (BusinessException e) {
            ebResVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            ebResVO.setResultMsg(e.getMessage());
            result = e.getMessage();
            LogUtil.error(MODULE, e.getErrorMessage(), e);
        }
        return result;
    }
}

