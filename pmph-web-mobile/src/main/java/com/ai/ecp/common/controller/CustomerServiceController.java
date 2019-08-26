package com.ai.ecp.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.util.BizUtil;
import com.ai.ecp.util.ConstantTool;
import com.ai.ecp.common.vo.CustInfoReqVO;
import com.ai.ecp.common.vo.CustServSatisfyReqVO;
import com.ai.ecp.common.vo.CustomerGdsRespVO;
import com.ai.ecp.common.vo.MessageHistoryReqVO;
import com.ai.ecp.common.vo.RCustomerOrdRespVO;
import com.ai.ecp.common.vo.SessionReqVO;
import com.ai.ecp.common.vo.SessionRespVO;
import com.ai.ecp.common.vo.StaffHotlineVO;
import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsBrowseHisReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsBrowseHisRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoDetailRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsBrowseHisRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.im.dubbo.dto.CustServSatisfyReqDTO;
import com.ai.ecp.im.dubbo.dto.CustServSatisfyResDTO;
import com.ai.ecp.im.dubbo.dto.ImCustReqDTO;
import com.ai.ecp.im.dubbo.dto.ImOfuserRelReqDTO;
import com.ai.ecp.im.dubbo.dto.ImOfuserRelResDTO;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineReqDTO;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineResDTO;
import com.ai.ecp.im.dubbo.interfaces.ICustServiceMgrRSV;
import com.ai.ecp.im.dubbo.interfaces.IOfuserRSV;
import com.ai.ecp.im.dubbo.interfaces.ISatisfyEvaluateRSV;
import com.ai.ecp.im.dubbo.interfaces.IStaffHotlineRSV;
import com.ai.ecp.im.dubbo.util.ImConstants;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsSub;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.MongoUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;



/**
 * 
 * Project Name:pmph-web-mobile <br>
 * Description: 客服系统-微信客户端会话功能<br>
 * Date:2017年3月8日下午5:56:00 <br>
 * 
 * @version
 * @since JDK 1.6
 */

@Controller
@RequestMapping(value = "/customerservice")
public class CustomerServiceController extends EcpBaseController {

	@Resource
	private ICustManageRSV iCustManageRSV;
	
	@Resource
	private IOfuserRSV iOfuserRSV;
	
	@Resource
	private ICustServiceMgrRSV custServiceMgrRSV;
	
	@Resource
	private IStaffHotlineRSV iStaffHotlineRSV;
	
	@Resource
    private IOrdMainRSV ordMainRSV;
	
	@Resource
	private IGdsBrowseHisRSV gdsBrowseHisRSV; 
	@Resource
	private IGdsInfoQueryRSV gdsInfoQueryRSV;
	
	@Resource
    private ISatisfyEvaluateRSV satisfyEvaluateRSV;
	
	private static String MODULE = CustomerServiceController.class.getName();
	//客服页面
	@RequestMapping(value = "/index/{shopId}-{issueType}-{gsdandOrdId}")
	public String index(Model model, EcpBasePageReqVO vo,HttpServletRequest request,@PathVariable(value="shopId")String shopId,@PathVariable(value="issueType")String issueType,@PathVariable(value="gsdandOrdId")String gsdandOrdId) throws Exception {
		model.addAttribute("issueType", issueType);
		model.addAttribute("boshService", BizUtil.getBOSH());
		if("1".equals(issueType)){
			model.addAttribute("ordId",gsdandOrdId);
		}else{
			model.addAttribute("gdsId",gsdandOrdId);
		}
		CustInfoReqDTO arg0 = new CustInfoReqDTO();
		String staffCode = arg0.getStaffCode();
		CustInfoResDTO custInfoResDTO = null;
		if(StringUtil.isNotBlank(staffCode)){
			arg0.setStaffCode(staffCode);
			custInfoResDTO = iCustManageRSV.findCustInfo(arg0);
		}else{
			custInfoResDTO = iCustManageRSV.findCustInfoById(arg0.getStaff().getId());
		}
		if(null!=custInfoResDTO){
			ImOfuserRelReqDTO dto = new ImOfuserRelReqDTO();
			dto.setStaffCode(custInfoResDTO.getStaffCode());
			dto.setStaffId(arg0.getStaff().getId());
			ImOfuserRelResDTO imOfuserRelResDTO = iOfuserRSV.findofuserByCust(dto);
			model.addAttribute("uName", StringUtil.isBlank(imOfuserRelResDTO.getOfStaffCode())?"":imOfuserRelResDTO.getOfStaffCode()+BizUtil.getOfServer());
			model.addAttribute("staffCode", custInfoResDTO.getStaffCode());
			model.addAttribute("uNameForLogout", imOfuserRelResDTO.getOfStaffCode());
			model.addAttribute("custLevel", custInfoResDTO.getCustLevelCode());
			if(StringUtil.isNotBlank(custInfoResDTO.getCustPic())){
				String imageUrl = ImageUtil.getImageUrl(custInfoResDTO.getCustPic()+"_50x50!");
				model.addAttribute("custPic", imageUrl);
			}else{
				BaseSysCfgRespDTO baseSysCfgRespDTO = SysCfgUtil.fetchSysCfg("IM_USER_PIC");
				String imageUrl = ImageUtil.getImageUrl(baseSysCfgRespDTO.getParaValue()+"_50x50!");
				model.addAttribute("custPic", imageUrl);
			}
		}
		
		//目前只有一家店铺
		model.addAttribute("shopId", StringUtil.isBlank(shopId)?"人民卫生出版社":"人民卫生出版社");
		model.addAttribute("shopIdForLogout",shopId);
		BaseSysCfgRespDTO baseSysCfgRespDTO = SysCfgUtil.fetchSysCfg("IM_CSA_PIC");
		if(null!=baseSysCfgRespDTO){
			model.addAttribute("servPic", baseSysCfgRespDTO.getParaValue());
		}
		BaseSysCfgRespDTO baseSysCfgRespDTO1 = SysCfgUtil.fetchSysCfg("IM_USER_SESSION_TIME");
		if(null!=baseSysCfgRespDTO1){
		model.addAttribute("sessionTime", baseSysCfgRespDTO1.getParaValue());
		}
		
		return "/customer/customer-service";
	}
	
	/**
	 * 排队获取等待人数，为0时接入
	 * @param custInfoReqVO
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getHotlineQueue")
	@ResponseBody
	public Map<String,Object> getHotlineQueue(CustInfoReqVO custInfoReqVO)throws Exception{
		Map<String,Object> map = new HashMap<>();
		ImCustReqDTO imCustReqDTO = new ImCustReqDTO();
		SessionReqVO reqVO = new SessionReqVO();
		imCustReqDTO.setOfStaffCode(custInfoReqVO.getOfStaffCode());
		imCustReqDTO.setBusinessType(custInfoReqVO.getBusinessType());
		imCustReqDTO.setShopId(custInfoReqVO.getShopId());
		imCustReqDTO.setCustLevel(custInfoReqVO.getCustLevelCode());
	    String issueType = String.valueOf(custInfoReqVO.getBusinessType());
	    if(issueType.equals(BizUtil.issueType_1)){
			reqVO.setOrdId(custInfoReqVO.getOrderId());
			imCustReqDTO.setBusinessId(custInfoReqVO.getOrderId());
		}else if(issueType.equals(BizUtil.issueType_2)){
			reqVO.setGdsId(custInfoReqVO.getGoodsId());
			imCustReqDTO.setBusinessId(custInfoReqVO.getGoodsId());
		}
	    ImStaffHotlineResDTO imStaffHotlineResDTO = getStaffHotline(imCustReqDTO);
	    if(imStaffHotlineResDTO.getWaitCount()==0&&StringUtil.isNotBlank(imStaffHotlineResDTO.getCsaCode())){
	    	ImStaffHotlineReqDTO dto = new ImStaffHotlineReqDTO();
	    	dto.setOfStaffCode(imStaffHotlineResDTO.getCsaCode());
	    	ImStaffHotlineResDTO hotlineResDTO = iStaffHotlineRSV.getStaffHotline(dto);
	    	map.put("serName", hotlineResDTO.getHotlinePerson());
			String csaCode = imStaffHotlineResDTO.getCsaCode();
			map.put("csaCode", csaCode);
			map.put("shopId", hotlineResDTO.getShopId());
			map.put("ofserver", BizUtil.getOfServer());
			map.put("waitCount", imStaffHotlineResDTO.getWaitCount());
				String uuid="";
				reqVO.setCsaCode(imStaffHotlineResDTO.getCsaCode());
				reqVO.setIssueType(issueType);
				reqVO.setStatus(BizUtil.status_1);
				reqVO.setUserCode(custInfoReqVO.getOfStaffCode());
				reqVO.setSessionTime(DateUtil.getSysDate());
				reqVO.setShopId(custInfoReqVO.getShopId());
				reqVO.setSource("WEB");
				SessionRespVO respVO = getSession(reqVO);
				if(StringUtil.isNotBlank(respVO.getId())){
					updateSession(respVO);
				}
				
				uuid = saveSession(reqVO);
				map.put("sessionId", uuid);
	    }else{
	    	map.put("waitCount", imStaffHotlineResDTO.getWaitCount());
	    }
	    
		
		return map;
	}
	
	public ImStaffHotlineResDTO getStaffHotline(ImCustReqDTO imCustReqDTO){
		ImStaffHotlineResDTO dto = new ImStaffHotlineResDTO();
		try {
			dto =	custServiceMgrRSV.getStaffHotline(imCustReqDTO);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return dto;
	}
	
	/**
	 * 获取session会话
	 * @param reqVO
	 * @return
	 */
	public SessionRespVO getSession(SessionReqVO reqVO){
		SessionRespVO respVO = new SessionRespVO();
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_SESSION_HISTORY");
		QueryBuilder doc = new QueryBuilder();  
		
		doc.and("csaCode").is(reqVO.getCsaCode());
		doc.and("userCode").is(reqVO.getUserCode());
		doc.and("status").is(reqVO.getStatus());
		doc.and("source").is(reqVO.getSource());
		
		DBCursor cursor= collection.find(doc.get());
		try {
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				ConstantTool.dbObjectToBean(obj, respVO);
			}
		} finally {
			cursor.close();
		}
		return respVO;
	}
	
	/**
	 * 请求创建session
	 * @param respVO
	 * @return
	 */
	public String saveSession(SessionReqVO reqVO){
		String uuid = UUID.randomUUID().toString();
		reqVO.setId(uuid);
		JSONObject doc = new JSONObject();
		doc = (JSONObject) JSON.toJSON(reqVO);
		MongoUtil.insert("T_IM_SESSION_HISTORY", doc);
		return uuid;
	}
	
	/**
	 * 每次接入都将之前的session置为失效
	 * @param reqVO
	 * @return
	 */
	public int updateSession(SessionRespVO reqVO){
		try {
			BasicDBObject query = new BasicDBObject();
			query.put("id", reqVO.getId());
			DBObject stuFound = MongoUtil.getDBCollection("T_IM_SESSION_HISTORY").findOne(query);
			stuFound.put("status", ImConstants.STATE_0);
			WriteResult result = MongoUtil.getDBCollection("T_IM_SESSION_HISTORY").update(query, stuFound);
		/*	if(result.getN()>0){
				ImStaffHotlineReqDTO dto = new ImStaffHotlineReqDTO();
				dto.setCsaCode(reqVO.getCsaCode());
				dto.setSessionId(reqVO.getId());
				custServiceMgrRSV.finishChat(dto);
			}*/
			return result.getN();
		} catch (Exception e) {
			throw new BusinessException("会话结束失败");
		}
		
		
	}

	/**
	 * 
	 * orderMsg:(我的订单). <br/> 
	 * 
	 * @param 
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/ordermsg")
	@ResponseBody
	public List<RCustomerOrdRespVO> orderMsg(Model model, MessageHistoryReqVO vo) throws BusinessException {
		
		RQueryOrderRequest rdor = new RQueryOrderRequest();
		rdor.setStaffId(rdor.getStaff().getId());
		//rdor.setStaffId(Long.parseLong("6600039"));	
		rdor.setSiteId(rdor.getCurrentSiteId());
		rdor.setShopId(vo.getShopId());
		rdor.setPageNo(vo.getPageNumber());
		rdor.setPageSize(vo.getPageSize());
		List<RCustomerOrdRespVO> ord01201Resps = new ArrayList<RCustomerOrdRespVO>();//主订单相关信息
		try {
			PageResponseDTO<RCustomerOrdResponse> rdors = this.ordMainRSV.queryOrderByStaffId(rdor);
			if(rdors != null && rdors.getResult() != null){
				for(RCustomerOrdResponse rord : rdors.getResult()){
					RCustomerOrdRespVO ord01201Resp = new RCustomerOrdRespVO();
					List<SOrderDetailsSub> ord01202Resps = new ArrayList<SOrderDetailsSub>();//子订单相关信息
					for(SOrderDetailsSub sds : rord.getsOrderDetailsSubs()){
						SOrderDetailsSub ord01202Resp = new SOrderDetailsSub();
						ObjectCopyUtil.copyObjValue(sds, ord01202Resp, "", false);
						ord01202Resps.add(ord01202Resp);
					}
					ObjectCopyUtil.copyObjValue(rord.getsCustomerOrdMain(), ord01201Resp, "", false);
					ord01201Resp.setOrd01102Resps(ord01202Resps);
					ord01201Resps.add(ord01201Resp);
				}
			}
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "查询订单信息失败", e);
			throw new BusinessException("查询订单信息失败");
		}
		return ord01201Resps;
	}
	
	/**
	 * 
	 * orderMsg:(浏览商品). <br/> 
	 * 
	 * @param 
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/browsegoodsmsg")
	@ResponseBody
	public List<CustomerGdsRespVO> browseGoodsMsg(Model model, MessageHistoryReqVO vo) throws BusinessException {
		List<CustomerGdsRespVO> listvo = new ArrayList<CustomerGdsRespVO>();
		GdsBrowseHisReqDTO gdsBrowseHisReqDTO = new GdsBrowseHisReqDTO();
		gdsBrowseHisReqDTO.setStaffId(gdsBrowseHisReqDTO.getStaff().getId());//当前用户
		gdsBrowseHisReqDTO.setShopId(vo.getShopId());	//访问店铺
		gdsBrowseHisReqDTO.setStatus("1");
		gdsBrowseHisReqDTO.setPageNo(vo.getPageNumber());	//当前页
		gdsBrowseHisReqDTO.setPageSize(vo.getPageSize());//分页大小
		PageResponseDTO<GdsBrowseHisRespDTO> pageGdsBrowseHisRespDTO = gdsBrowseHisRSV.queryGdsBrowseHisByPage(gdsBrowseHisReqDTO);
		if(pageGdsBrowseHisRespDTO == null){
			throw new BusinessException("浏览商品历史服务查询失败");
		}
		List<GdsBrowseHisRespDTO> listGdsBrowseHisRespDTO = pageGdsBrowseHisRespDTO.getResult();
		if(CollectionUtils.isNotEmpty(listGdsBrowseHisRespDTO)){
			for (GdsBrowseHisRespDTO rspDTO : listGdsBrowseHisRespDTO) {
				CustomerGdsRespVO vo01 = new CustomerGdsRespVO();
				GdsInfoDetailRespDTO gdsInfo = this.getGdsInfo(rspDTO.getGdsId());
				if(gdsInfo!=null){
					vo01.setId(gdsInfo.getId());
					vo01.setFirstSkuId(gdsInfo.getSkuInfo()==null?null:gdsInfo.getSkuInfo().getId());
					vo01.setImageUrl(gdsInfo.getMainPic()==null?"":gdsInfo.getMainPic().getURL());
					vo01.setGdsName(gdsInfo.getGdsName());
					vo01.setGdsSubHead(gdsInfo.getGdsSubHead());
					vo01.setGdsDesc(StringUtil.isBlank(gdsInfo.getGdsDesc())?"":FileUtil.readFile2Text(gdsInfo.getGdsDesc(), "UTF-8"));
					vo01.setShopId(gdsInfo.getShopId());
					vo01.setDefaultPrice(gdsInfo.getSkuInfo().getRealPrice());//默认
					listvo.add(vo01);
				}
			}
		}
		
		return listvo;
	}
	
	//获取商品信息
	private GdsInfoDetailRespDTO getGdsInfo(Long gdsId) throws BusinessException{
		GdsInfoReqDTO dto = new GdsInfoReqDTO();
        dto.setId(gdsId);
        GdsQueryOption[] gdsQueryOptions = new GdsQueryOption[2];
        SkuQueryOption[] skuQuerys = new SkuQueryOption[2];
        gdsQueryOptions[0] = GdsQueryOption.BASIC;
        gdsQueryOptions[1] = GdsQueryOption.MAINPIC;
        skuQuerys[0] = SkuQueryOption.BASIC;
        skuQuerys[1] = SkuQueryOption.PRICE;
        dto.setGdsQueryOptions(gdsQueryOptions);
        dto.setSkuQuerys(skuQuerys);
        GdsInfoDetailRespDTO resultDto = gdsInfoQueryRSV.queryGdsInfoDetail(dto);

		return resultDto;
	}
	
	
	/**
	 * 
	 * updateMsgStatus:(断开与客服的连接). <br/> 
	 * 
	 * @param staffHotlineVO
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	@RequestMapping("/disconn")
	@ResponseBody
	public EcpBaseResponseVO updateMsgStatus(StaffHotlineVO staffHotlineVO)throws BusinessException{
		
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		ImStaffHotlineReqDTO req = new ImStaffHotlineReqDTO();
		ObjectCopyUtil.copyObjValue(staffHotlineVO, req, null, false);
		//req.setStaffId(req.getStaff().getId());
		req.setOfStaffCode(staffHotlineVO.getOfStaffCode());
		//调用后场服务，结果客户端的会话
		custServiceMgrRSV.finishChat(req);
		vo.setResultFlag("ok");
		return vo;
	}
	
	/**
     * 
     * saveEvaluate:(保存客户评价). <br/>  
     * 
     * @param saveEvaluate
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/saveEvaluate")
    @ResponseBody
    public EcpBaseResponseVO saveEvaluate(CustServSatisfyReqVO vo){
    	EcpBaseResponseVO respVo = new EcpBaseResponseVO();
        CustServSatisfyReqDTO reqDto = new CustServSatisfyReqDTO();
        ObjectCopyUtil.copyObjValue(vo, reqDto, null, false); 
        try{
            CustServSatisfyResDTO respDto = satisfyEvaluateRSV.addSatisfyEvaluate(reqDto);
            ObjectCopyUtil.copyObjValue(respDto, respVo, null, false);
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        }catch(Exception e){
            LogUtil.error(MODULE, "提交评价出错");
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            respVo.setResultMsg(e.getMessage());
        }
        return respVo;
    } 
	
    /**
     * 
     * evaluateCheck:(查询客户是否已评价). <br/>  
     * 
     * @param evaluateCheck
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/evaluateCheck")
    @ResponseBody
    public EcpBaseResponseVO evaluateCheck(CustServSatisfyReqVO vo){
    	EcpBaseResponseVO respVo = new EcpBaseResponseVO();
        CustServSatisfyReqDTO reqDto = new CustServSatisfyReqDTO();
        ObjectCopyUtil.copyObjValue(vo, reqDto, null, false); 
        CustServSatisfyResDTO respDto = satisfyEvaluateRSV.qrySatisfyEvaluate(reqDto);
        if(StringUtil.isEmpty(respDto)){
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        }else{
            respVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            respVo.setResultMsg("您已提交评价~");
        }
        return respVo;
         
    }

}
