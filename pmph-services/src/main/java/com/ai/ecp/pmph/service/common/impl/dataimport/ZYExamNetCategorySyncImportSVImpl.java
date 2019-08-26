package com.ai.ecp.pmph.service.common.impl.dataimport;

import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCatgSyncReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.goods.service.common.impl.dataimport.AbstractCategorySyncImportSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IZYExamNetCategoryImportSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * 
 * Project Name:ecp-services-goods-server <br>
 * Description: 泽元电子书分类导入服务实现<br>
 * Date:2015-10-27下午2:22:12 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ZYExamNetCategorySyncImportSVImpl extends AbstractCategorySyncImportSV implements
		IZYExamNetCategoryImportSV {

	@Override
	public void executeImport(Map<String, Object> map) throws BusinessException {
		
		if (MapUtils.isEmpty(map)) {
			LogUtil.warn(MODULE, "考试网分类数据解析时传入数据为空");
			return;
		}
        super.executeImport(map);
	}
	
	
	

    @Override
    protected Long getStaffId() {
        return PmphGdsDataImportConstants.Commons.STAFF_ID;
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
	@Override
	protected GdsCatgSyncReqDTO buildGdsCatgSyncReqDTO(Map<String, Object> map) {
		GdsCatgSyncReqDTO record = new GdsCatgSyncReqDTO();
		record.setActionType(GdsDataImportConstants.ActionType.UPDATE);

        String catgParent = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_PARENT));
        if(!"null".equals(catgParent) && !"0".equals(catgParent) && StringUtil.isNotEmpty(catgParent)){
          record.setCatgParent(catgParent);
        }else{
        	record.setCatgParent(GdsDataImportConstants.Commons.ROOT_CATG_PARENT_CODE);
        }
		
		record.setStatus(GdsConstants.Commons.STATUS_VALID);
		
		// 1-辅导班  2-试卷
		String type = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.TYPE));
		
		if(!"null".equals(type) && ("1".equals(type)||"2".equals(type))){
			record.setCatgType(type);
		}else{
			throw new BusinessException("error.category.dataimport.zy.1", new String[]{record.getCatgCode()});
		}
		record.setSrcSystem(PmphGdsDataImportConstants.SrcSystem.ZY_02);
		String originCatgCode = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_CODE));
		
		if(!"null".equals(originCatgCode)&&StringUtil.isNotBlank(originCatgCode)){
		     record.setCatgCode(null != originCatgCode ? originCatgCode : null);
		}
		
		String originCatgName = String.valueOf(map.get(DataImportConstants.ZYDataMapKeys.CATG_NAME));
		if(!"null".equals(originCatgName)&&StringUtil.isNotBlank(originCatgName)){
		     record.setCatgName(originCatgName);
		}
		
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
		
		record.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		record.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
				
		return record;
	}
	
}
