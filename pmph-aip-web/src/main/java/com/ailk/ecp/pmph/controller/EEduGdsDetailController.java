package com.ailk.ecp.pmph.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.cms.dubbo.util.CmsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropValueRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2MediaRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.EEduGdsDetailVO;

/**
 * 
 * Project Name:pmph-aip-web <br>
 * Description: 提供给人卫e教的获取商品详情接口<br>
 * Date:2017年8月22日上午10:07:45  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
public class EEduGdsDetailController extends BaseController{
    public static final int MAXSIZE = 50;
    
    public static final Long VERNUMPROPID = 1008L;
    
    public static final Long MALLSIZEID = 1L;
    
    public static final String UUIDPROPIDS = ",1020,1023,1024,1025,";//需要去mogodb取详情的
    
    private static final String MODULE = EEduGdsDetailController.class.getName();
            
    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.pmph.gdsdetail.services")
    @Security(mustLogin=true,authorCheckType=DefaultServiceCheckChain.class)
    @ResponseBody
    public Map<String,Object> eEduGdsDetail(EEduGdsDetailVO vo) throws BusinessException{
        Map<String,Object> retMap = new HashMap<String,Object>();
        List<String> versionNumbers = this.dealParam(vo);
        if(0 >= versionNumbers.size()){
            retMap.put(RespUtil.CODE,"0");
            retMap.put(RespUtil.MSG, "参数versionNumber缺失");
            LogUtil.error(MODULE, "人卫e教通过本版编号获取商品详情，参数versionNumber缺失");
            return retMap;
        }
        if(MAXSIZE < versionNumbers.size()){
            retMap.put(RespUtil.CODE,"0");
            retMap.put(RespUtil.MSG, "单次请求参数versionNumber最多50个");
            LogUtil.error(MODULE, "人卫e教通过本版编号获取商品详情，单次请求参数versionNumber超过50个");
            return retMap;
        }
        GdsSku2PropPropIdxReqDTO reqDto = new GdsSku2PropPropIdxReqDTO();
        reqDto.setPropId(VERNUMPROPID);//本版编号
        reqDto.setPropValues(versionNumbers);
        reqDto.setPageNo(1);
        reqDto.setPageSize(Integer.MAX_VALUE);
        
        reqDto.setOptions(new GdsOption.SkuQueryOption[]{SkuQueryOption.BASIC,SkuQueryOption.MEDIA,SkuQueryOption.PROP});
        List<Long> propIds = new ArrayList<Long>();
        propIds.add(1001L);//作者
        propIds.add(1005L);//出版日期
        propIds.add(1006L);//出版社
        propIds.add(1007L);//读者对象
        propIds.add(1008L);//本版编号
        propIds.add(1010L);//版次
        propIds.add(1017L);//语言
        propIds.add(1020L);//内容简介
        propIds.add(1023L);//编辑推荐
        propIds.add(1024L);//专家推荐
        propIds.add(1025L);//章节节选
        propIds.add(1026L);//PDF
        propIds.add(1002L);//ISBN
        reqDto.setPropIds(propIds);
        
        try{
            PageResponseDTO<GdsSkuInfoRespDTO> result = gdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(reqDto);
            retMap.put(RespUtil.CODE,"1");
            retMap.put(RespUtil.MSG, "成功");
            retMap.put("count",result.getCount());
            retMap.put("responseData",dealResult(result.getResult()));
            LogUtil.info(MODULE, "人卫e教通过本版编号获取商品详情成功");
        }catch(Exception e){
            retMap.put(RespUtil.CODE,"2");
            retMap.put(RespUtil.MSG, "失败");
            LogUtil.error(MODULE, "人卫e教通过本版编号获取商品详情失败",e);
        }
        
        return RespUtil.renderRootResp(retMap);
    }
    /**
     * 
     * dealParam:(将参数分解成数组). <br/> 
     * 
     * @param vo
     * @return 
     * @since JDK 1.6
     */
    private List<String> dealParam (EEduGdsDetailVO vo){
        List<String> params = new ArrayList<String>();
        String paramStr = "";
        if(null != vo){
            paramStr = vo.getVersionNumber();
        }
        if(StringUtil.isNotBlank(paramStr)){
            String[] subParamStr = paramStr.split(",");
            if(null != subParamStr && 0 < subParamStr.length){
                for(String str : subParamStr){
                    if(StringUtil.isNotBlank(str)){
                        params.add(str);
                    }
                }
            }
        }
        LogUtil.info(MODULE, "人卫e教通过本版编号获取商品详情，参数versionNumber:"+params.toString());
        return params;
    }
    /**
     * 
     * dealResult:(处理结果). <br/> 
     * 
     * @param datas
     * @return 
     * @since JDK 1.6
     */
    private Map<String,Object> dealResult(List<GdsSkuInfoRespDTO> datas){
        Map<String,Object> resultMap= new HashMap<String,Object>();
        List<Map<String,Object>> gdsList = new ArrayList<Map<String,Object>>();
        resultMap.put("results", gdsList);
        if(CollectionUtils.isEmpty(datas)){
            return resultMap;
        }
        for(GdsSkuInfoRespDTO sku : datas){
            if(null == sku || null == sku.getId() || StringUtil.isNotBlank(sku.getExt1())){
                continue;
            }
            Map<String, GdsPropRespDTO> propMap = sku.getAllPropMaps();
            if(null == propMap){
                propMap = new HashMap<String, GdsPropRespDTO>();
            }
            Map<String,Object> skuJson = new HashMap<String,Object>();
            skuJson.put("gdsId", sku.getGdsId());
            skuJson.put("gdsName",sku.getGdsName());
            skuJson.put("gdsStatus",sku.getGdsStatus());
            skuJson.put("author",getPropValue(propMap,"1001").get("value"));
            skuJson.put("publicDate", getPropValue(propMap,"1005").get("value"));
            skuJson.put("publicCompany", getPropValue(propMap,"1006").get("value"));
            skuJson.put("reader", getPropValue(propMap,"1007").get("value"));
            skuJson.put("versionNumber", getPropValue(propMap,"1008").get("value"));
            skuJson.put("edition", getPropValue(propMap,"1010").get("value"));
            skuJson.put("language", getPropValue(propMap,"1017").get("value"));
            skuJson.put("isbn",getPropValue(propMap,"1002").get("value"));
            //简介列表
            List<Map<String,Object>> gdsDescList = new ArrayList<Map<String,Object>>();
            String[] gdsDescStrs = UUIDPROPIDS.split(",");//现在的商品描述都需要取mogodb  刚好是UUIDPROPIDS的值
            for(String propId : gdsDescStrs){
                if(StringUtil.isNotBlank(propId)){
                    Map<String,Object> propJson = getPropValue(propMap,propId);
                    String propName = (String) propJson.get("name");
                    if(StringUtil.isNotBlank(propName)){
                        Map<String,Object> gdsDescJson = new HashMap<String,Object>();
                        gdsDescJson.put("gdsDescName", propName);
                        gdsDescJson.put("gdsDescContent", propJson.get("value"));
                        gdsDescList.add(gdsDescJson);
                    }
                }
            }
            skuJson.put("gdsDescList", gdsDescList);
            //图片列表
            List<Map<String,Object>> imageList = new ArrayList<Map<String,Object>>();
            List<GdsSku2MediaRespDTO> sku2MediaRespDTOs = sku.getSku2MediaRespDTOs();
            if(CollectionUtils.isNotEmpty(sku2MediaRespDTOs)){
                for(GdsSku2MediaRespDTO media : sku2MediaRespDTOs){
                    if(null != media && GdsConstants.GdsMedia.MEDIA_TYPE_PIC.equalsIgnoreCase(media.getMediaType())){
                        Map<String,Object> mediaJson = new HashMap<String,Object>();
                        mediaJson.put("imgName", media.getMediaUuid());
                        mediaJson.put("imgUrl", ImageUtil.getImageUrl(media.getMediaUuid()));
                        imageList.add(mediaJson);
                    }
                }
            }
            if(0 >= imageList.size()){
                Map<String,Object> emptyMediaJson = new HashMap<String,Object>();
                emptyMediaJson.put("imgName", "");
                emptyMediaJson.put("imgUrl", ImageUtil.getImageUrl(""));
                imageList.add(emptyMediaJson);
            }
            skuJson.put("imageList", imageList);
            //访问地址
            String pdfUuid = (String) getPropValue(propMap,"1026").get("value");
            String pdfUrl = "";
            if(StringUtil.isNotBlank(pdfUuid)){
                pdfUrl = ImageUtil.getStaticDocUrl(pdfUuid, "doc");
            }
            skuJson.put("pdfFile", pdfUrl);
            String url = sku.getUrl();
            skuJson.put("webGdsDetailUrl", formatUrl(getPcMallSiteUrl(MALLSIZEID),url));
            skuJson.put("appGdsDetailUrl", formatUrl(getMobileMallUrl(MALLSIZEID),url));
            
            gdsList.add(skuJson);
        }
        return resultMap;
    }
    /**
     * 
     * getPropValue:(获取属性值). <br/> 
     * 
     * @param propMap
     * @param key
     * @return 
     * @since JDK 1.6
     */
    private Map<String,Object> getPropValue(Map<String, GdsPropRespDTO> propMap,String key){
        Map<String,Object> json = new HashMap<String,Object>();
        if(null == propMap || StringUtil.isBlank(key)){
            json.put("name", "");
            json.put("value", "");
            return json;
        }
        GdsPropRespDTO resp = null;
        try {
            resp = propMap.get(key);
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取属性值异常，key："+key,e);
        }
        if(null != resp){
            json.put("name", resp.getPropName());
            List<GdsPropValueRespDTO> values = resp.getValues();
            if(CollectionUtils.isNotEmpty(values)){
                String value = values.get(0).getPropValue();
                if(null == value){
                    value = "";
                }
                //需要去mongodb取
                if(StringUtil.isNotBlank(value) && 0 <= UUIDPROPIDS.indexOf(","+key+",")){
                    value = getRichText(value);
                }
                json.put("value", value);
            }else{
                json.put("value", "");
            }
        }else{
            json.put("name", "");
            json.put("value", "");
        }
        return json;
    }
    /**
     * 
     * getHtmlFormMongoDB:(从mongodb获取富文本源码). <br/> 
     * 
     * @param fileId
     * @return 
     * @since JDK 1.6
     */
    private String getRichText(String fileId){
        if(StringUtil.isEmpty(fileId)){
            return "";
        }
        String result = null;
        
        byte[] content = null;
        try {
            content = FileUtil.readFile(fileId); 
            if (ArrayUtils.isNotEmpty(content)) {
                Document dom = Jsoup.parse(new String(content));
                Elements imgs = dom.getElementsByTag("img");
                int len = imgs.size();
                for(int i=0; i < len ;i++){
                    Element img = imgs.get(i);
                    String src = img.attr("src");
                    if(StringUtil.isNotBlank(src)){
                        img.attr("src", dealImgSrc(src));
                    }
                }
                result = dom.toString();
            }
        } catch (Exception e) {
            LogUtil.error(MODULE, "mogoDB读取商品描述异常", e);
        }
        
        if(null == result){
            result = "";
        }
        return result;
    }
    /**
     * 
     * dealImgSrc:(处理图片地址). <br/> 
     * 
     * @param src /imageServer/image/59a9127b0cf2062bcb6e9dc8_200x200!.jpg
     * @return 
     * @since JDK 1.6
     */
    private String dealImgSrc(String src) {
        if(StringUtil.isBlank(src)){
            return src;
        }
        String reg = "^[/\\\\]*imageServer[/\\\\]+image[/\\\\]+([0-9a-zA-Z]+)(?:_[0-9a-zA-Z]+!?)?\\.[0-9a-zA-Z]+$";
        try {
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(src);
            if (matcher.matches()) {
               src =  ImageUtil.getImageUrl(matcher.group(1));
            }
        } catch (Exception e) {
            LogUtil.info(MODULE, "图片地址正则处理异常");
        }
        return src;
    }
    /**
     * 
     * formatUrl:(获取地址). <br/> 
     * 
     * @param base
     * @param url
     * @return 
     * @since JDK 1.6
     */
    private String formatUrl(String base,String url){
        if(null == base){
            base = "";
        }
        if(null == url){
            base = "";
        }
        return base.replaceAll("(?:/|\\\\)+$", "")+"/"+url.replaceAll("^(?:/|\\\\)+", "");
    }
    /**
     * 
     * getPcMallSiteUrl:(获取pc端mall商城地址 过滤掉尾部的/). <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    private String getPcMallSiteUrl(Long siteId){
        if(StringUtil.isEmpty(siteId)){
            return "";
        }
        CmsSiteRespDTO pcMallSiteDto = null;
        try {
            pcMallSiteDto = CmsCacheUtil.getCmsSiteCache(siteId);
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取pc商城地址异常");
        }
        String pcMallUrl = "";
        if(null != pcMallSiteDto){
            pcMallUrl = pcMallSiteDto.getSiteUrl();
        }
        pcMallUrl = null == pcMallUrl  ? "" : pcMallUrl.replaceAll("(?:/|\\\\)+$", "");
        return pcMallUrl;
    }
    /**
     * 
     * getMobileMallUrl:(从配置表获取微信端mall商城地址 过滤掉尾部的/). <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    private String getMobileMallUrl(Long siteId){
        if(StringUtil.isEmpty(siteId)){
            return "";
        }
        String mobileMallUrl = "";
        try {
            mobileMallUrl = BaseParamUtil.fetchParamValue(CmsConstants.ParamConfig.CMS_SITE_MAPPING,siteId.toString());//微商地址
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取微信商城地址异常，读取配置表CMS_SITE_MAPPING！");
        }
        mobileMallUrl = mobileMallUrl == null ? "" : mobileMallUrl.replaceAll("(?:/|\\\\)+$", "");
        return mobileMallUrl;
    }
}

