/*function addLoadParamsToUrl(_load_url_) {
	var shopId = $('select[name="backShopId"]').val();
	if (shopId != "" && shopId != undefined) {
		_load_url_ += '&shopId=' + shopId;
	}
	var orderId = $('input[name="backOrderId"]').val();
	if (orderId != "" && orderId != undefined) {
		_load_url_ += '&orderId=' + orderId;
	}
	var payTranNo = $('input[name="backPayTranNo"]').val();
	if (payTranNo != "" && payTranNo != undefined) {
		_load_url_ += '&payTranNo=' + payTranNo;
	}
	var begDate = $('input[name="backBegDate"]').val();
	if (begDate != "" && begDate != undefined) {
		_load_url_ += '&begDate=' + begDate;
	}
	var endDate = $('input[name="backEndDate"]').val();
	if (endDate != "" && endDate != undefined) {
		_load_url_ += '&endDate=' + endDate;
	}
	var applyStaffCode = $('input[name="applyStaffCode"]').val();
	if (applyStaffCode != "" && applyStaffCode != undefined) {
		_load_url_ += '&applyStaffCode=' + applyStaffCode;
	}
	var id = $('input[name="backDetailId"]').val();
	if (id != "" && id != undefined) {
		_load_url_ += '&id=' + id;
	}
	var payType = $('select[name="backType"]').val();
	if (payType != "" && payType != undefined) {
		_load_url_ += '&payType=' + payType;
	}
	var payWay = $('select[name="backWay"]').val();
	if (payWay != "" && payWay != undefined) {
		_load_url_ += '&payWay=' + payWay;
	}
	var optType = $('select[name="backOptType"]').val();
	if (optType != "" && optType != undefined) {
		_load_url_ += '&optType=' + optType;
	}
	var date = new Date();
    _load_url_ += '&t='+date.getTime();
	return _load_url_;
}*/
//页面初始化模块
$(function(){
    var expendInit = function(){
    	var init = function(){
    		var _load_url_ = '/seller/shopmgr/shopAcctDetail/expendlist?';
    		_load_url_= addExpendLoadParamsToUrl(_load_url_);
    	   
    	    //分页
	    	$('#pageControlbarExpend').bPage({
	    		url : GLOBAL.WEBROOT + _load_url_,
	    	    asyncLoad : true,
	    	    asyncTarget : '#pageMainBoxExpend',
	    	    params : {
	    	    	
	    	    }
	    	});
	    	
	    	/*//重置
    		$('#btnFormResetExpend').click(function(){
    			ebcForm.resetForm('#shopacctExpend');
    			$("#backBegDate").val($("#resetBackBegDate").val());
				$("#backEndDate").val($("#resetBackEndDate").val());
    		});
    		
	    	//绑定查询按钮事件
	    	$('#btnFormSearchExpend').unbind('click');
	    	$('#btnFormSearchExpend').click(function(){
	    		var shopId = $('#backShopId').val();
	    		var orderId = $('#backOrderId').val();
	    		var payTranNo = $('#backPayTranNo').val();
	    		var begDate = $('input[name="backBegDate"]').val();
	    		var endDate = $('input[name="backEndDate"]').val();
	    		var applyStaffCode = $('input[name="applyStaffCode"]').val();
	    		var id = $('input[name="backDetailId"]').val();
	    		var payType = $('#backType').val();
	    		var payWay = $('#backWay').val();
	    		var optType = $('#backOptType').val();
	    	    
	    		$('#pageMainBoxExpend').load(GLOBAL.WEBROOT + '/seller/shopmgr/shopAcctDetail/expendlist?v='+Math.random(), 
	    		{ 	
	    			"shopId":shopId,
	    			"orderId":orderId,
	    			"payTranNo":payTranNo,
	    			"begDate":begDate,
	    			"endDate":endDate,
	    			"applyStaffCode":applyStaffCode,
	    			"id":id,
	    			"payType":payType,
	    			"payWay":payWay,
	    			"optType":optType,
	    		});
	    	});	
	    	
	    	// 导出数据
    		$('#btnFormExportExpend').click(function(){
    			if(!$("#shopacctExpend").valid()) return false;
    			$('#shopacctExpend').submit();
    			
    		});*/
	    	
		};
    	return {
    		init : init
    	};
	};    	
	pageConfig.config({
		//指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : ['bPage','bForm'],
		//指定页面
		init : function(){
			var scoreList = new expendInit();
			scoreList.init();
		}
	});
});
