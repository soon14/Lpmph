package com.ai.ecp.pmph.busi.staff.controller;
import java.sql.Timestamp;
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
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctBillMonthVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillMonthDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillMonthReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillMonthResDTO;
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
@RequestMapping(value="/shopmgr/shopAcctBillMonth")
public class ShopAcctBillMonthController extends EcpBaseController{
	 private static String MODULE = ShopAcctBillMonthController.class.getName();
	 @Resource
	 private IShopAcctRSV shopAcctRSV;
	 @RequestMapping(value="/index")
	 public String shopAcct(Model model){
	    model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
	    model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
		return "/staff/shopacctbillmonth/billmonth";
	 }
	 /**
	  * gridList:(查询月结账单). <br/> 
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
	 public Model gridList(Model model, EcpBasePageReqVO vo,@Valid ShopAcctBillMonthVO shopAcctBillMonthVO) throws Exception{
		 ShopAcctBillMonthReqDTO info = vo.toBaseInfo(ShopAcctBillMonthReqDTO.class);
		 //店铺ID
		 info.setShopId(shopAcctBillMonthVO.getShopId());
		 //结算月起始时间
		 String begDateStr = shopAcctBillMonthVO.getBegDate();
	     Integer begDate = Integer.parseInt(begDateStr.replace("-",""));
		 info.setBegDate(begDate);
		 //结算月截止时间
		 String endDateStr = shopAcctBillMonthVO.getEndDate();
	     Integer endDate = Integer.parseInt(endDateStr.replace("-",""));
	     info.setEndDate(endDate);
		 PageResponseDTO<ShopAcctBillMonthResDTO> t = shopAcctRSV.listShopAcctBillMonth(info);
		 //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
	     EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
		 return super.addPageToModel(model, respVO);
	 }
	 /**
	  * finish:(这里用一句话描述这个方法的作用).先检查该月所有日结账单是否完成对账,如果是则修改月结账单状态为：完成对账 <br/> 
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
	 public EcpBaseResponseVO finish(@Valid ShopAcctBillMonthVO shopAcctBillMonthVO){
	     EcpBaseResponseVO resp = new EcpBaseResponseVO();  
	     ShopAcctBillMonthReqDTO reqDTO=new ShopAcctBillMonthReqDTO();
	        try {
	            //店铺ID
	            Long shopId = shopAcctBillMonthVO.getShopId();
	            reqDTO.setShopId(shopId);
	            //结算月
	            Integer billMonth = shopAcctBillMonthVO.getBillMonth();
	            reqDTO.setBillMonth(billMonth);
	            //修改月结账单状态为1:未提现
	            reqDTO.setStatus("1");
	            //检查该月所有日结账单是否完成对账,flag=true表示所有日结账单都完成对账,可以点击完成对账
	            boolean flag = shopAcctRSV.checkBillDayStatus(shopId, billMonth);
	            if(flag){
	                //检查通过,月结账单可以点击完成对账
                    shopAcctRSV.updateShopAcctBillMonthStatus(reqDTO);
                    resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
	            }else{
	                //不可点击完成提现
	                resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                    resp.setResultMsg("该月有未对账的日结账单!");
	            }
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
	 public String detail(Model model,Long shopId,Integer billMonth){
	   //根据店铺id和结算月查询账户本月的订单详情（订单收入明细、订单退货退款支出明细、调账明细等）
	     ShopAcctBillMonthDetailResDTO dto = null;
	     try {
	         dto = shopAcctRSV.findShopAcctBillMonthDetail(shopId, billMonth);
	     } catch (Exception e) {
	         LogUtil.error(MODULE, e.getMessage());
             throw new BusinessException("查询店铺月结账单详情异常");
         }
	     model.addAttribute("dto", dto);
	     return "/staff/shopacctbillmonth/billmonth-detail";
	 }
}

