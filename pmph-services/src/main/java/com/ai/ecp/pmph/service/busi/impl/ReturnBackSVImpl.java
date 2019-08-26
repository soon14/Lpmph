package com.ai.ecp.pmph.service.busi.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.order.dubbo.dto.*;
import org.apache.commons.collections.CollectionUtils;

import com.ai.ecp.coupon.dubbo.dto.req.CoupConsumeReqDTO;
import com.ai.ecp.coupon.dubbo.dto.req.CoupInfoReqDTO;
import com.ai.ecp.coupon.dubbo.dto.req.CoupOrdBackReqDTO;
import com.ai.ecp.coupon.dubbo.dto.resp.CoupConsumeRespDTO;
import com.ai.ecp.coupon.dubbo.dto.resp.CoupInfoRespDTO;
import com.ai.ecp.coupon.dubbo.dto.resp.CoupOrdNumBackRespDTO;
import com.ai.ecp.coupon.dubbo.dto.resp.OrdNumRespDTO;
import com.ai.ecp.coupon.dubbo.interfaces.ICoupDetailRSV;
import com.ai.ecp.coupon.dubbo.interfaces.ICoupRSV;
import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.order.dao.mapper.busi.OrdBackApplyMapper;
import com.ai.ecp.order.dao.mapper.busi.OrdBackDiscountMapper;
import com.ai.ecp.order.dao.mapper.busi.OrdBackGdsMapper;
import com.ai.ecp.order.dao.mapper.busi.OrdBackTrackMapper;
import com.ai.ecp.order.dao.mapper.busi.OrdSubMapper;
import com.ai.ecp.order.dao.model.OrdBackApply;
import com.ai.ecp.order.dao.model.OrdBackApplyCriteria;
import com.ai.ecp.order.dao.model.OrdBackDiscount;
import com.ai.ecp.order.dao.model.OrdBackDiscountCriteria;
import com.ai.ecp.order.dao.model.OrdBackGds;
import com.ai.ecp.order.dao.model.OrdBackGdsCriteria;
import com.ai.ecp.order.dao.model.OrdBackTrack;
import com.ai.ecp.order.dao.model.OrdBackTrackCriteria;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dao.model.OrdSub;
import com.ai.ecp.order.dao.model.ThingLock;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.order.dubbo.util.CommonConstants;
import com.ai.ecp.order.dubbo.util.LongUtils;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants.PayStatus;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackApplySV;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackCouponSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackDiscountSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackTrackSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.order.service.busi.interfaces.IThingLockSV;
import com.ai.ecp.pmph.dubbo.dto.CompensateBackResp;
import com.ai.ecp.pmph.dubbo.dto.OrdCompensateReq;
import com.ai.ecp.pmph.dubbo.dto.OrderBackMainRWReqDTO;
import com.ai.ecp.pmph.dubbo.dto.RBackApplyPmphOrdReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV;
import com.ai.ecp.pmph.service.busi.interfaces.IScoreToOrderRWSV;
import com.ai.ecp.pmph.service.busi.interfaces.IStaffUnionRWSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dao.model.ScoreInfo;
import com.ai.ecp.staff.dubbo.dto.OrderAcctMainResDTO;
import com.ai.ecp.staff.dubbo.dto.OrderAcctSubResDTO;
import com.ai.ecp.staff.dubbo.dto.OrderBackMainReqDTO;
import com.ai.ecp.staff.dubbo.dto.OrderBackSubReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.ecp.staff.service.busi.acct.interfaces.IAcctToOrderSV;
import com.ai.ecp.staff.service.busi.score.interfaces.IScoreInfoSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;


public class ReturnBackSVImpl implements IReturnBackSV{

    private static final String MODULE = ReturnBackSVImpl.class.getName();
    
    @Resource
    private IOrdBackApplySV ordBackApplySV;
    
    @Resource
    private IOrdMainSV ordMainSV;
    
    @Resource
    private IOrdSubSV ordSubSV;
    
    @Resource   
    private IOrdBackDiscountSV ordBackDiscountSV;
    
    @Resource
    private IStaffUnionRSV staffUnionRSV;
    
    @Resource   
    private ICoupDetailRSV coupDetailRSV;   
    
    @Resource
    private IAcctToOrderSV acctToOrderSV;
    
    @Resource
    private IScoreToOrderRWSV scoreToOrderRWSV;
    
    @Resource
    private IOrdBackCouponSV ordBackCouponSV;
    
    @Resource
    private IOrdBackTrackSV ordBackTrackSV;
    
    @Resource
    private OrdBackApplyMapper ordBackApplyMapper;
    
    @Resource
    private OrdBackGdsMapper ordBackGdsMapper;
    
    @Resource
    private OrdBackDiscountMapper ordBackDiscountMapper;
    
    @Resource
    private OrdSubMapper ordSubMapper;
    
    @Resource
    private IScoreInfoSV scoreInfoSV;
    
    @Resource
    private IStaffUnionRWSV staffUnionRWSV;

    @Resource
    private OrdBackTrackMapper ordBackTrackMapper;
    
    @Resource
    private IThingLockSV thingLockSV;
    
    @Resource
    private ICoupRSV coupRSV;
    /**
     * 
     * TODO 判断是否符合退款要求. 
     * @see com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV#queryBackOperChk(com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest)
     */
    @Override
    public ROrdCartsChkResponse queryBackOperChk(ROrderDetailsRequest info) {
        ROrdCartsChkResponse res=new ROrdCartsChkResponse();
        res.setStatus(false);
        String oper=info.getOper();
        switch (oper) {
        //退货申请
        case "00":
          //判断是否可以申请操作
            SRefundInfo sRefundInfo = this.ordBackApplySV.queryBackGdsStatus(info.getOrderId());
            if(sRefundInfo != null && "1".equals(sRefundInfo.getRefundStatus())){
                LogUtil.info(MODULE, "订单："+info.getOrderId()+"该订单已在退货流程中");
                res.setMsg("订单已在退货流程中");
                return res; 
            }else if(sRefundInfo!= null && "2".equals(sRefundInfo.getRefundStatus())){
                LogUtil.info(MODULE, "订单："+info.getOrderId()+"订单含有虚拟产品不允许退款");
                res.setMsg("订单含有虚拟产品不允许退款");
                return res;
            } 
            break;
          //退款申请
        case "01":
            SRefundInfo sRefundInfom = this.ordBackApplySV.queryRefundStatus(info.getOrderId());
            SOrderDetailsMain  sOrderDetailsMain = this.ordMainSV.queryOrderDetailsMain(info.getOrderId());
            if(("04".equals(sOrderDetailsMain.getStatus()))){  //部分发货
                LogUtil.info(MODULE, "订单："+info.getOrderId()+"订单已部分发货不允许退款");
                res.setMsg("订单已部分发货不允许退款");
                return res;
            }
            if(!("02".equals(sOrderDetailsMain.getStatus()))){  //不是待发货
                LogUtil.info(MODULE, "订单："+info.getOrderId()+"订单状态已更新");
                res.setMsg("订单状态已更新，请刷新页面");
                return res;
            }            
            if(sRefundInfom!= null && "1".equals(sRefundInfom.getRefundStatus())){
                LogUtil.info(MODULE, "订单："+info.getOrderId()+"该订单已在退款流程中");
                res.setMsg("订单已在退款流程中");
                return res; 
            }
            else if(sRefundInfom!= null && "2".equals(sRefundInfom.getRefundStatus())){
                LogUtil.info(MODULE, "订单："+info.getOrderId()+"订单含有虚拟产品不允许退款");
                res.setMsg("订单含有虚拟产品不允许退款");
                return res;
            } 
            break;
          //退货买家确认发货
        case "02":
            //校验申请状态是否正确
            ROrderBackReq rOrderBackReq = new ROrderBackReq();
            rOrderBackReq.setOrderId(info.getOrderId());
            rOrderBackReq.setBackId(info.getBackId());
            RBackApplyResp  chkResp =this.ordBackApplySV.queryOrdBackApply(rOrderBackReq);
            if(chkResp == null || !(BackConstants.ChkStatus.CHK_BACKGDS_SEND.contains(chkResp.getStatus()))){
                LogUtil.info(MODULE, "申请单状态不对"+info.getBackId()+chkResp.getStatus()+BackConstants.ChkStatus.CHK_BACKGDS_SEND);
                throw new BusinessException("申请单状态已更新，请刷新页面");
            }
            break;

        default:
            break;
        }
        res.setStatus(true);
        return res;
    }

    @Override
    public RBackApplyInfoResp calCulateShareApply(RBackApplyReq rBackApplyReq, OrdBackApply ordBackApply) throws BusinessException {
        String orderId = rBackApplyReq.getOrderId();
        // 如果是最后一笔退货，走最后一笔计算逻辑，如果不是，走正常的分摊计算
        RBackApplyInfoResp rBackApplyInfoResp = new RBackApplyInfoResp();
        rBackApplyInfoResp.setOrderId(ordBackApply.getOrderId());
        rBackApplyInfoResp.setBackId(ordBackApply.getId());
        rBackApplyInfoResp.setLastFlag("1".equals(ordBackApply.getIsEntire()));
        List<OrdSub> ordSubs = new ArrayList<OrdSub>();
        // 查询主订单的信息，取出realMoney
        OrdMain ordMain = ordMainSV.queryOrderByOrderId(orderId);
        Long siteId = ordMain.getSiteId();
        Long staffId = ordMain.getStaffId();
        
        Long discountMoneys = 0l;
        Long sumUsedScore = 0l;
        // 封装总的明细金额，总的使用积分
        List<OrderBackSubReqDTO> OrderBackSubReqDTOzList = new ArrayList<OrderBackSubReqDTO>();
        if (!CollectionUtils.isEmpty(rBackApplyReq.getrBackOrdSubReqs())) {
            for (RBackOrdSubReq rBackOrdSubReq:rBackApplyReq.getrBackOrdSubReqs()) {
                SBaseAndSubInfo sBaseAndSubInfo = new SBaseAndSubInfo();
                sBaseAndSubInfo.setOrderId(rBackApplyReq.getOrderId());
                sBaseAndSubInfo.setOrderSubId(rBackOrdSubReq.getOrderSubId());
                OrdSub ordSub = ordSubSV.findByOrderSubId(sBaseAndSubInfo);
                discountMoneys = discountMoneys + ordSub.getDiscountMoney();
                sumUsedScore = sumUsedScore + ordSub.getOrderSubScore();
                ordSubs.add(ordSub);
                // 封装积分查询入参
                OrderBackSubReqDTO OrderBackSubReqDTO = new OrderBackSubReqDTO();
                OrderBackSubReqDTO.setStaffId(staffId);
                OrderBackSubReqDTO.setOrderId(orderId);
                OrderBackSubReqDTO.setSubOrderId(rBackOrdSubReq.getOrderSubId());                
                SBaseAndSubInfo sOrderAOrderSubInfo = new SBaseAndSubInfo();
                sOrderAOrderSubInfo.setOrderSubId(rBackOrdSubReq.getOrderSubId());
                sOrderAOrderSubInfo.setOrderId(orderId);  
                OrderBackSubReqDTOzList.add(OrderBackSubReqDTO);                           
            }
        }       
        // 计算商品占总订单的分摊比例
        long scale = calCulateScaleApply(rBackApplyReq,ordBackApply,ordMain);
        rBackApplyInfoResp.setScale(scale);
        OrderBackMainReqDTO<OrderBackSubReqDTO> socreOrderBackMainReqDTO = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
        socreOrderBackMainReqDTO.setList(OrderBackSubReqDTOzList);
        socreOrderBackMainReqDTO.setStaffId(staffId);
        socreOrderBackMainReqDTO.setOrderId(orderId);
        socreOrderBackMainReqDTO.setScale(scale);
        socreOrderBackMainReqDTO.setSiteId(siteId);
        socreOrderBackMainReqDTO.setLastFlag(rBackApplyInfoResp.isLastFlag());
        // 计算退货申请的退款金额
        Long culateMoney = calCulateMoneyApply(rBackApplyReq,ordBackApply,ordMain,scale,rBackApplyInfoResp.isLastFlag());
        rBackApplyInfoResp.setBackMoney(culateMoney);
        // 积分信息封装开始
        // 分装退货积分信息
        RBackApplyScoreResp rBackApplyScoreResp = new RBackApplyScoreResp();
        // 设置订单退货扣减的积分----调用用户积分接口，查询用户需要扣减的积分--todo
        long sumReduCulateScore=scoreToOrderRWSV.selTotalScoreByBackOrderRW(socreOrderBackMainReqDTO);
        if(sumUsedScore.longValue()!=0 || sumReduCulateScore!=0){
            //设置订单退货增加的积分
            rBackApplyScoreResp.setSumUsedCulateScore(sumUsedScore);
            rBackApplyScoreResp.setSumReduCulateScore(sumReduCulateScore);
            //设置订单退货用户剩余的积分，调用用户积分接口，查询用户剩余积分
            ScoreInfoResDTO scoreInfoResDTO = staffUnionRSV.findScoreInfoByStaffId(staffId);
            if (!StringUtil.isEmpty(scoreInfoResDTO)) {
                rBackApplyScoreResp.setStaffScore(scoreInfoResDTO.getScoreBalance());
            }else{
                rBackApplyScoreResp.setStaffScore(0l);
            }
            rBackApplyInfoResp.setrBackApplyScoreResp(rBackApplyScoreResp);
        }
        // 积分信息封装结束
        // 资金账户信息封装开始
        RBackApplyAccResp rBackApplyAccResp = new RBackApplyAccResp();
        OrderBackMainReqDTO<OrderBackSubReqDTO> accinfoOrderBackMainReqDTO = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
        accinfoOrderBackMainReqDTO.setOrderId(orderId);
        accinfoOrderBackMainReqDTO.setStaffId(staffId);
        accinfoOrderBackMainReqDTO.setScale(scale);
        accinfoOrderBackMainReqDTO.setLastFlag(rBackApplyInfoResp.isLastFlag());
        //查询资金账户
        OrderAcctMainResDTO<OrderAcctSubResDTO> OrderAcctMainList=acctToOrderSV.selAcctByBackOrder(accinfoOrderBackMainReqDTO);        
        if(!StringUtil.isEmpty(OrderAcctMainList) && !CollectionUtils.isEmpty(OrderAcctMainList.getList())){
            rBackApplyAccResp.setSumUsedCulateAccList(OrderAcctMainList.getList());
            rBackApplyInfoResp.setrBackApplyAccResp(rBackApplyAccResp);
        }
        // 优惠劵封装开始
        boolean firstFlag=isFirstBackBatchApply(ordBackApply);
        if(firstFlag){
            CoupOrdBackReqDTO coupReq=new CoupOrdBackReqDTO();
            coupReq.setStaffId(staffId);
            coupReq.setOrderId(orderId);
            CoupOrdNumBackRespDTO coupRespList=coupDetailRSV.queryStaffCoup(coupReq);
            if(!StringUtil.isEmpty(coupRespList)){
                RBackApplyCoupResp rBackApplyCoupResp=new RBackApplyCoupResp();
                rBackApplyCoupResp.setBackApllyCoupList(coupRespList.getCoupNumBeans());
                rBackApplyInfoResp.setrBackApplyCoupResp(rBackApplyCoupResp);
            }
        }
        return rBackApplyInfoResp;
    }
    
    /**
     * 
     * TODO 计算退货总比例 
     * @see com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV#calCulateScaleApply(com.ai.ecp.order.dubbo.dto.RBackApplyReq, com.ai.ecp.order.dao.model.OrdBackApply, com.ai.ecp.order.dao.model.OrdMain)
     */
    @Override
    public long calCulateScaleApply(RBackApplyReq rBackApplyReq, OrdBackApply ordBackApply,OrdMain ordMain) throws BusinessException {

        //全部退货
        if(StringUtil.isNotBlank(rBackApplyReq.getCheckedAll())&&rBackApplyReq.getCheckedAll().equals("1")){
            return 1000000l;
        }        
        String orderId = ordBackApply.getOrderId();
        Long discountMoneys = 0l;
        Long amontMoney = ordSubSV.querySumDiscountMoney(orderId);
        //非全部退款时只允许单个子订单商品退款，因而子订单编号只有一个
        if (!CollectionUtils.isEmpty(rBackApplyReq.getrBackOrdSubReqs())) {
            for (RBackOrdSubReq rBackOrdSubReq : rBackApplyReq.getrBackOrdSubReqs()) {
                SBaseAndSubInfo sBaseAndSubInfo = new SBaseAndSubInfo();
                sBaseAndSubInfo.setOrderId(orderId);
                sBaseAndSubInfo.setOrderSubId(rBackOrdSubReq.getOrderSubId());
                OrdSub ordSub = ordSubSV.findByOrderSubId(sBaseAndSubInfo);
                discountMoneys = discountMoneys+ordSub.getDiscountMoney()*rBackOrdSubReq.getNum()/ordSub.getOrderAmount();
            }
        }        
        return (new BigDecimal(discountMoneys*1000000).divide(new BigDecimal(amontMoney),2)).longValue();       
    }

    /**
     * 
     * calCulateMoneyApply:(计算退款金额). <br/> 
     * 
     * @param rBackApplyReq
     * @param ordBackApply
     * @param ordMain
     * @param scale
     * @return 
     * @since JDK 1.6
     */
    private Long calCulateMoneyApply(RBackApplyReq rBackApplyReq, OrdBackApply ordBackApply, OrdMain ordMain,double scale,boolean isLastFlag) {
        Long realMoney=ordMain.getRealMoney();
        String applyType=rBackApplyReq.getApplyType();
        if(BackConstants.ApplyType.REFUND.equals(applyType)){
            return realMoney;
        }else{
            //全部退货
            if(StringUtil.isNotBlank(rBackApplyReq.getCheckedAll())&&rBackApplyReq.getCheckedAll().equals("1")||isLastFlag){
                Long result=calCulateLastApply(ordMain);
                return result;
            } else {                       
            	//主订单查	CoupConsume ，没有记录，按比例均摊
            	CoupConsumeReqDTO dto2 =new CoupConsumeReqDTO();
            	dto2.setOrderId(ordMain.getId());
            	PageResponseDTO<CoupConsumeRespDTO> page2 = coupDetailRSV.queryCoupConsumePage(dto2);
            	Boolean discountFlag = true;
            	if(null !=page2 && CollectionUtils.isNotEmpty(page2.getResult()) ){
            		List<CoupConsumeRespDTO> list = page2.getResult();
            		for (CoupConsumeRespDTO coupConsumeRespDTO : list) {
            			//查userRuleCode
    					CoupInfoReqDTO coInfo = new CoupInfoReqDTO();
    					coInfo.setId(coupConsumeRespDTO.getCoupId());
    					CoupInfoRespDTO coupInfo = coupRSV.queryCoupInfo(coInfo);
            			if(null!=coupInfo&&coupInfo.getUseRuleCode().contains("240")){
            				discountFlag = false;
            				break;
            			}
            		}
            	}
            	if(discountFlag){
            		 double money = realMoney*scale/1000000;    //实付金额*比例
                     LogUtil.info(MODULE, "主订单查coup为空");
                     return new BigDecimal(money).longValue();     
            	}
            	//2取子订单查询是否使用折扣劵 -补充逻辑
            	List<RBackOrdSubReq> subLs = rBackApplyReq.getrBackOrdSubReqs();
            	if(!CollectionUtils.isEmpty(subLs)){
            		Long backeMoney = 0L;
            		Boolean flag = true;
            		for(RBackOrdSubReq ls : subLs){
            			Long orderMoney = 0l;
            			Long orderAmount =1l;
            			Long num = ls.getNum();
            			Long oneMoney = 0l;//单价
            			//取订单原价
            			SBaseAndSubInfo subInfo = new SBaseAndSubInfo();
						subInfo.setOrderSubId(ls.getOrderSubId());
						subInfo.setOrderId(ordMain.getId());
						OrdSub ordsub = ordSubSV.findByOrderSubId(subInfo);
						if(null == ordsub){
							continue;
						}
						orderMoney = ordsub.getOrderMoney();
						orderAmount = ordsub.getOrderAmount();
						oneMoney = orderMoney/orderAmount;
						LogUtil.info(MODULE, "第二步退货计费子订单:ordsub"+ordsub.getId());
						LogUtil.info(MODULE, "第二步退货数量:num"+num);
						LogUtil.info(MODULE, "第二步退货单价:oneMoney"+oneMoney);
						CoupConsumeReqDTO dto =new CoupConsumeReqDTO();
            			dto.setOrderSubId(ls.getOrderSubId());
            			//子订单查 coupID
            			PageResponseDTO<CoupConsumeRespDTO> page = coupDetailRSV.queryCoupConsumePage(dto);
            			//没有优惠
            			if(null == page || CollectionUtils.isEmpty(page.getResult())){
            				double money = oneMoney*num;    //单价*个数     
							LogUtil.info(MODULE, "第二步4.1退货其他优惠路线:scale"+scale);
							Long back =  new BigDecimal(money).longValue(); 
							backeMoney +=back;
							flag = false;
            			}else{
            				if(page.getResult().size()==0){
            					double money = oneMoney*num;    //单价*个数     
    							LogUtil.info(MODULE, "第二步4.2退货其他优惠路线:scale"+scale);
    							Long back =  new BigDecimal(money).longValue(); 
    							backeMoney +=back;
    							flag = false;
            				
            				}else if(page.getResult().size()>0){
                				CoupConsumeRespDTO cupResp = page.getResult().get(0);
                				if(null != cupResp){
                					//查userRuleCode
                					CoupInfoReqDTO coInfo = new CoupInfoReqDTO();
                					coInfo.setId(cupResp.getCoupId());
                					CoupInfoRespDTO coupInfo = coupRSV.queryCoupInfo(coInfo);
                					if(null != coupInfo){
                						String useRuleCode = coupInfo.getUseRuleCode();
                						//userRuleCode包含240表示使用折扣劵
                						if(StringUtil.isNotBlank(useRuleCode) && useRuleCode.contains("240")){
                							Long coupValue = coupInfo.getCoupValue();//折扣率
                							LogUtil.info(MODULE, "第二步1退货折扣率路线:coupValue"+coupValue);
                							Long back = oneMoney*coupValue*num/10000;
                							backeMoney +=back;
                							flag = false;
                						}else{
                							//其他优惠
                							 double money = realMoney*scale/1000000;    //实付金额*比例
                							LogUtil.info(MODULE, "第二步2退货其他优惠路线:scale"+scale);
                							Long back =  new BigDecimal(money).longValue(); 
                							backeMoney +=back;
                							flag = false;
                						}
                					}
                				}
                			}
            			}

						
            		}
            		if(flag){
            			//3原来的逻辑：
                		//避免被向上取整出现9000xxx/1000000=10的情况
                        double money = realMoney*scale/1000000;    //实付金额*比例
                        LogUtil.info(MODULE, "第二步3退货原来逻辑路线:scale"+scale);
                        backeMoney = new BigDecimal(money).longValue();     
            		}
            		
            		return backeMoney;
            	}        
            }
        }
		return null;
    }
    
    public  Long calCulateLastApply(OrdMain ordMain)
            throws BusinessException {
        Long realMoney = ordMain.getRealMoney();
        Long usedbackMoney=0l;
        // 最后一次退货，金额取实付金额-已退金额,返回积分取下单使用返回积分，扣减积分取下单赠送扣减积分，百分比，资金账户取使用回退资金账户，和百分比
        ROrderBackReq rOrderBackReq=new ROrderBackReq();
        rOrderBackReq.setOrderId(ordMain.getId());
        List<RBackDiscountResp> rBackDiscountList=ordBackDiscountSV.queryOrdBackDiscountByOrderId(rOrderBackReq);
        if(!CollectionUtils.isEmpty(rBackDiscountList)){
            for(RBackDiscountResp rBackDiscountResp : rBackDiscountList){
                if(rBackDiscountResp.getDiscountType().equals("01")){
                    if(!LongUtils.isEmpty(rBackDiscountResp.getAmount())){
                        usedbackMoney=usedbackMoney+rBackDiscountResp.getAmount();
                    }
                }
            }
        }
        Long lastMoney=realMoney.longValue()-usedbackMoney.longValue();
        return lastMoney;
    }
    
    /**
     * 计算是否最后第一次退款退货
     *
     * @param
     * @param
     * @return
     */
    private boolean isFirstBackBatchApply(OrdBackApply ordBackApply) {
        // 返回1代表是最后一笔退款退货
        if (BackConstants.ApplyType.REFUND.equals(ordBackApply.getApplyType())) {
            return true;
        }
        String orderId = ordBackApply.getOrderId();
        // 查询已经退货的总数量
        OrdBackApplyCriteria ordBackApplyCriteria = new OrdBackApplyCriteria();
        ordBackApplyCriteria.createCriteria().andOrderIdEqualTo(orderId)
                .andStatusNotEqualTo(BackConstants.Status.REFUSE).andIdNotEqualTo(ordBackApply.getId());
        List<OrdBackApply> orderApplyList = ordBackApplyMapper
                .selectByExample(ordBackApplyCriteria);
        if(CollectionUtils.isEmpty(orderApplyList)){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 
     * calCulateBackInfo:(退货审核时查询当前退货积分和退款信息). <br/> 
     * 
     * @param req
     * @return 
     * @since JDK 1.6
     */
    public ROrdReturnRefundResp calCulateBackInfo (ROrderBackReq req){
        ROrdReturnRefundResp ordReturnRefundResp = new ROrdReturnRefundResp(); 
        SOrderDetailsMain sOrderDetailsMain = ordMainSV.queryOrderDetailsMain(req.getOrderId());
        OrdBackGdsCriteria ordBackGdsCriteria = new OrdBackGdsCriteria();
        ordBackGdsCriteria.createCriteria().andOrderIdEqualTo(req.getOrderId()).andBackIdEqualTo(req.getBackId());
        List<OrdBackGds> orderBackGdsList = ordBackGdsMapper.selectByExample(ordBackGdsCriteria);
        Long discountMoneys = 0l;
        for(OrdBackGds ordBackGds:orderBackGdsList){
            OrdSub ordsub = ordSubMapper.selectByPrimaryKey(ordBackGds.getOrderSubId());
            discountMoneys = discountMoneys+ordBackGds.getNum()*ordsub.getDiscountMoney()/ordsub.getOrderAmount();
        }
        boolean isLastFlag = isLastBackBatch(sOrderDetailsMain,orderBackGdsList);
        Long amontMoney = ordSubSV.querySumDiscountMoney(req.getOrderId());
        long scale = (new BigDecimal(discountMoneys*1000000).divide(new BigDecimal(amontMoney),2)).longValue();
        LogUtil.info(MODULE, "折扣信息:scale="+scale+"discountMoneys="+discountMoneys+"amontMoney"+amontMoney);
        ordReturnRefundResp.setScale(scale); 
        long backMoney = new BigDecimal(sOrderDetailsMain.getRealMoney()*scale/1000000).longValue();    
        ordReturnRefundResp.setBackMoney(backMoney);
        long curScore = 0l;
        ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(sOrderDetailsMain.getCreateStaff());
        if(scoreInfo!=null){        	
        	curScore = scoreInfo.getScoreTotal()-scoreInfo.getScoreUsed()-scoreInfo.getScoreFrozen();
        }
        ordReturnRefundResp.setCurScore(curScore);
        
        OrderBackMainReqDTO<OrderBackSubReqDTO> socreOrderBackMainReqDTO = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
        socreOrderBackMainReqDTO.setStaffId(sOrderDetailsMain.getCreateStaff());
        socreOrderBackMainReqDTO.setOrderId(sOrderDetailsMain.getId());
        socreOrderBackMainReqDTO.setScale(scale);
        socreOrderBackMainReqDTO.setSiteId(sOrderDetailsMain.getSiteId());
       
        socreOrderBackMainReqDTO.setLastFlag(isLastFlag);
        long sumReduCulateScore=0l;
        if(req.getBackId()!=null&&req.getBackId()>0){
            //判断是否有过调整记录
            OrdBackTrackCriteria criteria = new OrdBackTrackCriteria();
            criteria.createCriteria().andBackIdEqualTo(req.getBackId()).andOrderIdEqualTo(req.getOrderId()).andNodeEqualTo("99");
            long num = ordBackTrackMapper.countByExample(criteria);
            if(num>0){
                //重新计算调整后应减扣积分
                OrderBackMainReqDTO<OrderBackSubReqDTO> socreOrderBackMain = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
                socreOrderBackMain.setStaffId(sOrderDetailsMain.getCreateStaff());
                socreOrderBackMain.setOrderId(req.getOrderId());
                socreOrderBackMain.setScale(1000000l);
                socreOrderBackMain.setSiteId(sOrderDetailsMain.getSiteId());
                socreOrderBackMain.setLastFlag(true);
                
                //订单全部退货时减扣积分
                long totleScore=scoreToOrderRWSV.selTotalScoreByBackOrderAllRW(socreOrderBackMain);                    
                //实付金额
               // OrdMain ordMain = ordMainSV.queryOrderByOrderId(req.getOrderId());
                long realMoney = sOrderDetailsMain.getRealMoney();
                OrdBackApply ordBackApply  =ordBackApplyMapper.selectByPrimaryKey(req.getBackId());
                //计算新的冻结积分 
                sumReduCulateScore = (long) Math.floor(ordBackApply.getBackMoney()*totleScore*1.0/realMoney);
            }else{
                sumReduCulateScore=scoreToOrderRWSV.selTotalScoreByBackOrderAllRW(socreOrderBackMainReqDTO);
            }
        }else{
             sumReduCulateScore=scoreToOrderRWSV.selTotalScoreByBackOrderAllRW(socreOrderBackMainReqDTO);
        }
        ordReturnRefundResp.setReduCulateScore(sumReduCulateScore);
        ordReturnRefundResp.setLastFlag(isLastFlag);
        ordReturnRefundResp.setStaffId(sOrderDetailsMain.getCreateStaff());
        
        return ordReturnRefundResp;
    }
    
    public boolean isLastBackBatch(SOrderDetailsMain sOrderDetailsMain,List<OrdBackGds> orderBackGdsList) throws BusinessException {                
        String orderId = sOrderDetailsMain.getId();
        // 查询主订单的信息，取出总数量
        //OrdMain ordMain = ordMainSV.queryOrderByOrderId(orderId);
        Long orderAmounts = sOrderDetailsMain.getOrderAmount();
        // 查询本次退货申请的总数量
        Long applyOrderAmounts = 0l;
        if (!CollectionUtils.isEmpty(orderBackGdsList)) {
            for (int i=0;i<orderBackGdsList.size();i++) {
                applyOrderAmounts = applyOrderAmounts + orderBackGdsList.get(i).getNum();
            }
        }
        //查询已经退货的总数量
        OrdBackApplyCriteria ordBackApplyCriteria = new OrdBackApplyCriteria();
        ordBackApplyCriteria.createCriteria().andOrderIdEqualTo(orderId)
                .andStatusEqualTo(BackConstants.Status.REFUNDED);
        List<OrdBackApply> orderApplyList = ordBackApplyMapper
                .selectByExample(ordBackApplyCriteria);
        Long backOrderAmounts = 0l;
        if (!CollectionUtils.isEmpty(orderApplyList)) {
            for (OrdBackApply ordBackApply : orderApplyList) {
                backOrderAmounts = backOrderAmounts + ordBackApply.getNum();
            }
        }
        // 合计总的退货数量
        Long sumBackOrderAmounts = applyOrderAmounts + backOrderAmounts;
        if (orderAmounts.longValue() == sumBackOrderAmounts.longValue()) {
            return true;
        }
        
        return false;
    }


    /**
     * 
     * TODO 计算退款和退货时应退回的积分、资金账户和. 
     * @see com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV#calCulateBackInfo(java.util.List)
     */
    @Override
    public ROrdReturnRefundResp calCulateBackInfo(RBackApplyPmphOrdReq resp) {
        ROrdReturnRefundResp ordReturnRefundResp = new ROrdReturnRefundResp();
        
        if(resp.getApplyType().equals(BackConstants.ApplyType.BACK_GDS)){
            SOrderDetailsMain sOrderDetailsMain = ordMainSV.queryOrderDetailsMain(resp.getSOrderDetailsMain().getId());
            resp.setSOrderDetailsMain(sOrderDetailsMain);
        }
        boolean isLastFlag = isLastBackBatch(resp);
        long scale = calCulateScaleApply(resp);
        long backMoney = calCulateMoneyApply(resp, scale,isLastFlag);//返回退货金额
        long sumUsedScore = 0l;
        long curScore = 0l;
        ordReturnRefundResp.setScale(scale);
        ordReturnRefundResp.setBackMoney(backMoney);
       
        ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(resp.getSOrderDetailsMain().getCreateStaff());
        if(scoreInfo!=null){
        	curScore = scoreInfo.getScoreTotal()-scoreInfo.getScoreUsed()-scoreInfo.getScoreFrozen();
        }
        
        ordReturnRefundResp.setCurScore(curScore);
        //积分相关信息
        OrderBackMainReqDTO<OrderBackSubReqDTO> socreOrderBackMainReqDTO = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
        socreOrderBackMainReqDTO.setStaffId(resp.getSOrderDetailsMain().getCreateStaff());
        socreOrderBackMainReqDTO.setOrderId(resp.getSOrderDetailsMain().getId());
        socreOrderBackMainReqDTO.setScale(scale);
        socreOrderBackMainReqDTO.setSiteId(resp.getSOrderDetailsMain().getSiteId());
        socreOrderBackMainReqDTO.setLastFlag(isLastFlag);
     //   long sumReduCulateScore=scoreToOrderRWSV.selTotalScoreByBackOrderRW(socreOrderBackMainReqDTO);
        long sumReduCulateScore=scoreToOrderRWSV.selTotalScoreByBackOrderAllRW(socreOrderBackMainReqDTO);
        
        ordReturnRefundResp.setReduCulateScore(sumReduCulateScore);
        List<RBackDiscountResp> rBackDiscountResps = new ArrayList<RBackDiscountResp>();   
        List<RBackCouponResp> rBackCouponResps = new ArrayList<RBackCouponResp>();  
        if(sumReduCulateScore>0l){
            RBackDiscountResp ordBackDiscountResp = new RBackDiscountResp();
            ordBackDiscountResp.setOrderId(resp.getSOrderDetailsMain().getId());
            ordBackDiscountResp.setDiscountType(BackConstants.DiscountType.TYPE_SCORE_02);
            ordBackDiscountResp.setProcType(BackConstants.ProcType.GIVE_BAKC_1);                
            ordBackDiscountResp.setAmount(sumReduCulateScore);
            rBackDiscountResps.add(ordBackDiscountResp);
        }          
        if(!CollectionUtils.isEmpty(resp.getrBackApplyOrdSubResps())) {
            for (int i=0;i<resp.getrBackApplyOrdSubResps().size();i++) {
                SBaseAndSubInfo sBaseAndSubInfo = new SBaseAndSubInfo();
                sBaseAndSubInfo.setOrderId(resp.getSOrderDetailsMain().getId());
                sBaseAndSubInfo.setOrderSubId(resp.getrBackApplyOrdSubResps().get(i).getOrderSubId());
                OrdSub ordSub = ordSubSV.findByOrderSubId(sBaseAndSubInfo);
                sumUsedScore = sumUsedScore + ordSub.getOrderSubScore();
            }
            if(sumUsedScore>0l){
                RBackDiscountResp ordBackDiscountResp = new RBackDiscountResp();
                ordBackDiscountResp.setOrderId(resp.getSOrderDetailsMain().getId());
                ordBackDiscountResp.setDiscountType(BackConstants.DiscountType.TYPE_SCORE_02);
                ordBackDiscountResp.setProcType(BackConstants.ProcType.USE_BACK_0);                
                ordBackDiscountResp.setAmount(sumUsedScore);
                rBackDiscountResps.add(ordBackDiscountResp);
            }
        }
        
        //保存资金帐户相关信息
        OrderBackMainReqDTO<OrderBackSubReqDTO> accinfoOrderBackMainReqDTO = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
        accinfoOrderBackMainReqDTO.setOrderId(resp.getSOrderDetailsMain().getId());
        accinfoOrderBackMainReqDTO.setStaffId(resp.getSOrderDetailsMain().getCreateStaff());
        accinfoOrderBackMainReqDTO.setScale(scale);
        accinfoOrderBackMainReqDTO.setLastFlag(isLastFlag);
        OrderAcctMainResDTO<OrderAcctSubResDTO> OrderAcctMainList=acctToOrderSV.selAcctByBackOrder(accinfoOrderBackMainReqDTO);        
        if(!StringUtil.isEmpty(OrderAcctMainList) && OrderAcctMainList!=null){
            for(OrderAcctSubResDTO orderAcctSubResDTO:OrderAcctMainList.getList()){
                RBackDiscountResp ordBackDiscountResp = new RBackDiscountResp();
            
                ordBackDiscountResp.setOrderId(resp.getSOrderDetailsMain().getId());
                ordBackDiscountResp.setDiscountType(BackConstants.DiscountType.TYPE_ACCT_03);
                ordBackDiscountResp.setProcType(BackConstants.ProcType.USE_BACK_0);
                ordBackDiscountResp.setAcctType(orderAcctSubResDTO.getAcctType());
                ordBackDiscountResp.setAmount(orderAcctSubResDTO.getBackMoney());
                rBackDiscountResps.add(ordBackDiscountResp);
            }
        }  
      
        //保存优惠卷相关信息
      
        
        // 查询已经退货的总数量
        OrdBackApplyCriteria ordBackApplyCriteria = new OrdBackApplyCriteria();
        ordBackApplyCriteria.createCriteria().andOrderIdEqualTo(resp.getSOrderDetailsMain().getId())
                .andStatusNotEqualTo(BackConstants.Status.REFUSE);
        long num = ordBackApplyMapper.countByExample(ordBackApplyCriteria);
        if(num==0){
            CoupOrdBackReqDTO coupReq=new CoupOrdBackReqDTO();
            coupReq.setStaffId(resp.getSOrderDetailsMain().getCreateStaff());
            coupReq.setOrderId(resp.getSOrderDetailsMain().getId());            
            CoupOrdNumBackRespDTO coupRespList= coupDetailRSV.queryOrderCoup(coupReq);
            if(coupRespList != null && CollectionUtils.isNotEmpty(coupRespList.getCoupNumBeans())){
                for(OrdNumRespDTO ordNumRespDTO :coupRespList.getCoupNumBeans()){
                    if(ordNumRespDTO.getCoupBackNum() == null || ordNumRespDTO.getCoupBackNum() == 0){
                        continue;
                    }
                    RBackCouponResp ordBackCoupon = new RBackCouponResp();              
                    ordBackCoupon.setProcType(BackConstants.ProcType.GIVE_BAKC_1);
                    ordBackCoupon.setCouponTypeId(ordNumRespDTO.getCoupId());
                    ordBackCoupon.setCouponTypeName(ordNumRespDTO.getCoupName());
                    if(ordNumRespDTO.getCoupBackNum()>ordNumRespDTO.getCoupPresentNum()){
                        ordBackCoupon.setCouponCnt(ordNumRespDTO.getCoupPresentNum());
                    }else{
                        ordBackCoupon.setCouponCnt(ordNumRespDTO.getCoupBackNum());
                    }          
                    rBackCouponResps.add(ordBackCoupon);
                }
            }
        }
        ordReturnRefundResp.setrBackCouponResps(rBackCouponResps);
        ordReturnRefundResp.setrBackDiscountResps(rBackDiscountResps);
        return ordReturnRefundResp;
    }
    
    
    /**
     * 
     * TODO 计算退货总比例 
     * @see com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV#calCulateScaleApply(com.ai.ecp.order.dubbo.dto.RBackApplyReq, com.ai.ecp.order.dao.model.OrdBackApply, com.ai.ecp.order.dao.model.OrdMain)
     */
    public long calCulateScaleApply(RBackApplyPmphOrdReq resp) throws BusinessException {        
        //退款
        if(BackConstants.ApplyType.REFUND.equals(resp.getApplyType())){
            return 1000000l;
        }    
        String orderId = resp.getSOrderDetailsMain().getId();
        Long discountMoneys = 0l;
        Long amontMoney = ordSubSV.querySumDiscountMoney(orderId);
        //非全部退款时只允许单个子订单商品退款，因而子订单编号只有一个
        if (!CollectionUtils.isEmpty(resp.getrBackApplyOrdSubResps())) {
            for (int i=0;i<resp.getrBackApplyOrdSubResps().size();i++) {
                SBaseAndSubInfo sBaseAndSubInfo = new SBaseAndSubInfo();
                sBaseAndSubInfo.setOrderId(orderId);
                sBaseAndSubInfo.setOrderSubId(resp.getrBackApplyOrdSubResps().get(i).getOrderSubId());
                OrdSub ordSub = ordSubSV.findByOrderSubId(sBaseAndSubInfo);
                discountMoneys = discountMoneys+ordSub.getDiscountMoney()*resp.getrBackApplyOrdSubResps().get(i).getNum()/ordSub.getOrderAmount();
            }
        }        
        return (new BigDecimal(discountMoneys*1000000).divide(new BigDecimal(amontMoney),2)).longValue();       
    }
    
    /**
     * 
     * calCulateMoneyApply:(计算退款金额). <br/> 
     * 
     * @param
     * @param
     * @param
     * @param scale
     * @return 
     * @since JDK 1.6
     */
    private Long calCulateMoneyApply(RBackApplyPmphOrdReq resp,double scale,boolean isLastFlag) {
        Long realMoney=resp.getSOrderDetailsMain().getRealMoney();
        String applyType=resp.getApplyType();
        if(BackConstants.ApplyType.REFUND.equals(applyType)){
            return realMoney;
        }else{
            //1全部退货
            if(isLastFlag){
                OrdMain ordMain = new OrdMain();
                ordMain.setId(resp.getSOrderDetailsMain().getId());
                ordMain.setRealMoney(resp.getSOrderDetailsMain().getRealMoney());
                Long result=calCulateLastApply(ordMain);
                return result;
            } else {
		            //主订单查	CoupConsume ，没有记录，按比例均摊
	            	CoupConsumeReqDTO dto2 =new CoupConsumeReqDTO();
	            	dto2.setOrderId(resp.getSOrderDetailsMain().getId());
	            	PageResponseDTO<CoupConsumeRespDTO> page2 = coupDetailRSV.queryCoupConsumePage(dto2);
	            	Boolean discountFlag = true;
	            	if(null !=page2 && CollectionUtils.isNotEmpty(page2.getResult()) ){
	            		List<CoupConsumeRespDTO> list = page2.getResult();
	            		for (CoupConsumeRespDTO coupConsumeRespDTO : list) {
	            			//查userRuleCode
	    					CoupInfoReqDTO coInfo = new CoupInfoReqDTO();
	    					coInfo.setId(coupConsumeRespDTO.getCoupId());
	    					CoupInfoRespDTO coupInfo = coupRSV.queryCoupInfo(coInfo);
	            			if(null!=coupInfo&&coupInfo.getUseRuleCode().contains("240")){
	            				discountFlag = false;
	            				break;
	            			}
	            		}
	            	}
	            	if(discountFlag){
	            		 double money = realMoney*scale/1000000;    //实付金额*比例
	                     LogUtil.info(MODULE, "主订单查coup为空");
	                     return new BigDecimal(money).longValue();     
	            	}
            		//2取子订单查询是否使用折扣劵 -补充逻辑
	            	List<RBackApplyOrdSubResp> subLs = resp.getrBackApplyOrdSubResps();
	            	if(!CollectionUtils.isEmpty(subLs)){
	            		Long backeMoney = 0L;
	            		Boolean flag = true;
	            		for(RBackApplyOrdSubResp ls : subLs){
	            			Long orderMoney = 0l;
	            			Long orderAmount =1l;
	            			Long num = ls.getNum();
	            			Long oneMoney = 0l;
	            			//取订单原价
							SBaseAndSubInfo subInfo = new SBaseAndSubInfo();
							subInfo.setOrderSubId(ls.getOrderSubId());
							subInfo.setOrderId(resp.getSOrderDetailsMain().getId());
							OrdSub ordsub = ordSubSV.findByOrderSubId(subInfo);
							if(null == ordsub){
								continue;
							}
							orderMoney = ordsub.getOrderMoney();
							orderAmount = ordsub.getOrderAmount();
							oneMoney = orderMoney/orderAmount;
							LogUtil.info(MODULE, "退货计费子订单:ordsub"+ordsub.getId());
							LogUtil.info(MODULE, "退货数量:num"+num);
							LogUtil.info(MODULE, "退货单价:oneMoney"+oneMoney);
	            			CoupConsumeReqDTO dto =new CoupConsumeReqDTO();
	            			dto.setOrderSubId(ls.getOrderSubId());
	            			//查 coupID
	            			PageResponseDTO<CoupConsumeRespDTO> page = coupDetailRSV.queryCoupConsumePage(dto);
	            			//没有记录，退原价
	            			if(null == page || CollectionUtils.isEmpty(page.getResult())){
	            				double money = oneMoney*num;    //单价*退货个数
    							Long back =  new BigDecimal(money).longValue(); 
    							backeMoney +=back;
    							flag = false;
	            			}else{
	            				if(page.getResult().size()==0){
	            					double money = oneMoney*num;    //单价*退货个数
	    							Long back =  new BigDecimal(money).longValue(); 
	    							backeMoney +=back;
	    							flag = false;
	            				}else if(page.getResult().size()>0){
		            				CoupConsumeRespDTO cupResp = page.getResult().get(0);
		            				if(null != cupResp){
		            					//查userRuleCode
		            					CoupInfoReqDTO coInfo = new CoupInfoReqDTO();
		            					coInfo.setId(cupResp.getCoupId());
		            					CoupInfoRespDTO coupInfo = coupRSV.queryCoupInfo(coInfo);
		            					if(null != coupInfo){
		            						String useRuleCode = coupInfo.getUseRuleCode();
		            						//userRuleCode包含240表示使用折扣劵
		            						if(StringUtil.isNotBlank(useRuleCode) && useRuleCode.contains("240")){
		            							Long coupValue = coupInfo.getCoupValue();//折扣率
		            							LogUtil.info(MODULE, "1退货折扣率路线:coupValue"+coupValue);
		            							Long back = oneMoney*coupValue*num/10000;
		            							backeMoney +=back;
		            							flag = false;
		            						}else{
		            							//其他优惠
		            							 double money = realMoney*scale/1000000;    //实付金额*比例
		            							LogUtil.info(MODULE, "2退货其他优惠路线:scale"+scale);
		            							Long back =  new BigDecimal(money).longValue(); 
		            							backeMoney +=back;
		            							flag = false;
		            						}
		            					}
		            				}
		            			}
	            			}

	                	}
	            		if(flag){
	            			//3原来的逻辑：
	                		//避免被向上取整出现9000xxx/1000000=10的情况
	                        double money = realMoney*scale/1000000;    //实付金额*比例
	                        LogUtil.info(MODULE, "3退货原来逻辑路线:scale"+scale);
	                        backeMoney = new BigDecimal(money).longValue();     
	            		}
	            		
	            		return backeMoney;
	            	}
            	}
        }
		return null;
    }
    
    public boolean isLastBackBatch(RBackApplyPmphOrdReq resp) throws BusinessException {
        // 返回1代表是最后一笔退款退货
        if (BackConstants.ApplyType.REFUND.equals(resp.getApplyType())) {
            return true;
        }                 
        String orderId = resp.getSOrderDetailsMain().getId();
        // 查询主订单的信息，取出总数量
        //OrdMain ordMain = ordMainSV.queryOrderByOrderId(orderId);
        Long orderAmounts = resp.getSOrderDetailsMain().getOrderAmount();
        // 查询本次退货申请的总数量
        Long applyOrderAmounts = 0l;
        if (!CollectionUtils.isEmpty(resp.getrBackApplyOrdSubResps())) {
            for (int i=0;i<resp.getrBackApplyOrdSubResps().size();i++) {
                applyOrderAmounts = applyOrderAmounts + resp.getrBackApplyOrdSubResps().get(i).getNum();
            }
        }
        //查询已经退货的总数量
        OrdBackApplyCriteria ordBackApplyCriteria = new OrdBackApplyCriteria();
        ordBackApplyCriteria.createCriteria().andOrderIdEqualTo(orderId)
                .andStatusEqualTo(BackConstants.Status.REFUNDED);
        List<OrdBackApply> orderApplyList = ordBackApplyMapper
                .selectByExample(ordBackApplyCriteria);
        Long backOrderAmounts = 0l;
        if (!CollectionUtils.isEmpty(orderApplyList)) {
            for (OrdBackApply ordBackApply : orderApplyList) {
                backOrderAmounts = backOrderAmounts + ordBackApply.getNum();
            }
        }
        // 合计总的退货数量
        Long sumBackOrderAmounts = applyOrderAmounts + backOrderAmounts;
        if (orderAmounts.longValue() == sumBackOrderAmounts.longValue()) {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean modifyBackMoney(ROrdReturnRefundReq req) {

       // OrdBackApply ordBackApply  =ordBackApplyMapper.selectByPrimaryKey(req.getBackId());
       
        long score = 0l;
        boolean isSuccess = false;
        try{               
           // if(req.getReduCulateScore()==0){
            //    isSuccess = true;
            //}else{
                OrderBackMainReqDTO<OrderBackSubReqDTO> socreOrderBackMainReq = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
                socreOrderBackMainReq.setStaffId(req.getStaffId());
                socreOrderBackMainReq.setOrderId(req.getOrderId());
                socreOrderBackMainReq.setScale(1000000l);
                socreOrderBackMainReq.setSiteId(req.getCurrentSiteId());
                socreOrderBackMainReq.setLastFlag(false);
                
                //订单全部退货时减扣积分
                long sumReduCulateScore=scoreToOrderRWSV.selTotalScoreByBackOrderAllRW(socreOrderBackMainReq);                    
                //实付金额
                OrdMain ordMain = ordMainSV.queryOrderByOrderId(req.getOrderId());
                long realMoney = ordMain.getRealMoney();
               // Long amontMoney = ordSubSV.querySumDiscountMoney(req.getOrderId());
                //查询日志，判断是否首次调整
                //计算新的冻结积分   
                score = (long) Math.floor(req.getModifyBackMoney()*sumReduCulateScore*1.0/realMoney);
                long curScore = 0l;
                //查询用户当前积分
                ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(req.getStaffId());
                if(scoreInfo!=null){
                	curScore = scoreInfo.getScoreTotal()-scoreInfo.getScoreUsed()-scoreInfo.getScoreFrozen();
                }
                
                if(curScore+req.getReduCulateScore()<score){
                    score = curScore+req.getReduCulateScore();
                }
                
                //积分相关信息
                OrderBackMainRWReqDTO<OrderBackSubReqDTO> socreOrderBackMainReqDTO = new OrderBackMainRWReqDTO<OrderBackSubReqDTO>();              
                socreOrderBackMainReqDTO.setOrderId(req.getOrderId());//订单号
                socreOrderBackMainReqDTO.setBackId(req.getBackId());//退款退货编号
                socreOrderBackMainReqDTO.setBackScore(req.getReduCulateScore());//默认冻结积分
                socreOrderBackMainReqDTO.setModifyBackSocre(score);//调整后的冻结积分
                socreOrderBackMainReqDTO.setScale(req.getScale());//退货退款默认比例            
                socreOrderBackMainReqDTO.setStaffId(req.getStaffId());//买家
                socreOrderBackMainReqDTO.setLastFlag(req.isLastFlag());//是否最后一笔      
                //修改冻结积分
                Long ce = staffUnionRWSV.saveScoreFrozenModifyForOrderBackRW(socreOrderBackMainReqDTO);
                //差额为0，则未调整
                if(ce!=null&&ce.longValue()==0l){
                    score=req.getReduCulateScore();
                }
              
                isSuccess = true;
           // }
            if(isSuccess){
              
                //保存退款金额
                OrdBackApply record = new OrdBackApply();
                record.setBackMoney(req.getModifyBackMoney());
                record.setId(req.getBackId());
                record.setOrderId(req.getOrderId());
                record.setBackScore(score);
                
                
                OrdBackApplyCriteria criteria = new OrdBackApplyCriteria();
                criteria.createCriteria().andIdEqualTo(req.getBackId())
                .andOrderIdEqualTo(req.getOrderId());
                ordBackApplyMapper.updateByExampleSelective(record, criteria);
               
                
              //修改资金账户表中退款额信息
                OrdBackDiscount ordBackDiscount = new OrdBackDiscount();
                ordBackDiscount.setBackId(req.getBackId());
                ordBackDiscount.setOrderId(req.getOrderId());
                ordBackDiscount.setAmount(req.getModifyBackMoney());
                
                OrdBackDiscountCriteria discountCriteria = new OrdBackDiscountCriteria();
                discountCriteria.createCriteria().andBackIdEqualTo(req.getBackId()).andOrderIdEqualTo(req.getOrderId())
                .andDiscountTypeEqualTo(BackConstants.DiscountType.TYPE_MONEY_01);
                ordBackDiscountMapper.updateByExampleSelective(ordBackDiscount, discountCriteria);
                
               // if(!ordBackApply.getApplyType().equals(BackConstants.ApplyType.REFUND)){      
                    //修改积分信息
                    OrdBackDiscount ordBackDiscount1 = new OrdBackDiscount();
                    ordBackDiscount1.setBackId(req.getBackId());
                    ordBackDiscount1.setOrderId(req.getOrderId());
                    ordBackDiscount1.setAmount(score);
                    
                    OrdBackDiscountCriteria discountCriteria1 = new OrdBackDiscountCriteria();
                    discountCriteria1.createCriteria().andBackIdEqualTo(req.getBackId()).andOrderIdEqualTo(req.getOrderId())
                    .andDiscountTypeEqualTo(BackConstants.DiscountType.TYPE_SCORE_02);
                    ordBackDiscountMapper.updateByExampleSelective(ordBackDiscount1, discountCriteria1);
                }
                
                //保存操作信息
                OrdBackTrack ordBackTrack = new OrdBackTrack();
                ordBackTrack.setBackId(req.getBackId());
                ordBackTrack.setOrderId(req.getOrderId());
                ordBackTrack.setNode("99");
                ordBackTrack.setNodeDesc("调整退款金额"); 
                double backMoney = req.getBackMoney()/100.00;
                double modifyBackMoney = req.getModifyBackMoney()/100.00;
                //组装操作信息
                String memo = "退款金额由"+backMoney+"调整为"+modifyBackMoney+",调整原因:"+req.getMemo();
                ordBackTrack.setRemark(memo);
                
                ordBackTrack.setCreateStaff(req.getStaff().getId());
                ordBackTrack.setCreateTime(DateUtil.getSysDate());
                ordBackTrackSV.saveOrdBackTrack(ordBackTrack);
            //}
            return isSuccess;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 保存补偿性退款信息
     * @see com.ai.ecp.pmph.service.busi.interfaces.IReturnBackSV#saveCompensateBackMoney(com.ai.ecp.pmph.dubbo.dto.OrdCompensateReq)
     */
    @Override
    public void saveCompensateBackMoney(OrdCompensateReq req){
        ThingLock thingLock = new ThingLock();
        thingLock.setId(req.getOrderId());
        thingLock.setType(CommonConstants.LockType.LOCK_BACK_MONEY_APPLY);
        //加锁
        thingLockSV.addThingLock(thingLock);
        try{
          OrdMain om = this.ordMainSV.queryOrderByOrderId(req.getOrderId());
          OrdBackApply ordBackApply = new OrdBackApply();
          ordBackApply.setStaffId(om.getStaffId());
          ordBackApply.setShopId(om.getShopId());
          ordBackApply.setSiteId(om.getSiteId());
          ordBackApply.setOrderId(req.getOrderId());
          ordBackApply.setApplyType(BackConstants.ApplyType.REFUND);
          ordBackApply.setStatus(BackConstants.Status.APPLY);
          ordBackApply.setApplyTime(DateUtil.getSysDate());
          ordBackApply.setIsEntire("0");
          ordBackApply.setNum(0l);
          ordBackApply.setCreateStaff(req.getStaff().getId());
          ordBackApply.setCreateTime(DateUtil.getSysDate());
          ordBackApply.setUpdateStaff(null);
          ordBackApply.setUpdateTime(null);
          ordBackApply.setIsInAuditFile(PayStatus.ORD_MAIN_IS_IN_AUDIT_FILE_0);
          ordBackApply.setBackMoney(req.getBackMoney());
          ordBackApply.setIsCompenstate("0"); //0：是，1：否
          if(StringUtil.isNotBlank(om.getPayTranNo())){
        	  ordBackApply.setPayTranNo(om.getPayTranNo());
          }
          LogUtil.debug(MODULE, JSON.toJSONString(ordBackApply));         
          Long backId = ordBackApplySV.saveOrdBackApply(ordBackApply);
          ordBackApply.setId(backId);

          //更新订单主表的二级状态为退款流程中22193666
          OrdMain ordMain = new OrdMain();
          ordMain.setId(req.getOrderId());
          ordMain.setShopId(om.getShopId());
          ordMain.setStaffId(om.getStaffId());
          ordMain.setOrderTwoStatus(OrdConstants.OrderTwoStatus.STATUS_REFUND_YES);
          ordMainSV.updateOrderTwoStatusByOrderId(ordMain,req.getStaff().getId());

          //保存申请日志
          OrdBackTrack ordBackTrack = new OrdBackTrack();
          ordBackTrack.setBackId(backId);
          ordBackTrack.setOrderId(req.getOrderId());
          ordBackTrack.setNode(BackConstants.Status.APPLY);
          ordBackTrack.setNodeDesc("提交补偿性退款申请");
          ordBackTrack.setCreateStaff(req.getStaff().getId());
          ordBackTrack.setCreateTime(DateUtil.getSysDate());
          ordBackTrack.setRemark(req.getMemo());
          this.ordBackTrackSV.saveOrdBackTrack(ordBackTrack);
        }catch(Exception e){
            throw e;
        }
        
        //解锁
        this.thingLockSV.removeThingLock(thingLock);
        
    }

    @Override
    public CompensateBackResp queryCompensateBackMoney(ROrderBackReq req) {
        CompensateBackResp resp = new CompensateBackResp();
        
        //主订单信息
        OrdMain om = this.ordMainSV.queryOrderByOrderId(req.getOrderId());
        SOrderDetailsMain sOrderDetailsMain = new SOrderDetailsMain();
        ObjectCopyUtil.copyObjValue(om, sOrderDetailsMain, "", false);
        resp.setSOrderDetailsMain(sOrderDetailsMain);
        
        //子订单信息
        List<SOrderDetailsSub> orderDetailsSubs = ordSubSV.queryOrderDetailsSub(req.getOrderId());
        resp.setOrderDetailsSubs(orderDetailsSubs);
        
        //订单申请信息
        OrdBackApply ordBackApply = ordBackApplySV.queryOrdBackApplyByBackId(req.getBackId());
        RBackApplyResp backApplyResp = new RBackApplyResp();
        ObjectCopyUtil.copyObjValue(ordBackApply, backApplyResp, "", false);
        backApplyResp.setBackId(ordBackApply.getId());
        resp.setBackApplyResp(backApplyResp);
        
        //操作日志
        ROrderBackReq rOrderBackReq = new ROrderBackReq();
        rOrderBackReq.setBackId(req.getBackId());
        rOrderBackReq.setOrderId(req.getOrderId());
        List<RBackTrackResp> rBackTrackResps = ordBackTrackSV.queryOrdBackTrack(rOrderBackReq);
        resp.setBackTrackResps(rBackTrackResps);
        return resp;
    }
    
    
}



