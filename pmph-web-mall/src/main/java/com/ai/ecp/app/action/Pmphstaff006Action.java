package com.ai.ecp.app.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Pmphstaff006Req;
import com.ai.ecp.app.resp.Pmphstaff006Resp;
import com.ai.ecp.app.util.SsoUtil;
import com.ai.ecp.base.util.AppUserCacheUtils;
import com.ai.ecp.server.front.dto.BaseStaff;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.exception.SystemException;

import net.sf.json.JSONObject;

/**
 * 
 * Project Name:pmph-web-mall <br>
 * Description: 第三方登录：通过第三方帐号查询或生成平台的用户以及绑定商城用户<br>
 * a、如果第三方帐号有绑定商城用户，则返回商城用户
 * b、如果第三方帐号未绑定商城用户，但创建了用户，则返回创建的用户
 * c、如果第三方帐号未创建用户，则新建一个用户
 * Date:2017年2月16日下午2:19:45  <br>
 * 
 * @version  
 * @since JDK 1.6
 */

@Service("pmphstaff006")
@Action(bizcode="pmphstaff006", userCheck=false)
@Scope("prototype")
public class Pmphstaff006Action extends AppBaseAction<Pmphstaff006Req, Pmphstaff006Resp> {

	 @Resource
	 private ICustManageRSV custManageRSV;
    
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
    	Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		map.put("method", "ZASSocial.SocialBind");
		JSONObject value = new JSONObject();
		value.put("PlatCode", this.request.getPlatCode());//平台编码类型
		value.put("PlatUID", this.request.getPlatUID());//平台唯一ID
		value.put("NoBindTactics", this.request.getNoUserAction());//操作策略
		//如果是绑定已有商城用户，则需要提供用户密码
		if ("BindExistUser".equals(this.request.getNoUserAction())) {
			JSONObject bindUserInfo = new JSONObject();
			bindUserInfo.put("LoginID", this.request.getLoginId());
			bindUserInfo.put("Password", this.request.getPassword());
			value.put("BindUserInfo", bindUserInfo);
		}
		//有这个id时，需要传入
		if (StringUtil.isNotBlank(this.request.getPublicPlatUID())) {
			value.put("PublicPlatUID", this.request.getPublicPlatUID());//公用平台用户唯一ID
		}
		map.put("params", value.toString());
		
		/*调用sso接口*/
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
		
		boolean flag = jsonObject.getBoolean("Success");
		if(flag){
		    this.response.setSuccess("ok");
		    /*调用成功，查询在我们系统中，是否存在该用户*/
		    JSONObject userInfo = jsonObject.getJSONObject("UserData");
		    String staffCode = userInfo.getString("UserName");//用户名，即staffCode
		    CustInfoReqDTO custInfoReq = new CustInfoReqDTO();
		    custInfoReq.setStaffCode(staffCode);
		    CustInfoResDTO res = custManageRSV.findCustInfo(custInfoReq);
		    //如果系统中已经存在该用户，则返回用户信息并做登录处理
		    if (res != null && res.getId() != null && res.getId() != 0L) {
		    	//返回用户信息
		    	ObjectCopyUtil.copyObjValue(res, this.response, null, false);
		    	//登录
		    	BaseStaff basestaff = new BaseStaff();
	            basestaff.setId(res.getId());
	            basestaff.setStaffCode(res.getStaffCode());
	            basestaff.setStaffLevelCode(res.getCustLevelCode());
	            basestaff.setStaffClass(res.getCustType());
	            String tocken = AppUserCacheUtils.saveUser2Cache(basestaff);
	            this.response.setTocken(tocken);
		    	//如果系统不存在该用户，则把用户信息保存
		    } else {
		    	custInfoReq.setCustType(StaffConstants.custInfo.CUST_TYPE_P);
		    	custInfoReq.setStaffPassword("123456");
		    	if (StringUtil.isNotBlank(userInfo.getString("NickName")) && !"null".equals(userInfo.getString("NickName"))) {
		    		custInfoReq.setNickname(userInfo.getString("NickName"));
		    	}
		    	if (StringUtil.isNotBlank(userInfo.getString("RealName")) && !"null".equals(userInfo.getString("RealName"))) {
		    		custInfoReq.setCustName(userInfo.getString("RealName"));
		    	}
		    	if (StringUtil.isNotBlank(userInfo.getString("Mobile")) && !"null".equals(userInfo.getString("Mobile"))) {
		    		custInfoReq.setSerialNumber(userInfo.getString("Mobile"));
		    	}
		    	if (StringUtil.isNotBlank(userInfo.getString("Email")) && !"null".equals(userInfo.getString("Email"))) {
		    		custInfoReq.setEmail(userInfo.getString("Email"));
		    	}
		    	if (StringUtil.isNotBlank(userInfo.getString("Phone")) && !"null".equals(userInfo.getString("Phone"))) {
		    		custInfoReq.setTelephone(userInfo.getString("Phone"));
		    	}
		    	if (StringUtil.isNotBlank(userInfo.getString("Gender")) && !"null".equals(userInfo.getString("Gender"))) {
		    		custInfoReq.setGender(userInfo.getString("Gender"));
		    	}
		    	if (StringUtil.isNotBlank(userInfo.getString("Address")) && !"null".equals(userInfo.getString("Address"))) {
		    		custInfoReq.setDatailedAddress(userInfo.getString("Address"));
		    	}
		    	
		    	//微信对应sysType:10
		    	if ("Wechat".equals(this.request.getPlatCode())) {
		    		custInfoReq.setSysType("10");
		    		//QQ对应的sysType:11
		    	} else if ("QQ".equals(this.request.getPlatCode())) {
		    		custInfoReq.setSysType("11");
		    		//新浪微博对应的sysType:12
		    	} else {
		    		custInfoReq.setSysType("12");
		    	}
		    	long staffId = custManageRSV.saveCustInfo(custInfoReq);
		    	CustInfoResDTO saveCust = custManageRSV.findCustInfoById(staffId);
		    	//登录
		    	BaseStaff basestaff = new BaseStaff();
	            basestaff.setId(staffId);
	            basestaff.setStaffCode(saveCust.getStaffCode());
	            basestaff.setStaffLevelCode(saveCust.getCustLevelCode());
	            basestaff.setStaffClass(saveCust.getCustType());
	            String tocken = AppUserCacheUtils.saveUser2Cache(basestaff);
	            this.response.setTocken(tocken);
	            ObjectCopyUtil.copyObjValue(saveCust, this.response, null, false);
		    }
		}else{
			this.response.setSuccess("fail");
			this.response.setMessage(jsonObject.getString("Message"));
		}
    	
    }

}

