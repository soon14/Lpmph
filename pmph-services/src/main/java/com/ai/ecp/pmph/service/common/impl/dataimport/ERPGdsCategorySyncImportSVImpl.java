/** 
 * File Name:GdsERPCategorySVImpl.java 
 * Date:2015-10-24下午4:42:40 
 * 
 */ 
package com.ai.ecp.pmph.service.common.impl.dataimport;

import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCatgSyncReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.goods.service.common.impl.dataimport.AbstractCategorySyncImportSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IERPGdsCategoryImportSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.apache.commons.collections.MapUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description: <br>
 * Date:2015-10-24下午4:42:40  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class ERPGdsCategorySyncImportSVImpl extends AbstractCategorySyncImportSV implements
		IERPGdsCategoryImportSV {
	
	private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	
	@Override
	public void executeImport(Map<String, Object> map)
			throws BusinessException {
		if(MapUtils.isEmpty(map)){
			LogUtil.warn(MODULE, "执行ERP分类数据解析时传入数据为空");
			return;
		}
		super.executeImport(map);
	}
	
    @Override
	protected GdsCatgSyncReqDTO buildGdsCatgSyncReqDTO(Map<String, Object> map){
		
		String originCatgCode = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CATG_CODE));
		String originCatgName = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CATG_NAME));
		String catgParent = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CATG_PARENT));
		String actionType = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.XINXICAOZUO));
		String sortNo = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.SORT_NO));
		String orgCatgParentName = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CATG_PARENT_NAME));
		String operTime = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CAOZUORIQI));
		
		GdsCatgSyncReqDTO record = new GdsCatgSyncReqDTO();
		// 直接设置来源系统为ERP-01
		record.setSrcSystem(PmphGdsDataImportConstants.SrcSystem.ERP_01);
		if(!"null".equals(originCatgCode) && StringUtil.isNotEmpty(originCatgCode)){
			record.setCatgCode(originCatgCode);
		}
		if(!"null".equals(originCatgName)&&StringUtil.isNotEmpty(originCatgName)){
		    record.setCatgName(originCatgName);
		}
		if(!"null".equals(catgParent) && StringUtil.isNotEmpty(catgParent)){
		    record.setCatgParent(catgParent);
		}else{
			record.setCatgParent(GdsDataImportConstants.Commons.ROOT_CATG_PARENT_CODE);
		}
		record.setActionType(null != actionType ? Integer.valueOf(actionType) : null);
		if(!"null".equals(sortNo) && StringUtils.isNotEmpty(sortNo) && sortNo.length() <= 5){
		   record.setSortNo(null != sortNo ? Integer.valueOf(sortNo) : null);
		}
		if(!"null".equals(orgCatgParentName) && StringUtils.isNotEmpty(orgCatgParentName)){
			   record.setCatgParentName(orgCatgParentName);
		}
		try{
			if(!"null".equals(operTime) && StringUtils.isNotEmpty(operTime)){
			   record.setOperTime(getOperTime(operTime));
			}
		}catch (Exception e) {
			LogUtil.warn(MODULE, "operTime=["+operTime+"],OperTime格式异常!忽略操作时间写入");
		}
		record.setStatus(GdsConstants.Commons.STATUS_VALID);
		record.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		record.setUpdateStaff(record.getCreateStaff());
		return record;
	}
    
    
    
    @Override
    protected boolean isSkipProcess(GdsCatgSyncReqDTO reqDTO) {
        if(PmphGdsDataImportConstants.SrcSystem.ERP_01.equals(reqDTO.getSrcSystem()) && "无".equals(reqDTO.getCatgName())){
            LogUtil.warn(MODULE, "来源为"+reqDTO.getSrcSystem()+",原始编码为"+reqDTO.getCatgCode()+"的分类名称为无,忽略更新!");
            return true;
        }
        return false;
    }

    @Override
    protected Long getStaffId() {
        return PmphGdsDataImportConstants.Commons.STAFF_ID;
    }

    private static final Timestamp getOperTime(String operTime){
    	if(!"null".equals(operTime) && StringUtil.isNotEmpty(operTime)){
    		try {
    		    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
    			java.util.Date date = df.parse(operTime);
    			Timestamp d = new Timestamp(date.getTime());
    			return d;
    		} catch (Exception e) {
    			throw new RuntimeException("系统转换日期字符串时出错！", e);
    		}
    	}
    	return null;
    }
	

}

