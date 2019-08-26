package com.ai.ecp.app.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Pmphstaff001Req;
import com.ai.ecp.app.resp.Pmphstaff001Resp;
import com.ai.ecp.app.util.SsoUtil;
import com.ai.ecp.base.util.AppUserCacheUtils;
import com.ai.ecp.server.front.dto.BaseStaff;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoMsgResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.ISsoUserImportRSV;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.exception.SystemException;

import net.sf.json.JSONObject;

/**
 * Project Name:ecp-web-mall <br>
 * Description: APP集成登录<br>
 * Date:2016-2-22下午6:51:19  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */

@Service("pmphstaff001")
@Action(bizcode="pmphstaff001", userCheck=false)
@Scope("prototype")
public class Pmphstaff001Action extends AppBaseAction<Pmphstaff001Req, Pmphstaff001Resp> {

    private static String MODULE = Pmphstaff001Action.class.getName();
    
    @Resource
    private ICustManageRSV custManageRSV;
    
	@Resource
	private ISsoUserImportRSV ssoUserImportRSV;
    
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
      
    	  /*1、获取入参*/
        String loginCode = this.request.getLoginCode();//登录编码（可以是登录名、手机号、邮箱）
        
        String password = this.request.getPassword();//密码
        
        /*2、调用人卫登录接口*/
        Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("method", "login");
		
		JSONObject value = new JSONObject();
		value.put("username", loginCode);
		value.put("password", password);
		map.put("params", value.toString());
		JSONObject jsonObject = SsoUtil.postAuthSso("/api/json", map);
		
		String flag = jsonObject.getString("_Status");
		if(!flag.equals("1")){
			this.response.setSuccess("fail");
			this.response.setMessage(jsonObject.getString("_Message"));
		}else{
			  /*3、调用业务接口，验证用户登录*/
			SsoUserInfoMsgResDTO message = new SsoUserInfoMsgResDTO();
			SsoUserInfoReqDTO dto2 = new SsoUserInfoReqDTO();
 			JSONObject user  =jsonObject.getJSONObject("Mapx");
 			dto2.setUserName(user.getString("UserName"));
 			if (StringUtil.isNotBlank(user.getString("UserName"))) {
 				dto2.setMobile(user.getString("Mobile"));
 			}
 		    message = ssoUserImportRSV.changeStaffInfo(dto2);
 		    
 		    if(StringUtil.isNotBlank(message.getStaffCode())){
 		    	CustInfoReqDTO dto = new CustInfoReqDTO();
 				dto.setStaffCode(message.getStaffCode());
 				CustInfoResDTO loginRes =  custManageRSV.findCustInfo(dto);
 		        /*4、返回结果*/
 		        ObjectCopyUtil.copyObjValue(loginRes, this.response, null, false);
 		       /*5、SS登录成功后，同步用户信息*/
 			      
 		        if (loginRes.getId() != null && loginRes.getId() != 0L) {
 		            BaseStaff basestaff = new BaseStaff();
 		            basestaff.setId(loginRes.getId());
 		            basestaff.setStaffCode(loginRes.getStaffCode());
 		            basestaff.setStaffLevelCode(loginRes.getCustLevelCode());
 		            basestaff.setStaffClass(loginRes.getCustType());
 		            String tocken = AppUserCacheUtils.saveUser2Cache(basestaff);
 		            this.response.setStaffId(loginRes.getId());
 		            this.response.setStaffCode(loginRes.getStaffCode());
 		            this.response.setTocken(tocken);
 		            this.response.setMobile(user.optString("Mobile"));
 		            this.response.setSuccess("ok");
 		            
 		        }
 		    }
	    
		}
    }

}

