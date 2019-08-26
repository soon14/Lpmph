$(function(){
	//将搜索框显示
	$("#article-search").show();
	//初始化分页栏
    $('#pageControlbar').bPage({
        url : GLOBAL.WEBROOT + '/main/toGetData',
        asyncLoad : true,
        pageSizeMenu: [10,20,30],
        asyncTarget : '#aboutDetail',
        params : function(){
        	var $params = $("#serch-params");
        	return {
                "siteInfoId" : $params.data("siteInfoId")||"",
                "keyword" : $params.data("keyword")||"",
                "channelId" : $params.data("channelId")||"",
                "menuType" : "list"
            };
        }
    });
});