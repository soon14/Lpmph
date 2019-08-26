$(function(){
	$('#forgetPwd').on('click',function(){
		
		var serialNumber = $('#mobile').val();
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
		
		var phoneCode = $('#checkCode').val();
		if(phoneCode==''||phoneCode==null){
			eDialog.alert("验证码不能为空");
			return;
		}
		var StaffPassword = $('#newPwd').val();
		if(StaffPassword==""||StaffPassword==null){
			eDialog.alert("密码不能为空");
			return;
		} else {
			var strExp = /^[a-zA-Z0-9~!@#$%^&*()_-]{6,16}$/;
			var passFlag = strExp.test(StaffPassword);
			if (!passFlag) {
				eDialog.alert("密码由数字、字母或一些特殊字符组成，长度6~16位");
				return;
			}
		}
		if(StaffPassword!=$('#pwd2').val()){
			eDialog.alert("对不起，您两次输入的密码不一致");
			return;
		}
		var val = ebcForm.formParams($("#formForget"));
		$.eAjax({
			url : GLOBAL.WEBROOT+'/forget/resetpwd',
			datatype:'json',
			data : {newPwd:StaffPassword,mobile:serialNumber,checkCode:phoneCode},
			async : false,
			success : function(returnInfo) {
				if(returnInfo.resultFlag=='ok'){
					eDialog.alert(returnInfo.resultMsg,function(){
						window.location.href = GLOBAL.WEBROOT + '/login';
					});
				}else{
					eDialog.alert(returnInfo.resultMsg);
				}
			},
		});
		
	});
	
	
	$.smsDialogPlugin.show({
		PhoneNoId : "mobile",
		sendButtonId:"sms-win-getcode"
	});
	
	
});