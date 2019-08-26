/** 
 * File Name:Gds001ActionExtSVImpl.java 
 * Date:2016年11月7日上午10:21:38 
 * 
 */ 
package com.ai.ecp.pmph.service.busi.impl.app;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropValueRespDTO;
import com.ai.ecp.goods.dubbo.dto.app.AppExpandReqDTO;
import com.ai.ecp.goods.dubbo.dto.app.AppExpandRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoDetailRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.goods.service.busi.impl.app.Gds001ActionExtSVImpl;
import com.ai.ecp.goods.service.busi.interfaces.app.IGds001ActionExtSV;
import com.ai.ecp.order.dubbo.util.CommonConstants;
import com.ai.ecp.pmph.dubbo.dto.goods.Gds001ActionExpandDTO;
import com.ai.ecp.pmph.dubbo.dto.goods.GdsCatgCodeReqDTO;
import com.ai.ecp.pmph.dubbo.dto.goods.GdsParseISBNReqDTO;
import com.ai.ecp.prom.dubbo.dto.GdsPromListDTO;
import com.ai.ecp.prom.dubbo.dto.PromRuleCheckDTO;
import com.ai.ecp.prom.dubbo.interfaces.IPromQueryRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**
 * Project Name:pmph-services-server <br>
 * Description: <br>
 * Date:2016年11月7日上午10:21:38  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class Gds001ActionPmphExtSVImpl extends Gds001ActionExtSVImpl implements IGds001ActionExtSV{
    
    private static final String MODULE = Gds001ActionPmphExtSVImpl.class.getName();
    
    
    private static String GDS_E_BOOK_CAT_CODE = "1200";

    private static String GDS_PAPER_BOOK_CAT_CODE = "1115";

    private static String GDS_DIGITS_BOOK_CAT_CODE = "1201";
    
    private static int PAGE_SIZE_10 = 10;
    
    private static Long PROP_ID_1032 = 1032L;
    
    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
    @Resource
    private IGdsCategoryRSV igdsCategoryRSV;
    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;
    @Resource
    private IPromQueryRSV promQueryRSV;

    /** 
     * TODO 简单描述该方法的实现功能（可选）. 
     * @see com.ai.ecp.goods.service.busi.interfaces.app.IGds001ActionExtSV#setExpandInfo(com.ai.ecp.goods.dubbo.dto.app.AppExpandReqDTO) 
     */
    @Override
    public AppExpandRespDTO setExpandInfo(AppExpandReqDTO appExpandReqDTO) {
        if(null != appExpandReqDTO && null != appExpandReqDTO.getSkuInfo()){
            GdsSkuInfoRespDTO skuInfo = appExpandReqDTO.getSkuInfo();
            AppExpandRespDTO appExpandRespDTO = new AppExpandRespDTO();
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
            appExpandRespDTO.putObject("1001", author);
            
            
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
                        
                        Gds001ActionExpandDTO additionInfo = new Gds001ActionExpandDTO();
                        additionInfo.setExistOtherBook(existOtherBook);
                        additionInfo.setCorrespondingGdsId(correspondingGdsId);
                        additionInfo.setCorrespondingSkuId(correspondingSkuId);
                        additionInfo.setCoorespondingCatgName(existCatName);
                        
                        appExpandRespDTO.putObject("existOtherBook", existOtherBook);
                        appExpandRespDTO.putObject("correspondingGdsId", correspondingGdsId);
                        appExpandRespDTO.putObject("correspondingSkuId", correspondingSkuId);
                        appExpandRespDTO.putObject("existCatName", existCatName);
                        
                        getGdsDiscountFinalPrice(additionInfo,appExpandRespDTO);
                        break;
                    }
                }
            }
            
            return appExpandRespDTO;
        }
        return null;
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
    
    
    private void getGdsDiscountFinalPrice(Gds001ActionExpandDTO expandDTO,AppExpandRespDTO appExpandRespDTO){
        
        if(null != expandDTO){
            GdsQueryOption[] gdsQueryOptions = new GdsQueryOption[] { GdsQueryOption.BASIC, GdsQueryOption.MEDIA, GdsQueryOption.PROP };
            SkuQueryOption[] skuQueryOptions = null;
            skuQueryOptions = new SkuQueryOption[] { SkuQueryOption.BASIC, SkuQueryOption.SHOWSTOCK, SkuQueryOption.CAlDISCOUNT,SkuQueryOption.PROP };
            GdsInfoReqDTO dto = new GdsInfoReqDTO();
            dto.setGdsQueryOptions(gdsQueryOptions);
            dto.setId(expandDTO.getCorrespondingGdsId());
            dto.setSkuId(expandDTO.getCorrespondingSkuId());
            dto.setSkuQuerys(skuQueryOptions);
            GdsInfoDetailRespDTO resultDto = null;
            resultDto = gdsInfoQueryRSV.queryGdsInfoDetail(dto);
            
            if (resultDto != null && resultDto.getSkuInfo() != null) {
                GdsSkuInfoRespDTO skuInfoResp = resultDto.getSkuInfo();
                GdsPromListDTO list = new GdsPromListDTO();
                try {
                    Long appSpecPrice = skuInfoResp.getAppSpecPrice();
                    
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
                    appExpandRespDTO.putObject("corrDiscountFinalPrice", list.getPromList().get(0).getPromSkuPriceRespDTO().getDiscountFinalPrice());
                }else{
                    appExpandRespDTO.putObject("corrDiscountFinalPrice", skuInfoResp.getDiscountPrice());
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
    
    
}

