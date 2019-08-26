$(function(){
	$('#savemsg').click(function(){
		var val = ebcForm.formParams($("#detailfrom"));				
		$('#savemsg').attr("disabled",false);
		$.eAjax({
			url : GLOBAL.WEBROOT + "/seller/order/customer/updateSellerMsg",
			data : val,
			datatype:'json',
			success : function(result) {
				bDialog.closeCurrent(result);
			}
		});
	});
	$('#btnCancel1').click(function(){
		var result = {}
		result.result='close';
		bDialog.closeCurrent(result);
	});
});