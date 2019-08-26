package com.ai.ecp.app.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Pmphstaff003Req;
import com.ai.ecp.app.resp.Pmphstaff003Resp;
import com.ai.ecp.app.util.SsoUtil;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.exception.SystemException;

import net.sf.json.JSONObject;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 用户注册-短信验证<br>
 * Date:2016-2-22下午6:51:19  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */

@Service("pmphstaff003")
@Action(bizcode="pmphstaff003", userCheck=false)
@Scope("prototype")
public class Pmphstaff003Action extends AppBaseAction<Pmphstaff003Req, Pmphstaff003Resp> {

    private static String MODULE = Pmphstaff003Action.class.getName();
    
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
    	Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		
		map.put("method", "ZAS.SMSSend");
		
		JSONObject value = new JSONObject();
		value.put("Mobile", this.request.getMobile());
		value.put("CheckUsed", "N");
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

