$(function(){
	$("#money").keyup(function(){      
        $(this).val($(this).val().replace(/^\./g,"").replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3').replace(/[^\d.-]/g,''));      
    }).bind("paste",function(){  
        $(this).val($(this).val().replace(/^\./g,"").replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3').replace(/[^\d.-]/g,''));       
    }); 
	$('#btnFormSave').click(function(){
		var money = $("#money").val();
		//保留2位小数，避免出现问题
		money = toDecimal(money*100);
/*		if($("#allMoney").val()!=0){
			var allMoney = $("#allMoney").val();
			allMoney = toDecimal(allMoney*100);
		}else{
			var allMoney = 0;
		}
		var moneys = money+allMoney;*/
		if(!$("#detailInfoForm").valid()) return false;
		if(money==0){
			eDialog.alert('调账金额不能为0!');
			return;
		}
/*		if(moneys<0){
				eDialog.alert('调账金额不能大于账户总额!');
				return;
		}*/
		//debugger;
		/*var param = ebcForm.formParams($("#detailInfoForm"));*/
		var param = {
					"shopId":$("#shopId").val(),
				 	"billDay":$("#billDayVal").val(),
				 	"money":money,
				 	"applyDesc":$("#applyDesc").val()
					};
		$.eAjax({
			url : GLOBAL.WEBROOT + "/shopAcctAdjust/save",
			data : param,	
			datatype:'json',
			success : function(data) {
				if(data.resultFlag=="ok"){
					eDialog.success('申请成功！',{
						buttons:[{
							caption:"确定",
							callback:function(){
								var url=GLOBAL.WEBROOT + '/shopmgr/shopAcctBillDay/index';
								window.location.href=url;
					        }
						}]
					});
				}
				if(data.resultFlag=="fail"){
					eDialog.success('已在调账流程中',{
						buttons:[{
							caption:"确定",
							callback:function(){
/*								window.location.href = GLOBAL.WEBROOT+'/shopAcctAdjust/index';
*/							}
						}]
					});
				} 
			}
		});
	});
	$('#btnReturn').click(function(){
		var url=GLOBAL.WEBROOT + '/shopmgr/shopAcctBillDay/index';
		window.location.href=url;
	});
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