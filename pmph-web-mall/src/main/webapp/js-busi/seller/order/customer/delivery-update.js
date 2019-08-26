$(function(){
	//物流方式:如果是自提【物流公司】下拉框和【快递单号】输入框变灰色，如果是邮局挂号则只有【物流公司】下拉框变灰色
	var sendType=$("#sendType").val();
	if(sendType != "" && sendType != undefined){
		if(sendType=="1"){//快递
			$("._kd").click();
		}else if(sendType=="2"){//自提
			$("._zt").click();
			$('#expressNo').val('').prop('disabled',true).parent().removeClass('f_error').find('label').hide();
			$('#expressCompanyId').val('').prop('disabled',true);
		}else if(sendType=="0"){//邮局挂号
			$("._py").click();
			$('#expressCompanyId').val('').prop('disabled',true);
			$('#expressNo').val('').prop('disabled',false);
		}
	}
	//物流公司回显
	var expressCompanyFlag=$("#expressCompanyFlag").val();
	if(expressCompanyFlag != "" && expressCompanyFlag != undefined){
		var numbers = $("#expressCompanyId").find("option"); //获取select下拉框的所有值
		for (var j = 1; j < numbers.length; j++) {
			if ($(numbers[j]).val() == expressCompanyFlag) {
				$(numbers[j]).attr("selected", "selected");
			}
		} 
	}
	//快递单号回显
	var expressNoFlag=$("#expressNoFlag").val();
	if(expressNoFlag != "" && expressNoFlag != undefined){
		$("#expressNo").val(expressNoFlag);
	}
	$("._zt").click(function(){
		//去物流公司遗留样式
		$('#expressNo').val('').prop('disabled',true).parent().removeClass('f_error').find('label').hide();
		$('#expressCompanyId').val('').prop('disabled',true);
	});
	
	$("._kd").click(function(){
		$('#expressCompanyId').prop('disabled',false);
		$('#expressNo').prop('disabled',false);
	});

	$("._py").click(function(){
		$('#expressCompanyId').val('').prop('disabled',true);
		$('#expressNo').val('').prop('disabled',false);
	});
	//确认修改按钮
	$('#saveDeliveryInfo').click(function(){
		//检查数据是否符合规范
		if(!checkData()) return false;
		var val = ebcForm.formParams($("#deliveryInfoForm"));				
		var dlg = bDialog.getDialog();
		var params = bDialog.getDialogParams(dlg);
		$(this).button('提交...');//设置加载
		$.eAjax({
			url : GLOBAL.WEBROOT + "/seller/order/customer/saveDelivereyInfo",
			data : val,
			datatype:'json',
			success : function(result) {
				bDialog.closeCurrent(result);
			}
		});
	});
	//取消按钮
	$('#btnCancel1').click(function(){
		var result = {}
		result.result='close';
		bDialog.closeCurrent(result);
	});
});
function checkExpressNo(obj){
    	//取值
    	var expressNo=$(obj).val();
    	//去掉空格
    	expressNo=$.trim(expressNo);
    	var errorContext=$(".error").val();
    	if(expressNo.length<6||expressNo.length>32){
    		//if(errorContext==undefined){
        		//给出提示信息
        		$(".errorSpan").text("长度介于 6 和 32 之间的字符串");
        		//提示字体颜色修改
        		$(".errorSpan").css({ "color": "#C62626" });
        		//快递编号输入框边框颜色修改
        		$(obj).css({ "border-color": "#C62626" });
    		//}
    	}else{
    		//隐藏提示信息
    		$(".errorSpan").text("");
    		//提示字体颜色修改
    		$(".errorSpan").css({ "color": "" });
    		//快递编号输入框边框颜色修改
    		$(obj).css({ "border-color": "" });
    	}
    }
//检查数据是否符合规范
function checkData(){
	//1.如果物流方式为快递
	var isKdSended=$("._kd").attr("checked")
	if(isKdSended=="checked"){
		//1.1 快递公司是否选择
		//1.2 物流单号是否符合规范
		var expressNo=$("input[name='expressNo']").val();
		//去掉空格
    	expressNo=$.trim(expressNo);
		if(isEmpty($('#expressCompanyId').val())){
			eDialog.info('快递公司未选择');
			return false;
		}else if(expressNo==""){
			eDialog.info('请输入快递单号');
			return false;
		}else if(expressNo.length<6||expressNo.length>32){
			eDialog.info('长度介于 6 和 32 之间的字符串');
			return false;
		}else{
			return true;
		}
	}
	//2.是否选择邮局挂号方式发货
	var isPySended=$("._py").attr("checked");
	if(isPySended=="checked"){
		//检验快递单号是否符合规范
		var expressNo=$("input[name='expressNo']").val();
		//去掉空格
    	expressNo=$.trim(expressNo);
		//检验快递单号是否为空
		if(expressNo==""){
			eDialog.info('请输入快递单号');
			return false;
		}else if(expressNo.length<6||expressNo.length>32){
			eDialog.info('长度介于 6 和 32 之间的字符串');
			return false;
		}else{
			return true;
		}
	}
	//3.是否选择自提方式发货
	var isZtSended=$("._zt").attr("checked");
	if(isZtSended=="checked"){
		return true;
	}
}