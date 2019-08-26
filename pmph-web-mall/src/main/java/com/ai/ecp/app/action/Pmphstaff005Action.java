package com.ai.ecp.app.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Pmphstaff005Req;
import com.ai.ecp.app.resp.Pmphstaff005Resp;
import com.ai.ecp.app.util.SsoUtil;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.exception.SystemException;

import net.sf.json.JSONObject;

/**
 * 
 * Project Name:pmph-web-mall <br>
 * Description: sso 重置密码<br>
 * Date:2016年12月8日下午5:12:17  <br>
 * 
 * @version  
 * @since JDK 1.6
 */

@Service("pmphstaff005")
@Action(bizcode="pmphstaff005", userCheck=false)
@Scope("prototype")
public class Pmphstaff005Action extends AppBaseAction<Pmphstaff005Req, Pmphstaff005Resp> {

    
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
    	Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		
		map.put("method", "ZAS.ResetPwd");
		
		JSONObject value = new JSONObject();
		value.put("VerifyMode", "Code");//验证类型
		value.put("VerifyValue", this.request.getCheckCode());//验证码
		value.put("AddressType", "Mobile");//找回密码的类型
		value.put("Address", this.request.getMobile());//手机号码 
		value.put("NewPwd", this.request.getNewPwd());//新密码
		map.put("params", value.toString());
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
		boolean flag = jsonObject.getBoolean("Success");
		if(flag){
		    this.response.setSuccess("ok");
		
		}else{
			this.response.setSuccess("fail");
			this.response.setMessage(jsonObject.getString("Message"));
		}
    	
    }

}

