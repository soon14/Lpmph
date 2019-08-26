/** 
 * File Name:IGdsPmphExSV.java 
 * Date:2016年8月15日下午4:42:57 
 * 
 */ 
package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.goods.dubbo.dto.common.LongReqDTO;
import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;

/**
 * Project Name:pmph-services-server <br>
 * Description: 商品域人卫扩展服务<br>
 * Date:2016年8月15日下午4:42:57  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IGdsPmphExRSV {
    /**
     * 
     * isVirtualCard:判断是否是虚拟卡. <br/> 
     * 
     * @param reqDTO
     * @return true代表是虚拟卡，false代表不是虚拟卡。
     * @since JDK 1.6
     */
    public boolean isVirtualCard(LongReqDTO reqDTO);
    
    /**
     * 
     * isVirtualProduct:是否是虚拟商品. <br/> 
     * 
     * @param reqDTO
     * @return 
     * @since JDK 1.6
     */
    public boolean isVirtualProduct(LongReqDTO reqDTO);
    
    
    /**
     * 
     * executeEBookIDSync:电子书ID同步. <br/> 
     * 
     * @param reqDTO 
     * @since JDK 1.6
     */
    public void executeEBookIDSync(EBookIDSyncReqDTO reqDTO);

}

