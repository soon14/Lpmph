$(function(){
	$("#adjustDataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn':false,
        "sAjaxSource": GLOBAL.WEBROOT + '/shopmgr/shopAcctDetail/adjustlist',
        'params':[
                  {name:"adjBegDate",value:$("input[name='adjBegDate']").val()},
                  {name:"adjEndDate",value:$("input[name='adjEndDate']").val()},
                  {name:"adjShopId",value:$("#adjShopId").val()}
        ],
        //指定列数据位置
        "aoColumns": [
            { "mData": "id", "sTitle":"调账流水号","sWidth":"80px","bSortable":false},  
            { "mData": "optType", "sTitle":"调账类型","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if(data == '31'){
                	return "调账收入";
                } else {
                	return "调账支出";
                }
	         }},    
	        { "mData": "adjId", "sTitle":"调账申请ID","sWidth":"80px","bSortable":false},
			{ "mData": "adjTime", "sTitle":"调账时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "billDay", "sTitle":"调账结算日","sWidth":"80px","bSortable":false},
			{ "mData": "billMonth", "sTitle":"调账结算月","sWidth":"80px","bSortable":false},
			{ "mData": "adjMoney", "sTitle":"调账金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "applyStaffCode", "sTitle":"申请人","sWidth":"80px","bSortable":false},
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	$('#btnFormSearchAdjust').click(function(){
		if(!$("#shopacctAdjust").valid()) return false;
		var p = ebcForm.formParams($("#shopacctAdjust"));
		$('#adjustDataGridTable').gridSearch(p);
	});
	
	$('#btnFormResetAdjust').click(function(){
		ebcForm.resetForm('#shopacctAdjust');
	});
	
	$('#btn_excel_adjust_export').click(function(){
		if(!$("#shopacctAdjust").valid()) return false;
        $("#shopacctAdjust").submit();
	});
});
