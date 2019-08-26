/**
 * 
 */
package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ai.ecp.pmph.dao.mapper.busi.OrdImportLogMapper;
import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.dao.model.OrdImportLogCriteria;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmImportLogResp;
import com.ai.ecp.pmph.dubbo.dto.ROrdZDImportLogResp;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.paas.utils.ObjectCopyUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * 
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月10日下午2:44:54 <br>
 * 
 * @version
 * @since JDK 1.6
 */
//@Service("ordImportLogSV")
public class OrdImportLogSVImpl extends GeneralSQLSVImpl implements IOrdImportLogSV {

    @Resource
    private OrdImportLogMapper ordImportLogMapper;
    
    private static final String MODULE = OrdImportLogSVImpl.class.getName();

    @Override
    public void saveOrdImportLog(OrdImportLog info) {
        this.ordImportLogMapper.insertSelective(info);       
    }
    @Override
    public OrdImportLog queryOrdImport(String oldOrderCode,String importType,String status){
        OrdImportLogCriteria ordImportLogCriteria=new OrdImportLogCriteria();
        ordImportLogCriteria.createCriteria().andOldOrderIdEqualTo(oldOrderCode).andImportTypeEqualTo(importType).andStatusEqualTo(status);       
        List<OrdImportLog> ordImportLogs=this.ordImportLogMapper.selectByExample(ordImportLogCriteria);
        if(ordImportLogs.size()>0){
            return ordImportLogs.get(0);
        }
        return null;
    }
    
    @Override
    public List<ROrdTmImportLogResp> queryFailTmOrdImport(String fileId,String importType){
        OrdImportLogCriteria ordImportLogCriteria=new OrdImportLogCriteria();
        ordImportLogCriteria.createCriteria().andFileIdEqualTo(fileId).andImportTypeEqualTo(importType).andStatusEqualTo("0");       
        List<OrdImportLog> ordImportLogs=this.ordImportLogMapper.selectByExample(ordImportLogCriteria);
        if(CollectionUtils.isEmpty(ordImportLogs)){
            return null;
        }
        List<ROrdTmImportLogResp> rOrdTmImportLogResps=new ArrayList<ROrdTmImportLogResp>();
        for(OrdImportLog ordImportLog:ordImportLogs){
            ROrdTmImportLogResp rOrdTmImportLogResp = new ROrdTmImportLogResp();
            ObjectCopyUtil.copyObjValue(ordImportLog, rOrdTmImportLogResp, null, false); 
            rOrdTmImportLogResps.add(rOrdTmImportLogResp);
        }
       return rOrdTmImportLogResps;
    }
	@Override
	public List<ROrdZDImportLogResp> queryFailZDOrdImport(String fileId, String importType) {
		OrdImportLogCriteria ordImportLogCriteria=new OrdImportLogCriteria();
		ordImportLogCriteria.createCriteria().andFileIdEqualTo(fileId).andImportTypeEqualTo(importType);       
		List<OrdImportLog> ordImportLogs=this.ordImportLogMapper.selectByExample(ordImportLogCriteria);
        if(CollectionUtils.isEmpty(ordImportLogs)){
            return null;
        }
        List<ROrdZDImportLogResp> rOrdZDImportLogResps=new ArrayList<ROrdZDImportLogResp>();
        for(OrdImportLog ordImportLog:ordImportLogs){
        	ROrdZDImportLogResp rOrdZDImportLogResp = new ROrdZDImportLogResp();
        	ObjectCopyUtil.copyObjValue(ordImportLog, rOrdZDImportLogResp, null, false); 
        	rOrdZDImportLogResps.add(rOrdZDImportLogResp);
        }
		return rOrdZDImportLogResps;
	}  
}
