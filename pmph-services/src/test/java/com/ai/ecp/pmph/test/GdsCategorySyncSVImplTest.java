package com.ai.ecp.pmph.test;

import com.ai.ecp.goods.dubbo.constants.GdsDataImportConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCatgSyncReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCatgSyncRespDTO;
import com.ai.ecp.goods.service.common.interfaces.IGdsCategorySyncSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.server.test.EcpServicesTest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;

import javax.annotation.Resource;

public class GdsCategorySyncSVImplTest extends EcpServicesTest{

    @Resource
    private IGdsCategorySyncSV gdsCategorySyncSV;
    
    @Test
    public void testSaveGdsCategory() throws Exception {
        GdsCatgSyncReqDTO catgSyncReqDTO = new GdsCatgSyncReqDTO();
        catgSyncReqDTO.setCatgCode("322");
        catgSyncReqDTO.setCatgName("测试分类映射");
        catgSyncReqDTO.setCatgParent("测试分类映射");
        catgSyncReqDTO.setMapCatgCode("3333");
        catgSyncReqDTO.setSrcSystem("测试来源");
     
            gdsCategorySyncSV.addGdsCatgSyncInfo(catgSyncReqDTO);
    
    }
    
    @Test
    public void testQueryGdsCategorySyncByPK()throws Exception{
       GdsCatgSyncReqDTO catgSyncReqDTO = new GdsCatgSyncReqDTO();
	   catgSyncReqDTO.setCatgCode("14479");
       catgSyncReqDTO.setSrcSystem(PmphGdsDataImportConstants.SrcSystem.ZY_02);
       GdsCatgSyncRespDTO respDTO =  gdsCategorySyncSV.queryGdsCategorySyncByPK(catgSyncReqDTO);
       System.out.println("=============="+ToStringBuilder.reflectionToString(respDTO,ToStringStyle.SHORT_PREFIX_STYLE));
    }
}

