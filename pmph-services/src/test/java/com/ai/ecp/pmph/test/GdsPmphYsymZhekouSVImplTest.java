package com.ai.ecp.pmph.test;

import com.ai.ecp.goods.dao.mapper.busi.StockInfoAdaptMapper;
import com.ai.ecp.goods.dubbo.constants.GdsOption;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsSkuInfoQuerySV;
import com.ai.ecp.goods.service.busi.interfaces.price.IGdsPriceSV;
import com.ai.ecp.order.dubbo.dto.RQueryGoodPayedRequest;
import com.ai.ecp.order.dubbo.interfaces.IReportGoodPayedRSV;
import com.ai.ecp.pmph.dubbo.interfaces.dataexport.IERPGdsInfoExportRSV;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IERPGdsInfoImportRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IGdsPmphYsymZhekouSV;
import com.ai.ecp.server.test.EcpServicesTest;
import org.apache.poi.util.SystemOutLogger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 */
public class GdsPmphYsymZhekouSVImplTest  extends EcpServicesTest {

    @Autowired(required = false)
    private IGdsPriceSV gdsPriceSV;

    @Resource
    private IERPGdsInfoExportRSV erpGdsInfoExportRSV;

    @Resource
    private IGdsSkuInfoQuerySV gdsSkuInfoQuerySV;

    @Resource
    private IReportGoodPayedRSV reportGoodPayedRSV;

    @Test
    public void testExport() throws InterruptedException {
        while(true){
            Map<String,Object> map=new HashMap<>();
            map.put("exportType","1");
            List<Map<String, Object>> result= erpGdsInfoExportRSV.export(map);
            System.out.println(result);
            System.out.println("本次分页结束！");
            TimeUnit.SECONDS.sleep(5);
        }
    }

    @Test
    public void testExportGdsSkuSV(){
        GdsSkuInfoReqDTO skuInfoReqDTO = new GdsSkuInfoReqDTO();
        skuInfoReqDTO.setId(128823l);
        skuInfoReqDTO.setGdsId(124789l);
        GdsOption.SkuQueryOption[] skuQuery = new GdsOption.SkuQueryOption[] { GdsOption.SkuQueryOption.BASIC,
                GdsOption.SkuQueryOption.MAINPIC};
        this.gdsSkuInfoQuerySV.querySkuInfoByOptions(skuInfoReqDTO,skuQuery);
    }

    @Test
    public void test(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("skuId", 6601307L);
        params.put("gdsId", 6601302L);
        params.put("staffId", 3022L);
        params.put("shopId", 1101L);
        params.put("amount", 1L);
        params.put("ifLadderPrice", "0");
        params.put("siteId",1L);

        /*RQueryGoodPayedRequest rqueryGoodPayedRequest=new RQueryGoodPayedRequest();
        rqueryGoodPayedRequest.setSkuId(6601306L);
        rqueryGoodPayedRequest.getStaff().setId(3025L);
        rqueryGoodPayedRequest.setCurrentSiteId(1L);
        rqueryGoodPayedRequest.setSiteId(1L);
        Long sum=reportGoodPayedRSV.querySumBuyNumByGoodStaff(rqueryGoodPayedRequest);
        System.out.println(sum);*/

        long price=this.gdsPriceSV.caculatePrice(params);
        System.out.println("price ------------------: "+price);
        System.out.println("-------------555555555555-----------------");

        System.exit(-1);
    }


}
