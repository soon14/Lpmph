package com.ai.ecp.busi.pub.controller;
import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.util.ParamsTool;
import com.ai.ecp.order.dubbo.dto.PubOrdOffReviewReqDTO;
import com.ai.ecp.order.dubbo.dto.PubOrderSummaryResponse;
import com.ai.ecp.order.dubbo.dto.PubOrderZdRequsetDTO;
import com.ai.ecp.order.dubbo.dto.PubOrderZdResponseDTO;
import com.ai.ecp.order.dubbo.interfaces.IPubOrderZdRSV;
import com.ai.ecp.pmph.busi.pub.vo.PubOrdOffReviewReqVO;
import com.ai.ecp.pmph.busi.pub.vo.PubOrderZdVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage 征订单查询控制器<br>
 * DesXZcription: <br>
 * Date:2018年8月6日上午9:55:42  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value="/pub/order")
public class PubOrderController extends EcpBaseController{
    
    private static String MODULE = PubOrderController.class.getName();
    
    @Resource
    private IPubOrderZdRSV pubOrderZdRSV;
    /**
     * 征订单查询初始化页面
     */
    @RequestMapping(value = "/managegrid")
    public String manageGrid(Model model) {
        //开始时间和结束时间都默认当天
        model.addAllAttributes(ParamsTool.paramsToday());
        return "/pub/order/pub-order-grid";
    }
    
    /**
     * 征订单查询 管理员全表查询方法
     * 
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping("/gridlist")
    @ResponseBody
    public Model gridList(Model model, PubOrderZdVO vo) throws Exception {
        // 后场服务所需要的DTO；
        PubOrderZdRequsetDTO r = new PubOrderZdRequsetDTO();
        r = vo.toBaseInfo(PubOrderZdRequsetDTO.class);
        ObjectCopyUtil.copyObjValue(vo, r, null, false);
        r.setId(vo.getPubOrderId());
        r.setStatus(vo.getPubStatus());
        r.setEndDate(new Timestamp(DateUtils.addDays(r.getEndDate(), 1).getTime()));
        PageResponseDTO<PubOrderZdResponseDTO> resp = pubOrderZdRSV.queryPubOrderZd(r);
        EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
        if (resp != null) {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(resp);
        }
        return super.addPageToModel(model, respVO);
    }
    @RequestMapping("/pubOrdersum")
    @ResponseBody
    public PubOrderSummaryResponse pubOrderSummaryData(Model model, PubOrderZdVO vo) throws Exception {
        // 后场服务所需要的DTO；
        PubOrderZdRequsetDTO r = new PubOrderZdRequsetDTO();
        r = vo.toBaseInfo(PubOrderZdRequsetDTO.class);
        ObjectCopyUtil.copyObjValue(vo, r, "", false);
        r.setId(vo.getPubOrderId());
        r.setStatus(vo.getPubStatus());
        r.setEndDate(new Timestamp(DateUtils.addDays(r.getEndDate(), 1).getTime()));
        PubOrderSummaryResponse response = pubOrderZdRSV.queryPubOrderSummaryData(r);
        return response;
    }
    
    //线下支付审核
    @RequestMapping(value="/offlinesave")
    @ResponseBody
    public EcpBaseResponseVO offlineSave(@Valid PubOrdOffReviewReqVO vo){
        LogUtil.info(MODULE, "============审核方法开始=============");
        PubOrdOffReviewReqDTO reqDTO=new PubOrdOffReviewReqDTO();
        ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);
        EcpBaseResponseVO resp = new EcpBaseResponseVO();
        try {
            //检查征订单状态是否正确
            String pubOrderId = reqDTO.getPubOrderId();
            boolean flag = pubOrderZdRSV.checkPubOrderStatus(pubOrderId);
            if(!flag) throw new BusinessException("征订单状态异常!");
            pubOrderZdRSV.pubOrderOfflineReview(reqDTO);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        }catch(BusinessException bus){
            LogUtil.error(MODULE, "============出错了============="+bus.getMessage());
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(bus.getMessage());
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了============="+e.getMessage());
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        LogUtil.info(MODULE, "============审核方法结束=============");
        return resp;
    }
}

