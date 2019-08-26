package com.ai.ecp.pmph.service.busi.masterworker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.paas.utils.LogUtil;

public class TaskManager<E> {
	
	private static final String MODULE = TaskManager.class.getName();
	
	private RFileImportRequest info;
	
	//0 定义一个原子计数器
	private AtomicInteger countTool;
	
	//1 定义承装任务的集合
	private ConcurrentLinkedQueue<List<Object>> workQueue = new ConcurrentLinkedQueue<List<Object>>();
	
	//2 使用HashMap去承装所有的worker对象
	private HashMap<String, Thread> workers = new HashMap<String, Thread>();
	
	//3 使用一个容器承装每一个worker并发执行任务的结果集
	private ConcurrentHashMap<String, E> resultMap = new ConcurrentHashMap<String, E>();
	
	//4 构造方法
	public TaskManager(RFileImportRequest info, AtomicInteger countTool, TaskWorker<E> taskWorker, int taskWorkerCount){
		// 每一个worker对象都需要有Master的引用workQueue用于任务的领取, resultMap用于任务的提交
		taskWorker.setWorkerQueue(this.workQueue);
		taskWorker.setResultMap(this.resultMap);
		this.countTool = countTool;
		taskWorker.setCountTool(this.countTool);
		this.info = info;
		taskWorker.setInfo(this.info);
		
		for(int i = 0; i < taskWorkerCount; i++){
			//key代表每一个taskWorker的名字, value表示线程执行对象
			workers.put(new StringBuffer("taskWorker子节点").append(Integer.toString(i)).toString()
					, new Thread(taskWorker));
		}
	}
	
	//5 提交方法(测试)
	public void submit(List<Object> task){
		this.workQueue.add(task);
	}
	
	//5 直接使用ConcurrentLinkedQueue
	/*public void submitTasks(ConcurrentLinkedQueue<E> tasks){
		this.workQueue = tasks;
	}*/
	
	//6 执行方法(启动使所有taskWorker开始工作)
	public void execute(){
		for(Map.Entry<String, Thread> me : workers.entrySet()){
			LogUtil.info(MODULE, new StringBuffer(me.getKey()).append("start").toString());
			me.getValue().start();
		}
	}
	
	//7 判断线程是否执行完毕
	public boolean isComplete() {
		for(Map.Entry<String, Thread> me : workers.entrySet()){
			if(me.getValue().getState() != Thread.State.TERMINATED){
				return false;
			}
		}
		return true;
	}
	
	//8 返回结果集数据(测试)
	public ConcurrentHashMap<String, E> getResult() {
		return this.resultMap;
	}
}
