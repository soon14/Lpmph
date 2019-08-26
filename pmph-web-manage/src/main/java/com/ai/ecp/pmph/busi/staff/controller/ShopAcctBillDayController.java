package com.ai.ecp.pmph.busi.staff.controller;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctBillDayVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillDayDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillDayReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillDayResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.paas.utils.LogUtil;
/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: <br>
 * Date:2018年5月29日下午7:46:38  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value="/shopmgr/shopAcctBillDay")
public class ShopAcctBillDayController extends EcpBaseController{
	 private static String MODULE = ShopAcctBillDayController.class.getName();
	 @Resource
	 private IShopAcctRSV shopAcctRSV;
	 @RequestMapping(value="/index")
	 public String shopAcct(Model model){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
		return "/staff/shopacctbillday/billday";
	 }
	 /**
	  * gridList:(查询日结账单). <br/> 
	  * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	  * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	  * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	  * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	  * 
	  * @author mwz 
	  * @param model
	  * @param vo
	  * @param shopAcctBillDayVO
	  * @return
	  * @throws Exception 
	  * @since JDK 1.6
	  */
	 @RequestMapping(value="/gridlist")
	 @ResponseBody
	 public Model gridList(Model model, EcpBasePageReqVO vo,@Valid ShopAcctBillDayVO shopAcctBillDayVO) throws Exception{
		 ShopAcctBillDayReqDTO info = vo.toBaseInfo(ShopAcctBillDayReqDTO.class);
		 SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		 //店铺ID
		 info.setShopId(shopAcctBillDayVO.getShopId());
		 //结算日起始时间
		 Date begDate = shopAcctBillDayVO.getBegDate();
	     String begDateStr = format.format(begDate);
	     Integer begDateInt = Integer.parseInt(begDateStr);
		 info.setBegDate(begDateInt);
		 //结算日截止时间
		 shopAcctBillDayVO.setEndDate(new Timestamp(DateUtils.addDays(shopAcctBillDayVO.getEndDate(), 1).getTime()));
		 Date endDate = shopAcctBillDayVO.getEndDate();
	     String endDateStr = format.format(endDate);
	     Integer endDateInt = Integer.parseInt(endDateStr);
	     info.setEndDate(endDateInt);
		 PageResponseDTO<ShopAcctBillDayResDTO> t = shopAcctRSV.listShopAcctBillDay(info); 
		 //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
	     EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
		 return super.addPageToModel(model, respVO);
	 }
	 /**
	  * finish:(这里用一句话描述这个方法的作用).修改日结账单状态为：完成对账 <br/> 
	  * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	  * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	  * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	  * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	  * 
	  * @author mwz 
	  * @param shopAcctBillDayVO
	  * @return 
	  * @since JDK 1.7
	  */
	 @RequestMapping(value="/finish")
     @ResponseBody
	 public EcpBaseResponseVO finish(@Valid ShopAcctBillDayVO shopAcctBillDayVO){
	     EcpBaseResponseVO resp = new EcpBaseResponseVO();  
	     ShopAcctBillDayReqDTO reqDTO=new ShopAcctBillDayReqDTO();
	        try {
	            //店铺ID
	            reqDTO.setShopId(shopAcctBillDayVO.getShopId());
	            //结算日
	            reqDTO.setBillDay(shopAcctBillDayVO.getBillDay());
	            //修改日结账单状态为1:完成对账
	            reqDTO.setStatus("1");
	            shopAcctRSV.updateShopAcctBillDayStatus(reqDTO);
	            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
	        }catch(Exception e){
	            LogUtil.error(MODULE, "============出错了============="+e.getMessage());
	            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
	            resp.setResultMsg(e.getMessage());
	        }
	     return resp;
	 }
	 /**
	  * detail:(查看日结账单详情). <br/> 
	  * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	  * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	  * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	  * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	  * 
	  * @author mwz 
	  * @param model
	  * @param shopId
	  * @param billDay
	  * @return 
	  * @since JDK 1.6
	  */
	 @RequestMapping(value="/detail")
	 public String detail(Model model,Long shopId,Integer billDay){
	   //根据店铺id和结算日查询账户本日的订单详情（订单收入明细、订单退货退款支出明细、调账明细等）
	     ShopAcctBillDayDetailResDTO dto = null;
	     try {
	         dto = shopAcctRSV.findShopAcctBillDayDetail(shopId, billDay);
	     } catch (Exception e) {
	         LogUtil.error(MODULE, e.getMessage());
             throw new BusinessException("查询店铺日结账单详情异常");
         }
	     model.addAttribute("dto", dto);
	     return "/staff/shopacctbillday/billday-detail";
	 }
}

