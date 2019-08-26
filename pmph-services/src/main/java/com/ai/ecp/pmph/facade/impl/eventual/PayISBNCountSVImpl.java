package com.ai.ecp.pmph.facade.impl.eventual;

import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.order.dao.model.OrdMain;
import com.ai.ecp.order.dao.model.OrdSub;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdSubResponse;
import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdManageRSV;
import com.ai.ecp.order.dubbo.util.OrderUtils;
import com.ai.ecp.order.service.busi.interfaces.IOrdMainSV;
import com.ai.ecp.order.service.busi.interfaces.IOrdSubSV;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouReqDTO;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouRespDTO;
import com.ai.ecp.pmph.dubbo.interfaces.IGdsPmphYsymZhekouRSV;
import com.ai.ecp.pmph.facade.interfaces.eventual.IPayISBNCountSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.distribute.tx.common.TransactionStatus;
import net.sf.json.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一书一码 统计信息
 */
public class PayISBNCountSVImpl extends PayAbstractExternalUrlSVImpl implements IPayISBNCountSV{

    private static final String MODULE = PayISBNCountSVImpl.class.getName();

    private static final String URL_CODE = "ORD_PAY_ISBN_COUNT_URL";

    @Resource
    private IOrdSubSV ordSubSV;

    @Resource
    private IStaffUnionRSV staffUnionRSV;

    @Resource
    private IOrdMainSV ordMainSV;

    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;

    @Resource
    private IGdsPmphYsymZhekouRSV gdsPmphYsymZhekouRSV;

    @Override
    protected String setMethod() {
        return METHOD_GET_01;
    }

    @Override
    public String getParamJson(PaySuccInfo paySuccInfo) {
        List<RPreOrdSubResponse> ordsubs = ordSubSV.findOrdSubByOrderId(paySuccInfo.getOrderId());

        List<RPreOrdSubResponse> isbns = new ArrayList<>();
        Long staffId = paySuccInfo.getStaffId();
        CustInfoResDTO custInfoResDTO = staffUnionRSV.findCustOrAdminByStaffId(staffId);

        String userName = custInfoResDTO.getStaffCode();

        for(RPreOrdSubResponse ordsub : ordsubs){
            //调用商品接口判断是否电子书
            if(!gdsPmphYsymZhekouRSV.ifDigitalTeachOrDigitalBook(ordsub.getSkuId())){
                continue;
            }
            OrdSub ordSub = new OrdSub();
            ObjectCopyUtil.copyObjValue(ordsub,ordSub,"",false);
            GdsSkuInfoRespDTO gdsInfoRespDTO = gdsSkuInfoQueryRSV.querySkuInfoByOptions(OrderUtils.codeGdsRequest(ordSub));
            String ISBN = OrderUtils.getZsCode(gdsInfoRespDTO);
            if(StringUtil.isNotBlank(ISBN)) {
                GdsPmphYsymZhekouReqDTO gdsPmph = new GdsPmphYsymZhekouReqDTO();
                gdsPmph.setProp1(ISBN);
                gdsPmph.setAdduser(userName);
                GdsPmphYsymZhekouRespDTO gdsPmphYsymZhekouRespDTO = gdsPmphYsymZhekouRSV.queryZEResourceActivation(gdsPmph);
                if(!(gdsPmphYsymZhekouRespDTO != null && "Y".equals(gdsPmphYsymZhekouRespDTO.getProp2()))){
                    continue;
                }
            } else {
                continue;
            }
            isbns.add(ordsub);
        }

        if(CollectionUtils.isEmpty(isbns)) return "";

        LogUtil.info(MODULE,"外域ISBN链接参数处理开始");
        Map<String,Object> params = new HashMap<String,Object>();
        //构造部分参数

        params.put("UserName", userName);

        List<Map> gdsList = new ArrayList<>();
        LogUtil.info(MODULE,"处理子订单个数："+isbns.size());
        for(RPreOrdSubResponse ordSubResponse : isbns){
            OrdSub ordSub = new OrdSub();
            ObjectCopyUtil.copyObjValue(ordSubResponse,ordSub,"",false);

            GdsSkuInfoRespDTO gdsInfoRespDTO = gdsSkuInfoQueryRSV
                    .querySkuInfoByOptions(OrderUtils.codeGdsRequest(ordSub));

            Map isbn = new HashMap();
            Map gds = new HashMap();
            gds.put("Price", ordSubResponse.getDiscountPrice());
            String ISBN = OrderUtils.getZsCode(gdsInfoRespDTO);
            if(StringUtil.isNotBlank(ISBN)){
                GdsPmphYsymZhekouReqDTO gdsPmph = new GdsPmphYsymZhekouReqDTO();
                gdsPmph.setProp1(ISBN);
                gdsPmph.setAdduser(userName);
                GdsPmphYsymZhekouRespDTO gdsPmphYsymZhekouRespDTO = gdsPmphYsymZhekouRSV.queryZEResourceActivation(gdsPmph);
                gds.put("Discount", gdsPmphYsymZhekouRespDTO.getProp3());
                isbn.put(ISBN, gds);
            }else{
                LogUtil.error(MODULE, "本域获取ISBN号失败"+JSON.toJSONString(params));
                continue;
            }
            gdsList.add(isbn);
        }
        LogUtil.info(MODULE, "处理子订单成功的个数：" + gdsList.size());

        params.put("ISBN", gdsList);
        params.put("OrderNo", paySuccInfo.getOrderId());

        ROrdMainResponse ordMain = ordMainSV.findOrdMianByOrderId(paySuccInfo.getOrderId());
        params.put("OrderAmount",ordMain.getRealMoney());
        params.put("Project","pmphmall");

        LogUtil.info(MODULE, "外域ISBN链接参数处理完成"+JSON.toJSONString(params));
        return JSON.toJSONString(params);
    }

    @Override
    public String getParamNameValue(PaySuccInfo paySuccInfo) {
        return null;
    }

    @Override
    public String setUrlPrefix() {
        return URL_CODE;
    }

    @Override
    public String getImportType() {
        return "08";
    }

    @Override
    public void handleResult(PaySuccInfo paySuccInfo, String resultStr) {
        if(!StringUtil.isBlank(resultStr)) {
            LogUtil.info(MODULE, "处理获取外域ISBN统计接口结束,子订单号：" + paySuccInfo.getSubOrder());
        }
    }

    @Override
    public String getModule() {
        return MODULE;
    }

    @Override
    public void deal(PaySuccInfo paySuccInfo) {
        super.deal(paySuccInfo,getImportType());
    }
}
