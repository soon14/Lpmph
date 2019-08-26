package com.ai.ecp.pmph.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ai.ecp.order.dao.model.OrdSub;
import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayExternalMedicareSV;
import com.ai.ecp.server.test.EcpServicesTest;
import com.ai.paas.utils.ObjectCopyUtil;

public class PayExternalMedicareSVImplTest extends EcpServicesTest {
	
	@Resource
	private IPayExternalMedicareSV payExternalMedicareSV;
	
	@Resource
    private IOrdSubSV ordSubSV;
	
	@Test
	public void testPayExternalMedicareSV(){
		PaySuccInfo paySuccInfo =new PaySuccInfo();
		paySuccInfo.setOrderId("RW18062240184459");
		paySuccInfo.setStaffId(812678L);
		List<OrdSub> OrdSubList = ordSubSV.queryOrderSubByOrderId(paySuccInfo.getOrderId());
        if(OrdSubList!=null&&!OrdSubList.isEmpty()){
        	PaySuccInfo paySuccInfoPara = new PaySuccInfo();
            for(OrdSub ordSub:OrdSubList){
            	ObjectCopyUtil.copyObjValue(paySuccInfo, paySuccInfoPara, null, false);
                paySuccInfoPara.setSubOrder(ordSub.getId());
                payExternalMedicareSV.deal(paySuccInfoPara);
            }
        }
	}
}
