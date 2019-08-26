package com.ai.ecp.pmph.service.busi.impl;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.order.dao.mapper.busi.OrdBackShopIdxMapper;
import com.ai.ecp.order.dao.model.OrdBackApply;
import com.ai.ecp.order.dao.model.OrdBackShopIdx;
import com.ai.ecp.order.dao.model.OrdBackShopIdxCriteria;
import com.ai.ecp.order.dubbo.dto.RBackApplyResp;
import com.ai.ecp.order.dubbo.dto.ROrderBackReq;
import com.ai.ecp.order.dubbo.util.BackConstants;
import com.ai.ecp.order.service.busi.impl.OrdBackShopIdxSVImpl;
import com.ai.ecp.order.service.busi.interfaces.IOrdBackShopIdxSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.db.sequence.Sequence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrdBackShopIdxPmphSVImpl extends OrdBackShopIdxSVImpl{
    
    @Resource
    private OrdBackShopIdxMapper ordBackShopIdxMapper;
    

    private static final String MODULE = OrdBackShopIdxPmphSVImpl.class.getName();

    @Override
    public PageResponseDTO<RBackApplyResp> queryBackGdsByShop(ROrderBackReq rOrderBackReq)
            throws BusinessException {
        OrdBackShopIdxCriteria obasic = new OrdBackShopIdxCriteria();
        obasic.setLimitClauseCount(rOrderBackReq.getPageSize());
        obasic.setLimitClauseStart(rOrderBackReq.getStartRowIndex());
        obasic.setOrderByClause("apply_time desc");
        OrdBackShopIdxCriteria.Criteria ca = obasic.createCriteria()
                                                   .andShopIdEqualTo(rOrderBackReq.getShopId());
        if(rOrderBackReq.getBegDate() != null){
            ca.andApplyTimeGreaterThan(rOrderBackReq.getBegDate());
        }
        if(rOrderBackReq.getEndDate() != null){
            ca.andApplyTimeLessThanOrEqualTo(rOrderBackReq.getEndDate());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getApplyType())){
            ca.andApplyTypeEqualTo(rOrderBackReq.getApplyType());
        }
        if(rOrderBackReq.getSiteId()!= null){
            ca.andSiteIdEqualTo(rOrderBackReq.getSiteId());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getOrderId())){
            ca.andOrderIdEqualTo(rOrderBackReq.getOrderId().trim());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getPayTranNo())){
            ca.andPayTranNoEqualTo(rOrderBackReq.getPayTranNo().trim());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getIsCompensate())){
            ca.andIsCompenstateEqualTo(rOrderBackReq.getIsCompensate());
        }
        if(!CollectionUtils.isEmpty(rOrderBackReq.getOrderIds())){
        	ca.andOrderIdIn(rOrderBackReq.getOrderIds());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getTabFlag())){
            if("00".equals(rOrderBackReq.getTabFlag())){
                List<String> status = new ArrayList<String>();
                status.add(BackConstants.Status.APPLY);
                status.add(BackConstants.Status.REVIEW_PASS);
                status.add(BackConstants.Status.SEND);
                status.add("06");  //二级审核
                status.add("07");  //三级审核
                ca.andStatusIn(status);
            } else if("02".equals(rOrderBackReq.getTabFlag())){
                List<String> status = new ArrayList<String>();
                status.add(BackConstants.Status.WAIT_REFUND);
                ca.andStatusIn(status);
            } else {
                List<String> status = new ArrayList<String>();
                status.add(BackConstants.Status.REFUNDED);
                status.add(BackConstants.Status.REFUSE);
                ca.andStatusIn(status);
            }
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getStatus())){
            ca.andStatusEqualTo(rOrderBackReq.getStatus());
        }
        return super.queryByPagination(rOrderBackReq, obasic, true, new PaginationCallback<OrdBackShopIdx, RBackApplyResp>() {

            @Override
            public List<OrdBackShopIdx> queryDB(BaseCriteria bCriteria) {
                return ordBackShopIdxMapper.selectByExample((OrdBackShopIdxCriteria)bCriteria) ;
            }

            @Override
            public long queryTotal(BaseCriteria bCriteria) {
                return ordBackShopIdxMapper.countByExample((OrdBackShopIdxCriteria)bCriteria);
            }

            @Override
            public List<Comparator<OrdBackShopIdx>> defineComparators() {
                List<Comparator<OrdBackShopIdx>> ls = new ArrayList<Comparator<OrdBackShopIdx>>();
                ls.add(new Comparator<OrdBackShopIdx>(){
                    @Override
                    public int compare(OrdBackShopIdx o1, OrdBackShopIdx o2) {
                        return o2.getApplyTime().compareTo(o1.getApplyTime());
                    }
                });
                return ls;
            }

            @Override
            public RBackApplyResp warpReturnObject(OrdBackShopIdx ordBackApplyShopIdx) {
                RBackApplyResp sdoi = new RBackApplyResp();
                BeanUtils.copyProperties(ordBackApplyShopIdx, sdoi);
                return sdoi;
            }
        });
    }
	@Override
	public List<OrdBackShopIdx> queryBackGdsByShopNp(ROrderBackReq rOrderBackReq)
			throws BusinessException {
		OrdBackShopIdxCriteria obasic = new OrdBackShopIdxCriteria();
        /*obasic.setLimitClauseCount(rOrderBackReq.getPageSize());
        obasic.setLimitClauseStart(rOrderBackReq.getStartRowIndex());*/
        obasic.setOrderByClause("apply_time desc");
        OrdBackShopIdxCriteria.Criteria ca = obasic.createCriteria()
                                                   .andShopIdEqualTo(rOrderBackReq.getShopId());
        if(rOrderBackReq.getBegDate() != null){
            ca.andApplyTimeGreaterThan(rOrderBackReq.getBegDate());
        }
        if(rOrderBackReq.getEndDate() != null){
            ca.andApplyTimeLessThanOrEqualTo(rOrderBackReq.getEndDate());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getApplyType())){
            ca.andApplyTypeEqualTo(rOrderBackReq.getApplyType());
        }
        if(rOrderBackReq.getSiteId()!= null){
            ca.andSiteIdEqualTo(rOrderBackReq.getSiteId());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getOrderId())){
            ca.andOrderIdEqualTo(rOrderBackReq.getOrderId().trim());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getPayTranNo())){
            ca.andPayTranNoEqualTo(rOrderBackReq.getPayTranNo().trim());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getIsCompensate())){
            ca.andIsCompenstateEqualTo(rOrderBackReq.getIsCompensate());
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getTabFlag())){
            if("00".equals(rOrderBackReq.getTabFlag())){
                List<String> status = new ArrayList<String>();
                status.add(BackConstants.Status.APPLY);
                status.add(BackConstants.Status.REVIEW_PASS);
                status.add(BackConstants.Status.SEND);
                status.add("06");  //二级审核
                status.add("07");  //三级审核
                ca.andStatusIn(status);
            } else if("02".equals(rOrderBackReq.getTabFlag())){
                List<String> status = new ArrayList<String>();
                status.add(BackConstants.Status.WAIT_REFUND);
                ca.andStatusIn(status);
            } else {
                List<String> status = new ArrayList<String>();
                status.add(BackConstants.Status.REFUNDED);
                status.add(BackConstants.Status.REFUSE);
                ca.andStatusIn(status);
            }
        }
        if(StringUtil.isNotBlank(rOrderBackReq.getStatus())){
            ca.andStatusEqualTo(rOrderBackReq.getStatus());
        }
        List<OrdBackShopIdx> result = ordBackShopIdxMapper.selectByExample(obasic);
		return result;
	}
}

