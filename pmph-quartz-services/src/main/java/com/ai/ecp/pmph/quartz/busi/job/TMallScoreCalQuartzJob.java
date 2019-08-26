/** 
 * File Name:DemoQuartzJob.java 
 * Date:2015-11-15下午6:05:25 
 * 
 */ 
package com.ai.ecp.pmph.quartz.busi.job;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionException;
import org.springframework.util.CollectionUtils;

import com.ai.ecp.general.order.dto.PayQuartzInfoRequest;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdTmMainRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IScoreCalRSV;
import com.ai.ecp.quartz.QuartzContextHolder;
import com.ai.ecp.quartz.busi.common.AbstractCommonQuartzJob;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;

/**
 * 
 * Project Name:quartz-services <br>
 * Description: 天猫订单积分计算定时任务<br>
 * Date:2016-2-18下午5:04:05  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@DisallowConcurrentExecution
public class TMallScoreCalQuartzJob extends AbstractCommonQuartzJob {
    
    private static final String MODULE = TMallScoreCalQuartzJob.class.getName();
    
    private IScoreCalRSV scoreCalRSV = QuartzContextHolder.getBean(IScoreCalRSV.class);
    
    private IOrdTmMainRSV ordTmMainRSV = QuartzContextHolder.getBean(IOrdTmMainRSV.class);

    @Override
    protected String getModule() {
        return MODULE;
    }

    /**
     * 
     * 符合条件的天猫订单赠送积分计算任务 
     * @see com.ai.ecp.quartz.busi.common.AbstractCommonQuartzJob#doJob(java.util.Map)
     */
    @Override
    protected void doJob(Map<String, String> extendParams) throws JobExecutionException {
        LogUtil.info(MODULE, "=============begin 天猫订单，赠送积分任务开始  job =============");
        try{
            String timeStr = extendParams.get("createTime");
            int overDay = Integer.parseInt(extendParams.get("overDay"));
            ROrdTmMainReq req = new ROrdTmMainReq();
            Timestamp createTime = DateUtil.getTimestamp(timeStr, "yyyy-MM-dd");
            req.setPageNo(1);
            req.setPageSize(10000);
            req.setCreateTime(createTime);//条件的起始时间
            List<ROrdTmMainResp> respList = ordTmMainRSV.queryOrderTmMainNoGift(req);
            if (!CollectionUtils.isEmpty(respList)) {
                for (ROrdTmMainResp res : respList) {
                   //导入时间之后的15天，交易状态还为：交易成功时，才能进行积分赠送
                   String importTime = DateUtil.getDateString(res.getImportTime(), "yyyy-MM-dd");
                   if (DateUtil.getOffsetDaysTime(DateUtil.getTimestamp(importTime), overDay).after(DateUtil.getSysDate())) {
                       continue;
                   }
                   //该参数用于传入staff_id
                   CustInfoReqDTO custInfo = new CustInfoReqDTO();
                   custInfo.setId(res.getRwStaffId());//本平台对应的会员id
                   //该参数用于传入order_id与real_money
                   PayQuartzInfoRequest quartz = new PayQuartzInfoRequest();
                   quartz.setOrderId(res.getOrderCode());//订单编码
                   Double realMoney = Double.parseDouble(res.getRealMoney());
                   realMoney = realMoney * 100;//单位转为分
                   quartz.setPayment(realMoney.longValue());//实付金额
                   /*调用接口，赠送积分，用户行为04：天猫订单*/
                   long score = scoreCalRSV.saveScoreCal(StaffConstants.TMALL_ORDER_TYPE, custInfo, quartz);
                   /*回调订单域接口：更新该条记录为已赠送积分*/
                   ROrdTmMainReq callBackReq = new ROrdTmMainReq();
                   callBackReq.setOrderCode(res.getOrderCode());//订单编码
                   callBackReq.setRwScoreFlag("1");//已赠送积分标识
                   callBackReq.setRwScore(score);//赠送的积分
                   ordTmMainRSV.updateOrderTmMainScore(callBackReq);
                }
            }
        } catch(Exception err){
            LogUtil.error(MODULE,"=============天猫订单，赠送积分任务  业务处理异常 =============",err);
        }
        LogUtil.info(MODULE,"=============end 天猫订单，赠送积分任务开始 job =============");
    }
   
}

