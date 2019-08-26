package com.ai.ecp.common.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
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
import com.ai.ecp.common.vo.MessageHistoryReqVO;
import com.ai.ecp.common.vo.MessageHistoryRespVO;
import com.ai.ecp.common.vo.SessionReqVO;
import com.ai.ecp.common.vo.SessionRespVO;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineReqDTO;
import com.ai.ecp.im.dubbo.interfaces.ICustServiceMgrRSV;
import com.ai.ecp.im.dubbo.util.ImConstants;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.util.BizUtil;
import com.ai.ecp.util.ConstantTool;
import com.ai.ecp.util.EmoticonUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.MongoUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;

import sun.misc.BASE64Decoder;

/**
 * 
 * Project Name:ecp-web-im <br>
 * Description: <br>
 * Date:2017年3月29日下午4:46:05  <br>
 * 
 * @version  
 * @since JDK 1.7
 */

@Controller
@RequestMapping(value = "/mobilecusthistory")
public class MobileCustHistoryController  extends EcpBaseController {
	private static String MODULE = MobileCustHistoryController.class.getName();
	
	@Resource
	private ICustServiceMgrRSV custServiceMgrRSV;
	
	/**
	 * 保存会话
	 * @param reqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("saveSession")
	@ResponseBody
	public String saveSession(SessionReqVO reqVO) throws BusinessException{
		String uuid = UUID.randomUUID().toString();
		reqVO.setId(uuid);
		reqVO.setSessionTime(DateUtil.getSysDate());
		JSONObject doc = new JSONObject();
		doc = (JSONObject) JSON.toJSON(reqVO);
		MongoUtil.insert("T_IM_SESSION_HISTORY", doc);
		return uuid;
	}
	
	
	
	/**
	 * 修改会话
	 * @param reqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("updateSession")
	@ResponseBody
	public EcpBaseResponseVO updateSession(SessionReqVO reqVO)throws BusinessException{
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		try {
			BasicDBObject query = new BasicDBObject();
			query.put("id", reqVO.getId());
			DBObject stuFound = MongoUtil.getDBCollection("T_IM_SESSION_HISTORY").findOne(query);
			stuFound.put("status", ImConstants.STATE_0);
			WriteResult result = MongoUtil.getDBCollection("T_IM_SESSION_HISTORY").update(query, stuFound);
			if(result.getN()>0){
				ImStaffHotlineReqDTO dto = new ImStaffHotlineReqDTO();
				dto.setOfStaffCode(reqVO.getUserCode());
				dto.setCsaCode(reqVO.getCsaCode());
				dto.setReqTime(DateUtil.getSysDate().getTime());
				custServiceMgrRSV.staffFinishChat(dto);
			}
			vo.setResultFlag("ok");
		} catch (Exception e) {
			throw new BusinessException("会话结束失败");
		}
		
		return vo;
	}
	
	
	/**
	 * 获取会话次数
	 * @param reqVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getSessionCount")
	@ResponseBody
	public Map<String,Object> getSessionCount(SessionReqVO reqVO)throws Exception{
		Map<String,Object> ap = new HashMap<>();
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_SESSION_HISTORY");
		QueryBuilder queryBuilder = new QueryBuilder();
		
		if(StringUtil.isNotBlank(reqVO.getUserCode())){
			queryBuilder.and("userCode").is(reqVO.getUserCode());
		}
		
		 long count = collection.count(queryBuilder.get());
		 ap.put("count", count);
		 queryBuilder.and("status").is(ImConstants.STATE_0);
		 DBCursor cursor = collection.find(queryBuilder.get()).sort(new BasicDBObject("sessionTime",-1));
			try {
				while (cursor.hasNext()) {
					
					DBObject obj = cursor.next();
					ap.put("csaCode", obj.get("csaCode")+BizUtil.getOfServer());
					ap.put("sessionTime", obj.get("sessionTime"));
					return ap;
				}
			} finally {
				cursor.close();
			}
			return ap;
	}
	
	
	/**
	 * 获取会话列表
	 * @param reqVO
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getSessionByone")
	@ResponseBody
	public SessionRespVO getSessionByone(SessionReqVO reqVO)throws Exception{
	
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_SESSION_HISTORY");
		QueryBuilder queryBuilder = new QueryBuilder();
		
		if(StringUtil.isNotBlank(reqVO.getId())){
			queryBuilder.and("id").is(reqVO.getId());
		}
		
		//queryBuilder.and("status").is(ImConstants.STATE_1);
		SessionRespVO respVO=null;
		DBCursor cursor = collection.find(queryBuilder.get());
		try {
			while (cursor.hasNext()) {
				respVO = new SessionRespVO();
				DBObject obj = cursor.next();
				ConstantTool.dbObjectToBean(obj, respVO);
				respVO.setUserServer(respVO.getUserCode()+BizUtil.getOfServer());
				return respVO;
			}
		} finally {
			cursor.close();
		}
			return respVO;	
	}
	
	/**
	 * 获取会话列表
	 * @param reqVO
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getSessionList")
	@ResponseBody
	public List<SessionRespVO> getSessionList(SessionReqVO reqVO)throws Exception{
		List<SessionRespVO> list = new ArrayList<>();
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_SESSION_HISTORY");
		QueryBuilder queryBuilder = new QueryBuilder();
		
		if(StringUtil.isNotBlank(reqVO.getCsaCode())){
			queryBuilder.and("csaCode").is(reqVO.getCsaCode());
		}
		
		queryBuilder.and("status").is(BizUtil.status_1);
		
		DBCursor cursor = collection.find(queryBuilder.get());
		try {
			while (cursor.hasNext()) {
				SessionRespVO respVO = new SessionRespVO();
				DBObject obj = cursor.next();
				ConstantTool.dbObjectToBean(obj, respVO);
				respVO.setUserServer(respVO.getUserCode()+BizUtil.getOfServer());
				list.add(respVO);
			}
		} finally {
			cursor.close();
		}
			return list;	
	}
	
	
	/**
	 * 获取最近一次会话
	 * @param reqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("getSession")
	@ResponseBody
	public SessionRespVO getSession(SessionReqVO reqVO)throws BusinessException{
		
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_SESSION_HISTORY");
		BasicDBObject query = new BasicDBObject();
		query.put("userCode", reqVO.getUserCode());
		query.put("issueType", reqVO.getIssueType());
		if(reqVO.getIssueType().equals(ImConstants.issueType_1)){
			query.put("ordId", reqVO.getOrdId());
		}else if(reqVO.getIssueType().equals(ImConstants.issueType_2)){
			query.put("gdsId", reqVO.getGdsId());
		}
		DBCursor cursor =collection.find(query).sort(new BasicDBObject("sessionTime",-1));
		try {
			SessionRespVO resDTO = new SessionRespVO();
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				ConstantTool.dbObjectToBean(obj, resDTO);
				MessageHistoryReqVO historyReqVO = new MessageHistoryReqVO();
				historyReqVO.setSessionId(resDTO.getId());
				resDTO.setList(getMessageHistory(historyReqVO));
				return resDTO;
			}
		} finally {
			cursor.close();
		}
		return null;
		
		
	}
	
	
	/**
	 * 保存历史消息
	 * @param historyReqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("saveMsgHistory")
	@ResponseBody
	public MessageHistoryRespVO saveMsgHistory(MessageHistoryReqVO historyReqVO)throws BusinessException{
		//过滤下http超链接
		if(StringUtil.isNotBlank(historyReqVO.getBody())){
			
			if("0".equals(historyReqVO.getContentType()) ){
				String body = http(historyReqVO.getBody());
				historyReqVO.setBody(body);
			}
		}
		if(StringUtil.isBlank(historyReqVO.getSessionId())){
			SessionReqVO reqVO = new SessionReqVO();
			ObjectCopyUtil.copyObjValue(historyReqVO, reqVO, null, false);
			String uuid = saveSession(reqVO);
			historyReqVO.setSessionId(uuid);
		}
		MessageHistoryRespVO messageHistoryRespVO =new MessageHistoryRespVO();
		String uuid = UUID.randomUUID().toString();
		historyReqVO.setId(uuid);
		historyReqVO.setStatus(BizUtil.msg_status_10);
		historyReqVO.setBeginDate(DateUtil.getSysDate());
		JSONObject doc = new JSONObject();
		doc = (JSONObject) JSON.toJSON(historyReqVO);
		MongoUtil.insert("T_IM_MESSAGE_HISTORY", doc);
		ObjectCopyUtil.copyObjValue(historyReqVO, messageHistoryRespVO, null, false);
	
		return messageHistoryRespVO;
	}
	
	public String http(String body){
	    if(body.indexOf("</a>")>-1){
	    	body = body.replaceAll("<a href=", "<a target='_blank' href=");
	    	return body;
	    }
	    if(body.indexOf("<img")>-1){
	    	//String regxpForImgTag = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
	        String regexp  
	        = "(((http|ftp|https|file)://)|((?<!((http|ftp|https|file)://))www\\.))"  // 以http...或www开头  
	        + ".*?"                                                                   // 中间为任意内容，惰性匹配  
	        + "(?=(&nbsp;|\\s|　|<br />|$|[<>]))";  // 结束条件
	    	Pattern pattern = Pattern.compile(regexp);
	    	Matcher matcher = pattern.matcher(body);
	    	StringBuffer sb = new StringBuffer();
	    	while (matcher.find()) {
	    	    String temp = matcher.group();
	    	    String hz = temp.substring(temp.length()-4, temp.length()-1);
	    	    String hz1 = temp.substring(temp.length()-5, temp.length()-2);
	    	    if(hz.equals("gif")||hz.equals("jpg")||hz.equals("png")||hz.equals("jpeg")||hz.equals("bmp")){

	    	    }else if(hz1.equals("gif")||hz1.equals("jpg")||hz1.equals("png")||hz1.equals("jpeg")||hz1.equals("bmp")){

	    	    }
	    	    else{
	    	    	// body = body.replaceAll(temp,"<a target='_blank' href='"+temp+"'>"+temp+"</a>");
	    	    	matcher.appendReplacement(sb, "<a target='_blank' href='"+temp+"'>"+temp+"</a>");
	    	    }
	    	}
	    	matcher.appendTail(sb);
	    	return sb.toString();
	    }
	    body = body.replaceAll("(?is)(?<!')(http://[/\\.\\w.-]+)","<a target='_blank' href='$1'>$1</a>");
	    return body;
	}

	/**
	 * 通过session获取会话下的历史聊天记录
	 * @param historyReqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("getMessageHistory")
	@ResponseBody
	public List<MessageHistoryRespVO> getMessageHistory(MessageHistoryReqVO historyReqVO)throws BusinessException{
		List<MessageHistoryRespVO> list = new ArrayList<>();
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY");
		QueryBuilder  query = new QueryBuilder();
		if(StringUtil.isNotBlank(historyReqVO.getSessionId())){
			query.and("sessionId").is(historyReqVO.getSessionId());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getCsaCode())){
			query.and("csaCode").is(historyReqVO.getCsaCode());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getUserCode())){
			query.and("userCode").is(historyReqVO.getUserCode());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getFrom())){
			query.and("from").is(historyReqVO.getFrom());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getMessageType())){
			query.and("messageType").is(historyReqVO.getMessageType());
		}else{
			query.and("messageType").notIn(Arrays.asList("welcome", "inform"));
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getBody())){
			Pattern pattern = Pattern.compile("^.*" + historyReqVO.getBody()+ ".*$", Pattern.CASE_INSENSITIVE); 
			query.and("body").regex(pattern);
		}
		query.and("beginDate").lessThanEquals(DateUtil.getSysDate());
		query.and("beginDate").greaterThan(DateUtil.getOffsetDaysTime(DateUtil.getSysDate(), -7));
		String status = historyReqVO.getStatus();
		if(StringUtil.isNotBlank(status)){
			query.and("status").is(status);
		}
		DBCursor cursor =collection.find(query.get()).sort(new BasicDBObject("beginDate",-1)).skip(historyReqVO.getStartRowIndex()).limit(historyReqVO.getEndRowIndex());
		try {
			
			while (cursor.hasNext()) {
				MessageHistoryRespVO resDTO = new MessageHistoryRespVO();
				DBObject obj = cursor.next();
				ConstantTool.dbObjectToBean(obj, resDTO);
				transMessage(resDTO);
				list.add(resDTO);
			}
		} finally {
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 
	 * transMessage:(转化成web可展示的消息). <br/> 
	 * 
	 * @param vo
	 * @throws BusinessException 
	 * @since JDK 1.7
	 */
	private static void transMessage(MessageHistoryRespVO vo) throws BusinessException{
		if("APP".equals(vo.getFrom().split("/")[1]) && "msg".equals(vo.getMessageType()) 
				&& StringUtil.isNotBlank(vo.getContentType())){
			
			String msgBody = vo.getBody();
			String contentType = vo.getContentType();
			//消息内容处理
			if("1".equals(contentType)){//处理接收的图片类型消息
				String imageUrl = ImageUtil.getImageUrl(msgBody);//转义图片
				String htmlBody = "<img src=\""+imageUrl+"\" alt=\"\">";
				vo.setBody(htmlBody);
			}else if("0".equals(contentType)){//处理接收的文字类型消息，如果包含表情
				Pattern r = Pattern.compile("\\[\\S+?\\]");
				Matcher m = r.matcher(msgBody);
				while(m.find()){
					String matchWord = m.group(0);
					msgBody = msgBody.replace(matchWord, EmoticonUtil.getHtmlTag(matchWord));
				}
				vo.setBody(msgBody);
			} 
		}
	}
	
	/**
	 * 获取聊天记录总记录数
	 * @param historyReqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("getMessageCount")
	@ResponseBody
	public Long getMessageCount(MessageHistoryReqVO historyReqVO)throws BusinessException{
		
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY");
		QueryBuilder  query = new QueryBuilder();
		
		if(StringUtil.isNotBlank(historyReqVO.getCsaCode())){
			query.and("csaCode").is(historyReqVO.getCsaCode());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getUserCode())){
			query.and("userCode").is(historyReqVO.getUserCode());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getFrom())){
			query.and("from").is(historyReqVO.getFrom());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getMessageType())){
			query.and("messageType").is(historyReqVO.getMessageType());
		}
		query.and("status").is("20");
		if(StringUtil.isNotBlank(historyReqVO.getBody())){
			Pattern pattern = Pattern.compile("^.*" + historyReqVO.getBody()+ ".*$", Pattern.CASE_INSENSITIVE); 
			query.and("body").regex(pattern);
		}
		query.and("beginDate").lessThanEquals(DateUtil.getSysDate());
		query.and("beginDate").greaterThan(DateUtil.getOffsetDaysTime(DateUtil.getSysDate(), -7));
		//BasicDBObject dbObject = (BasicDBObject) query.get().put("$gte", BasicDBObjectBuilder.start("beginDate",DateUtil.getOffsetDaysDate(DateUtil.getSysDate(), 7)).add("$lt", DateUtil.getSysDate()));
		long count  =collection.count(query.get());
		return count;
	}
	
	
	/**
	 *  查找聊天记录
	 * @param historyReqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("getMessageHistoryDate")
	@ResponseBody
	public List<MessageHistoryRespVO> getMessageHistoryByDate(MessageHistoryReqVO historyReqVO)throws BusinessException{
		List<MessageHistoryRespVO> list = new ArrayList<>();
		DBCollection collection  =MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY");
		QueryBuilder  query = new QueryBuilder();
		
		if(StringUtil.isNotBlank(historyReqVO.getCsaCode())){
			query.and("csaCode").is(historyReqVO.getCsaCode());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getUserCode())){
			query.and("userCode").is(historyReqVO.getUserCode());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getFrom())){
			query.and("from").is(historyReqVO.getFrom());
		}
		
		if(StringUtil.isNotBlank(historyReqVO.getMessageType())){
			query.and("messageType").is(historyReqVO.getMessageType());
		}else{
			query.and("messageType").notIn(Arrays.asList("welcome", "inform"));
		}
	//	query.and("status").is("10");
		if(StringUtil.isNotBlank(historyReqVO.getBody())){
			Pattern pattern = Pattern.compile("^.*" + historyReqVO.getBody()+ ".*$", Pattern.CASE_INSENSITIVE); 
			query.and("body").regex(pattern);
		}
		query.and("beginDate").lessThanEquals(DateUtil.getSysDate());
		query.and("beginDate").greaterThan(DateUtil.getOffsetDaysTime(DateUtil.getSysDate(), -7));
		DBCursor cursor =collection.find(query.get()).sort(new BasicDBObject("beginDate",-1)).skip(historyReqVO.getStartRowIndex()).limit(historyReqVO.getEndRowIndex());
		try {
			
			while (cursor.hasNext()) {
				MessageHistoryRespVO resDTO = new MessageHistoryRespVO();
				DBObject obj = cursor.next();
				ConstantTool.dbObjectToBean(obj, resDTO);
				transMessage(resDTO);
				list.add(resDTO);
			}
		} finally {
			cursor.close();
		}
		return list;
	}
	
	
	/**
	 * 消息到达
	 * @param historyReqVO
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("updateMsgStatus")
	@ResponseBody
	public EcpBaseResponseVO updateMsgStatus(MessageHistoryReqVO historyReqVO)throws BusinessException{
		
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		try {
			BasicDBObject query = new BasicDBObject();
			query.put("id", historyReqVO.getId());
			DBObject stuFound = MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY").findOne(query);
			stuFound.put("status", "20");
			stuFound.put("arriveDate", DateUtil.getSysDate());
			WriteResult result = MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY").update(query, stuFound);
			/*if(result.getN()>0){
				//消息未到达时，如果session失效
				BasicDBObject basicDBObject = new BasicDBObject();
				Map map = stuFound.toMap();
				basicDBObject.put("id", map.get("sessionId"));
				DBObject dbObject = MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY").findOne(query);
				dbObject.put("status", ImConstants.STATE_1);
				WriteResult result2 = MongoUtil.getDBCollection("T_IM_MESSAGE_HISTORY").update(dbObject, stuFound);
				
			}*/
			vo.setResultFlag("ok");
		} catch (Exception e) {
			LogUtil.debug(MODULE, e.getMessage());
			throw new BusinessException("网络异常",e);
		}
	
		return vo;
	}
	
	/**
	 * 
	 * uploadImage:(上传图片). <br/>  
	 * 
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
        MultipartFile file = multipartRequest.getFile("upfile");
        
        
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
            String extensionName = "." + ConstantTool.getExtensionName(fileName);//获取文件拓展名

            /** 支持文件拓展名：.jpg,.png,.jpeg,.gif,.bmp */
            boolean flag = Pattern.compile("\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$|\\.(bmp)$")
                    .matcher(extensionName.toLowerCase()).find();
            if (!flag) {
                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                obj.put("message", "请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)!");
                LogUtil.error(MODULE, "上传图片失败,原因---请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)!");
                return obj.toJSONString();
            }
            
            byte[] datas = ConstantTool.inputStream2Bytes(file.getInputStream());//将InputStream转换成byte数组
//            if(datas.length>5*1024*1024){
//                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
//                obj.put("message", "上传的图片大于5M!");
//                LogUtil.error(MODULE, "图片上传失败，上传的图片必须小于5M!");
//                return obj.toJSONString();
//            }
            resultMap.put("imageName", fileName);
            fileName = Math.random()+"";
            String vfsId = ImageUtil.upLoadImage(datas, fileName);
            resultMap.put("vfsId", vfsId);
            resultMap.put("id", id);
            //resultMap.put("imagePath", ConstantTool.getImageUrl(vfsId,"150x150!"));
            resultMap.put("imagePath", ConstantTool.getImageUrl(vfsId,""));
            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            obj.put("message", "保存成功!");
            obj.put("map", resultMap);
        } catch (BusinessException e) {
			LogUtil.error(MODULE, "上传图片出错,原因---" + e.getMessage(), e);
			obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
        } catch (IOException e) {
			LogUtil.error(MODULE, "上传图片出错,原因---" + e.getMessage(), e);
			obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
        }
        return obj.toJSONString();
    }
    
    /**
	 * 
	 * uploadImage:(上传图片). <br/>  
	 * 
	 * @param model
	 * @param req
	 * @param rep
	 * @return 
	 * @since JDK 1.6
	 */
    @RequestMapping(value = "/uploadimg")
    @ResponseBody
    private Map uploadImg(Model model,HttpServletRequest req, HttpServletResponse rep) {
      String img = req.getParameter("img");
      String imgs[] = img.split(",");
      String suffix = imgs[0];
      String base64 = imgs[1];
      Map obj = new HashMap<>();
      Map<String, Object> resultMap = new HashMap<String, Object>();
      try {
    	  BASE64Decoder decoder = new BASE64Decoder();
    	  byte[] bytes = decoder.decodeBuffer(base64);
          for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {// 调整异常数据
              bytes[i] += 256;
            }
          }
          String fileName = Math.random()+"";
          resultMap.put("imageName", fileName);
          String vfsId = ImageUtil.upLoadImage(bytes, fileName);
          resultMap.put("vfsId", vfsId);
          resultMap.put("imagePath", ConstantTool.getImageUrl(vfsId,"250x250!"));
         // resultMap.put("imagePath", ConstantTool.getImageUrl(vfsId,""));
          obj.put("success", EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
          obj.put("message", "保存成功!");
          obj.put("map", resultMap);
      } catch (BusinessException e) {
			LogUtil.error(MODULE, "上传图片出错,原因---" + e.getMessage(), e);
			obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
      } catch (IOException e) {
			LogUtil.error(MODULE, "上传图片出错,原因---" + e.getMessage(), e);
			obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			obj.put("message", "保存失败，图片服务器异常，请联系管理员!");
      }
      return obj;
    }
}
