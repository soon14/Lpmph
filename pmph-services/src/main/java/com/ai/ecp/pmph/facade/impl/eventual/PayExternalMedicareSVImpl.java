package com.ai.ecp.pmph.facade.impl.eventual;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.aip.dubbo.interfaces.IOrderQueryRSV;
import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2CatgReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2PropRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dao.model.OrdSub;
import com.ai.ecp.order.dao.model.PayQuartzInfo;
import com.ai.ecp.order.dubbo.dto.RConfirmDeliveRequest;
import com.ai.ecp.order.dubbo.dto.RConfirmSubInfo;
import com.ai.ecp.order.dubbo.dto.SBaseAndSubInfo;
import com.ai.ecp.order.dubbo.dto.pay.PayIntfReqLogDTO;
import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayResultResponse;
import com.ai.ecp.order.dubbo.interfaces.IOrdDeliveryRSV;
import com.ai.ecp.order.dubbo.interfaces.pay.IPayResultRSV;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants.Common;
import com.ai.ecp.order.dubbo.util.OrdConstants.PayStatus;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayIntfReqLogSV;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayQuartzInfoSV;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipExternalAuthResponse;
import com.ai.ecp.pmph.aip.dubbo.interfaces.IAipExternalAuthRSV;
import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayExternalMedicareSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.distribute.tx.common.TransactionStatus;

import net.sf.json.JSONObject;

public class PayExternalMedicareSVImpl implements IPayExternalMedicareSV {
	
	@Resource
    private IOrderQueryRSV orderQueryRSV;
    
    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;
    
    @Resource
    private IPayIntfReqLogSV payIntfReqLogSV;
    
    @Resource
    private IAipExternalAuthRSV aipExternalAuthRSV;
    
    @Resource
    private IPayQuartzInfoSV payQuartzInfoSV;
    
    @Resource
    private IOrdMainSV ordMainSV;
    
    @Resource
    private IOrdSubSV ordSubSV;
    
    @Resource
    private ICustManageRSV custManageRSV;
    
    @Resource
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;
    
    @Resource
    private IOrdImportLogSV ordImportLogSV;
    
    @Resource
    private IOrdDeliveryRSV ordDeliveryRSV;
    
    @Resource
    private IPayResultRSV payResultRSV;
    
    private static final String MODULE = PayExternalMedicareSVImpl.class.getName();
	
	@Override
	public void joinTransaction(JSONObject message, TransactionStatus status, String transactionName) {
		try{
			final PaySuccInfo paySuccInfo = JSON.parseObject(message.toString(), PaySuccInfo.class);
            LogUtil.info(MODULE,"PayZYDigitalSVImpl============="+paySuccInfo.toString());
            LogUtil.info(MODULE,"处理外系统商品开始，订单号："+paySuccInfo.getOrderId());
            
            List<OrdSub> OrdSubList = ordSubSV.queryOrderSubByOrderId(paySuccInfo.getOrderId());
            if(OrdSubList!=null&&!OrdSubList.isEmpty()){
            	PaySuccInfo paySuccInfoPara = new PaySuccInfo();
                for(OrdSub ordSub:OrdSubList){
                	ObjectCopyUtil.copyObjValue(paySuccInfo, paySuccInfoPara, null, false);
                    paySuccInfoPara.setSubOrder(ordSub.getId());
                    deal(paySuccInfoPara);
                }
            }

            LogUtil.info(MODULE,"处理外系统结束，订单号："+paySuccInfo.getOrderId());
		}catch(BusinessException be){
			LogUtil.error(MODULE, "支付回调外系统商品授权接口处理异常", be);
            be.printStackTrace();
            status.setRollbackOnly();
            throw new BusinessException(be.getErrorCode());
		}catch(Exception e){
			LogUtil.error(MODULE, "支付回调外系统商品授权接口处理异常", e);
            status.setRollbackOnly();
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310024);
		}
	}

	@Override
	public void deal(PaySuccInfo paySuccInfo) {
		try{
			LogUtil.info(MODULE,"处理外系统商品开始============="+paySuccInfo.toString());
	        LogUtil.info(MODULE,"处理外系统商品开始，子订单号："+paySuccInfo.getSubOrder());
	        
	        //获取定时任务表数据
            RPayQuartzInfoRequest payQuartzInfoRequest = new RPayQuartzInfoRequest();
            payQuartzInfoRequest.setSubOrder(paySuccInfo.getSubOrder());
            payQuartzInfoRequest.setTaskType(PayStatus.PAY_TASK_TYPE_05);
            payQuartzInfoRequest.setDealFlag(PayStatus.PAY_DEAL_FLAG_0);
            PayQuartzInfo payQuartzInfo = payQuartzInfoSV.getBeanBySubOrderIdTaskTypeDealFlag(payQuartzInfoRequest);
            
            //找不到就直接返回
            if(payQuartzInfo==null){
                return;
            }
            //获取子订单信息    
            SBaseAndSubInfo sOrderAOrderSubInf = new SBaseAndSubInfo();
            sOrderAOrderSubInf.setOrderId(payQuartzInfo.getOrderId());
            sOrderAOrderSubInf.setOrderSubId(payQuartzInfo.getSubOrder());
            OrdSub ordSub = ordSubSV.findByOrderSubId(sOrderAOrderSubInf);
            if(ordSub==null){
                throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310005);
            }
            //先从缓存获取分类value
            String lcPropVal = SysCfgUtil.fetchSysCfg("GDS_LC_CAT_CODE").getParaValue();
            String yyPropVal = SysCfgUtil.fetchSysCfg("GDS_YY_CAT_CODE").getParaValue();
            String yjkPropVal = SysCfgUtil.fetchSysCfg("GDS_YJK_CAT_CODE").getParaValue();
            Boolean lcFlag = false;
            Boolean yyFlag = false;
            Boolean yjkFlag = false;
            //判断类型
            GdsGds2CatgReqDTO gds2CatgReqDTO = new GdsGds2CatgReqDTO();
            gds2CatgReqDTO.setGdsId(ordSub.getGdsId());
            gds2CatgReqDTO.setCatgCode(lcPropVal);
            Boolean flag = gdsInfoQueryRSV.isBelongToCategory(gds2CatgReqDTO);
            lcFlag = flag;
            //初始化String
	        String str1 = "gdsId:"+ordSub.getGdsId()+",CatgCode:"+gds2CatgReqDTO.getCatgCode()+",flag:"+flag;
	        String str2 = "";
	        String str3 = "";
	        @SuppressWarnings("unused")
			String str4 = "";
	        //判断类型
	        if(!flag){
	        	gds2CatgReqDTO.setCatgCode(yyPropVal);
	        	flag = gdsInfoQueryRSV.isBelongToCategory(gds2CatgReqDTO);
	        	yyFlag = flag;
	        	//TODO
	            str2 = "gdsId:"+ordSub.getGdsId()+",CatgCode:"+gds2CatgReqDTO.getCatgCode()+",flag:"+flag;
	        }
	        //判断类型
	        if(!flag){
	            gds2CatgReqDTO.setCatgCode(yjkPropVal);
	            flag = gdsInfoQueryRSV.isBelongToCategory(gds2CatgReqDTO);
	            yjkFlag = flag;
	            //TODO
	            str3 = "gdsId:"+ordSub.getGdsId()+",CatgCode:"+gds2CatgReqDTO.getCatgCode()+",flag:"+flag;
	          //TODO
	        }
	        
	        //组装参数
	        String authUrl = "";
	        String staff_code = "";
	        String gds_detail = "";
	        String order_code = "";
	        String order_time = "";
	        String pay_time = "";
	        String pay_way = "";
	        //是否需要变更本条记录为为处理，只要更改状态为正在处理就一定要变为true
	        boolean updateToUndo = false;
	        //属于外部系统商品的        
            if(flag){
            	//authUrl
            	if(lcFlag){            		
            		BaseSysCfgRespDTO sysDTO = SysCfgUtil.fetchSysCfg(PayStatus.PAY_LC_MEDICARE_URL);
            		authUrl = sysDTO.getParaValue();
            	}else if(yyFlag){
            		BaseSysCfgRespDTO sysDTO = SysCfgUtil.fetchSysCfg(PayStatus.PAY_YY_MEDICARE_URL);
            		authUrl = sysDTO.getParaValue();
            	}else if(yjkFlag){
            		BaseSysCfgRespDTO sysDTO = SysCfgUtil.fetchSysCfg(PayStatus.PAY_YJK_MEDICARE_URL);
            		authUrl = sysDTO.getParaValue();
            	}
            	
            	//staff_code
                CustInfoResDTO custInfoResDTO = custManageRSV.findCustInfoById(ordSub.getStaffId());
                staff_code = custInfoResDTO.getStaffCode();
                
                //gds_detail
                com.alibaba.fastjson.JSONArray gdsDetails = new com.alibaba.fastjson.JSONArray();
                com.alibaba.fastjson.JSONObject gdsDetail = new com.alibaba.fastjson.JSONObject(4,true);
                
                //gds_detail:gds_code
	            LongReqDTO longReqDTO = new LongReqDTO();
	            longReqDTO.setId(ordSub.getGdsId());
	            List<GdsGds2PropRespDTO> gdsProps = gdsInfoQueryRSV.queryGds2PropsByGdsId(longReqDTO);
	            if(gdsProps!=null){
	            	for (GdsGds2PropRespDTO gdsGds2PropRespDTO : gdsProps) {
	    				if(gdsGds2PropRespDTO.getPropId() == Long.parseLong(SysCfgUtil.fetchSysCfg("GDS_OLD_CODE_PROP_ID").getParaValue())){
	    					gdsDetail.put("gds_code", gdsGds2PropRespDTO.getPropValue());
	    				}
	    			}
	            	
	            	//TODO
	                str4 = "OriginGdsCode:"+gdsDetail.getString("gds_code");
	                //TODO
	            }
                
	            gdsDetail.put("base_price", new StringBuffer().append(ordSub.getBasePrice()).toString());
	            gdsDetail.put("real_money", new StringBuffer().append(ordSub.getRealMoney()).toString());
	            gdsDetail.put("order_amount", new StringBuffer().append(ordSub.getOrderAmount()).toString());
	            
	            gdsDetails.add(gdsDetail);
                gds_detail = gdsDetails.toJSONString();
                LogUtil.info(MODULE, "gds_detail="+gds_detail);
                //order_code && order_time
                //OrdMain ordMain = ordMainSV.queryOrderByOrderId(paySuccInfo.getOrderId());
                order_code = ordSub.getId();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                order_time = sdf.format(ordSub.getOrderTime());
                
                //pay_time && pay_way
                OrdMain ordMain = ordMainSV.queryOrderByOrderId(paySuccInfo.getOrderId());
                if(ordMain != null){
                    pay_time = sdf.format(ordMain.getPayTime());
                    pay_way = ordMain.getPayWay();
                }
//                List<RPayResultResponse> payResults = payResultRSV.getPayResultByOrderId(paySuccInfo.getOrderId());
//                if(payResults.size()>0){
//                	RPayResultResponse payResult = payResults.get(0);
//                	pay_time = sdf.format(payResult.getPayTime());
//                	pay_way = payResult.getPayWay();
//                }
                
                //处理
                RPayQuartzInfoRequest payQuartzInfoDTO = new RPayQuartzInfoRequest();
                payQuartzInfoDTO.setTaskType(PayStatus.PAY_TASK_TYPE_05);
                payQuartzInfoDTO.setId(payQuartzInfo.getId());
                int i = payQuartzInfoSV.updateDealFlag(payQuartzInfoDTO, PayStatus.PAY_DEAL_FLAG_0, PayStatus.PAY_DEAL_FLAG_2);
                //更新状态成功
                if(i==1){
                	//更新状态成功后设置为需要变更为未处理
                    updateToUndo = true;
                    //捕获异常，出现异常把定时任务表正在处理状态变成未处理
                    try{
                    	AipExternalAuthRequest authRequest = new AipExternalAuthRequest();
                    	authRequest.setAuthUrl(authUrl);
                    	authRequest.setStaff_code(staff_code);
                    	authRequest.setGds_detail(gds_detail);
                    	authRequest.setOrder_code(order_code);
                    	authRequest.setOrder_time(order_time);
                    	authRequest.setPay_time(pay_time);
                    	authRequest.setPay_way(pay_way);
                    	
                    	Timestamp requestTime = DateUtil.getSysDate();
                    	AipExternalAuthResponse response = null;
                    	try{
                    		response = aipExternalAuthRSV.sendAuthRequest(authRequest);
                    	}catch(Exception e){
                    		LogUtil.error(MODULE, "外系统授权aip接口异常", e);
                    	}
	            		if(AipExternalAuthRequest._EXTERNAL_STATUS_OK.equals(response.getCode())){
	            			System.out.println("=========外系统发货接口进入");
	            			//调用发货接口
	                        RConfirmDeliveRequest rConfirmDeliveRequest = new RConfirmDeliveRequest();
	                        rConfirmDeliveRequest.setOrderId(payQuartzInfo.getOrderId());
	                        
	                        List<RConfirmSubInfo> rConfirmSubInfoList = new ArrayList<RConfirmSubInfo>();
	                        RConfirmSubInfo rConfirmSubInfo = new RConfirmSubInfo();
	                        rConfirmSubInfo.setOrderSubId(payQuartzInfo.getSubOrder());
	                        rConfirmSubInfo.setDeliveryAmount(ordSub.getOrderAmount());
	                        rConfirmSubInfo.setIsImport("0");
	                        
	                        rConfirmSubInfoList.add(rConfirmSubInfo);
	                        rConfirmDeliveRequest.setrConfirmSubInfo(rConfirmSubInfoList);
	                        rConfirmDeliveRequest.setRemark("外系统授权");
	                        rConfirmDeliveRequest.setStaffId(ordSub.getStaffId());
	                        rConfirmDeliveRequest.getStaff().setId(Common.DEFAULT_STAFFID);
	                        rConfirmDeliveRequest.setDeliveryType(OrdConstants.Order.ORDER_DELIVER_FLAG_FALSE);
	                        ordDeliveryRSV.orderDelivery(rConfirmDeliveRequest);
	                        //成功后删除定时任务
	                        payQuartzInfoSV.deletePayQuartzInfo(payQuartzInfo.getId());
	            		}
	            		Timestamp responseTime = DateUtil.getSysDate();
	                    PayIntfReqLogDTO log = new PayIntfReqLogDTO();
	                    log.setPayWay("");
	                    log.setOrderId(paySuccInfo.getOrderId());
	                    log.setStaffId(paySuccInfo.getStaffId());
	//               log.setRlRequestNo("");
	                    log.setRequestTime(requestTime);
	                    log.setResponseTime(responseTime);
	                    if(response!=null){
	                        if(StringUtil.isBlank(response.getRequestMessage())){
	                            log.setRequestMsg(JSON.toJSONString(authRequest));
	                        }else{
	                            log.setRequestMsg(response.getRequestMessage());
	                        }
	                        if(StringUtil.isBlank(response.getResponseMessage())){
	                            log.setResponseMsg(JSON.toJSONString(response));
	                        }else{
	                            log.setResponseMsg(response.getResponseMessage());
	                        }
	                    }
	                    payIntfReqLogSV.addPayExternalDigitalLog(log);
                    	
                    }catch(Exception e){
                    	LogUtil.error(MODULE, "外系统授权接口处理异常", e);
                    }
                }
            }else{
            	//不属于外部系统的订单直接删除定时任务表
                payQuartzInfoSV.deletePayQuartzInfo(payQuartzInfo.getId());
            }
            
            if(updateToUndo){
            	//把正在处理中的状态改到未处理
            	RPayQuartzInfoRequest payQuartzInfoRequest2 = new RPayQuartzInfoRequest();
                payQuartzInfoRequest2.setTaskType(PayStatus.PAY_TASK_TYPE_05);
                payQuartzInfoRequest2.setId(payQuartzInfo.getId());
                payQuartzInfoSV.updateDealFlag(payQuartzInfoRequest2, PayStatus.PAY_DEAL_FLAG_2, PayStatus.PAY_DEAL_FLAG_0);
            }
            //增加错误次数
            payQuartzInfoSV.addErrorTimes(payQuartzInfo.getId());
            
            //TODO
            OrdImportLog info = new OrdImportLog();
            info.setImportType("11");
            info.setOldOrderId(payQuartzInfo.getOrderId());
            info.setNewOrderId(payQuartzInfo.getSubOrder());
            info.setExceptionMsg(str1+";"+str2+";"+str3);
            ordImportLogSV.saveOrdImportLog(info);
            //TODO
            LogUtil.info(MODULE,"处理外系统授权结束,子订单号："+paySuccInfo.getSubOrder());
		}catch(Exception e){
			LogUtil.error(MODULE, "外系统授权接口处理异常,子订单："+paySuccInfo.getSubOrder(), e);
            throw e;
		}
	}
}
