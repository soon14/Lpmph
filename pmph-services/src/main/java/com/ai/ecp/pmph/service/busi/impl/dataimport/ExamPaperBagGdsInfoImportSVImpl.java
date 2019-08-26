package com.ai.ecp.pmph.service.busi.impl.dataimport;

import com.ai.ecp.goods.dao.model.GdsCategory;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCatgSyncReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatgSyncRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoAddReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2CatgReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2PriceReqDTO;
import com.ai.ecp.goods.dubbo.dto.price.GdsPriceReqDTO;
import com.ai.ecp.goods.service.busi.impl.dataimport.BaseGdsInfoImportSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IExamPaperBagGdsInfoImportSV;
import com.ai.ecp.server.front.exception.BusinessException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project Name:ecp-services-goods <br>
 * Description: 试卷包商品导入，试卷、试卷包、辅导班统一使用同一个考试网的其它分类<br>
 * Date:2015年11月3日下午8:27:13  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class ExamPaperBagGdsInfoImportSVImpl extends BaseGdsInfoImportSV implements IExamPaperBagGdsInfoImportSV {

    /**
     * 商品分类ID，KEY
     */
    public final static String KEY_GOODTYPEID = "GoodTypeID";

    @SuppressWarnings("rawtypes")
    @Override
    protected void addValidation(Map map) throws BusinessException {
        super.addValidation(map);
      //分类ID为空就按照默认归到其它分类处理
//        if (!this.isNotNull_(map,KEY_GOODTYPEID)) {
//            throw new BusinessException(PmphGdsDataImportErrorConstants.ZY.ERROR_GOODS_DATAIMPORT_ZY_2,
//                    new String[] { map.toString() });
//        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void updateValidation(Map map) throws BusinessException {
        super.updateValidation(map);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void deleteValidation(Map map) throws BusinessException {
        super.deleteValidation(map);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected EActionType doActionType(Map map) throws BusinessException {
        return EActionType.ADD;
    }

    @Override
    protected String getOriginType() throws BusinessException {
        return PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN;
    }
    
    @Override
    protected String getSrcGdsCodePrefix() throws BusinessException {
        return PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPERBAG_ORIGN_CODE_PREFIX;
    }
    
//    @Override
//    protected String getSrcGdsCatgCodePrefix() throws BusinessException {
//        return PmphGdsDataImportConstants.Commons.ZY_EXAM_ORIGN_CODE_PREFIX;
//    }

    @SuppressWarnings("rawtypes")
    @Override
    protected String getGdsStatus(Map map, List<String> list) throws BusinessException {
        
        Long zhipindingjia = 0L;
        boolean flag = true;
        if (this.isNotNull_(map,"Price")) {
            zhipindingjia = (long) (Float.parseFloat(this.getValue_(map,"Price")) * 100);
        }
        if (zhipindingjia <= 0) {
            flag = false;
        }

        if (flag) {
            return GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES;
        } else {
            return GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES;
        }
    }

    @Override
    protected String getPkKeyName() throws BusinessException {
        return "ID";
    }
    
    @Override
    protected String getGdsNameKeyName() throws BusinessException {
        return "Name";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected GdsInfoAddReqDTO createAddGdsInfoParam(Map map) throws BusinessException {
        GdsInfoAddReqDTO gdsInfoAddReqDTO = new GdsInfoAddReqDTO();

        // 设置商品基本信息
        GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
        gdsInfoAddReqDTO.setGdsInfoReqDTO(gdsInfoReqDTO);
        // gdsInfoReqDTO.setId(id);
        // gdsInfoReqDTO.setSkuId(skuId);
        // gdsInfoReqDTO.setSkuProps(skuProps);

        gdsInfoReqDTO.setCatlogId(PmphGdsDataImportConstants.Commons.CATLOG_RENWEI);
        gdsInfoReqDTO.setCompanyId(PmphGdsDataImportConstants.Commons.COMPANY_ID);
        gdsInfoReqDTO.setStaffId(PmphGdsDataImportConstants.Commons.STAFF_ID);
        // gdsInfoReqDTO.setSnapId(snapId);
        if (this.isNotNull_(map,this.getGdsNameKeyName())) {
            gdsInfoReqDTO.setGdsName(this.getValue_(map,this.getGdsNameKeyName()));
        }

        // 分类信息设置
        gdsInfoReqDTO = this.addCategoryInfo(map, gdsInfoReqDTO);

        // gdsInfoReqDTO.setGdsSubHead(gdsSubHead);

        // 描述内容上传MongoDB，记录Id
        if (this.isNotNull_(map,"Description")) {
            gdsInfoReqDTO.setGdsDesc(this.saveToMongoDB(this.getValue_(map,"Description"), "gdsDesc"));
        }

        // gdsInfoReqDTO.setGdsPartlist(gdsPartlist);
        gdsInfoReqDTO.setGdsTypeId(PmphGdsDataImportConstants.Commons.GOODTYPE_VIRTUAL_ID);// 虚拟商品
        gdsInfoReqDTO.setGdsTypeCode(PmphGdsDataImportConstants.Commons.GOODTYPE_VIRTUAL_CODE);// 虚拟商品
//        gdsInfoReqDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES);
        // gdsInfoReqDTO.setGdsStatusArr(gdsStatusArr);
        // gdsInfoReqDTO.setGdsApprove(gdsApprove);
        // gdsInfoReqDTO.setGdsUrl(gdsUrl);
        gdsInfoReqDTO.setSortNo(1L);
        gdsInfoReqDTO.setShopId(PmphGdsDataImportConstants.Commons.SHOP_ID);
        // gdsInfoReqDTO.setTaxId(taxId);
        // gdsInfoReqDTO.setGdsLabel(gdsLabel);
        // gdsInfoReqDTO.setPutonTime(putonTime);
        // gdsInfoReqDTO.setPutoffTime(putoffTime);
        // gdsInfoReqDTO.setPostTime(postTime);
        gdsInfoReqDTO.setIfSendscore("0");
        gdsInfoReqDTO.setIfScoreGds("0");
        gdsInfoReqDTO.setIfSalealone("0");
        gdsInfoReqDTO.setIfRecomm("0");
        gdsInfoReqDTO.setIfNew("0");
        gdsInfoReqDTO.setIfStocknotice("0");
        gdsInfoReqDTO.setIfFree("0");
        gdsInfoReqDTO.setIfDisperseStock("0");// 0不启用
        gdsInfoReqDTO.setIfSeniorPrice("0");
        gdsInfoReqDTO.setIfLadderPrice("0");// 0普通价格
        gdsInfoReqDTO.setIfEntityCode("1");// 1不需要录入串号
        gdsInfoReqDTO.setShipTemplateId(-1L);// -1为未配置
        // gdsInfoReqDTO.setSupplierId(supplierId);
        gdsInfoReqDTO.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        // gdsInfoReqDTO.setCountryCode(countryCode);
        // gdsInfoReqDTO.setProvinceCode(provinceCode);
        // gdsInfoReqDTO.setCityCode(cityCode);

        // 设置公司编码
        gdsInfoAddReqDTO.setCompanyId(PmphGdsDataImportConstants.Commons.COMPANY_ID);

        // 属性信息映射
        List<String[]> propInfoList = this.createAddPropInfoList(map);

        // 设置商品属性关系
        gdsInfoAddReqDTO.setGds2PropReqDTOs(this.createGdsGds2PropReqDTOList(propInfoList, null));

        // 设置商品单品已选规格属性
        // List<GdsGds2PropReqDTO> skuProps = new ArrayList<GdsGds2PropReqDTO>();
        // gdsInfoAddReqDTO.setSkuProps(skuProps);

        // 设置商品分类关系
        List<GdsGds2CatgReqDTO> gds2CatgReqDTOList = new ArrayList<GdsGds2CatgReqDTO>();
        GdsGds2CatgReqDTO gdsGds2CatgReqDTO = new GdsGds2CatgReqDTO();
        // gdsGds2CatgReqDTO.setGdsId(gdsId);
        gdsGds2CatgReqDTO.setCatgCode(gdsInfoReqDTO.getMainCatgs());
        gdsGds2CatgReqDTO.setCatgType(GdsConstants.GdsCategory.CATG_TYPE_1);
        gdsGds2CatgReqDTO.setGds2catgType(GdsConstants.GdsInfo.GDS_2_CATG_RTYPE_MAIN);
        gds2CatgReqDTOList.add(gdsGds2CatgReqDTO);
        gdsInfoAddReqDTO.setGds2CatgReqDTOs(gds2CatgReqDTOList);

        // 单品价格关系设置
        List<GdsSku2PriceReqDTO> sku2PriceReqDTOs = new ArrayList<GdsSku2PriceReqDTO>();
        GdsSku2PriceReqDTO gdsSku2PriceReqDTO = new GdsSku2PriceReqDTO();
        gdsSku2PriceReqDTO.setPriceTypeCode(GdsConstants.GdsInfo.SKU_PRICE_TYPE_ORDINARY);
        GdsPriceReqDTO gdsPriceReqDTO = new GdsPriceReqDTO();
        // 价格默认设置为0
        Long tpPrice = 0L;
        if (this.isNotNull_(map,"Price")) {
            tpPrice = (long) (Float.parseFloat(this.getValue_(map,"Price")) * 100);
        }
        gdsPriceReqDTO.setPrice(tpPrice);
        gdsSku2PriceReqDTO.setPrice(gdsPriceReqDTO);
        sku2PriceReqDTOs.add(gdsSku2PriceReqDTO);
        
        gdsInfoReqDTO.setGuidePrice(tpPrice);

        // 设置单品价格关系到商品
        // gdsInfoAddReqDTO.setSku2PriceReqDTOs(sku2PriceReqDTOs);

        // 设置单品列表
        List<GdsSkuInfoReqDTO> skuInfoReqDTOs = new ArrayList<GdsSkuInfoReqDTO>();
        GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
        gdsSkuInfoReqDTO.setCommonPrice(tpPrice);
        // 设置单品价格关系到单品
        gdsSkuInfoReqDTO.setSku2PriceReqDTOs(sku2PriceReqDTOs);
        // 设置单品属性关系//直接从商品属性关系复制
        gdsSkuInfoReqDTO.setGdsProps(this.createGdsSku2PropReqDTOList(gdsInfoAddReqDTO
                .getGds2PropReqDTOs()));
        skuInfoReqDTOs.add(gdsSkuInfoReqDTO);
        gdsInfoAddReqDTO.setSkuInfoReqDTOs(skuInfoReqDTOs);

        // 设置商品图片关系
        // List<GdsGds2MediaReqDTO> gds2MediaReqDTOs = new ArrayList<GdsGds2MediaReqDTO>();
        // gdsInfoAddReqDTO.setGds2MediaReqDTOs(gds2MediaReqDTOs);

        // 设置商品积分关系
        // List<GdsScoreExtReqDTO> gdsScoreExtReqDTOs = new ArrayList<GdsScoreExtReqDTO>();
        // gdsInfoAddReqDTO.setGdsScoreExtReqDTOs(gdsScoreExtReqDTOs);

        return gdsInfoAddReqDTO;
    }

    @SuppressWarnings("rawtypes")
    private List<String[]> createAddPropInfoList(Map map) throws BusinessException {

        List<String[]> propInfoList = new ArrayList<String[]>();
        
        //考试网商品类型标识属性
        propInfoList.add(new String[] { "1040", PmphGdsDataImportConstants.Commons.ZY_EXAM_PROP_PAPERBAG});
        
        //出版社写死
        propInfoList.add(new String[] { "1006",PmphGdsDataImportConstants.Commons.RENWEI_NAME});// 出版社

        return propInfoList;

    }
    
    @SuppressWarnings("rawtypes")
    private List<String[]> createUpdatePropInfoList(Map map,List<String> list) throws BusinessException {

        List<String[]> propInfoList = new ArrayList<String[]>();
        
        //考试网商品类型标识属性
        //propInfoList.add(new String[] { "1040", PmphGdsDataImportConstants.Commons.ZY_EXAM_PROP_PAPERBAG});
        
        //出版社写死
        //propInfoList.add(new String[] { "1006",PmphGdsDataImportConstants.Commons.RENWEI_NAME});// 出版社

        return propInfoList;

    }

    @SuppressWarnings("rawtypes")
    private GdsInfoReqDTO addCategoryInfo(Map map, GdsInfoReqDTO gdsInfoReqDTO)
            throws BusinessException {

        String mainCatgs = this.getValue_(map,KEY_GOODTYPEID);
        
        GdsCatgSyncRespDTO gdsCatgSyncRespDTO=null;
        
        if(StringUtils.isNotBlank(mainCatgs)){
            
            // 分类映射关系查询
        	GdsCatgSyncReqDTO gdsCatgSyncReqDTO=new GdsCatgSyncReqDTO();
        	gdsCatgSyncReqDTO.setCatgCode(mainCatgs);
        	gdsCatgSyncReqDTO.setSrcSystem(PmphGdsDataImportConstants.SrcSystem.ZY_02);
        	try {
                gdsCatgSyncRespDTO=this.gdsCategorySyncSV.queryGdsCategorySyncByPK(gdsCatgSyncReqDTO);
            } catch (BusinessException e) {
                //BusinessException异常不中断
            } catch (Exception e) {
                throw new BusinessException(e.getMessage());
            }

        	
        }
        
        String ecpCatgs="";

        // 找不到分类映射关系
        if (gdsCatgSyncRespDTO != null) {
        	
            if(!StringUtils.equals(gdsCatgSyncRespDTO.getMapCatgCode(), "-1")&&StringUtils.isNotBlank(gdsCatgSyncRespDTO.getMapCatgCode())){
                ecpCatgs = gdsCatgSyncRespDTO.getMapCatgCode();
                
                //判断分类是否存在
                try{
                    GdsCategory gdsCategory=this.gdsCategorySV.queryGdsCategoryById(gdsCatgSyncRespDTO.getMapCatgCode());
                    if(gdsCategory==null){
                        ecpCatgs="";
                    }
                }catch(Exception e){
                    ecpCatgs="";
                }
                
            }
            
        } 
        
        if(StringUtils.isBlank(ecpCatgs)){
        	//设置成试卷的其它分类
        	ecpCatgs = PmphGdsDataImportConstants.Commons.CATEGORY_EXAM_PAPER_OTHER_ID;
        }

        gdsInfoReqDTO.setMainCatgs(ecpCatgs);
        gdsInfoReqDTO.setPlatCatgs("<" + ecpCatgs + ">");
        // gdsInfoReqDTO.setShopCatgs(shopCatgs);

        return gdsInfoReqDTO;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected GdsInfoAddReqDTO createUpdateGdsInfoParam(Map map, List<String> list, Long gdsId,
            Long skuId) throws BusinessException {
        GdsInfoAddReqDTO gdsInfoAddReqDTO = new GdsInfoAddReqDTO();

        // 设置商品基本信息
        GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
        gdsInfoAddReqDTO.setGdsInfoReqDTO(gdsInfoReqDTO);

        // =====================新增的时候写死的信息有些更新的时候还是需要用到start=====================
        gdsInfoReqDTO.setCatlogId(PmphGdsDataImportConstants.Commons.CATLOG_RENWEI);
        gdsInfoReqDTO.setCompanyId(PmphGdsDataImportConstants.Commons.COMPANY_ID);
        gdsInfoReqDTO.setStaffId(PmphGdsDataImportConstants.Commons.STAFF_ID);

        gdsInfoReqDTO.setGdsTypeId(PmphGdsDataImportConstants.Commons.GOODTYPE_VIRTUAL_ID);// 虚拟商品
        gdsInfoReqDTO.setGdsTypeCode(PmphGdsDataImportConstants.Commons.GOODTYPE_VIRTUAL_CODE);// 虚拟商品
//        gdsInfoReqDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES);
        gdsInfoReqDTO.setShopId(PmphGdsDataImportConstants.Commons.SHOP_ID);
        gdsInfoReqDTO.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);

        // 设置公司编码
        gdsInfoAddReqDTO.setCompanyId(PmphGdsDataImportConstants.Commons.COMPANY_ID);

        // =====================新增的时候写死的信息有些更新的时候还是需要用到end=====================

        if (!list.contains("gdsName") && this.isNotNull_(map,this.getGdsNameKeyName())) {
            gdsInfoReqDTO.setGdsName(this.getValue_(map,this.getGdsNameKeyName()));
        }

        // 分类信息设置
        if (!list.contains("mainCatgs") && !list.contains("platCatgs")) {
            gdsInfoReqDTO = this.addCategoryInfo(map, gdsInfoReqDTO);

            // 设置商品分类关系
            List<GdsGds2CatgReqDTO> gds2CatgReqDTOList = new ArrayList<GdsGds2CatgReqDTO>();
            GdsGds2CatgReqDTO gdsGds2CatgReqDTO = new GdsGds2CatgReqDTO();
            gdsGds2CatgReqDTO.setGdsId(gdsId);
            gdsGds2CatgReqDTO.setCatgCode(gdsInfoReqDTO.getMainCatgs());
            gdsGds2CatgReqDTO.setCatgType(GdsConstants.GdsCategory.CATG_TYPE_1);
            gdsGds2CatgReqDTO.setGds2catgType(GdsConstants.GdsInfo.GDS_2_CATG_RTYPE_MAIN);
            gds2CatgReqDTOList.add(gdsGds2CatgReqDTO);
            gdsInfoAddReqDTO.setGds2CatgReqDTOs(gds2CatgReqDTOList);
        }

        // 描述内容上传MongoDB，记录Id
        if (!list.contains("gdsDesc") && this.isNotNull_(map,"Description")) {
            gdsInfoReqDTO.setGdsDesc(this.updateMongoDB(this.getValue_(map,"Description"),
                    gdsInfoReqDTO.getGdsDesc(), "gdsDesc"));
        }

        // 属性信息映射
        List<String[]> propInfoList = this.createUpdatePropInfoList(map,list);

        // 设置商品属性关系
        gdsInfoAddReqDTO.setGds2PropReqDTOs(this.createGdsGds2PropReqDTOList(propInfoList, list));

        // 设置单品列表
        List<GdsSkuInfoReqDTO> skuInfoReqDTOs = new ArrayList<GdsSkuInfoReqDTO>();
        GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();

        if (!list.contains("gdsPrice") && this.isNotNull_(map,"Price")) {

            // 单品价格关系设置
//            List<GdsSku2PriceReqDTO> sku2PriceReqDTOs = new ArrayList<GdsSku2PriceReqDTO>();
//            GdsSku2PriceReqDTO gdsSku2PriceReqDTO = new GdsSku2PriceReqDTO();
//            gdsSku2PriceReqDTO.setPriceTypeCode(GdsConstants.GdsInfo.SKU_PRICE_TYPE_ORDINARY);
//            GdsPriceReqDTO gdsPriceReqDTO = new GdsPriceReqDTO();
            Long tpPrice = (long) (Float.parseFloat(this.getValue_(map,"Price")) * 100);
//            gdsPriceReqDTO.setPrice(tpPrice);
//            gdsSku2PriceReqDTO.setPrice(gdsPriceReqDTO);
//            sku2PriceReqDTOs.add(gdsSku2PriceReqDTO);
//
//            // 设置单品价格关系到单品
//            gdsSkuInfoReqDTO.setSku2PriceReqDTOs(sku2PriceReqDTOs);
            
            gdsInfoReqDTO.setGuidePrice(tpPrice);
            gdsSkuInfoReqDTO.setCommonPrice(tpPrice);

        }

        // 设置单品属性关系//直接从商品属性关系复制
        gdsSkuInfoReqDTO.setGdsProps(this.createGdsSku2PropReqDTOList(gdsInfoAddReqDTO
                .getGds2PropReqDTOs()));
        skuInfoReqDTOs.add(gdsSkuInfoReqDTO);
        gdsInfoAddReqDTO.setSkuInfoReqDTOs(skuInfoReqDTOs);

        return gdsInfoAddReqDTO;
    }

    @Override
    protected Long getStaffId() throws BusinessException {
        return PmphGdsDataImportConstants.Commons.STAFF_ID;
    }

    @Override
    protected Long getCompanyId() throws BusinessException {
        return PmphGdsDataImportConstants.Commons.COMPANY_ID;
    }

}

