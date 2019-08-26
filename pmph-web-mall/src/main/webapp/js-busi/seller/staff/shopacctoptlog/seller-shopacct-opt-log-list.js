
//页面初始化模块
$(function(){
    var pInit = function(){
    	var init = function(){
    		var _load_url_ = '/seller/shopmgr/shopAcctOptLog/optLoglist?_t='+new Date().getTime();
	    	//分页
	    	$('#pageControlbar').bPage({
	    	    url : GLOBAL.WEBROOT + _load_url_,
	    	    asyncLoad : true,
				params: function(){
					var params= {};
					var search = ebcForm.formParams($("#searchForm"));
					for(var i= 0,length=search.length;i<length;i++){
						params[search[i].name] = search[i].value;
					}
					return params;
				},
	    	    asyncTarget : '#log-contentDiv'
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
