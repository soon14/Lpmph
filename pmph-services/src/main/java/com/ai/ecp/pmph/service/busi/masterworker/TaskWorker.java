package com.ai.ecp.pmph.service.busi.masterworker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;

public abstract class TaskWorker<E> implements Runnable{
	
	private static final String MODULE = TaskWorker.class.getName();
	
	private RFileImportRequest info;
	private AtomicInteger countTool;
	private ConcurrentLinkedQueue<List<Object>> workQueue;
	private ConcurrentHashMap<String, E> resultMap;
	
	public void setInfo(RFileImportRequest info){
		this.info = info;
	}
	
	public void setCountTool(AtomicInteger countTool){
		this.countTool = countTool;
	}
	
	public void setWorkerQueue(ConcurrentLinkedQueue<List<Object>> workQueue) {
		this.workQueue = workQueue;
	}

	public void setResultMap(ConcurrentHashMap<String, E> resultMap) {
		this.resultMap = resultMap;
	}
	
	@Override
	public void run() {
		
		while(true){
			List<Object> input = this.workQueue.poll();
			
			if(input == null){
				//System.out.println("input is null");
				break;
			}
			
			try{
				//真正地去做业务处理
				E output = handle(info, input);
				this.resultMap.put(generateResultMapID(this.countTool), output);
			}catch (BusinessException e){
				LogUtil.error(MODULE, "订单导入失败!");
			}
		}
	}
	//此方法用于被继承
	public abstract E handle(RFileImportRequest info, List<Object> input);
	//此方法被继承用于resultMap标记获取
	public abstract String generateResultMapID(AtomicInteger countTool);
}
