$(function(){

	$('#loginsubmit').click(function(){
	/*	$.eAjax({
			url : GLOBAL.WEBROOT+'/login/check',
			data : ebcForm.formParams($("#loginform")),
			datatype:'json',
			success : function(returnInfo) {	
				window.location.href = GLOBAL.WEBROOT + '/homepage';
			},
			exception:function(){
//				ebcForm.resetForm('#loginform');
				}
			});*/
		var Referer = $('#Referer').val();
		$.eAjax({
			url : GLOBAL.WEBROOT+'/j_spring_security_check',
			data : ebcForm.formParams($("#loginform")),
			datatype:'json',
			success : function(returnInfo) {
				if(!Referer || Referer == ""||Referer =="undefined" ||Referer==undefined){
				window.location.href = GLOBAL.WEBROOT + '/homepage';
				}else{
					window.location.href = Referer;
				}
			},
			exception:function(){
				eDialog.alert("登录异常");
			}
			});
		});
	
	$('#loginform').keydown(function(e){
		if(e.keyCode==13){
		   $('#loginsubmit').click();
		}
		});

});