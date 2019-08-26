package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoResDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * 
 * Project Name:ecp-services-staff <br>
 * Description: dubbo层：会员卡信息管理接口<br>
 * Date:2015-10-27上午11:45:22  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public interface ICardMgrRSV {

    /**
     * 
     * listCardInfo:(查询会员卡信息列表). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public PageResponseDTO<CardInfoResDTO> listCardInfo(CardInfoReqDTO req) throws BusinessException;
    
    /**
     * 
     * listCardBindPage:(根据staff_id查询客户历史绑卡记录). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public PageResponseDTO<CardBindResDTO> listCardBindPageByStaff(CardBindReqDTO req) throws BusinessException;


    /**
     * 
     * updateCardInfo:(通过主键ID，更新会员卡信息). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public int updateCardInfo(CardInfoReqDTO req) throws BusinessException;
    
    /**
     * 
     * saveCardBindOpt:(绑定会员卡). <br/> 
     * 1、绑定会员卡；2、变更会员等级成长值，更新会员信息表中的会员卡号信息；3、保存会员信息变更日志
     * @param cardBind
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public int saveCardBindOpt(CardBindReqDTO req) throws BusinessException;
    
    /**
     * 
     * buildCard:(批量生成会员卡). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    
    public int buildCard(CardInfoReqDTO req) throws BusinessException;
    
    /**
     * 
     * updateCardStatus:(批量修改会员状态). <br/> 
     * 
     * @param req
     * @throws BusinessException 
     * @since JDK 1.7
     */
    
    public Long updateCardStatus(CardInfoReqDTO req) throws BusinessException;
    
    /**
     * 
     * listTempPage:(查询临时错误模板数据). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    
    public PageResponseDTO<CardImportTempResDTO> listTempPage(CardImportTempReqDTO req)throws BusinessException;
    
    /**
     * 
     * deleteCardImport:(删除临时表数据). <br/> 
     * 
     * @param req
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public void deleteCardImport(CardImportTempReqDTO req) throws BusinessException;
    
    /**
     * 
     * updateCardstatus2:(批量修改状态，勾选). <br/> 
     * 
     * @param req
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public void updateCardstatus2(CardInfoReqDTO req) throws BusinessException;
    
    /**
     * 
     * listCardBind:(会员卡绑定列表). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public PageResponseDTO<CardBindResDTO> listCardBind(CardBindReqDTO req) throws BusinessException;

    
}

