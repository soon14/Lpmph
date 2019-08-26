/**
 * 
 */
package com.ai.ecp.pmph.service.busi.impl;

import javax.annotation.Resource;

import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.pmph.dao.mapper.busi.InfOrdSubMapper;
import com.ai.ecp.pmph.dao.model.InfOrdSub;
import com.ai.ecp.pmph.dubbo.util.PmphRealOriginalGdsCodeProcessor;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfSubSV;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月10日下午2:44:54 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class OrdInfSubSVImpl extends GeneralSQLSVImpl implements IOrdInfSubSV {

    @Resource
    private InfOrdSubMapper infOrdSubMapper;
    @Resource
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;
    
    private static final String MODULE = OrdInfSubSVImpl.class.getName();

    @Override
    public void saveOrdInfSub(InfOrdSub infOrdSub) {
        if(infOrdSub.getGdsId() != null){
            GdsInterfaceGdsGidxReqDTO res=new GdsInterfaceGdsGidxReqDTO();
            res.setOrigin("08");
            res.setGdsId(infOrdSub.getGdsId());
            GdsInterfaceGdsGidxRespDTO rep=gdsInterfaceGdsRSV.queryGdsInterfaceGdsGidxByEcpGdsId(res,new PmphRealOriginalGdsCodeProcessor());
            if(!StringUtil.isEmpty(rep)){
                infOrdSub.setInfGdsId(rep.getOriginGdsId());
            }
        }
        this.infOrdSubMapper.insert(infOrdSub);
    }
}
