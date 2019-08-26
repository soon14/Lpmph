package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.pmph.dao.model.ZEResourceActivation;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouReqDTO;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouRespDTO;

/**
 */
public interface IGdsPmphYsymZhekouRSV {

    public GdsPmphYsymZhekouRespDTO queryZEResourceActivation(GdsPmphYsymZhekouReqDTO reqDTO);

    public boolean ifDigitalTeachOrDigitalBook(Long skuId);

}
