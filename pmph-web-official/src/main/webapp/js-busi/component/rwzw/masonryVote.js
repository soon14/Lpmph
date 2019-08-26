;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(["Masonry","infinitescroll","imagesLoaded","artTemplate"], function(Masonry,infinitescroll,imagesLoaded,artTemplate) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$("#voteBox").masonryJs(Masonry,artTemplate);
				}
			};

		});
	} else {
		// 全局模式 ，不使用AMD 规范的时候，使用的插件
		factory(jQuery);
	}
}(function($) {
	
	$.fn.masonryJs = function(Masonry,artTemplate) {
		return this.each(function() {

			$init(this,Masonry,artTemplate);
			
			return $(this);
		});
	};
	
	$init = function(el,Masonry,artTemplate){
	           var $container=$("#"+$(el).attr("id"));
               var newlist;
//               $container.imagesLoaded( function(){
//                   newlist=new Masonry("#"+$(el).attr("id"),{
//                       itemSelector: '.vote-block',
//                       gutter: 10,
//                       isFitWidth: true,
//                       callBack:function(){
//                       }
//                   });
//               });
               loadHtmlAndLayout($('.vote-box .dataLoad'));
               $(el).infinitescroll({
                    navSelector: "#navigation", //导航的选择器，会被隐藏
                    nextSelector: "#navigation a", //包含下一页链接的选择器
                    itemSelector: ".vote-block", //你将要取回的选项(内容块)extraScrollPx:250, //滚动条距离底部多少像素的时候开始加载，默认150
                    errorCallback: function() {
                      //  alert('error');
                    }, //当出错的时候，比如404页面的时候执行的函数
                    path:function(page){
                    	return GLOBAL.WEBROOT+"/vote/voteList?page="+page+"&channelId="+$('#channelId').val();
                    },
                    localMode: true, //是否允许载入具有相同函数的页面，默认为false
                    dataType: 'json',//json数据
                    template: function(data) {
                        //data表示服务端返回的json格式数据，这里需要把data转换成瀑布流块的html格式，然后返回给回到函数
                         var html ="";
                         if(data.values&&data.values.length>0){
                        	 var dataDel={
                        		values:[]
                        	 };
                        	 var dataVal=data.values;
                        	 $.each(dataVal,function(i,item){
                        		 var itemDel=item;
                        		 itemDel.createTime=ebcDate.dateFormat(item['createTime'],"yyyy-MM-dd");
                        		 dataDel.values[i]=item;
                        	 })
                        	 html = artTemplate('voteBlocktpl',dataDel);
                        	 
                        	 /*if(data.values.length<10){
                        		 $('.load-wrap .nodata').show();
                        	 }*/
                         }else{
                        	 $('.load-wrap .nodata').show();
                         }
                        return html;
                    },
                    loading: {
                        selector: '.load-wrap',
                        finishedMsg:'加载完成',
                        msgText:'加载中...',
                        img:'../images/loading.gif',
                    }
                }, function(newElems,opt) {
                    //程序执行完的回调函数
                    var $newElems = $(newElems);
                    $newElems.hide();
                    loadHtmlAndLayout($('.vote-box .dataLoad'),true,$newElems);
                 
                });
               
           	function loadHtmlAndLayout($objs,isAppend,appenObj){
				$objs.each(function(index){
				   	//根据静态文件路径，填充富文本内容。
//					var $obj=$(this);
//		           	var articleContent = $(".bmain",$obj); 
//		           	var staticUrl = articleContent.data('articleUrl');
//		           	if(staticUrl){
//		           		$.ajax({
//		           			url : staticUrl,
//		           			async : true,
//		           			type : "get",
//		           			dataType : 'jsonp',
//		           			jsonp :'jsonpCallback',//注意此处写死jsonCallback
//		           			success: function (data) {
//		           				articleContent.empty();
//		           				articleContent.html(data.result);
//		           				$obj.removeClass('dataLoad');
//		           				if(index==($objs.size()-1)){
//		           					if(!isAppend){
		           					 $container.imagesLoaded( function(){
		                                 newlist=new Masonry("#"+$(el).attr("id"),{
		                                     itemSelector: '.vote-block',
		                                     gutter: 10,
		                                     isFitWidth: true,
		                                     callBack:function(){
		                                     }
		                                 });
		                             });
//		           					}
//		           				}
		           				if(isAppend){
		           				  $container.imagesLoaded( function(){
		           						appenObj.eq(index).show();
		           						newlist.appended(appenObj.eq(index));
		                             });
		           				}
//		           		    }
//		           		});
//		           	}else{
//		           		articleContent.empty();
//		           		articleContent.html("<div class ='pro-empty'>亲，暂无数据！</div>");
//		           	}
				});
	        }
	
	};
}));