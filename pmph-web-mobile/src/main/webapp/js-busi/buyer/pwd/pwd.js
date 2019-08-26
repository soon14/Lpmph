
$(function(){
	window.errorInfo = "";
	$.validator.addMethod("defineRequired",function(value, element){
		errorInfo = $(element).attr('requiredInfo');
		return value!=""||$.trim(value)!="";
	},function(value, element){
		return $(element).attr('requiredInfo');
	});
	$('#savePwd').unbind('click');
	//保存密码修改
	$('#savePwd').click(function(){
		if(!$("#modifyPwdForm").valid()) return false;
		var val = ebcForm.formParams($("#modifyPwdForm"));
		$.eAjax({
			url : GLOBAL.WEBROOT + "/sso/savepwd",
			data : val,
			datatype:'json',
			method:'post',
			success : function(returnInfo) {
				if(returnInfo.resultFlag=="ok"){
					new AmLoad({content:'修改成功！'});
					window.location.href = GLOBAL.WEBROOT+'/infomation/index';
				}else{
					new AmLoad({content:returnInfo.resultMsg,type:'2'});
				}
				
			}
		});
	});
	$('#cancle').click(function(){
		history.back();
	});
});	
