$(function(){

	
	$('#btnFormSave').click(function(){
		if(!$("#detailInfoForm").valid()) return false;  
		var param = ebcForm.formParams($("#detailInfoForm"));
		$(this).button('loading');
		$.eAjax({
			url : GLOBAL.WEBROOT + "/custcardsend/save",
			data : param,
			datatype:'json',
			success : function(returnInfo) {
				eDialog.success('保存成功！',{
					buttons:[{
						caption:"确定",
						callback:function(){
							$('#btnFormSave').button('reset');
							window.location.href = GLOBAL.WEBROOT+'/custcardsend/main?tab=2';
				        }
					}]
				}); 
			},
			exception:function(){
				$('#btnFormSave').button('reset');
			}
		});
					
	});
	
	$('#btnReturn').click(function(){
		//$('#returnFrom').submit();
//		history.back();
		window.location.href = GLOBAL.WEBROOT+'/custcardsend/main?tab=2';
	});

	
});