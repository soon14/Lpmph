$(function(){

	//处理弹出窗口登录超时问题
	//2016.06.08
	//曾海沥
	//if (window.self != window.top) window.top.location.replace(GLOBAL.WEBROOT + '/login');

	//处理嵌入式页面登录超时问题
	//2016.06.16
	//曾海沥
	//var body = $('div.loginMainBodyBox').closest('body');
	//if (!$(body).hasClass('loginMainBodyBox')) window.top.location.replace(GLOBAL.WEBROOT + '/login');


	$('#loginsubmit').click(function() {
		$("input[name='j_username']").blur();
		$("input[name='j_password']").blur();
	    var Referer = $('#Referer').val();

	    /*页面加上用户名，密码的非空校验  2016-5-3 by huangxl5*/
	    var userName = $("input[name='j_username']").val();
	    var password = $("input[name='j_password']").val();
	    if (userName.trim() == '') {
	        eDialog.alert("您好，请输入用户名。");
	        return;
	    }
	    if (password.trim() == '') {
	        eDialog.alert("您好，请输入密码。");
	        return;
	    }
	    var df = ebcForm.formParams($("#loginform"));
	    //先进行用户校验
	    $.eAjax({
	        url: GLOBAL.WEBROOT + '/sso/ssocheck',
	        data: ebcForm.formParams($("#loginform")),
	        datatype: 'json',
	        async: false,
	        success: function(returnInfo) {
	            if (returnInfo.resultFlag == 'ok') {
	
	          //  window.location.href=GLOBAL.WEBROOT+"/j_spring_security_check?j_username="+userName+"&j_password=0e848f708b1550083fe3074b358302d8";
            	//再进行框架上的权限操作
        	    $.eAjax({
        	        url: GLOBAL.WEBROOT + '/j_spring_security_check',
        	        data: {j_username:userName,j_from:'SSO'},
        	        datatype: 'json',
        	        success: function(returnInfo) {
        	            if (!Referer || Referer == "" || Referer == "undefined" || Referer == undefined) {
        	                window.location.href = GLOBAL.WEBROOT + '/homepage';
        	            } else {
        	                if (Referer.indexOf('http') > -1) {
        	                    window.location.href = Referer;
        	                } else {
        	                    window.location.href = GLOBAL.WEBROOT + Referer;
        	                }
        	            }
        	        },
        	        exception: function() {
        	            //eDialog.alert("登录异常");
        	        }
        	    });
	        	        
	            }else if(returnInfo.resultFlag == 'login'){
	    			      	    $.eAjax({
	    		        	        url: GLOBAL.WEBROOT + '/j_spring_security_check',
	    		        	        data: {j_username:userName,j_from:'SSO'},
	    		        	        datatype: 'json',
	    		        	        success: function(returnInfo) {
	    		        	            if (!Referer || Referer == "" || Referer == "undefined" || Referer == undefined) {
	    		        	                window.location.href = GLOBAL.WEBROOT + '/homepage';
	    		        	            } else {
	    		        	                if (Referer.indexOf('http') > -1) {
	    		        	                    window.location.href = Referer;
	    		        	                } else {
	    		        	                    window.location.href = GLOBAL.WEBROOT + Referer;
	    		        	                }
	    		        	            }
	    		        	        },
	    		        	        exception: function() {
	    		        	            //eDialog.alert("登录异常");
	    		        	        }
	    		        	    });
	    			
	            	}else {
	            	eDialog.alert(returnInfo.resultMsg);
	            }
	        },
	        exception: function() {
	            //eDialog.alert("登录异常");
	        }
	    });
	    
	    
	});

	$('#loginform').keydown(function(e) {
	    if (e.keyCode == 13) {
	        $('#loginsubmit').click();
	    }
	});

	//第三方登录： 微博
	$('#weibologin').click(function() {
		var url = GLOBAL.WEBROOT + '/sso/third/weibologin';
		SocialURL.getSocialURL(url);
	});
	//第三方登录： QQ
	$('#qqlogin').click(function() {
		var url = GLOBAL.WEBROOT + '/sso/third/qqlogin';
		SocialURL.getSocialURL(url);
	});

});
//获取当前地址参数的方法
var getUrlStr = {
		getUrlParamVal : function (){
			var q=location.search.substr(1);
			var qs=q.split('?');
			var paramVal='';
			if(qs){
				//取第一个数组'='符号后面的参数值
		        paramVal = qs[0].substring(qs[0].indexOf('=')+1,qs[0].length)
		    }
			return paramVal;
		}
};
//获取社会化登录链接
var SocialURL = {
		getSocialURL : function (url){
			var relreferer = '';
			var Referer = getUrlStr.getUrlParamVal();
			//当用户已经登录,点击退出时,当前页面url 的？号后不携带 	Referer ,而是openId
			if(Referer.indexOf('http') > -1){
				relreferer = Referer;
			}else{
				//没有Referer的时候指定一个默认地址
				relreferer ='http://'+document.domain+'/member/index';
			}
			console.log("Referer="+Referer);
			$.eAjax({
				url: url,
		        data: {"ClientId":'4c8a231d017f44f6b60c755e4fc9e00b',"ContentType":'json',"Referer":relreferer},
		        datatype: 'json',
		        async: false,
		        success: function(returnInfo) {
		        	 if (returnInfo.resultFlag == 'ok') {
		        		 window.location.href =  returnInfo.resultMsg;
		        	 }
		        },
		        exception: function() {
		            eDialog.alert("登录异常");
		        }
		    });
		}
}