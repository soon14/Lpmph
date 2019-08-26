package com.ai.ecp.pmph.test;

import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IZYGdsCategoryImportRSV;
import com.ai.ecp.server.test.EcpServicesTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class ZYGdsCategoryImportRSVImplTest extends EcpServicesTest{
    
    @Resource
    private IZYGdsCategoryImportRSV zyGdsCategoryImportRSV;
    
    @Test
    public void testExecuteImpot(){
        Map<String, Object> map=new HashMap<String, Object>();
        
        map.put(DataImportConstants.ZYDataMapKeys.CATG_CODE, 11111111L);
        map.put(DataImportConstants.ZYDataMapKeys.CATG_NAME,"泽元分类");
        map.put(DataImportConstants.ZYDataMapKeys.CATG_PARENT,0L);
        map.put(DataImportConstants.ZYDataMapKeys.SORT_NO, 1);
        map.put(DataImportConstants.ZYDataMapKeys.TYPE, 1);
        
        this.zyGdsCategoryImportRSV.executeImport(map);
    }

}

