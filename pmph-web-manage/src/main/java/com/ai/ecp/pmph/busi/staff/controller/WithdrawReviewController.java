package com.ai.ecp.pmph.busi.staff.controller;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctApplicationMoneyReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctWithdrawReviewReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctWithdrawVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAppMoneyDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawTrackReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawTrackResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.alibaba.fastjson.JSON;
/**
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: 提现一级、二级审核<br>
 * Date:2018年6月4日下午1:52:09  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author mwz
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value="/withdrawReview")
public class WithdrawReviewController extends EcpBaseController {
    
    @Resource
    private IShopAcctRSV shopAcctRSV;
    
    private static String MODULE = WithdrawReviewController.class.getName();
    /** 
     * withdraw1:提现一级审核. <br/> 
     * @param model
     * @return 
     * @since JDK 1.7
     */ 
    @RequestMapping(value="/withdraw1")
    public String withdraw1(Model model){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
        return "/staff/shopacctwithdraw/withdraw1";
    }
    /** 
     * withdraw2:提现二级审核. <br/> 
     * @param model
     * @return 
     * @since JDK 1.7
     */ 
    @RequestMapping(value="/withdraw2")
    public String withdraw2(Model model){
        model.addAttribute("begDate", new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("endDate", new Timestamp(DateUtils.addYears(new Date(), 0).getTime()));
        return "/staff/shopacctwithdraw/withdraw2";
    }
    
    /**
     * queryWithdrawByStatus:(查询一级、二级待审核的申请提现账单). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author mwz 
     * @param model
     * @param shopAcctWithdrawVO
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping("/queryWithdraw")
    @ResponseBody
    public Model queryWithdrawByStatus(Model model, ShopAcctWithdrawVO shopAcctWithdrawVO) throws Exception {
        LogUtil.info(MODULE, JSON.toJSONString(shopAcctWithdrawVO).toString());
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        try{
            ShopAcctWithdrawApplyReqDTO reqDTO=new ShopAcctWithdrawApplyReqDTO();
            reqDTO=shopAcctWithdrawVO.toBaseInfo(ShopAcctWithdrawApplyReqDTO.class);
            ObjectCopyUtil.copyObjValue(shopAcctWithdrawVO, reqDTO, "", false);
            //查询符合状态的申请单（状态值从前台传的）
            reqDTO.setStatus(shopAcctWithdrawVO.getTabFlag());
            reqDTO.setEndDate(new Timestamp(DateUtils.addDays(shopAcctWithdrawVO.getEndDate(), 1).getTime()));
            //查询符合条件提现申请单
            PageResponseDTO<ShopAcctWithdrawApplyResDTO> resp = this.shopAcctRSV.queryShopAcctWithdrawApply(reqDTO);
    
            EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
            if (resp != null) {
                respVO = EcpBasePageRespVO.buildByPageResponseDTO(resp);
            }
            return super.addPageToModel(model, respVO);
        }catch(Exception e){
            e.printStackTrace();
            return model;
        }
    }
    //点击审核按钮进入---》账户提现申请审核【详情】页面展示
    @RequestMapping(value="/checkDetail")
    public String checkDetail(Long id,Model model){
        ShopAcctApplicationMoneyReqVO vo=new ShopAcctApplicationMoneyReqVO();
        //申请单ID
        vo.setApplyId(id);
        //根据申请单ID查询该申请单的详情
        ShopAcctWithdrawApplyResDTO withdrawApplyResDTO = shopAcctRSV.findWithdrawApplyById(vo.getApplyId());
        if (withdrawApplyResDTO == null) {
            LogUtil.info("根据提现申请编码查询提现申请记录信息出现异常：", null);
            throw new BusinessException(StaffConstants.STAFF_SELECT_ERROR);
        }
        // 店铺全称
        vo.setShopFullName(withdrawApplyResDTO.getShopName());
        //申请提现金额
        vo.setApplicationMoney(withdrawApplyResDTO.getMoney());
        
        //根据店铺ID查询店铺账户详情
        ShopAcctInfoReqDTO reqDTO=new ShopAcctInfoReqDTO();
        Long shopId = withdrawApplyResDTO.getShopId();
        reqDTO.setShopId(shopId);
        ShopAcctInfoResDTO resDTO = shopAcctRSV.findShopAcctInfoResDTOByShopId(reqDTO);
        // 如果是已提现状态，提现前得账户余额应该加上提现金额
        if (withdrawApplyResDTO.getStatus().equals("03")) {
            // 目前账户总额
            Long acctTotal=resDTO.getAcctTotal();
            // 将提现前账户总额存到vo中
            vo.setPrevAcctTotal(acctTotal + withdrawApplyResDTO.getMoney());
            // 提现后账户余额就是当前账户总额
            vo.setAlreadyAcctTotal(acctTotal);
        } else {
            //提现前账户总额
            Long acctTotal=resDTO.getAcctTotal();
            //将提现前账户总额存到vo中
            vo.setPrevAcctTotal(acctTotal);
            //计算提现后账户总额=提现前账户总额-申请提现金额
            Long  alreadyAcctTotal=acctTotal-withdrawApplyResDTO.getMoney();
            vo.setAlreadyAcctTotal(alreadyAcctTotal);
        }
        
        // 结算账期
        List<Integer> billMonths=new ArrayList<>();
        String months = withdrawApplyResDTO.getBillMonths();
        String[] month = months.split(",");
        for (String m : month) {
            Integer billMonth = Integer.parseInt(m);
            billMonths.add(billMonth);
        }
        
        //根据店铺id和结算月查询账户该月的订单详情（订单收入明细、订单退货退款支出明细、调账明细等）
        List<ShopAcctAppMoneyDetailResDTO> list=new ArrayList<>();
        for (Integer billMonth : billMonths) {
            ShopAcctAppMoneyDetailResDTO detailDTO = 
                    shopAcctRSV.findShopAcctAppMoneyDetail(shopId, billMonth);
            list.add(detailDTO);
        }
        
        // 根据提现申请ID查询提现申请进度信息
        ShopAcctWithdrawTrackReqDTO withdrawTrackReqDTO = new ShopAcctWithdrawTrackReqDTO();
        withdrawTrackReqDTO.setApplyId(vo.getApplyId());
        List<ShopAcctWithdrawTrackResDTO> withdrawTrackResps = shopAcctRSV.queryWithdrawTrack(withdrawTrackReqDTO);
        
        //金额详情
        model.addAttribute("reqVO", vo);
        //订单详情
        model.addAttribute("list", list);
        // 操作节点跟踪信息
        model.addAttribute("withdrawTrackResps", withdrawTrackResps);
        //需要传店铺ID、申请单ID、审核前状态
        model.addAttribute("shopId", shopId);
        model.addAttribute("id", id);
        //当前审核单的状态
        String status="";
        //根据withdrawTrackResps中最后一个节点判断该申请单当前状态值。
        String node = withdrawTrackResps.get(withdrawTrackResps.size()-1).getNode();
        //node=00表示一级待审核对应提现申请表中的00:一级待审核  
        //node=0101表示一级审核通过对应提现申请表中的01:二级待审核  
        //node=0102表示同意提现对应提现申请表中的02:同意提现
        if("00".equals(node)){
            status="00";
        }else if("0101".equals(node)){
            status="01";
        }else if("0102".equals(node)){
            status="02";
        }else{
            status="";
        }
        model.addAttribute("status", status);
        return "/staff/shopacctwithdraw/withdrawCheck";
    }
    /**
     * confirmReview:提现一级审核、二级审核通过方法. <br/>
     * @param vo
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value="/confirmReview")
    @ResponseBody
    public EcpBaseResponseVO confirmReview(@Valid ShopAcctWithdrawReviewReqVO vo) {
        LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo));
        EcpBaseResponseVO resp = new EcpBaseResponseVO();
        try {
            ShopAcctWithdrawApplyReqDTO reqDTO=new ShopAcctWithdrawApplyReqDTO();
            reqDTO = vo.toBaseInfo(ShopAcctWithdrawApplyReqDTO.class);
            ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);
            //提现申请ID
            reqDTO.setId(vo.getApplyId());
            if(vo.getStatus().equals("00")) {
                //一级审核通过,修改审核单状态为待二级审核状态
                reqDTO.setStatus("01");
            } else if(vo.getStatus().equals("01")){ //二级审核通过,修改审核单状态为同意提线
                reqDTO.setStatus("02");
            }else{
                throw new BusinessException("提现申请单状态异常");
            }
            LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo));
            this.shopAcctRSV.checkShopAcctWithdrawApply(reqDTO);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
    /**
     * refush:提现一级审核、二级审核不通过方法. <br/>
     * @param vo
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value="/refush")
    @ResponseBody
    public EcpBaseResponseVO refush(@Valid ShopAcctWithdrawReviewReqVO vo) {
        LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo));
        EcpBaseResponseVO resp = new EcpBaseResponseVO();
        try {
            ShopAcctWithdrawApplyReqDTO reqDTO=new ShopAcctWithdrawApplyReqDTO();
            reqDTO = vo.toBaseInfo(ShopAcctWithdrawApplyReqDTO.class);
            ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);
            //提现申请ID
            reqDTO.setId(vo.getApplyId());
            //将状态为[00:待一级审核]、[01:待二级审核]的提现申请单状态修改为[04:拒绝提现]
            String status = vo.getStatus();
            if("00".equals(status)||"01".equals(status)){
                reqDTO.setStatus("04");
            }else{
                throw new BusinessException("提现申请单状态异常");
            }
            LogUtil.info(MODULE, "---------------"+JSON.toJSONString(vo));
            this.shopAcctRSV.checkShopAcctWithdrawApply(reqDTO);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        }catch(Exception e){
            LogUtil.error(MODULE, "============出错了=============",e);
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
        return resp;
    }
}

