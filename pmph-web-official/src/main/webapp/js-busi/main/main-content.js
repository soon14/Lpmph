$(function(){

	//页面业务逻辑处理内容
    var pageInit = function(){
        var init = function(){
        	/*//初始化显示首页
    		ajaxReq("");
        	
        	//绑定tab页切换
			$("#site_column a").live("click",function(e){
				//获取楼层商品数据
				$("#site_column a").removeClass("active");
				$(this).addClass("active");
				ajaxReq($(this).attr("url"));
			});*/
        };

        return {
            "init" : init
        };
    };
    pageConfig.config({
        //指定需要加载的插件，名称请参考common中定义的插件名称，注意大小写
        plugin : [],
        //指定页面
        init : function(){
            var p = pageInit();
            p.init();
        }
    });
});

function ajaxReq(reqUrl){
	var defaultUrl = "/homepage/homepage";
	if(reqUrl == ""){
		reqUrl = defaultUrl;
	}
	$.eAjax({
		url : GLOBAL.WEBROOT + reqUrl,
		data : {
			//placeId : $("#placeId").val()
		},
		dataType : "html",
		success : function(returnInfo) {
			//alert(returnInfo);
			$("#content_div").empty();
			$("#content_div").html(returnInfo);
		}
	});
}