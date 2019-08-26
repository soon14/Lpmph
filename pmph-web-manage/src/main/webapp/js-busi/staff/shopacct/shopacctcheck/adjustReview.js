$(function(){
	$('#allow').click(function(){
		var id = $("#id").val();
		var checkDesc = $("#checkDesc").val();
		var data = [];
		data.push({name:'id',value:id},{name:'checkDesc',value:checkDesc});
		$.eAjax({
			url : GLOBAL.WEBROOT + "/shopAcctAdjust/adjustUpdateReview?status="+1,
			data : data,
			async : true,
			type : "post",
			datatype:'json',
			success : function(returnInfo) {
				if(returnInfo.resultFlag=='ok'){
					eDialog.success('已审核通过！',{
						buttons:[{
							caption:"确定",
							callback:function(){
								var url=GLOBAL.WEBROOT + '/shopAcctAdjust/ajustCheck';
								window.location.href=url;
					        }
						}]
					}); 
				}
			}
		});
	});
	$('#unallow').click(function(){
		var id = $("#id").val();
		var checkDesc = $("#checkDesc").val();
		var data = [];
		if(!checkDesc || checkDesc == ''){
			eDialog.alert("审核意见不能为空",function(){
			},'error');
			return;
		}
		data.push({name:'id',value:id},{name:'checkDesc',value:checkDesc});
		$.eAjax({
			url : GLOBAL.WEBROOT + "/shopAcctAdjust/adjustUpdateReview?status="+2,
			data : data,
			async : true,
			type : "post",
			datatype:'json',
			success : function(returnInfo) {
				if(returnInfo.resultFlag=='ok'){
					eDialog.success('已审核拒绝！',{
						buttons:[{
							caption:"确定",
							callback:function(){
								var url=GLOBAL.WEBROOT + '/shopAcctAdjust/ajustCheck';
								window.location.href=url;
					        }
						}]
					}); 
				}
			}
		});
	});
	$('#return').click(function(){
		var url=GLOBAL.WEBROOT + '/shopAcctAdjust/ajustCheck';
		window.location.href=url;
	});
});