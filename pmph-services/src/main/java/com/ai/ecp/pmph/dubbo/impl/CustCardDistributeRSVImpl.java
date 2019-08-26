package com.ai.ecp.pmph.dubbo.impl;

import javax.annotation.Resource;

import com.ai.ecp.pmph.dubbo.dto.CardDistributeReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardDistributeResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICustCardDistributeRSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICardMgrSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICustCardDistributeSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.FileImportReqDTO;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.LogUtil;

public class CustCardDistributeRSVImpl implements ICustCardDistributeRSV {
    private static final String MODULE = CustCardDistributeRSVImpl.class.getName();
    @Resource
    private ICustCardDistributeSV cardDistributeSV;
    @Resource
    private ICardMgrSV cardMgrSV;
    
    @Override
    public int update(CardDistributeReqDTO paramReqDTO) {
        if(paramReqDTO == null || paramReqDTO.getId() ==null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO:[id]"});
        }
        CardInfoReqDTO cardInfoReqDTO = new CardInfoReqDTO();
        cardInfoReqDTO.setCustCardId(paramReqDTO.getCustCardId());
        CardInfoResDTO cardInfoResDTO = cardMgrSV.findCardInfoByCardId(cardInfoReqDTO);
        if(cardInfoResDTO == null)
        {
            throw new BusinessException(StaffConstants.Card.CARD_BIND_NOEXIST_ERROR, new String[]{paramReqDTO.getCustCardId()});
        }
        
        try {
            cardDistributeSV.update(paramReqDTO);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BusinessException(StaffConstants.STAFF_UPDATE_ERROR, new String[]{"更新发卡记录信息表t_card_distribute出现异常"});
        }
        return 0;
    }

    @Override
    public int deleteActive(CardDistributeReqDTO paramReqDTO) {
        if(paramReqDTO == null || paramReqDTO.getId() ==null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO:[id]"});
        }
        try {
            cardDistributeSV.deleteActive(paramReqDTO);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BusinessException(StaffConstants.STAFF_UPDATE_ERROR, new String[]{"更新发卡记录信息表t_card_distribute出现异常"});
        }
        return 0;
    }

    @Override
    public int insert(CardDistributeReqDTO paramReqDTO) {
        if(paramReqDTO == null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO"});
        }
        
        CardInfoReqDTO cardInfoReqDTO = new CardInfoReqDTO();
        cardInfoReqDTO.setCustCardId(paramReqDTO.getCustCardId());
        CardInfoResDTO cardInfoResDTO = cardMgrSV.findCardInfoByCardId(cardInfoReqDTO);
        if(cardInfoResDTO == null || StaffConstants.Card.CUST_CARD_SEND.equals(cardInfoResDTO.getCardStatus()))
        {
            throw new BusinessException(StaffConstants.Card.CARD_BIND_NOEXIST_ERROR, new String[]{paramReqDTO.getCustCardId()});
        }
        
        if(cardDistributeSV.find(paramReqDTO)!= null)
        {
            throw new BusinessException(StaffConstants.Card.CARD_DISTRIBUTE_EXITS, new String[]{paramReqDTO.getCustCardId()});    
        }
        
        try {
            cardDistributeSV.insert(paramReqDTO);
        } catch (Exception e) {
            // TODO: handle exception
            LogUtil.error(MODULE, "新增发卡人异常", e);
            throw new BusinessException(StaffConstants.STAFF_INSERT_ERROR, new String[]{"插入发卡记录信息表t_card_distribute出现异常"});
        }
        return 0;
    }

    @Override
    public PageResponseDTO<CardDistributeResDTO> listPageInfo(CardDistributeReqDTO paramReqDTO) {
        return cardDistributeSV.listPageInfo(paramReqDTO);
    }

    @Override
    public CardDistributeResDTO findById(CardDistributeReqDTO paramReqDTO) {
        if(paramReqDTO == null || paramReqDTO.getId() == null || paramReqDTO.getDisId()== null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO:[id,disId]"});
        }
        CardDistributeResDTO resultDto = null;
        try {
            resultDto = cardDistributeSV.find(paramReqDTO);

        } catch (Exception e) {
            // TODO: handle exception
            throw new BusinessException(StaffConstants.STAFF_SELECT_ERROR, new String[]{"查询发卡记录表t_card_distribute出现异常"});
        }
        return resultDto;
    }

    @Override
    public void importExcelData(FileImportReqDTO paramExcelReqDTO) throws BusinessException {
        cardDistributeSV.importExcelData(paramExcelReqDTO);
    }

}

