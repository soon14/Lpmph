
//页面初始化模块
$(function(){
    var incomeInit = function(){
    	var init = function(){
    		var _load_url_ = '/seller/shopmgr/shopAcctDetail/incomelist?';
    		_load_url_= addIncomeLoadParamsToUrl(_load_url_);
    	    //分页
	    	$('#pageControlbarIncome').bPage({
	    	    url : GLOBAL.WEBROOT + _load_url_,
	    	    asyncLoad : true,
	    	    asyncTarget : '#pageMainBoxIncome',
	    	    params : {
	    	    	
	    	    }
	    	});

	    	/*//重置
    		$('#btnFormResetIncome').click(function(){
    			ebcForm.resetForm('#shopacctIncome');
    			$("#payBegDate").val($("#resetPayBegDate").val());
				$("#payEndDate").val($("#resetPayEndDate").val());
    		});
    		
	    	//绑定查询按钮事件
	    	$('#btnFormSearchIncome').unbind('click');
	    	$('#btnFormSearchIncome').click(function(){
	    		var shopId = $('#inShopId').val();
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
	    		
	    		
	    	});	
	    	
	    	// 导出数据
    		$('#btnFormExportIncome').click(function(){
    			if(!$("#shopacctIncome").valid()) return false;
    			$('#shopacctIncome').submit();
    		});
	    	*/
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


