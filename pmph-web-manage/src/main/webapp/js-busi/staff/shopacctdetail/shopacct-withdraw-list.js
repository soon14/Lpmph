var withdrawApply = {
	detail:function(id){
		var url=GLOBAL.WEBROOT + '/shopmgr/shopAcctWithdraw/withdrawApplyDetail?applyId='+id;
		window.open(url);
	}
}
$(function(){
	$("#withdrawDataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn':false,
        "sAjaxSource": GLOBAL.WEBROOT + '/shopmgr/shopAcctDetail/withdrawlist',
        'params':[
                  {name:"withdrawBegDate",value:$("input[name='withdrawBegDate']").val()},
                  {name:"withdrawEndDate",value:$("input[name='withdrawEndDate']").val()},
                  {name:"withdrawShopId",value:$("#withdrawShopId").val()}
        ],
        //指定列数据位置
        "aoColumns": [
            { "mData": "id", "sTitle":"提现流水号","sWidth":"80px","bSortable":false},  
	        { "mData": "applyId", "sTitle":"提现申请ID","sWidth":"80px","bSortable":false},
			{ "mData": "applyTime", "sTitle":"申请时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "applyStaffCode", "sTitle":"申请人","sWidth":"80px","bSortable":false},
			{ "mData": "withdrawTime", "sTitle":"提现日期","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "withdrawMoney", "sTitle":"提现金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "applyId", "sTitle":"操作","bSortable":false, "sClass":"center", "mRender": function(data,type,row){
				var str='<a href="javascript:void(0)" onclick="withdrawApply.detail(\''+row.applyId+'\')">查看详情</a>';
					str += '| <a href="javascript:void(0)" onclick="">打印</a>';
				return str;
			}}
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	$('#btnFormSearchWithdraw').click(function(){
		if(!$("#shopacctWithdraw").valid()) return false;
		var p = ebcForm.formParams($("#shopacctWithdraw"));
		$('#withdrawDataGridTable').gridSearch(p);
	});
	
	$('#btnFormResetWithdraw').click(function(){
		ebcForm.resetForm('#shopacctWithdraw');
	});
	
	$('#btn_excel_withdraw_export').click(function(){
		if(!$("#shopacctWithdraw").valid()) return false;
        $("#shopacctWithdraw").submit();
		
	});
});
