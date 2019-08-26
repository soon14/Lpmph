/**
 * 生成会员卡号
 */
$(function(){
	
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);
	
	
	$('#btnFormSave').click(function(){
		    if(!$("#detailInfoForm").valid()) return false;
			var _custLevelCode = $('#custLevelCode').val();
			var _val = $('#row').val();
			var _cardGroup = $('#cardGroup').val();
			if(_val>1000){
				eDialog.alert("生成条数不能超过1000条");
				return;
			}
			$.gridLoading({"el":"#fluid","messsage":"正在加载中...."});
			$.eAjax({
				url : GLOBAL.WEBROOT + "/card/buildcard",
				data : {'row':_val,'custLevelCode':_custLevelCode,'cardGroup':_cardGroup},
				datatype:'json',
				success : function(returnInfo) {
					$.gridUnLoading({"el":"#fluid"});
					eDialog.success('生成成功！共生成'+returnInfo.resultMsg+'条记录',{buttons:[{
						caption:"确定",
						callback:function(){
							bDialog.closeCurrent({'ifsubmit': true});
				        }
					}]
					}); 
					
				}
			});
	
	});
	
	$('#btn_code_back').click(function(){
		
		bDialog.closeCurrent({'ifsubmit': false});
	})
	
	
	
	
	
})