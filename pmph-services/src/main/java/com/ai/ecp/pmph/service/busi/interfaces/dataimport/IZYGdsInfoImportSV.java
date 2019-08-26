package com.ai.ecp.pmph.service.busi.interfaces.dataimport;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;

import java.util.Map;

/**
 * Project Name:ecp-services-goods <br>
 * Description: 泽云图书信息导入<br>
 * Date:2015年10月24日上午10:31:17  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IZYGdsInfoImportSV {
    
    /**
     * 导入单个商品信息,新增/修改/删除
     * @param map
     * @throws BusinessException
     */
    @SuppressWarnings("rawtypes")
    GdsInfoRespDTO saveGdsInfo(Map map) throws BusinessException;
    
    /**
     * 下架
     * @param map
     * @throws BusinessException
     */
    @SuppressWarnings("rawtypes")
    GdsInfoRespDTO offShelves(Map map) throws BusinessException;

}

