$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);
	
	
	$('#btnFormSave').click(function(){
		if(!$("#detailInfoForm").valid())return false;
			var _tmallCode = $('#tmallCode').val();
			var _orderCode = $('#orderCode').val();
			var id =_param.Id;
			$.eAjax({
				url : GLOBAL.WEBROOT + "/custtm/bindthirdcode",
				data : {'thirdCode':_tmallCode,'orderCode':_orderCode,'staffId':id},
				datatype:'json',
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						eDialog.alert('绑定成功',function(){
							setTimeout(function(){bDialog.closeCurrent({'ifsubmit': true});},200); 
						});
					/*	eDialog.success('绑定成功！',{buttons:[{
							caption:"确定",
							callback:function(){
								bDialog.closeCurrent({'ifsubmit': true});
					        }
						}]
						});*/
					}else{
						if(returnInfo.resultMsg!=''){
							eDialog.alert(returnInfo.resultMsg,function(){
								setTimeout(function(){bDialog.closeCurrent({'ifsubmit': false});},200); 
							//	bDialog.closeCurrent({'ifsubmit': false});
							});
						}else{
						eDialog.alert('请确认导入的会员名和订单号，订单需为一天前且交易成功的订单。如会员名和订单号确认无误，请您次工作日再进行尝试。');
						}
					}
					
				}
			});
	
	});
	
	$('#btn_code_back').click(function(){
		
		bDialog.closeCurrent({'ifsubmit': false});
	});
});