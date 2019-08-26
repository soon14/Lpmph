
$(function(){
	

	$('#allow').click(function(){
		$('#allow').attr('href','#');//去掉a标签中的href属性
		$('#allow').attr('onclick','');//去掉a标签中的onclick事件
		var orderId = $('#orderId').val();
		var backId = $('#backId').val();
		var payWay = $("input[name='payway']:checked").val();
		var checkDesc = $('#checkDesc').val();
		var applyType = $('#applyType').val();
		var shareInfo = $('#shareInfo').val();
		var status = $('#status').val();
		var data = [];
		data.push({name:'payType',value:payWay});
		data.push({name:'checkDesc',value:checkDesc});
		data.push({name:'orderId',value:orderId});
		data.push({name:'backId',value:backId});
		if(status == "00"){ //一级审核
			data.push({name:'status',value:"1"});
		} else if(status =="06"){ //二级审核
			data.push({name:'status',value:"2"});
		} else if(status == "07") { //三级审核
			data.push({name:'status',value:"3"});
		}

		data.push({name:'shareInfo',value:shareInfo});
		var reviewUrl = null;
		if(applyType == "0"){
			reviewUrl = GLOBAL.WEBROOT+'/seller/refundReview/confirmReview';
		} else if(applyType == "1"){
			reviewUrl = GLOBAL.WEBROOT+'/seller/backReview/confirmReview';
		} else {
			return ;
		}
		$.eAjax({
			url:reviewUrl,
			data:data,
			success:function(result){
				if(result&&result.resultFlag=='ok'){
					
					eDialog.alert('审核成功',function(){
						if(applyType == "0"){
							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund3';
							}

						} else if(applyType == "1"){

							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds3';
							}
						}
						bDialog.closeCurrent({result:'ok'});
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
//						window.location.href = GLOBAL.WEBROOT+'/ordback/review';
						if(applyType == "0"){
							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund3';
							}
						} else if(applyType == "1"){
							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds3';
							}
						}
						bDialog.closeCurrent({result:'ok'});
					},'error');
					
				}
			},
			failure:function(){
				if(applyType == "0"){
					if(status == "00"){ //一级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund1';
					} else if(status =="06"){ //二级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund2';
					} else if(status == "07") { //三级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund3';
					}
				} else if(applyType == "1"){
					if(status == "00"){ //一级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds1';
					} else if(status =="06"){ //二级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds2';
					} else if(status == "07") { //三级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds3';
					}
				}
				bDialog.closeCurrent();
			}
		});
		       
	});
	$('#unallow').click(function(){
		$('#unallow').attr('href','#');//去掉a标签中的href属性
		$('#unallow').attr('onclick','');//去掉a标签中的onclick事件
		var orderId = $('#orderId').val();
		var backId = $('#backId').val();
		var payWay = $("input[name='payway']:checked").val();
		var checkDesc = $('#checkDesc').val();
		var applyType = $('#applyType').val();
		var status = $('#status').val();
		
		var data = [];
		if(!checkDesc || checkDesc == ''){
			eDialog.alert("审核意见不能为空",function(){
			},'error');
			return;
		}
		
		data.push({name:'payType',value:payWay});
		data.push({name:'checkDesc',value:checkDesc});
		data.push({name:'orderId',value:orderId});
		data.push({name:'backId',value:backId});
		data.push({name:'status',value:"0"});
		var reviewUrl = null;
		if(applyType == "0"){
			reviewUrl = GLOBAL.WEBROOT+'/seller/refundReview/confirmReview';
		} else if(applyType == "1"){
			reviewUrl = GLOBAL.WEBROOT+'/seller/backReview/confirmReview';
		} else {
			return ;
		}
		$.eAjax({
			url:reviewUrl,
			data:data,
			success:function(result){
				if(result&&result.resultFlag=='ok'){
					eDialog.alert('【审核不通过】操作成功',function(){
						if(applyType == "0"){
							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund3';
							}
							
						} else if(applyType == "1"){
							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds3';
							}
						}
						bDialog.closeCurrent({result:'ok'});
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
//						window.location.href = GLOBAL.WEBROOT+'/ordback/review';
						if(applyType == "0"){
							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund3';
							}
						} else if(applyType == "1"){
							if(status == "00"){ //一级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds1';
							} else if(status =="06"){ //二级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds2';
							} else if(status == "07") { //三级审核
								window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds3';
							}
						}
						bDialog.closeCurrent();
					},'error');
					
				}
			},
			failure:function(){
				if(applyType == "0"){
					if(status == "00"){ //一级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund1';
					} else if(status =="06"){ //二级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund2';
					} else if(status == "07") { //三级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund3';
					}
				} else if(applyType == "1"){
					if(status == "00"){ //一级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds1';
					} else if(status =="06"){ //二级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds2';
					} else if(status == "07") { //三级审核
						window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds3';
					}
				}
				bDialog.closeCurrent();
			}
		});
	});
	$('#return').click(function(){
		var applyType = $('#applyType').val();
		var status = $('#status').val();
		if(applyType == "0"){
			if(status == "00"){ //一级审核
				window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund1';
			} else if(status =="06"){ //二级审核
				window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund2';
			} else if(status == "07") { //三级审核
				window.location.href = GLOBAL.WEBROOT+'/seller/refundReview/refund3';
			}
			
		} else if(applyType == "1"){
			if(status == "00"){ //一级审核
				window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds1';
			} else if(status =="06"){ //二级审核
				window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds2';
			} else if(status == "07") { //三级审核
				window.location.href = GLOBAL.WEBROOT+'/seller/backReview/backgds3';
			}
		}
	});

	$('#update_money_btn').click(function(){
		var ordId=$('#orderId').val();
		var scale = $('#scale').val();
		var backId = $('#backId').val();
		var backMoney = $('#backMoney').val();
		var reduCulateScore = $('#backScore').val();
		var staffId = $('#staffId').val();
		var isLastFlag = $('#isLastFlag').val();
		backMoney = toDecimal(backMoney);
		bDialog.open({
		    title : '调整退款金额',
		    width : 600,
		    height : 300,
		    scroll : false,
		    url : GLOBAL.WEBROOT+'/seller/backReview/goModifyMoney?orderId=' + ordId+ '&scale=' + scale+ '&backId=' + backId+ '&backMoney=' + backMoney+ '&reduCulateScore=' + reduCulateScore+ '&staffId=' + staffId+ '&lastFlag=' + isLastFlag,
			callback:function(data){
				
			},		   
		});
	});

	
	$('body').delegate('div.dialogInActive #save_money_btn', 'click', function(e) {
		var dlg = bDialog.getDialog();
		var _this = this;
		if($(_this).hasClass('disabled')){
			return false;
		}
		if(!$('#updateBackMoneyForm',$(dlg)).valid()) return false;
		var data = ebcForm.formParams($('#updateBackMoneyForm',$(dlg)));
		var orderId = $('#orderId').val();
		var backId = $('#backId').val();
		var backMoney = $('#backMoney').val();
		var backScore = $('#backScore').val();
		backMoney = toDecimal(backMoney);
		data.push({name:'reduCulateScore',value:backScore});
		data.push({name:'backMoney',value:backMoney});
		data.push({name:'orderId',value:orderId});
		data.push({name:'backId',value:backId});
		$(_this).addClass('disabled');
		
		$.eAjax({
			url:GLOBAL.WEBROOT+'/seller/backReview/modifyBackMoney',
			data:data,
			success:function(result){
				if(result&&result.resultFlag=='ok'){
					alert('OK!');
				}
			}
		});
	});

	$('body').delegate('div.dialogInActive #close_btn', 'click', function(e) {
		var dlg = bDialog.getDialog();
		bDialog.closeCurrent();
	});
	
	// //返回
	// $('#btnReturn').click(function(){
	// 	 history.go(-1);
	// });

});

//功能：将浮点数四舍五入，取小数点后2位 
function toDecimal(x) { 
 var f = parseFloat(x); 
 if (isNaN(f)) { 
  return; 
 } 
 f = Math.round(x*100)/100; 
 return f; 
} 