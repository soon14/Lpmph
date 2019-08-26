/** 
 * File Name:AipUtil.java 
 * Date:2015-10-29下午4:23:40 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.utils;

import com.ai.ecp.pmph.aip.dubbo.constants.PmphConstants;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: <br>
 * Date:2015-10-29下午4:23:40  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AipUtil {
	
	private static final String MODULE = AipUtil.class.getName();
	
	 /**
     * 
     * 必传参数检测，仅对普通参数进行判空处理，抛出异常信息为:必传参数{0}缺失!<br/>
     * 其中两个参数的params与msgs的数组长度要一致。
     * 
     * @param params
     * @param msgs
     * @since JDK 1.6
     */
    public static void paramCheck(Object[] params, String[] msgs) {
        if (null != params && null != msgs && params.length == msgs.length) {
               StringBuffer errorMsg = new StringBuffer();
               for(int i = 0; i < params.length; ++ i){
                   Object obj = params[i];
                   if(obj instanceof String){
                       if(StringUtil.isBlank((String)obj)){
                           errorMsg.append(msgs[i]);
                           errorMsg.append(",");
                       }
                   }else if(obj instanceof Object[]){
                	   if(obj == null || ((Object[])obj).length == 0){
                		   errorMsg.append(msgs[i]);
                           errorMsg.append(",");
                	   }
                   }else if(obj instanceof Collection<?>){
                	   if(obj == null || CollectionUtils.isEmpty((Collection<?>)obj)){
                		   errorMsg.append(msgs[i]);
                           errorMsg.append(",");
                	   }
                   }else{
                       if(null == obj){
                           errorMsg.append(msgs[i]);
                           errorMsg.append(",");
                       }
                   }
               }
               if(0 < errorMsg.length()){
                   errorMsg.deleteCharAt(errorMsg.length() - 1);
                   throw new BusinessException(PmphConstants.ErrorCode.AIP_A20001,new String[]{errorMsg.toString()});
               }
        } else {
            LogUtil.error(MODULE, "参数检测方法执行异常！请保证params与msgs不为空，并且两个参数数组长度一致");
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20002,
                    new String[] { Thread.currentThread().getStackTrace()[1].getMethodName() });
        }
    }

}

