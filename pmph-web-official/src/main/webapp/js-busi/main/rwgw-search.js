/**
 * 官网搜索功能
 * zhanbh
 */
$(function(){
	$("#rwgwsearch").find("#searchkeyword").live("keydown",function(e){
		if(e.keyCode==13){
			rwgwsearch.search("open");
		}
	});
	
	$("#rwgwsearch").find("#rwgwdosearch").live("click",function(){
		rwgwsearch.search("open");
	});

});

var rwgwsearch ={
		"search" : function (openType){//openType:href  当前网页   open  开一个新网页
			if($("#mallsiteurl").val()){
				if($("#mallsiteurl").val()){
					var url = $("#mallsiteurl").val() + "/search?keyword="+$("#searchkeyword").val()
				}else{
					return false;
				}
				
				if(openType=="href"){
					window.location.href = url;
				}else if(openType=="open"){
					window.open(url,"_blank");
				}
			}else{
				return false;
			}
			
		}
		
}