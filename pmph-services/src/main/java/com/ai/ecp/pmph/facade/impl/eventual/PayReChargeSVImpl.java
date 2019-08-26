package com.ai.ecp.pmph.facade.impl.eventual;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.ai.ecp.goods.dubbo.dto.GdsGiftReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsGiftRespDTO;
import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2CatgReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsGiftRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.order.dao.mapper.busi.OrdMainMapper;
import com.ai.ecp.order.dao.model.OrdGift;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dao.model.OrdMainCriteria;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdSubResponse;
import com.ai.ecp.order.dubbo.dto.SBaseAndStatusInfo;
import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.dubbo.util.DelyConstants.DeliverStatus;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants.OrderStatus;
import com.ai.ecp.order.service.busi.interfaces.IOrdGiftSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainShopIdxSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainStaffIdxSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphExRSV;
import com.ai.ecp.pmph.dubbo.util.CryptUtils;
import com.ai.ecp.pmph.dubbo.util.EncodeUtils;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayReChargeSV;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**
 */
public class PayReChargeSVImpl extends PayAbstractExternalUrlSVImpl implements IPayReChargeSV{

    private static final String KEY = "5965bab05ca97eebcdebd4d906ad21c1";

    private static final String ORD_ACCOUNT_CHARGE_URL = "ORD_ACCOUNT_CHARGE_URL";

    private static final String MODULE = PayReChargeSVImpl.class.getName();

    @Resource
    private IStaffUnionRSV staffUnionRSV;

    @Resource
    private OrdMainMapper ordMainMapper;
    
    @Resource
    private IOrdMainSV ordMainSV;

    @Resource
    private IOrdMainShopIdxSV ordMainShopIdxSV;

    @Resource
    private IOrdMainStaffIdxSV ordMainStaffIdxSV;
    
    @Resource
    private IOrdSubSV ordSubSV;

    @Resource
    public IGdsPmphExRSV gdsPmphExRSV;

    @Resource
    private IGdsSkuInfoQueryRSV iGdsSkuInfoQueryRSV;

    @Resource
    private IOrdGiftSV orderGifSV;
    @Resource
    private IGdsGiftRSV gdsGiftRSV;

    @Override
    protected String setMethod() {
        return METHOD_POST;
    }

    @Override
    public String getParamJson(PaySuccInfo paySuccInfo) {
        return null;
    }

    @Override
    public String getParamNameValue(PaySuccInfo paySuccInfo){
        //根据订单编号获取所有子订单
        List<RPreOrdSubResponse> ordsubs = ordSubSV.findOrdSubByOrderId(paySuccInfo.getOrderId());
        float fee = 0f;
        boolean hasVirtualCard = false; //是否存在消费卡
        for(RPreOrdSubResponse ordsub : ordsubs){
            //获取商品
            GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
            gdsSkuInfoReqDTO.setId(ordsub.getSkuId());
            GdsSkuInfoRespDTO gdsSkuInfoRespDTO = iGdsSkuInfoQueryRSV.queryGdsSkuInfoResp(gdsSkuInfoReqDTO);
            LongReqDTO longReqDTO = new LongReqDTO();
            //加载参数
            longReqDTO.setId(gdsSkuInfoRespDTO.getGdsTypeId());
            //判断是否为消费卡
            if(gdsPmphExRSV.isVirtualCard(longReqDTO)){
                //调用商品接口获取原始单价
                fee += gdsSkuInfoRespDTO.getCommonPrice()*ordsub.getOrderAmount();
                hasVirtualCard = true;
            }
        }
        if(!hasVirtualCard){
            return "";
        }

        //fee保留两位小数
        BigDecimal feeb =  new BigDecimal(fee);
        feeb.setScale(2,  BigDecimal.ROUND_HALF_UP);//四舍五入       
        //获取该订单线下的所有增值劵，并计算增值劵金额
        ROrderIdRequest info = new ROrderIdRequest();
        info.setOrderId(paySuccInfo.getOrderId());
        List<OrdGift> ordGifts = orderGifSV.queryOrdGift(info);

        float bonus = 0f;
        if(ordGifts!=null&&ordGifts.size()>0){
            for(OrdGift ordGift:ordGifts){
                //填充礼品金额
                Long giftId=ordGift.getGiftId();
                GdsGiftReqDTO gdsGiftReqDTO=new GdsGiftReqDTO();
                gdsGiftReqDTO.setId(giftId);
                GdsGiftRespDTO gdsGiftRespDTO= gdsGiftRSV.querySingleGiftInfo(gdsGiftReqDTO);
                if(!StringUtil.isEmpty(gdsGiftRespDTO)){
                    //如果赠品类型为0---充值劵则需要加入充值劵金额
                    if(!StringUtil.isEmpty(gdsGiftRespDTO.getGiftType()) && "0".equals(gdsGiftRespDTO.getGiftType())){
                        bonus=bonus+gdsGiftRespDTO.getGiftValue()*ordGift.getGiftCount();
                    }
                }
            }
        }
        BigDecimal bonusb =  new BigDecimal(bonus);
        bonusb.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入

        String params = "";
        ROrdMainResponse ordMain = ordMainSV.findOrdMianByOrderId(paySuccInfo.getOrderId());
        if(ordMain==null||ordMain.getStaffId()==null){
        	 LogUtil.error(getModule(),"订单编号无法匹配主订单信息！");
        	 return "";
        }
        Long staffId = ordMain.getStaffId();
        CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustOrAdminByStaffId(staffId);
        String userName = custInfoResDTO.getStaffCode();
        LogUtil.info(MODULE,"外域充值账户："+userName);
        LogUtil.info(MODULE,"外域充值链接参数处理开始");

        params += "userName="+userName;
        Long t = new Date().getTime();
        params += "&timeStamp="+t;
        params +="&fee="+feeb.floatValue();
        params +="&bonus="+bonusb.floatValue();
        //加密token
        String token = "";
        byte[] key = null;
        try{
            key = Hex.decodeHex(KEY.toCharArray());
            token = EncodeUtils.encodeHex(CryptUtils.aesEncrypt(String.valueOf(t).getBytes(),key));
        }catch (Exception e){
            LogUtil.error(getModule(),"key加密失败");
            return "";
        }
        params += "&token="+token;

        LogUtil.info(MODULE,"参数是："+params);

        return params;
    }

    @Override
    public String setUrlPrefix() {
        return ORD_ACCOUNT_CHARGE_URL;
    }

    @Override
    public String getImportType() {
        return "09";
    }

    @Override
    public void handleResult(PaySuccInfo paySuccInfo, String resultStr) {
        if(!StringUtil.isBlank(resultStr)) {
            LogUtil.info(MODULE, "处理获取外域充值卡接口结束,子订单号：" + paySuccInfo.getSubOrder());
            try {
            	Long flag = 0L;
                //根据订单编号获取所有子订单
                List<RPreOrdSubResponse> ordsubs = ordSubSV.findOrdSubByOrderId(paySuccInfo.getOrderId());
                boolean hasAllVirtualCard = true; //是否全部是消费卡
                for(RPreOrdSubResponse ordsub : ordsubs){
                    //获取商品
                    GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
                    gdsSkuInfoReqDTO.setId(ordsub.getSkuId());
                    GdsSkuInfoRespDTO gdsSkuInfoRespDTO = iGdsSkuInfoQueryRSV.queryGdsSkuInfoResp(gdsSkuInfoReqDTO);
                    LongReqDTO longReqDTO = new LongReqDTO();
                    //加载参数
                    longReqDTO.setId(gdsSkuInfoRespDTO.getGdsTypeId());
                    
                    //判断是否全部为消费卡
                    if(!ordsub.getCategoryCode().equals(SysCfgUtil.fetchSysCfg("GDS_XXK_CAT_CODE").getParaValue())){
                    	continue;
                    }
                    if(gdsPmphExRSV.isVirtualCard(longReqDTO)){
                    	ordsub.setDeliverStatus(DeliverStatus.DELIVER_STATUS_TRUE);
                    	flag++;
                    }else{
                    	hasAllVirtualCard=false;
                    }
                }
                if(flag==0L){
                	return;
                }
                LogUtil.info(MODULE, "开始更新主表子表状态");
                this.updateOrderStatus(paySuccInfo,hasAllVirtualCard,ordsubs);
            } catch (Exception e) {
                LogUtil.error(MODULE, "修改充值卡状态异常：" + e);
            }
            
        }
    }

    @Override
    public String getModule() {
        return PayReChargeSVImpl.class.getName();
    }

    @Override
    public void deal(PaySuccInfo paySuccInfo) {
        super.deal(paySuccInfo,getImportType());
    }
    
    private void updateOrderStatus(PaySuccInfo paySuccInfo,boolean hasAllVirtualCard,List<RPreOrdSubResponse> ordsubs){
    	/* 更新主订单表订单状态 */
        OrdMain ordMain = ordMainSV.queryOrderByOrderId(paySuccInfo.getOrderId());
        SBaseAndStatusInfo sBaseAndStatusInfo=new SBaseAndStatusInfo();
        sBaseAndStatusInfo.setOrderId(paySuccInfo.getOrderId());
        sBaseAndStatusInfo.setStaffId(ordMain.getStaffId());// 更新状态时使用订单表里的staffid更新索引表
        sBaseAndStatusInfo.setShopId(ordMain.getShopId());// 更新状态时使用订单表里的shopid更新索引表
        sBaseAndStatusInfo.setOperatorId(paySuccInfo.getStaffId());   
        if(hasAllVirtualCard){
        	sBaseAndStatusInfo.setStatus(OrdConstants.OrderStatus.ORDER_STATUS_RECEPT);
        	sBaseAndStatusInfo.setOrderTwoStatus(OrdConstants.OrderTwoStatus.STATUS_RECEPT_BUYER);
        	sBaseAndStatusInfo.setDeliverDate(DateUtil.getSysDate());
        }else{
        	sBaseAndStatusInfo.setStatus(OrdConstants.OrderStatus.ORDER_STATUS_SEND_SECTION);
        }
        OrdMainCriteria omc = new OrdMainCriteria();
        omc.createCriteria().andIdEqualTo(sBaseAndStatusInfo.getOrderId());
        OrdMain om = new OrdMain();
        om.setStatus(sBaseAndStatusInfo.getStatus());
        if(StringUtil.isNotEmpty(sBaseAndStatusInfo.getOrderTwoStatus())){
            om.setOrderTwoStatus(sBaseAndStatusInfo.getOrderTwoStatus());	
        }
        if (sBaseAndStatusInfo.getDeliverDate() != null) {
            om.setDeliverDate(sBaseAndStatusInfo.getDeliverDate());
        }
        om.setUpdateStaff(sBaseAndStatusInfo.getOperatorId());
        om.setUpdateTime(DateUtil.getSysDate());
        this.ordMainMapper.updateByExampleSelective(om, omc);
    
        /* 更新卖家索引表订单状态 */
        this.ordMainShopIdxSV.updateOrderStatus(sBaseAndStatusInfo);
        /* 更新买家索引表订单状态 */
        this.ordMainStaffIdxSV.updateOrderStatus(sBaseAndStatusInfo);
        
        this.ordSubSV.updateStatusByOrderId(sBaseAndStatusInfo);
        /* 更新子表状态---针对发货收货*/
        for(RPreOrdSubResponse ordsub : ordsubs){
        	if(DeliverStatus.DELIVER_STATUS_TRUE.equals(ordsub.getDeliverStatus())){
        		this.ordSubSV.updateStatusInfo(ordsub,sBaseAndStatusInfo);	
        	}
        }
    } 
}
