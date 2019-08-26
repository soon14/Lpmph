function addIncomeLoadParamsToUrl(_load_url_) {
	var shopId = $('select[name="inShopId"]').val();
	if (shopId != "" && shopId != undefined) {
		_load_url_ += '&shopId=' + shopId;
	}
	var orderId = $('input[name="inOrderId"]').val();
	if (orderId != "" && orderId != undefined) {
		_load_url_ += '&orderId=' + orderId;
	}
	var payTranNo = $('input[name="inPayTranNo"]').val();
	if (payTranNo != "" && payTranNo != undefined) {
		_load_url_ += '&payTranNo=' + payTranNo;
	}
	var id = $('input[name="incomeId"]').val();
	if (id != "" && id != undefined) {
		_load_url_ += '&id=' + id;
	}
	var payType = $('select[name="inPayType"]').val();
	if (payType != "" && payType != undefined) {
		_load_url_ += '&payType=' + payType;
	}
	var payWay = $('select[name="inPayWay"]').val();
	if (payWay != "" && payWay != undefined) {
		_load_url_ += '&payWay=' + payWay;
	}
	var orderStaffCode = $('input[name="orderStaffCode"]').val();
	if (orderStaffCode != "" && orderStaffCode != undefined) {
		_load_url_ += '&orderStaffCode=' + orderStaffCode;
	}
	var begDate = $('input[name="payBegDate"]').val();
	if (begDate != "" && begDate != undefined) {
		_load_url_ += '&begDate=' + begDate;
	}
	var endDate = $('input[name="payEndDate"]').val();
	if (endDate != "" && endDate != undefined) {
		_load_url_ += '&endDate=' + endDate;
	}
    var date = new Date();
    _load_url_ += '&t='+date.getTime();
	return _load_url_;
}
//页面初始化模块
$(function(){
    var incomeInit = function(){
    	var init = function(){
//    		alert("店铺ID："+$("#shopId").val());
    		/*var shopId = $("#shopId").val();
    		var orderId = $('#inOrderId').val();
    		var payTranNo = $('#inPayTranNo').val();
    		var begDate = $('input[name="payBegDate"]').val();
    		var endDate = $('input[name="payEndDate"]').val();
    		var orderStaffCode = $('input[name="orderStaffCode"]').val();
    		var id = $('input[name="incomeId"]').val();
    		var payType = $('#inPayType').val();
    		var payWay = $('#inPayWay').val();
    	    //分页
	    	$('#pageControlbarIncome').bPage({
	    	    url : GLOBAL.WEBROOT + '/seller/shopmgr/shopAcctDetail/incomelist',
	    	    asyncLoad : true,
	    	    asyncTarget : '#pageMainBoxIncome',
	    	    params : {
	    	    	shopId : $('#shopId').val(),
	    	    	orderId : $('#inOrderId').val(),
	    	    	payTranNo : $('#inPayTranNo').val(),
	    	    	begDate : $('input[name="payBegDate"]').val(),
	    	    	endDate : $('input[name="payEndDate"]').val(),
	    	    	orderStaffCode : $('input[name="orderStaffCode"]').val(),
	    	    	id : $('input[name="incomeId"]').val(),
	    	    	payType : $('#inPayType').val(),
	    	    	payWay : $('#inPayWay').val(),
	    	    }
	    	});*/
	    	
	    	function queryIncome(){
	    	/*	var shopId = $('#inShopId').val();
	    		var orderId = $('#inOrderId').val();
	    		var payTranNo = $('#inPayTranNo').val();
	    		var begDate = $('input[name="payBegDate"]').val();
	    		var endDate = $('input[name="payEndDate"]').val();
	    		var orderStaffCode = $('input[name="orderStaffCode"]').val();
	    		var id = $('input[name="incomeId"]').val();
	    		var payType = $('#inPayType').val();
	    		var payWay = $('#inPayWay').val();
	    	    
	    		$('#pageMainBoxIncome').load(GLOBAL.WEBROOT + '/seller/shopmgr/shopAcctDetail/incomelist?v='+Math.random(), 
	    		{ 	
	    			"shopId":shopId,
	    			"orderId":orderId,
	    			"payTranNo":payTranNo,
	    			"begDate":begDate,
	    			"endDate":endDate,
	    			"orderStaffCode":orderStaffCode,
	    			"id":id,
	    			"payType":payType,
	    			"payWay":payWay,
	    		});
	    		*/
	    		
	    		var _load_url_ = '/seller/shopmgr/shopAcctDetail/incomelist?_t='+new Date().getTime();
            	_load_url_ = addIncomeLoadParamsToUrl(_load_url_);
//            	alert(_load_url_);
            	$(this).attr("class","active");
//                $('#log-contentDiv').load(GLOBAL.WEBROOT + _load_url_, ebcForm.formParams($("#searchForm")));
                $('#pageMainBoxIncome').load(GLOBAL.WEBROOT + _load_url_);
    		
	    	}
	    	queryIncome();
	    	
	    	//重置
    		$('#btnFormResetIncome').click(function(){
    			ebcForm.resetForm('#shopacctIncome');
    			$("#payBegDate").val($("#resetPayBegDate").val());
				$("#payEndDate").val($("#resetPayEndDate").val());
    		});
    		
	    	//绑定查询按钮事件
	    	$('#btnFormSearchIncome').unbind('click');
	    	$('#btnFormSearchIncome').click(function(){
	    		/*var shopId = $('#inShopId').val();
	    		var orderId = $('#inOrderId').val();
	    		var payTranNo = $('#inPayTranNo').val();
	    		var begDate = $('input[name="payBegDate"]').val();
	    		var endDate = $('input[name="payEndDate"]').val();
	    		var orderStaffCode = $('input[name="orderStaffCode"]').val();
	    		var id = $('input[name="incomeId"]').val();
	    		var payType = $('#inPayType').val();
	    		var payWay = $('#inPayWay').val();
	    	    
	    		$('#pageMainBoxIncome').load(GLOBAL.WEBROOT + '/seller/shopmgr/shopAcctDetail/incomelist?v='+Math.random(), 
	    		{ 	
	    			"shopId":shopId,
	    			"orderId":orderId,
	    			"payTranNo":payTranNo,
	    			"begDate":begDate,
	    			"endDate":endDate,
	    			"orderStaffCode":orderStaffCode,
	    			"id":id,
	    			"payType":payType,
	    			"payWay":payWay,
	    		});*/

	    		if(!$('#shopacctIncome').valid())
    				return;
	    		queryIncome();
	    		
	    	});	
	    	
	    	// 导出数据
    		$('#btnFormExportIncome').click(function(){
    			if(!$("#shopacctIncome").valid()) return false;
    			$('#shopacctIncome').submit();
    		});
    		
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
			var scoreList = new incomeInit();
			scoreList.init();
		}
	});
});
