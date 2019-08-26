$(function(){
	var orderId = $('#orderId').val();
	var startdate = $("input[name='begDate']").val();
	var enddate = $("input[name='endDate']").val();
	$("#backMoney").keyup(function(){      
        $(this).val($(this).val().replace(/^\./g,"").replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3').replace(/[^\d.]/g,''));      
    }).bind("paste",function(){  
        $(this).val($(this).val().replace(/^\./g,"").replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3').replace(/[^\d.]/g,''));       
    }); 
	
	$("#dataGridTable").initDT({ 
		'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false, 
        'pCheckRow' : false,
        'pIdColumn' : 'id',
        'pLengthMenu' : [25,10,50,100],
        "sAjaxSource": GLOBAL.WEBROOT + '/refundReview/gridordlist',
        'params':[
                  {name:"begDate",value:$("input[name='begDate']").val()},
                  {name:"endDate",value:$("input[name='endDate']").val()},
                  {name:"orderId",value:$("input[name='orderId']").val()}
        ],
        //指定列数据位置
        "aoColumns": [
			{ "mData": "id", "sTitle":"订单编号", "sClass":"center","bSortable":false, "mRender":function(data,type,row){
				return '<a href="javascript:void(0)" onclick="oUtil.getOrdDetail(\''+data+'\')">'+data+'</a>';
			}},
			{ "mData": "orderTime", "sTitle":"下单日期", "sClass":"center", "bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "payTime", "sTitle":"支付时间", "sClass":"center", "bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},			
			{ "mData": "realMoney", "sTitle":"实付金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "status", "sTitle":"订单状态",  "sClass":"center","bSortable":false, "mRender": function(data,type,row){
				var val = oUtil.constant[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
			{ "mData": "contactPhone", "sTitle":"手机号码", "sClass":"center", "bSortable":false},			
			{ "mData": "contactName", "sTitle":"收货人姓名", "sClass":"center", "bSortable":false},
			{ "mData": "payType", "sTitle":"支付方式", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				var val = oUtil.constant.pay[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
			{ "mData": "payWay", "sTitle":"支付通道", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				var val = oUtil.constant.payWay[data];
				if(val == null){
					val = '';
					return val;
				}
				return val;
			}},
			{ "mData": "staffName", "sTitle":"会员名", "sClass":"center", "bSortable":false},
			{ "mData": "shopName", "sTitle":"商铺名称",  "sClass":"center","bSortable":false},
			{ "mData": "id", "sTitle":"操作", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				if(!row.hasBack){
					var val = "<a href='#' onclick=\"selectOrder(\'"+row.id+"\',\'"+row.realMoney+"\',\'"+row.staffName+"\')\" >选择</a>";
					return val;
				}else{
					return "";
				}
			}},
        ],
        "eDbClick" : function(){
        	
        }
	});
	
	$('#btnFormBaseSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	$('#btnFormReset').click(function(){
		 $("input[name='begDate']").val(startdate);
    	 $("input[name='endDate']").val(enddate);
    	 $("input[name='orderId']").val('');
	});
	$('#btn_submit').click(function(){
		if (!$("#submitForm").valid()){
			return false;
		}
		var orderId = $('#orderId').val();
		var backMoney = $('#backMoney').val();
		var memo = $('#memo').val();
		var realMoney = $('#realMoney').val();
		//保留2位小数，避免出现问题
		backMoney = toDecimal(backMoney*100);
		realMoney = toDecimal(realMoney);

		if(realMoney<backMoney){
			eDialog.alert('退款金额不能大于实付金额！');
			return;
		}
		if(orderId==null||orderId==''){
			return;
		}
		if(backMoney==null||backMoney==''){
			return;
		}
		if(memo==null||memo==''){
			return;
		}
		var data = [];
		data.push({name:'orderId',value:orderId},
				{name:'backMoney',value:backMoney},
				{name:'memo',value:memo}
				);
		var url =  GLOBAL.WEBROOT + '/refundReview/savecompenstate';
		$.eAjax({
			url : url,
			data :data,
			async : false,
			type : "post",
			dataType : "json",
			success : function(result) {
				$.gridUnLoading();
				if(result.resultFlag == "ok"){  //不跳页面
					//刷新退款列表页面
					window.location.href=GLOBAL.WEBROOT + '/refundReview/refund1';
				}else{  //跳页面
					eDialog.alert(result.message);
					return;
				}
			},
			failure:function(){
			}
		});
	});
	$('#btn_return').click(function(){
		window.location.href=GLOBAL.WEBROOT + '/refundReview/refund1';
	});
})

  //功能：将浮点数四舍五入，取小数点后2位 
function toDecimal(x) { 
   var f = parseFloat(x); 
   if (isNaN(f)) { 
    return; 
   } 
   f = Math.round(x*100)/100; 
   return f; 
} 

function selectOrder(orderId,realMoney,staffName){
	$('#orderId').val(orderId);
	$('#orderIdspan').html(orderId);
	$('#realMoney').val(realMoney);
	$('#realMoneyspan').html(realMoney/100);
	$('#staffName').val(staffName);
	$('#staffNamespan').html(staffName);
}
