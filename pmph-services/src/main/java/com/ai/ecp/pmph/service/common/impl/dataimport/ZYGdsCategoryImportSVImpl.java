package com.ai.ecp.pmph.service.common.impl.dataimport;

import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgRespDTO;
import com.ai.ecp.goods.service.common.impl.dataimport.AbstractCategoryImportSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IZYGdsCategoryImportSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Project Name:ecp-services-goods-server <br>
 * Description: 泽元电子书数字教材分类导入服务实现<br>
 * Date:2015-10-27下午2:22:12 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ZYGdsCategoryImportSVImpl extends AbstractCategoryImportSV implements
		IZYGdsCategoryImportSV {

	// 默认人卫商城分类目录。
	private static final Long DEFAULT_CATLOG_ID = fetchDefaultCatlogId();
	// 数字教材ID.
	private static final String DIGITAL_TEACHING_MATERIALS_CATG_CODE = "1201";
	// 电子书分类ID.
	private static final String E_BOOK_CATG_CODE = "1200";


	@Override
	public void executeImport(Map<String, Object> map) throws BusinessException {
		
		if (MapUtils.isEmpty(map)) {
			LogUtil.warn(MODULE, "ERP分类数据解析时传入数据为空");
			return;
		}
        super.executeImport(map);
	}
	
	
	@Override
	public void batchImport(List<Map<String, Object>> dataMaps)
			throws BusinessException {
        
//		List<GdsInterfaceCatgReqDTO> interfactCatgLst = new ArrayList<GdsInterfaceCatgReqDTO>();
		// 从数据集中提取出该批次全量有效原始分类编码。
		List<String> validCatgCodes = getValidCatgCodes(dataMaps);
		// 查询出当前属于泽元有效分类关系。
		PageResponseDTO<GdsInterfaceCatgRespDTO> page = this.queryCurrentValidInterfaceCatg();
		for(Map<String, Object> map : dataMaps){
			executeImport(map);
		}
		clearDatas(validCatgCodes, page);
		
	}
	
	

    @Override
    protected Long getStaffId() {
        return PmphGdsDataImportConstants.Commons.STAFF_ID;
    }


    /**
	 * 
	 * deleteCategory:根据分类映射关系删除分类信息。 
	 * 
	 * @param interfaceCatgReqDTO 
	 * @since JDK 1.6
	 */
	public void deleteCategory(GdsInterfaceCatgReqDTO interfaceCatgReqDTO) {
		LogUtil.info(MODULE, "执行分类删除操作,原始分类来源:["+interfaceCatgReqDTO.getOrigin()+"]原始分类ID["+interfaceCatgReqDTO.getOriginCatgCode()+"]");
		GdsCategoryRespDTO catg = gdsInterfaceCatgSV
				.queryCategoryByOriginCatgCode(interfaceCatgReqDTO);
		if (null != catg) {
			GdsCategoryReqDTO delCondition = new GdsCategoryReqDTO();
			delCondition.setCatgCode(catg.getCatgCode());
			gdsCategorySV.deleteGdsCategory(delCondition);
			GdsInterfaceCatgReqDTO interfaceCatgDel = new GdsInterfaceCatgReqDTO();
			interfaceCatgDel.setCatgCode(catg.getCatgCode());
			gdsInterfaceCatgSV.deleteInterfaceCatgByCatgCode(interfaceCatgDel);
		}
	}


	// ************************
	// ****private methods*****
	// ************************


	




	protected GdsCategoryReqDTO buildGdsCategoryReqDTO(
			GdsInterfaceCatgReqDTO reqDTO) {
		GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
		dto.setCatgName(reqDTO.getOriginCatgName());
		dto.setCatgType(GdsConstants.GdsCategory.CATG_TYPE_1);
		dto.setCatlogId(DEFAULT_CATLOG_ID);
		dto.setIfShow(GdsConstants.GdsCategory.IF_SHOW_0);
		if (StringUtil.isNotBlank(reqDTO.getCatgParent())) {
			if (StringUtil.isNotBlank(reqDTO.getCatgParent())) {
				GdsInterfaceCatgReqDTO icrd = new GdsInterfaceCatgReqDTO();
				icrd.setOriginCatgCode(reqDTO.getCatgParent());
				icrd.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);
				GdsCategoryRespDTO parent = gdsInterfaceCatgSV.queryCategoryByOriginCatgCode(icrd);
				if(null == parent){
					throw new BusinessException("error.category.dataimport.2",new String[]{reqDTO.getOrigin(),reqDTO.getOriginCatgCode()});
				}
				dto.setCatgParent(parent.getCatgCode());
			}
		}else{
			if(null == reqDTO.getCatgType() || (1 != reqDTO.getCatgType() && 2 != reqDTO.getCatgType())){
				throw new BusinessException("error.category.dataimport.zy.1", new String[]{reqDTO.getOriginCatgCode()});
			}
			if(1 == reqDTO.getCatgType()){
				dto.setCatgParent(E_BOOK_CATG_CODE);
			}else{
				dto.setCatgParent(DIGITAL_TEACHING_MATERIALS_CATG_CODE);
			}
		}
		dto.setSortNo(reqDTO.getSortNo());
		dto.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		dto.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		dto.setIfAbleEdit(GdsConstants.Commons.STATUS_VALID);
		return dto;
	}

	/*
	 * 
	 * 返回分类中间表映射对象。
	 * 
	 * 
	 * @param map
	 * 
	 * @return
	 * 
	 * @since JDK 1.6
	 */
	protected GdsInterfaceCatgReqDTO buildGdsInterfaceCatg(Map<String, Object> map) {
		GdsInterfaceCatgReqDTO record = new GdsInterfaceCatgReqDTO();
		record.setId(seqGdsInterfaceCatgr.nextValue());
		record.setActionType(GdsDataImportConstants.ActionType.UPDATE);

        String catgParent = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_PARENT));
        if(!"null".equals(catgParent) && !"0".equals(catgParent) && StringUtil.isNotEmpty(catgParent)){
          record.setCatgParent(catgParent);
        }
		
		record.setStatus(GdsConstants.Commons.STATUS_VALID);
		
		String type = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.TYPE));
		
		record.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);
		
		String originCatgCode = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_CODE));
		if(!"null".equals(originCatgCode)&&StringUtil.isNotBlank(originCatgCode)){
		     record.setOriginCatgCode(null != originCatgCode ? originCatgCode : null);
		}
		
		String originCatgName = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_NAME));
		if(!"null".equals(originCatgName)&&StringUtil.isNotBlank(originCatgName)){
		     record.setOriginCatgName(originCatgName);
		}
		
		//String sortNo = (String) map.get(DataImportConstants.ZYDataMapKeys.SORT_NO);
		
		//record.setSortNo(null != sortNo ? Integer.valueOf(sortNo) : null);
		
		record.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		record.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		
		if(!"null".equals(type)&&StringUtil.isNotEmpty(type)){
			record.setCatgType(StringUtil.isNotBlank(type) ? Integer.valueOf(type) : null);
		}/*else{
			throw new BusinessException("error.category.dataimport.zy.1", new String[]{record.getOriginCatgCode()});
		}*/
		
		return record;
	}
	
	private PageResponseDTO<GdsInterfaceCatgRespDTO> queryCurrentValidInterfaceCatg(){
		GdsInterfaceCatgReqDTO reqDTO = new GdsInterfaceCatgReqDTO();
		reqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
		reqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);
		reqDTO.setPageSize(Integer.MAX_VALUE);
		// 添加排除泽元考试网原始分类条件,用于只更新泽元数字教材电子书分类.
		reqDTO.setOriginCatgCodeExcludePrefix(PmphGdsDataImportConstants.Commons.ZY_EXAM_ORIGN_CODE_PREFIX);
		PageResponseDTO<GdsInterfaceCatgRespDTO> page = gdsInterfaceCatgSV.queryPaing(reqDTO);
		return page;
	}
	/*
	 * 
	 */
	private void clearDatas(List<String> validCatgCodes,
			PageResponseDTO<GdsInterfaceCatgRespDTO> page) {
		if(!CollectionUtils.isEmpty(validCatgCodes) && null != page && !CollectionUtils.isEmpty(page.getResult()) ){
			for(GdsInterfaceCatgRespDTO obj : page.getResult())
			{
				if(!validCatgCodes.contains(obj.getOriginCatgCode())){
					GdsInterfaceCatgReqDTO delCondition = new GdsInterfaceCatgReqDTO();
					ObjectCopyUtil.copyObjValue(obj, delCondition, null, true);
					deleteCategory(delCondition, null);
				}
			}
		}
	}
	
	
	
	private List<String> getValidCatgCodes(List<Map<String, Object>> dataMaps){
		List<String> lst = new ArrayList<String>();
		for(Map<String, Object> map : dataMaps){
			String originCatgCode = (String) map.get(DataImportConstants.ZYDataMapKeys.CATG_CODE);
		    if(StringUtil.isNotBlank(originCatgCode)){
		    	lst.add(originCatgCode);
		    }
		}
		return lst;
	}

	private static Long fetchDefaultCatlogId() {
		BaseSysCfgRespDTO respDTO = SysCfgUtil
				.fetchSysCfg(GdsConstants.SysCfgConstants.CATLOG_ID_RENWEI);
		return Long.valueOf(respDTO.getParaValue());
	}
	
	
	

}
