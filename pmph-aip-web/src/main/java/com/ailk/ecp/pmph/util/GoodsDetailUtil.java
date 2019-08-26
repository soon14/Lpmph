package com.ailk.ecp.pmph.util;

import com.ai.ecp.base.util.ApplicationContextUtil;
import com.ai.ecp.goods.dubbo.constants.GdsOption;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropValueRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoDetailRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;

import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class GoodsDetailUtil {
	private static String MODULE = GoodsDetailUtil.class.getName();
	private static IGdsInfoQueryRSV gdsInfoQueryRSV = (IGdsInfoQueryRSV) ApplicationContextUtil
			.getBean("gdsInfoQueryRSV", IGdsInfoQueryRSV.class);

	public static GdsInfoDetailRespDTO getGdsDetailByGdsId(GdsInfoReqDTO reqDto) {
		LogUtil.info(MODULE, "获取商品详情开始");

		if ((reqDto == null) || (StringUtil.isEmpty(reqDto.getId()))) {
			LogUtil.info(MODULE, "获取商品详情结束:参数错误");
			return null;
		}
		if (null == reqDto.getPropIds())
			reqDto.setPropIds(new ArrayList<Long>());
		else {
			reqDto.setPropIds(new ArrayList<Long>(reqDto.getPropIds()));
		}

		GdsInfoDetailRespDTO gdsInfoDetailRespDTO = null;
		try {
			gdsInfoDetailRespDTO = gdsInfoQueryRSV.queryGdsInfoDetail(reqDto);
		} catch (Exception e) {
			LogUtil.error(MODULE, "查询商品详情出异常", e);
			throw e;
		}
		LogUtil.info(MODULE, "获取商品详情结束:成功");
		return gdsInfoDetailRespDTO;
	}

	public static List<GdsInfoDetailRespDTO> analysJsonObjToGdsDetialList(JSONObject jsonResult, List<Long> props,
			Long staffId) {
		LogUtil.info(MODULE, "批量将行为分析返回的json转成商品详情列表开始");

		if ((jsonResult == null) || (jsonResult.isEmpty())) {
			LogUtil.info(MODULE, "批量调用用户行为分析解析商品详情结束:json为空");
			return null;
		}
		if (!(jsonResult.containsKey("goodsList"))) {
			LogUtil.info(MODULE, "批量调用用户行为分析解析商品分类结束:不存在goodsList");
			return null;
		}
		if (null == props) {
			props = new ArrayList<Long>();
		}

		JSONArray jsonGoodsArray = jsonResult.getJSONArray("goodsList");
		if ((jsonGoodsArray == null) || (jsonGoodsArray.isEmpty())) {
			LogUtil.info(MODULE, "批量调用用户行为分析解析商品分类结束:goodsList为空");
			return null;
		}

		List gdsInfoDetailRespList = new ArrayList();
		int goodsCount = jsonGoodsArray.size();
		for (int i = 0; i < goodsCount; ++i) {
			JSONObject gdsJson = jsonGoodsArray.getJSONObject(i);
			GdsInfoDetailRespDTO gdsInfo = null;
			try {
				gdsInfo = analysJsonObjToGdsDetial(gdsJson, new ArrayList(props), staffId);
			} catch (Exception e) {
				LogUtil.error(MODULE, "将行为分析返回的json转成商品详情异常");
			}
			if ((gdsInfo != null) && (StringUtil.isNotEmpty(gdsInfo.getId())))
				gdsInfoDetailRespList.add(gdsInfo);
		}

		LogUtil.info(MODULE, "批量将行为分析返回的json转成商品详情列表结束：完成");
		return gdsInfoDetailRespList;
	}

	public static GdsInfoDetailRespDTO analysJsonObjToGdsDetial(JSONObject gdsJson, List<Long> props, Long staffId) {
		LogUtil.info(MODULE, "将行为分析返回的json转成商品详情开始");

		if ((gdsJson == null) || (gdsJson.isEmpty())) {
			LogUtil.info(MODULE, "将行为分析返回的json转成商品详情结束：json为空");
			return null;
		}
		if (!(gdsJson.containsKey("gdsId"))) {
			LogUtil.info(MODULE, "将行为分析返回的json转成商品详情结束：gdsId为空");
			return null;
		}
		if (null == props) {
			props = new ArrayList();
		}

		GdsInfoDetailRespDTO gdsInfo = null;
		Long gdsId = Long.valueOf(gdsJson.getLong("gdsId"));
		Long skuId = null;
		String gdsDetialUrl = null;
		if (gdsJson.containsKey("skuId")) {
			skuId = Long.valueOf(gdsJson.getLong("skuId"));
		}

		if (StringUtil.isNotEmpty(gdsId)) {
			gdsDetialUrl = "/gdsdetail/" + gdsId.toString() + "-";

			GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
			GdsOption.GdsQueryOption[] gdsOptions = { GdsOption.GdsQueryOption.BASIC };

			GdsOption.SkuQueryOption[] skuOptions = { GdsOption.SkuQueryOption.BASIC,
					GdsOption.SkuQueryOption.CAlDISCOUNT };

			gdsInfoReqDTO.setGdsQueryOptions(gdsOptions);
			gdsInfoReqDTO.setSkuQuerys(skuOptions);
			gdsInfoReqDTO.setId(gdsId);
			if (null != staffId)
				gdsInfoReqDTO.setStaffId(staffId);

			if (StringUtil.isNotEmpty(skuId)) {
				gdsDetialUrl = gdsDetialUrl + skuId.toString();
				gdsInfoReqDTO.setSkuId(skuId);
			}
			gdsInfo = getGdsDetailByGdsId(gdsInfoReqDTO);
			gdsInfo.setUrl(gdsDetialUrl);
		}

		if ((gdsInfo != null) && (StringUtil.isNotEmpty(gdsInfo.getId()))) {
			GdsSkuInfoRespDTO skuInfo = gdsInfo.getSkuInfo();
			Map allPropMaps = null;
			if (gdsJson.containsKey("skuProps")) {
				allPropMaps = analysJsonObjToPropMap(gdsJson.getJSONArray("skuProps"), props);
			}

			gdsInfo.setAllPropMaps(allPropMaps);
			if (null != skuInfo) {
				skuInfo.setAllPropMaps(allPropMaps);
			}
			
			if (gdsJson.containsKey("tradeAmount")) { // 销售数量
				gdsInfo.setExt1(String.valueOf(gdsJson.getLong("tradeAmount")));
			}
		}
		LogUtil.info(MODULE, "将行为分析返回的json转成商品详情结束");
		return gdsInfo;
	}

	public static Map<String, GdsPropRespDTO> analysJsonObjToPropMap(JSONArray jsonPropArray, List<Long> props) {
		LogUtil.info(MODULE, "将行为分析返回的json转成商品属性开始");

		if (CollectionUtils.isEmpty(jsonPropArray)) {
			LogUtil.info(MODULE, "将行为分析返回的json转成商品属性结束:json为空");
			return null;
		}
		if (CollectionUtils.isEmpty(props)) {
			LogUtil.info(MODULE, "将行为分析返回的json转成商品属性结束:要查找属性为空");
			return null;
		}
		props = new ArrayList(props);

		Map propMap = new HashMap();
		int jsonArraySize = jsonPropArray.size();
		for (int i = 0; i < jsonArraySize; ++i) {
			JSONObject propJson = jsonPropArray.getJSONObject(i);
			GdsPropRespDTO propRespDto = new GdsPropRespDTO();
			Long propId = null;
			if ((propJson.containsKey("prop_id")) && (propJson.containsKey("prop_name"))
					&& (propJson.containsKey("prop_value")))
				propId = Long.valueOf(propJson.getLong("prop_id"));

			if ((StringUtil.isNotEmpty(propId)) && (props.indexOf(propId) != -1)) {
				props.remove(propId);
				propRespDto.setId(propId);
				propRespDto.setPropName(propJson.getString("prop_name"));
				List propValueList = new ArrayList();
				GdsPropValueRespDTO propValue = new GdsPropValueRespDTO();
				propValue.setPropId(propId);
				propValue.setPropValue(propJson.getString("prop_value"));
				propValueList.add(propValue);
				propRespDto.setValues(propValueList);
				propMap.put(propId.toString(), propRespDto);
			}

			if (props.isEmpty())
				break;
		}

		LogUtil.info(MODULE, "将行为分析返回的json转成商品属性结束：成功");
		return propMap;
	}
}