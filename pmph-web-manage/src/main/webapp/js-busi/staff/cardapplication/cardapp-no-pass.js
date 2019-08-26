
$(function () { 
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);
	
    //获取选中选项的值 
    $("#btnSave").click(function(){ 
    	if ($.trim($('#remark').val()) == '') {
    		eDialog.alert("请填写审核不通过原因！");
    		return;
    	}
    	$.eAjax({
			url : GLOBAL.WEBROOT + "/cardapplication/nopass/save",
			data : {'id':$('#id').val(),
					'remark':$('#remark').val(),
					'staffId':$('#staffId').val()
					},
			datatype:'json',
			success : function(returnInfo) {
				eDialog.alert(returnInfo.resultMsg);
				bDialog.closeCurrent();
			}
		});
	
    });
    
    $("#btnReturn").click(function(){ 
    	bDialog.closeCurrent();
    }); 
    
});  