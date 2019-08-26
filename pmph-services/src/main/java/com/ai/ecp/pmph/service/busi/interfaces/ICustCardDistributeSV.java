package com.ai.ecp.pmph.service.busi.interfaces;

import com.ai.ecp.pmph.dubbo.dto.CardDistributeReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardDistributeResDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.FileImportReqDTO;

/**
 * 
 * Project Name:ecp-services-staff-server <br>
 * Description: <br>
 * Date:2015-10-23下午2:13:19  <br>
 * 
 * @version  
 * @since JDK 1.6
 * 
 * 发卡信息记录管理
 */
public interface ICustCardDistributeSV {
    
    /**
     * 
     * update:(发卡记录更新). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public int update(CardDistributeReqDTO paramReqDTO) throws BusinessException ;
    
    /**
     * 
     * delete:(删除发卡记录). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public int deleteActive(CardDistributeReqDTO paramReqDTO) throws BusinessException ;
    
    /**
     * 
     * insert:(插入发卡记录). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public int insert(CardDistributeReqDTO paramReqDTO) throws BusinessException ;
    
    /**
     * 
     * listPageInfo:(查询发卡记录列表信息). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public PageResponseDTO<CardDistributeResDTO> listPageInfo(CardDistributeReqDTO paramReqDTO) throws BusinessException ;
    
    /**
     * 
     * find:(查询发卡记录信息). <br/> 
     * 
     * @param paramReqDTO
     * @return 
     * @since JDK 1.6
     */
    public CardDistributeResDTO find(CardDistributeReqDTO paramReqDTO) throws BusinessException ;
    
    /**
     * 
     * importExcelData:(导入excel文件). <br/> 
     * 
     * @param paramExcelReqDTO
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public void importExcelData(FileImportReqDTO paramExcelReqDTO) throws BusinessException ;
     
}

