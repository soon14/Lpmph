package com.ai.ecp.busi.order.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.mvc.annotation.NativeJson;
import com.ai.ecp.base.velocity.ParamToolUtil;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.OrdConstant;
import com.ai.ecp.busi.order.vo.CoupCheckBeanRespVO;
import com.ai.ecp.busi.order.vo.CoupCodeReqVo;
import com.ai.ecp.busi.order.vo.CoupDetailRespVO;
import com.ai.ecp.busi.order.vo.CoupPaperReqVo;
import com.ai.ecp.busi.order.vo.RCrePreOrdItemReqVO;
import com.ai.ecp.busi.order.vo.RCrePreOrdReqVO;
import com.ai.ecp.busi.order.vo.RCrePreOrdsReqVO;
import com.ai.ecp.busi.order.vo.ROrderMainCheckCarRespVO;
import com.ai.ecp.busi.order.vo.RSubmitOrderRespVO;
import com.ai.ecp.busi.order.vo.RSumbitMainReqVO;
import com.ai.ecp.busi.order.vo.RSumbitMainsReqVO;
import com.ai.ecp.busi.staff.buyer.vo.CustAddrVO;
import com.ai.ecp.coupon.dubbo.dto.resp.CoupCodeRespDTO;
import com.ai.ecp.coupon.dubbo.dto.resp.CoupInfoRespDTO;
import com.ai.ecp.coupon.dubbo.dto.resp.CoupOrdCheckRespDTO;
import com.ai.ecp.coupon.dubbo.dto.resp.CoupPaperRespDTO;
import com.ai.ecp.coupon.dubbo.interfaces.ICoupDetailRSV;
import com.ai.ecp.general.order.dto.CoupCheckBeanRespDTO;
import com.ai.ecp.general.order.dto.CoupCheckInfoRespDTO;
import com.ai.ecp.general.order.dto.CoupDetailRespDTO;
import com.ai.ecp.general.order.dto.CoupSkuRespDTO;
import com.ai.ecp.general.order.dto.ROrdCartCommRequest;
import com.ai.ecp.general.order.dto.ROrdCartItemCommRequest;
import com.ai.ecp.general.order.dto.ROrdCartsCommRequest;
import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.goods.dubbo.dto.sharePoint.ShareMsgDto;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoExternalRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsShiptemRSV;
import com.ai.ecp.order.dubbo.dto.RCrePreOrdItemRequest;
import com.ai.ecp.order.dubbo.dto.RCrePreOrdRequest;
import com.ai.ecp.order.dubbo.dto.RCrePreOrdsRequest;
import com.ai.ecp.order.dubbo.dto.ROrdCartItemResponse;
import com.ai.ecp.order.dubbo.dto.ROrdCartRequest;
import com.ai.ecp.order.dubbo.dto.ROrdCartResponse;
import com.ai.ecp.order.dubbo.dto.ROrdDeliveAddrRequest;
import com.ai.ecp.order.dubbo.dto.ROrdInvoiceCommRequest;
import com.ai.ecp.order.dubbo.dto.ROrdInvoiceTaxRequest;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrdMainsResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdSubResponse;
import com.ai.ecp.order.dubbo.dto.RShowOrdCartsResponse;
import com.ai.ecp.order.dubbo.dto.RSumbitMainRequest;
import com.ai.ecp.order.dubbo.dto.RSumbitMainsRequest;
import com.ai.ecp.order.dubbo.dto.RSumbitSubRequest;
import com.ai.ecp.order.dubbo.dto.cart.FastPreOrdRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdCartItemRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdCartRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.util.CommonConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.prom.dubbo.dto.CartPromDTO;
import com.ai.ecp.prom.dubbo.dto.CartPromItemDTO;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.AcctInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustAddrReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustAddrResDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopStaffGroupReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustAddrRSV;
import com.ai.ecp.staff.dubbo.interfaces.ICustInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopManageRSV;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value="/order/build")
public class BuildOrdController extends EcpBaseController {

	private static final String MODULE = BuildOrdController.class.getName();

	
	@Resource
	private IOrdCartRSV ordCartRSV;
	
    @Resource
    private ICustAddrRSV custaddrRSV;
    
    @Resource
    private IShopInfoRSV shopInfoRSV;
    
    @Resource
    private IOrdMainRSV ordMain;

	@Resource
	private IGdsInfoExternalRSV gdsInfoExternalRSV;
	
	@Resource
	private ICustInfoRSV  custInfoRSV;
	
	@Resource
    private IOrdCartItemRSV  ordCartItemRSV;
	
	@Resource
	private IShopManageRSV shopManageRSV;
	
	@Resource
	private ICoupDetailRSV coupDetailRSV;
	
	/*
	 * 客户管理域服务
	 */
	@Resource
	private ICustAddrRSV custAddrRSV;
	
	@Resource
    private IGdsShiptemRSV gdsShiptemRSV;
	
	@RequestMapping
	public String init(Model model){
	    return "/order/build/init";
	}
	
	//获取订单信息
	@RequestMapping(value="/info")
	public Model orderInfo(Model model){
		return model;
	}
	
	//疯狂购物车
//	@RequestMapping(value="/checkcard")
//	@ResponseBody
	public EcpBaseResponseVO  checkCard(RCrePreOrdsReqVO rvo,HttpServletRequest request){
		LogUtil.info(MODULE, "===========================开始展示订单================================");
		EcpBaseResponseVO respVO = new EcpBaseResponseVO();
		try{
			RCrePreOrdsRequest rOrdCartsRequest = new RCrePreOrdsRequest();
			Long staffId = rOrdCartsRequest.getStaff().getId();
			ROrdCartRequest info = new ROrdCartRequest();
			// info.setStaffId(ParamsTool.getStaffId());
			info.setStaffId(info.getStaff().getId());
			// info.setStaffId(104L);
			RShowOrdCartsResponse cartList = ordCartRSV.queryCart(info);
			rOrdCartsRequest.setStaffId(staffId);
			
			List<RCrePreOrdRequest> ordList = new ArrayList<RCrePreOrdRequest>();
			for(ROrdCartResponse car : cartList.getOrdCartList()){
				RCrePreOrdRequest rOrdCartItemRequest = new RCrePreOrdRequest();
				
				//ObjectCopyUtil.copyObjValue(car, rOrdCartItemRequest, "groupLists", false);
				rOrdCartItemRequest.setCartId(car.getId());
				rOrdCartItemRequest.setPromId(car.getPromId());
				
				List<RCrePreOrdItemRequest> ordItemList = new ArrayList<RCrePreOrdItemRequest>();
				for(ROrdCartItemResponse carItem : car.getOrdCartItemList()){
					RCrePreOrdItemRequest ordItem = new RCrePreOrdItemRequest();
					ordItem.setCartItemId(carItem.getId());
					ordItem.setPromId(carItem.getPromId());
					//ObjectCopyUtil.copyObjValue(carItem, ordItem, "", false);
					ordItemList.add(ordItem);
				}
				
				for(List<ROrdCartItemResponse> group : car.getGroupLists()){
					for(ROrdCartItemResponse groupItem: group){
						RCrePreOrdItemRequest ordItem = new RCrePreOrdItemRequest();
						ordItem.setCartItemId(groupItem.getId());
						ordItem.setPromId(groupItem.getPromId());
						ordItemList.add(ordItem);
					}
				}
				
				rOrdCartItemRequest.setCartItemIdList(ordItemList);
				ordList.add(rOrdCartItemRequest);
			}
			
			rOrdCartsRequest.setCarList(ordList);
			rOrdCartsRequest.setSource(CommonConstants.SOURCE.SOURCE_WEB);
			RPreOrdMainsResponse resp = ordCartRSV.createPreOrd(rOrdCartsRequest);
			
			LogUtil.info(MODULE, "===========================调用ordCartRSV.createPreOrd================================");
			//把购物车列表信息缓存起来 时间为半小时
			HttpSession session = request.getSession();
			session.setAttribute(OrdConstant.ORDER_SESSION_KEY_PREFIX+staffId, resp);
			session.setMaxInactiveInterval(OrdConstant.ORDER_SESSION_TIME);
			
			LogUtil.info(MODULE, "==========================保存session信息完毕===============================================");

			respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
			
		}catch(Exception e){
			LogUtil.error(MODULE, e.getMessage());
			respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
			respVO.setResultMsg(e.getMessage());
		}
		
		return respVO;
	}
	
	//校验购物车列表
	@RequestMapping(value="/checkcar")
	@ResponseBody
	public ROrderMainCheckCarRespVO checkCar(RCrePreOrdsReqVO rvo,HttpServletRequest request){
		LogUtil.info(MODULE, "===========================开始展示订单================================");
		ROrderMainCheckCarRespVO respVO = new ROrderMainCheckCarRespVO();
		try{
			
			//行选中属性
			String itemCheck = "checked";
			//获取staffId测试获取
	
			RCrePreOrdsRequest rOrdCartsRequest = new RCrePreOrdsRequest();
			Long staffId = rOrdCartsRequest.getStaff().getId();
			
			rOrdCartsRequest.setStaffId(staffId);
			//放入预订单
			List<RCrePreOrdRequest> ordCartList = new ArrayList<RCrePreOrdRequest>() ;
		
			LogUtil.info(MODULE, "===========================封装参数================================");

			if(!CollectionUtils.isEmpty(rvo.getCarList())){
				for(RCrePreOrdReqVO rOrdCartReqVO : rvo.getCarList()){
					RCrePreOrdRequest rOrdCartRequest = new RCrePreOrdRequest();
					ObjectCopyUtil.copyObjValue(rOrdCartReqVO, rOrdCartRequest, "ordCartItemList", false);

					List<RCrePreOrdItemRequest> ordCartItemList = new ArrayList<RCrePreOrdItemRequest>();
					if(!CollectionUtils.isEmpty(rOrdCartReqVO.getCartItemIdList())){
						for(RCrePreOrdItemReqVO rOrdCartItemReqVO : rOrdCartReqVO.getCartItemIdList()){
							//判断是否明细项目被选中
							if(StringUtils.isNotEmpty(rOrdCartItemReqVO.getItemCheck()) && rOrdCartItemReqVO.getItemCheck().equals(itemCheck)){
								RCrePreOrdItemRequest rOrdCartItemRequest = new RCrePreOrdItemRequest();
								ObjectCopyUtil.copyObjValue(rOrdCartItemReqVO, rOrdCartItemRequest, "", false);
								ordCartItemList.add(rOrdCartItemRequest);
							}
						}
						//店铺中有明细被选中才添加
						if(ordCartItemList.size()>0){
							rOrdCartRequest.setCartItemIdList(ordCartItemList);
							ordCartList.add(rOrdCartRequest);
						}
					}

				}
			}

			
			//数据来源
			rOrdCartsRequest.setSource(CommonConstants.SOURCE.SOURCE_WEB);
			rOrdCartsRequest.setCarList(ordCartList);

			RPreOrdMainsResponse resp = ordCartRSV.createPreOrd(rOrdCartsRequest);
			
			LogUtil.info(MODULE, "===========================调用ordCartRSV.createPreOrd================================");
			//把购物车列表信息缓存起来 时间为半小时
			
			String redisKey = md5key(""+staffId+ DateUtil.getCurrentTimeMillis());
		    CacheUtil.addItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+redisKey, resp,OrdConstant.ORDER_SESSION_TIME);      
		    respVO.setMainHashKey(redisKey);
		    
			LogUtil.info(MODULE, "==========================保存session信息完毕===============================================");

			respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		
		}catch(Exception e){
			LogUtil.error(MODULE, e.getMessage());
			respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
			respVO.setResultMsg(e.getMessage());
		}
		//加入跳转判断进入session;
		CacheUtil.addItem("jumpFlag", "1");
		return respVO;
	}

	//展示订单
	@RequestMapping(value="/preord/{mainHashKey}")
	public String createPreOrd(Model model,@PathVariable String mainHashKey,HttpServletRequest request){
		LogUtil.info(MODULE, "===========================开始展示订单================================");
		try{

			Long staffId = new BaseInfo().getStaff().getId();
			model.addAttribute("staffId", staffId);
			RPreOrdMainsResponse resp = null;

			//String sourceKey = this.getParam(request, "key");
			resp = (RPreOrdMainsResponse) CacheUtil.getItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+mainHashKey);           

			if(resp == null){
				return "redirect:/homepage";
			}

			List<RPreOrdMainResponse> list = new ArrayList<RPreOrdMainResponse>();
			list = resp.getPreOrdMainList();
			LogUtil.info(MODULE, list.toString());
			//提交订单时的合计价格
			Long agentMoneys = 0l;
			model.addAttribute("agentMoneys",agentMoneys);
			model.addAttribute("cartProm", resp.getCartPromRespDTO());
			Map<Long,Integer> coupSizeMap = new HashMap<>();

			for(RPreOrdMainResponse ordMain :list){
				int size = 0;
				if(CollectionUtils.isNotEmpty(ordMain.getCoupCheckBeanRespDTOs())){
					for(CoupCheckBeanRespDTO coup :ordMain.getCoupCheckBeanRespDTOs()){
						size +=coup.getCoupSize();
					}
				}
				coupSizeMap.put(ordMain.getShopId(),size);
			}
			//跟页面对应，判断是否应该展示优惠券字段
			if(StringUtil.isNotEmpty(resp.getCoupOrdCheckRespDTO())){
			    //获取的是所有可以使用的优惠券信息
				model.addAttribute("coupOrdSkuMap",resp.getCoupOrdCheckRespDTO().getCoupOrdSkuMap());
				model.addAttribute("coupIdskuIdMap",resp.getCoupOrdCheckRespDTO().getCoupIdskuIdMap());
			}
			model.addAttribute("preOrdMainList", list);
			model.addAttribute("coupSizeMap",coupSizeMap);
			//新增一个字段用于展示优惠券的开关，算出搜优可用优惠券数量
			int coupCounts=0;
			for (RPreOrdMainResponse rPreOrdMainResponse : list) {
			    Integer count = coupSizeMap.get(rPreOrdMainResponse.getShopId());
			    coupCounts+=count;
            }
			//如果coupSwitch=1；则展示优惠券等相关信息；
			model.addAttribute("coupCounts", coupCounts);
			//配送方式
			Map<Long,String> deliverTypes = new HashMap<Long,String>();
			LogUtil.info(MODULE, "==============================获取配送方式===============================================");
			for(RPreOrdMainResponse ordMain : list){
				ShopInfoResDTO shopInfo = shopInfoRSV.findShopInfoByShopID(ordMain.getShopId());
				if(StringUtil.isBlank(shopInfo.getDistribution())){
					deliverTypes.put(ordMain.getShopId(),OrdConstant.DeliverType.EXPERSS);
				}else{
					deliverTypes.put(ordMain.getShopId(),shopInfo.getDistribution());
				}

			}
			model.addAttribute("deliverTypes", deliverTypes);
			LogUtil.info(MODULE, deliverTypes.toString());

			Long orderMoneys = 0l;//总金额
			Long discountMoneys = 0l;//总优惠金额
			Long realExpressFees = 0l;//运费
			Long allMoney = 0l;//应付总额
			Long orderAmounts = 0l;//总数量

			LogUtil.info(MODULE, "==============================计算金额===============================================");
			Map<Long,CartPromItemDTO> cartPromItemDTOMap = new HashMap<>();
			Map<Long,CartPromDTO> cartPromDTOMap = new HashMap<>();
			Map<Long,Long> discountPriceMoneyMap = new HashMap<>();
			if(resp.getCartPromRespDTO()!=null){
				cartPromItemDTOMap = resp.getCartPromRespDTO().getCartPromItemDTOMap();
				cartPromDTOMap = resp.getCartPromRespDTO().getCartPromDTOMap();
			}

			for(int i=0;i<list.size();i++){
				RPreOrdMainResponse ordMain = list.get(i);
				orderMoneys += ordMain.getOrderMoney();
				Long discountMoney = 0l;
				boolean isAllVirGds = true;


				orderAmounts += ordMain.getOrderAmount();
				if(cartPromDTOMap.get(ordMain.getCartId())!=null){//店铺促销优惠减免金额
					discountMoney += cartPromDTOMap.get(ordMain.getCartId()).getDiscountMoney().longValue();
				}

				if(CollectionUtils.isNotEmpty(ordMain.getGroupLists())){
					for(List<RPreOrdSubResponse> groups : ordMain.getGroupLists()){
						if(CollectionUtils.isNotEmpty(groups)){
							for(RPreOrdSubResponse groupItem : groups){

								//是否免邮
								LongReqDTO longReqDTO = new LongReqDTO();
								longReqDTO.setId(groupItem.getGdsType());
								isAllVirGds = gdsInfoExternalRSV.isGdsTypeFreightFree(longReqDTO);

								if(cartPromItemDTOMap.get(groupItem.getCartItemId())!=null){
									CartPromItemDTO cartPromItemDTO = cartPromItemDTOMap.get(groupItem.getCartItemId());

									if(!cartPromItemDTO.isIfFulfillProm()){
										//没参与促销时，自行计算价格差值
										Long discountPrice = groupItem.getBasePrice() - groupItem.getBuyPrice();
										discountPrice = (discountPrice<0?(-discountPrice):discountPrice);
										discountMoney += discountPrice * groupItem.getOrderAmount();

									}else{
										discountMoney += cartPromItemDTO.getDiscountMoney().longValue();//商品促销优惠减免金额
									}
								}
							}
						}
					}
				}

				if(CollectionUtils.isNotEmpty(ordMain.getPreOrdSubList())){
					for(RPreOrdSubResponse ordsub : ordMain.getPreOrdSubList()){

						//是否免邮
						LongReqDTO longReqDTO = new LongReqDTO();
						longReqDTO.setId(ordsub.getGdsType());
						isAllVirGds = gdsInfoExternalRSV.isGdsTypeFreightFree(longReqDTO);

						if(cartPromItemDTOMap.get(ordsub.getCartItemId())!=null){
							CartPromItemDTO cartPromItemDTO = cartPromItemDTOMap.get(ordsub.getCartItemId());

							if(!cartPromItemDTO.isIfFulfillProm()){
								//没参与促销时，自行计算价格差值
								Long discountPrice = ordsub.getBasePrice() - ordsub.getBuyPrice();
								discountPrice = (discountPrice<0?(-discountPrice):discountPrice);
								discountMoney += discountPrice * ordsub.getOrderAmount();

							}else{
								discountMoney += cartPromItemDTO.getDiscountMoney().longValue();//商品促销优惠减免金额
							}
						}
					}
				}

				if(deliverTypes.get(ordMain.getShopId()).contains(OrdConstant.DeliverType.EXPERSS) && !isAllVirGds){
					realExpressFees += ordMain.getRealExpressFee();//有包含快递就使用快递
				}


				discountMoneys += discountMoney;
				discountPriceMoneyMap.put(ordMain.getCartId(),discountMoney);

			}
			LogUtil.info(MODULE, "==============================计算金额结束===============================================");
			allMoney = orderMoneys + realExpressFees - discountMoneys;
			FastPreOrdRequest fastPreOrdRequest = (FastPreOrdRequest) CacheUtil.getItem(OrdConstants.Common.ORDER_FAST_KEY_PREFIX + mainHashKey);
			model.addAttribute("orderMoneys", orderMoneys);
			model.addAttribute("realExpressFees", realExpressFees);
			model.addAttribute("allMoney",allMoney);
			model.addAttribute("orderAmounts",orderAmounts);
			model.addAttribute("discountMoneys",discountMoneys);
			model.addAttribute("discountPriceMoneyMap",discountPriceMoneyMap);
			model.addAttribute("addrs",getCustAddr());
			if(fastPreOrdRequest==null){
			    model.addAttribute("sourceKey","");
			}else{
			    model.addAttribute("sourceKey",mainHashKey);
			}
			model.addAttribute("mainHashKey", mainHashKey);
		}catch(BusinessException bus){
			LogUtil.error(MODULE, "==================================展示订单出错===========================");
			LogUtil.error(MODULE, bus.getMessage());
			bus.printStackTrace();

		}catch(Exception e){
			LogUtil.error(MODULE, "==================================展示订单出错============================");
			LogUtil.error(MODULE, e.getMessage());
			e.printStackTrace();
		}
		//取出跳转标记
		Object jumpFlag = CacheUtil.getItem("jumpFlag");
		if(jumpFlag==null){
			model.addAttribute("jumpFlag","0");
		}else{			
			model.addAttribute("jumpFlag","1");
		}
		CacheUtil.delItem("jumpFlag");
		LogUtil.info(MODULE, "===========================展示订单结束================================");
		return "/order/build/build-create";
	}
	//使用优惠券前先获取该优惠券的shopId
	@RequestMapping(value="/getCoupCodeShopId")
	@ResponseBody
	public CoupCodeRespDTO getCoupCodeShopId(CoupCodeReqVo vo){
	    CoupCodeRespDTO coupCodeResp = new CoupCodeRespDTO();
	    //获取该优惠券所对应的店铺，将其加到vo中。将这个店铺id与主订单中店铺ID集合进行对比。
        String coupCode = vo.getCoupCode();
        //获取主订单中子订单所在的店铺集合
        String shopIdList = vo.getShopIdList();
        CoupInfoRespDTO coupInfo= null;
        if(StringUtils.isNotBlank(coupCode)){
            //从数据库中查该优惠码对应的详细信息
            coupInfo= coupDetailRSV.queryCoupShopIdByCode(coupCode);
        }
        if(coupInfo!=null){
            Long coupCodeshopId = coupInfo.getShopId();
            String[] shopIds = shopIdList.split(",");
            //遍历该数组，查看其中是否有与优惠码的店铺id一致的
            for (String id : shopIds) {
                if(Long.parseLong(id)==coupCodeshopId){
                    coupCodeResp.setShopId(coupCodeshopId);
                    break;
                }
            }
            //如果该优惠码所对应的店铺id与主订单中店铺id无一匹配。
            if(vo.getShopId()==null){
                coupCodeResp.setResultMsg("优惠码"+coupCode+"只能在"+coupInfo.getShopName()+"店铺使用");
                return coupCodeResp;
            }
        }else{
            coupCodeResp.setResultMsg("您输入的优惠码无效或已过期，请检查确认！");
            return coupCodeResp;
        }
	    return coupCodeResp;
	}
	@RequestMapping(value="/getCoupCode")
	@ResponseBody
	public CoupCodeRespDTO getCoupCode(CoupCodeReqVo vo,HttpServletRequest request){
	    CoupCodeRespDTO coupCodeResp = new CoupCodeRespDTO();
	    ROrdCartCommRequest rOrdCartCommRequest = new ROrdCartCommRequest();
	    
	    Long staffId = rOrdCartCommRequest.getStaff().getId();
	    //获取缓存中保存购物车信息
	    RPreOrdMainsResponse ordMains = (RPreOrdMainsResponse) CacheUtil.getItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+vo.getMainHashKey());;
	    
        if(StringUtils.isBlank(vo.getSourceKey())){	
            vo.setSourceKey("1");
        }
        //如果缓存中保存的购物车信息失效，从而导致ordMains=null。
        if(ordMains==null){
            coupCodeResp.setResultMsg("页面已失效，请刷新页面！");
            return coupCodeResp;
        }
        //获取使用优惠码的订单
        RPreOrdMainResponse ordMain = null;
        for(int i=0;i<ordMains.getPreOrdMainList().size();i++){
            ordMain = ordMains.getPreOrdMainList().get(i);
            if(vo.getShopId().longValue()==ordMain.getShopId().longValue()){
                break;
            }
        }
        rOrdCartCommRequest.setCoupCode(vo.getCoupCode());
        rOrdCartCommRequest.setId(ordMain.getCartId());
        rOrdCartCommRequest.setSource(vo.getSourceKey());
        rOrdCartCommRequest.setStaffId(staffId);
        rOrdCartCommRequest.setShopId(vo.getShopId());
        // 补齐购物车信息
        CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
        custInfoReqDTO.setId(staffId);
        CustInfoResDTO custInfoResDTO = custInfoRSV.getCustInfoById(custInfoReqDTO);
        ShopStaffGroupReqDTO shopStaffGroupReqDTO = new ShopStaffGroupReqDTO();
        shopStaffGroupReqDTO.setStaffId(custInfoResDTO.getId());
        shopStaffGroupReqDTO.setShopId(vo.getShopId());
        // 客户组id
        String custGroupValue = null;
        if (custInfoResDTO != null && custInfoResDTO.getId() != null
                && custInfoResDTO.getId() != 0) {
            custGroupValue = shopManageRSV.queryShopStaffGroup(shopStaffGroupReqDTO);
        }
        // 客户基本信息
        rOrdCartCommRequest.setCustGroupValue(custGroupValue);
        rOrdCartCommRequest.setCustLevelValue(custInfoResDTO.getCustLevelCode());
        rOrdCartCommRequest.setAreaValue(custInfoResDTO.getProvinceCode());
        //web  渠道
        rOrdCartCommRequest.setChannelValue(vo.getSourceKey());  

        // 单品购物车明细信息
        List<ROrdCartItemCommRequest> rOrdCartItemCommRequests = new ArrayList<ROrdCartItemCommRequest>();
        if(ordMain.getPreOrdSubList()!=null){
            for (RPreOrdSubResponse preOrdSubresp : ordMain.getPreOrdSubList()) {
                ROrdCartItemCommRequest rOrdCartItemCommRequest = new ROrdCartItemCommRequest();
                if(preOrdSubresp.getCartItemId()!=null){           
                    rOrdCartItemCommRequest.setId(preOrdSubresp.getCartItemId());            
                    ROrdCartItemResponse ordCartItem = ordCartItemRSV.queryCartItemByItemId(rOrdCartItemCommRequest);
                    ObjectCopyUtil.copyObjValue(ordCartItem, rOrdCartItemCommRequest, null, false);
                    rOrdCartItemCommRequest.setPromId(ordCartItem.getPromId());
                }else{
                    rOrdCartItemCommRequest.setGdsId(preOrdSubresp.getGdsId());
                    rOrdCartItemCommRequest.setGdsName(preOrdSubresp.getGdsName());
                    rOrdCartItemCommRequest.setGroupType(preOrdSubresp.getGroupType());
                    rOrdCartItemCommRequest.setGroupDetail(preOrdSubresp.getGroupDetail());
                    rOrdCartItemCommRequest.setGdsType(preOrdSubresp.getGdsType());
                    rOrdCartItemCommRequest.setShopId(vo.getShopId());
                    rOrdCartItemCommRequest.setShopName(ordMain.getShopName());
                    rOrdCartItemCommRequest.setOrderAmount(preOrdSubresp.getOrderAmount());
                    rOrdCartItemCommRequest.setSiteId(rOrdCartItemCommRequest.getSiteId());
                    rOrdCartItemCommRequest.setSkuId(preOrdSubresp.getSkuId());
                    
                }          
                rOrdCartItemCommRequest.setDiscountMoney(preOrdSubresp.getBaseDiscountMoney());
                rOrdCartItemCommRequests.add(rOrdCartItemCommRequest);
            }
        }
        if(ordMain.getGroupLists()!=null){
            //组合套餐购物车明细信息
            for(List<RPreOrdSubResponse> preOrdSubResps:ordMain.getGroupLists()){
                for(RPreOrdSubResponse preOrdSubResp:preOrdSubResps){
                    ROrdCartItemCommRequest rOrdCartItemCommRequest = new ROrdCartItemCommRequest();
                    if(preOrdSubResp.getCartItemId()!=null){           
                        rOrdCartItemCommRequest.setId(preOrdSubResp.getCartItemId());            
                        ROrdCartItemResponse ordCartItem = ordCartItemRSV.queryCartItemByItemId(rOrdCartItemCommRequest);
                        ObjectCopyUtil.copyObjValue(ordCartItem, rOrdCartItemCommRequest, null, false);
                        rOrdCartItemCommRequest.setPromId(ordCartItem.getPromId());
                    }else{
                        rOrdCartItemCommRequest.setGdsId(preOrdSubResp.getGdsId());
                        rOrdCartItemCommRequest.setGdsName(preOrdSubResp.getGdsName());
                        rOrdCartItemCommRequest.setGroupType(preOrdSubResp.getGroupType());
                        rOrdCartItemCommRequest.setGroupDetail(preOrdSubResp.getGroupDetail());
                        rOrdCartItemCommRequest.setGdsType(preOrdSubResp.getGdsType());
                        rOrdCartItemCommRequest.setShopId(vo.getShopId());
                        rOrdCartItemCommRequest.setShopName(ordMain.getShopName());
                        rOrdCartItemCommRequest.setOrderAmount(preOrdSubResp.getOrderAmount());
                        rOrdCartItemCommRequest.setSiteId(rOrdCartItemCommRequest.getSiteId());
                        rOrdCartItemCommRequest.setSkuId(preOrdSubResp.getSkuId());
                        
                    }          
                    rOrdCartItemCommRequest.setDiscountMoney(preOrdSubResp.getBaseDiscountMoney());
                    rOrdCartItemCommRequests.add(rOrdCartItemCommRequest);
                }
            }
        }
        // 每个店铺的明细列表
        rOrdCartCommRequest.setOrdCartItemCommList(rOrdCartItemCommRequests);
        coupCodeResp = coupDetailRSV.queryOrdCheckCoupByCode(rOrdCartCommRequest);
        String redisKey = md5key("" +staffId+ DateUtil.getCurrentTimeMillis());//临时存储优惠码信息
        CacheUtil.addItem(OrdConstant.ORDER_COUPCODE_PREFIX+redisKey, coupCodeResp,OrdConstant.ORDER_SESSION_TIME);       
        coupCodeResp.setHashKey(redisKey);
        coupCodeResp.setShopId(vo.getShopId());
	    return coupCodeResp;
	}
	
	@RequestMapping(value="/getCoupInfo")
	@ResponseBody
	public CoupPaperRespDTO getCoupInfo(CoupPaperReqVo vo, HttpServletRequest request){
		CoupPaperRespDTO coupPaperResp = new CoupPaperRespDTO();
		ROrdCartsCommRequest rOrdCartsCommRequest = new ROrdCartsCommRequest();
		List<ROrdCartCommRequest> rOrdCartCommRequests = new ArrayList<>();
		
		long staffId = rOrdCartsCommRequest.getStaff().getId();
		rOrdCartsCommRequest.setStaffId(staffId);
		//获取缓存中保存购物车信息
	    RPreOrdMainsResponse ordMains = (RPreOrdMainsResponse) CacheUtil.getItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+vo.getMainHashKey());;
	    
	    //遍历shopId
	    String shopIdList = vo.getShopIdList();
	    if(shopIdList==null||StringUtil.isBlank(shopIdList)){
	    	return null;
	    }
	    String[] shopIds = shopIdList.split(",");
	    //遍历skuId
	    String skuIdList = vo.getSkuIds();
	    if(skuIdList == null||StringUtil.isBlank(skuIdList)){
	    	return null;
	    }
	    String[] skuIds = skuIdList.split(",");
	    //for (String shopId : shopIds) {
	    	if(StringUtils.isBlank(vo.getSourceKey())){
	    		vo.setSourceKey("1");
	    	}
		//}
	    
	    //如果缓存中保存的购物车信息失效，从而导致ordMains=null。
        if(ordMains==null){
        	coupPaperResp.setResultMsg("页面已失效，请刷新页面！");
            return coupPaperResp;
        }
        int count = 0;
        for(String shopId : shopIds){
        	Long shopIdLongValue = Long.parseLong(shopId);
        	
        	//获取使用优惠券的订单
            RPreOrdMainResponse ordMain = null;
            for(int i=0;i<ordMains.getPreOrdMainList().size();i++){
                ordMain = ordMains.getPreOrdMainList().get(i);
                if(skuIds[count].equals(ordMain.getPreOrdSubList().get(0).getGroupDetail())){
                    break;
                }
            }
            count++;
            ROrdCartCommRequest rOrdCartCommRequest = new ROrdCartCommRequest();
            rOrdCartCommRequest.setId(ordMain.getCartId());
            rOrdCartCommRequest.setSource(vo.getSourceKey());
            rOrdCartCommRequest.setStaffId(staffId);
            rOrdCartCommRequest.setShopId(shopIdLongValue);
            if(ordMain.getCoupCheckBeanRespDTOs()!=null){            	
            	rOrdCartCommRequest.setPayMoney(ordMain.getPreOrdSubList().get(0).getBaseDiscountMoney()+ordMain.getRealExpressFee()-ordMain.getCoupCheckBeanRespDTOs().get(0).getCoupValue());
            }else{
            	rOrdCartCommRequest.setPayMoney(ordMain.getPreOrdSubList().get(0).getBaseDiscountMoney()+ordMain.getRealExpressFee());
            }
            // 补齐购物车信息
            CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
            custInfoReqDTO.setId(staffId);
            CustInfoResDTO custInfoResDTO = custInfoRSV.getCustInfoById(custInfoReqDTO);
            ShopStaffGroupReqDTO shopStaffGroupReqDTO = new ShopStaffGroupReqDTO();
            shopStaffGroupReqDTO.setStaffId(custInfoResDTO.getId());
            shopStaffGroupReqDTO.setShopId(shopIdLongValue);
            // 客户组id
            String custGroupValue = null;
            if (custInfoResDTO != null && custInfoResDTO.getId() != null
                    && custInfoResDTO.getId() != 0) {
                custGroupValue = shopManageRSV.queryShopStaffGroup(shopStaffGroupReqDTO);
            }
            // 客户基本信息
            rOrdCartCommRequest.setCustGroupValue(custGroupValue);
            rOrdCartCommRequest.setCustLevelValue(custInfoResDTO.getCustLevelCode());
            rOrdCartCommRequest.setAreaValue(custInfoResDTO.getProvinceCode());
            //web  渠道
            rOrdCartCommRequest.setChannelValue(vo.getSourceKey());
            
            // 单品购物车明细信息
            List<ROrdCartItemCommRequest> rOrdCartItemCommRequests = new ArrayList<ROrdCartItemCommRequest>();
            if(ordMain.getPreOrdSubList()!=null){
            	for (RPreOrdSubResponse preOrdSubresp : ordMain.getPreOrdSubList()) {
            		ROrdCartItemCommRequest rOrdCartItemCommRequest = new ROrdCartItemCommRequest();
            		if(preOrdSubresp.getCartItemId()!=null){
            			rOrdCartItemCommRequest.setId(preOrdSubresp.getCartItemId());            
                        ROrdCartItemResponse ordCartItem = ordCartItemRSV.queryCartItemByItemId(rOrdCartItemCommRequest);
                        ObjectCopyUtil.copyObjValue(ordCartItem, rOrdCartItemCommRequest, null, false);
                        rOrdCartItemCommRequest.setPromId(ordCartItem.getPromId());
            		}else{
            			rOrdCartItemCommRequest.setGdsId(preOrdSubresp.getGdsId());
                        rOrdCartItemCommRequest.setGdsName(preOrdSubresp.getGdsName());
                        rOrdCartItemCommRequest.setGroupType(preOrdSubresp.getGroupType());
                        rOrdCartItemCommRequest.setGroupDetail(preOrdSubresp.getGroupDetail());
                        rOrdCartItemCommRequest.setGdsType(preOrdSubresp.getGdsType());
                        rOrdCartItemCommRequest.setShopId(shopIdLongValue);
                        rOrdCartItemCommRequest.setShopName(ordMain.getShopName());
                        rOrdCartItemCommRequest.setOrderAmount(preOrdSubresp.getOrderAmount());
                        rOrdCartItemCommRequest.setSiteId(rOrdCartItemCommRequest.getSiteId());
                        rOrdCartItemCommRequest.setSkuId(preOrdSubresp.getSkuId());
            		}
            		rOrdCartItemCommRequest.setDiscountMoney(preOrdSubresp.getBaseDiscountMoney());
                    rOrdCartItemCommRequests.add(rOrdCartItemCommRequest);
            	}
            }
            if(ordMain.getGroupLists()!=null){
            	//组合套餐购物车明细信息
                for(List<RPreOrdSubResponse> preOrdSubResps:ordMain.getGroupLists()){
                	for(RPreOrdSubResponse preOrdSubResp:preOrdSubResps){
                		ROrdCartItemCommRequest rOrdCartItemCommRequest = new ROrdCartItemCommRequest();
                		if(preOrdSubResp.getCartItemId()!=null){
                			rOrdCartItemCommRequest.setId(preOrdSubResp.getCartItemId());            
                            ROrdCartItemResponse ordCartItem = ordCartItemRSV.queryCartItemByItemId(rOrdCartItemCommRequest);
                            ObjectCopyUtil.copyObjValue(ordCartItem, rOrdCartItemCommRequest, null, false);
                            rOrdCartItemCommRequest.setPromId(ordCartItem.getPromId());
                		}else{
                			rOrdCartItemCommRequest.setGdsId(preOrdSubResp.getGdsId());
                            rOrdCartItemCommRequest.setGdsName(preOrdSubResp.getGdsName());
                            rOrdCartItemCommRequest.setGroupType(preOrdSubResp.getGroupType());
                            rOrdCartItemCommRequest.setGroupDetail(preOrdSubResp.getGroupDetail());
                            rOrdCartItemCommRequest.setGdsType(preOrdSubResp.getGdsType());
                            rOrdCartItemCommRequest.setShopId(shopIdLongValue);
                            rOrdCartItemCommRequest.setShopName(ordMain.getShopName());
                            rOrdCartItemCommRequest.setOrderAmount(preOrdSubResp.getOrderAmount());
                            rOrdCartItemCommRequest.setSiteId(rOrdCartItemCommRequest.getSiteId());
                            rOrdCartItemCommRequest.setSkuId(preOrdSubResp.getSkuId());
                		}
                		rOrdCartItemCommRequest.setDiscountMoney(preOrdSubResp.getBaseDiscountMoney());
                        rOrdCartItemCommRequests.add(rOrdCartItemCommRequest);
                	}
                }
            }
            // 每个店铺的明细列表
            rOrdCartCommRequest.setOrdCartItemCommList(rOrdCartItemCommRequests);
            rOrdCartCommRequests.add(rOrdCartCommRequest);
        }
        rOrdCartsCommRequest.setOrdCartsCommList(rOrdCartCommRequests);
        CoupOrdCheckRespDTO queryOrdCheckCoup = coupDetailRSV.queryOrdCheckCoup(rOrdCartsCommRequest);
        
        String redisKey = md5key("" +staffId+ DateUtil.getCurrentTimeMillis());//临时存储优惠码信息
        CacheUtil.addItem(OrdConstant.ORDER_COUPPAPER_PREFIX+redisKey, queryOrdCheckCoup,OrdConstant.ORDER_SESSION_TIME);       
        coupPaperResp.setHashKey(redisKey);
        
		return coupPaperResp;
	}
	
	/**
	 * 
	 * 功能描述：地址更换重计运费
	 *
	 * <p>创建日期 ：2018-6-1 下午8:30:52</p>
	 *
	 * @param model
	 * @param vo
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	@RequestMapping(value="/getExpressFees")
	@ResponseBody
	public Map<Long, Long> getExpressFees(RSumbitMainsReqVO vo){
		LogUtil.info(MODULE, "运费请求入参");
		// 封装公有入参
        ROrdCartsCommRequest rOrdCartsCommRequest = new ROrdCartsCommRequest();
        RPreOrdMainsResponse ordMains = (RPreOrdMainsResponse) CacheUtil.getItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+vo.getMainHashKey());
        //加入收货人地址并打印
        CustAddrReqDTO custAddrReqDTO = new CustAddrReqDTO();
        custAddrReqDTO.setId(vo.getAddrId());
        @SuppressWarnings("rawtypes")
		BaseInfo baseInfo = new BaseInfo();
		Long staffId = baseInfo.getStaff().getId();
        custAddrReqDTO.setStaffId(staffId);
        CustAddrResDTO custAddrResDTO = custAddrRSV.findAddr(custAddrReqDTO);
        if(custAddrResDTO!=null){
        	if(custAddrResDTO.getCountryCode()==null){
            	rOrdCartsCommRequest.setCountryCode("156");
            }else{
            	rOrdCartsCommRequest.setCountryCode(custAddrResDTO.getCountryCode());
            }
            rOrdCartsCommRequest.setProvinceCode(custAddrResDTO.getProvince());
            rOrdCartsCommRequest.setCityCode(custAddrResDTO.getCityCode());
        }
        
        rOrdCartsCommRequest.setCurrentSiteId(baseInfo.getCurrentSiteId());
        
        //整合单品信息 订单级别优惠
        Map<Long, CartPromDTO> ordMap = null;
        // 子订单级别优惠
        Map<Long, CartPromItemDTO> ordSubMap = null;
        if (ordMains.getCartPromRespDTO() != null) {
            ordMap = ordMains.getCartPromRespDTO().getCartPromDTOMap();
            ordSubMap = ordMains.getCartPromRespDTO().getCartPromItemDTOMap();
        }
        List<ROrdCartCommRequest> rOrdCartCommRequests = new ArrayList<>();
        for(RPreOrdMainResponse rPreOrdMainResponse : ordMains.getPreOrdMainList()){
            Long payMoney = 0l;
        	ROrdCartCommRequest rOrdCartCommRequest = new ROrdCartCommRequest();
        	rOrdCartCommRequest.setShopId(rPreOrdMainResponse.getShopId());
        	if (ordMap != null) {
				List<RPreOrdSubResponse> preOrdSubList = rPreOrdMainResponse.getPreOrdSubList();
        		for(RPreOrdSubResponse rPreOrdSubResponse : preOrdSubList){
        			Long baseDiscountPrice = rPreOrdSubResponse.getBaseDiscountMoney();
        			Long baseDiscountMoney = baseDiscountPrice * rPreOrdSubResponse.getOrderAmount();
        			
        			payMoney += baseDiscountMoney;
        		}
        		
        		CartPromDTO cartProm = ordMap.get(rPreOrdMainResponse.getCartId());
        		if (cartProm != null) {
        			Long discountMoney = cartProm.getDiscountMoney() == null ? 0l
                            : cartProm.getDiscountMoney().longValue();
                    Long discountPriceMoney = cartProm.getDiscountPriceMoney() == null ? 0l
                            : cartProm.getDiscountPriceMoney().longValue();
                    payMoney -= (discountMoney+discountPriceMoney);
                    rOrdCartCommRequest.setPayMoney(payMoney);
        			//是否满足促销条件
                    rOrdCartCommRequest.setIfFulfillProm(cartProm.isIfFulfillProm());
                    if(cartProm.getPromInfoDTO().getId()!=null&&cartProm.getPromInfoDTO().getId()!=0){
                    	rOrdCartCommRequest.setPromId(cartProm.getPromInfoDTO().getId());
                    }
                    List<ROrdCartItemCommRequest> cartItemCommRequests = new ArrayList<>();
                    for(RPreOrdSubResponse rPreOrdSubResponse : rPreOrdMainResponse.getPreOrdSubList()){
                    	ROrdCartItemCommRequest cartItemCommRequest = new ROrdCartItemCommRequest();
                    	cartItemCommRequest.setCategoryCode(rPreOrdSubResponse.getCategoryCode());
                    	cartItemCommRequest.setGdsId(rPreOrdSubResponse.getGdsId());
                    	cartItemCommRequest.setOrderAmount(rPreOrdSubResponse.getOrderAmount());
                    	cartItemCommRequest.setGdsType(rPreOrdSubResponse.getGdsType());
                    	cartItemCommRequest.setSkuId(rPreOrdSubResponse.getSkuId());
                    	cartItemCommRequest.setOrderMoney(rPreOrdSubResponse.getBaseDiscountMoney());
            			CartPromItemDTO itemProm = ordSubMap.get(rPreOrdSubResponse.getCartItemId());
                    	cartItemCommRequest.setIfFulfillProm(itemProm.isIfFulfillProm());
                    	if(itemProm.getPromInfoDTO().getId()!=null&&itemProm.getPromInfoDTO().getId()!=0){
                    		cartItemCommRequest.setPromId(itemProm.getPromInfoDTO().getId());
                    	}
                    	cartItemCommRequests.add(cartItemCommRequest);
                    }
                    rOrdCartCommRequest.setOrdCartItemCommList(cartItemCommRequests);
        		}
        	}
            rOrdCartCommRequests.add(rOrdCartCommRequest);
        }
        rOrdCartsCommRequest.setOrdCartsCommList(rOrdCartCommRequests);
        Map<Long, Long> shipExpenseMap = gdsShiptemRSV.calcShipExpenseByCarts(rOrdCartsCommRequest);
        
        for(Map.Entry<Long, Long> entry:shipExpenseMap.entrySet()){
        	for(RPreOrdMainResponse rPreOrdMainResponse : ordMains.getPreOrdMainList()){
        		if(rPreOrdMainResponse.getShopId().longValue()==entry.getKey().longValue()){
        			rPreOrdMainResponse.setRealExpressFee(entry.getValue());
        		}
        	}
        }
        CacheUtil.addItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+vo.getMainHashKey(), ordMains);
		return shipExpenseMap;
	}
	/**
	 * 
	 * 功能描述：提交订单
	 *
	 * <p>创建日期 ：2015-10-9 下午8:30:52</p>
	 *
	 * @param model
	 * @param vo
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	@RequestMapping(value="/submitOrd")
	@ResponseBody
	public RSubmitOrderRespVO submitOrd(Model model,RSumbitMainsReqVO vo,HttpServletRequest request){
	    LogUtil.info(MODULE, "===========================提交订单开始===============================");
	    //统一获取到有关发票信息的数据，所有主订单同意使用一样的发票数据
	    String invoiceType4Tax = vo.getInvoiceType();
	    ROrdInvoiceCommRequest rOrdInvoiceCommRequest4Tax = vo.getrOrdInvoiceCommRequest();
	    ROrdInvoiceTaxRequest rOrdInvoiceTaxRequest4Tax = vo.getrOrdInvoiceTaxRequest();
	    //将发票数据统一存放到每一条主订单中。
	    List<RSumbitMainReqVO> sumbitMainList4Tax = vo.getSumbitMainList();
	    for (RSumbitMainReqVO rSumbitMainReqVO : sumbitMainList4Tax) {
	        rSumbitMainReqVO.setInvoiceType(invoiceType4Tax);
	        rSumbitMainReqVO.setrOrdInvoiceCommRequest(rOrdInvoiceCommRequest4Tax);
	        rSumbitMainReqVO.setrOrdInvoiceTaxRequest(rOrdInvoiceTaxRequest4Tax);
        }
		RSubmitOrderRespVO resp = new RSubmitOrderRespVO();
		//dto初始化
	    RSumbitMainsRequest sumbitMains = new RSumbitMainsRequest();
	    List<RSumbitMainRequest> sumbitMainList = new ArrayList<RSumbitMainRequest>();
		
		//从session里面拿对应的购物车信息
	    Long staffId = sumbitMains.getStaff().getId();

		//行选中属性
		String itemCheck = "checked";
		sumbitMains.setSourceKey(vo.getSourceKey());

		boolean addrError = false;
		
		try{
			//获取缓存中保存购物车信息
			RPreOrdMainsResponse ordMains = (RPreOrdMainsResponse) CacheUtil.getItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+vo.getMainHashKey());
						
			if(ordMains==null){
				if(sumbitMains.getStaff()==null || sumbitMains.getStaff().getId()==0){
					throw new Exception("用户未登录");
				}
				throw new Exception("订单信息已失效");
			}

	        //设置促销信息
	        sumbitMains.setCartPromRespDTO(ordMains.getCartPromRespDTO());

			//优惠券全局检测信息(RPreOrdMainsResponse待定是否只存oupIdskuIdMap)
			if(ordMains.getCoupOrdCheckRespDTO()!=null){
				sumbitMains.setCoupIdskuIdMap(ordMains.getCoupOrdCheckRespDTO().getCoupIdskuIdMap());
			}

	        //开始复制信息
	        LogUtil.info(MODULE, "=========================开始复制信息==========================");	       
	        for(int i=0;i<ordMains.getPreOrdMainList().size();i++){
	            Long coupCodeMoney = 0l;
	            //session信息
	            RPreOrdMainResponse ordMain = (RPreOrdMainResponse)ordMains.getPreOrdMainList().get(i);
	            RSumbitMainRequest sumbitMain = new RSumbitMainRequest();
	            sumbitMain.setStaffId(staffId);
	            sumbitMain.setShopId(ordMain.getShopId());
	            sumbitMain.setShopName(ordMain.getShopName());
	            sumbitMain.setBasicMoney(ordMain.getBasicMoney());
	            sumbitMain.setOrderMoney(ordMain.getOrderMoney());
	            sumbitMain.setOrderAmount(ordMain.getOrderAmount());
	            sumbitMain.setCartId(ordMain.getCartId());
	            //优惠码
	            sumbitMain.setCoupCode(vo.getSumbitMainList().get(i).getCoupCode());
	            if(vo.getSumbitMainList().get(i).getCoupCodeMoney()!=null&&vo.getSumbitMainList().get(i).getCoupCodeMoney()>0){
	                sumbitMain.setCoupCodeMoney(vo.getSumbitMainList().get(i).getCoupCodeMoney());
    	            //优惠码优惠总金额
    	            //coupCodeMoney = coupCodeMoney+vo.getSumbitMainList().get(i).getCoupCodeMoney();
                    CoupCodeRespDTO coupCodeRespDTO = (CoupCodeRespDTO) CacheUtil.getItem(OrdConstant.ORDER_COUPCODE_PREFIX+vo.getSumbitMainList().get(i).getHashKey());
                    coupCodeMoney = coupCodeMoney+coupCodeRespDTO.getCoupValue();
                    
    	            if(coupCodeRespDTO!=null){
    	                Map<Long,CoupCheckInfoRespDTO> coupCodeSkuIdMap = coupCodeRespDTO.getCoupIdskuIdMap();
        	            if(sumbitMains.getCoupIdskuIdMap()!=null){
        	                sumbitMains.getCoupIdskuIdMap().putAll(coupCodeSkuIdMap);
        	            }else{
        	                sumbitMains.setCoupIdskuIdMap(coupCodeSkuIdMap);
        	            }
    	            }
	            }
				//复制子订单信息
				List<RSumbitSubRequest> preOrdSubList = new ArrayList<RSumbitSubRequest>();
				for(RPreOrdSubResponse ordSub : ordMain.getPreOrdSubList()){
					RSumbitSubRequest sumbitSub = new RSumbitSubRequest();
					ObjectCopyUtil.copyObjValue(ordSub, sumbitSub, "", false);
					if(StringUtil.isNotEmpty(vo.getSourceKey())){
						sumbitSub.setCartType(OrdConstants.ShopCart.CART_TYPE_EASYBUY);
					}
					sumbitSub.setStaffId(staffId);
					preOrdSubList.add(sumbitSub);
				}
				/**
				 *=======================================
				 * 优惠券多个(在一个循环里检测另一个循环的判断会重复)
				 * 在session当中存放的位置顺序是否和页面上传的相同
				 *=======================================
				 */
				Long coupMoney = 0l;
				//需要获取coupHashKey
				if(CollectionUtils.isNotEmpty(vo.getSumbitMainList())&&vo.getSumbitMainList().size()>0){
					RSumbitMainReqVO rSumbitMainReqVO = vo.getSumbitMainList().get(i);
					List<CoupCheckBeanRespVO> coupCheckBean = rSumbitMainReqVO.getCoupCheckBean();
					List<CoupCheckBeanRespDTO> coupCheckBeanResp = new ArrayList<>();
					if(coupCheckBean!=null && coupCheckBean.size()>0){
						for(CoupCheckBeanRespVO coupCheckBeanRespVO : coupCheckBean){
							CoupCheckBeanRespDTO coupCheckBeanDTO = new CoupCheckBeanRespDTO();

							if(itemCheck.equals(coupCheckBeanRespVO.getChecked())){
								ObjectCopyUtil.copyObjValue(coupCheckBeanRespVO,coupCheckBeanDTO,"coupDetails",false);
								if(CollectionUtils.isNotEmpty(coupCheckBeanRespVO.getCoupDetails())){

									List<CoupDetailRespDTO> coupDetailRespDTOs = new ArrayList<>();
									for(CoupDetailRespVO coupDetailRespVO : coupCheckBeanRespVO.getCoupDetails()){
										CoupDetailRespDTO coupDetailDTO = new CoupDetailRespDTO();
										if(itemCheck.equals(coupDetailRespVO.getChecked())){
											ObjectCopyUtil.copyObjValue(coupDetailRespVO,coupDetailDTO,"",false);
											coupDetailRespDTOs.add(coupDetailDTO);
											if(!coupDetailRespVO.getNoExpress().equals("1")){
												if(!coupDetailRespVO.getDiscountCoup().equals("1")){
													//获取coupMoney缓存信息
													String redisKey = vo.getCouponHashKey();
													CoupOrdCheckRespDTO queryOrdCheckCoup = (CoupOrdCheckRespDTO) CacheUtil.getItem(OrdConstant.ORDER_COUPPAPER_PREFIX+redisKey);
													Map<Long, CoupCheckInfoRespDTO> coupIdskuIdMap = queryOrdCheckCoup.getCoupIdskuIdMap();
											        for(Map.Entry<Long, CoupCheckInfoRespDTO> entry : coupIdskuIdMap.entrySet()){
											        	if(coupDetailRespVO.getCoupId().longValue()==entry.getValue().getCoupCheckBeanRespDTO().getCoupId().longValue()){
											        		coupMoney += entry.getValue().getCoupCheckBeanRespDTO().getCoupValue();
											        	}
											        }
													//coupMoney += coupDetailRespVO.getCoupValue();
													//优惠券梳理
												}else{
													List<CoupSkuRespDTO> coupSkus = (List<CoupSkuRespDTO>) ordMains.getCoupOrdCheckRespDTO().getCoupIdskuIdMap().get(coupDetailRespVO.getCoupId()).getCoupSkuRespDTO();
													Long orderMoney = 0L;
													for (CoupSkuRespDTO coupSkuRespDTO : coupSkus) {
														for(RPreOrdSubResponse ordSub : ordMain.getPreOrdSubList()){
															if(ordSub.getSkuId().equals(coupSkuRespDTO.getSkuId())){
																Long discountFinalPrice = ordSub.getBasePrice();
																orderMoney += discountFinalPrice*ordSub.getOrderAmount();
																break;
															}
														}
													}
													Long discountMony = orderMoney- (orderMoney*coupDetailRespVO.getCoupValue()/10000);
													coupMoney += discountMony;
												}
											}
											
										}
									}
									coupCheckBeanDTO.setCoupDetails(coupDetailRespDTOs);
								}
								coupCheckBeanResp.add(coupCheckBeanDTO);
							}

						}

						sumbitMain.setCoupCheckBean(coupCheckBeanResp);
					}

				}

	          	//复制组合商品信息
	            if(CollectionUtils.isNotEmpty(ordMain.getGroupLists())){
	            	for(List<RPreOrdSubResponse> groups : ordMain.getGroupLists()){
		            	for(RPreOrdSubResponse group : groups){
		            		RSumbitSubRequest sumbitSub = new RSumbitSubRequest();
			                ObjectCopyUtil.copyObjValue(group, sumbitSub, "", false);
			                sumbitSub.setStaffId(staffId);
			                preOrdSubList.add(sumbitSub);
		            	}
		            }
	            }
	            
                sumbitMain.setPreOrdSubList(preOrdSubList);
	            //复制资金账户信息
                List<AcctInfoResDTO> acctInfoList = ordMain.getOrdAcctInfoList();
                //首先清零使用资金
                for(int a=0;a<acctInfoList.size();a++){
            		acctInfoList.get(a).setDeductOrderMoney(0l);
            	}
	            //页面信息(能否保证放入session的顺序和页面排列的顺序一致？？？？？)
	            RSumbitMainReqVO sumbitMainVO = vo.getSumbitMainList().get(i);
	            //将页面资金使用情况设置到资金账户当中
	            if((sumbitMainVO.getOrdAcctInfoList() !=null) && (sumbitMainVO.getOrdAcctInfoList().size() == acctInfoList.size())){
	            	for(int m=0;m<sumbitMainVO.getOrdAcctInfoList().size();m++){
		            	acctInfoList.get(m).setDeductOrderMoney(sumbitMainVO.getOrdAcctInfoList().get(m).getDeductOrderMoney());
		            }
	            }
	            //主订单发票信息
	            sumbitMain.setInvoiceType(sumbitMainVO.getInvoiceType());
	            
	            //买家留言
	            sumbitMain.setBuyerMsg(sumbitMainVO.getBuyerMsg());
	            
	            ROrdInvoiceCommRequest rOrdInvoiceCommRequest = sumbitMainVO.getrOrdInvoiceCommRequest();
				sumbitMain.setrOrdInvoiceCommRequest(rOrdInvoiceCommRequest);
	            
				ROrdInvoiceTaxRequest rOrdInvoiceTaxRequest = sumbitMainVO.getrOrdInvoiceTaxRequest();
                sumbitMain.setrOrdInvoiceTaxRequest(rOrdInvoiceTaxRequest);
                
	            //session金额校验从前台页面获取到得orderMoney+运费是否和 realMoney是否一致
				/**========================================================
				 * 校验钱  校验钱 前台传递的根据一组运算规则要计算成orderMoney这个不变的值才行
				 * ========================================================
				 */
                
                //页面资金账户的钱
                boolean moneyFlag = true;
                Long pageAcctMoney = 0l;
                for(AcctInfoResDTO acct : acctInfoList){
                	pageAcctMoney += acct.getDeductOrderMoney();
                }
                //后台设值无运费配送方式
                if(sumbitMainVO.getDeliverType().equals("2")||sumbitMainVO.getDeliverType().equals("0")){
                	sumbitMainVO.setRealExpressFee(0L);
                	ordMain.setRealExpressFee(0L);
                }
				//页面金额重计
	            Long pageOrderMoney = Long.valueOf(sumbitMainVO.getRealMoney())
						-Long.valueOf(ordMain.getRealExpressFee())
						+sumbitMainVO.getDiscountMoney()
						+ pageAcctMoney
						+ coupMoney
						+ coupCodeMoney;
	            Long sessionOrderMoney = ordMain.getOrderMoney();
	            Long pageRealExpressFees = Long.valueOf(sumbitMainVO.getRealExpressFee());
	            Long sessionRealExpressFees = ordMain.getRealExpressFee();
	            
	            if(!pageOrderMoney.equals(sessionOrderMoney)) moneyFlag = false;
	            LogUtil.info(MODULE, "订单金额"+pageOrderMoney+"||"+sessionOrderMoney);
	            LogUtil.info(MODULE, "运费金额"+pageRealExpressFees+"||"+sessionRealExpressFees);

				//控制js修改金额和0元以下单
	            if(!moneyFlag){
	                LogUtil.error(MODULE, "订单号："+ordMain.getOrderId()+"订单金额异常");
	                throw new BusinessException("订单号："+ordMain.getOrderId()+"订单金额异常,请重新检查");
	            }
				if(sumbitMainVO.getRealMoney() <= 0||pageOrderMoney<=0l){
					LogUtil.error(MODULE, "系统不支持0元以及以下订单");
					throw new BusinessException("系统不支持0元以及以下订单");
				}
	            /**===================================
				 * 校验钱
				 * ===================================
				 */
	            
	            sumbitMain.setOrdAcctInfoList(acctInfoList);
	            sumbitMain.setDeliverType(sumbitMainVO.getDeliverType());
	            sumbitMain.setRealExpressFee(Long.valueOf(sumbitMainVO.getRealExpressFee()));
	            sumbitMain.setRealMoney(Long.valueOf(sumbitMainVO.getRealMoney()));
	            
	            
	            sumbitMainList.add(sumbitMain);
	        }
	        LogUtil.info(MODULE, "=========================结束复制信息==========================");
	        
	        //收货地址管理
	        Long addrId = vo.getAddrId();
	        String gdsType = vo.getGdsType();
	        
	        CustAddrReqDTO cust = new CustAddrReqDTO();
	        cust.setStaffId(cust.getStaff().getId());
	        cust.setId(addrId);
	        CustAddrResDTO custresp = custAddrRSV.findAddr(cust);
			//校验收货地址
			if(gdsType.contains(OrdConstant.ORDER_ENTITY_TYPE)){
				if(custresp == null){
					addrError = true;
					throw new BusinessException("收货地址不允许为空");
				}
			}
	        
	        
	        ROrdDeliveAddrRequest addrreq = new ROrdDeliveAddrRequest();
	        if(!StringUtil.isEmpty(custresp)){
	            ObjectCopyUtil.copyObjValue(custresp, addrreq, "", false);   
    			//获取省市县具体位置
    			ParamToolUtil area = new ParamToolUtil();
    			String newchnlAddress = custresp.getChnlAddress();
    			String newProvince = area.getAreaName(custresp.getProvince());
    			String newCity = area.getAreaName(custresp.getCityCode());
    			String newCounty = area.getAreaName(custresp.getCountyCode());
    			if(StringUtil.isBlank(newProvince)){
    				newProvince = "";
    			}
    			if(StringUtil.isBlank(newProvince)){
    				newCity = "";
    			}
    			if(StringUtil.isBlank(newProvince)){
    				newCounty = "";
    			}
    			newchnlAddress = newProvince + newCity + newCounty + newchnlAddress;
    			addrreq.setChnlAddress(newchnlAddress);
	        }
	        LogUtil.info(MODULE, "======================收货地址管理=========================");
	        
	        //设置地址信息
	        sumbitMains.setrOrdDeliveAddrRequest(addrreq);
	        //购物车信息
	        sumbitMains.setSumbitMainList(sumbitMainList);
	        //设置用户Id
	        sumbitMains.setStaffId(staffId);
	        //设置支付方式
	        sumbitMains.setPayType(vo.getPayType());
	        
	        sumbitMains.setSource(CommonConstants.SOURCE.SOURCE_WEB);
	        
	        //获取分享送积分信息
	        Cookie[] cookies = request.getCookies();
	        String cookieValue = "";
	        if(cookies!=null){
	        	Map<Long,Long> shareMap = new HashMap<Long,Long>();
		        for(Cookie cookie:cookies){
		        	if(cookie.getName().equals("shareMsg")){
		        		cookieValue = cookie.getValue();		        		
		    	        if(StringUtil.isNotBlank(cookieValue)){
		    	        	List<ShareMsgDto> shareMsgs = JSONObject.parseArray(cookieValue, ShareMsgDto.class);
		    	        	if(shareMsgs!=null&&shareMsgs.size()>0){
		    	        		for(ShareMsgDto shareMsg:shareMsgs){
		    	        			//防止提交空值和非数字
		    	        			if(StringUtil.isNotBlank(shareMsg.getStaffId())&&StringUtils.isNumeric(shareMsg.getStaffId())){
			    	        			Long shareStaffId = Long.parseLong(shareMsg.getStaffId());	
			    	        			//过滤分享人未登录提交订单和自己分享自己下单
			    	        			if(shareStaffId!=null&&shareStaffId.longValue()!=0l&&staffId.longValue()!=shareStaffId.longValue()){
			    	        				shareMap.put(Long.parseLong(shareMsg.getGdsId()),Long.parseLong(shareMsg.getStaffId()));
			    	        			}	
		    	        			}
		    	        		}
		    	        	}
		    	        }       	
		        	}		        	
		        }
		        sumbitMains.setShareMap(shareMap);
	        }
	        //保存订单信息
	        ROrdMainsResponse rOrdMainsResponse = ordMain.sumbitOrd(sumbitMains);

			//提交成功清理缓存
	        CacheUtil.delItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+vo.getMainHashKey());
	        
	        List<ROrdMainResponse> orderList =  rOrdMainsResponse.getOrdMainList();
            LogUtil.info(MODULE, "===========================线上支付参数保存session=============================");
            if(StringUtil.isNotEmpty(orderList)){
                String onlineKey =md5key("" +staffId+ DateUtil.getCurrentTimeMillis());//临时存储优惠码信息
                CacheUtil.addItem(OrdConstant.OnlineOrd.ORDER_ONLINE_KEY+onlineKey, orderList,OrdConstant.ORDER_SESSION_TIME);  
                resp.setOnlineKey(onlineKey);              
            }
            
            String payTypeKey =md5key(""+staffId+ DateUtil.getCurrentTimeMillis());//临时存储优惠码信息
            CacheUtil.addItem(OrdConstant.ORDER_PAY_TYPE+payTypeKey, vo.getPayType(),OrdConstant.ORDER_SESSION_TIME);  
            resp.setPayTypeKey(payTypeKey);
           
            LogUtil.info(MODULE, "===========================线上支付参数保存session完毕=============================");
	        
	        resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		}catch(Exception e){
		    e.printStackTrace();
		    LogUtil.error(MODULE, "提交订单失败");
		    resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
		    resp.setResultMsg(e.getMessage()!=null?e.getMessage():"系统异常");
			if(!addrError) {
			    CacheUtil.delItem(OrdConstant.ORDER_SESSION_KEY_PREFIX+vo.getMainHashKey());
			}
		}
		
		LogUtil.info(MODULE, "========================提交订单结束=============================");
		
		return resp;
	}

	//收货地址新增弹出窗口 repCode
	@RequestMapping(value="/buyeraddrnew")
	public String buyerAddrNew(){
		return "/order/build/open/buyer-addressnew";
	}
	
	/**
	 * 
	 * 功能描述：普通发票信息设置
	 *
	 * <p>创建日期 ：2015-10-9 上午11:28:58</p>
	 *
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	@RequestMapping(value="/billInfoInit")
	public String billInfoInit(){
		return "/order/build/bill/bill-info";
	}
	
	
	//收货地址保存
	@RequestMapping(value="/saveaddr")
	@ResponseBody
	public Map<String,Object> saveAddr(@ModelAttribute CustAddrVO custaddr){
		Map<String, Object> cust = new HashMap<String, Object>();
	    LogUtil.info(MODULE, "============== 保存店铺收货地址    开始  =============");
        
        CustAddrReqDTO cusraddrDTO = new CustAddrReqDTO();
        ObjectCopyUtil.copyObjValue(custaddr, cusraddrDTO, null, false);
        CustAddrResDTO custaddrresp = new CustAddrResDTO();
//        cusraddrDTO.setStaffId(ParamsTool.getStaffId());
        cusraddrDTO.setStaffId(cusraddrDTO.getStaff().getId());

        try{
            if(cusraddrDTO.getId() == null)
            {
                custaddrresp = custaddrRSV.saveCustAddr(cusraddrDTO);
            }
            else {
                custaddrRSV.updateCustAddr(cusraddrDTO);
                ObjectCopyUtil.copyObjValue(custaddr, custaddrresp, null, false);
            }
            cust.put("resultFlag","ok");
            
        }catch(Exception e){
            custaddrresp = null;
            LogUtil.error(MODULE, "保存地址出错");
            cust.put("resultFlag","expt");
            
        }
        cust.put("resultInfo", custaddrresp);
        return cust;
	}
	
	//收货地址编辑弹出窗口 repCode
    @RequestMapping(value="/buyeraddrupdate")
    public String buyerAddrUpdate(Model model,@RequestParam(value="id")String id){
        //获取staffId
//        Long staffId = ParamsTool.getStaffId();
    	//根据店铺id，查找地址信息
        CustAddrReqDTO dto = new CustAddrReqDTO();
    	Long staffId = dto.getStaff().getId();
        if(StringUtil.isBlank(id)){
            throw new BusinessException("----");
		}

		dto.setStaffId(Long.valueOf(staffId));
		dto.setId(Long.valueOf(id));
        
        CustAddrResDTO custaddr = custaddrRSV.findAddr(dto);
        model.addAttribute("buyerAddr", custaddr);
        return "/order/build/open/buyer-addressupdate";
    }
    
    //用户收货地址数据
    private List<CustAddrResDTO> getCustAddr(){
        List<CustAddrResDTO> addrs = new ArrayList<CustAddrResDTO>();
        //获取用户收货地址信息
        CustAddrReqDTO dto = new CustAddrReqDTO();
//        dto.setStaffId(ParamsTool.getStaffId());
		dto.setStaffId(dto.getStaff().getId());
        
        addrs = custaddrRSV.listCustAddr(dto);
        return addrs;
    }

	
	/**
	 * 
	 * uploadImage:(上传图片). <br/>  
	 * 
	 * @param model
	 * @param req
	 * @param rep
	 * @return 
	 * @since JDK 1.6
	 */
    @RequestMapping(value = "/uploadimage")
    @ResponseBody
    @NativeJson(true)
    private String uploadImage(Model model,HttpServletRequest req, HttpServletResponse rep) {
        JSONObject obj = new JSONObject();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
        // 获取图片
        Iterator<MultipartFile> files = multipartRequest.getFileMap().values().iterator();
        MultipartFile file = null;
        if (files.hasNext()) {
            file = files.next();
        }
        Iterator<String> ids = multipartRequest.getFileMap().keySet().iterator();
        String id = null;
        if (ids.hasNext()) {
            id = ids.next();
        }
        try {
            if (file == null) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择上传文件！");
                LogUtil.error(MODULE, "请选择上传文件！");
                return obj.toJSONString();
            }
            String fileName = file.getOriginalFilename();
            String extensionName = "." + getExtensionName(fileName);

            /** 支持文件拓展名：.jpg,.png,.jpeg,.gif,.bmp */
            boolean flag = Pattern.compile("\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$|\\.(bmp)$")
                    .matcher(extensionName.toLowerCase()).find();
            if (!flag) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)!");
                LogUtil.error(MODULE, "上传图片失败,原因---请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)!");
                return obj.toJSONString();
            }
            //判断图片的长宽（像素）
            /*BufferedImage bi = ImageIO.read(file.getInputStream());;   
            int width = bi.getWidth(); 
            int height = bi.getHeight();
            if(width != height){
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "上传的图片的宽度和高度像素不一致！");
                LogUtil.error(MODULE, "上传的图片的宽度和高度像素不一致！");
                return obj.toJSONString();
            }*/
            byte[] datas = inputStream2Bytes(file.getInputStream());
            if(datas.length>5*1024*1024){
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "上传的图片大于5M!");
                LogUtil.error(MODULE, "图片上传失败，上传的图片必须小于5M!");
                return obj.toJSONString();
            }
            fileName = Math.random()+"";
            String vfsId = ImageUtil.upLoadImage(datas, fileName);
            resultMap.put("vfsId", vfsId);
            resultMap.put("imageName", fileName);
            resultMap.put("id", id);
            resultMap.put("imagePath", getImageUrl(vfsId,"150x150!"));
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            obj.put("message", "保存成功!");
            obj.put("map", resultMap);
        } catch (BusinessException e) {
			LogUtil.error(MODULE, "上传图片出错,原因---" + e.getMessage(), e);
			obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
        } catch (IOException e) {
			LogUtil.error(MODULE, "上传图片出错,原因---" + e.getMessage(), e);
			obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
        }
        return obj.toJSONString();
    }

    /**
     * 
     * inputStream2Bytes:(将InputStream转换成byte数组). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param in
     * @return
     * @throws IOException 
     * @since JDK 1.6
     */
    private byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count = -1;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return outStream.toByteArray();
    }

   /**
    * 
    * getExtensionName:(获取文件拓展名). <br/> 
    * 
    * @param fileName
    * @return 
    * @since JDK 1.6
    */
    private String getExtensionName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }
 
    
    /**
     * 
     * getImageUrl:(根据上传到mongoDB的图片ID 从mongoDB上获取图片路径). <br/> 
     * 
     * @param vfsId
     * @param param
     * @return 
     * @since JDK 1.6
     */
    private String getImageUrl(String vfsId,String param){
        StringBuilder sb=new StringBuilder();
        sb.append(vfsId);
        if(!StringUtil.isBlank(param)){
            sb.append("_");
            sb.append(param);
        }
        return ImageUtil.getImageUrl(sb.toString());
    }
    
    /**
     * 
     * md5key:MD5加密. <br/> 
     * @param str
     * @return 
     * @since JDK 1.6
     */
    private String md5key(String str){
        return ParamsTool.MD5(str).toLowerCase();
    }

	
}
