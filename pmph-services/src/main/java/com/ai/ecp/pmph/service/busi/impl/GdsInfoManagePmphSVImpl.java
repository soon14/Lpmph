package com.ai.ecp.pmph.service.busi.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import com.ai.ecp.goods.dao.model.GdsInterfaceGds;
import com.ai.ecp.goods.dao.model.GdsInterfaceGdsGidx;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoAddReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2CatgReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2PropReqDTO;
import com.ai.ecp.goods.service.busi.impl.gdsinfo.GdsInfoManageSVImpl;
import com.ai.ecp.goods.service.busi.interfaces.IGdsInterfaceGdsSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsInfoQuerySV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsErrorConstants;
import com.ai.ecp.pmph.facade.interfaces.eventual.IEEduVNNoticeMainSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:ecp-services-goods <br>
 * Description: 商品业务操作(更新，写入) <br>
 * Date:2015年8月25日下午5:20:36 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class GdsInfoManagePmphSVImpl extends GdsInfoManageSVImpl {
	
	public static  String prefixB="<";
	public static  String prefixA=">";
	
	@Resource
	private IGdsInterfaceGdsSV gdsInterfaceGdsSV;
	
	@Resource
    private IEEduVNNoticeMainSV eEduVNNoticeMainSV;

	/**
     * 商品查询SV
     */
    @Resource
    private IGdsInfoQuerySV gdsInfoQuerySV;
	/**
	 * 
	 * 添加商品.
	 * 
	 * 
	 */
	@Override
	public GdsInfoRespDTO addGdsInfo(GdsInfoAddReqDTO gdsInfoAddReqDTO) throws BusinessException {

		String originalGdsId =null;

		//维护商品编码映射表
		if(gdsInfoAddReqDTO.isIfMaintainGdsInterfaceGds()){
			originalGdsId = getOriginalGdsId(gdsInfoAddReqDTO);
			if (originalGdsId != null) {
				GdsInterfaceGdsReqDTO gdsInterfaceGdsReqExistsDTO = new GdsInterfaceGdsReqDTO();
				gdsInterfaceGdsReqExistsDTO.setOriginGdsId(originalGdsId);
				GdsInterfaceGds gdsInterfaceGds = gdsInterfaceGdsSV
						.queryGdsInterfaceGdsByOriginGdsId(gdsInterfaceGdsReqExistsDTO);
				if (gdsInterfaceGds != null) {
					throw new BusinessException(PmphGdsErrorConstants.GdsInfo.ERROR_PMPH_GOODS_ORIG_HAS_EXISTS);
				}
			}
		}

		GdsInfoRespDTO gdsInfoRespDTO = super.addGdsInfo(gdsInfoAddReqDTO);

		//维护商品编码映射表
		if(gdsInfoAddReqDTO.isIfMaintainGdsInterfaceGds()){
			if (originalGdsId != null) {
				originalGdsId=this.getOriginalGdsId4Prefix(originalGdsId, gdsInfoAddReqDTO);
				GdsInterfaceGdsReqDTO gdsInterfaceGdsReqDTO = new GdsInterfaceGdsReqDTO();
				gdsInterfaceGdsReqDTO.setShopId(gdsInfoRespDTO.getShopId());
				gdsInterfaceGdsReqDTO.setGdsId(gdsInfoRespDTO.getId());
				gdsInterfaceGdsReqDTO.setSkuId(gdsInfoRespDTO.getSkuIds().get(0));
				gdsInterfaceGdsReqDTO.setOriginGdsId(originalGdsId);
				gdsInterfaceGdsReqDTO.setOriginSkuId(originalGdsId);

				//TODO 在管理平台新增商品，来源系统无法确定，需要新增属性用于指定来源系统。
				//gdsInterfaceGdsReqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);

				gdsInterfaceGdsReqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
				gdsInterfaceGdsSV.saveGdsInterfaceGds(gdsInterfaceGdsReqDTO);
			}
		}

		return gdsInfoRespDTO;

	}
	/**
	 *  考试网、电子书、数字教材  加前缀
	 * @param originalGdsId
	 * @return
	 */
	private String getOriginalGdsId4Prefix(String originalGdsId,GdsInfoAddReqDTO gdsInfoAddReqDTO){
		if(null!=gdsInfoAddReqDTO.getGds2CatgReqDTOs()&&gdsInfoAddReqDTO.getGds2CatgReqDTOs().size()>0){
			for (GdsGds2CatgReqDTO catgReqDTO : gdsInfoAddReqDTO.getGds2CatgReqDTOs()) {
				if(GdsConstants.GdsCategory.CATG_TYPE_1.equals(catgReqDTO.getCatgType())&&GdsConstants.GdsInfo.GDS_2_CATG_RTYPE_MAIN.equals(catgReqDTO.getGds2catgType())){// 平台分类 ， 主分类
					if(catgReqDTO.getCatgPath().indexOf(prefixB+PmphGdsDataImportConstants.Commons.CATEGORY_EBOOK_ID+prefixA)>0||catgReqDTO.getCatgPath().indexOf(prefixB+PmphGdsDataImportConstants.Commons.CATEGORY_DBOOK_ID+prefixA)>0){
						return PmphGdsDataImportConstants.CodePreFix.ZY_EDBOOK_ORIGN_CODE_PREFIX+originalGdsId;
					}else if(catgReqDTO.getCatgPath().indexOf(prefixB+PmphGdsDataImportConstants.Commons.TUTORIUM_CLASS_CATGCODE+prefixA)>0){
						return PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_TUTORIALCLASS_ORIGN_CODE_PREFIX+originalGdsId;
					}else if(catgReqDTO.getCatgPath().indexOf(prefixB+PmphGdsDataImportConstants.Commons.TEST_PAPER_CATGCODE+prefixA)>0){
						return PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPERBAG_ORIGN_CODE_PREFIX+originalGdsId;
					}
				}
			}
		}
		return originalGdsId;
	}
	private String getOriginalGdsId(GdsInfoAddReqDTO gdsInfoAddReqDTO) {
		String orginalGdsId = null;
		// 保存普通参数/扩展 属性信息到商品属性关系表
		List<GdsGds2PropReqDTO> props = gdsInfoAddReqDTO.getGds2PropReqDTOs();
		if (CollectionUtils.isNotEmpty(props)) {
			for (GdsGds2PropReqDTO prop : props) {
				if (1051L == prop.getPropId()) {
					orginalGdsId = prop.getPropValue();
					break;
				}
			}
		}

		return orginalGdsId;
	}

	/**
	 * 
	 * TODO 编辑商品（可选）.
	 * 
	 * @see com.ai.ecp.goods.service.busi.impl.gdsinfo.GdsInfoManageSVImpl#editGdsInfoAndReference(com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoAddReqDTO)
	 */
	@Override
	public List<Long> editGdsInfoAndReference(GdsInfoAddReqDTO gdsInfoAddReqDTO) throws BusinessException {
	    String versionNumber = getPropValue(gdsInfoAddReqDTO.getGds2PropReqDTOs(),1008L);//获取本版编号
	    
	    List<Long> skuIds = super.editGdsInfoAndReference(gdsInfoAddReqDTO);
	    
	    if(null == versionNumber){//编辑时没传本版编号  需要从新获取  在 super.editGdsInfoAndReference中会读库整理成最终入库属性值
	        versionNumber = getPropValue(gdsInfoAddReqDTO.getGds2PropReqDTOs(),1008L);//获取本版编号
	    }
		//维护商品编码映射表
		if(gdsInfoAddReqDTO.isIfMaintainGdsInterfaceGds()){
			String orginalGdsId = getOriginalGdsId(gdsInfoAddReqDTO);

			if (!StringUtil.isBlank(orginalGdsId)) {

				//1、查询原始商品编码或来源系统信息
				GdsInterfaceGdsGidxReqDTO gdsInterfaceGdsGidxReqDTO=new GdsInterfaceGdsGidxReqDTO();
				gdsInterfaceGdsGidxReqDTO.setGdsId(gdsInfoAddReqDTO.getGdsInfoReqDTO().getId());
				GdsInterfaceGdsGidx gdsInterfaceGdsGidx=gdsInterfaceGdsSV.queryGdsInterfaceGdsGidxByEcpGdsId(gdsInterfaceGdsGidxReqDTO);
				String origin="";
				String originGdsIdPreFix="";
				if(gdsInterfaceGdsGidx!=null){
					origin=gdsInterfaceGdsGidx.getOrigin();
					String originGdsId=gdsInterfaceGdsGidx.getOriginGdsId();
					if(originGdsId.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EDBOOK_ORIGN_CODE_PREFIX)){
						originGdsIdPreFix=PmphGdsDataImportConstants.CodePreFix.ZY_EDBOOK_ORIGN_CODE_PREFIX;
					}else if(originGdsId.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPER_ORIGN_CODE_PREFIX)){
						originGdsIdPreFix=PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPER_ORIGN_CODE_PREFIX;
					}else if(originGdsId.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPERBAG_ORIGN_CODE_PREFIX)){
						originGdsIdPreFix=PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPERBAG_ORIGN_CODE_PREFIX;
					}else if(originGdsId.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_TUTORIALCLASS_ORIGN_CODE_PREFIX)){
						originGdsIdPreFix=PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_TUTORIALCLASS_ORIGN_CODE_PREFIX;
					}
				}

				// 2、失效之前的中间数据
				GdsInterfaceGdsReqDTO gdsInterfaceGdsDelReqDTO = new GdsInterfaceGdsReqDTO();
				gdsInterfaceGdsDelReqDTO.setShopId(gdsInfoAddReqDTO.getGdsInfoReqDTO().getShopId());
				gdsInterfaceGdsDelReqDTO.setGdsId(gdsInfoAddReqDTO.getGdsInfoReqDTO().getId());
				gdsInterfaceGdsDelReqDTO.setSkuId(skuIds.get(0));
				gdsInterfaceGdsDelReqDTO.setOrigin(gdsInterfaceGdsGidx.getOrigin());
				//不需要指定来源系统，只要能够失效和当前商品绑定的映射记录即可。
				//gdsInterfaceGdsDelReqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);
				gdsInterfaceGdsSV.deleteGdsInterfaceGdsByGdsId(gdsInterfaceGdsDelReqDTO);

				GdsInterfaceGdsReqDTO gdsInterfaceGdsReqExistsDTO = new GdsInterfaceGdsReqDTO();
				gdsInterfaceGdsReqExistsDTO.setOriginGdsId(orginalGdsId);
				GdsInterfaceGds gdsInterfaceGds = gdsInterfaceGdsSV
						.queryGdsInterfaceGdsByOriginGdsId(gdsInterfaceGdsReqExistsDTO);
				if (gdsInterfaceGds != null) {

					throw new BusinessException(PmphGdsErrorConstants.GdsInfo.ERROR_PMPH_GOODS_ORIG_HAS_EXISTS);
				}

				// 3、新增新的中间数据
				GdsInterfaceGdsReqDTO gdsInterfaceGdsReqDTO = new GdsInterfaceGdsReqDTO();
				gdsInterfaceGdsReqDTO.setShopId(gdsInfoAddReqDTO.getGdsInfoReqDTO().getShopId());
				gdsInterfaceGdsReqDTO.setGdsId(gdsInfoAddReqDTO.getGdsInfoReqDTO().getId());
				gdsInterfaceGdsReqDTO.setSkuId(skuIds.get(0));
				//前缀取原来的
				gdsInterfaceGdsReqDTO.setOriginGdsId(originGdsIdPreFix+orginalGdsId);
				gdsInterfaceGdsReqDTO.setOriginSkuId(originGdsIdPreFix+orginalGdsId);
				//来源系统应该置为原来的
				gdsInterfaceGdsReqDTO.setOrigin(origin);
				gdsInterfaceGdsReqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
				gdsInterfaceGdsSV.saveGdsInterfaceGds(gdsInterfaceGdsReqDTO);
			}
		}
		//发送versionNumber通知给人卫e教   不考虑修改了versionNumber 约定是versionNumber不能修改
		//所以在这里如果修改了  只会通知最新的versionNumber给对方
		if(StringUtil.isNotBlank(versionNumber)){
		    eEduVNNoticeMainSV.startAsyncEEduVNNoticMain(versionNumber);
		}
		return skuIds;
	}

	/**
	 * 
	 * deleteGdsInfo:(删除商品). <br/>
	 * 
	 * @param gdsInfoReqDTO
	 * @return
	 * @throws BusinessException
	 * @since JDK 1.6
	 */
	@Override
	public List<Long> deleteGdsInfo(GdsInfoReqDTO gdsInfoReqDTO) throws BusinessException {
		List<Long> skuIds = super.deleteGdsInfo(gdsInfoReqDTO);

		//维护商品编码映射表
		if(gdsInfoReqDTO.isIfMaintainGdsInterfaceGds()){
		    
		    GdsInterfaceGdsGidxReqDTO gdsInterfaceGdsGidxReqDTO=new GdsInterfaceGdsGidxReqDTO();
            gdsInterfaceGdsGidxReqDTO.setGdsId(gdsInfoReqDTO.getId());
            GdsInterfaceGdsGidx gdsInterfaceGdsGidx=gdsInterfaceGdsSV.queryGdsInterfaceGdsGidxByEcpGdsId(gdsInterfaceGdsGidxReqDTO);
			// 失效之前的中间数据
            if(null != gdsInterfaceGdsGidx){
                GdsInterfaceGdsReqDTO gdsInterfaceGdsDelReqDTO = new GdsInterfaceGdsReqDTO();
                gdsInterfaceGdsDelReqDTO.setShopId(gdsInfoReqDTO.getShopId());
                gdsInterfaceGdsDelReqDTO.setGdsId(gdsInfoReqDTO.getId());
                gdsInterfaceGdsDelReqDTO.setSkuId(skuIds.get(0));
                //不需要指定来源系统，只要能够失效和当前商品绑定的映射记录即可。
                gdsInterfaceGdsDelReqDTO.setOrigin(gdsInterfaceGdsGidx.getOrigin());
                gdsInterfaceGdsSV.deleteGdsInterfaceGdsByGdsId(gdsInterfaceGdsDelReqDTO); 
            }
		}

		return skuIds;
	}

	/**
	 * 
	 * batchDelGdsInfo:(批量删除商品). <br/>
	 * 
	 * @param gdsInfoReqDTO
	 * @since JDK 1.6
	 */
	@Override
	public void batchDelGdsInfo(GdsInfoReqDTO gdsInfoReqDTO) {
		Long[] ids = gdsInfoReqDTO.getIds();
		if (!ArrayUtils.isEmpty(ids)) {
			for (Long id : ids) {
				gdsInfoReqDTO.setId(id);
				deleteGdsInfo(gdsInfoReqDTO);
			}
		}
	}
	/**
	 * 
	 * getPropValue:(根据属性id获取属性值). <br/> 
	 * 
	 * @param propList
	 * @param propId
	 * @return null代表没有对应属性   非null代表有对应属性  属性值为空  则返回""空字符串
	 * @since JDK 1.6
	 */
	private String getPropValue(List<GdsGds2PropReqDTO> propList,Long propId){
	    String value = null;
	    if(null == propId){
	        return value;
	    }
	    if(CollectionUtils.isNotEmpty(propList)){
	        for(GdsGds2PropReqDTO prop : propList){
	            if(null != prop.getPropId() && propId.longValue() == prop.getPropId().longValue()){
	                value = prop.getPropValue();
	                value = null == value?"":value.trim();
	                break;
	            }
	        }
	    }
	    return value;
	}
}
