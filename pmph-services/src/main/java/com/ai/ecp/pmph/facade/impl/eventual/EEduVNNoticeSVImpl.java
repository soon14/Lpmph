package com.ai.ecp.pmph.facade.impl.eventual;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ai.ecp.pmph.aip.dubbo.constants.PmphConstants;
import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeResponse;
import com.ai.ecp.pmph.aip.dubbo.interfaces.IAipEEduNoticeRSV;
import com.ai.ecp.pmph.facade.interfaces.eventual.IEEduVNNoticeSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;
import com.distribute.tx.common.TransactionStatus;

public class EEduVNNoticeSVImpl implements IEEduVNNoticeSV {
    
    @Resource
    private IAipEEduNoticeRSV aipEEduNoticeRSV;
    
    private static final String MODULE = EEduVNNoticeSVImpl.class.getName();

    @Override
    public void joinTransaction(JSONObject message, TransactionStatus status, String transactionName) {
        LogUtil.info(MODULE,"EEduVNNoticeSVImpl============="+message);
        try {
            JSONArray msgs = message.getJSONArray("keys");
            LogUtil.info(MODULE,"通知人卫e教开始，本版编号："+msgs);
            AipEEduVNNoticeRequest request = new AipEEduVNNoticeRequest();
            int size = msgs.size();
            for(int i = 0; i < size ; i++){
                JSONObject msg = msgs.getJSONObject(i);
                request.setContent(PmphConstants.EEduNoteiceType.UPDATE, msg.getString("versionNumber"));
            }
            deal(request);
            LogUtil.info(MODULE,"EEduVNNoticeSVImpl=============通知成功");
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "通知人卫e教接口处理异常", be);
            status.setRollbackOnly();
            throw new BusinessException(be.getErrorCode());
        } catch (Exception e) {
            LogUtil.error(MODULE, "通知人卫e教接口处理异常接口处理异常", e);
            status.setRollbackOnly();
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20002);
        }
    }

    @Override
    public void deal(AipEEduVNNoticeRequest request) throws BusinessException {
        request.setAct(PmphConstants.EEduActType.SHOP);
        String url = null;
        BaseSysCfgRespDTO sysCfgRespDTO = SysCfgUtil.fetchSysCfg(PmphConstants.RemoteURLSysCfgCode.EEDU_REMOTE_VN_NOTICE_URL);
        if(null != sysCfgRespDTO){
            url = sysCfgRespDTO.getParaValue();
        }
        if(null == url){
            url = "";
        }
        request.setReqUrl(url.trim());
        AipEEduVNNoticeResponse respone = aipEEduNoticeRSV.sendVNNoticeRequest(request);
        LogUtil.info(MODULE, ToStringBuilder.reflectionToString(respone));
        //不是成功状态则抛出异常
        if(!AipEEduVNNoticeRequest._ZVING_STATUS_OK.equalsIgnoreCase(respone.getStatus())){
            throw new BusinessException(PmphConstants.ErrorCode.AIP_A20002);
        }
    }
}

