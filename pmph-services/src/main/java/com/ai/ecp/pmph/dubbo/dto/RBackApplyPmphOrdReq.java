package com.ai.ecp.pmph.dubbo.dto;

import java.util.List;

import com.ai.ecp.order.dubbo.dto.RBackApplyOrdSubResp;
import com.ai.ecp.server.front.dto.BaseResponseDTO;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsMain;

public class RBackApplyPmphOrdReq extends BaseResponseDTO{

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 4949746171721818364L;

    /**
     * 申请类型
     */
    private String applyType;
    /** 
     * orderId:订单号. 
     * @since JDK 1.6 
     */ 
    private SOrderDetailsMain SOrderDetailsMain;
    
    /** 
     * rBackApplyOrdSubResps:订单明细列表. 
     * @since JDK 1.6 
     */ 
    private List<RBackApplyOrdSubResp> rBackApplyOrdSubResps;

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public SOrderDetailsMain getSOrderDetailsMain() {
        return SOrderDetailsMain;
    }

    public void setSOrderDetailsMain(SOrderDetailsMain sOrderDetailsMain) {
        SOrderDetailsMain = sOrderDetailsMain;
    }

    public List<RBackApplyOrdSubResp> getrBackApplyOrdSubResps() {
        return rBackApplyOrdSubResps;
    }

    public void setrBackApplyOrdSubResps(List<RBackApplyOrdSubResp> rBackApplyOrdSubResps) {
        this.rBackApplyOrdSubResps = rBackApplyOrdSubResps;
    }

}

