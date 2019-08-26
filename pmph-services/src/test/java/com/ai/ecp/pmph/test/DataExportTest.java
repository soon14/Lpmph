package com.ai.ecp.pmph.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.ai.ecp.server.test.EcpServicesTest;
import com.ai.ecp.server.util.DataInoutUtil;

public class DataExportTest extends EcpServicesTest{
	
	@Resource
	DataSourceTransactionManager txManagerEcp;
	
	@Test
	public void testDataExport(){
		
		/*byte[] readFile = FileUtil.readFile("5b66df7d4eace34eb12d6dc6");
		OutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(readFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("++++++++++++:"+readFile);*/
		
		List<List<Object>> datas = new ArrayList<List<Object>>();
		
		List<String> titles = new ArrayList<String>();
		
		titles.add("序号");
		titles.add("名称");
		titles.add("日期");
		
		for(int i=0; i<20; i++){
			List<Object> data = new ArrayList<Object>();
			data.add(i);
			data.add("字符串"+i);
			data.add(new Date());
			datas.add(data);
		}
		DataInoutUtil.exportExcel(datas, titles, "测试", "xlsx", "demo", "jys");
		
		System.out.println("+++++++++");
	}
	
	@Test
	public void testMultiThreadsTransaction(){
		
		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		if(txManagerEcp!=null){
			System.out.println("++++++++++++txManager不为空!");
			txManagerEcp.getTransaction(def);
		}*/
	}
}
