//隐藏显示详细信息
function tableToggle(current){
	var curId=current.getAttribute('id');
	 $("#"+curId).parents(".wdr-order-toggleBtn").siblings(".wdr-order-hide").toggle(500);
}
$(function(){
    //一加载 就隐藏掉
	$(".wdr-order-hide").hide();
});