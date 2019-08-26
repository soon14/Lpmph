package com.ai.ecp.pmph.busi.seller.staff.controller;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.order.dubbo.util.LongUtils;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctAdjustReqVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctAdjustReqVO2;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctApplyMoneyReqVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctExpendReqVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctExpendReqVO2;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctIncomeReqVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctIncomeReqVO2;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctWithdrawReqVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctWithdrawReqVO2;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.SellerResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAppMoneyDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBackDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBackDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctIncomeDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctIncomeDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawTrackReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawTrackResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.system.filter.SellerLocaleUtil;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.MoneyUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;


/**
 * 
 * Title: SHOP <br>
 * Project Name:pmph-web-mall <br>
 * Description: 店铺账户账务明细查询:收入、支出、调账、提现<br>
 * Date:2018年5月18日下午5:38:10  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author myf
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value="/seller/shopmgr/shopAcctDetail")
public class ShopAcctDetailController extends EcpBaseController{
	
	private static String MODULE = ShopAcctDetailController.class.getName();
	
	@Resource
    private IStaffUnionRSV staffUnionRSV;
	
	@Resource
	private IShopAcctRSV shopAcctRSV;
	
	private static final String SELLER_SHOPACCT_DETAIL_VM_PATH = "/seller/staff/shopacctdetail";
	
	/**
	 * 
	 * init:(账务明细页面初始化 ). <br/> 
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
    public String init(Model model,@RequestParam(value="shopId", required=false)Long shopId) throws Exception{
    	SellerResDTO sellerResDTO = SellerLocaleUtil.getSeller(); //通过工具类获取登录会员信息
        //通过sellerResDTO.getShoplist().get(0)返回第一个List<ShopInfoResDTO>对象，getId()获取shopid
        if(shopId==null||shopId==0){
        	shopId = sellerResDTO.getShoplist().get(0).getId();
        }
    	// 店铺id
    	model.addAttribute("shopId", shopId);
    	model.addAttribute("payBegDate",new Timestamp(new Date().getTime()));
        model.addAttribute("payEndDate",new Timestamp(new Date().getTime()));
        model.addAttribute("backBegDate",new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("backEndDate",new Timestamp(new Date().getTime()));
        model.addAttribute("adjBegDate",new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("adjEndDate",new Timestamp(new Date().getTime()));
        model.addAttribute("withdrawBegDate",new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("withdrawEndDate",new Timestamp(new Date().getTime()));
        // 初始化查询收入明细
        ShopAcctIncomeReqVO inncomeVO = new ShopAcctIncomeReqVO();
        inncomeVO.setShopId(shopId);
        ShopAcctIncomeDetailReqDTO incomeReqDTO = inncomeVO.toBaseInfo(ShopAcctIncomeDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(inncomeVO, incomeReqDTO, null, false);		
    	incomeReqDTO.setBegDate(new Timestamp(new Date().getTime()));
    	incomeReqDTO.setEndDate(new Timestamp(DateUtils.addDays(new Date(), 1).getTime()));
    	PageResponseDTO<ShopAcctIncomeDetailResDTO> incomeResp = shopAcctRSV.queryIncomeDetail(incomeReqDTO);
    	model.addAttribute("incomeResp", incomeResp);
    	// 初始化查询支出明细列表
    	ShopAcctExpendReqVO expendVO = new ShopAcctExpendReqVO();
    	expendVO.setShopId(shopId);
    	ShopAcctBackDetailReqDTO expendReqDTO = expendVO.toBaseInfo(ShopAcctBackDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(expendVO, expendReqDTO, null, false);	
    	expendReqDTO.setBegDate(new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
    	expendReqDTO.setEndDate(new Timestamp(DateUtils.addDays(new Date(), 1).getTime()));
    	PageResponseDTO<ShopAcctBackDetailResDTO> expendResp = shopAcctRSV.queryExpendDetail(expendReqDTO);
    	model.addAttribute("expendResp", expendResp);
    	// 初始化查询调账明细列表
    	ShopAcctAdjustReqVO adjustVO = new ShopAcctAdjustReqVO();
    	adjustVO.setShopId(shopId);
    	ShopAcctAdjustDetailReqDTO adjustReqDTO = adjustVO.toBaseInfo(ShopAcctAdjustDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(adjustVO, adjustReqDTO, null, false);	
    	adjustReqDTO.setBegDate(new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
    	adjustReqDTO.setEndDate(new Timestamp(DateUtils.addDays(new Date(), 1).getTime()));
    	PageResponseDTO<ShopAcctAdjustDetailResDTO> adjustResp = shopAcctRSV.queryAdjustDetail(adjustReqDTO);
    	model.addAttribute("adjustResp", adjustResp);
    	// 初始化查询提现明细列表
    	ShopAcctWithdrawReqVO withdrawVO = new ShopAcctWithdrawReqVO();
    	withdrawVO.setShopId(shopId);
    	ShopAcctWithdrawDetailReqDTO withdrawReqDTO = withdrawVO.toBaseInfo(ShopAcctWithdrawDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(withdrawVO, withdrawReqDTO, null, false);		
    	withdrawReqDTO.setBegDate(new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
    	withdrawReqDTO.setEndDate(new Timestamp(DateUtils.addDays(new Date(), 1).getTime()));
    	PageResponseDTO<ShopAcctWithdrawDetailResDTO> withdrawResp = shopAcctRSV.queryWithdrawDetail(withdrawReqDTO);
    	model.addAttribute("withdrawResp", withdrawResp);
        return SELLER_SHOPACCT_DETAIL_VM_PATH+"/seller-shopacct-detail";
    }
    
    /**
     * 
     * incomelist:(收入明细列表). <br/> 
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
    @RequestMapping("/incomelist")
    public String incomelist(Model model, ShopAcctIncomeReqVO vo) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctIncomeDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctIncomeDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);	
    	reqDTO.setEndDate(new Timestamp(DateUtils.addDays(reqDTO.getEndDate(), 1).getTime()));
    	PageResponseDTO<ShopAcctIncomeDetailResDTO> resp = null;
    	// 根据输入的下单人的姓名查询其ID号
    	if (StringUtil.isNotBlank(vo.getOrderStaffCode())) {
    		 CustInfoReqDTO CustInfoReqDTO  = new CustInfoReqDTO();
             CustInfoReqDTO.setStaffCode(vo.getOrderStaffCode());
             CustInfoResDTO  custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
             if(custInfoResDTO == null){
            	 model.addAttribute("incomeResp", resp);
                 return SELLER_SHOPACCT_DETAIL_VM_PATH + "/page/seller-shopacct-income-list";
             } else {
                 reqDTO.setOrderStaff(custInfoResDTO.getId());
             }
		}
    	resp = shopAcctRSV.queryIncomeDetail(reqDTO);
    	model.addAttribute("incomeResp", resp);

    	return SELLER_SHOPACCT_DETAIL_VM_PATH + "/page/seller-shopacct-income-list";
    }
    
    /**
     * 
     * expendlist:(支出明细列表). <br/> 
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
    @RequestMapping("/expendlist")
    public String expendlist(Model model, ShopAcctExpendReqVO vo) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctBackDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctBackDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);		
    	reqDTO.setEndDate(new Timestamp(DateUtils.addDays(reqDTO.getEndDate(), 1).getTime()));
    	PageResponseDTO<ShopAcctBackDetailResDTO> resp = null;
    	// 根据输入的下单人的姓名查询其ID号
    	if (StringUtil.isNotBlank(vo.getApplyStaffCode())) {
    		 CustInfoReqDTO   CustInfoReqDTO  = new CustInfoReqDTO();
             CustInfoReqDTO.setStaffCode(vo.getApplyStaffCode());
             CustInfoResDTO  custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
             if(custInfoResDTO == null){
            	 model.addAttribute("expendResp", resp);
                 return SELLER_SHOPACCT_DETAIL_VM_PATH + "/page/seller-shopacct-expend-list";
             } else {
                 reqDTO.setApplyStaff(custInfoResDTO.getId());
             } 
		}
    	resp = shopAcctRSV.queryExpendDetail(reqDTO);
    	model.addAttribute("expendResp", resp);

    	return SELLER_SHOPACCT_DETAIL_VM_PATH + "/page/seller-shopacct-expend-list";
    }
    
    /**
     * 
     * adjustlist:(调账明细列表). <br/> 
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
    @RequestMapping("/adjustlist")
    public String adjustlist(Model model, ShopAcctAdjustReqVO vo) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctAdjustDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctAdjustDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);		
    	reqDTO.setEndDate(new Timestamp(DateUtils.addDays(reqDTO.getEndDate(), 1).getTime()));
    	PageResponseDTO<ShopAcctAdjustDetailResDTO> resp = shopAcctRSV.queryAdjustDetail(reqDTO);
    	model.addAttribute("adjustResp", resp);

    	return SELLER_SHOPACCT_DETAIL_VM_PATH + "/page/seller-shopacct-adjust-list";
    }
    
    /**
     * 
     * withdrawlist:(提现明细列表). <br/> 
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
    @RequestMapping("/withdrawlist")
    public String withdrawlist(Model model, ShopAcctWithdrawReqVO vo) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctWithdrawDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctWithdrawDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);	
    	reqDTO.setEndDate(new Timestamp(DateUtils.addDays(reqDTO.getEndDate(), 1).getTime()));
    	PageResponseDTO<ShopAcctWithdrawDetailResDTO> resp = shopAcctRSV.queryWithdrawDetail(reqDTO);
    	model.addAttribute("withdrawResp", resp);

    	return SELLER_SHOPACCT_DETAIL_VM_PATH + "/page/seller-shopacct-withdraw-list";
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
    @RequestMapping("/withdrawApplyDetail/{applyId}")
    public String withdrawApplyDetail(Model model,@PathVariable("applyId") Long applyId) throws Exception {
    	if (applyId == null || applyId == 0L) {
    		LogUtil.info(MODULE, "入参对象不能为空");
			throw new BusinessException(StaffConstants.STAFF_NULL_ERROR);
		}
    	ShopAcctApplyMoneyReqVO vo = new ShopAcctApplyMoneyReqVO();
    	vo.setApplyId(applyId);
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
        
    	return "/seller/staff/shopacctdetail/shopacct-withdraw-detail";
    }
    
    /**
     * 
     * exportIncomeExcel:(导出订单收入数据). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author myf 
     * @param response
     * @param cardBindVO 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/exportIncomeExcel", method = RequestMethod.POST)
    public void exportIncomeExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctIncomeReqVO2 vo) {
    	try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "订单收入数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + ParamsTool.encodeChineseDownloadFileName(request,fileName));
            String[] titles = { "收入流水", "收入类型","订单编号", "商户订单号", "支付时间", "支付方式",
            		"支付通道","实付金额","手续费","入账金额","下单人" };
            List titleList = new ArrayList();
            for (int i = 0; i < titles.length; i++) {
                titleList.add(titles[i]);
            }
            List<ShopAcctIncomeDetailResDTO> list2 = queryIncomeData(vo);

            this.exportExcelIncome(titles, response, list2);
        } catch (IOException e) {
            LogUtil.error(MODULE, "shopWhitelist exportExcel", e);
        }
    }
    
    private void exportExcelIncome(String[] titles, HttpServletResponse response, List<ShopAcctIncomeDetailResDTO> data) {
    	// 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("订单收入");
        XSSFCellStyle headStyle = this.getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = this.getBodyStyle(workBook);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        if (data != null && data.size() > 0) {
            for (int j = 0; j < data.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                ShopAcctIncomeDetailResDTO resDTO = data.get(j);
                
                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getId()));
                
                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                String optTypeName = "-";
                if (StringUtil.isNotBlank(resDTO.getOptType())) {
                	if ("11".equals(resDTO.getOptType())) {
                		optTypeName = "在线支付";
					} else {
						optTypeName = "线下支付审核";
					}
				}
                cell.setCellValue(optTypeName);
                
                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getOrderId()==null?'-':resDTO.getOrderId()));
                
                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getPayTranNo()==null?'-':resDTO.getPayTranNo()));
                
                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                String payTime = "";
                if(resDTO.getPayTime() != null){
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	payTime = dateFormat.format(new Date(resDTO.getPayTime().getTime()));
                }
                cell.setCellValue(isNull(payTime)); 
                
                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                String payTypeName = "-";
                if (StringUtil.isNotBlank(resDTO.getPayType())) {
                	if ("0".equals(resDTO.getPayType())) {
                		payTypeName = "线上支付";
					} else if ("1".equals(resDTO.getPayType())) {
						payTypeName = "上门支付";
					} else if ("2".equals(resDTO.getPayType())) {
						payTypeName = "邮局汇款";
					} else if ("3".equals(resDTO.getPayType())) {
						payTypeName = "银行转账";
					}
				}
                cell.setCellValue(payTypeName);
                
                cell = bodyRow.createCell(6);
                cell.setCellStyle(bodyStyle);
                String payWay = resDTO.getPayWay();
                String payWayName = "-";
                if(StringUtil.isNotBlank(payWay) && !payWay.equals("null")){
                    payWayName = this.getPayWayName(payWay);
                }
                cell.setCellValue(payWayName);
                
                cell = bodyRow.createCell(7);
                cell.setCellStyle(bodyStyle);
                String realMoney = "0.00";
                if (resDTO.getRealMoney() != null) {
                	realMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getRealMoney()))+"";
				}
                cell.setCellValue(realMoney);
                
                cell = bodyRow.createCell(8);
                cell.setCellStyle(bodyStyle);
                String fee = "0.00";
                if (resDTO.getFee() != null) {
                	fee = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getFee()))+"";
				}
                cell.setCellValue(fee);
                
                cell = bodyRow.createCell(9);
                cell.setCellStyle(bodyStyle);
                String inMoney = "0.00";
                if (resDTO.getInMoney() != null) {
                	inMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getInMoney()))+"";
				}
                cell.setCellValue(inMoney);
                
                cell = bodyRow.createCell(10);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getOrderStaffCode()==null?'-':resDTO.getOrderStaffCode()));
            }
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workBook.write(outputStream);
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
                LogUtil.error(MODULE, "exportExcel", e);
            }
        }

    }

	private List<ShopAcctIncomeDetailResDTO> queryIncomeData(ShopAcctIncomeReqVO2 incomeVO) {
		// 后场服务所需要的DTO
    	ShopAcctIncomeDetailReqDTO reqDTO = incomeVO.toBaseInfo(ShopAcctIncomeDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(incomeVO, reqDTO, null, false);	
    	
    	List<ShopAcctIncomeDetailResDTO> list = new ArrayList<ShopAcctIncomeDetailResDTO>();
    	PageResponseDTO<ShopAcctIncomeDetailResDTO> resp = new PageResponseDTO<ShopAcctIncomeDetailResDTO>();
    	// 支付开始日期
    	if (incomeVO.getPayBegDate() != null) {
    		Timestamp begDate = new Timestamp(incomeVO.getPayBegDate().getTime());
			reqDTO.setBegDate(begDate);
		}
    	// 支付结束日期
    	if (incomeVO.getPayEndDate() != null) {
//    		Timestamp endDate = new Timestamp(incomeVO.getPayEndDate().getTime());
//			reqDTO.setEndDate(endDate);
    		reqDTO.setEndDate(new Timestamp(DateUtils.addDays(incomeVO.getPayEndDate(), 1).getTime()));
		}
    	// 订单编号
    	if (StringUtil.isNotBlank(incomeVO.getInOrderId())) {
			reqDTO.setOrderId(incomeVO.getInOrderId());
		}
    	// 商户订单号
    	if (StringUtil.isNotBlank(incomeVO.getInPayTranNo())) {
			reqDTO.setPayTranNo(incomeVO.getInPayTranNo());
		}
    	// 收入流水号
    	if (LongUtils.isNotEmpty(incomeVO.getIncomeId())) {
			reqDTO.setId(incomeVO.getIncomeId());
		}
    	// 店铺ID
    	if (LongUtils.isNotEmpty(incomeVO.getInShopId())) {
			reqDTO.setShopId(incomeVO.getInShopId());
		}
    	// 支付方式
    	if (StringUtil.isNotBlank(incomeVO.getInPayType())) {
			reqDTO.setPayType(incomeVO.getInPayType());
		}
    	// 支付通道
    	if (StringUtil.isNotBlank(incomeVO.getInPayWay())) {
			reqDTO.setPayWay(incomeVO.getInPayWay());
		}
    	// 根据输入的下单人的姓名查询其ID号
    	if (StringUtil.isNotBlank(incomeVO.getOrderStaffCode())) {
    		 CustInfoReqDTO CustInfoReqDTO  = new CustInfoReqDTO();
             CustInfoReqDTO.setStaffCode(incomeVO.getOrderStaffCode());
             CustInfoResDTO  custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
             if(custInfoResDTO == null){
            	 resp.setPageNo(reqDTO.getPageNo());
            	 resp.setPageSize(reqDTO.getPageSize());
            	 resp.setPageCount(0);
            	 resp.setCount(0);
            	 resp.setResult(null);
            	 return resp.getResult();
             } else {
                 reqDTO.setOrderStaff(custInfoResDTO.getId());
             }
		}
    	reqDTO.setPageSize(Integer.MAX_VALUE);
    	resp = shopAcctRSV.queryIncomeDetail(reqDTO);
    	if (null != resp.getResult()) {
            return resp.getResult();
        }

        return list;
	}

	/**
     * 
     * exportExpendExcel:(导出退货退款支出数据). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author myf 
     * @param response
     * @param cardBindVO 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/exportExpendExcel", method = RequestMethod.POST)
    public void exportExpendExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctExpendReqVO2 vo) {
    	try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "退货退款明细数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + ParamsTool.encodeChineseDownloadFileName(request,fileName));
            String[] titles = { "支出流水", "支出类型","退款编号","订单编号", "商户订单号", "退款时间", "退款通道",
            		"退款方式","退款金额","手续费","支出金额","退款人" };
            List titleList = new ArrayList();
            List<List<Object>> dataList = new ArrayList();
            for (int i = 0; i < titles.length; i++) {
                titleList.add(titles[i]);
            }
            List<ShopAcctBackDetailResDTO> list2 = queryExpendData(vo);

            this.exportExcelExpend(titles, response, list2);
        } catch (IOException e) {
            LogUtil.error(MODULE, "shopWhitelist exportExcel", e);
        }
    }
    
    private void exportExcelExpend(String[] titles, HttpServletResponse response,
			List<ShopAcctBackDetailResDTO> data) {
    	// 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("退货退款");
        XSSFCellStyle headStyle = this.getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = this.getBodyStyle(workBook);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        if (data != null && data.size() > 0) {
            for (int j = 0; j < data.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                ShopAcctBackDetailResDTO resDTO = data.get(j);
                
                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getId()));
                
                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                String optTypeName = "-";
                if (StringUtil.isNotBlank(resDTO.getOptType())) {
                	if ("21".equals(resDTO.getOptType())) {
                		optTypeName = "退款支出";
					} else {
						optTypeName = "退货支出";
					}
				}
                cell.setCellValue(optTypeName);
                
                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getBackId()==null?'-':resDTO.getBackId()));
                
                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getOrderId()==null?'-':resDTO.getOrderId()));
                
                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getPayTranNo()==null?'-':resDTO.getPayTranNo()));
                
                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                String backTime = "";
                if(resDTO.getBackTime() != null){
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	backTime = dateFormat.format(new Date(resDTO.getBackTime().getTime()));
                }
                cell.setCellValue(isNull(backTime)); 
                
                cell = bodyRow.createCell(6);
                cell.setCellStyle(bodyStyle);
                String payWay = resDTO.getPayWay();
                String payWayName = "-";
                if(StringUtil.isNotBlank(payWay) && !payWay.equals("null")){
                	if ("2000".equals(payWay)) {
                		payWayName = "线下退款";
					} else {
						payWayName = this.getPayWayName(payWay);
					}
                }
                cell.setCellValue(payWayName);
                
                cell = bodyRow.createCell(7);
                cell.setCellStyle(bodyStyle);
                String payTypeName = "-";
                if (StringUtil.isNotBlank(resDTO.getPayType())) {
                	if ("0".equals(resDTO.getPayType())) {
                		payTypeName = "线下退款";
					} else {
						payTypeName = "线上退款";
					} 
				}
                cell.setCellValue(payTypeName);
                
                cell = bodyRow.createCell(8);
                cell.setCellStyle(bodyStyle);
                String backMoney = "0.00";
                if (resDTO.getBackMoney() != null) {
                	backMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getBackMoney()))+"";
				}
                cell.setCellValue(backMoney);
                
                cell = bodyRow.createCell(9);
                cell.setCellStyle(bodyStyle);
                String fee = "0.00";
                if (resDTO.getFee() != null) {
                	fee = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getFee()))+"";
				}
                cell.setCellValue(fee);
                
                cell = bodyRow.createCell(10);
                cell.setCellStyle(bodyStyle);
                String expendMoney = "0.00";
                if (resDTO.getExpendMoney() != null) {
                	expendMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getExpendMoney()))+"";
				}
                cell.setCellValue(expendMoney);
                
                cell = bodyRow.createCell(11);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getApplyStaffCode()==null?'-':resDTO.getApplyStaffCode()));
            }
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workBook.write(outputStream);
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
                LogUtil.error(MODULE, "exportExcel", e);
            }
        }
	}

	private List<ShopAcctBackDetailResDTO> queryExpendData(ShopAcctExpendReqVO2 expendVO) {
		// 后场服务所需要的DTO
    	ShopAcctBackDetailReqDTO reqDTO = expendVO.toBaseInfo(ShopAcctBackDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(expendVO, reqDTO, null, false);		
    	
    	List<ShopAcctBackDetailResDTO> list = new ArrayList<ShopAcctBackDetailResDTO>();
    	PageResponseDTO<ShopAcctBackDetailResDTO> resp = new PageResponseDTO<ShopAcctBackDetailResDTO>();
    	// 退款开始时间
    	if (expendVO.getBackBegDate() != null) {
    		Timestamp begDate = new Timestamp(expendVO.getBackBegDate().getTime());
			reqDTO.setBegDate(begDate);
		}
    	// 退款结束时间
    	if (expendVO.getBackEndDate() != null) {
    		/*Timestamp endDate = new Timestamp(expendVO.getBackEndDate().getTime());
			reqDTO.setEndDate(endDate);*/
    		reqDTO.setEndDate(new Timestamp(DateUtils.addDays(expendVO.getBackEndDate(), 1).getTime()));
		}
    	// 订单编号
    	if (StringUtil.isNotBlank(expendVO.getBackOrderId())) {
			reqDTO.setOrderId(expendVO.getBackOrderId());
		}
    	// 商户订单号
    	if (StringUtil.isNotBlank(expendVO.getBackPayTranNo())) {
			reqDTO.setPayTranNo(expendVO.getBackPayTranNo());
		}
    	// 退款方式
    	if (StringUtil.isNotBlank(expendVO.getBackType())) {
			reqDTO.setPayType(expendVO.getBackType());
		}
    	// 退款通道
    	if (StringUtil.isNotBlank(expendVO.getBackWay())) {
			reqDTO.setPayWay(expendVO.getBackWay());
		}
    	// 支出类型
    	if (StringUtil.isNotBlank(expendVO.getBackOptType())) {
			reqDTO.setOptType(expendVO.getBackOptType());
		}
    	// 支出流水号
    	if (LongUtils.isNotEmpty(expendVO.getBackDetailId())) {
			reqDTO.setId(expendVO.getBackDetailId());
		}
    	// 店铺ID
    	if (LongUtils.isNotEmpty(expendVO.getBackShopId())) {
			reqDTO.setShopId(expendVO.getBackShopId());
		}
    	// 根据输入的下单人的姓名查询其ID号
    	if (StringUtil.isNotBlank(expendVO.getApplyStaffCode())) {
    		 CustInfoReqDTO   CustInfoReqDTO  = new CustInfoReqDTO();
             CustInfoReqDTO.setStaffCode(expendVO.getApplyStaffCode());
             CustInfoResDTO  custInfoResDTO = staffUnionRSV.findCustInfo(CustInfoReqDTO);
             if(custInfoResDTO == null){
            	 resp.setPageNo(reqDTO.getPageNo());
            	 resp.setPageSize(reqDTO.getPageSize());
            	 resp.setPageCount(0);
            	 resp.setCount(0);
            	 resp.setResult(null);
             	 return resp.getResult();
             } else {
                 reqDTO.setApplyStaff(custInfoResDTO.getId());
             } 
		}
    	reqDTO.setPageSize(Integer.MAX_VALUE);
    	resp = shopAcctRSV.queryExpendDetail(reqDTO);
    	if (null != resp.getResult()) {
            return resp.getResult();
        }

        return list;
	}

	/**
     * 
     * exportAdjustExcel:(导出调账数据). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author myf 
     * @param response
     * @param cardBindVO 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/exportAdjustExcel", method = RequestMethod.POST)
    public void exportAdjustExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctAdjustReqVO2 vo) {
    	try { 
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "调账数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + ParamsTool.encodeChineseDownloadFileName(request,fileName));
            String[] titles = { "调账流水号", "调账类型","调账申请ID", "调账时间", "调账结算日", "调账结算月",
            		"调账金额","申请人"};
            List titleList = new ArrayList();
            List<List<Object>> dataList = new ArrayList();
            for (int i = 0; i < titles.length; i++) {
                titleList.add(titles[i]);
            }
            List<ShopAcctAdjustDetailResDTO> list2 = queryAdjustData(vo);

            this.exportExcelAdjust(titles, response, list2);
        } catch (IOException e) {
            LogUtil.error(MODULE, "shopWhitelist exportExcel", e);
        }
    }
    
    private void exportExcelAdjust(String[] titles, HttpServletResponse response,
			List<ShopAcctAdjustDetailResDTO> data) {
       	// 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("调账");
        XSSFCellStyle headStyle = this.getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = this.getBodyStyle(workBook);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        if (data != null && data.size() > 0) {
            for (int j = 0; j < data.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                ShopAcctAdjustDetailResDTO resDTO = data.get(j);
                
                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getId()));
                
                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                String optTypeName = "-";
                if (StringUtil.isNotBlank(resDTO.getOptType())) {
                	if ("31".equals(resDTO.getOptType())) {
                		optTypeName = "调账收入";
					} else {
						optTypeName = "调账支出";
					}
				}
                cell.setCellValue(optTypeName);
                
                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getAdjId()==null?'-':resDTO.getAdjId()));
                
                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                String adjTime = "";
                if(resDTO.getAdjTime() != null){
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	adjTime = dateFormat.format(new Date(resDTO.getAdjTime().getTime()));
                }
                cell.setCellValue(isNull(adjTime)); 
                
                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getBillDay()==null?'-':resDTO.getBillDay()));
                
                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getBillMonth()==null?'-':resDTO.getBillMonth()));
               
                cell = bodyRow.createCell(6);
                cell.setCellStyle(bodyStyle);
                String adjMoney = "0.00";
                if (resDTO.getAdjMoney() != null) {
                	adjMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getAdjMoney()))+"";
				}
                cell.setCellValue(adjMoney);
                
                cell = bodyRow.createCell(7);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getApplyStaffCode()==null?'-':resDTO.getApplyStaffCode()));
            }
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workBook.write(outputStream);
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
                LogUtil.error(MODULE, "exportExcel", e);
            }
        }
	}

	private List<ShopAcctAdjustDetailResDTO> queryAdjustData(ShopAcctAdjustReqVO2 adjustVO) {
		// 后场服务所需要的DTO
    	ShopAcctAdjustDetailReqDTO reqDTO = adjustVO.toBaseInfo(ShopAcctAdjustDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(adjustVO, reqDTO, null, false);		
    	List<ShopAcctAdjustDetailResDTO> list = new ArrayList<ShopAcctAdjustDetailResDTO>();
    	if (adjustVO.getAdjBegDate() != null) {
    		Timestamp begDate = new Timestamp(adjustVO.getAdjBegDate().getTime());
			reqDTO.setBegDate(begDate);
		}
    	if (adjustVO.getAdjEndDate() != null) {
    		/*Timestamp endDate = new Timestamp(adjustVO.getAdjEndDate().getTime());
			reqDTO.setEndDate(endDate);*/
    		reqDTO.setEndDate(new Timestamp(DateUtils.addDays(adjustVO.getAdjEndDate(), 1).getTime()));
		}
    	// 调账类型
    	if (StringUtil.isNotBlank(adjustVO.getAdjOptType())) {
			reqDTO.setOptType(adjustVO.getAdjOptType());
		}
    	// 调账流水号
    	if (LongUtils.isNotEmpty(adjustVO.getAdjDetailId())) {
			reqDTO.setId(adjustVO.getAdjDetailId());
		}
    	// 店铺ID
    	if (LongUtils.isNotEmpty(adjustVO.getAdjShopId())) {
			reqDTO.setShopId(adjustVO.getAdjShopId());
		}
    	reqDTO.setPageSize(Integer.MAX_VALUE);
    	PageResponseDTO<ShopAcctAdjustDetailResDTO> resp = shopAcctRSV.queryAdjustDetail(reqDTO);
    	if (null != resp.getResult()) {
            return resp.getResult();
        }

        return list;
	}

	/**
     * 
     * exportWithdrawExcel:(导出提现数据). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author myf 
     * @param response
     * @param cardBindVO 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/exportWithdrawExcel", method = RequestMethod.POST)
    public void exportWithdrawExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctWithdrawReqVO2 vo) {
    	try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "提现数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + ParamsTool.encodeChineseDownloadFileName(request,fileName));
            String[] titles = { "提现流水号", "提现申请ID","申请时间", "申请人", "提现日期", "提现金额"};
            List titleList = new ArrayList();
            List<List<Object>> dataList = new ArrayList();
            for (int i = 0; i < titles.length; i++) {
                titleList.add(titles[i]);
            }
            List<ShopAcctWithdrawDetailResDTO> list2 = queryWithdrawData(vo);

            this.exportExcelWithdraw(titles, response, list2);
        } catch (IOException e) {
            LogUtil.error(MODULE, "shopWhitelist exportExcel", e);
        }
    }
    
    private void exportExcelWithdraw(String[] titles, HttpServletResponse response, List<ShopAcctWithdrawDetailResDTO> data) {
       	// 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("提现");
        XSSFCellStyle headStyle = this.getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = this.getBodyStyle(workBook);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        if (data != null && data.size() > 0) {
            for (int j = 0; j < data.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                ShopAcctWithdrawDetailResDTO resDTO = data.get(j);
                
                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getId()));
                
                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getApplyId()==null?'-':resDTO.getApplyId()));
                
                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                String applyTime = "";
                if(resDTO.getApplyTime() != null){
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	applyTime = dateFormat.format(new Date(resDTO.getApplyTime().getTime()));
                }
                cell.setCellValue(isNull(applyTime)); 
                
                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getApplyStaffCode()==null?'-':resDTO.getApplyStaffCode()));
                
                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                String withdrawTime = "";
                if(resDTO.getWithdrawTime() != null){
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	withdrawTime = dateFormat.format(new Date(resDTO.getWithdrawTime().getTime()));
                }
                cell.setCellValue(isNull(withdrawTime)); 
                
                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                String withdrawMoney = "0.00";
                if (resDTO.getWithdrawMoney() != null) {
                	withdrawMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getWithdrawMoney()))+"";
				}
                cell.setCellValue(withdrawMoney);
                
            }
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workBook.write(outputStream);
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
                LogUtil.error(MODULE, "exportExcel", e);
            }
        }
    }

	private List<ShopAcctWithdrawDetailResDTO> queryWithdrawData(ShopAcctWithdrawReqVO2 withdrawVO) {
		// 后场服务所需要的DTO
    	ShopAcctWithdrawDetailReqDTO reqDTO = withdrawVO.toBaseInfo(ShopAcctWithdrawDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(withdrawVO, reqDTO, null, false);	
    	List<ShopAcctWithdrawDetailResDTO> list = new ArrayList<ShopAcctWithdrawDetailResDTO>();
    	if (withdrawVO.getWithdrawBegDate() != null) {
    		Timestamp begDate = new Timestamp(withdrawVO.getWithdrawBegDate().getTime());
			reqDTO.setBegDate(begDate);
		}
    	if (withdrawVO.getWithdrawEndDate() != null) {
    		/*Timestamp endDate = new Timestamp(withdrawVO.getWithdrawEndDate().getTime());
			reqDTO.setEndDate(endDate);*/
    		reqDTO.setEndDate(new Timestamp(DateUtils.addDays(withdrawVO.getWithdrawEndDate(), 1).getTime()));
		}
    	// 提现流水号
    	if (LongUtils.isNotEmpty(withdrawVO.getWithdrawId())) {
			reqDTO.setId(withdrawVO.getWithdrawId());
		}
    	// 店铺ID
    	if (LongUtils.isNotEmpty(withdrawVO.getWithdrawShopId())) {
			reqDTO.setShopId(withdrawVO.getWithdrawShopId());
		}
    	reqDTO.setPageSize(Integer.MAX_VALUE);
    	PageResponseDTO<ShopAcctWithdrawDetailResDTO> resp = shopAcctRSV.queryWithdrawDetail(reqDTO);
    	if (null != resp.getResult()) {
            return resp.getResult();
        }

        return list;
	}

	//支付通道名称
    private String getPayWayName(String payWay){
        String payWayName = "-";
        if(payWay.equals("9002")){
            payWayName = "鸿支付";
        }else if(payWay.equals("9003")){
            payWayName = "支付宝";
        }else if(payWay.equals("9004")){
            payWayName = "农行支付";
        }else if(payWay.equals("9006")){
            payWayName = "微信支付";
        }else if(payWay.equals("9007")){
            payWayName = "微信扫码支付";
        }else if (payWay.equals("9008")) {
        	payWayName = "微信APP支付";
		}
        return payWayName;
    }  
    
    /**
     * 设置表头的单元格样式
     * 
     * @return
     */
    private XSSFCellStyle getHeadStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格的背景颜色为淡蓝色
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 设置表体的单元格样式
     * 
     * @return
     */
    private XSSFCellStyle getBodyStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
       // font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }
    
    //空字符以-显示
    private String isNull(String str){ 
    	if(StringUtil.isBlank(str) || str.equals("null")){
    		str="-";
    	}
    	return str;
    }

}
