$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);
	var _shopId = _param.shopId;
	$("#shopId").val(_shopId);
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pCheckColumn':false,
        'pSingleCheckClean' : true,
        'pCheck' : 'single',
        "sAjaxSource": GLOBAL.WEBROOT + '/gift/gridskulist',
        //指定列数据位置
        "aoColumns": [
			{ "mData": "id", "sTitle":"单品编码","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "gdsName", "sTitle":"单品名称","sWidth":"80px","sClass": "center",
				"mRender": function(data,type,row){
					if(row.skuProps == null || row.skuProps == ""){
					return row.gdsName;
					}else{
					return row.gdsName+"（"+row.skuProps+"）";
					}
				}
			},
			{ "mData": "isbn", "sTitle":"ISBN号","sWidth":"80px","sClass": "center","bSortable":false},
			{"targets": -1,"mData": "gdsId","sTitle":"操作","sWidth":"80px","sClass": "center",
				"mRender": function(data,type,row){
					return "<a href='javascript:void(0);' onclick=\"checkSku(this,'"+data+"')\">选择</a>";
				}	
			}
        ],
        "params" : [{
			name : 'shopId',
			value : $("#shopId").val()
		}],
		"createdRow": function ( row, data, index ) {
			$('td', row).eq(1).css('word-break',"break-all");
        }
	});	
	//查询
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		p.push({ 'name': 'shopId','value' : $("#shopId").val() });
		$('#dataGridTable').gridSearch(p);
	});
	//重置
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
});
function checkSku(obj,gdsId){
	SkuGrid.checkSku(obj,gdsId);
}
var SkuGrid = {
		checkSku : function(obj,gdsId){
			var skuId = $(obj).parent().siblings().eq(0).text(); 
			var skuName =  $(obj).parent().siblings().eq(1).text(); 
			bDialog.closeCurrent({
				'gdsId' : gdsId,
				'skuId' : skuId,
				'skuName' : skuName
			});
		}
};
