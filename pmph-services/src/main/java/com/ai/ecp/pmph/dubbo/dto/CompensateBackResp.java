package com.ai.ecp.pmph.dubbo.dto;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.RBackApplyResp;
import com.ai.ecp.order.dubbo.dto.RBackPicResp;
import com.ai.ecp.order.dubbo.dto.RBackTrackResp;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsMain;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsSub;
import com.ai.ecp.server.front.dto.BaseResponseDTO;

public class CompensateBackResp extends BaseResponseDTO{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -2257804553071564151L;

    private RBackApplyResp backApplyResp;
    
    /** 
     * orderId:订单号. 
     * @since JDK 1.6 
     */ 
    private SOrderDetailsMain SOrderDetailsMain;
    
    private List<SOrderDetailsSub> orderDetailsSubs;
    
    private List<RBackTrackResp> backTrackResps;
    
    /** 
     * rBackPicResps:退款凭证图片
     * @since JDK 1.6 
     */ 
    private List<RBackPicResp> rBackPicResps;

    public RBackApplyResp getBackApplyResp() {
        return backApplyResp;
    }

    public void setBackApplyResp(RBackApplyResp backApplyResp) {
        this.backApplyResp = backApplyResp;
    }

    public SOrderDetailsMain getSOrderDetailsMain() {
        return SOrderDetailsMain;
    }

    public void setSOrderDetailsMain(SOrderDetailsMain sOrderDetailsMain) {
        SOrderDetailsMain = sOrderDetailsMain;
    }

    public List<SOrderDetailsSub> getOrderDetailsSubs() {
        return orderDetailsSubs;
    }

    public void setOrderDetailsSubs(List<SOrderDetailsSub> orderDetailsSubs) {
        this.orderDetailsSubs = orderDetailsSubs;
    }

    public List<RBackTrackResp> getBackTrackResps() {
        return backTrackResps;
    }

    public void setBackTrackResps(List<RBackTrackResp> backTrackResps) {
        this.backTrackResps = backTrackResps;
    }

    public List<RBackPicResp> getrBackPicResps() {
        return rBackPicResps;
    }

    public void setrBackPicResps(List<RBackPicResp> rBackPicResps) {
        this.rBackPicResps = rBackPicResps;
    }
}

