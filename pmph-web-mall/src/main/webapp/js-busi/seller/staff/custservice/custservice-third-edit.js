$(function(){
	//选择用户
	$('#btn_cust_select').click(function(){ 
		bDialog.open({
			title : '客户选择',
			width : 820,
			height : 540,
			url : GLOBAL.WEBROOT + "/seller/custservice/custgrid",
			callback:function(data){
				if(data && data.results && data.results.length > 0 ){
					$("#staffId").val(data.results[0].id); 
					$("#staffCode").val(data.results[0].staffCode); 
				}
			}
		});
	});
	
	$('#btnFormSave').click(function(){
		var url =  GLOBAL.WEBROOT + '/seller/custservice/third/savecust';
		if($("#staffCode").val()==""){
			eDialog.alert('请选择用户！');
			return;
		}
		custservice.saveOrUpdate(url);
		
	});
	$('#btnFormUpdate').click(function(){ 
		var url =  GLOBAL.WEBROOT + '/seller/custservice/third/updatecust';
		custservice.saveOrUpdate(url);
	});
	
	$('#btnReturn').click(function(){
		history.back();
	}); 
	
});

var custservice = {
	saveOrUpdate:function(url){
		if(!$("#detailInfoForm").valid())return false; 
		if(parseInt($("#receptionCount").val())>100){
			eDialog.alert("上限接待人数不能超过100!");
			return false; 
		}
		$.eAjax({
			url : url,
			data : ebcForm.formParams($("#detailInfoForm")),
			success : function(data) {
				if(data.resultFlag=="ok"){
					eDialog.success('用户保存成功！',{
						buttons:[{
							caption:"确定",
							callback:function(){
								window.location.href = 'grid';
					        }
						}]
					}); 
				}else{
					eDialog.error(data.resultMsg); 
				}
				
			}
		});
	}
}