//页面初始化模块
$(function(){
	
	$('#btnSaveBillInfo0').click(function() {
		if ($(this).closest('form').attr('id') == 'billForm0') {
			if(!$("#billForm0").valid()) return false;
			var val = ebcForm.formParams($("#billForm0"));		
			$.eAjax({
				url : GLOBAL.WEBROOT + "/seller/order/customer/savebill",
				data : val,
				datatype:'json',
				success : function(result) {
					$(this).button('reset');//按钮还原
					bDialog.closeCurrent(result);
				}
			});
		}
	});
	
	$('#btnSaveBillInfo').click(function(){
    	if(!$("#billForm1").valid())return false;
    	    	
		var detailFlag = $("input:radio[name='ordInvoiceCommRequest.detailFlag']:checked").val();
		var billContent = $("input:radio[name='ordInvoiceCommRequest.invoiceContent']:checked").val();
    	if(billContent==''||billContent==null){
    		eDialog.alert('请选择一项发票内容！');
    	}else{
			if($("input[name='organization']:checked").val()==0){
				$("#taxpayerNo").val("");
			}
    		var val = ebcForm.formParams($("#billForm1"));		
			$.eAjax({
				url : GLOBAL.WEBROOT + "/seller/order/customer/savebill",
				data : val,
				datatype:'json',
				success : function(result) {
					$(this).button('reset');//按钮还原
					bDialog.closeCurrent(result);
				}
			});    		
    	}
	});
	
	$('.billc-swtich>a').click(function(){
	  $(this).addClass('checked').siblings().removeClass('checked');
	  $(this).find("input").prop("checked", "checked");
	})
	
	//发票开关
	$('._bill').find('a').click(function(){
		 $(this).removeClass('checked');
		var tab = $(this).attr("class");
		 $(this).addClass('checked').siblings().removeClass('checked');
		if(tab == 'pane1'){
			$("#pane1").show();
			$("#pane0").hide();
			//解决IE11 二次修改发票光标不锁定问题
			//$("#pane1 #billTitle")[0].focus();
		}else{
			$("#pane0").show();
			$("#pane1").hide();
		}
	});  
	
	$('#btnCancel0').click(function(){
		var result = {'result':'close'}
		bDialog.closeCurrent(result);
	});
	$('#btnCancel1').click(function(){
		var result = {'result':'close'}
		bDialog.closeCurrent(result);
	});
	$("input[name='organization']").click(function(){
		if($(this).val()==1){
			$(".pane4").show();
			$("#titleTip").show();
		}else{
			$(".pane4").hide();
			$("#titleTip").hide();
		}
	});
	updateCheck();
});
	function changeTitleColor(obj){
		if(!$("#billTitle").valid()){
			$(obj).css({ "border-color": "#C62626" });
		}else{
			$(obj).css({ "border-color": "" });
		}
	}
	function changeTaxpayerNoColor(obj){
		
		if(!$("#taxpayerNo").valid()){
			$(obj).css({ "border-color": "#C62626" });
		}else{
			$(obj).css({ "border-color": "" });
		}
	}
	function updateCheck(){
		if($("#type").val()==0){
			$('.pane1').trigger("click");
			if($("#taxpayerNo").val()==""){
				$(".radio2").trigger("click");
			}
		}
	}