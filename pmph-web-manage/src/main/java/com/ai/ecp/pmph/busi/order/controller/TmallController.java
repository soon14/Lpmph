package com.ai.ecp.pmph.busi.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.mvc.annotation.NativeJson;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.order.util.ParamsTool;
import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.pmph.busi.order.vo.RFileImportReqVO;
import com.ai.ecp.pmph.busi.order.vo.ROrdTmMainReqVO;
import com.ai.ecp.pmph.busi.order.vo.ROrdTmSubReqVO;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmImportLogResp;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmMainResp;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportTMMainRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportTMSubRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdTmMainRSV;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdTmSubRSV;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/order/tmall")
public class TmallController extends EcpBaseController {

    private static String MODULE = TmallController.class.getName();
    
    
 
    @Resource 
    private IOrdTmMainRSV ordTmMainRSV; 
    
    @Resource 
    private IOrdTmSubRSV ordTmSubRSV; 
    
    @Resource 
    private IOrdImportTMMainRSV ordImportTMMainRSV; 
    
    @Resource 
    private IOrdImportTMSubRSV ordImportTMSubRSV; 

    /**
     * 
     * init:(默认进入天猫订单页面 ). <br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping()
    public String init(Model model) {
        model.addAllAttributes(ParamsTool.params());
        return "/order/tmall/tmall-grid";
    }


    /**
     * 
     * tmallList:(天猫订单列表). <br/> 
     * 
     * @param model
     * @param vo
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value="/tmallList")
    public Model tmallList(Model model,ROrdTmMainReqVO vo) throws Exception{
        LogUtil.info(MODULE, "======天猫订单列表查询开始 ======");
        ROrdTmMainReq rOrdTmMainReq = new ROrdTmMainReq();
        rOrdTmMainReq = vo.toBaseInfo(ROrdTmMainReq.class);
        ObjectCopyUtil.copyObjValue(vo, rOrdTmMainReq, "", false);
        PageResponseDTO<ROrdTmMainResp> dto =ordTmMainRSV.queryOrderTmMain(rOrdTmMainReq);
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(dto); 
        LogUtil.info(MODULE, "======天猫订单列表查询结束 ======");
        return super.addPageToModel(model, ParamsTool.ordDetailSiteUrl(respVO)); 
    }   

    /**
     * 
     * tmallDetailList:(天猫订单明细列表). <br/>
     * 
     * @param model
     * @param orderId
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value="/tmallDetailList")
    @ResponseBody
    public Model tmallDetailList(Model model,ROrdTmSubReqVO vo) throws Exception{
        LogUtil.info(MODULE, "======天猫订单明细查询开始 ======");
        ROrdTmSubReq rOrdTmSubReq = new ROrdTmSubReq();
        rOrdTmSubReq = vo.toBaseInfo(ROrdTmSubReq.class);
        ObjectCopyUtil.copyObjValue(vo, rOrdTmSubReq, "", false);
        PageResponseDTO<ROrdTmSubResp> dto =ordTmSubRSV.queryOrderTmSubByOrderId(rOrdTmSubReq);
        // 调用，并结果返回；从后场返回的分页对象，封装为前店所需要的分页对象；
        EcpBasePageRespVO<Map> respVO = EcpBasePageRespVO.buildByPageResponseDTO(dto); 
        LogUtil.info(MODULE, "======天猫订单明细查询结束======"); 
        return super.addPageToModel(model, respVO);

    }
    
    /**
     * 
     * ordImportPage:(天猫订单导入页面). <br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/ordImportPage")
    public String ordImportPage(Model model){  
        Long staffId = new BaseInfo().getStaff().getId();
        model.addAttribute("staffId",staffId);
        return "order/tmall/import/tmall-import";
    }

    /**
     * 
     * uploadFile:(上传文件). <br/> 
     * 
     * @param model
     * @param uploadFileObj
     * @param req
     * @param rep
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/uploadfile")
    @ResponseBody
    @NativeJson(true)
    public String uploadFile(Model model, 
            @RequestParam(value = "uploadFileObj", required = false) MultipartFile uploadFileObj,
            HttpServletRequest req,HttpServletResponse rep) throws Exception {
        return this.fileValid(uploadFileObj);
    }

    /**
     * 
     * uploadFile:(上传文件). <br/> 
     * 
     * @param model
     * @param uploadFileObj
     * @param req
     * @param rep
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/uploadfile2")
    @ResponseBody
    @NativeJson(true)
    public String uploadFile2(Model model, 
            @RequestParam(value = "uploadFileObj2", required = false) MultipartFile uploadFileObj2,
            HttpServletRequest req,HttpServletResponse rep) throws Exception {
        return this.fileValid(uploadFileObj2);
    }
    
    /**
     * 
     * ordImport:(天猫订单导入). <br/>
     * 
     * @param model
     * @param vo
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/ordImport")
    public Map<String,Object> ordImport(Model model,RFileImportReqVO vo){ 
        Map<String, Object> map = new HashMap<String, Object>();
        RFileImportRequest rFileImportRequest = new RFileImportRequest();
        rFileImportRequest = vo.toBaseInfo(RFileImportRequest.class);
        ObjectCopyUtil.copyObjValue(vo, rFileImportRequest, "", false);
        List<ROrdTmImportLogResp> rOrdTmImportLogResps = new ArrayList<ROrdTmImportLogResp>();
        try{
            rOrdTmImportLogResps = ordImportTMMainRSV.importTMMain(rFileImportRequest);
            if(StringUtil.isNotEmpty(rOrdTmImportLogResps)){
                map.put("flag",1);
            }else{
                map.put("flag",0);
            }
        }catch(Exception e){
            LogUtil.info(MODULE, "天猫订单导入失败,原因---" + e.getMessage(), e);
            map.put("flag",2);
            map.put("msg",e.getMessage());
        }
        map.put("fileId",vo.getFileId());
        return map;
    }  
    
    /**
     * 
     * ordDetailImport:(天猫订单明细导入). <br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/ordDetailImport")
    public Map<String,Object> ordDetailImport(Model model,RFileImportReqVO vo){  
        Map<String, Object> map = new HashMap<String, Object>();
        RFileImportRequest rFileImportRequest = new RFileImportRequest();
        rFileImportRequest = vo.toBaseInfo(RFileImportRequest.class);
        ObjectCopyUtil.copyObjValue(vo, rFileImportRequest, "", false);
        List<ROrdTmImportLogResp> rOrdTmImportLogResps = new ArrayList<ROrdTmImportLogResp>();
        try{
            rOrdTmImportLogResps = ordImportTMSubRSV.importTMSub(rFileImportRequest);
            if(StringUtil.isNotEmpty(rOrdTmImportLogResps)){
                map.put("flag",1);
            }else{
                map.put("flag",0);
            }
        }catch(Exception e){
            LogUtil.info(MODULE, "天猫订单明细导入失败,原因---" + e.getMessage(), e);
            map.put("flag",2);
            map.put("msg",e.getMessage());
        }
        map.put("fileId",vo.getFileId());
        return map;
    }
    
    /**
     * 
     * tmallDetailGrid:(天猫订单明细页面). <br/>
     * 
     * @param model
     * @param orderId
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/tmallDetailGrid")
    public String tmallDetailGrid(Model model,@RequestParam(value="orderId",required=false) String orderId){
        model.addAttribute("orderId", orderId);
        return "/order/tmall/detail/tmall-detail";  
    }  
    
    /**
     * 
     * importTMFailure:(天猫订单导入失败页面). <br/>
     * 
     * @param model
     * @param orderId
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/importTMFailure/{fileId}")
    public String importTMFailure(Model model, @PathVariable("fileId") String fileId){
        List<ROrdTmImportLogResp> importTMFailures = ordImportTMMainRSV.queryFailTmOrdImport(fileId, "06");
        model.addAttribute("importTMFailures", importTMFailures);
        return "/order/tmall/import/import-failure";  
    }
    
    /**
     * 
     * importTMDetailFailure:(天猫订单导入失败页面). <br/>
     * 
     * @param model
     * @param orderId
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value="/importTMDetailFailure/{fileId}")
    public String importTMDetailFailure(Model model, @PathVariable("fileId") String fileId){
        List<ROrdTmImportLogResp> importTMFailures = ordImportTMMainRSV.queryFailTmOrdImport(fileId, "07");
        model.addAttribute("importTMFailures", importTMFailures);
        return "/order/tmall/import/import-failure";  
    }
    
    
    /**
     * 
     * fileValid:(证证上传文件). <br/>
     * 
     * @param uploadFileObj
     * @return 
     * @since JDK 1.6
     */
    private String fileValid(MultipartFile uploadFileObj){
        JSONObject obj = new JSONObject();// 返回结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            if (uploadFileObj == null) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择上传文件！");
                LogUtil.info(MODULE, "请选择上传文件！");
                return obj.toJSONString();
            }
            String vfsName = uploadFileObj.getOriginalFilename();// 文件名
            byte[] datas = uploadFileObj.getBytes();
            if (datas.length > 10 * 1024 * 1024) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "附件上传失败，上传的文件必须小于10M!");
                LogUtil.error(MODULE, "附件上传失败，上传的文件必须小于10M!");
                return obj.toJSONString();
            }

            String vfsId = FileUtil.saveFile(datas, vfsName,uploadFileObj.getContentType());
            resultMap.put("vfsId", vfsId);
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            obj.put("message", "附件上传成功!");
            obj.put("resultMap", resultMap);
        } catch (Exception e) {
            LogUtil.info(MODULE, "附件上传失败,原因---" + e.getMessage(), e);
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            obj.put("message", "附件上传失败，图片服务器异常，请联系管理员!");
        }
        return obj.toJSONString();
        
    } 
    
}
