package com.ai.ecp.pmph.dubbo.impl;

import com.ai.ecp.pmph.dao.model.ZEResourceActivation;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouReqDTO;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouRespDTO;
import com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphYsymZhekouRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IGdsPmphYsymZhekouSV;
import com.ai.paas.utils.ObjectCopyUtil;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

/**
 */
public class GdsPmphYsymZhekouRSVImpl implements IGdsPmphYsymZhekouRSV {

    @Resource
    private IGdsPmphYsymZhekouSV gdsPmphYsymZhekouSV;

    @Override
    public GdsPmphYsymZhekouRespDTO queryZEResourceActivation(GdsPmphYsymZhekouReqDTO reqDTO) {
        ZEResourceActivation zeResourceActivation=this.gdsPmphYsymZhekouSV.queryZEResourceActivation(reqDTO);
        GdsPmphYsymZhekouRespDTO dto = new GdsPmphYsymZhekouRespDTO();
        ObjectCopyUtil.copyObjValue(zeResourceActivation, dto, null, true);
        return dto;
    }

    @Override
    public boolean ifDigitalTeachOrDigitalBook(Long skuId) {
        return this.gdsPmphYsymZhekouSV.ifDigitalTeachOrDigitalBook(skuId);
    }
}
