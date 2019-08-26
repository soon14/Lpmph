//页面初始化模块
$(function() {
	document.onreadystatechange = loadingChange;//当页面加载状态改变的时候执行这个方法.  
    function loadingChange()   
    {   
            $("#nos").hide();//当页面加载完成后将loading页隐藏  
    } 
	var pInit = function() {
		var init = function() {
			//分页
			$('#pageControlbar').bPage(
					{url : GLOBAL.WEBROOT+ '/seller/custservice/subacctlist',
						asyncLoad : true,
						asyncTarget : '#stockListDiv',
						params:function(){
							return {
								staffCode : $('#staffCode').val(),
								shopId  : $('#shopId').val(),
								serialNumber  :$('#serialNumber').val()
								};
						}
					});
		};
		return {
			init : init
		};
	};
	pageConfig.config({
		//指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : [ 'bPage' ],
		//指定页面
		init : function() {
			var groupList = new pInit();
			groupList.init();
		}
	});
});