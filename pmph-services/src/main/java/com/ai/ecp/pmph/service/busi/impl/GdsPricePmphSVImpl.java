package com.ai.ecp.pmph.service.busi.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.ai.ecp.general.order.dto.ROrdCartCommRequest;
import com.ai.ecp.general.order.dto.ROrdCartItemCommRequest;
import com.ai.ecp.general.order.dto.ROrdCartsCommRequest;
import com.ai.ecp.goods.dao.model.GdsGds2Prop;
import com.ai.ecp.goods.dao.model.GdsGds2PropCriteria;
import com.ai.ecp.goods.dao.model.GdsInfo;
import com.ai.ecp.goods.dao.model.GdsSkuInfo;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsErrorConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption;
import com.ai.ecp.goods.dubbo.dto.CalCatgCustDiscReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.dto.price.CartItemPriceInfo;
import com.ai.ecp.goods.dubbo.dto.price.GdsPriceTypeRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.goods.service.busi.impl.price.GdsPriceSVImpl;
import com.ai.ecp.goods.service.busi.interfaces.price.IGdsSkuPiceStrategySV;
import com.ai.ecp.order.dubbo.dto.RQueryGoodPayedRequest;
import com.ai.ecp.order.dubbo.interfaces.IReportGoodPayedRSV;
import com.ai.ecp.order.dubbo.util.CommonConstants;
import com.ai.ecp.pmph.dao.model.ZEResourceActivation;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouReqDTO;
import com.ai.ecp.pmph.service.busi.interfaces.IGdsPmphYsymZhekouSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;

/**
 */
public class GdsPricePmphSVImpl extends GdsPriceSVImpl {

    @Resource
    private IGdsPmphYsymZhekouSV gdsPmphYsymZhekouSV;

    @Resource
    private IReportGoodPayedRSV reportGoodPayedRSV;

    @Resource
    private IGdsSkuInfoQueryRSV iGdsSkuInfoQueryRSV;

    /**
     * 一书一码折扣计算
     * @param params
     * @return 负数表示不符合折扣要求
     * @throws BusinessException
     */
    private Long caculateYsymPrice(Map<String, Object> params) throws BusinessException {
        Long skuId = (Long) params.get("skuId");
        GdsSkuInfoReqDTO gdsSkuInfoReqDTO=new GdsSkuInfoReqDTO();
        gdsSkuInfoReqDTO.setId(skuId);
        List<Long> propIds = new ArrayList<Long>();
        // 五位ISBN
        propIds.add(1004l);
        gdsSkuInfoReqDTO.setPropIds(propIds);
        gdsSkuInfoReqDTO.setSkuQuery(new GdsOption.SkuQueryOption[]{GdsOption.SkuQueryOption.BASIC,
                 GdsOption.SkuQueryOption.PROP});
        GdsSkuInfoRespDTO gdsSkuInfo = gdsSkuInfoQuerySV.querySkuInfoByOptions(gdsSkuInfoReqDTO, gdsSkuInfoReqDTO.getSkuQuery());

        //判断是否数字教材/电子书
        if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1200>")||StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1201>")){

            if(StringUtils.isBlank(gdsSkuInfo.getIsbn())){
                return -1l;
            }
            
            String fiveISBN = null;
            
            if(MapUtils.isNotEmpty(gdsSkuInfo.getAllPropMaps())){
                Map<String,GdsPropRespDTO> map = gdsSkuInfo.getAllPropMaps();
                if(MapUtils.isNotEmpty(map)){
                    GdsPropRespDTO propResp = map.get("1004");
                    fiveISBN = CollectionUtils.isNotEmpty(propResp.getValues())?(propResp.getValues().get(0).getPropValue()):"";
                }
            }
            
            if(null == fiveISBN){
                return -1l;
            }


            GdsPmphYsymZhekouReqDTO req2=new GdsPmphYsymZhekouReqDTO();
            //req2.setMemo(bookIsbn);
            req2.setProp1(fiveISBN);
            req2.setAdduser(req2.getStaff().getStaffCode());
            ZEResourceActivation resp=gdsPmphYsymZhekouSV.queryZEResourceActivation(req2);

            //取tCommonPrice，以分为单位
            Long price=gdsSkuInfo.getCommonPrice();

            //可用，并且折扣值不为空
            if(resp!=null&&org.apache.commons.lang.StringUtils.equals(resp.getProp2(),"Y")&&
                    org.apache.commons.lang.StringUtils.isNotBlank(resp.getProp3())){

                BigDecimal discount=new BigDecimal(resp.getProp3());

                //折扣率不为零
                if(discount.doubleValue() != 0.0D&&discount.doubleValue() != 1.0D) {
                    discount=discount.multiply(new BigDecimal("100"));
                    Long result=GdsUtils.getDiscountPrice(price,discount);
                    return result;
                }
            }else{
                //判断 该会员是否在商城购买过   购物车中电子书/数字教材商品  所对应的纸质书（订单状态为 已支付、已发货、已收货），
                // 如果已经购买过相应的纸质书，统一对  电子书/数字教材 进行打折30%折扣（即 原价*30%）;
                /////////////////////////////////
              //纸质书单品编码列表
                List<Long> bookSkuIds=new ArrayList<Long>();
                //纸质书ISBN列表
               // List<String> bookIsbns=new ArrayList<String>();

                //查数字教材/电子书对应纸质书ISBN
                GdsSku2PropPropIdxReqDTO reqDTO = new GdsSku2PropPropIdxReqDTO();
                //标准ISBN
                reqDTO.setPropId(1032l);
                //电子书标准ISBN和ISBN一样
                reqDTO.setPropValue(gdsSkuInfo.getIsbn());
                reqDTO.setPageSize(10);
                PageResponseDTO<GdsSkuInfoRespDTO> rspDto = iGdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(reqDTO);
                if (rspDto != null&&rspDto.getResult() != null && rspDto.getResult().size() > 0) {
                    //找到多本纸质书？
                    for(GdsSkuInfoRespDTO skuResp:rspDto.getResult()){
                        //纸质书
                        if(StringUtils.contains(skuResp.getPlatCatgs(),"<1115>")&& org.apache.commons.lang.StringUtils.isNotBlank(skuResp.getIsbn())){
                            bookSkuIds.add(skuResp.getId());
                        }else{
                            continue;
                        }
                    }
                }else{
                    return -1l;
                }

                if(CollectionUtils.isEmpty(bookSkuIds)){
                    return -1l;
                }

                //使用第一个纸质书作为折扣计算
                long bookSkuId=bookSkuIds.get(0);
                /////////////////////////////////
                Long staffId=(Long) params.get("staffId");
                Long siteId=(Long)params.get("siteId");
                //查询购买记录
                RQueryGoodPayedRequest rqueryGoodPayedRequest=new RQueryGoodPayedRequest();
                rqueryGoodPayedRequest.setSkuId(bookSkuId);
                rqueryGoodPayedRequest.getStaff().setId(staffId);
                rqueryGoodPayedRequest.setCurrentSiteId(siteId);
                rqueryGoodPayedRequest.setSiteId(siteId);
                Long sum=reportGoodPayedRSV.queryStaffBuyNumByGoodStaff(rqueryGoodPayedRequest);
                if(sum>0){
                    //统一折扣
                    String _discount= SysCfgUtil.fetchSysCfg("PMPH_DISCOUNT_DTORDB").getParaValue();

                    if(org.apache.commons.lang.StringUtils.isNotBlank(_discount)){
                        BigDecimal discount=new BigDecimal(_discount);
                        discount=discount.multiply(new BigDecimal("100"));
                        Long result=GdsUtils.getDiscountPrice(price,discount);
                        return result;
                    }

                }

            }
        }

        return -1l;
    }

    @Override
    public Long caculatePrice(Map<String, Object> params) throws BusinessException {
        Long price=this.caculateYsymPrice(params);
        if(price.longValue()>0l){
            return price;
        }
        return super.caculatePrice(params);
    }

    @Override
    public Map<Long, CartItemPriceInfo> caculatePrice(ROrdCartsCommRequest reqDtos) throws BusinessException {
        //return super.caculatePrice(reqDtos);

        Map<Long, CartItemPriceInfo> allCart = new HashMap<Long, CartItemPriceInfo>();

        if (reqDtos != null && CollectionUtils.isNotEmpty(reqDtos.getOrdCartsCommList())) {
            for (ROrdCartCommRequest reqDto : reqDtos.getOrdCartsCommList()) {
                List<ROrdCartItemCommRequest> ordCartItem = reqDto.getOrdCartItemCommList();
                // 按照商品分组
                Map<Long, List<ROrdCartItemCommRequest>> gdsGroup = new HashMap<Long, List<ROrdCartItemCommRequest>>();
                sortGds(ordCartItem, gdsGroup);
                Set<Long> gdsIds = gdsGroup.keySet();
                Long staffId = reqDto.getStaffId();
                for (Long gdsId : gdsIds) {
                    List<ROrdCartItemCommRequest> carts = gdsGroup.get(gdsId);
                    boolean ifDigitProduct = false;
                    // 判断当前商品是否是数字印刷版
                    if ("1".equals(carts.get(0).getPrnFlag())) {
                        ifDigitProduct = true;
                    }
                    GdsInfoReqDTO reqDTO = new GdsInfoReqDTO();
                    reqDTO.setId(gdsId);
                    GdsInfo gdsInfo = gdsInfoQuerySV.queryGdsInfoModel(reqDTO);
                    if (gdsInfo == null) {
                        throw new BusinessException(GdsErrorConstants.GdsInfo.ERROR_GOODS_GDSINFO_210005);
                    }

                    // 阶梯价计算
                    if (GdsUtils.isEqualsValid(gdsInfo.getIfLadderPrice())) {

                        Map<String, Object> params = new HashMap<String, Object>();
                        //只要有一个商品满足一书一码折扣要求，则直接跳过其它计费方式
                        boolean flag=true;
                        for (ROrdCartItemCommRequest rOrdCartItemCommRequest : carts) {
                            params.put("skuId", rOrdCartItemCommRequest.getSkuId());
                            params.put("staffId", rOrdCartItemCommRequest.getStaff().getId());
                            params.put("shopId", rOrdCartItemCommRequest.getShopId());
                            params.put("siteId",rOrdCartItemCommRequest.getCurrentSiteId());
                            Long price=this.caculateYsymPrice(params);
                            if(price.longValue()>0l){
                                flag=false;
                                CartItemPriceInfo cartItemPriceInfo = new CartItemPriceInfo();
                                cartItemPriceInfo.setBasePrice(price);
                                cartItemPriceInfo.setBuyPrice(price);
                                cartItemPriceInfo.setItemId(rOrdCartItemCommRequest.getId());
                                cartItemPriceInfo.setGdsId(rOrdCartItemCommRequest.getGdsId());
                                cartItemPriceInfo.setSkuId(rOrdCartItemCommRequest.getSkuId());
                                allCart.put(rOrdCartItemCommRequest.getSkuId(), cartItemPriceInfo);
                            }
                        }

                        if(flag){
                            params = new HashMap<String, Object>();
                            long count = 0;
                            for (ROrdCartItemCommRequest cart : carts) {
                                count = count + cart.getOrderAmount();
                            }
                            IGdsSkuPiceStrategySV gdsSkuPiceStrategySV = getPriceStrategySV(GdsConstants.GdsInfo.SKU_PRICE_TYPE_LADDER);
                            params.put("gdsId", gdsId);
                            params.put("amount", count);
                            Long price = gdsSkuPiceStrategySV.calculatePrice(params);

                            for (ROrdCartItemCommRequest rOrdCartItemCommRequest : carts) {
                                CartItemPriceInfo cartItemPriceInfo = new CartItemPriceInfo();
                                cartItemPriceInfo.setBasePrice(price);
                                cartItemPriceInfo.setBuyPrice(price);
                                cartItemPriceInfo.setItemId(rOrdCartItemCommRequest.getId());
                                cartItemPriceInfo.setGdsId(rOrdCartItemCommRequest.getGdsId());
                                cartItemPriceInfo.setSkuId(rOrdCartItemCommRequest.getSkuId());
                                allCart.put(rOrdCartItemCommRequest.getSkuId(), cartItemPriceInfo);
                            }
                        }else{//只要有一个商品满足一书一码折扣要求，则直接跳过其它计费方式
                            for (ROrdCartItemCommRequest rOrdCartItemCommRequest : carts) {

                                //CommonPrice获取
                                GdsSkuInfoReqDTO gdsSkuInfoReqDTO=new GdsSkuInfoReqDTO();
                                gdsSkuInfoReqDTO.setId(rOrdCartItemCommRequest.getSkuId());
                                GdsSkuInfo gdsSkuInfo = gdsSkuInfoQuerySV.queryGdsSkuInfo(gdsSkuInfoReqDTO);

                                //不能覆盖已计算的一书一码折扣
                                if(!allCart.containsKey(rOrdCartItemCommRequest.getSkuId())){
                                    CartItemPriceInfo cartItemPriceInfo = new CartItemPriceInfo();
                                    cartItemPriceInfo.setBasePrice(gdsSkuInfo.getCommonPrice());
                                    cartItemPriceInfo.setBuyPrice(gdsSkuInfo.getCommonPrice());
                                    cartItemPriceInfo.setItemId(rOrdCartItemCommRequest.getId());
                                    cartItemPriceInfo.setGdsId(rOrdCartItemCommRequest.getGdsId());
                                    cartItemPriceInfo.setSkuId(rOrdCartItemCommRequest.getSkuId());
                                    allCart.put(rOrdCartItemCommRequest.getSkuId(), cartItemPriceInfo);
                                }

                            }
                        }

                        // 积分商品计算
                    } else if (GdsUtils.isEqualsValid(gdsInfo.getIfScoreGds())) {

                        for (ROrdCartItemCommRequest rOrdCartItemCommRequest : carts) {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("gdsId", rOrdCartItemCommRequest.getGdsId());
                            params.put("amount", rOrdCartItemCommRequest.getOrderAmount());
                            params.put("gdsScorePriceId", rOrdCartItemCommRequest.getScoreTypeId());
                            CartItemPriceInfo itemPriceInfo = getScoreGdsPrice(params);

                            CartItemPriceInfo cartItemPriceInfo = new CartItemPriceInfo();
                            cartItemPriceInfo.setBasePrice(itemPriceInfo.getBasePrice());
                            cartItemPriceInfo.setBuyPrice(itemPriceInfo.getBuyPrice());
                            cartItemPriceInfo.setScore(itemPriceInfo.getScore());
                            cartItemPriceInfo.setItemId(rOrdCartItemCommRequest.getId());
                            cartItemPriceInfo.setGdsId(rOrdCartItemCommRequest.getGdsId());
                            cartItemPriceInfo.setSkuId(rOrdCartItemCommRequest.getSkuId());
                            allCart.put(rOrdCartItemCommRequest.getSkuId(), cartItemPriceInfo);
                        }

                    } else {
                        // 普通价格计算
                        List<GdsPriceTypeRespDTO> gdsPriceTypeRespDTOs = queryAllPriceType();
                        for (ROrdCartItemCommRequest rOrdCartItemCommRequest : carts) {

                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("skuId", rOrdCartItemCommRequest.getSkuId());
                            params.put("staffId", rOrdCartItemCommRequest.getStaff().getId());
                            params.put("shopId", rOrdCartItemCommRequest.getShopId());
                            params.put("siteId",rOrdCartItemCommRequest.getCurrentSiteId());
                            Long price=this.caculateYsymPrice(params);
                            if(price.longValue()>0l){
                                CartItemPriceInfo cartItemPriceInfo = new CartItemPriceInfo();
                                cartItemPriceInfo.setBasePrice(price);
                                cartItemPriceInfo.setBuyPrice(price);
                                cartItemPriceInfo.setItemId(rOrdCartItemCommRequest.getId());
                                cartItemPriceInfo.setGdsId(rOrdCartItemCommRequest.getGdsId());
                                cartItemPriceInfo.setSkuId(rOrdCartItemCommRequest.getSkuId());
                                allCart.put(rOrdCartItemCommRequest.getSkuId(), cartItemPriceInfo);

                                //跳过其它计价
                                continue;
                            }

                            params = new HashMap<String, Object>();
                            params.put("staffId", staffId);
                            params.put("skuId", rOrdCartItemCommRequest.getSkuId());
                            params.put("gdsId", rOrdCartItemCommRequest.getGdsId());
                            params.put("amount", rOrdCartItemCommRequest.getOrderAmount());
                            price = 0L;
                            if (ifDigitProduct) {// 如果是数字印刷版,取属性表的数字版价格
                                GdsGds2PropCriteria example = new GdsGds2PropCriteria();
                                GdsGds2PropCriteria.Criteria criteria = example.createCriteria();
                                criteria.andPropIdEqualTo(GdsConstants.GdsProp.GDS_DIGITAL_PRODUCT_PRICE_PROP_ID);
                                criteria.andGdsIdEqualTo(gdsId);
                                criteria.andStatusEqualTo(GdsConstants.Commons.STATUS_VALID);
                                List<GdsGds2Prop> gdsGds2Props = gds2PropMapper.selectByExample(example);
                                if (gdsGds2Props != null && gdsGds2Props.size() != 0) {
                                    String propStr = gdsGds2Props.get(0).getPropValue();
                                    if (propStr == null) {
                                        LogUtil.error(MOUDLE, "数字印刷版的价格没配置");
                                        ;
                                    }
                                    price = Long.parseLong(propStr);

                                }

                            } else {
                                price = getOptimalPrice(gdsPriceTypeRespDTOs, params);
                            }
                            Long buyPrice = 0L;
                            CalCatgCustDiscReqDTO calCatgCustDiscReqDTO = new CalCatgCustDiscReqDTO();
                            calCatgCustDiscReqDTO.setGdsId(gdsInfo.getId());
                            calCatgCustDiscReqDTO.setCustNo(staffId);
                            BigDecimal discount = catgCustDiscSV.calCatgCustDisc(calCatgCustDiscReqDTO);
                            // 如果是数字印刷版，则取原始价，不参加折扣
                            if (ifDigitProduct) {
                                buyPrice = price;
                            } else {
                                buyPrice = GdsUtils.getDiscountPrice(price, discount);
                            }
                            if (buyPrice == 0) {
                                buyPrice = price;
                            }
                            
                            if(isFromApp(reqDtos.getSource())){
                                GdsSkuInfoReqDTO skuInfoQuery = new GdsSkuInfoReqDTO();
                                skuInfoQuery.setGdsId(rOrdCartItemCommRequest.getGdsId());
                                skuInfoQuery.setId(rOrdCartItemCommRequest.getSkuId());
                                GdsSkuInfoRespDTO skuInfo = gdsSkuInfoQuerySV.querySkuInfoByOptions(skuInfoQuery, GdsOption.SkuQueryOption.BASIC);
                                // 获取手机专享价。
                                Long appSpecPrice = (null != skuInfo ? skuInfo.getAppSpecPrice() : null); 
                                if(null != appSpecPrice && !appSpecPrice.equals(0L)){
                                    price = appSpecPrice;
                                    buyPrice = price;
                                }
                            }
                            
                            

                            CartItemPriceInfo cartItemPriceInfo = new CartItemPriceInfo();
                            cartItemPriceInfo.setBasePrice(price);
                            cartItemPriceInfo.setBuyPrice(buyPrice);
                            cartItemPriceInfo.setItemId(rOrdCartItemCommRequest.getId());
                            cartItemPriceInfo.setGdsId(rOrdCartItemCommRequest.getGdsId());
                            cartItemPriceInfo.setSkuId(rOrdCartItemCommRequest.getSkuId());
                            allCart.put(rOrdCartItemCommRequest.getSkuId(), cartItemPriceInfo);
                        }
                    }
                }
            }
        }
        return allCart;
    }
    
    
    
    /**
     * 
     * isFromApp:来源是否是手机端. <br/> 
     * 
     * @param source
     * @return 
     * @since JDK 1.6
     */
    private boolean isFromApp(String source){
          return CommonConstants.SOURCE.SOURCE_APP.equals(source) || CommonConstants.SOURCE.SOURCE_OTH.equals(source);        
    }

}
