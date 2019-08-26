
//页面初始化模块
$(function(){
	$.cookie('SUPPORT-WEB-COOKIE-MOUDEL', "member", {path : '/'});
    //跳转个人信息
	$('#infomation,#imgHeader').on('click',function(){
		window.location.href=GLOBAL.WEBROOT+"/infomation/index"; 
	});
	
	$('#message').on('click',function(){
		window.location.href=GLOBAL.WEBROOT+"/msg/index"; 
	});
	
	$('#score').click(function(){
		window.location.href=GLOBAL.WEBROOT+"/score/index";
	});
	
	$('#mall-point').click(function(){
		//如果当前站点是商城，则跳到积分商城
		if ($("#currSiteId").val() == "1") {
			window.location.href = $("#mallPointSiteUrl").val() + "/homepage";
		} else {
			window.location.href = $("#mallSiteUrl").val() + "/homepage";
		}
		
	});
	
	$('#favgoods').on('click',function(){
		window.location.href=GLOBAL.WEBROOT+"/favgoods";
	});
	
	$('#membercoup').on('click',function(){
		window.location.href=GLOBAL.WEBROOT+"/membercoup/index";
	});
	
	$('#pointOrder').on('click',function(){
		window.location.href=$("#mallPointSiteUrl").val()+"/point/order/my";
	});
	
	$('#orderMy').on('click',function(){
		window.location.href=GLOBAL.WEBROOT+"/order/member";
	});
	
	$('#wallet').on('click',function(){
		window.location.href=GLOBAL.WEBROOT+"/wallet";
	});
});