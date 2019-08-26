package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.server.front.dto.BaseParamDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.ecp.pmph.dao.mapper.busi.CardBindMapper;
import com.ai.ecp.pmph.dao.mapper.busi.CardImportTempMapper;
import com.ai.ecp.pmph.dao.mapper.busi.CardInfoMapper;
import com.ai.ecp.pmph.dao.model.CardBind;
import com.ai.ecp.pmph.dao.model.CardBindCriteria;
import com.ai.ecp.pmph.dao.model.CardImportTemp;
import com.ai.ecp.pmph.dao.model.CardImportTempCriteria;
import com.ai.ecp.pmph.dao.model.CardInfo;
import com.ai.ecp.pmph.dao.model.CardInfoCriteria;
import com.ai.ecp.pmph.dao.model.CardInfoCriteria.Criteria;
import com.ai.ecp.staff.dao.model.CustGrowInfo;
import com.ai.ecp.staff.dao.model.CustInfo;
import com.ai.ecp.staff.dao.model.CustLevelRule;
import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustLevelInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustLevelInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustLevelRuleReqDTO;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.pmph.service.busi.interfaces.ICardMgrSV;
import com.ai.ecp.staff.service.busi.custinfo.interfaces.ICustInfoSV;
import com.ai.ecp.staff.service.busi.manage.interfaces.ICustManageSV;
import com.ai.ecp.staff.service.common.vip.interfaces.ICustVipSV;
import com.ai.ecp.staff.service.util.StaffTools;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.db.sequence.Sequence;

/**
 * 
 * Project Name:ecp-services-staff <br>
 * Description: 会员卡管理接口实现类<br>
 * Date:2015-10-26下午8:27:34  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public class CardMgrSVImpl extends GeneralSQLSVImpl implements ICardMgrSV{

    @Resource
    private CardInfoMapper cardInfoMapper;
    
    @Resource
    private CardBindMapper cardBindMapper;
    
    @Resource
    private ICustVipSV custVipSV;
    
    @Resource
    private ICustManageSV custManageSV;
    
    @Resource
    private ICustInfoSV custInfoSV;
    
    @Resource
    private CardImportTempMapper cardImportTempMapper;
    
    @Resource(name="seq_staff_card_id")
    private Sequence sequence;  
    
    @Resource(name="seq_cust_card_bind_id")
    private Sequence seq_cust_card_bind_id;
    
    private static String MODULE = CardMgrSVImpl.class.getName();
    
    @Override
    public PageResponseDTO<CardInfoResDTO> listCardInfo(CardInfoReqDTO req)
            throws BusinessException {
        PageResponseDTO<CardInfoResDTO> pageInfo = new PageResponseDTO<CardInfoResDTO>();
        CardInfoCriteria criteria = new CardInfoCriteria();
        criteria.setOrderByClause("create_time desc");
        Criteria sql = criteria.createCriteria();
        //查询条件：会员等级
        if (StringUtil.isNotBlank(req.getCustLevelCode())) {
            sql.andCustLevelCodeEqualTo(req.getCustLevelCode());
        }
        //查询条件：状态
        if (StringUtil.isNotBlank(req.getCardStatus())) {
            sql.andCardStatusEqualTo(req.getCardStatus());
        }
        //查询条件：会员卡
        if (StringUtil.isNotBlank(req.getCustCardId())) {
            sql.andCustCardIdLike("%" + req.getCustCardId() + "%");
        }
        //查询条件：所属群组
        if (StringUtil.isNotBlank(req.getCardGroup())) {
            sql.andCardGroupLike("%" + req.getCardGroup() + "%");
        }
        pageInfo.setPageNo(req.getPageNo());
        pageInfo.setPageSize(req.getPageSize());
        //设置查询的开始及终止的rownum
        criteria.setLimitClauseStart(pageInfo.getStartRowIndex());
        criteria.setLimitClauseCount(pageInfo.getPageSize());
        pageInfo = super.queryByPagination(req, criteria, true, new PaginationCallback<CardInfo, CardInfoResDTO>() {

            @Override
            public List<CardInfo> queryDB(BaseCriteria bc) {
                return cardInfoMapper.selectByExample((CardInfoCriteria)bc);
            }

            @Override
            public long queryTotal(BaseCriteria bc) {
                return cardInfoMapper.countByExample((CardInfoCriteria)bc);
            }

            @Override
            public List<Comparator<CardInfo>> defineComparators() {
                List<Comparator<CardInfo>> ls=new ArrayList<Comparator<CardInfo>>();
                ls.add(new Comparator<CardInfo>(){

                    @Override
                    public int compare(CardInfo o1, CardInfo o2) {
                        return o1.getCreateTime().getTime()<o2.getCreateTime().getTime()?1:-1;
                    }
                    
                });
                return ls;
            }
            @Override
            public CardInfoResDTO warpReturnObject(CardInfo cardInfo) {
                CardInfoResDTO res = new CardInfoResDTO();
                ObjectCopyUtil.copyObjValue(cardInfo, res, null, true);
                BaseParamDTO dto = BaseParamUtil.fetchParamDTO("STAFF_CUST_LEVEL", res.getCustLevelCode());
                if(null!=dto){
                res.setCustLevelName(dto.getSpValue());
                }

                return res;
            }
        });
        return pageInfo;
    }

    @Override
    public int updateCardInfo(CardInfoReqDTO req) throws BusinessException {
        CardInfo cardInfo = new CardInfo();
        ObjectCopyUtil.copyObjValue(req, cardInfo, null, false);
        cardInfo.setUpdateTime(DateUtil.getSysDate());
        return cardInfoMapper.updateByPrimaryKeySelective(cardInfo);
    }

    @Override
    public int saveCardBindOpt(CardBindReqDTO req) throws BusinessException {
        /*1、绑定会员卡*/
        this.saveCardBind(req);
        
        /*2、更新会员等级成长值(如果用户当前成长值大于会员卡等级查出的初始值，则不更新),更新会员等级，更新会员信息表中的会员卡号信息*/
        /*2-1、查询变更会员等级后的初始成长值*/
        CustInfoReqDTO custInfo = new CustInfoReqDTO();
        CustLevelRuleReqDTO ruleInfo = new CustLevelRuleReqDTO();
        ruleInfo.setCustLevelCode(req.getBindCustLevelCode());
        CustLevelRule levelRule = custVipSV.queryCustLevelMinValueAndMaxValue(ruleInfo);
        CustInfoResDTO custRes = custManageSV.findCustInfoById(req.getStaffId());
        if (levelRule != null && custRes != null) {
            //用户当前的成长值 小于 申请的会员卡的初始值时，进行成长值的变更
            if (custRes.getCustGrowValue().longValue() < levelRule.getMinValue().longValue()) {
                //设置对应会员等级的成长值
                custInfo.setCustGrowValue(levelRule.getMinValue().longValue());
                custInfo.setCustLevelCode(req.getBindCustLevelCode());//会员等级
            }
        }
        custInfo.setId(req.getStaffId());
        custInfo.setUpdateTime(DateUtil.getSysDate());
        custInfo.setCustCardId(req.getCustCardId());//会员卡号
        /*2-2、更新会员信息*/
        int result = custManageSV.updateCustInfo(custInfo);
        
        /*3、保存会员信息变更日志*/
        if(result > 0){
            CustInfoResDTO res = custManageSV.findCustInfoById(req.getStaffId());
            CustInfo log = new CustInfo();
            ObjectCopyUtil.copyObjValue(res, log, null, false);
            if (StringUtil.isNotBlank(custInfo.getCustLevelCode())) {
                log.setCustLevelCode(req.getBindCustLevelCode());
                log.setCustGrowValue(levelRule.getMinValue().longValue());
            }
            log.setCustCardId(req.getCustCardId());
            log.setUpdateTime(DateUtil.getSysDate());
            log.setUpdateStaff(req.getUpdateStaff());
            StaffTools.logInfo("saveCustInfoLog", log);
        }
        
        /*4、变更会员卡状态信息*/
        CardInfoReqDTO cardInfo = new CardInfoReqDTO();
        cardInfo.setCustCardId(req.getCustCardId());//会员卡号
        cardInfo.setCardStatus(StaffConstants.Card.CUST_CARD_SEND);//已发卡
        cardInfo.setUpdateStaff(req.getUpdateStaff());//更新操作人
        this.updateCardInfo(cardInfo);
        
        
        /*5、如果会员等级有升级，需要清空原来的成长值来源（以免计算时重复累加）*/
        if (StringUtil.isNotBlank(custInfo.getCustLevelCode())) {
            CustGrowInfo custGrowInfo = new CustGrowInfo();
            custGrowInfo.setStaffId(req.getStaffId());
            custGrowInfo.setStatus("0");//状态置为失效
            custGrowInfo.setUpdateStaff(req.getStaffId());
            custGrowInfo.setUpdateTime(DateUtil.getSysDate());
            custInfoSV.updateCustGrowInfo(custGrowInfo);
        }
        /*6、如果会员等级有升级，则需要把变更后等级的初始成长值，记录到成长值来源表中*/
        if (StringUtil.isNotBlank(custInfo.getCustLevelCode())) {
            CustGrowInfo crowInfo = new CustGrowInfo();
            crowInfo.setStaffId(req.getStaffId());
            crowInfo.setGrowValue(levelRule.getMinValue().longValue());
            crowInfo.setRemark("通过会员卡绑定，会员等级变更，成长值设为该等级的初始成长值。");
            crowInfo.setCreateStaff(req.getStaffId());
            crowInfo.setCreateTime(DateUtil.getSysDate());
            crowInfo.setUpdateStaff(req.getStaffId());
            crowInfo.setUpdateTime(DateUtil.getSysDate());
            crowInfo.setStatus("1");
            custInfoSV.insertCustGrowInfo(crowInfo);
        }
        
        return result;
    }

    @Override
    public int saveCardInfo(CardInfoReqDTO req) throws BusinessException {
        
        CustLevelInfoReqDTO levelreq = new CustLevelInfoReqDTO();
        levelreq.setCustLevelCode(req.getCustLevelCode());
        CustLevelInfoResDTO dto =  custVipSV.queryCustLevelInfo(levelreq);
        int s =0;
        for (int i = 0; i < req.getRow().longValue(); i++) {
            String f = "%0" + 8 + "d";
            String seq = String.format(f, sequence.nextValue());
            int n = (int)(Math.random()*100000);
            String s1 = "%0" + 5 + "d";
            String s2 = String.format(s1, n);
            seq = dto.getCustCardNum()+seq+s2;
            CardInfo info = new CardInfo();
            info.setCustCardId(seq);
            info.setCardStatus(StaffConstants.Card.CUST_CARD_NO_SEND);
            info.setCustLevelCode(req.getCustLevelCode());
            info.setCardGroup(req.getCardGroup());
            info.setCreateTime(DateUtil.getSysDate());
            info.setCreateStaff(req.getStaff().getId());
            info.setUpdateTime(DateUtil.getSysDate());
            info.setUpdateStaff(req.getStaff().getId());
            try {
                cardInfoMapper.insert(info);
                s=i+1;
            } catch (Exception e) {
                LogUtil.error(MODULE, "生成"+seq+"失败", e);
                throw new BusinessException(StaffConstants.Card.BULID_CARD_ID_ERROR,new String[]{"seq"});
            }
            
        }
       
        return s;
    }
    /**
     * 
     * saveCardBind:(保存绑定会员卡信息). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public int saveCardBind(CardBindReqDTO req) throws BusinessException {
        CardBind cardBind = new CardBind();
        ObjectCopyUtil.copyObjValue(req, cardBind, null, false);
        cardBind.setId(seq_cust_card_bind_id.nextValue());
        cardBind.setCreateTime(DateUtil.getSysDate());
        cardBind.setUpdateTime(DateUtil.getSysDate());
        cardBind.setBindTime(DateUtil.getSysDate());
        return cardBindMapper.insert(cardBind);
    }

    public PageResponseDTO<CardBindResDTO> listCardBindPage(CardBindReqDTO req)throws BusinessException
    {
        CardBindCriteria criteria = new CardBindCriteria();
        com.ai.ecp.pmph.dao.model.CardBindCriteria.Criteria sql = criteria.createCriteria();
        criteria.setLimitClauseStart(req.getStartRowIndex());
        criteria.setLimitClauseCount(req.getPageSize());
        criteria.setOrderByClause("create_time desc");

        if(req.getStaffId() != null)
        {
            sql.andStaffIdEqualTo(req.getStaffId());
        }
        return super.queryByPagination(req, criteria, true, new PaginationCallback<CardBind, CardBindResDTO>() {

            @Override
            public List<CardBind> queryDB(BaseCriteria criteria) {
                return cardBindMapper.selectByExample((CardBindCriteria)criteria);
            }

            @Override
            public long queryTotal(BaseCriteria criteria) {
                return cardBindMapper.countByExample((CardBindCriteria)criteria);
            }
            @Override
            public List<Comparator<CardBind>> defineComparators() {
                List<Comparator<CardBind>> ls = new ArrayList<Comparator<CardBind>>();
                ls.add(new Comparator<CardBind>() {

                    @Override
                    public int compare(CardBind o1, CardBind o2) {
                        return o1.getCreateTime().getTime() < o2.getCreateTime().getTime()?1:-1;
                    }

                });
                return ls;
            }
            @Override
            public CardBindResDTO warpReturnObject(CardBind arg0) {
                
                CardBindResDTO resDTO = new CardBindResDTO();
                
                ObjectCopyUtil.copyObjValue(arg0, resDTO, null, false);
                CustLevelInfoReqDTO _paramCustLevelInfoReqDTO = new CustLevelInfoReqDTO();
                _paramCustLevelInfoReqDTO.setCustCardNum(resDTO.getCustCardId().substring(0, 2));
                
                CustLevelInfoResDTO custLevelInfoResDTO = custVipSV.queryCustLevelInfo(_paramCustLevelInfoReqDTO);
                if(custLevelInfoResDTO != null)
                {
                    resDTO.setCustCardLevelName(custLevelInfoResDTO.getCustLevelName());
                }
                return resDTO;
            }
        });
    }
    
    @Override
    public CardInfoResDTO findCardInfoByCardId(CardInfoReqDTO req) throws BusinessException {
        CardInfo cardInfo = null;
        try {
            cardInfo = cardInfoMapper.selectByPrimaryKey(req.getCustCardId());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if(cardInfo == null)
        {
            return null;
        }
        CardInfoResDTO resDTO = new CardInfoResDTO();
        
        ObjectCopyUtil.copyObjValue(cardInfo, resDTO, null, false);
        
        return resDTO;
    }

    @Override
    public Long updateCardStatus(CardInfoReqDTO cardInfoReqDTO,Long importId) throws BusinessException {
       
       
            CardInfo info =   cardInfoMapper.selectByPrimaryKey(cardInfoReqDTO.getCustCardId());
            if(null==info||StringUtil.isBlank(info.getCustCardId())){
                saveTempCard(cardInfoReqDTO,"该卡不存在",importId);
                return importId;
            }
            if(info.getCardStatus().equals(cardInfoReqDTO.getCardStatus())){
                saveTempCard(cardInfoReqDTO,"状态重复变更",importId);
                return importId;
            }
            if(!cardInfoReqDTO.getCardStatus().equals("02")&&!cardInfoReqDTO.getCardStatus().equals("01")&&!cardInfoReqDTO.getCardStatus().equals("03")){
                saveTempCard(cardInfoReqDTO,"状态不存在",importId);
                return importId;
            }
            CardInfo infos = new CardInfo();
            infos.setCustCardId(cardInfoReqDTO.getCustCardId());
            infos.setCardStatus(cardInfoReqDTO.getCardStatus());
            infos.setUpdateTime(DateUtil.getSysDate());
            infos.setUpdateStaff(cardInfoReqDTO.getStaff().getId());
            cardInfoMapper.updateByPrimaryKeySelective(infos);
            
        
        return importId;
    }
    
    /**
     * 
     * saveTempCard:(新增错误模板). <br/> 
     * 
     * @param list
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public void saveTempCard(CardInfoReqDTO dto,String errorMessage,Long importId) throws BusinessException{
        
        CardImportTemp temp = new CardImportTemp();
        temp.setCustCardId(dto.getCustCardId());
        temp.setErrorMess(errorMessage);
        temp.setImportId(importId);
        temp.setCreateTime(DateUtil.getSysDate());
        temp.setCreateStaff(dto.getStaff().getId());
        temp.setRemark(errorMessage);
        try {
            cardImportTempMapper.insert(temp);
        } catch (Exception e) {
          LogUtil.error(MODULE, "新增临时表异常", e);
        }
       
        
    }

    @Override
    public PageResponseDTO<CardImportTempResDTO> listTempPage(CardImportTempReqDTO req)
            throws BusinessException {
        
        PageResponseDTO<CardImportTempResDTO> pageInfo = new PageResponseDTO<CardImportTempResDTO>();
        CardImportTempCriteria criteria = new CardImportTempCriteria();
        criteria.setOrderByClause("create_time desc");
        com.ai.ecp.pmph.dao.model.CardImportTempCriteria.Criteria sql = criteria.createCriteria();
        if(null!=req.getImportId()&&0!=req.getImportId()){
        sql.andImportIdEqualTo(req.getImportId());
        }
        sql.andCreateStaffEqualTo(req.getStaff().getId());
        pageInfo.setPageNo(req.getPageNo());
        pageInfo.setPageSize(req.getPageSize());
        //设置查询的开始及终止的rownum
        criteria.setLimitClauseStart(pageInfo.getStartRowIndex());
        criteria.setLimitClauseCount(pageInfo.getPageSize());
        pageInfo = super.queryByPagination(req, criteria, true, new PaginationCallback<CardImportTemp, CardImportTempResDTO>() {

            @Override
            public List<CardImportTemp> queryDB(BaseCriteria bc) {
                return cardImportTempMapper.selectByExample((CardImportTempCriteria)bc);
            }

            @Override
            public long queryTotal(BaseCriteria bc) {
                return cardImportTempMapper.countByExample((CardImportTempCriteria)bc);
            }

            @Override
            public List<Comparator<CardImportTemp>> defineComparators() {
                List<Comparator<CardImportTemp>> ls=new ArrayList<Comparator<CardImportTemp>>();
                ls.add(new Comparator<CardImportTemp>(){

                    @Override
                    public int compare(CardImportTemp o1, CardImportTemp o2) {
                        return o1.getCreateTime().getTime()<o2.getCreateTime().getTime()?1:-1;
                    }
                    
                });
                return ls;
            }
            @Override
            public CardImportTempResDTO warpReturnObject(CardImportTemp cardImportTemp) {
                CardImportTempResDTO res = new CardImportTempResDTO();
                ObjectCopyUtil.copyObjValue(cardImportTemp, res, null, true);
                return res;
            }
        });
        return pageInfo;
        
    }

    @Override
    public void deleteCardImport(CardImportTempReqDTO req) throws BusinessException {
        CardImportTempCriteria c = new CardImportTempCriteria();
        com.ai.ecp.pmph.dao.model.CardImportTempCriteria.Criteria sql = c.createCriteria();
        sql.andCreateStaffEqualTo(req.getStaff().getId());
        try {
            cardImportTempMapper.deleteByExample(c);
        } catch (Exception e) {
          LogUtil.error(MODULE, "删除临时导入表异常", e);
          throw new BusinessException("删除临时导入表异常");
        }
    }

    @Override
    public void updateCardStatus1(CardInfoReqDTO req) throws BusinessException {
        
        List<CardInfoReqDTO> list = req.getList();
        try {
            for (CardInfoReqDTO cardInfoReqDTO : list) {
                CardInfo info = new CardInfo();
                info.setCustCardId(cardInfoReqDTO.getCustCardId());
                info.setCardStatus(cardInfoReqDTO.getCardStatus());
                info.setUpdateTime(DateUtil.getSysDate());
                cardInfoMapper.updateByPrimaryKeySelective(info);
            }
        } catch (Exception e) {
           LogUtil.error(MODULE, StaffConstants.STAFF_UPDATE_ERROR, e);
        }
      
        
    }

    @Override
    public PageResponseDTO<CardBindResDTO> listCardBind(CardBindReqDTO req)
            throws BusinessException {
        PageResponseDTO<CardBindResDTO> pageInfo = new PageResponseDTO<CardBindResDTO>();
        CardBindCriteria criteria = new CardBindCriteria();
        criteria.setOrderByClause("create_time desc");
        com.ai.ecp.pmph.dao.model.CardBindCriteria.Criteria sql = criteria.createCriteria();
        //查询条件：绑定方式
        if (StringUtil.isNotBlank(req.getBindType())) {
            sql.andBindTypeEqualTo(req.getBindType());
        }
        //查询条件：所属群组
        if (StringUtil.isNotBlank(req.getCardGroup())) {
            sql.andCardGroupLike("%" + req.getCardGroup() + "%");
        }
        //查询条件：联系人姓名
        if (StringUtil.isNotBlank(req.getContactName())) {
            sql.andContactNameLike("%" + req.getContactName() + "%");
        }
        //查询条件：联系人手机
        if (StringUtil.isNotBlank(req.getContactPhone())) {
            sql.andContactPhoneLike("%" + req.getContactPhone() + "%");
        }
        if (StringUtil.isNotBlank(req.getCustCardId())) {
            sql.andCustCardIdLike("%" + req.getCustCardId() + "%");
        }
       
        pageInfo.setPageNo(req.getPageNo());
        pageInfo.setPageSize(req.getPageSize());
        //设置查询的开始及终止的rownum
        criteria.setLimitClauseStart(pageInfo.getStartRowIndex());
        criteria.setLimitClauseCount(pageInfo.getPageSize());
        pageInfo = super.queryByPagination(req, criteria, true, new PaginationCallback<CardBind, CardBindResDTO>() {

            @Override
            public List<CardBind> queryDB(BaseCriteria bc) {
                return cardBindMapper.selectByExample((CardBindCriteria)bc);
            }

            @Override
            public long queryTotal(BaseCriteria bc) {
                return cardBindMapper.countByExample((CardBindCriteria)bc);
            }

            @Override
            public List<Comparator<CardBind>> defineComparators() {
                List<Comparator<CardBind>> ls=new ArrayList<Comparator<CardBind>>();
                ls.add(new Comparator<CardBind>(){

                    @Override
                    public int compare(CardBind o1, CardBind o2) {
                        return o1.getCreateTime().getTime()<o2.getCreateTime().getTime()?1:-1;
                    }
                    
                });
                return ls;
            }
            @Override
            public CardBindResDTO warpReturnObject(CardBind cardBind) {
                CardBindResDTO res = new CardBindResDTO();
                ObjectCopyUtil.copyObjValue(cardBind, res, null, true);
                CustInfoResDTO custInfo = custManageSV.findCustInfoById(cardBind.getStaffId());
                if (custInfo != null) {
                    res.setStaffCode(custInfo.getStaffCode());
                }
                return res;
            }
        });
        return pageInfo;
    }

}

