package com.ai.ecp.pmph.busi.seller.staff.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillDayReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillDayResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillMonthReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillMonthResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.paas.utils.LogUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * 
 * Title: SHOP <br>
 * Project Name:pmph-web-mall <br>
 * Description: <br>
 * Date:2018-5-18下午2:57:01  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author LBQ
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value="/seller/shopAccount")
public class ShopAcctController extends EcpBaseController{
	 private static String MODULE = ShopAcctController.class.getName();
	 @Resource
	 private IShopAcctRSV shopAcctRSV;
	 @RequestMapping(value="/shopAcctList")
	 public String shopAcct(){
		return "seller/staff/shop/account/shopAcct";
	 }
	 @RequestMapping(value="/gridlist")
	 @ResponseBody
	 public ShopAcctVO gridList(Model model, EcpBasePageReqVO vo,@Valid ShopAcctVO shopAcctVO) throws Exception{
		 ShopAcctInfoReqDTO info = vo.toBaseInfo(ShopAcctInfoReqDTO.class);
		 if(StringUtils.isNotBlank(shopAcctVO.getShopName())){
			 info.setShopName(shopAcctVO.getShopName());
		 }
		 if(shopAcctVO.getCompanyId()!=null){
			 info.setCompanyId(shopAcctVO.getCompanyId());
		 }
		 if(shopAcctVO.getShopId()!=null){
			 info.setShopId(shopAcctVO.getShopId());
		 }
		 ShopAcctInfoResDTO dto = shopAcctRSV.findShopAcctByShopId(info); 
		 ShopAcctVO reqVO = new ShopAcctVO();
		 if(dto.getAcctTotals()!=null){
			 reqVO.setAcctTotals(dto.getAcctTotals());
		 }else{
			 reqVO.setAcctTotals("0.00");
		 }
		 if(dto.getAcctBalances()!=null){
			 reqVO.setAcctBalances(dto.getAcctBalances());
		 }else{
			 reqVO.setAcctBalances("0.00");
		 }
		 if(dto.getAcctFrozens()!=null){
			 reqVO.setAcctFrozens(dto.getAcctFrozens());
		 }else{
			 reqVO.setAcctFrozens("0.00");
		 }
		 if(dto.getAcctTotal()!=null){
			 reqVO.setAcctTotal(dto.getAcctTotal());
		 }else{
			 reqVO.setAcctTotal(0l);
		 }
		 if(dto.getAcctBalance()!=null){
			 reqVO.setAcctBalance(dto.getAcctBalance());
		 }else{
			 reqVO.setAcctBalance(0l);
		 }
		 return reqVO;
	 }
	 @RequestMapping(value="/balanceList")
	 @ResponseBody
	 public List<ShopAcctBillDayResDTO> balanceList(EcpBasePageReqVO vo,@Valid ShopAcctVO shopAcctVO) throws Exception{
		 ShopAcctBillDayReqDTO bill = vo.toBaseInfo(ShopAcctBillDayReqDTO.class);
		 List<ShopAcctBillDayResDTO>list = new ArrayList<ShopAcctBillDayResDTO>();
		 if(shopAcctVO.getShopId()!=null){
			 bill.setShopId(shopAcctVO.getShopId());
			 PageResponseDTO<ShopAcctBillDayResDTO> dto= shopAcctRSV.listShopAcctBillDay1(bill);
			 if(dto.getResult()!=null){
				 list = dto.getResult();
			 }
		 }
		 return list;
	 }
	 @RequestMapping(value="/billMonthList")
	 @ResponseBody
	 public Map<String, Object> billMonthList(EcpBasePageReqVO vo,@Valid ShopAcctVO shopAcctVO) throws Exception{
		 List<ShopAcctBillMonthResDTO>list = new ArrayList<ShopAcctBillMonthResDTO>();
		 List<ShopAcctBillMonthResDTO>lastMonthList = new ArrayList<ShopAcctBillMonthResDTO>();
		 Map<String,Object>map = new HashMap<String,Object>();
		 int billTime = 0;
		 int lastBillTime = 0;
		 if(shopAcctVO.getShopId()!=null){
		 if(shopAcctVO.getYear()!=null && shopAcctVO.getMonth()!=null){
		 String m = shopAcctVO.getMonth().toString();
		 if(m.length()<2){
			int t = shopAcctVO.getYear().intValue()*10;
			String s = t+""+shopAcctVO.getMonth().toString();
			billTime = Integer.parseInt(s);
		 }else{
		    String s = shopAcctVO.getYear().toString()+shopAcctVO.getMonth().toString();
		    billTime = Integer.parseInt(s);
		 }
	     }
	     	list= shopAcctRSV.findShopAcctBillMonthByDay(shopAcctVO.getShopId(),billTime);
	     }		
	 	 if(shopAcctVO.getMonth().intValue()!=1){
	 		lastBillTime = billTime-1;
	 	 }else{
	 		int year = Integer.parseInt(shopAcctVO.getYear().toString());
	 		String lastYear = String.valueOf(year-1);
	 		shopAcctVO.setMonth(12l);
	 		String lastBillTimes = lastYear + shopAcctVO.getMonth().toString();
	 		lastBillTime = Integer.parseInt(lastBillTimes);
	 	 }
	 	    lastMonthList= shopAcctRSV.findShopAcctBillMonthByDay(shopAcctVO.getShopId(),lastBillTime);
	 	    if(CollectionUtils.isEmpty(list)){
	 	    	map.put("money", "0.00");
	 	    }else{
	 	    	map.put("money", list.get(0).getMoneys());
	 	    }
	 	    if(CollectionUtils.isEmpty(lastMonthList)){
	 	    	map.put("lastMonth", "0.00");
	 	    }else{
	 	    	map.put("lastMonth", lastMonthList.get(0).getMoneys());
	 	    }
		 return map;
	 }
	 @RequestMapping(value="/withdrawApply")
	 @ResponseBody
	 public List<ShopAcctWithdrawApplyResDTO> withdrawApply(EcpBasePageReqVO vo,@Valid ShopAcctVO shopAcctVO) throws Exception{
		 ShopAcctWithdrawApplyReqDTO dto = vo.toBaseInfo(ShopAcctWithdrawApplyReqDTO.class);
		 Long id = 0l;
		 Long count = 0l;
		 Long acctId = 0l;
		 if(shopAcctVO.getShopId()!=null){
			 dto.setShopId(shopAcctVO.getShopId());
		 }
		 PageResponseDTO<ShopAcctWithdrawApplyResDTO> result = shopAcctRSV.withdrawApplyState(dto);
		 if(!CollectionUtils.isEmpty(result.getResult())){
			 id = result.getResult().get(0).getId();
		     count = result.getCount();
		     acctId = result.getResult().get(0).getAcctId();
		 }
		 List<ShopAcctWithdrawApplyResDTO> list = new ArrayList<ShopAcctWithdrawApplyResDTO>();
		 ShopAcctWithdrawApplyResDTO rsDto = new ShopAcctWithdrawApplyResDTO();
		 rsDto.setId(id);
		 rsDto.setStatus(count.toString());
		 rsDto.setAcctId(acctId);
		 list.add(rsDto);
		 return list;
	 }
	 @RequestMapping(value="/updateWithdrawApply")
	 @ResponseBody
	 public EcpBaseResponseVO updateWithdrawApply(EcpBasePageReqVO vo,@Valid ShopAcctVO shopAcctVO) throws Exception{
		 EcpBaseResponseVO respVo = new EcpBaseResponseVO(); 
		 ShopAcctWithdrawApplyReqDTO dto = vo.toBaseInfo(ShopAcctWithdrawApplyReqDTO.class);
		 ShopAcctBillMonthReqDTO reqDto = new ShopAcctBillMonthReqDTO();
		 ShopAcctWithdrawApplyResDTO dto2 = shopAcctRSV.findWithdrawApplyById(shopAcctVO.getId());
		 if(dto2!=null){
			 if(!"00".equals(dto2.getStatus())){
				 respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
				 return respVo;
			 }
		 }
		 String billMoths = dto2.getBillMonths();
		 System.out.println(billMoths);
		 String [] result = null;
		 if(billMoths.indexOf(",")>=0){
			 result = billMoths.split(",");
		 }
		 if(shopAcctVO.getShopId()!=null){
			 dto.setShopId(shopAcctVO.getShopId());
			 reqDto.setShopId(shopAcctVO.getShopId());
		 }
		 if(shopAcctVO.getId()!=null){
			 dto.setId(shopAcctVO.getId());
		 }
		 if(shopAcctVO.getCompanyId()!=null){
			 dto.setAcctId(shopAcctVO.getCompanyId());
		 }
		 dto.setStatus("05");
		 reqDto.setStatus("1");
		 try{
			 shopAcctRSV.updateWithdrawApplyStatus(dto);
			 if(result!=null){
			 for(int i=0;i<result.length;i++){
			 int billMonth = Integer.parseInt(result[i]);	 
			 reqDto.setBillMonth(billMonth);
			 shopAcctRSV.updateShopAcctBillMonthStatus(reqDto);
			 }
			 }else{
				 int billMonth = Integer.parseInt(billMoths);	
				 reqDto.setBillMonth(billMonth);	 
				 shopAcctRSV.updateShopAcctBillMonthStatus(reqDto);
			 }
			 respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS); 
		 }catch(BusinessException e){
			 respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
             respVo.setResultMsg(e.getMessage());
             LogUtil.error(MODULE, e.getErrorMessage(), e);
		 } 
		 return respVo;
	 }
}

