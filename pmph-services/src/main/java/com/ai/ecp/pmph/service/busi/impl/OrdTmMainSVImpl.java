/**
 * 
 */
package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.pmph.dao.mapper.busi.OrdMainTMMapper;
import com.ai.ecp.pmph.dao.model.OrdMainTM;
import com.ai.ecp.pmph.dao.model.OrdMainTMCriteria;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainResp;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdTmMainSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * 
 * Project Name:ecp-services-order-server <br>
 * Description: <br>
 * Date:2016年2月23日下午4:50:08  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Service("ordTmMainSV")
public class OrdTmMainSVImpl extends GeneralSQLSVImpl implements IOrdTmMainSV {
    
    @Resource
    private OrdMainTMMapper ordMainTMMapper;

    private static final String MODULE = OrdTmMainSVImpl.class.getName();

    @Override
    public PageResponseDTO<ROrdTmMainResp> queryTmOrderMain(ROrdTmMainReq rOrdTmMainReq) throws BusinessException {
        
        PageResponseDTO<ROrdTmMainResp> pageResponse = PageResponseDTO
                .buildByBaseInfo(rOrdTmMainReq, ROrdTmMainResp.class);
        pageResponse.setResult(new ArrayList<ROrdTmMainResp>());
        
        PageResponseDTO<ROrdTmMainResp> psdoi = queryTmOrder(rOrdTmMainReq);
        pageResponse.setCount(psdoi.getCount());
        pageResponse.setPageCount(psdoi.getPageCount());
        if (CollectionUtils.isEmpty(psdoi.getResult())) {
            pageResponse.setResult(null);
            return pageResponse;
        } 
        pageResponse.getResult().addAll(psdoi.getResult());
        return pageResponse;
    }
    
    @Override
    public List<ROrdTmMainResp> queryOrderTmMainNoGift(ROrdTmMainReq rOrdTmMainReq) throws BusinessException {
        List<ROrdTmMainResp> rOrdTmMainResps = new ArrayList<ROrdTmMainResp>();
        OrdMainTMCriteria ordMainTMCriteria=new OrdMainTMCriteria();
        OrdMainTMCriteria.Criteria ca = ordMainTMCriteria.createCriteria();
        ca.andImportTimeGreaterThanOrEqualTo(rOrdTmMainReq.getCreateTime()).andRwStaffCodeIsNotNull().andRwScoreFlagEqualTo("0").andStatusEqualTo("交易成功"); 
        List<OrdMainTM> ordMainTMs = this.ordMainTMMapper.selectByExample(ordMainTMCriteria);
        for (OrdMainTM ordMainTM : ordMainTMs) {
            ROrdTmMainResp rOrdTmMainResp = new ROrdTmMainResp();
            ObjectCopyUtil.copyObjValue(ordMainTM, rOrdTmMainResp, "", false);
            rOrdTmMainResps.add(rOrdTmMainResp);
        }
        return rOrdTmMainResps;
    }
    
    @Override
    public OrdMainTM queryTmOrderMainInfo(ROrdTmMainReq rOrdTmMainReq) throws BusinessException{
        
        OrdMainTMCriteria ordMainTMCriteria=new OrdMainTMCriteria();
        ordMainTMCriteria.createCriteria().andOrderCodeEqualTo(rOrdTmMainReq.getOrderCode())
            .andTmStaffCodeEqualTo(rOrdTmMainReq.getTmStaffCode())
            .andStatusEqualTo("交易成功");       
        List<OrdMainTM> ordMainTMs=this.ordMainTMMapper.selectByExample(ordMainTMCriteria);
        if(ordMainTMs.size()>0){
            //如果输入的天猫账号或订单号已绑定过其他用户，则不能绑定，提示错误
            OrdMainTMCriteria ordMainTMCriteria1=new OrdMainTMCriteria();
            ordMainTMCriteria1.createCriteria().andOrderCodeEqualTo(rOrdTmMainReq.getOrderCode())
                .andRwStaffCodeIsNotNull();
            List<OrdMainTM> omts = this.ordMainTMMapper.selectByExample(ordMainTMCriteria1);
            if(CollectionUtils.isNotEmpty(omts)){
                return null;
            }
            OrdMainTMCriteria ordMainTMCriteria2=new OrdMainTMCriteria();
            ordMainTMCriteria2.createCriteria().andTmStaffCodeEqualTo(rOrdTmMainReq.getTmStaffCode())
                .andRwStaffCodeIsNotNull();
            List<OrdMainTM> omts2 = this.ordMainTMMapper.selectByExample(ordMainTMCriteria2);
            if(CollectionUtils.isNotEmpty(omts2)){
                return null;
            }
            
            return ordMainTMs.get(0);
        }
        
        return null;
    }
    
    @Override
    public void updateOrderTmMainScore(ROrdTmMainReq rOrdTmMainReq) throws BusinessException{
        OrdMainTMCriteria ordMainTMCriteria=new OrdMainTMCriteria();
        ordMainTMCriteria.createCriteria().andOrderCodeEqualTo(rOrdTmMainReq.getOrderCode());
        OrdMainTM ordMainTM = new OrdMainTM();
        ordMainTM.setRwScoreFlag(rOrdTmMainReq.getRwScoreFlag());
        ordMainTM.setRwScore(rOrdTmMainReq.getRwScore()); 
        this.ordMainTMMapper.updateByExampleSelective(ordMainTM, ordMainTMCriteria);
    }
    
    @Override
    public void updateOrderTmMainStaff(ROrdTmMainReq rOrdTmMainReq) throws BusinessException{
       
        
        //清空原先已绑定的不同天猫账号
        OrdMainTMCriteria omt =new OrdMainTMCriteria();
        omt.createCriteria().andRwStaffIdEqualTo(rOrdTmMainReq.getRwStaffId())
                            .andRwStaffCodeEqualTo(rOrdTmMainReq.getRwStaffCode());
/*                            .andTmStaffCodeNotEqualTo(rOrdTmMainReq.getOrderCode());*/
        OrdMainTM oTM = new OrdMainTM();
        oTM.setRwStaffCode("");
        this.ordMainTMMapper.updateByExampleSelective(oTM, omt);
        
        //正常业务
        OrdMainTMCriteria ordMainTMCriteria=new OrdMainTMCriteria();
        ordMainTMCriteria.createCriteria().andTmStaffCodeEqualTo(rOrdTmMainReq.getTmStaffCode());
/*        ordMainTMCriteria.createCriteria().andOrderCodeEqualTo(rOrdTmMainReq.getOrderCode());*/
        OrdMainTM ordMainTM = new OrdMainTM();
        ordMainTM.setRwStaffId(rOrdTmMainReq.getRwStaffId()); 
        ordMainTM.setRwStaffCode(rOrdTmMainReq.getRwStaffCode());
        this.ordMainTMMapper.updateByExampleSelective(ordMainTM, ordMainTMCriteria);
    }
    
    /**
     * 
     * queryTmOrder:(全表查询天猫订单主表). <br/>
     * 
     * @param rOrdTmMainReq
     * @return 
     * @since JDK 1.6
     */
    private PageResponseDTO<ROrdTmMainResp> queryTmOrder(ROrdTmMainReq rOrdTmMainReq) {
        
        OrdMainTMCriteria omsic = createCriteria(rOrdTmMainReq);
        
        return super.queryByPagination(rOrdTmMainReq, omsic, true,
                new PaginationCallback<OrdMainTM, ROrdTmMainResp>() {

                    @Override
                    public List<OrdMainTM> queryDB(BaseCriteria bCriteria) {
                        return ordMainTMMapper.selectByExample((OrdMainTMCriteria) bCriteria);
                    }

                    @Override
                    public long queryTotal(BaseCriteria bCriteria) {
                        return ordMainTMMapper.countByExample((OrdMainTMCriteria) bCriteria);
                    }

                    @Override
                    public List<Comparator<OrdMainTM>> defineComparators() {
                        List<Comparator<OrdMainTM>> ls = new ArrayList<Comparator<OrdMainTM>>();
                        ls.add(new Comparator<OrdMainTM>(){

                            @Override
                            public int compare(OrdMainTM o1, OrdMainTM o2) {
                                return o2.getUpdateTime().compareTo(o1.getUpdateTime());
                            }
                            
                        });
                        return ls;
                    }

                    @Override
                    public ROrdTmMainResp warpReturnObject(OrdMainTM ordMainTM) {
                        ROrdTmMainResp sdoi = new ROrdTmMainResp();
                        BeanUtils.copyProperties(ordMainTM, sdoi);
                        return sdoi;
                    }
                });
    }
    
    /**
     * 
     * createCriteria:(查询条件). <br/> 
     * 
     * @param rQueryOrderRequest
     * @return 
     * @since JDK 1.6
     */
    private OrdMainTMCriteria createCriteria(ROrdTmMainReq rOrdTmMainReq){
        
        OrdMainTMCriteria omsic = new OrdMainTMCriteria(); 
        omsic.setLimitClauseCount(rOrdTmMainReq.getPageSize());
        omsic.setLimitClauseStart(rOrdTmMainReq.getStartRowIndex());
        omsic.setOrderByClause("update_time desc");
        OrdMainTMCriteria.Criteria ca = omsic.createCriteria();
        if(rOrdTmMainReq.getBegDate() != null){
            ca.andImportTimeGreaterThanOrEqualTo(rOrdTmMainReq.getBegDate());
        }
        if(rOrdTmMainReq.getEndDate() != null){
            ca.andImportTimeLessThanOrEqualTo(rOrdTmMainReq.getEndDate());
        }
        if(StringUtil.isNotBlank(rOrdTmMainReq.getOrderCode())){
            ca.andOrderCodeEqualTo(rOrdTmMainReq.getOrderCode());
        }
        if (StringUtil.isNotEmpty(rOrdTmMainReq.getRwStaffCode())) {
            ca.andRwStaffCodeEqualTo(rOrdTmMainReq.getRwStaffCode());
        }
        if (StringUtil.isNotEmpty(rOrdTmMainReq.getTmStaffCode())) {
            ca.andTmStaffCodeEqualTo(rOrdTmMainReq.getTmStaffCode());
        }
        if (StringUtil.isNotEmpty(rOrdTmMainReq.getContractName())) {
            ca.andContractNameEqualTo(rOrdTmMainReq.getContractName());
        }
        if (StringUtil.isNotEmpty(rOrdTmMainReq.getStatus())) {
            ca.andStatusEqualTo(rOrdTmMainReq.getStatus());
        }
        if (StringUtil.isNotEmpty(rOrdTmMainReq.getRwScoreFlag())) {
            ca.andRwScoreFlagEqualTo(rOrdTmMainReq.getRwScoreFlag());
        }
         
        return omsic;
    }

    @Override
    public OrdMainTM queryTMOrderByOrderId(String orderId) throws BusinessException {
        OrdMainTMCriteria ordMainTMCriteria=new OrdMainTMCriteria();
        ordMainTMCriteria.createCriteria().andOrderCodeEqualTo(orderId);
        List<OrdMainTM> ordMainTMs=this.ordMainTMMapper.selectByExample(ordMainTMCriteria);
        if(ordMainTMs.size()>0){
            return ordMainTMs.get(0);
        }
        return null;
    }  
}
