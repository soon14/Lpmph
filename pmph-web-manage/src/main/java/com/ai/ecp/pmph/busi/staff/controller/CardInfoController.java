/** 
 * File Name:DemoController.java 
 * Date:2015-8-5下午2:51:38 
 * 
 */
package com.ai.ecp.pmph.busi.staff.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.staff.vo.CardBindVO;
import com.ai.ecp.pmph.busi.staff.vo.CardInfoVO;
import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICardMgrRSV;
import com.ai.ecp.server.front.dto.BaseParamDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * Project Name:ecp-web-demo <br>
 * Description: <br>
 * Date:2015-8-5下午2:51:38 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "/card")
public class CardInfoController extends EcpBaseController {

    private static String MODULE = CardInfoController.class.getName();

    @Resource
    private ICardMgrRSV cardMgrRSV;

    /**
     * 
     * grid:(用户管理列表). <br/>
     * 
     * @return
     * @since JDK 1.7
     */

    @RequestMapping(value = "/grid")
    public String grid(Model model) {

        return "/staff/cardinfo/cardinfo-grid";
    }

    /**
     * 
     * build:(会员卡生成). <br/>
     * 
     * @param model
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/build")
    public String build(Model model) {
        return "/staff/cardinfo/build/cardbuild";
    }

    @RequestMapping(value = "/buildcard")
    @ResponseBody
    public EcpBaseResponseVO buildcard(@RequestParam("row")
    String row, @RequestParam("custLevelCode")
    String custLevelCode, @RequestParam("cardGroup")
    String cardGroup) {
        CardInfoReqDTO dto = new CardInfoReqDTO();
        dto.setRow(Long.parseLong(row));
        dto.setCustLevelCode(custLevelCode);
        dto.setCardGroup(cardGroup);
        int s = cardMgrRSV.buildCard(dto);
        EcpBaseResponseVO resultVo = new EcpBaseResponseVO();
        resultVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        resultVo.setResultMsg("" + s);
        return resultVo;
    }

    /**
     * 
     * gridList:(会员列表). <br/>
     * 
     * @param model
     * @param vo
     * @param custInfoList
     * @return
     * @throws Exception
     * @since JDK 1.7
     */

    @RequestMapping("/gridlist")
    @ResponseBody
    public Model gridList(Model model, EcpBasePageReqVO vo, @ModelAttribute
    CardInfoVO cardInfoVO) throws Exception {

        CardInfoReqDTO dto = vo.toBaseInfo(CardInfoReqDTO.class);
        if (StringUtil.isNotBlank(cardInfoVO.getCardStatus())) {
            dto.setCardStatus(cardInfoVO.getCardStatus());
        }
        if (StringUtil.isNotBlank(cardInfoVO.getCustLevelCode())) {
            dto.setCustLevelCode(cardInfoVO.getCustLevelCode());
        }
        if (StringUtil.isNotBlank(cardInfoVO.getCustCardId())) {
            dto.setCustCardId(cardInfoVO.getCustCardId());
        }
        if (StringUtil.isNotBlank(cardInfoVO.getCardGroup())) {
            dto.setCardGroup(cardInfoVO.getCardGroup());
        }
        
        PageResponseDTO<CardInfoResDTO> t = cardMgrRSV.listCardInfo(dto);
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
        return super.addPageToModel(model, respVO);

    }

    /**
     * 
     * tempList:(查询错误的导入数据). <br/>
     * 
     * @param model
     * @param vo
     * @param cardInfoVO
     * @return
     * @throws Exception
     * @since JDK 1.7
     */

    @RequestMapping("/templist")
    @ResponseBody
    public Model tempList(Model model, EcpBasePageReqVO vo, @ModelAttribute
    CardInfoVO cardInfoVO) throws Exception {

        CardImportTempReqDTO dto = vo.toBaseInfo(CardImportTempReqDTO.class);
        if (StringUtil.isNotBlank(cardInfoVO.getImportId())) {
            dto.setImportId(Long.parseLong(cardInfoVO.getImportId()));
        }
        PageResponseDTO<CardImportTempResDTO> t = cardMgrRSV.listTempPage(dto);
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
        return super.addPageToModel(model, respVO);

    }

    @RequestMapping("/deletetemp")
    @ResponseBody
    public void deleteTemp(@ModelAttribute
    CardInfoVO cardInfoVO) throws Exception {
        CardImportTempReqDTO dto = new CardImportTempReqDTO();
        cardMgrRSV.deleteCardImport(dto);

    }

    /**
     * 
     * exportshopWhiteExcel:(导出EXCEL模板). <br/>
     * 
     * @param response
     * @param cardInfoVO
     * @since JDK 1.7
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    public void exportshopWhiteExcel(HttpServletResponse response,HttpServletRequest request, @ModelAttribute
    CardInfoVO cardInfoVO) {

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "会员卡导出.xls";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
            response.setHeader("content-disposition","attachment;filename=" + encodeChineseDownloadFileName(request,fileName));
            String[] titles = { "会员卡号编码", "状态" };
            List titleList = new ArrayList();
            List<List<Object>> dataList = new ArrayList();
            for (int i = 0; i < titles.length; i++) {
                titleList.add(titles[i]);
            }
            List<CardInfoResDTO> list2 = queryData(cardInfoVO);

            dataList = this.getDataList(list2);
            this.exportExcel(titles, response, list2);
        } catch (IOException e) {
            LogUtil.error(MODULE, "shopWhitelist exportExcel", e);
        }

    }

    private List<CardInfoResDTO> queryData(CardInfoVO cardInfoVO) {
        CardInfoReqDTO dto = new CardInfoReqDTO();
        List<CardInfoResDTO> list = new ArrayList<CardInfoResDTO>();
        dto.setPageNo(0);
        if (StringUtil.isNotBlank(cardInfoVO.getCardStatus())) {
            dto.setCardStatus(cardInfoVO.getCardStatus());
        }
        if (StringUtil.isNotBlank(cardInfoVO.getCustLevelCode())) {
            dto.setCustLevelCode(cardInfoVO.getCustLevelCode());
        }
        if (StringUtil.isNotBlank(cardInfoVO.getCardGroup())) {
            dto.setCardGroup(cardInfoVO.getCardGroup());
        }
        if (StringUtil.isNotBlank(cardInfoVO.getCustCardId())) {
            dto.setCustCardId(cardInfoVO.getCustCardId());
        }
        PageResponseDTO<CardInfoResDTO> t = cardMgrRSV.listCardInfo(dto);
        if (null != t.getResult()) {
            return t.getResult();
        }

        return list;

    }

    private List<List<Object>> getDataList(List<CardInfoResDTO> data) {
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        for (CardInfoResDTO cardInfoResDTO : data) {
            List<Object> rowList = new ArrayList<Object>();
            rowList.add(cardInfoResDTO.getCustCardId());
            if(StringUtil.isNotBlank(cardInfoResDTO.getCardStatus())){
                BaseParamDTO dto =  BaseParamUtil.fetchParamDTO("STAFF_CARD_STATUS", cardInfoResDTO.getCardStatus());
            rowList.add(dto.getSpValue());
            }
            dataList.add(rowList);
        }

        return dataList;
    }

    private void exportExcel(String[] titles, HttpServletResponse response,
            List<CardInfoResDTO> data) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("导出白名单");
        sheet.setColumnWidth(0, 20 * 256);
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
                CardInfoResDTO cardInfoResDTO = data.get(j);

                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(cardInfoResDTO.getCustCardId()));

                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                BaseParamDTO dto =  BaseParamUtil.fetchParamDTO("STAFF_CARD_STATUS", cardInfoResDTO.getCardStatus());
                cell.setCellValue(String.valueOf(dto.getSpValue()));

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

    @RequestMapping("/excelupdate")
    public String excelUpdate(Model model) throws Exception {
        Long staffId =  WebContextUtil.getCurrentUser().getStaffId();
        model.addAttribute("staffId", staffId);
        return "staff/cardinfo/excel/excelupdate";
    }

    /**
     * 
     * excelfile:(excel导入). <br/> 
     * 
     * @param excelFileIdd
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/excelfile")
    @ResponseBody
    public EcpBaseResponseVO excelfile(@RequestParam(value = "excelFileId", required = true) String excelFileIdd,@RequestParam(value = "fileName") String fileName) throws Exception {
    	EcpBaseResponseVO resultVo = new EcpBaseResponseVO();
    	long importid =0;
        CardInfoReqDTO dto = new CardInfoReqDTO();
        dto.setFileId(excelFileIdd);
        dto.setFileName(fileName);
        try {
            CardImportTempReqDTO aa = new CardImportTempReqDTO();
            cardMgrRSV.deleteCardImport(aa);//删除用户导入的excel表数据（临时表）
            dto.setCreateStaff(aa.getStaff().getId());
            importid = cardMgrRSV.updateCardStatus(dto);//变更表中数据的状态，这里只传入了文件id，后台通过id自己取文件处理
        } catch (Exception e) {
        	e.printStackTrace();
            LogUtil.error(MODULE, "update excelfilpdate", e);
        }
        resultVo.setResultMsg(importid + "");
        return resultVo;
    }

    @RequestMapping(value = "/updatecardstatus", method = RequestMethod.POST)
    @ResponseBody
    public EcpBaseResponseVO updateCardStatus(@RequestParam(value = "cardlist")String cardlist,@RequestParam(value = "status")String status) {
        CardInfoReqDTO dto = new CardInfoReqDTO();
        List<CardInfoReqDTO> list = new ArrayList<CardInfoReqDTO>();
        String[] a = cardlist.split(",");
        for (String string : a) {
            CardInfoReqDTO dto1 = new CardInfoReqDTO();
            dto1.setCustCardId(string);
            dto1.setCardStatus(status);
            list.add(dto1);
        }
        dto.setList(list);
        cardMgrRSV.updateCardstatus2(dto);
        EcpBaseResponseVO resultVo = new EcpBaseResponseVO();
        resultVo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        return resultVo;
    }
    
    
    @RequestMapping("/bindgrid")
    @ResponseBody
    public Model bindgrid(Model model, EcpBasePageReqVO vo, @ModelAttribute
    CardBindVO cardBindVO) throws Exception {

        CardBindReqDTO dto = vo.toBaseInfo(CardBindReqDTO.class);
        //条件：绑定方式
        if (StringUtil.isNotBlank(cardBindVO.getBindType())) {
            dto.setBindType(cardBindVO.getBindType());
        }
        //条件：群组
        if (StringUtil.isNotBlank(cardBindVO.getCardGroup())) {
            dto.setCardGroup(cardBindVO.getCardGroup());
        }
        //条件：联系人姓名
        if (StringUtil.isNotBlank(cardBindVO.getContactName())) {
            dto.setContactName(cardBindVO.getContactName());
        }
        //条件：联系人手机
        if (StringUtil.isNotBlank(cardBindVO.getContactPhone())) {
            dto.setContactPhone(cardBindVO.getContactPhone());
        }
        //会员卡号
        if (StringUtil.isNotBlank(cardBindVO.getCustCardId())) {
            dto.setCustCardId(cardBindVO.getCustCardId());
        }
        
        PageResponseDTO<CardBindResDTO> t = cardMgrRSV.listCardBind(dto);
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(t);
        return super.addPageToModel(model, respVO);

    }
    /**
     * 
     * exportBindExcel:(导出会员卡绑定数量). <br/> 
     * 
     * @param response
     * @param cardBindVO 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/exportBindExcel", method = RequestMethod.POST)
    public void exportBindExcel(HttpServletResponse response, @ModelAttribute
    CardBindVO cardBindVO) {

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = "会员卡绑定数据导出";
            response.setContentType("application/binary;charset=ISO8859_1");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8")
                            + ".xlsx");// 组装附件名称和格式
            String[] titles = { "会员卡号", "登录工号","所属群组", "联系人姓名", "联系人手机", "联系人地址" };
            List titleList = new ArrayList();
            List<List<Object>> dataList = new ArrayList();
            for (int i = 0; i < titles.length; i++) {
                titleList.add(titles[i]);
            }
            List<CardBindResDTO> list2 = queryBindData(cardBindVO);

            dataList = this.getBindDataList(list2);
            this.exportExcelBind(titles, response, list2);
        } catch (IOException e) {
            LogUtil.error(MODULE, "shopWhitelist exportExcel", e);
        }

    }
    private List<CardBindResDTO> queryBindData(CardBindVO cardBindVO) {
        CardBindReqDTO dto = new CardBindReqDTO();
        List<CardBindResDTO> list = new ArrayList<CardBindResDTO>();
        dto.setPageNo(0);
        if (StringUtil.isNotBlank(cardBindVO.getBindType())) {
            dto.setBindType(cardBindVO.getBindType());
        }
        if (StringUtil.isNotBlank(cardBindVO.getCardGroup())) {
            dto.setCardGroup(cardBindVO.getCardGroup());
        }
        if (StringUtil.isNotBlank(cardBindVO.getContactName())) {
            dto.setContactName(cardBindVO.getContactName());
        }
        if (StringUtil.isNotBlank(cardBindVO.getContactPhone())) {
            dto.setContactPhone(cardBindVO.getContactPhone());
        }
        if (StringUtil.isNotBlank(cardBindVO.getCustCardId())) {
            dto.setCustCardId(cardBindVO.getCustCardId());
        }
        
        PageResponseDTO<CardBindResDTO> t = cardMgrRSV.listCardBind(dto);
        if (null != t.getResult()) {
            return t.getResult();
        }

        return list;

    }
    private List<List<Object>> getBindDataList(List<CardBindResDTO> data) {
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        for (CardBindResDTO cardBindResDTO : data) {
            List<Object> rowList = new ArrayList<Object>();
            rowList.add(cardBindResDTO.getCustCardId());
            rowList.add(cardBindResDTO.getStaffCode());
            rowList.add(cardBindResDTO.getBindType());
            rowList.add(cardBindResDTO.getCardGroup());
            rowList.add(cardBindResDTO.getContactName());
            rowList.add(cardBindResDTO.getContactPhone());
            rowList.add(cardBindResDTO.getContactAddress());
            dataList.add(rowList);
        }

        return dataList;
    }

    private void exportExcelBind(String[] titles, HttpServletResponse response,
            List<CardBindResDTO> data) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("导出白名单");
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
                CardBindResDTO cardBindResDTO = data.get(j);

                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(cardBindResDTO.getCustCardId()));
                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(cardBindResDTO.getStaffCode()));
                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(cardBindResDTO.getCardGroup()==null?'-':cardBindResDTO.getCardGroup()));
                
                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(cardBindResDTO.getContactName()==null?'-':cardBindResDTO.getContactName()));
                
                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(cardBindResDTO.getContactPhone()==null?'-':cardBindResDTO.getContactPhone()));
                
                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(String.valueOf(cardBindResDTO.getContactAddress()==null?'-':cardBindResDTO.getContactAddress()));

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
