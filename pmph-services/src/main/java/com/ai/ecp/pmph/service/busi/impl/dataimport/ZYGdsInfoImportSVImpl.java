package com.ai.ecp.pmph.service.busi.impl.dataimport;

import com.ai.ecp.goods.dao.model.GdsCategory;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportErrorConstants;
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
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportErrorConstants;
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IZYGdsInfoImportSV;
import com.ai.ecp.server.front.exception.BusinessException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project Name:ecp-services-goods <br>
 * Description: 泽云图书信息导入<br>
 * (1)泽云过来的商品没有上架标志字段，过来的商品默认全部上架<br>
 * (2)泽云过来的商品没有操作标志字段，过来的商品默认全部新增，若已经存在则update<br>
 * Date:2015年10月26日下午9:26:23 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ZYGdsInfoImportSVImpl extends BaseGdsInfoImportSV implements IZYGdsInfoImportSV {

    /**
     * 商品分类ID，KEY
     */
    public final static String KEY_TPGOODTYPEID = "tpGoodTypeId";

    /**
     * 商品类型，KEY
     */
    public final static String KEY_TPTYPE = "tpType";

    @SuppressWarnings("rawtypes")
    @Override
    protected void addValidation(Map map) throws BusinessException {
        super.addValidation(map);
        //分类ID为空就按照默认归到其它分类处理
//        if (!this.isNotNull_(map,KEY_TPGOODTYPEID)) {
//            throw new BusinessException(PmphGdsDataImportErrorConstants.ZY.ERROR_GOODS_DATAIMPORT_ZY_2,
//                    new String[] { map.toString() });
//        }
        if (!this.isNotNull_(map,KEY_TPTYPE)) {
            throw new BusinessException(PmphGdsDataImportErrorConstants.ZY.ERROR_GOODS_DATAIMPORT_ZY_4,
                    new String[] { map.toString() });
        }
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
        return PmphGdsDataImportConstants.CodePreFix.ZY_EDBOOK_ORIGN_CODE_PREFIX;
    }
    
//    @Override
//    protected String getSrcGdsCatgCodePrefix() throws BusinessException {
//        return "";
//    }

    @SuppressWarnings("rawtypes")
    @Override
    protected String getGdsStatus(Map map, List<String> list) throws BusinessException {
        
        Long zhipindingjia = 0L;
        boolean flag = true;
        if (this.isNotNull_(map,"tpPrice")) {
            zhipindingjia = (long) (Float.parseFloat(this.getValue_(map,"tpPrice")) * 100);
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
        return "tpGoodsId";
    }
    
    @Override
    protected String getGdsNameKeyName() throws BusinessException {
        return "tpGoodsName";
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
        
        String isbn="";
        
        //ISBN字段
        if (this.isNotNull_(map,"tpISBN")) {
            isbn=this.getValue_(map,"tpISBN");
            
            //去除前后空格，防止后面解析出错
            isbn=isbn.trim();
            
            gdsInfoReqDTO.setIsbn(isbn);
        }
        

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
        if (this.isNotNull_(map,"tpEBookIntro")) {
            gdsInfoReqDTO.setGdsDesc(this.saveToMongoDB(this.getValue_(map,"tpEBookIntro"), "gdsDesc"));
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
        // 价格默认设置为0，价格单位统一以元为单位处理。
        Long tpPrice = 0L;
        if (this.isNotNull_(map,"tpPrice")) {
            tpPrice = (long) (Float.parseFloat(this.getValue_(map,"tpPrice")) * 100);
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
        if(StringUtils.isNotBlank(isbn)){
            gdsSkuInfoReqDTO.setIsbn(isbn);
        }
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

        if (this.isNotNull_(map,"tpAuthorName")) {
            
            //作者名称中空格出现乱码解决"  "：谢  幸  苟文丽--->谢? 幸? 苟文丽
            String tpAuthorName=this.getValue_(map,"tpAuthorName").replaceAll(" ","");
            propInfoList.add(new String[] { "1001",tpAuthorName});// 作者
        }else{
            propInfoList.add(new String[] { "1001",""});
        }
        
        /*-----------------------ISBN---------------------------*/

        //***电子书ISBN==EISBN***
        //***电子书ISBN==电子书标准ISBN==纸质书标准ISBN***
        if (this.isNotNull_(map,"tpISBN")) {
            String tpISBN=this.getValue_(map,"tpISBN");
            
            //电子书标准ISBN和ISBN一样
            propInfoList.add(new String[] { "1032",tpISBN});// 标准ISBN//7-117-03573-0
            propInfoList.add(new String[] { "1002",tpISBN});// ISBN//7-117-03573-0
            
            //5位ISBN号处理，取倒数第二段
            //7-117-06305-X--->06305
            //978-7-117-13899-5--->13899
            String arr[]=tpISBN.split("-");
            if(arr!=null&&arr.length>=2){
                tpISBN=arr[arr.length-2];
            }
            propInfoList.add(new String[] { "1004",tpISBN});// 五位ISBN号
        }else{
            propInfoList.add(new String[] { "1032",""});
            propInfoList.add(new String[] { "1002",""});
            propInfoList.add(new String[] { "1004",""});
        }

        if (this.isNotNull_(map,"tpEISBN")) {
            propInfoList.add(new String[] { "1003", this.getValue_(map,"tpEISBN") });// EISBN
        }else{
            propInfoList.add(new String[] { "1003",""});
        }
        
        /*-----------------------ISBN---------------------------*/
        
        // 出版日期日期格式：2012-07-01。泽云无需转换。
        if (this.isNotNull_(map,"tpPublishDate")) {
            propInfoList.add(new String[] { "1005", this.getValue_(map,"tpPublishDate") });// 出版日期
        }else{
            propInfoList.add(new String[] { "1005",""});
        }

        if (this.isNotNull_(map,"tpEBookSize")) {
            propInfoList.add(new String[] { "1030", this.getValue_(map,"tpEBookSize") });// 文件大小
        }else{
            propInfoList.add(new String[] { "1030",""});
        }

        if (this.isNotNull_(map,"tpIsAcademic")) {
            propInfoList.add(new String[] { "1031", this.getValue_(map,"tpIsAcademic") });// 是否提供试读//单选
        }else{
            propInfoList.add(new String[] { "1031",""});
        }
        
        //出版社写死
        propInfoList.add(new String[] { "1006",PmphGdsDataImportConstants.Commons.RENWEI_NAME});// 出版社
        
        if (this.isNotNull_(map,"tpEBookIntro")) {
            propInfoList.add(new String[] { "1020",
                    this.saveToMongoDB(this.getValue_(map,"tpEBookIntro"), "tpEBookIntro") });// 内容简介//富文本
        }else{
            propInfoList.add(new String[] { "1020",""});
        }

        return propInfoList;

    }
    
    @SuppressWarnings("rawtypes")
    private List<String[]> createUpdatePropInfoList(Map map,List<String> list) throws BusinessException {

        List<String[]> propInfoList = new ArrayList<String[]>();

        if(!list.contains("1001")){
            if (this.isNotNull_(map,"tpAuthorName")) {
                
                //作者名称中空格出现乱码解决"  "：谢  幸  苟文丽--->谢? 幸? 苟文丽
                String tpAuthorName=this.getValue_(map,"tpAuthorName").replaceAll(" ","");
                propInfoList.add(new String[] { "1001",tpAuthorName});// 作者
            }else{
                propInfoList.add(new String[] { "1001",""});
            }
        }
        
        /*-----------------------ISBN---------------------------*/
        if(!list.contains("1032")){
            //***电子书ISBN==EISBN***
            //***电子书ISBN==电子书标准ISBN==纸质书标准ISBN***
            if (this.isNotNull_(map,"tpISBN")) {
                String tpISBN=this.getValue_(map,"tpISBN");
                
                //电子书标准ISBN和ISBN一样
                propInfoList.add(new String[] { "1032",tpISBN});// 标准ISBN//7-117-03573-0
                
            }else{
                propInfoList.add(new String[] { "1032",""});
            }
        }
        
        if(!list.contains("1002")){// ISBN//7-117-03573-0
            if (this.isNotNull_(map,"tpISBN")) {
                String isbn=this.getValue_(map,"tpISBN");
                propInfoList.add(new String[] { "1002",isbn});
            }else{
                propInfoList.add(new String[] { "1002",""});
            }
        }
        
        if(!list.contains("1004")){// 五位ISBN号
            if (this.isNotNull_(map,"tpISBN")) {
                //5位ISBN号处理，取倒数第二段
                //7-117-06305-X--->06305
                //978-7-117-13899-5--->13899
                String tpISBN=this.getValue_(map,"tpISBN");
                String arr[]=tpISBN.split("-");
                if(arr!=null&&arr.length>=2){
                    tpISBN=arr[arr.length-2];
                }
                propInfoList.add(new String[] { "1004",tpISBN});
            }else{
                propInfoList.add(new String[] { "1004",""});
            }
        }

        if(!list.contains("1003")){
            if (this.isNotNull_(map,"tpEISBN")) {
                propInfoList.add(new String[] { "1003", this.getValue_(map,"tpEISBN") });// EISBN
            }else{
                propInfoList.add(new String[] { "1003",""});
            }
        }
        
        /*-----------------------ISBN---------------------------*/
        if(!list.contains("1005")){
            // 出版日期日期格式：2012-07-01。泽云无需转换。
            if (this.isNotNull_(map,"tpPublishDate")) {
                propInfoList.add(new String[] { "1005", this.getValue_(map,"tpPublishDate") });// 出版日期
            }else{
                propInfoList.add(new String[] { "1005",""});
            }
        }

        if(!list.contains("1030")){
            if (this.isNotNull_(map,"tpEBookSize")) {
                propInfoList.add(new String[] { "1030", this.getValue_(map,"tpEBookSize") });// 文件大小
            }else{
                propInfoList.add(new String[] { "1030",""});
            }
        }

        if(!list.contains("1031")){
            if (this.isNotNull_(map,"tpIsAcademic")) {
                propInfoList.add(new String[] { "1031", this.getValue_(map,"tpIsAcademic") });// 是否提供试读//单选
            }else{
                propInfoList.add(new String[] { "1031",""});
            }
        }
        
        //出版社写死
        //propInfoList.add(new String[] { "1006",PmphGdsDataImportConstants.Commons.RENWEI_NAME});// 出版社
        
        if(!list.contains("1020")){
            if (this.isNotNull_(map,"tpEBookIntro")) {
                propInfoList.add(new String[] { "1020",
                        this.saveToMongoDB(this.getValue_(map,"tpEBookIntro"), "tpEBookIntro") });// 内容简介//富文本
            }else{
                propInfoList.add(new String[] { "1020",""});
            }
        }

        return propInfoList;

    }

    @SuppressWarnings("rawtypes")
    private GdsInfoReqDTO addCategoryInfo(Map map, GdsInfoReqDTO gdsInfoReqDTO)
            throws BusinessException {

        String mainCatgs = this.getValue_(map,KEY_TPGOODTYPEID);
        
        GdsCatgSyncRespDTO gdsCatgSyncRespDTO=null;
        
        if(StringUtils.isNotBlank(mainCatgs)){
            
            // 分类映射关系查询
        	GdsCatgSyncReqDTO gdsCatgSyncReqDTO=new GdsCatgSyncReqDTO();
        	gdsCatgSyncReqDTO.setCatgCode(mainCatgs);
        	gdsCatgSyncReqDTO.setSrcSystem(PmphGdsDataImportConstants.SrcSystem.ZY_01);
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
        	String tpType = this.getValue_(map,KEY_TPTYPE);
            if (StringUtils.equals(tpType, "1")) {// 电子书其它分类
            	ecpCatgs = PmphGdsDataImportConstants.Commons.CATEGORY_EBOOK_OTHER_ID;
            } else if (StringUtils.equals(tpType, "2")) {// 数字教材其它分类
            	ecpCatgs = PmphGdsDataImportConstants.Commons.CATEGORY_DBOOK_OTHER_ID;
            } else {
                
                //数字产品没有其它分类
                throw new BusinessException(
                        PmphGdsDataImportErrorConstants.ZY.ERROR_GOODS_DATAIMPORT_ZY_5,
                        new String[] { map.toString() });
            }
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
        
        //ISBN字段
        String isbn="";
        if (!list.contains("isbn") &&this.isNotNull_(map,"tpISBN")) {
            isbn=this.getValue_(map,"tpISBN");
            
            //去除前后空格，防止后面解析出错
            isbn=isbn.trim();
            
            gdsInfoReqDTO.setIsbn(isbn);
        }

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
        if (!list.contains("gdsDesc") && this.isNotNull_(map,"tpEBookIntro")) {
            gdsInfoReqDTO.setGdsDesc(this.updateMongoDB(this.getValue_(map,"tpEBookIntro"),
                    gdsInfoReqDTO.getGdsDesc(), "gdsDesc"));
        }

        // 属性信息映射
        List<String[]> propInfoList = this.createUpdatePropInfoList(map,list);

        // 设置商品属性关系
        gdsInfoAddReqDTO.setGds2PropReqDTOs(this.createGdsGds2PropReqDTOList(propInfoList, list));

        // 设置单品列表
        List<GdsSkuInfoReqDTO> skuInfoReqDTOs = new ArrayList<GdsSkuInfoReqDTO>();
        GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
        
        if(StringUtils.isNotBlank(isbn)){
            gdsSkuInfoReqDTO.setIsbn(isbn);
        }

        if (!list.contains("gdsPrice") && this.isNotNull_(map,"tpPrice")) {

            // 单品价格关系设置
//            List<GdsSku2PriceReqDTO> sku2PriceReqDTOs = new ArrayList<GdsSku2PriceReqDTO>();
//            GdsSku2PriceReqDTO gdsSku2PriceReqDTO = new GdsSku2PriceReqDTO();
//            gdsSku2PriceReqDTO.setPriceTypeCode(GdsConstants.GdsInfo.SKU_PRICE_TYPE_ORDINARY);
//            GdsPriceReqDTO gdsPriceReqDTO = new GdsPriceReqDTO();
            Long tpPrice = (long) (Float.parseFloat(this.getValue_(map,"tpPrice")) * 100);
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
