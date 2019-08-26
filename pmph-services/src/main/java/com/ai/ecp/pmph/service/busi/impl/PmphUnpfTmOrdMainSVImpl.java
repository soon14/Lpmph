package com.ai.ecp.pmph.service.busi.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.ai.ecp.aip.third.dubbo.dto.resp.OrderRespDTO;
import com.ai.ecp.aip.third.dubbo.dto.resp.SubOrder;
import com.ai.ecp.pmph.dao.mapper.busi.OrdMainTMMapper;
import com.ai.ecp.pmph.dao.mapper.busi.OrdSubTMMapper;
import com.ai.ecp.pmph.dao.model.OrdMainTM;
import com.ai.ecp.pmph.dao.model.OrdMainTMCriteria;
import com.ai.ecp.pmph.dao.model.OrdSubTM;
import com.ai.ecp.pmph.dao.model.OrdSubTMCriteria;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustThirdCodeRSV;
import com.ai.ecp.unpf.service.busi.order.impl.UnpfTmOrdMainSVImpl;
import com.ai.paas.utils.StringUtil;

public class PmphUnpfTmOrdMainSVImpl extends UnpfTmOrdMainSVImpl {
	
	@Resource
	private OrdMainTMMapper ordMainTMMapper;
	
	@Resource
	private OrdSubTMMapper ordSubTMMapper;
	
	@Resource
	private ICustThirdCodeRSV custThirdCodeRSV;
	
	@Override
	public void saveUnpfTmOrdMain(OrderRespDTO orderDetial) throws BusinessException {
		// TODO Auto-generated method stub
		OrdMainTM orderMainTM = new  OrdMainTM();		
		List<OrdSubTM> ordSubTMs = new ArrayList<OrdSubTM>();
		this.copyDetialToOrderMainTM(orderDetial, orderMainTM, ordSubTMs);
		ordMainTMMapper.insert(orderMainTM);
		for(OrdSubTM ordSubTM:ordSubTMs){
			ordSubTMMapper.insert(ordSubTM);
		}

	}

	@Override
	public void updateUnpfTmOrdMain(OrderRespDTO orderDetial) throws BusinessException {
		// TODO Auto-generated method stub
		OrdMainTM orderMainTM = new  OrdMainTM();		
		List<OrdSubTM> ordSubTMs = new ArrayList<OrdSubTM>();
		this.copyDetialToOrderMainTM(orderDetial, orderMainTM, ordSubTMs);
		
		//ordMainTMMapper.updateByPrimaryKey(orderMainTM);
		//xx这表竟然没主键
		OrdMainTMCriteria example = new OrdMainTMCriteria();
		example.createCriteria().andIdEqualTo(orderMainTM.getId());
		ordMainTMMapper.updateByExample(orderMainTM, example);
		
		for(OrdSubTM ordSubTM:ordSubTMs){
			//ordSubTMMapper.updateByPrimaryKey(ordSubTM);
			OrdSubTMCriteria subord = new OrdSubTMCriteria();
			subord.createCriteria().andIdEqualTo(ordSubTM.getId());
			ordSubTMMapper.updateByExample(ordSubTM, subord);
		}
	}
	
	/**
	 * 对象转换
	 * @param orderDetial
	 * @param orderMainTM
	 * @param ordSubTMs
	 */
	private void copyDetialToOrderMainTM(OrderRespDTO orderDetial,OrdMainTM orderMainTM,List<OrdSubTM> ordSubTMs){
		orderMainTM.setId(orderDetial.getOrderId());
		orderMainTM.setOrderCode(orderDetial.getOrderId());
		orderMainTM.setTmStaffCode(orderDetial.getBuyerNick());
		//orderMainTM.setAlipayAccount("");//没有置空
		orderMainTM.setOrderMoney(orderDetial.getTotalFee());//商品金额
		orderMainTM.setRealExpressFee(orderDetial.getPostFee());//邮费
		
		//计算总金额
		Double sumMoney = 0d;
		if(StringUtil.isNotBlank(orderDetial.getTotalFee())){
			sumMoney = sumMoney+Double.parseDouble(orderDetial.getTotalFee());
		}
		if(StringUtil.isNotBlank(orderDetial.getPostFee())){
			sumMoney = sumMoney+Double.parseDouble(orderDetial.getPostFee());
		}
		BigDecimal b = new  BigDecimal(sumMoney); 
		sumMoney  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();	//保留2位小数
		orderMainTM.setSumMoney(""+sumMoney);//总金额=商品金额+邮费
		orderMainTM.setRealMoney(orderDetial.getPayment());//实付金额
		
		//orderMainTM.setOrderScore("");//订单积分
		//orderMainTM.setBackScore("");//退回积分
		//orderMainTM.setRealScore("");//		

		orderMainTM.setStatus(this.convertTaoBaoOrderStatus(orderDetial.getStatus()));//订单状态
		orderMainTM.setBuyerMsg(orderDetial.getBuyerMessage());
		orderMainTM.setContractName(orderDetial.getReceiverName());
		//地址
		String addr = orderDetial.getReceiverState()+orderDetial.getReceiverCity()+orderDetial.getReceiverDistrict()+orderDetial.getReceiverAddress();
		orderMainTM.setContractAddr(addr);
		
		orderMainTM.setDispatchType("快递");//默认快递
		if(StringUtil.isNotBlank(orderDetial.getReceiverMobile())){
			orderMainTM.setContractNum(orderDetial.getReceiverMobile());
		}else{
			orderMainTM.setContractNum(orderDetial.getReceiverPhone());
		}
		if(StringUtil.isNotEmpty(orderDetial.getCreated())){
			orderMainTM.setOrderTime(new Timestamp(orderDetial.getCreated().getTime()));
		}		
		if(StringUtil.isNotEmpty(orderDetial.getPayTime())){
			orderMainTM.setPayTime(new Timestamp(orderDetial.getPayTime().getTime()));
		}
		orderMainTM.setBbTitle(orderDetial.getTitle());
		//orderMainTM.setBbType("");//啥玩意？？？
		//orderMainTM.setExpressNo(expressNo);
		//orderMainTM.setExpressCompany(expressCompany);
		orderMainTM.setRemark(orderDetial.getBuyerMemo());
			
		orderMainTM.setShopId(""+orderDetial.getShopId());
		orderMainTM.setShopName(orderDetial.getSellerNick());//店铺昵称
		//orderMainTM.setShopServiceFee(shopServiceFee);
		//orderMainTM.setStaffServiceFee(staffServiceFee);
		//orderMainTM.setAppFlag(appFlag);
		//orderMainTM.setRwScore(rwScore);
		if(orderDetial.getBuyerNick()!=null){
			CustThirdCodeReqDTO custThirdCodeReqDTO = new CustThirdCodeReqDTO();
			custThirdCodeReqDTO.setThirdCode(orderDetial.getBuyerNick());
			custThirdCodeReqDTO.setThirdCodeType("10");
			CustThirdCodeResDTO  custThirdCodeResDTO = custThirdCodeRSV.queryThirdCode(custThirdCodeReqDTO);
			if(StringUtil.isNotEmpty(custThirdCodeResDTO)){
				orderMainTM.setRwStaffId(custThirdCodeResDTO.getStaffId());
				orderMainTM.setRwStaffCode(custThirdCodeResDTO.getStaffCode());
				orderMainTM.setCreateStaff(custThirdCodeResDTO.getStaffId());
				orderMainTM.setUpdateStaff(custThirdCodeResDTO.getStaffId());
			}else{
				orderMainTM.setCreateStaff(1000L);
				orderMainTM.setUpdateStaff(1000L);
			}				
		}else{
			orderMainTM.setCreateStaff(1000L);
			orderMainTM.setUpdateStaff(1000L);
		}
		orderMainTM.setTmStaffCode(orderDetial.getBuyerNick());//用户昵称
		orderMainTM.setRwScoreFlag("0");//默认0
		orderMainTM.setCreateTime(new Timestamp(new Date().getTime()));
		orderMainTM.setUpdateTime(orderMainTM.getCreateTime());
		orderMainTM.setImportTime(orderMainTM.getUpdateTime());
		Long orderAmount = 0L;
		
		if(CollectionUtils.isNotEmpty(orderDetial.getSubOrders())){
			for(SubOrder order:orderDetial.getSubOrders()){
				OrdSubTM ordSubTM = new OrdSubTM();				
				if(order.getNum()!=null){
					orderAmount = orderAmount+order.getNum();
					ordSubTM.setOrderAmount(""+order.getNum());
				}
				ordSubTM.setId(""+order.getOid());
				ordSubTM.setOrderId(orderDetial.getOrderId());
				ordSubTM.setTitle(order.getTitle());
				ordSubTM.setOrderPrice(order.getPrice());
				//ordSubTM.setExternalSysCode(externalSysCode);//????啥玩意？？？
				//ordSubTM.setRemark(remark);
				ordSubTM.setStatus(this.convertTaoBaoOrderStatus(order.getStatus()));
				ordSubTM.setShopCode(orderDetial.getSellerNick());
				ordSubTM.setCreateTime(new Timestamp(new Date().getTime()));
				ordSubTM.setUpdateTime(new Timestamp(new Date().getTime()));
				ordSubTM.setCreateStaff(orderMainTM.getCreateStaff());
				ordSubTM.setUpdateStaff(orderMainTM.getCreateStaff());
				ordSubTMs.add(ordSubTM);
			}			
		}
		if(StringUtil.isEmpty(orderDetial.getNum())){
			orderMainTM.setOrderAmount(""+orderAmount);//订单商品数量
		}else{
			orderMainTM.setOrderAmount(""+orderDetial.getNum());
		}
	}
	
	//转换天猫订单状态
    private String convertTaoBaoOrderStatus(String status){
    	String orderStatus = "";
    	switch (status){
	    	case  "TRADE_NO_CREATE_PAY" :{
	    		orderStatus = "没有创建支付宝交易";
	    		break;
	    	}
	    	case  "WAIT_BUYER_PAY" :{
	    		orderStatus = "等待买家付款";
	    		break;
	    	}
	    	case  "SELLER_CONSIGNED_PART" :{
	    		orderStatus = "卖家部分发货";
	    		break;
	    	}
	    	case  "WAIT_SELLER_SEND_GOODS" :{
	    		orderStatus = "买家已付款,等待卖家发货";
	    		break;
	    	}
	    	case  "WAIT_BUYER_CONFIRM_GOODS" :{
	    		orderStatus = "卖家已发货,等待买家确认收货";
	    		break;
	    	}
	    	case  "TRADE_BUYER_SIGNED" :{
	    		orderStatus = "买家已签收,货到付款专用";
	    		break;
	    	}
	    	case  "TRADE_CLOSED" :{
	    		orderStatus = "交易关闭";
	    		break;
	    	}
	    	case  "TRADE_CLOSED_BY_TAOBAO" :{
	    		orderStatus = "付款以前，卖家或买家主动关闭交易";
	    		break;
    		}
	    	case  "PAY_PENDING" :{
	    		orderStatus = "国际信用卡支付付款确认中";
	    		break;
	    	}
	    	case  "TRADE_FINISHED" :{
	    		orderStatus = "交易成功";
	    		break;
	    	}
	    	default :{
	    		orderStatus = "0元购合约中";
	    		break;
	    	}
    	}
    	return orderStatus;
    }
    
}