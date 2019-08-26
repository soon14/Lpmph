/** 
 * File Name:ExamNetCategoryImportSVImpl.java 
 * Date:2015-11-6上午11:03:26 
 * 
 */ 
package com.ai.ecp.pmph.service.common.impl.dataimport;

import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgReqDTO;
import com.ai.ecp.goods.service.common.impl.dataimport.AbstractCategoryImportSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IZYExamNetCategoryImportSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description: 考试网分类导入服务实现类。辅导班的type设置为3,试卷试卷包type设置成1.<br>
 * Date:2015-11-6上午11:03:26  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class ZYExamNetCategoryImportSVImpl extends AbstractCategoryImportSV implements
		IZYExamNetCategoryImportSV {
	
	// 默认人卫商城分类目录。
	private static final Long DEFAULT_CATLOG_ID = fetchDefaultCatlogId();

	/** 
	 * TODO 简单描述该方法的实现功能（可选）. 
	 * @see com.ai.ecp.goods.service.common.interfaces.dataimport.IExamNetCategoryImportSV#executeImport(Map)
	 */
	@Override
	public void executeImport(Map<String, Object> map) throws BusinessException {
		if(MapUtils.isEmpty(map)){
			LogUtil.warn(MODULE, "执行考试网分类数据解析时传入数据为空");
			return;
		}
		super.executeImport(map);		
	}
	
	


    @Override
    protected Long getStaffId() {
        return PmphGdsDataImportConstants.Commons.STAFF_ID;
    }



    @Override
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
				icrd.setOriginCatgCode(PmphGdsDataImportConstants.Commons.ZY_EXAM_ORIGN_CODE_PREFIX+reqDTO.getCatgParent());
				icrd.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);
				GdsCategoryRespDTO parent = gdsInterfaceCatgSV.queryCategoryByOriginCatgCode(icrd);
				if(null == parent){
					throw new BusinessException("error.category.dataimport.2",new String[]{reqDTO.getOrigin(),reqDTO.getOriginCatgCode()});
				}
				dto.setCatgParent(parent.getCatgCode());
			}
		}else{
			// 试卷/试卷包
			if(1 == reqDTO.getCatgType()){
			    dto.setCatgParent(PmphGdsDataImportConstants.Commons.TEST_PAPER_CATGCODE);
			}else if(3 == reqDTO.getCatgType()){
				// 辅导班。
				dto.setCatgParent(PmphGdsDataImportConstants.Commons.TUTORIUM_CLASS_CATGCODE);
			}
		}
		dto.setSortNo(reqDTO.getSortNo());
		dto.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		dto.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		dto.setIfAbleEdit(GdsConstants.Commons.STATUS_VALID);
		return dto;
	}

	@Override
	protected GdsInterfaceCatgReqDTO buildGdsInterfaceCatg(
			Map<String, Object> map) {
		GdsInterfaceCatgReqDTO record = new GdsInterfaceCatgReqDTO();
		record.setId(seqGdsInterfaceCatgr.nextValue());
		record.setActionType(GdsDataImportConstants.ActionType.UPDATE);

        String catgParent = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_PARENT));
        if(!"null".equals(catgParent)&&!"0".equals(catgParent)&&StringUtil.isNotEmpty(catgParent)){
           record.setCatgParent(catgParent);
        }
		
		record.setStatus(GdsConstants.Commons.STATUS_VALID);
		
		
		record.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);
		
		String originCatgCode = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_CODE));
		if(!"null".equals(originCatgCode) && StringUtil.isNotBlank(originCatgCode)){
			// 考试网原始编码添加考试网编码前缀.
		    record.setOriginCatgCode(PmphGdsDataImportConstants.Commons.ZY_EXAM_ORIGN_CODE_PREFIX+originCatgCode);
		}
		
		String originCatgName = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_NAME));
		
		if(!"null".equals(originCatgName) && StringUtil.isNotBlank(originCatgName)){
		   record.setOriginCatgName(originCatgName);
		}
		
		record.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		record.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);

		// 排序。
		String orderFlag = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.SORT_NO));
		
		try {
			if(!"null".equals(orderFlag) && StringUtil.isNotBlank(orderFlag)){
			   Integer sortNo = Integer.valueOf(orderFlag);
			   record.setSortNo(sortNo);
			}
		} catch (NumberFormatException e) {
			LogUtil.warn(MODULE, "原始编码为"+originCatgCode+"分类排序转义异异常或者超过99999排序范围，不设置排序。");
		}
		
		
		// 考试网的分类类型(类型:1=试卷，2=试卷包,3=辅导班)目前试卷与试卷包合并到试卷分类，辅导班挂到预建辅导班分类下。
		String type = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.TYPE));
		
		if(!"null".equals(type) && ("1".equals(type)||"3".equals(type))){
			record.setCatgType(StringUtil.isNotBlank(type) ? Integer.valueOf(type) : null);
		}else{
			throw new BusinessException("error.category.dataimport.zy.1", new String[]{record.getOriginCatgCode()});
		}
		
		return record;
	}
	
	private static Long fetchDefaultCatlogId() {
		BaseSysCfgRespDTO respDTO = SysCfgUtil
				.fetchSysCfg(GdsConstants.SysCfgConstants.CATLOG_ID_RENWEI);
		return Long.valueOf(respDTO.getParaValue());
	}

}

