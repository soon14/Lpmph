var username=$("#username").val();
var servChatUrl = $("#servChatUrl").val();
if(username!="0"){	
	toChat1(username,servChatUrl);
}else{
	eDialog.alert("该用户不是客服，不能访问客服系统",function(){		
			window.location.href = GLOBAL.WEBROOT+"/seller/shopdashboard/index";
		}
	);
}

function toChat1(username,servChatUrl){
	window.location.href = servChatUrl + "/j_spring_security_check?j_username=" + username
                + "&j_from=SSO";
}