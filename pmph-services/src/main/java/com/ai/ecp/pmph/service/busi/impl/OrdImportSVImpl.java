/**
 * 
 */
package com.ai.ecp.pmph.service.busi.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.price.GdsPriceCalReqDTO;
import com.ai.ecp.goods.dubbo.dto.price.GdsPriceCalRespDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoForGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsPriceRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsStockRSV;
import com.ai.ecp.order.dao.mapper.busi.OrdMainMapper;
import com.ai.ecp.order.dao.mapper.busi.OrdSubMapper;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dao.model.OrdMainShopIdx;
import com.ai.ecp.order.dao.model.OrdMainStaffIdx;
import com.ai.ecp.order.dao.model.OrdSub;
import com.ai.ecp.order.dao.model.OrdSubShopIdx;
import com.ai.ecp.order.dao.model.OrdSubStaffIdx;
import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.ROrdImportZYRequest;
import com.ai.ecp.order.dubbo.dto.ROrdInvoiceCommRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdSubResponse;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdSubRSV;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainShopIdxSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainStaffIdxSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubShopIdxSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubStaffIdxSV;
import com.ai.ecp.pmph.dao.mapper.busi.OrdMainTMMapper;
import com.ai.ecp.pmph.dao.mapper.busi.OrdSubTMMapper;
import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.dao.model.OrdMainTM;
import com.ai.ecp.pmph.dao.model.OrdMainTMCriteria;
import com.ai.ecp.pmph.dao.model.OrdSubTM;
import com.ai.ecp.pmph.dao.model.OrdSubTMCriteria;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdTmMainSV;
import com.ai.ecp.server.front.dto.BaseStaff;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustThirdCodeResDTO;
import com.ai.ecp.staff.dubbo.dto.PubCatgLimitRespDTO;
import com.ai.ecp.staff.dubbo.dto.PubUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.ICustThirdCodeRSV;
import com.ai.ecp.staff.dubbo.interfaces.IPubLimitRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopCacheRSV;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.db.sequence.Sequence;

/**
 * 
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月10日下午2:44:54 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@Service("ordImportSV")
public class OrdImportSVImpl extends GeneralSQLSVImpl implements IOrdImportSV {

    @Resource
    private OrdMainTMMapper ordMainTMMapper;

    @Resource
    private OrdSubTMMapper ordSubTMMapper;

    @Resource
    private OrdMainMapper ordMainMapper;

    @Resource
    private IOrdMainShopIdxSV ordMainShopIdxSV;

    @Resource
    private IOrdMainStaffIdxSV ordMainStaffIdxSV;

    @Resource
    private OrdSubMapper ordSubMapper;

    @Resource
    private IOrdSubShopIdxSV ordSubShopIdxSV;

    @Resource
    private IOrdSubStaffIdxSV ordSubStaffIdxSV;

    @Resource
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;

    @Resource
    private ICustManageRSV custManageRSV;

    @Resource
    private IOrdImportLogSV ordImportLogSV;

    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
    @Resource
    private IGdsPriceRSV gdsPriceRSV;
    
    @Resource
    private IShopCacheRSV shopCacheRSV;
    
    @Resource
    private IPubLimitRSV pubLimitRSV;

    @Resource
    private IOrdTmMainSV ordTmMainSV;
    @Resource
    private ICustThirdCodeRSV  custThirdCodeRSV;

    @Resource(name = "seq_ord_main")
    private Sequence seqOrdMain;

    @Resource(name = "seq_ord_sub")
    private Sequence seqOrdSub;
    
    @Resource
    private IGdsStockRSV gdsStockRSV;
    
    @Resource
    private IOrdMainRSV ordMainRSV;
    
    @Resource
    private IOrdSubRSV ordSubRSV;

    private static final String MODULE = OrdImportSVImpl.class.getName();

    @Override
    public void saveOrdMainTM(OrdMainTM ordMainTM) {
        try {
            this.ordMainTMMapper.insert(ordMainTM);
        } catch (BusinessException be) {
            LogUtil.info(MODULE, be.getErrorCode() + "===" + be.getErrorMessage());
            be.printStackTrace();
            throw new BusinessException(be.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310000);
        }
    }

    @Override
    public OrdMainTM queryOrdMainTMRepeat(String orderId, String status) {

        OrdMainTMCriteria ordMainTMCriteria2 = new OrdMainTMCriteria();
        ordMainTMCriteria2.createCriteria().andOrderCodeEqualTo(orderId);
        List<OrdMainTM> ordMainTMs2 = this.ordMainTMMapper.selectByExample(ordMainTMCriteria2);
        // 如订单状态发生变化，只需覆盖原订单状态即可
        if (CollectionUtils.isNotEmpty(ordMainTMs2)) {

            OrdMainTMCriteria ordMainTMCriteria = new OrdMainTMCriteria();
            ordMainTMCriteria.createCriteria().andOrderCodeEqualTo(orderId.trim())
                    .andStatusEqualTo(status.trim());
            List<OrdMainTM> ordMainTMs = this.ordMainTMMapper.selectByExample(ordMainTMCriteria);
            // 如订单状态不变的话，重复的订单无需导入
            if (CollectionUtils.isNotEmpty(ordMainTMs)) {
                return ordMainTMs.get(0);
            }

            OrdMainTM ordMainTM = new OrdMainTM();
            if ("交易成功".equals(status.trim())) {
                ordMainTM.setStatus(status);
                ordMainTM.setImportTime(DateUtil.getSysDate());
                ordMainTM.setUpdateTime(DateUtil.getSysDate());
            } else {
                ordMainTM.setStatus(status);
                ordMainTM.setUpdateTime(DateUtil.getSysDate());
            }
            this.ordMainTMMapper.updateByExampleSelective(ordMainTM, ordMainTMCriteria2);
            return ordMainTMs2.get(0);
        }
        return null;
    }

    @Override
    public void saveOrdSubTM(OrdSubTM ordSubTM) {
        try {
            this.ordSubTMMapper.insert(ordSubTM);
        } catch (BusinessException be) {
            LogUtil.info(MODULE, be.getErrorCode() + "===" + be.getErrorMessage());
            be.printStackTrace();
            throw new BusinessException(be.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310000);
        }
    }

    @Override
    public OrdSubTM queryOrdSubTMRepeat(String orderId, String externalSysCode, String status,String title) {

        OrdSubTMCriteria ordSubTMCriteria2 = new OrdSubTMCriteria();
        if (StringUtil.isBlank(externalSysCode)) {
            ordSubTMCriteria2.createCriteria().andOrderIdEqualTo(orderId).andTitleEqualTo(title.trim());
        } else {
            ordSubTMCriteria2.createCriteria().andOrderIdEqualTo(orderId)
                    .andExternalSysCodeEqualTo(externalSysCode);
        }
        List<OrdSubTM> ordSubTMs2 = this.ordSubTMMapper.selectByExample(ordSubTMCriteria2);
        // 如订单状态发生变化，只需覆盖原订单状态即可
        if (CollectionUtils.isNotEmpty(ordSubTMs2)) {

            OrdSubTMCriteria ordSubTMCriteria = new OrdSubTMCriteria();
            if (StringUtil.isBlank(externalSysCode)) {
                ordSubTMCriteria.createCriteria().andOrderIdEqualTo(orderId)
                        .andTitleEqualTo(title.trim()).andStatusEqualTo(status.trim());
            } else {
                ordSubTMCriteria.createCriteria().andOrderIdEqualTo(orderId)
                        .andExternalSysCodeEqualTo(externalSysCode).andStatusEqualTo(status.trim());
            }
            List<OrdSubTM> ordSubTMs = this.ordSubTMMapper.selectByExample(ordSubTMCriteria);
            // 如订单状态不变的话，重复的订单无需导入
            if (CollectionUtils.isNotEmpty(ordSubTMs)) {
                LogUtil.info(MODULE, "重复的子订单：" + orderId + ":" + externalSysCode);
                return ordSubTMs.get(0);
            }

            OrdSubTM ordSubTM = new OrdSubTM();
            ordSubTM.setStatus(status);
            ordSubTM.setUpdateTime(DateUtil.getSysDate());
            this.ordSubTMMapper.updateByExampleSelective(ordSubTM, ordSubTMCriteria2);
            return ordSubTMs2.get(0);
        }
        return null;
    }

    @Override
    public void saveOrdMainZY(ROrdImportZYRequest info) {
        String id = info.getId();
        Long staffId = info.getStaffId();
        String status = info.getStatus();
        String paymentStatus = info.getPaymentStatus();
        String orderAmount = info.getOrderAmounts();
        String realMoney = info.getRealMoney();
        OrdMain ordMain = new OrdMain();
        /* String ordMainId = seqOrdMain.nextValue().toString(); */
        ordMain.setId("ZY" + id);
        ordMain.setOrderCode(id);
        ordMain.setOrderTime(DateUtil.getSysDate());
        ordMain.setStaffId(staffId);
        // 店铺写死为人卫商城店铺100，名称就叫人卫商城
        ordMain.setShopId(100l);
        ordMain.setShopName("人卫商城");
        if (status.equals("0") || status.equals("1")) {
            ordMain.setStatus(OrdConstants.OrderStatus.ORDER_STATUS_CLOSE);
        } else {
            ordMain.setStatus(OrdConstants.OrderStatus.ORDER_STATUS_CANCLE);
        }
        ordMain.setSource(OrdConstants.Order.ORDER_SOURCE_0);
        ordMain.setOrderType(OrdConstants.Order.ORDER_TYPE_01);
        if (paymentStatus.equals("0")) {
            ordMain.setPayFlag(OrdConstants.Order.ORDER_PAY_FLAG_0);
        } else {
            ordMain.setPayFlag(OrdConstants.Order.ORDER_PAY_FLAG_1);
        }
        if (StringUtil.isBlank(orderAmount)) {
            orderAmount = "0";
        }
        if (StringUtil.isBlank(realMoney)) {
            realMoney = "0";
        }
        ordMain.setOrderAmount(Long.valueOf(orderAmount));
        ordMain.setOrderMoney(Long.valueOf(realMoney) * 100);
        ordMain.setRealMoney(Long.valueOf(realMoney) * 100);
        ordMain.setRealExpressFee(0l);
        ordMain.setSiteId(1l);
        ordMain.setSysType(OrdConstants.SysType.SYS_TYPE_ZYMALL);
        ordMain.setPayType("0");
        ordMain.setCreateTime(DateUtil.getSysDate());
        ordMain.setCreateStaff(1000l);
        ordMain.setUpdateStaff(1000l);
        ordMain.setUpdateTime(DateUtil.getSysDate());
        ordMain.setIsInAuditFile(OrdConstants.PayStatus.ORD_MAIN_IS_IN_AUDIT_FILE_0);
        // 订单总积分
        ordMain.setOrderScore(0l);
        LogUtil.info(MODULE, "=======" + ordMain.toString());
        this.ordMainMapper.insert(ordMain);
        OrdMainShopIdx ordMainShopIdx = new OrdMainShopIdx();
        ObjectCopyUtil.copyObjValue(ordMain, ordMainShopIdx, null, false);
        ordMainShopIdx.setOrderId(ordMain.getId());
        this.ordMainShopIdxSV.saveOrdMainShopIdx(ordMainShopIdx);
        OrdMainStaffIdx OrdMainStaffIdx = new OrdMainStaffIdx();
        ObjectCopyUtil.copyObjValue(ordMain, OrdMainStaffIdx, null, false);
        OrdMainStaffIdx.setOrderId(ordMain.getId());
        this.ordMainStaffIdxSV.saveOrdMainStaffIdx(OrdMainStaffIdx);
    }

    @Override
    public void saveOrdSubZY(ROrdImportZYRequest info) {

        String orderID = info.getOrderID();
        String productID = info.getProductID();
        String productSN = info.getProductSN();
        String cost = info.getCost();
        String price = info.getPrice();
        OrdSub ordSub = new OrdSub();
        ordSub.setId("SZY" + orderID + "_" + productID);
        // 根据zyOrderCode查询主订单的orderId
        ordSub.setOrderId("ZY" + orderID);
        ordSub.setOrderCode("ZY" + orderID);
        // 根据zySkuId查询到本系统的gdsId,skuId,gdsName,categoryCode,gdsType,repCode,stockId
        Long skuId = info.getSkuId();
        Long gdsId = info.getGdsId();
        String categoryCode = info.getCategoryCode();
        String skuInfo = info.getSkuInfo();
        String gdsName = info.getProductName();
        Long repCode = info.getRepCode();
        Long stockId = info.getStockId();
        String orderAmount = info.getOrderAmount();
        if (StringUtil.isBlank(orderAmount)) {
            orderAmount = "0";
        }
        if (StringUtil.isBlank(cost)) {
            cost = "0";
        }
        if (StringUtil.isBlank(price)) {
            price = "0";
        }
        ordSub.setGdsId(gdsId);
        ordSub.setGdsName(gdsName);
        ordSub.setSkuId(skuId);
        ordSub.setStatus(OrdConstants.OrderStatus.ORDER_STATUS_CLOSE);
        ordSub.setSkuInfo(skuInfo);
        ordSub.setCategoryCode(categoryCode);
        ordSub.setRepCode(repCode);
        ordSub.setStockId(stockId);
        ordSub.setOrderAmount(Long.valueOf(orderAmount));
        ordSub.setGroupType("0");
        ordSub.setGroupDetail(skuId.toString());
        ordSub.setBasePrice(Long.valueOf(cost) * 100);
        ordSub.setBuyPrice(Long.valueOf(price) * 100);
        ordSub.setDiscountPrice(Long.valueOf(price) * 100);
        ordSub.setOrderMoney(ordSub.getBuyPrice() * ordSub.getOrderAmount());
        ordSub.setDiscountMoney(ordSub.getOrderMoney());
        ordSub.setReduceMoney(0l);
        ordSub.setDeliverStatus("0");
        ordSub.setDeliverAmount(0l);
        ordSub.setOrderTime(DateUtil.getSysDate());
        ordSub.setStaffId(180l);
        ordSub.setShopId(100l);

        ordSub.setSysType(OrdConstants.SysType.SYS_TYPE_ZYMALL);
        ordSub.setSiteId(1l);
        ordSub.setEvalFlag(OrdConstants.Common.COMMON_INVALID);
        ordSub.setGdsType(info.getGdsType());
        ordSub.setRemainAmount(ordSub.getOrderAmount());
        ordSub.setCreateTime((DateUtil.getSysDate()));
        ordSub.setCreateStaff(1000l);
        ordSub.setUpdateStaff(1000l);
        ordSub.setUpdateTime(DateUtil.getSysDate());

        ordSub.setScore(0l);
        ordSub.setOrderSubScore(0l);
        ordSub.setPrnFlag("0");
        this.ordSubMapper.insert(ordSub);
        OrdSubShopIdx ordSubShopIdx = new OrdSubShopIdx();
        ObjectCopyUtil.copyObjValue(ordSub, ordSubShopIdx, null, false);
        ordSubShopIdx.setOrderSubId(ordSub.getId());
        this.ordSubShopIdxSV.saveOrdSubShopIdx(ordSubShopIdx);
        OrdSubStaffIdx OrdSubStaffIdx = new OrdSubStaffIdx();
        ObjectCopyUtil.copyObjValue(ordSub, OrdSubStaffIdx, null, false);
        OrdSubStaffIdx.setOrderSubId(ordSub.getId());
        this.ordSubStaffIdxSV.saveOrdSubStaffIdx(OrdSubStaffIdx);
    }

    @Override
    public void saveBatchOrdMainTM(RFileImportRequest info, List<List<Object>> group) {
        for (List<Object> row : group) {
            saveOneOrdMainTM(info, row);
        }
    }

    @Override
    public void saveOneOrdMainTM(RFileImportRequest info, List<Object> row) {
        if (StringUtil.isBlank(StringUtil.toString(row.get(0)))) {
            // 订单ID为空不处理
            return;
        }

        if (row.size() < 40) {
            for (int i = row.size(); i < 40; i++) {
                row.add(i, "");
            }
        }
        try {
            String tmOrderId = new BigDecimal(StringUtil.toString(row.get(0))).toPlainString();
            String newOrderId = OrdConstants.Common.TM_CODE + tmOrderId;
            String orderStatus = StringUtil.toString(row.get(10));
            OrdMainTM ordMainTM2 = queryOrdMainTMRepeat(tmOrderId, orderStatus);
            if (ordMainTM2 != null) {
                // 订单已导入或者状态有更新
                return;
            }
            OrdMainTM ordMainTM = new OrdMainTM();
            ordMainTM.setId(tmOrderId);
            ordMainTM.setOrderCode(tmOrderId);
            ordMainTM.setTmStaffCode(StringUtil.toString(row.get(1)));
            try {
                ordMainTM.setAlipayAccount(StringUtil
                        .toString(new BigDecimal(StringUtil.toString(row.get(2))).toPlainString()));
            } catch (Exception e) {
                ordMainTM.setAlipayAccount(StringUtil.toString(row.get(2))); // 买家支付宝账号
            }
            //查询是否存在已经绑定的淘宝账号，如果存在，获取商城的staffId和Code，并直接插入
            CustThirdCodeReqDTO custThirdCodeReqDTO=new CustThirdCodeReqDTO();
            custThirdCodeReqDTO.setThirdCodeType("10");
            custThirdCodeReqDTO.setThirdCode(ordMainTM.getTmStaffCode());
            CustThirdCodeResDTO custThirdCodeResDTO=custThirdCodeRSV.queryThirdCode(custThirdCodeReqDTO);
            if(!StringUtil.isEmpty(custThirdCodeResDTO)){
                ordMainTM.setRwStaffCode(custThirdCodeResDTO.getStaffCode());
                ordMainTM.setRwStaffId(custThirdCodeResDTO.getStaffId());
            }
            ordMainTM.setOrderMoney(StringUtil.toString(row.get(3))); // 买家应付货款
            ordMainTM.setRealExpressFee(StringUtil.toString(row.get(4))); // 买家应付邮费
            ordMainTM.setOrderScore(StringUtil.toString(row.get(5))); // 买家支付积分
            ordMainTM.setSumMoney(StringUtil.toString(row.get(6))); // 总金额
            ordMainTM.setBackScore(StringUtil.toString(row.get(7))); // 返点积分
            ordMainTM.setRealMoney(StringUtil.toString(row.get(8))); // 买家实际支付金额
            ordMainTM.setRealScore(StringUtil.toString(row.get(9))); // 买家实际支付积分
            ordMainTM.setStatus(StringUtil.toString(row.get(10))); // 订单状态
            ordMainTM.setBuyerMsg(StringUtil.toString(row.get(11))); // 买家留言
            ordMainTM.setContractName(StringUtil.toString(row.get(12))); // 收货人姓名
            ordMainTM.setContractAddr(StringUtil.toString(row.get(13))); // 收货地址
            ordMainTM.setDispatchType(StringUtil.toString(row.get(14))); // 运送方式
            ordMainTM.setContractTel(StringUtil.toString(row.get(15))); // 联系电话
            ordMainTM.setContractNum(StringUtil.toString(row.get(16))); // 联系手机
            // ordMainTM.setOrderTime(DateUtil.getTimestamp(StringUtil.toString(row.get(17))));//
            // 订单创建时间
            ordMainTM.setOrderTime(getTime(StringUtil.toString(row.get(17)))); // 订单创建时间
            // ordMainTM.setPayTime(DateUtil.getTimestamp(StringUtil.toString(row.get(18))));//
            // 订单支付时间
            ordMainTM.setPayTime(getTime(StringUtil.toString(row.get(18))));// 订单支付时间
            ordMainTM.setBbTitle(StringUtil.toString(row.get(19))); // 宝贝标题
            ordMainTM.setBbType(StringUtil.toString(row.get(20))); // 宝贝种类
            ordMainTM.setExpressNo(StringUtil.toString(row.get(21))); // 物流单号
            ordMainTM.setExpressCompany(StringUtil.toString(row.get(22))); // 物流公司
            ordMainTM.setRemark(StringUtil.toString(row.get(23))); // 订单备注
            if (StringUtil.isNotEmpty(row.get(24))) { // 宝贝总数量
                ordMainTM.setOrderAmount(StringUtil
                        .toString(Double.valueOf(StringUtil.toString(row.get(24))).longValue()));
            }
            ordMainTM.setShopId(StringUtil.toString(row.get(25))); // 店铺Id
            ordMainTM.setShopName(StringUtil.toString(row.get(26))); // 店铺名称
            ordMainTM.setCloseReason(StringUtil.toString(row.get(27))); // 订单关闭原因
            ordMainTM.setShopServiceFee(StringUtil.toString(row.get(28))); // 卖家服务费
            ordMainTM.setStaffServiceFee(StringUtil.toString(row.get(29))); // 买家服务费
            ordMainTM.setInvoiceTitle(StringUtil.toString(row.get(30))); // 发票抬头
            ordMainTM.setAppFlag(StringUtil.toString(row.get(31))); // 是否手机订单
            ordMainTM.setStageOrderMsg(StringUtil.toString(row.get(32))); // 分阶段订单信息
            ordMainTM.setDownRank(StringUtil.toString(row.get(33))); // 定金排名
            ordMainTM.setModitySku(StringUtil.toString(row.get(34))); // 修改后的sku
            ordMainTM.setModifyContractAddr(StringUtil.toString(row.get(35))); // 修改后的收货地址
            ordMainTM.setExceptionMsg(StringUtil.toString(row.get(36))); // 异常信息
            ordMainTM.setTmCoupDeduct(StringUtil.toString(row.get(37))); // 天猫卡券抵扣
            ordMainTM.setJfbDeduct(StringUtil.toString(row.get(38))); // 集分宝抵扣
            ordMainTM.setO2oFlag(StringUtil.toString(row.get(39))); // 是否是O2O交易
            ordMainTM.setCreateStaff(info.getStaffId());
            ordMainTM.setCreateTime(DateUtil.getSysDate());
            ordMainTM.setUpdateStaff(info.getStaffId());
            ordMainTM.setUpdateTime(DateUtil.getSysDate());
            ordMainTM.setImportTime(DateUtil.getSysDate());
            ordMainTM.setRwScoreFlag("0");
            saveOrdMainTM(ordMainTM);

            OrdImportLog ordImportLog = new OrdImportLog();
            ordImportLog.setId(StringUtil.getRandomString(32));
            ordImportLog.setOldOrderId(tmOrderId);
            ordImportLog.setNewOrderId(tmOrderId);
            ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
            ordImportLog.setNewStaffId(info.getStaffId());
            ordImportLog.setCreateStaff(info.getStaffId());
            ordImportLog.setCreateTime(DateUtil.getSysDate());
            ordImportLog.setUpdateStaff(info.getStaffId());
            ordImportLog.setUpdateTime(DateUtil.getSysDate());
            ordImportLog.setImportType("06");
            ordImportLog.setStatus(OrdConstants.Common.COMMON_VALID);
            ordImportLog.setFileId(info.getFileId());
            ordImportLog.setExceptionMsg("导入成功");
            LogUtil.info(MODULE, "天猫主订单导入成功后开始进行日志保存");
            ordImportLogSV.saveOrdImportLog(ordImportLog);

        } catch (Exception e) {
            LogUtil.info(MODULE, "导入失败日志", e);
            OrdImportLog ordImportLog = new OrdImportLog();
            String tmOrderId = new BigDecimal(StringUtil.toString(row.get(0))).toPlainString();
            ordImportLog.setId(StringUtil.getRandomString(32));
            ordImportLog.setOldOrderId(OrdConstants.Common.TM_CODE + tmOrderId);
            ordImportLog.setNewOrderId(OrdConstants.Common.TM_CODE + tmOrderId);
            ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
            ordImportLog.setNewStaffId(info.getStaffId());
            ordImportLog.setCreateStaff(info.getStaffId());
            ordImportLog.setCreateTime(DateUtil.getSysDate());
            ordImportLog.setUpdateStaff(info.getStaffId());
            ordImportLog.setUpdateTime(DateUtil.getSysDate());
            ordImportLog.setImportType("06");
            ordImportLog.setFileId(info.getFileId());
            ordImportLog.setExceptionMsg("天猫主订单导入失败:订单号" + tmOrderId + "的数据无法正确插入系统");
            ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
            LogUtil.info(MODULE, "服务异常后进行日志保存");
            ordImportLogSV.saveOrdImportLog(ordImportLog);
        }
    }

    @Override
    public void saveBatchOrdSubTM(RFileImportRequest info, List<List<Object>> group) {
        for (List<Object> row : group) {
            saveOneOrdSubTM(info, row);
        }
    }

    @Override
    public void saveOneOrdSubTM(RFileImportRequest info, List<Object> row) {
        if (StringUtil.isBlank(StringUtil.toString(row.get(0)))) {
            // 订单ID为空不处理
            return;
        }
        if (row.size() < 10) {
            for (int i = row.size(); i < 10; i++) {
                row.add(i, "");
            }
        }
        try {
            String tmId = new BigDecimal(StringUtil.toString(row.get(0))).toPlainString();
            String tmOrderId = OrdConstants.Common.TM_CODE + tmId;
            String stmOrderId = OrdConstants.Common.STM_CODE + tmId;

            // 判断订单是否已经导入成功
            OrdMainTM ordMainTM = this.ordTmMainSV.queryTMOrderByOrderId(tmOrderId);
            if (ordMainTM == null) {
                OrdImportLog ordImportLog = new OrdImportLog();
                ordImportLog.setId(StringUtil.getRandomString(32));
                ordImportLog.setOldOrderId(stmOrderId);
                ordImportLog.setNewOrderId(stmOrderId);
                ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
                ordImportLog.setNewStaffId(info.getStaffId());
                ordImportLog.setCreateStaff(info.getStaffId());
                ordImportLog.setCreateTime(DateUtil.getSysDate());
                ordImportLog.setUpdateStaff(info.getStaffId());
                ordImportLog.setUpdateTime(DateUtil.getSysDate());
                ordImportLog.setImportType("07");
                ordImportLog.setFileId(info.getFileId());
                ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
                ordImportLog.setExceptionMsg("导入文件中的OrderCode为" + tmId + "在系统中找不到对应的主订单信息");
                LogUtil.info(MODULE, "找不到主订单数据，天猫子订单导入失败后开始进行日志保存");
                ordImportLogSV.saveOrdImportLog(ordImportLog);
                return;
            }

            OrdSubTM ordSubTM2 = queryOrdSubTMRepeat(tmOrderId, StringUtil.toString(row.get(4)),
                    StringUtil.toString(row.get(8)),StringUtil.toString(row.get(1)));
            if (ordSubTM2 != null) {
                // 子订单已导入或者状态有更新
                return;
            }
            OrdSubTM ordSubTM = new OrdSubTM();
            ordSubTM.setId(stmOrderId);
            ordSubTM.setOrderId(tmOrderId);
            ordSubTM.setTitle(StringUtil.toString(row.get(1)));
            ordSubTM.setOrderPrice(StringUtil.toString(row.get(2)));
            if (StringUtil.isNotEmpty(row.get(3))) {
                // ordSubTM.setOrderAmount(StringUtil.toString(row.get(3)));
                ordSubTM.setOrderAmount(StringUtil
                        .toString(Double.valueOf(StringUtil.toString(row.get(3))).longValue()));
            }
            ordSubTM.setExternalSysCode(StringUtil.toString(row.get(4)));
            ordSubTM.setSkuInfo(StringUtil.toString(row.get(5)));
            ordSubTM.setPackageInfo(StringUtil.toString(row.get(6)));
            ordSubTM.setRemark(StringUtil.toString(row.get(7)));
            ordSubTM.setStatus(StringUtil.toString(row.get(8)).trim());
            ordSubTM.setShopCode(StringUtil.toString(row.get(9)).trim());
            ordSubTM.setCreateStaff(info.getStaffId());
            ordSubTM.setCreateTime(DateUtil.getSysDate());
            ordSubTM.setUpdateStaff(info.getStaffId());
            ordSubTM.setUpdateTime(DateUtil.getSysDate());
            saveOrdSubTM(ordSubTM);

            OrdImportLog ordImportLog = new OrdImportLog();
            ordImportLog.setId(StringUtil.getRandomString(32));
            ordImportLog.setOldOrderId(stmOrderId);
            ordImportLog.setNewOrderId(stmOrderId);
            ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
            ordImportLog.setNewStaffId(info.getStaffId());
            ordImportLog.setCreateStaff(info.getStaffId());
            ordImportLog.setCreateTime(DateUtil.getSysDate());
            ordImportLog.setUpdateStaff(info.getStaffId());
            ordImportLog.setUpdateTime(DateUtil.getSysDate());
            ordImportLog.setImportType("07");
            ordImportLog.setFileId(info.getFileId());
            ordImportLog.setStatus(OrdConstants.Common.COMMON_VALID);
            ordImportLog.setExceptionMsg("导入成功");
            LogUtil.info(MODULE, "天猫子订单导入成功后开始进行日志保存");
            ordImportLogSV.saveOrdImportLog(ordImportLog);

            // OrdImportLog ordImportMainLog = ordImportLogSV.queryOrdImport(tmOrderId, "06", "1");
            // if (ordImportMainLog != null) {
            // return;
            // }
            // OrdImportLog ordImportLog = new OrdImportLog();
            // ordImportLog.setId(StringUtil.getRandomString(32));
            // ordImportLog.setOldOrderId(stmOrderId);
            // ordImportLog.setNewOrderId(stmOrderId);
            // ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
            // ordImportLog.setNewStaffId(info.getStaffId());
            // ordImportLog.setCreateStaff(info.getStaffId());
            // ordImportLog.setCreateTime(DateUtil.getSysDate());
            // ordImportLog.setUpdateStaff(info.getStaffId());
            // ordImportLog.setUpdateTime(DateUtil.getSysDate());
            // ordImportLog.setImportType("07");
            // ordImportLog.setFileId(info.getFileId());
            // ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
            // ordImportLog.setExceptionMsg("导入文件中的OrderCode为" + tmId + "在系统中找不到对应的主订单信息");
            // LogUtil.info(MODULE, "找不到主订单数据，天猫子订单导入失败后开始进行日志保存");
            // ordImportLogSV.saveOrdImportLog(ordImportLog);

        } catch (Exception e) {
            LogUtil.debug(MODULE, "导入失败，保存日志", e);
            OrdImportLog ordImportLog = new OrdImportLog();
            ordImportLog.setId(StringUtil.getRandomString(32));
            ordImportLog.setOldOrderId(row.get(0).toString());
            ordImportLog.setNewOrderId(StringUtil.toString(row.get(7)));
            ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
            ordImportLog.setNewStaffId(info.getStaffId());
            ordImportLog.setCreateStaff(info.getStaffId());
            ordImportLog.setCreateTime(DateUtil.getSysDate());
            ordImportLog.setUpdateStaff(info.getStaffId());
            ordImportLog.setUpdateTime(DateUtil.getSysDate());
            ordImportLog.setImportType("07");
            ordImportLog.setExceptionMsg("天猫子订单导入失败:订单号" + row.get(0).toString() + "的数据无法正确插入系统");
            ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
            LogUtil.info(MODULE, "服务异常后进行日志保存");
            ordImportLogSV.saveOrdImportLog(ordImportLog);
        }
    }

    private Timestamp getTime(String strTime) {
        if (StringUtil.isBlank(strTime)) {
            return null;
        }
        Double time = Double.valueOf(strTime);
        long day1970 = 25569;
        long days = time.longValue();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((days - day1970) * 86400000);
        String dayTime = formatter.format(calendar.getTime());
        Double hhtmp = (time - days) * 24;
        long hh = hhtmp.longValue();
        Double mmtmp = (hhtmp - hh) * 60;
        long mm = mmtmp.longValue();
        Double sstmp = (mmtmp - mm) * 60;
        long ss = sstmp.longValue();
        dayTime = dayTime + " " + hh + ":" + mm + ":" + ss;

        return Timestamp.valueOf(dayTime);
    }

	@SuppressWarnings("rawtypes")
	@Override
	public RPreOrdMainsResponse saveOneOrdMainZD(RFileImportRequest info, List<Object> row) throws BusinessException{
		
		LogUtil.info(MODULE, "生成征订预订单开始");
		RPreOrdMainsResponse rPreOrdsResponse = new RPreOrdMainsResponse();
		
		try {
			List<RPreOrdMainResponse> preOrdMainList = new ArrayList<RPreOrdMainResponse>();
			
			/*订单*/
			RPreOrdMainResponse rPreOrdMainResponse = new RPreOrdMainResponse();
			// 初始化是否全部虚拟商品的flag,初始化为1
	        String virtualFlag="1";
	        // 初始化是否全部都是数字教材/电子书/数字图书馆消费卡的flag,初始化为1
	        String platCatgsFlag="1";
	        // 初始化预订单list
	        List<RPreOrdSubResponse> preOrdSubList = new ArrayList<RPreOrdSubResponse>();
	        /*
	         * //预订单总数量 Long orderAmount=0l; //预订单总金额 Long orderMoney=0l;
	         */
	        
	        //暂时先写死shopId为人卫:100
	        rPreOrdMainResponse.setShopId(100L);
	        String shopName = shopCacheRSV.getCacheShopByShopId(100L).getShopName();
	        rPreOrdMainResponse.setShopName(shopName);
	        
	        // 进行一次出参的循环，把对应的运费和价格封装进去
	        Long orderAmount = 0l;
	        Long orderMoney = 0l;
	        Long basicMoney = 0l;
	        Long discountMoney = 0L;
	        Boolean ifExpressFree = false;
	        for(int i=0; i<row.size(); i++){
	        	if(i>=10 && i%2==0){
	        		//数据校验
	        		
	        		if(row.get(i)==null || StringUtils.trim(StringUtil.toString(row.get(i))).equals("")){
	        			//如果商品条形码未填写则跳出循环
	        			continue;
	        		}
	        		//收货人不能为空
	        		if(row.get(0)==null){
	        			LogUtil.error(MODULE, "收货人不能为空!");
	        			throw new BusinessException("收货人不能为空!");
	        		}
	        		//收货地址不能为空
	        		if(row.get(1)==null){
	        			LogUtil.error(MODULE, "收货地址不能为空!");
	        			throw new BusinessException("收货地址不能为空!");
	        		}
	        		//手机号码不能为空
	        		if(row.get(2)==null){
	        			LogUtil.error(MODULE, "手机号码不能为空!");
	        			throw new BusinessException("手机号码不能为空!");
	        		}
	        		
	        		if(StringUtils.trim(StringUtil.toString(row.get(3))).equals("个人")){
	        			row.set(4, "");
	        			row.set(5, "");
	        			if(!(StringUtils.trim(StringUtil.toString(row.get(6))).equals("图书") || StringUtils.trim(StringUtil.toString(row.get(6))).equals("教材") || StringUtils.trim(StringUtil.toString(row.get(6))).equals("资料费"))){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，发票内容只能为在 (图书，教材，资料费) 中选填一个");
		        			throw new BusinessException("当发票归属为个人或单位时，发票内容只能为在 (图书，教材，资料费) 中选填一个");
	        			}
	        			if(!(StringUtils.trim(StringUtil.toString(row.get(7))).equals("是") || StringUtils.trim(StringUtil.toString(row.get(7))).equals("否"))){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，是否附加明细只能为在 (是， 否) 中选填一个");
	        				throw new BusinessException("当发票归属为个人或单位时，是否附加明细只能为在 (是， 否) 中选填一个");
	        			}
	        			if(StringUtils.trim(StringUtil.toString(row.get(8))).equals("") || row.get(8)==null){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，发票金额不能为空!");
		        			throw new BusinessException("当发票归属为个人或单位时，发票金额不能为空!");
	        			}else if(!isPositiveNumRoundedUp2Decimal(StringUtils.trim(StringUtil.toString(row.get(8))))){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，发票金额必须是非零最多保留两位小数的数字!");
		        			throw new BusinessException("当发票归属为个人或单位时，发票金额必须是非零最多保留两位小数的数字!");
	        			}
	        		}else if(StringUtils.trim(StringUtil.toString(row.get(3))).equals("单位")){
	        			if(StringUtils.trim(StringUtil.toString(row.get(4))).equals("") || row.get(4)==null){
	        				LogUtil.error(MODULE, "当发票归属为单位时，发票抬头必须不能为空！");
	        				throw new BusinessException("当发票归属为单位时，发票抬头必须不能为空！");
	        			}
	        			if(StringUtils.trim(StringUtil.toString(row.get(5))).equals("") || row.get(5)==null){
	        				LogUtil.error(MODULE, "当发票归属为单位时，纳税人识别号必须不能为空！");
	        				throw new BusinessException("当发票归属为单位时，纳税人识别号必须不能为空！");
	        			}
	        			if(!(StringUtils.trim(StringUtil.toString(row.get(6))).equals("图书") || StringUtils.trim(StringUtil.toString(row.get(6))).equals("教材") || StringUtils.trim(StringUtil.toString(row.get(6))).equals("资料费"))){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，发票内容只能为在 (图书，教材，资料费) 中选填一个");
		        			throw new BusinessException("当发票归属为个人或单位时，发票内容只能为在 (图书，教材，资料费) 中选填一个");
	        			}
	        			if(!(StringUtils.trim(StringUtil.toString(row.get(7))).equals("是") || StringUtils.trim(StringUtil.toString(row.get(7))).equals("否"))){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，是否附加明细只能为在 (是， 否) 中选填一个");
	        				throw new BusinessException("当发票归属为个人或单位时，是否附加明细只能为在 (是， 否) 中选填一个");
	        			}
	        			if(StringUtils.trim(StringUtil.toString(row.get(8))).equals("") || row.get(8)==null){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，发票金额不能为空!");
		        			throw new BusinessException("当发票归属为个人或单位时，发票金额不能为空!");
	        			}else if(!isPositiveNumRoundedUp2Decimal(StringUtils.trim(StringUtil.toString(row.get(8))))){
	        				LogUtil.error(MODULE, "当发票归属为个人或单位时，发票金额必须是非零最多保留两位小数的数字!");
		        			throw new BusinessException("当发票归属为个人或单位时，发票金额必须是非零最多保留两位小数的数字!");
	        			}
	        		}
	        		
	        		
	        		if(!isPositiveNum(StringUtils.trim(StringUtil.toString(row.get(i+1))))){
	        			LogUtil.error(MODULE, "订购数量，请填写大于0的整数!");
	        			throw new BusinessException("订购数量，请填写大于0的整数!");
	        		}
	        		
	        		Long subOrderAmount = generatePositiveNum(StringUtils.trim(StringUtil.toString(row.get(i+1))));
	        		
	        		
	        		/*预订单*/
	                RPreOrdSubResponse rPreOrdSubResponse = new RPreOrdSubResponse();
	                //1.预订单店铺基本信息
	                rPreOrdSubResponse.setOrdSubId(ordSubRSV.createOrdSubId());
	                rPreOrdSubResponse.setShopId(100L);
	                rPreOrdSubResponse.setShopName(0L);
	                //2.预订单商品基本信息
	                GdsSkuInfoRespDTO gdsSkuInfoRespDTO = gdsSkuInfoQueryRSV.querySkuInfoByBarCode(StringUtil.toString(row.get(i)));
	                if(!gdsSkuInfoRespDTO.getGdsStatus().equals("11")){
	                	throw new BusinessException("此订单中包含未上架的商品,导入失败!");
	                }
	                if(gdsSkuInfoRespDTO.getShopId().longValue()!=100){
	                	throw new BusinessException("此订单中部分商品不属于人卫店铺,导入失败!");
	                }
	                if (gdsSkuInfoRespDTO != null) {
	                	rPreOrdSubResponse.setCategoryCode(gdsSkuInfoRespDTO.getMainCatgs());
	                	rPreOrdSubResponse.setGdsCateName(gdsSkuInfoRespDTO.getMainCatgName());
	                	rPreOrdSubResponse.setGdsId(gdsSkuInfoRespDTO.getGdsId());
	                	rPreOrdSubResponse.setSkuId(gdsSkuInfoRespDTO.getId());
	                	rPreOrdSubResponse.setGdsName(gdsSkuInfoRespDTO.getGdsName());
	                	rPreOrdSubResponse.setGdsType(gdsSkuInfoRespDTO.getGdsTypeId());
	                	rPreOrdSubResponse.setGroupDetail(StringUtil.toString(gdsSkuInfoRespDTO.getId()));
	                	rPreOrdSubResponse.setPrnFlag("0");
	                	rPreOrdSubResponse.setOrderAmount(subOrderAmount);
	                	if (gdsSkuInfoRespDTO.getMainPic() != null) {
	                		rPreOrdSubResponse.setPicUrl(gdsSkuInfoRespDTO.getMainPic().getURL());
	                		rPreOrdSubResponse.setPicId(gdsSkuInfoRespDTO.getMainPic().getMediaUuid());
						}
	                	rPreOrdSubResponse.setGdsUrl(gdsSkuInfoRespDTO.getUrl());
	                	//3.预订单用户ID
	                	rPreOrdSubResponse.setStaffId(info.getStaffId());
	                	//4.预订单库存信息
	                	// 库存，调用商品查询库存接口
	                	StockInfoForGdsReqDTO stockInfoForGdsDTO = new StockInfoForGdsReqDTO();
	                	stockInfoForGdsDTO.setShopId(100L);
	                	stockInfoForGdsDTO.setGdsId(gdsSkuInfoRespDTO.getGdsId());
	                	stockInfoForGdsDTO.setSkuId(gdsSkuInfoRespDTO.getId());
	                	StockInfoRespDTO stockInfoDTO = gdsStockRSV.queryStockInfoByGds(stockInfoForGdsDTO);
	                	Long availCount = 0l;
	                	if (!StringUtil.isEmpty(stockInfoDTO)) {
	                		availCount = stockInfoDTO.getAvailCount();
	                	}
	                	rPreOrdSubResponse.setStockAvailCount(availCount);
					}
	                
	                //5 根据封装的参数，进行运费和价格的计算
	            	GdsPriceCalReqDTO reqDto = new GdsPriceCalReqDTO();
	            	CustInfoResDTO cust = custManageRSV.findCustInfoById(info.getStaffId());
	            	BaseStaff baseStaff = new BaseStaff();
	            	baseStaff.setId(info.getStaffId());
	            	baseStaff.setStaffClass(cust.getCustType());
	            	baseStaff.setStaffCode(cust.getStaffCode());
	            	baseStaff.setStaffLevelCode(cust.getCustLevelCode());
	            	reqDto.setStaff(baseStaff);
	            	
	            	reqDto.setGdsId(rPreOrdSubResponse.getGdsId());
	            	reqDto.setSkuId(rPreOrdSubResponse.getSkuId()); 
	            	reqDto.setAmount(subOrderAmount);
	            	GdsPriceCalRespDTO gdsPriceCalRespDTO = gdsPriceRSV.caculate(reqDto);
	            	//basePrice
	            	rPreOrdSubResponse.setBasePrice(gdsPriceCalRespDTO.getBasePrice());
	            	//buyPrice需要加入征订会员逻辑:先在该商品之下查找单品规则看是否有折扣如果有则buyPrice * discount
	            	//如果没有则查询分类规则,如果还是没有则为buyPrice
	            	Map<String, Object> priceAndIfExpressFree = discountBuyPriceCal(gdsPriceCalRespDTO.getBasePrice(), gdsPriceCalRespDTO.getBuyPrice(),
	            			reqDto.getStaff().getStaffCode(), rPreOrdSubResponse.getSkuId(), rPreOrdSubResponse.getCategoryCode(),gdsSkuInfoRespDTO.getPlatCatgs(),ifExpressFree);
	            	
	            	Long discountBuyPrice = (Long)priceAndIfExpressFree.get("discountBuyPrice");
	            	ifExpressFree = (boolean)priceAndIfExpressFree.get("priceAndIfExpressFree");
	            	
	            	rPreOrdSubResponse.setBuyPrice(discountBuyPrice);
	            	//在这里判断是否免邮
	            	/*if(discountBuyPrice == gdsPriceCalRespDTO.getBuyPrice()){
	            		ifExpressFree = true;
	            	}*/
	            	rPreOrdSubResponse.setOrderMoney(subOrderAmount * discountBuyPrice);
	            	rPreOrdSubResponse.setOrderScore(0L);
	            	//orderAmount
	            	orderAmount = orderAmount + subOrderAmount;
	            	//orderMoney
	            	orderMoney = orderMoney + subOrderAmount
	            		* discountBuyPrice;
	            	//basicMoney
	            	basicMoney = basicMoney + subOrderAmount
	            		* gdsPriceCalRespDTO.getBasePrice();
	            	//discountMoney
	            	discountMoney = discountMoney + subOrderAmount
	            		* (gdsPriceCalRespDTO.getBasePrice()-discountBuyPrice);
	                preOrdSubList.add(rPreOrdSubResponse);
	        	}
	        	
	        }
	        
	    	rPreOrdMainResponse.setPreOrdSubList(preOrdSubList);
	    	// 初始化是否全部虚拟商品的flag
	        rPreOrdMainResponse.setIsVirtual(virtualFlag);
	        //初始化是否全部都是数字教材/电子书/数字图书馆消费卡的flag
	        rPreOrdMainResponse.setIsPlatCatgs(platCatgsFlag);
	        //金额设置
	        rPreOrdMainResponse.setOrderAmount(orderAmount);
	        rPreOrdMainResponse.setOrderMoney(orderMoney);
	        rPreOrdMainResponse.setBasicMoney(basicMoney);
	        rPreOrdMainResponse.setDiscountMoney(discountMoney);
	        
	        //6. 运费金额设置
	        if(orderMoney>=10000 || ifExpressFree){
	        	rPreOrdMainResponse.setRealExpressFee(0L);
	        }else{
	        	rPreOrdMainResponse.setRealExpressFee(500L);
	        }
	        
	        rPreOrdMainResponse.setRealMoney(basicMoney + rPreOrdMainResponse.getRealExpressFee() - discountMoney);
	        //征订单导入订单基本信息
	        //收货人
	        rPreOrdMainResponse.setReceiver(StringUtil.toString(row.get(0)));
	        //收货地址
	        rPreOrdMainResponse.setAddress(StringUtil.toString(row.get(1)));
	        //电话号码
	        String phone = "";
	        if(StringUtil.toString(row.get(2)).contains(".")){
	        	phone = StringUtil.toString(row.get(2)).replace(".", "");
	        	if(phone.contains("E")){
	        		phone = phone.replace("E", "");
	        		phone = phone.substring(0, phone.length()-2);
	        	}
	        }
	        rPreOrdMainResponse.setPhone(phone);
	        rPreOrdMainResponse.setStaffId(info.getStaffId());
	        rPreOrdMainResponse.setOrderId(ordMainRSV.createOrdMainId());
	        
	        //发票信息 
	        if(!row.get(3).equals("")){
	        	rPreOrdMainResponse.setInvoiceType("0");
		        ROrdInvoiceCommRequest rOrdInvoiceCommRequest = new ROrdInvoiceCommRequest();
		        rOrdInvoiceCommRequest.setInvoiceType("0");
		        if(StringUtils.trim(StringUtil.toString(row.get(3))).equals("个人")){
		        	rOrdInvoiceCommRequest.setInvoiceTitle("个人");
		        }else{	        	
		        	rOrdInvoiceCommRequest.setInvoiceTitle(StringUtil.toString(row.get(4)));
		        }
		        rOrdInvoiceCommRequest.setTaxpayerNo(StringUtil.toString(row.get(5)));
		        rOrdInvoiceCommRequest.setInvoiceContent(StringUtil.toString(row.get(6)));
		        if(StringUtil.toString(row.get(7)).equals("是")){	        	
		        	rOrdInvoiceCommRequest.setDetailFlag("1");
		        }else{
		        	rOrdInvoiceCommRequest.setDetailFlag("0");
		        }
		        //rOrdInvoiceCommRequest.setInvoiceMoney(Long.parseLong(StringUtil.toString(row.get(8)).replace(".", "")));
		        rOrdInvoiceCommRequest.setInvoiceMoney(Long.parseLong(StringUtil.toString((int)(Double.parseDouble(row.get(8).toString())*100))));
		        rPreOrdMainResponse.setrOrdInvoiceCommRequest(rOrdInvoiceCommRequest);
		        
	        }
	        
	        rPreOrdMainResponse.setRemark(StringUtil.toString(row.get(9)));
	        
	        preOrdMainList.add(rPreOrdMainResponse);
	        
	        rPreOrdsResponse.setStaffId(info.getStaffId());
	        rPreOrdsResponse.setPreOrdMainList(preOrdMainList);
	        
	        
	        OrdImportLog ordImportLog = new OrdImportLog();
	        ordImportLog.setId(StringUtil.getRandomString(32));
	        ordImportLog.setNewOrderId(StringUtil.getRandomString(32));
	        ordImportLog.setOldOrderId(StringUtil.getRandomString(32));
	        ordImportLog.setOldStaffId(StringUtil.toString(info.getStaffId()));
	        ordImportLog.setNewStaffId(info.getStaffId());
	        ordImportLog.setCreateStaff(info.getStaffId());
	        ordImportLog.setCreateTime(DateUtil.getSysDate());
	        ordImportLog.setUpdateStaff(info.getStaffId());
	        ordImportLog.setUpdateTime(DateUtil.getSysDate());
	        ordImportLog.setImportType("08");
	        ordImportLog.setStatus(OrdConstants.Common.COMMON_VALID);
	        ordImportLog.setFileId(info.getFileId());
	        ordImportLog.setExceptionMsg("导入成功");
	        LogUtil.info(MODULE, "征订单导入成功后开始进行日志保存");
	        ordImportLogSV.saveOrdImportLog(ordImportLog);
	        
		} catch(BusinessException e){
			
			LogUtil.debug(MODULE, "导入失败，保存日志", e);
            OrdImportLog ordImportLog = new OrdImportLog();
            ordImportLog.setId(StringUtil.getRandomString(32));
	        ordImportLog.setNewOrderId(StringUtil.getRandomString(32));
	        ordImportLog.setOldOrderId(StringUtil.getRandomString(32));
            ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
            ordImportLog.setNewStaffId(info.getStaffId());
            ordImportLog.setCreateStaff(info.getStaffId());
            ordImportLog.setCreateTime(DateUtil.getSysDate());
            ordImportLog.setUpdateStaff(info.getStaffId());
            ordImportLog.setUpdateTime(DateUtil.getSysDate());
            ordImportLog.setImportType("08");
            ordImportLog.setFileId(info.getFileId());
            ordImportLog.setExceptionMsg(new StringBuffer("征订单导入失败,原因：").append(e.getErrorCode()).toString());
            ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
            LogUtil.info(MODULE, "服务异常后进行日志保存");
            ordImportLogSV.saveOrdImportLog(ordImportLog);
            
            //把错误的数据加入缓存
            for(;;){//效率高于while
            	if(ZPassLock.lock()){
                	if(CacheUtil.getItem(new StringBuffer("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString())==null){
                		//将错误原因放入list第一个元素之中
                		List<Object> newRow = new ArrayList<Object>();
                    	newRow.add(e.getErrorCode());
                		for(int i=0; i<row.size(); i++){
                			newRow.add(row.get(i));
                		}
                    	List<List<Object>> failRows = new ArrayList<List<Object>>();
                    	failRows.add(newRow);
                    	CacheUtil.addItem(new StringBuffer("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString(), failRows, 1800);
                    }else{
                    	//将错误原因放入list第一个元素之中
                    	List<Object> newRow = new ArrayList<Object>();
                    	newRow.add(e.getErrorCode());
                		for(int i=0; i<row.size(); i++){
                			newRow.add(row.get(i));
                		}
                    	@SuppressWarnings("unchecked")
        				List<List<Object>> failRows = (List<List<Object>>)CacheUtil.getItem(new StringBuffer("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString());
                    	failRows.add(newRow);
                    	CacheUtil.addItem(new StringBuffer("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString(), failRows, 1800);
                    }
                	ZPassLock.unlock();
                	break;
                }
            }
            //throw new BusinessException("订单导入失败!");
		} catch(Exception e){
			
			LogUtil.debug(MODULE, "导入失败，保存日志", e);
            OrdImportLog ordImportLog = new OrdImportLog();
            ordImportLog.setId(StringUtil.getRandomString(32));
            ordImportLog.setNewOrderId(StringUtil.getRandomString(32));
	        ordImportLog.setOldOrderId(StringUtil.getRandomString(32));
            ordImportLog.setOldStaffId(String.valueOf(info.getStaffId()));
            ordImportLog.setNewStaffId(info.getStaffId());
            ordImportLog.setCreateStaff(info.getStaffId());
            ordImportLog.setCreateTime(DateUtil.getSysDate());
            ordImportLog.setUpdateStaff(info.getStaffId());
            ordImportLog.setUpdateTime(DateUtil.getSysDate());
            ordImportLog.setImportType("08");
            ordImportLog.setExceptionMsg("征订单导入失败,原因：系统错误!");
            ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
            LogUtil.info(MODULE, "服务异常后进行日志保存");
            ordImportLogSV.saveOrdImportLog(ordImportLog);
		}
		
		return rPreOrdsResponse;
	}
	
	/**
	 * description:该类利用jdk特性CAS操作提高锁的性能
	 * 
	 * @author Huinan
	 *
	 */
	public static class ZPassLock {
		
		private static AtomicBoolean locked = new AtomicBoolean(false);
		
		public static boolean lock(){
			return locked.compareAndSet(false, true);
		}
		
		public static boolean unlock(){
			return locked.compareAndSet(true, false);
		}
	}
	
	private Map<String, Object> discountBuyPriceCal(Long basePrice, Long buyPrice, String staffCode, Long skuId, String catgCode, String platCatgs, boolean ifExpressFree) {
		//初始化一个Map
		Map<String, Object> priceAndIfExpressFree = new HashMap<String, Object>();
		//在这里初始化一个备份值如果basePrice和备份basePrice相等则返回buyPrice
		Long basePriceCopy = basePrice;
		
		// 将该商品对应的平台分类统计出来，进行折扣比较
		String[] catgs = platCatgs.replace("<", "").split(">");
		
		PubUserInfoReqDTO pubUserInfoReqDTO = new PubUserInfoReqDTO();
		pubUserInfoReqDTO.setStaffCode(staffCode);
		pubUserInfoReqDTO.setStatus("1");
		List<PubCatgLimitRespDTO> pubCatgLimitRespDTOs = pubLimitRSV.queryPubCatgByStaffCode(pubUserInfoReqDTO);
		
		if(pubCatgLimitRespDTOs!=null){
			for (PubCatgLimitRespDTO pubCatgLimitRespDTO : pubCatgLimitRespDTOs) {
				//如果有单品折扣
				if(pubCatgLimitRespDTO.getSkuId()!=null){
					if(pubCatgLimitRespDTO.getSkuId().longValue()==skuId.longValue()){
						if(pubCatgLimitRespDTO.getFreeShipping().equals("1")){
							ifExpressFree = true;
						}
						// 可能在下面分类中已经配置过折扣，所以basePrice需要还原
						basePrice = basePriceCopy;
						basePrice = basePrice * pubCatgLimitRespDTO.getDiscount() / 100;
						// 当有单品条件符合的时候，直接跳出循环
						break; 
					}
			    // 如果没有单品折扣，看是否有对应的直接分类折扣
				}else if(pubCatgLimitRespDTO.getCatgCode().equals(catgCode)){
					basePrice = basePriceCopy;
					basePrice = basePrice * pubCatgLimitRespDTO.getDiscount() / 100;
				}else {
					// 如果不是直接分类，可以查看父级分类是否配置了折扣
					for (int i = 0; i < catgs.length; i++) {
						if (pubCatgLimitRespDTO.getCatgCode().equals(catgs[i])) {
							// 因为可能多个父级分类配置了折扣，需要找到最接近的一个折扣来计算，所以当有分类的时候，需要先将basePrice还原
							basePrice = basePriceCopy;
							basePrice = basePrice * pubCatgLimitRespDTO.getDiscount() / 100;
						}
					}
				}
			}
		}
		
		if(basePrice == basePriceCopy){
			priceAndIfExpressFree.put("discountBuyPrice", buyPrice);
		}else{			
			priceAndIfExpressFree.put("discountBuyPrice", basePrice);
		}
		
		priceAndIfExpressFree.put("priceAndIfExpressFree", ifExpressFree);
		return priceAndIfExpressFree;
	}
	
	//判断字符串是否为两位小数的正实数
	public boolean isPositiveNumRoundedUp2Decimal(String str){
		Pattern pattern = Pattern.compile("^([1-9][0-9]*)+(.[0-9]{1,2})?$");  
        return pattern.matcher(str).matches(); 
	}
	//判断字符串是否为大于0的正整数
	public boolean isPositiveNum(String str){
		Pattern pattern = Pattern.compile("^([1-9][0-9]*)+(.[0]{1})?$");
        return pattern.matcher(str).matches(); 
	}
	//转换为Long类型数组
	public Long generatePositiveNum(String str){
		return Long.parseLong(str.substring(0, str.length()-2));
	}
}
