package com.ai.ecp.pmph.service.busi.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.order.dao.mapper.common.PayQuartzInfoLogMapper;
import com.ai.ecp.order.dao.mapper.common.PayQuartzInfoMapper;
import com.ai.ecp.order.dao.model.PayQuartzInfo;
import com.ai.ecp.order.dao.model.PayQuartzInfoCriteria;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoRequest;
import com.ai.ecp.order.dubbo.dto.pay.RPayQuartzInfoResponse;
import com.ai.ecp.order.dubbo.util.OrdConstants.PayStatus;
import com.ai.ecp.pmph.service.busi.interfaces.IPayQuartzInfoPmphSV;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.db.sequence.Sequence;

public class PayQuartzInfoPmphSVImpl extends GeneralSQLSVImpl implements IPayQuartzInfoPmphSV {

    @Resource
    private PayQuartzInfoMapper payQuartzInfoMapper;

    @Resource
    private PayQuartzInfoLogMapper payQuartzInfoLogMapper;

    @Resource(name = "seq_pay_quartz_info")
    private Sequence seqPayQuartzInfo;

    @Resource(name = "seq_pay_quartz_info_log")
    private Sequence seqPayQuartzInfoLog;

    @Override
    public void addZYDigitalInfo(RPayQuartzInfoRequest payQuartzInfo) {
        PayQuartzInfo record = new PayQuartzInfo();
        ObjectCopyUtil.copyObjValue(payQuartzInfo, record, null, false);
        record.setId(seqPayQuartzInfo.nextValue());
        record.setTaskType(PayStatus.PAY_TASK_TYPE_03);
        record.setErrorTimes(0);
        record.setCreateTime(DateUtil.getSysDate());
        payQuartzInfoMapper.insert(record);
    }
    @Override
    public void addZYExaminationInfo(RPayQuartzInfoRequest payQuartzInfo) {
        PayQuartzInfo record = new PayQuartzInfo();
        ObjectCopyUtil.copyObjValue(payQuartzInfo, record, null, false);
        record.setId(seqPayQuartzInfo.nextValue());
        record.setTaskType(PayStatus.PAY_TASK_TYPE_04);
        record.setErrorTimes(0);
        record.setCreateTime(DateUtil.getSysDate());
        payQuartzInfoMapper.insert(record);
    }


    private PageResponseDTO<RPayQuartzInfoResponse> queryByPagination(BaseInfo baseInfo,BaseCriteria criteria){
        return super.queryByPagination(baseInfo, criteria, true, new PaginationCallback<PayQuartzInfo, RPayQuartzInfoResponse>() {

            @Override
            public List<PayQuartzInfo> queryDB(BaseCriteria bCriteria) {
                return payQuartzInfoMapper.selectByExample((PayQuartzInfoCriteria)bCriteria) ;
            }

            @Override
            public long queryTotal(BaseCriteria bCriteria) {
                return payQuartzInfoMapper.countByExample((PayQuartzInfoCriteria)bCriteria);
            }

            @Override
            public List<Comparator<PayQuartzInfo>> defineComparators() {
                List<Comparator<PayQuartzInfo>> ls = new ArrayList<Comparator<PayQuartzInfo>>();
                ls.add(new Comparator<PayQuartzInfo>(){

                    @Override
                    public int compare(PayQuartzInfo o1, PayQuartzInfo o2) {
                        return o2.getCreateTime().compareTo(o1.getCreateTime());
                    }
                    
                });
                return ls;
            }

            @Override
            public RPayQuartzInfoResponse warpReturnObject(PayQuartzInfo ordMain) {
                RPayQuartzInfoResponse sdoi = new RPayQuartzInfoResponse();
                BeanUtils.copyProperties(ordMain, sdoi);
                return sdoi;
            }

            
        });
      }
    
    
    @Override
    public List<RPayQuartzInfoResponse> queryNotDealZYDigitalOrder(
            RPayQuartzInfoRequest rPayQuartzInfoRequest) {
    	//获取间隔时间
        String secondsStr = BaseParamUtil.fetchParamValue("PAY_QUARTZ_TIME", "2");
        int seconds = Integer.parseInt(secondsStr);
        PayQuartzInfoCriteria example = new PayQuartzInfoCriteria();
        PayQuartzInfoCriteria.Criteria criteria = example.createCriteria();
        criteria.andTaskTypeEqualTo(PayStatus.PAY_TASK_TYPE_03);
        criteria.andDealFlagEqualTo(PayStatus.PAY_DEAL_FLAG_0);
        if(seconds>0){
        	Timestamp time = DateUtil.getSysDate();
        	Calendar calendar=Calendar.getInstance();   
        	calendar.setTime(time); 
        	calendar.add(Calendar.SECOND, -seconds);
        	time = new Timestamp(calendar.getTimeInMillis());
        	criteria.andCreateTimeLessThan(time);
        }
        criteria.andErrorTimesLessThan(10);
        example.setOrderByClause(" UPDATE_TIME,CREATE_TIME ");
        example.setLimitClauseStart(0);
        example.setLimitClauseCount(rPayQuartzInfoRequest.getCount());
        PageResponseDTO<RPayQuartzInfoResponse> pageResponseDTO = queryByPagination(rPayQuartzInfoRequest,example);
        return pageResponseDTO.getResult();
    }
    
    @Override
    public List<RPayQuartzInfoResponse> queryNotDealZYExaminationOrder(
            RPayQuartzInfoRequest rPayQuartzInfoRequest) {
    	//获取间隔时间
        String secondsStr = BaseParamUtil.fetchParamValue("PAY_QUARTZ_TIME", "3");
        int seconds = Integer.parseInt(secondsStr);
        PayQuartzInfoCriteria example = new PayQuartzInfoCriteria();
        PayQuartzInfoCriteria.Criteria criteria = example.createCriteria();
        criteria.andTaskTypeEqualTo(PayStatus.PAY_TASK_TYPE_04);
        criteria.andDealFlagEqualTo(PayStatus.PAY_DEAL_FLAG_0);
        if(seconds>0){
        	Timestamp time = DateUtil.getSysDate();
        	Calendar calendar=Calendar.getInstance();   
        	calendar.setTime(time); 
        	calendar.add(Calendar.SECOND, -seconds);
        	time = new Timestamp(calendar.getTimeInMillis());
        	criteria.andCreateTimeLessThan(time);
        }
        criteria.andErrorTimesLessThan(10);
        example.setOrderByClause(" UPDATE_TIME,CREATE_TIME ");
        example.setLimitClauseStart(0);
        example.setLimitClauseCount(rPayQuartzInfoRequest.getCount());
        PageResponseDTO<RPayQuartzInfoResponse> pageResponseDTO = queryByPagination(rPayQuartzInfoRequest,example);
        return pageResponseDTO.getResult();
    }
	@Override
	public void addExternalMedicareInfo(RPayQuartzInfoRequest payQuartzInfo) {
		PayQuartzInfo record = new PayQuartzInfo();
        ObjectCopyUtil.copyObjValue(payQuartzInfo, record, null, false);
        record.setId(seqPayQuartzInfo.nextValue());
        record.setTaskType(PayStatus.PAY_TASK_TYPE_05);
        record.setErrorTimes(0);
        record.setCreateTime(DateUtil.getSysDate());
        payQuartzInfoMapper.insert(record);
	}
	@Override
	public List<RPayQuartzInfoResponse> queryNotDealExternalMedicareOrder(RPayQuartzInfoRequest rPayQuartzInfoRequest) {
		//获取间隔时间
        String secondsStr = BaseParamUtil.fetchParamValue("PAY_QUARTZ_TIME", "4");
        int seconds = Integer.parseInt(secondsStr);
        PayQuartzInfoCriteria example = new PayQuartzInfoCriteria();
        PayQuartzInfoCriteria.Criteria criteria = example.createCriteria();
        criteria.andTaskTypeEqualTo(PayStatus.PAY_TASK_TYPE_05);
        criteria.andDealFlagEqualTo(PayStatus.PAY_DEAL_FLAG_0);
        if(seconds>0){
        	Timestamp time = DateUtil.getSysDate();
        	Calendar calendar=Calendar.getInstance();   
        	calendar.setTime(time); 
        	calendar.add(Calendar.SECOND, -seconds);
        	time = new Timestamp(calendar.getTimeInMillis());
        	criteria.andCreateTimeLessThan(time);
        }
        criteria.andErrorTimesLessThan(10);
        example.setOrderByClause(" UPDATE_TIME,CREATE_TIME ");
        example.setLimitClauseStart(0);
        example.setLimitClauseCount(rPayQuartzInfoRequest.getCount());
        PageResponseDTO<RPayQuartzInfoResponse> pageResponseDTO = queryByPagination(rPayQuartzInfoRequest,example);
        return pageResponseDTO.getResult();
	}
}
