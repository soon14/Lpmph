package com.ai.ecp.common.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.util.ConstantTool;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.alibaba.fastjson.JSONObject;


/**
 * 
 * Project Name:ecp-web-im <br>
 * Description: <br>
 * Date:2016年8月4日上午11:30:47  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value = "/ueditfileupload")
public class UeditFileUploadController extends EcpBaseController {
	private static String MODULE = UeditFileUploadController.class.getName();
	
	@RequestMapping(value = "/ue")
	public String ue() throws Exception{
		return "/im/uedit/ue";
	}
	
	 private String readFile(String path) throws IOException
	 {
	    StringBuilder builder = new StringBuilder();
	    try
	    {
	      InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
	      BufferedReader bfReader = new BufferedReader(reader);
	      
	      String tmpContent = null;
	      while ((tmpContent = bfReader.readLine()) != null) {
	        builder.append(tmpContent);
	      }
	      bfReader.close();
	    }
	    catch (UnsupportedEncodingException localUnsupportedEncodingException) {
	    	LogUtil.error(MODULE, localUnsupportedEncodingException.getMessage(),localUnsupportedEncodingException);
	    }
	    return filter(builder.toString());
     }
			  
	  private String filter(String input)
	  {
	    return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
	  }

	@RequestMapping(value = "/upload")
	@ResponseBody
	public String imgload4(String action,HttpServletRequest request, HttpServletResponse response) throws Exception{
		if("uploadimage".equals(action)){
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
			MultipartFile uploadFileObj = mRequest.getFile("upfile");
			   JSONObject obj = new JSONObject();//返回结果
		        try {
		            if (uploadFileObj == null) {
		                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
		                obj.put("message", "请选择上传文件！");
		                LogUtil.info(MODULE, "请选择上传文件！");
		                return obj.toJSONString();
		            }
		            String vfsName = uploadFileObj.getOriginalFilename();
		            vfsName = vfsName.replace(" ", ""); 
		            String extensionName = "." + ConstantTool.getExtensionName(vfsName);
		          
		            /** 支持文件拓展名：.jpg,.png,.jpeg,.gif,.bmp */
		            boolean flag = Pattern.
		                    compile("\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$|\\.(bmp)$")
		                    .matcher(extensionName.toLowerCase()).find();
		            if (!flag) {
		                obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
		                obj.put("message", "请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)！");
		                LogUtil.error(MODULE, "上传图片失败,原因---请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp)!");
		                return obj.toJSONString();
		            }
		            
		            byte[] datas = ConstantTool.inputStream2Bytes(uploadFileObj.getInputStream());
		       
		            String vfsId ;
		            if(extensionName.equalsIgnoreCase(".png")){
		            	vfsId = ImageUtil.saveToRomte(datas, "image", "png");
		            }
		            else{
		            	vfsId = ImageUtil.upLoadImage(datas, "image");
		            }
		            obj.put("original",vfsName);
		            obj.put("name", vfsName);
	
		            obj.put("url", ConstantTool.getImageUrl(vfsId,""));
		            obj.put("state","SUCCESS");
		            obj.put("size", "99697");
		            obj.put("type", ".jpg");
		        } catch (Exception e) {
		            LogUtil.info(MODULE,"图片上传失败,原因---"+e.getMessage(), e);
		            obj.put("success", EcpBaseResponseVO.RESULT_FLAG_FAILURE);
		            obj.put("message", "图片上传失败，图片服务器异常，请联系管理员!");
		        }
		        return obj.toJSONString();
		}else{
			String filePath=request.getServletContext().getRealPath("/") +"js/UEdit/config.json";
			System.out.println("=============================="+filePath);
			String config=readFile(filePath);
			return config;
		}
	}
	
}
