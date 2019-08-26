$(function(){

	
	$('#p_btnFormSave').click(function(){
		if($('#p_btnFormSave').hasClass('disabled')){
			return false;  
		}	
		$('#p_btnFormSave').addClass('disabled');
		if(!$("#p_detailInfoForm").valid()){
			$('#p_btnFormSave').removeClass('disabled');
			return false;  
		}
		
		var param = ebcForm.formParams($("#p_detailInfoForm"));
			
		$.eAjax({
			url : GLOBAL.WEBROOT + "/carderinfo/save",
			data : param,
			datatype:'json',
			success : function(returnInfo) {
				if(returnInfo.resultFlag=="ok"){
					eDialog.success('保存成功！',{
						buttons:[{
							caption:"确定",
							callback:function(){
								window.location.href = GLOBAL.WEBROOT+'/custcardsend/main?tab=3';
					        }
						}]
					}); 
				}else{
					eDialog.error(returnInfo.resultMsg);
				}
				$('#p_btnFormSave').removeClass('disabled');
				
			}
		});
					
	});
	
	$('#p_btnReturn').click(function(){
		//$('#returnFrom').submit();
//		history.back();
		window.location.href = GLOBAL.WEBROOT+'/custcardsend/main?tab=3';
	});

	
});