package com.ai.ecp.app.action;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Gds001Req;
import com.ai.ecp.app.resp.Gds001Resp;
import com.ai.ecp.app.resp.gds.GdsDetailBaseInfo;
import com.ai.ecp.app.resp.gds.GdsPropBaseInfo;
import com.ai.ecp.app.resp.gds.GdsPropValueBaseInfo;
import com.ai.ecp.app.resp.gds.GdsSkuBaseInfo;
import com.ai.ecp.app.resp.gds.GdscatgsCodeAndNameVO;
import com.ai.ecp.base.velocity.AiToolUtil;
import com.ai.ecp.busi.goods.gdsdetail.utils.GdsDetailUtil;
import com.ai.ecp.busi.search.vo.AddToCartButtonVO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsErrorConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsBrowseHisReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCollectReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCollectRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropValueRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareRespDTO;
import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoDetailRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2MediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCollectRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoExternalRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.goods.dubbo.util.GdsMessageUtil;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.order.dubbo.dto.RQueryGoodPayedRequest;
import com.ai.ecp.order.dubbo.interfaces.IReportGoodPayedRSV;
import com.ai.ecp.order.dubbo.util.CommonConstants;
import com.ai.ecp.pmph.dubbo.dto.goods.GdsCatgCodeReqDTO;
import com.ai.ecp.pmph.dubbo.dto.goods.GdsParseISBNReqDTO;
import com.ai.ecp.prom.dubbo.dto.GdsPromListDTO;
import com.ai.ecp.prom.dubbo.dto.PromRuleCheckDTO;
import com.ai.ecp.prom.dubbo.interfaces.IPromQueryRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.StaffLocaleUtil;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.CipherUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.SystemException;

/**
 * 获取商品信息 Title: ECP <br>
 * Description: 复写人卫二期获取商品详情接口<br>
 * Date:2016年3月10日上午10:18:08 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@Service("gds001")
@Action(bizcode = "gds001", userCheck = false)
@Scope("prototype")
public class Gds001Action extends AppBaseAction<Gds001Req, Gds001Resp> {
    
    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;

    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;

    @Resource
    private IShopInfoRSV shopInfoRSV;

    @Resource
    private IGdsCollectRSV gdsCollectRSV;

    @Resource
    private IReportGoodPayedRSV reportGoodPayedRSV;
    
    @Resource
    private IGdsInfoExternalRSV gdsInfoExternalRSV;
    
    @Resource
	private ICustInfoRSV iCustInfoRSV;

    private static final String MODULE = Gds001Action.class.getName();

    private static String KEY = "GDS_BROWSE_HIS";
    
    private static String GDS_E_BOOK_CAT_CODE = "1200";

    private static String GDS_PAPER_BOOK_CAT_CODE = "1115";

    private static String GDS_DIGITS_BOOK_CAT_CODE = "1201";
    
    private static int PAGE_SIZE_10 = 10;
    
    private static Long PROP_ID_1032 = 1032L;
    
    private static Long VALUE_ADDED_PROP_ID = 1027L;
    
    private static Long VALUE_ADDED_PROP_VALUE_ID = 308L;
    @Resource
    private IGdsCategoryRSV igdsCategoryRSV;
    @Resource
    private IPromQueryRSV promQueryRSV;

    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {

        Gds001Req gds001Req = this.request;
        Gds001Resp gds001Resp = this.response;
        GdsInfoReqDTO dto = new GdsInfoReqDTO();
        if (StringUtil.isNotEmpty(gds001Req.getGdsId())) {
            dto.setId(gds001Req.getGdsId());
        }
        if (StringUtil.isNotEmpty(gds001Req.getSkuId())) {
            dto.setSkuId(gds001Req.getSkuId());
        }
        
        
        GdsQueryOption[] gdsQueryOptions = new GdsQueryOption[] { GdsQueryOption.BASIC, GdsQueryOption.MEDIA, GdsQueryOption.PROP };
        SkuQueryOption[] skuQueryOptions = null;
        skuQueryOptions = new SkuQueryOption[] { SkuQueryOption.BASIC, SkuQueryOption.SHOWSTOCK, SkuQueryOption.CAlDISCOUNT,SkuQueryOption.PROP };
        
        dto.setGdsQueryOptions(gdsQueryOptions);
        dto.setSkuQuerys(skuQueryOptions);

        List<String> propInputType = new ArrayList<String>();
        propInputType.add(GdsConstants.GdsProp.GDS_PROP_VALUE_INPUT_TYPE_RICHTXT);
        dto.setPropInputTypes(propInputType);

        GdsInfoDetailRespDTO resultDto = null;
        try {
            resultDto = gdsInfoQueryRSV.queryGdsInfoDetail(dto);
            
            if (resultDto != null && resultDto.getSkuInfo() != null) {
                // 计算分类折扣价格
                // dealDiscountPrice(resultDto);
                // 发送消息
                sendRecentlyBrowMsg(resultDto);
                
                boolean isVirtualProduct = gdsInfoExternalRSV.isVirtualProduct(new LongReqDTO(resultDto.getGdsTypeId()));
                
              //判断是否有专享价，没有就返回第一个的促销价格
                List<GdsSkuInfoRespDTO> gdsInfos = resultDto.getSkuInfos();
                if(CollectionUtils.isNotEmpty(gdsInfos)){
                	for(GdsSkuInfoRespDTO gdsInfoDto : gdsInfos){
                		if(StringUtil.isNotBlank(gdsInfoDto.getAppSpecPrice().toString()) && gdsInfoDto.getAppSpecPrice()!=0){
                			gdsInfoDto.setDiscountPrice(gdsInfoDto.getAppSpecPrice());
                		}else{
                			String salePrice = getFirstSaleGdsPrice(gdsInfoDto);
                			if(StringUtil.isNotBlank(salePrice)){
                				gdsInfoDto.setDiscountPrice(Long.parseLong(salePrice));
                			}
                		}
                	}
                }
                resultDto.setVirtualProduct(isVirtualProduct);
            } else {
                resultDto = new GdsInfoDetailRespDTO();
                GdsSkuInfoRespDTO gdsSkuInfoRespDTO = new GdsSkuInfoRespDTO();
                gdsSkuInfoRespDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_DELETE);
                resultDto.setSkuInfo(gdsSkuInfoRespDTO);
                GdsDetailBaseInfo gdsDetailBaseInfo = new GdsDetailBaseInfo();
                ObjectCopyUtil.copyObjValue(resultDto, gdsDetailBaseInfo, null, false);
                GdsSkuBaseInfo gdsSkuBaseInfo = new GdsSkuBaseInfo();
                ObjectCopyUtil.copyObjValue(gdsSkuInfoRespDTO, gdsSkuBaseInfo, null, false);
                gdsDetailBaseInfo.setGdsSkuBaseInfo(gdsSkuBaseInfo);
                gds001Resp.setGdsDetailBaseInfo(gdsDetailBaseInfo);
            }

        } catch (BusinessException e) {
            if (resultDto == null || GdsErrorConstants.GdsInfo.ERROR_GOODS_GDSINFO_210005.equals(e.getErrorCode())) {
                resultDto = new GdsInfoDetailRespDTO();
                GdsSkuInfoRespDTO gdsSkuInfoRespDTO = new GdsSkuInfoRespDTO();
                gdsSkuInfoRespDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_DELETE);
                resultDto.setSkuInfo(gdsSkuInfoRespDTO);

            }
            LogUtil.error(MODULE, "无法获取商品详情信息！", e);
        }
        String shopName = "";
        String stockStatus = "";
        String stockStatusDesc = "";
        if (StringUtil.isNotEmpty(resultDto)) {
            if (resultDto.getSkuInfo() != null) {
                stockStatus = GdsUtils.checkStcokStatus(resultDto.getSkuInfo().getRealAmount());
                stockStatusDesc = GdsUtils.checkStcokStatusDesc(resultDto.getSkuInfo().getRealAmount());
            }
            ShopInfoResDTO shopInfo = shopInfoRSV.findShopInfoByShopID(resultDto.getShopId());
            if (StringUtil.isNotEmpty(shopInfo)) {
                shopName = shopInfo.getShopName();
            } else {
                throw new BusinessException("web.gds.2000012");
            }
        }

        GdsDetailBaseInfo gdsDetailBaseInfo = new GdsDetailBaseInfo();
        ObjectCopyUtil.copyObjValue(resultDto, gdsDetailBaseInfo, null, false);
        GdsSkuBaseInfo gdsSkuBaseInfo = new GdsSkuBaseInfo();
        ObjectCopyUtil.copyObjValue(resultDto.getSkuInfo(), gdsSkuBaseInfo, null, false);
        
        
        gdsDetailBaseInfo.setStockStatus(stockStatus);
        
        setPmphExternalInfo(resultDto.getSkuInfo(), gdsDetailBaseInfo); 
        
        
        // 设置基本信息描述URL地址。
        String baseInfoUrl = getBaseInfoUrl(gdsSkuBaseInfo.getGdsId(), gdsSkuBaseInfo.getId(),resultDto);
        gdsDetailBaseInfo.setBaseInfoUrl(baseInfoUrl);
       
        gdsDetailBaseInfo.setGdsSkuBaseInfo(gdsSkuBaseInfo);
        if (resultDto.getParams() != null) {
            List<GdsPropBaseInfo> params = new ArrayList<GdsPropBaseInfo>();
            for (GdsPropRespDTO gdsPropRespDTO : resultDto.getParams()) {
                GdsPropBaseInfo propBaseInfo = new GdsPropBaseInfo();
                ObjectCopyUtil.copyObjValue(gdsPropRespDTO, propBaseInfo, null, false);
                List<GdsPropValueBaseInfo> gdsPropValueBaseInfos = new ArrayList<GdsPropValueBaseInfo>();
                for (GdsPropValueRespDTO gdsPropValueRespDTO : gdsPropRespDTO.getValues()) {
                    GdsPropValueBaseInfo gdsPropValueBaseInfo = new GdsPropValueBaseInfo();
                    ObjectCopyUtil.copyObjValue(gdsPropValueRespDTO, gdsPropValueBaseInfo, null, false);
                    gdsPropValueBaseInfo.setPropId(gdsPropRespDTO.getId());
                    gdsPropValueBaseInfos.add(gdsPropValueBaseInfo);
                }
                propBaseInfo.setValues(gdsPropValueBaseInfos);
                params.add(propBaseInfo);
            }
            gdsDetailBaseInfo.setParams(params);
        }
        if (StringUtil.isBlank(gdsDetailBaseInfo.getContentInfoUrl())) {
        	if(gds001Req.getSkuId()==null || gds001Req.getSkuId()==0){
        		gdsDetailBaseInfo.setContentInfoUrl(getContentHtmlUrl(gds001Req.getGdsId().toString(),""));
        	}else{
        		gdsDetailBaseInfo.setContentInfoUrl(getContentHtmlUrl(gds001Req.getGdsId().toString(),gds001Req.getSkuId().toString()));
        	}
        }
        if (StringUtil.isNotBlank(gdsDetailBaseInfo.getGdsDesc())) {
            gdsDetailBaseInfo.setGdsDesc(getHtmlUrl(gdsDetailBaseInfo.getGdsDesc()));
        } else {
            List<GdsPropRespDTO> props = resultDto.getProps();
            if (CollectionUtils.isNotEmpty(props)) {
                for (GdsPropRespDTO gdsPropRespDTO : props) {
                    if(GdsConstants.GdsProp.GDS_PROP_VALUE_INPUT_TYPE_RICHTXT.equals(gdsPropRespDTO.getPropInputType())){
                        List<GdsPropValueRespDTO> propValue=gdsPropRespDTO.getValues();
                        if (CollectionUtils.isNotEmpty(propValue)) {
                            gdsDetailBaseInfo.setGdsDesc(getHtmlUrl(propValue.get(0).getPropValue()));
                            if(StringUtil.isNotBlank(gdsDetailBaseInfo.getGdsDesc())){
                                break;
                            }
                        }
                    }
                }
            }
        }

        // 获取售买数量
        RQueryGoodPayedRequest goodPayedRequest = new RQueryGoodPayedRequest();
        goodPayedRequest.setSkuId(gdsDetailBaseInfo.getGdsSkuBaseInfo().getId());
        goodPayedRequest.setSiteId(1l);
        Long saleCount = reportGoodPayedRSV.querySumBuyNumBySkuId(goodPayedRequest);
        gdsDetailBaseInfo.setSaleCount(saleCount);

        // 获取商品是否被浏览
        GdsCollectReqDTO reqDTO = new GdsCollectReqDTO();
        reqDTO.setGdsId(this.request.getGdsId());
        reqDTO.setSkuId(this.request.getSkuId());
        reqDTO.setStaffId(reqDTO.getStaff().getId());

        PageResponseDTO<GdsCollectRespDTO> pageResponseDTO = gdsCollectRSV.queryGdsCollectRespDTOPagingByStaff(reqDTO);
        if (pageResponseDTO.getCount() > 0) {
            gdsDetailBaseInfo.setIfBrowse(true);
            gdsDetailBaseInfo.setCollectId(pageResponseDTO.getResult().get(0).getId());
        } else {
            gdsDetailBaseInfo.setIfBrowse(false);

        }

        // 获取库存阈值的配置参数
        gdsDetailBaseInfo.setShopName(shopName);
        gdsDetailBaseInfo.setStockStatusDesc(stockStatusDesc);
        
        // 中等图片规格.
        String middleImgSpec = "";
        // 中图宽高不为空.
        if(null != gds001Req.getMiddleImgHeight() && null != gds001Req.getMiddleImgWidth()){
            middleImgSpec = gds001Req.getMiddleImgHeight() + "x" + gds001Req.getMiddleImgWidth() + "!";
        }else{
            middleImgSpec = "700x700!";
        }
        
        if (resultDto.getMainPic() != null) {
            gdsDetailBaseInfo.setMainPicUrl(new  AiToolUtil().genImageUrl(resultDto.getMainPic().getMediaUuid(), middleImgSpec));
        } else {
            gdsDetailBaseInfo.setMainPicUrl(new  AiToolUtil().genImageUrl("", middleImgSpec));
        }

        List<String> imageUrlList = new ArrayList<String>();
       
        if (resultDto.getMedias() != null && resultDto.getMedias().size() > 0) {
            for (GdsGds2MediaRespDTO media : resultDto.getMedias()) {
                if (media.getMediaUuid() != null) {
                    if(imageUrlList.size() >= 5){
                        break;
                    }
                    String mediaUrl = new  AiToolUtil().genImageUrl(media.getMediaUuid(), middleImgSpec);
                    imageUrlList.add(mediaUrl);
                    
                }

            }

        }
        
        //设置缺货阈值数量
        Long num=0L;
        try {
            num=Long.parseLong(SysCfgUtil.fetchSysCfg(GdsConstants.GdsStock.STOCK_LACK_THRESHOLD).getParaValue());
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取库存缺货阈值失败",e);
        }
        String shareUrl = "";
        if(resultDto != null && resultDto.getSkuInfo() != null){
        	shareUrl = getShareUrl(gds001Req.getGdsId().toString(),resultDto.getSkuInfo().getId().toString());
        }
        gdsDetailBaseInfo.setStockLackNum(num);
        gdsDetailBaseInfo.setShareUrl(shareUrl);
        gdsDetailBaseInfo.setImageUrlList(imageUrlList);
        gds001Resp.setGdsDetailBaseInfo(gdsDetailBaseInfo);
    }

    public void sendRecentlyBrowMsg(GdsInfoDetailRespDTO resultDto) {
        GdsBrowseHisReqDTO gdsBrowseHisReqDTO = new GdsBrowseHisReqDTO();
        try {
            // GDS_BROWSE_HIS_用户ID_单品ID
            String key = KEY + "_" + gdsBrowseHisReqDTO.getStaff().getId() + "_" + resultDto.getSkuInfo().getId();
            if (CacheUtil.getItem(key) == null) {
                // 不存在则发消息
                gdsBrowseHisReqDTO.setGdsId(resultDto.getSkuInfo().getGdsId());
                gdsBrowseHisReqDTO.setSkuId(resultDto.getSkuInfo().getId());
                gdsBrowseHisReqDTO.setShopId(resultDto.getShopId());
                gdsBrowseHisReqDTO.setStaffId(gdsBrowseHisReqDTO.getStaff().getId());
                gdsBrowseHisReqDTO.setBrowsePrice(resultDto.getSkuInfo().getRealPrice());
                GdsMessageUtil.sendGdsBrowseMessage(gdsBrowseHisReqDTO);
                // 缓存到redis
                CacheUtil.addItem(key, key, 1 * 60 * 60 * 6);
            }
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "发送消息失败！", e);
        } catch (Exception e) {
            LogUtil.error(MODULE, "发送消息失败！", e);
        }
    }
    
    
    private void setPmphExternalInfo(GdsSkuInfoRespDTO skuInfo,GdsDetailBaseInfo gdsDetailBaseInfo){
    	// 获取出版日期
        GdsPropRespDTO propAppearDate = skuInfo.getAllPropMaps().get("1005");
        String appearDate = "";
        
        if(null != propAppearDate){
            List<GdsPropValueRespDTO> valueLst = propAppearDate.getValues();
            if(CollectionUtils.isNotEmpty(valueLst)){
            	appearDate = valueLst.get(0).getPropValue();
            }
        }
        gdsDetailBaseInfo.setAppearDate(appearDate);
    	// 获取作者.
        GdsPropRespDTO propAuthor = skuInfo.getAllPropMaps().get("1001");
        String author = "";
        
        if(null != propAuthor){
            List<GdsPropValueRespDTO> valueLst = propAuthor.getValues();
            if(CollectionUtils.isNotEmpty(valueLst)){
                author = valueLst.get(0).getPropValue();
            }
        }
        // 填充作者属性
        gdsDetailBaseInfo.setAuthor(author);
        
        //判断是否有网络增值服务
        boolean hasValueAdded = false;
        GdsPropRespDTO propValueAdded = skuInfo.getAllPropMaps().get(VALUE_ADDED_PROP_ID.toString());
        if(null != propValueAdded){
            List<GdsPropValueRespDTO> valueLst = propValueAdded.getValues();
            if(CollectionUtils.isNotEmpty(valueLst)){
                for(GdsPropValueRespDTO propValue : valueLst){
                    if(VALUE_ADDED_PROP_VALUE_ID.equals(propValue.getId())){
                        hasValueAdded = true;
                        break;
                    }
                }
            }
        }
        gdsDetailBaseInfo.setHasValueAdded(hasValueAdded);
        
        String isbn = "";
        String existCatName = "";
        String catgCode = "";
        // 对应商品ID。
        Long correspondingGdsId = null;
        // 对应单品ID。
        Long correspondingSkuId = null;
        boolean existOtherBook = false;
        GdsPropRespDTO propIsbn = skuInfo.getAllPropMaps().get("1032");
        if(null != propIsbn){
            List<GdsPropValueRespDTO> valueLst = propIsbn.getValues();
            if(CollectionUtils.isNotEmpty(valueLst)){
                isbn = valueLst.get(0).getPropValue();
            }
        }
        
        catgCode = skuInfo.getMainCatgs();
        //获取分类路径
        List<GdscatgsCodeAndNameVO> catgsList = querycatgcodelist(catgCode);
        gdsDetailBaseInfo.setCateList(catgsList);
        
      //判断是显示电子书 还是显示数字教材
        String edbook="";
        if(skuInfo!=null){
        	String platCags = skuInfo.getPlatCatgs();
        	if(platCags.contains("<1200>")){
        		edbook = "0";
        	}else if(platCags.contains("<1201>")){
        		edbook = "1";
        	}else{
        	}
        }
        gdsDetailBaseInfo.setEdbook(edbook);
        
        // 展示对应电子书、数字教材属性
        GdsParseISBNReqDTO gdsParseISBNVO = new GdsParseISBNReqDTO();
        gdsParseISBNVO.setBiazhunisbn(isbn);
        gdsParseISBNVO.setSkuId(skuInfo.getId());
        gdsParseISBNVO.setCatgCode(skuInfo.getMainCatgs());
        List<GdsCatgCodeReqDTO> gdsCateCode = querygdsbyisbn(gdsParseISBNVO);
        
        if(gdsCateCode.size()>1){
            for(int i=0;i<gdsCateCode.size();i++){
                if(!catgCode.equals(gdsCateCode.get(i).getCatgCode())){
                    existOtherBook = true;
                    correspondingGdsId = gdsCateCode.get(i).getGdsId();
                    correspondingSkuId = gdsCateCode.get(i).getSkuId();
                    existCatName = gdsCateCode.get(i).getCatgName();
                    
                    
                    gdsDetailBaseInfo.setExistOtherBook(existOtherBook);
                    gdsDetailBaseInfo.setCorrespondingGdsId(correspondingGdsId);
                    gdsDetailBaseInfo.setCorrespondingSkuId(correspondingSkuId);
                    gdsDetailBaseInfo.setCorrespondingCatgName(existCatName);
                    
                    getGdsDiscountFinalPrice(gdsDetailBaseInfo);
                    break;
                }
            }
        }
        
        AddToCartButtonVO btn = GdsDetailUtil.getDefaultAddToCartButton(gdsDetailBaseInfo.getStockStatus(), skuInfo.isIfNeedStock());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            StockInfoRespDTO skuStock = skuInfo.getStockInfoDTO();
            // 获取1005属性值.
            GdsPropRespDTO prop1005 = skuInfo.getAllPropMaps().get("1005");
            String prop1005Value = null;
            if(null != prop1005){
                if(CollectionUtils.isNotEmpty(prop1005.getValues())){
                    prop1005Value =  prop1005.getValues().get(0).getPropValue();
                }
            }
            
            
            btn = GdsDetailUtil.getAddToCartButton(String.valueOf(skuInfo.getId()), 
                                                                     String.valueOf(skuInfo.getGdsId()), 
                                                                     skuInfo.getRealAmount(), 
                                                                     skuStock.getFacStock(), 
                                                                     gdsDetailBaseInfo.getStockStatus() , 
                                                                     prop1005Value, 
                                                                     sdf.format(skuStock.getZeroStockStarttime()),skuInfo.isIfNeedStock());
            }catch(Exception e){
                LogUtil.error(MODULE, "[商城商品域]商品详请设置加入购物车按钮遇到异常!",e);
            }
        
          gdsDetailBaseInfo.setAddToCartPromp(btn.getAddToCartPromp());
          gdsDetailBaseInfo.setEnableAddToCart(btn.isAddToCartEnable());
    }
    
    private List<GdsCatgCodeReqDTO> querygdsbyisbn(GdsParseISBNReqDTO gdsParseISBNVO) {

        GdsSku2PropPropIdxReqDTO reqDTO = new GdsSku2PropPropIdxReqDTO();
        List<GdsCatgCodeReqDTO> resultList = new ArrayList<GdsCatgCodeReqDTO>();
        try {
            // 纸质书编码
            String paperBookCateCode = SysCfgUtil.fetchSysCfg("GDS_PAPER_BOOK_CAT_CODE").getParaValue();
            if (StringUtil.isBlank(paperBookCateCode)) {
                paperBookCateCode = GDS_PAPER_BOOK_CAT_CODE;
            }
            // 电子书编码
            String eBookCateCode = SysCfgUtil.fetchSysCfg("GDS_E_BOOK_CAT_CODE").getParaValue();
            if (StringUtil.isBlank(eBookCateCode)) {
                eBookCateCode = GDS_E_BOOK_CAT_CODE;
            }
            // 数字教材
            String gdsDigitsBookCatCode = SysCfgUtil.fetchSysCfg("GDS_DIGITS_BOOK_CAT_CODE").getParaValue();
            reqDTO.setPropId(PROP_ID_1032);
            if (StringUtil.isNotBlank(gdsParseISBNVO.getBiazhunisbn())) {
                reqDTO.setPropValue(gdsParseISBNVO.getBiazhunisbn());
            }
            reqDTO.setPageSize(PAGE_SIZE_10);
            // 只取当前商品的
            Map<String, GdsCatgCodeReqDTO> map = new HashMap<String, GdsCatgCodeReqDTO>();
            PageResponseDTO<GdsSkuInfoRespDTO> rspDto = gdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(reqDTO);
            if (rspDto != null) {
                if (rspDto.getResult() != null && rspDto.getResult().size() > 0) {
                    GdsCatgCodeReqDTO gdsCatgCodeVO = null;
                    for (GdsSkuInfoRespDTO gdsSkuInfoRespDTO : rspDto.getResult()) {
                        if (!GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES.equals(gdsSkuInfoRespDTO.getGdsStatus())) {
                            continue;
                        }
                        // 初始化回传到前店的vo,一个标准的isbn可能对应多个商品
                        gdsCatgCodeVO = new GdsCatgCodeReqDTO();

                        GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
                        dto.setCatgCode(gdsSkuInfoRespDTO.getMainCatgs());

                        List<GdsCategoryRespDTO> list = igdsCategoryRSV.queryCategoryTraceUpon(dto);
                        if (list != null && list.size() > 0) {
                            for (GdsCategoryRespDTO gdsCategoryRespDTO : list) {
                                if (gdsSkuInfoRespDTO.getMainCatgs().equals(gdsCategoryRespDTO.getCatgCode())) {
                                    if (gdsParseISBNVO.getCatgCode().equals(gdsCategoryRespDTO.getCatgCode())
                                            && gdsParseISBNVO.getSkuId().equals(gdsSkuInfoRespDTO.getId())) {
                                        map.put("checked", gdsCatgCodeVO);
                                        gdsCatgCodeVO.setChecked("1");
                                        break;
                                    } else {
                                        gdsCatgCodeVO.setChecked("0");
                                    }
                                }
                            }
                            for (GdsCategoryRespDTO gdsCategoryRespDTO1 : list) {
                                if (paperBookCateCode.equals(gdsCategoryRespDTO1.getCatgCode())) {
                                    // 纸质书
                                    matchMapCatgCode(map, paperBookCateCode, gdsCategoryRespDTO1, gdsCatgCodeVO,
                                            gdsSkuInfoRespDTO);
                                    break;
                                } else if (eBookCateCode.equals(gdsCategoryRespDTO1.getCatgCode())) {
                                    // 电子书
                                    matchMapCatgCode(map, eBookCateCode, gdsCategoryRespDTO1, gdsCatgCodeVO,
                                            gdsSkuInfoRespDTO);
                                    break;
                                } else if (gdsDigitsBookCatCode.equals(gdsCategoryRespDTO1.getCatgCode())) {
                                    // 数字教材
                                    matchMapCatgCode(map, gdsDigitsBookCatCode, gdsCategoryRespDTO1, gdsCatgCodeVO,
                                            gdsSkuInfoRespDTO);
                                    break;
                                }
                            }
                        }
                    }
                    int hasPaper = 0;
                    if (map.containsKey("checked")) {
                        Iterator<String> it = map.keySet().iterator();
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            GdsCatgCodeReqDTO value = map.get(key);
                            if ("1".equals(value.getChecked()) && value.getSkuId() != null && !"checked".equals(key)) {
                                if (paperBookCateCode.equals(key)) {
                                    hasPaper++;
                                }
                                resultList.add(value);
                            }
                        }
                        if (hasPaper == 0) {
                            // 如果当前选中不是纸质书，则取纸质书
                            if (map.containsKey(paperBookCateCode)) {
                                resultList.add(map.get(paperBookCateCode));
                            }
                        } else {
                            Iterator<String> its = map.keySet().iterator();
                            while (its.hasNext()) {
                                String key = (String) its.next();
                                GdsCatgCodeReqDTO value = map.get(key);
                                if (resultList.size() <= 1) {
                                    if (!"1".equals(value.getChecked())) {
                                        resultList.add(value);
                                    }
                                } else {
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "根据ISBN号获取商品信息失败！", e);
        } catch (Exception e) {
            LogUtil.error(MODULE, "根据ISBN号获取商品信息失败！", e);
        }
        return resultList;
    
    }
    
    
    
    
    
    private void getGdsDiscountFinalPrice(GdsDetailBaseInfo gdsDetailBaseInfo){
        
        if(null != gdsDetailBaseInfo){
            GdsQueryOption[] gdsQueryOptions = new GdsQueryOption[] { GdsQueryOption.BASIC, GdsQueryOption.MEDIA, GdsQueryOption.PROP };
            SkuQueryOption[] skuQueryOptions = null;
            skuQueryOptions = new SkuQueryOption[] { SkuQueryOption.BASIC, SkuQueryOption.SHOWSTOCK, SkuQueryOption.CAlDISCOUNT,SkuQueryOption.PROP };
            GdsInfoReqDTO dto = new GdsInfoReqDTO();
            dto.setGdsQueryOptions(gdsQueryOptions);
            dto.setId(gdsDetailBaseInfo.getCorrespondingGdsId());
            dto.setSkuId(gdsDetailBaseInfo.getCorrespondingSkuId());
            dto.setSkuQuerys(skuQueryOptions);
            GdsInfoDetailRespDTO resultDto = null;
            resultDto = gdsInfoQueryRSV.queryGdsInfoDetail(dto);
            
            if (resultDto != null && resultDto.getSkuInfo() != null) {
                GdsSkuInfoRespDTO skuInfoResp = resultDto.getSkuInfo();
                GdsPromListDTO list = new GdsPromListDTO();
                Long appSpecPrice = null;
                try {
                    appSpecPrice = skuInfoResp.getAppSpecPrice();
                    
                    PromRuleCheckDTO promRuleCheckDTO = new PromRuleCheckDTO();
                    CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
                    promRuleCheckDTO.setSiteId(promRuleCheckDTO.getCurrentSiteId());
                    promRuleCheckDTO.setStaffId(custInfoReqDTO.getStaff().getId() + "");
                    promRuleCheckDTO.setGdsId(skuInfoResp.getGdsId());
                    promRuleCheckDTO.setChannelValue(CommonConstants.SOURCE.SOURCE_APP);
                    promRuleCheckDTO.setShopId(skuInfoResp.getShopId());
                    promRuleCheckDTO.setCalDate(new Date(System.currentTimeMillis()));
                    promRuleCheckDTO.setSkuId(skuInfoResp.getId());
                    if(null == appSpecPrice || appSpecPrice.equals(0L)){
                        promRuleCheckDTO.setBasePrice(skuInfoResp.getRealPrice());
                        promRuleCheckDTO.setBuyPrice(skuInfoResp.getDiscountPrice());
                    }else{
                        promRuleCheckDTO.setBasePrice(appSpecPrice);
                        promRuleCheckDTO.setBuyPrice(appSpecPrice);
                    }
                    promRuleCheckDTO.setGdsName(skuInfoResp.getGdsName());
                    promRuleCheckDTO.setShopName(promRuleCheckDTO.getShopName());
                    list = promQueryRSV.listPromNew(promRuleCheckDTO);
                } catch (BusinessException e) {
                    LogUtil.error(MODULE, "获取促销列表失败", e);
                }
                if(null != list && CollectionUtils.isNotEmpty(list.getPromList())){
                    gdsDetailBaseInfo.setCorrDiscountFinalPrice(list.getPromList().get(0).getPromSkuPriceRespDTO().getDiscountFinalPrice().longValue());
                }else{
                    if(null != appSpecPrice && !appSpecPrice.equals(0L)){
                        gdsDetailBaseInfo.setCorrDiscountFinalPrice(appSpecPrice);
                    }else{
                        gdsDetailBaseInfo.setCorrDiscountFinalPrice(skuInfoResp.getDiscountPrice());
                    }
                    
                }
            }    
        }
    } 
            
    private GdsCategoryCompareRespDTO comapre(String sourceCode, String targetCode) {
        GdsCategoryCompareReqDTO compareReqDTO = new GdsCategoryCompareReqDTO();
        compareReqDTO.setSourceCode(sourceCode);
        compareReqDTO.setTargetCode(targetCode);
        return igdsCategoryRSV.executeCompare(compareReqDTO);
    }

    
    private void matchMapCatgCode(Map<String, GdsCatgCodeReqDTO> map, String catgCode,
            GdsCategoryRespDTO gdsCategoryRespDTO, GdsCatgCodeReqDTO gdsCatgCodeVO, GdsSkuInfoRespDTO gdsSkuInfoRespDTO) {
        if ("1".equals(gdsCatgCodeVO.getChecked())) {
            gdsCatgCodeVO.setCatgName(gdsCategoryRespDTO.getCatgName());
            gdsCatgCodeVO.setCatgCode(gdsSkuInfoRespDTO.getMainCatgs());
            gdsCatgCodeVO.setGdsId(gdsSkuInfoRespDTO.getGdsId());
            gdsCatgCodeVO.setSkuId(gdsSkuInfoRespDTO.getId());
            map.put(catgCode, gdsCatgCodeVO);
        } else {
            if (!map.containsKey(catgCode)) {
                gdsCatgCodeVO.setCatgName(gdsCategoryRespDTO.getCatgName());
                gdsCatgCodeVO.setCatgCode(gdsSkuInfoRespDTO.getMainCatgs());
                gdsCatgCodeVO.setGdsId(gdsSkuInfoRespDTO.getGdsId());
                gdsCatgCodeVO.setSkuId(gdsSkuInfoRespDTO.getId());
                map.put(catgCode, gdsCatgCodeVO);
            }
        }
    }
    
    private String getBaseInfoUrl(Long gdsId,Long skuId,GdsInfoDetailRespDTO gdsInfoDetailRespDTO){
        if(null != gdsId && null != skuId && CollectionUtils.isNotEmpty(gdsInfoDetailRespDTO.getSkuInfo().getGdsProps())){
            
//            if(!existBaseInfo(gdsInfoDetailRespDTO.getSkuInfo().getGdsProps())){
//                return "";
//            }
            CmsSiteRespDTO cms = CmsCacheUtil.getCmsSiteCache(1L);
            if (cms != null && StringUtil.isNotBlank(cms.getSiteUrl())) {
                String baseUrl = cms.getSiteUrl();
                boolean b = baseUrl.endsWith("/");
                if(b){
                    baseUrl = baseUrl.substring(0, baseUrl.length()-1);
                }
                return baseUrl + "/gdsdetail/html/baseinfo/" + gdsId + "-"+skuId;
            }  
        }
        return "";
    }
    
    
    private boolean existBaseInfo(List<GdsPropRespDTO> gdsPropRespDTOLst){
        boolean exist = false;
        for(GdsPropRespDTO resp : gdsPropRespDTOLst){
            if(GdsConstants.Commons.STATUS_VALID.equals(resp.getIfBasic())){
                List<GdsPropValueRespDTO> values = resp.getValues();
                if(CollectionUtils.isNotEmpty(values)){
                    for(GdsPropValueRespDTO value : values){
                        if(StringUtil.isNotBlank(value.getPropValue())){
                            exist = true;
                            break;
                        }
                    }
                }
            }
        }
        return exist;
    }
    

    private String getHtmlUrl(String vfsId) {
        if (StringUtil.isBlank(vfsId)) {
            return "";
        }
        CmsSiteRespDTO cms = CmsCacheUtil.getCmsSiteCache(1L);
        if (cms != null && StringUtil.isNotBlank(cms.getSiteUrl())) {
            String baseUrl = cms.getSiteUrl();
            boolean b = baseUrl.endsWith("/");
            if(b){
                baseUrl = baseUrl.substring(0, baseUrl.length()-1);
            }
            return baseUrl + "/gdsdetail/html/" + vfsId;
        }
        return "";
    }
    
    private String getContentHtmlUrl(String gdsId,String skuId) {
        if (StringUtil.isBlank(gdsId)) {
            return "";
        }
        CmsSiteRespDTO cms = CmsCacheUtil.getCmsSiteCache(1L);
        if (cms != null && StringUtil.isNotBlank(cms.getSiteUrl())) {
            String baseUrl = cms.getSiteUrl();
            boolean b = baseUrl.endsWith("/");
            if(b){
                baseUrl = baseUrl.substring(0, baseUrl.length()-1);
            }
            return baseUrl + "/gdsdetail/html/contentinfo/" + gdsId+"-"+skuId;
        }
        return "";
    }
    
    /**
     * 
     * querycatgcodelist:(获取分类路径). <br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param mainCatgs
     * @return 
     * @since JDK 1.6
     */
    private List<GdscatgsCodeAndNameVO> querycatgcodelist(String mainCatgs){
        List<GdsCategoryRespDTO> cateList = new ArrayList<GdsCategoryRespDTO>();
        List<GdscatgsCodeAndNameVO> GdscatgsCodeAndName = new ArrayList<GdscatgsCodeAndNameVO>();
        GdsCategoryReqDTO gdsCategoryReqDTO = new GdsCategoryReqDTO();
        gdsCategoryReqDTO.setCatgCode(mainCatgs);
        cateList = igdsCategoryRSV.queryCategoryTraceUpon(gdsCategoryReqDTO);
        if(CollectionUtils.isNotEmpty(cateList)){
        	for(GdsCategoryRespDTO gdsCategoryRespDTO : cateList){
        		GdscatgsCodeAndNameVO gdsDto = new GdscatgsCodeAndNameVO();
        		gdsDto.setCode(gdsCategoryRespDTO.getCatgCode());
        		gdsDto.setName(gdsCategoryRespDTO.getCatgName());
        		GdscatgsCodeAndName.add(gdsDto);
        	}
        }    
        return GdscatgsCodeAndName;
    }
    /** 
     * getFirstSaleGdsPrice:(获取第一个促销价格). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * 
     * @return 
     * @since JDK 1.6 
     */ 
    private String getFirstSaleGdsPrice(GdsSkuInfoRespDTO gdsInfo) {
    	String otherBP="";
        GdsPromListDTO listDto ;
        try {
        	PromRuleCheckDTO promRuleCheckDTO = new PromRuleCheckDTO();
        	CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
        	
        	CustInfoResDTO custInfoResDTO = null;
            if (custInfoReqDTO.getStaff().getId() == 0) {
            	promRuleCheckDTO.setCustLevelValue(custInfoReqDTO.getStaff().getStaffLevelCode());
            } else {
	            custInfoReqDTO.setId(custInfoReqDTO.getStaff().getId());
	            custInfoResDTO = iCustInfoRSV.getCustInfoById(custInfoReqDTO);
	            promRuleCheckDTO.setCustLevelValue(custInfoResDTO.getCustLevelCode());
	            promRuleCheckDTO.setAreaValue(custInfoResDTO.getProvinceCode());
	            promRuleCheckDTO.setStaffId(custInfoResDTO.getId() + "");
            }
        	
        	promRuleCheckDTO.setSiteId(promRuleCheckDTO.getCurrentSiteId());
//        	promRuleCheckDTO.setStaffId(custInfoReqDTO.getStaff().getId() + "");
        	promRuleCheckDTO.setGdsId(gdsInfo.getId());
        	promRuleCheckDTO.setChannelValue(CommonConstants.SOURCE.SOURCE_WEB);
        	promRuleCheckDTO.setShopId(gdsInfo.getShopId());
        	promRuleCheckDTO.setCalDate(new Date(System.currentTimeMillis()));
        	promRuleCheckDTO.setSkuId(gdsInfo.getId());
        	promRuleCheckDTO.setBasePrice(gdsInfo.getRealPrice());
        	promRuleCheckDTO.setBuyPrice(gdsInfo.getDiscountPrice());
        	promRuleCheckDTO.setGdsName(gdsInfo.getGdsName());
        	promRuleCheckDTO.setShopName(promRuleCheckDTO.getShopName());
        	listDto =promQueryRSV.listPromNew(promRuleCheckDTO);
        	if (StringUtil.isNotEmpty(listDto)) {
        		otherBP=listDto.getPromList().get(0).getPromSkuPriceRespDTO().getDiscountFinalPrice().toString();
        	}
        } catch (BusinessException e) {
        	LogUtil.error(MODULE, "获取对应的电子书或者纸质书促销列表失败", e);
        }
        return otherBP;
	}
    /** 
     * getFirstSaleGdsPrice:(获取第一个促销价格). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * 
     * @return 
     * @since JDK 1.6 
     */ 
    private String getShareUrl(String gdsId,String skuId) {
    	String shareUrl="";
		String pcUrl = "";
		CmsSiteRespDTO cms = CmsCacheUtil.getCmsSiteCache(1L);
        if (cms != null && StringUtil.isNotBlank(cms.getSiteUrl())) {
        	pcUrl = cms.getSiteUrl();
            boolean c = pcUrl.endsWith("/");
            if(c){
            	pcUrl = pcUrl.substring(0, pcUrl.length()-1);
            }
//            return baseUrl + "/gdsdetail/html/" + gdsIdAndSkuId;
        }
        shareUrl = pcUrl+"/gdsdetail/share?gdsId="+gdsId+"&skuId="+skuId+"&staffId="+CipherUtil.encrypt(String.valueOf(StaffLocaleUtil.getStaff().getId()));
        return shareUrl;
	}
}
