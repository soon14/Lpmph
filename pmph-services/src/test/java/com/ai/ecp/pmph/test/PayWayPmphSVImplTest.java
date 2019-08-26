package com.ai.ecp.pmph.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.util.OrdConstants.PayStatus;
import com.ai.ecp.pmph.service.busi.interfaces.IPayQuartzInfoPmphSV;
import com.ai.ecp.server.test.EcpServicesTest;

public class PayWayPmphSVImplTest extends EcpServicesTest{
	
	@Resource
	private IPayQuartzInfoPmphSV payQuartzInfoPmphSV;
	
	@Test
	public void testPayBackSuc(){
		
		RPayQuartzInfoRequest payQuartzInfo = new RPayQuartzInfoRequest();
		payQuartzInfo.setOrderId("RW00001l");
        payQuartzInfo.setStaffId(120L);
        payQuartzInfo.setDealFlag(PayStatus.PAY_DEAL_FLAG_0);
        payQuartzInfo.setCreateStaff(120L);
        payQuartzInfo.setSubOrder("RW00001l");
		
		payQuartzInfoPmphSV.addExternalMedicareInfo(payQuartzInfo);
	}
}
