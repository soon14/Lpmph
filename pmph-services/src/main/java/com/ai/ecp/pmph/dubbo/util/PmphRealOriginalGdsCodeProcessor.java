package com.ai.ecp.pmph.dubbo.util;

import com.ai.ecp.goods.dubbo.interfaces.IRealOriginalGdsCodeProcessor;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;

import java.io.Serializable;

/**
 */
public class PmphRealOriginalGdsCodeProcessor implements IRealOriginalGdsCodeProcessor,Serializable {

    @Override
    public String getRealOriginalGdsCode(String processGdsCode){
        String srcCode=processGdsCode;
        if(processGdsCode.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EDBOOK_ORIGN_CODE_PREFIX)){
            srcCode=processGdsCode.substring(PmphGdsDataImportConstants.CodePreFix.ZY_EDBOOK_ORIGN_CODE_PREFIX.length());
        }else if(processGdsCode.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPER_ORIGN_CODE_PREFIX)){
            srcCode=processGdsCode.substring(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPER_ORIGN_CODE_PREFIX.length());
        }else if(processGdsCode.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPERBAG_ORIGN_CODE_PREFIX)){
            srcCode=processGdsCode.substring(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_PAPERBAG_ORIGN_CODE_PREFIX.length());
        }else if(processGdsCode.startsWith(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_TUTORIALCLASS_ORIGN_CODE_PREFIX)){
            srcCode=processGdsCode.substring(PmphGdsDataImportConstants.CodePreFix.ZY_EXAM_TUTORIALCLASS_ORIGN_CODE_PREFIX.length());
        }
        return srcCode;
    }

}
