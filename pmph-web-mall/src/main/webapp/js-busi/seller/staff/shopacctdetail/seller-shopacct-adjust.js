function addAdjustLoadParamsToUrl(_load_url_) {
	var shopId = $('select[name="adjShopId"]').val();
	if (shopId != "" && shopId != undefined) {
		_load_url_ += '&shopId=' + shopId;
	}
	var optType = $('select[name="adjOptType"]').val();
	if (optType != "" && optType != undefined) {
		_load_url_ += '&optType=' + optType;
	}
	var begDate = $('input[name="adjBegDate"]').val();
	if (begDate != "" && begDate != undefined) {
		_load_url_ += '&begDate=' + begDate;
	}
	var endDate = $('input[name="adjEndDate"]').val();
	if (endDate != "" && endDate != undefined) {
		_load_url_ += '&endDate=' + endDate;
	}
	var id = $('input[name="adjDetailId"]').val();
	if (id != "" && id != undefined) {
		_load_url_ += '&id=' + id;
	}
	var date = new Date();
    _load_url_ += '&t='+date.getTime();
	return _load_url_;
}
//页面初始化模块
$(function(){
    var adjustInit = function(){
    	var init = function(){
    		
    		/*var _load_url_ = '/seller/shopmgr/shopAcctDetail/adjustlist?';
    		
        	_load_url_ = addLoadParamsToUrl(_load_url_);
    	   
    	    //分页
	    	$('#pageControlbarAdjust').bPage({
	    		url : GLOBAL.WEBROOT + _load_url_,
	    	    asyncLoad : true,
	    	    asyncTarget : '#pageMainBoxAdjust',
	    	    params : {
	    	    	
	    	    }
	    	});*/
    		
    		function queryAdjust(){
    		/*	var shopId = $("#adjShopId").val();
	    		var optType = $('#adjOptType').val();
	    		var begDate = $('input[name="adjBegDate"]').val();
	    		var endDate = $('input[name="adjEndDate"]').val();
	    		var id = $('input[name="adjDetailId"]').val();
	    	    
	    		$('#pageMainBoxAdjust').load(GLOBAL.WEBROOT + '/seller/shopmgr/shopAcctDetail/adjustlist?v='+Math.random(), 
	    		{ 	
	    			"shopId":shopId,
	    			"optType":optType,
	    			"begDate":begDate,
	    			"endDate":endDate,
	    			"id":id,
	    		});*/
    			
    			
    			var _load_url_ = '/seller/shopmgr/shopAcctDetail/adjustlist?_t='+new Date().getTime();
            	_load_url_ = addAdjustLoadParamsToUrl(_load_url_);
//            	alert(_load_url_);
            	$(this).attr("class","active");
//                $('#log-contentDiv').load(GLOBAL.WEBROOT + _load_url_, ebcForm.formParams($("#searchForm")));
                $('#pageMainBoxAdjust').load(GLOBAL.WEBROOT + _load_url_);
    			
    		}
    		
    		queryAdjust();
	    	
	    	
	    	//重置
    		$('#btnFormResetAdjust').click(function(){
    			ebcForm.resetForm('#shopacctAdjust');
    			$("#adjBegDate").val($("#resetAdjBegDate").val());
				$("#adjEndDate").val($("#resetAdjEndDate").val());
    		});
    		
	    	//绑定查询按钮事件
	    	$('#btnFormSearchAdjust').unbind('click');
	    	$('#btnFormSearchAdjust').click(function(){
	    		/*var shopId = $("#adjShopId").val();
	    		var optType = $('#adjOptType').val();
	    		var begDate = $('input[name="adjBegDate"]').val();
	    		var endDate = $('input[name="adjEndDate"]').val();
	    		var id = $('input[name="adjDetailId"]').val();
	    	    
	    		$('#pageMainBoxAdjust').load(GLOBAL.WEBROOT + '/seller/shopmgr/shopAcctDetail/adjustlist?v='+Math.random(), 
	    		{ 	
	    			"shopId":shopId,
	    			"optType":optType,
	    			"begDate":begDate,
	    			"endDate":endDate,
	    			"id":id,
	    		});*/
	    		

	    		if(!$('#shopacctAdjust').valid())
    				return;
	    		queryAdjust();
	    	});	
	    	
	    	// 导出数据
    		$('#btnFormExportAdjust').click(function(){
    			if(!$("#shopacctAdjust").valid()) return false;
    			$('#shopacctAdjust').submit();
    			
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
			var scoreList = new adjustInit();
			scoreList.init();
		}
	});
});
