package com.ai.ecp.pmph.facade.impl.eventual;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.aip.dubbo.interfaces.IOrderQueryRSV;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2CatgReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.order.dao.model.OrdSub;
import com.ai.ecp.order.dao.model.PayQuartzInfo;
import com.ai.ecp.order.dubbo.dto.RConfirmDeliveRequest;
import com.ai.ecp.order.dubbo.dto.RConfirmSubInfo;
import com.ai.ecp.order.dubbo.dto.SBaseAndSubInfo;
import com.ai.ecp.order.dubbo.dto.pay.PayIntfReqLogDTO;
import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdDeliveryRSV;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants.Common;
import com.ai.ecp.order.dubbo.util.OrdConstants.PayStatus;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayIntfReqLogSV;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayQuartzInfoSV;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthResponse;
import com.ai.ecp.pmph.aip.dubbo.interfaces.IAipZYAuthRSV;
import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.dubbo.util.PmphRealOriginalGdsCodeProcessor;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayZYDigitalSV;
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

public class PayZYDigitalSVImpl implements IPayZYDigitalSV {
    
    @Resource
    private IOrderQueryRSV orderQueryRSV;
    
    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;
    
    @Resource
    private IPayIntfReqLogSV payIntfReqLogSV;
    
    @Resource
    private IAipZYAuthRSV aipZYAuthRSV;
    
    @Resource
    private IPayQuartzInfoSV payQuartzInfoSV;
    
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
    
    private static final String MODULE = PayZYDigitalSVImpl.class.getName();
    
    @Override
    public void joinTransaction(JSONObject message, TransactionStatus status, String transactionName) {
        try {
            final PaySuccInfo paySuccInfo = JSON.parseObject(message.toString(), PaySuccInfo.class);
            LogUtil.info(MODULE,"PayZYDigitalSVImpl============="+paySuccInfo.toString());
            LogUtil.info(MODULE,"处理泽元数字教材开始，订单号："+paySuccInfo.getOrderId());
            
            List<OrdSub> OrdSubList = ordSubSV.queryOrderSubByOrderId(paySuccInfo.getOrderId());
            if(OrdSubList!=null&&!OrdSubList.isEmpty()){
                PaySuccInfo paySuccInfoPara = new PaySuccInfo();
                for(OrdSub ordSub:OrdSubList){
                    ObjectCopyUtil.copyObjValue(paySuccInfo, paySuccInfoPara, null, false);
                    paySuccInfoPara.setSubOrder(ordSub.getId());
                    deal(paySuccInfoPara);
                }
            }
            
            LogUtil.info(MODULE,"处理泽元数字教材结束，订单号："+paySuccInfo.getOrderId());
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "支付回调泽元数字教材授权接口处理异常", be);
            be.printStackTrace();
            status.setRollbackOnly();
            throw new BusinessException(be.getErrorCode());
        } catch (Exception e) {
            LogUtil.error(MODULE, "支付回调泽元数字教材授权接口处理异常", e);
            status.setRollbackOnly();
            throw new BusinessException(MsgConstants.PayServiceMsgCode.PAY_SERVER_310012);
        }
        
    }

    @Override
    public void deal(PaySuccInfo paySuccInfo) {
        try{
            LogUtil.info(MODULE,"处理泽元数字教材开始============="+paySuccInfo.toString());
            LogUtil.info(MODULE,"处理泽元数字教材开始，子订单号："+paySuccInfo.getSubOrder());
            
            //获取定时任务表数据
            RPayQuartzInfoRequest payQuartzInfoRequest = new RPayQuartzInfoRequest();
            payQuartzInfoRequest.setSubOrder(paySuccInfo.getSubOrder());
            payQuartzInfoRequest.setTaskType(PayStatus.PAY_TASK_TYPE_03);
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
            
            //判断类型
            GdsGds2CatgReqDTO gds2CatgReqDTO = new GdsGds2CatgReqDTO();
            gds2CatgReqDTO.setGdsId(ordSub.getGdsId());
            gds2CatgReqDTO.setCatgCode("1200");
            Boolean flag = gdsInfoQueryRSV.isBelongToCategory(gds2CatgReqDTO);
            
            //TODO
            String str1 = "gdsId:"+ordSub.getGdsId()+",CatgCode:"+gds2CatgReqDTO.getCatgCode()+",flag:"+flag;
            String str2 = "";
            String str3 = "";
            //TODO
            if(!flag){
                gds2CatgReqDTO.setCatgCode("1201");
                flag = gdsInfoQueryRSV.isBelongToCategory(gds2CatgReqDTO);
                //TODO
                str2 = "gdsId:"+ordSub.getGdsId()+",CatgCode:"+gds2CatgReqDTO.getCatgCode()+",flag:"+flag;
              //TODO
            }
            
            //组装参数
            String userName = "";
            String goodsId = "";
            String readType = "1";//1:正式，2：试用
            String orderSN = payQuartzInfo.getSubOrder();
            String authUrl = "";
            //是否需要变更本条记录为为处理，只要更改状态为正在处理就一定要变为true
            boolean updateToUndo = false;
            //属于数字教材的        
            if(flag){
                
                //userName
                CustInfoResDTO custInfoResDTO = custManageRSV.findCustInfoById(ordSub.getStaffId());
                userName = custInfoResDTO.getStaffCode();
                
                //goodsId
                GdsInterfaceGdsGidxReqDTO gdsInterfaceGdsGidxReqDTO = new GdsInterfaceGdsGidxReqDTO();
                gdsInterfaceGdsGidxReqDTO.setOrigin("09");
                gdsInterfaceGdsGidxReqDTO.setGdsId(ordSub.getGdsId());
                GdsInterfaceGdsGidxRespDTO gdsInterfaceGdsGidxRespDTO = gdsInterfaceGdsRSV.queryGdsInterfaceGdsGidxByEcpGdsId(gdsInterfaceGdsGidxReqDTO,new PmphRealOriginalGdsCodeProcessor());
                if(gdsInterfaceGdsGidxRespDTO!=null){
                    goodsId = gdsInterfaceGdsGidxRespDTO.getOriginGdsId();
                    
                    //TODO
                    str3 = "OriginGdsId:"+gdsInterfaceGdsGidxRespDTO.getOriginGdsId();
                    //TODO
                }
                
                //authUrl
                BaseSysCfgRespDTO sysDTO = SysCfgUtil.fetchSysCfg(PayStatus.PAY_ZYDIGITAL_URL);
                authUrl = sysDTO.getParaValue();
                
                //处理
                RPayQuartzInfoRequest payQuartzInfoDTO = new RPayQuartzInfoRequest();
                payQuartzInfoDTO.setTaskType(PayStatus.PAY_TASK_TYPE_03);
                payQuartzInfoDTO.setId(payQuartzInfo.getId());
                int i = payQuartzInfoSV.updateDealFlag(payQuartzInfoDTO, PayStatus.PAY_DEAL_FLAG_0, PayStatus.PAY_DEAL_FLAG_2);
                //更新状态成功
                if(i==1){
                    //更新状态成功后设置为需要变更为未处理
                    updateToUndo = true;
                    //捕获异常，出现异常把定时任务表正在处理状态变成未处理
                    try{
                        AipZYAuthRequest authRequest = new AipZYAuthRequest();
                        authRequest.setUserName(userName);
                        authRequest.setGoodsId(goodsId);
                        authRequest.setReadType(readType);
    //                    authRequest.setTime("");
                        authRequest.setOrderSN(orderSN);
                        authRequest.setAuthUrl(authUrl);
                        Timestamp requestTime = DateUtil.getSysDate();
                        AipZYAuthResponse response = null;
                        String responseMge = "";
                        try{
                            response = aipZYAuthRSV.sendAuthRequest(authRequest);
                        }catch(Exception e){
                            LogUtil.error(MODULE, "泽元数字教材授权aip接口异常", e);
                            responseMge=e.getMessage();
                        }
                        if(AipZYAuthRequest._ZVING_STATUS_OK.equals(response.getStatus())){
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
                            rConfirmDeliveRequest.setRemark("泽元数字教材授权");
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
                        }else{
                            log.setRequestMsg(JSON.toJSONString(authRequest));
                            log.setResponseMsg(responseMge);
                        }
                        payIntfReqLogSV.addPayZYDigitalLog(log);
                    }catch (Exception e) {
                        LogUtil.error(MODULE, "泽元数字教材授权接口处理异常", e);
                    }
                }
                
            }else{
                //不属于数字教材的订单直接删除定时任务表
                payQuartzInfoSV.deletePayQuartzInfo(payQuartzInfo.getId());
            }
            
            if(updateToUndo){
                //把正在处理中的状态改到未处理
                RPayQuartzInfoRequest payQuartzInfoRequest2 = new RPayQuartzInfoRequest();
                payQuartzInfoRequest2.setTaskType(PayStatus.PAY_TASK_TYPE_03);
                payQuartzInfoRequest2.setId(payQuartzInfo.getId());
                payQuartzInfoSV.updateDealFlag(payQuartzInfoRequest2, PayStatus.PAY_DEAL_FLAG_2, PayStatus.PAY_DEAL_FLAG_0);
            }
            
            
            //增加错误次数
            payQuartzInfoSV.addErrorTimes(payQuartzInfo.getId());
            
            //TODO
            OrdImportLog info = new OrdImportLog();
            info.setImportType("05");
            info.setOldOrderId(payQuartzInfo.getOrderId());
            info.setNewOrderId(payQuartzInfo.getSubOrder());
            info.setExceptionMsg(str1+";"+str2+";"+str3);
            ordImportLogSV.saveOrdImportLog(info);
            //TODO
            LogUtil.info(MODULE,"处理泽元数字教材结束,子订单号："+paySuccInfo.getSubOrder());
                
        }catch(Exception e){
            LogUtil.error(MODULE, "泽元数字教材授权接口处理异常,子订单："+paySuccInfo.getSubOrder(), e);
            throw e;
        }
       
    }

}

