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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctApplicationMoneyReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctWithdrawApplyReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctWithdrawTrackReqVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAppMoneyDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawTrackReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawTrackResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.alibaba.fastjson.JSON;


/**
 * 
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: 后台管理：提现管理<br>
 * Date:2018年5月18日下午5:38:10  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author myf
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value="/shopmgr/shopAcctWithdraw")
public class ShopAcctWithdrawController extends EcpBaseController{
	
	private static String MODULE = ShopAcctWithdrawController.class.getName();
	
	@Resource
    private IStaffUnionRSV staffUnionRSV;
	
	@Resource
	private IShopAcctRSV shopAcctRSV;
	
	/**
	 * 
	 * init:(提现列表初始化). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author myf 
	 * @param model
	 * @return 
	 * @since JDK 1.6
	 */
    @RequestMapping("/index")
    public String init(Model model) throws Exception{
    	model.addAttribute("begDate",new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
    	model.addAttribute("endDate",new Timestamp(new Date().getTime()));
    	return "/staff/shopacctwithdraw/shopacct-withdraw";
    }
    
    /**
     * 
     * withdrawApplyList:(提现申请列表  已处理 未处理). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author myf 
     * @param model
     * @param vo
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping("/withdrawApplyList")
    @ResponseBody
    public Model withdrawApplyList(Model model,@Valid ShopAcctWithdrawApplyReqVO vo ) throws Exception {
    	LogUtil.info(MODULE, JSON.toJSONString(vo).toString());
        // 后场服务所需要的DTO
    	ShopAcctWithdrawApplyReqDTO reqDTO = vo.toBaseInfo(ShopAcctWithdrawApplyReqDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);	
    	reqDTO.setEndDate(new Timestamp(DateUtils.addDays(reqDTO.getEndDate(), 1).getTime()));
    	PageResponseDTO<ShopAcctWithdrawApplyResDTO> resp = shopAcctRSV.queryWithdrawApply(reqDTO);
    	EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
        if (resp != null) {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(resp);
        }
        return super.addPageToModel(model, respVO);
    }
    
    /**
     * 
     * withdrawApplyDetail:(查看提现申请明细). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author myf 
     * @param model
     * @param vo
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping("/withdrawApplyDetail")
    public String withdrawApplyDetail(Model model,@Valid ShopAcctApplicationMoneyReqVO vo) throws Exception {
    	if (vo.getApplyId() == null || vo.getApplyId() == 0L) {
    		LogUtil.info(MODULE, "入参对象不能为空");
			throw new BusinessException(StaffConstants.STAFF_NULL_ERROR);
		}
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
            // 体现后账户余额就是当前账户总额
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
        
    	return "/staff/shopacctwithdraw/shopacct-withdraw-detail";
    }
    
    /**
     * 
     * confirmWithdraw:(确认提现得操作). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author myf 
     * @param vo
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value="/confirmWithdraw")
    @ResponseBody
    public EcpBaseResponseVO confirmWithdraw(@Valid ShopAcctWithdrawTrackReqVO vo) throws Exception{
    	LogUtil.info(MODULE, "============确认提现方法开始=============");
    	EcpBaseResponseVO resp = new EcpBaseResponseVO();
    	if (vo.getApplyId() ==null) {
    		LogUtil.info(MODULE, "入参对象不能为空");
			throw new BusinessException(StaffConstants.STAFF_NULL_ERROR);
		}
    	ShopAcctWithdrawTrackReqDTO trackReqDTO = new ShopAcctWithdrawTrackReqDTO();
    	ObjectCopyUtil.copyObjValue(vo, trackReqDTO, null, false);
    	ShopAcctWithdrawDetailReqDTO detailReqDTO = new ShopAcctWithdrawDetailReqDTO();
    	detailReqDTO.setApplyId(vo.getApplyId());
    	try {
    	    //确认提现之前先判断账户余额是否大于或等于申请提现金额,如果大于允许提线,否则不允许提现
    	    boolean flag = shopAcctRSV.withdrawCheck(vo.getApplyId());
    	    if(!flag){
    	        resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
    	        resp.setResultMsg("您好,店铺可用余额不足,暂时不能提现！");
    	        return resp;
    	    }
    	    //将申请表中的状态改为03，向明细表中插入一条数据，更新店铺账户余额，记录日志，向提现跟踪表中插入一条数据等信息
			shopAcctRSV.saveShopAcctWithdrawOpt(detailReqDTO);
			resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch(BusinessException bus){
        	LogUtil.error(MODULE, "============出错了============="+bus.getMessage());
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(bus.getMessage());
        }catch(Exception e){
        	LogUtil.error(MODULE, "============出错了============="+e.getMessage());
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage());
        }
    	LogUtil.info(MODULE, "============确认提现方法结束=============");
    	return resp;
    	
    }
    
}
