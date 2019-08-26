package com.ai.ecp.pmph.busi.pub.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.mvc.annotation.NativeJson;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.seller.order.vo.RFileImportReqVO;
import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.ROrdZDResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdZDResponse;
import com.ai.ecp.order.dubbo.dto.RSumbitZDMainsRequest;
import com.ai.ecp.order.dubbo.interfaces.IZDOrdMainRSV;
import com.ai.ecp.pmph.dubbo.dto.ROrdZDImportLogResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportZDMainRSV;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.PubUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.PubUserNAcctInfoRespDTO;
import com.ai.ecp.staff.dubbo.interfaces.IPubRSV;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value="/puborder/build")
public class BuildPubOrdController extends EcpBaseController {

	private static final String MODULE = BuildPubOrdController.class.getName();
	
	@Resource
	private IOrdImportZDMainRSV ordImportZDMainRSV;
	
	@Resource
	private IZDOrdMainRSV zDOrdMainRSV;
	
	@Resource
	private IPubRSV pubRSV;
	
	@RequestMapping()
    public String init(Model model) throws Exception {
		
        model.addAttribute("staffId", new BaseInfo<>().getStaff().getId());
        
        return "/pub/build/build";
    }

	@RequestMapping(value= "/create-success/{rOrdZDResponseRedisKey}/{ordMainMapsFailedRedisKey}")
	public String success2(@PathVariable("rOrdZDResponseRedisKey")String rOrdZDResponseRedisKey, @PathVariable("ordMainMapsFailedRedisKey")String ordMainMapsFailedRedisKey, Model model){
		
		model.addAttribute("rOrdZDResponseRedisKey", rOrdZDResponseRedisKey);
		model.addAttribute("ordMainMapsFailedRedisKey", ordMainMapsFailedRedisKey);
		
		return "/pub/build/create-success";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value= "/create-success-ajax/{rOrdZDResponseRedisKey}/{ordMainMapsFailedRedisKey}")
	@ResponseBody
	public Map<String, Object> successAjax(@PathVariable("rOrdZDResponseRedisKey")String rOrdZDResponseRedisKey, @PathVariable("ordMainMapsFailedRedisKey")String ordMainMapsFailedRedisKey){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(!ordMainMapsFailedRedisKey.equals("0")){
            //所需数据
        	//1.如果导入失败，则需要显示失败的订单号存入一个list返回
        	//初始化一个list用于装载失败的orderId
        	List<String> ordMainIdsFailed = new ArrayList<String>();
        	
        	@SuppressWarnings("unchecked")
			List<ConcurrentHashMap<String, RPreOrdMainsResponse>> ordMainMapsListFailed = (List<ConcurrentHashMap<String, RPreOrdMainsResponse>>)CacheUtil.getItem(ordMainMapsFailedRedisKey);
        	for (ConcurrentHashMap<String, RPreOrdMainsResponse> ordMainMapsFailed : ordMainMapsListFailed) {
        		for(Map.Entry<String, RPreOrdMainsResponse> ordMainMapFailed : ordMainMapsFailed.entrySet()){
        			ordMainIdsFailed.add(ordMainMapFailed.getValue().getPreOrdMainList().get(0).getPhone());
        		}
			}
        	
        	map.put("ordMainIdsFailed", ordMainIdsFailed);
        	//清除缓存
            CacheUtil.delItem(ordMainMapsFailedRedisKey);
        }
        ROrdZDResponse rOrdZDResponse = (ROrdZDResponse)CacheUtil.getItem(rOrdZDResponseRedisKey);
        //把成功与失败提交订单数加入页面显示
        map.put("sucCount", rOrdZDResponse.getSucCount());
        map.put("failCount", rOrdZDResponse.getFailCount());
        //2.判断如果征订单的status状态是01则表示余额不足,需要查询征订单账户余额
        if(rOrdZDResponse.getStatus()!=null){
        	if(!rOrdZDResponse.getSuccessFlag()){        	
            	
            	//2.1需要显示征订单账户实体内容
            	PubUserInfoReqDTO pubUserInfoReqDTO = new PubUserInfoReqDTO();
        		pubUserInfoReqDTO.setId(new BaseInfo().getStaff().getId());
        		PageResponseDTO<PubUserNAcctInfoRespDTO> pubUserNAcctInfo = pubRSV.queryPubUserNAcctInfoPage(pubUserInfoReqDTO);
        		PubUserNAcctInfoRespDTO pubUserNAcctInfoRespDTO = pubUserNAcctInfo.getResult().get(0);
        		map.put("pubUserNAcctInfoRespDTO", pubUserNAcctInfoRespDTO);
            	//2.2需要显示征订单金额
        		map.put("zDRealMoney", rOrdZDResponse.getRealMoney());
        		map.put("status", "01");
            }else{

                //3.如果征订单的status状态是02则表示余额充足无需显示任何内容
            	map.put("status", "02");
            }
        }
        
        map.put("flag", rOrdZDResponse.getSuccessFlag());
        
        //4.清除缓存
        CacheUtil.delItem(rOrdZDResponseRedisKey);
		
		return map;
	}
    
    /**
     * 
     * uploadfile:(上传征订单excel文件). <br/> 
     * 
     * @param excel
     * @param model
     * @param request
     * @param response
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	@ResponseBody
	@NativeJson(true)
    public String uploadFile(Model model, 
            @RequestParam(value = "uploadFileObj", required = false) MultipartFile uploadFileObj,
            HttpServletRequest req,HttpServletResponse rep){
    	
    	return this.fileValid(uploadFileObj);
    }
    
    /**
     * 
     * initData:(征订单页面初始化或刷新). <br/>
     * 
     * @param model
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/initdata")
    public Map<String,Object> initData(){
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	@SuppressWarnings("unchecked")
		List<String> dataCacheImpList = (List<String>)CacheUtil
        	.getItem(new StringBuilder("zDImpLogList").append(new BaseInfo<>().getStaff().getStaffCode()).toString());
        
    	
    	@SuppressWarnings("rawtypes")
		String redisKey = (String)CacheUtil.getItem(new BaseInfo().getStaff().getStaffCode());
        if(redisKey!=null){
        	map.put("zDRedisHash", redisKey);
        	map.put("zDPreOrd", (RPreOrdZDResponse)CacheUtil.getItem(redisKey));
        }
        if(dataCacheImpList!=null){        	
        	map.put("dataCacheImpList", dataCacheImpList);
        }
    	return map;
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
    
    /**
     * 
     * pubOrdImport:(征订单导入). <br/>
     * 
     * @param model
     * @return 
     * @since JDK 1.7
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pubOrdImport")
    public Map<String,Object> pubOrdImport(Model model, RFileImportReqVO vo){
    	Map<String, Object> map = new HashMap<String, Object>();
        RFileImportRequest rFileImportRequest = new RFileImportRequest();
        rFileImportRequest = vo.toBaseInfo(RFileImportRequest.class);
        ObjectCopyUtil.copyObjValue(vo, rFileImportRequest, "", false);
        List<ROrdZDImportLogResp> rOrdZDImportLogResps = new ArrayList<ROrdZDImportLogResp>();
        try{
        	rOrdZDImportLogResps = ordImportZDMainRSV.importZDMain(rFileImportRequest);
            if(StringUtil.isNotEmpty(rOrdZDImportLogResps)){
                map.put("flag",1);
                //分成两个list一个为成功的，一个失败的
                List<ROrdZDImportLogResp> successList = new ArrayList<ROrdZDImportLogResp>();
                List<ROrdZDImportLogResp> failList = new ArrayList<ROrdZDImportLogResp>();
                for (ROrdZDImportLogResp rOrdZDImportLogResp : rOrdZDImportLogResps) {
					if(rOrdZDImportLogResp.getStatus().equals("1")){
						successList.add(rOrdZDImportLogResp);
					}else{
						failList.add(rOrdZDImportLogResp);
					}
				}
                map.put("successCount", successList.size());
                map.put("failCount", failList.size());
            }else{
                map.put("flag",0);
            }
            //把预订单的数据导入
			String redisKey = (String)CacheUtil.getItem(new BaseInfo().getStaff().getStaffCode());
            RPreOrdZDResponse rPreOrdZDResponse = (RPreOrdZDResponse) CacheUtil.getItem(redisKey);
            map.put("zDRedisHash", redisKey);
            map.put("zDPreOrd", rPreOrdZDResponse);
        }catch(Exception e){
            LogUtil.info(MODULE, "征订单导入失败,原因---" + e.getMessage(), e);
            map.put("flag",2);
            map.put("msg",e.getMessage());
        }
        map.put("fileId",vo.getFileId());
        map.put("fileName", vo.getFileName());
        map.put("errorFileId", (String)CacheUtil.getItem(new StringBuilder("errorFileId").append(new BaseInfo().getStaff().getStaffCode()).toString()));
    	return map;
    }
    
    /**
     * 
     * allzddownload:(征订单导出{包含错误信息}). <br/>
     * 
     * @param model
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/allzddownload/{fileId}")
    @ResponseBody
    public void allzddownload(@PathVariable("fileId") String fileId, HttpServletResponse response){
		
    	if(fileId == null || StringUtil.isEmpty(fileId)){
    		return;
    	}
    	
		ServletOutputStream outputStream = null;
		try{
            byte[] bytes=FileUtil.readFile(fileId);
            String fileType=FileUtil.getFileType(fileId);
            String getFileName=FileUtil.getFileName(fileId) ;
            String fileName =getFileName + ".xls";
            if("xlsx".equals(fileType)){
            	fileName = getFileName + ".xlsx";
            }
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //设置响应头和下载保存的文件名      用关键字命名
//            response.setHeader("content-disposition","attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("content-disposition","attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
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
                e.printStackTrace();
            }
        }
    	
    }
    
    /**
     * 
     * delordmain:(征订单下删除某个订单). <br/>
     * 
     * @param model
     * @return 
     * @since JDK 1.7
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delordmain")
    public Map<String,Object> delOrdMain(@RequestParam("preOrdMainId") String preOrdMainId, @RequestParam("zDRedisHash") String zDRedisHash){
    	Map<String, Object> map = new HashMap<String, Object>();
    	
		RPreOrdZDResponse rPreOrdZDResponse = (RPreOrdZDResponse) CacheUtil.getItem(zDRedisHash);
		if(rPreOrdZDResponse == null){
			throw new BusinessException("预征订单已失效!");
		}
		//step1:初始化征订单元素变量
		Long orderMoney = 0L;
		Long realMoney = 0L;
		Long discountMoney = 0L;
		Long realExpressFee = 0L;
		
		//step2:删除订单元素
		for (ConcurrentHashMap<String, RPreOrdMainsResponse> mainOrdMap : rPreOrdZDResponse.getOrdMainMap()) {
			for (Map.Entry<String, RPreOrdMainsResponse> mainOrders : mainOrdMap.entrySet()){
				if(mainOrders.getKey().equals(preOrdMainId)){
					RPreOrdMainResponse OrdMainNeedRemove = mainOrders.getValue().getPreOrdMainList().get(0);
					//step3:变量值处理
					orderMoney = rPreOrdZDResponse.getOrderMoney() - OrdMainNeedRemove.getOrderMoney();
					realMoney = rPreOrdZDResponse.getRealMoney() - OrdMainNeedRemove.getRealMoney();
					discountMoney = rPreOrdZDResponse.getDiscountMoney() - OrdMainNeedRemove.getDiscountMoney();
					realExpressFee = rPreOrdZDResponse.getRealExpressFee() - OrdMainNeedRemove.getRealExpressFee();
					mainOrdMap.remove(preOrdMainId);
				}
			}
		}
		//step4:重新赋值
		rPreOrdZDResponse.setOrderMoney(orderMoney);
		rPreOrdZDResponse.setRealMoney(realMoney);
		rPreOrdZDResponse.setDiscountMoney(discountMoney);
		rPreOrdZDResponse.setRealExpressFee(realExpressFee);
		//step5:重新放数据进入缓存
		String redisKey = md5key(new StringBuilder().append(rPreOrdZDResponse.getStaffId()).append(DateUtil.getCurrentTimeMillis()).toString());
		CacheUtil.addItem(new StringBuilder().append(redisKey).toString(), rPreOrdZDResponse,1800);
		//把redisKey也放入缓存
		CacheUtil.addItem(new StringBuilder().append(new BaseInfo().getStaff().getStaffCode()).toString(), redisKey, 1800);
		
		//放入map返回
		map.put("zDRedisHash", redisKey);
    	map.put("zDPreOrd", (RPreOrdZDResponse)CacheUtil.getItem(redisKey));
		return map;
    }
    /**
     * 
     * md5key:MD5加密. <br/> 
     * @param str
     * @return 
     * @since JDK 1.6
     */
    private String md5key(String str){
        return DigestUtils.md5Hex(str);
    }
    
    /**
     * 
     * submitOrd:征订单提交. <br/> 
     * @param str
     * @return 
     * @since JDK 1.6
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/submitOrd/{zDRedisHash}")
    public Map<String,Object> submitOrd(@PathVariable("zDRedisHash") String zDRedisHash, @RequestParam(value="buyerMsgMap")String buyerMsgMap, Model model){
    	Map<String, Object> buyerMsgs = JSON.parseObject(buyerMsgMap);
    	//初始化返回map
    	Map<String,Object> map = new HashMap<String, Object>();
    	//初始化DTO
    	RSumbitZDMainsRequest sumbitZDMains = new RSumbitZDMainsRequest();
    	
    	try {
    		
    		RPreOrdZDResponse zDOrdMains = (RPreOrdZDResponse) CacheUtil.getItem(zDRedisHash);
    		
	    	if(zDOrdMains==null){
				if(sumbitZDMains.getStaff()==null || sumbitZDMains.getStaff().getId()==0){
					throw new Exception("用户未登录");
				}
				throw new Exception("征订单信息已失效");
			}
	    	
	    	ObjectCopyUtil.copyObjValue(zDOrdMains, sumbitZDMains, "", false);
	    	//设置卖家备注
	    	for (ConcurrentHashMap<String, RPreOrdMainsResponse> preOrdMains : sumbitZDMains.getOrdMainMap()) {
				for(Map.Entry<String, RPreOrdMainsResponse> preOrdMain : preOrdMains.entrySet()){
					for(Map.Entry<String, Object> buyerMsg : buyerMsgs.entrySet()){
						if(preOrdMain.getKey().equals(buyerMsg.getKey())){
							preOrdMain.getValue().getPreOrdMainList().get(0).setRemark((String)buyerMsg.getValue());
						}
					}
				}
			}
	    	Map<String, String> mapforRedisKeys = zDOrdMainRSV.sumbitOrd(sumbitZDMains);
	    	map.put("mapforRedisKeys", mapforRedisKeys);
	    	if(mapforRedisKeys.get("ordMainMapsFailedRedisKey").equals("0")){	    		
	    		map.put("msg", "订单提交成功!");
	    	}else{
	    		StringBuilder s = new StringBuilder();
	    		s.append("订单提交未完成,失败订单订单号为:");
	    		int n = 0;
	    		@SuppressWarnings("unchecked")
				List<ConcurrentHashMap<String, RPreOrdMainsResponse>> ordMainMapsFailed = (List<ConcurrentHashMap<String, RPreOrdMainsResponse>>) CacheUtil.getItem(mapforRedisKeys.get("ordMainMapsFailedRedisKey"));
	    		for (ConcurrentHashMap<String, RPreOrdMainsResponse> ordMainMapFailed : ordMainMapsFailed) {
					for (Map.Entry<String, RPreOrdMainsResponse> ordMainFailed : ordMainMapFailed.entrySet()) {
						if(n==ordMainMapsFailed.size()-1){
							s.append(ordMainFailed.getValue().getPreOrdMainList().get(0).getOrderId());
						}else{
							s.append(ordMainFailed.getValue().getPreOrdMainList().get(0).getOrderId()).append(",");
						}
					}
					
					n++;
				}
	    		map.put("msg", s.toString());
	    	}
	    	
	    	//清除预征订单导入日志数据
	    	CacheUtil.delItem(new StringBuilder("zDImpLogList").append(new BaseInfo().getStaff().getStaffCode()).toString());
	    	//清除预征订单redisKey
	    	CacheUtil.delItem(new StringBuilder().append(new BaseInfo().getStaff().getStaffCode()).toString());
	    	//清除预征订单缓存
	    	CacheUtil.delItem(zDRedisHash);
	    	
    	} catch (Exception e){
    		e.printStackTrace();
    		LogUtil.error(MODULE, "提交订单失败");
    		map.put("msg", "提交订单失败!");
    	}
    	return map;
    }
    
    /**
     * 
     * cacheImpLog:缓存征订单日志导入. <br/> 
     * @param str
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/cacheImpLog")
    public void cacheImpLog(@RequestParam(value="dataJsonArr")String dataJsonArr, Model model){
    	List<String> dataImpList = (List<String>)JSONArray.parseArray(dataJsonArr, String.class);
    	
    	@SuppressWarnings("unchecked")
    	List<String> dataCacheImpList = (List<String>)CacheUtil
    		.getItem(new StringBuilder("zDImpLogList").append(new BaseInfo<>().getStaff().getStaffCode()).toString());
    	if(dataCacheImpList==null){
    		CacheUtil
    			.addItem(new StringBuilder("zDImpLogList").append(new BaseInfo<>().getStaff().getStaffCode()).toString(), dataImpList, 1800);
    	}else{
    		for (String string : dataImpList) {
    			dataCacheImpList.add(string);
			}
    		CacheUtil
    			.addItem(new StringBuilder("zDImpLogList").append(new BaseInfo<>().getStaff().getStaffCode()).toString(), dataCacheImpList, 1800);
    	}
    }
}
