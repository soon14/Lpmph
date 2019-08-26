package com.ai.ecp.pmph.service.busi.interfaces;

import java.util.Map;

import com.ai.ecp.pmph.dubbo.dto.CardInformationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationResDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;

/**
 * 
 * Project Name:ecp-services-staff-server <br>
 * Description: <br>
 * Date:2015-10-23下午2:20:18  <br>
 * 
 * @version  
 * @since JDK 1.6
 * 
 * 发卡人资料管理
 */
public interface ICarderInfoSV {
    
    /**
     * 
     * update:(更新发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int update(CardInformationReqDTO paramReqDTO);
    
    /**
     * 
     * delete:(失效发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int deleteAtive(CardInformationReqDTO paramReqDTO);
    
    /**
     * 
     * delete:(生效发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int updateToAtive(CardInformationReqDTO paramReqDTO);
    /**
     * 
     * insert:(添加发卡人资料). <br/> 
     * 
     * @param record
     * @return 
     * @since JDK 1.6
     */
    public int insert(CardInformationReqDTO paramReqDTO);
    
    /**
     * 
     * findById:(查找发卡人资料信息). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public CardInformationResDTO findById(CardInformationReqDTO paramReqDTO);

    
    /**
     * 
     * listPageInfo:(查询发卡人资料信息). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public PageResponseDTO<CardInformationResDTO> listPageInfo(CardInformationReqDTO paramReqDTO);
    
    /**
     * 
     * listCarderInfoMap:(获取发卡人编码跟名称的MAP对象). <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public Map<Long, String> listCarderInfoMap();
}

