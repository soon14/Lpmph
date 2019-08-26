package com.ai.ecp.busi.common.controller;

import java.net.URLEncoder;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoMsgResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ISsoUserImportRSV;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.core.config.Application;
import com.ailk.butterfly.core.web.WebConstants;
import com.zving.zas.client.PGTUtil;
import com.zving.zas.client.ZASConstant;
import com.zving.zas.client.ZASFilter;
import com.zving.zas.client.ZASUserData;

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
@RequestMapping(value = "/login")
public class LoginController extends EcpBaseController {

    private static String MODULE = LoginController.class.getName();

    @Autowired
    protected HttpSession session;

    @Resource
    private ISsoUserImportRSV ssoUserImportRSV;

    @RequestMapping()
    public String init(Model model,HttpServletRequest request) {
        model.addAttribute("Referer", request.getParameter("Referer"));
        return "/common/login/main-login";
    }

    @RequestMapping(value = "/ssosync")
    public String ssoSync(HttpServletRequest request) throws Exception {
        LogUtil.info(MODULE, "=====================sso同步开始=====================");
        ZASUserData user = ZASFilter.getUserData(request);
        SsoUserInfoMsgResDTO message = new SsoUserInfoMsgResDTO();
        SsoUserInfoReqDTO dto = new SsoUserInfoReqDTO();

        // 此处插入用户数据至本系统
        dto.setUserName(user.getUserName());
        HashMap map =  user.getAllValue();
        if(null!=map&&!map.isEmpty()){
        	String realName = "";
        	if (StringUtil.isNotBlank(user.getValue("RealName"))) {
        		realName = new String(user.getValue("RealName").getBytes("ISO-8859-1"), "utf-8");
			}
	        dto.setRealName(realName);
	        dto.setEmail(user.getValue("Email"));
	        dto.setMobile(user.getValue("Mobile"));
	        if (StringUtil.isNotBlank(user.getValue("OrgID"))) {
	            dto.setOrgID(user.getValue("OrgID"));
	        }
	        if (StringUtil.isNotBlank(user.getValue("OrgName"))) {
	            String orgName = new String(user.getValue("OrgName").getBytes("ISO-8859-1"), "utf-8");
	            dto.setOrgName(orgName);
	        }
	        if (StringUtil.isNotBlank(user.getValue("OrgUserType"))) {
	            dto.setOrgUserType(user.getValue("OrgUserType"));
	        }
        }
        LogUtil.info(MODULE, "===================本次入参为:" + dto + "==========================");
        message = ssoUserImportRSV.saveStaffInfo(dto);

        LogUtil.info(MODULE, "=====================" + message.getMessage()
                + "=====================");
        
        String referer = request.getParameter("Referer");
        System.out.println("===================Referer参数："+referer+"====================");
        /*if (Referer != null && !Referer.equals("")) {
            return "redirect:" + Referer;
        } else {
            return "redirect:/j_spring_security_check?j_username=" + message.getStaffCode()
                    + "&j_password=" + "123456";
        }*/
        String url = "redirect:/j_spring_security_check?j_username=" + message.getStaffCode()
                + "&j_password=" + "0e848f708b1550083fe3074b358302d8";
        ///这里要加上非空校验的；
        if(StringUtils.isBlank(referer) || referer.indexOf("/login/ssosync") > 0){
            //为空，则不需要后面的参数；
        } else {
            url = url + "&Referer=" + URLEncoder.encode (referer, "UTF-8" );
        }
        
        return url;

    }

    @RequestMapping(value = "/ssologout")
    public void ssologout(HttpServletRequest request,HttpServletResponse response) throws Exception {
        ZASUserData user = (ZASUserData) session.getAttribute(ZASConstant.UserSessionAttrName);
        PGTUtil.removePGT(user.getUserName());
        WebContextUtil.logout(request, response);
    }

    @RequestMapping(value = "/page")
    public String page(HttpServletRequest request) throws Exception {
        String Referer =   request.getParameter("Referer");
        LogUtil.debug(MODULE, "==============Referer参数:"+Referer+"==================");
        String url = Application.getValue(WebConstants.URL_LOGIN_PAGE);
        if(StringUtil.isNotBlank(Referer)){
            return "redirect:" + url+"?Referer="+Referer;
        }else{
            return "redirect:" + url;
        }
      
    }
    
    @RequestMapping(value = "/gwpage")
    public String gwpage(HttpServletRequest request) throws Exception {
        String Referer =   request.getParameter("Referer");
        LogUtil.debug(MODULE, "==============Referer参数:"+Referer+"==================");
        String url = Application.getValue(ParamsTool.Page.GW_URL);
        if(StringUtil.isNotBlank(Referer)){
            return "redirect:" + url+"?Referer="+Referer;
        }else{
            return "redirect:" + url;
        }
      
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
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

}
