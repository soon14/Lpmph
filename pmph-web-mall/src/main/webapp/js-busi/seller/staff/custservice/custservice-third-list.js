//页面初始化模块
$(function(){
/*	$.eAjax({
		url : GLOBAL.WEBROOT + '/custservice/gridlist',
		data :"",
		type : "post",
		dataType : "json",
		success : function(data) {
		}
	});*/
	var pInit = function() {
			var init = function() {
				var shopId = $("#ids").val();
				var data = new Object();
				data.shopId = shopId;
					// 分页
					$('#pageControlbar').bPage({
						url : GLOBAL.WEBROOT+ '/seller/custservice/gridlist?ids='+shopId,
						asyncLoad : true,
						asyncTarget : '#stockListDiv',
						params : function() {
							return data;
						}
					});
			};
			return {
				init : init
			};
	};
	pageConfig.config({
		// 指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : ['bPage'],
		// 指定页面
		init : function() {
			var stockList = new pInit();
			stockList.init();
		}
	});
});
