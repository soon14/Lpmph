package com.ai.ecp.pmph.dubbo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

import com.ai.ecp.frame.context.EcpFrameContextHolder;
import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
/**
 * 
 * Project Name:pmph-services-server <br>
 * Description: <br>
 * Date:2019-1-16下午3:45:02  <br>
 * 
 * @version  
 * @since JDK 1.7
 * 
 * forkjoin并行任务分配后，数据导入工作类
 * 
 */
public class OrdImportDistributeDataSubFJTask extends RecursiveTask<List<ConcurrentHashMap<String, RPreOrdMainsResponse>>>{

    private static final long serialVersionUID = -5838207561858858969L;
    private static final String MODULE = OrdImportDistributeDataSubFJTask.class.getName();

    //需要处理的数据
    private List<List<Object>> datas;
    //处理的文件
    private String datasFileId = null;
    private int startIndex;
    private int endIndex;
    
    private CountDownLatch countDownLatch = null;
    
    private RFileImportRequest rFileImportRequest = null;
    
    private IOrdImportSV ordImportSV = EcpFrameContextHolder.getBean("ordImportSV",IOrdImportSV.class);
    
    public OrdImportDistributeDataSubFJTask()
    {
        
    }
    
    public OrdImportDistributeDataSubFJTask(int startIndex, int endIndex, CountDownLatch countDownLatch, List<List<Object>> datas, RFileImportRequest rFileImportRequest)
    {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.countDownLatch = countDownLatch;
        this.datas = datas;
        this.rFileImportRequest = rFileImportRequest;
    }

    @Override
    protected List<ConcurrentHashMap<String, RPreOrdMainsResponse>> compute() {       
       return doCompute();
    }
    
    private List<ConcurrentHashMap<String, RPreOrdMainsResponse>>  doCompute(){
    	 LogUtil.info(MODULE, "子任务[id:"+Thread.currentThread().getId()+"]开始执行,工作区间[startIndex="+startIndex+",endIndex="+endIndex+"]");
         
    	 List<ConcurrentHashMap<String, RPreOrdMainsResponse>> rPreOrdMainsList = new ArrayList<ConcurrentHashMap<String, RPreOrdMainsResponse>>();
         
    	//添加一个计数器
		AtomicInteger countTool = new AtomicInteger(0);
    	 //处理子任务应该处理的数据偏移
         for(int i=startIndex; i<endIndex; i++)
         {
             List<Object> row= datas.get(i);
             RPreOrdMainsResponse rPreOrdMain = ordImportSV.saveOneOrdMainZD(rFileImportRequest, row);
             ConcurrentHashMap<String, RPreOrdMainsResponse> ordMainMap = new ConcurrentHashMap<String, RPreOrdMainsResponse>();
             ordMainMap.put(DateUtil.getDateString("yyyyMMddHHmmss")+countTool.incrementAndGet(), rPreOrdMain);
             rPreOrdMainsList.add(ordMainMap);
         }
         LogUtil.info(MODULE, "子任务[id:"+Thread.currentThread().getId()+"]执行结束");

         //计算器做减法
         countDownLatch.countDown();
         
         return rPreOrdMainsList;
    }

    public List<List<Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<List<Object>> datas) {
        this.datas = datas;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

	public RFileImportRequest getrFileImportRequest() {
		return rFileImportRequest;
	}

	public void setrFileImportRequest(RFileImportRequest rFileImportRequest) {
		this.rFileImportRequest = rFileImportRequest;
	}

	public String getDatasFileId() {
        return datasFileId;
    }

    public void setDatasFileId(String datasFileId) {
        this.datasFileId = datasFileId;
    }

}

