package com.ai.ecp.pmph.busi.staff.controller;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.order.dubbo.util.LongUtils;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctAdjustReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctExpendReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctIncomeReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctOptLogReqVO;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctWithdrawReqVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAdjustDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBackDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBackDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctIncomeDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctIncomeDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctOptLogResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawDetailReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawDetailResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.MoneyUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;


/**
 * 
 * Title: SHOP <br>
 * Project Name:pmph-web-manage <br>
 * Description: 后台管理：店铺账户账务明细查询:收入、支出、调账、提现<br>
 * Date:2018年5月18日下午5:38:10  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author myf
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value="/shopmgr/shopAcctDetail")
public class ShopAcctDetailController extends EcpBaseController{
	
	private static String MODULE = ShopAcctDetailController.class.getName();
	
	@Resource
    private IStaffUnionRSV staffUnionRSV;
	
	@Resource
	private IShopAcctRSV shopAcctRSV;
	
	private static final String SHOPACCT_DETAIL_VM_PATH = "/staff/shopacctdetail";
	
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
    public String init(Model model,@RequestParam(required = false) String tab) throws Exception{
    	 //控制页签跳转:默认跳到第一页
        if (StringUtil.isNotBlank(tab)) {
            model.addAttribute("tab", tab);
        } else {
            model.addAttribute("tab", 1);
        }
        model.addAttribute("payBegDate",new Timestamp(new Date().getTime()));
        model.addAttribute("payEndDate",new Timestamp(new Date().getTime()));
        model.addAttribute("backBegDate",new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("backEndDate",new Timestamp(new Date().getTime()));
        model.addAttribute("adjBegDate",new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("adjEndDate",new Timestamp(new Date().getTime()));
        model.addAttribute("withdrawBegDate",new Timestamp(DateUtils.addYears(new Date(), -1).getTime()));
        model.addAttribute("withdrawEndDate",new Timestamp(new Date().getTime()));
        return SHOPACCT_DETAIL_VM_PATH+"/shopacct-detail";
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
    @ResponseBody
    public Model incomelist(Model model, EcpBasePageReqVO vo, @ModelAttribute ShopAcctIncomeReqVO incomeVO) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctIncomeDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctIncomeDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(incomeVO, reqDTO, null, false);		
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
            	 EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(resp);
             	 return super.addPageToModel(model, pageinfo);
             } else {
                 reqDTO.setOrderStaff(custInfoResDTO.getId());
             }
		}
    	resp = shopAcctRSV.queryIncomeDetail(reqDTO);
    	EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(resp);
    	return super.addPageToModel(model, pageinfo);
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
    @ResponseBody
    public Model expendlist(Model model,EcpBasePageReqVO vo, @ModelAttribute ShopAcctExpendReqVO expendVO) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctBackDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctBackDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(expendVO, reqDTO, null, false);		
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
            	 EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(resp);
             	 return super.addPageToModel(model, pageinfo);
             } else {
                 reqDTO.setApplyStaff(custInfoResDTO.getId());
             } 
		}
    	resp = shopAcctRSV.queryExpendDetail(reqDTO);
    	EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(resp);
    	return super.addPageToModel(model, pageinfo);
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
    @ResponseBody
    public Model adjustlist(Model model, EcpBasePageReqVO vo, @ModelAttribute ShopAcctAdjustReqVO adjustVO) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctAdjustDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctAdjustDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(adjustVO, reqDTO, null, false);		
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
    	PageResponseDTO<ShopAcctAdjustDetailResDTO> resp = shopAcctRSV.queryAdjustDetail(reqDTO);
    	EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(resp);
    	return super.addPageToModel(model, pageinfo);
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
    @ResponseBody
    public Model withdrawlist(Model model, EcpBasePageReqVO vo, @ModelAttribute ShopAcctWithdrawReqVO withdrawVO) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctWithdrawDetailReqDTO reqDTO = vo.toBaseInfo(ShopAcctWithdrawDetailReqDTO.class);
    	ObjectCopyUtil.copyObjValue(withdrawVO, reqDTO, null, false);		
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
    	PageResponseDTO<ShopAcctWithdrawDetailResDTO> resp = shopAcctRSV.queryWithdrawDetail(reqDTO);
    	EcpBasePageRespVO<Map> pageinfo = EcpBasePageRespVO.buildByPageResponseDTO(resp);
    	return super.addPageToModel(model, pageinfo);
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
    public void exportIncomeExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctIncomeReqVO vo) {
    	try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "订单收入数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + encodeChineseDownloadFileName(request,fileName));
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
        	// 设置单元格的宽度
//        	sheet.setColumnWidth(i, 6000); //第一个参数代表列id(从0开始),第2个参数代表宽度值  参考 ："2012-08-10"的宽度为2500  
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

	private List<ShopAcctIncomeDetailResDTO> queryIncomeData(ShopAcctIncomeReqVO incomeVO) {
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
    public void exportExpendExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctExpendReqVO vo) {
    	try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "退货退款明细数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + encodeChineseDownloadFileName(request,fileName));
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
        	// 设置单元格的宽度
//        	sheet.setColumnWidth(i, 6000); //第一个参数代表列id(从0开始),第2个参数代表宽度值  参考 ："2012-08-10"的宽度为2500  
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

	private List<ShopAcctBackDetailResDTO> queryExpendData(ShopAcctExpendReqVO expendVO) {
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
    public void exportAdjustExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctAdjustReqVO vo) {
    	try { 
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "调账数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + encodeChineseDownloadFileName(request,fileName));
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
        	// 设置单元格的宽度
//        	sheet.setColumnWidth(i, 6000); //第一个参数代表列id(从0开始),第2个参数代表宽度值  参考 ："2012-08-10"的宽度为2500  
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

	private List<ShopAcctAdjustDetailResDTO> queryAdjustData(ShopAcctAdjustReqVO adjustVO) {
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
    public void exportWithdrawExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctWithdrawReqVO vo) {
    	try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "提现数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + encodeChineseDownloadFileName(request,fileName));
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
        	// 设置单元格的宽度
//        	sheet.setColumnWidth(i, 6000); //第一个参数代表列id(从0开始),第2个参数代表宽度值  参考 ："2012-08-10"的宽度为2500  
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

	private List<ShopAcctWithdrawDetailResDTO> queryWithdrawData(ShopAcctWithdrawReqVO withdrawVO) {
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
    
  //文件名兼容  文件名带中文
    /**
     * pFileName.getBytes("utf-8") 在于调用方法时是否设置了utf-8
     * @param request
     * @param pFileName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeChineseDownloadFileName(
            HttpServletRequest request, String pFileName) throws UnsupportedEncodingException {

        String filename = null;
        String agent = request.getHeader("USER-AGENT");
        if (null != agent){
            if (-1 != agent.indexOf("Firefox")) {//Firefox
                filename = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))+ "?=";
            }else if (-1 != agent.indexOf("Chrome")) {//Chrome
                filename = new String(pFileName.getBytes("utf-8"), "ISO8859-1");
            } else {//IE7+
                filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
                filename = StringUtils.replace(filename, "+", "%20");//替换空格
            }
        } else {
            filename = new String(pFileName.getBytes("utf-8"), "ISO8859-1");
        }
        return filename;
    }
}
