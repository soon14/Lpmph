
//页面初始化模块
$(function(){
    var pInit = /**
     * @returns {___anonymous3909_3936}
     */
    function(){
    	var init = function(){
    		var _load_url_ = '/seller/refundReview/gridordlist?';
    		_load_url_= addLoadParamsToUrl(_load_url_);
	    	//分页
	    	$('#pageControlbar').bPage({
	    	    url : GLOBAL.WEBROOT + _load_url_,
	    	    asyncLoad : true,
	    	    asyncTarget : '#order-contentDiv',
	    	    params : {

	    	    }
	    	});
		
	    	//订单详情
			$('.orderId').click(function(){
				var url =  "";
	        	var tr = $(this).closest('tr');
	        	var orderId = $(".orderId",$(tr)).text();
	        	url = GLOBAL.WEBROOT+"/ord/orderdetails/"+orderId;
	        	window.open(url);
	        });
			
			$('.operation').click(function(){
	        	var tr = $(this).closest('tr');
	        	var orderId = $(".orderId",$(tr)).text();
	        	var realMoney=$(".realMoney",$(tr)).text();
	        	var staffName=$(".staffName",$(tr)).text();
	        	selectOrder(orderId,realMoney,staffName);
			});
			
    	};
    	return {
    		init : init
    	};
	};    	
	pageConfig.config({
		//指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : ['bPage'],
		//指定页面
		init : function(){
			var scoreList = new pInit();
			scoreList.init();
		}
	});
});
