//detail
$("#backToWdr").live("click",function(){
	window.location.href = GLOBAL.WEBROOT+'/seller/shopmgr/withdraw';
});	
$("#btnConfirm").live("click",function(){
	//店铺ID
	var shopId=$("#shopIdHidden").val();
//	alert("店铺ID:"+shopId);
	//提现金额
	var applicationMoneyHidden=$("#applicationMoneyHidden").val();
//	alert("提现金额:"+applicationMoneyHidden);
	//结算账期
	var monthStr=$("#monthStrHidden").val();
//	alert("结算账期:"+monthStr);
	//申请人备注
	var remark=$("#remark").val();
//	alert("申请人备注:"+remark);
	$.eAjax({
		url : GLOBAL.WEBROOT + "/seller/shopmgr/withdraw/realSubmit",
		async : true,
		data : {shopId : shopId,money : applicationMoneyHidden,billMonths : monthStr,applyDesc : remark},
		type : "post",
		dataType:'json',
		success : function(data) {
			if(data.resultFlag=='ok'){
				window.location.href = GLOBAL.WEBROOT+'/seller/shopmgr/withdraw';
			}
			if(data.resultFlag=='expt'){
				eDialog.alert(returnInfo.resultMsg);
			}
		}
	});
});
//隐藏显示详细信息
//function tableToggle(current){
//	var curId=current.getAttribute('id');
//	 $("#"+curId).parents(".wdr-order-toggleBtn").siblings(".wdr-order-hide").toggle(500);
//}
function tableToggle(obj){
	$(obj).parents(".wdr-order-toggleBtn").siblings(".wdr-order-hide").toggle(500);
}
$(function(){
    //一加载 就隐藏掉
	$(".wdr-order-hide").hide();
});