$(function(){
	
	/*$("#refleshCaptchaCode").click(function(){
		$("#captchaCodeImg").attr("src","captcha/CapthcaImage?"+(new Date()).getTime());
	});*/
	
	$('#regist').on('click',function(){
/*		var staff_code = $('#staffCode').val();
		if(staff_code==""||staff_code==null){
			eDialog.alert("登录账号不能为空");
			return;
		} else {
			var strExp = /^[a-zA-Z0-9~!@#$%^&*()_-]{6,15}$/;
			var codeFlag = strExp.test(staff_code);
			if (!codeFlag) {
				eDialog.alert("账号由数字、字母或一些特殊字符组成，长度6~15位");
				return;
			}
		}*/
		var StaffPassword = $('#StaffPassword').val();
		if(StaffPassword==""||StaffPassword==null){
			eDialog.alert("登录密码不能为空");
			return;
		} else {
			var strExp = /^[a-zA-Z0-9~!@#$%^&*()_-]{6,16}$/;
			var passFlag = strExp.test(StaffPassword);
			if (!passFlag) {
				eDialog.alert("密码由数字、字母或一些特殊字符组成，长度6~16位");
				return;
			}
		}
		if(StaffPassword!=$('#StaffPassword2').val()){
			eDialog.alert("对不起，您输入的两次密码不正确");
			return;
		}
		var serialNumber = $('#serialNumber').val();
		if(serialNumber==''||serialNumber==null){
			eDialog.alert("手机号码不能为空");
			return;
		} else {
			var mobileReg = /^(13|15|18|17|14)[0-9]{9}$/;
			var mobileFlag = mobileReg.test(serialNumber);
			if(!mobileFlag){
				eDialog.alert("请输入正确的手机号码");
				return;
			}
		}
		
			var phoneCode = $('#phoneCode').val();
			if(phoneCode==''||phoneCode==null){
				eDialog.alert("手机验证码不能为空");
				return;
			}
		
		var val = ebcForm.formParams($("#formRegist"));
		$.eAjax({
			url : GLOBAL.WEBROOT+'/sso/regist',
			datatype:'json',
			data : {StaffPassword:StaffPassword,serialNumber:serialNumber,phoneCode:phoneCode},
			async : false,
			success : function(returnInfo) {
				if(returnInfo.resultFlag=='ok'){
					$('#j_username').val($('#staffCode').val());
				//	$('#j_password').val($('#StaffPassword').val());
					eDialog.alert(returnInfo.resultMsg,function(){
						$.eAjax({
			    			url : GLOBAL.WEBROOT+'/j_spring_security_check',
			    			data : {j_username:serialNumber,j_from:'SSO'},
			    			datatype:'json',
			    			success : function(returnInfo) {
			        	                window.location.href = GLOBAL.WEBROOT + '/homepage';
			    			},
			    			exception:function(){
			    				eDialog.alert("登录异常");
			    			}
			    			});
						
					});
				    }else{
				    	eDialog.alert(returnInfo.resultMsg);
				}
			},
		});
		
/*		$.eAjax({
			url : GLOBAL.WEBROOT+'/sso/regist',
			data : val,
			datatype:'json',
			async : false,
			success : function(returnInfo) {
			
			},
			exception:function(){
				
		      }
		});*/
		
	});
	//校验用户名是否存在
	$('#staffCode').blur(function(){
		var staffCode = $('#staffCode').val();
		var strExp=/^[a-zA-Z0-9~!@#$%^&*()_-]{4,18}$/;
		//这个校验，必须与统一的校验脚本一致ecp-web-js包的e.validate.method.js
		if (strExp.test(staffCode)) {
			$.eAjax({
    			url : GLOBAL.WEBROOT+'/register/staffcode/check?staffCode=' + staffCode,
    			datatype:'json',
    			success : function(returnInfo) {
    				if (returnInfo.resultFlag != 'ok') {
    					eDialog.alert("用户名已经存在");
    					/*$('#isExist').html("<font color='red'>用户名已经存在</font>");*/
    				} else {
    					//$('#isExist').html("该用户名可以使用");
    				}
    			},
    		});
		}
	});
	
	$.smsDialogPlugin.show({
		PhoneNoId : "serialNumber",
		sendButtonId:"sms-win-getcode"
	});
	
	
});