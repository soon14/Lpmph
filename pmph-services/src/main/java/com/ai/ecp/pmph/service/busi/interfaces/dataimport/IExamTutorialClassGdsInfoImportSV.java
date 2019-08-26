package com.ai.ecp.pmph.service.busi.interfaces.dataimport;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;

import java.util.Map;

/**
 * Project Name:ecp-services-goods <br>
 * Description: 辅导班商品导入<br>
 * Date:2015年11月3日下午8:26:25  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IExamTutorialClassGdsInfoImportSV {
    
    /**
     * 导入单个商品信息,新增/修改/删除
     * @param map
     * @throws BusinessException
     */
    @SuppressWarnings("rawtypes")
    GdsInfoRespDTO saveGdsInfo(Map map) throws BusinessException;

}

