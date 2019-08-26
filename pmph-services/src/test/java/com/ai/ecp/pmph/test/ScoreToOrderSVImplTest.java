package com.ai.ecp.pmph.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.ai.ecp.pmph.dubbo.dto.OrderBackMainRWReqDTO;
import com.ai.ecp.pmph.service.busi.interfaces.IStaffUnionRWSV;
import com.ai.ecp.server.test.EcpServicesTest;
import com.ai.ecp.staff.dubbo.dto.OrderBackMainReqDTO;
import com.ai.ecp.staff.dubbo.dto.OrderBackSubReqDTO;

public class ScoreToOrderSVImplTest  extends EcpServicesTest {


    @Resource
    private IStaffUnionRWSV staffUnionRWSV;


    @Test
    public void testFrozen(){
    	OrderBackMainReqDTO<OrderBackSubReqDTO> req = new OrderBackMainReqDTO<OrderBackSubReqDTO>();
    	req.setOrderId("RW15112300008716");
    	req.setStaffId(3104L);
    	req.setScale(500000L);
    	req.setLastFlag(true);
    	Long score = staffUnionRWSV.saveScoreFrozenForOrderBackRW(req);
    	System.out.println(score);
    }


    @Test
    public void testFrozenModify(){
    	OrderBackMainRWReqDTO<OrderBackSubReqDTO>  req = new OrderBackMainRWReqDTO<OrderBackSubReqDTO>();
    	req.setOrderId("RW15112300008716");
    	req.setStaffId(3104L);
    	req.setScale(600000L);
    	req.setBackScore(38L);
    	req.setModifyBackSocre(40L);
    	Long score = staffUnionRWSV.saveScoreFrozenModifyForOrderBackRW(req);
    	System.out.println(score);
    }

    @Test
    public void testSaveScore(){
    	OrderBackMainRWReqDTO<OrderBackSubReqDTO>  req = new OrderBackMainRWReqDTO<OrderBackSubReqDTO>();
    	req.setOrderId("RW15112300008716");
    	req.setStaffId(3104L);
    	req.setScale(600000L);
    	req.setBackScore(56L);
    	req.setModifyBackSocre(56L);
    	req.setBackId(4L);
    	staffUnionRWSV.saveScoreAcctForOrderBackRW(req, null);
    }
    

}
