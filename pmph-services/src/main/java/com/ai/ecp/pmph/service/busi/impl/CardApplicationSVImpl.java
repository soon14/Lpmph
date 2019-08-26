package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.ecp.pmph.dao.mapper.busi.CardApplicationMapper;
import com.ai.ecp.pmph.dao.mapper.busi.CardInfoMapper;
import com.ai.ecp.pmph.dao.model.CardApplication;
import com.ai.ecp.pmph.dao.model.CardApplicationCriteria;
import com.ai.ecp.pmph.dao.model.CardApplicationCriteria.Criteria;
import com.ai.ecp.pmph.dao.model.CardInfo;
import com.ai.ecp.staff.dao.mapper.busi.CustInfoMapper;
import com.ai.ecp.staff.dao.mapper.common.CustLevelInfoMapper;
import com.ai.ecp.staff.dao.model.CustInfo;
import com.ai.ecp.staff.dao.model.CustLevelInfo;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.util.PmphConstants;
import com.ai.ecp.pmph.service.busi.interfaces.ICardApplicationSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICardMgrSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.db.sequence.Sequence;
/**
 * 
 * Project Name:ecp-services-staff <br>
 * Description:会员卡申请管理实现类 <br>
 * Date:2015-10-26下午7:42:48  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public class CardApplicationSVImpl extends GeneralSQLSVImpl implements ICardApplicationSV{

    @Resource
    private CardApplicationMapper cardApplicationMapper;
    
    @Resource
    private CustLevelInfoMapper custLevelInfoMapper;
    
    @Resource
    private CustInfoMapper custInfoMapper;
    
    @Resource 
    private CardInfoMapper cardInfoMapper;
    
    @Resource(name="seq_cust_card_apply_id")
    private Sequence sequence;
    
    @Resource
    private ICardMgrSV cardMgrSV;
    
    @Override
    public PageResponseDTO<CardApplicationResDTO> listCardApplication(CardApplicationReqDTO req)
            throws BusinessException {
        PageResponseDTO<CardApplicationResDTO> pageInfo = new PageResponseDTO<CardApplicationResDTO>();
        CardApplicationCriteria criteria = new CardApplicationCriteria();
        Criteria sql = criteria.createCriteria();
        //查询条件：会员等级
        if (StringUtil.isNotBlank(req.getCustLevelCode())) {
            sql.andCustLevelCodeEqualTo(req.getCustLevelCode());
        }
        //查询条件：用户ID
        if(req.getStaffId()!=null)
        {
            sql.andStaffIdEqualTo(req.getStaffId());
        }
        //查询条件：开始时间
        if (StringUtil.isNotEmpty(req.getSelTimeFrom())) {
            sql.andCreateTimeGreaterThanOrEqualTo(req.getSelTimeFrom());
        }
        //查询条件：结束时间
        if (StringUtil.isNotEmpty(req.getSelTimeEnd())) {
            sql.andCreateTimeLessThanOrEqualTo(req.getSelTimeEnd());
        }
        //查询条件：审核状态
        if (StringUtil.isNotBlank(req.getCheckStatus())) {
            sql.andCheckStatusEqualTo(req.getCheckStatus());
        }
        //查询条件：联系人姓名
        if (StringUtil.isNotBlank(req.getContactName())) {
            sql.andContactNameLike("%" + req.getContactName() + "%");
        }
        //查询条件：联系人手机
        if (StringUtil.isNotBlank(req.getContactPhone())) {
            sql.andContactPhoneLike("%" + req.getContactPhone() + "%");
        }
        pageInfo.setPageNo(req.getPageNo());
        pageInfo.setPageSize(req.getPageSize());
        //设置查询的开始及终止的rownum
        criteria.setLimitClauseStart(pageInfo.getStartRowIndex());
        criteria.setLimitClauseCount(pageInfo.getPageSize());
        criteria.setOrderByClause(" create_time desc");
        pageInfo = super.queryByPagination(req, criteria, true, new PaginationCallback<CardApplication, CardApplicationResDTO>() {

            @Override
            public List<CardApplication> queryDB(BaseCriteria bc) {
                return cardApplicationMapper.selectByExample((CardApplicationCriteria)bc);
            }

            @Override
            public long queryTotal(BaseCriteria bc) {
                return cardApplicationMapper.countByExample((CardApplicationCriteria)bc);
            }

            @Override
            public List<Comparator<CardApplication>> defineComparators() {
                List<Comparator<CardApplication>> ls=new ArrayList<Comparator<CardApplication>>();
                ls.add(new Comparator<CardApplication>(){

                    @Override
                    public int compare(CardApplication o1, CardApplication o2) {
                        return o1.getCreateTime().getTime()<o2.getCreateTime().getTime()?1:-1;
                    }
                    
                });
                return ls;
            }
            @Override
            public CardApplicationResDTO warpReturnObject(CardApplication cardApplication) {
                CardApplicationResDTO res = new CardApplicationResDTO();
                ObjectCopyUtil.copyObjValue(cardApplication, res, null, true);
                if (StringUtil.isNotBlank(res.getCustLevelCode())) {
                    CustLevelInfo level = custLevelInfoMapper.selectByPrimaryKey(cardApplication.getCustLevelCode());
                    if (level != null) {
                        res.setCustLevelName(level.getCustLevelName());
                    }
                }
                if (res.getStaffId() != null && res.getStaffId() != 0) {
                    CustInfo custInfo = custInfoMapper.selectByPrimaryKey(res.getStaffId());
                    if (custInfo != null) {
                        res.setStaffName(custInfo.getCustName());
                        //这里不改字段名了，用昵称字段来显示登录工号
                        res.setNickName(custInfo.getStaffCode());
                    }
                }
                return res;
            }
        });
        return pageInfo;
    }

    @Override
    public int updateCardApplication(CardApplicationReqDTO req) throws BusinessException {
        CardApplication cardApp = new CardApplication();
        ObjectCopyUtil.copyObjValue(req, cardApp, null, false);
        cardApp.setUpdateTime(DateUtil.getSysDate());
        return cardApplicationMapper.updateByPrimaryKeySelective(cardApp);
    }

    @Override
    public int saveCardAppPass(CardApplicationReqDTO req,CardBindReqDTO bindReq) throws BusinessException {
        /*1、更新会员卡申请表*/
        this.updateCardApplication(req);
        //查询出联系人信息
        CardApplication cardApp = cardApplicationMapper.selectByPrimaryKey(req.getId());
        if (cardApp != null) {
            bindReq.setContactName(cardApp.getContactName());
            bindReq.setContactPhone(cardApp.getContactPhone());
            bindReq.setContactAddress(cardApp.getContactAddress());
        }
        //查询出群组信息
        if (StringUtil.isNotBlank(req.getCustCardId())) {
            CardInfo card = cardInfoMapper.selectByPrimaryKey(req.getCustCardId());
            if (card != null) {
                bindReq.setCardGroup(card.getCardGroup());
            }
        }
        /*2、绑定会员卡及一系列操作*/
        cardMgrSV.saveCardBindOpt(bindReq);
        return 1;
    }

    @Override
    public boolean isExistApplyRecordByStaff(CustInfo custinfo) throws BusinessException {
        
        CardApplicationCriteria criteria = new CardApplicationCriteria();
        criteria.createCriteria().andStaffIdEqualTo(custinfo.getId()).andCheckStatusEqualTo(PmphConstants.Card.CHECK_STATUS_0);
        long count = 0;
        try {
            count = cardApplicationMapper.countByExample(criteria);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
        
        return count>0?true:false;
    }

    @Override
    public int saveCardApplicationRequest(CardApplicationReqDTO req) throws BusinessException {
        
        CardApplication record = new CardApplication();
        
        ObjectCopyUtil.copyObjValue(req, record, null, false);
        record.setId(sequence.nextValue());
        record.setCheckStatus(PmphConstants.Card.CHECK_STATUS_0);
        record.setCreateStaff(req.getStaff().getId());
        record.setCreateTime(DateUtil.getSysDate());
        record.setUpdateStaff(req.getStaff().getId());
        record.setUpdateTime(DateUtil.getSysDate());
        try {
            cardApplicationMapper.insertSelective(record);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
        return 0;
    }

}

