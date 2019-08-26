/** 
 * File Name:DemoQuartzJob.java 
 * Date:2015-11-15下午6:05:25 
 * 
 */ 
package com.ai.ecp.pmph.quartz.busi.job;

import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionException;

import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;
import com.ai.ecp.pmph.dubbo.interfaces.IPayQuartzInfoForZYOrdRSV;
import com.ai.ecp.quartz.QuartzContextHolder;
import com.ai.ecp.quartz.busi.common.AbstractCommonQuartzJob;
import com.ai.ecp.quartz.busi.util.JobConstants;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

/**
 * Project Name:quartz-services <br>
 * Description: <br>
 * Date:2015-11-15下午6:05:25  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
@DisallowConcurrentExecution
public class PayZYDigitalJob extends AbstractCommonQuartzJob {
    
    private static final String MODULE = PayZYDigitalJob.class.getName();
    
    private IPayQuartzInfoForZYOrdRSV payQuartzInfoForZYOrdRSV = QuartzContextHolder.getBean(IPayQuartzInfoForZYOrdRSV.class);
    
    @Override
    protected String getModule() {
        return MODULE;
    }

    @Override
    protected void doJob(Map<String, String> extendParams) throws JobExecutionException {
        LogUtil.info(MODULE, "=============begin PayZYDigitalJob job =============");
        try{
            int count = JobConstants.DEFAULT_DEALCOUNT;
            if(extendParams!=null){
                String countStr = extendParams.get(JobConstants.DealCount);
                if(!StringUtil.isBlank(countStr)){
                    count = Integer.parseInt(countStr);
                }
            }else{
                LogUtil.info(MODULE, "未获取到页面入参map");
            }
            RPayQuartzInfoRequest payQuartzInfoRequest = new RPayQuartzInfoRequest();
            payQuartzInfoRequest.setCount(count);
            List<RPayQuartzInfoResponse> list = payQuartzInfoForZYOrdRSV.queryNotDealZYDigitalOrder(payQuartzInfoRequest);
            if(list!=null&&!list.isEmpty()){
                for(RPayQuartzInfoResponse quartzInfoResponse:list){
                    try{
                        RPayQuartzInfoRequest rPayQuartzInfoRequest= new RPayQuartzInfoRequest();
                        ObjectCopyUtil.copyObjValue(quartzInfoResponse, rPayQuartzInfoRequest, null, false);
                        rPayQuartzInfoRequest.setStaffId(JobConstants.DEFAULT_STAFFID);
                        payQuartzInfoForZYOrdRSV.dealZYDigitalOrder(rPayQuartzInfoRequest);
                    }catch(Exception e){
                        LogUtil.error(MODULE,"=============订单"+quartzInfoResponse.getId()+"处理泽元数字教程授权接口异常=============",e);
                    }
                }
            }
        } catch(Exception err){
            LogUtil.error(MODULE,"=============PayZYDigitalJob error =============",err);
        }
        LogUtil.info(MODULE,"=============end PayZYDigitalJob job =============");
    }

}

