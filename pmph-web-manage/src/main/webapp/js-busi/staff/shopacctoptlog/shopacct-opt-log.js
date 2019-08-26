$(function(){
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn':false,
        "sAjaxSource": GLOBAL.WEBROOT + '/shopmgr/shopAcctOptLog/optLoglist',
        'params':[
                  {name:"begDate",value:$("input[name='begDate']").val()},
                  {name:"endDate",value:$("input[name='endDate']").val()},
                  {name:"shopId",value:$("#shopId").val()}
        ],
        //指定列数据位置
        "aoColumns": [
            { "mData": "id", "sTitle":"交易流水号","sWidth":"80px","bSortable":false},  
            { "mData": "detailId", "sTitle":"收支流水号","sWidth":"80px","bSortable":false},
	        { "mData": "orderId", "sTitle":"订单编号","sWidth":"80px","bSortable":false},
			{ "mData": "payTranNo", "sTitle":"商户订单号","sWidth":"80px","bSortable":false},
			{ "mData": "createTime", "sTitle":"发生时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "shopName", "sTitle":"店铺名称","sWidth":"80px","bSortable":false},  
			{ "mData": "inMoney", "sTitle":"收入金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				if(data == 0 || data == null){
					return '';
				}
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "outMoney", "sTitle":"支出金额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				if(data == 0 || data == null){
					return '';
				}
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "acctBalance", "sTitle":"店铺账户余额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "payWay", "sTitle":"支付通道", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				var val = oUtil.constant.payWay[data];
				if(val == null){
					val = '';
					return val;
				}
				return val;
			}},
			{ "mData": "optType", "sTitle":"操作类型","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				var val = acctUtil.constant.acctOptType[data];
				if(val == null){
					val = '';
					return val;
				}
				return val;
	         }},  
			
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	
	$('#btn_excel_export').click(function(){
//		var p = ebcForm.formParams($("#searchForm"));
		//导出限制
//        p.push({name:'pageNo',value:1});
//        p.push({name:'pageSize',value:10000});
        //导出限制
		if(!$("#searchForm").valid()) return false;
        $("#searchForm").submit();
		
	});
});

