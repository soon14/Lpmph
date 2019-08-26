package com.ai.ecp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.staff.dubbo.dto.AuthManageReqDTO;
import com.ai.ecp.staff.dubbo.dto.AuthMenuResDTO;
import com.ai.ecp.staff.dubbo.dto.AuthPrivilegeResDTO;
import com.ai.ecp.staff.dubbo.dto.CompanyInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.util.StaffUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.mongodb.DBObject;


public class ConstantTool{
    private static final String MODULE = ConstantTool.class.getName();
   
   
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
    
    
    public List<AuthMenuResDTO> getMenuByStaffCache(){
        
        List<AuthMenuResDTO> list = new ArrayList<AuthMenuResDTO>();
        try {
            List<Long> privList = WebContextUtil.getCurrentUser().getPrivList();
            AuthManageReqDTO reqDto = new AuthManageReqDTO();
            reqDto.setPrivList(privList);
            list = StaffUtil.getMenuByStaff(reqDto);
        } catch (Exception e) {
           LogUtil.error(MODULE, "获取权限对应的菜单失败", e);
        }
        return list;
    }
    
    
    /** 
     * getImageUrl:(根据上传到mongoDB的图片ID 从mongoDB上获取图片路径). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param vfsId
     * @param param
     * @return 
     * @since JDK 1.6 
     */ 
    public static String getImageUrl(String vfsId,String param){
        StringBuilder sb=new StringBuilder();
        sb.append(vfsId);
        if(!StringUtil.isBlank(param)){
            sb.append("_");
            sb.append(param);
        }
        return ImageUtil.getImageUrl(sb.toString());
    }
    
    /** 
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
    public static byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count = -1;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return outStream.toByteArray();
    }
    
    /** 
     * getExtensionName:(获取文件拓展名). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param fileName
     * @return 
     * @since JDK 1.6 
     */ 
    public static String getExtensionName(String fileName) {
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
     * getCustInfo:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @param staffId
     * @return 
     * @since JDK 1.7
     */
    public AuthPrivilegeResDTO getCustInfo(){
        com.ai.ecp.server.front.security.AuthPrivilegeResDTO auth =  WebContextUtil.getCurrentUser();
        AuthPrivilegeResDTO res = new AuthPrivilegeResDTO();
        if(null==auth||0==auth.getStaffId()){
            return res;
        } else {
        	ObjectCopyUtil.copyObjValue(auth, res, null, false);
        }
        return res;
    }
    /**
     * 
     * siteCache:(获取站点缓存). <br/>
     * 
     * @return
     * @since JDK 1.7
     */
    public CmsSiteRespDTO siteCache(String siteId) {
        CmsSiteRespDTO site = null;
        Map<Long, CmsSiteRespDTO> map = new HashMap<Long, CmsSiteRespDTO>();
        try {
            map = CmsCacheUtil.queryCmsSiteCache();
            if (!CollectionUtils.isEmpty(map)) {
                if (StringUtil.isNotBlank(siteId)) {
                    site = map.get(Long.valueOf(siteId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return site;
    }
	
	/**
	 * 将DBObject转换成Bean对象
	 * 
	 */
	public static <T> T dbObjectToBean(DBObject dbObject, T bean) {

		try {
			if (bean == null) {
				return null;
			}
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				String varName = field.getName();
				Object object = dbObject.get(varName);
				if (object != null) {
					BeanUtils.setProperty(bean, varName, object);
				}

			}
			return bean;
		} catch (Exception e) {
		LogUtil.error(MODULE, e.getMessage(), e);
		}
		return null;
	}
    
}
