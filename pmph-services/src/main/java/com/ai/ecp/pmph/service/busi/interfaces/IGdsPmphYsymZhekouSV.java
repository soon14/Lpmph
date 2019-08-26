package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.pmph.dao.model.ZEResourceActivation;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouReqDTO;

/**
 */
public interface IGdsPmphYsymZhekouSV {

    public ZEResourceActivation queryZEResourceActivation(GdsPmphYsymZhekouReqDTO reqDTO);

    public boolean ifDigitalTeachOrDigitalBook(Long skuId);

}
