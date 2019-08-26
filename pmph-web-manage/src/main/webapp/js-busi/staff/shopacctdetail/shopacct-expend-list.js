$(function(){
	$("#expendDataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn':false,
        "sAjaxSource": GLOBAL.WEBROOT + '/shopmgr/shopAcctDetail/expendlist',
        'params':[
                  {name:"backBegDate",value:$("input[name='backBegDate']").val()},
                  {name:"backEndDate",value:$("input[name='backEndDate']").val()},
                  {name:"backShopId",value:$("#backShopId").val()}
        ],
        //指定列数据位置
        "aoColumns": [
            { "mData": "id", "sTitle":"支出流水","sWidth":"80px","bSortable":false},  
            { "mData": "optType", "sTitle":"支出类型","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if(data == '21'){
                	return "退款支出";
                } else {
                	return "退货支出";
                }
	         }},    
	         { "mData": "backId", "sTitle":"退款编号","sWidth":"80px","bSortable":false},
	        { "mData": "orderId", "sTitle":"订单编号","sWidth":"80px","bSortable":false},
			{ "mData": "payTranNo", "sTitle":"商户订单号","sWidth":"80px","bSortable":false},
			{ "mData": "backTime", "sTitle":"退款时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "payWay", "sTitle":"退款通道", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				if(data == '2000'){
					return "线下退款";
				}
				var val = oUtil.constant.payWay[data];
				if(val == null){
					val = '';
					return val;
				}
				return val;
			}},
			{ "mData": "payType", "sTitle":"退款方式", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				if(data == '0'){
					return "线下退款";
				}else{
					return "线上退款";
				}
			}},
			{ "mData": "backMoney", "sTitle":"退款金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "fee", "sTitle":"手续费", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "expendMoney", "sTitle":"支出金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "applyStaffCode", "sTitle":"退款人","sWidth":"80px","bSortable":false},
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	$('#btnFormSearchExpend').click(function(){
		if(!$("#shopacctExpend").valid()) return false;
		var p = ebcForm.formParams($("#shopacctExpend"));
		$('#expendDataGridTable').gridSearch(p);
	});
	
	$('#btnFormResetExpend').click(function(){
		ebcForm.resetForm('#shopacctExpend');
	});
	
	$('#btn_excel_expend_export').click(function(){
		if(!$("#shopacctExpend").valid()) return false;
        $("#shopacctExpend").submit();
		
	});
});
