package com.ai.ecp.pmph.dubbo.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;

import com.ai.ecp.goods.dubbo.constants.GdsOption;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoForGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoExternalRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.order.dubbo.dto.ROrdImportZYRequest;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportZYSubRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
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
public class OrdImportZYSubRSVImpl implements IOrdImportZYSubRSV {

    @Resource
    private IOrdImportSV ordImportSV;

    @Resource
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;


    @Resource
    private IOrdImportLogSV ordImportLogSV;
    
    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;   
    
    @Resource
    private IGdsInfoExternalRSV gdsInfoExternalRSV;     
    

    private static final String MODULE = OrdImportZYSubRSVImpl.class.getName();


    @Override
    public void receive(Map<String, Object> data) {
        LogUtil.info(MODULE, "泽元子订单导入dubbo服务开始导入");
        if (data != null) {
            String orderID =  String.valueOf(data.get("OrderID"));
            String productID = String.valueOf(data.get("ProductID"));
            String siteID=String.valueOf(data.get("SiteID"));
            String productSN=String.valueOf(data.get("ProductSN"));
            if (StringUtil.isBlank(orderID) || StringUtil.isBlank(productID) || StringUtil.isBlank(siteID) || StringUtil.isBlank(productSN)) {
                return;
            }
            if (!siteID.equals("34") && !siteID.equals("35") && !siteID.equals("33")) {
                return;
            }
            String productName = String.valueOf(data.get("ProductName"));
            String cost =String.valueOf(data.get("Cost"));
            String price = String.valueOf(data.get("Price"));
            String orderAmount = String.valueOf(data.get("OrderAmount"));
            try {
                // 调用导入服务，获取主订单zyOrderCode对应的用户信息              
                LogUtil.info(MODULE, "开始查询子订单对应的主订单数据，入参为"+orderID);
                OrdImportLog ordImportMainLog=ordImportLogSV.queryOrdImport(orderID,"03","0");   
                ROrdImportZYRequest rOrdImportZYRequest = new ROrdImportZYRequest();
                if (StringUtil.isEmpty(ordImportMainLog)) {
                    OrdImportLog ordImportLog = new OrdImportLog();
                    ordImportLog.setId(StringUtil.getRandomString(32));
                    ordImportLog.setOldOrderId(orderID);
                    ordImportLog.setImportType("04");
                    ordImportLog.setImportTime(DateUtil.getSysDate());
                    ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
                    ordImportLog.setExceptionMsg("导入文件中的zyOrderCode为" + orderID
                            + "在系统中找不到对应的主订单信息");
                    ordImportLog.setCreateStaff(100l);
                    ordImportLog.setCreateTime(DateUtil.getSysDate());
                    ordImportLog.setUpdateStaff(100l);
                    ordImportLog.setUpdateTime(DateUtil.getSysDate());
                    LogUtil.info(MODULE, "找不到主订单数据，泽元子订单导入失败后开始进行日志保存");
                    ordImportLogSV.saveOrdImportLog(ordImportLog);
                    return;
                } else {
                    LogUtil.info(MODULE, "子订单对应的主订单数据结束，出参为"+ordImportMainLog.toString());
                    Long staffId=ordImportMainLog.getNewStaffId();
                    if(siteID.equals("33") || siteID.equals("34")){
                        LogUtil.info(MODULE, "开始查询子订单商品id对应的skuId服务,入参为"+productID);
                        GdsInterfaceGdsReqDTO gdsInterfaceGdsReqDTO = new GdsInterfaceGdsReqDTO();
                        //数字教材和电子书查询接口，入参的productID必须加上EDBOOK
                        gdsInterfaceGdsReqDTO.setOriginGdsId("EDBOOK-"+productID);
                        GdsInterfaceGdsRespDTO gdsInterfaceGdsRespDTO = gdsInterfaceGdsRSV
                                .queryGdsInterfaceGdsByOriginGdsId(gdsInterfaceGdsReqDTO);
                        if(!StringUtil.isEmpty(gdsInterfaceGdsRespDTO)){
                            LogUtil.info(MODULE, "查询子订单商品id对应的skuId服务结束");
                            Long skuId = gdsInterfaceGdsRespDTO.getSkuId();
                            Long gdsId=gdsInterfaceGdsRespDTO.getGdsId();
                            rOrdImportZYRequest.setSkuId(skuId);
                            rOrdImportZYRequest.setGdsId(gdsId);
                            // 补充其余信息
                            LogUtil.info(MODULE, "开始查询子订单商品对应的商品信息服务,入参为"+skuId);
                            GdsSkuInfoReqDTO gdsInfoReqDTO = new GdsSkuInfoReqDTO();
                            gdsInfoReqDTO.setId(skuId);
                            gdsInfoReqDTO.setGdsId(gdsId);
                            gdsInfoReqDTO.setSkuQuery(new GdsOption.SkuQueryOption[] {
                                    GdsOption.SkuQueryOption.BASIC, GdsOption.SkuQueryOption.MAINPIC });
                            GdsSkuInfoRespDTO gdsInfoRespDTO = gdsSkuInfoQueryRSV
                                    .querySkuInfoByOptions(gdsInfoReqDTO);
                            if(!StringUtil.isEmpty(gdsInfoRespDTO)){
                                LogUtil.info(MODULE, "查询子订单商品id对应的商品信息服务结束");
                                rOrdImportZYRequest.setGdsType(gdsInfoRespDTO.getGdsTypeId());
                                rOrdImportZYRequest.setCategoryCode(gdsInfoRespDTO.getMainCatgs());
                                //补充仓库的repCode和stockId
                                Long shopId=gdsInfoRespDTO.getShopId();
                                // 库存，调用商品查询库存接口
                                LogUtil.info(MODULE, "查询子订单商品id对应的库存信息服务开始,入参为店铺id:"+shopId+"商品id:"+gdsId+"单品id:"+skuId+"商品类型:"+gdsInfoRespDTO.getGdsTypeId());
                                StockInfoForGdsReqDTO stockInfoForGdsDTO = new StockInfoForGdsReqDTO();
                                stockInfoForGdsDTO.setShopId(shopId);
                                stockInfoForGdsDTO.setGdsId(gdsId);
                                stockInfoForGdsDTO.setSkuId(skuId);
                                stockInfoForGdsDTO.setTypeId(gdsInfoRespDTO.getGdsTypeId());
                                StockInfoRespDTO stockInfoDTO = gdsInfoExternalRSV.getStockAmount(stockInfoForGdsDTO);
                                if(!StringUtil.isEmpty(stockInfoDTO)){
                                    LogUtil.info(MODULE, "查询子订单商品id对应的库存信息服务结束");
                                    rOrdImportZYRequest.setRepCode(stockInfoDTO.getRepCode());
                                    rOrdImportZYRequest.setStockId(stockInfoDTO.getId());
                                }
                            }
                        }else{
                            OrdImportLog ordImportLog = new OrdImportLog();
                            ordImportLog.setId(StringUtil.getRandomString(32));
                            ordImportLog.setOldOrderId(orderID);
                            ordImportLog.setImportType("04");
                            ordImportLog.setImportTime(DateUtil.getSysDate());
                            ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
                            ordImportLog.setExceptionMsg("导入文件中的zyOrderCode为" + orderID
                                    + "的明细productID为"+productID+"在系统中找不到对应的单品信息");
                            ordImportLog.setCreateStaff(100l);
                            ordImportLog.setCreateTime(DateUtil.getSysDate());
                            ordImportLog.setUpdateStaff(100l);
                            ordImportLog.setUpdateTime(DateUtil.getSysDate());
                            LogUtil.info(MODULE, "productID找不到对应的单品信息，泽元子订单导入失败后开始进行日志保存");
                            ordImportLogSV.saveOrdImportLog(ordImportLog);
                            return;
                        }
                    }else{
                        LogUtil.info(MODULE, "开始查询子订单商品ISBN对应的skuId服务,入参为"+productSN);
                        GdsSku2PropPropIdxReqDTO gdsSku2PropPropIdxReqDTO=new GdsSku2PropPropIdxReqDTO();
                        gdsSku2PropPropIdxReqDTO.setPropId(1002l);
                        gdsSku2PropPropIdxReqDTO.setPropValue(productSN);
                        gdsSku2PropPropIdxReqDTO.setGdsStatus("11");
                        gdsSku2PropPropIdxReqDTO.setOptions(new GdsOption.SkuQueryOption[] {
                            GdsOption.SkuQueryOption.BASIC });
                        PageResponseDTO<GdsSkuInfoRespDTO> gdsSkuInfo=gdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(gdsSku2PropPropIdxReqDTO);
                        if(!CollectionUtils.isEmpty(gdsSkuInfo.getResult())){
                            LogUtil.info(MODULE, "查询子订单商品ISBN对应的skuId服务结束");
                            GdsSkuInfoRespDTO gdsSkuInfoRespDTO=gdsSkuInfo.getResult().get(0);
                            rOrdImportZYRequest.setSkuId(gdsSkuInfoRespDTO.getId());
                            rOrdImportZYRequest.setGdsId(gdsSkuInfoRespDTO.getGdsId());
                            rOrdImportZYRequest.setCategoryCode(gdsSkuInfoRespDTO.getMainCatgs());
                            rOrdImportZYRequest.setGdsType(gdsSkuInfoRespDTO.getGdsTypeId());
                            //补充仓库的repCode和stockId
                            Long shopId=gdsSkuInfoRespDTO.getShopId();
                            // 库存，调用商品查询库存接口
                            LogUtil.info(MODULE, "查询子订单商品id对应的库存信息服务开始,入参为店铺id:"+shopId+"商品id:"+gdsSkuInfoRespDTO.getGdsId()+"单品id:"+gdsSkuInfoRespDTO.getId()+"商品类型:"+gdsSkuInfoRespDTO.getGdsTypeId());
                            StockInfoForGdsReqDTO stockInfoForGdsDTO = new StockInfoForGdsReqDTO();
                            stockInfoForGdsDTO.setShopId(shopId);
                            stockInfoForGdsDTO.setGdsId(gdsSkuInfoRespDTO.getGdsId());
                            stockInfoForGdsDTO.setSkuId(gdsSkuInfoRespDTO.getId());
                            stockInfoForGdsDTO.setTypeId(gdsSkuInfoRespDTO.getGdsTypeId());
                            StockInfoRespDTO stockInfoDTO = gdsInfoExternalRSV.getStockAmount(stockInfoForGdsDTO);
                            if(!StringUtil.isEmpty(stockInfoDTO)){
                                LogUtil.info(MODULE, "查询子订单商品id对应的库存信息服务结束");
                                rOrdImportZYRequest.setRepCode(stockInfoDTO.getRepCode());
                                rOrdImportZYRequest.setStockId(stockInfoDTO.getId());
                            }
                        }else{
                            OrdImportLog ordImportLog = new OrdImportLog();
                            ordImportLog.setId(StringUtil.getRandomString(32));
                            ordImportLog.setOldOrderId(orderID);
                            ordImportLog.setImportType("04");
                            ordImportLog.setImportTime(DateUtil.getSysDate());
                            ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
                            ordImportLog.setExceptionMsg("导入文件中的zyOrderCode为" + orderID
                                    + "的明细productSN为"+productSN+"在系统中找不到对应的单品信息");
                            ordImportLog.setCreateStaff(100l);
                            ordImportLog.setCreateTime(DateUtil.getSysDate());
                            ordImportLog.setUpdateStaff(100l);
                            ordImportLog.setUpdateTime(DateUtil.getSysDate());
                            LogUtil.info(MODULE, "productSN找不到对应的子订单信息，泽元子订单导入失败后开始进行日志保存");
                            ordImportLogSV.saveOrdImportLog(ordImportLog);
                            return;
                        }
                    }      

                    rOrdImportZYRequest.setOrderID(orderID);
                    rOrdImportZYRequest.setProductID(productID);
                    rOrdImportZYRequest.setSiteID(siteID);
                    rOrdImportZYRequest.setProductSN(productSN);
                    rOrdImportZYRequest.setProductName(productName);
                    rOrdImportZYRequest.setCost(cost);
                    rOrdImportZYRequest.setPrice(price);
                    rOrdImportZYRequest.setOrderAmount(orderAmount);
                    rOrdImportZYRequest.setStaffId(staffId);
                    ordImportSV.saveOrdSubZY(rOrdImportZYRequest);
                    OrdImportLog ordImportLog = new OrdImportLog();
                    ordImportLog.setOldOrderId(orderID);
                    ordImportLog.setNewOrderId("ZY"+orderID+"_"+productID);
                    ordImportLog.setImportType("04");
                    ordImportLog.setStatus(OrdConstants.Common.COMMON_VALID);
                    ordImportLog.setExceptionMsg("导入成功");
                    LogUtil.info(MODULE, "泽元子订单导入成功后开始进行日志保存");
                    ordImportLogSV.saveOrdImportLog(ordImportLog);
                }
            } catch (Exception e) {
                LogUtil.info(MODULE, "导入失败，保存日志",e);
                OrdImportLog ordImportLog = new OrdImportLog();
                ordImportLog.setOldOrderId(orderID);
                ordImportLog.setImportType("04");
                ordImportLog.setExceptionMsg("泽元子订单导入失败:订单号" + orderID + "的数据无法正确插入系统");
                ordImportLog.setStatus(OrdConstants.Common.COMMON_INVALID);
                LogUtil.info(MODULE, "服务异常后进行日志保存");
                ordImportLogSV.saveOrdImportLog(ordImportLog);
            }
        }  
    }




}
