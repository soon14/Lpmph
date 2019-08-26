package com.ai.ecp.pmph.service.busi.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.order.dao.model.GoodPayedReport;
import com.ai.ecp.order.dao.model.GoodStaffPayedReport;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dao.model.OrdSub;
import com.ai.ecp.order.dubbo.dto.ROrdPayRelReq;
import com.ai.ecp.order.dubbo.dto.ROrdPayRelResp;
import com.ai.ecp.order.dubbo.dto.pay.PayResultDTO;
import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.util.OrdConstants.PayStatus;
import com.ai.ecp.order.service.busi.impl.pay.PayWaySVImpl;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdPayRelSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayQuartzInfoSV;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayResultSV;
import com.ai.ecp.order.service.busi.interfaces.report.IGoodPayedSV;
import com.ai.ecp.order.service.busi.interfaces.report.IGoodStaffPayedSV;
import com.ai.ecp.pmph.dao.model.InfOrdMain;
import com.ai.ecp.pmph.dao.model.InfOrdSub;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfMainSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfSubSV;
import com.ai.ecp.pmph.service.busi.interfaces.IPayQuartzInfoPmphSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;

/**
 * 
 * Project Name:ecp-services-order-server <br>
 * Description: 支付通道信息<br>
 * Date:2015年10月8日下午2:59:09 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class PayWayPmphSVImpl extends PayWaySVImpl {

	@Resource
	private IPayResultSV payResultSV;

	@Resource
	private IOrdMainSV ordMainSV;

	@Resource
	private IOrdInfMainSV ordInfMainSV;

	@Resource
	private IPayQuartzInfoSV payQuartzInfoSV;

	@Resource
	private IPayQuartzInfoPmphSV payQuartzInfoPmphSV;

	@Resource
	private IOrdSubSV ordSubSV;

	@Resource
	private IOrdInfSubSV ordInfSubSV;

	@Resource
	private IGoodPayedSV goodPayedSV;

	@Resource
	private IGoodStaffPayedSV goodStaffPayedSV;

	@Resource
	private IOrdPayRelSV iOrdPayRelSV;
	

	public static final String MODULE = PayWayPmphSVImpl.class.getName();

	/**
	 * 
	 * payBackSuc:处理支付成功逻辑.记录重复支付 <br/>
	 * 
	 * @param paySuccInfo
	 * @since JDK 1.6
	 */
	public void payBackSuc(PaySuccInfo paySuccInfo) {
		super.payBackSuc(paySuccInfo);
		// 定时任务补上泽元的定时任务表的数据
        List<OrdSub> ordSubList = ordSubSV.queryOrderSubByOrderId(paySuccInfo.getOrderId());
        for(OrdSub ordSub :ordSubList){
            RPayQuartzInfoRequest payQuartzInfo = new RPayQuartzInfoRequest();
            payQuartzInfo.setOrderId(paySuccInfo.getOrderId());
            payQuartzInfo.setStaffId(paySuccInfo.getStaffId());
            payQuartzInfo.setDealFlag(PayStatus.PAY_DEAL_FLAG_0);
            payQuartzInfo.setCreateStaff(paySuccInfo.getStaffId());
            //泽元接口
            payQuartzInfo.setSubOrder(ordSub.getId());
            payQuartzInfoPmphSV.addZYDigitalInfo(payQuartzInfo);
            payQuartzInfoPmphSV.addZYExaminationInfo(payQuartzInfo);
            //外系统接口
            payQuartzInfoPmphSV.addExternalMedicareInfo(payQuartzInfo);
            InfOrdSub infOrdSub = new InfOrdSub();
            ObjectCopyUtil.copyObjValue(ordSub, infOrdSub, null, false);
            ordInfSubSV.saveOrdInfSub(infOrdSub);
        }
		// ERP同步接口
		ROrdPayRelReq rOrdPayRelReq = new ROrdPayRelReq();
		String joinOrderid = paySuccInfo.getOrderId();
		rOrdPayRelReq.setJoinOrderid(joinOrderid);
		List<ROrdPayRelResp> resultList = iOrdPayRelSV.queryOrdPayRelByOption(rOrdPayRelReq);
		if (resultList != null && resultList.size() >= 1) {
			for (ROrdPayRelResp resp : resultList) {
				InfOrdMain infOrdMain = new InfOrdMain();
				OrdMain ordMain = ordMainSV.queryOrderByOrderId(resp.getOrderId());
				ObjectCopyUtil.copyObjValue(ordMain, infOrdMain, null, false);
				ordInfMainSV.saveOrdInfMain(infOrdMain);
			}
		}else{
			InfOrdMain infOrdMain = new InfOrdMain();
			OrdMain ordMain = ordMainSV.queryOrderByOrderId(paySuccInfo.getOrderId());
			ObjectCopyUtil.copyObjValue(ordMain, infOrdMain, null, false);
			ordInfMainSV.saveOrdInfMain(infOrdMain);			
		}
	}
}
