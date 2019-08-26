/**
 * 
 */
package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.pmph.dao.model.InfOrdSub;
import com.ai.ecp.server.service.interfaces.IGeneralSQLSV;



/**
 *
 */
public interface IOrdInfSubSV  extends IGeneralSQLSV{

    public void saveOrdInfSub(InfOrdSub infOrdSub);

}
