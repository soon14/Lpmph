/** 
 * File Name:EBookIDSyncSVImpl.java 
 * Date:2016年8月16日上午11:28:40 
 * 
 */ 
package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.ecp.goods.dao.mapper.busi.GdsGds2PropPropIdxMapper;
import com.ai.ecp.goods.dao.mapper.busi.GdsSku2PropPropIdxMapper;
import com.ai.ecp.goods.dao.model.GdsGds2PropPropIdx;
import com.ai.ecp.goods.dao.model.GdsGds2PropPropIdxCriteria;
import com.ai.ecp.goods.dao.model.GdsProp;
import com.ai.ecp.goods.dao.model.GdsSku2Prop;
import com.ai.ecp.goods.dao.model.GdsSku2PropPropIdx;
import com.ai.ecp.goods.dao.model.GdsSku2PropPropIdxCriteria;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCatg2PropRelationRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatg2PropReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatg2PropRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2PropReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2PropReqDTO;
import com.ai.ecp.goods.dubbo.impl.AbstractRSVImpl;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsSkuInfoQuerySV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfores.IGdsInfo2PropSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfores.IGdsSkuInfo2PropSV;
import com.ai.ecp.goods.service.common.interfaces.IGdsCategorySV;
import com.ai.ecp.goods.service.common.interfaces.IGdsCatg2PropSV;
import com.ai.ecp.goods.service.common.interfaces.IGdsPropSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;
import com.ai.ecp.pmph.service.busi.interfaces.IEBookIDSyncSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * Project Name:pmph-services-server <br>
 * Description: 电子书ID同步服务实现类。<br>
 * Date:2016年8月16日上午11:28:40  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class EBookIDSyncSVImpl extends AbstractRSVImpl implements IEBookIDSyncSV {
    
    @Autowired(required=false)
    private IGdsSkuInfoQuerySV gdsSkuInfoQuerySV;
    @Autowired(required=false)
    private IGdsCategorySV gdsCategorySV;
    @Autowired(required=false)
    private IGdsSkuInfo2PropSV gdsSkuInfo2PropSV;
    @Autowired(required=false)
    private IGdsInfo2PropSV gdsInfo2PropSV;
    @Autowired(required=false)
    private IGdsPropSV gdsPropSV;
    @Autowired(required=false)
    private IGdsCatg2PropSV gdsCatg2PropSV;
    
    @Resource
    private GdsGds2PropPropIdxMapper gds2PropPropIdxMapper;

    @Resource
    private GdsSku2PropPropIdxMapper sku2PropPropIdxMapper;
    
    /** 
     * TODO 简单描述该方法的实现功能（可选）. 
     * @see com.ai.ecp.pmph.service.busi.interfaces.IEBookIDSyncSV#executeEBookIDSync(com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO) 
     */
    @Override
    public List<GdsSkuInfoRespDTO> executeEBookIDSync(EBookIDSyncReqDTO reqDTO) throws BusinessException{
        List<GdsSkuInfoRespDTO> lst = new ArrayList<>();
        paramValidation(reqDTO);
        Operate oper = getOperate(reqDTO);
        if(Operate.DEL != oper){
            if(StringUtils.isBlank(reqDTO.getEbook_id())){
                throw new BusinessException("ebook_id不允许为空!"); 
             } 
        }
        List<GdsSkuInfoRespDTO> list = queryGdsSkuInfoResp(reqDTO);
        for(GdsSkuInfoRespDTO resp : list){
            // 获取主分类ID。
            String mainCatgs = resp.getMainCatgs();
            boolean bool = isBelongPBook(mainCatgs);
            if(bool){
                doSync(resp,reqDTO,oper);
                lst.add(resp);
            }
        }
        if(lst.size() == 0){
            throw new BusinessException("不存在ISBN＝"+reqDTO.getIsbn()+"的纸质书信息");
        }
        return lst;
    }
    /**
     * 
     * doEBookIDSync:执行电子书ID同步. <br/> 
     * 
     * @param resp
     * @param reqDTO 
     * @since JDK 1.6
     */
    private void doSync(GdsSkuInfoRespDTO resp, EBookIDSyncReqDTO reqDTO, Operate oper) {
        GdsCatg2PropReqDTO relationQuery = new GdsCatg2PropReqDTO();
        relationQuery.setPropId(EBOOKID_PROP_ID);
        relationQuery.setCatgCode(P_BOOK_CATG);
        relationQuery.setStatus(GdsConstants.Commons.STATUS_VALID);
        PageResponseDTO<GdsCatg2PropRespDTO> page = gdsCatg2PropSV.queryConfigedGdsCatg2PropRespDTOPaging(relationQuery);
        //GdsCatg2PropRelationRespDTO relationRespDTO = gdsCatg2PropSV.queryCategoryPropsByCondition(relationQuery);
        List<GdsCatg2PropRespDTO> propList = page.getResult();
        if(CollectionUtils.isEmpty(propList)){
            throw new BusinessException("数字图书编号属性不存在!");
        }
        GdsProp gdsPropRecord = gdsPropSV.queryGdsPropByPK(EBOOKID_PROP_ID,true);
        if(null == gdsPropRecord){
            throw new BusinessException("数字图书编号属性不存在!");
        }
        
        if(Operate.ADD == oper || Operate.EDIT == oper){
            GdsSku2PropReqDTO query = new GdsSku2PropReqDTO();
            query.setSkuId(resp.getId());
            query.setPropId(EBOOKID_PROP_ID);
            GdsSku2Prop record = gdsSkuInfo2PropSV.queryGdsSku2Prop(query); 
            if(null == record){
                addProp(resp,reqDTO,propList.get(0),gdsPropRecord);
            }else{
                editProp(resp,reqDTO,propList.get(0),gdsPropRecord);
            }
        }else{
            deleteProp(resp,reqDTO,propList.get(0),gdsPropRecord);
        }
        delGdsInfoCache(resp.getGdsId());
        delSkuInfoCache(resp.getId());
    }
    private void deleteProp(GdsSkuInfoRespDTO resp,EBookIDSyncReqDTO reqDTO,GdsCatg2PropRespDTO propRespDTO,GdsProp prop) {
        GdsSku2PropReqDTO sku2PropEditReq = new GdsSku2PropReqDTO();
        sku2PropEditReq.setPropValue("");
        sku2PropEditReq.setUpdateTime(DateUtil.getSysDate());
        GdsSku2PropReqDTO sku2PropEditCondition = new GdsSku2PropReqDTO();
        sku2PropEditCondition.setGdsId(resp.getGdsId());
        sku2PropEditCondition.setShopId(resp.getShopId());
        sku2PropEditCondition.setSkuId(resp.getId());
        sku2PropEditCondition.setPropId(propRespDTO.getPropId());
        sku2PropEditCondition.setStatus(GdsConstants.Commons.STATUS_VALID);
        gdsSkuInfo2PropSV.updateSku2Prop(sku2PropEditReq, sku2PropEditCondition);
        
        
        GdsSku2PropPropIdx sku2propPropIdxEditReq = new GdsSku2PropPropIdx();
        sku2propPropIdxEditReq.setPropValue("");
        sku2propPropIdxEditReq.setUpdateTime(DateUtil.getSysDate());
        GdsSku2PropPropIdxCriteria sku2propPropIdxEditCondition = new GdsSku2PropPropIdxCriteria();
        GdsSku2PropPropIdxCriteria.Criteria sku2propPropIdxEditCriteria = sku2propPropIdxEditCondition.createCriteria();
        sku2propPropIdxEditCriteria.andGdsIdEqualTo(resp.getGdsId());
        sku2propPropIdxEditCriteria.andSkuIdEqualTo(resp.getId());
        sku2propPropIdxEditCriteria.andPropIdEqualTo(propRespDTO.getPropId());
        sku2propPropIdxEditCriteria.andShopIdEqualTo(resp.getShopId());
        sku2propPropIdxEditCriteria.andStatusEqualTo(GdsConstants.Commons.STATUS_VALID);
        sku2PropPropIdxMapper.updateByExampleSelective(sku2propPropIdxEditReq, sku2propPropIdxEditCondition);
        
        
        GdsGds2PropReqDTO gds2PropEditReqDTO =  new GdsGds2PropReqDTO();
        gds2PropEditReqDTO.setPropValue("");
        gds2PropEditReqDTO.setUpdateTime(DateUtil.getSysDate());
        GdsGds2PropReqDTO gds2PropEditCondition = new GdsGds2PropReqDTO();
        gds2PropEditCondition.setGdsId(resp.getGdsId());
        gds2PropEditCondition.setShopId(resp.getShopId());
        gds2PropEditCondition.setPropId(propRespDTO.getPropId());
        gds2PropEditCondition.setPropValue(reqDTO.getEbook_id());
        gds2PropEditCondition.setStatus(GdsConstants.Commons.STATUS_VALID);
        gdsInfo2PropSV.updateGds2Prop(gds2PropEditReqDTO, gds2PropEditCondition);
        
        
        
        GdsGds2PropPropIdx gds2propPropIdxEditReq = new GdsGds2PropPropIdx();
        gds2propPropIdxEditReq.setPropValue("");
        gds2propPropIdxEditReq.setUpdateTime(DateUtil.getSysDate());
        GdsGds2PropPropIdxCriteria gdsGds2PropPropIdxEditCondition = new GdsGds2PropPropIdxCriteria();
        GdsGds2PropPropIdxCriteria.Criteria gdsGds2PropPropIdxEditCriteria = gdsGds2PropPropIdxEditCondition.createCriteria();
        gdsGds2PropPropIdxEditCriteria.andGdsIdEqualTo(resp.getGdsId());
        gdsGds2PropPropIdxEditCriteria.andPropIdEqualTo(propRespDTO.getPropId());
        gdsGds2PropPropIdxEditCriteria.andShopIdEqualTo(resp.getShopId());
        gdsGds2PropPropIdxEditCriteria.andStatusEqualTo(GdsConstants.Commons.STATUS_VALID);
        gds2PropPropIdxMapper.updateByExampleSelective(gds2propPropIdxEditReq, gdsGds2PropPropIdxEditCondition);
        
    }
    private void editProp(GdsSkuInfoRespDTO resp,EBookIDSyncReqDTO reqDTO,GdsCatg2PropRespDTO propRespDTO,GdsProp prop) {
        GdsSku2PropReqDTO sku2PropEditReq = new GdsSku2PropReqDTO();
        sku2PropEditReq.setPropValue(reqDTO.getEbook_id());
        sku2PropEditReq.setPropValueType(prop.getPropValueType());
        sku2PropEditReq.setUpdateTime(DateUtil.getSysDate());
        GdsSku2PropReqDTO sku2PropEditCondition = new GdsSku2PropReqDTO();
        sku2PropEditCondition.setGdsId(resp.getGdsId());
        sku2PropEditCondition.setShopId(resp.getShopId());
        sku2PropEditCondition.setSkuId(resp.getId());
        sku2PropEditCondition.setPropId(propRespDTO.getPropId());
        sku2PropEditCondition.setStatus(GdsConstants.Commons.STATUS_VALID);
        gdsSkuInfo2PropSV.updateSku2Prop(sku2PropEditReq, sku2PropEditCondition);
        
        
        GdsSku2PropPropIdx sku2propPropIdxEditReq = new GdsSku2PropPropIdx();
        sku2propPropIdxEditReq.setPropValue(reqDTO.getEbook_id());
        sku2propPropIdxEditReq.setPropValueType(prop.getPropValueType());
        sku2propPropIdxEditReq.setUpdateTime(DateUtil.getSysDate());
        GdsSku2PropPropIdxCriteria sku2propPropIdxEditCondition = new GdsSku2PropPropIdxCriteria();
        GdsSku2PropPropIdxCriteria.Criteria sku2propPropIdxEditCriteria = sku2propPropIdxEditCondition.createCriteria();
        sku2propPropIdxEditCriteria.andGdsIdEqualTo(resp.getGdsId());
        sku2propPropIdxEditCriteria.andSkuIdEqualTo(resp.getId());
        sku2propPropIdxEditCriteria.andPropIdEqualTo(propRespDTO.getPropId());
        sku2propPropIdxEditCriteria.andShopIdEqualTo(resp.getShopId());
        sku2propPropIdxEditCriteria.andStatusEqualTo(GdsConstants.Commons.STATUS_VALID);
        sku2PropPropIdxMapper.updateByExampleSelective(sku2propPropIdxEditReq, sku2propPropIdxEditCondition);
        
        
        GdsGds2PropReqDTO gds2PropEditReqDTO =  new GdsGds2PropReqDTO();
        gds2PropEditReqDTO.setPropValue(reqDTO.getEbook_id());
        gds2PropEditReqDTO.setPropValueType(prop.getPropValueType());
        gds2PropEditReqDTO.setUpdateTime(DateUtil.getSysDate());
        GdsGds2PropReqDTO gds2PropEditCondition = new GdsGds2PropReqDTO();
        gds2PropEditCondition.setGdsId(resp.getGdsId());
        gds2PropEditCondition.setShopId(resp.getShopId());
        gds2PropEditCondition.setPropId(prop.getId());
        gds2PropEditCondition.setStatus(GdsConstants.Commons.STATUS_VALID);
        gdsInfo2PropSV.updateGds2Prop(gds2PropEditReqDTO, gds2PropEditCondition);
        
        
        
        GdsGds2PropPropIdx gds2propPropIdxEditReq = new GdsGds2PropPropIdx();
        gds2propPropIdxEditReq.setPropValue(reqDTO.getEbook_id());
        gds2propPropIdxEditReq.setPropValueType(prop.getPropValueType());
        gds2propPropIdxEditReq.setUpdateTime(DateUtil.getSysDate());
        GdsGds2PropPropIdxCriteria gdsGds2PropPropIdxEditCondition = new GdsGds2PropPropIdxCriteria();
        GdsGds2PropPropIdxCriteria.Criteria gdsGds2PropPropIdxEditCriteria = gdsGds2PropPropIdxEditCondition.createCriteria();
        gdsGds2PropPropIdxEditCriteria.andGdsIdEqualTo(resp.getGdsId());
        gdsGds2PropPropIdxEditCriteria.andPropIdEqualTo(propRespDTO.getPropId());
        gdsGds2PropPropIdxEditCriteria.andShopIdEqualTo(resp.getShopId());
        gdsGds2PropPropIdxEditCriteria.andStatusEqualTo(GdsConstants.Commons.STATUS_VALID);
        gds2PropPropIdxMapper.updateByExampleSelective(gds2propPropIdxEditReq, gdsGds2PropPropIdxEditCondition);
    }
    
    private void addProp(GdsSkuInfoRespDTO resp,EBookIDSyncReqDTO reqDTO,GdsCatg2PropRespDTO propRespDTO,GdsProp prop) {
        GdsSku2PropReqDTO sku2PropAddReq = new GdsSku2PropReqDTO();
        sku2PropAddReq.setGdsId(resp.getGdsId());
        sku2PropAddReq.setShopId(resp.getShopId());
        sku2PropAddReq.setSkuId(resp.getId());
        sku2PropAddReq.setGdsStatus(resp.getGdsStatus());
        sku2PropAddReq.setIfBasic(propRespDTO.getIfBasic());
        sku2PropAddReq.setIfMust(propRespDTO.getIfHaveto());
        sku2PropAddReq.setPropId(propRespDTO.getPropId());
        sku2PropAddReq.setPropInputRule(prop.getPropInputRule());
        sku2PropAddReq.setPropInputType(prop.getPropInputType());
        sku2PropAddReq.setPropName(prop.getPropName());
        sku2PropAddReq.setPropValue(reqDTO.getEbook_id());
        sku2PropAddReq.setPropValueType(prop.getPropValueType());
        sku2PropAddReq.setStatus(GdsConstants.Commons.STATUS_VALID);
        sku2PropAddReq.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        sku2PropAddReq.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        sku2PropAddReq.setCreateTime(DateUtil.getSysDate());
        sku2PropAddReq.setUpdateTime(sku2PropAddReq.getCreateTime());
        sku2PropAddReq.setPropType(prop.getPropType());
        gdsSkuInfo2PropSV.saveSku2Prop(sku2PropAddReq);
        
        
        GdsSku2PropPropIdx sku2propPropIdxAddReq = new GdsSku2PropPropIdx();
        sku2propPropIdxAddReq.setGdsId(resp.getGdsId());
        sku2propPropIdxAddReq.setSkuId(resp.getId());
        sku2propPropIdxAddReq.setShopId(resp.getShopId());
        sku2propPropIdxAddReq.setGdsStatus(resp.getGdsStatus());
        sku2propPropIdxAddReq.setIfBasic(propRespDTO.getIfBasic());
        sku2propPropIdxAddReq.setIfHaveto(propRespDTO.getIfHaveto());
        sku2propPropIdxAddReq.setPropId(prop.getId());
        sku2propPropIdxAddReq.setPropInputRule(prop.getPropInputRule());
        sku2propPropIdxAddReq.setPropInputType(prop.getPropInputType());
        sku2propPropIdxAddReq.setPropName(prop.getPropName());
        sku2propPropIdxAddReq.setPropValue(reqDTO.getEbook_id());
        sku2propPropIdxAddReq.setPropValueType(prop.getPropValueType());
        sku2propPropIdxAddReq.setStatus(GdsConstants.Commons.STATUS_VALID);
        sku2propPropIdxAddReq.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        sku2propPropIdxAddReq.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        sku2propPropIdxAddReq.setCreateTime(DateUtil.getSysDate());
        sku2propPropIdxAddReq.setUpdateTime(sku2propPropIdxAddReq.getCreateTime());
        sku2propPropIdxAddReq.setPropType(prop.getPropType());
        sku2PropPropIdxMapper.insertSelective(sku2propPropIdxAddReq);
        
        
        GdsGds2PropReqDTO gds2PropAddReqDTO =  new GdsGds2PropReqDTO();
        gds2PropAddReqDTO.setGdsId(resp.getGdsId());
        gds2PropAddReqDTO.setShopId(resp.getShopId());
        gds2PropAddReqDTO.setGdsStatus(resp.getGdsStatus());
        gds2PropAddReqDTO.setIfBasic(propRespDTO.getIfBasic());
        gds2PropAddReqDTO.setIfMust(propRespDTO.getIfHaveto());
        gds2PropAddReqDTO.setPropId(prop.getId());
        gds2PropAddReqDTO.setPropInputRule(prop.getPropInputRule());
        gds2PropAddReqDTO.setPropInputType(prop.getPropInputType());
        gds2PropAddReqDTO.setPropName(prop.getPropName());
        gds2PropAddReqDTO.setPropValue(reqDTO.getEbook_id());
        gds2PropAddReqDTO.setPropValueType(prop.getPropValueType());
        gds2PropAddReqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
        gds2PropAddReqDTO.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        gds2PropAddReqDTO.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        gds2PropAddReqDTO.setCreateTime(DateUtil.getSysDate());
        gds2PropAddReqDTO.setUpdateTime(gds2PropAddReqDTO.getCreateTime());
        gds2PropAddReqDTO.setPropType(prop.getPropType());
        gdsInfo2PropSV.saveGds2Prop(gds2PropAddReqDTO);
        
        
        GdsGds2PropPropIdx gds2propPropIdxAddReq = new GdsGds2PropPropIdx();
        gds2propPropIdxAddReq.setGdsId(resp.getGdsId());
        gds2propPropIdxAddReq.setShopId(resp.getShopId());
        gds2propPropIdxAddReq.setGdsStatus(resp.getGdsStatus());
        gds2propPropIdxAddReq.setIfBasic(propRespDTO.getIfBasic());
        gds2propPropIdxAddReq.setIfHaveto(propRespDTO.getIfHaveto());
        gds2propPropIdxAddReq.setPropId(prop.getId());
        gds2propPropIdxAddReq.setPropInputRule(prop.getPropInputRule());
        gds2propPropIdxAddReq.setPropInputType(prop.getPropInputType());
        gds2propPropIdxAddReq.setPropName(prop.getPropName());
        gds2propPropIdxAddReq.setPropValue(reqDTO.getEbook_id());
        gds2propPropIdxAddReq.setPropValueType(prop.getPropValueType());
        gds2propPropIdxAddReq.setStatus(GdsConstants.Commons.STATUS_VALID);
        gds2propPropIdxAddReq.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        gds2propPropIdxAddReq.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        gds2propPropIdxAddReq.setCreateTime(DateUtil.getSysDate());
        gds2propPropIdxAddReq.setUpdateTime(gds2propPropIdxAddReq.getCreateTime());
        gds2propPropIdxAddReq.setPropType(prop.getPropType());
        gds2PropPropIdxMapper.insertSelective(gds2propPropIdxAddReq);
    }
    /**
     * 
     * isBelongPBook:是否隶属于纸质书分类. <br/> 
     * 
     * @param mainCatgs
     * @return 
     * @since JDK 1.6
     */
    private boolean isBelongPBook(String mainCatgs) {
        GdsCategoryCompareReqDTO compareReqDTO = new GdsCategoryCompareReqDTO();
        compareReqDTO.setSourceCode(mainCatgs);
        // 纸质书分类ID。
        compareReqDTO.setTargetCode("1115");
        GdsCategoryCompareRespDTO  compareResp = gdsCategorySV.executeCompare(compareReqDTO);
        if (null != compareResp) {
            if (GdsCategoryCompareRespDTO.RESULT_EQUAL == compareResp.getResult() || GdsCategoryCompareRespDTO.RESULT_GREAT_THAN == compareResp.getResult()
                    || GdsCategoryCompareRespDTO.RESULT_LESS_THAN == compareResp.getResult()) {
                return true;
            }
        }
        return false;
    }

    private void paramValidation(EBookIDSyncReqDTO reqDTO){
        paramNullCheck(reqDTO, "reqDTO");
        String eBookId = reqDTO.getEbook_id();
        String oper = reqDTO.getOperate();
        String isbn = reqDTO.getIsbn();
        if(StringUtils.isBlank(isbn)){
            throw new BusinessException("isbn不允许为空!");
        }
        if(StringUtils.isBlank(oper)){
            throw new BusinessException("operate不允许为空");
        }
    }
    
    
    private Operate getOperate(EBookIDSyncReqDTO reqDTO){
        String oper = reqDTO.getOperate();
        if("1".equals(oper)){
            return Operate.ADD;
        }else if("2".equals(oper)){
            return Operate.EDIT;
        }else if("3".equals(oper)){
            return Operate.DEL;
        }else{
            throw new BusinessException("operator不合法！");
        }
    }
    
    
    private void delSkuInfoCache(Long skuId) {
        try {
            CacheUtil.delItem(GdsConstants.GdsInfoCacheKey.SKU_CACHE_KEY_PREFIX + skuId);
        } catch (Exception e) {
            LogUtil.error(MODULE, "delete skuInfo cache failed! please check  Cache Server!", e);
        }
    }

    private void delGdsInfoCache(Long gdsId) {
        // 删除商品主图缓存
        try {
            CacheUtil.delItem(GdsConstants.GdsInfoCacheKey.GDS_MAINPIC_CACHE_KEY_PREFIX + gdsId);
        } catch (Exception e) {
            LogUtil.error(MODULE, "del gdsInfo main pic cache failed! ! please check  Cache Server!", e);
        }

        // 删除商品信息缓存
        try {
            CacheUtil.delItem(GdsConstants.GdsInfoCacheKey.GDS_CACHE_KEY_PREFIX + gdsId);
        } catch (Exception e) {
            LogUtil.error(MODULE, "edit gdsInfo cache failed! please check  Cache Server!", e);
        }
    }
    
    
    private List<GdsSkuInfoRespDTO> queryGdsSkuInfoResp(EBookIDSyncReqDTO reqDTO){
        GdsSku2PropPropIdxReqDTO query = new GdsSku2PropPropIdxReqDTO();
        // 设置ISBN属性ID。
        query.setPropId(ISBN_PROP_ID);
        // 设置ISBN属性值。
        query.setPropValue(reqDTO.getIsbn());
        query.setPageSize(10);
        query.setStatus(GdsConstants.Commons.STATUS_VALID);
        PageResponseDTO<GdsSkuInfoRespDTO> page = gdsSkuInfoQuerySV.queryGdsSkuInfoPagingByProp(query);
        if(0 >= page.getCount()){
            throw new BusinessException("不存在ISBN＝"+reqDTO.getIsbn()+"的纸质书信息");
        }
        return page.getResult();
    }

}

