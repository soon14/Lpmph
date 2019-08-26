$(function(){

	//页面业务逻辑处理内容
    var pageInit = function(){
        var init = function(){
         	$('#pageControlbar').bPage({
        	    url : GLOBAL.WEBROOT + '/getjournalslist',
        	    asyncLoad : true,
        	    asyncTarget : '#dataList',
        	    params : function(){
        	    	return{placeId:$('#placeId').val()}
        	    }
        	});
        };

        return {
            "init" : init
        };
    };
    pageConfig.config({
        //指定需要加载的插件，名称请参考common中定义的插件名称，注意大小写
        plugin : ['bPage'],
        //指定页面
        init : function(){
            var p = pageInit();
            p.init();
        }
    });
});
