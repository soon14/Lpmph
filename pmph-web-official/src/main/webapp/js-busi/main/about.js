$(function(){
	about.initMenuType("1403,1404","grid");//通过 内容位置 初始化列表限制类型
//	about.bindSiteInfoClick();
	about.bindSearchClick();
	about.bindArticleClick();
	about.bindBread1Click();
	about.bindBread2Click();
});

var about = {
	init : function(){//初始化
		var siteInfoId = $("#siteInfoId").val();
		var articleId = $("#articleId").val();
		var channelId = $("#channelId").val();
		var menuType = $("#menuType").val();
		
		//初始化面包削
		var breadName = "";
		if(siteInfoId){
			breadName = $("#siteInfoName").val() || "";
		}else{
			breadName = $("#channelName").val() || "";
		}
		about.initBread0();
		about.initBread1(siteInfoId,channelId,breadName);
		about.initBread2(articleId,$("#articleTitle").val() || "");
		//将当前网站信息选中
		if(siteInfoId){
			$("#siteinfo-"+siteInfoId).addClass("active");
		}	
		if(articleId){//有文章id则去取文章的内容
			about.queryArticle(articleId);
		}else{//没有文章id则去取网站信息的内容或者栏目下的文章列表
			about.toGetData(siteInfoId,channelId,menuType);
		}
	},
	"toGetData":function(siteInfoId,channelId,menuType,keyword){//获取栏目 或网站信息  数据
		siteInfoId = 0 === siteInfoId ? "0" : siteInfoId;
		channelId = 0 === channelId ? "0" : channelId;
		
		//如果没指定展示类型则取网站信息的展示类型  都无则按列表显示  网站信息的展示类型是按首页的内容位置指定的
		if(!menuType){
			menuType = $("#siteinfo-"+siteInfoId).attr("menuType") || $("#deMenuType").val() || "";
		}
		var pageSize = 10;
		if(menuType && menuType == "grid"){
			pageSize = 8;
		}else if(menuType == "list2"){
			pageSize = 6;
		}
		var $aboutDetail = $("#aboutDetail");
		$aboutDetail.html("<div class='loading-small'></div>");
		$.eAjax({
			url : $webroot + '/main/toGetData',
			data : {
				"keyword" : keyword?keyword:"",
				"siteInfoId" : siteInfoId?siteInfoId:"",
				"channelId" : channelId?channelId:"",
				"menuType" :menuType?menuType:'',
				"pageSize" : pageSize?pageSize:"10"
			},
			async : true,
			type : "post",
			dataType : "html",
			success : function(data, textStatus) {
				$aboutDetail.html(data);
			},
			error :  function(){
				$aboutDetail.empty();
				eDialog.alert("网络异常，请重试");
			}
		});
	},
	"queryArticle":function(articleId){
		articleId = 0 === articleId ? "0" : articleId;
		
		var $aboutDetail = $("#aboutDetail");
		$aboutDetail.html("<div class='loading-small'></div>");
		$.eAjax({
			url : $webroot + '/main/queryArticle',
			data : {
				"articleId" : articleId
			},
			async : true,
			type : "post",
			dataType : "html",
			success : function(data, textStatus) {
				$aboutDetail.html(data);
			},
			error :  function(){
				$aboutDetail.empty();
				eDialog.alert("网络异常，请重试");
			}
		});
	},
	"bindSiteInfoClick": function(){
		$(".siteinfoitem").live("click",function(){
			var $this = $(this);
			$(".siteinfoitem").removeClass("active");
			$this.addClass("active");
			about.toGetData($this.data("id"));
			about.initBread1($this.data("id"),"",$this.data("name"));
		});
	},
	//根据内容位置id 初始化网站信息的显示类型
	"initMenuType" : function (placeIds,menuType) {
		if(placeIds && menuType){
			$.eAjax({
				url : $webroot + '/main/qryChannelIds',
				data : {
					"placeIds" : placeIds
				},
				async : true,
				type : "post",
				dataType : "json",
				success : function(data) {
					if(data && data.length > 0){
						$.each(data, function(i, n) {
							$(".siteinfoitem[data-channel-id="+n+"]").attr("menuType",menuType);
						});
					}
					about.init();
				},
				error :  function(){
					about.init();
				},
				exception: function(){
					about.init();
				}
			});
		}else{
			about.init();
		}
	},
	"bindSearchClick":function(){//绑定文章搜索框事件   文章搜索框只有在新版的列表才会有
		$(".ibtn",$("#article-search")).live("click",function(){
			var $params = $("#serch-params");//因为搜索是在当前栏目下搜索  故取其分页栏参数是最准确的
        	about.toGetData($params.data("siteInfoId")||"",$params.data("channelId")||"",$params.data("menuType")||"",$(".itxt",$("#article-search")).val() || "");
		});
		$(".itxt",$("#article-search")).live("keydown",function(e){
			if(e.keyCode==13){
				var $params = $("#serch-params");//因为搜索是在当前栏目下搜索  故取其分页栏参数是最准确的
	        	about.toGetData($params.data("siteInfoId")||"",$params.data("channelId")||"",$params.data("menuType")||"",$(".itxt",$("#article-search")).val() || "");
			}
		});
	},
	"bindArticleClick":function(){//绑定文章事件   
		$(".article-item").live("click",function(){
			var $this = $(this);
        	about.queryArticle($this.data("id"));
        	about.initBread2($this.data("id"),$this.data("title"));
		});
	},
	"initBread0":function(){//只初始化一次
		var topSiteInfoName = $("#topSiteInfoName").val();
		var $bread0 = $("#breadcrumb-0");
		if(topSiteInfoName){
			$(".bread-name",$bread0.show()).html(topSiteInfoName);
		}else{
			$(".divider",$("#breadcrumb-1")).hide();
		}
	},
	"initBread1":function(siteInfoId,channelId,name){//初始化一级面包屑
		siteInfoId = 0 === siteInfoId ? "0" : siteInfoId;
		channelId = 0 === channelId ? "0" : channelId;
		if(name){
			var $bread1 = $("#breadcrumb-1");
			$bread1.attr("title",name);
			if(10 < name.length){
				name = name.substring(0,10)+"...";
			}
			$("#breadcrumb-2").hide();
			$(".bread-name",$bread1.show()).html(name);
			$bread1.data("siteInfoId",siteInfoId||"");
			$bread1.data("channelId",channelId||"");
		}
	},
	"initBread2":function(id,name){//初始化二级面包屑
		var $bread2 = $("#breadcrumb-2");
		if(name){
			$bread2.attr("title",name);
			if(25 < name.length){
				name = name.substring(0,25)+"...";
			}
			$(".bread-name",$bread2.show()).html(name);
			$bread2.data("articleId",id);
		}
	},
	"bindBread1Click":function(){
		$("#breadcrumb-1").live("click",function(){
			$(".itxt",$("#article-search")).val("");
			$("#breadcrumb-2").hide();
			var $this = $(this);
        	about.toGetData($this.data("siteInfoId")||"",$this.data("channelId")||"");
		});
	}
	,
	"bindBread2Click":function(){
		$("#breadcrumb-2").live("click",function(){
        	about.queryArticle($(this).data("articleId"));
		});
	}
	
};