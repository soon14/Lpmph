$(function(){

	//页面业务逻辑处理内容
    var pageInit = function(){
        var init = function(){
        	
        	// 加载数据
        	$.eAjax({
        		url : $webroot + '/getjournalslist',
        		async : false,
        		type : "post",
        		dataType : "html",
        		data : {
        			'pageNumber' : 1,
        			'pageSize' : 10,
        			'placeId' : $('#placeId').val()
        		},
        		success : function(data) {
        			
        			$('#dataList').html(data)
        			
        		},
        		exception : function(data) {
        			$('#dataList').html("暂无数据");
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
