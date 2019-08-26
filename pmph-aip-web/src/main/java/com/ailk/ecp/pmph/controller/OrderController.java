package com.ailk.ecp.pmph.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.general.sys.mc.dto.McParamsDTO;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.order.dubbo.dto.ROrdMainResponse;
import com.ai.ecp.order.dubbo.dto.ROrdSubResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdSubResponse;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdSubRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.staff.dubbo.dto.AuthStaffResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoMsgResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.IAuthStaffRSV;
import com.ai.ecp.staff.dubbo.interfaces.ISsoUserImportRSV;
import com.ai.ecp.sys.dubbo.interfaces.IMcSyncSendRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.BizContentVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/*
 * 订单相关接口
 */
@Controller
public class OrderController extends BaseController{
    
    private static final String MODULE = OrderController.class.getName();
    
    @Resource
    private IOrdMainRSV ordMainRSV;
    
    @Resource
    private IOrdSubRSV ordSubRSV;
    
    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
    @Resource
    private IAuthStaffRSV authStaffRSV;
    
    @Resource
    private ISsoUserImportRSV ssoUserImportRSV;
    
    @Resource
    private IMcSyncSendRSV mcSyncSendRSV;
    
    /*
     * 订单同步接口
     */
    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.pmph.order.create")
    @Security(mustLogin=true,authorCheckType=DefaultServiceCheckChain.class)
    @ResponseBody
    public Map<String,Object> create(BizContentVO bizContentVO) throws BusinessException{
        Map<String,Object> retMap = new HashMap<String,Object>();
        String bizContent=bizContentVO.getBiz_content();
        
        //1.判断入参是否为空，为空返回错误信息
        if(StringUtil.isBlank(bizContent)){
            retMap.put(RespUtil.CODE, "1");
            retMap.put(RespUtil.MSG, "参数不允许为空");
            return RespUtil.renderRootResp(retMap);
        }
        
        //2.判断入参JSON格式是否正确
        try{
            JSONObject jsonObject=JSONObject.parseObject(bizContentVO.getBiz_content());
            String orderCode=jsonObject.getString("order_code");
            if (StringUtil.isBlank(orderCode)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[order_code]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            String payTranNo=jsonObject.getString("pay_tran_no");
            if (StringUtil.isBlank(payTranNo)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[pay_tran_no]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            String staffCode=jsonObject.getString("staff_code");
            if (StringUtil.isBlank(staffCode)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[staff_code]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            String gdsDetail=jsonObject.getString("gds_detail");
            if (StringUtil.isBlank(gdsDetail)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[gds_detail]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            
            JSONArray gdsList=JSONObject.parseArray(gdsDetail);
            for (Object object : gdsList) {
                Map gdsMap=(Map) object;
                if (gdsMap.get("gds_code")==null) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[gds_code]参数不允许为空");
                    return RespUtil.renderRootResp(retMap); 
                }
                if (StringUtil.isBlank(gdsMap.get("gds_code").toString())) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[gds_code]参数不允许为空");
                    return RespUtil.renderRootResp(retMap); 
                }
                if (gdsMap.get("base_price")==null) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[base_price]参数不允许为空");
                    return RespUtil.renderRootResp(retMap); 
                }
                if (StringUtil.isBlank(gdsMap.get("base_price").toString())) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[base_price]参数不允许为空");
                    return RespUtil.renderRootResp(retMap); 
                }
                if (gdsMap.get("real_money")==null) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[real_money]参数不允许为空");
                    return RespUtil.renderRootResp(retMap); 
                }
                if (StringUtil.isBlank(gdsMap.get("real_money").toString())) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[real_money]参数不允许为空");
                    return RespUtil.renderRootResp(retMap); 
                }
            }
            
            String payWay=jsonObject.getString("pay_way");
            if (StringUtil.isBlank(payWay)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[pay_way]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            String basicMoney=jsonObject.getString("basic_money");
            if (StringUtil.isBlank(basicMoney)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[basic_money]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            String realMoney=jsonObject.getString("real_money");
            if (StringUtil.isBlank(realMoney)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[real_money]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            
            String source=jsonObject.getString("source");
            if (StringUtil.isBlank(source)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[source]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            
            
            ROrdMainResponse response=new ROrdMainResponse();
            String orderID=source+orderCode;
            
            ROrdMainResponse ordMainResponse=ordMainRSV.queryOrderByOrderId(orderID);
            if(ordMainResponse!=null){
                retMap.put("CODE", "2");
                retMap.put("MSG", "[order_code="+orderCode+"]该订单号已经存在，请核实！");
                return RespUtil.renderRootResp(retMap); 
            }
            
            response.setId(orderID);
            response.setOrderCode(orderID);
            response.setPayTranNo(payTranNo);
            
            response.setContactName(jsonObject.getString("contact_name"));
            response.setContactPhone(jsonObject.getString("contact_phone"));
            response.setChnlAddress(jsonObject.getString("contact_address"));
            response.setPayWay(payWay);
            response.setBasicMoney(Long.parseLong(basicMoney));
            response.setOrderMoney(Long.parseLong(basicMoney));
            response.setRealMoney(Long.parseLong(realMoney));
            String expressFee=jsonObject.getString("express_fee");
            if(StringUtil.isBlank(expressFee)){
                expressFee="0";
            }
            response.setRealExpressFee(Long.parseLong(expressFee));
            response.setShopId((long) 100);//默认自营店铺
            response.setStatus(OrdConstants.OrderStatus.ORDER_STATUS_RECEPT);
            response.setOrderType("01");
            response.setPayType(OrdConstants.Order.ORDER_PAY_TYPE_0);
            response.setPayFlag(OrdConstants.Order.ORDER_PAY_FLAG_1);
            response.setDispatchType(OrdConstants.Order.ORDER_DELIVER_FLAG_FALSE);
            response.setSource("2");//移动端
            response.setOrderTime(DateUtil.getSysDate());
            
            
            Long amount=(long)0;
            
            List<ROrdSubResponse> ordSubList=new ArrayList<>();
            for (Object object : gdsList) {
                Map gdsMap=(Map) object;
                ROrdSubResponse subResponse=new ROrdSubResponse();
                
                LogUtil.info(MODULE, "《--------处理原商品编码，处理成单品信息---------------》");
                String old_code=gdsMap.get("gds_code").toString();
                GdsSku2PropPropIdxReqDTO idxReqDTO=new GdsSku2PropPropIdxReqDTO();
                idxReqDTO.setPageSize(100);
                idxReqDTO.setPropId((long) 1051);
                idxReqDTO.setPropValue(old_code);
                idxReqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
                SkuQueryOption[] skuQuerys = new SkuQueryOption[1];
                skuQuerys[0]=SkuQueryOption.BASIC;
                idxReqDTO.setOptions(skuQuerys);
                PageResponseDTO<GdsSkuInfoRespDTO> responseDTO=gdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(idxReqDTO);
                if (responseDTO.getCount()!=1) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[gds_code=="+old_code+"]查不到相应的商品，请核实输入的商品编码是否正确");
                    return RespUtil.renderRootResp(retMap); 
                }
                List<GdsSkuInfoRespDTO> respDTOs=responseDTO.getResult();
                GdsSkuInfoRespDTO respDTO=respDTOs.get(0);
                subResponse.setGdsId(respDTO.getGdsId());
                subResponse.setSkuId(respDTO.getId());
                subResponse.setCategoryId(Long.parseLong(respDTO.getMainCatgs()));
                subResponse.setGdsName(respDTO.getGdsName());
                
                LogUtil.info(MODULE, "《--------处理原商品编码处理完毕---------------》");
                
                Long orderAmount=(long) 1;
                if (gdsMap.get("order_amount")!=null) {
                    orderAmount=Long.parseLong(gdsMap.get("order_amount").toString());
                }
                amount=amount+orderAmount;
                subResponse.setOrderAmount(orderAmount);
                subResponse.setStandardPrice(Long.parseLong(gdsMap.get("base_price").toString()));
                subResponse.setBuyPrice((Long.parseLong(gdsMap.get("real_money").toString())));
                subResponse.setOrderMoney(Long.parseLong(gdsMap.get("real_money").toString())*orderAmount);
                subResponse.setDeliverStatus("1");//发货状态是已发货
                subResponse.setRemainAmount((long) 0);//剩余未发货数量
                subResponse.setDeliverAmount(orderAmount);//已发货数量
                subResponse.setGdsType((long)2);//数字产品
                ordSubList.add(subResponse);
            }
            
            response.setOrdSubList(ordSubList);
            
            response.setOrderAmount(amount);
            
            //处理员工编号,当获取不到会员信息，新增会员信息
            AuthStaffResDTO staffResDTO= authStaffRSV.selectByStaffCode(staffCode);
            Long createStaff = staffResDTO.getId();
            if (createStaff==null) {
                SsoUserInfoReqDTO infoReqDTO=new SsoUserInfoReqDTO();
                infoReqDTO.setUserName(staffCode);
                SsoUserInfoMsgResDTO msgResDTO= ssoUserImportRSV.saveStaffInfo(infoReqDTO);
                if (!msgResDTO.isFlag()) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[staff_code:"+staffCode+"]参数非法，请核实该参数值");
                    return RespUtil.renderRootResp(retMap); 
                }
                createStaff=authStaffRSV.selectByStaffCode(staffCode).getId();
            }
            response.setStaffCode(staffCode);
            response.setCreateStaff(createStaff.toString());
            
            //生成订单信息
            ROrdMainResponse response2=ordMainRSV.createOrder(response);
            retMap.put(RespUtil.CODE, "0");
            retMap.put(RespUtil.MSG, "同步订单信息成功！订单编号为："+response2.getId());
            
        }catch(Exception e){
            retMap.put(RespUtil.CODE,"1");
            retMap.put(RespUtil.MSG, "系统错误："+e.getMessage());
        }
        return RespUtil.renderRootResp(retMap);
    }
    
    /*
     * 为临床/用药助手提供授权码接收接口
     */
	@RequestMapping(value="/rest" ,params="method=com.ai.ecp.pmph.order.receive")
	@Security(mustLogin=true,authorCheckType=DefaultServiceCheckChain.class)
	@ResponseBody
	public Map<String,Object> authorize(BizContentVO bizContentVO) throws BusinessException{
		Map<String,Object> retMap = new HashMap<String,Object>();
		String bizContent=bizContentVO.getBiz_content();
		
		//1.判断入参是否为空，为空返回错误信息
        if(StringUtil.isBlank(bizContent)){
            retMap.put(RespUtil.CODE, "1");
            retMap.put(RespUtil.MSG, "参数不允许为空");
            return RespUtil.renderRootResp(retMap);
        }
        
        //2.判断入参JSON格式是否正确
        try{
        	JSONObject jsonObject=JSONObject.parseObject(bizContentVO.getBiz_content());
        	String orderCode=jsonObject.getString("order_code");
        	if (StringUtil.isBlank(orderCode)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[order_code]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
        	String auth_code_uncoded=jsonObject.getString("auth_code");
        	if (StringUtil.isBlank(auth_code_uncoded)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[auth_code]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
        	String staffCode=jsonObject.getString("staff_code");
            if (StringUtil.isBlank(staffCode)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[staff_code]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            String source=jsonObject.getString("source");
            if (StringUtil.isBlank(source)) {
                retMap.put(RespUtil.CODE, "2");
                retMap.put(RespUtil.MSG, "[source]参数不允许为空");
                return RespUtil.renderRootResp(retMap); 
            }
            
            LogUtil.info(MODULE, "接受对端系统消息，子订单号为"+orderCode);
            RPreOrdSubResponse rPreOrdSubResponse = ordSubRSV.queryOrderSubBySubOrderId(orderCode);
            
            //开始发送MSG
            McParamsDTO mcParamsDTO = new McParamsDTO();
            mcParamsDTO.setFromUserId(OrdConstants.Common.DEFAULT_STAFFID);
            mcParamsDTO.setToUserId(rPreOrdSubResponse.getStaffId());
            mcParamsDTO.setMsgCode("code.send.notice");
            Map<String,String> notice = new java.util.HashMap<>();
            notice.put("orderId", rPreOrdSubResponse.getOrdMainId());
            notice.put("gdsName", rPreOrdSubResponse.getGdsName());
            
            //处理字符集
            String auth_code = new String(auth_code_uncoded.getBytes("ISO8859-1"),"UTF-8");
            notice.put("message", auth_code);
            mcParamsDTO.setMsgParams(notice);
            LogUtil.debug(MODULE, "处理外系统MSG"+JSON.toJSONString(mcParamsDTO));
            this.mcSyncSendRSV.sendMsg(mcParamsDTO);
            
            retMap.put(RespUtil.CODE,"0");
            retMap.put(RespUtil.MSG, "OK");
            
        }catch(Exception e){
            retMap.put(RespUtil.CODE,"1");
            retMap.put(RespUtil.MSG, "系统错误："+e.getMessage());
        }
        
		return RespUtil.renderRootResp(retMap);
	}
}

