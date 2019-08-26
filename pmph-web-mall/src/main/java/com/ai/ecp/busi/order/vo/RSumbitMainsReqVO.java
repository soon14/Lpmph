/**
 * RSumbitMainsReqVO.java	  V1.0   2015-10-9 下午8:00:14
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.ai.ecp.busi.order.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.general.order.dto.CoupCheckBeanRespDTO;
import com.ai.ecp.order.dubbo.dto.ROrdInvoiceCommRequest;
import com.ai.ecp.order.dubbo.dto.ROrdInvoiceTaxRequest;

public class RSumbitMainsReqVO extends EcpBasePageReqVO {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
    
    //订单收货地址信息----提交订单时候用
	@NotNull
    private Long addrId;
    
    //支付方式---提交订单时候使用、
	@NotNull
    private String payType;
	
	//收货地址
	private String gdsType;
    
    List<RSumbitMainReqVO> sumbitMainList;

    // 发票类型---提交订单时候使用、
    private String invoiceType;

    // 普票信息---提交订单时候使用、
    private ROrdInvoiceCommRequest rOrdInvoiceCommRequest;

    // 增票信息---提交订单时候使用、
    private ROrdInvoiceTaxRequest rOrdInvoiceTaxRequest;
    
    private String sourceKey;
    
    /**
     * redis keyName
     */
    private String mainHashKey;
    
    private String couponHashKey;
    
    //可使用的优惠券信息(平台)
    private List<CoupCheckBeanRespDTO> coupPlatfBean;
    
    public List<RSumbitMainReqVO> getSumbitMainList() {
        return sumbitMainList;
    }

    public void setSumbitMainList(List<RSumbitMainReqVO> sumbitMainList) {
        this.sumbitMainList = sumbitMainList;
    }

    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

	public String getGdsType() {
		return gdsType;
	}

	public void setGdsType(String gdsType) {
		this.gdsType = gdsType;
	}

    public List<CoupCheckBeanRespDTO> getCoupPlatfBean() {
        return coupPlatfBean;
    }

    public void setCoupPlatfBean(List<CoupCheckBeanRespDTO> coupPlatfBean) {
        this.coupPlatfBean = coupPlatfBean;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getMainHashKey() {
        return mainHashKey;
    }

    public void setMainHashKey(String mainHashKey) {
        this.mainHashKey = mainHashKey;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public ROrdInvoiceCommRequest getrOrdInvoiceCommRequest() {
        return rOrdInvoiceCommRequest;
    }

    public void setrOrdInvoiceCommRequest(ROrdInvoiceCommRequest rOrdInvoiceCommRequest) {
        this.rOrdInvoiceCommRequest = rOrdInvoiceCommRequest;
    }

    public ROrdInvoiceTaxRequest getrOrdInvoiceTaxRequest() {
        return rOrdInvoiceTaxRequest;
    }

    public void setrOrdInvoiceTaxRequest(ROrdInvoiceTaxRequest rOrdInvoiceTaxRequest) {
        this.rOrdInvoiceTaxRequest = rOrdInvoiceTaxRequest;
    }

	public String getCouponHashKey() {
		return couponHashKey;
	}

	public void setCouponHashKey(String couponHashKey) {
		this.couponHashKey = couponHashKey;
	}
}
