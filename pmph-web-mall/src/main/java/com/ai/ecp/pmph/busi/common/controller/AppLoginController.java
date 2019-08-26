package com.ai.ecp.pmph.busi.common.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.util.AppUserCacheUtils;
import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.BaseStaff;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoMsgResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.ISsoUserImportRSV;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.CacheUtil;
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
@RequestMapping(value = "/rwlogin")
public class AppLoginController extends EcpBaseController {

    private static String MODULE = AppLoginController.class.getName();

    @Autowired
    protected HttpSession session;

    @Resource
    private ISsoUserImportRSV ssoUserImportRSV;
    
    @Resource
    private ICustManageRSV custManageRSV;

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
        String realName = user.getValue("RealName");
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
        if(!CacheUtil.exists("SSO_USER_"+user.getUserName())){//判断是否已经在执行该操作
        message = ssoUserImportRSV.changeStaffInfo(dto);
        }else{
            
        }
        LogUtil.info(MODULE, "=====================" + message.getMessage()
                + "=====================");
        /*1、获取入参*/
        String loginCode = user.getUserName();//登录编码（可以是登录名、手机号、邮箱）
        CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
        custInfoReqDTO.setStaffCode(loginCode);
        CustInfoResDTO custInfoResDTO = custManageRSV.findCustInfo(custInfoReqDTO);
        
        /*4、登录成功后，往缓存里放入一个登录授权key*/
        String url="";
        if (custInfoResDTO.getId() != null && custInfoResDTO.getId() != 0L) {
            BaseStaff basestaff = new BaseStaff();
            basestaff.setId(custInfoResDTO.getId());
            basestaff.setStaffCode(custInfoResDTO.getStaffCode());
            basestaff.setStaffLevelCode(custInfoResDTO.getCustLevelCode());
            basestaff.setStaffClass(custInfoResDTO.getCustType());
            String tocken = AppUserCacheUtils.saveUser2Cache(basestaff);
            url = "redirect:/login?tocken="+tocken;
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
        BaseInfo info = new BaseInfo();
        CmsSiteRespDTO dto = CmsCacheUtil.getCmsSiteCache(info.getCurrentSiteId());
        LogUtil.debug(MODULE, "==============Referer参数:"+Referer+"==================");
        String url = Application.getValue(WebConstants.URL_LOGIN_PAGE);
        if(StringUtil.isNotBlank(Referer)&&Referer.indexOf("mall.pmph.com")>0){
            return "redirect:" +Referer;
        }else if(StringUtil.isNotBlank(Referer)&&Referer.indexOf("mall.pmph.com")<1){
            if(url.indexOf("?")>0){
                return "redirect:"+ url+"&Referer="+dto.getSiteUrl()+Referer;
            }else{
                return "redirect:"+ url+"?Referer="+dto.getSiteUrl()+Referer;
            }
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
    
  /*  @RequestMapping(value = "/point")
    public String point(HttpServletRequest request) throws Exception {
        String url = Application.getValue(ParamsTool.Page.POINT_URL);
        return "redirect:" + url;
    }*/

}
