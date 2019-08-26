package com.ai.ecp.pmph.service.busi.impl.dataimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.prom.service.busi.importdata.impl.SkuInfoSwitchExSVImpl;

public class PmphSkuInfoSwitchSVImpl  extends SkuInfoSwitchExSVImpl{
	
	@Resource
	private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
	
	private String regex = "^(-?[0-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";

	@Override
	public String switchSkuId(List<Object> row) {
		// TODO Auto-generated method stub
		String skuId = "";
		String pId = "";
     	//制品编码和单品编号skuid的转换
     	GdsSku2PropPropIdxReqDTO reqDTO = new GdsSku2PropPropIdxReqDTO();
     	// 单品编码
 		boolean item0 = row.get(0).toString().matches(regex);
 		if (!(item0)) {
 			throw new BusinessException("prom.400188", new String[] { row.get(0).toString() });
 		}
 		pId = row.get(0).toString();
 		if (pId.indexOf(".") > 0) {
 			pId = pId.replaceAll("0+?$", "");// 去掉多余的0
 			pId = pId.replaceAll("[.]$", "");// 如最后一位是.则去掉
 		}
     	reqDTO.setPropValue(pId);//制品编号
     	reqDTO.setPropId(1004L);
     	reqDTO.setPageSize(10);//默认10条
     	PageResponseDTO<GdsSkuInfoRespDTO> pageDTO = gdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(reqDTO);   
     	if(pageDTO!=null&&pageDTO.getResult()!=null&&pageDTO.getResult().size()>0){     		    		
 			if(row.get(5).toString().startsWith("0")){//实体商品处理
 				for(GdsSkuInfoRespDTO dto :pageDTO.getResult()){
 					if(dto.getGdsTypeId().longValue()==1l){
 						if (dto.getGdsStatus().equals("33") || dto.getGdsStatus().equals("99")) {
							continue;
						} else {							
							skuId = ""+dto.getId();
						}
 					}else{
 						continue;
 					}
 				}
 			}else{//虚拟商品处理
 				for(GdsSkuInfoRespDTO dto :pageDTO.getResult()){
 					if(dto.getGdsTypeId().longValue()!=1l){
 						if (dto.getGdsStatus().equals("33") || dto.getGdsStatus().equals("99")) {
							continue;
						} else {							
							skuId = ""+dto.getId();
						}
 					}else{
 						continue;
 					}
 				}
 			} 			
 			if(skuId.equals("")){
 				throw new BusinessException("prop.400212");
 			}
     	}else{
     		throw new BusinessException("prop.400212");
     	}
     	
		return skuId;
	}

	@Override
	public  Map<String,Object> filterDatas(List<List<Object>> datas) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
			//数据不做重复过滤
	        map.put("totalCount", datas.size());
	        map.put("repeatCount", 0);//允许第一列重复
	        map.put("resultDatas", datas);
	        return map;
	}
	
	@Override
	public void checkedRow(List<Object> row) {
		// TODO Auto-generated method stub
		if(row.get(5)==null||row.get(5).toString().equals("")){
			throw new BusinessException("是否虚拟商品字段不能为空");
		}
		String isV = row.get(5).toString();
 		if (isV.indexOf(".") > 0) {
 			isV = isV.replaceAll("0+?$", "");// 去掉多余的0
 			isV = isV.replaceAll("[.]$", "");// 如最后一位是.则去掉
 		}
		if(isV.equals("0")||isV.equals("1")){
			
		}else{
			throw new BusinessException("是否虚拟商品字段必须为0或者1");
		}
	}

}
