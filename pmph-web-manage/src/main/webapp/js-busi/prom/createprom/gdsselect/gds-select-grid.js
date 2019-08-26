$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	//var _callback = bDialog.getDialogCallback(_dlg);
	
	$('#btn_code_add_gdsList').unbind('click').click(function(){
		add();
	});
	
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheck' : 'multi',
        "sAjaxSource": GLOBAL.WEBROOT + '/gdsprom/gdsgrid',
        'params' : [{name:'shopId',value:$('#shopId').val()},{name:'gdsStatus',value:$('#gdsStatus').val()}],
        //指定列数据位置
        "aoColumns": [
			{ "mData": "id", "sTitle":"编码","sWidth":"60px","bSortable":false},
			{ "mData": "gdsName", "sTitle":"名称","sWidth":"100px","bSortable":false},
			{ "mData": "mainCatgName", "sTitle":"分类","sWidth":"60px","bSortable":false},
			{ "mData": "isbn", "sTitle":"ISBN","sWidth":"50px","bSortable":false},
			{ "mData": "gdsStatusName", "sTitle":"状态","sWidth":"50px","bSortable":false},
			{ "mData": "skuProps", "sTitle":"属性","sWidth":"100px","bSortable":false},
			{ "mData": "guidePrice", "sTitle":"参考价格","sWidth":"50px","bSortable":false,"mRender": function(data,type,row){
				var str = (data/100).toFixed(2) + '';
				var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
				var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
				var ret = intSum + dot;
				return ret;
			}},
			{ "mData": "realPrice", "sTitle":"价格","sWidth":"50px","bSortable":false,"mRender": function(data,type,row){
				var str = (data/100).toFixed(2) + '';
				var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
				var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
				var ret = intSum + dot;
				return ret;
			}}
        ],
        "eDbClick" : function(){
        	add();
        }
	});
	//添加
	var add=function(){
		
		var ids = $('#dataGridTable').getCheckIds();
		
		if(ids && ids.length>=1){
			var _val = $('#dataGridTable').getSelectedData();
			var _idsArr=new Array();
		    for(var i=0;i<_val.length;i++){
		    	_idsArr.push(_val[i].id);
		    }
			var parm=new Object();
			parm._if_query="1";
			parm.skuIds=_idsArr;
			//bDialog.getDialogCallback(_dlg);
			bDialog.closeCurrent(parm);
			
		}else if(!ids || ids.length==0){
			eDialog.alert('请至少选择一个商品进行操作！');
		}
	};
	//查询
	$('#btnFormSearch').click(function(){
		if(!$("#searchGdsForm").valid()) return false;
		var p = ebcForm.formParams($("#searchGdsForm"));
		$('#dataGridTable').gridSearch(p);
	});
	//重置
	$('#btnFormReset').click(function(){
		//ebcForm.resetForm('#searchGdsForm');
		$('#gdsName').val('');
		$('#gdsStatus').val('');
		$('#platCatgs').val('');
		$('#mainCatgsName').val('');
		$('#isbn').val('');
		
	});
	//关闭
	$('#btnReturn').click(function(){
		bDialog.closeCurrent();
	});
	

});