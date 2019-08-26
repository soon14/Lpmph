/**
 * 从服务器后去静态html代码
 * zhanbh
 */
$(function(){
	//根据静态文件路径，填充富文本内容。
	var staticUrl = $("#articleStaticUrl").val();
	var articleContent = $("#htmlCont"); 
	var url = staticUrl;
	if(staticUrl){
		//var url = "http://192.168.1.102:8080/imageServer/static/html/55f68185cc964eefd42d1429";
		$.ajax({
			url : url,
			async : true,
			type : "get",
			dataType : 'jsonp',
			jsonp :'jsonpCallback',//注意此处写死jsonCallback
			success: function (data) {
				articleContent.empty();
				articleContent.html(data.result);
		    }
		});
	}else{
		articleContent.empty();
		articleContent.html("<div class ='pro-empty'>亲，暂无数据！</div>");
	}
	
});