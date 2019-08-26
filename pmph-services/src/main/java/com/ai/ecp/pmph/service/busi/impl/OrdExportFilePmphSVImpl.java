package com.ai.ecp.pmph.service.busi.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.ai.ecp.order.dubbo.dto.RExportExcleResponse;
import com.ai.ecp.order.dubbo.dto.RGoodSaleRequest;
import com.ai.ecp.order.dubbo.dto.RGoodSaleResponse;
import com.ai.ecp.order.dubbo.dto.ROrdBarCodeResponse;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsResponse;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsDelivery;
import com.ai.ecp.order.dubbo.util.OrderUtils;
import com.ai.ecp.order.service.busi.impl.OrdExportFileSVImpl;
import com.ai.ecp.order.service.busi.interfaces.IOrdExportFileSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.server.util.DataInoutUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.MoneyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;

public class OrdExportFilePmphSVImpl extends OrdExportFileSVImpl {
//    public class OrdExportFilePmphSVImpl extends OrdExportFileSVImpl implements IOrdExportFileSV  {
    
    @Resource
    private IOrdMainSV ordMainSV;
    
    @Resource
    private IOrdSubSV  ordSubSV;
    
    private static final String MODULE = OrdExportFilePmphSVImpl.class.getName();
    
    @Override
    public RExportExcleResponse queryOrder2Excle(RQueryOrderRequest rQueryOrderRequest)
            throws BusinessException {
        LogUtil.info(MODULE, "=====导出订单入参======="+JSON.toJSONString(rQueryOrderRequest));
        Long oper = rQueryOrderRequest.getStaff().getId();
        String fileName = oper.toString();
        String fileType = "xls";
        String moduleName = "First";
        String operator = oper.toString();
        List<String> titles = new ArrayList<String>();
        titles.add("单号");
        titles.add("部门");
        titles.add("操作人");
        titles.add("操作日期");
        titles.add("复核人");
        titles.add("复核日期");
        titles.add("客户名称");
        titles.add("业务类型");
        titles.add("收货单位");
        titles.add("联系人");
        titles.add("联系电话");
        titles.add("省份");
        titles.add("联系地址");
        titles.add("邮编");
        titles.add("运费");
        titles.add("其他费用");
        titles.add("发货方式");
        titles.add("发运单号");
        titles.add("汇款票号");
        titles.add("汇款金额");
        titles.add("汇款方式");
        titles.add("汇款日期");
        titles.add("退款方式");
        titles.add("退款金额");

        List<List<Object>> datas = new ArrayList<List<Object>>();
        PageResponseDTO<ROrderDetailsResponse> rdor = super.queryOrderExport(rQueryOrderRequest);
        for (ROrderDetailsResponse shor : rdor.getResult()) {
            List<Object> data = new ArrayList<Object>();
            data.add(shor.getsOrderDetailsMain().getId());
            data.add("");
            data.add("");
            data.add("");
            data.add("");
            data.add("");
            data.add("");
            data.add("");
            data.add("");
            String contactName = shor.getsOrderDetailsMain().getContactName();
            data.add(StringUtil.isBlank(contactName) ? "" : contactName);
            String contactPhone = shor.getsOrderDetailsMain().getContactPhone();
            data.add(StringUtil.isBlank(contactPhone) ? "" : contactPhone);
            data.add("");
            String chnlAddress = shor.getsOrderDetailsMain().getChnlAddress();
            data.add(StringUtil.isBlank(chnlAddress) ? "" : chnlAddress);
            data.add("");
            long expressFee = shor.getsOrderDetailsMain().getRealExpressFee();
            data.add(Double.parseDouble(MoneyUtil.convertCentToYuan(expressFee)));
            data.add("");
            data.add(BaseParamUtil.fetchParamValue("STAFF_SHOP_DISTRIBUTION_WAY",
                    shor.getsOrderDetailsMain().getDispatchType()));
            if (shor.getsOrderDetailsDeliverys().size() > 0) {
                SOrderDetailsDelivery delivery = shor.getsOrderDetailsDeliverys().get(0);
                String expressNo = delivery.getExpressNo();
                String expressName = delivery.getExpressName();
                expressName = (StringUtil.isBlank(expressName)?"":expressName+" ");
                data.add(StringUtil.isBlank(expressNo) ? "" : expressName + expressNo);
            } else {
                data.add("");
            }

            String orderId = shor.getsOrderDetailsMain().getId();
            data.add(shor.getsOrderDetailsMain().getId().substring(orderId.length() - 4));
            long realMoney = shor.getsOrderDetailsMain().getRealMoney();
            data.add(Double.parseDouble(MoneyUtil.convertCentToYuan(realMoney)));
            data.add(BaseParamUtil.fetchParamValue("ORD_PAY_TYPE",
                    shor.getsOrderDetailsMain().getPayType()));
            Timestamp payTime = shor.getsOrderDetailsMain().getPayTime();
            String dateTime = "";
            if(payTime!=null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateTime = dateFormat.format(new Date(payTime.getTime()));
            }
            data.add(dateTime);
            data.add("");
            data.add("");
            datas.add(data);
        }
        String fileId = DataInoutUtil.exportExcel(datas, titles, "Order", fileType, moduleName, operator);
//        FileUtil.saveFile(fileName, fileType);
//        String localFileName = "E:\\1.xlsx";
//        FileUtil.readFile(fileId, localFileName);
        
        LogUtil.info(MODULE, "======================"+fileId);
        RExportExcleResponse rer = new RExportExcleResponse();
        rer.setFileId(fileId);
        return rer;
    }

    @Override
    public RExportExcleResponse queryOrderBarCode(RQueryOrderRequest queryOrderRequest)
            throws BusinessException {
        RExportExcleResponse response = new RExportExcleResponse();
        Long oper = queryOrderRequest.getStaff().getId();
        String fileName = oper.toString();
        String fileType = "xls";
        String moduleName = "BarCode";
        String operator = oper.toString();
        List<String> titles = new ArrayList<String>();
        titles.add("单号");
        titles.add("序号");
        titles.add("条码");
        titles.add("数量");
        titles.add("定价");
        titles.add("折扣");
        titles.add("订单码洋");
        titles.add("订单实洋");

        LogUtil.info(MODULE, "=====导出订单条码入参======="+JSON.toJSONString(queryOrderRequest));
        List<List<Object>> datas = new ArrayList<List<Object>>();
        PageResponseDTO<ROrdBarCodeResponse> rdor = queryOrdBarCodeInfo(queryOrderRequest);
        Long serial = 0l;
        for (ROrdBarCodeResponse barCode : rdor.getResult()) {
            List<Object> data = new ArrayList<Object>();
            data.add(barCode.getOrderId());
            serial ++;
            data.add(serial);
            data.add(barCode.getBarCode());
            data.add(barCode.getOrderAmount());
            data.add(Double.parseDouble(barCode.getBasePrice()));
            data.add(barCode.getDiscount());
            data.add(Double.parseDouble(barCode.getBasePrice())*barCode.getOrderAmount());
            data.add(Double.parseDouble(barCode.getRealMoney()));
            datas.add(data);
        }

        String fileId = DataInoutUtil.exportExcel(datas, titles, "0", fileType, moduleName, operator);

        LogUtil.info(MODULE, "======================条码文件Id"+fileId+"======================");
        response.setFileId(fileId);
        return response;
    }

    @Override
    public RExportExcleResponse exportGoodSaleExcel(RGoodSaleRequest rGoodSaleRequest) {
        LogUtil.info(MODULE, "=====导出销售明细入参======="+JSON.toJSONString(rGoodSaleRequest));
        Long oper = rGoodSaleRequest.getStaff().getId();
        String fileName = "Sale"+"("+DateUtil.getDateString(rGoodSaleRequest.getBegDate(), DateUtil.YYYYMMDD)+"-"+DateUtil.getDateString(DateUtil.getOffsetDaysDate(rGoodSaleRequest.getEndDate(),-1), DateUtil.YYYYMMDD)+")";
        String fileType = "xls";
        String moduleName = "GoodSaleForOrdSub";
        String operator = oper.toString();
        List<String> titles = new ArrayList<String>();
        titles.add("订单编号");
        titles.add("商品名称");
//        titles.add("ISBN号");
        titles.add("书号");
        titles.add("产品一级分类");
        titles.add("产品二级分类");
        titles.add("产品三级分类");
        titles.add("产品四级分类");
        titles.add("购买单价");
        titles.add("购买数量");
        titles.add("购买总金额");
        titles.add("是否活动商品");
        titles.add("购买日期");
        titles.add("会员名");
        titles.add("联系人");
        titles.add("联系电话");
        titles.add("联系地址");
        List<List<Object>> datas = new ArrayList<List<Object>>();
        List<RGoodSaleResponse> shors = ordSubSV.queryGoodSaleExport(rGoodSaleRequest);
        for (RGoodSaleResponse shor : shors) {
            List<Object> data = new ArrayList<Object>();
            String orderId = shor.getOrderId();

            data.add(shor.getOrderId()==null?"":shor.getOrderId());
            data.add(shor.getGdsName()==null?"":shor.getGdsName());
//            data.add(shor.getIsbn()==null?"":shor.getIsbn());
            data.add(shor.getZsCode()==null?"":shor.getZsCode());
            data.add(shor.getCategory01()==null?"":shor.getCategory01());
            data.add(shor.getCategory02()==null?"":shor.getCategory02());
            data.add(shor.getCategory03()==null?"":shor.getCategory03());
            data.add(shor.getCategory04()==null?"":shor.getCategory04());
            data.add(shor.getDiscountPrice() == null ? 0l : OrderUtils.doubleDiv(shor.getDiscountPrice()));
            data.add(shor.getOrderAmount()==null?0l:shor.getOrderAmount());
            data.add(shor.getOrderMoney() == null ? 0l : OrderUtils.doubleDiv(shor.getOrderMoney()));
            data.add(shor.getIsProm() == null ? "" : shor.getIsProm()==true?"是":"否");

            Timestamp orderTime = shor.getOrderTime();
            String dateTime = "";
            if(orderTime!=null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateTime = dateFormat.format(new Date(orderTime.getTime()));
            }
            data.add(dateTime);

            data.add(shor.getStaffCode() == null ? "" : shor.getStaffCode());
            data.add(shor.getContactName() == null ? "" : shor.getContactName());
            data.add(shor.getContactPhone() == null ? "" : shor.getContactPhone());
            data.add(shor.getChnlAddress() == null ? "" : shor.getChnlAddress());

            datas.add(data);
        }
        String fileId = DataInoutUtil.exportExcel(datas, titles, fileName, fileType, moduleName, operator);
//        FileUtil.saveFile(fileName, fileType);
//        String localFileName = "E:\\1.xlsx";
//        FileUtil.readFile(fileId, localFileName);

        LogUtil.info(MODULE, "======================"+fileId);
        RExportExcleResponse rer = new RExportExcleResponse();
        rer.setFileId(fileId);
        return rer;

    }
}

