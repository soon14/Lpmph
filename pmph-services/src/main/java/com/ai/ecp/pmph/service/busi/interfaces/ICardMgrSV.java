package com.ai.ecp.pmph.service.busi.interfaces;

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
 * Description: 会员卡管理接口<br>
 * Date:2015-10-26下午8:26:55  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public interface ICardMgrSV {

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
     * updateCardInfo:(更新会员卡信息). <br/> 
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
     * 1、绑定会员卡；
     * 2、变更会员等级成长值，更新会员信息表中的会员卡号信息；
     * 3、保存会员信息变更日志
     * 4、变更会员卡状态信息
     * @param cardBind
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public int saveCardBindOpt(CardBindReqDTO req) throws BusinessException;
    
    public PageResponseDTO<CardBindResDTO> listCardBindPage(CardBindReqDTO req)throws BusinessException;

    /**
     * 
     * saveCardInfo:(批量生成会员卡). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public int saveCardInfo(CardInfoReqDTO req) throws BusinessException;
    
    /**
     * 
     * findCardInfoByCardId:(根据会员卡号码查询会员卡信息). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public CardInfoResDTO findCardInfoByCardId(CardInfoReqDTO req)throws BusinessException;
    
    /**
     * 
     * updateCardStatus:(批量更新会员卡状态). <br/>
     * 
     * @param req
     * @throws BusinessException 
     * @since JDK 1.7
     */
    
    public Long updateCardStatus(CardInfoReqDTO cardInfoReqDTO,Long importId) throws BusinessException;
    
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
     * deleteCardImport:(删除临时卡数据). <br/> 
     * 
     * @param req
     * @throws BusinessException 
     * @since JDK 1.7
     */
    
    public void deleteCardImport(CardImportTempReqDTO req)throws BusinessException;
    
    
    /**
     * 
     * updateCardStatus1:(修改会员卡状态，勾选). <br/> 
     * 
     * @param req
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public void updateCardStatus1(CardInfoReqDTO req)throws BusinessException;
    
    
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

