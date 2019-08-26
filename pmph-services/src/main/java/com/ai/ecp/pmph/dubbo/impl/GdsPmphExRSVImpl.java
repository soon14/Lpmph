/** 
 * File Name:GdsPmphExRSVImpl.java 
 * Date:2016年8月15日下午5:30:48 
 * 
 */ 
package com.ai.ecp.pmph.dubbo.impl;

import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.goods.dubbo.impl.AbstractRSVImpl;
import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;
import com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphExRSV;
import com.ai.ecp.pmph.dubbo.util.PmphConstants;

/**
 * Project Name:pmph-services-server <br>
 * Description: <br>
 * Date:2016年8月15日下午5:30:48  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class GdsPmphExRSVImpl extends AbstractRSVImpl implements IGdsPmphExRSV {

    /** 
     * TODO 简单描述该方法的实现功能（可选）. 
     * @see com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphExRSV#isVirtualCard(com.ai.ecp.goods.dubbo.dto.common.LongReqDTO) 
     */
    @Override
    public boolean isVirtualCard(LongReqDTO reqDTO) {
        paramNullCheck(reqDTO, "reqDTO");
        paramNullCheck(reqDTO.getId(), "reqDTO.id");
        return reqDTO.getId().equals(PmphConstants.GdsType.GDSTYPE_VIRTUAL_CARD);
    }
    


    @Override
    public boolean isVirtualProduct(LongReqDTO reqDTO) {
        paramNullCheck(reqDTO, "reqDTO");
        paramNullCheck(reqDTO.getId(), "reqDTO.id");
        return reqDTO.getId().equals(PmphConstants.GdsType.GDSTYPE_VIRTUAL_PRODUCT);
    }



    @Override
    public void executeEBookIDSync(EBookIDSyncReqDTO reqDTO) {
    }
    
    
    

}

