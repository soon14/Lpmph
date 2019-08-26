package com.ai.ecp.pmph.dubbo.impl;

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

import com.ai.ecp.pmph.dubbo.dto.CardBindReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardBindResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardImportTempResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoResDTO;
import com.ai.ecp.pmph.dubbo.interfaces.ICardMgrRSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICardMgrSV;
import com.ai.ecp.server.front.dto.BaseParamDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.staff.dao.model.CustInfo;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.staff.service.busi.custinfo.interfaces.ICustInfoSV;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.db.sequence.Sequence;

/**
 * 
 * Project Name:ecp-services-staff <br>
 * Description: dubbo层：会员卡管理接口实现类<br>
 * Date:2015-10-27上午11:47:01  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
public class CardMgrRSVImpl implements ICardMgrRSV{

    
    @Resource
    private ICardMgrSV cardMgrSV;
    
    @Resource
    private ICustInfoSV custInfoSV;
    
    @Resource(name="seq_card_import_temp_id")
    private Sequence seq_card_import_temp_id;
    
    private static String MODULE = CardMgrRSVImpl.class.getName();
    
    @Override
    public PageResponseDTO<CardInfoResDTO> listCardInfo(CardInfoReqDTO req)
            throws BusinessException {
        return cardMgrSV.listCardInfo(req);
    }

    @Override
    public int updateCardInfo(CardInfoReqDTO req) throws BusinessException {
        //入参对象不能为空
        if (req == null) {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR,new String[]{"入参对象"});
        }
        return cardMgrSV.updateCardInfo(req);
    }

    @Override
    public int saveCardBindOpt(CardBindReqDTO req) throws BusinessException {
        //入参对象不能为空
        if (req == null || req.getCustCardId() == null || req.getStaffId() == null) {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR,new String[]{"入参对象"});
        }
        
        
        //判断会员卡状态，是否已经被绑定
        CardInfoReqDTO cardInfoReqDTO = new CardInfoReqDTO();
        cardInfoReqDTO.setCustCardId(req.getCustCardId());
        CardInfoResDTO cardInfoResDTO = cardMgrSV.findCardInfoByCardId(cardInfoReqDTO);
        if(cardInfoResDTO == null)
        {
            throw new BusinessException(StaffConstants.Card.CARD_BIND_NOEXIST_ERROR, new String[]{req.getCustCardId()});
        }
        if(StaffConstants.Card.CUST_CARD_SEND.equals(cardInfoResDTO.getCardStatus()))
        {
            //已绑定，抛出异常
            throw new BusinessException(StaffConstants.Card.CARD_IS_BINDED_EVER, new String[]{req.getCustCardId()});
        }
        if(StringUtil.isBlank(req.getBindCustLevelCode()))
        {
            req.setBindCustLevelCode(cardInfoResDTO.getCustLevelCode());
        }
        CustInfo custInfo = custInfoSV.findCustInfoById(req.getStaffId());
        
        LogUtil.info(MODULE, "会员卡绑定开始：[绑定的会员卡号："+req.getCustCardId()+"],[会员信息："+custInfo.toString()+"]");

        if(custInfo.getCustLevelCode()!=null&&custInfo.getCustLevelCode().compareTo(cardInfoResDTO.getCustLevelCode())>0)
        {
            throw new BusinessException(StaffConstants.Card.CARD_LEVEL_LOW_ERROR, new String[]{req.getCustCardId()});
        }
        req.setCardGroup(cardInfoResDTO.getCardGroup());
        return cardMgrSV.saveCardBindOpt(req);
    }

    @Override
    public int buildCard(CardInfoReqDTO req) throws BusinessException {
        
        if(null==req.getRow()||0==req.getRow()){
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR,new String[]{"row"});
        }
        
        if(StringUtil.isBlank(req.getCustLevelCode())){
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR,new String[]{"custLevelCode"});
        }
        int ss = 0;
        int rr = 0;
        if(req.getRow()>300){
            int q = req.getRow().intValue()/10; 
            int r = req.getRow().intValue()%10;
            req.setRow(new Long(q));
            for (int i = 0; i < 10; i++) {
              ss =   cardMgrSV.saveCardInfo(req);
            }
           if(r>0){
               req.setRow(new Long(r));
               rr =   cardMgrSV.saveCardInfo(req);
           }
            return ss*10+rr;
        }else{
            ss =   cardMgrSV.saveCardInfo(req);
            return ss;
        }
     
    }

    @Override
    public PageResponseDTO<CardBindResDTO> listCardBindPageByStaff(CardBindReqDTO req)throws BusinessException {
        
        if(req == null || req.getStaffId() == null)
        {
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR, new String[]{"CardBindReqDTO:staffId"});
        }
        
        return cardMgrSV.listCardBindPage(req);
    }

    @Override
    public Long updateCardStatus(CardInfoReqDTO req) throws BusinessException {
        Long importId = seq_card_import_temp_id.nextValue();
        byte[] byteFile = FileUtil.readFile(req.getFileId());
        InputStream inputs = new ByteArrayInputStream(byteFile);
        Workbook wb = null;
        List<CardInfoReqDTO> list = new ArrayList<CardInfoReqDTO>();
        String number = "";
        List<BaseParamDTO> cardstatuslist = BaseParamUtil.fetchParamList(StaffConstants.Card.CARD_STATUS_KEY);
            try {
                if (req.getFileName().endsWith(".xls"))
                    wb = (Workbook) new HSSFWorkbook(inputs);
                else if (req.getFileName().endsWith(".xlsx")) {
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
                LogUtil.error(MODULE, "excelfile", e);
            }
            
            for (CardInfoReqDTO cardInfoReqDTO : list) {
                cardMgrSV.updateCardStatus(cardInfoReqDTO,importId);
            }
        return  importId;
    }

    @Override
    public PageResponseDTO<CardImportTempResDTO> listTempPage(CardImportTempReqDTO req)
            throws BusinessException {
        return cardMgrSV.listTempPage(req);
    }

    @Override
    public void deleteCardImport(CardImportTempReqDTO req) throws BusinessException {
        cardMgrSV.deleteCardImport(req);
    }

    @Override
    public void updateCardstatus2(CardInfoReqDTO req) throws BusinessException {
        cardMgrSV.updateCardStatus1(req);
    }

    @Override
    public PageResponseDTO<CardBindResDTO> listCardBind(CardBindReqDTO req)
            throws BusinessException {
        return cardMgrSV.listCardBind(req);
    }

}

