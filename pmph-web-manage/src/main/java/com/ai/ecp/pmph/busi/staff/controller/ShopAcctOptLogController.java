package com.ai.ecp.pmph.busi.staff.controller;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.order.dubbo.util.LongUtils;
import com.ai.ecp.pmph.busi.staff.vo.ShopAcctOptLogReqVO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.staff.dubbo.dto.ShopAcctOptLogReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctOptLogResDTO;
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
 * Description: 店铺账户账务交易日志查询<br>
 * Date:2018年6月9日下午5:38:10  <br>
 * Copyright (c) 2018 pmph All Rights Reserved <br>
 * 
 * @author myf
 * @version  
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value="/shopmgr/shopAcctOptLog")
public class ShopAcctOptLogController extends EcpBaseController{
	
	private static String MODULE = ShopAcctOptLogController.class.getName();
	
	@Resource
    private IStaffUnionRSV staffUnionRSV;
	
	@Resource
	private IShopAcctRSV shopAcctRSV;
	
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
    public String init(Model model) throws Exception{
    	model.addAttribute("begDate", DateUtil.getDate());
        model.addAttribute("endDate", DateUtil.getDate());
        return "/staff/shopacctoptlog/shopacct-opt-log"; 
    }
    
    /**
     * 
     * optLoglist:(账户交易日志查询). <br/> 
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
    @RequestMapping("/optLoglist")
    @ResponseBody
    public Model optLoglist(Model model,@Valid ShopAcctOptLogReqVO vo) throws Exception {
        // 后场服务所需要的DTO
    	ShopAcctOptLogReqDTO reqDTO = vo.toBaseInfo(ShopAcctOptLogReqDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);
    	reqDTO.setEndDate(new Timestamp(DateUtils.addDays(reqDTO.getEndDate(), 1).getTime()));
    	PageResponseDTO<ShopAcctOptLogResDTO> resp = shopAcctRSV.queryOptLogList(reqDTO);
    	EcpBasePageRespVO<Map> respVO = new EcpBasePageRespVO<Map>();
        if (resp != null) {
            respVO = EcpBasePageRespVO.buildByPageResponseDTO(resp);
        }
        return super.addPageToModel(model, respVO);
    }
    
    /**
     * 
     * exportOptLogExcel:(导出交易日志数据). <br/> 
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
    @RequestMapping(value = "/exportOptLogExcel", method = RequestMethod.POST)
    public void exportOptLogExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute ShopAcctOptLogReqVO vo) {
    	try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "交易日志数据导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + encodeChineseDownloadFileName(request,fileName));
            String[] titles = { "交易流水号", "收支流水号","订单编号", "商户订单号", "发生时间", "店铺名称",
            		"收入金额","支出金额","店铺账户余额","支付通道","操作类型" };
            List titleList = new ArrayList();
            List<List<Object>> dataList = new ArrayList();
            for (int i = 0; i < titles.length; i++) {
                titleList.add(titles[i]);
            }
            List<ShopAcctOptLogResDTO> list2 = queryOptLogData(vo);

            dataList = this.getOptLogDataList(list2);
            this.exportExcelOptLog(titles, response, list2);
        } catch (IOException e) {
            LogUtil.error(MODULE, "shopWhitelist exportExcel", e);
        }
    }
    
    
    private List<ShopAcctOptLogResDTO> queryOptLogData(ShopAcctOptLogReqVO vo) {
    	// 后场服务所需要的DTO
    	ShopAcctOptLogReqDTO reqDTO = vo.toBaseInfo(ShopAcctOptLogReqDTO.class);
    	ObjectCopyUtil.copyObjValue(vo, reqDTO, null, false);
    	List<ShopAcctOptLogResDTO> list = new ArrayList<ShopAcctOptLogResDTO>();
    	reqDTO.setEndDate(new Timestamp(DateUtils.addDays(reqDTO.getEndDate(), 1).getTime()));
    	reqDTO.setPageSize(Integer.MAX_VALUE);
    	PageResponseDTO<ShopAcctOptLogResDTO> resp = shopAcctRSV.queryOptLogList(reqDTO);
        if (null != resp.getResult()) {
            return resp.getResult();
        }

        return list;

    }
    
    private List<List<Object>> getOptLogDataList(List<ShopAcctOptLogResDTO> data) {
        List<List<Object>> optLogDataList = new ArrayList<List<Object>>();
        for (ShopAcctOptLogResDTO resDTO : data) {
            List<Object> rowList = new ArrayList<Object>();
            rowList.add(resDTO.getId());
            rowList.add(resDTO.getDetailId());
            rowList.add(resDTO.getOrderId());
            rowList.add(resDTO.getPayTranNo());
            rowList.add(resDTO.getCreateTime());
            rowList.add(resDTO.getShopName());
            rowList.add(resDTO.getInMoney());
            rowList.add(resDTO.getOutMoney());
            rowList.add(resDTO.getAcctBalance());
            rowList.add(resDTO.getPayWay());
            rowList.add(resDTO.getOptType());
            optLogDataList.add(rowList);
        }

        return optLogDataList;
    }

    private void exportExcelOptLog(String[] titles, HttpServletResponse response,
            List<ShopAcctOptLogResDTO> data) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("交易日志");
        XSSFCellStyle headStyle = this.getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = this.getBodyStyle(workBook);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        for (int i = 0; i < titles.length; i++) {
        	// 设置单元格的宽度
//        	sheet.setColumnWidth(i, 5000); //第一个参数代表列id(从0开始),第2个参数代表宽度值  参考 ："2012-08-10"的宽度为2500  
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        if (data != null && data.size() > 0) {
            for (int j = 0; j < data.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                ShopAcctOptLogResDTO resDTO = data.get(j);
                
                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getId()));
                
                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getDetailId()==null?'-':resDTO.getDetailId()));
                
                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getOrderId()==null?'-':resDTO.getOrderId()));
                
                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getPayTranNo()==null?'-':resDTO.getPayTranNo()));
                
                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                String createTime = "";
                if(resDTO.getCreateTime() != null){
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	createTime = dateFormat.format(new Date(resDTO.getCreateTime().getTime()));
                }
                cell.setCellValue(isNull(createTime)); 
                
                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(resDTO.getShopName()==null?'-':resDTO.getShopName()));

                cell = bodyRow.createCell(6);
                cell.setCellStyle(bodyStyle);
                String inMoney = "0.00";
                if (resDTO.getInMoney() != null) {
					inMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getInMoney()))+"";
				}
                cell.setCellValue(inMoney);
                
                cell = bodyRow.createCell(7);
                cell.setCellStyle(bodyStyle);
                String outMoney = "0.00";
                if (resDTO.getOutMoney() != null) {
                	outMoney = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getOutMoney()))+"";
				}
                cell.setCellValue(outMoney);
                
                cell = bodyRow.createCell(8);
                cell.setCellStyle(bodyStyle);
                String acctBalance = "0.00";
                if (resDTO.getAcctBalance() != null) {
                	acctBalance = Double.parseDouble(MoneyUtil.convertCentToYuan(resDTO.getAcctBalance()))+"";
				}
                cell.setCellValue(acctBalance);
                
                cell = bodyRow.createCell(9);
                cell.setCellStyle(bodyStyle);
                String payWay = resDTO.getPayWay();
                String payWayName = "-";
                if(StringUtil.isNotBlank(payWay) && !payWay.equals("null")){
                    payWayName = this.getPayWayName(payWay);
                }
                cell.setCellValue(payWayName);
                
                cell = bodyRow.createCell(10);
                cell.setCellStyle(bodyStyle);
                String optTypeName = "-";
                if (StringUtil.isNotBlank(resDTO.getOptType())) {
                	optTypeName = BaseParamUtil.fetchParamValue("SHOPACCT_OPT_TYPE", resDTO.getOptType());
				}
                cell.setCellValue(optTypeName);
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
