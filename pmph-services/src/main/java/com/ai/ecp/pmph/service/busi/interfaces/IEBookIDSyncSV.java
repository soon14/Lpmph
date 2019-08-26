/** 
 * File Name:IEBookIDSyncSV.java 
 * Date:2016年8月16日上午11:27:13 
 * 
 */ 
package com.ai.ecp.pmph.service.busi.interfaces;

import java.util.List;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * Project Name:pmph-services-server <br>
 * Description: 电子书ID同步服务。<br>
 * Date:2016年8月16日上午11:27:13  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IEBookIDSyncSV {
    
    //1.新增 2.修改 3.删除.
    public enum Operate{
        ADD(1),EDIT(2),DEL(3);
        
        private int oper;

        public int getOper() {
            return oper;
        }

        Operate(int oper) {
            this.oper = oper;
        }

    };
    
    /**
     * ISBN属性ID。
     */
    public Long ISBN_PROP_ID = 1002L;
    /**
     * 标准ISBN属性ID。
     */
    public Long STAND_ISBN_PROP_ID = 1032L;
    
    /**
     * 数字图书编号属性ID。
     */
    public Long EBOOKID_PROP_ID = 1052L;
    
    /**
     * 数字图书编号属性ID。
     */
    public String P_BOOK_CATG = "1115";
    
    
    /**
     * 
     * executeEBookIDSync:电子书ID同步. <br/> 
     * 
     * @param reqDTO 
     * @since JDK 1.6
     */
    public List<GdsSkuInfoRespDTO> executeEBookIDSync(EBookIDSyncReqDTO reqDTO) throws BusinessException;

}

