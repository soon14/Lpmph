$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);
	var _gdsId = _param.gdsId;
	var _shopId = _param.shopId;
	$("#gdsId").val(_gdsId);
	$("#shopId").val(_shopId);
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pCheckColumn':false,
        'pSingleCheckClean' : true,
        'pCheck' : 'single',
        "sAjaxSource": GLOBAL.WEBROOT + '/gdsverify/gridlist?ignoreStatus=true',
        //指定列数据位置
        "aoColumns": [
			{ "mData": "gdsId", "sTitle":"商品编码","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "gdsName", "sTitle":"商品名称","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "catgName", "sTitle":"主分类名称","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "isbn", "sTitle":"ISBN","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "guidePrice", "sTitle":"商品指导价","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					var str = (data/100).toFixed(2) + '';
					var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
					var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
					var ret = intSum + dot;
					return ret;
				}
			},
			{ "mData": "operateStaffCode", "sTitle":"操作人","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "operateTime", "sTitle":"操作时间","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				}
			},
			{ "mData": "operateType", "sTitle":"操作类型","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					if(data=='11'){ 
						return "上架提交";
					}else if(data=='99'){
						return "删除提交";
					}
				}
			},
			{ "mData": "verifyStaffCode", "sTitle":"审核人","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "verifyTime", "sTitle":"审核时间","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				}
			},
			{ "mData": "verifyStatus", "sTitle":"审核状态","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					if(data=='00'){ 
						return "提交待初审";
					}else if(data=='01'){
						return "复审通过";
					}else if(data=='02'){
						return "复审拒绝";
					}else if(data=='03'){
						return "提交待复审";
					}else if(data=='04'){
						return "初审拒绝";
					}
				}
			},
			{ "mData": "verifyOption", "sTitle":"审核意见","sWidth":"80px","sClass": "center","bSortable":false},
        ],
        "params" : [{name : 'shopId',value :_shopId},{name : 'gdsId',value : _gdsId}]
	});	
	$('#btnReturn').click(function(){
		bDialog.closeCurrent();
	});
});
