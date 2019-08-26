package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.goods.dao.mapper.busi.GdsExcelImpMapper;
import com.ai.ecp.goods.dao.model.GdsExcelImp;
import com.ai.ecp.goods.dao.model.GdsExcelImpCriteria;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.excelImp.ExcelImportGdsModelDTO;
import com.ai.ecp.goods.dubbo.dto.excelImp.GdsExcelImpPropValueInfo;
import com.ai.ecp.goods.dubbo.dto.excelImp.GdsExcelImpReqDTO;
import com.ai.ecp.goods.dubbo.dto.excelImp.GdsExcelImpRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoAddReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2CatgReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2PropReqDTO;
import com.ai.ecp.goods.service.busi.impl.excelImp.GdsExcelImpSVImpl;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsInfoQuerySV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GdsExcelImpPmphSVImpl extends GdsExcelImpSVImpl  {

	@Resource
	private GdsExcelImpMapper gdsExcelImpMapper;
	@Resource
	private IShopInfoRSV shopInfoRSV;
	@Resource
	private IGdsInfoQuerySV gdsInfoQuerySV;
	
	
	private static final String MODULE = GdsExcelImpPmphSVImpl.class.getName();
	// 正则表达式
	// 数字
	private static final String digitalReg = "[0-9]+";

	private static final String VALIDATE_ERROR_CATG_CODE = "分类编码不合法！";



	@Override
	public GdsInfoAddReqDTO transformAddGdsExcelImp(GdsExcelImpReqDTO excelImpReqDTO)throws Exception {
		GdsInfoAddReqDTO addReqDTO = new GdsInfoAddReqDTO();
		GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
		gdsInfoReqDTO.setGdsName(excelImpReqDTO.getGdsName());
		gdsInfoReqDTO.setGdsTypeId(excelImpReqDTO.getGdsType());
		// 特别处理企业编码
		ShopInfoResDTO shopInfoResDTO = shopInfoRSV.findShopInfoByShopID(excelImpReqDTO.getShopId());
		gdsInfoReqDTO.setCompanyId(shopInfoResDTO.getCompanyId());
		gdsInfoReqDTO.setShopId(excelImpReqDTO.getShopId());
		//设置指导价
		gdsInfoReqDTO.setGuidePrice(excelImpReqDTO.getGdsPrice());
		//设置主分类
		gdsInfoReqDTO.setMainCatgs(excelImpReqDTO.getCatgCode());
		gdsInfoReqDTO.setGdsSubHead(excelImpReqDTO.getGdsTitle());
		addReqDTO.setCompanyId(shopInfoResDTO.getCompanyId());
	
		addReqDTO.setGdsInfoReqDTO(gdsInfoReqDTO);
		//拼接单品信息
		List<GdsSkuInfoReqDTO> gdsSkuInfoReqDTOs = new ArrayList<GdsSkuInfoReqDTO>();
		GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
		gdsSkuInfoReqDTO.setRealCount(excelImpReqDTO.getGdsStock());
		gdsSkuInfoReqDTO.setGdsTypeId(excelImpReqDTO.getGdsType());
		gdsSkuInfoReqDTO.setCompanyId(shopInfoResDTO.getCompanyId());
		gdsSkuInfoReqDTO.setCommonPrice(excelImpReqDTO.getGdsPrice());
		gdsSkuInfoReqDTOs.add(gdsSkuInfoReqDTO);
		addReqDTO.setSkuInfoReqDTOs(gdsSkuInfoReqDTOs);
		//商品分类
		List<GdsGds2CatgReqDTO> catgReqDTOs = new ArrayList<GdsGds2CatgReqDTO>();
		addReqDTO.setGds2CatgReqDTOs(catgReqDTOs);
		GdsGds2CatgReqDTO catgReqDTO = new GdsGds2CatgReqDTO();
		catgReqDTO.setCatgCode(excelImpReqDTO.getCatgCode());
		catgReqDTO.setCatgType(GdsConstants.GdsCategory.CATG_TYPE_1);
		catgReqDTO.setGds2catgType("1");
		catgReqDTO.setGdsName(excelImpReqDTO.getGdsName());
		catgReqDTOs.add(catgReqDTO);
//		catgReqDTO.setCatgPath(catgPath);
		// 保存普通参数/扩展 属性信息到商品属性关系表
		List<GdsGds2PropReqDTO> props = new ArrayList<GdsGds2PropReqDTO>();

		// 保存单品属性到商品属性关系表
		List<GdsGds2PropReqDTO> skuParams = new ArrayList<GdsGds2PropReqDTO>();
		addReqDTO.setGds2PropReqDTOs(props);
		addReqDTO.setSkuProps(skuParams);
		if(StringUtil.isNotBlank(excelImpReqDTO.getGdsPropStr() )){
			if (excelImpReqDTO.getGdsPropStr() != null ) {
				JSONArray jsonArray = JSONArray.fromObject(excelImpReqDTO.getGdsPropStr());
				Iterator iterator = jsonArray.iterator();
				while (iterator.hasNext()) {

					JSONObject jsonObject = (JSONObject) iterator.next();
					GdsExcelImpPropValueInfo excelImpPropValueInfo =	(GdsExcelImpPropValueInfo) JSONObject.toBean(jsonObject, GdsExcelImpPropValueInfo.class);
					// 特别处理isbn号
					if (1002 == excelImpPropValueInfo.getPropId()) {
						gdsInfoReqDTO.setIsbn(excelImpPropValueInfo.getPropValue());
					}
					GdsGds2PropReqDTO gds2PropReqDTO = new GdsGds2PropReqDTO(); 
					ObjectCopyUtil.copyObjValue(excelImpPropValueInfo, gds2PropReqDTO, null, false);
					//按照属性类型分别存放属性
					if(GdsConstants.GdsProp.PROP_TYPE_1.equals(excelImpPropValueInfo.getPropType())){
				
						skuParams.add(gds2PropReqDTO);
					}else{
						props.add(gds2PropReqDTO);
						
					}
					
				}
			}
		}
		

		return addReqDTO;
	}

	
	@Override
	public GdsInfoAddReqDTO transformEditGdsExcelImp(GdsExcelImpReqDTO excelImpReqDTO)throws Exception {
		GdsInfoAddReqDTO addReqDTO = new GdsInfoAddReqDTO();
		addReqDTO.setStaff(excelImpReqDTO.getStaff());
		GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
		gdsInfoReqDTO.setStaff(excelImpReqDTO.getStaff());
		gdsInfoReqDTO.setGdsName(excelImpReqDTO.getGdsName());
		gdsInfoReqDTO.setId(excelImpReqDTO.getGdsId());
		gdsInfoReqDTO.setGdsSubHead(excelImpReqDTO.getGdsTitle());
		
	//获取商品信息
		
		GdsInfoRespDTO gdsInfoRespDTO = gdsInfoQuerySV.queryGdsInfoByOption(gdsInfoReqDTO, new GdsQueryOption[]{
				GdsQueryOption.BASIC,GdsQueryOption.CATG
		}, new SkuQueryOption[]{SkuQueryOption.BASIC});
		gdsInfoReqDTO.setGdsTypeId(gdsInfoRespDTO.getGdsTypeId());
//		// 特别处理企业编码
//		ShopInfoResDTO shopInfoResDTO = shopInfoRSV.findShopInfoByShopID(excelImpReqDTO.getShopId());
		gdsInfoReqDTO.setShopId(gdsInfoRespDTO.getShopId());
		//设置指导价
		gdsInfoReqDTO.setGuidePrice(gdsInfoRespDTO.getGuidePrice());
		//设置主分类
		gdsInfoReqDTO.setMainCatgs(gdsInfoRespDTO.getMainCatgs());
//		gdsInfoReqDTO.setGdsSubHead(gdsInfoRespDTO.getGdsSubHead());
	
		addReqDTO.setGdsInfoReqDTO(gdsInfoReqDTO);
		//拼接单品信息
		GdsSkuInfoRespDTO skuDto = gdsInfoRespDTO.getSkus().get(0);
		List<GdsSkuInfoReqDTO> gdsSkuInfoReqDTOs = new ArrayList<GdsSkuInfoReqDTO>();
		GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
		gdsSkuInfoReqDTO.setStaff(excelImpReqDTO.getStaff());
		gdsSkuInfoReqDTO.setId(skuDto.getId());
		gdsSkuInfoReqDTOs.add(gdsSkuInfoReqDTO);
		addReqDTO.setSkuInfoReqDTOs(gdsSkuInfoReqDTOs);
	
//		商品分类
		List<GdsGds2CatgReqDTO> catgReqDTOs = new ArrayList<GdsGds2CatgReqDTO>();
		addReqDTO.setGds2CatgReqDTOs(catgReqDTOs);
		GdsGds2CatgReqDTO catgReqDTO = new GdsGds2CatgReqDTO();
		catgReqDTO.setStaff(excelImpReqDTO.getStaff());
		catgReqDTO.setCatgCode(		gdsInfoRespDTO.getMainCategory().getCatgCode());
		catgReqDTO.setCatgType(GdsConstants.GdsCategory.CATG_TYPE_1);
		catgReqDTO.setGds2catgType("1");
		catgReqDTO.setGdsName(excelImpReqDTO.getGdsName());
		catgReqDTOs.add(catgReqDTO);
		catgReqDTO.setCatgPath(gdsInfoRespDTO.getMainCategory().getCatgPath());
		// 保存普通参数/扩展 属性信息到商品属性关系表
		List<GdsGds2PropReqDTO> props = new ArrayList<GdsGds2PropReqDTO>();

		// 保存单品属性到商品属性关系表
		List<GdsGds2PropReqDTO> skuParams = new ArrayList<GdsGds2PropReqDTO>();
		addReqDTO.setGds2PropReqDTOs(props);
		addReqDTO.setSkuProps(skuParams);
		if(StringUtil.isNotBlank(excelImpReqDTO.getGdsPropStr() )){
			if (excelImpReqDTO.getGdsPropStr() != null ) {
				JSONArray jsonArray = JSONArray.fromObject(excelImpReqDTO.getGdsPropStr());
				Iterator iterator = jsonArray.iterator();
				while (iterator.hasNext()) {

					JSONObject jsonObject = (JSONObject) iterator.next();
					GdsExcelImpPropValueInfo excelImpPropValueInfo =	(GdsExcelImpPropValueInfo) JSONObject.toBean(jsonObject, GdsExcelImpPropValueInfo.class);
					// 特别处理isbn号
					if (1002 == excelImpPropValueInfo.getPropId()) {
						gdsInfoReqDTO.setIsbn(excelImpPropValueInfo.getPropValue());
					}
					GdsGds2PropReqDTO gds2PropReqDTO = new GdsGds2PropReqDTO(); 
					gds2PropReqDTO.setStaff(excelImpReqDTO.getStaff());
					ObjectCopyUtil.copyObjValue(excelImpPropValueInfo, gds2PropReqDTO, null, false);
					//按照属性类型分别存放属性
					if(GdsConstants.GdsProp.PROP_TYPE_1.equals(excelImpPropValueInfo.getPropType())){
				
						skuParams.add(gds2PropReqDTO);
					}else{
						props.add(gds2PropReqDTO);
						
					}
					
				}
			}
		}
		

		return addReqDTO;
	}

}
