package com.ai.ecp.pmph.service.busi.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.ecp.order.dao.model.OrdMainCriteria.Criteria;
import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.order.dao.mapper.busi.OrdBackApplyMapper;
import com.ai.ecp.order.dao.mapper.busi.OrdMainMapper;
import com.ai.ecp.order.dao.mapper.busi.OrdMainStaffIdxMapper;
import com.ai.ecp.order.dao.model.OrdBackApply;
import com.ai.ecp.order.dao.model.OrdBackApplyCriteria;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dao.model.OrdMainCriteria;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrderBackReq;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.dto.SCustomerOrdMain;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsMain;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsSub;
import com.ai.ecp.order.dubbo.dto.SOrderIdx;
import com.ai.ecp.order.dubbo.dto.SRefundInfo;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.order.dubbo.util.MsgConstants.ServiceMsgCode;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackApplySV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainStaffIdxSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.pmph.dubbo.dto.OrdMainCompensateResponse;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundReq;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdPmphMainSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

public class OrdPmphMainSVImpl extends GeneralSQLSVImpl implements IOrdPmphMainSV {

    @Resource
    private IOrdMainStaffIdxSV ordMainStaffIdxSV;
    
    @Resource
    private IOrdMainSV ordMainSV;
    
    @Resource
    private IOrdSubSV ordSubSV;
    
    @Autowired(required = false)
    private IOrdBackApplySV ordBackApplySV;
    
    @Resource
    private OrdMainStaffIdxMapper ordMainStaffIdxMapper;
    
    @Resource
    private OrdMainMapper ordMainMapper;
    
    @Resource
    private OrdBackApplyMapper ordBackApplyMapper;
    
    @Resource
    private IStaffUnionRSV staffUnionRSV;
    
 
    
    private static final String MODULE = OrdPmphMainSVImpl.class.getName();
    
    @Override
    public PageResponseDTO<RCustomerOrdResponse> queryOrderByStaffIdPage(
            RQueryOrderRequest rQueryOrderRequest) {

        PageResponseDTO<RCustomerOrdResponse> pageResponse = PageResponseDTO
                .buildByBaseInfo(rQueryOrderRequest, RCustomerOrdResponse.class);
        pageResponse.setResult(new ArrayList<RCustomerOrdResponse>());

        PageResponseDTO<SOrderIdx> psoi = this.ordMainStaffIdxSV
                .queryOrderByStaffIdPage(rQueryOrderRequest);
        pageResponse.setCount(psoi.getCount());
        pageResponse.setPageCount(psoi.getPageCount());
        if (CollectionUtils.isEmpty(psoi.getResult())) {
            LogUtil.info(MODULE, "未找到订单数据");
            pageResponse.setResult(null);
            return pageResponse;
            // throw new BusinessException(MsgConstants.ProMsgCode.ORD_PRO_312000);
        } else {
            List<RCustomerOrdResponse> rcor = queryByOrderIdForCustomer(psoi.getResult());
            pageResponse.getResult().addAll(rcor);
        }
        return pageResponse;
    }
    
    private List<RCustomerOrdResponse> queryByOrderIdForCustomer(List<SOrderIdx> sOrderIdxs) {
        List<RCustomerOrdResponse> rcors = new ArrayList<RCustomerOrdResponse>();
        for (SOrderIdx soi : sOrderIdxs) {
            RCustomerOrdResponse rcor = new RCustomerOrdResponse();

            // 主订单相关字段
            SOrderDetailsMain sodm = ordMainSV.queryOrderDetailsMain(soi.getOrderId());
            SCustomerOrdMain sCustomerOrdMain = new SCustomerOrdMain();
            ObjectCopyUtil.copyObjValue(sodm, sCustomerOrdMain, null, false);
            sCustomerOrdMain.setOrderId(soi.getOrderId());
            getBackStatus(sCustomerOrdMain);
            rcor.setsCustomerOrdMain(sCustomerOrdMain);

            // 子订单相关字段
            List<SOrderDetailsSub> sOrderDetailsSubs = this.ordSubSV
                    .queryOrderDetailsSub(soi.getOrderId());
            rcor.setsOrderDetailsSubs(sOrderDetailsSubs);
            rcors.add(rcor);
        }
        return rcors;
    }
    
    /** 
     * getBackStatus:获取退货退款状态. <br/> 
     * @since JDK 1.6 
     */ 
    private void getBackStatus(SCustomerOrdMain sCustomerOrdMain){
        
        if(sCustomerOrdMain.getPayTime() == null){
            sCustomerOrdMain.setRefundStatus("0");//可以退款
            sCustomerOrdMain.setBackGdsStatus("0");//可以退货
            return;
        }
        
        //退款判断
        SRefundInfo sRefundInfo = this.ordBackApplySV.queryRefundStatus(sCustomerOrdMain.getOrderId());
        if(sRefundInfo != null) {
            if("1".equals(sRefundInfo.getRefundStatus())){
                sCustomerOrdMain.setRefundStatus("1");  //退款流程中
                sCustomerOrdMain.setRefundId(sRefundInfo.getRefundId());
            } else if("2".equals(sRefundInfo.getRefundStatus())){
                sCustomerOrdMain.setRefundStatus("2");  //订单含有虚拟产品不允许退款
               // sCustomerOrdMain.setBackGdsStatus("2");  //订单含有虚拟产品不允许退货
            }
        } else {
            sCustomerOrdMain.setRefundStatus("0");  //可以退款
        }
        
        
      
        
        //退货判断
        SRefundInfo sbackGdsInfo = ordBackApplySV.queryBackGdsStatus(sCustomerOrdMain.getOrderId());
        if(sbackGdsInfo != null){
            if("1".equals(sbackGdsInfo.getRefundStatus())){
                sCustomerOrdMain.setBackGdsStatus("1");  //退货流程中
                sCustomerOrdMain.setBackGdsId(sbackGdsInfo.getRefundId());
            }else if("2".equals(sbackGdsInfo.getRefundStatus())){
                sCustomerOrdMain.setBackGdsStatus("2");  //除虚拟商品外其他已经退光光
            }
        }else {
            //获取允许退货最长时间
            String onLineHourStr = BaseParamUtil.fetchParamValue("ORD_PAY_LIMIT", "2");
            int onLineHour = 0;
            try{
                onLineHour=Integer.parseInt(onLineHourStr);
            }catch(Exception e){
                LogUtil.error(MODULE, "设置超时取消时间错误", e);
                throw new BusinessException(ServiceMsgCode.ORD_SERVER_350012);
            }
            Timestamp onLineLimitTime = DateUtil.getSysDate();
            Calendar calendar=Calendar.getInstance();   
            calendar.setTime(onLineLimitTime); 
            calendar.add(Calendar.HOUR, -onLineHour);
            onLineLimitTime = new Timestamp(calendar.getTimeInMillis());
            //下单时间 --可以考虑用收货时间
            if(sCustomerOrdMain.getDeliverDate()!= null && sCustomerOrdMain.getDeliverDate().compareTo(onLineLimitTime) < 0 ){
                sCustomerOrdMain.setBackGdsStatus("2");  //超过退货时限
            } else {
                sCustomerOrdMain.setBackGdsStatus("0"); //可以退货
            }
        }
        
        OrdBackApply ordBackApply = new OrdBackApply();
        ordBackApply.setOrderId(sCustomerOrdMain.getOrderId());
        List<OrdBackApply> ordBackApplys =  ordBackApplySV.queryHasBackOrdBackApplyByOrderID(ordBackApply);
        long hasbackmoney = 0l;
        for(OrdBackApply orderBackApply:ordBackApplys){
            hasbackmoney +=orderBackApply.getBackMoney();
        }
        if(sCustomerOrdMain.getRealMoney()<=hasbackmoney){
            //金额已经全部退光光
            sCustomerOrdMain.setRefundStatus("2"); 
            sCustomerOrdMain.setBackGdsStatus("2");
        }
        
        OrdBackApplyCriteria criteria = new OrdBackApplyCriteria();
        criteria.createCriteria().andOrderIdEqualTo(sCustomerOrdMain.getOrderId()).andIsCompenstateEqualTo("0")
        .andStatusNotEqualTo(BackConstants.Status.REFUSE);
        
        long num = ordBackApplyMapper.countByExample(criteria);
        if(num>0){
            //有过补偿性退款的订单不允许退款、不允许退货
            sCustomerOrdMain.setRefundStatus("2"); 
            sCustomerOrdMain.setBackGdsStatus("2");
        }
    }
    
    @Override
    public PageResponseDTO<OrdMainCompensateResponse> queryOrderByCompensatePage(
            RQueryOrderRequest rQueryOrderRequest) {
        PageResponseDTO<OrdMainCompensateResponse> page = this.queryByCompensatePage(rQueryOrderRequest);
        if(page.getResult()!=null){
            for(int i=0;i<page.getResult().size();i++){
                OrdMainCompensateResponse ordMain = page.getResult().get(i);
                //判断是否进行过退款退货
                OrdBackApplyCriteria criteria = new OrdBackApplyCriteria();         
                criteria.createCriteria().andOrderIdEqualTo(ordMain.getId()).andStatusNotEqualTo(BackConstants.Status.REFUSE);
                long num = ordBackApplyMapper.countByExample(criteria);
                if(num==0l){
                    page.getResult().get(i).setHasBack(false);
                }else{
                    page.getResult().get(i).setHasBack(true);
                }
                CustInfoResDTO custInfoResDTO = this.staffUnionRSV.findCustOrAdminByStaffId(ordMain.getStaffId());
                if(custInfoResDTO != null && custInfoResDTO.getStaffCode() != null){
                    page.getResult().get(i).setStaffName(custInfoResDTO.getStaffCode());
                } else {
                    page.getResult().get(i).setStaffName(ordMain.getStaffId().toString());
                }
            }       
        }     
        return page;
    }

 
    private PageResponseDTO<OrdMainCompensateResponse> queryByCompensatePage(
            RQueryOrderRequest rQueryOrderRequest) {
        final OrdMainCriteria ordMainCriteria = new OrdMainCriteria();
        
        ordMainCriteria.setLimitClauseCount(rQueryOrderRequest.getPageSize());
        ordMainCriteria.setLimitClauseStart(rQueryOrderRequest.getStartRowIndex());
        ordMainCriteria.setOrderByClause("order_time desc");
        Criteria ca =  ordMainCriteria.createCriteria();
        if(rQueryOrderRequest.getBegDate() != null){
            ca.andOrderTimeGreaterThanOrEqualTo(rQueryOrderRequest.getBegDate());
        }
        if(rQueryOrderRequest.getEndDate() != null){
            ca.andOrderTimeLessThanOrEqualTo(rQueryOrderRequest.getEndDate());
        }
        if(rQueryOrderRequest.getShopId() != null){
        	ca.andShopIdEqualTo(rQueryOrderRequest.getShopId());
        }
        if(StringUtil.isNotBlank(rQueryOrderRequest.getOrderId())){
            ca.andIdEqualTo(rQueryOrderRequest.getOrderId());
        }
        ca.andPayTypeEqualTo("0");
        ca.andSiteIdEqualTo(1l);
        //ca.andPayWayNotEqualTo("9006");//屏蔽微信补偿性退款
        List<String> status = new ArrayList<String>();
        status.add(OrdConstants.OrderStatus.ORDER_STATUS_PAID);//已支付
        status.add(OrdConstants.OrderStatus.ORDER_STATUS_SEND_SECTION);  //部分发货
        status.add(OrdConstants.OrderStatus.ORDER_STATUS_SEND_ALL); //已发货
        status.add(OrdConstants.OrderStatus.ORDER_STATUS_RECEPT); //已收货
        ca.andStatusIn(status);
        
        //ca.andOrderTwoStatusNotEqualTo(OrdConstants.OrderTwoStatus.STATUS_REFUND_YES);
        //ca.andOrderTwoStatusNotEqualTo(OrdConstants.OrderTwoStatus.STATUS_BACKGDS_YES);
        //ca.andOrderTwoStatusNotEqualTo(OrdConstants.OrderTwoStatus.STATUS_BACKGDS_NO);
        //ca.andOrderTwoStatusNotEqualTo(OrdConstants.OrderTwoStatus.STATUS_REFUND_NO);
            
        return  super.queryByPagination(rQueryOrderRequest,ordMainCriteria,true,new PaginationCallback<OrdMain,OrdMainCompensateResponse>(){    
            @Override
            public OrdMainCompensateResponse warpReturnObject(OrdMain t) {
                OrdMainCompensateResponse ordMainCompensateResponse = new OrdMainCompensateResponse();
                BeanUtils.copyProperties(t, ordMainCompensateResponse);
                ordMainCompensateResponse.setOrderTime(t.getOrderTime());
                ordMainCompensateResponse.setUpdateTime(t.getUpdateTime());
                ordMainCompensateResponse.setPayTime(t.getPayTime());
                ordMainCompensateResponse.setCreateTime(t.getCreateTime());
                return ordMainCompensateResponse;
            }
    
            @Override
            public List<OrdMain> queryDB(BaseCriteria criteria) {
                List<OrdMain> list  = ordMainMapper.selectByExample((OrdMainCriteria)criteria);
                return list;
            }
    
            @Override
            public long queryTotal(BaseCriteria criteria) {
                long num = ordMainMapper.countByExample((OrdMainCriteria)criteria);
                return num;
            }
            
            @Override
            public List<Comparator<OrdMain>> defineComparators() {
            	// TODO Auto-generated method stub
            	List<Comparator<OrdMain>> ls = new ArrayList<Comparator<OrdMain>>();
            	ls.add(new Comparator<OrdMain>(){
					@Override
					public int compare(OrdMain o1, OrdMain o2) {
						// TODO Auto-generated method stub
						return o2.getOrderTime().compareTo(o1.getOrderTime());
					}           		
            	});
            	return ls;
            }
            
        });
    }
    
    @Override
    public boolean checkBackMoney(ROrdReturnRefundReq req){
    	boolean canReturn = false;
    	OrdMain ordMain = ordMainSV.queryOrderByOrderId(req.getOrderId());
    	long realMoney = ordMain.getRealMoney();
    	OrdBackApply ordBackApply = new OrdBackApply();
    	ordBackApply.setOrderId(req.getOrderId());
    	List<OrdBackApply> ordBackApplys =  ordBackApplySV.queryHasBackOrdBackApplyByOrderID(ordBackApply);
    	long hasbackmoney = 0l;
    	for(OrdBackApply orderBackApply:ordBackApplys){
    		hasbackmoney +=orderBackApply.getBackMoney();
    	}
    	if(req.getModifyBackMoney()<=realMoney-hasbackmoney){
    		canReturn = true;
    	}
    	return canReturn;
    }

    @Override
    public ROrdMainResponse queryOrderMain(ROrderIdRequest req) {
        ROrdMainResponse resp = new ROrdMainResponse();
        OrdMain ordMain = ordMainSV.queryOrderByOrderId(req.getOrderId());
        ObjectCopyUtil.copyObjValue(ordMain, resp, "", false);
        return resp;
    }

   
}

