package com.ai.ecp.pmph.busi.staff.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctAdjustVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustApplyReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustApplyResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustTrackReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustTrackResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopCacheRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.alibaba.fastjson.JSON;
/**
 * 
 * Title: SHOP <br>
 * Project Name:pmph-web-manage<br>
 * Description: <br>
 * Date:2018-6-4下午1:50:35  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author LBQ
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value="/shopAcctAdjust")
public class ShopAcctAdjustController extends EcpBaseController{
	 private static String MODULE = ShopAcctAdjustController.class.getName();
	 @Resource
	 private IShopAcctRSV shopAcctRSV;
     @Resource
     private IShopInfoRSV shopInfoRSV;
     @Resource
     private IShopCacheRSV shopCacheRSV;
	 @RequestMapping(value="/index")
	 public String shopAcct(Model model,@RequestParam("billDay") int billDay,@RequestParam("shopId") long shopId){
		 String shopName = null;
		 Long allMoney = null;
		 ShopAcctInfoResDTO result;
		 ShopAcctInfoReqDTO dto = new ShopAcctInfoReqDTO();
		 if(shopId!=0){
			 ShopInfoResDTO shopRes = shopInfoRSV.findShopInfoByShopID(shopId);
			 shopName = shopRes.getShopName();
			 dto.setShopId(shopId);
			 result = shopAcctRSV.findShopAcctByShopId(dto);
			 allMoney = result.getAcctTotal();
		 }
		 model.addAttribute("allMoney", allMoney);
		 model.addAttribute("billDay", billDay);
		 model.addAttribute("shopName", shopName);
		 model.addAttribute("shopId", shopId);
		 return "/staff/shopacct/acct-adjust";
	 }
	 /**
	  * 
	  * adjustMent:(新增调账申请单). <br/> 
	  * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	  * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	  * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	  * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	  * 
	  * @author LBQ 
	  * @param shopAcctAdjustVO
	  * @return 
	  * @since JDK 1.7
	  */
	 @RequestMapping(value="/save")
	 @ResponseBody
	 public EcpBaseResponseVO adjustMent(@Valid ShopAcctAdjustVO shopAcctAdjustVO){
		 ShopAcctAdjustApplyReqDTO reqDto = new ShopAcctAdjustApplyReqDTO();
		 EcpBaseResponseVO vo = new EcpBaseResponseVO();
		 if(shopAcctAdjustVO.getShopId()!=null){
			 reqDto.setShopId(shopAcctAdjustVO.getShopId());
		 }
		 if(shopAcctAdjustVO.getBillDay()!=null){
			 reqDto.setBillDay(shopAcctAdjustVO.getBillDay());
		 }
		 ObjectCopyUtil.copyObjValue(shopAcctAdjustVO, reqDto, null, true);
		 List<ShopAcctAdjustApplyResDTO>list = shopAcctRSV.querySaveShopAcctAdjust(reqDto);
		 int count=0;
		 if(CollectionUtils.isEmpty(list)){
			 shopAcctRSV.saveShopAcctAdjust(reqDto);
			 vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		 }else{
			 for(ShopAcctAdjustApplyResDTO res:list){
				 if(res.getStatus().equals("00")){
					 count+=1;
				 }
			 }
			 if(count!=0){
				 /*reqDto.setId(list.get(0).getId());
				 reqDto.setStatus("00");
				 shopAcctRSV.updateShopAcctAdjustStatus(reqDto);*/
				 vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			 }else{
				 shopAcctRSV.saveShopAcctAdjust(reqDto);
				 vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
			 }
		 }
		 return vo;
	 }
	 @RequestMapping(value="/ajustManage")
	 public String ajustManage(Model model){
		 model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
	     model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
		 return "/staff/shopacct/shopacctmanage/acctmanage-grid";
	 }
	 /**
	  * 
	  * shopAcctAdjustList:(待处理和已处理列表数据). <br/> 
	  * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	  * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	  * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	  * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	  * 
	  * @author LBQ 
	  * @param model
	  * @param vo
	  * @param shopAcctAdjustVO
	  * @return
	  * @throws Exception 
	  * @since JDK 1.7
	  */
	 @RequestMapping(value="/gridList")
	 @ResponseBody
	 public Model shopAcctAdjustList(Model model, EcpBasePageReqVO vo,@ModelAttribute ShopAcctAdjustVO shopAcctAdjustVO)throws Exception{
		 ShopAcctAdjustApplyReqDTO reqDto = vo.toBaseInfo(ShopAcctAdjustApplyReqDTO.class);
		 Long shopId = shopAcctAdjustVO.getShopId();
		 if(shopId!=null){
			 reqDto.setShopId(shopId);
		 }
		 if(StringUtils.isNotBlank(shopAcctAdjustVO.getStatus())){
			 reqDto.setStatus(shopAcctAdjustVO.getStatus());
		 }
		 ObjectCopyUtil.copyObjValue(shopAcctAdjustVO, reqDto, "", false);
		 reqDto.setEndDate(new Timestamp(DateUtils.addDays(shopAcctAdjustVO.getEndDate(), 1).getTime()));
		 PageResponseDTO<ShopAcctAdjustApplyResDTO> resp = shopAcctRSV.queryShopAcctAdjustList(reqDto);
		 List<ShopAcctAdjustDetailResDTO> list = shopAcctRSV.listAdjustDetailByShopId(shopId);
		 if(resp.getResult()!=null){
			 for(int i=0;i<resp.getResult().size();i++){
				 Long id =  resp.getResult().get(i).getId();
				 for(int j=0;j<list.size();j++){
					 Long adjId = list.get(j).getAdjId();
					 Timestamp adjTime = list.get(j).getAdjTime();
					 if(id.equals(adjId)){
						 resp.getResult().get(i).setAdjTime(adjTime);
					 }
				 }
			 }
		 }
		 if(resp.getResult()!=null){
			for(ShopAcctAdjustApplyResDTO dto:resp.getResult()){
				ShopInfoResDTO info = shopInfoRSV.findShopInfoByShopID(dto.getShopId());
				dto.setShopName(info.getShopName());
				String staffCode = shopInfoRSV.queryAuthStaffById(dto.getApplyStaff());
				dto.setStaffCode(staffCode);
				if(dto.getStatus().equals("00")){
					dto.setStatus("待审核");
				}
				if(dto.getStatus().equals("01")){
					dto.setStatus("已调账");
				}
				if(dto.getStatus().equals("02")){
					dto.setStatus("拒绝调账");
				}
				if(dto.getStatus().equals("03")){
					dto.setStatus("撤销");
				}
			}
		 }
		 
	     EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(resp);
		 return super.addPageToModel(model, pageinfo);
	 }
	 /**
	  * 
	  * adjustUpdate:(修改调账单Status状态). <br/> 
	  * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	  * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	  * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	  * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	  * 
	  * @author LBQ 
	  * @param shopAcctAdjustVO
	  * @return 
	  * @since JDK 1.7
	  */
	 @RequestMapping(value="/adjustUpdate")
	 @ResponseBody
	 public EcpBaseResponseVO adjustUpdate(@Valid ShopAcctAdjustVO shopAcctAdjustVO){
		 	EcpBaseResponseVO resp = new EcpBaseResponseVO();
		 	ShopAcctAdjustApplyReqDTO reqDTO = new ShopAcctAdjustApplyReqDTO();
		 	try{
		 		reqDTO.setId(shopAcctAdjustVO.getId());
		 		reqDTO.setStatus("03");
		 		reqDTO.setApplyDesc(shopAcctAdjustVO.getApplyDesc());
		 		shopAcctRSV.updateShopAcctAdjustStatus(reqDTO);
		 		resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
		 	}catch(Exception e){
		 		LogUtil.error(MODULE, "============出错了============="+e.getMessage());
	            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
	            resp.setResultMsg(e.getMessage());
		 	}
		 return resp;
	 }
	 @RequestMapping(value="/backAdjust")
	 public String backAdjust(Model model,@Valid ShopAcctAdjustVO vo){
		 LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
		 ShopAcctAdjustTrackReqDTO reqDTO = vo.toBaseInfo(ShopAcctAdjustTrackReqDTO.class);
		 reqDTO.setApplyId(vo.getId());
		 List<ShopAcctAdjustTrackResDTO>list = shopAcctRSV.queryAddustTrack(reqDTO);
		 ShopAcctAdjustApplyReqDTO reqDto = new ShopAcctAdjustApplyReqDTO();
		 reqDto.setId(vo.getId());
		 List<ShopAcctAdjustApplyResDTO>ApplyList = shopAcctRSV.querySaveShopAcctAdjust(reqDto);
		 ShopInfoResDTO dto = shopCacheRSV.getCacheShopByShopId(ApplyList.get(0).getShopId());
		 if(!CollectionUtils.isEmpty(list)){
			 for(int i=0;i<list.size();i++){
				 long id = list.get(i).getCreateStaff();
				 String staffCode = shopInfoRSV.queryAuthStaffById(id);
				 list.get(i).setCreateName(staffCode);
			 }
		 }
		 model.addAttribute("rBackTrack", list);
		 model.addAttribute("rBackApplyId", ApplyList.get(0).getId());
		 if(ApplyList.get(0).getStatus().equals("00")){
			 model.addAttribute("rBackApplyStatus", "待审核");
		 }
		 if(ApplyList.get(0).getStatus().equals("01")){
			 model.addAttribute("rBackApplyStatus", "已调账");
		 }
		 if(ApplyList.get(0).getStatus().equals("02")){
			 model.addAttribute("rBackApplyStatus", "拒绝调账");
		 }
		 if(ApplyList.get(0).getStatus().equals("03")){
			 model.addAttribute("rBackApplyStatus", "撤销");
		 }
		 model.addAttribute("rBackApplyShopName", dto.getShopName());
		 model.addAttribute("rBackApplyMoney", ApplyList.get(0).getMoney());
		 model.addAttribute("rBackApplyBillDay", ApplyList.get(0).getBillDay());
		 model.addAttribute("rBackApplyApplyDesc", ApplyList.get(0).getApplyDesc());
		return "/staff/shopacct/shopacctmanage/backAdjust";
	 }
	 @RequestMapping(value="/ajustCheck")
	 public String ajustCheck(Model model){
		 model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
	     model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
		 return "/staff/shopacct/shopacctcheck/acctcheck-grid";
	 }	 
	 @RequestMapping(value="/adjustReview")
	 public String adjustReview(Model model,@Valid ShopAcctAdjustVO vo){
		 LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
		 ShopAcctAdjustTrackReqDTO reqDTO = vo.toBaseInfo(ShopAcctAdjustTrackReqDTO.class);
		 reqDTO.setApplyId(vo.getId());
		 List<ShopAcctAdjustTrackResDTO>list = shopAcctRSV.queryAddustTrack(reqDTO);
		 ShopAcctAdjustApplyReqDTO reqDto = new ShopAcctAdjustApplyReqDTO();
		 reqDto.setId(vo.getId());
		 List<ShopAcctAdjustApplyResDTO>ApplyList = shopAcctRSV.querySaveShopAcctAdjust(reqDto);
		 ShopInfoResDTO dto = shopCacheRSV.getCacheShopByShopId(ApplyList.get(0).getShopId());
		 if(!CollectionUtils.isEmpty(list)){
			 for(int i=0;i<list.size();i++){
				 long id = list.get(i).getCreateStaff();
				 String staffCode = shopInfoRSV.queryAuthStaffById(id);
				 list.get(i).setCreateName(staffCode);
			 }
		 }
		 model.addAttribute("rBackTrack", list);
		 model.addAttribute("rBackApplyId", ApplyList.get(0).getId());
		 if(ApplyList.get(0).getStatus().equals("00")){
			 model.addAttribute("rBackApplyStatus", "待审核");
		 }
		 model.addAttribute("rBackApplyShopName", dto.getShopName());
		 model.addAttribute("rBackApplyMoney", ApplyList.get(0).getMoney());
		 model.addAttribute("rBackApplyBillDay", ApplyList.get(0).getBillDay());
		 model.addAttribute("rBackApplyApplyDesc", ApplyList.get(0).getApplyDesc());
		return "/staff/shopacct/shopacctcheck/adjustReview";
	 }
	 @RequestMapping(value="/adjustUpdateReview")
	 @ResponseBody
	 public EcpBaseResponseVO adjustUpdateReview(@Valid ShopAcctAdjustVO shopAcctAdjustVO,String status){
		 	EcpBaseResponseVO resp = new EcpBaseResponseVO();
		 	ShopAcctAdjustApplyReqDTO reqDTO = new ShopAcctAdjustApplyReqDTO();
		 	ShopAcctAdjustTrackReqDTO reqDTOs = new ShopAcctAdjustTrackReqDTO();
		 	reqDTOs.setApplyId(shopAcctAdjustVO.getId());
		 	int count = 0;
		 	List<ShopAcctAdjustTrackResDTO>list = shopAcctRSV.queryAddustTrack(reqDTOs);
		 	for(ShopAcctAdjustTrackResDTO res:list){
		 		if(res.getNode().equals("01") || res.getNode().equals("02")){
		 			count+=1;
		 		}
		 	}
		 	if(count!=0){
		 		resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE); 
		 	}else{
		 		try{
		 			reqDTO.setId(shopAcctAdjustVO.getId());
		 			if(status.equals("1")){
		 				reqDTO.setStatus("01");
		 			}
		 			if(status.equals("2")){
		 				reqDTO.setStatus("02");
		 			}
		 			if(shopAcctAdjustVO.getCheckDesc()!=null){
		 				reqDTO.setCheckDesc(shopAcctAdjustVO.getCheckDesc());
		 			}
		 			shopAcctRSV.updateShopAcctAdjustStatus(reqDTO);
		 			if("01".equals(reqDTO.getStatus())){
		      		ShopAcctAdjustDetailReqDTO detailDTO = new ShopAcctAdjustDetailReqDTO();
		      		detailDTO.setAdjId(reqDTO.getId());
		      		shopAcctRSV.saveShopAcctAdjOpt(detailDTO);
		 			}
		 			resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
		 		}catch(Exception e){
		 			LogUtil.error(MODULE, "============出错了============="+e.getMessage());
		 			resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
		 			resp.setResultMsg(e.getMessage());
		 		}
		 	}
		 return resp;
	 }
 }