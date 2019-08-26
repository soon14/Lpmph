package com.ai.ecp.pmph.test;

import com.ai.ecp.goods.dubbo.dto.category.dataimport.DataImportConstants;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IERPGdsCategoryImportRSV;
import com.ai.ecp.server.test.EcpServicesTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class ERPGdsCategoryImportRSVImplTest extends EcpServicesTest{
    
    @Resource
    private IERPGdsCategoryImportRSV erpGdsCategoryImportRSV;
    
    @Test
    public void testExecuteImpot(){
        Map<String, Object> map=new HashMap<String, Object>();
        
        map.put(DataImportConstants.ERPDataMapKeys.RECORD_ID, 11111111);
        map.put(DataImportConstants.ERPDataMapKeys.XIANGMUPAIXU,10);
        map.put(DataImportConstants.ERPDataMapKeys.ZHUJIANXIANGMU,"ERP");
        map.put(DataImportConstants.ERPDataMapKeys.SORT_NO, 1);
        map.put(DataImportConstants.ERPDataMapKeys.CATG_CODE, "10000");
        map.put(DataImportConstants.ERPDataMapKeys.CATG_NAME, "ERP测试子分类A");
        map.put(DataImportConstants.ERPDataMapKeys.CATG_PARENT,"9999");
        map.put(DataImportConstants.ERPDataMapKeys.CATG_PARENT_NAME,"ERP测试分类");
        map.put(DataImportConstants.ERPDataMapKeys.XINXICAOZUO,1);
        
        this.erpGdsCategoryImportRSV.executeImport(map);
    }

}

