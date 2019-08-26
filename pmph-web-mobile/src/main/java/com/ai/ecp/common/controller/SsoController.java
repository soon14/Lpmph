package com.ai.ecp.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.antlr.v4.parse.GrammarTreeVisitor.mode_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.util.AppUserCacheUtils;
import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.buyer.vo.ModifyPwdVO;
import com.ai.ecp.busi.common.vo.RegisterVO;
import com.ai.ecp.server.front.dto.BaseStaff;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.AuthStaffReqDTO;
import com.ai.ecp.staff.dubbo.dto.AuthStaffResDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustWechatRelReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustWechatRelRespDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoMsgResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.IAuthLoginRSV;
import com.ai.ecp.staff.dubbo.interfaces.IAuthStaffRSV;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.ICustWechatRelRSV;
import com.ai.ecp.staff.dubbo.interfaces.ISsoUserImportRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.ecp.util.SsoUtil;
import com.ai.ecp.wxbase.util.WeixinUtil;
import com.ai.ecp.wxbase.util.WxConstants;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.core.config.Application;
import com.ailk.butterfly.core.web.WebConstants;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * 
 * Project Name:ecp-web-mall <br>
 * Description: <br>
 * Date:2015年10月9日上午9:27:02 <br>
 * 
 * @version
 * @since JDK 1.6
 * 
 *        用户登录
 */

@Controller
@RequestMapping(value = "/sso")
public class SsoController extends EcpBaseController {

    private static String MODULE = SsoController.class.getName();

    @Autowired
    protected HttpSession session;
    
    @Resource
    private IAuthStaffRSV authStaffRSV;
    
    @Resource
    private IAuthLoginRSV authLoginRSV;
    
    @Resource
    private ICustManageRSV custManageRSV;
    
    @Resource
    private ICustWechatRelRSV custWechatRelRSV;
    
	@Resource
	private ISsoUserImportRSV ssoUserImportRSV;
    
    
	/**
	 * 登录校验
	 * @param request
	 * @param j_username
	 * @param j_password
	 * @return
	 */
    @RequestMapping("/ssocheck")
    @ResponseBody
    public EcpBaseResponseVO ssoCheck(HttpServletRequest request,@RequestParam("j_username") String j_username,@RequestParam("j_password") String j_password,@RequestParam("openId") String openId){

    	EcpBaseResponseVO vo = new EcpBaseResponseVO();
    	String url = "";
    	Map<Object, Object> map = new HashMap<Object, Object>();
   		map.put("username", SsoUtil.UserName);
   		map.put("password", SsoUtil.PassWord);
   		map.put("method", "login");
   		
   		JSONObject value = new JSONObject();
   		value.put("username", j_username);
   		value.put("password", j_password);
   		map.put("params", value.toString());
   		JSONObject jsonObject = SsoUtil.postAuthSso("/api/json", map);
   		
   		String flag = jsonObject.getString("_Status");
 		LogUtil.info(MODULE, jsonObject.toString());
 		
 		if(flag.equals("1")){
 			//String j_openid = (String) CacheUtil.getItem(session.getId() + "_openid");
 			String j_openid =  openId;
 			LogUtil.info(MODULE, "=====================sso同步开始=====================");
 			JSONObject user  =jsonObject.getJSONObject("Mapx");
 			// 此处插入用户数据至本系统
 			CustInfoReqDTO custInfoReq = new CustInfoReqDTO();
 			custInfoReq.setStaffCode(user.getString("UserName"));
		    CustInfoResDTO custInfoRes = custManageRSV.findCustInfo(custInfoReq);
		    Long f = 0l;
		    SsoUserInfoMsgResDTO message = new SsoUserInfoMsgResDTO();
		    if(null==custInfoRes){
		    	SsoUserInfoReqDTO dto = new SsoUserInfoReqDTO();
		    	dto.setUserName(user.getString("UserName"));
	 			if (StringUtil.isNotBlank(user.getString("UserName"))) {
	 				dto.setMobile(user.getString("Mobile"));
	 			}
	 		    message = ssoUserImportRSV.changeStaffInfo(dto);
		    }

 			if (j_openid == null) {
 				vo.setResultMsg("微信openid缺失。");
 				vo.setResultFlag("login");
 			} else {
 				CustInfoReqDTO custReq = new CustInfoReqDTO();
 			    custReq.setStaffCode(user.getString("UserName"));
 			    CustInfoResDTO custRes = custManageRSV.findCustInfo(custReq);
 				// 如果登录成功
 				if (custRes != null && "1".equals(custRes.getStatus())) {
 					// 查询是否已绑定商城的会员
 					CustWechatRelReqDTO wechatReq = new CustWechatRelReqDTO();
 					
 					wechatReq.setStaffId(custRes.getId());
 					wechatReq.setPageNo(1);
 					wechatReq.setPageSize(1);
 					PageResponseDTO<CustWechatRelRespDTO> page = custWechatRelRSV.findCustWechatRel(wechatReq);
 					// 如果没有绑定，则进行绑定
 					if (page == null || CollectionUtils.isEmpty(page.getResult())) {
 						CustWechatRelRespDTO wechatRespDTO = custWechatRelRSV.findCustWechatRelByWechatId(j_openid);
 						if (wechatRespDTO != null && !wechatRespDTO.getStaffCode().equals(custRes.getStaffCode())) {
 							vo.setResultFlag("fail");
 							vo.setResultMsg("该用户" + custRes.getStaffCode() + "已绑定其他微信账号，无法登录。");
 							return vo;
 						}
 						CustWechatRelReqDTO weixinReq = new CustWechatRelReqDTO();
 						weixinReq.setStaffId(custRes.getId());
 						weixinReq.setStaffCode(custRes.getStaffCode());
 						weixinReq.setWechatId(String.valueOf(j_openid));
 						weixinReq.setCreateStaff(custRes.getId());
 						custWechatRelRSV.saveCustWechatRel(weixinReq);
 					} else {
 						CustWechatRelRespDTO resp = page.getResult().get(0);
 						// 判断绑定的微信账号是否跟当前的一致，如果不一致，给出相应提示，不让登录
 						if (!resp.getWechatId().equals(String.valueOf(j_openid))) {
 							vo.setResultFlag("fail");
 							vo.setResultMsg("该用户" + custRes.getStaffCode() + "已绑定其他微信账号，无法登录。");
 							return vo;
 						}
 					}
 					vo.setResultFlag("ok");
 				}
 			}

 		}else{
 			vo.setResultFlag("fail");
 			vo.setResultMsg(jsonObject.getString("_Message"));
 		}
 	    return vo;
    	
    }
    

    /**
     * 短信验证码
     * @param phone
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/smscode")
    @ResponseBody
    public EcpBaseResponseVO getSmsCode(@RequestParam("phone") String phone)throws BusinessException{
    	JSONObject jsonObject = new JSONObject();
    	EcpBaseResponseVO baseResponseVO = new EcpBaseResponseVO();
    	Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		
		map.put("method", "ZAS.SMSSend");
		JSONObject value = new JSONObject();
		value.put("Mobile", phone);
		value.put("CheckUsed", "N");
		map.put("params", value.toString());
        try {
        	jsonObject  = SsoUtil.postAuthSso("/api/json", map);
    		boolean flag = jsonObject.getBoolean("Success");
    		if(flag){
    			baseResponseVO.setResultFlag("ok");
    		
    		}else{
    			baseResponseVO.setResultFlag("fail");
    			baseResponseVO.setResultMsg(jsonObject.getString("Message"));
    		}
		} catch (Exception e) {
			throw new BusinessException("获取短信验证码失败");
		}
    	
    	return baseResponseVO;
    }
    /**
     * 
     * unbind:(解除绑定). <br/> 
     * 跳转到登录页面，清除session里面的openId
     * @param request
     * @param openId
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping("/unbind")
    @ResponseBody
    public EcpBaseResponseVO unbind(HttpServletRequest request) throws Exception{
    	EcpBaseResponseVO vo = new EcpBaseResponseVO();
    	CustWechatRelReqDTO req = new CustWechatRelReqDTO();
    	req.setStaffId(req.getStaff().getId());
    	custWechatRelRSV.deleteCustWechatRelBy(req);
    	//request.removeAttribute("openId");
    	request.removeAttribute("openIdForLogin");
    	vo.setResultFlag("ok");
        return vo;
    }
    
    /**
     * sso注册服务
     * @param request
     * @param registerVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/regist")
	@ResponseBody
	public EcpBaseResponseVO regist(HttpServletRequest request,
			RegisterVO registerVO) throws Exception {
		
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		try {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		
		map.put("method", "ZAS.MobileRegist");
		JSONObject value = new JSONObject();
		value.put("Mobile", registerVO.getSerialNumber());
		value.put("SMSCode", registerVO.getPhoneCode());
		value.put("Password", registerVO.getStaffPassword());
		value.put("PwdIsEncryption", "N");
		map.put("params", value.toString());
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
		boolean flag = jsonObject.getBoolean("Success");
    	if(flag){
    		String openId =(String) CacheUtil.getItem(session.getId() + "_openid");
    		if (openId == null) {
    			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
    			vo.setResultMsg("微信openid缺失。");
    			return vo;
    		}

    		CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
    		ObjectCopyUtil.copyObjValue(registerVO, custInfoReqDTO, null, false);
    		custInfoReqDTO.setCustType(StaffConstants.custInfo.CUST_TYPE_P);
    		custInfoReqDTO.setStaffPassword("123456");
    		custInfoReqDTO.setStaffCode(registerVO.getSerialNumber());
    		Long id = custManageRSV.saveCustInfo(custInfoReqDTO);
    		
    		CustInfoReqDTO custReq = new CustInfoReqDTO();
    		custReq.setId(id);
    		CustInfoResDTO custRes = custManageRSV.findCustInfo(custReq);
    		// 如果登录成功
    		if (custRes != null && "1".equals(custRes.getStatus())) {
    			CustWechatRelReqDTO wechatReq = new CustWechatRelReqDTO();
    			wechatReq.setWechatId(String.valueOf(openId));
    			wechatReq.setStaffCode(custRes.getStaffCode());
    			wechatReq.setStaffId(custRes.getId());
    			wechatReq.setCreateStaff(custRes.getId());
    			custWechatRelRSV.saveCustWechatRel(wechatReq);
    		}
    		vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
    		vo.setResultMsg("注册成功！");
    	
    	}else{
    		vo.setResultFlag("fail");
    		if(jsonObject.containsKey("_Message")){
    			vo.setResultMsg(jsonObject.getString("_Message"));
    		}else if(jsonObject.containsKey("Message")){
    			vo.setResultMsg(jsonObject.getString("Message"));
    		}else{
    			vo.setResultMsg("注册失败，请检查网络");
    		}
    	}
    	} catch (BusinessException e) {
			throw new BusinessException(e.getMessage());
		}
		
		return vo;
	}
    
	/**
	 * 
	 * savePwd:(保存密码修改). <br/> 
	 * 
	 * @param custInfoVO
	 * @return
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/savepwd")
	@ResponseBody
	public EcpBaseResponseVO savePwd(ModifyPwdVO vo)throws BusinessException{
		EcpBaseResponseVO res = new EcpBaseResponseVO();
		AuthStaffReqDTO req = new AuthStaffReqDTO();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		map1.put("username",  SsoUtil.UserName);
		map1.put("password", SsoUtil.PassWord);
  		
		map1.put("method", "ZAS.UserInfo");
  		JSONObject value1 = new JSONObject();
  		value1.put("Password", vo.getOldPassword());
  		value1.put("LoginID", req.getStaff().getStaffCode());
  		map1.put("params", value1.toString());
  		JSONObject jsonObject1  = SsoUtil.postAuthSso("/api/json", map1);
  		System.out.println(jsonObject1);
  		if(jsonObject1.containsKey("Success")){
  			boolean flag1 = jsonObject1.getBoolean("Success");
  			if(flag1){
  		      req.setId(req.getStaff().getId());//id
  		        req.setStaffPasswd(vo.getNewPassword());//新密码
  		        req.setStaffPwdOld(vo.getOldPassword());//旧密码
  		        try {
  		        	Map<Object, Object> map = new HashMap<Object, Object>();
  		      		map.put("username", SsoUtil.UserName);
  		      		map.put("password", SsoUtil.PassWord);
  		      		
  		      		map.put("method", "ZAS.ModifyUser");
  		      		JSONObject value = new JSONObject();
  		      		value.put("Password", vo.getNewPassword());
  		      		value.put("LoginID", req.getStaff().getStaffCode());
  		      		map.put("params", value.toString());
  		      		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
  		      		String flag = jsonObject.getString("_Status");
  		      		if(flag.equals("1")){
  		      			res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
  		      		}
  		        	//authStaffRSV.updatePwdById(req);
  		        
  				} catch (Exception e) {
  					res.setResultMsg(e.getMessage());
  					res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
  				}
  		}else{
  			res.setResultMsg("旧密码错误");
  			res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
  		   }
  		}else{
  			res.setResultMsg("旧密码错误");
  			res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
  		}
		return res;
	}
	
	/**
	 * 第三方登录：微博
	 * @param request
	 * @param clientId
	 * @param ContentType
	 * @return
	 */
    @RequestMapping("/third/weibologin")
    @ResponseBody
    public EcpBaseResponseVO weiboLogin(HttpServletRequest request,@RequestParam("ClientId") String client_id,@RequestParam("ContentType") String ContentType,@RequestParam("Referer") String Referer){
    	EcpBaseResponseVO vo = new EcpBaseResponseVO();
    	Map<Object, Object> map = new HashMap<Object, Object>();
   		map.put("client_id", SsoUtil.ClientId);
   		map.put("ContentType", ContentType);
   		map.put("referer",Referer);
   		LogUtil.info(MODULE, "Referer:"+Referer);
   		JSONObject jsonObject = SsoUtil.postAuthSso("/getSocialURL", map);
   		String flag = jsonObject.getString("Success");
 		LogUtil.info(MODULE, jsonObject.toString());
 		if(flag.equals("true")){
 			JSONObject Report	= jsonObject.getJSONObject("Report");
 			if((Report != null) && !(Report.isNullObject())){
 				JSONObject Weibo = Report.getJSONObject("Weibo");
 				if((Weibo != null) && !(Weibo.isNullObject())){
 					String LoginURL = Weibo.getString("LoginURL");
 					vo.setResultFlag("ok");
 		 			vo.setResultMsg(LoginURL);
 				}
 			}
 		}else{
 			vo.setResultFlag("fail");
 			vo.setResultMsg(jsonObject.getString("Message"));
 		}
 	    return vo;
    	
    }
    
    
    /**
	 * 第三方登录：QQ
	 * @param request
	 * @param clientId
	 * @param ContentType
	 * @return
	 */
    @RequestMapping("/third/qqlogin")
    @ResponseBody
    public EcpBaseResponseVO qqlogin(HttpServletRequest request,@RequestParam("ClientId") String client_id,@RequestParam("ContentType") String ContentType,@RequestParam("Referer") String Referer){
    	EcpBaseResponseVO vo = new EcpBaseResponseVO();
    	Map<Object, Object> map = new HashMap<Object, Object>();
   		map.put("client_id", SsoUtil.ClientId);
   		map.put("ContentType", ContentType);
   		map.put("referer",Referer);
   		LogUtil.info(MODULE, "Referer:"+Referer);
   		JSONObject jsonObject = SsoUtil.postAuthSso("/getSocialURL", map);
   		String flag = jsonObject.getString("Success");
 		LogUtil.info(MODULE, jsonObject.toString());
 		if(flag.equals("true")){
 			JSONObject Report	= jsonObject.getJSONObject("Report");
 			if((Report != null) && !(Report.isNullObject())){
 				JSONObject QQ = Report.getJSONObject("QQ");
 				if((QQ != null) && !(QQ.isNullObject())){
 					String LoginURL = QQ.getString("LoginURL");
 					vo.setResultFlag("ok");
 		 			vo.setResultMsg(LoginURL);
 				}
 			}
 		}else{
 			vo.setResultFlag("fail");
 			vo.setResultMsg(jsonObject.getString("Message"));
 		}
 	    return vo;
    	
    }
   
    /**
	 * 从回调URL获取Code,使用Code获取Token,使用Token获取用户信息
	 * @param request
	 * @param 
	 * @param 
	 * @return
	 */
   
    @RequestMapping(value = "/thirdpartycallback")
	public String fetchCode(Model model,HttpServletRequest request) throws Exception {
    	
    	try {
    		String code = request.getParameter("code");
    		String Referer = request.getParameter("referer");
    		LogUtil.info(MODULE, "回调Referer:"+Referer);
        	Map<Object, Object> map = new HashMap<Object, Object>();
        	map.put("client_id",SsoUtil.ClientId);
        	map.put("client_secret",SsoUtil.ClientSecret);
        	map.put("code",code);
        	map.put("grant_type","authorization_code");
        	map.put("redirect_uri",SsoUtil.RedirectUri);
        	JSONObject jsonObject = SsoUtil.postAuthSso("/oauth/token", map);
        	LogUtil.info(MODULE, jsonObject.toString());
        	model.addAttribute("data", "");
        	if((jsonObject != null) && !(jsonObject.isNullObject())){
        		String accessToken = jsonObject.getString("access_token");
        		if(null != accessToken){
        			JSONObject resp = getUserInfoByToken(accessToken);
        			LogUtil.info(MODULE, resp.toString());
            		if((resp != null) && !(resp.isNullObject())){
            			JSONObject data	= resp.getJSONObject("data");
            			if((data != null) && !(data.isNullObject())){
            				String j_openid = (String) CacheUtil.getItem(session.getId() + "_openid");
            				LogUtil.info(MODULE, "j_openid"+j_openid);
            				// 此处插入用户数据至本系统
            				CustInfoReqDTO custInfoReq = new CustInfoReqDTO();
            				custInfoReq.setStaffCode(data.getString("UserName"));
            				CustInfoResDTO custInfoRes = custManageRSV.findCustInfo(custInfoReq);
            				SsoUserInfoMsgResDTO message = new SsoUserInfoMsgResDTO();
            				if(null==custInfoRes){
            			    	SsoUserInfoReqDTO dto = new SsoUserInfoReqDTO();
            			    	dto.setUserName(data.getString("UserName"));
            		 		    message = ssoUserImportRSV.changeStaffInfo(dto);
            			    }
            				if (j_openid == null) {
            					//throw new BusinessException("微信openid缺失。");
            					return "/common/login/error";
        		 			} else {
        		 				CustInfoReqDTO custReq = new CustInfoReqDTO();
        		 			    custReq.setStaffCode(data.getString("UserName"));
        		 			    CustInfoResDTO custRes = custManageRSV.findCustInfo(custReq);
        		 				// 如果登录成功
        		 				if (custRes != null && "1".equals(custRes.getStatus())) {
        		 					AuthStaffResDTO authRes = authStaffRSV.findAuthStaffById(custRes.getId());

        		 					// 查询是否已绑定商城的会员
        		 					CustWechatRelReqDTO wechatReq = new CustWechatRelReqDTO();
        		 					wechatReq.setStaffCode(custRes.getStaffCode());
        		 					wechatReq.setStaffId(custRes.getId());
        		 					wechatReq.setPageNo(1);
        		 					wechatReq.setPageSize(1);
        		 					PageResponseDTO<CustWechatRelRespDTO> page = custWechatRelRSV.findCustWechatRel(wechatReq);
        		 					// 如果没有绑定，则进行绑定
        		 					if (page == null || CollectionUtils.isEmpty(page.getResult())) {

        		 						CustWechatRelRespDTO wechatRespDTO = custWechatRelRSV.findCustWechatRelByWechatId(j_openid);
        		 						if (wechatRespDTO != null && !wechatRespDTO.getStaffCode().equals(custRes.getStaffCode())) {
        		 							//throw new BusinessException("该用户" + custRes.getStaffCode() + "已绑定其他微信账号，无法登录。");
        		 							return "/common/login/error";
        		 						}
        		 						wechatReq.setWechatId(String.valueOf(j_openid));
        		 						wechatReq.setCreateStaff(custRes.getId());
        		 						custWechatRelRSV.saveCustWechatRel(wechatReq);
        		 					} else {
        		 						CustWechatRelRespDTO resp1 = page.getResult().get(0);
        		 						// 判断绑定的微信账号是否跟当前的一致，如果不一致，给出相应提示，不让登录
        		 						if (!resp1.getWechatId().equals(String.valueOf(j_openid))) {
        		 							//throw new BusinessException("该用户" + custRes.getStaffCode() + "已绑定其他微信账号，无法登录。");
        		 							return "/common/login/error";
        		 						}
        		 					}
        		 					model.addAttribute("data", data);
	 								return "redirect:/j_spring_security_check?j_username="+data.getString("UserName")+"&j_from=SSO"+"&Referer="+Referer;
        		 				}
        		 			}
            			}
            			
            		}
        		}
        		
        		
        	}
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage());
		}
    	
		return "";
	}
    
    //使用Token获取用户信息
    public JSONObject getUserInfoByToken(String token){
    	Map<Object, Object> map = new HashMap<Object, Object>();
    	map.put("client_id",SsoUtil.ClientId);
    	map.put("token",token);
    	map.put("method","user_info");
    	JSONObject resp = SsoUtil.postAuthSso("/oauth/api", map);
    	return resp;
    			
    }
   
    
}
