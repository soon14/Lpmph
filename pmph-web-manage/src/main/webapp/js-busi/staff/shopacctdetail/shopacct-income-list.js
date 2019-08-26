$(function(){
	$("#incomeDataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn':false,
        "sAjaxSource": GLOBAL.WEBROOT + '/shopmgr/shopAcctDetail/incomelist',
        'params':[
                  {name:"payBegDate",value:$("input[name='payBegDate']").val()},
                  {name:"payEndDate",value:$("input[name='payEndDate']").val()},
                  {name:"inShopId",value:$("#inShopId").val()}
        ],
        //指定列数据位置
        "aoColumns": [
            { "mData": "id", "sTitle":"收入流水","sWidth":"80px","bSortable":false},  
            { "mData": "optType", "sTitle":"收入类型","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if(data == '11'){
                	return "在线支付";
                } else {
                	return "线下支付审核";
                }
	         }},    
	        { "mData": "orderId", "sTitle":"订单编号","sWidth":"80px","bSortable":false},
			{ "mData": "payTranNo", "sTitle":"商户订单号","sWidth":"80px","bSortable":false},
			{ "mData": "payTime", "sTitle":"支付时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
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
			{ "mData": "realMoney", "sTitle":"实付金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "fee", "sTitle":"手续费", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "inMoney", "sTitle":"入账金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "orderStaffCode", "sTitle":"下单人","sWidth":"80px","bSortable":false},
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	$('#btnFormSearchIncome').click(function(){
		if(!$("#shopacctIncome").valid()) return false;
		var p = ebcForm.formParams($("#shopacctIncome"));
		$('#incomeDataGridTable').gridSearch(p);
	});
	
	$('#bindBtnFormReset').click(function(){
		ebcForm.resetForm('#shopacctIncome');
	});
	
	$('#btn_excel_income_export').click(function(){
		if(!$("#shopacctIncome").valid()) return false;
        $("#shopacctIncome").submit();
		
	});
});
