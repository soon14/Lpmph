package com.ai.ecp.busi.order.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.mvc.annotation.NativeJson;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.vo.RQueryOrderReqVO;
import com.ai.ecp.order.dubbo.dto.RCustomerOrdResponse;
import com.ai.ecp.order.dubbo.dto.ROfflinePayResponse;
import com.ai.ecp.order.dubbo.dto.RQueryOrderRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IOrdReceiptRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdPmphMainRSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value="/order/pay")
public class PayOrdController extends EcpBaseController {

    private static final String MODULE = PayOrdController.class.getName();
    
    @Resource 
    private IOrdMainRSV ordMainRSV;
    
    @Resource
    private IOrdReceiptRSV ordReceiptRSV;
    
    @Resource
    private IOrdPmphMainRSV ordPmphMainRSV;
    
    /**
     * 
     * init:待支付列表获取
     * 
     * @param model
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping()
    public String init(Model model,RQueryOrderReqVO vo) throws Exception{
        LogUtil.debug(MODULE, vo.toString());
        //后场服务所需要的DTO；
        RQueryOrderRequest rdor = vo.toBaseInfo(RQueryOrderRequest.class);
        String status = OrdConstants.CustomerRequestStatus.REQUEST_STATUS_PAY;
        
//        rdor.setStaffId(ParamsTool.getStaffId());
        rdor.setStaffId(rdor.getStaff().getId());
        
        rdor.setSiteId(1l);
        rdor.setSysType("00");
        rdor.setStatus(status); // 
        
        ObjectCopyUtil.copyObjValue(vo, rdor, "", false);
        PageResponseDTO<RCustomerOrdResponse> rdors = ordPmphMainRSV.queryOrderByStaffId(rdor);
        if(rdors==null){
            rdors = new PageResponseDTO<RCustomerOrdResponse>();
            rdors.setPageSize(1);
        }
        //调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        model.addAttribute("status", status);
        
        //返回时间
        Map<String,Timestamp> dates = ParamsTool.params(vo);
        model.addAllAttributes(dates);
        model.addAttribute("resp",rdors);
        // 获取合并支付的开关
        BaseSysCfgRespDTO payMergeSysCfg = SysCfgUtil.fetchSysCfg(OrdConstants.OrdPayRel.SWITCH_PAY_MERGE);
        model.addAttribute("switchPayMerge", payMergeSysCfg.getParaValue());
        return "/order/pay-list";
    }
    
    /**
     * 
     * offline:(线下支付页面). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    //线下支付页面
    @RequestMapping(value="/offline")
    public String offline(Model model){
        ROfflinePayResponse offlineresp = new ROfflinePayResponse();
        offlineresp.setOrderAmount(100l);
        offlineresp.setOrderTime(new Timestamp(new Date().getTime()));
        offlineresp.setOrderType("01");
        offlineresp.setRealExpressFee(10000l);
        offlineresp.setRealMoney(1000l);
        offlineresp.setShopId(100l);
        offlineresp.setShopName("王的测试店铺");
        model.addAttribute("offlineresp", offlineresp);
        model.addAttribute("orderId", "D70014039");
        return "/order/pay/pay-offline";
    }
   
    /**
     * 
     * uploadImage:(上传图片). <br/> 
     * @param model
     * @param req
     * @param rep
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/uploadimage")
    @ResponseBody
    @NativeJson(true)
    private String uploadImage(Model model,HttpServletRequest req, HttpServletResponse rep) {
        JSONObject obj = new JSONObject();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
        // 获取图片
        Iterator<MultipartFile> files = multipartRequest.getFileMap().values().iterator();
        MultipartFile file = null;
        if (files.hasNext()) {
            file = files.next();
        }
        Iterator<String> ids = multipartRequest.getFileMap().keySet().iterator();
        String id = null;
        if (ids.hasNext()) {
            id = ids.next();
        }
        try {
            if (file == null) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择上传文件！");
                LogUtil.error(MODULE, "请选择上传文件！");
                return obj.toJSONString();
            }
            String fileName = file.getOriginalFilename();
            String extensionName = "." + getExtensionName(fileName);

            /** 支持文件拓展名：.jpg,.png,.jpeg,.gif,.bmp */
            boolean flag = Pattern.compile("\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$|\\.(bmp)$")
                    .matcher(extensionName.toLowerCase()).find();
            if (!flag) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)!");
                LogUtil.error(MODULE, "上传图片失败,原因---请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)!");
                return obj.toJSONString();
            }
            byte[] datas = inputStream2Bytes(file.getInputStream());
            if(datas.length>5*1024*1024){
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "上传的图片大于5M!");
                LogUtil.error(MODULE, "图片上传失败，上传的图片必须小于5M!");
                return obj.toJSONString();
            }
            String vfsId = ImageUtil.upLoadImage(datas, fileName);
            resultMap.put("vfsId", vfsId);
            resultMap.put("imageName", fileName);
            resultMap.put("id", id);
            resultMap.put("imagePath", getImageUrl(vfsId,"150x150!"));
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            obj.put("message", "保存成功!");
            obj.put("map", resultMap);
        } catch (Exception e) {
            LogUtil.error(MODULE,"上传图片出错,原因---"+e.getMessage(), e);
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
        }
        return obj.toJSONString();
    }
    
    /**
     * 获取文件拓展名
     * @param fileName
     * @return
     */
    private String getExtensionName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }
    
    /**
     * 将InputStream转换成byte数组
     * @param in
     * @return
     * @throws IOException
     */
    private byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count = -1;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return outStream.toByteArray();
    }
    
    /**
     * 根据上传到mongoDB的图片ID 从mongoDB上获取图片路径
     * @param vfsId
     * @param param
     * @return
     */
    private String getImageUrl(String vfsId,String param){
        StringBuilder sb=new StringBuilder();
        sb.append(vfsId);
        if(!StringUtil.isBlank(param)){
            sb.append("_");
            sb.append(param);
        }
        return ImageUtil.getImageUrl(sb.toString());
    }

}

