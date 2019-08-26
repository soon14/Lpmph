package com.ai.ecp.pmph.dubbo.interfaces;

import com.ai.ecp.pmph.dubbo.dto.CardApplicationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;

/**
 * 
 * Project Name:ecp-services-staff <br>
 * Description:dubbo层：会员卡申请接口 <br>
 * Date:2015-10-27上午11:46:44  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public interface ICardApplicationRSV {

    /**
     * 
     * listCardApplication:(查询会员卡待审核列表). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public PageResponseDTO<CardApplicationResDTO> listCardApplication(CardApplicationReqDTO req) throws BusinessException;
    
    /**
     * 
     * updateCardApplication:(更新会员卡申请信息). <br/> 
     * 
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public int updateCardApplication(CardApplicationReqDTO req) throws BusinessException;
    
    
    /**
     * 
     * saveCardAppPass:(会员卡申请审核通过). <br/> 
     * 1、变更申请表审核状态为：审核通过
     * 2、调用会员卡绑定方法
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.7
     */
    public int saveCardAppPass(CardApplicationReqDTO req, CardBindReqDTO bindReq) throws BusinessException;
    
    /**
     * 
     * applyCustCardOnLine:(在线申请会员卡). <br/> 
     * 
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public int applyCustCardOnLine(CardApplicationReqDTO req)throws BusinessException;
}

