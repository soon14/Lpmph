package com.ai.ecp.pmph.service.busi.impl;

import javax.annotation.Resource;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineResDTO;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineReqDTO;
import com.ai.ecp.im.dubbo.interfaces.IStaffHotlineRSV;
import com.ai.ecp.order.dao.mapper.common.ImOrdBelongMapper;
import com.ai.ecp.order.dao.model.ImOrdBelong;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dubbo.dto.ROfflineReviewRequest;
import com.ai.ecp.order.service.busi.impl.ImOrdBelongServiceImpl;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;

/**
 * 线下支付审核扩展
 *
 */
public class PmphImOrdBelongServiceImpl extends ImOrdBelongServiceImpl{

	public static final String MODULE = PmphImOrdBelongServiceImpl.class.getName();
	@Resource
	private ImOrdBelongMapper imOrdBelongMapper;
	
	@Resource
	private IStaffHotlineRSV staffHotlineRSV;
	
	@Resource
	private IOrdMainSV ordMainSV;
	
	@Override
	public void dealImOrdBelong(ROfflineReviewRequest rOfflineReviewRequest) {
		// TODO Auto-generated method stub

		ImStaffHotlineReqDTO dto = new ImStaffHotlineReqDTO();
		dto.setStaffCode(rOfflineReviewRequest.getStaff().getStaffCode());
		ImStaffHotlineResDTO resp = staffHotlineRSV.getStaffHotline(dto);
		//判断当前staffId是否为客服，若为客服择保存
		if(resp!=null&&resp.getStaffId()!=null&&resp.getStaffId()>0L){
			OrdMain ordMain = ordMainSV.queryOrderByOrderId(rOfflineReviewRequest.getOrderId());
			if(ordMain==null||ordMain.getId()==null){
				LogUtil.info(MODULE, "该订单不存在!");
				throw new BusinessException("该订单不存在!"); 
			}
			ImOrdBelong imOrdBelong = new ImOrdBelong();
			imOrdBelong.setOrdId(ordMain.getId());
			imOrdBelong.setBelongSerCode(resp.getOfStaffCode());
			imOrdBelong.setBelongSerName(resp.getHotlinePerson());
			imOrdBelong.setContactName(ordMain.getContactName());
			imOrdBelong.setMobileNumber(ordMain.getContactNumber());
			imOrdBelong.setOrdTime(ordMain.getOrderTime());
			imOrdBelong.setPayTime(ordMain.getPayTime());
			imOrdBelong.setRealMoney(ordMain.getRealMoney());
			imOrdBelong.setStaffCode(ordMain.getStaffCode());
			imOrdBelong.setStaffId(ordMain.getStaffId());
			LogUtil.info(MODULE, PmphImOrdBelongServiceImpl.class+":-----开始保存-----");
			imOrdBelongMapper.insertSelective(imOrdBelong);
			LogUtil.info(MODULE, PmphImOrdBelongServiceImpl.class+":-----保存成功-----");
		}
	}
}
