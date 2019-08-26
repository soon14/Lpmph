package com.ai.ecp.pmph.service.busi.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfMainHisSV;
import org.apache.commons.lang3.StringUtils;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.pmph.dao.model.InfOrdMain;
import com.ai.ecp.pmph.dao.model.InfOrdSub;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfMainSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfSubSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustThirdCodeRSV;
import com.ai.ecp.unpf.dao.model.UnpfOrdMain;
import com.ai.ecp.unpf.dubbo.dto.order.RUnpfOrdSubReq;
import com.ai.ecp.unpf.dubbo.util.UnpfOrdConstants;
import com.ai.ecp.unpf.service.busi.order.impl.UnpfErpOrderSVImpl;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class PmphUnpfErpOrderSVImpl extends UnpfErpOrderSVImpl {
	
	public static final String MODULE = PmphUnpfErpOrderSVImpl.class.getName();
	
	@Resource
	private  IOrdInfMainSV ordInfMainSV;
	
	@Resource
	private IOrdInfSubSV ordInfSubSV;
	
	
	@Resource
	private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
	
	@Resource
	private ICustThirdCodeRSV custThirdCodeRSV;

	@Resource
	private IOrdInfMainHisSV ordInfMainHisSV;
	
	
	public void saveErpOrder(UnpfOrdMain unpfOrdMain,List<RUnpfOrdSubReq> subOrders,String nickName) throws BusinessException {
		// TODO Auto-generated method stub
		//天猫订单入库，其他不入库
/* 		if(!unpfOrdMain.getPlatType().equals("taobao")){
			return;
		}*/
		//订单状态待支付
		if(unpfOrdMain.getStatus().equals(UnpfOrdConstants.OrderStatus.ORDER_STATUS_PAID)){
			//判断是否已经保存过该订单
			long num = ordInfMainSV.queryOrdInfMainNumByOrderId(unpfOrdMain.getOuterId());
			long num1 = this.ordInfMainHisSV.queryOrdInfMainHisNumByOrderId(unpfOrdMain.getOuterId());
			if((num + num1)<1l){
				//保存主订单
				InfOrdMain infOrdMain = new InfOrdMain();
				infOrdMain.setId(unpfOrdMain.getOuterId());
				infOrdMain.setOrderCode(unpfOrdMain.getOuterId());

				if(StringUtil.isNotBlank(unpfOrdMain.getOrderAmount())){
					infOrdMain.setOrderAmount(Long.parseLong(unpfOrdMain.getOrderAmount()));
				}

				infOrdMain.setRealMoney(unpfOrdMain.getRealMoney());
				infOrdMain.setShopId(unpfOrdMain.getShopId());
				infOrdMain.setShopName(unpfOrdMain.getShopName());
				infOrdMain.setOrderTime(unpfOrdMain.getOrderTime());
				infOrdMain.setPayTime(unpfOrdMain.getPayTime());
				infOrdMain.setOrderType("01");
				infOrdMain.setStatus(unpfOrdMain.getStatus());
				if(unpfOrdMain.getCreateTime()==null){
					infOrdMain.setCreateTime(new Timestamp(System.currentTimeMillis()));
				}else{
					infOrdMain.setCreateTime(unpfOrdMain.getCreateTime());
				}
				if(unpfOrdMain.getUpdateTime()==null){
					infOrdMain.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				}else{
					infOrdMain.setUpdateTime(unpfOrdMain.getUpdateTime());
				}

				infOrdMain.setStaffName(unpfOrdMain.getContractName());
				if(nickName!=null){
					CustThirdCodeReqDTO custThirdCodeReqDTO = new CustThirdCodeReqDTO();
					custThirdCodeReqDTO.setThirdCode(nickName);
					custThirdCodeReqDTO.setThirdCodeType("10");
					CustThirdCodeResDTO  custThirdCodeResDTO = custThirdCodeRSV.queryThirdCode(custThirdCodeReqDTO);
					if(StringUtil.isNotEmpty(custThirdCodeResDTO)){
						infOrdMain.setStaffId(custThirdCodeResDTO.getStaffId());
						infOrdMain.setStaffCode(custThirdCodeResDTO.getStaffCode());
					}
				}
				//infOrdMain.setStaffCode(staffCode);
				//infOrdMain.setCreateStaff(createStaff);
				//infOrdMain.setStaffId(staffId);
				//infOrdMain.setUpdateStaff(updateStaff);
				try{
					ordInfMainSV.saveOrdInfMain(infOrdMain);

					//保存子订单
					for(RUnpfOrdSubReq subOrder:subOrders){
						InfOrdSub infOrdSub = new InfOrdSub();
						infOrdSub.setId(subOrder.getOuterSubId());
						infOrdSub.setOrderId(unpfOrdMain.getOuterId());
						if(StringUtil.isNotBlank(subOrder.getGdsId()) && StringUtils.isNumeric(subOrder.getGdsId())){
							infOrdSub.setGdsId(Long.parseLong(subOrder.getGdsId()));
						}
						if(StringUtil.isNotBlank(subOrder.getSkuId()) && StringUtils.isNumeric(subOrder.getSkuId())){
							infOrdSub.setSkuId(Long.parseLong(subOrder.getSkuId()));
							GdsSkuInfoReqDTO skuInfoReqDTO = new GdsSkuInfoReqDTO();
							skuInfoReqDTO.setId(Long.parseLong(subOrder.getSkuId()));
							GdsSkuInfoRespDTO skuInfo = gdsSkuInfoQueryRSV.querySkuInfoByOptions( skuInfoReqDTO);
							if(skuInfo!=null){
								infOrdSub.setGdsType(skuInfo.getGdsTypeId());
								infOrdSub.setCategoryCode(""+skuInfo.getCatlogId());
							} else {
								//如果不是本系统的商品则默认为1
								infOrdSub.setGdsType(1L);
							}
						} else {
							//如果不是本系统的商品则默认为1
							infOrdSub.setGdsType(1L);
						}
						if(StringUtil.isNotBlank(subOrder.getOrderAmount())){
							infOrdSub.setOrderAmount(Long.parseLong(subOrder.getOrderAmount()));
						}
						if(StringUtil.isNotBlank(subOrder.getOrderPrice())){
							infOrdSub.setDiscountPrice(Long.parseLong(subOrder.getOrderPrice()));
						}

						if(subOrder.getCreateTime()==null){
							infOrdSub.setCreateTime(new Timestamp(System.currentTimeMillis()));
						}else{
							infOrdSub.setCreateTime(unpfOrdMain.getCreateTime());
						}
						if(subOrder.getUpdateTime()==null){
							infOrdSub.setUpdateTime(new Timestamp(System.currentTimeMillis()));
						}else{
							infOrdSub.setUpdateTime(unpfOrdMain.getUpdateTime());
						}

						//infOrdSub.setCreateStaff(createStaff);
						//infOrdSub.setUpdateStaff(updateStaff);
						//infOrdSub.setInfGdsId(infGdsId);
						ordInfSubSV.saveOrdInfSub(infOrdSub);
					}

				}catch(Exception e){
					LogUtil.debug(MODULE, "------OrderId："+unpfOrdMain.getId()+" -------Save t_inf_ord_main Record error---------");
				}

			}
		}
	}
}
