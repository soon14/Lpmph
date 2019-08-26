/**
*手机短信验证码组件
*auth:wangbh
**/

(function($){
	
	var smsWin = {
			
			/**
			 * 短信发送；
			 * @param phoneNo  接收短信的手机号码
			 * @param busiType 业务类型
			 * @param callback 回调；function  ；
			 */
			"sendsms" : function(phoneNo,callback){
				if(!phoneNo || phoneNo =="" || phoneNo == "undefined"){
					eDialog.alert("接收短信手机号码不能为空，请重新输入");
					return ;
				}
				
				var mobileReg =/^1[34578]\d{9}$/; //简单的正则校验
			 	if(!(mobileReg.test(phoneNo))){ 
			 		eDialog.alert("手机号码格式不正确，请重新输入");
			 	    return ; 
			 	 } 

		    	$.eAjax({
	    			url : $webroot+'/sso/smscode',
	    			data : {'phone':phoneNo},
	    			datatype:'json',
	    			async : false,
	    			success : function(returnInfo) {
	    				if(returnInfo.resultFlag=='ok'){
	    					if($.isFunction(callback)){
								callback();
							}
	    				    }else{
	    				    	eDialog.alert(returnInfo.resultMsg);
	    				}
	    			}
	    		});
			},
			
			afterSendSuccess : function(){
				var sendButtonId = "sms-win-getcode";
				//发送后，60秒效果
			    $("#"+sendButtonId).attr("disabled",true);
			    //颜色变灰
			    $("#"+sendButtonId).css("background-color","#ecebe9");
		        var se = 60;
		        $("#"+sendButtonId).html("重新获取验证码("+se+")");
		        var timer = window.setInterval(function(){
		          se --;
		          $("#"+sendButtonId).html("重新获取验证码("+se+")");
		          if(se==0){
		            $("#"+sendButtonId).html("重新获取验证码");
		            $("#"+sendButtonId).attr("disabled",false);
		            $("#"+sendButtonId).css("background-color","#ff6b01");
		            clearInterval(timer);
		          }
		        },1000);
		        
			}
			
			
	}
	
	$.smsDialogPlugin = {
			
			show : function(options){
				 ///默认参数为 ，修改或绑定手机号码；
				var opts = $.extend({},{
					PhoneNoId : "",
					sendButtonId:"",
					closedable:false
				}, options);
				
				
				
				$('#'+opts.sendButtonId).on('click',function(){
					var PhoneNo = $('#'+opts.PhoneNoId).val();
					smsWin.sendsms(PhoneNo,smsWin.afterSendSuccess);
					
				});
				
			},
		
		    ///独立的发送手机验证码的申请请求；
		    /**
		    * @param phoneNo 接收验证码的手机号码；
		    * @param busiType 业务编码；
		    * @param callback function；手机验证码发送成功之后的回调处理；无入参;
		    */
			"sendSmsSecurity" : function(phoneNo,callback){
			    smsWin.sendsms(phoneNo, callback);
		    }
		
		    
		};
	
})(jQuery)