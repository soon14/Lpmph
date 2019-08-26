$(function(){	
    //一加载 就隐藏掉
	$(".wdr-order-hide").hide();
	//审核通过
	$('#allow').click(function(){
		$('#allow').attr('href','#');//去掉a标签中的href属性
		$('#allow').attr('onclick','');//去掉a标签中的onclick事件
		var shopId = $('#shopId').val();
		var applyId = $('#applyId').val();
		var status = $('#status').val();
		var checkDesc = $('#checkDesc').val();
		var data = [];
		//店铺ID
		data.push({name:'shopId',value:shopId});
		//审核人备注
		data.push({name:'checkDesc',value:checkDesc});
		//提现申请ID
		data.push({name:'applyId',value:applyId});
		//审核前状态，如果是一级审核，审核后该申请单状态为待二级审核，如果该申请单状态为待二级审核，审核后该申请单状态为允许提现
		data.push({name:'status',value:status});
		var reviewUrl = GLOBAL.WEBROOT+'/withdrawReview/confirmReview';
		$.eAjax({
			url:reviewUrl,
			data:data,
			success:function(result){
				if(result&&result.resultFlag=='ok'){
					
					eDialog.alert('审核成功',function(){

						if(status == "00"){ //跳转到提现一级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw1';
						} else if(status =="01"){ //跳转到提现二级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw2';
						}						
						bDialog.closeCurrent({result:'ok'});
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
						if(status == "00"){ //跳转到提现一级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw1';
						} else if(status =="01"){ //跳转到提现二级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw2';
						}					
						bDialog.closeCurrent({result:'ok'});
					},'error');					
				}
			},
			failure:function(){
				if(status == "00"){ //跳转到提现一级审核页面
					window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw1';
				} else if(status =="01"){ //跳转到提现二级审核页面
					window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw2';
				}
				bDialog.closeCurrent();
			}
		});
		       
	});
	//审核不通过
	$('#unallow').click(function(){
		$('#unallow').attr('href','#');//去掉a标签中的href属性
		$('#unallow').attr('onclick','');//去掉a标签中的onclick事件
		var shopId = $('#shopId').val();
		var applyId = $('#applyId').val();
		var status = $('#status').val();
		var checkDesc = $('#checkDesc').val();		
		var data = [];
		if(!checkDesc || checkDesc == ''){
			eDialog.alert("审核意见不能为空",function(){
			},'error');
			return;
		}		
		//店铺ID
		data.push({name:'shopId',value:shopId});
		//审核人备注
		data.push({name:'checkDesc',value:checkDesc});
		//提现申请ID
		data.push({name:'applyId',value:applyId});
		//审核前状态
		data.push({name:'status',value:status});
		//拒绝提现单独的控制器方法
		var reviewUrl = GLOBAL.WEBROOT+'/withdrawReview/refush';
		$.eAjax({
			url:reviewUrl,
			data:data,
			success:function(result){
				if(result&&result.resultFlag=='ok'){
					eDialog.alert('【审核不通过】操作成功',function(){
						if(status == "00"){ //跳转到提现一级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw1';
						} else if(status =="01"){ //跳转到提现二级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw2';
						}
						bDialog.closeCurrent({result:'ok'});
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
						if(status == "00"){ //跳转到提现一级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw1';
						} else if(status =="01"){ //跳转到提现二级审核页面
							window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw2';
						}
						bDialog.closeCurrent();
					},'error');
					
				}
			},
			failure:function(){
				if(status == "00"){ //跳转到提现一级审核页面
					window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw1';
				} else if(status =="01"){ //跳转到提现二级审核页面
					window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw2';
				}
				bDialog.closeCurrent();
			}
		});
	});
	//返回
	$('#return').click(function(){
		var status = $('#status').val();
		if(status == "00"){ //跳转到提现一级审核页面
			window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw1';
		} else if(status =="01"){ //跳转到提现二级审核页面
			window.location.href = GLOBAL.WEBROOT+'/withdrawReview/withdraw2';
		}
	});
});