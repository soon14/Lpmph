function addLoadParamsToUrl(_load_url_){
	var begDate = $('input[name="begDate"]').val();
    if(begDate != "" && begDate != undefined)
    {
    	_load_url_ += '&begDate=' + begDate;	
    }
    var endDate = $('input[name="endDate"]').val();
    if(endDate != "" && endDate != undefined)
    {
    	_load_url_ += '&endDate=' + endDate;	
    }
    var shopId = $('select[name="shopId"]').val();
    if(shopId != "" && shopId != undefined)
    {
    	_load_url_ += '&shopId=' + shopId;
    }
    var optType  = $('select[name="optType"]').val();
    if(optType != "" && optType != undefined)
    {
    	_load_url_ += '&optType=' + optType;	
    }   
    var payWay = $('select[name="payWay"]').val();
    if(payWay != "" && payWay != undefined)
    {
    	_load_url_ += '&payWay=' + payWay;
    }
    var orderId = $('input[name="orderId"]').val();
    if(orderId != "" && orderId != undefined)
    {
    	_load_url_ += '&orderId=' + orderId;
    }
    var payTranNo = $('input[name="payTranNo"]').val();
    if(payTranNo != "" && payTranNo != undefined)
    {
    	_load_url_ += '&payTranNo=' + payTranNo;
    }
    var date = new Date();
    _load_url_ += '&t='+date.getTime();
    return _load_url_;
}
//页面初始化模块
$(function(){
	
	//页面业务逻辑处理内容
	var pInit = function(){

    	var init = function(){
    		
    		function queryLogs(){
    			var _load_url_ = '/seller/shopmgr/shopAcctOptLog/optLoglist?_t='+new Date().getTime();
            	_load_url_ = addLoadParamsToUrl(_load_url_);
//            	alert(_load_url_);
            	$(this).attr("class","active");
//                $('#log-contentDiv').load(GLOBAL.WEBROOT + _load_url_, ebcForm.formParams($("#searchForm")));
                $('#log-contentDiv').load(GLOBAL.WEBROOT + _load_url_);
    		}
    		
    		queryLogs();
    		//查询
    		$('#btnFormSearch').click(function(){
    			if(!$('#searchForm').valid())
    				return;
    			queryLogs();
    		});	
    		//重置
    		$('#btnFormReset').click(function(){
    			ebcForm.resetForm('#searchForm');
    			$("#begDate").val($("#resetBegDate").val());
				$("#endDate").val($("#resetEndDate").val());
    		});
    		// 导出数据
    		$('#btnFormExport').click(function(){
    			if(!$("#searchForm").valid()) return false;
    			$('#searchForm').submit();
    			
    		});
    	};
    	return {
    		init : init
    	};
	};
	pageConfig.config({
		//指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : ['bForm','bPage'],
		//指定页面
		init : function(){
			var p = new pInit();
			p.init();
			
		}
	});
	
});


