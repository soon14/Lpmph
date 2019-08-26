package com.ai.ecp.pmph.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.service.busi.interfaces.ICardMgrSV;
import com.ai.ecp.server.front.dto.BaseParamDTO;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.server.test.EcpServicesTest;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;

public class TestFile extends EcpServicesTest{
	
	@Resource
    private ICardMgrSV cardMgrSV;
	
	 @Test
	 public void testSaveRep() throws Exception {
		 Long importId = 1L;
        byte[] byteFile = FileUtil.readFile("57b43e0afea2e50fabf7bc12");
        InputStream inputs = new ByteArrayInputStream(byteFile);
        Workbook wb = null;
        List<CardInfoReqDTO> list = new ArrayList<CardInfoReqDTO>();
        String number = "";
        List<BaseParamDTO> cardstatuslist = BaseParamUtil.fetchParamList(StaffConstants.Card.CARD_STATUS_KEY);
            try {
                if ("sss.xlsx".endsWith(".xls"))
                    wb = (Workbook) new HSSFWorkbook(inputs);
                else if ("sss.xlsx".endsWith(".xlsx")) {
                    wb = (Workbook) new XSSFWorkbook(inputs);
                }
                Sheet sheet = wb.getSheetAt(0);
                for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                    CardInfoReqDTO item = new CardInfoReqDTO();
                    Row row = sheet.getRow(i);
                    Iterator<?> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        Cell cell = (Cell) cells.next();
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        number = cell.getStringCellValue();
                        if (cell.getColumnIndex() == 0 && !"".equals(number)) {
                            item.setCustCardId(number);
                        } else if (cell.getColumnIndex() == 1 && !"".equals(number)) {
                            number = number.trim();
                            for (BaseParamDTO baseParamDTO : cardstatuslist) {
                                if(number.equals(baseParamDTO.getSpValue())){
                                    item.setCardStatus(baseParamDTO.getSpCode());
                                    continue;
                                }
                                
                            }
                            
                           
                        } else {
                            number = "";
                        }

                    }
                    list.add(item);

                }
            } catch (Exception e) {
               e.printStackTrace();
            }
            
            for (CardInfoReqDTO cardInfoReqDTO : list) {
                cardMgrSV.updateCardStatus(cardInfoReqDTO,importId);
            }
	 }

}

