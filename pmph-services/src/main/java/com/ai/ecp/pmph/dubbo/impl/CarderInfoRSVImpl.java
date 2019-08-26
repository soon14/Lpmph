package com.ai.ecp.pmph.dubbo.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.pmph.dubbo.dto.CardInformationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICarderInfoRSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICarderInfoSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.util.StaffConstants;

public class CarderInfoRSVImpl implements ICarderInfoRSV {

    @Resource
    private ICarderInfoSV carderInfoSV;
    
    @Override
    public int update(CardInformationReqDTO paramReqDTO) throws BusinessException{
        if(paramReqDTO == null || paramReqDTO.getId() ==null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO:[id]"});
        }
        try {
            carderInfoSV.update(paramReqDTO);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BusinessException(StaffConstants.STAFF_UPDATE_ERROR, new String[]{"更新发卡人信息表t_card_information出现异常"});
        }
        
        return 0;
    }

    @Override
    public int deleteAtive(CardInformationReqDTO paramReqDTO) throws BusinessException{
        if(paramReqDTO == null || paramReqDTO.getId() ==null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO:[id]"});
        }
        try {
            carderInfoSV.deleteAtive(paramReqDTO);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BusinessException(StaffConstants.STAFF_UPDATE_ERROR, new String[]{"更新发卡人信息表t_card_information出现异常"});
        }
        
        return 0;
    }
    @Override
    public int updateToAtive(CardInformationReqDTO paramReqDTO) throws BusinessException{
        if(paramReqDTO == null || paramReqDTO.getId() ==null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO:[id]"});
        }
        
        try {
            carderInfoSV.updateToAtive(paramReqDTO);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BusinessException(StaffConstants.STAFF_UPDATE_ERROR, new String[]{"更新发卡人信息表t_card_information出现异常"});
        }
        
        return 0;
    }
    @Override
    public int insert(CardInformationReqDTO paramReqDTO) throws BusinessException{
        
        return carderInfoSV.insert(paramReqDTO);
    }

    @Override
    public PageResponseDTO<CardInformationResDTO> listPageInfo(CardInformationReqDTO paramReqDTO) throws BusinessException{
        
        return carderInfoSV.listPageInfo(paramReqDTO);
    }

    @Override
    public CardInformationResDTO findById(CardInformationReqDTO paramReqDTO) throws BusinessException{
        CardInformationResDTO recorDto = null;
        
        if(paramReqDTO == null || paramReqDTO.getId() ==null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"paramReqDTO:[id]"});
        }
        
        try {
            recorDto = carderInfoSV.findById(paramReqDTO);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BusinessException(StaffConstants.STAFF_SELECT_ERROR, new String[]{"查询发卡人信息表t_card_information出现异常"});
        }
        
        return recorDto;
    }

    @Override
    public Map<Long, String> listCarderInfoMap() throws BusinessException {
        return carderInfoSV.listCarderInfoMap();
    }



}

