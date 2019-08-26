/**
 * 
 */
package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.pmph.dao.model.InfOrdMain;



/**
 *
 */
public interface IOrdInfMainSV {

    public void saveOrdInfMain(InfOrdMain infOrdMain);
    
    public long queryOrdInfMainNumByOrderId(String orderId);

}
