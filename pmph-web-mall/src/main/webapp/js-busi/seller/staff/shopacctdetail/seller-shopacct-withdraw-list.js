/*function addLoadParamsToUrl(_load_url_) {
	var shopId = $('select[name="withdrawShopId"]').val();
	if (shopId != "" && shopId != undefined) {
		_load_url_ += '&shopId=' + shopId;
	}
	var begDate = $('input[name="withdrawBegDate"]').val();
	if (begDate != "" && begDate != undefined) {
		_load_url_ += '&begDate=' + begDate;
	}
	var endDate = $('input[name="withdrawEndDate"]').val();
	if (endDate != "" && endDate != undefined) {
		_load_url_ += '&endDate=' + endDate;
	}
	var id = $('input[name="withdrawId"]').val();
	if (id != "" && id != undefined) {
		_load_url_ += '&id=' + id;
	}
	var date = new Date();
    _load_url_ += '&t='+date.getTime();
	return _load_url_;
}*/
//页面初始化模块
$(function(){
    var withdrawInit = function(){
    	var init = function(){
    		var _load_url_ = '/seller/shopmgr/shopAcctDetail/withdrawlist?';
    		_load_url_= addWithdrawLoadParamsToUrl(_load_url_);
    	    //分页
	    	$('#pageControlbarWithdraw').bPage({
	    		url : GLOBAL.WEBROOT + _load_url_,
	    	    asyncLoad : true,
	    	    asyncTarget : '#pageMainBoxWithdraw',
	    	    params : {
	    	    	
	    	    }
	    	});
	    	
	    	/*//重置
    		$('#btnFormResetWithdraw').click(function(){
    			ebcForm.resetForm('#shopacctWithdraw');
    			$("#withdrawBegDate").val($("#resetWithdrawBegDate").val());
				$("#withdrawEndDate").val($("#resetWithdrawEndDate").val());
    		});
    		
	    	//绑定查询按钮事件
	    	$('#btnFormSearchWithdraw').unbind('click');
	    	$('#btnFormSearchWithdraw').click(function(){
	    		var shopId = $("#withdrawShopId").val();
	    		var begDate = $('input[name="withdrawBegDate"]').val();
	    		var endDate = $('input[name="withdrawEndDate"]').val();
	    		var id = $('input[name="withdrawId"]').val();
	    	    
	    		$('#pageMainBoxWithdraw').load(GLOBAL.WEBROOT + '/seller/shopmgr/shopAcctDetail/withdrawlist?v='+Math.random(), 
	    		{ 	
	    			"shopId":shopId,
	    			"begDate":begDate,
	    			"endDate":endDate,
	    			"id":id,
	    		});
	    	});	
	    	
	    	// 导出数据
    		$('#btnFormExportWithdraw').click(function(){
    			if(!$("#shopacctWithdraw").valid()) return false;
    			$('#shopacctWithdraw').submit();
    			
    		});*/
	    	
	    	/*//查看详情
			$('.seeWithdrawApply').click(function(){
	        	var tr = $(this).closest('tr');
	        	var applyId = $(".applyId",$(tr)).text();
	        	var url = GLOBAL.WEBROOT+"/seller/shopmgr/shopAcctDetail/withdrawApplyDetail/"+ applyId;
	        	window.open(url);
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
			var scoreList = new withdrawInit();
			scoreList.init();
		}
	});
});
