package com.ai.ecp.busi.order.file.controller;

import com.ai.ecp.busi.order.file.vo.RExportFileReqVO;
import com.ai.ecp.busi.order.file.vo.RExportFileRespVO;
import com.ai.ecp.busi.order.sub.vo.RGoodSaleReqVO;
import com.ai.ecp.busi.order.vo.RDelyOrderReqVO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.order.dubbo.dto.*;
import com.ai.ecp.order.dubbo.interfaces.IOrdExportRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Controller
@RequestMapping(value="/ordExport")
public class ExportController {

    @Resource
    private IOrdExportRSV ordExportRSV;

    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;

    @Resource
    private IStaffUnionRSV staffUnionRSV;

    @Resource
    private IGdsCategoryRSV gdsCategoryRSV;

    private static final String MODULE = ExportController.class.getName();

    @RequestMapping(value="/exportPage")
    public String exportFilePage(@RequestParam("exportInfo")String exportInfo, Model model){
        model.addAttribute("exportInfo",exportInfo);
        return "/order/export/export-page";
    }

    @RequestMapping(value="/exportPageOrd")
    public String exportFilePageOrd(@RequestParam("exportInfo")String exportInfo, Model model){
        model.addAttribute("exportInfo",exportInfo);
        return "/order/export/export-pageord";
    }
    @RequestMapping(value = "/exportKey")
    @ResponseBody
    public RExportFileRespVO exportKey(RGoodSaleReqVO vo){
    	if(!StringUtils.isNotBlank(vo.getOrderRull())){
    		vo.setOrderRull("1");
    	}
        RExportFileRespVO resp = new RExportFileRespVO();
        
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

            RExportFileResp rfr = ordExportRSV.creatSaleDetailFileKey(rGoodSaleRequest);
            resp.setKey(rfr.getKey());
            resp.setResultFlag(resp.RESULT_FLAG_SUCCESS);
        }catch (BusinessException e){
            LogUtil.error(MODULE,"============订单模板导出异常==========");
            resp.setResultFlag(resp.RESULT_FLAG_EXCEPTION);
        }

        return resp;
    }
    @RequestMapping(value = "/exportProgress")
    @ResponseBody
    public RExportFileRespVO exportProgress(RExportFileReqVO vo){
        RExportFileRespVO resp = new RExportFileRespVO();
        try{

            RExportFileReq rExportFileReq = new RExportFileReq();
            rExportFileReq.setKey(Long.parseLong(vo.getKey()));
            RExportFileResp rfr = ordExportRSV.querySaleDetailFileKeyProgress(rExportFileReq);
            resp.setKey(rfr.getKey());
            resp.setFileId(rfr.getFileId());
            resp.setCompleteFlag(rfr.getCompleteFlag());
            resp.setProgress(rfr.getProgress());
            resp.setResultFlag(resp.RESULT_FLAG_SUCCESS);
        }catch (BusinessException e){
            LogUtil.error(MODULE,"============订单模板导出异常==========");
            resp.setResultFlag(resp.RESULT_FLAG_EXCEPTION);
        }
        return resp;
    }

    /**
     * 导出单头/明细
     * @param vo
     * @return
     */
    @RequestMapping(value = "/exportKeyOrd")
    @ResponseBody
    public RExportFileRespVO exportKeyDt(RDelyOrderReqVO vo){
        RExportFileRespVO resp = new RExportFileRespVO();

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
            RExportFileResp rfr = ordExportRSV.creatQueryOrderFileKey(r);
            resp.setKey(rfr.getKey());
            resp.setResultFlag(resp.RESULT_FLAG_SUCCESS);
        }catch (BusinessException e){
            LogUtil.error(MODULE,"============订单模板导出异常==========");
            resp.setResultFlag(resp.RESULT_FLAG_EXCEPTION);
        }

        return resp;
    }
}
