package com.ai.ecp.pmph.busi.seller.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.busi.order.util.ParamsTool;
import com.ai.ecp.busi.seller.order.vo.RDelyOrderReqVO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.order.dubbo.dto.OrdDeliveryBatchReq;
import com.ai.ecp.order.dubbo.dto.OrdDeliveryBatchResp;
import com.ai.ecp.order.dubbo.dto.ROrdAddressRequest;
import com.ai.ecp.order.dubbo.dto.ROrdAddressResponse;
import com.ai.ecp.order.dubbo.dto.ROrdBuyerMsgReq;
import com.ai.ecp.order.dubbo.dto.ROrdSellerMsgDTO;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsResponse;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.ROrderInvoiceReq;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.dto.RShopOrderResponse;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsComm;
import com.ai.ecp.order.dubbo.interfaces.IOrdDetailsRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdManageRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrderExpressRSV;
import com.ai.ecp.pmph.busi.seller.order.vo.CustAddrVO;
import com.ai.ecp.pmph.busi.seller.order.vo.OrdDeliveryBatchVO;
import com.ai.ecp.pmph.busi.seller.order.vo.OrderBuyMsgVO;
import com.ai.ecp.pmph.busi.seller.order.vo.OrderInvoiceVO;
import com.ai.ecp.pmph.busi.seller.order.vo.OrderSellerMsgVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.AuthStaffAdminReqDTO;
import com.ai.ecp.staff.dubbo.dto.AuthStaffAdminResDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IAuthAdminRSV;
import com.ai.ecp.staff.dubbo.interfaces.ICustAddrRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.CipherUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/seller/order/customer")
public class OrdCustomerController extends EcpBaseController {
	private static final String MODULE = OrdCustomerController.class.getName();
	@Resource
	private ICustAddrRSV custaddrRSV;

	@Resource(name = "ordDetailsRSV")
	private IOrdDetailsRSV ordDetailsRSV;
	
    @Resource(name = "ordMainRSV")
    private IOrdMainRSV ordMainRSV;
    
    @Resource(name = "authAdminRSV")
    private IAuthAdminRSV authAdminRSV;

	@Resource
	private IGdsCategoryRSV gdsCategoryRSV;

	@Resource
	private IStaffUnionRSV staffUnionRSV;

	@Resource(name = "ordManageRSV")
	private IOrdManageRSV ordManageRSV;
	
	@Resource
	private IOrderExpressRSV orderExpressRSV;
	
	@Resource
	private IShopInfoRSV shopInfoRSV;
	/**
	 * 订单详情
	 * 
	 * @param model
	 * @param sign: {flag:'1',orderId:'xxxxx',usercode:'xxxx',time:'xxxx'}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/details/{sign}")
	public String customerOrderDetails(Model model, @PathVariable("sign") String sign) {
	
		if(StringUtil.isBlank(sign)){
			throw new BusinessException("order.orderid.null.error");
		}
		//解密
		String json = CipherUtil.decrypt(sign, CipherUtil.DES_CBC_ALGORITHM);
	   
		String orderid = getSignString(json,"orderId");
		String flag = getSignString(json,"flag");
		String userCode = getSignString(json,"usercode");
		String time = getSignString(json,"time");
		
		if (StringUtil.isBlank(orderid)) {
			throw new BusinessException("order.orderid.null.error");
		}
		
		if(StringUtil.isNotBlank(time)){
			Long s = (System.currentTimeMillis() - Long.parseLong(time)) / (1000 * 60);
			if(s>3L){//超过3分钟，抛出认证异常
				throw new BusinessException("order.auth.error");
			}
		}	
		
		if(!auth(userCode)){//无法匹配客服账号
			throw new BusinessException("order.auth.error");
		}
		
		ROrderDetailsRequest rdto = new ROrderDetailsRequest();
		
		rdto.setOrderId(orderid);
		ROrderDetailsResponse resp = new ROrderDetailsResponse();
		resp = ordDetailsRSV.queryOrderDetails(rdto);

		// 订单id
		model.addAttribute("orderId", orderid);
		// 主订单相关字段
		model.addAttribute("sOrderDetailsMain", resp.getsOrderDetailsMain());
		// 子订单相关字段
		model.addAttribute("sOrderDetailsSubs", resp.getsOrderDetailsSubs());
		// 订单优惠相关字段
		model.addAttribute("sOrderDetailsDiscount", resp.getsOrderDetailsDiscount());
		// 订单跟踪相关字段
		model.addAttribute("sOrderDetailsTracks", resp.getsOrderDetailsTracks());
		// 订单收货地址相关字段
		model.addAttribute("sOrderDetailsAddr", resp.getsOrderDetailsAddr());
		// 订单普通发票相关字段
		model.addAttribute("sOrderDetailsComm", resp.getsOrderDetailsComm());
		// 订单增值税发票相关字段
		model.addAttribute("sOrderDetailsTax", resp.getsOrderDetailsTax());
		// 物流信息相关字段
		model.addAttribute("sOrderDetailsDeliverys", resp.getsOrderDetailsDeliverys());

		Map<String, Integer> status = ParamsTool.getStatusMap();
		List<String> statuslist = ParamsTool.getStatusList();
		model.addAttribute("status", status);
		model.addAttribute("statuslist", statuslist);
		// 是否启用编辑标志 1:启用 0:不启用
		model.addAttribute("editflag", flag);

		return "/order/customer/order-detail";
	}

	@RequestMapping(value = "/billinfo/{orderId}")
	public String billinfo(Model model,@PathVariable("orderId") String orderId) {
		model.addAttribute("orderId", orderId);
		SOrderDetailsComm  sOrderDetailsComm = ordMainRSV.queryOrderInvoice(orderId);
		model.addAttribute("billinfo", sOrderDetailsComm);
		return "/seller/order/customer/bill/bill-info";
	}
	//修改收货人信息
	@RequestMapping(value = "/buyerinfo/{orderId}")
	public String buyerinfo(Model model,@PathVariable("orderId") String orderId) {
		model.addAttribute("orderId", orderId);
		//查询收货信息
		ROrderIdRequest req=new ROrderIdRequest();
		req.setOrderId(orderId);
		ROrdAddressResponse resp=ordMainRSV.queryOrderAddress(req);
		model.addAttribute("buyerAddr", resp);
		return "/seller/order/customer/address/buyer-addressupdate";
	}
	
	@RequestMapping(value = "/msginfo/{orderId}")
	public String msginfo(Model model,@PathVariable("orderId") String orderId) {
		model.addAttribute("orderId", orderId);
		return "/seller/order/customer/buymsg/buyrmsg";
	}
	
	//收货地址保存
	@RequestMapping(value="/saveaddr")
	@ResponseBody
	public Map<String,Object> saveAddr(@ModelAttribute CustAddrVO custaddr){
		Map<String, Object> cust = new HashMap<String, Object>();
	    LogUtil.info(MODULE, "============== 保存店铺收货地址    开始  =============");       
        ROrdAddressRequest rOrdAddressRequest = new ROrdAddressRequest();	       
        try{
        	ObjectCopyUtil.copyObjValue(custaddr, rOrdAddressRequest, "", false);
            ordMainRSV.updateOrderAddress(rOrdAddressRequest);
            cust.put("resultFlag","ok");            
        }catch(Exception e){
            LogUtil.error(MODULE, "保存地址出错");
            cust.put("resultFlag","expt");	            
        }
        return cust;
	}
	
	//发票信息保存
	@RequestMapping(value="/savebill")
	@ResponseBody
	public Map<String,Object> savebill(@ModelAttribute OrderInvoiceVO orderInvoiceVO){
		Map<String, Object> cust = new HashMap<String, Object>();
	    LogUtil.info(MODULE, "============== 保存发票信息    开始  =============");              
        try{
        	ROrderInvoiceReq req = new ROrderInvoiceReq();
        	ObjectCopyUtil.copyObjValue(orderInvoiceVO, req, "", false);
        	ordMainRSV.updateInvoice(req);
            cust.put("resultFlag","ok");            
        }catch(Exception e){
            LogUtil.error(MODULE, "保存地址出错");
            cust.put("resultFlag","expt");	            
        }
        return cust;
	}
	
	//发票信息保存
		@RequestMapping(value="/savebuymsg")
		@ResponseBody
		public Map<String,Object> saveBuyMsg(@ModelAttribute OrderBuyMsgVO orderBuyMsgVO){
			Map<String, Object> cust = new HashMap<String, Object>();
		    LogUtil.info(MODULE, "============== 保存发票信息    开始  =============");              
	        try{
	        	ROrdBuyerMsgReq req = new ROrdBuyerMsgReq();
	        	ObjectCopyUtil.copyObjValue(orderBuyMsgVO, req, "", false);
	        	ordMainRSV.updateBuyerMsg(req);
	            cust.put("resultFlag","ok");            
	        }catch(Exception e){
	            LogUtil.error(MODULE, "保存地址出错");
	            cust.put("resultFlag","expt");	            
	        }
	        return cust;
		}
	
	
	/**
	 * 根据key值获取json中的value
	 * @param sign
	 * @param key
	 * @return
	 */
	public String getSignString(String sign,String key){		
		JSONObject jsonObj = JSONObject.parseObject(sign);
		Object obj = jsonObj.get(key);
		if(obj!=null){
			return obj.toString();
		}
		return null;
	}
	
	/**
	 * 客服账号认证
	 * @param userCode
	 * @return
	 */
	private boolean auth(String userCode){
		try{
			AuthStaffAdminReqDTO req = new AuthStaffAdminReqDTO();
			req.setStaffCode(userCode);
			AuthStaffAdminResDTO adminDto = authAdminRSV.findAuthStaffAdminByCode(req);
			if(adminDto!=null&&adminDto.getId()!=null&&adminDto.getId()>0){
				return true;
			}
			return false;
		}catch(BusinessException e){
			return false;
		}
	}

	@RequestMapping(value = "/managegrid")
	public String manageGrid(Model model) {
		model.addAllAttributes(ParamsTool.paramsToday());
		return "/seller/order/orderUpdate/order-grid";
	}
	@RequestMapping("/gridlist")
	public String gridList(Model model, RDelyOrderReqVO vo) throws Exception {
	    //解决前台传入收货人姓名【中文】后，后台接收出现乱码问题，解决方案：先解码，再编码。
	    String contactName=vo.getContactName();
	    if(StringUtil.isNotBlank(contactName)){
	        byte[] b=contactName.getBytes("ISO-8859-1");
	        String contactNameStr=new String(b,"utf-8");
	        vo.setContactName(contactNameStr);
	    }
		// 后场服务所需要的DTO；
		RQueryOrderRequest r = new RQueryOrderRequest();
		r = vo.toBaseInfo(RQueryOrderRequest.class);
		ObjectCopyUtil.copyObjValue(vo, r, "", false);
		r.setSysType("00");

		GdsCategoryReqDTO gdsdto = new GdsCategoryReqDTO();
		GdsCategoryRespDTO gdsresp = new GdsCategoryRespDTO();
		if (!StringUtil.isBlank(vo.getCategoryCode())) {
			gdsdto.setCatgCode(vo.getCategoryCode());
			gdsresp = gdsCategoryRSV.queryGdsCategoryByPK(gdsdto);
		}
		if(StringUtil.isNotBlank(vo.getStaffCode())){
			CustInfoReqDTO CustInfoReqDTO  = new CustInfoReqDTO();
			CustInfoReqDTO.setStaffCode(vo.getStaffCode());
			CustInfoResDTO  custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
			if(custInfoResDTO == null){
				PageResponseDTO<RShopOrderResponse> resp = PageResponseDTO.buildByBaseInfo(r, RShopOrderResponse.class);

				model.addAttribute("resp", resp);
		        return "/seller/order/ordbackgdslist/order-detail-todo";
			} else {
				r.setStaffId(custInfoResDTO.getId());
			}

		}
//        r.setStatus(OrdConstants.CustomerRequestStatus.REQUEST_STATUS_ALL);

		r.setCategoryCodes(null);
		// 其它的查询条件；
		LogUtil.debug(MODULE, vo.toString());

		PageResponseDTO<RShopOrderResponse> resp = ordManageRSV.queryOrder(r);

		model.addAttribute("resp", resp);
        return "/seller/order/ordbackgdslist/order-detail-todo";
	}

	private EcpBasePageRespVO<Map> bulidPageResp(PageResponseDTO<RShopOrderResponse> t)
			throws Exception {
		EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
		if (t != null) {
			respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
		}
		return respVO;
	}
	@RequestMapping(value = "/details")
	public String orderUpdateDetails(Model model, @RequestParam("orderId") String orderid) {

		ROrderDetailsRequest rdto = new ROrderDetailsRequest();

		rdto.setOrderId(orderid);
		ROrderDetailsResponse resp = new ROrderDetailsResponse();
		resp = ordDetailsRSV.queryOrderDetails(rdto);

		// 订单id
		model.addAttribute("orderId", orderid);
		// 主订单相关字段
		model.addAttribute("sOrderDetailsMain", resp.getsOrderDetailsMain());
		// 子订单相关字段
		model.addAttribute("sOrderDetailsSubs", resp.getsOrderDetailsSubs());
		// 订单优惠相关字段
		model.addAttribute("sOrderDetailsDiscount", resp.getsOrderDetailsDiscount());
		// 订单跟踪相关字段
		model.addAttribute("sOrderDetailsTracks", resp.getsOrderDetailsTracks());
		// 订单收货地址相关字段
		model.addAttribute("sOrderDetailsAddr", resp.getsOrderDetailsAddr());
		// 订单普通发票相关字段
		model.addAttribute("sOrderDetailsComm", resp.getsOrderDetailsComm());
		// 订单增值税发票相关字段
		model.addAttribute("sOrderDetailsTax", resp.getsOrderDetailsTax());
		// 物流信息相关字段
		model.addAttribute("sOrderDetailsDeliverys", resp.getsOrderDetailsDeliverys());

		Map<String, Integer> status = ParamsTool.getStatusMap();
		List<String> statuslist = ParamsTool.getStatusList();
		model.addAttribute("status", status);
		model.addAttribute("statuslist", statuslist);
		// 是否启用编辑标志 1:启用 0:不启用
		model.addAttribute("editflag", "1");

		return "/seller/order/orderUpdate/order-detail";
	}
	
	@RequestMapping(value = "/sellerMsginfo/{orderId}")
	public String sellerMsginfo(Model model,@PathVariable("orderId") String orderId) {
		ROrdSellerMsgDTO rOrdSellerMsgDTO = new ROrdSellerMsgDTO();
		rOrdSellerMsgDTO.setOrderId(orderId);
		ROrdSellerMsgDTO resp = ordMainRSV.querySellerMsgByOrderId(rOrdSellerMsgDTO);
		model.addAttribute("resp", resp);
		return "/seller/order/customer/sellermsg/sellermsg";
	}
	/**
	 * 保存卖家留言
	 * @param orderSellerMsgVO
	 * @return
	 */
	@RequestMapping(value="/updateSellerMsg")
	@ResponseBody
	public Map<String,Object> updateSellerMsg(@ModelAttribute OrderSellerMsgVO orderSellerMsgVO){
		Map<String, Object> cust = new HashMap<String, Object>();
	    LogUtil.info(MODULE, "============== 开始保存卖家留言  =============");              
        try{
        	ROrdSellerMsgDTO rOrdSellerMsgDTO = new ROrdSellerMsgDTO();
        	ObjectCopyUtil.copyObjValue(orderSellerMsgVO, rOrdSellerMsgDTO, "", false);
        	ordMainRSV.updateSellerMsg(rOrdSellerMsgDTO);
            cust.put("resultFlag","ok");  
        }catch(Exception e){
            LogUtil.error(MODULE, "保存卖家留言出错",e);
            cust.put("resultFlag","expt");	            
        }
        return cust;
	}
	
	@RequestMapping(value = "/sellerDeliveryinfo/{deliveryId}")
	public String deliveryInfo(Model model,@PathVariable("deliveryId") String deliveryId){
	    OrdDeliveryBatchReq req=new OrdDeliveryBatchReq();
	    Long id = null;
	    try {
	       id = Long.parseLong(deliveryId);
        } catch (Exception e) {
            throw e;
        }
	    req.setId(id);
	    //物流信息详情
	    OrdDeliveryBatchResp ordDeliveryBatchResp = 
	            orderExpressRSV.queryDeliveryByID(id);
	    Long shopId = ordDeliveryBatchResp.getShopId();
	    //根据shopId查询该店铺所支持的物流公司Id
	    Map<Long, String> logistics = shopInfoRSV.listExpress(shopId);
	    model.addAttribute("ordDeliveryBatchResp", ordDeliveryBatchResp);
	    model.addAttribute("logistics", logistics);
	    return "/seller/order/customer/delivery/delivery-update";
	}
	
	/**
	 * updateDeliveryInfo:(保存物流信息). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author mwz 
	 * @param orderSellerMsgVO
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/saveDelivereyInfo")
    @ResponseBody
    public Map<String,Object> updateDeliveryInfo(@ModelAttribute OrdDeliveryBatchVO ordDeliveryBatchVO){
        Map<String, Object> cust = new HashMap<String, Object>();
        LogUtil.info(MODULE, "============== 开始修改物流信息  =============");              
        try{
            //判断物流方式
            String deliveryType = ordDeliveryBatchVO.getDeliveryType();
            //自提(2)没有物流公司和快递单号/邮局挂号(0)没有物流公司    快递(1)
            if("2".equals(deliveryType)){
                ordDeliveryBatchVO.setExpressId(null);
                ordDeliveryBatchVO.setExpressNo("");
            }else if("0".equals(deliveryType)){
                ordDeliveryBatchVO.setExpressId(null);
            }
            OrdDeliveryBatchReq ordDeliveryBatchReq=new OrdDeliveryBatchReq();
            ObjectCopyUtil.copyObjValue(ordDeliveryBatchVO,ordDeliveryBatchReq,null, false);
            orderExpressRSV.updateDeliveryInfo(ordDeliveryBatchReq);
            cust.put("resultFlag","ok");  
        }catch(Exception e){
            LogUtil.error(MODULE, "修改物流信息出错",e);
            cust.put("resultFlag","expt");              
        }
        return cust;
    }
}
