/** 
 * File Name:GdsERPCategorySVImpl.java 
 * Date:2015-10-24下午4:42:40 
 * 
 */ 
package com.ai.ecp.pmph.service.common.impl.dataimport;

import com.ai.ecp.frame.sequence.PaasSequence;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgReqDTO;
import com.ai.ecp.goods.service.common.impl.dataimport.AbstractCategoryImportSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.common.interfaces.dataimport.IERPGdsCategoryImportSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.apache.commons.collections.MapUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description: <br>
 * Date:2015-10-24下午4:42:40  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class ERPGdsCategoryImportSVImpl extends AbstractCategoryImportSV implements
		IERPGdsCategoryImportSV {
	// 默认人卫商城分类目录。
	private static final Long DEFAULT_CATLOG_ID = fetchDefaultCatlogId();
	//获取纸质书分类code.
	private static final String PAPERY_CATG_CODE = fetchPaperyCatgCode();
	
	@Resource(name="seq_gds_interface_catgr")
	private PaasSequence seqGdsInterfaceCatgr;

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
	protected GdsCategoryReqDTO buildGdsCategoryReqDTO(GdsInterfaceCatgReqDTO reqDTO){
			GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
			dto.setCatgName(reqDTO.getOriginCatgName());
			dto.setCatgType(GdsConstants.GdsCategory.CATG_TYPE_1);
			dto.setCatlogId(DEFAULT_CATLOG_ID);
			/*if(StringUtil.isBlank(reqDTO.getCatgParent())){
				dto.setCatgParent(PAPERY_CATG_CODE);
			}
			*/
			dto.setIfShow(GdsConstants.GdsCategory.IF_SHOW_0);
			if (StringUtil.isNotBlank(reqDTO.getCatgParent())) {
				if (StringUtil.isNotBlank(reqDTO.getCatgParent())) {
					GdsInterfaceCatgReqDTO icrd = new GdsInterfaceCatgReqDTO();
					icrd.setOriginCatgCode(reqDTO.getCatgParent());
					icrd.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
					GdsCategoryRespDTO parent = gdsInterfaceCatgSV.queryCategoryByOriginCatgCode(icrd);
					if(null == parent){
						throw new BusinessException("error.category.dataimport.2",new String[]{reqDTO.getOrigin(),reqDTO.getOriginCatgCode()});
					}
					dto.setCatgParent(parent.getCatgCode());
				}
			}else{
				dto.setCatgParent(PAPERY_CATG_CODE);
			}
			dto.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
			dto.setUpdateStaff(dto.getCreateStaff());
			dto.setSortNo(reqDTO.getSortNo());
			dto.setIfAbleEdit(GdsConstants.Commons.STATUS_VALID);
			return dto;
	}
	
	

	@Override
    protected boolean isSkipProcess(GdsInterfaceCatgReqDTO reqDTO) {
        if(PmphGdsDataImportConstants.Commons.ORIGIN_ERP.equals(reqDTO.getOrigin()) && "无".equals(reqDTO.getOriginCatgName())){
            LogUtil.warn(MODULE, "来源分类ERP,原始编码为"+reqDTO.getOriginCatgCode()+"的分类名称为无,忽略添加!");
            return true;
        }
        return false;
    }


    @Override
    protected Long getStaffId() {
        return PmphGdsDataImportConstants.Commons.STAFF_ID;
    }


    protected GdsInterfaceCatgReqDTO buildGdsInterfaceCatg(Map<String, Object> map){
		
		String originCatgCode = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CATG_CODE));
		String originCatgName = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CATG_NAME));
		String catgParent = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.CATG_PARENT));
		String actionType = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.XINXICAOZUO));
		String sortNo = String.valueOf(map.get(DataImportConstants.ERPDataMapKeys.SORT_NO));
		
		GdsInterfaceCatgReqDTO record = new GdsInterfaceCatgReqDTO();
		record.setId(seqGdsInterfaceCatgr.nextValue());
		record.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
		if(!"null".equals(originCatgCode) && StringUtil.isNotEmpty(originCatgCode)){
			record.setOriginCatgCode(originCatgCode);
		}
		if(!"null".equals(originCatgName)&&StringUtil.isNotEmpty(originCatgName)){
		    record.setOriginCatgName(originCatgName);
		}
		if(!"null".equals(catgParent) && StringUtil.isNotEmpty(catgParent)){
		    record.setCatgParent(catgParent);
		}
		record.setActionType(null != actionType ? Integer.valueOf(actionType) : null);
		if(!"null".equals(sortNo) && StringUtils.isNotEmpty(sortNo)){
		   record.setSortNo(null != sortNo ? Integer.valueOf(sortNo) : null);
		}
		record.setStatus(GdsConstants.Commons.STATUS_VALID);
		record.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
		record.setUpdateStaff(record.getCreateStaff());
		return record;
	}
	
	
	private static Long fetchDefaultCatlogId(){
		BaseSysCfgRespDTO respDTO = SysCfgUtil.fetchSysCfg(GdsConstants.SysCfgConstants.CATLOG_ID_RENWEI);
		return Long.valueOf(respDTO.getParaValue());
	}
	
	
	private static String fetchPaperyCatgCode(){
		BaseSysCfgRespDTO respDTO = SysCfgUtil.fetchSysCfg(GdsConstants.SysCfgConstants.PAPERY_CATG_CODE);
		return respDTO.getParaValue();
	}

}

