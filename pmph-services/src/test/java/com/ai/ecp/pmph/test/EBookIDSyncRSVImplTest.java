package com.ai.ecp.pmph.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;
import com.ai.ecp.pmph.dubbo.interfaces.IEBookIDSyncRSV;
import com.ai.ecp.server.test.EcpServicesTest;

public class EBookIDSyncRSVImplTest extends EcpServicesTest{
    
    @Resource
    private IEBookIDSyncRSV eBookIDSyncRSV;
    
    @Test
    public void executeEBookIDSync(){
        System.out.println("===========================Begin===========================");
        EBookIDSyncReqDTO reqDTO = new EBookIDSyncReqDTO();
        reqDTO.setIsbn("ISBN 7-117-04692-9/R·4693");
        reqDTO.setEbook_id("ABCDEFG999");
        reqDTO.setOperate("3");
        eBookIDSyncRSV.executeEBookIDSync(reqDTO);
        System.out.println("===========================End===========================");
    }

}

