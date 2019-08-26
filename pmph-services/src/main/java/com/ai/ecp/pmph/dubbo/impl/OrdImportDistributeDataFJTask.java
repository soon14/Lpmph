package com.ai.ecp.pmph.dubbo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RecursiveTask;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.paas.utils.LogUtil;

/**
 * 
 * Project Name:pmph-services-server <br>
 * Description: <br>
 * Date:2019-1-16下午2:17:18  <br>
 * 
 * @version  
 * @since JDK 1.7
 * 
 * forkjoin并行任务分配控制类
 */
public class OrdImportDistributeDataFJTask extends RecursiveTask<List<ConcurrentHashMap<String, RPreOrdMainsResponse>>>{
    
    private static final long serialVersionUID = -46220250989880427L;
    
    private static final String MODULE = OrdImportDistributeDataFJTask.class.getName();
    
    //一个任务单执行的数据量,根据测试调整
    private final int ONE_TASK_DEAL_NUMBER = 20;
    
    //秒  86400秒=24小时
    public static final int cacheSeconds = 86400;
    
    //计数器，计算器执行完，表示最后一个任务也跑完
    private CountDownLatch countDownLatch = null;

    //导入文件
    private String fileId = null;
    //需要处理的数据
    private List<List<Object>> datas;
    //文件信息
    private RFileImportRequest rFileImportRequest = null;
    
    public OrdImportDistributeDataFJTask(String fileId, RFileImportRequest info, List<List<Object>> datas)
    {
        this.fileId = fileId;
        this.rFileImportRequest = info;
        this.datas = datas;
    }
    
    @Override
    protected List<ConcurrentHashMap<String, RPreOrdMainsResponse>> compute() {
        
        LogUtil.warn(MODULE, "主线程[ID："+Thread.currentThread().getId()+"]开始执行");
        
        List<ConcurrentHashMap<String, RPreOrdMainsResponse>> resultMapList = new ArrayList<ConcurrentHashMap<String, RPreOrdMainsResponse>>();
        
        //需要拆分成多少个任务单数
        int needSplitCount = 1;
        //剩余多少任务单
        int leftCount = datas.size()%ONE_TASK_DEAL_NUMBER;
        needSplitCount = (leftCount == 0?datas.size()/ONE_TASK_DEAL_NUMBER:datas.size()/ONE_TASK_DEAL_NUMBER+1);
        //初始化计数器
        countDownLatch = new CountDownLatch(needSplitCount);
        
        //保存子任务
        List<OrdImportDistributeDataSubFJTask> subTasks = new ArrayList<OrdImportDistributeDataSubFJTask>(needSplitCount);
        
        //拆分任务单
        for(int i=0; i<needSplitCount; i++)
        {
            OrdImportDistributeDataSubFJTask workTask = null;
            if(i == needSplitCount-1 && leftCount!=0)
            {
                
                workTask  = new OrdImportDistributeDataSubFJTask(i*ONE_TASK_DEAL_NUMBER, i*ONE_TASK_DEAL_NUMBER+leftCount, countDownLatch, datas, rFileImportRequest);
            }else {
                workTask = new OrdImportDistributeDataSubFJTask(i*ONE_TASK_DEAL_NUMBER, (i+1)*ONE_TASK_DEAL_NUMBER, countDownLatch, datas, rFileImportRequest);
            }
            workTask.setDatasFileId(this.fileId);
            workTask.setrFileImportRequest(this.rFileImportRequest);
            subTasks.add(workTask);
            //提交到线程池队列中
            workTask.fork();
        }
        
        /*该注释也可打开，下面的for循环中的join也有阻塞的作用
        try {
            //等待所有子任务执行完毕，考虑设置超时
            countDownLatch.await();
        } catch (InterruptedException e) {
        }*/
        
        //所有任务执行完毕汇总子任务单处理结果
        for(OrdImportDistributeDataSubFJTask task : subTasks)
        {
        	List<ConcurrentHashMap<String, RPreOrdMainsResponse>> subRPreOrdMain = task.join();
        	resultMapList.addAll(subRPreOrdMain);
        }
        LogUtil.info(MODULE, "所有子任务执行完毕");
        
        return resultMapList;
    }

}

