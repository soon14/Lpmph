package com.ailk.ecp.pmph.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Name:ecp-aip-web <br>
 * Description: <br>
 * Date:2015年11月2日下午2:35:38  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class RespUtil {
    
    /**
     * 业务返回的根元素
     */
    private final static String RESP="RESP";
    
    /**
     * 应答编码：
        1   执行成功
        2   执行失败
     */
    public final static String CODE="CODE";
    
    /**
     * 应答码中文描述
     */
    public final static String MSG="MSG";
    
    /**
     * 统一给响应报文添加RESP根元素包装，避免KEY冲突和易于扩展
     * @param map
     * @return
     */
    public static Map<String,Object> renderRootResp(Map<String,Object> map){
        
        Map<String,Object> rootMap=new HashMap<String,Object>();
        rootMap.put(RESP, map);
     
        return rootMap;
    }

}

