package com.ai.ecp.pmph.busi.seller.staff.controller;

import java.util.ArrayList;
import java.util.List;
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
import com.ai.ecp.busi.seller.staff.vo.SubAcctVO;
import com.ai.ecp.im.dubbo.dto.ImHotlineInfoReqDTO;
import com.ai.ecp.im.dubbo.dto.ImHotlineInfoResDTO;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineReqDTO;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineResDTO;
import com.ai.ecp.im.dubbo.interfaces.IStaffHotlineRSV;
import com.ai.ecp.pmph.busi.seller.staff.vo.ImHotlineInfoVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ImStaffHotlineVO;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.staff.dubbo.dto.AuthStaffAdminReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustSubInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.SellerResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IAuthAdminRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopSubAuthStaffRSV;
import com.ai.ecp.staff.dubbo.interfaces.IStaffGroupManageRSV;
import com.ai.ecp.system.filter.SellerLocaleUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.ResourceMsgUtil;

/**
 *  
 * Project Name:ecp-web-manage <br>
 * Description: 客服管理<br>
 * Date:2016年11月1日下午8:55:05  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "/seller/custservice")
public class CustServiceController extends EcpBaseController {
	private static String URL = "/seller/staff";
	private static String MODULE = CustServiceController.class.getName();
	@Resource
    private IStaffHotlineRSV staffHotlineRSV; 
    
    @Resource
    private IStaffGroupManageRSV staffGroupManageRSV;
    @Resource
    private IAuthAdminRSV authAdminRSV;
    
    @Resource
    private IShopSubAuthStaffRSV shopSubAuthStaffRSV;

    /**
     * 
     * grid:(客服人员). <br/>  
     * 
     * @param model
     * @param companyId
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/grid")
    public String grid(Model model,@ModelAttribute ImHotlineInfoVO vo) throws Exception {
    		//long s = StaffLocaleUtil.getStaff().getId();
    		//AuthPrivilegeResDTO dtto =WebContextUtil.getCurrentUser();
        	Long shopId = 0l;
        	SellerResDTO srd =  SellerLocaleUtil.getSeller();
        	List<ShopInfoResDTO> shopLst = srd.getShoplist();
            shopId = shopLst.get(0).getId();
            vo.setShopId(shopId);
            ImHotlineInfoReqDTO reqDto = vo.toBaseInfo(ImHotlineInfoReqDTO.class);
            ObjectCopyUtil.copyObjValue(vo, reqDto, null, false); 
            reqDto.setPlatSource("00");//只查本平台的客服
            PageResponseDTO<ImHotlineInfoResDTO> t = staffHotlineRSV.getHotlineList(reqDto);
            //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
            if(t.getResult() == null){
            	t.setResult(new ArrayList<ImHotlineInfoResDTO>());
            }
            EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
            super.addPageToModel(model, respVO);
            model.addAttribute("resp", respVO);
        return URL+"/custservice/custservice-grid";
    }
    /**
     * 
     * add:(新增客服页面). <br/>  
     * 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/add")
    public String add(Model model){
        return URL+"/custservice/custservice-add";
    }
    
    /**
     * 
     * gridList:(客服人员列表). <br/>  
     * 
     * @param model
     * @param vo
     * @param custInfoList
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping("/gridlist")
    public String gridList(Model model, @ModelAttribute ImHotlineInfoVO vo,@RequestParam("ids")Long ids) throws Exception {
	        //long s = StaffLocaleUtil.getStaff().getId();
	        //AuthPrivilegeResDTO dtto =WebContextUtil.getCurrentUser();
        	Long shopId = 0l;
        	SellerResDTO srd =  SellerLocaleUtil.getSeller();
        	if(srd!=null){
    			List<ShopInfoResDTO> shopLst = srd.getShoplist();
        		shopId = shopLst.get(0).getId();
        		vo.setShopId(shopId);
        	}
        	if(null!=ids){
        		vo.setShopId(ids);
        	}
            ImHotlineInfoReqDTO reqDto = vo.toBaseInfo(ImHotlineInfoReqDTO.class);
            ObjectCopyUtil.copyObjValue(vo, reqDto, null, false); 
            reqDto.setPlatSource("00");//只查本平台的客服
            PageResponseDTO<ImHotlineInfoResDTO> t = staffHotlineRSV.getHotlineList(reqDto);
            //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
            if(t.getResult() == null){
            	t.setResult(new ArrayList<ImHotlineInfoResDTO>());
            }
            EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
            model.addAttribute("resp", respVO);
            super.addPageToModel(model, respVO);
            return URL+"/custservice/list/custservice-list";
    }
    
    /**
     * 
     * custGrid:(用户选择页面). <br/>  
     * 
     * @param model
     * @param companyId
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/custgrid")
    public String custGrid(Model model) {
        BaseInfo baseInfo = new BaseInfo<>();
        model.addAttribute("staffCode", baseInfo.getStaff().getStaffCode());
        return URL+"/custservice/cust/cust-grid";
    } 
    
    /**
     * 
     * edit:(编辑客服页面). <br/>  
     * 
     * @param model
     * @param ids
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/edit")
    public String edit(Model model,@Valid ImStaffHotlineVO vo){
        ImStaffHotlineReqDTO dto = new ImStaffHotlineReqDTO(); 
        ObjectCopyUtil.copyObjValue(vo, dto, null, false);
        ImStaffHotlineResDTO info = staffHotlineRSV.getStaffHotline(dto);  
        model.addAttribute("info", info);
        return URL+"/custservice/custservice-edit";
    }

    /**
     * 
     * saveCust:(添加客服信息). <br/>  
     * 
     * @param custInfo
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/savecust")
    @ResponseBody
    public EcpBaseResponseVO saveCust(@Valid ImStaffHotlineVO vo) throws Exception {
        EcpBaseResponseVO respVo = new EcpBaseResponseVO(); 
        try {
        	if(vo.getModuleType().equals("0")){
                vo.setShopId(0l);
            }
            ImStaffHotlineReqDTO dto = new ImStaffHotlineReqDTO(); 
            ObjectCopyUtil.copyObjValue(vo, dto, null, false);
            dto.setPlatSource("00");//本平台
            staffHotlineRSV.addHotlineStaff(dto); 
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
        } catch (BusinessException e) {
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            respVo.setResultMsg(e.getMessage());
            LogUtil.error(MODULE, e.getErrorMessage(), e);
        }
        return respVo;
    }
    
    /**
     * 
     * updateCust:(修改客服信息). <br/>  
     * 
     * @param custInfo
     * @param staffId
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/updatecust")
    @ResponseBody
    public EcpBaseResponseVO updateCust(@Valid ImHotlineInfoVO vo) throws Exception {
        EcpBaseResponseVO respVo = new EcpBaseResponseVO(); 
        try {
            ImHotlineInfoReqDTO dto = new ImHotlineInfoReqDTO(); 
            ObjectCopyUtil.copyObjValue(vo, dto, null, false);
            if(vo.getModuleType().equals("0")){
                vo.setShopId(0l);
            }
            staffHotlineRSV. updateHotlineInfo(dto); 
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        } catch (BusinessException e) {
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            respVo.setResultMsg(e.getMessage());
            LogUtil.error(MODULE, e.getErrorMessage(), e);
        }
        return respVo;
    }
        
    /**
     * 
     * custValid:(客服列表：使生效). <br/>  
     * 
     * @param model
     * @param staffId
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping("/custvalid")
    @ResponseBody
    public EcpBaseResponseVO custValid(Model model,  @RequestParam("id")Long id) throws Exception {
        EcpBaseResponseVO result = new EcpBaseResponseVO();
        ImHotlineInfoReqDTO dto = new ImHotlineInfoReqDTO();
        dto.setId(id);
        dto.setStatus("1");
        dto.setUpdateTime(DateUtil.getSysDate());
        int resCount = staffHotlineRSV.updateHotlineInfo(dto);
        if (resCount > 0) {
            result.setResultMsg(ResourceMsgUtil.getMessage("web.staff.101001", new String[]{}));
        } else {
            result.setResultMsg(ResourceMsgUtil.getMessage("web.staff.101000", new String[]{}));
        }
        return result;
    } 
    
    /**
     * 
     * custInvalid:(客服列表：使失效). <br/>  
     * 
     * @param model
     * @param staffId
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping("/custinvalid")
    @ResponseBody
    public EcpBaseResponseVO custInvalid(Model model,  @RequestParam("id")Long id) throws Exception {
        EcpBaseResponseVO result = new EcpBaseResponseVO();
        ImHotlineInfoReqDTO dto = new ImHotlineInfoReqDTO();
        dto.setId(id);
        dto.setStatus("0");
        dto.setUpdateTime(DateUtil.getSysDate());
        int resCount = staffHotlineRSV.updateHotlineInfo(dto);
        if (resCount > 0) {
            result.setResultMsg(ResourceMsgUtil.getMessage("web.staff.101001", new String[]{}));
        } else {
            result.setResultMsg(ResourceMsgUtil.getMessage("web.staff.101000", new String[]{}));
        }
        return result;
    }
    /**
     * 
     * subAcctList:(查询子帐号列表数据). <br/> 
     * 
     * @param model
     * @param subAcctVO
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value="/subacctlist")
    public String subAcctList(Model model,@ModelAttribute SubAcctVO subAcctVO,EcpBasePageReqVO vo) throws Exception{
        
        //获取卖家店铺信息
        SellerResDTO sellerResDTO = SellerLocaleUtil.getSeller();
        //设置查询条件
        CustInfoReqDTO cust = vo.toBaseInfo(CustInfoReqDTO.class);
        long count = 0L;
        if (subAcctVO.getShopId() == null || subAcctVO.getShopId() == 0L) {
        	cust.setShopId(sellerResDTO.getShoplist().get(0).getId());//设置店铺id
        	model.addAttribute("shopId", sellerResDTO.getShoplist().get(0).getId());
        	count = shopSubAuthStaffRSV.countShopSubAuthStaff(sellerResDTO.getShoplist().get(0).getId());
        } else {
        	cust.setShopId(subAcctVO.getShopId());
        	model.addAttribute("shopId", subAcctVO.getShopId());
        	count = shopSubAuthStaffRSV.countShopSubAuthStaff(subAcctVO.getShopId());
        }
        CustSubInfoReqDTO custSub = vo.toBaseInfo(CustSubInfoReqDTO.class);
        custSub.setStaffCode(subAcctVO.getStaffCode());//用户名
        custSub.setStatus(subAcctVO.getStatus());//状态
        //调用业务方法，查询数据
        PageResponseDTO<CustInfoResDTO> page = shopSubAuthStaffRSV.listShopSubAuthStaff(cust,custSub);
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(page);
        model.addAttribute("resp", respVO);//结果集
        model.addAttribute("page", page);//结果集
        model.addAttribute("staffCode", subAcctVO.getStaffCode());
        model.addAttribute("status", subAcctVO.getStatus());
        model.addAttribute("defaultShopId", sellerResDTO.getShoplist().get(0).getId());
        String maxCount = BaseParamUtil.fetchParamValue("SHOP_SUB_ACCT_COUNT", "MAX_COUNT");
        model.addAttribute("maxCount", maxCount);//最大可创建数量
        model.addAttribute("controlSub", Long.parseLong(maxCount) - count);//剩余可创建的数量
        return URL+"/custservice/cust/list/cust-list";
    }
}
