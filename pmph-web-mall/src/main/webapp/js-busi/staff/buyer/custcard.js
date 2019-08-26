$(function(){
	//阅读协议后，确定提交
	$('#submit').click(function(){
		window.location.href = GLOBAL.WEBROOT+'/companysign/accompany';
	});

	$('#applysubmit').click(function(){
		if(!$("#cardapplyform").valid()) return false;
		var val = ebcForm.formParams($("#cardapplyform"));
		$(this).button('loading');

		eDialog.confirm("您确定提交该会员卡申请么？", {
			buttons : [{
				caption : '确认',
				callback : function(){	
					$.eAjax({
						url : GLOBAL.WEBROOT + "/custcard/saveapply",
						data : val,
						datatype:'json',
						success : function(returnInfo) {
							$('#applysubmit').button('reset');
							eDialog.alert("本次会员卡申请成功，请耐心等待审核！", function(){
								window.location.href = GLOBAL.WEBROOT+'/custcard/index';
							});						
						},
						exception:function(){
							$('#applysubmit').button('reset');
						}
					});
					
				}
			},{
				caption : '取消',
				callback : $.noop
			}]
		});
	});

	$('#cardbindsubmit').click(function(){
		if(!$("#cardbindform").valid()) return false;
//		var val = ebcForm.formParams($("#cardbindform"));
		var cust_card_id = $('#cust_card_id').val();
		eDialog.confirm("请确定要绑定该会员卡？绑定成功后，会员等级将更改为该会员卡所对应的等级！", {
			buttons : [{
				caption : '确认',
				callback : function(){	
					$.eAjax({
						url : GLOBAL.WEBROOT + "/custcard/bindcard",
						data : {'cardid':cust_card_id},
						datatype:'json',
						success : function(returnInfo) {
							if(null != returnInfo && 'ok' == returnInfo.resultFlag)
							{
								eDialog.alert(returnInfo.resultMsg, function(){
									window.location.href = GLOBAL.WEBROOT+'/custcard/index';
								});
							}
							else
							{
								eDialog.alert("系统异常!");
							}
						}
					});
					
				}
			},{
				caption : '取消',
				callback : $.noop
			}]
		});
	});	
});