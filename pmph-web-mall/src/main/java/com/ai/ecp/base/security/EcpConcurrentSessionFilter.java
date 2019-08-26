/**
 * SrpConcurrentSessionFilter.java    V1.0   2013-8-7 上午10:46:08
 *
 * Copyright 2013 FUJIAN FUJITSU COMMUNICATION SOFTWARE CO., LTD. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.ai.ecp.base.security;

import com.ai.ecp.base.mvc.MessageModel;
import com.ai.ecp.base.mvc.ResponseModel;
import com.ai.ecp.base.mvc.ResponseModel.ResultTypeEnum;
import com.ai.ecp.base.util.ParamConstant;
import com.ai.ecp.base.util.WebContextUtil;
import com.ai.paas.utils.ResourceMsgUtil;
import com.ailk.butterfly.core.config.Application;
import com.ailk.butterfly.core.web.WebConstants;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EcpConcurrentSessionFilter extends GenericFilterBean {
    
    private SessionRegistry sessionRegistry;
    private String expiredUrl;
    private LogoutHandler[] handlers = new LogoutHandler[] {new SecurityContextLogoutHandler()};
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private boolean requiredSession=false;
    private List<String> excludeUrls;
    private List<String> includeUrls;
    private String refererParameter;
    //~ Methods ========================================================================================================



    public EcpConcurrentSessionFilter(SessionRegistry sessionRegistry) {
        this(sessionRegistry, null);
    }

    public EcpConcurrentSessionFilter(SessionRegistry sessionRegistry, String expiredUrl) {
        this.sessionRegistry = sessionRegistry;
        this.expiredUrl = expiredUrl;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(sessionRegistry, "SessionRegistry required");
        Assert.isTrue(expiredUrl == null || UrlUtils.isValidRedirectUrl(expiredUrl),
                expiredUrl + " isn't a valid redirect URL");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
            String url=request.getServletPath();
            if(!requiredSession && !isFilterUrl(url,this.includeUrls)){
                if(null==info){
                    sessionRegistry.registerNewSession(session.getId(), "anonymous");
                    info = sessionRegistry.getSessionInformation(session.getId());
                }
            }else if(null!=info && "anonymous".equalsIgnoreCase(String.valueOf(info.getPrincipal()))){
                Boolean flag=(Boolean)session.getAttribute(ParamConstant.LOGIN_FLAG_KEY);
                if(null== flag || !flag.booleanValue()){
                    info=null;
                }
            }else if(null==info){
                SecurityContext scx=WebContextUtil.getSecurityContext(request);
                if(null!=scx){
                    sessionRegistry.registerNewSession(session.getId(), scx.getAuthentication().getPrincipal());
                    info = sessionRegistry.getSessionInformation(session.getId());
                }
            }
            
            if (info != null) {
                if (info.isExpired()) {
                    // Expired - abort processing
                    doLogout(request, response);

                    String targetUrl = determineExpiredUrl(request, info);

                    if (targetUrl != null) {
                        redirectStrategy.sendRedirect(request, response, targetUrl);

                        return;
                    } else {
                        
                        responseFlush(response,request);
                        
//                        response.setHeader("Content-Type", "application/json;charset=UTF-8");
//                        ResponseModel respModel=new ResponseModel();
//                        respModel.setAjaxResult(ResultTypeEnum.TIMEOUT);
//                        respModel.setErrorMessage(new ArrayList<MessageModel>());
//                        respModel.getErrorMessage().add(new MessageModel("webcore.000003",ResourceMsgUtil.getMessage("webcore.000002", new Object[]{})));
//                        JSONObject jsonObject=JSONObject.fromObject(respModel);
//                        String jsonString=jsonObject.toString();
//                        response.getWriter().print(jsonString);
////                        response.getWriter().print("This session has been expired (possibly due to multiple concurrent " +
////                                "logins being attempted as the same user).");
//                        response.flushBuffer();
                    }

                    return;
                } else {
                    // Non-expired - update last request date/time
                    sessionRegistry.refreshLastRequest(info.getSessionId());
                }
            }else{
                boolean flag=checkLoginIn(request,response);
                if(flag){
                    return;
                }
            }
        }else{
            
            boolean flag=checkLoginIn(request,response);
            if(flag){
                return;
            }
        }

        chain.doFilter(request, response);
    }
    
    private void responseFlush(HttpServletResponse response,HttpServletRequest request) throws IOException{
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        ResponseModel respModel=new ResponseModel();
        respModel.setAjaxResult(ResultTypeEnum.TIMEOUT);
        respModel.setErrorMessage(new ArrayList<MessageModel>());
        Map<String,String> map=new HashMap<String,String>();
        String url=Application.getValue(WebConstants.URL_LOGIN_PAGE);
        String referer=request.getHeader("Referer");
        //当参数中有多个&&时，需要进行编码，避免丢失参数
        referer = URLEncoder.encode(referer,"UTF-8");
        String protocol=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

        if(StringUtils.isNotBlank(url) && StringUtils.isNotBlank(referer)){
            //不做判断，造成北京测试环境未登录点击购物车登录后无法返回原页面
            //原因是referer和protocol的取值一直都是一致的除了端口号不一样
//            boolean flag=referer.startsWith(protocol);
//            if(flag){
//                referer=referer.replace(protocol, "");
//            }
            referer=response.encodeURL(referer);
            if(url.indexOf("?")>0){
                url=url+"&Referer="+referer;
            }else{
                url=url+"?Referer="+referer;
            }
            if(!url.startsWith(request.getScheme()+"://")){

                if(url.startsWith("/")){
                    url=protocol+url;
                }else{
                    url=protocol+"/"+url;
                }
            }
        }
        map.put("url", url);
        respModel.setValues(map);
        respModel.getErrorMessage().add(new MessageModel("webcore.000003",ResourceMsgUtil.getMessage("webcore.000002", new Object[]{})));
        JSONObject jsonObject=JSONObject.fromObject(respModel);
        String jsonString=jsonObject.toString();
        response.getWriter().print(jsonString);
//        response.getWriter().print("This session has been expired (possibly due to multiple concurrent " +
//                "logins being attempted as the same user).");
        response.flushBuffer();
    }
    
    private boolean checkLoginIn(ServletRequest req, ServletResponse res) throws IOException{
        HttpServletRequest request=(HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url=request.getServletPath();
        String suffix=url.substring(url.lastIndexOf(".")+1);
        if("js".equalsIgnoreCase(suffix) || "css".equalsIgnoreCase(suffix)){
            return false;
        }
        boolean flag=isFilterUrl(url,this.excludeUrls);
        if(flag){
            return false;
        }
        if(requiredSession | isFilterUrl(url,this.includeUrls)){
            
            if(WebContextUtil.isAjaxRequest(request)){
//                response.addHeader(ParamConstant.SESSION_TIMEOUT_HEAD_KEY, ParamConstant.SESSION_TIMEOUT_HEAD_VALUE);
                responseFlush(response,request);
            }else{
                String targetUrl = determineExpiredUrl(request, null);
                if(StringUtils.isNotBlank(refererParameter)){
//                    request.setAttribute(refererParameter, url);
                    if(targetUrl.indexOf("?")>-1){
                        targetUrl=targetUrl+"&"+refererParameter+"="+url;
                    }else{
                        targetUrl=targetUrl+"?"+refererParameter+"="+url;
                    }
                }
                
                redirectStrategy.sendRedirect(request, response, targetUrl);
            }
        }else{
            return false;
        }
        return true;
    }
    
    private boolean isFilterUrl(String url,List<String> curls){
        boolean flag=false;
        if(!CollectionUtils.isEmpty(curls)){
            flag=curls.contains(url);
        }
        if(!flag && CollectionUtils.isNotEmpty(curls)){
            for(String curl : curls){
                flag=url.startsWith(curl);
                if(flag){
                    break;
                }
            }
        }
        
        return flag;
    }
    
    protected String determineExpiredUrl(HttpServletRequest request, SessionInformation info) {
        return expiredUrl;
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        for (LogoutHandler handler : handlers) {
            handler.logout(request, response, auth);
        }
    }

    public void setLogoutHandlers(LogoutHandler[] handlers) {
        Assert.notNull(handlers);
        this.handlers = handlers;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    
    public void setRequiredSession(boolean requiredSession) {
    
        this.requiredSession = requiredSession;
    }

    
    public void setExcludeUrls(List<String> excludeUrls) {
    
        this.excludeUrls = excludeUrls;
    }

    public void setIncludeUrls(List<String> includeUrls) {
        this.includeUrls = includeUrls;
    }

    public void setRefererParameter(String refererParameter) {
        this.refererParameter = refererParameter;
    }

}
