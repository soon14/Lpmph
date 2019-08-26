$(function(){
	$('#saveaddr').click(function(){
		if(!$("#addrdetailfrom").valid()) return false;
		var val = ebcForm.formParams($("#addrdetailfrom"));				
		var dlg = bDialog.getDialog();
		var params = bDialog.getDialogParams(dlg);
		$('#saveaddr').attr("disabled",false);
		$(this).button('提交...');//设置加载
		$.eAjax({
			url : GLOBAL.WEBROOT + "/seller/order/customer/saveaddr",
			data : val,
			datatype:'json',
			success : function(result) {
				$(this).button('reset');//按钮还原
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