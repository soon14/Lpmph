package com.ai.ecp.busi.seller.order.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.busi.seller.order.vo.RExportExcelRespVO;
import com.ai.ecp.busi.seller.order.vo.RGoodSaleReqVO;
import com.ai.ecp.busi.seller.order.vo.RGoodSaleSumRespVO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.order.dubbo.dto.RExportExcleResponse;
import com.ai.ecp.order.dubbo.dto.RGoodSaleRequest;
import com.ai.ecp.order.dubbo.dto.RGoodSaleResponse;
import com.ai.ecp.order.dubbo.dto.RGoodSaleSumResp;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdManageRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdSubRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

@Controller
@RequestMapping(value = "/seller/order/detail")
public class SellerDetailController extends EcpBaseController{
    
    private static String MODULE = SellerDetailController.class.getName();
    
    @Resource
    private IOrdMainRSV ordMainRSV;
    
    @Resource
    private IOrdManageRSV ordManageRSV;
    
    @Resource
    private IStaffUnionRSV staffUnionRSV;
    
    @Resource
    private IGdsCategoryRSV gdsCategoryRSV;
    
    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;
    
    @Resource(name = "ordSubRSV")
    private IOrdSubRSV ordSubRSV;
    
    private static final String SELLER_ORDER_DELIVERY_VM_PATH = "/seller/order";

    @RequestMapping(value="index")
    public String index(Model model) throws Exception{
    	//时间默认为当天时间
        model.addAttribute("begDate", DateUtil.getDate());
        model.addAttribute("endDate", DateUtil.getDate());
        
        return SELLER_ORDER_DELIVERY_VM_PATH + "/seller-detail";
    }
    @RequestMapping("/salesum")
    @ResponseBody
    public RGoodSaleSumRespVO querySaleSumData(RGoodSaleReqVO vo) throws Exception {

        RGoodSaleSumRespVO resp = new RGoodSaleSumRespVO();
        resp.setResultFlag(RGoodSaleSumRespVO.RESULT_FLAG_SUCCESS);

        RGoodSaleSumRespVO sumResp = new RGoodSaleSumRespVO();
        sumResp.setResultFlag(RGoodSaleSumRespVO.RESULT_FLAG_SUCCESS);
        sumResp.setRealMoney(0l);
        sumResp.setSaleNum(0l);
        sumResp.setOrderNum(0l);
        sumResp.setBasicMoney(0l);

        RGoodSaleRequest rGoodSaleRequest = new RGoodSaleRequest();
        ObjectCopyUtil.copyObjValue(vo, rGoodSaleRequest, null, false);
/*        rGoodSaleRequest.setEndDate(new Timestamp(DateUtils.addDays(vo.getEndDate(), 1).getTime()));*/

        //搜索会员
        if(StringUtil.isNotBlank(vo.getStaffCode())) {
            CustInfoReqDTO CustInfoReqDTO = new CustInfoReqDTO();
            CustInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
            if(custInfoResDTO!=null) {
                rGoodSaleRequest.setStaffId(custInfoResDTO.getId());
            }else {
                return sumResp;
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
            }else{
                return sumResp;
            }
            if(CollectionUtils.isNotEmpty(gdsIds)) rGoodSaleRequest.setGdsIds(gdsIds);
        }
        try{
            LogUtil.debug(MODULE, "开始查询销售情况统计=========");

            RGoodSaleSumResp sumResp0 = ordSubRSV.queryGoodSaleSum(rGoodSaleRequest);
            ObjectCopyUtil.copyObjValue(sumResp0,resp,"",false);
        }catch (Exception e){
            resp.setResultFlag(RGoodSaleSumRespVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/detaillist")
    public String queryDetailList(Model model, RGoodSaleReqVO vo) throws Exception{

//        RGoodSaleRequest rGoodSaleRequest = new RGoodSaleRequest();
        RGoodSaleRequest rGoodSaleRequest = vo.toBaseInfo(RGoodSaleRequest.class);
        ObjectCopyUtil.copyObjValue(vo, rGoodSaleRequest, null, false);
/*        rGoodSaleRequest.setEndDate(new Timestamp(DateUtils.addDays(vo.getEndDate(), 1).getTime()));*/
        //搜索会员
        if(StringUtil.isNotBlank(vo.getStaffCode())) {
            CustInfoReqDTO CustInfoReqDTO = new CustInfoReqDTO();
            CustInfoReqDTO.setStaffCode(vo.getStaffCode());
            CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
            if(custInfoResDTO!=null) {
                rGoodSaleRequest.setStaffId(custInfoResDTO.getId());
            }else {
                return SELLER_ORDER_DELIVERY_VM_PATH+"/detaillist/seller-detail-list";
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
                return SELLER_ORDER_DELIVERY_VM_PATH+"/detaillist/seller-detail-list";
            }
        }

        PageResponseDTO<RGoodSaleResponse> resp = ordSubRSV.queryGoodSaleInfo(rGoodSaleRequest);

        model.addAttribute("resp", resp );
        return SELLER_ORDER_DELIVERY_VM_PATH+"/detaillist/seller-detail-list";

    }
    
    @RequestMapping(value="/exportDetail")
    public String exportDetail(@RequestParam("exportInfo")String exportInfo,@RequestParam("exportType")String exportType,Model model){
        model.addAttribute("exportInfo",exportInfo);
        model.addAttribute("exportType",exportType);
        return SELLER_ORDER_DELIVERY_VM_PATH+"/export/order-export";

    }
    
    @RequestMapping(value = "/getSellerDetail")
    @ResponseBody
    public RExportExcelRespVO getFileId(RGoodSaleReqVO vo){
        RExportExcelRespVO resp = new RExportExcelRespVO();

        // 后场服务所需要的DTO；
        RGoodSaleRequest rGoodSaleRequest = new RGoodSaleRequest();
        rGoodSaleRequest = vo.toBaseInfo(RGoodSaleRequest.class);
        ObjectCopyUtil.copyObjValue(vo, rGoodSaleRequest, "", false);
/*        rGoodSaleRequest.setEndDate(new Timestamp(DateUtils.addDays(vo.getEndDate(), 1).getTime()));*/

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
}
