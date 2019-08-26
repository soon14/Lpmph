package com.ai.ecp.pmph.service.busi.impl;

import com.ai.ecp.pmph.dao.mapper.busi.InfOrdMainHisMapper;
import com.ai.ecp.pmph.dao.model.InfOrdMainHis;
import com.ai.ecp.pmph.dao.model.InfOrdMainHisCriteria;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfMainHisSV;

import javax.annotation.Resource;

/**
 */
public class OrdInfMainHisSVImpl implements IOrdInfMainHisSV{

    @Resource
    private InfOrdMainHisMapper infOrdMainHisMapper;

    @Override
    public long queryOrdInfMainHisNumByOrderId(String orderId) {
        InfOrdMainHisCriteria infOrdMainHisCriteria = new InfOrdMainHisCriteria();
        infOrdMainHisCriteria.createCriteria().andIdEqualTo(orderId);
        Long num = this.infOrdMainHisMapper.countByExample(infOrdMainHisCriteria);
        return num;
    }
}
