package com.ai.ecp.pmph.dubbo.interfaces;

import java.util.List;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * 
 * Project Name:pmph-services-server <br>
 * Description: 数字图书馆电子书ID同步更新Dubbo接口。<br>
 * Date:2016年8月12日下午2:44:08  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IEBookIDSyncRSV {
    
    
    public void executeEBookIDSync(EBookIDSyncReqDTO reqDTO)
            throws BusinessException;

}

