package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.pmph.dao.mapper.busi.CardInformationMapper;
import com.ai.ecp.pmph.dao.model.CardInformation;
import com.ai.ecp.pmph.dao.model.CardInformationCriteria;
import com.ai.ecp.pmph.dao.model.CardInformationCriteria.Criteria;
import com.ai.ecp.pmph.dubbo.dto.CardInformationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationResDTO;
import com.ai.ecp.pmph.service.busi.interfaces.ICarderInfoSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.ecp.staff.dubbo.dto.AuthStaffResDTO;
import com.ai.ecp.staff.service.busi.manage.interfaces.IAuthStaffManageSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.db.sequence.Sequence;

@Service
public class CarderInfoSVImpl extends GeneralSQLSVImpl implements ICarderInfoSV{

    private static String MODULE = CarderInfoSVImpl.class.getName();
    
    private static String ACTIVE_STATUS = "1";
    private static String UNACTIVE_STATUS = "0";

    
    @Resource
    private IAuthStaffManageSV authStaffManageSV;
    
    @Resource
    private CardInformationMapper cardInformationMapper;
    
    @Resource(name="seq_staff_card_information_id")
    private Sequence sequence;  

    @Override
    public int update(CardInformationReqDTO paramReqDTO) {
        int count = 0;
        
        CardInformation record = new CardInformation();
        ObjectCopyUtil.copyObjValue(paramReqDTO, record, null, false);
        
        record.setUpdateTime(DateUtil.getSysDate());
        record.setUpdateStaff(paramReqDTO.getStaff().getId());
        
        try {
            count = cardInformationMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return count;
    }

    @Override
    public int deleteAtive(CardInformationReqDTO paramReqDTO) {
        
        int count = 0;
        
        paramReqDTO.setStatus(UNACTIVE_STATUS);
        
        count = this.update(paramReqDTO);
        
        return count;
    }
    @Override
    public int updateToAtive(CardInformationReqDTO paramReqDTO) {
        int count = 0;
        
        paramReqDTO.setStatus(ACTIVE_STATUS);
        
        count = this.update(paramReqDTO);
        
        return count;
    }
    @Override
    public int insert(CardInformationReqDTO paramReqDTO) {
    	//去重判断
    	CardInformationReqDTO query = new CardInformationReqDTO();
    	query.setDisName(paramReqDTO.getDisName());
    	query.setDisMobile(paramReqDTO.getDisMobile());
    	query.setPageNo(0);//全集
    	PageResponseDTO<CardInformationResDTO> list = this.listPageInfo(paramReqDTO);
    	if(list.getCount()>0){
    		//已存在信息相同的发卡人
    		throw new BusinessException("pmph.100001");
    	}
    	
        int count = 0;
        
        CardInformation record = new CardInformation();
        record.setId(sequence.nextValue());
        
        ObjectCopyUtil.copyObjValue(paramReqDTO, record, null, false);
        record.setCreateStaff(paramReqDTO.getStaff().getId());
        record.setUpdateStaff(paramReqDTO.getStaff().getId());
        record.setCreateTime(DateUtil.getSysDate());
        record.setUpdateTime(DateUtil.getSysDate());
        record.setStatus(ACTIVE_STATUS);
        
        try {
            count = cardInformationMapper.insertSelective(record);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
        return count;    
    }

    @Override
    public PageResponseDTO<CardInformationResDTO> listPageInfo(CardInformationReqDTO paramReqDTO) {
        
        CardInformationCriteria criteria = new CardInformationCriteria();
        criteria.setLimitClauseCount(paramReqDTO.getPageSize());
        criteria.setLimitClauseStart(paramReqDTO.getStartRowIndex());
        criteria.setOrderByClause("create_time desc");
        
        Criteria sql = criteria.createCriteria();
        if(!StringUtil.isBlank(paramReqDTO.getDisName()))
        {
            sql.andDisNameLike("%"+paramReqDTO.getDisName()+"%");
        }
        if(!StringUtil.isBlank(paramReqDTO.getDisMobile()))
        {
            sql.andDisMobileLike("%"+paramReqDTO.getDisMobile()+"%");
        }
        if(!StringUtil.isBlank(paramReqDTO.getStatus()))
        {
            sql.andStatusEqualTo(paramReqDTO.getStatus());
        }
        return super.queryByPagination(paramReqDTO, criteria, true, new PaginationCallback<CardInformation, CardInformationResDTO>() {

            @Override
            public List<CardInformation> queryDB(BaseCriteria criteria) {
                return cardInformationMapper.selectByExample((CardInformationCriteria)criteria);
            }

            @Override
            public long queryTotal(BaseCriteria criteria) {
                return cardInformationMapper.countByExample((CardInformationCriteria)criteria);
            }
            @Override
            public List<Comparator<CardInformation>> defineComparators() {
                List<Comparator<CardInformation>> ls = new ArrayList<Comparator<CardInformation>>();
                ls.add(new Comparator<CardInformation>() {

                    @Override
                    public int compare(CardInformation o1, CardInformation o2) {
                        return o1.getCreateTime().getTime() < o2.getCreateTime().getTime()?1:-1;
                    }

                });
                return ls;
            }
            @Override
            public CardInformationResDTO warpReturnObject(CardInformation arg0) {
                CardInformationResDTO dto = new CardInformationResDTO();
                ObjectCopyUtil.copyObjValue(arg0, dto, null, false);
                
                if(dto.getCreateStaff() != null)
                {
                    AuthStaffResDTO custinfo= authStaffManageSV.findAuthStaffById(dto.getCreateStaff());
                    if(custinfo != null)
                    {
                        dto.setCreateStaffName(custinfo.getStaffCode());
                    }
                }
                
                return dto;
            }
        });
    }

    @Override
    public CardInformationResDTO findById(CardInformationReqDTO paramReqDTO) {
        CardInformation  resultSel = null;
        
        try {
            resultSel = cardInformationMapper.selectByPrimaryKey(paramReqDTO.getId());

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
        if(resultSel != null)
        {
            CardInformationResDTO recorDto = new CardInformationResDTO();
            ObjectCopyUtil.copyObjValue(resultSel, recorDto, null, false);
            
            return recorDto;
        }
        return null;
    }

    @Override
    public Map<Long, String> listCarderInfoMap() {
        CardInformationCriteria criteria = new CardInformationCriteria();
        List<CardInformation> _listCardInformations =  cardInformationMapper.selectByExample(criteria);
        
        if(CollectionUtils.isNotEmpty(_listCardInformations))
        {
            Map<Long, String> resultMap = new HashMap<Long, String>(_listCardInformations.size());
            for(CardInformation cf : _listCardInformations)
            {
                resultMap.put(cf.getId(), cf.getDisName());
            }
            return resultMap;
        }
        return null;
    }



}

