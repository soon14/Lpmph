package com.ai.ecp.busi.goods.controller;

import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.goods.vo.GdsVerifyVO;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.dto.GdsVerifyReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsVerifyRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoManageRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

@RequestMapping(value="/gdsverify")
@Controller
public class GdsVerifyController extends EcpBaseController{
    private String MODULE = GdsVerifyController.class.getName();
    private String URL = "/goods/gdsVerify";
    
    @Resource
    private IGdsInfoQueryRSV iGdsInfoQueryRSV;
    @Resource
    private IGdsInfoManageRSV iGdsInfoManageRSV;
    @Resource
    private IShopInfoRSV iShopInfoRSV;
    
    @RequestMapping()
    public String init(Model model,GdsVerifyVO gdsVerifyVO){
        model.addAttribute("shopId", gdsVerifyVO.getShopId());
        return URL+"/gds-verify-grid";
    }
    
    /**
     * 
     * queryGdsVerifyInfoList:(获取商品操作审核列表). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/gridlist")
    public Model gridList(Model model,GdsVerifyVO gdsVerifyVO){
        GdsVerifyReqDTO dto = gdsVerifyVO.toBaseInfo(GdsVerifyReqDTO.class);
       
        EcpBasePageRespVO<Map> respVO = null;
        try {
            ObjectCopyUtil.copyObjValue(gdsVerifyVO, dto, null, false);
            if(StringUtil.isNotEmpty(gdsVerifyVO.getStartTime())){
                dto.setBegCreateTime(new Timestamp(gdsVerifyVO.getStartTime().getTime()));
            }
            if(StringUtil.isNotEmpty(gdsVerifyVO.getEndTime())){
                dto.setEndCreateTime(new Timestamp(gdsVerifyVO.getEndTime().getTime()));
            }

//        		dto.setStatus(GdsConstants.Commons.STATUS_VALID);
            
            
            PageResponseDTO<GdsVerifyRespDTO> pageInfo = iGdsInfoQueryRSV.queryGdsVerifyInfoPage(dto);
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(pageInfo);
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "获取商品审核列表失败！",e);
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取商品审核列表失败！",e);
        }
        return super.addPageToModel(model, respVO);
    }
    
    /**
     * 
     * verifyGds:(对商品操作进行审核). <br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param gdsVerifyVO
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/verifygds")
    @ResponseBody
    public EcpBaseResponseVO verifyGds(GdsVerifyVO gdsVerifyVO){
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        GdsVerifyReqDTO gdsVerifyReqDTO = new GdsVerifyReqDTO();
        ObjectCopyUtil.copyObjValue(gdsVerifyVO, gdsVerifyReqDTO, null, false);
        if(GdsConstants.GdsVerify.VERIFY_APPROVED.equals(gdsVerifyReqDTO.getVerifyStatus())){
            // 如果为删除操作，获取企业编码
            ShopInfoResDTO shopInfo = iShopInfoRSV.findShopInfoByShopID(gdsVerifyReqDTO.getShopId());
            Long companyId = null;
            if (StringUtil.isNotEmpty(shopInfo)) {
                companyId = shopInfo.getCompanyId();
            } else {
                throw new BusinessException("web.gds.2000012");
            }
            gdsVerifyReqDTO.setCompanyId(companyId);
        }
        try {
            Long[] ids = new Long[]{gdsVerifyVO.getGdsId()};
            gdsVerifyReqDTO.setIds(ids);
            iGdsInfoManageRSV.editGdsVerify(gdsVerifyReqDTO);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            vo.setResultMsg("审核成功！");
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "商品审核失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            vo.setResultMsg("审核失败！");
        } catch (Exception e) {
            LogUtil.error(MODULE, "商品审核失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            vo.setResultMsg("审核失败！");
        }
        return vo;
    }
    
    /**
     * 
     * toGdsVerifyVindow:(跳转到批量审核的审核窗口). <br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/togdsverifywindow")
    public String toGdsVerifyVindow(){
        return URL+"/list/gds-verify-window";
    }
    
    /**
     * 
     * verifyGds:(对商品操作进行审核). <br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param gdsVerifyVO
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/batchverifygds")
    @ResponseBody
    public EcpBaseResponseVO batchVerifyGds(GdsVerifyVO gdsVerifyVO){
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        GdsVerifyReqDTO gdsVerifyReqDTO = new GdsVerifyReqDTO();
        ObjectCopyUtil.copyObjValue(gdsVerifyVO, gdsVerifyReqDTO, null, false);
        if(GdsConstants.GdsVerify.VERIFY_APPROVED.equals(gdsVerifyReqDTO.getVerifyStatus())){
            // 如果为删除操作，获取企业编码
            ShopInfoResDTO shopInfo = iShopInfoRSV.findShopInfoByShopID(gdsVerifyReqDTO.getShopId());
            Long companyId = null;
            if (StringUtil.isNotEmpty(shopInfo)) {
                companyId = shopInfo.getCompanyId();
            } else {
                throw new BusinessException("web.gds.2000012");
            }
            gdsVerifyReqDTO.setCompanyId(companyId);
        }
        try {
            String[] idList = gdsVerifyVO.getOperateId().split(",");
            Long[] ids = new Long[idList.length];
            if (ids.length > 0) {
                for (int i = 0; i < idList.length; i++) {
                    if (StringUtil.isNotBlank(idList[i])) {
                        ids[i] = Long.parseLong(idList[i]);
                    }
                }
            }
            gdsVerifyReqDTO.setIds(ids);
            iGdsInfoManageRSV.editGdsVerify(gdsVerifyReqDTO);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            vo.setResultMsg("审核成功！");
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "商品审核失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            vo.setResultMsg("审核失败！");
        } catch (Exception e) {
            LogUtil.error(MODULE, "商品审核失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            vo.setResultMsg("审核失败！");
        }
        return vo;
    }
}

