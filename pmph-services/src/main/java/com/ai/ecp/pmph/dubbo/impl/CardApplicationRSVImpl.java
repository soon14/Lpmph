package com.ai.ecp.pmph.dubbo.impl;

import javax.annotation.Resource;

import org.jfree.util.Log;

import com.ai.ecp.general.sys.mc.dto.McParamsDTO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardApplicationResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICardApplicationRSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICardApplicationSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dao.model.CustInfo;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.staff.service.busi.custinfo.interfaces.ICustInfoSV;
import com.ai.ecp.sys.dubbo.interfaces.IMcSyncSendRSV;

/**
 * 
 * Project Name:ecp-services-staff <br>
 * Description: dubbo层：会员卡申请接口实现类<br>
 * Date:2015-10-27上午11:47:29  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public class CardApplicationRSVImpl implements ICardApplicationRSV{

    @Resource
    private ICardApplicationSV cardApplicationSV;
    
    @Resource
    private ICustInfoSV custInfoSV;
    
    @Resource
    private IMcSyncSendRSV mcSyncSendRSV;
    
    @Override
    public PageResponseDTO<CardApplicationResDTO> listCardApplication(CardApplicationReqDTO req)
            throws BusinessException {
        return cardApplicationSV.listCardApplication(req);
    }

    @Override
    public int updateCardApplication(CardApplicationReqDTO req) throws BusinessException {
        return cardApplicationSV.updateCardApplication(req);
    }

    @Override
    public int saveCardAppPass(CardApplicationReqDTO req, CardBindReqDTO bindReq)
            throws BusinessException {
        return cardApplicationSV.saveCardAppPass(req, bindReq);
    }

    @Override
    public int applyCustCardOnLine(CardApplicationReqDTO req) throws BusinessException {
        //1.参数校验：staffID，custlevelcode
        
        //2.判断是否存在申请记录
        CustInfo custInfo = custInfoSV.findCustInfoById(req.getStaffId());
        
        if(cardApplicationSV.isExistApplyRecordByStaff(custInfo))
        {
            //2.1存在则抛出异常信息
            throw new BusinessException(StaffConstants.Card.CARD_EXIST_APPLY_RECORD);
        }
        cardApplicationSV.saveCardApplicationRequest(req);
        
        //发送消息：站内消息、手机短信、邮件；三种信息视开关是否开启而定
        try {
            McParamsDTO mc = new McParamsDTO();
            mc.setMsgCode(StaffConstants.Msg.MC_CARD_APPLY);
            mc.setToUserId(req.getStaffId());
            mcSyncSendRSV.sendMsg(mc);
        } catch (Exception e) {
            Log.error("会员卡申请短信发送异常：" + e.getMessage());
        }
        return 0;
    }

}

