package com.ai.ecp.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.PageResponseDTO;
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
import com.ai.ecp.system.util.ParamsTool;
import com.ai.ecp.util.SsoUtil;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.core.config.Application;
import com.ailk.butterfly.core.web.WebConstants;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.zving.zas.client.PGTUtil;
import com.zving.zas.client.ZASConstant;
import com.zving.zas.client.ZASFilter;
import com.zving.zas.client.ZASUserData;

import net.sf.json.JSONObject;

/**
 * 
 * Project Name:ecp-web-mall <br>
 * Description: <br>
 * Date:2015年10月9日上午9:27:02 <br>
 * 
 * @version
 * @since JDK 1.6
 * 
 *        登陆控制
 */

@Controller
@RequestMapping(value = "/rwlogin")
public class AppLoginController extends EcpBaseController {

	private static String MODULE = AppLoginController.class.getName();

	@Autowired
	protected HttpSession session;

	@Resource
	private ISsoUserImportRSV ssoUserImportRSV;

	@Resource
	private ICustManageRSV custManageRSV;

	@Resource
	private IAuthLoginRSV authLoginRSV;

	@Resource
	private IAuthStaffRSV authStaffRSV;

	@Resource
	private ICustWechatRelRSV custWechatRelRSV;

	@RequestMapping()
	public String init(Model model, HttpServletRequest request) {
		model.addAttribute("Referer", request.getParameter("Referer"));
		return "/common/login/main-login";
	}

	@RequestMapping(value = "/ssosync")
	@ResponseBody
	public EcpBaseResponseVO ssoSync(HttpServletRequest request, @RequestParam("userName") String userName,@RequestParam("passWord") String passWord)
			throws Exception {
		String j_openid = (String) CacheUtil.getItem(ParamsTool.getCookSessionId(request) + "_openid");
		LogUtil.info(MODULE, "=====================sso同步开始=====================");
		SsoUserInfoMsgResDTO message = new SsoUserInfoMsgResDTO();
		SsoUserInfoReqDTO dto = new SsoUserInfoReqDTO();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);

		map.put("method", "ZAS.UserInfo");
		JSONObject value = new JSONObject();
		value.put("LoginID", userName);
		value.put("Password", passWord);
		map.put("params", value.toString());
		JSONObject jsonObject = SsoUtil.postAuthSso("/api/json", map);
		JSONObject user = new JSONObject();
		// 此处插入用户数据至本系统

		dto.setUserName(user.getString("UserName"));
		if (StringUtil.isNotBlank(user.getString("UserName"))) {
			String realName = user.getString("RealName");
			dto.setRealName(realName);
			dto.setEmail(user.getString("Email"));
			dto.setMobile(user.getString("Mobile"));
		}
		LogUtil.info(MODULE, "===================本次入参为:" + dto + "==========================");
		if (!CacheUtil.exists("SSO_USER_" + user.getString("RealName"))) {// 判断是否已经在执行该操作
			message = ssoUserImportRSV.changeStaffInfo(dto);
		} else {

		}

		LogUtil.info(MODULE, "=====================" + message.getMessage() + "=====================");
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		vo.setResultFlag("fail");

		// String j_openid = "sadahusifhdsygfuysfs";
		if (j_openid == null) {
			vo.setResultMsg("微信openid缺失。");
		} else {
			CustInfoReqDTO custReq = new CustInfoReqDTO();
			custReq.setStaffCode(user.getString("RealName"));
			CustInfoResDTO custRes = custManageRSV.findCustInfo(custReq);
			// 如果登录成功
			if (custRes != null && "1".equals(custRes.getStatus())) {
				AuthStaffResDTO authRes = authStaffRSV.findAuthStaffById(custRes.getId());

				// 查询是否已绑定商城的会员
				CustWechatRelReqDTO wechatReq = new CustWechatRelReqDTO();
				//wechatReq.setStaffCode(custRes.getStaffCode());
				wechatReq.setStaffId(custRes.getId());
				wechatReq.setPageNo(1);
				wechatReq.setPageSize(1);
				PageResponseDTO<CustWechatRelRespDTO> page = custWechatRelRSV.findCustWechatRel(wechatReq);
				// 如果没有绑定，则进行绑定
				if (page == null || CollectionUtils.isEmpty(page.getResult())) {

					CustWechatRelRespDTO wechatRespDTO = custWechatRelRSV.findCustWechatRelByWechatId(j_openid);
					if (wechatRespDTO != null && !wechatRespDTO.getStaffCode().equals(custRes.getStaffCode())) {
						vo.setResultMsg("该用户" + custRes.getStaffCode() + "已绑定其他微信账号，无法登录。");
						return vo;
					}
					wechatReq.setWechatId(String.valueOf(j_openid));
					wechatReq.setCreateStaff(custRes.getId());
					custWechatRelRSV.saveCustWechatRel(wechatReq);
				} else {
					CustWechatRelRespDTO resp = page.getResult().get(0);
					// 判断绑定的微信账号是否跟当前的一致，如果不一致，给出相应提示，不让登录
					if (!resp.getWechatId().equals(String.valueOf(j_openid))) {
						vo.setResultMsg("该用户" + custRes.getStaffCode() + "已绑定其他微信账号，无法登录。");
						return vo;
					}
				}
				vo.setResultFlag("ok");
			}
		}

		return vo;

	}

	@RequestMapping(value = "/ssologout")
	public void ssologout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ZASUserData user = (ZASUserData) session.getAttribute(ZASConstant.UserSessionAttrName);
		PGTUtil.removePGT(user.getUserName());
		WebContextUtil.logout(request, response);
	}

	@RequestMapping(value = "/page")
	public String page(HttpServletRequest request) throws Exception {
		String Referer = request.getParameter("Referer");
		BaseInfo info = new BaseInfo();
		CmsSiteRespDTO dto = CmsCacheUtil.getCmsSiteCache(info.getCurrentSiteId());
		LogUtil.debug(MODULE, "==============Referer参数:" + Referer + "==================");
		String url = Application.getValue(WebConstants.URL_LOGIN_PAGE);
		if (StringUtil.isNotBlank(Referer) && Referer.indexOf("mall.pmph.com") > 0) {
			return "redirect:" + Referer;
		} else if (StringUtil.isNotBlank(Referer) && Referer.indexOf("mall.pmph.com") < 1) {
			if (url.indexOf("?") > 0) {
				return "redirect:" + url + "&Referer=" + dto.getSiteUrl() + Referer;
			} else {
				return "redirect:" + url + "?Referer=" + dto.getSiteUrl() + Referer;
			}
		} else {
			return "redirect:" + url;
		}

	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WebContextUtil.logout(request, response);
		String url = Application.getValue(WebConstants.URL_LOGOUT_PAGE);
		return "redirect:" + url;
	}

	@RequestMapping(value = "/register")
	public String register(HttpServletRequest request) throws Exception {
		String url = Application.getValue(ParamsTool.Page.REGISTER_PAGE);
		return "redirect:" + url;
	}

	@RequestMapping(value = "/lostpassword")
	public String lostpassword(HttpServletRequest request) throws Exception {
		String url = Application.getValue(ParamsTool.Page.LOSTPASSWORD_PAGE);
		return "redirect:" + url;
	}

	@RequestMapping(value = "/updatepassword")
	public String updatepassword(HttpServletRequest request) throws Exception {
		String url = Application.getValue(ParamsTool.Page.UPDATEPASSWORD_PAGE);
		return "redirect:" + url;
	}

	/*
	 * @RequestMapping(value = "/point") public String point(HttpServletRequest
	 * request) throws Exception { String url =
	 * Application.getValue(ParamsTool.Page.POINT_URL); return "redirect:" +
	 * url; }
	 */

}
