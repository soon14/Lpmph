$(function(){
	$.eAjax({
		url : GLOBAL.WEBROOT+"/puborder/build/create-success-ajax/"+$("#rOrdZDResponseRedisKey").val()+"/"+$("#ordMainMapsFailedRedisKey").val(),
		async : false,
		type : "post",
		dataType : "json",
		success : function(data) {
			//window.location.href =GLOBAL.WEBROOT+"/puborder/build/create-success/"+data.mapforRedisKeys.rOrdZDResponseRedisKey+"/"+data.mapforRedisKeys.ordMainMapsFailedRedisKey;
			//window.location.href = GLOBAL.WEBROOT+"/puborder/build/create-success2/"+data.mapforRedisKeys.rOrdZDResponseRedisKey+"/"+data.mapforRedisKeys.ordMainMapsFailedRedisKey;
			
			//img
			if(data.sucCount!=0){
				$(".success-img").append('<span class="success-icon">&nbsp;</span>');
			}else{
				$(".success-img").append('<span class="fail-icon">&nbsp;</span>');
			}
			
			//head
			if(data.sucCount==0){
				$(".success-msg-head").append('<p>您的订单提交失败!</p>');
			}else if(data.failCount==0){
				$(".success-msg-head").append('<p>您的订单提交成功！</p>');
			}else{
				$(".success-msg-head").append('<p>您的订单提交部分成功</p><span style="color:black;">其中成功的共有'+data.sucCount+'笔,失败的共有'+data.failCount+'笔,失败的订单电话号为['+data.ordMainIdsFailed+']</span>');
			}
			
			var userType = '';
			if(data.pubUserNAcctInfoRespDTO){
				if(data.pubUserNAcctInfoRespDTO.userType==0){
					userType = '月结用户';
				}else if(data.pubUserNAcctInfoRespDTO.userType==1){
					userType = '预付款用户';
				}else{
					userType = '现款用户';
				}
			}
			
			//body
			if(data.sucCount!=0 && data.flag){
				$(".success-msg-body").append('<div class="msg-txt-tab">您的征订单会尽快发货，如果您想了解征订单发货状态，请进入<a id="myZdOrd" href="#" onclick="jumpToMyZDOrder();" >我的征订单</a>进行查询！</div>');
			}else if(data.pubUserNAcctInfoRespDTO.userType==0){
				$(".success-msg-body").append('<p>备注：</p><div class="msg-txt-detail">由于您是<span class="msg-txt-detail-type">'+userType+'</span>，且在31天内未进行月结付款，目前您的账户余额为<span class="msg-txt-detail-money">'+ebcUtils.numFormat(accDiv(data.pubUserNAcctInfoRespDTO.acctMoney,100),2)+'元</span>，余额不足，至少需要 <span class="msg-txt-detail-type">付款</span> <span class="msg-txt-detail-money">'+ebcUtils.numFormat(accDiv(data.zDRealMoney,100),2)+'元</span> ，请您联系客服（4008989100)进行付款操作，客服会确认过您充值之后，才会对征订单进行发货，谢谢！</div>');
			}else{
				$(".success-msg-body").append('<p>备注：</p><div class="msg-txt-detail">由于您是<span class="msg-txt-detail-type">'+userType+'</span>，目前您的账户余额为<span class="msg-txt-detail-money">'+ebcUtils.numFormat(accDiv(data.pubUserNAcctInfoRespDTO.acctMoney,100),2)+'元</span>，余额不足，至少需要 <span class="msg-txt-detail-type">付款</span> <span class="msg-txt-detail-money">'+ebcUtils.numFormat(accDiv(data.zDRealMoney,100),2)+'元</span> ，请您联系客服（4008989100)进行付款操作，客服会确认过您充值之后，才会对征订单进行发货，谢谢！</div>');
			}
			
		}
	});
});

function jumpToMyZDOrder(){
	window.location.href = GLOBAL.WEBROOT + "/pubord/all";
}