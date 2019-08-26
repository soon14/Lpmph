package com.ai.ecp.pmph.service.busi.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.pmph.service.busi.interfaces.IZDOrdImportSV;
import com.ai.ecp.pmph.service.busi.masterworker.TaskManager;
import com.ai.ecp.pmph.service.busi.masterworker.ZDOrdWorker;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.paas.utils.LogUtil;

@Service("zDOrdImportSV")
public class ZDOrdImportSVImpl extends GeneralSQLSVImpl implements IZDOrdImportSV {
	
	private static final String MODULE = ZDOrdImportSVImpl.class.getName();
	
	@Override
	public ConcurrentHashMap<String, RPreOrdMainsResponse> importZDPreOrdData(RFileImportRequest info, List<List<Object>> group, AtomicInteger countTool, ConcurrentHashMap<String, RPreOrdMainsResponse> resultMap) {
		LogUtil.info(MODULE, new StringBuffer("征订单导入service开启,当先机器性能线程数为").append(Runtime.getRuntime().availableProcessors()).toString());
		TaskManager<RPreOrdMainsResponse> taskManager = new TaskManager<RPreOrdMainsResponse>(info, countTool, new ZDOrdWorker(), 
				Runtime.getRuntime().availableProcessors()/2);
		for (List<Object> row : group) {
			
			taskManager.submit(row);
		}
		taskManager.execute();
		long start = System.currentTimeMillis();
		
		ConcurrentHashMap<String, RPreOrdMainsResponse> result = null;
		while(true){
			if(taskManager.isComplete()){
				long end = System.currentTimeMillis() - start;
				result = taskManager.getResult();
				LogUtil.info(MODULE, new StringBuffer("最终执行结果:").append(result).append(", 其中耗时:").append(end).append("毫秒").toString());
				break;
			}
		}
		return result;
		
		//单线程测试
		/*long start = System.currentTimeMillis();
		for (List<Object> row : group) {
			
			IOrdImportSV ordImportSV = EcpFrameContextHolder.getBean(
	                "ordImportSV",
	                IOrdImportSV.class);

			RPreOrdMainsResponse rPreOrdMains = ordImportSV.saveOneOrdMainZD(info, row);
			resultMap.put(new StringBuffer().append(countTool.incrementAndGet()).toString(), rPreOrdMains);
		}
		long end = System.currentTimeMillis() - start;
		LogUtil.info(MODULE, new StringBuffer("最终执行结果:").append(", 其中耗时:").append(end).append("毫秒").toString());
		return resultMap;*/
	}

}
