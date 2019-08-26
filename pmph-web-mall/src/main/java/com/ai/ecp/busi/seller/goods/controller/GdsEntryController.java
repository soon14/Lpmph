package com.ai.ecp.busi.seller.goods.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.velocity.AiToolUtil;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.seller.goods.vo.GdsInfoEditVO;
import com.ai.ecp.busi.seller.goods.vo.GdsInfoVO;
import com.ai.ecp.busi.seller.goods.vo.GdsMediaVO;
import com.ai.ecp.busi.seller.goods.vo.GdsShiptempVO;
import com.ai.ecp.demo.dubbo.dto.DemoCfgRespDTO;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatg2PropRelationRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatg2PropReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatlog2ShopReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsMediaReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsMediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropValueRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsShiptempReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsShiptempRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsType2PropRelationRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsType2PropReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsTypeRespDTO;
import com.ai.ecp.goods.dubbo.dto.common.LongListRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoAddReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2CatgReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2MediaReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2MediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2PropReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2MediaReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2MediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2PriceReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2PropReqDTO;
import com.ai.ecp.goods.dubbo.dto.price.GdsPriceLadderReqDTO;
import com.ai.ecp.goods.dubbo.dto.price.GdsPriceReqDTO;
import com.ai.ecp.goods.dubbo.dto.price.GdsPriceTypeRespDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepAdaptReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCatlog2ShopRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoManageRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsMediaRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsPriceRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsShiptemRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsStockRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsType2PropRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsTypeRSV;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.BaseParamDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.SellerResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.ecp.system.filter.SellerLocaleUtil;
import com.ai.ecp.system.util.Escape;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.MoneyUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
/**
 * 
 * Project Name:ecp-web-manage <br>
 * Description: 商品录入（包括商品信息编辑、商品信息查看）<br>
 * Date:2015年8月25日下午5:00:46  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value="/seller/goods/gdsinfoentry")
public class GdsEntryController extends GdsBaseController {
    private static String MODULE = GdsEntryController.class.getName();
    private static String URL = "/seller/goods/gdsinfoentry";
    private static String EDIT_URL = "/seller/goods/gdsinfoentry/gdsinfoedit";
    private static final String PIC_MAIN_FLAG  = "1";
    private static final String MULTI_SWITCH_ON = "1";//多功能开
    private static final String MULTI_SWITCH_OFF = "0";//多功能关
    private static final String STOCK_SWITCH_ON = "1";//高级库存能开
    private static final String STOCK_SWITCH_OFF = "0";//高级库存关
    private static final String PRICE_SWITCH_ON = "1";//高级价格开
    private static final String PRICE_SWITCH_OFF = "0";//高级价格关
    private static final String GDS_LADDER_PRICE = "1";
    private static final String PROP_INPUT_TYPE_3 = "3";
    private static final String COPY_FLAG = "1";
    private static final String NOT_SCORE_GDS = "0";//不是积分商品，是普通商品
    @Resource
    private IGdsCategoryRSV igdsCategoryRSV;
    @Resource
    private IGdsType2PropRSV gdsType2PropRSV;
    @Resource
    private IGdsTypeRSV iGdsTypeRSV;
    @Resource
    private IGdsInfoManageRSV iGdsInfoManageRSV;
    @Resource
    private IGdsPriceRSV iGdsPriceRSV;
    @Resource
    private IGdsShiptemRSV iGdsShiptemRSV;
    @Resource
    private IGdsMediaRSV iGdsMediaRSV;
    @Resource
    private IGdsInfoQueryRSV iGdsInfoQueryRSV;
    @Resource
    private IGdsStockRSV iGdsStockRSV;
    @Resource
    private IShopInfoRSV iShopInfoRSV;
    @Resource
    private IGdsTypeRSV gdsTypeRSV;
    @Resource
    private IGdsCatlog2ShopRSV gdsCatlog2ShopRSV;
    @RequestMapping()
    public String init(Model model){
        GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
        //dto.setCatlogId(1l);
        dto.setCatlogIds(fetchCatlogIdsByCurrentUser(null));
        List<GdsCategoryRespDTO> list = igdsCategoryRSV.queryRootCategory(dto);
        model.addAttribute("classList", list);
        
      //商品类型列表获取
        List<GdsTypeRespDTO> typeList=gdsTypeRSV.queryAllGdsTypesFromCache();
        model.addAttribute("typeList", typeList);
        
        model.addAttribute("haveType", CollectionUtils.isNotEmpty(typeList));
        
        return URL+"/gds-chooseClass";
    }
    
    /**
     * 
     * queryClass:(产品录入前初始化分类). <br/> 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/queryclass")
    @ResponseBody
    public Model queryClass(Model model){
        PageResponseDTO<DemoCfgRespDTO> t = new PageResponseDTO<DemoCfgRespDTO>();
        EcpBasePageRespVO<Map> respVO = null;
        try {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
           
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "获取分类失败！", e);
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取分类失败！", e);
        }
        return super.addPageToModel(model, respVO);
    }
    
    @RequestMapping("/getCategorysByShop")
    
	public String fetchCateGorysByCurrentShop(Model model,Long shopId) {

		List<Long> catlogIds = new ArrayList<Long>();
		GdsCatlog2ShopReqDTO relationQuery = new GdsCatlog2ShopReqDTO();
		relationQuery.setShopId(shopId);
		LongListRespDTO longListResp = gdsCatlog2ShopRSV.queryGdsCatlog2ShopRespDTOByShopId(relationQuery);
		if (null != longListResp && CollectionUtils.isNotEmpty(longListResp.getLst())) {
			for (Long l : longListResp.getLst()) {
				if (!catlogIds.contains(l)) {
					catlogIds.add(l);
				}
			}
		}

		GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
		dto.setCatlogIds(catlogIds);
		List<GdsCategoryRespDTO> list = igdsCategoryRSV.queryRootCategory(dto);
		 model.addAttribute("classList", list);
		return URL + "/list/list-shopclass/gds-shopClass";
	}
    
    
    private List<GdsPropRespDTO> propMerge(List<GdsPropRespDTO> oldProp,List<GdsPropRespDTO> newProp){
         //属性合并：覆盖优先级从低到高为 类型-->分类-->子分类
        List<GdsPropRespDTO> list;
        if(CollectionUtils.isNotEmpty(oldProp)){
            list=oldProp;
        }else{
            list=new ArrayList<GdsPropRespDTO>();
        }
        for(GdsPropRespDTO o:newProp){
            
            boolean contains=false;
            for(GdsPropRespDTO i:oldProp){
                if(o.getId().longValue()==i.getId().longValue()){
                    contains=true;
                    break;
                }
            }
            
            if(!contains){
                list.add(o);
            }
            
        }
        
        return list;
    }
    
    /**
     * 
     * entryBaseInfo:(进去产品录入界面时候，进行的数据初始化). <br/> 
     * @param model
     * @param catgCode
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/entrybaseinfo")
    public String entryBaseInfo(Model model,GdsInfoVO gdsInfoVO){
        if(StringUtil.isBlank(gdsInfoVO.getCatgCode())){
            return "redirect:/seller/goods/gdsinfoentry";
        }
        String isVistaul = "0";
        try {
            GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
            dto.setCatgCode(gdsInfoVO.getCatgCode());
            List<GdsCategoryRespDTO> list = igdsCategoryRSV.queryCategoryTraceUpon(dto);
            if(list!= null && list.size()>0){
                for(GdsCategoryRespDTO gdsCategoryRespDTO : list){
                    if("1199".equals(gdsCategoryRespDTO.getCatgCode())){
                        isVistaul = "1";
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取分类的顶级分类错误！", e);
        }
        model.addAttribute("isVistaul", isVistaul);
       
        GdsCatg2PropReqDTO reqDto = new GdsCatg2PropReqDTO();
        reqDto.setCatgCode(gdsInfoVO.getCatgCode());
        reqDto.setIfContainTopProp(true);
        reqDto.setIfGdsInputQuery(false);
        GdsCatg2PropRelationRespDTO rspDto = igdsCategoryRSV.queryCategoryProps(reqDto);
        
        //加入优先级最低的类型属性
        GdsType2PropReqDTO reqDTO=new GdsType2PropReqDTO();
        reqDTO.setTypeId(gdsInfoVO.getGdsTypeId()); 
        reqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
        GdsType2PropRelationRespDTO gdsType2PropRelationRespDTO=gdsType2PropRSV.queryTypeProps(reqDTO);
        
//        List<BaseParamDTO> gdsTypeCodeList = BaseParamUtil.fetchParamList("GDS_INFO_TYPE");
        model.addAttribute("catgCode", gdsInfoVO.getCatgCode());
        model.addAttribute("catgName", gdsInfoVO.getCatgName());
        model.addAttribute("gdsTypeId", gdsInfoVO.getGdsTypeId());
        model.addAttribute("gdsTypeName", gdsInfoVO.getGdsTypeName());
//        model.addAttribute("gdsTypeList", gdsTypeCodeList);
        model.addAttribute("rspDto", rspDto);
        
        String multiValue = SysCfgUtil.fetchSysCfg("GDS_MULTI_PRODUCT_CATEGORY").getParaValue();
        
        if(rspDto==null&&gdsType2PropRelationRespDTO==null){
            List<GdsPropRespDTO> list=new ArrayList<GdsPropRespDTO>();
            model.addAttribute("basics", JSONArray.fromObject(list).toString().replaceAll("\"", "\'"));
            model.addAttribute("params", JSONArray.fromObject(list).toString().replaceAll("\"", "\'"));
            model.addAttribute("others", JSONArray.fromObject(list).toString().replaceAll("\"", "\'"));
        }else{
            
            List<GdsPropRespDTO> list=propMerge(rspDto.getBasics(), gdsType2PropRelationRespDTO.getBasics());
            model.addAttribute("basics", JSONArray.fromObject(list).toString().replaceAll("\"", "\'"));
            rspDto.setBasics(list);
            
            list=propMerge(rspDto.getParams(), gdsType2PropRelationRespDTO.getParams());
            model.addAttribute("params", JSONArray.fromObject(list).toString().replaceAll("\"", "\'"));
            rspDto.setParams(list);
            
            list=propMerge(rspDto.getOthers(), gdsType2PropRelationRespDTO.getOthers());
            model.addAttribute("others", JSONArray.fromObject(list).toString().replaceAll("\"", "\'"));
            rspDto.setOthers(list);
            
            list=propMerge(rspDto.getSpecs(), gdsType2PropRelationRespDTO.getSpecs());
            rspDto.setSpecs(list);
            if(CollectionUtils.isEmpty(list)){
                multiValue = "0";
            }
            
            list=propMerge(rspDto.getFileParam(), gdsType2PropRelationRespDTO.getFileParam());
            rspDto.setFileParam(list);
            
            list=propMerge(rspDto.getEditoParam(), gdsType2PropRelationRespDTO.getEditoParam());
            rspDto.setEditoParam(list);
            
        }
       
        //默认最多可上传多少张图片BaseParamUtil.fetchParamValue("GDS_ENTRY_PIC_LIMIT_COUNT", "1")
        String picAmount = SysCfgUtil.fetchSysCfg("GDS_ENTRY_PIC_LIMIT_COUNT").getParaValue();
        model.addAttribute("upPictrueAmount", new Array[Integer.parseInt(picAmount)]);
        //手工录入 BaseParamUtil.fetchParamValue("GDS_PROP_VALUE_TYPE", "1")
        model.addAttribute("PROP_VALUE_TYPE_1", GdsConstants.GdsProp.PROP_VALUE_TYPE_1);
        //单选 BaseParamUtil.fetchParamValue("GDS_PROP_VALUE_TYPE", "2")
        model.addAttribute("PROP_VALUE_TYPE_2",GdsConstants.GdsProp.PROP_VALUE_TYPE_2);
        //多选BaseParamUtil.fetchParamValue("GDS_PROP_VALUE_TYPE", "3")
        model.addAttribute("PROP_VALUE_TYPE_3",GdsConstants.GdsProp.PROP_VALUE_TYPE_3);
        //富文本编辑
        model.addAttribute("PROP_VALUE_TYPE_4",GdsConstants.GdsProp.PROP_VALUE_TYPE_4);
        //高级库存默认开启或关闭SysCfgUtil.fetchSysCfg("GDS_ENTRY_STOCK_SENIOR")
        List<BaseParamDTO> mediaTypeList = BaseParamUtil.fetchParamList("GDS_MEDIA_TYPE");
        model.addAttribute("mediaTypeList", mediaTypeList);
        //商品归属分类最大数量
        model.addAttribute("GDS_CATEGORY_MAX_COUNT", SysCfgUtil.fetchSysCfg("GDS_CATEGORY_MAX_COUNT").getParaValue());
        model.addAttribute("stock-switch", SysCfgUtil.fetchSysCfg("GDS_ENTRY_STOCK_SENIOR").getParaValue());
        //高级价格默认开启或关闭SysCfgUtil.fetchSysCfg("GDS_ENTRY_PRICE_SENIOR")
        model.addAttribute("price-switch", SysCfgUtil.fetchSysCfg("GDS_ENTRY_PRICE_SENIOR").getParaValue());
        //多功能默认开启或关闭
        model.addAttribute("multi-switch", multiValue);
        //阶梯价开关
        model.addAttribute("ladder-switch",  SysCfgUtil.fetchSysCfg("GDS_ENTRY_LADDER_PRICE").getParaValue()); 
        //店铺平台分类开关
        model.addAttribute("GDS_SHOP_CATEGORY", SysCfgUtil.fetchSysCfg("GDS_SHOP_CATEGORY").getParaValue());
        //设置店铺id
        model.addAttribute("shopId", gdsInfoVO.getShopIdVal());
        return URL+"/gdsInfoEntry";
    }
    
    /**
     * 
     * gridmedialist:(获取媒体库列表). <br/> 
     * @param model
     * @param picCatgCode
     * @param mediaName
     * @param mediaType
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/gridmedialist")
    public String gridmedialist(Model model,GdsMediaVO gdsMediaVO){
        GdsMediaReqDTO dto = gdsMediaVO.toBaseInfo(GdsMediaReqDTO.class);
        if(StringUtil.isNotBlank(gdsMediaVO.getMediaName())){
            dto.setMediaName(gdsMediaVO.getMediaName());
        }
        if(StringUtil.isNotBlank(gdsMediaVO.getMediaType())){
            dto.setMediaType(gdsMediaVO.getMediaType());
        }else{
            dto.setMediaType("1");
        }
        if(StringUtil.isNotEmpty(gdsMediaVO.getShopId())){
            dto.setShopId(gdsMediaVO.getShopId()); 
        }else{
            throw new BusinessException("web.gds.200008");
        }
        PageResponseDTO<GdsMediaRespDTO> list = null;
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = null;
        try {
            list =  iGdsMediaRSV.queryGdsInfoListPage(dto);
            if(list !=null && list.getResult()!=null && list.getResult().size()>0){
                for(GdsMediaRespDTO gdsMediaRespDTO : list.getResult()){
                    gdsMediaRespDTO.setURL(new AiToolUtil().genImageUrl(gdsMediaRespDTO.getMediaUuid(),"124x124!"));
                }
            }
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(list);
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "获取媒体列表失败！", e);
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取媒体列表失败！", e);
        }
        model.addAttribute("shopId", gdsMediaVO.getShopId());
        model.addAttribute("list", respVO);
        return URL+"/medialist/gds-medialist";
    }
    
    /**
     * 
     * toGdsCombine:(商品组合弹出框内元素信息页). <br/> 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="togdscombine")
    public String toGdsCombine(){
        return URL+"/list/gds-combine";
    }
    /**
     * 
     * toPlatform:(平台分类弹出框内的元素信息页). <br/> 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/toplatform")
    public String toPlatform(){
        return URL+"/list/gds-platform";
    }
    /**
     * 
     * toShopClass:(店铺分类弹出框内的元素信息页). <br/> 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/toshopclass")
    public String toShopClass(){
        return URL+"/list/gds-shopClass";
    }
    /**
     * 
     * toSeniorPrice:(高级价格弹出框内的元素信息页). <br/> 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/toseniorprice")
    public String toSeniorPrice(Model model){
        List<GdsPriceTypeRespDTO> priceTypeList = iGdsPriceRSV.queryAllPriceType(new BaseInfo());
        model.addAttribute("priceTypeList", priceTypeList);
        return URL+"/list/senior-price";
    }
    
    /**
     * 
     * getpricetarget:(根据价格类型获取目标客户). <br/> 
     * @param model
     * @param priceTypeCode
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/getpricetarget")
    public Model getpricetarget(Model model,@RequestParam("priceTypeCode") String priceTypeCode){
        model.addAttribute("priceTargetList", "");
        return model;
    }

    /**
     * 
     * toSkuPictrues:(弹出单品图片的窗口). <br/> 
     * @param model
     * @param pictrueList
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/toskupictrues")                
    public String toSkuPictrues(Model model,@RequestParam("shopId")String shopId){
        //媒体图片
        GdsMediaReqDTO dto = new GdsMediaReqDTO();
        dto.setMediaType("1");
        if(StringUtil.isNotBlank(shopId)){
            dto.setShopId(Long.parseLong(shopId));
        }else{
            throw new BusinessException("web.gds.200008");
        }
        PageResponseDTO<GdsMediaRespDTO> list = null;
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = null;
        try {
            list =  iGdsMediaRSV.queryGdsInfoListPage(dto);
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(list);
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "获取媒体列表失败！", e);
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取媒体列表失败！", e);
        }
        model.addAttribute("picList", respVO);
        String picAmount = SysCfgUtil.fetchSysCfg("GDS_ENTRY_PIC_LIMIT_COUNT").getParaValue();
        model.addAttribute("upPictrueAmount", new Array[Integer.parseInt(picAmount)]);
        List<BaseParamDTO> mediaTypeList = BaseParamUtil.fetchParamList("GDS_MEDIA_TYPE");
        model.addAttribute("mediaTypeList", mediaTypeList);
        return URL+"/list/gds-skuPictrues";
    }
    
    /**
     * 
     * toShiptemp:(弹出运费模板的窗口). <br/> 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/toshiptemp")
    public String toShiptemp(Model model){
      
        return URL+"/list/gds-shiptemp";
    }
    /**
     * 
     * gridShiptempList:(获取模板运费列表). <br/> 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/gridshiptemplist")
    public String gridShiptempList (Model model,GdsShiptempVO vo){
        GdsShiptempReqDTO reqDTO = vo.toBaseInfo(GdsShiptempReqDTO.class);
        ObjectCopyUtil.copyObjValue(vo, reqDTO, "", false);
        if(StringUtil.isNotEmpty(vo.getShipTemplateId())){
            reqDTO.setId(vo.getShipTemplateId());
        }
        if(StringUtil.isNotEmpty(vo.getShopId())){
            reqDTO.setShopId(vo.getShopId());
        }
//        reqDTO.setIfFilterValue(GdsConstants.Commons.STATUS_VALID);
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = null;
        try {
            PageResponseDTO<GdsShiptempRespDTO> list = iGdsShiptemRSV.queryGdsShipTemp(reqDTO);
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(list);
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "获取运费模板列表失败！", e);
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取运费模板列表失败！", e);
        }
        super.addPageToModel(model, respVO);
        return URL+"/list/gds-shiptemp-list";
    }
    /**
     * 
     * toSeniorStock:(高级库存弹出框内的元素信息页). <br/> 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/toseniorstock")
    public String toSeniorStock(Model model,@RequestParam("shopId")String shopId,@RequestParam("editFlag")String editFlag){
        StockRepReqDTO dto = new StockRepReqDTO();
        if(StringUtil.isNotBlank(shopId)){
            dto.setShopId(Long.parseLong(shopId));
        }else{
            throw new BusinessException("web.gds.200008");
        }
        dto.setIfRegionalStock(true);
        List<StockRepRespDTO> stockList = iGdsStockRSV.queryShopRepInfoForGdsInput(dto);
        model.addAttribute("stockList", stockList);
        model.addAttribute("editFlag", editFlag);
        return URL+"/list/senior-stock";
    }
    /**
     * 
     * getNextNode:(获取下一个节点的信息列表). <br/> 
     * @param model
     * @param catgCode
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/getnextnode")
    @ResponseBody
    public Model getNextNode(Model model,@RequestParam("catgCode") String catgCode){
        GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        if(StringUtil.isNotBlank(catgCode)){
            dto.setCatgParent(catgCode);
        }
        List<GdsCategoryRespDTO> list = null;
        try {
            list = igdsCategoryRSV.querySubCategory(dto);
            if(StringUtil.isEmpty(list)){
                list = new ArrayList<GdsCategoryRespDTO>();
            }
            model.addAttribute("nextNodeList", list);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        } catch (BusinessException e) {
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            vo.setResultMsg("获取数据失败！");
        }
        model.addAttribute(vo);
        return model;
    }
    
    /**
     * 
     * saveGdsInfo:(新增保存商品信息). <br/> 
     * @param model
     * @param gdsInfoVO
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/savegdsinfo")
    public EcpBaseResponseVO saveGdsInfo(Model model,@Valid GdsInfoVO gdsInfoVO){
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        GdsInfoAddReqDTO dto = new GdsInfoAddReqDTO();
        initSaveGdsInfoParam(dto,gdsInfoVO);
        try {
            dto.getGdsInfoReqDTO().setCopyPropFromConfiged(true);
            iGdsInfoManageRSV.addGdsInfo(dto);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        } catch (BusinessException e) {
            
            LogUtil.error(MODULE, "商品保存失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
        }
        return vo;
    }
    private GdsInfoAddReqDTO initSaveGdsInfoParam(GdsInfoAddReqDTO dto,GdsInfoVO gdsInfoVO){
        ShopInfoResDTO shopInfo = iShopInfoRSV.findShopInfoByShopID(gdsInfoVO.getShopId());
        Long companyId = null;
        if(StringUtil.isNotEmpty(shopInfo)){
            companyId = shopInfo.getCompanyId();
        }else{
            throw new BusinessException("web.gds.2000012");
        }
        if(StringUtil.isEmpty(companyId)){
            throw new BusinessException("web.gds.2000013");
        }
        gdsInfoVO.setCompanyId(companyId);
        //商品详情、订购说明
        GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
        //获取选中的单品属性 ，根据商品参数组成的单品
        List<GdsSkuInfoReqDTO> gdsSkuInfos = getGdsSkuInfo(gdsInfoVO.getSkuParam(),gdsInfoVO);
        //获取规格属性
        List<GdsGds2PropReqDTO> autoParam = getAutoParam(gdsInfoVO.getAutoParam(),gdsInfoReqDTO);
       //获取选中的属性
        List<GdsGds2PropReqDTO> checkParam = getCheckParam(gdsInfoVO.getCheckedParam());
        //获取图片vfsId
        List<GdsGds2MediaReqDTO> gdsPics = getGdsPics(gdsInfoVO.getPictrueParam());
        
        //获取平台分类、店铺分类
        List<GdsGds2CatgReqDTO> gds2CatgReqDTOs = getCategoryParam(gdsInfoVO.getGetCategoryParam(),gdsInfoReqDTO);
        //获取阶梯价格
        getLadderPrice(dto,gdsInfoVO.getLadderPriceParam(),gdsInfoVO);
        //获取商品的基本信息
        getBaseInfoParam(gdsInfoReqDTO,gdsInfoVO);
        dto.setCompanyId(companyId);
        initHtmlInfo(gdsInfoVO.getGdsDesc(),gdsInfoVO.getGdsPartlist(),gdsInfoReqDTO);
        dto.setGds2CatgReqDTOs(gds2CatgReqDTOs);
        dto.setGdsInfoReqDTO(gdsInfoReqDTO);
        dto.setSkuInfoReqDTOs(gdsSkuInfos);
        dto.setGds2MediaReqDTOs(gdsPics);
        dto.setGds2PropReqDTOs(autoParam);
        dto.setSkuProps(checkParam);
        return dto;
    }
    
    /**
     * 
     * getLadderPrice:(这里用一句话描述这个方法的作用). <br/> 
     * @param dto
     * @param gdsInfoVO 
     * @since JDK 1.6MoneyUtilz
     */
    private void getLadderPrice(GdsInfoAddReqDTO dto,String ladderPriceParam,GdsInfoVO gdsInfoVO){
        JSONArray categoryParamList = JSONArray.fromObject(ladderPriceParam);
        List<GdsSku2PriceReqDTO> list = new ArrayList<GdsSku2PriceReqDTO>();
        int size = categoryParamList.size();
        if(GDS_LADDER_PRICE.equals(gdsInfoVO.getIfLadderPrice())&& size ==0){
            throw new BusinessException("web.gds.2000011");
        }
        GdsSku2PriceReqDTO gdsSku2PriceReqDTO = null;
        for(int i = 0;i < size;i++){
            JSONObject object = (JSONObject) categoryParamList.get(i);
            String amount = object.getString("amount");
            String ladderPrice = object.getString("ladderPrice");
            gdsSku2PriceReqDTO = new GdsSku2PriceReqDTO();
            GdsPriceLadderReqDTO priceDto = new GdsPriceLadderReqDTO();
            if(!StringUtil.isBlank(amount)){
                priceDto.setStartAmount(Long.parseLong(amount));
            }
            if(StringUtil.isNotBlank(ladderPrice)){
                priceDto.setPrice(MoneyUtil.convertYuanToCent(ladderPrice));
            }
            
            gdsSku2PriceReqDTO.setPrice(priceDto);
            list.add(gdsSku2PriceReqDTO);
        }
        dto.setSku2PriceReqDTOs(list);
    }
    /**
     * 
     * getCategoryParam:(获取平台分类、店铺分类). <br/> 
     * @param categoryParam
     * @return 
     * @since JDK 1.6
     */
    private List<GdsGds2CatgReqDTO> getCategoryParam(String categoryParam,GdsInfoReqDTO gdsInfoReqDTO){
        JSONArray categoryParamList = JSONArray.fromObject(categoryParam);
        List<GdsGds2CatgReqDTO> list = new ArrayList<GdsGds2CatgReqDTO>();
        int size = categoryParamList.size();
        GdsGds2CatgReqDTO dto = null;
        for(int i = 0;i < size;i++){
            JSONObject object = (JSONObject) categoryParamList.get(i);
            String catgCode = object.getString("catgCode");
            String gds2catgType = object.getString("gds2catgType");
            String catType = object.getString("catType");
            dto = new GdsGds2CatgReqDTO();
            dto.setCatgCode(catgCode);
            //主分类
            if(!StringUtil.isBlank(gds2catgType)){
                if(GdsConstants.GdsInfo.GDS_2_CATG_RTYPE_MAIN.equals(gds2catgType)){
                    gdsInfoReqDTO.setMainCatgs(catgCode);
                }
                dto.setGds2catgType(gds2catgType);
            }
            if(StringUtil.isNotBlank(catType)){
                dto.setCatgType(catType);
            }
            list.add(dto);
        }
        return list;
    }
    /**
     * 
     * getGdsSkuInfo:(获取单品信息). <br/> 
     * @param skuParam
     * @return 
     * @since JDK 1.6
     */
    private List<GdsSkuInfoReqDTO> getGdsSkuInfo(String skuParam,GdsInfoVO gdsInfoVO){
        //选中的单品参数列表
        JSONArray skuParamList = JSONArray.fromObject(skuParam);
        int size = skuParamList.size();
        if(size <= 0){
            throw new BusinessException("web.gds.2000014");
        }
        //单品列表
        List<GdsSkuInfoReqDTO> skuList = new ArrayList<GdsSkuInfoReqDTO>();
        //属性列表
        List<GdsSku2PropReqDTO>  propList = null;
        //单个属性
        GdsSku2PropReqDTO singleProp = null;
        //单个单品
        GdsSkuInfoReqDTO singleSku = null;
        //一个单品的价格
        List<GdsSku2PriceReqDTO> skuPriceList = null;
        GdsSku2PriceReqDTO singlePrice = null;
        //singlePrice的价格入参
        GdsPriceReqDTO dto = null;
        //一个单品的图片
        List<GdsSku2MediaReqDTO> skuPictrueList = null;
        GdsSku2MediaReqDTO singlePictrue = null;
        //一个单品的库存
        List<StockInfoReqDTO> skuStockList = null;
        StockInfoReqDTO singleStock = null;
        if(size <=0 ){
            
        }else {
            for (int j = 0; j < size; j++) {
                if (j == 0) {
                    // 初始化一个单品
                    singleSku = new GdsSkuInfoReqDTO();
                    //初始化一个单品价格
                    skuPriceList = new ArrayList<GdsSku2PriceReqDTO>();
                    //初始化一个单品图片
                    skuPictrueList = new ArrayList<GdsSku2MediaReqDTO>();
                    //初始化一个单品库存
                    skuStockList = new ArrayList<StockInfoReqDTO>();
                    // 初始化一个N个key-value的属性礼列表
                    propList = new ArrayList<GdsSku2PropReqDTO>();
                }
                JSONObject object = (JSONObject) skuParamList.get(j);
                String propId = object.getString("propId");
                String propName = object.getString("propName");
                String propValueId = object.getString("propValueId");
                String propValue = object.getString("propValue");
                String priceList = object.getString("skuPriceList");
                String stockList = object.getString("skuStockList");
                String pictrueList = object.getString("skuPictrueList");
                String skuAmount = object.getString("skuAmount");
                String skuId = object.getString("skuId");
                String ifHaveto = object.getString("ifHaveto");
                String ifBasic = object.getString("ifBasic");
                if(StringUtil.isNotBlank(propId)&& StringUtil.isNotBlank(propValueId)){
                    // 初始化一个1个key-value的属性
                    singleProp = new GdsSku2PropReqDTO();
                    if (!StringUtil.isBlank(propId)) {
                        singleProp.setPropId(Long.parseLong(propId));
                    }
                    singleProp.setPropName(propName);
                    if (!StringUtil.isBlank(propValueId)) {
                        singleProp.setPropValueId(Long.parseLong(propValueId));
                    }
                    singleProp.setPropValue(propValue);
                    singleProp.setPropType(GdsConstants.GdsProp.PROP_TYPE_1);
                    singleProp.setPropValueType(GdsConstants.GdsProp.PROP_VALUE_TYPE_3);
                    singleProp.setIfBasic(ifBasic);
                    singleProp.setIfMust(ifHaveto);
                    propList.add(singleProp);
                }
                if ((j + 1) % (size/Integer.parseInt(skuAmount)) == 0) {
                    //获取单品价格
                    parseSkuPrice(skuPriceList,singlePrice,dto,priceList,singleSku);
                   //获取单品库存
                    parseSkuStock(skuStockList,singleStock,stockList,singleSku);
                   //获取单品图片
                    parseSKuPictrue(skuPictrueList,singlePictrue,pictrueList);
                    singleSku.setSku2MediaReqDTOs(skuPictrueList);
                    singleSku.setSku2PriceReqDTOs(skuPriceList);
                    singleSku.setSku2PropReqDTOs(propList);
                    singleSku.setStocks(skuStockList);
                    if(StringUtil.isNotBlank(skuId)){
                        singleSku.setId(Long.parseLong(skuId));
                    }
                    singleSku.setCompanyId(gdsInfoVO.getCompanyId());
                    skuList.add(singleSku);
                    //重新初始化一个
                    // 初始化一个单品
                    singleSku = new GdsSkuInfoReqDTO();
                    //初始化存放N个的key-value
                    propList = new ArrayList<GdsSku2PropReqDTO>();
                    //初始化一个单品价格
                    skuPriceList = new ArrayList<GdsSku2PriceReqDTO>();
                    //初始化一个单品图片
                    skuPictrueList = new ArrayList<GdsSku2MediaReqDTO>();
                    //初始化一个单品库存
                    skuStockList = new ArrayList<StockInfoReqDTO>();
                }           
           }
        }
        return skuList;
    }
    /**
     * 
     * parseSkuPrice:(解析单品价格). <br/> 
     * @param skuPriceList
     * @param singlePrice
     * @param dto
     * @param priceList 
     * @since JDK 1.6
     */
    private void parseSkuPrice( List<GdsSku2PriceReqDTO> skuPriceList,
            GdsSku2PriceReqDTO singlePrice,GdsPriceReqDTO dto,String priceList,GdsSkuInfoReqDTO singleSku){
        JSONArray list = JSONArray.fromObject(priceList);
        int size = list.size();
        if(size >= 1){
            for(int i = 0;i < size ;i++){
              //默认价格也当做一条记录 放进list{skuPrice:'',priceType:'',priceTarget:'',defaultPrice:'',switch:'',}
                JSONObject object = (JSONObject) list.get(i);
                String switch_ = object.getString("switch");
                String defaultPrice = object.getString("defaultPrice");
                String priceTarget = object.getString("priceTarget");
                //priceType 就是priceCode
                String priceTypeCode = object.getString("priceTypeCode");
                String priceTypeId = object.getString("priceTypeId");
                String skuPrice = object.getString("skuPrice");
                // 添加手机专享价
                String appSpecPrice = object.getString("appSpecPrice");
                
                singlePrice = new GdsSku2PriceReqDTO();
                dto = new GdsPriceReqDTO();
                if(!StringUtil.isBlank(defaultPrice)){
                    dto.setPrice(MoneyUtil.convertYuanToCent(defaultPrice));
                    //普通价格，即默认价格
                    singlePrice.setPriceTypeCode(GdsConstants.GdsInfo.SKU_PRICE_TYPE_ORDINARY);
                    //将默认价格存到单品主表
                    singleSku.setCommonPrice(MoneyUtil.convertYuanToCent(defaultPrice));
                }else{
                    if(StringUtil.isNotBlank(skuPrice)){
                        dto.setPrice(MoneyUtil.convertYuanToCent(skuPrice));
                    }
                }
                if(!StringUtil.isBlank(priceTarget)){
                    dto.setPriceTarget(priceTarget);
                }
                if(!StringUtil.isBlank(priceTypeCode)){
                    singlePrice.setPriceTypeCode(priceTypeCode);
                }else{
                    singlePrice.setPriceTypeCode(GdsConstants.GdsInfo.SKU_PRICE_TYPE_ORDINARY);
                    //如果是基础价格则传给单品
                    singleSku.setCommonPrice(MoneyUtil.convertYuanToCent(skuPrice));

                }
                if(!StringUtil.isBlank(priceTypeId)){
                    singlePrice.setPriceTypeId(Long.parseLong(priceTypeId));
                }
                
                if(StringUtil.isNotBlank(appSpecPrice)){
                    singleSku.setAppSpecPrice(MoneyUtil.convertYuanToCent(appSpecPrice)); 
                }
                
                singlePrice.setPrice(dto);
                skuPriceList.add(singlePrice);
            }
        }
    }
    /**
     * 
     * parseSkuStock:(解析单品库存). <br/> 
     * @param skuStockList
     * @param singleStock
     * @param stockList 
     * @since JDK 1.6
     */
    private void parseSkuStock(List<StockInfoReqDTO> skuStockList,StockInfoReqDTO singleStock,String stockList,
            GdsSkuInfoReqDTO singleSku){
        JSONArray list = JSONArray.fromObject(stockList);
        int size = list.size();
        if(size >= 1){
            for(int i = 0;i < size ;i++){
              //默认价格也当做一条记录 放进list{skuStock:'',repCode:'',switch:''},{skuStock:'',repCode:'',switch:''}
                JSONObject object = (JSONObject) list.get(i);
                String switch_ = object.getString("switch");
                String skuStock = object.getString("skuStock");
                String repCode = object.getString("repCode");
                String repType = object.getString("repType");
                String areaList = object.getString("areaList");
                //库存的适用范围
                List<StockRepAdaptReqDTO>  stockRepAdaptDTOs = new ArrayList<StockRepAdaptReqDTO>();
                if(StringUtil.isNotBlank(areaList)){
                    JSONArray listArea = JSONArray.fromObject(areaList);
                    StockRepAdaptReqDTO stockRepAdaptReqDTO = null;
                    for(int j = 0 ;j < listArea.size();j++){
                        JSONObject objectArea = (JSONObject) listArea.get(j);
                        String country = objectArea.getString("country");
                        String provinceCode = objectArea.getString("provinceCode");
                        String cityCode = objectArea.getString("cityCode");
                        stockRepAdaptReqDTO = new StockRepAdaptReqDTO();
                        stockRepAdaptReqDTO.setAdaptCountry(country);
                        stockRepAdaptReqDTO.setAdaptProvince(provinceCode);
                        if(!cityCode.contains("adaptCity")){
                            stockRepAdaptReqDTO.setAdaptCity(cityCode);
                        }
                        stockRepAdaptDTOs.add(stockRepAdaptReqDTO);
                    }
                }
                //是否启用分仓
                if(GdsConstants.Commons.STATUS_VALID.equals(switch_)){
                    singleSku.setIfDisperseStock(switch_);
                }else{
                    //共仓模式下，库存放进单品级别的realcount
                    if(StringUtil.isNotBlank(skuStock)){
                        singleSku.setRealCount(Long.parseLong(skuStock));
                    }
                }
                singleStock = new StockInfoReqDTO();
                if(!StringUtil.isBlank(repCode)){
                    singleStock.setRepCode(Long.parseLong(repCode));
                }
                if(!StringUtil.isBlank(skuStock)){
//                    singleStock.setRealCount(Long.parseLong(skuStock));
                    singleStock.setTurnCount(Long.parseLong(skuStock));
                }
                if(!StringUtil.isBlank(repType)){
                    singleStock.setRepType(repType);
                }
                singleStock.setStockType(GdsConstants.GdsStock.STOCK_INFO_TYPE_PUBLIC);
                singleStock.setStockRepAdapts(stockRepAdaptDTOs);
                skuStockList.add(singleStock);
            }
        }
    }
    /**
     * 
     * parseSKuPictrue:(解析单品图片). <br/> 
     * @param skuPictrueList
     * @param singlePictrue
     * @param pictrueList 
     * @since JDK 1.6
     */
    private void parseSKuPictrue( List<GdsSku2MediaReqDTO> skuPictrueList,GdsSku2MediaReqDTO singlePictrue,String pictrueList){
        JSONArray list = JSONArray.fromObject(pictrueList);
        int size = list.size();
        if(size >= 1){
            for(int i = 1;i < size +1 ;i++){
              //默认价格也当做一条记录 放进list{picId:'',meidaRtype:'',mediaType:'',sortNo:''}
                JSONObject object = (JSONObject) list.get(i-1);
                String picVfsId = object.getString("picVfsId"+i);
                //1位媒体引用，2位手动上传
                String meidaRtype = object.getString("mediaRtype");
                // mediaType 1位图片，2位视频 3为音频
                String mediaType = object.getString("mediaType");
                String mediaId = object.getString("mediaId");
                String sortNo = object.getString("sortNo");
                singlePictrue = new GdsSku2MediaReqDTO();
                if(!StringUtil.isBlank(picVfsId)){
                    singlePictrue.setMediaUuid(picVfsId);
                }
                singlePictrue.setMediaType(mediaType);
                if(!StringUtil.isBlank(sortNo)){
                    singlePictrue.setSortNo(Integer.parseInt(sortNo));
                }
                if(StringUtil.isNotBlank(mediaId) && !"undefined".equals(mediaId.trim())){
                    singlePictrue.setMediaId(Long.parseLong(mediaId));
                }
                singlePictrue.setMediaRtype(meidaRtype);
                skuPictrueList.add(singlePictrue);
            }
        }
    }
    /**
     * 
     * getAutoParam:(获取商品规格属性，或者说是自动加载的属性). <br/> 
     * @param autoParam
     * @return 
     * @since JDK 1.6
     */
    private List<GdsGds2PropReqDTO> getAutoParam(String autoParam,GdsInfoReqDTO gdsInfoReqDTO){
        List<GdsGds2PropReqDTO> list = new ArrayList<GdsGds2PropReqDTO>();
        JSONArray autoParamList = JSONArray.fromObject(autoParam);
        int size = autoParamList.size();
        GdsGds2PropReqDTO dto = null;
        for(int i = 0;i < size; i++){
            //{propType:'2',propValueType:'1',propId:'4',valueId:'1',propName:'重量',gdsValue:'123'},
            //propType:1为规格属性 2为参数属性  3为普通属性
            //propValueType:1为手工输入 2单选多值  3位多选多值
            dto = new GdsGds2PropReqDTO();
            JSONObject object = (JSONObject) autoParamList.get(i);
            String propType = object.getString("propType");
            String propValueType = object.getString("propValueType");
            String propId = object.getString("propId");
            String valueId = object.getString("valueId");
            String propName = object.getString("propName");
            String gdsValue = object.getString("gdsValue");
            String ifBasic = object.getString("ifBasic");
            String editor = object.getString("editor");
            //editor 0 不是富文本  1 是富文本 4 是附件文件
            if(GdsConstants.Commons.STATUS_VALID.equals(editor)){
                //是富文本，那把值存到mongoDB上
                if(StringUtil.isNotBlank(gdsValue)){
                    gdsValue = getHtmlId(Escape.unescape(gdsValue));
                    dto.setPropValue(gdsValue);
                    dto.setPropInputType(GdsConstants.GdsProp.GDS_PROP_VALUE_INPUT_TYPE_RICHTXT);
                }
            }else if(GdsConstants.GdsProp.GDS_PROP_VALUE_INPUT_TYPE_FILE.equals(editor)){
                //是附件文件
                dto.setPropValue(gdsValue);
                dto.setPropInputType(GdsConstants.GdsProp.GDS_PROP_VALUE_INPUT_TYPE_FILE);
            }else{
                if(StringUtil.isNotBlank(gdsValue)){
                	if("1029".equals(propId)){
                        String a = Escape.unescape(gdsValue);
                        dto.setPropValue(MoneyUtil.convertYuanToCent(a)+"");
                    }else{
                        if("1002".equals(propId)){
                            gdsInfoReqDTO.setIsbn(Escape.unescape(gdsValue));
                        }
                        dto.setPropValue(Escape.unescape(gdsValue));
                    }
                }
            }
            dto.setPropType(propType);
            dto.setPropValueType(propValueType);
            if(!StringUtil.isBlank(propId)){
                dto.setPropId(Long.parseLong(propId));
            }
            if(!StringUtil.isBlank(ifBasic)){
                dto.setIfBasic(ifBasic);
            }
            if(!StringUtil.isBlank(valueId)){
                dto.setPropValueId(Long.parseLong(valueId));
            }
            dto.setPropName(propName);
           
            list.add(dto);
        }
        return list;
    }
    /**
     * 
     * getAutoParam:(获取商品已选中的属性). <br/> 
     * @param autoParam
     * @return 
     * @since JDK 1.6
     */
    private List<GdsGds2PropReqDTO> getCheckParam(String checkParam){
        List<GdsGds2PropReqDTO> list = new ArrayList<GdsGds2PropReqDTO>();
        JSONArray autoParamList = JSONArray.fromObject(checkParam);
        int size = autoParamList.size();
        GdsGds2PropReqDTO dto = null;
        for(int i = 0;i < size; i++){
            //{propType:'2',propValueType:'1',propId:'4',valueId:'1',propName:'重量',gdsValue:'123'},
            //propType:1为规格属性 2为参数属性  3为普通属性
            //propValueType:1为手工输入 2单选多值  3位多选多值
            dto = new GdsGds2PropReqDTO();
            JSONObject object = (JSONObject) autoParamList.get(i);
            String propType = object.getString("propType");
            String propValueType = object.getString("propValueType");
            String propId = object.getString("propId");
            String valueId = object.getString("valueId");
            String propName = object.getString("propName");
            String gdsValue = object.getString("gdsValue");
            String ifBasic = object.getString("ifBasic");
            dto.setPropType(propType);
            dto.setPropValueType(propValueType);
            if(!StringUtil.isBlank(propId)){
                dto.setPropId(Long.parseLong(propId));
            }
            if(!StringUtil.isBlank(valueId)){
                dto.setPropValueId(Long.parseLong(valueId));
            }
            dto.setPropName(propName);
            dto.setPropValue(gdsValue);
            dto.setIfBasic(ifBasic);
            list.add(dto);
        }
        return list;
    }
    
    /**
     * 
     * getGdsPics:(获取商品的图片入参). <br/> 
     * @param pitruePara
     * @return 
     * @since JDK 1.6
     */
    private  List<GdsGds2MediaReqDTO> getGdsPics(String pitrueParam){
        List<GdsGds2MediaReqDTO> gdsPics = new ArrayList<GdsGds2MediaReqDTO>();
        JSONArray pitrueParaList = JSONArray.fromObject(pitrueParam);
        int picsSize = pitrueParaList.size();
        GdsGds2MediaReqDTO dto = null;
        if(picsSize == 0){
            throw new BusinessException("web.gds.2000010");
        }
        int mainCount = 0;
        for(int i = 0;i < picsSize ; i ++){
            //{picId"+picCount+":'"+vfsId+"',picName:'"+picName+"',meidaRtype:'',mediaType:'',sortNo:''}
            JSONObject object = (JSONObject) pitrueParaList.get(i);
            dto = new GdsGds2MediaReqDTO();
            String picId = object.getString("picVfsId"+i);
            String picName = object.getString("picName");
            String meidaRtype = object.getString("mediaRtype");
            String mediaType = object.getString("mediaType");
            String mediaId = object.getString("mediaId");
            String sortNo = object.getString("sortNo");
            if(PIC_MAIN_FLAG.equals(sortNo)){
                mainCount ++;
            }
            if(!StringUtil.isBlank(picId)){
                dto.setMediaUuid(picId);
            }
            dto.setMediaType(mediaType);
            dto.setMediaRtype(meidaRtype);
            if(!StringUtil.isBlank(mediaId) && !"undefined".equals(mediaId.trim())){
                dto.setMediaId(Long.parseLong(mediaId));
            }
            if(!StringUtil.isBlank(sortNo)){
                dto.setSortNo(Integer.parseInt(sortNo));
            }
            if(!"99".equals(sortNo) && !"100".equals(sortNo)){
                gdsPics.add(dto);
            }
        }
        if(mainCount == 0 ){
            throw new BusinessException("web.gds.2000010");
        }
        return gdsPics;
    }
    
    /**
     * 
     * initHtmlInfo:(获取编辑器的内容). <br/> 
     * @param productDescHtml
     * @param orderDescribeHtml
     * @param gdsInfo 
     * @since JDK 1.6
     */
    private void initHtmlInfo(String productDescHtml,String orderDescribeHtml,GdsInfoReqDTO gdsInfo) {
        String productDescId = "";
        if(StringUtil.isNotBlank(productDescHtml)){
            productDescId = getHtmlId(productDescHtml);
        }
        String partListId = "";
        if(StringUtil.isNotBlank(orderDescribeHtml)){
            partListId = getHtmlId(orderDescribeHtml);
        }
        gdsInfo.setGdsDesc(productDescId);
        gdsInfo.setGdsPartlist(partListId);
    }
    /**
     * 
     * getHtmlId:(保存静态页面到mongoDB，并返回值). <br/> 
     * @param html
     * @return 
     * @since JDK 1.6
     */
    private String getHtmlId(String html) {
        try {
            return FileUtil.saveFile(html.getBytes("utf-8"), "goodsDetails", ".html");
        } catch (BusinessException e) {
            LogUtil.error("保存html代码串出错,原因---"+e.getMessage(), "",e);
        } catch (UnsupportedEncodingException e) {
            LogUtil.error("保存html代码串出错,原因---"+e.getMessage(), "",e);
        } 
        return null;
    };
    /**
     * 
     * getBaseInfoParam:(获取商品的其他简单的信息的入参). <br/> 
     * @param gdsInfoReqDTO
     * @param gdsInfoVO 
     * @since JDK 1.6
     */
    private void getBaseInfoParam(GdsInfoReqDTO gdsInfoReqDTO,GdsInfoVO gdsInfoVO){
        ObjectCopyUtil.copyObjValue(gdsInfoVO, gdsInfoReqDTO, "", false);
        gdsInfoReqDTO.setGdsName(gdsInfoVO.getGdsName());
        if(StringUtil.isNotEmpty(gdsInfoVO.getShopId())){
            gdsInfoReqDTO.setShopId(gdsInfoVO.getShopId());
        }else{
            throw new BusinessException("web.gds.200008");
        }
        gdsInfoReqDTO.setGdsSubHead(gdsInfoVO.getGdsSubHead());
        gdsInfoReqDTO.setGuidePrice(MoneyUtil.convertYuanToCent(gdsInfoVO.getGuidePrice()));
        gdsInfoReqDTO.setIfFree(gdsInfoVO.getIfFree());
        gdsInfoReqDTO.setIfDisperseStock(gdsInfoVO.getIfDisperseStock());
        gdsInfoReqDTO.setIfEntityCode(gdsInfoVO.getIfEntityCode());
        gdsInfoReqDTO.setIfFree(gdsInfoVO.getIfFree());
        gdsInfoReqDTO.setIfLadderPrice(gdsInfoVO.getIfLadderPrice());
        gdsInfoReqDTO.setIfRecomm(gdsInfoVO.getIfRecomm());
        gdsInfoReqDTO.setIfSalealone(gdsInfoVO.getIfSalealone());
        gdsInfoReqDTO.setIfSendscore(gdsInfoVO.getIfSendscore());
        gdsInfoReqDTO.setIfStocknotice(gdsInfoVO.getIfStocknotice());
        gdsInfoReqDTO.setIfSeniorPrice(gdsInfoVO.getIfSeniorPrice());
        gdsInfoReqDTO.setGdsTypeId(gdsInfoVO.getGdsTypeId());
        gdsInfoReqDTO.setIfScoreGds(NOT_SCORE_GDS);
        // 核心版本添加商品编号取值。
        //gdsInfoReqDTO.setIsbn(gdsInfoVO.getIsbn());
        if(StringUtil.isNotEmpty(gdsInfoVO.getId())){
            gdsInfoReqDTO.setId(gdsInfoVO.getId());
        }
    }
    
    /*******************************商品编辑 start***************************************/
    /**
     * 
     * toGdsEdit:(商品编辑初始化页面). <br/> 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/togdsedit")
    public String toGdsEdit(Model model,GdsInfoEditVO gdsInfoEditVO){
        if(StringUtil.isEmpty(gdsInfoEditVO.getGdsId())){
            return "redirect:/seller/goods/gdsmanage";
        }
        int ifReturn=getGdsInfo(model,gdsInfoEditVO);
        if(ifReturn != 1){
        	return "redirect:/seller/goods/gdsmanage";
        }
        model.addAttribute("gdsDetailFlag", "0");
        model.addAttribute("GDS_SHOP_CATEGORY", SysCfgUtil.fetchSysCfg("GDS_SHOP_CATEGORY").getParaValue());
        return EDIT_URL+"/gdsInfoEdit";
    }
    /**
     * 
     * toGdsDetail:(商品录入的详情基本信息初始化). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * @param model
     * @param gdsInfoEditVO
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/togdsdetail")
    public String toGdsDetail(Model model,GdsInfoEditVO gdsInfoEditVO){
        if(StringUtil.isEmpty(gdsInfoEditVO.getGdsId())){
            return "redirect:/seller/goods/gdsmanage";
        }
        int ifReturn=getGdsInfo(model,gdsInfoEditVO);
        if(ifReturn == -1){
        	return "redirect:/seller/goods/gdsmanage";
        }
        model.addAttribute("gdsDetailFlag", "1");
        model.addAttribute("GDS_SHOP_CATEGORY", SysCfgUtil.fetchSysCfg("GDS_SHOP_CATEGORY").getParaValue());
        model.addAttribute("secondVerifySwitch",gdsInfoEditVO.getSecondVerifySwitch());
        return EDIT_URL+"/gdsInfoEdit";
    }
    private int getGdsInfo(Model model,GdsInfoEditVO gdsInfoEditVO){
        GdsInfoReqDTO dto = new GdsInfoReqDTO();
        dto.setId(gdsInfoEditVO.getGdsId());
        GdsQueryOption[] gdsQueryOptions = new GdsQueryOption[1];
        SkuQueryOption[] skuQuerys = new SkuQueryOption[1];
        gdsQueryOptions[0] = GdsQueryOption.ALL;
        skuQuerys[0] = SkuQueryOption.ALL;
        dto.setGdsQueryOptions(gdsQueryOptions);
        dto.setSkuQuerys(skuQuerys);
        try {
            GdsInfoRespDTO resultDto = iGdsInfoQueryRSV.queryGdsInfoByOption(dto);
            
			if (resultDto != null && resultDto.getShopId() != null) {
				shopIdCheck(resultDto.getShopId());
			}
            List<BaseParamDTO> mediaTypeList = BaseParamUtil.fetchParamList("GDS_MEDIA_TYPE");
            model.addAttribute("mediaTypeList", mediaTypeList);
//            List<BaseParamDTO> gdsTypeCodeList = BaseParamUtil.fetchParamList("GDS_INFO_TYPE");
            model.addAttribute("gdsInfo", resultDto);
          
            //商品类型列表获取
            List<GdsTypeRespDTO> typeList=gdsTypeRSV.queryAllGdsTypesFromCache();
            model.addAttribute("gdsTypeList", typeList);
            model.addAttribute("PROP_VALUE_TYPE_1", GdsConstants.GdsProp.PROP_VALUE_TYPE_1);
            model.addAttribute("PROP_VALUE_TYPE_2", GdsConstants.GdsProp.PROP_VALUE_TYPE_2);
            model.addAttribute("PROP_VALUE_TYPE_3", GdsConstants.GdsProp.PROP_VALUE_TYPE_3);
            model.addAttribute("PROP_VALUE_TYPE_4", GdsConstants.GdsProp.PROP_VALUE_TYPE_4);
            String priceButton = "0";
            String stockButton = "0";
            String multi = MULTI_SWITCH_OFF;
            String skuListStr = "";
            int mediaSize = 0;
            if(resultDto!=null){
                
                if(CollectionUtils.isNotEmpty(resultDto.getMedias())){
                    mediaSize = resultDto.getMedias().size();
                }
                
                
                String spuDescUrl = "";
                if(StringUtil.isNotEmpty(resultDto.getGdsDesc())){
                    spuDescUrl = getHtmlUrl(resultDto.getGdsDesc()+".html");
                }
                String orderUrl = "";
                if(StringUtil.isNotEmpty(resultDto.getGdsPartlist())){
                    orderUrl = getHtmlUrl(resultDto.getGdsPartlist()+".html");
                }
                resultDto.setGdsDesc(spuDescUrl);
                resultDto.setGdsPartlist(orderUrl);
                List<GdsPropRespDTO> list = resultDto.getProps();
                if(CollectionUtils.isNotEmpty(list)){
                    for(GdsPropRespDTO gdsPropRespDTO : list){
                        if(GdsConstants.GdsProp.PROP_VALUE_TYPE_3.equals(gdsPropRespDTO.getPropInputType())){
                            List<GdsPropValueRespDTO> valueList = gdsPropRespDTO.getValues();
                            for(GdsPropValueRespDTO value : valueList){
                                if(StringUtil.isNotBlank(value.getPropValue())){
                                    value.setPropValue(getHtmlUrl( value.getPropValue()+".html")); 
                                }
                            }
                        }
                    }
                }
                List<GdsSkuInfoRespDTO> skus=resultDto.getSkus();
                if(CollectionUtils.isNotEmpty(skus)){
                    if(CollectionUtils.isNotEmpty(skus.get(0).getProps())){
                        if(skus.get(0).getProps().size()>=1){
                            multi = MULTI_SWITCH_ON;
                        }
                    }
                    for (GdsSkuInfoRespDTO gdsSkuInfoRespDTO : skus) {
                        List<GdsSku2MediaRespDTO> skuMedias=gdsSkuInfoRespDTO.getSku2MediaRespDTOs();
                        if(CollectionUtils.isNotEmpty(skuMedias)){
                            for (GdsSku2MediaRespDTO gdsSku2MediaRespDTO : skuMedias) {
                                gdsSku2MediaRespDTO.setURL(new AiToolUtil().genImageUrl(gdsSku2MediaRespDTO.getMediaUuid(),"124x124!"));
                            }
                        }
                    }
                }
                
                GdsType2PropReqDTO reqDTO=new GdsType2PropReqDTO();
                reqDTO.setTypeId(resultDto.getGdsTypeId()); 
                reqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
                GdsType2PropRelationRespDTO gdsType2PropRelationRespDTO=gdsType2PropRSV.queryTypeProps(reqDTO);
                
                //获取分类下的属性列表
                GdsCatg2PropReqDTO reqDto = new GdsCatg2PropReqDTO();
                reqDto.setCatgCode(resultDto.getPlatformCategory().get(0).getCatgCode());
                reqDto.setIfContainTopProp(true);
                GdsCatg2PropRelationRespDTO rspDto = igdsCategoryRSV.queryCategoryProps(reqDto);
                
                List<GdsPropRespDTO> list_=propMerge(rspDto.getBasics(), gdsType2PropRelationRespDTO.getBasics());
                rspDto.setBasics(list_);
                
                list_=propMerge(rspDto.getParams(), gdsType2PropRelationRespDTO.getParams());
                rspDto.setParams(list_);
                
                list_=propMerge(rspDto.getOthers(), gdsType2PropRelationRespDTO.getOthers());
                rspDto.setOthers(list_);
                
                list_=propMerge(rspDto.getSpecs(), gdsType2PropRelationRespDTO.getSpecs());
                rspDto.setSpecs(list_);
                
                list_=propMerge(rspDto.getFileParam(), gdsType2PropRelationRespDTO.getFileParam());
                rspDto.setFileParam(list_);
                
                list_=propMerge(rspDto.getEditoParam(), gdsType2PropRelationRespDTO.getEditoParam());
                rspDto.setEditoParam(list_);
                
                model.addAttribute("rspDto", rspDto);
                if(STOCK_SWITCH_ON.equals(resultDto.getIfDisperseStock())){
                    stockButton = STOCK_SWITCH_ON;
                }
                if(PRICE_SWITCH_ON.equals(resultDto.getIfSeniorPrice())){
                    priceButton = PRICE_SWITCH_ON;
                }
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.setAllowNonStringKeys(true);
                skuListStr = JSONArray.fromObject(resultDto.getSkus(),jsonConfig).toString().replaceAll("\"", "\'");
                if(CollectionUtils.isNotEmpty(resultDto.getMedias())){
                    for(GdsGds2MediaRespDTO gdsGds2MediaRespDTO : resultDto.getMedias()){
                        gdsGds2MediaRespDTO.setURL(new AiToolUtil().genImageUrl(gdsGds2MediaRespDTO.getMediaUuid(),"124x124!"));
                    }
                }
            }
          //商品归属分类最大数量
            model.addAttribute("GDS_CATEGORY_MAX_COUNT", SysCfgUtil.fetchSysCfg("GDS_CATEGORY_MAX_COUNT").getParaValue());
            //多功能默认开启或关闭
            model.addAttribute("multi-switch", multi);
            //高级库存默认开启或关闭SysCfgUtil.fetchSysCfg("GDS_ENTRY_STOCK_SENIOR")
            model.addAttribute("stock-switch", stockButton);
            //高级价格默认开启或关闭SysCfgUtil.fetchSysCfg("GDS_ENTRY_PRICE_SENIOR")
            model.addAttribute("price-switch", priceButton);
            //默认最多可上传多少张图片BaseParamUtil.fetchParamValue("GDS_ENTRY_PIC_LIMIT_COUNT", "1")
            String picAmount = SysCfgUtil.fetchSysCfg("GDS_ENTRY_PIC_LIMIT_COUNT").getParaValue();
            //阶梯价开关
            model.addAttribute("ladder-switch",  SysCfgUtil.fetchSysCfg("GDS_ENTRY_LADDER_PRICE").getParaValue());
            
            int picAmount1 = 0;
            if(mediaSize==0){
                picAmount1 = Integer.parseInt(picAmount)-1;
            }else{
                picAmount1 = Integer.parseInt(picAmount)-mediaSize;
            }
            if(picAmount1 <0){
                picAmount1 = 0;
            }
            model.addAttribute("upPictrueAmount", new Array[picAmount1]);
            model.addAttribute("skuList", skuListStr);
            model.addAttribute("gdsId", resultDto.getId());
            model.addAttribute("copyFlag", gdsInfoEditVO.getFormCopyFlag());
            model.addAttribute("gdsVerifyFlag", gdsInfoEditVO.getGdsVerifyFlag());
            if(GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES.equals(resultDto.getGdsStatus())){
            	if(!GdsUtils.isEqualsValid(gdsInfoEditVO.getFormCopyFlag())){
            		return -2;
            	}
            }
        } catch (BusinessException e) {
        	if(e.getErrorCode().equals("web.gds.2000020")){
        		return -1;
        	}
            LogUtil.error(MODULE, "获取商品信息失败！", e);
        }
        return 1;
    }

	private void shopIdCheck(Long shopId) {
		SellerResDTO srd = SellerLocaleUtil.getSeller();
		List<ShopInfoResDTO> shopLst = srd.getShoplist();
		if (CollectionUtils.isEmpty(shopLst)) {
			throw new BusinessException("web.gds.2000020");
		} else {
			boolean isContain = false;
			for (ShopInfoResDTO shopInfoResDTO : shopLst) {
				if (shopId.longValue() == shopInfoResDTO.getId().longValue()) {
					isContain = true;
				}
			}
			if (!isContain) {
				throw new BusinessException("web.gds.2000020");
			}
		}
	}
    /**
     * 
     * editGdsInfo:(商品编辑保存). <br/> 
     * @param model
     * @param gdsInfoVO
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/editgdsinfo")
    public EcpBaseResponseVO editGdsInfo(Model model,@Valid GdsInfoVO gdsInfoVO){
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        GdsInfoAddReqDTO dto = new GdsInfoAddReqDTO();
        initSaveGdsInfoParam(dto,gdsInfoVO);
        try {
            dto.getGdsInfoReqDTO().setCopyPropFromConfiged(true);
            if(COPY_FLAG.equals(gdsInfoVO.getCopyFlag())){
                iGdsInfoManageRSV.addGdsInfo(dto);
            }else{
                dto.setIfLocalEdit("1");
                iGdsInfoManageRSV.editGdsInfoAndReference(dto);
            }
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        } catch (BusinessException e) {
            
            LogUtil.error(MODULE, "商品编辑保存失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
        }
        return vo;
    }
    
    @RequestMapping(value="/toeditor")
    public String toEditor(Model model){
        
        return URL+"/list/input-editor";
    }
    
    
    private List<Long> fetchCatlogIdsByCurrentUser(List<Long> selectedShopIds){
            
            List<Long> catlogIds = new ArrayList<Long>();
            SellerResDTO srd =  SellerLocaleUtil.getSeller();
            List<ShopInfoResDTO> shopLst = srd.getShoplist();
            if(CollectionUtils.isNotEmpty(shopLst)){
                for (Iterator<ShopInfoResDTO> iterator = shopLst.iterator(); iterator.hasNext();) {
                    ShopInfoResDTO shopInfoResDTO = iterator.next();
                    if(CollectionUtils.isNotEmpty(selectedShopIds)){
                        if(!selectedShopIds.contains(shopInfoResDTO.getId())){
                            continue;
                        }
                    }
                    Long shopId = shopInfoResDTO.getId();
                    GdsCatlog2ShopReqDTO relationQuery = new GdsCatlog2ShopReqDTO();
                    relationQuery.setShopId(shopId);
                    LongListRespDTO longListResp = gdsCatlog2ShopRSV.queryGdsCatlog2ShopRespDTOByShopId(relationQuery);
                    if(null != longListResp && CollectionUtils.isNotEmpty(longListResp.getLst())){
                        for(Long l : longListResp.getLst()){
                            if(!catlogIds.contains(l)){
                                catlogIds.add(l);
                            }
                        }
                    }
                }
            }
            return catlogIds;
        }
    /**
     * 
     * getHtmlUrl:(根据id 获取静态文件). <br/> 
     * @param vfsId
     * @return 
     * @since JDK 1.6
     */
    private String getHtmlUrl(String vfsId){
        if(StringUtil.isBlank(vfsId)){
            return "";
        }
        return ImageUtil.getStaticDocUrl(vfsId, "html");
    }
    /*******************************商品编辑 end*****************************************/
    
    /**
     * 
     * double2long:(double 转long). <br/> 
     * @param in
     * @return 
     * @since JDK 1.6
     */
    private long double2long(double in){  
        return (long) (in > 0 ? (in + 0.5) : (in - 0.5));  
    } 
    
}

