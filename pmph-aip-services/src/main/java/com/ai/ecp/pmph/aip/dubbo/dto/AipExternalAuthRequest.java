package com.ai.ecp.pmph.aip.dubbo.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * Project Name:ecp-services-aip-server <br>
 * Description: 外部系统商品授权 ，临床/用药/约健康提供<br>
 * Date:2018-06-21下午13:52:02  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public class AipExternalAuthRequest extends AipAbstractRequest {
	
	/**
     * 失败,业务错误
     */
	public static final String _EXTERNAL_STATUS_BUSERR = "2";
	/**
     * 失败,系统错误
     */
    public static final String _EXTERNAL_STATUS_SYSERR = "1";
    /**
     * 成功!
     */
    public static final String _EXTERNAL_STATUS_OK = "0";

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
	private static final long serialVersionUID = 5055003584130518676L;
	
	/**
     * 请求URL地址。
     */
    private String authUrl;
    /**
     * 支付用户ID
     */
    @JSONField(name="staff_code")
    private String staff_code = "";
    /**
     * 支付商品详情
     */
    @JSONField(name="gds_detail")
    private String gds_detail = "";
    /**
     * 支付订单号
     */
    @JSONField(name="order_code")
    private String order_code = "";
    /**
     * 下单时间
     */
    @JSONField(name="order_time")
    private String order_time = "";
    /**
     * 支付时间
     */
    @JSONField(name="pay_time")
    private String pay_time = "";
    /**
     * 在线支付方式
     */
    @JSONField(name="pay_way")
    private String pay_way = "";
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
	public String getStaff_code() {
		return staff_code;
	}
	public void setStaff_code(String staff_code) {
		this.staff_code = staff_code != null ? staff_code.trim() : null;
	}
	public String getGds_detail() {
		return gds_detail;
	}
	public void setGds_detail(String gds_detail) {
		this.gds_detail = gds_detail != null ? gds_detail.trim() : null;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code != null ? order_code.trim() : null;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time != null ? order_time.trim() : null;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time != null ? pay_time.trim() : null;
	}
	public String getPay_way() {
		return pay_way;
	}
	public void setPay_way(String pay_way) {
		this.pay_way = pay_way != null ? pay_way.trim() : null;
	}
}
