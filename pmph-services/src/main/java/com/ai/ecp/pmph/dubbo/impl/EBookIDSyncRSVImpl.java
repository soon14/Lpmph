/** 
 * File Name:EBookIDSyncRSVImpl.java 
 * Date:2016年8月16日下午7:12:38 
 * 
 */ 
package com.ai.ecp.pmph.dubbo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.impl.AbstractRSVImpl;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;
import com.ai.ecp.pmph.dubbo.interfaces.IEBookIDSyncRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IEBookIDSyncSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * Project Name:pmph-services-server <br>
 * Description: 数字图书ID更新Dubbo服务实现类<br>
 * Date:2016年8月16日下午7:12:38  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class EBookIDSyncRSVImpl extends AbstractRSVImpl implements IEBookIDSyncRSV {
    
    @Autowired(required=false)
    private IEBookIDSyncSV eBookIDSyncSV;

    /** 
     * TODO 简单描述该方法的实现功能（可选）. 
     * @see com.ai.ecp.pmph.service.busi.interfaces.IEBookIDSyncSV#executeEBookIDSync(com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO) 
     */
    @Override
    public void executeEBookIDSync(EBookIDSyncReqDTO reqDTO)
            throws BusinessException {
        List<GdsSkuInfoRespDTO> lst = eBookIDSyncSV.executeEBookIDSync(reqDTO);
        if(CollectionUtils.isNotEmpty(lst)){
            for(GdsSkuInfoRespDTO respDTO : lst){
                GdsUtils.sendGdsIndexMsg(null, "T_GDS_INFO", MODULE, respDTO.getGdsId(), respDTO.getCatlogId());
            }
        }
    }

}

