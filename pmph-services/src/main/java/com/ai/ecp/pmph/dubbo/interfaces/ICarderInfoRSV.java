package com.ai.ecp.pmph.dubbo.interfaces;

import java.util.Map;

import com.ai.ecp.pmph.dubbo.dto.CardInformationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationResDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

public interface ICarderInfoRSV {
    
    /**
     * 
     * update:(更新发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int update(CardInformationReqDTO paramReqDTO) throws BusinessException;
    
    /**
     * 
     * delete:(失效发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int deleteAtive(CardInformationReqDTO paramReqDTO) throws BusinessException;
    
    /**
     * 
     * delete:(生效发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int updateToAtive(CardInformationReqDTO paramReqDTO) throws BusinessException;
    /**
     * 
     * insert:(添加发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int insert(CardInformationReqDTO paramReqDTO) throws BusinessException;
    
    /**
     * 
     * findById:(查找发卡人资料信息). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public CardInformationResDTO findById(CardInformationReqDTO paramReqDTO) throws BusinessException;

    /**
     * 
     * listPageInfo:(查询发卡人资料信息). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public PageResponseDTO<CardInformationResDTO> listPageInfo(CardInformationReqDTO paramReqDTO) throws BusinessException;
    
    public Map<Long, String> listCarderInfoMap()throws BusinessException;
}

