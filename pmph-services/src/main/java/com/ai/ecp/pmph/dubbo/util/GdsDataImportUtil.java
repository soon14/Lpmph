package com.ai.ecp.pmph.dubbo.util;

import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.GdsInterfaceCatgRespDTO;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.paas.utils.StringUtil;

/**
 */
public class GdsDataImportUtil {

    public static String getOriginCatgCodeWithoutPrefix(GdsInterfaceCatgRespDTO gdsInterfaceCatgRespDTO) {
        if(StringUtil.isNotBlank(gdsInterfaceCatgRespDTO.getOriginCatgCode())){
            if(PmphGdsDataImportConstants.Commons.ORIGIN_ZEYUN.equals(gdsInterfaceCatgRespDTO.getOrigin())){
                if(gdsInterfaceCatgRespDTO.getOriginCatgCode().startsWith(PmphGdsDataImportConstants.Commons.ZY_EXAM_ORIGN_CODE_PREFIX)){
                    return gdsInterfaceCatgRespDTO.getOriginCatgCode().substring(gdsInterfaceCatgRespDTO.getOriginCatgCode().indexOf(PmphGdsDataImportConstants.Commons.ZY_EXAM_ORIGN_CODE_PREFIX));
                }
            }
        }
        return gdsInterfaceCatgRespDTO.getOriginCatgCode();
    }
}
