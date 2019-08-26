package com.ailk.ecp.pmph.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoDetailRespDTO;
import com.ai.ecp.order.dubbo.util.LongUtils;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.GoodsDetailUtil;
import com.ailk.ecp.pmph.util.HttpClientUtil;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.BizContentVO;

import net.sf.json.JSONObject;

/*
 * 图书销售排行榜对外接口
 */
@Controller
public class SaleRankController extends BaseController {

	private static final String MODULE = SaleRankController.class.getName();

	/*
	 * 图书销售排行榜对外接口
	 */
	@RequestMapping(value = "/rest", params = "method=com.ai.ecp.pmph.order.saleRank")
	@Security(mustLogin = true, authorCheckType = DefaultServiceCheckChain.class)
	@ResponseBody
	public Map<String, Object> saleRank(BizContentVO bizContentVO) throws BusinessException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		String bizContent=bizContentVO.getBiz_content();
        
        //1.判断入参是否为空，为空返回错误信息
        if(StringUtil.isBlank(bizContent)){
            retMap.put(RespUtil.CODE, "1");
            retMap.put(RespUtil.MSG, "参数不允许为空");
            return RespUtil.renderRootResp(retMap);
        }
        JSONObject jsonObject=JSONObject.fromObject(bizContentVO.getBiz_content());
        Long num=jsonObject.getLong("num");
        if (LongUtils.isEmpty(num)) {
            retMap.put(RespUtil.CODE, "2");
            retMap.put(RespUtil.MSG, "[num]参数不允许为空");
            return RespUtil.renderRootResp(retMap); 
        }

		try {
			List<GdsInfoDetailRespDTO> gdsDetailInfoList = null;

			// 行为分析访问排行榜地址
			String url = HttpClientUtil.getRankAnalysUrl();

			// 请求参数
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			formparams.add(new BasicNameValuePair("pageSize", "1"));
			formparams.add(new BasicNameValuePair("pageNumber", num.toString()));

			List<Long> propIds = new ArrayList<Long>();
			propIds.add(1008l);// 本版编号
			JSONObject jsonResult = null;
			try {
				// 远程请求
				jsonResult = JSONObject.fromObject(HttpClientUtil.doRequest(url, formparams));
			} catch (Exception e) {
				LogUtil.error(MODULE, "排行榜数据转化为json对象错误", e);
				throw new Exception("远程请求异常");
			}

			if (null == jsonResult) {
				LogUtil.error(MODULE, "排行榜远程服务返回json为空！");
				throw new Exception("远程请求异常");
			}
			if ((jsonResult.containsKey("serviceState"))
					&& ("0000".equalsIgnoreCase(jsonResult.getString("serviceState")))) {
				LogUtil.info(MODULE, "调用排行榜行为分析成功");
				try {
					gdsDetailInfoList = GoodsDetailUtil.analysJsonObjToGdsDetialList(jsonResult, propIds, null);
				} catch (Exception e) {
					LogUtil.error(MODULE, "排行榜远程服务返回json转化数据出现异常！", e);
					throw new Exception("远程请求异常");
				}

			} else {
				if (jsonResult.containsKey("serviceMsg")) {
					LogUtil.error(MODULE, jsonResult.getString("serviceMsg"));
					throw new Exception("远程请求异常");
				}
				LogUtil.error(MODULE, "排行榜调用远程服务未知错误！");
				throw new Exception("远程请求异常");
			}

			LogUtil.info(MODULE, "调用排行榜行为分析结束：");

			List<Map<String, String>> goodsList = new ArrayList<Map<String, String>>();
			for (GdsInfoDetailRespDTO gdsDetail : gdsDetailInfoList) {
				Map<String, String> gdsListMap = new HashMap<String, String>();

				if (gdsDetail.getSkuInfo() != null && gdsDetail.getSkuInfo().getAllPropMaps() != null
						&& gdsDetail.getSkuInfo().getAllPropMaps().get("1008") != null
						&& gdsDetail.getSkuInfo().getAllPropMaps().get("1008").getValues() != null
						&& gdsDetail.getSkuInfo().getAllPropMaps().get("1008").getValues().get(0) != null) {
					gdsListMap.put("bb_code",
							gdsDetail.getSkuInfo().getAllPropMaps().get("1008").getValues().get(0).toString());
				}
				gdsListMap.put("trade_amount", gdsDetail.getExt1());
				goodsList.add(gdsListMap);
			}
			retMap.put("goodsList", goodsList);
			retMap.put(RespUtil.CODE, "0");
			retMap.put(RespUtil.MSG, "查询图书销售排行榜数据成功。");

		} catch (Exception e) {
			retMap.put(RespUtil.CODE, "1");
			retMap.put(RespUtil.MSG, "系统错误：" + e.getMessage());
		}
		return RespUtil.renderRootResp(retMap);
	}

}
