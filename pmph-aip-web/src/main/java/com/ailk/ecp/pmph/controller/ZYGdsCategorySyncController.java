/** 
 * File Name:ZYGdsCategorySyncController.java 
 * Date:2015-11-2下午2:10:49 
 * 
 */ 
package com.ailk.ecp.pmph.controller;

import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceCatgRSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IZYGdsCategoryImportRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.ZYGdsCategory;
import com.ailk.ecp.pmph.vo.ZYGdsCategoryUpdateReq;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name:ecp-aip-web <br>
 * Description: 泽元数字教材/电子书分类全量同步Controller<br>
 * Date:2015-11-2下午2:10:49  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
@Controller
public class ZYGdsCategorySyncController extends BaseController {
	
	    private static final String MODULE = ZYGdsCategorySyncController.class.getName();
	
	    
	    //private IZYGdsCategoryBatchImportRSV zyGdsCategoryBatchImportRSV;
	    @Resource(name="zyGdsCategoryImportRSV")
	    private IZYGdsCategoryImportRSV zyGdsCategoryImportRSV;
	    @Resource(name="gdsInterfaceCatgRSV")
	    private IGdsInterfaceCatgRSV gdsInterfaceCatgRSV;

	    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.goods.zyGdsCategorySync")
	    @Security(mustLogin=true,authorCheckType=DefaultServiceCheckChain.class)
	    @ResponseBody
	    public Map<String, Object> executeSync(ZYGdsCategoryUpdateReq categoryUpdateReq) throws BusinessException{
	    	
	    	LogUtil.info(MODULE, "=================接收到泽元数字教材电子数分类全量同步数据JSON="+categoryUpdateReq.getCategorys());
	    	List<ZYGdsCategory> lst = null;//categoryUpdateReq.getCategorys();
	    	Map<String, Object> result = new HashMap<String, Object>();
	    	try{
	    	   lst = JSONObject.parseArray(categoryUpdateReq.getCategorys(), ZYGdsCategory.class);
	    	}catch (Exception e) {
	    		  LogUtil.error(MODULE, "传入JSON数据结构不合法!");
	    		  result.put(RespUtil.CODE, 2);
	    	      result.put(RespUtil.MSG, "JSON数据不合法,解析异常!"+e.getMessage());
	    	      return RespUtil.renderRootResp(result);	    	
			}
	    	try{
	    	   List<Map<String, Object>> dataMaps = createDataMaps(lst);
	    	   if(CollectionUtils.isNotEmpty(dataMaps)){
	    		   
	    		   //List<GdsInterfaceCatgReqDTO> interfactCatgLst = new ArrayList<GdsInterfaceCatgReqDTO>();
	    			// 从数据集中提取出该批次全量有效原始分类编码。
	    			//List<String> validCatgCodes = getValidCatgCodes(dataMaps);
	    			// 查询出当前属于泽元有效分类关系。
	    			//PageResponseDTO<GdsInterfaceCatgRespDTO> page = this.queryCurrentValidInterfaceCatg();
	    		   
	    		   List<String> errMsg = new ArrayList<String>();
	    		   
	    		   for(Map<String, Object> data : dataMaps){
	    			   try{
	    			      zyGdsCategoryImportRSV.executeImport(data);
	    			   }catch (Exception e) {
						  if(e instanceof BusinessException){
							  errMsg.add(((BusinessException) e).getErrorMessage());
						  }else{
							  errMsg.add(e.getMessage());
						  }
					  }
	    		   }
	    		   // 因比对删除分类会造成较大隐患,所以将清理代码注释掉.(Modify by liyong at 2015-11-14 11:43)
	    		   //clearDatas(validCatgCodes, page);
	    		   
	    		   if(!errMsg.isEmpty()){
	    			   result.put(RespUtil.CODE,2);
	    			   StringBuffer buf = new StringBuffer();
	    			   for(String s : errMsg){
	    				   buf.append(s);
	    				   buf.append(",");
	    			   }
	    			   if(buf.length() > 0){
	    				   buf.deleteCharAt(buf.length() - 1);
	    			   }
			    	   result.put(RespUtil.MSG, "数字教材电子书分类同步失败!失败原因:"+buf.toString());  
	    		   }else{
	    			   result.put(RespUtil.CODE, 1);
			    	   result.put(RespUtil.MSG, "数字教材电子书分类同步成功!");
			    	   LogUtil.info(MODULE, "=================泽元数字教材电子数分类全量同步数据执行成功!");
			    	   
	    		   }
	    	   }else{
	    		  LogUtil.error(MODULE, "传入数据集为空!不执行任务同步操作");
	    		  result.put(RespUtil.CODE, 2);
	    	      result.put(RespUtil.MSG, "传入数据集为空!不执行任务同步操作!");
	    	      LogUtil.info(MODULE, "=================泽元数字教材电子数分类全量同步数据结束!");
	    	   }
	    	   
	    	}catch (Exception e) {
				LogUtil.error(MODULE, "执行全量同步异常!",e);
				result.put(RespUtil.CODE, 2);
				if(e instanceof BusinessException){
					result.put(RespUtil.MSG, "数字教材电子书分类同步分类全量同步异常!"+((BusinessException)e).getErrorMessage());
				}else{
					result.put(RespUtil.MSG, "数字教材电子书分类同步分类全量同步内部异常!");
				}
			}
            return RespUtil.renderRootResp(result);	    	
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
	    
	    
	    private void clearDatas(List<String> validCatgCodes,
				PageResponseDTO<GdsInterfaceCatgRespDTO> page) {
			/*if(!CollectionUtils.isEmpty(validCatgCodes) && null != page && !CollectionUtils.isEmpty(page.getResult()) ){
				for(GdsInterfaceCatgRespDTO obj : page.getResult())
				{
					if(!validCatgCodes.contains(obj.getOriginCatgCode())){
						GdsInterfaceCatgReqDTO delCondition = new GdsInterfaceCatgReqDTO();
						ObjectCopyUtil.copyObjValue(obj, delCondition, null, true);
						zyGdsCategoryImportRSV.deleteCategory(delCondition);
					}
				}
			}*/
		}
	    
	    
	    private PageResponseDTO<GdsInterfaceCatgRespDTO> queryCurrentValidInterfaceCatg(){
			GdsInterfaceCatgReqDTO reqDTO = new GdsInterfaceCatgReqDTO();
			reqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
			reqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN);
			// 排除掉考试网分类.
			reqDTO.setOriginCatgCodeExcludePrefix(PmphGdsDataImportConstants.Commons.ZY_EXAM_ORIGN_CODE_PREFIX);
			reqDTO.setPageSize(Integer.MAX_VALUE);
			PageResponseDTO<GdsInterfaceCatgRespDTO> page = gdsInterfaceCatgRSV.queryPaging(reqDTO);
			return page;
		}
	    
	    private List<Map<String, Object>> createDataMaps(List<ZYGdsCategory> lst){
	    	List<Map<String, Object>> mapLst = new ArrayList<Map<String, Object>>();
	    	
	    	if(CollectionUtils.isNotEmpty(lst)){
	    		for(ZYGdsCategory gdsCatg : lst){
	    			Map<String, Object> dataMap = new HashMap<String, Object>();
	    			Long id = gdsCatg.getId();
	    			String name = gdsCatg.getName();
	    			Long sortNo = gdsCatg.getSort();
	    			Long parentId = gdsCatg.getParentId();
	    			Integer type = gdsCatg.getCatgType();
	    			// 转换成数据集.
	    			dataMap.put(DataImportConstants.ZYDataMapKeys.CATG_CODE,  null != id ? id.toString() : null);
	    			dataMap.put(DataImportConstants.ZYDataMapKeys.CATG_NAME, StringUtil.isNotBlank(name) ? name:null);
	    			dataMap.put(DataImportConstants.ZYDataMapKeys.CATG_PARENT, null != parentId ? parentId.toString() : null);
	    			dataMap.put(DataImportConstants.ZYDataMapKeys.SORT_NO, null != sortNo ? sortNo.toString() : null);
	    			dataMap.put(DataImportConstants.ZYDataMapKeys.TYPE, null != type ? type.toString() : null);
	    			
	    			mapLst.add(dataMap);
	    		}
	    	}
	    	return mapLst;
	    }
	    
	    
	   /* @InitBinder
	    protected void initBinder(WebDataBinder binder) {
	       // binder.registerCustomEditor(ZYGdsCategory.class,new JsonCategorysEditor());
	    }*/
	    
	    
	   
	    
	    
}

