
package com.ai.ecp.pmph.busi.staff.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.staff.dubbo.dto.AuthStaffAdminReqDTO;
import com.ai.ecp.staff.dubbo.dto.AuthStaffAdminResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IAuthAdminRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

import small.danfer.sso.http.HttpSingleSignOnService;

/**
 * 
 * Project Name:ecp-web-manage <br>
 * Description: 第三方系统登录对接<br>
 * Date:2015年9月15日下午6:19:26  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value="/third/sys")
public class SsoController extends EcpBaseController {
    
    @Resource
    private IAuthAdminRSV authAdminRSV; //管理员
    
    /**
     * 
     * login:(跳转到第三方身份认证系统的登录页面). <br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/login")
    public String login(Model model){
        HttpSingleSignOnService  service = new HttpSingleSignOnService();
        String url = service.getSingleSignOnURL();
        return "redirect:" + url;
    }
    
    /**
     * 
     * ssoToMainPage:(sso登录后，返回的管理平台页面). <br/> 
     * 校验是否已经登录，如果没登录，则跳转到管理平台的登录页
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/back")
    public String ssoToMainPage(Model model,HttpServletRequest request) {
        
        HttpSingleSignOnService  service = new HttpSingleSignOnService();
        String ssoLoginUrl = service.getSingleSignOnURL();
        Principal principal;
        try {
            principal = service.singleSignOn(request);
            String userName = principal.getName();
            /*1、判断用户是否登录*/
            /*1-1、已登录*/
            if (StringUtil.isNotBlank(userName)) {
                AuthStaffAdminReqDTO req = new AuthStaffAdminReqDTO();
                req.setStaffCode(userName);
                
                /*2、登录成功后，判断登录的用户在管理平台中是否存在*/
                AuthStaffAdminResDTO staffAdmin = authAdminRSV.findAuthStaffAdminByCode(req);
                
                /*3、不存在，则在我们平台里面新建一个用户*/
                if (staffAdmin.getId() == null || staffAdmin.getId() == 0L) {
                    AuthStaffAdminReqDTO saveStaff = new AuthStaffAdminReqDTO();
                    saveStaff.setStaffCode(userName);
                    saveStaff.setStaffName(userName);//目前没有传姓名给我们，先使用登录名来入值
                    saveStaff.setStaffPasswd("123456");//默认密码
                    Long staffId = authAdminRSV.saveAuthStaffAdmin(saveStaff);
                    saveStaff.setId(staffId);
                }
                /*4、存在，则调用登录成功后的处理方法，参数：j_from=SSO，可绕过密码及验证码*/
                String url = "redirect:/j_spring_security_check?j_username=" + staffAdmin.getStaffCode()
                        + "&j_from=SSO";
                LogUtil.debug("SsoController", "第三方身份认证成功");
                return url;   
                /*1-2、未登录，跳转到平台的登录页面*/
            } else {
            	LogUtil.debug("SsoController", "第三方身份认证未登录");
                return "redirect:" + ssoLoginUrl;
            }
        } catch (Exception e) {
            LogUtil.debug("SsoController", "第三方身份认证失败：" + e.getMessage());
            return "redirect:" + ssoLoginUrl;
        }
    }
}

