/** 
 * File Name:DemoController.java 
 * Date:2015-8-5下午2:51:38 
 * 
 */
package com.ai.ecp.busi.order.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.ai.ecp.busi.order.file.vo.RExportFileReqVO;
import com.ai.ecp.busi.order.file.vo.RExportFileRespVO;
import com.ai.ecp.busi.order.vo.*;
import com.ai.ecp.order.dubbo.interfaces.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.busi.order.sub.vo.RGoodSaleReqVO;
import com.ai.ecp.busi.order.util.OrdConstant;
import com.ai.ecp.busi.order.util.ParamsTool;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.order.dubbo.dto.RDeliveryPrintRequest;
import com.ai.ecp.order.dubbo.dto.RDeliveryPrintResponse;
import com.ai.ecp.order.dubbo.dto.RExportExcleResponse;
import com.ai.ecp.order.dubbo.dto.RGoodSaleRequest;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsResponse;
import com.ai.ecp.order.dubbo.dto.ROrderDetialPrintReq;
import com.ai.ecp.order.dubbo.dto.ROrderGiftsResponse;
import com.ai.ecp.order.dubbo.dto.ROrderIdRequest;
import com.ai.ecp.order.dubbo.dto.ROrderSummaryResponse;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.dto.RShopOrderResponse;
import com.ai.ecp.order.dubbo.dto.SOrderDetailsMain;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * Project Name:ecp-web-demo <br>
 * Description: <br>
 * Date:2015-8-5下午2:51:38  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */

/**
 * @since JDK 1.7
 * @notice 功能 待发货订单查询=delyedList 订单发货明细查询=queryOrder 已发货订单查询=searchList 订单详情=queryOrderDetails
 *         买家确认=buyerConfirm 加入购物车 收货评价 发货人信息
 * @throws Exception
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends EcpBaseController {

    private static String MODULE = OrderController.class.getName();

    @Resource(name = "ordMainRSV")
    private IOrdMainRSV ordMainRSV;

    @Resource(name = "ordManageRSV")
    private IOrdManageRSV ordManageRSV;

    @Resource(name = "ordSubRSV")
    private IOrdSubRSV ordSubRSV;

    @Resource(name = "ordDetailsRSV")
    private IOrdDetailsRSV ordDetailsRSV;
    

    @Resource
    private IShopInfoRSV shopInfoRSV;

    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;

    @Resource
    private IOrdDeliveryRSV ordDeliveryRSV;

    @Resource
    private IGdsCategoryRSV gdsCategoryRSV; 
    
    @Resource
    private IStaffUnionRSV staffUnionRSV;



    /**
     * 默认进入代发货和已发货页面 init:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 
     * @param model
     * @return
     * @since JDK 1.6
     */
    @RequestMapping()
    public String init(Model model) {
        model.addAllAttributes(ParamsTool.params());
        return "/order/search/search-grid";
    }


    /**
     * 管理员全表查询订单列表初始化页面
     */
    @RequestMapping(value = "/managegrid")
    public String manageGrid(Model model) {
//        model.addAllAttributes(ParamsTool.params());   //需求要求修改成默认当天 #11561
        model.addAllAttributes(ParamsTool.paramsToday());
        return "/order/order-grid";
    }

    // 避免空指针异常
    @SuppressWarnings("rawtypes")
    private EcpBasePageRespVO<Map> bulidPageResp(PageResponseDTO<RShopOrderResponse> t)
            throws Exception {
        EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
        if (t != null) {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
        }
        return respVO;
    }
    
    /**
     * 
     * 已发货订单查询
     * 
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping("/delyedlist")
    @ResponseBody
    public Model delyedList(Model model, RDelyOrderReqVO vo) throws Exception {
    	String status = OrdConstants.ShopRequestStatus.REQUEST_STATUS_DELY;
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = queryList(model,vo,status);

        return super.addPageToModel(model, ParamsTool.ordDetailSiteUrl(respVO));
    }

    public EcpBasePageRespVO<Map> queryList(Model model, RDelyOrderReqVO vo, String status) throws Exception {
    	// 后场服务所需要的DTO；
        RQueryOrderRequest r = new RQueryOrderRequest();
        r = vo.toBaseInfo(RQueryOrderRequest.class);
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
//        r.setShopId(ParamsTool.getShopId());
        r.setSysType("00");
//        r.setCategoryCodes(ParamsTool.getCategoryCodes());
        r.setCategoryCodes(null);
        r.setStatus(status);
        // 其它的查询条件；
        LogUtil.debug(MODULE, vo.toString());

        PageResponseDTO<RShopOrderResponse> t = ordMainRSV.queryOrderByShopId(r);

        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = bulidPageResp(t);
        
        return ParamsTool.setSiteUrl(respVO);
    }

    /**
     * 待发货订单查询
     * 
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping("/searchlist")
    @ResponseBody
    public Model searchList(Model model, RDelyOrderReqVO vo) throws Exception {
    	String status = OrdConstants.ShopRequestStatus.REQUEST_STATUS_SEND;
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
    	EcpBasePageRespVO<Map> respVO = queryList(model,vo,status);

    	return super.addPageToModel(model, ParamsTool.ordDetailSiteUrl(respVO));
    }

    /**
     * 订单查询 管理员全表查询方法
     * 
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping("/gridlist")
    @ResponseBody
    public Model gridList(Model model, RDelyOrderReqVO vo) throws Exception {
    	if(!StringUtils.isNotBlank(vo.getOrderRull())){
    		vo.setOrderRull("1");
    	}
    	// 后场服务所需要的DTO；
        RQueryOrderRequest r = new RQueryOrderRequest();
        r = vo.toBaseInfo(RQueryOrderRequest.class);
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
        r.setSysType("00");
        if(StringUtils.isNotBlank(vo.getRealMoney())){
        	 BigDecimal maxlng1 = new BigDecimal(vo.getRealMoney());
	   		 BigDecimal flag = new BigDecimal(100);
	   		 BigDecimal b = maxlng1.multiply(flag);
	   		 Long money = b.longValue();
	   		 r.setRealMoney(money);
        }
        if("".equals(vo.getRealMoney())){
        	r.setExt1("1");
        }
        GdsCategoryReqDTO gdsdto = new GdsCategoryReqDTO();
        GdsCategoryRespDTO gdsresp = new GdsCategoryRespDTO();
        if (!StringUtil.isBlank(vo.getCategoryCode())) {
            gdsdto.setCatgCode(vo.getCategoryCode());
            gdsresp = gdsCategoryRSV.queryGdsCategoryByPK(gdsdto);
        }
        if(StringUtil.isNotBlank(vo.getStaffCode())){
            CustInfoReqDTO   CustInfoReqDTO  = new CustInfoReqDTO();
            CustInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO  custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
            if(custInfoResDTO == null){
                PageResponseDTO<RShopOrderResponse> t = PageResponseDTO.buildByBaseInfo(r, RShopOrderResponse.class);

                // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
                EcpBasePageRespVO<Map> respVO = ParamsTool.ordDetailSiteUrl(bulidPageResp(t));
                return super.addPageToModel(model, respVO);
            } else {
                r.setStaffId(custInfoResDTO.getId());
            }
            
        }
//        r.setStatus(OrdConstants.CustomerRequestStatus.REQUEST_STATUS_ALL);
        
        r.setCategoryCodes(null);
        // 其它的查询条件；
        LogUtil.debug(MODULE, vo.toString());
        
        PageResponseDTO<RShopOrderResponse> t = ordManageRSV.queryOrder(r);

        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = ParamsTool.ordDetailSiteUrl(bulidPageResp(t));

        return super.addPageToModel(model, respVO);
    }

    @RequestMapping(value = "/send")
    public String sendList(Model model,@RequestParam("orderId")String orderId){

        if (StringUtil.isBlank(orderId)) {
            throw new BusinessException("order.orderid.null.error");
        }

        //获取赠品
        ROrderIdRequest orderIdRequest = new ROrderIdRequest();
        orderIdRequest.setOrderId(orderId);
        ROrderGiftsResponse gifts = ordMainRSV.queryOrderGift(orderIdRequest);

        //发票信息打印
        RDeliveryPrintRequest printRequest = new RDeliveryPrintRequest();
        printRequest.setOrderId(orderId);
        RDeliveryPrintResponse printResponse = ordDeliveryRSV.queryInvoiceInfo(printRequest);

        Map<Long, String> logistics = shopInfoRSV.listExpress(Long.valueOf(printResponse.getsOrderDetailsMain().getShopId()));
        LogUtil.info(MODULE, logistics + "");
        // 根据订单查店铺id
        model.addAttribute("logistics", logistics);

        SOrderDetailsMain ordMain = printResponse.getsOrderDetailsMain();
        // ----这里应该把物流或者自提的文本对应信息传递回去
        model.addAttribute("orderId", orderId);
        model.addAttribute("dispatchType", ordMain.getDispatchType());
        model.addAttribute("contactName", ordMain.getContactName());
        model.addAttribute("contactPhone", ordMain.getContactPhone());
        model.addAttribute("contactNumber", ordMain.getContactNumber());
        model.addAttribute("chnlAddress", ordMain.getChnlAddress());
        model.addAttribute("invoiceType", ordMain.getInvoiceType());
        //新增卖家留言和买家留
        model.addAttribute("buyerMsg", ordMain.getBuyerMsg());
        model.addAttribute("sellerMsg", ordMain.getSellerMsg());
        String invoiceTitle = "";
        if(ordMain.getInvoiceType().equals(OrdConstant.InvoiceType.COMM)) invoiceTitle = printResponse.getsOrderDetailsComm().getInvoiceTitle();
        if(ordMain.getInvoiceType().equals(OrdConstant.InvoiceType.TAX)) invoiceTitle = printResponse.getsOrderDetailsTax().getInvoiceTitle();

        model.addAttribute("invoiceTitle",invoiceTitle);

        String invoiceContent = "";
        if(ordMain.getInvoiceType().equals(OrdConstant.InvoiceType.COMM)) invoiceContent = printResponse.getsOrderDetailsComm().getInvoiceContent();

        String detailFlag = "0";
        if(ordMain.getInvoiceType().equals(OrdConstant.InvoiceType.COMM)) detailFlag = printResponse.getsOrderDetailsComm().getDetailFlag();
        model.addAttribute("invoiceContent",invoiceContent);
        model.addAttribute("detailFlag",detailFlag);
        //赠品信息
        model.addAttribute("gifts",gifts);

        return "order/orderSub/orderSub-grid";
    }


    /**
     * 订单详情
     * 
     * @param model
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderdetails")
    public String queryOrderDetails(Model model, @RequestParam("orderId") String orderid) {

        ROrderDetailsRequest rdto = new ROrderDetailsRequest();
        if (StringUtil.isBlank(orderid)) {
            throw new BusinessException("order.orderid.null.error");
        }
        rdto.setOrderId(orderid);
        ROrderDetailsResponse resp = new ROrderDetailsResponse();
        resp = ordDetailsRSV.queryOrderDetails(rdto);

        // 订单id
        model.addAttribute("orderId", orderid);
        // 主订单相关字段
        if(resp.getsOrderDetailsMain().getBuyerMsg() != null){
            resp.getsOrderDetailsMain().setBuyerMsg(resp.getsOrderDetailsMain().getBuyerMsg().replace("\n","<br />"));
        }

        model.addAttribute("sOrderDetailsMain", resp.getsOrderDetailsMain());
        // 子订单相关字段
        model.addAttribute("sOrderDetailsSubs", resp.getsOrderDetailsSubs());
        // 订单优惠相关字段
        model.addAttribute("sOrderDetailsDiscount", resp.getsOrderDetailsDiscount());
        // 订单跟踪相关字段
        model.addAttribute("sOrderDetailsTracks", resp.getsOrderDetailsTracks());
        // 订单收货地址相关字段
        model.addAttribute("sOrderDetailsAddr", resp.getsOrderDetailsAddr());
        // 订单普通发票相关字段
        model.addAttribute("sOrderDetailsComm", resp.getsOrderDetailsComm());
        // 订单增值税发票相关字段
        model.addAttribute("sOrderDetailsTax", resp.getsOrderDetailsTax());
        //物流信息相关字段
        model.addAttribute("sOrderDetailsDeliverys", resp.getsOrderDetailsDeliverys());
        //物流信息相关字段
        model.addAttribute("ordExpressDetailsResps",resp.getOrdExpressDetailsResps());
        
        Map<String, Integer> status = ParamsTool.getStatusMap();
        List<String> statuslist = ParamsTool.getStatusList();
        model.addAttribute("status", status);
        model.addAttribute("statuslist", statuslist);

        return "/order/order-detail";
    }
    
    
    /**
     * 
     * printList:(打印清单页面). <br/> 
     * 
     * @param vo
     * @param model
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value="/printList")
    public String printList(Model model, RDelyOrderReqVO vo){
        // 后场服务所需要的DTO；
        RQueryOrderRequest r = new RQueryOrderRequest();
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
        r.setSysType("00");
        if(StringUtils.isNotBlank(vo.getRealMoney())){
       	 BigDecimal maxlng1 = new BigDecimal(vo.getRealMoney());
	   		 BigDecimal flag = new BigDecimal(100);
	   		 BigDecimal b = maxlng1.multiply(flag);
	   		 Long money = b.longValue();
	   		 r.setRealMoney(money);
        }
        if("".equals(vo.getRealMoney())){
        	r.setExt1("1");
        }
        if(StringUtil.isNotBlank(vo.getStaffCode())){
            CustInfoReqDTO   custInfoReqDTO  = new CustInfoReqDTO();
            custInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO  custInfoResDTO = staffUnionRSV.findCustInfo(custInfoReqDTO);
            if(custInfoResDTO != null){
                r.setStaffId(custInfoResDTO.getId());
            }  
        }
        PageResponseDTO<ROrderDetailsResponse> dto = ordMainRSV.exportOrder2Print(r);
        List<ROrderDetailsResponse> orders = dto.getResult();
        List<Map<String,Object>> orderList = new ArrayList<Map<String,Object>>();
        if(StringUtil.isNotEmpty(orders)){
            for(int i = 0; i < orders.size(); i++){
                Map<String,Object> map = new HashMap<String,Object>();
                ROrderDetailsResponse order = orders.get(i); 
                // 主订单相关字段
                map.put("sOrderDetailsMain", order.getsOrderDetailsMain());
                // 子订单相关字段
                map.put("sOrderDetailsSubs", order.getsOrderDetailsSubs());
                // 订单优惠相关字段
                map.put("sOrderDetailsDiscount", order.getsOrderDetailsDiscount());
                // 订单跟踪相关字段
                map.put("sOrderDetailsTracks", order.getsOrderDetailsTracks());
                // 订单收货地址相关字段
                map.put("sOrderDetailsAddr", order.getsOrderDetailsAddr());
                // 订单普通发票相关字段
                map.put("sOrderDetailsComm", order.getsOrderDetailsComm());
                // 订单增值税发票相关字段
                map.put("sOrderDetailsTax", order.getsOrderDetailsTax());
                orderList.add(map);
            }
        }
        model.addAttribute("orderList", orderList); 
        return "/order/print/order-print"; 
    }

    @RequestMapping(value="/exportDetail")
    public String exportDetail(
            @RequestParam("exportInfo")String exportInfo,
            @RequestParam("exportType")String exportType,
            Model model){
        model.addAttribute("exportInfo",exportInfo);
        model.addAttribute("exportType",exportType);
        return "/order/export/order-export";

    }

    @RequestMapping(value = "/export/{fileId}")
    public void exportExcel(@PathVariable("fileId") String fileId, HttpServletResponse response){
        ServletOutputStream outputStream = null;
        try{
            byte[] bytes=FileUtil.readFile(fileId);
            String fileType=FileUtil.getFileType(fileId);
            String getFileName=FileUtil.getFileName(fileId) ;
            String fileName =getFileName + ".xls";
            if("xlsx".equals(fileType)){
            	fileName = getFileName + ".xlsx";
            }
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
//            response.setHeader("content-disposition","attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("content-disposition","attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/getFileId")
    @ResponseBody
    public RExportExcelResponse getFileId(RDelyOrderReqVO vo){
        RExportExcelResponse resp = new RExportExcelResponse();

        // 后场服务所需要的DTO；
        RQueryOrderRequest r = new RQueryOrderRequest();
        r = vo.toBaseInfo(RQueryOrderRequest.class);
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
        r.setSysType("00");

        GdsCategoryReqDTO gdsdto = new GdsCategoryReqDTO();
        GdsCategoryRespDTO gdsresp = new GdsCategoryRespDTO();
        if (!StringUtil.isBlank(vo.getCategoryCode())) {
            gdsdto.setCatgCode(vo.getCategoryCode());
            gdsresp = gdsCategoryRSV.queryGdsCategoryByPK(gdsdto);
        }

        //搜索会员
        if(StringUtil.isNotBlank(vo.getStaffCode())) {
            CustInfoReqDTO CustInfoReqDTO = new CustInfoReqDTO();
            CustInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
            if(custInfoResDTO!=null) r.setStaffId(custInfoResDTO.getId());
        }

        r.setCategoryCodes(null);
        // 其它的查询条件；
        LogUtil.debug(MODULE, vo.toString());

        try{
            RExportExcleResponse excel = ordMainRSV.exportOrder2Excle(r);
            if(StringUtil.isBlank(excel.getFileId())){
                resp.setFileId(excel.getFileId());
                resp.setResultFlag(resp.RESULT_FLAG_FAILURE);
            }else{
                resp.setFileId(excel.getFileId());
                resp.setResultFlag(resp.RESULT_FLAG_SUCCESS);
            }

        }catch (BusinessException e){
            LogUtil.error(MODULE,"============订单模板导出异常==========");
            resp.setResultFlag(resp.RESULT_FLAG_EXCEPTION);
        }

        return resp;
    }

    @RequestMapping(value = "/getSaleFileId")
    @ResponseBody
    public RExportExcelResponse getFileId(RGoodSaleReqVO vo){
        RExportExcelResponse resp = new RExportExcelResponse();

        // 后场服务所需要的DTO；
        RGoodSaleRequest rGoodSaleRequest = new RGoodSaleRequest();
        rGoodSaleRequest = vo.toBaseInfo(RGoodSaleRequest.class);
        ObjectCopyUtil.copyObjValue(vo, rGoodSaleRequest, "", false);

        //搜索会员
        if(StringUtil.isNotBlank(vo.getStaffCode())) {
            CustInfoReqDTO CustInfoReqDTO = new CustInfoReqDTO();
            CustInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
            if(custInfoResDTO!=null) {
                rGoodSaleRequest.setStaffId(custInfoResDTO.getId());
            }else {
                resp.setResultFlag(resp.RESULT_FLAG_FAILURE);
                resp.setResultMsg("查询无结果");
                return resp;
            }
        }

        if(StringUtil.isNotBlank(vo.getIsbn())){

            List<Long> gdsIds = new ArrayList<Long>();
            GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
            gdsInfoReqDTO.setShopId(vo.getShopId());
            gdsInfoReqDTO.setFullInfo(false);
            gdsInfoReqDTO.setIsbn(vo.getIsbn());
            PageResponseDTO<GdsInfoRespDTO> gdsInfo = gdsInfoQueryRSV.queryGdsInfoListPage(gdsInfoReqDTO);
            if(CollectionUtils.isNotEmpty(gdsInfo.getResult())){
                for(GdsInfoRespDTO gds: gdsInfo.getResult()){
                    gdsIds.add(gds.getId());
                }
                if(CollectionUtils.isNotEmpty(gdsIds)) rGoodSaleRequest.setGdsIds(gdsIds);
            }else{
                resp.setResultFlag(resp.RESULT_FLAG_FAILURE);
                resp.setResultMsg("查询无结果");
                return resp;
            }
        }
        // 其它的查询条件；
        LogUtil.debug(MODULE, vo.toString());

        try{
            RExportExcleResponse excel = ordSubRSV.exportGoodSaleExcel(rGoodSaleRequest);
            if(StringUtil.isBlank(excel.getFileId())){
                resp.setFileId(excel.getFileId());
                resp.setResultFlag(resp.RESULT_FLAG_FAILURE);
            }else{
                resp.setFileId(excel.getFileId());
                resp.setResultFlag(resp.RESULT_FLAG_SUCCESS);
            }

        }catch (BusinessException e){
            LogUtil.error(MODULE,"============订单模板导出异常==========");
            resp.setResultFlag(resp.RESULT_FLAG_EXCEPTION);
        }

        return resp;
    }

    @RequestMapping(value = "/getBarCodeFileId")
    @ResponseBody
    public RExportExcelResponse getBarCodeFileId(RDelyOrderReqVO vo){
        RExportExcelResponse resp = new RExportExcelResponse();

        // 后场服务所需要的DTO；
        RQueryOrderRequest r = new RQueryOrderRequest();
        r = vo.toBaseInfo(RQueryOrderRequest.class);
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
        r.setSysType("00");

        GdsCategoryReqDTO gdsdto = new GdsCategoryReqDTO();
        GdsCategoryRespDTO gdsresp = new GdsCategoryRespDTO();
        if (!StringUtil.isBlank(vo.getCategoryCode())) {
            gdsdto.setCatgCode(vo.getCategoryCode());
            gdsresp = gdsCategoryRSV.queryGdsCategoryByPK(gdsdto);
        }

        //搜索会员
        if(StringUtil.isNotBlank(vo.getStaffCode())) {
            CustInfoReqDTO CustInfoReqDTO = new CustInfoReqDTO();
            CustInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
            if(custInfoResDTO!=null) r.setStaffId(custInfoResDTO.getId());
        }

        r.setCategoryCodes(null);
        // 其它的查询条件；
        LogUtil.debug(MODULE, vo.toString());

        try{
            RExportExcleResponse excel = ordMainRSV.exportOrderBarCode(r);
            if(StringUtil.isBlank(excel.getFileId())){
                resp.setFileId(excel.getFileId());
                resp.setResultFlag(resp.RESULT_FLAG_FAILURE);
            }else{
                resp.setFileId(excel.getFileId());
                resp.setResultFlag(resp.RESULT_FLAG_SUCCESS);
            }

        }catch (BusinessException e){
            LogUtil.error(MODULE, "============订单条码导出异常==========");
            resp.setResultFlag(resp.RESULT_FLAG_EXCEPTION);
        }

        return resp;
    }
    
    @RequestMapping("/ordersum")
    @ResponseBody
    public ROrderSummaryResp orderSummaryData(Model model, RDelyOrderReqVO vo) throws Exception {
        // 后场服务所需要的DTO；
        RQueryOrderRequest r = new RQueryOrderRequest();
        r = vo.toBaseInfo(RQueryOrderRequest.class);
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
        //搜索会员
        if(StringUtil.isNotBlank(vo.getStaffCode())) {
            CustInfoReqDTO CustInfoReqDTO = new CustInfoReqDTO();
            CustInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
            if(custInfoResDTO!=null) r.setStaffId(custInfoResDTO.getId());
        }
        r.setSysType("00");
        r.setCategoryCodes(null);
        if(StringUtils.isNotBlank(vo.getRealMoney())){
        	 BigDecimal maxlng1 = new BigDecimal(vo.getRealMoney());
	   		 BigDecimal flag = new BigDecimal(100);
	   		 BigDecimal b = maxlng1.multiply(flag);
	   		 Long money = b.longValue();
	   		 r.setRealMoney(money);
        }
        if("".equals(vo.getRealMoney())){
        	r.setExt1("1");
        }
        // 其它的查询条件；
        LogUtil.debug(MODULE, vo.toString());
        ROrderSummaryResp rosr = new ROrderSummaryResp();
        if(StringUtil.isNotEmpty(vo.getStaffCode()) && StringUtil.isEmpty(r.getStaffId())){
        	rosr.setSumOrderMoney(0L);
        	rosr.setSumRealMoney(0L);
        	rosr.setOrderCount(0L);
        	rosr.setPayedCount(0L);
        	rosr.setPayedRate(0L);
        }else{
        	 ROrderSummaryResponse  t = ordManageRSV.queryOrderSummaryData(r); 
             ObjectCopyUtil.copyObjValue(t, rosr, "", false);
        }
        return rosr;
    }
    
    /**
     * 
     * printDetail:(销售明细页面). <br/> 
     * 
     * @param orderId
     * @param
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/printDetail")
    public String printDetail(Model model, @RequestParam("orderId") String orderId){
        ROrderDetailsRequest rdto = new ROrderDetailsRequest();
        if (StringUtil.isBlank(orderId)) {
            throw new BusinessException("order.orderid.null.error");
        }
        rdto.setOrderId(orderId);
        ROrderDetailsResponse resp = new ROrderDetailsResponse();
        resp = ordDetailsRSV.queryOrderDetails(rdto);

        // 主订单相关字段
        model.addAttribute("sOrderDetailsMain", resp.getsOrderDetailsMain());
        // 子订单相关字段
        model.addAttribute("sOrderDetailsSubs", resp.getsOrderDetailsSubs());
        // 订单优惠相关字段
        model.addAttribute("sOrderDetailsDiscount", resp.getsOrderDetailsDiscount());
        // 订单跟踪相关字段
        model.addAttribute("sOrderDetailsTracks", resp.getsOrderDetailsTracks());
        // 订单收货地址相关字段
        model.addAttribute("sOrderDetailsAddr", resp.getsOrderDetailsAddr());
        // 订单普通发票相关字段
        model.addAttribute("sOrderDetailsComm", resp.getsOrderDetailsComm());
        // 订单增值税发票相关字段
        model.addAttribute("sOrderDetailsTax", resp.getsOrderDetailsTax());

        return "/order/search/print/print-detail"; 
    }
    
    /**
     * 批量打印销售明细页面
     * @param model
     * @param orderIds
     * @return
     */
    @RequestMapping(value="/printDetailList")
    public String printListDetail(Model model,String orderIds){
    	ROrderDetialPrintReq req = new ROrderDetialPrintReq();
    	req.setOrderIds(orderIds);
    	List<ROrderDetailsResponse> orders = ordMainRSV.findPrintOrderDetails(req);
    	List<Map<String,Object>> orderList = new ArrayList<Map<String,Object>>();
        if(StringUtil.isNotEmpty(orders)){
            for(int i = 0; i < orders.size(); i++){
                 Map<String,Object> map = new HashMap<String,Object>();
                 ROrderDetailsResponse order = orders.get(i); 
                 // 主订单相关字段
                 map.put("sOrderDetailsMain", order.getsOrderDetailsMain());
                 // 子订单相关字段
                 map.put("sOrderDetailsSubs", order.getsOrderDetailsSubs());
                 // 订单优惠相关字段
                 map.put("sOrderDetailsDiscount", order.getsOrderDetailsDiscount());
                 // 订单跟踪相关字段
                 map.put("sOrderDetailsTracks", order.getsOrderDetailsTracks());
                 // 订单收货地址相关字段
                 map.put("sOrderDetailsAddr", order.getsOrderDetailsAddr());
                 // 订单普通发票相关字段
                 map.put("sOrderDetailsComm", order.getsOrderDetailsComm());
                 // 订单增值税发票相关字段
                 map.put("sOrderDetailsTax", order.getsOrderDetailsTax());
                 orderList.add(map);
             }
         }
         model.addAttribute("orderList", orderList); 
    	return "/order/search/print/print-detail-list"; 
    }

}
