var tmall_grid = { 
		getTmallOrdDetail: function(orderId){
			bDialog.open({
				title : '订单明细',
				width : 900,
				height : 300,
				params : {
					'orderId':orderId,
					},
				url : GLOBAL.WEBROOT+'/order/tmall/tmallDetailGrid?orderId='+orderId,
				callback:function(data){
					$('#dataGridTable').gridReload();
				}
			});
		}
};
$(function(){ 
	$("#dataGridTable").initDT({ 
		'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false, 
        'pCheckRow' : false,
        'pIdColumn' : 'orderId',
        "sAjaxSource": GLOBAL.WEBROOT + '/order/tmall/tmallList',
        'params':[
                  {name:"begDate",value:$("input[name='begDate']").val()},
                  {name:"endDate",value:$("input[name='endDate']").val()},
                  {name:"categoryCode",value:$("input[name='categoryCode']").val()}
        ],
        //指定列数据位置
        "aoColumns": [
			{ "mData": "orderCode", "sTitle":"订单编号", "sClass":"center","bSortable":false},
			{ "mData": "tmStaffCode", "sTitle":"淘宝会员号", "sClass":"center", "bSortable":false},
			{ "mData": "alipayAccount", "sTitle":"买家支付宝账号", "sClass":"center", "bSortable":false},
			{ "mData": "rwStaffCode", "sTitle":"商城会员号",  "sClass":"center","bSortable":false},
			{ "mData": "sumMoney", "sTitle":"总金额", "sClass":"center", "bSortable":false},
			{ "mData": "status", "sTitle":"订单状态",  "sClass":"center","bSortable":false},
			{ "mData": "orderTime", "sTitle":"订单创建时间", "sClass":"center", "bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "contractName", "sTitle":"收货人姓名", "sClass":"center", "bSortable":false},
			{ "mData": "contractAddr", "sTitle":"收货人地址", "sClass":"center", "bSortable":false},
			{ "mData": "contractNum", "sTitle":"联系手机", "sClass":"center", "bSortable":false},
			{ "mData": "orderAmount", "sTitle":"宝贝总数量", "sClass":"center", "bSortable":false},
			{ "mData": "rwScore", "sTitle":"赠送积分数", "sClass":"center", "bSortable":false},
			{ "mData": "importTime", "sTitle":"订单导入时间", "sClass":"center", "bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "orderCode", "sTitle":"操作","bSortable":false, "sClass":"center", "mRender": function(data,type,row){
				var str='<a href="javascript:void(0)" onclick="tmall_grid.getTmallOrdDetail(\''+row.orderCode+'\')">明细</a>';
				return str;
			}}
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	var modifyBiz = function(){
		var ids = $('#dataGridTable').getCheckIds();
		if(ids && ids.length==1){
			window.location.href = GLOBAL.WEBROOT+'/order/edit';
		}else if(ids && ids.length>1){
			eDialog.alert('只能选择一个项目进行操作！');
		}else if(!ids || ids.length==0){
			eDialog.alert('请选择至少选择一个项目进行操作！');
		}
	};
	
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
		
	});
	
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	
	$('#btnImport').click(function(){
		bDialog.open({
			title : '天猫订单导入',
			width : 480,
			height : 250,
			params : {},
			url : GLOBAL.WEBROOT+'/order/tmall/ordImportPage',
			callback:function(data){
				$('#dataGridTable').gridReload();
			}
		});
	}); 
	 
});