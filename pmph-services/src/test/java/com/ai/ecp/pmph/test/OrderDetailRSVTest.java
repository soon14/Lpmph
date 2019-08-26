package com.ai.ecp.pmph.test;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.ecp.aip.third.dubbo.constants.AipThirdConstants;
import com.ai.ecp.aip.third.dubbo.dto.req.OrderReqDTO;
import com.ai.ecp.server.test.EcpServicesTest;
import com.ai.ecp.unpf.dubbo.interfaces.order.IUnpfOrdDetialRSV;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrderDetailRSVTest extends EcpServicesTest {

	//SELECT * FROM T_UNPF_SHOP_AUTH_01
	
	@Resource
	private IUnpfOrdDetialRSV unpfOrdDetialRSV;
	
	/*@Test
	public void testSimpleUnpfOrderMain(){
		OrderReqDTO orderReqDTO=new OrderReqDTO();
		orderReqDTO.setOrderId("2874429102559866");
    	orderReqDTO.setShopId(100L);
    	orderReqDTO.setAuthId(7L);
    	
    	orderReqDTO.setPlatType(AipThirdConstants.Commons.TAOBAO);
    	orderReqDTO.setAppkey("1023521935");    	
    	orderReqDTO.setAppscret("sandbox75de5da394607a610715e572a");    	
    	orderReqDTO.setServerUrl("https://gw.api.tbsandbox.com/router/rest");
    	orderReqDTO.setAccessToken("6201b068ddc6cceg74de95d579fe8465061144b5ab9eafd2074082786");
    	unpfOrdDetialRSV.dealSimpleUnpfOrderMain(orderReqDTO);
	}*/
	
	@Test
	public void testUnpfOrderMain(){
		OrderReqDTO orderReqDTO=new OrderReqDTO();
		orderReqDTO.setOrderId("2886838681759866");
    	orderReqDTO.setShopId(35L);
    	orderReqDTO.setAuthId(45L);
    	
    	orderReqDTO.setPlatType(AipThirdConstants.Commons.TAOBAO);
    	orderReqDTO.setAppkey("23521935");    	
    	orderReqDTO.setAppscret("bedf75375de5da394607a610715e572a");    	
    	orderReqDTO.setServerUrl("https://eco.taobao.com/router/rest");
    	orderReqDTO.setAccessToken("62021072e189dbceg470c7d398ed700fe0ffd4e4f2dd5e91125014918");
    	unpfOrdDetialRSV.dealUnpfOrderMain(orderReqDTO);
    	System.out.println("成功啦");
	}
/*	
	@Test
	public void testCallBack(){
		OrderReqDTO ordReq=new OrderReqDTO();
		ordReq.setShopId(35L);
		ordReq.setPlatType("taobao");
		ordReq.setAuthId(45L);

		String content = "{\"buyer_nick\":\"hjx0111\",\"payment\":\"0.01\",\"status\":\"TRADE_NO_CREATE_PAY\",\"oid\":2870548692739866,\"tid\":2870548692739866,\"type\":\"guarantee_trade\",\"seller_nick\":\"人民卫生出版社旗舰店\"}";
		JSONObject jsob=JSON.parseObject(content);
		ordReq.setOrderId(jsob.getString("tid"));
		//dubbbo内部组织店铺对应的授权信息
		unpfOrdDetialRSV.dealUnpfOrderMain(ordReq);
	}*/
}
