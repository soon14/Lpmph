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
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IERPGdsInfoImportSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Project Name:ecp-services-goods <br>
 * Description: ERP图书信息导入<br>
 * Date:2015年10月24日上午10:32:22 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ERPGdsInfoImportSVImpl extends BaseGdsInfoImportSV implements IERPGdsInfoImportSV {

    /**
     * 上架标志KEY，正常[0] 下架[1]
     */
    public final static String KEY_SHANGJIABIAOZHI = "shangjiabiaozhi";

    /**
     * 操作模式KEY，增加[1]删除[2]修改[3]
     */
    public final static String KEY_XINXICAOZUO = "xinxicaozuo";

    @SuppressWarnings("rawtypes")
    @Override
    protected void addValidation(Map map) throws BusinessException {
        super.addValidation(map);
        
        //上架标志为空，直接设为为待上架
//        if (!this.isNotNull_(KEY_SHANGJIABIAOZHI)) {
//            throw new BusinessException(
//                    PmphGdsDataImportErrorConstants.Commons.ERROR_GOODS_DATAIMPORT_7,
//                    new String[] { map.toString() });
//        }
        if (!this.isNotNull_(map,KEY_XINXICAOZUO)) {
            throw new BusinessException(
                    PmphGdsDataImportErrorConstants.Commons.ERROR_PMPH_GOODS_DATAIMPORT_1,
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
    
    @Override
    protected String getSrcGdsCodePrefix() throws BusinessException {
        return "";
    }
    
//    @Override
//    protected String getSrcGdsCatgCodePrefix() throws BusinessException {
//        return "";
//    }

    @SuppressWarnings("rawtypes")
    @Override
    protected EActionType doActionType(Map map) throws BusinessException {

        String xinxicaozuo = this.getValue_(map,KEY_XINXICAOZUO);

        // 增加
        if (StringUtils.equals(xinxicaozuo, "1")) {
            return EActionType.ADD;
        } else if (StringUtils.equals(xinxicaozuo, "2")) {// 删除
            return EActionType.DELETE;
        } else if (StringUtils.equals(xinxicaozuo, "3")) {// 修改
            return EActionType.UPDATE;
        }

        throw new BusinessException(PmphGdsDataImportErrorConstants.Commons.ERROR_PMPH_GOODS_DATAIMPORT_2,
                new String[] { xinxicaozuo });
    }

    @Override
    protected String getOriginType() throws BusinessException {
        return PmphGdsDataImportConstants.Commons.ORIGIN_ERP;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected String getGdsStatus(Map map, List<String> list) throws BusinessException {
        String shangjiabiaozhi = this.getValue_(map,KEY_SHANGJIABIAOZHI);
        String shoushuriqi = this.getValue_(map,"shoushuriqi");

        String gdsStatus = "";

        // 出版日期为空则设为待上架
 /*       if (StringUtils.isBlank(shoushuriqi)) {
            gdsStatus = GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES;
        } else {*/

//            Long zhipindingjia = 0L;
//            boolean flag = true;
//            if (this.realActionType.getActionType() == EActionType.ADD.getActionType()) {
//                if (this.isNotNull_(map,"zhipindingjia")) {
//                    zhipindingjia = (long) (Float.parseFloat(this.getValue_(map,"zhipindingjia")) * 100);
//                }
//                if (zhipindingjia <= 0) {
//                    flag = false;
//                }
//            } else if (this.realActionType.getActionType() == EActionType.UPDATE.getActionType()) {
//                if (!list.contains("zhipindingjia") && this.isNotNull_(map,"zhipindingjia")) {
//                    zhipindingjia = (long) (Float.parseFloat(this.getValue_(map,"zhipindingjia")) * 100);
//                    if (zhipindingjia <= 0) {
//                        flag = false;
//                    }
//                }
//            }
            
            Long zhipindingjia = 0L;
            boolean flag = true;
            if (this.isNotNull_(map,"zhipindingjia")) {
                zhipindingjia = (long) (Float.parseFloat(this.getValue_(map,"zhipindingjia")) * 100);
            }
            if (zhipindingjia <= 0) {
                flag = false;
            }

            if (flag) {

                // 正常
                if (StringUtils.equals(shangjiabiaozhi, "0")) {
                    gdsStatus = GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES;
                } else if (StringUtils.equals(shangjiabiaozhi, "1")) {// 下架
                    gdsStatus = GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES;
                } else {
                    
                    //上架标志为空，或上架标志不合法，直接设为下架
                    gdsStatus = GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES;
//                    throw new BusinessException(
//                            PmphGdsDataImportErrorConstants.Commons.ERROR_GOODS_DATAIMPORT_11,
//                            new String[] { shangjiabiaozhi });
                }
            } else {
                gdsStatus = GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES;
            }

  //      }

        return gdsStatus;
    }

    @Override
    protected String getPkKeyName() throws BusinessException {
        return "benbanbianhao";
    }
    
    @Override
    protected String getGdsNameKeyName() throws BusinessException {
        return "zhipinmingcheng";
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
        if (this.isNotNull_(map,"biaozhunshuhao")) {
            isbn=this.getValue_(map,"biaozhunshuhao");
            
            //去除前后空格，防止后面解析出错
            isbn=isbn.trim();
            
            gdsInfoReqDTO.setIsbn(isbn);
        }
        

        // 查询条件
//        List<Long> catalogIds = new ArrayList<Long>();
//        catalogIds.add(PmphGdsDataImportConstants.Commons.CATLOG_RENWEI);
//        gdsInfoReqDTO.setCatalogIds(catalogIds);
        
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

        // if(this.isNotNull_(map,"zhipindingjia")){
        // Long guidePrice = (long) (Float.parseFloat(this.getValue_(map,"zhipindingjia")) *
        // 100);
        // gdsInfoReqDTO.setGuidePrice(guidePrice);
        // }

        // 描述内容上传MongoDB，记录Id
        if (this.isNotNull_(map,"neirongtiyao")) {
            gdsInfoReqDTO.setGdsDesc(this.saveToMongoDB(this.getValue_(map,"neirongtiyao"), "gdsDesc"));
        }

        // gdsInfoReqDTO.setGdsPartlist(gdsPartlist);
        
        // 商品类型设置。针对映射为平台虚拟卡分类的商品进行特殊处理。
        setupGdsType(gdsInfoReqDTO);
        
        
        
       // gdsInfoReqDTO.setGdsTypeId(PmphGdsDataImportConstants.Commons.GOODTYPE_ORDINARY_ID);// 实物商品
       // gdsInfoReqDTO.setGdsTypeCode(PmphGdsDataImportConstants.Commons.GOODTYPE_ORDINARY_CODE);// 实物商品
//        gdsInfoReqDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES);
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
        Long zhipindingjia = 0L;
        if (this.isNotNull_(map,"zhipindingjia")) {
            zhipindingjia = (long) (Float.parseFloat(this.getValue_(map,"zhipindingjia")) * 100);
        }
        gdsPriceReqDTO.setPrice(zhipindingjia);
        gdsSku2PriceReqDTO.setPrice(gdsPriceReqDTO);
        sku2PriceReqDTOs.add(gdsSku2PriceReqDTO);
        
        gdsInfoReqDTO.setGuidePrice(zhipindingjia);

        // 设置单品价格关系到商品
        // gdsInfoAddReqDTO.setSku2PriceReqDTOs(sku2PriceReqDTOs);

        // 设置单品列表
        List<GdsSkuInfoReqDTO> skuInfoReqDTOs = new ArrayList<GdsSkuInfoReqDTO>();
        GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
        gdsSkuInfoReqDTO.setCommonPrice(zhipindingjia);
        // 设置单品价格关系到单品
//        gdsSkuInfoReqDTO.setSku2PriceReqDTOs(sku2PriceReqDTOs);
        // 设置单品属性关系//直接从商品属性关系复制
        gdsSkuInfoReqDTO.setGdsProps(this.createGdsSku2PropReqDTOList(gdsInfoAddReqDTO
                .getGds2PropReqDTOs()));
        if(StringUtil.isNotBlank(isbn)){
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

    private void setupGdsType(GdsInfoReqDTO gdsInfoReqDTO) {
        if(PmphGdsDataImportConstants.Commons.VIRTUAL_CARD_CATGCODE.equals(gdsInfoReqDTO.getMainCatgs())){
            gdsInfoReqDTO.setGdsTypeId(PmphGdsDataImportConstants.Commons.GOODTYPE_VIRTUAL_CARD_ID);// 虚拟卡商品ID
            gdsInfoReqDTO.setGdsTypeCode(PmphGdsDataImportConstants.Commons.GOODTYPE_VIRTUAL_CODE);// 虚拟商品
        }else{
            gdsInfoReqDTO.setGdsTypeId(PmphGdsDataImportConstants.Commons.GOODTYPE_ORDINARY_ID);// 实物商品
            gdsInfoReqDTO.setGdsTypeCode(PmphGdsDataImportConstants.Commons.GOODTYPE_ORDINARY_CODE);// 实物商品
        }
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    private List<String[]> createAddPropInfoList(Map map) throws BusinessException {

        List<String[]> propInfoList = new ArrayList<String[]>();

        if (this.isNotNull_(map,"zuozhemingcheng")) {
            propInfoList.add(new String[] { "1001", this.getValue_(map,"zuozhemingcheng") });// 作者
        }else{
            propInfoList.add(new String[] { "1001",""});
        }
        
        /*-----------------------ISBN---------------------------*/
        if (this.isNotNull_(map,"biaozhunshuhao")) {
            
            //如果数据源ISBN有问题，如：ISBN 7-117-03573-0或7-117-03573-0/R·3574或7-117-03573-0，
            //暂时不做处理，ISBN字段也直接存入格式有问题的数据来源
            String biaozhunshuhao=this.getValue_(map,"biaozhunshuhao");
            
            //去除前后空格，防止后面解析出错
            biaozhunshuhao=biaozhunshuhao.trim();
            
            //ISBN格式：ISBN 7-117-03573-0/R·3574或ISRC CN-M22-10-0008-0/V·R或。。。。
            propInfoList.add(new String[] { "1002",biaozhunshuhao});
            
            //标准ISBN号处理，ISBN 7-117-03573-0/R·3574--->7-117-03573-0
            if(biaozhunshuhao.contains(" ")){
                biaozhunshuhao=biaozhunshuhao.substring(biaozhunshuhao.indexOf(' ')+1, biaozhunshuhao.length());
            }
            if(biaozhunshuhao.contains("/")){
                biaozhunshuhao=biaozhunshuhao.substring(0, biaozhunshuhao.lastIndexOf('/'));
            }
            propInfoList.add(new String[] { "1032",biaozhunshuhao});// 标准ISBN格式：7-117-03573-0
        }else{
            propInfoList.add(new String[] { "1002",""});
            propInfoList.add(new String[] { "1032",""});
        }
        
        // 纸质书没有EISBN
        propInfoList.add(new String[] { "1003",""});// EISBN

        if (this.isNotNull_(map,"zhipinshuhao")) {
            propInfoList.add(new String[] { "1004", this.getValue_(map,"zhipinshuhao") });// 五位ISBN号
        }else{
            propInfoList.add(new String[] { "1004",""});
        }
        
        /*-----------------------ISBN---------------------------*/

        // 出版日期日期格式：2012-07-01。ERP需转换。
        if (this.isNotNull_(map,"shoushuriqi")) {
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            
            Object value = map.get("shoushuriqi");
            if (value==null) {
                value=map.get("SHOUSHURIQI");;
            }
            
            //ERP 表输入日期类型
            if(value instanceof Timestamp){
                Timestamp timestamp=(Timestamp)value;
                propInfoList.add(new String[] { "1005",
                        formatter.format(timestamp)});// 出版日期
            }else{//Excel输入日期类型当文本处理
                propInfoList.add(new String[] { "1005",
                        formatter.format(new Date(this.getValue_(map,"shoushuriqi"))) });// 出版日期
            }
            
        }else{
            
            //后台查询需要，出版日期属性为空时属性仍然需要创建
            propInfoList.add(new String[] { "1005",""});// 出版日期
            
        }

//        if (this.isNotNull_(map,"yuanshuchubandanwei")) {
//            propInfoList.add(new String[] { "1006", this.getValue_(map,"yuanshuchubandanwei") });// 出版社
//        }
        //出版社写死
        propInfoList.add(new String[] { "1006",PmphGdsDataImportConstants.Commons.RENWEI_NAME});// 出版社
        
        if (this.isNotNull_(map,"duzheduixiang")) {
            propInfoList.add(new String[] { "1007", this.getValue_(map,"duzheduixiang") });// 读者对象
        }else{
            propInfoList.add(new String[] { "1007",""});
        }

        if (this.isNotNull_(map,"benbanbianhao")) {
            propInfoList.add(new String[] { "1008", this.getValue_(map,"benbanbianhao") });// 本版编号
        }else{
            propInfoList.add(new String[] { "1008",""});
        }

        if (this.isNotNull_(map,"shoubanbianhao")) {
            propInfoList.add(new String[] { "1009", this.getValue_(map,"shoubanbianhao") });// 首版编号
        }else{
            propInfoList.add(new String[] { "1009",""});
        }

        if (this.isNotNull_(map,"zhipinbanci")) {
            propInfoList.add(new String[] { "1010", this.getValue_(map,"zhipinbanci") });// 版次
        }else{
            propInfoList.add(new String[] { "1010",""});
        }

        propInfoList.add(new String[] { "1011",""});// 规格

        if (this.isNotNull_(map,"zishu")) {
            propInfoList.add(new String[] { "1012", this.getValue_(map,"zishu") });// 字数
        }else{
            propInfoList.add(new String[] { "1012",""});
        }

        if (this.isNotNull_(map,"peipanshu")) {
            propInfoList.add(new String[] { "1013", this.getValue_(map,"peipanshu") });// 配盘数
        }else{
            propInfoList.add(new String[] { "1013",""});
        }

        if (this.isNotNull_(map,"kaiben")) {
            propInfoList.add(new String[] { "1014", this.getValue_(map,"kaiben") });// 开本规格
        }else{
            propInfoList.add(new String[] { "1014",""});
        }

        if (this.isNotNull_(map,"yinzhang")) {
            propInfoList.add(new String[] { "1015", this.getValue_(map,"yinzhang") });// 印张
        }else{
            propInfoList.add(new String[] { "1015",""});
        }

        if (this.isNotNull_(map,"zhuangzheng")) {
            propInfoList.add(new String[] { "1016", this.getValue_(map,"zhuangzheng") });// 装帧
        }else{
            propInfoList.add(new String[] { "1016",""});
        }

        if (this.isNotNull_(map,"wenzhong")) {
            propInfoList.add(new String[] { "1017", this.getValue_(map,"wenzhong") });// 语言
        }else{
            propInfoList.add(new String[] { "1017",""});
        }

        if (this.isNotNull_(map,"zhuyifangshi")) {
            propInfoList.add(new String[] { "1018", this.getValue_(map,"zhuyifangshi") });// 著译方式
        }else{
            propInfoList.add(new String[] { "1018",""});
        }

        if (this.isNotNull_(map,"yuanshumingcheng")) {
            propInfoList.add(new String[] { "1019", this.getValue_(map,"yuanshumingcheng") });// 原书信息
        }else{
            propInfoList.add(new String[] { "1019",""});
        }

        if (this.isNotNull_(map,"neirongtiyao")) {
            propInfoList.add(new String[] { "1020",
                    this.saveToMongoDB(this.getValue_(map,"neirongtiyao"), "neirongtiyao") });// 内容简介//富文本
        }else{
            propInfoList.add(new String[] { "1020",""});
        }

//        propInfoList.add(new String[] { "1021",""});//目录//富文本

        if (this.isNotNull_(map,"zuozheziliao")) {
            propInfoList.add(new String[] { "1022",
                    this.saveToMongoDB(this.getValue_(map,"zuozheziliao"), "zuozheziliao") });// 作者介绍//富文本
        }else{
            propInfoList.add(new String[] { "1022",""});
        }

        //添加和编辑商品这几个找不到富文本对应的字段，不设置关系。如果设置了空值，则更新的时候会导致没有值的字段为空了，从而无法做到几个富文本的局部更新。
        //其它普通属性不支持局部更新，因此如果值为空，也需要设置关系。
//        propInfoList.add(new String[] { "1023",""});//编辑推荐//富文本
//
//        propInfoList.add(new String[] { "1024",""});//专家推荐//富文本
//
//        propInfoList.add(new String[] { "1025",""});//章节节选//富文本
//
//        propInfoList.add(new String[] { "1026", ""});// 在线试读PDF//文件

        if(this.isNotNull_(map,"shifouzengzhi")){
            propInfoList.add(new String[] { "1027", this.getValue_(map,"shifouzengzhi")});// 是否有网络增值服务
        }else{
            propInfoList.add(new String[] { "1027",""});
        }
        
        if(this.isNotNull_(map,"tiaoxingmahao")){
            propInfoList.add(new String[] { "1050", this.getValue_(map,"tiaoxingmahao")});// 条形码
        }else{
            propInfoList.add(new String[] { "1050",""});
        }

        propInfoList.add(new String[] { "1028",""});// 是否有数字印刷

        propInfoList.add(new String[] { "1029",""});// 数字印刷定价

        return propInfoList;

    }
    
    @SuppressWarnings({ "rawtypes", "deprecation" })
    private List<String[]> createUpdatePropInfoList(Map map,List<String> list) throws BusinessException {

        List<String[]> propInfoList = new ArrayList<String[]>();

        //属性没被修改过，才允许覆盖
        if(!list.contains("1001")){
            if (this.isNotNull_(map,"zuozhemingcheng")) {
                propInfoList.add(new String[] { "1001", this.getValue_(map,"zuozhemingcheng") });// 作者
            }else{
                propInfoList.add(new String[] { "1001",""});
            }
        }
        
        /*-----------------------ISBN---------------------------*/
        if(!list.contains("1002")){
            if (this.isNotNull_(map,"biaozhunshuhao")) {
                
                //如果数据源ISBN有问题，如：ISBN 7-117-03573-0或7-117-03573-0/R·3574或7-117-03573-0，
                //暂时不做处理，ISBN字段也直接存入格式有问题的数据来源
                String biaozhunshuhao=this.getValue_(map,"biaozhunshuhao");
                
                //去除前后空格，防止后面解析出错
                biaozhunshuhao=biaozhunshuhao.trim();
                
                //ISBN格式：ISBN 7-117-03573-0/R·3574或ISRC CN-M22-10-0008-0/V·R或。。。。
                propInfoList.add(new String[] { "1002",biaozhunshuhao});
                
            }else{
                propInfoList.add(new String[] { "1002",""});
            }
        }
        if(!list.contains("1032")){
            
            if (this.isNotNull_(map,"biaozhunshuhao")) {
                String biaozhunshuhao=this.getValue_(map,"biaozhunshuhao");
                
                //去除前后空格，防止后面解析出错
                biaozhunshuhao=biaozhunshuhao.trim();
                
                String standardIsbn=biaozhunshuhao;
                
                //标准ISBN号处理，ISBN 7-117-03573-0/R·3574--->7-117-03573-0
                if(standardIsbn.contains(" ")){
                    standardIsbn=standardIsbn.substring(standardIsbn.indexOf(' ')+1, standardIsbn.length());
                }
                if(standardIsbn.contains("/")){
                    standardIsbn=standardIsbn.substring(0, standardIsbn.lastIndexOf('/'));
                }
                
                propInfoList.add(new String[] { "1032",standardIsbn});// 标准ISBN格式：7-117-03573-0
            }else{
                propInfoList.add(new String[] { "1032",""});
            }
            
        }
        
        // 纸质书没有EISBN
        // propInfoList.add(new String[] { "1003",""});// EISBN

        if(!list.contains("1004")){
            if (this.isNotNull_(map,"zhipinshuhao")) {
                propInfoList.add(new String[] { "1004", this.getValue_(map,"zhipinshuhao") });// 五位ISBN号
            }else{
                propInfoList.add(new String[] { "1004",""});
            }
        }
        
        /*-----------------------ISBN---------------------------*/

        if(!list.contains("1005")){
            // 出版日期日期格式：2012-07-01。ERP需转换。
            if (this.isNotNull_(map,"shoushuriqi")) {
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                
                Object value = map.get("shoushuriqi");
                if (value==null) {
                    value=map.get("SHOUSHURIQI");;
                }
                
                //ERP 表输入日期类型
                if(value instanceof Timestamp){
                    Timestamp timestamp=(Timestamp)value;
                    propInfoList.add(new String[] { "1005",
                            formatter.format(timestamp)});// 出版日期
                }else{//Excel输入日期类型当文本处理
                    propInfoList.add(new String[] { "1005",
                            formatter.format(new Date(this.getValue_(map,"shoushuriqi"))) });// 出版日期
                }
                
            }else{
                
                //后台查询需要，出版日期属性为空时属性仍然需要创建
                propInfoList.add(new String[] { "1005",""});// 出版日期
                
            }
        }

//        if (this.isNotNull_(map,"yuanshuchubandanwei")) {
//            propInfoList.add(new String[] { "1006", this.getValue_(map,"yuanshuchubandanwei") });// 出版社
//        }
        //出版社写死
        //propInfoList.add(new String[] { "1006",PmphGdsDataImportConstants.Commons.RENWEI_NAME});// 出版社
        
        if(!list.contains("1007")){
            if (this.isNotNull_(map,"duzheduixiang")) {
                propInfoList.add(new String[] { "1007", this.getValue_(map,"duzheduixiang") });// 读者对象
            }else{
                propInfoList.add(new String[] { "1007",""});
            }
        }

        if(!list.contains("1008")){
            if (this.isNotNull_(map,"benbanbianhao")) {
                propInfoList.add(new String[] { "1008", this.getValue_(map,"benbanbianhao") });// 本版编号
            }else{
                propInfoList.add(new String[] { "1008",""});
            }
        }

        if(!list.contains("1009")){
            if (this.isNotNull_(map,"shoubanbianhao")) {
                propInfoList.add(new String[] { "1009", this.getValue_(map,"shoubanbianhao") });// 首版编号
            }else{
                propInfoList.add(new String[] { "1009",""});
            }
        }

        if(!list.contains("1010")){
            if (this.isNotNull_(map,"zhipinbanci")) {
                propInfoList.add(new String[] { "1010", this.getValue_(map,"zhipinbanci") });// 版次
            }else{
                propInfoList.add(new String[] { "1010",""});
            }
        }

        //propInfoList.add(new String[] { "1011",""});// 规格

        if(!list.contains("1012")){
            if (this.isNotNull_(map,"zishu")) {
                propInfoList.add(new String[] { "1012", this.getValue_(map,"zishu") });// 字数
            }else{
                propInfoList.add(new String[] { "1012",""});
            }
        }
        
        if(!list.contains("1013")){
            if (this.isNotNull_(map,"peipanshu")) {
                propInfoList.add(new String[] { "1013", this.getValue_(map,"peipanshu") });// 配盘数
            }else{
                propInfoList.add(new String[] { "1013",""});
            }
        }

        if(!list.contains("1014")){
            if (this.isNotNull_(map,"kaiben")) {
                propInfoList.add(new String[] { "1014", this.getValue_(map,"kaiben") });// 开本规格
            }else{
                propInfoList.add(new String[] { "1014",""});
            }
        }

        if(!list.contains("1015")){
            if (this.isNotNull_(map,"yinzhang")) {
                propInfoList.add(new String[] { "1015", this.getValue_(map,"yinzhang") });// 印张
            }else{
                propInfoList.add(new String[] { "1015",""});
            }
        }

        if(!list.contains("1016")){
            if (this.isNotNull_(map,"zhuangzheng")) {
                propInfoList.add(new String[] { "1016", this.getValue_(map,"zhuangzheng") });// 装帧
            }else{
                propInfoList.add(new String[] { "1016",""});
            }
        }

        if(!list.contains("1017")){
            if (this.isNotNull_(map,"wenzhong")) {
                propInfoList.add(new String[] { "1017", this.getValue_(map,"wenzhong") });// 语言
            }else{
                propInfoList.add(new String[] { "1017",""});
            }
        }

        if(!list.contains("1018")){
            if (this.isNotNull_(map,"zhuyifangshi")) {
                propInfoList.add(new String[] { "1018", this.getValue_(map,"zhuyifangshi") });// 著译方式
            }else{
                propInfoList.add(new String[] { "1018",""});
            }
        }

        if(!list.contains("1019")){
            if (this.isNotNull_(map,"yuanshumingcheng")) {
                propInfoList.add(new String[] { "1019", this.getValue_(map,"yuanshumingcheng") });// 原书信息
            }else{
                propInfoList.add(new String[] { "1019",""});
            }
        }

        if(!list.contains("1020")){
            if (this.isNotNull_(map,"neirongtiyao")) {
                propInfoList.add(new String[] { "1020",
                        this.saveToMongoDB(this.getValue_(map,"neirongtiyao"), "neirongtiyao") });// 内容简介//富文本
            }else{
                propInfoList.add(new String[] { "1020",""});
            }
        }

//        propInfoList.add(new String[] { "1021",""});//目录//富文本

        if(!list.contains("1022")){
            if (this.isNotNull_(map,"zuozheziliao")) {
                propInfoList.add(new String[] { "1022",
                        this.saveToMongoDB(this.getValue_(map,"zuozheziliao"), "zuozheziliao") });// 作者介绍//富文本
            }else{
                propInfoList.add(new String[] { "1022",""});
            }
        }

        //添加和编辑商品这几个找不到富文本对应的字段，不设置关系。如果设置了空值，则更新的时候会导致没有值的字段为空了，从而无法做到几个富文本的局部更新。
        //其它普通属性不支持局部更新，因此如果值为空，也需要设置关系。
//        propInfoList.add(new String[] { "1023",""});//编辑推荐//富文本
//
//        propInfoList.add(new String[] { "1024",""});//专家推荐//富文本
//
//        propInfoList.add(new String[] { "1025",""});//章节节选//富文本
//
        //propInfoList.add(new String[] { "1026", ""});// 在线试读PDF//文件

        if(!list.contains("1027")){
            if(this.isNotNull_(map,"shifouzengzhi")){
                propInfoList.add(new String[] { "1027", this.getValue_(map,"shifouzengzhi")});// 是否有网络增值服务
            }else{
                propInfoList.add(new String[] { "1027",""});
            }
        }
        
        if(!list.contains("1050")){
            if(this.isNotNull_(map,"tiaoxingmahao")){
                propInfoList.add(new String[] { "1050", this.getValue_(map,"tiaoxingmahao")});// 条形码
            }else{
                propInfoList.add(new String[] { "1050",""});
            }
        }

        //propInfoList.add(new String[] { "1028",""});// 是否有数字印刷

        //propInfoList.add(new String[] { "1029",""});// 数字印刷定价

        return propInfoList;

    }

    @SuppressWarnings("rawtypes")
    private GdsInfoReqDTO addCategoryInfo(Map map, GdsInfoReqDTO gdsInfoReqDTO)
            throws BusinessException {

        String zhipinyijifenleibianhao = this.getValue_(map,"zhipinyijifenleibianhao");
        
        String zhipinerjifenleibianhao = this.getValue_(map,"zhipinerjifenleibianhao");
        String zhipinerjifenlei = this.getValue_(map,"zhipinerjifenlei");
        
        String zhipinsanjifenleibianhao = this.getValue_(map,"zhipinsanjifenleibianhao");
        String zhipinsanjifenlei = this.getValue_(map,"zhipinsanjifenlei");
        
        String zhipinsijifenleibianhao = this.getValue_(map,"zhipinsijifenleibianhao");
        String zhipinsijifenlei = this.getValue_(map,"zhipinsijifenlei");

        String mainCatgs = "";

        // 从四级分类开始，逐级往上找
        // 跳过分类名称为"无"的三四级分类
        if (StringUtils.isNotBlank(zhipinsijifenleibianhao)&&!StringUtils.equals("无", zhipinsijifenlei)) {
            mainCatgs = zhipinsijifenleibianhao;
        } else if (StringUtils.isNotBlank(zhipinsanjifenleibianhao)&&!StringUtils.equals("无", zhipinsanjifenlei)) {
            mainCatgs = zhipinsanjifenleibianhao;
        } else if (StringUtils.isNotBlank(zhipinerjifenleibianhao)&&!StringUtils.equals("无", zhipinerjifenlei)) {
            mainCatgs = zhipinerjifenleibianhao;
        } else if (StringUtils.isNotBlank(zhipinyijifenleibianhao)) {
            mainCatgs = zhipinyijifenleibianhao;
        }

        GdsCatgSyncRespDTO gdsCatgSyncRespDTO=null;
        
        if(StringUtils.isNotBlank(mainCatgs)){
            
            // 分类映射关系查询
//            GdsInterfaceCatgReqDTO reqDTO = new GdsInterfaceCatgReqDTO();
//            reqDTO.setOriginCatgCode(mainCatgs);
//            reqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
//            gdsCategoryRespDTO = this.gdsCategorySyncSV
//                    .queryCategoryByOriginCatgCode(reqDTO);
        	
        	GdsCatgSyncReqDTO gdsCatgSyncReqDTO=new GdsCatgSyncReqDTO();
        	gdsCatgSyncReqDTO.setCatgCode(mainCatgs);
        	gdsCatgSyncReqDTO.setSrcSystem(PmphGdsDataImportConstants.SrcSystem.ERP_01);
        	try {
				gdsCatgSyncRespDTO=this.gdsCategorySyncSV.queryGdsCategorySyncByPK(gdsCatgSyncReqDTO);
			} catch (BusinessException e) {
			    LogUtil.error(this.getClass().getName(), "",e);
			    //BusinessException异常不中断
            } catch (Exception e) {
                LogUtil.error(this.getClass().getName(), "",e);
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
                    LogUtil.error(this.getClass().getName(), "",e);
        		}
        		
            }
        	
        } 
        
        if(StringUtils.isBlank(ecpCatgs)){
        	// ERP系统来源统一设置为，纸质书的其它分类
        	ecpCatgs = PmphGdsDataImportConstants.Commons.CATEGORY_BOOK_OTHER_ID;
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

//        gdsInfoReqDTO.setGdsTypeId(PmphGdsDataImportConstants.Commons.GOODTYPE_ORDINARY_ID);// 实物商品
//        gdsInfoReqDTO.setGdsTypeCode(PmphGdsDataImportConstants.Commons.GOODTYPE_ORDINARY_CODE);// 实物商品
//        gdsInfoReqDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES);
        gdsInfoReqDTO.setShopId(PmphGdsDataImportConstants.Commons.SHOP_ID);
        gdsInfoReqDTO.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);

        // 设置公司编码
        gdsInfoAddReqDTO.setCompanyId(PmphGdsDataImportConstants.Commons.COMPANY_ID);

        // =====================新增的时候写死的信息有些更新的时候还是需要用到end=====================
        
        //ISBN字段
        String isbn="";
        if (!list.contains("isbn") &&this.isNotNull_(map,"biaozhunshuhao")) {
            isbn=this.getValue_(map,"biaozhunshuhao");
            
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
        
        setupGdsType(gdsInfoReqDTO);

        // 描述内容上传MongoDB，记录Id
        if (!list.contains("gdsDesc") && this.isNotNull_(map,"neirongtiyao")) {
            gdsInfoReqDTO.setGdsDesc(this.updateMongoDB(this.getValue_(map,"neirongtiyao"),
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

        if (!list.contains("gdsPrice") && this.isNotNull_(map,"zhipindingjia")) {

            // 单品价格关系设置
//            List<GdsSku2PriceReqDTO> sku2PriceReqDTOs = new ArrayList<GdsSku2PriceReqDTO>();
//            GdsSku2PriceReqDTO gdsSku2PriceReqDTO = new GdsSku2PriceReqDTO();
//            gdsSku2PriceReqDTO.setPriceTypeCode(GdsConstants.GdsInfo.SKU_PRICE_TYPE_ORDINARY);
//            GdsPriceReqDTO gdsPriceReqDTO = new GdsPriceReqDTO();
            Long zhipindingjia = (long) (Float.parseFloat(this.getValue_(map,"zhipindingjia")) * 100);
//            gdsPriceReqDTO.setPrice(zhipindingjia);
//            gdsSku2PriceReqDTO.setPrice(gdsPriceReqDTO);
//            sku2PriceReqDTOs.add(gdsSku2PriceReqDTO);
//
//            // 设置单品价格关系到单品
//            gdsSkuInfoReqDTO.setSku2PriceReqDTOs(sku2PriceReqDTOs);
            
            gdsInfoReqDTO.setGuidePrice(zhipindingjia);
            gdsSkuInfoReqDTO.setCommonPrice(zhipindingjia);

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
