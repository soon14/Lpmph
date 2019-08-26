package com.ai.ecp.pmph.dubbo.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.kettle.IReceiveData;

import java.util.Map;

/**
 * Project Name:ecp-services-goods <br>
 * Description: 泽云图书信息导入<br>
 * Date:2015年10月24日上午10:32:46  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IZYGdsInfoImportRSV extends IReceiveData{
    
    /**
     * 下架，供API调用
     * @param map
     * @throws BusinessException
     */
    @SuppressWarnings("rawtypes")
    void offShelves(Map map) throws BusinessException;

}

