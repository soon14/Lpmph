package com.ai.ecp.pmph.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IERPGdsStockInfoImportSV;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.ecp.general.order.dto.ROrdCartItemCommRequest;
import com.ai.ecp.goods.dao.mapper.busi.StockInfoAdaptMapper;
import com.ai.ecp.goods.dao.mapper.busi.StockShopRepIdxMapper;
import com.ai.ecp.goods.dao.mapper.busi.manual.StockOptRecordExtraMapper;
import com.ai.ecp.goods.dao.mapper.busi.manual.StockPreOccupyExtraMapper;
import com.ai.ecp.goods.dao.model.StockInfoAdapt;
import com.ai.ecp.goods.dao.model.StockInfoAdaptCriteria;
import com.ai.ecp.goods.dao.model.StockOptRecord;
import com.ai.ecp.goods.dao.model.StockPreOccupy;
import com.ai.ecp.goods.dao.model.StockShopRepIdx;
import com.ai.ecp.goods.dao.model.StockShopRepIdxCriteria;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.dto.AffirmStockDTO;
import com.ai.ecp.goods.dubbo.dto.DelPreOccupyReqDTO;
import com.ai.ecp.goods.dubbo.dto.DeliverySkuStcokReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockPreOccupyReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepAdaptReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepAdaptRespDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepMainReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepPageRespDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockRepRespDTO;
import com.ai.ecp.goods.service.busi.interfaces.IGdsStockSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.test.EcpServicesTest;
import com.ai.paas.utils.LogUtil;
import com.db.sequence.Sequence;

@RunWith(SpringJUnit4ClassRunner.class)
public class GoodsStockSVImplTest extends EcpServicesTest {
    
    public final static String KEY_RECORD_ID = "record_id";

    
    public final static String KEY_BENBANBIANHAO = "benbanbianhao";
    
    public final static String KEY_KUCUNCESHU = "kucunceshu";
    @Resource
    IGdsStockSV goodsStockSV;

    @Resource
    private StockInfoAdaptMapper stockInfoAdaptMapper;

    @Resource
    private StockShopRepIdxMapper shopRepIdxMapper;
    
    @Resource
    private StockOptRecordExtraMapper optRecordExtraMapper;
    
    @Resource
    private IERPGdsStockInfoImportSV gdsStockInfoImportSV;
    @Resource
    private StockPreOccupyExtraMapper preOccupyExtraMapper;

    @Resource(name = "seq_gds_prop_value")
    private Sequence seqGdsPropValue;

    @Test
    public void testSaveRep() throws Exception {

        StockRepMainReqDTO rep = new StockRepMainReqDTO();
        StockRepReqDTO stockRepDTO = new StockRepReqDTO();
        stockRepDTO.setShopId(1l);
        stockRepDTO.setCodeType("00");
        stockRepDTO.setCompanyId(1L);
        stockRepDTO.setRepName("测试仓库");
        stockRepDTO.setRepType("00");
        stockRepDTO.setStaffId(11l);
        rep.setStockRepDTO(stockRepDTO);
        try {
            goodsStockSV.addStockRep(rep);
        } catch (Exception e) {
            LogUtil.info("","",e);
        }

    }

    @Test
    public void testListRepInfo() throws Exception {
        StockRepReqDTO stockRepDTO = new StockRepReqDTO();
        stockRepDTO.setShopId(1l);
        stockRepDTO.setPageNo(0);
        stockRepDTO.setPageSize(10);
        PageResponseDTO<StockRepPageRespDTO> pageStockRep = goodsStockSV
                .queryStockRepInfoByShopId(stockRepDTO);
        for (StockRepPageRespDTO repPageDTO : pageStockRep.getResult()) {

            System.out.println(repPageDTO.getRepName() + "的商品数量为" + repPageDTO.getCount());

        }
    }

    @Test
    public void testListRepAdapt() throws Exception {
        StockRepAdaptReqDTO stockRepAdaptReqDTO = new StockRepAdaptReqDTO();
        stockRepAdaptReqDTO.setShopId(1l);
        stockRepAdaptReqDTO.setAdaptCountry("00");
        List<StockRepAdaptRespDTO> repAdaptList = goodsStockSV
                .queryStockRepAdaptProvinceByShopId(stockRepAdaptReqDTO);
        System.out.println(repAdaptList.size());
    }

    @Test
    public void testAddStockInfo() throws Exception {

        StockInfoReqDTO stockInfoDTO = new StockInfoReqDTO();
        stockInfoDTO.setShopId(1l);
        // stockInfoDTO.setAvailCount(100L);
        // stockInfoDTO.setRealCount(100l);
        stockInfoDTO.setRepCode(4009L);
        stockInfoDTO.setGdsId(1l);
        stockInfoDTO.setSkuId(1L);
        goodsStockSV.addStockInfo(stockInfoDTO);
    }

    @Test
    public void testUpdateStockInfo() throws Exception {
        StockInfoReqDTO stockInfoDTO = new StockInfoReqDTO();
        stockInfoDTO.setTurnType(GdsConstants.GdsStock.STOCK_INFO_TURN_ADD_PRE);
        stockInfoDTO.setId(1l);
        stockInfoDTO.setTurnCount(9L);
        goodsStockSV.updateStockInfo(stockInfoDTO);

    }

    @Test
    public void testInsertAdaptHis() throws Exception {
        // StockRepAdapt repAdapt = new StockRepAdapt();
        // repAdapt.setId(111l);
        // repAdapt.setRepCode(111l);
        // repAdapt.setAdaptCountry("ZH");
        // repAdapt.setAdaptProvince("222");
        // repAdapt.setAdaptCity("22");
        //
        // repAdapt.setShopId(11l);
        // goodsStockSV.addStockRepAdaptHis(repAdapt);
        // StockRepAdaptHis his = new StockRepAdaptHis();
        // goodsStockSV.queryHis(his);

    }

    @Test
    public void testLike() throws Exception {
        StockRepReqDTO repDTO = new StockRepReqDTO();
        repDTO.setRepName("神神");
        repDTO.setShopId(1l);
        repDTO.setPageSize(10);
        repDTO.setPageNo(1);
        PageResponseDTO<StockRepPageRespDTO> list = goodsStockSV.queryStockRepInfoByShopId(repDTO);
        System.out.println(list.getCount());

    }

    @Test
    public void testAddStockInfoForInput() throws Exception {

        // StockRepDTO stockRepDTO = new StockRepDTO();
        // stockRepDTO.setShopId(1L);
        // stockRepDTO.setIfRegionalStock(true);
        // List<StockRepDTO> stockReps =
        // goodsStockSV.queryShopRepInfoForGdsInput(stockRepDTO);
        // for(StockRepDTO repDTO:stockReps){
        // StockInfoDTO stockInfoDTO = new StockInfoDTO();
        // stockInfoDTO.setCatgCode(1l);
        // stockInfoDTO.setCompanyId(1l);
        // stockInfoDTO.setGdsId(1l);
        // stockInfoDTO.setIsOver("0");
        // stockInfoDTO.setIsUsewarning("0");
        // stockInfoDTO.setLackCount(0l);
        // stockInfoDTO.setRepCode(repDTO.getId());
        // // stockInfoDTO.setTurnCount(100l);
        // stockInfoDTO.setRepType(GdsConstants.GdsStock.STOCK_REP_TYPE_SEPERATE);
        // stockInfoDTO.setShopId(1l);
        // stockInfoDTO.setSkuId(1l);
        // stockInfoDTO.setStaffId(1l);
        // //为每个区域赋值库存量
        // for(int i = 0 ; i < repDTO.getStockRepAdaptDTOs().size() ;i++ )
        // {
        // StockRepAdaptDTO adaptDTO = repDTO.getStockRepAdaptDTOs().get(i);
        // adaptDTO.setCount(i * 100l);
        // }
        // stockInfoDTO.setStockRepAdapts(repDTO.getStockRepAdaptDTOs());
        // stockInfoDTO.setTypeId(1l);
        // stockInfoDTO.setWarningCount(11l);
        // stockInfoDTO.setStockType(GdsConstants.GdsStock.STOCK_INFO_TYPE_SEPERATE);
        // goodsStockSV.addStockInfoForInput(stockInfoDTO);
        // }

        StockRepReqDTO stockRepDTO = new StockRepReqDTO();
        stockRepDTO.setShopId(1234L);
        stockRepDTO.setIfRegionalStock(true);
        List<StockRepRespDTO> stockReps = goodsStockSV.queryShopRepInfoForGdsInput(stockRepDTO);
        for (StockRepRespDTO repDTO : stockReps) {
            StockInfoReqDTO stockInfoDTO = new StockInfoReqDTO();
            stockInfoDTO.setCatgCode("1");
            stockInfoDTO.setCompanyId(1l);
            stockInfoDTO.setGdsId(1l);
            stockInfoDTO.setIsOver("0");
            stockInfoDTO.setIsUsewarning("0");
            stockInfoDTO.setLackCount(0l);
            stockInfoDTO.setRepCode(repDTO.getId());
            stockInfoDTO.setTurnCount(300l);
            // stockInfoDTO.setTurnCount(100l);
            stockInfoDTO.setRepType(GdsConstants.GdsStock.STOCK_REP_TYPE_SEPERATE);
            stockInfoDTO.setShopId(1234l);
            stockInfoDTO.setSkuId(1l);
            stockInfoDTO.setStaffId(1l);
            // 为每个区域赋值库存量
            for (int i = 0; i < repDTO.getStockRepAdaptDTOs().size(); i++) {
                StockRepAdaptReqDTO adaptDTO = repDTO.getStockRepAdaptDTOs().get(i);
                adaptDTO.setCount((i + 1) * 100l);
            }
            stockInfoDTO.setStockRepAdapts(repDTO.getStockRepAdaptDTOs());
            stockInfoDTO.setTypeId(1l);
            stockInfoDTO.setWarningCount(11l);
            stockInfoDTO.setStockType(GdsConstants.GdsStock.STOCK_INFO_TYPE_PUBLIC);
            goodsStockSV.addStockInfoForInput(stockInfoDTO);
        }

    }

    @Test
    public void testAddPreOccupy() throws Exception {
//        StockPreOccupyReqDTO preOccupyDTO = new StockPreOccupyReqDTO();
//    2 7136    4084    984 69  RW16022900005802    SRW16022900007305   75256   1   1   2016/2/29 10:39:46  180 180 2016/2/29 10:39:46
        ROrdCartItemCommRequest cartItemCommRequest = new ROrdCartItemCommRequest(); 
        cartItemCommRequest.setOrderAmount(100L);
        cartItemCommRequest.setOrderId("RW16022900005802");
        cartItemCommRequest.setOrderSubId("SRW16022900007305");
        cartItemCommRequest.setRepCode(4084l);
        cartItemCommRequest.setStockId(984L);
        cartItemCommRequest.setSkuId(75256L);
        cartItemCommRequest.setShopId(69l);
        cartItemCommRequest.setStaffId(180l);
         goodsStockSV.addStockPreOccupy(cartItemCommRequest);

    }

    @Test
    public void testDelPreOccupy() throws Exception {
        DelPreOccupyReqDTO delPreOccupyDTO = new DelPreOccupyReqDTO();
        delPreOccupyDTO.setStaffId(1l);
        List<StockPreOccupyReqDTO> preOccupyDTOs = new ArrayList<StockPreOccupyReqDTO>();
        StockPreOccupyReqDTO preOccupyDTO = new StockPreOccupyReqDTO();
        preOccupyDTO.setCount(20L);
        preOccupyDTO.setOrderId("111");
        preOccupyDTO.setSubOrder("1111");
        preOccupyDTO.setRepCode(4043l);
        preOccupyDTO.setStockId(25L);
        preOccupyDTO.setSkuId(1L);
        preOccupyDTO.setShopId(1l);

        preOccupyDTOs.add(preOccupyDTO);
        delPreOccupyDTO.setPreOccupyDTOs(preOccupyDTOs);
        // preOccupyDTO.setStaffId(1l);
        // goodsStockSV.deleteStockPreOccupy(delPreOccupyDTO);

    }

    @Test
    public void testUpdateDev() throws Exception {
        DeliverySkuStcokReqDTO deliverySkuStcokDTO = new DeliverySkuStcokReqDTO();
        deliverySkuStcokDTO.setDeliveryCount(5l);
        deliverySkuStcokDTO.setIsSerial(true);
        deliverySkuStcokDTO.setOrderId("112");
        deliverySkuStcokDTO.setSkuId(1l);
        deliverySkuStcokDTO.setStockId(25l);
        deliverySkuStcokDTO.setSubOrder("1121");
        deliverySkuStcokDTO.setIsDelivAll(false);
        List<String> list = new ArrayList<>();
        String[] s = new String[] { "sss", "ss", "sss", "333", "222" };
        for (String ss : s) {
            list.add(ss);

        }
        deliverySkuStcokDTO.setSerialNoList(list);
        // goodsStockSV.updateDeliverySkuStcok(deliverySkuStcokDTO);

    }

    @Test
    public void testUpdateAffirm() throws Exception {
        AffirmStockDTO affirmStockDTO = new AffirmStockDTO();
        affirmStockDTO.setAffirmCount(5l);
        affirmStockDTO.setStockId(25l);
        affirmStockDTO.setCatgCode("1");
        affirmStockDTO.setSkuId(1l);
        affirmStockDTO.setGdsId(1l);
        affirmStockDTO.setShopId(1L);
        // affirmStockDTO.setIfSerial(true);
        affirmStockDTO.setOrderId("112");
        affirmStockDTO.setSubOrderId("1121");
        affirmStockDTO.setStaffId(1L);
        affirmStockDTO.setCompanyId(2L);
        // goodsStockSV.updateAffirmReceiptStock(affirmStockDTO);

    }

    @Test
    public void testTurnSeq() {
        for (int i = 0; i < 200; i++) {
            seqGdsPropValue.nextValue();
        }
    }

    @Test
    public void testQueryStock() throws Exception {
        // StockInfoForGdsReqDTO stockInfoForGdsDTO = new StockInfoForGdsReqDTO();
        // stockInfoForGdsDTO.setShopId(35l);
        // stockInfoForGdsDTO.setGdsId(1008609478l);
        // stockInfoForGdsDTO.setSkuId(1008617923L);
        // stockInfoForGdsDTO.setAdaptCountry("156");
        // stockInfoForGdsDTO.setAdaptProvince("340000");
        // stockInfoForGdsDTO.setAdaptCity("340700");
        // StockInfoRespDTO infoRespDTO = goodsStockSV.queryStockInfoByGds(stockInfoForGdsDTO);
        // System.out.println(infoRespDTO.getAvailCount());
        //
        StockInfoAdaptCriteria adaptCriteria = new StockInfoAdaptCriteria();
        StockInfoAdaptCriteria.Criteria criteria = adaptCriteria.createCriteria();
        criteria.andShopIdEqualTo(35l);
        List<StockInfoAdapt> adapts = stockInfoAdaptMapper.selectByExample(adaptCriteria);
        System.out.println("---------------------------------------------------" + adapts.size());
    }

    @Test
    public void testQueryRep() throws Exception {
        StockRepReqDTO repReqDTO = new StockRepReqDTO();
        repReqDTO.setRepName("9");
        repReqDTO.setStatus("1");
        repReqDTO.setShopId(69L);
     
        // goodsStockSV.queryStockRepInfoByShopId(repReqDTO);

        StockShopRepIdxCriteria stockShopRepIdxCriteria = new StockShopRepIdxCriteria();
        StockShopRepIdxCriteria.Criteria criteria = stockShopRepIdxCriteria.createCriteria();
        criteria.andShopIdEqualTo(repReqDTO.getShopId());
        if (repReqDTO.getStatus() != null && !"".equals(repReqDTO.getStatus())) {
            criteria.andStatusEqualTo(repReqDTO.getStatus());
        }
        if (repReqDTO.getRepName() != null && !"".equals(repReqDTO.getRepName())) {
            criteria.andRepNameLike("%" + repReqDTO.getRepName() + "%");
        }
        
        stockShopRepIdxCriteria.setLimitClauseStart(0);
        stockShopRepIdxCriteria.setLimitClauseCount(10);
        stockShopRepIdxCriteria.setOrderByClause("rep_code desc");
        List<StockShopRepIdx> idxs =   shopRepIdxMapper.selectByExample(stockShopRepIdxCriteria);
        System.out.println(idxs.size());
    }
    @Test
    public void testGdsStockInfoImportSV()throws Exception{
  
        Map map  = new HashMap<String,Object>();
        map.put(KEY_BENBANBIANHAO, "0000Z28613_1");
        map.put(KEY_KUCUNCESHU, 111111l);
        gdsStockInfoImportSV.saveGdsStockInfo(map);
        
        
    }
    
    @Test
    public void testInsertStockOptRecord()throws Exception{
        StockOptRecord optRecord = new StockOptRecord();
        optRecord.setId(999999999l);
        optRecord.setStockId(12l);
        optRecord.setCount(111l);
        optRecord.setOptRepCode(33l);
        optRecord.setOrdOptNo("111");
        optRecord.setSkuId(1122l);
        optRecord.setOptType("03");
        int key =  optRecordExtraMapper.insertStockOptRecordNotExists(optRecord);
        System.out.println(key);
    }
    
    
    @Test
    public void testInsertStockPreOccupy()throws Exception{
        StockPreOccupy optRecord = new StockPreOccupy();
        optRecord.setId(999999999l);
        optRecord.setStockId(12l);
        optRecord.setCount(111l);
        optRecord.setOrderId("1232243324");
        optRecord.setSkuId(1122l);
        optRecord.setSubOrder("ERER122133");
        optRecord.setShopId(100l);
        optRecord.setRepCode(10l);
        optRecord.setStatus("1");
       int key =  preOccupyExtraMapper.insertPreOccupyNotExists(optRecord);
       System.out.println(key);
    }
    
    @Test
    public void testStatisticStockLack() throws Exception{
    	StockInfoReqDTO stockInfoReqDTO = new StockInfoReqDTO();
    	stockInfoReqDTO.setShopId(100L);
    		Long count = goodsStockSV.statisticStockLack(stockInfoReqDTO);
			System.out.println("---------------------------------汇总缺货总数:" + count + "-------------------");
		
    }
    
}
