package com.ai.ecp.pmph.quartz.busi.job;

import java.util.List;
import java.util.Map;

import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;
import com.ai.ecp.pmph.dubbo.interfaces.IPayQuartzInfoForExternalOrdRSV;
import com.ai.ecp.quartz.QuartzContextHolder;
import com.ai.ecp.quartz.busi.common.AbstractCommonQuartzJob;
import com.ai.ecp.quartz.busi.util.JobConstants;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

public class PayExternalMedicareJob extends AbstractCommonQuartzJob {
	
	private static final String MODULE = PayExternalMedicareJob.class.getName();

    private IPayQuartzInfoForExternalOrdRSV payQuartzInfoForExternalOrdRSV = QuartzContextHolder.getBean(IPayQuartzInfoForExternalOrdRSV.class);
    
	@Override
	protected String getModule() {
		
		return MODULE;
	}

	@Override
	protected void doJob(Map<String, String> extendParams) throws Exception {
		LogUtil.info(MODULE, "=============begin PayExternalDigitalJob job =============");
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
            List<RPayQuartzInfoResponse> list = payQuartzInfoForExternalOrdRSV.queryNotDealExternalMedicareOrder(payQuartzInfoRequest);
            if(list!=null&&!list.isEmpty()){
            	for(RPayQuartzInfoResponse quartzInfoResponse:list){
            		try{
            			RPayQuartzInfoRequest rPayQuartzInfoRequest= new RPayQuartzInfoRequest();
                        ObjectCopyUtil.copyObjValue(quartzInfoResponse, rPayQuartzInfoRequest, null, false);
                        rPayQuartzInfoRequest.setStaffId(JobConstants.DEFAULT_STAFFID);
                        payQuartzInfoForExternalOrdRSV.dealExternalMedicareOrder(rPayQuartzInfoRequest);
            		}catch(Exception e){
                        LogUtil.error(MODULE,"=============订单"+quartzInfoResponse.getId()+"处理外系统授权接口异常=============",e);
                    }
            	}
            }
		} catch(Exception err){
            LogUtil.error(MODULE,"=============PayExternalDigitalJob error =============",err);
        }
        LogUtil.info(MODULE,"=============end PayExternalDigitalJob job =============");
	}
}
