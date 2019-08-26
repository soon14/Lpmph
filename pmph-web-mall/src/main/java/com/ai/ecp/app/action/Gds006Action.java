package com.ai.ecp.app.action;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Gds006Req;
import com.ai.ecp.app.req.gds.SearchPropReqInfo;
import com.ai.ecp.app.resp.Gds006Resp;
import com.ai.ecp.app.resp.gds.GoodSearchResultVO;
import com.ai.ecp.base.util.ApplicationContextUtil;
import com.ai.ecp.busi.search.vo.ExtraRespVO;
import com.ai.ecp.busi.search.vo.GoodSearchResult;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatg2PropRelationRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatg2PropReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCollectReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCollectRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCatalog2SiteRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCollectRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.prom.dubbo.dto.PromListRespDTO;
import com.ai.ecp.prom.dubbo.dto.PromRuleCheckDTO;
import com.ai.ecp.prom.dubbo.interfaces.IPromRSV;
import com.ai.ecp.search.dubbo.search.ExtraFieldQueryField;
import com.ai.ecp.search.dubbo.search.MulValueExtraFieldQueryField;
import com.ai.ecp.search.dubbo.search.RangeQueryField;
import com.ai.ecp.search.dubbo.search.SearchFacade;
import com.ai.ecp.search.dubbo.search.SearchParam;
import com.ai.ecp.search.dubbo.search.SortField;
import com.ai.ecp.search.dubbo.search.ext.filter.ExtraQueryInfo;
import com.ai.ecp.search.dubbo.search.ext.filter.ExtraQueryInfo.QueryCategory;
import com.ai.ecp.search.dubbo.search.ext.filter.ExtraQueryInfo.QueryProperty;
import com.ai.ecp.search.dubbo.search.ext.filter.QueryFilter;
import com.ai.ecp.search.dubbo.search.ext.filter.good.GoodCategoriesQueryFilter;
import com.ai.ecp.search.dubbo.search.ext.filter.good.GoodPropertyQueryFilter;
import com.ai.ecp.search.dubbo.search.result.FieldFacetResult.Count;
import com.ai.ecp.search.dubbo.search.result.SearchResult;
import com.ai.ecp.search.dubbo.search.translator.GdsTranslator;
import com.ai.ecp.search.dubbo.search.util.ESort;
import com.ai.ecp.search.dubbo.search.util.SearchConstants;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SiteLocaleUtil;
import com.ai.ecp.server.front.util.StaffLocaleUtil;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustInfoRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.SystemException;

/**
 * 商品搜索 Title: ECP <br>
 * Description: <br>
 * Date:2016年3月10日上午11:47:09 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@Service("gds006")
@Action(bizcode = "gds006", userCheck = false)
@Scope("prototype")
public class Gds006Action extends AppBaseAction<Gds006Req, Gds006Resp> {

    @Resource
    private IGdsCollectRSV gdsCollectRSV;

    @Resource
    private IGdsCategoryRSV gdsCategoryRSV;

    @Resource
    private IPromRSV promRSV;
    
    @Resource
    private IGdsSkuInfoQueryRSV iGdsSkuInfoQueryRSV;
    
    @Resource
	private ICustInfoRSV iCustInfoRSV;

    private static final String MODULE = Gds006Action.class.getName();

    public final static String FIELD_DISCOUNTPRICE = "discountPriceOfLevel";

    private static final String FIELD_CATEGORYPATH = "categoryPath";

    //private final static String ROPERTYCODE_DEFAULTPRICE = "-1";
    public final static String ROPERTYCODE_DEFAULTPRICE = "-1";
    
    public final static String ROPERTYCODE_PMPHSERVICE = "pmphService";
    
    public final static String ROPERTYCODE_CATEGORIES = "categories";
    
    public final static String ROPERTYCODE_PMPHAUTHOR = "authorNameService";
    
    //有网络增值服务的propertycode值
    public final static String VALUE_ADDED_PROPERTY_CODE = "1027_308";
    /**
     * 产品类型限定。
     */
    private static final String GDS_TYPE_LIMIT = "GDS_TYPE_LIMIT";
    

    private static String GDS_E_BOOK_CAT_CODE = "1200";
    private static String GDS_DIGITS_BOOK_CAT_CODE = "1201";
    private static String WEB = "1";
    private static String GDS_PAPER_BOOK_CAT_CODE = "1115";
    private static Long PROP_ID_1032 = 1032l;
    private static int PAGE_SIZE_10 = 10;
    @SuppressWarnings("rawtypes")
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
        Gds006Req gds006Req = this.request;
        Gds006Resp gds006Resp = this.response;

        SearchParam searchParam = new SearchParam();
        searchParam.setCurrentSiteId(SiteLocaleUtil.getSite());

        // GET 中文解码
        if (StringUtils.isBlank(gds006Req.getKeyword())) {
            searchParam.setKeyword(SearchConstants.STAR);
        } else {
            searchParam.setKeyword(gds006Req.getKeyword());
        }

        ExtraRespVO extraRespVO = new ExtraRespVO();

        // 分类搜索
        ExtraQueryInfo extraQueryInfo = new ExtraQueryInfo();
        List<QueryFilter> filterList = new ArrayList<QueryFilter>();
        if (StringUtils.isNotBlank(gds006Req.getCategory())) {
            // 不管分类是否在管理平台变更了，都进行搜索
            extraRespVO.setSearchCategory(gds006Req.getCategory());
            List<QueryCategory> queryCategoryList = new ArrayList<QueryCategory>();
            queryCategoryList.add(new QueryCategory(Arrays.asList(gds006Req.getCategory().split(SearchConstants.COMMA)), ExtraQueryInfo.FIELD_CATEGORIES));
            extraQueryInfo.setQueryCategoryList(queryCategoryList);
            filterList.add(new GoodCategoriesQueryFilter());
        }

        // 属性过滤
        // 可能为空
        extraQueryInfo = addPropertyFilterSupport(searchParam, extraQueryInfo, gds006Req);
        if (CollectionUtils.isNotEmpty(extraQueryInfo.getQueryPropertyList())) {
            filterList.add(new GoodPropertyQueryFilter());
        }

        searchParam.setPageNo(gds006Req.getPageNumber());
        searchParam.setPageSize(gds006Req.getPageSize());

        searchParam = this.addFieldFilterSupport(searchParam, gds006Req);
        
        
        

        //店铺商品查询
        if(StringUtils.isNoneBlank(gds006Req.getShopId())){
            List<ExtraFieldQueryField> extraANDFieldQueryList=searchParam.getExtraANDFieldQueryList();
            if(extraANDFieldQueryList==null){
                extraANDFieldQueryList=new ArrayList<ExtraFieldQueryField>();
            }
            ExtraFieldQueryField extraFieldQueryField=new ExtraFieldQueryField();
            extraFieldQueryField.setName("shopId");
            extraFieldQueryField.setValue(gds006Req.getShopId());
            extraANDFieldQueryList.add(extraFieldQueryField);
            searchParam.setExtraANDFieldQueryList(extraANDFieldQueryList);
        }
        
        //新增仅看自营标识
        if(StringUtils.isNoneBlank(gds006Req.getIfSelfOperate())){
        	List<ExtraFieldQueryField> extraANDFieldQueryList=searchParam.getExtraANDFieldQueryList();
            if(extraANDFieldQueryList==null){
                extraANDFieldQueryList=new ArrayList<ExtraFieldQueryField>();
            }
            ExtraFieldQueryField extraFieldQueryField=new ExtraFieldQueryField();
            extraFieldQueryField.setName("ifSelfOperate");
            extraFieldQueryField.setValue(gds006Req.getIfSelfOperate());
            extraANDFieldQueryList.add(extraFieldQueryField);
            searchParam.setExtraANDFieldQueryList(extraANDFieldQueryList);
        }
        
        List<String> gdsTypeLimit = getGdsTypeLimit();
        // 增加APP端虚拟产品检索过滤.
        if(CollectionUtils.isNotEmpty(gdsTypeLimit)){
            List<MulValueExtraFieldQueryField> mulValueExtraFieldQueryFieldLst = searchParam.getExtraANDMulValueFieldQueryList();
            if(mulValueExtraFieldQueryFieldLst == null){
                mulValueExtraFieldQueryFieldLst = new ArrayList<MulValueExtraFieldQueryField>();
            }
            MulValueExtraFieldQueryField mulValueExtraFieldQueryField = new MulValueExtraFieldQueryField();
            mulValueExtraFieldQueryField.setName("gdsTypeId");
            
            mulValueExtraFieldQueryField.setValue(gdsTypeLimit);
            mulValueExtraFieldQueryFieldLst.add(mulValueExtraFieldQueryField);
            searchParam.setExtraANDMulValueFieldQueryList(mulValueExtraFieldQueryFieldLst);
        }
        
        
        
        // Facet参数设置
        List<String> queryFacetFieldList = new ArrayList<String>();
        queryFacetFieldList.add(FIELD_CATEGORYPATH);
        searchParam.setFieldFacetFieldList(queryFacetFieldList);

        // 查询综合评分和排序下的查询列表
        SearchResult<GoodSearchResult> result = SearchFacade.search(GoodSearchResult.class, searchParam, extraQueryInfo, filterList,
                new GdsTranslator(ApplicationContextUtil.getBean("gdsCatalog2SiteRSV", IGdsCatalog2SiteRSV.class)));
        
        if (result.isSuccess()) {

            PageResponseDTO<GoodSearchResult> pageResponseDTO = this.renderRespVO(gds006Req, result);
            List<GoodSearchResultVO> goodSearchResultVOs = new ArrayList<GoodSearchResultVO>();
            for (GoodSearchResult goodSearchResult : pageResponseDTO.getResult()) {
                GoodSearchResultVO goodSearchResultVO = new GoodSearchResultVO();
                ObjectCopyUtil.copyObjValue(goodSearchResult, goodSearchResultVO, null, false);
                // 添加收藏记录ID.
                goodSearchResultVO.setCollectId(queryGdsCollect(goodSearchResult.getId()));
                //其他书本类型
                goodSearchResultVO.setBookOtherType(queryBookOtherType(goodSearchResult));
                //作者
                goodSearchResultVO.setAuthorStr(authorListToStr(goodSearchResultVO.getAuthor()));
                //0显示电子书,1显示数字教材
                String edbook="";
                String platCags = goodSearchResult.getMainCategoryCode();
                
                GdsCategoryReqDTO catgQuery = new GdsCategoryReqDTO();
                catgQuery.setCatgCode(platCags);
                GdsCategoryRespDTO catgResp = gdsCategoryRSV.queryGdsCategoryByPK(catgQuery);
                
                if(null != catgResp){
                	if(StringUtils.isNotBlank(catgResp.getCatgPath())){
                		if(catgResp.getCatgPath().indexOf("<1200>") != -1){
                			edbook = "0";
                		}else if(catgResp.getCatgPath().indexOf("<1201>") != -1){
                			edbook = "1";
                		}
                	}
                	
                }
                
                goodSearchResultVO.setEdbook(edbook);
                //是否有网络增值服务
                goodSearchResultVO.setHasValueAdded(this.hasValueAdded(goodSearchResult.getPropertycodes()));
                goodSearchResultVOs.add(goodSearchResultVO);
            }

            gds006Resp.setPageRespVO(goodSearchResultVOs);
            gds006Resp.setCount(pageResponseDTO.getCount());
            gds006Resp.setPageCount(pageResponseDTO.getPageCount());
            extraRespVO.setSearchType("1");
            if (StringUtils.isBlank(gds006Req.getKeyword())) {
                extraRespVO.setKeyword("");
            } else {
                extraRespVO.setKeyword(gds006Req.getKeyword());
            }

            // 是否指定了查询分类
            GdsCategoryRespDTO gdsCategoryRespDTO = null;
            if (StringUtils.isNotBlank(gds006Req.getCategory())) {

                GdsCategoryReqDTO gdsCategoryReqDTO = new GdsCategoryReqDTO();
                gdsCategoryReqDTO.setCatgCode(gds006Req.getCategory());
                gdsCategoryRespDTO = this.gdsCategoryRSV.queryGdsCategoryByPK(gdsCategoryReqDTO);

                // 分类已被删除
                if (gdsCategoryRespDTO == null) {
                    // TODO 暂时为分类不存在搜索全部
                    // throw new BusinessException("分类不存在或已被删除：" + gds006Req.getCategory());
                } else {
                    extraRespVO.setSearchType("2");
                }

            }

            if (result.getResultList().size() > 0) {

                // 指定了查询分类
                if (gdsCategoryRespDTO != null) {
                    extraRespVO = this.addCategoryQueryResult(extraRespVO, gds006Req.getCategory(), gdsCategoryRespDTO.getCatgName(), gdsCategoryRespDTO.getCatgParent(), result.getNumFound() + "");
                } else {

                    extraRespVO.setParentCateCode("");
                    extraRespVO.setParentCateName("所有分类");

                    // 查询Main分类Facet列表
                    List<Map<String, String>> facetCateInfoList = new ArrayList<Map<String, String>>();
                    if (result.getFieldFacetResultMap().containsKey(FIELD_CATEGORYPATH)) {
                        if (CollectionUtils.isNotEmpty(result.getFieldFacetResultMap().get(FIELD_CATEGORYPATH).getValue())) {

                            List<Count> counts = result.getFieldFacetResultMap().get(FIELD_CATEGORYPATH).getValue();
                            Map<String, String> fieldInfoMap;
                            String temp[];
                            for (Count count : counts) {
                                fieldInfoMap = new HashMap<String, String>();
                                temp = count.getValue().split(SearchConstants.SEPERATOR);
                                fieldInfoMap.put("topCateCode", temp[0]);
                                fieldInfoMap.put("topCateValue", temp[1]);
                                fieldInfoMap.put("code", temp[2]);
                                fieldInfoMap.put("value", temp[3]);
                                fieldInfoMap.put("count", count.getCount() + "");
                                facetCateInfoList.add(fieldInfoMap);
                            }

                        }
                    }

                    // key为顶级分类名称
                    Map<String, List<Map<String, String>>> cateInfoMap = new HashMap<String, List<Map<String, String>>>();
                    List<String> topCateList = new ArrayList<String>();

                    // 根据Facet分类查询其顶级分类，并进行归类
                    for (Map<String, String> map : facetCateInfoList) {

                        String topCateValue = map.get("topCateValue");

                        if (cateInfoMap.containsKey(topCateValue)) {
                            cateInfoMap.get(topCateValue).add(map);
                        } else {
                            List<Map<String, String>> cateList = new ArrayList<Map<String, String>>();
                            cateList.add(map);
                            cateInfoMap.put(topCateValue, cateList);

                            topCateList.add(topCateValue);
                        }

                    }

                    extraRespVO.setCateInfoMap(cateInfoMap);
                    extraRespVO.setTopCateNameList(topCateList);

                    // 查询第一个Top分类的属性和属性值列表
                    if (facetCateInfoList.size() > 0) {

                        String firstTopCateCode = facetCateInfoList.get(0).get("code");

                        if (StringUtils.isNotBlank(firstTopCateCode)) {
                            GdsCatg2PropReqDTO reqDTO = new GdsCatg2PropReqDTO();
                            reqDTO.setCatgCode(firstTopCateCode);
                            GdsCatg2PropRelationRespDTO gdsCatg2PropRelationRespDTO = gdsCategoryRSV.querySearchProps(reqDTO);
                            if (gdsCatg2PropRelationRespDTO != null) {
                                List<GdsPropRespDTO> gdsPropRespDTOList = gdsCatg2PropRelationRespDTO.getSearchProps();
                                if (CollectionUtils.isNotEmpty(gdsPropRespDTOList)) {
                                    extraRespVO.setPropList(gdsPropRespDTOList);
                                }
                            }
                        }

                    }
                }

            } else {

                // 指定了查询分类的情况下，虽然查询结果为空，但是分类目录信息任然需要展示
                if (gdsCategoryRespDTO != null) {
                    extraRespVO = this.addCategoryQueryResult(extraRespVO, gds006Req.getCategory(), gdsCategoryRespDTO.getCatgName(), gdsCategoryRespDTO.getCatgParent(), "0");
                }

            }
        } else {
            throw new BusinessException(result.getMessage());
        }

    }
    /**
     * 
     * hasValueAdded:(判断是否有增值服务). <br/> 
     * 
     * @param propertycodes
     * @return 
     * @since JDK 1.6
     */
    private boolean hasValueAdded(List<String> propertycodes) {
        boolean hasValueAdded = false;
        if(CollectionUtils.isNotEmpty(propertycodes)){
            hasValueAdded = propertycodes.contains(VALUE_ADDED_PROPERTY_CODE);
        }
        return hasValueAdded;
    }
    @SuppressWarnings({ "rawtypes" })
    private PageResponseDTO<GoodSearchResult> renderRespVO(Gds006Req gds006Req, SearchResult<GoodSearchResult> result) throws BusinessException {
        PageResponseDTO<GoodSearchResult> t = new PageResponseDTO<GoodSearchResult>();
        t.setResult(this.renderSearchResult(result.getResultList()));
        t.setPageNo(gds006Req.getPageNumber());
        t.setPageSize(gds006Req.getPageSize());
        t.setCount(result.getNumFound());
        t.setPageCount(result.getTotallyPage());

        return t;
    }

    public static final String APP = "2";

    private List<GoodSearchResult> renderSearchResult(List<GoodSearchResult> goodSearchResultVOList) {

        if (CollectionUtils.isNotEmpty(goodSearchResultVOList)) {
            for (GoodSearchResult goodSearchResultVO : goodSearchResultVOList) {
                goodSearchResultVO.render();

                List<String> typeList = new ArrayList<String>();
                goodSearchResultVO.setPromotionType(typeList);

                try {
                    // 获取促销信息
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
                    promRuleCheckDTO.setStaffId(custInfoReqDTO.getStaff().getId() + "");
                    promRuleCheckDTO.setGdsId(Long.parseLong(goodSearchResultVO.getId()));
                    promRuleCheckDTO.setChannelValue(APP);
                    promRuleCheckDTO.setShopId(Long.parseLong(goodSearchResultVO.getShopId()));
                    promRuleCheckDTO.setCalDate(new Date(System.currentTimeMillis()));
                    promRuleCheckDTO.setSkuId(goodSearchResultVO.getFirstSkuId());
                    promRuleCheckDTO.setBasePrice(goodSearchResultVO.getDefaultPrice());
                    promRuleCheckDTO.setBuyPrice(goodSearchResultVO.getDiscountPrice());
                    promRuleCheckDTO.setShopName(promRuleCheckDTO.getShopName());
                    promRuleCheckDTO.setCalDate(DateUtil.getSysDate());
                    // 获取促销价格
                    List<PromListRespDTO> promInfoDTOList = promRSV.listPromForSolr(promRuleCheckDTO);
                    List<String> promotionType=new ArrayList<String>();
                    if (CollectionUtils.isNotEmpty(promInfoDTOList)) {
                        PromListRespDTO prom = promInfoDTOList.get(0);
                        if (prom.getPromSkuPriceRespDTO() != null) {
                            BigDecimal price = prom.getPromSkuPriceRespDTO().getDiscountFinalPrice();
                            if (price != null && price.longValue() != 0) {
                                goodSearchResultVO.setDiscountPrice(price.longValue());
                            }
                        }
                        for (PromListRespDTO promListRespDTO : promInfoDTOList) {
                            if(promListRespDTO.getPromInfoDTO() !=null && StringUtils.isNoneBlank(promListRespDTO.getPromInfoDTO().getPromTypeName())){
                                promotionType.add(promListRespDTO.getPromInfoDTO().getPromTypeShow());
                            }
                        }
                        goodSearchResultVO.setPromotionType(promotionType);
                    }
                } catch (Exception e) {
                    LogUtil.error(this.getClass().getName(), "cal prom price failed", e);
                }
            }
        }

        return goodSearchResultVOList;

    }

    /**
     * 单个分类查询
     * 
     * @param extraRespVO
     * @param currCategoryCode
     * @param currCategoryName
     * @return
     */
    private ExtraRespVO addCategoryQueryResult(ExtraRespVO extraRespVO, String currCategoryCode, String currCategoryName, String parentCateCode, String hits) {

        GdsCategoryReqDTO gdsCategoryReqDTO = null;
        List<GdsCategoryRespDTO> childrenCateList = null;
        if (StringUtils.isNotBlank(parentCateCode)) {

            // 查询当前分类的父分类
            gdsCategoryReqDTO = new GdsCategoryReqDTO();
            gdsCategoryReqDTO.setCatgCode(parentCateCode);
            GdsCategoryRespDTO gdsCategoryRespDTO = this.gdsCategoryRSV.queryGdsCategoryByPK(gdsCategoryReqDTO);

            // 出现异常，父分类已被删除
            if (gdsCategoryRespDTO == null) {
                throw new BusinessException("分类不存在或已被删除：" + parentCateCode);
            } else {
                extraRespVO.setParentCateCode(gdsCategoryRespDTO.getCatgCode());
                extraRespVO.setParentCateName(gdsCategoryRespDTO.getCatgName());
            }

            gdsCategoryReqDTO = new GdsCategoryReqDTO();
            gdsCategoryReqDTO.setCatgParent(parentCateCode);
            childrenCateList = this.gdsCategoryRSV.querySubCategory(gdsCategoryReqDTO);

        } else {

            // 没有父分类(当前分类已经是一级分类)，则设置当前分类为父分类
            extraRespVO.setParentCateCode("");
            extraRespVO.setParentCateName("所有分类");

            gdsCategoryReqDTO = new GdsCategoryReqDTO();
            gdsCategoryReqDTO.setCatlogId(1l);
            childrenCateList = this.gdsCategoryRSV.queryRootCategory(gdsCategoryReqDTO);
        }

        // 查询当前分类的其它兄弟分类
        List<String> topCateNameList = new ArrayList<String>();
        topCateNameList.add(currCategoryName);
        Map<String, Map<String, String>> topCateInfoMap = new HashMap<String, Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", currCategoryCode);
        map.put("count", hits);// 只能统计出当前分类的查询结果个数
        topCateInfoMap.put(currCategoryName, map);

        gdsCategoryReqDTO = new GdsCategoryReqDTO();
        gdsCategoryReqDTO.setCatgCode(parentCateCode);
        if (CollectionUtils.isNotEmpty(childrenCateList)) {
            for (GdsCategoryRespDTO childrenCate : childrenCateList) {
                if (!StringUtils.equals(currCategoryCode, childrenCate.getCatgCode())) {
                    topCateNameList.add(childrenCate.getCatgName());
                    map = new HashMap<String, String>();
                    map.put("code", childrenCate.getCatgCode());
                    topCateInfoMap.put(childrenCate.getCatgName(), map);
                }
            }
        }

        extraRespVO.setTopCateNameList(topCateNameList);
        extraRespVO.setTopCateInfoMap(topCateInfoMap);

        // 查询当前分类子分类列表
        gdsCategoryReqDTO = new GdsCategoryReqDTO();
        gdsCategoryReqDTO.setCatgParent(currCategoryCode);
        childrenCateList = this.gdsCategoryRSV.querySubCategory(gdsCategoryReqDTO);

        if (CollectionUtils.isNotEmpty(childrenCateList)) {

            // key为顶级分类名称
            Map<String, List<Map<String, String>>> cateInfoMap = new HashMap<String, List<Map<String, String>>>();
            List<Map<String, String>> cateList = new ArrayList<Map<String, String>>();
            for (GdsCategoryRespDTO childrenCate : childrenCateList) {

                // 查询Main分类Facet列表
                Map<String, String> fieldInfoMap = new HashMap<String, String>();
                fieldInfoMap.put("code", childrenCate.getCatgCode());
                fieldInfoMap.put("value", childrenCate.getCatgName());
                cateList.add(fieldInfoMap);

            }

            cateInfoMap.put(currCategoryName, cateList);
            extraRespVO.setCateInfoMap(cateInfoMap);

        }

        // 查询分类的属性和属性值列表
        GdsCatg2PropReqDTO reqDTO = new GdsCatg2PropReqDTO();
        reqDTO.setCatgCode(currCategoryCode);
        GdsCatg2PropRelationRespDTO gdsCatg2PropRelationRespDTO = gdsCategoryRSV.querySearchProps(reqDTO);
        if (gdsCatg2PropRelationRespDTO != null) {
            List<GdsPropRespDTO> gdsPropRespDTOList = gdsCatg2PropRelationRespDTO.getSearchProps();
            if (CollectionUtils.isNotEmpty(gdsPropRespDTOList)) {
                extraRespVO.setPropList(gdsPropRespDTOList);
            }
        }

        return extraRespVO;

    }

    /**
     * 字段处理过滤
     * 
     * @param searchParam
     * @param vo
     * @return
     */
    private SearchParam addFieldFilterSupport(SearchParam searchParam, Gds006Req vo) {

        List<SortField> sortFieldList = new ArrayList<SortField>();

        // 自定义排序
        if (StringUtils.isNotBlank(vo.getField())) {

            // 排序字段校验
            if (StringUtils.equals("sales", vo.getField()) || StringUtils.equals("discountPrice", vo.getField())|| StringUtils.equals("publishDate", vo.getField()) || StringUtils.equals("newProductTime", vo.getField())) {
                ESort eSort = ESort.getAndValidSort(vo.getSort());
                if (null != eSort) {
                    if(StringUtils.equals("discountPrice", vo.getField())){
                        sortFieldList.add(new SortField(FIELD_DISCOUNTPRICE+StaffLocaleUtil.getStaff().getStaffLevelCode(), eSort));
                    }else if(StringUtils.equals("sales", vo.getField())){
                        sortFieldList.add(new SortField(vo.getField(), eSort));
                    }else if(StringUtils.equals("publishDate", vo.getField())){
                        sortFieldList.add(new SortField(vo.getField(), eSort));
                    }else{
                        sortFieldList.add(new SortField(vo.getField(), eSort));
                    }
                }
            }

            // 自定义排序，无需加入评分排序和相关度排序

        } else {

            // 无自定义排序，则第一排序字段为商品类型，目的是纸质书拍前面
            sortFieldList.add(new SortField("gdsTypeId", ESort.ASC));

            if (!StringUtils.isBlank(searchParam.getKeyword()) && !StringUtils.equals(SearchConstants.STAR, searchParam.getKeyword())) {
                // 无自定义排序，需要加入评分排序和相关度排序
                // searchParam.setIfSortByScore(true, 1);
                sortFieldList.add(new SortField("gdsNameBoost", ESort.ASC));
            }

            // 默认按出版日期降序
            sortFieldList.add(new SortField("newProductTime", ESort.DESC));
        }

        searchParam.setSortFieldList(sortFieldList);

        return searchParam;

    }

    /**
     * 
     * queryGdsCollect:根据产品ID获取收藏记录ID. <br/> 
     * 
     * @param gdsId
     * @return 
     * @since JDK 1.6
     */
    private Long queryGdsCollect (String gdsId){
        // 获取用户对商品的收藏信息
        try{
            // 商品ID转换.
            Long id = Long.valueOf(gdsId);
            GdsCollectReqDTO collectReqDTO = new GdsCollectReqDTO();
            collectReqDTO.setGdsId(id);
            collectReqDTO.setStaffId(collectReqDTO.getStaff().getId());
            collectReqDTO.setPageNo(0);
            collectReqDTO.setPageSize(1);
            PageResponseDTO<GdsCollectRespDTO> pageResponseDTO = gdsCollectRSV.queryGdsCollectRespDTOPagingByStaff(collectReqDTO);
            if ( pageResponseDTO.getCount() > 0) {
                return pageResponseDTO.getResult().get(0).getId();
            }
        }catch(Exception e){
            LogUtil.error(MODULE, "查询商品收藏记录ID遇到异常!该异常仅作打印处理",e);
        }
        return null;
    }
    
    /**
     * 
     * queryBookOtherType:(判断该商品是否有其他类型书籍). <br/> 
     * 
     * @param model
     * @param gdsDetailVO
     * @return 
     * @since JDK 1.6
     */
    public String queryBookOtherType (GoodSearchResult goodSearchResult){
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
		if (StringUtil.isBlank(gdsDigitsBookCatCode)) {
			gdsDigitsBookCatCode = GDS_DIGITS_BOOK_CAT_CODE;
		}
    	//1:纸质书  2:电子书 3:数字教材
        String bookOtherType = "";
        String bookGds2SkuId = "";
        // 查询单品信息
        GdsSkuInfoReqDTO skuInfoReqDTO = new GdsSkuInfoReqDTO();
        skuInfoReqDTO.setGdsId(Long.valueOf(goodSearchResult.getId()));
        skuInfoReqDTO.setId(goodSearchResult.getFirstSkuId());
        SkuQueryOption[] skuQuery = new SkuQueryOption[] { SkuQueryOption.BASIC,
                SkuQueryOption.PROP };
        skuInfoReqDTO.setSkuQuery(skuQuery);
        GdsSkuInfoRespDTO gdsSkuInfoRespDTO = iGdsSkuInfoQueryRSV
                .querySkuInfoByOptions(skuInfoReqDTO);
        
       //获取当前商品本身类型
  		GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
  		dto.setCatgCode(gdsSkuInfoRespDTO.getMainCatgs());
  		
  		List<GdsCategoryRespDTO> list = gdsCategoryRSV.queryCategoryTraceUpon(dto);
  		//获取主分类是否为(纸质书，电子书，数字教材)
  		String itselfType = "";
  		if (list != null && list.size() > 0) {
  			for (GdsCategoryRespDTO categoryRespDTO : list) {
  				if (paperBookCateCode.equals(categoryRespDTO.getCatgCode())) {
  					itselfType = paperBookCateCode;
  				}
  			}
  		}
  		
  		if(StringUtil.isNotBlank(itselfType)){
  			//标准ISBN 获取是否有 电子书，纸质书，数字材料
  			if (gdsSkuInfoRespDTO.getAllPropMaps() != null) {
  				Map<String, GdsPropRespDTO> propMaps = gdsSkuInfoRespDTO.getAllPropMaps();
  				GdsPropRespDTO  standIsbnProp = propMaps.get(String.valueOf(PROP_ID_1032));
  				if(standIsbnProp!=null && standIsbnProp.getValues() != null){
  					String standIsbn = String.valueOf(standIsbnProp.getValues().get(0).getPropValue());
  					if(StringUtils.isNotBlank(standIsbn) && !"".equals(standIsbn)&&!"null".equalsIgnoreCase(standIsbn)){
  						GdsSku2PropPropIdxReqDTO reqDTO = new GdsSku2PropPropIdxReqDTO();
  						reqDTO.setPropId(PROP_ID_1032);
  						reqDTO.setPropValue(standIsbn);
  						reqDTO.setPageSize(PAGE_SIZE_10);
  						
  						// 只取当前商品的
  						PageResponseDTO<GdsSkuInfoRespDTO> rspDto = iGdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(reqDTO);
  						
  						if (rspDto != null) {
  							if (rspDto.getResult() != null && rspDto.getResult().size() > 0) {
  								for (GdsSkuInfoRespDTO skuInfoRespDTO : rspDto.getResult()) {
  									if (!GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES.equals(skuInfoRespDTO.getGdsStatus())) {
  										continue;
  									}
  									if(itselfType != ""){
  										int index = skuInfoRespDTO.getPlatCatgs().indexOf(itselfType);
  										if(index == -1 ){
  											if(skuInfoRespDTO.getPlatCatgs().indexOf(eBookCateCode)!=-1){
  												bookOtherType = "2";
  												bookGds2SkuId = skuInfoRespDTO.getGdsId()+"-"+skuInfoRespDTO.getId();
  											}else if(skuInfoRespDTO.getPlatCatgs().indexOf(gdsDigitsBookCatCode)!=-1){
  												bookOtherType = "3";
  												bookGds2SkuId = skuInfoRespDTO.getGdsId()+"-"+skuInfoRespDTO.getId();
  											}
  										}
  									}
  								}
  							}
  						}
  					}
  				}
  			}
  		}
        
        return bookOtherType;
    }
    
    /**
     * 属性过滤
     * 
     * @param extraQueryInfo
     * @param propertyGroup
     * @return
     */
    private ExtraQueryInfo addPropertyFilterSupport(SearchParam searchParam, ExtraQueryInfo extraQueryInfo, Gds006Req vo) {

        if (CollectionUtils.isEmpty(vo.getPropertyGroup())) {
            return extraQueryInfo;
        }

        int length = vo.getPropertyGroup().size();
        
        if (length > 0) {

        	List<ExtraFieldQueryField> extraANDFieldQueryList=new ArrayList<ExtraFieldQueryField>();
        	
            List<ExtraQueryInfo.QueryProperty> queryPropertyList = new ArrayList<ExtraQueryInfo.QueryProperty>();
            List<MulValueExtraFieldQueryField> extraANDMulValueFieldQueryList = new ArrayList<MulValueExtraFieldQueryField>();
            ExtraQueryInfo.QueryProperty queryProperty ;
           /* List<String> propertyValueCodeList;
            String propertyValueIdArr[] ;*/
            List<String> propertyValueCodeList = null;
            List<String> propertyValueIdArr = null;
            for (int i = 0; i < length; i++) {
                SearchPropReqInfo searchPropReqInfo = vo.getPropertyGroup().get(i);
                
                //人卫二期查询搜索改造
                if (StringUtils.equals(searchPropReqInfo.getPropertyId(), ROPERTYCODE_PMPHAUTHOR)) {
                	propertyValueIdArr = searchPropReqInfo.getPropertyValues();
                	List<String> authorList = new ArrayList<String>();
                	if(CollectionUtils.isNotEmpty(propertyValueIdArr)){
                		for(String author : propertyValueIdArr){
                			authorList.add(SearchFacade.escapeQueryChars(author));
                		}
                	}
                	MulValueExtraFieldQueryField queryField = new MulValueExtraFieldQueryField();
            		queryField.setName("author");
            		queryField.setValue(authorList);
            		queryField.setIfFuzzyQuery(true);
                    
            		extraANDMulValueFieldQueryList.add(queryField);
                }else if (StringUtils.equals(searchPropReqInfo.getPropertyId(), ROPERTYCODE_PMPHSERVICE)) {
                	/*propertyValueIdArr = jsonObj.getString("propertyValueIds").split(
                            SearchConstants.COMMA);*/
                	propertyValueIdArr = searchPropReqInfo.getPropertyValues();
                	String pmphServiceVal = propertyValueIdArr.get(0);
            		if(StringUtils.equals(pmphServiceVal, "ifAppSpecPrice")){
            			ExtraFieldQueryField extraFieldQueryField=new ExtraFieldQueryField();
                        extraFieldQueryField.setName("ifAppSpecPrice");
                        extraFieldQueryField.setValue("1");
                        extraANDFieldQueryList.add(extraFieldQueryField);
            		}else if(StringUtils.equals(pmphServiceVal, "ifStorage")){
            			ExtraFieldQueryField extraFieldQueryField=new ExtraFieldQueryField();
            			extraFieldQueryField.setName("ifStorage");
                        extraFieldQueryField.setValue("1");
                        extraANDFieldQueryList.add(extraFieldQueryField);
            		}else if(StringUtils.equals(pmphServiceVal, "ifPromotion")){
            			ExtraFieldQueryField extraFieldQueryField=new ExtraFieldQueryField();
            			extraFieldQueryField.setName("ifPromotion");
                        extraFieldQueryField.setValue("1");
                        extraANDFieldQueryList.add(extraFieldQueryField);
            		}
                }else if(StringUtils.equals(searchPropReqInfo.getPropertyId(), ROPERTYCODE_CATEGORIES)){
                	propertyValueIdArr = searchPropReqInfo.getPropertyValues();
                	String categoryId = propertyValueIdArr.get(0);
                	if(StringUtils.isNotEmpty(categoryId)){
                		ExtraFieldQueryField extraFieldQueryField=new ExtraFieldQueryField();
            			extraFieldQueryField.setName("categories");
                        extraFieldQueryField.setValue(categoryId);
                        extraANDFieldQueryList.add(extraFieldQueryField);
                	}
            	}else if (StringUtils.equals(searchPropReqInfo.getPropertyId(), ROPERTYCODE_DEFAULTPRICE)) {

                    propertyValueIdArr = searchPropReqInfo.getPropertyValues();
                    if (propertyValueIdArr != null && propertyValueIdArr.size() > 0) {
                        String type = propertyValueIdArr.get(0);
                        String priceStart = "";
                        String priceEnd = "";

                        if (StringUtils.equals("1", type)) {
                            priceStart = "0";
                            priceEnd = "8900";
                        } else if (StringUtils.equals("2", type)) {
                            priceStart = "9000";
                            priceEnd = "19900";
                        } else if (StringUtils.equals("3", type)) {
                            priceStart = "20000";
                            priceEnd = "29900";
                        } else if (StringUtils.equals("4", type)) {
                            priceStart = "30000";
                            priceEnd = "39900";
                        } else if (StringUtils.equals("5", type)) {
                            priceStart = "40000";
                            priceEnd = "*";
                        }

                        if (StringUtils.isNotBlank(priceStart) && StringUtils.isNotBlank(priceEnd)) {

                            // 价格范围查询
                            List<RangeQueryField> rangeQueryFieldList = new ArrayList<RangeQueryField>();
                            RangeQueryField rangeQueryField = new RangeQueryField();
                            rangeQueryField.setName(FIELD_DISCOUNTPRICE + StaffLocaleUtil.getStaff().getStaffLevelCode());
                            rangeQueryField.setStart(priceStart);
                            rangeQueryField.setEnd(priceEnd);
                            rangeQueryFieldList.add(rangeQueryField);
                            searchParam.setRangeQueryFieldList(rangeQueryFieldList);
                        }

                    }

                } else {
                	queryProperty = new QueryProperty();
                    queryProperty.setPropertyCode(searchPropReqInfo.getPropertyId());

                    propertyValueCodeList = new ArrayList<String>();

                    propertyValueIdArr = searchPropReqInfo.getPropertyValues();
                    if (propertyValueIdArr != null && propertyValueIdArr.size() > 0) {
                        for (String propertyValueId : propertyValueIdArr) {
                            propertyValueCodeList.add(propertyValueId);
                        }
                    }
                    queryProperty.setPropertyValueCodeList(propertyValueCodeList);
                    queryPropertyList.add(queryProperty);
                }
            }
            searchParam.setExtraANDFieldQueryList(extraANDFieldQueryList);
            searchParam.setExtraANDMulValueFieldQueryList(extraANDMulValueFieldQueryList);
            extraQueryInfo.setQueryPropertyList(queryPropertyList);

        }

        // 自定义范围查询
        // 以范围属性查询优先
        if (CollectionUtils.isEmpty(searchParam.getRangeQueryFieldList())) {
            if (StringUtils.isNotBlank(vo.getPriceStart()) && StringUtils.isNotBlank(vo.getPriceEnd())) {
                List<RangeQueryField> rangeQueryFieldList = new ArrayList<RangeQueryField>();
                RangeQueryField rangeQueryField = new RangeQueryField();
                rangeQueryField.setName(FIELD_DISCOUNTPRICE + StaffLocaleUtil.getStaff().getStaffLevelCode());
                try {
                    String start;
                    if(!org.apache.commons.lang.StringUtils.equals(vo.getPriceStart(),SearchConstants.STAR)){
                        start=Long.parseLong(vo.getPriceStart()) * 100+"";
                    }else{
                        start=SearchConstants.STAR;
                    }
                    String end;
                    if(!org.apache.commons.lang.StringUtils.equals(vo.getPriceEnd(),SearchConstants.STAR)){
                        end=Long.parseLong(vo.getPriceEnd()) * 100+"";
                    }else{
                        end=SearchConstants.STAR;
                    }
                    rangeQueryField.setStart(start);
                    rangeQueryField.setEnd(end);
                    rangeQueryFieldList.add(rangeQueryField);
                    searchParam.setRangeQueryFieldList(rangeQueryFieldList);
                } catch (Exception e) {
                    // TODO 查询范围超出或格式异常
                }

            }
        }

        return extraQueryInfo;

    }
    
    
    /**
     * 
     * getGdsTypeLimit:获取产品限定类型列表. <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    private List<String> getGdsTypeLimit(){
        List<String> typeLst = null;
        BaseSysCfgRespDTO sysCfg = SysCfgUtil.fetchSysCfg(GDS_TYPE_LIMIT);
        if(null != sysCfg && StringUtil.isNotBlank(sysCfg.getParaValue())){
            String paraVal = sysCfg.getParaValue();
            if(StringUtil.isNotBlank(paraVal) && !"-1".equals(paraVal)){
                typeLst = Arrays.asList(paraVal.split(",")); 
            }
        }
        return typeLst;
    }
    
    
    
    //返回作者列表的字符串格式数据
    private String authorListToStr(List<String> authorList){
    	String authorStr = "";
    	if(CollectionUtils.isNotEmpty(authorList)){
    		for(String author:authorList){
    			authorStr += author+"、";
    		}
    	}
    	if(StringUtil.isNotBlank(authorStr)){
    		authorStr = authorStr.substring(0, authorStr.length()-1);
    	}
    	return authorStr;
    }
}
