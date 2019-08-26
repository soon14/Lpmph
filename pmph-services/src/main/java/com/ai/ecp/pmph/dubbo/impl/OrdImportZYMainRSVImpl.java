package com.ai.ecp.pmph.dubbo.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.order.dubbo.dto.ROrdImportZYRequest;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportZYMainRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportSV;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月12日下午8:22:28 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class OrdImportZYMainRSVImpl implements IOrdImportZYMainRSV {

    @Resource
    private IOrdImportSV ordImportSV;


    @Resource
    private ICustManageRSV custManageRSV;

    @Resource
    private IOrdImportLogSV ordImportLogSV;
       
    

    private static final String MODULE = OrdImportZYMainRSVImpl.class.getName();

    @Override
    public void receive(Map<String, Object> data) {
        LogUtil.info(MODULE, "泽元主订单导入dubbo服务开始导入");
        if (data != null) {
            String id = String.valueOf(data.get("ID"));
            String userName = String.valueOf(data.get("UserName"));
            if (StringUtil.isBlank(id) || StringUtil.isBlank(userName)) {
                return;
            }
            String realMoney = String.valueOf(data.get("RealMoney"));
            String status = String.valueOf(data.get("Status"));
            String paymentStatus = String.valueOf(data.get("PaymentStatus"));
            String orderAmounts = String.valueOf(data.get("OrderAmount"));
            try {
                // 调用客户域服务，获取和zyStaffId对应的用户信息
                LogUtil.info(MODULE, "开始查询客户域数据，入参为"+userName);
                CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
                custInfoReqDTO.setStaffCode(userName);
                CustInfoResDTO custInfoResDTO = custManageRSV.findCustInfo(custInfoReqDTO);
                if (StringUtil.isEmpty(custInfoResDTO)) {
                    OrdImportLog ordImportLog = new OrdImportLog();
                    ordImportLog.setId(StringUtil.getRandomString(32));
                    ordImportLog.setOldOrderId(id);
                    ordImportLog.setImportType("03");
                    ordImportLog.setImportTime(DateUtil.getSysDate());
                    ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
                    ordImportLog.setExceptionMsg("导入文件中的id为" + id
                            + ",用户id为"+userName+"在系统中找不到对应的staffId");
                    ordImportLog.setCreateStaff(100l);
                    ordImportLog.setCreateTime(DateUtil.getSysDate());
                    ordImportLog.setUpdateStaff(100l);
                    ordImportLog.setUpdateTime(DateUtil.getSysDate());
                    LogUtil.info(MODULE, "找不到staffId泽元主订单导入失败后开始进行日志保存");
                    ordImportLogSV.saveOrdImportLog(ordImportLog);
                } else {
                    LogUtil.info(MODULE, "查询客户域数据结束");
                    Long staffId=custInfoResDTO.getId();
                    ROrdImportZYRequest rOrdImportZYRequest = new ROrdImportZYRequest();
                    rOrdImportZYRequest.setStaffId(staffId);
                    rOrdImportZYRequest.setOrderAmounts(orderAmounts);
                    rOrdImportZYRequest.setId(id);
                    rOrdImportZYRequest.setPaymentStatus(paymentStatus);
                    rOrdImportZYRequest.setRealMoney(realMoney);
                    rOrdImportZYRequest.setStatus(status);
                    LogUtil.info(MODULE, "开始泽元主订单sv服务导入");
                    ordImportSV.saveOrdMainZY(rOrdImportZYRequest);
                    OrdImportLog ordImportLog = new OrdImportLog();
                    ordImportLog.setOldOrderId(id);
                    ordImportLog.setNewOrderId("ZY"+id);
                    ordImportLog.setOldStaffId(userName);
                    ordImportLog.setNewStaffId(staffId);
                    ordImportLog.setImportType("03");
                    ordImportLog.setStatus(OrdConstants.Common.COMMON_VALID);
                    ordImportLog.setExceptionMsg("导入成功");
                    LogUtil.info(MODULE, "泽元主订单导入成功后开始进行日志保存");
                    ordImportLogSV.saveOrdImportLog(ordImportLog);
                }
            } catch (Exception e) {
                LogUtil.debug(MODULE, "导入失败，保存日志",e);
                OrdImportLog ordImportLog = new OrdImportLog();
                ordImportLog.setOldOrderId(id);
                ordImportLog.setImportType("03");
                ordImportLog.setExceptionMsg("泽元主订单导入失败:订单号" + id + "的数据无法正确插入系统");
                ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
                LogUtil.info(MODULE, "服务异常后进行日志保存");
                ordImportLogSV.saveOrdImportLog(ordImportLog);
            }
        }
    }   
}
