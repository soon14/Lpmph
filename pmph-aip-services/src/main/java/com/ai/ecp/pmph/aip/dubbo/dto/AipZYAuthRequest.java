package com.ai.ecp.pmph.aip.dubbo.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * Project Name:ecp-services-aip-server <br>
 * Description: 考试网授权 ，数字教材/电子书提供（泽元提供）授权（试用、正式）请求.<br>
 * Date:2015-10-28下午4:41:02  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class AipZYAuthRequest extends AipAbstractRequest{
    
    /**
     * 失败!
     */
    public static final String _ZVING_STATUS_FALT = "FAIL";
    /**
     * 成功!
     */
    public static final String _ZVING_STATUS_OK = "SUCCESS";

    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 302668428768942855L;
    /**
     * 用户名:非空
     */
    @JSONField(name="UserName")
    private String userName;
    /**
     * 商品主键:数字教材/电子书/考试网的商品ID（泽元的商品ID）
     */
    @JSONField(name="GoodsID")
    private String goodsId;
    /**
     * 阅读方式:1:正式，2：试用
     */
    @JSONField(name="ReadType")
    private String readType;
    /**
     * 授权时长:单位（天），预留，暂时传空
     */
    @JSONField(name="Time")
    private Long time;
    /**
     * 订单号:订单号，试用的无订单号
     */
    @JSONField(name="OrderSN")
    private String orderSN;
    /**
     * 请求URL地址。
     */
    private String authUrl;
    
    /**
     * 考试网商品 
     * 试卷：PAPER
     * 试卷包：PACKAGE
     * 辅导班：FDB
     * (切记：大写)
     */
    @JSONField(name="GoodsType")
    private String goodsType;
    
    
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName != null ? userName.trim() : null;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getReadType() {
        return readType;
    }
    public void setReadType(String readType) {
        this.readType = readType != null ? readType.trim() : null;
    }
    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public String getOrderSN() {
        return orderSN;
    }
    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN != null ? orderSN.trim() : null;
    }
    public String getAuthUrl() {
        return authUrl;
    }
    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl != null ? authUrl.trim() : null;
    }
    public String getGoodsType() {
        return goodsType;
    }
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType != null ? goodsType.trim() : null;
    }
    
    
    
}

