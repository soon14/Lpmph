package com.ai.ecp.pmph.service.busi.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.pmph.dao.mapper.busi.CardDistributeMapper;
import com.ai.ecp.pmph.dao.model.CardDistribute;
import com.ai.ecp.pmph.dao.model.CardDistributeCriteria;
import com.ai.ecp.pmph.dao.model.CardDistributeCriteria.Criteria;
import com.ai.ecp.pmph.dubbo.dto.CardDistributeReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardDistributeResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInfoResDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationReqDTO;
import com.ai.ecp.pmph.dubbo.dto.CardInformationResDTO;
import com.ai.ecp.pmph.service.busi.interfaces.ICardMgrSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICarderInfoSV;
import com.ai.ecp.pmph.service.busi.interfaces.ICustCardDistributeSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.impl.datainout.DataImportHandler;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.ecp.server.util.DataInoutUtil;
import com.ai.ecp.staff.dubbo.dto.CustLevelInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustLevelInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.FileImportReqDTO;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.staff.service.common.vip.interfaces.ICustVipSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.db.sequence.Sequence;

@Service
public class CustCardDistributeSVImpl extends GeneralSQLSVImpl implements ICustCardDistributeSV {

    private static String MODULE = CustCardDistributeSVImpl.class.getName();
    
    private static String ACTIVE_STATUS = "1";
    private static String UNACTIVE_STATUS = "0";
    
    @Resource
    private ICarderInfoSV carderInfoSV;
    
    @Resource
    private ICustVipSV custLevleInfoSV;
    
    @Resource
    private ICardMgrSV cardMgrSV;
    

    
    @Resource
    private CardDistributeMapper cardDistributeMapper;
    
    @Resource(name="seq_staff_card_dist_id")
    private Sequence sequence;
    @Override
    public int update(CardDistributeReqDTO paramReqDTO) throws BusinessException {
        
        int count = 0;
        
        CardDistribute record = new CardDistribute();
        ObjectCopyUtil.copyObjValue(paramReqDTO, record, null, false);
        
        record.setUpdateTime(DateUtil.getSysDate());
        record.setUpdateStaff(paramReqDTO.getStaff().getId());
        try {
            count = cardDistributeMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
        return count;
    }

    @Override
    public int deleteActive(CardDistributeReqDTO paramReqDTO) throws BusinessException {
        int count = 0;

        paramReqDTO.setStatus(UNACTIVE_STATUS);//设置为无效状态
        
        count = this.update(paramReqDTO); 
        
        return count;
    }

    @Override
    public int insert(CardDistributeReqDTO paramReqDTO) throws BusinessException {
        
        int count = 0;
        
        CardDistribute record = new CardDistribute();
        record.setId(sequence.nextValue());
        
        ObjectCopyUtil.copyObjValue(paramReqDTO, record, null, false);
        
        if(StringUtil.isBlank(paramReqDTO.getCustLevelCode()))
        {
            CustLevelInfoReqDTO _paramCustLevelInfoReqDTO = new CustLevelInfoReqDTO();
            _paramCustLevelInfoReqDTO.setCustCardNum(paramReqDTO.getCustCardId().substring(0, 2));
            
            CustLevelInfoResDTO custLevelInfoResDTO = custLevleInfoSV.queryCustLevelInfo(_paramCustLevelInfoReqDTO);
            if(custLevelInfoResDTO != null)
            {
                record.setCustLevelCode(custLevelInfoResDTO.getCustLevelCode());
            }
        }
        record.setCreateStaff(paramReqDTO.getStaff().getId());
        record.setUpdateStaff(paramReqDTO.getStaff().getId());
        record.setCreateTime(DateUtil.getSysDate());
        record.setUpdateTime(DateUtil.getSysDate());
        record.setStatus(ACTIVE_STATUS);
        
//        CardInfoReqDTO cardinforeq = new CardInfoReqDTO();
//        List<CardInfoReqDTO> cardinforeqList = new ArrayList<CardInfoReqDTO>();
//        cardinforeq.setCustCardId(record.getCustCardId());
//        cardinforeq.setCardStatus(StaffConstants.Card.CUST_CARD_SEND);
//        cardinforeqList.add(cardinforeq);
//        
//        cardinforeq.setList(cardinforeqList);
        try {
            count = cardDistributeMapper.insertSelective(record);
//            cardMgrSV.updateCardStatus1(cardinforeq);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
        return count;
    }

    @Override
    public PageResponseDTO<CardDistributeResDTO> listPageInfo(CardDistributeReqDTO paramReqDTO) throws BusinessException {
        
        CardDistributeCriteria criteria = new CardDistributeCriteria();
        criteria.setLimitClauseCount(paramReqDTO.getPageSize());
        criteria.setLimitClauseStart(paramReqDTO.getStartRowIndex());
        criteria.setOrderByClause("create_time desc");
        
        Criteria sql = criteria.createCriteria();
        if(paramReqDTO.getDisId() != null)
        {
            sql.andDisIdEqualTo(paramReqDTO.getDisId());
        }
        if(!StringUtil.isBlank(paramReqDTO.getCustCardId()))
        {
            sql.andCustCardIdEqualTo(paramReqDTO.getCustCardId());
        }
        if(!StringUtil.isBlank(paramReqDTO.getCustLevelCode()))
        {
            sql.andCustLevelCodeEqualTo(paramReqDTO.getCustLevelCode());
        }
        if(paramReqDTO.getBeginTime() != null)
        {
            sql.andSendCardGreaterThanOrEqualTo(paramReqDTO.getBeginTime());
        }
        if(paramReqDTO.getEndTime() != null)
        {
            sql.andSendCardLessThanOrEqualTo(paramReqDTO.getEndTime());
        }
        
        return super.queryByPagination(paramReqDTO, criteria, true, new PaginationCallback<CardDistribute, CardDistributeResDTO>() {

            @Override
            public List<CardDistribute> queryDB(BaseCriteria criteria) {
                return cardDistributeMapper.selectByExample((CardDistributeCriteria)criteria);
            }

            @Override
            public long queryTotal(BaseCriteria criteria) {
                return cardDistributeMapper.countByExample((CardDistributeCriteria)criteria);
            }
            @Override
            public List<Comparator<CardDistribute>> defineComparators() {
                List<Comparator<CardDistribute>> ls = new ArrayList<Comparator<CardDistribute>>();
                ls.add(new Comparator<CardDistribute>() {

                    @Override
                    public int compare(CardDistribute o1, CardDistribute o2) {
                        return o1.getCreateTime().getTime() < o2.getCreateTime().getTime()?1:-1;
                    }

                });
                return ls;
            }
            @Override
            public CardDistributeResDTO warpReturnObject(CardDistribute arg0) {
                CardDistributeResDTO dto = new CardDistributeResDTO();
                ObjectCopyUtil.copyObjValue(arg0, dto, null, false);
                
                doFullInfo(dto);//取出发卡人姓名与会员编码对应的中文等级
  
                return dto;
            }
        });
    }

    @Override
    public CardDistributeResDTO find(CardDistributeReqDTO paramReqDTO) throws BusinessException {
        List<CardDistribute>  resultList = null;

        CardDistributeCriteria criteria = new CardDistributeCriteria();
        Criteria sqlCriteria =  criteria.createCriteria();
        if(StringUtil.isNotBlank(paramReqDTO.getCustCardId()))
        {
            sqlCriteria.andCustCardIdEqualTo(paramReqDTO.getCustCardId());
        }
        if(paramReqDTO.getId() != null)
        {
            sqlCriteria.andIdEqualTo(paramReqDTO.getId());
        }
        try {
            resultList = cardDistributeMapper.selectByExample(criteria);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
        if(CollectionUtils.isNotEmpty(resultList))
        {
            CardDistribute resultSel = resultList.get(0);//保证只有一项
            
            CardDistributeResDTO recorDto = new CardDistributeResDTO();
            ObjectCopyUtil.copyObjValue(resultSel, recorDto, null, false);
            
            doFullInfo(recorDto);//取出发卡人姓名与会员编码对应的中文等级
            
            return recorDto;
        }
        return null;
    }
    private void doFullInfo(CardDistributeResDTO recorDto) throws BusinessException 
    {
        CardInformationReqDTO _paramCardInfo = new CardInformationReqDTO();
        _paramCardInfo.setId(recorDto.getDisId());
        
        CardInformationResDTO _paramResDTO = carderInfoSV.findById(_paramCardInfo);
        if(_paramResDTO != null)
        {
            recorDto.setDisName(_paramResDTO.getDisName());
        }
        CustLevelInfoReqDTO _paramLevle = new CustLevelInfoReqDTO();
        _paramLevle.setCustLevelCode(recorDto.getCustLevelCode());
        
        CustLevelInfoResDTO custLevelInfo = custLevleInfoSV.queryCustLevelInfo(_paramLevle);
        if(custLevelInfo != null)
        {
            recorDto.setCustLevelCodeName(custLevelInfo.getCustLevelName());
        }
    }

    @Override
    public void importExcelData(FileImportReqDTO paramExcelReqDTO) throws BusinessException {
        byte[] byteFile = FileUtil.readFile(paramExcelReqDTO.getFileId());//根据文件id得到文件
        InputStream inputs = new ByteArrayInputStream(byteFile);
        //importExcel 调用局部final 变量
        final FileImportReqDTO importDto = new FileImportReqDTO();
        ObjectCopyUtil.copyObjValue(paramExcelReqDTO, importDto, null, false);
        
        //文件解析
        DataInoutUtil.importExcel(new DataImportHandler(inputs, paramExcelReqDTO.getFileName(), "xls", "staff", paramExcelReqDTO.getStaff().getStaffCode()) {
            @Override
            public boolean doCallback(List<List<Object>> datas, String fileId) {
                boolean flagFirstRowPass = false;
                for (List<Object> row : datas) {
                    if(!flagFirstRowPass){
                        flagFirstRowPass = true;
                        continue;
                    }
                    Long disIdString = null;
                    try {
                        Double tempStaffId = Double.parseDouble(row.get(0).toString());
                        disIdString  = tempStaffId.longValue();//发卡人编码
                    } catch (Exception e) {
                        throw new BusinessException("发卡人编码：" + row.get(0).toString() + "，不符合规范。");
                    }
                    String custCardId   = row.get(1).toString();//会员卡号
                    String sendCardTime = row.get(2).toString();//发卡时间
                    String sendRemark   = row.get(3).toString();//发卡原因
                    
                    CardInfoReqDTO cardInfoReqDTO = new CardInfoReqDTO();
                    cardInfoReqDTO.setCustCardId(custCardId);
                    CardInfoResDTO cardInfo = cardMgrSV.findCardInfoByCardId(cardInfoReqDTO);
                    if(cardInfo == null || StaffConstants.Card.CUST_CARD_SEND.equals(cardInfo.getCardStatus()))
                    {
                        throw new BusinessException(StaffConstants.Card.CARD_BIND_NOEXIST_ERROR, new String[]{custCardId});
                    }
                    CardDistributeReqDTO paramReqDTO = new CardDistributeReqDTO();
                    paramReqDTO.setCustCardId(custCardId);
                    if(CustCardDistributeSVImpl.this.find(paramReqDTO)!= null)
                    {
                        throw new BusinessException(StaffConstants.Card.CARD_DISTRIBUTE_EXITS, new String[]{paramReqDTO.getCustCardId()});    
                    }
                    
                    // 根据发卡人编码判断发卡人状态
                    CardInformationReqDTO cardInfoParamReqDTO = new CardInformationReqDTO();
                    cardInfoParamReqDTO.setId(disIdString); 
                    CardInformationResDTO cardInfoResDTO = carderInfoSV.findById(cardInfoParamReqDTO);
                    //发卡人如果失效或者不存在，数据不录入。将错误数据的发卡人编码作为消息提示弹出。
                    if(cardInfoResDTO == null || UNACTIVE_STATUS.equals(cardInfoResDTO.getStatus()))
                    {
                        throw new BusinessException(StaffConstants.STAFF_EXECUTE_ERROR, new String[]{"发卡人编码不存在或者已失效，发卡人编码为："+disIdString});
                    }
                    
                    CardDistributeReqDTO record = new CardDistributeReqDTO();
                    //1.设置发卡人编码
                    record.setDisId(disIdString);
                    
                    //2.设置会员卡等级编码
                    CustLevelInfoReqDTO _paramCustLevelInfoReqDTO = new CustLevelInfoReqDTO();
                    _paramCustLevelInfoReqDTO.setCustCardNum(custCardId.substring(0, 2));
                    
                    CustLevelInfoResDTO custLevelInfoResDTO = custLevleInfoSV.queryCustLevelInfo(_paramCustLevelInfoReqDTO);
                    if(custLevelInfoResDTO != null)
                    {
                        record.setCustLevelCode(custLevelInfoResDTO.getCustLevelCode());
                    }
                    //3.设置会员卡号
                    record.setCustCardId(custCardId);
                    //4.设置发卡时间
                    record.setSendCard(DateUtil.getTimestamp(sendCardTime, DateUtil.DATE_FORMAT));
                    //5.设置备注
                    record.setRemark(sendRemark);
                    
                    insert(record);
 
                }
                return true;
            }
        }, 1, 1);
    }
}

