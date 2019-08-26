package com.ailk.ecp.pmph.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoForGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsStockRSV;
import com.ai.ecp.order.dubbo.dto.ROrdCartItemRequest;
import com.ai.ecp.order.dubbo.dto.ROrdCartRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdCartRSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoMsgResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.ISsoUserImportRSV;
import com.ai.paas.utils.StringUtil;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.BizContentVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Title: 购物车同步接口 <br>
 * Project Name:pmph-aip-web <br>
 * Description: <br>
 * Date:2019年3月7日上午9:50:27 <br>
 * Copyright (c) 2019 pmph All Rights Reserved <br>
 * 
 * @author myf
 * @version
 * @since JDK 1.7
 */
@Controller
public class CartAddController extends BaseController {

	private static final String MODULE = CartAddController.class.getName();

	@Resource
	private ICustManageRSV custManageRSV;

	@Resource
	private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;

	@Resource
	private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;

	@Resource
	private IShopInfoRSV shopInfoRSV;

	@Resource
	private IOrdCartRSV ordCartRSV;
	
	@Resource
    private IGdsStockRSV gdsStockRSV;
	
	@Resource
    private ISsoUserImportRSV ssoUserImportRSV;

	/*
	 * 购物车同步接口
	 */
	@RequestMapping(value = "/rest", params = "method=com.ai.ecp.pmph.order.cartAdd")
	@Security(mustLogin = true, authorCheckType = DefaultServiceCheckChain.class)
	@ResponseBody
	public Map<String, Object> cartAdd(BizContentVO bizContentVO) throws BusinessException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String bizContent = bizContentVO.getBiz_content();

		// 1.判断入参是否为空，为空返回错误信息
		if (StringUtil.isBlank(bizContent)) {
			retMap.put(RespUtil.CODE, "2");
			retMap.put(RespUtil.MSG, "参数不允许为空");
			return RespUtil.renderRootResp(retMap);
		}

		// 2.判断入参JSON格式是否正确
		try {
			JSONObject jsonObject = JSONObject.parseObject(bizContentVO.getBiz_content());
			String staffCode = jsonObject.getString("staff_code");
			if (StringUtil.isBlank(staffCode)) {
				retMap.put(RespUtil.CODE, "2");
				retMap.put(RespUtil.MSG, "[staff_code]参数不允许为空");
				return RespUtil.renderRootResp(retMap);
			}
			String gdsDetail = jsonObject.getString("gds_detail");
			if (StringUtil.isBlank(gdsDetail)) {
				retMap.put(RespUtil.CODE, "2");
				retMap.put(RespUtil.MSG, "[gds_detail]参数不允许为空");
				return RespUtil.renderRootResp(retMap);
			}

			JSONArray gdsList = JSONObject.parseArray(gdsDetail);
			for (Object object : gdsList) {
				Map gdsMap = (Map) object;
				if (gdsMap.get("bb_code") == null) {
					retMap.put(RespUtil.CODE, "2");
					retMap.put(RespUtil.MSG, "[bb_code]参数不允许为空");
					return RespUtil.renderRootResp(retMap);
				}
				if (StringUtil.isBlank(gdsMap.get("order_amount").toString())) {
					retMap.put(RespUtil.CODE, "2");
					retMap.put(RespUtil.MSG, "[order_amount]参数不允许为空");
					return RespUtil.renderRootResp(retMap);
				}
			}
			// 1、查询用户是否存在
			CustInfoReqDTO cust = new CustInfoReqDTO();
			cust.setStaffCode(staffCode);
			CustInfoResDTO custRes = this.custManageRSV.findCustInfo(cust);
			
			//处理员工编号,当获取不到会员信息，新增会员信息
            Long createStaff = custRes.getId();
            if (createStaff==null) {
                SsoUserInfoReqDTO infoReqDTO=new SsoUserInfoReqDTO();
                infoReqDTO.setUserName(staffCode);
                SsoUserInfoMsgResDTO msgResDTO= ssoUserImportRSV.saveStaffInfo(infoReqDTO);
                if (!msgResDTO.isFlag()) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[staff_code:"+staffCode+"]参数非法，请核实该参数值");
                    return RespUtil.renderRootResp(retMap); 
                }
            }
            
            custRes = this.custManageRSV.findCustInfo(cust);			

			if (custRes != null && custRes.getId() != null) {
				// 用于存储同步失败的商品信息
				List<Map<String, String>> goodsList = new ArrayList<Map<String, String>>();
				int failCount = 0; // 用于统计失败的商品数量
				// 加入购物车的商品列表信息 只有所有商品都有库存才能添加购物车
				ROrdCartRequest cartItem = new ROrdCartRequest();
				cartItem.setShopId(100l);
				cartItem.setStaffId(custRes.getId());
				String shopName = "";
				if (cartItem.getShopId() != null) {
					ShopInfoResDTO shop = shopInfoRSV.findShopInfoByShopID(cartItem.getShopId());
					shopName = shop.getShopName();
				}
				cartItem.setShopName(shopName);
				cartItem.setCartType("01");
				cartItem.setCurrentSiteId(1l);
				List<ROrdCartItemRequest> itList = new ArrayList<ROrdCartItemRequest>();

				for (Object object : gdsList) {
					Map gdsMap = (Map) object;
					/* 2、判断本版编号在系统中是否能查询到对应商品 */
					GdsInterfaceGdsReqDTO gdsReq = new GdsInterfaceGdsReqDTO();
					gdsReq.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
					String bbCode = gdsMap.get("bb_code").toString();
					gdsReq.setOriginGdsId(bbCode);
					GdsInterfaceGdsRespDTO resDto = gdsInterfaceGdsRSV.queryGdsInterfaceGdsByOriginGdsId(gdsReq);
					/* 2-1、没查到商品，则抛出通过本版编号未查到商品的异常 */
					if (resDto == null || resDto.getGdsId() == null || resDto.getGdsId() == 0L) {
						retMap.put(RespUtil.CODE, "2");
						retMap.put(RespUtil.MSG, "通过本版编号找不到商品");
						return RespUtil.renderRootResp(retMap);
					}

					/* 2-2、根据商品id、单品id查具体的商品 */
					GdsSkuInfoReqDTO skuReq = new GdsSkuInfoReqDTO();
					skuReq.setGdsId(resDto.getGdsId());// 商品id
					skuReq.setId(resDto.getSkuId());// 单品id
					GdsSkuInfoRespDTO sku = gdsSkuInfoQueryRSV.queryGdsSkuInfoResp(skuReq);// 查询到的商品
					if (sku == null || sku.getId() == null || sku.getId() == 0L) {
						retMap.put(RespUtil.CODE, "2");
						retMap.put(RespUtil.MSG, "通过本版编号找不到商品");
						return RespUtil.renderRootResp(retMap);
					}
					Map<String, String> gdsListMap = new HashMap<String, String>();

					/* 3、判断商品库存是否充足 */
					// 库存，调用商品查询库存接口
			        StockInfoForGdsReqDTO stockInfoForGdsDTO = new StockInfoForGdsReqDTO();
			        stockInfoForGdsDTO.setShopId(sku.getShopId());
			        stockInfoForGdsDTO.setGdsId(sku.getGdsId());
			        stockInfoForGdsDTO.setSkuId(sku.getId());
			        StockInfoRespDTO stockInfoDTO = gdsStockRSV.queryStockInfoByGds(stockInfoForGdsDTO);
			        Long availCount = 0l;
			        if (!StringUtil.isEmpty(stockInfoDTO)) {
			            availCount = stockInfoDTO.getAvailCount();
			            sku.setRealAmount(availCount);
			        }
			        
					Long orderAmount = Long.valueOf(gdsMap.get("order_amount").toString());
					if ((availCount -orderAmount) <= 10) { // 可用库存 - 购买数量 <= 10
						// 购买数量大于商品库存   保存本版编号，商品名称返回
						gdsListMap.put("bb_code", bbCode);
						gdsListMap.put("gds_name", sku.getGdsName());
						goodsList.add(gdsListMap);
						failCount += 1; // 失败商品数加1
					}
					ROrdCartItemRequest item = new ROrdCartItemRequest();
					item.setShopId(sku.getShopId());
					
					if (item.getShopId() != null) {
						ShopInfoResDTO shop = shopInfoRSV.findShopInfoByShopID(cartItem.getShopId());
						shopName = shop.getShopName();
					}
					item.setShopName(shopName);
					item.setCartType("01");
					item.setStaffId(custRes.getId());
					item.setGdsId(sku.getGdsId());
					item.setGdsName(sku.getGdsName());
					item.setSkuId(sku.getId());
					item.setSkuInfo(sku.getSkuProps());
					item.setGroupType("0");
					item.setGroupDetail(sku.getId().toString());
					item.setGdsType(sku.getGdsTypeId());
					item.setOrderAmount(orderAmount);
					item.setCategoryCode(sku.getMainCatgs());
					item.setPrnFlag("0");
					itList.add(item);
				}
				cartItem.setOrdCartItemList(itList);
				retMap.put("goodsList", goodsList);

				// 如果失败个数为0，则全部加入购物车
				if (failCount == 0) {
					ordCartRSV.addToCart(cartItem);
					retMap.put(RespUtil.CODE, "0");
					retMap.put(RespUtil.MSG, "同步购物车信息成功！");
				}else {
					retMap.put(RespUtil.CODE, "2");
					retMap.put(RespUtil.MSG, "同步购物车信息失败！");
				}
			}
			
		} catch (Exception e) {
			retMap.put(RespUtil.CODE, "1");
			retMap.put(RespUtil.MSG, "系统错误：" + e.getMessage());
		}
		return RespUtil.renderRootResp(retMap);
	}
}
