package com.ai.ecp.system.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.server.front.security.AuthPrivilegeResDTO;
import com.ai.ecp.server.front.util.SiteLocaleUtil;
import com.ai.ecp.staff.dubbo.dto.CompanyInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.util.StaffUtil;
import com.ai.paas.utils.StringUtil;


public class ConstantTool{
    
   
    /**
     * 
     * getComapnyCache:(获取企业下拉框). <br/> 
     * 
     * @return 
     * @since JDK 1.7
     */
    public Map<Long,CompanyInfoResDTO> getComapnyCache(){
        Map<Long,CompanyInfoResDTO> map = new HashMap<Long,CompanyInfoResDTO>();
        try {
            map =  StaffUtil.getComapnyCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
       return map;
    }
    /**
     * 
     * getComapnyCache:(获取店铺下拉框). <br/> 
     * 
     * @return 
     * @since JDK 1.7
     */
    public Map<Long,ShopInfoResDTO> getShopCache(){
        Map<Long,ShopInfoResDTO> map = new HashMap<Long, ShopInfoResDTO>();
        try {
            map = StaffUtil.getShopCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
     }
    
    /**
     * 
     * getCustInfo:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @param staffId
     * @return 
     * @since JDK 1.7
     */
    public AuthPrivilegeResDTO getCustInfo(){
        com.ai.ecp.server.front.security.AuthPrivilegeResDTO auth =  WebContextUtil.getCurrentUser();
        if(null==auth||0==auth.getStaffId()){
            return auth;
        }
        
        return auth;
    }
    
    /**
     * 
     * siteCache:(获取站点缓存). <br/> 
     * 
     * @return 
     * @since JDK 1.7
     */
    public String siteCache(String siteId){
        CmsSiteRespDTO site = new CmsSiteRespDTO();
        Map<Long,CmsSiteRespDTO> map = new HashMap<Long,CmsSiteRespDTO>();
        try {
            map =  CmsCacheUtil.queryCmsSiteCache();
            if(!CollectionUtils.isEmpty(map)){
                if(StringUtil.isNotBlank(siteId)){
                    site = map.get(Long.valueOf(siteId));
                }
            }
            if(StringUtil.isBlank(site.getSiteUrl())){
                return "";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
       return site.getSiteUrl();
    }
    
    /**
     * 
     * siteCache:(获取站点缓存). <br/> 
     * 
     * @return 
     * @since JDK 1.7
     */
    public CmsSiteRespDTO siteCacheURL(String siteId){
        CmsSiteRespDTO site = null;
        Map<Long,CmsSiteRespDTO> map = new HashMap<Long,CmsSiteRespDTO>();
        try {
            map =  CmsCacheUtil.queryCmsSiteCache();
            if(!CollectionUtils.isEmpty(map)){
                if(StringUtil.isNotBlank(siteId)){
                    site = map.get(Long.valueOf(siteId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return site;
    }
    
    /** 
     * siteName:(获取当前站点名称). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param siteId  当站点ID为空时，获取当前站点名称
     * @return 
     * @since JDK 1.6 
     */ 
    public CmsSiteRespDTO siteName(String siteId) {
        if(StringUtil.isEmpty(siteId)){
            siteId = String.valueOf(SiteLocaleUtil.getSite());
        }
        return CmsCacheUtil.getCmsSiteCache(Long.valueOf(siteId));
    }
    
}
