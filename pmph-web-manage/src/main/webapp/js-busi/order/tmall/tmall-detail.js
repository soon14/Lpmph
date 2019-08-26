$(function(){
	
	$("#tmallDetailGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false, //是否显示单选/复选框的列
        "sAjaxSource": GLOBAL.WEBROOT+'/order/tmall/tmallDetailList',
        "params":[{name:"orderId",value:$('#orderId').val()}],
        //指定列数据位置 
        "aoColumns": [
			{ "mData": "id", "sTitle":"订单编号","sClass": "center","bSortable":false},
			{ "mData": "externalSysCode", "sTitle":"制品编码","sClass": "center","bSortable":false},
			{ "mData": "mainCatgs", "sTitle":"主分类名称","sClass": "center","bSortable":false},
			{ "mData": "guidePrice", "sTitle":"商品定价","sClass": "center","bSortable":false, "mRender": function(data,type,row){
				if(data == null)
					data = 0;
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "orderAmount", "sTitle":"购买数量","sClass": "center","bSortable":false},
			{ "mData": "title", "sTitle":"购买制品名称","sClass": "center","bSortable":false},
			{ "mData": "orderPrice", "sTitle":"购买价格","sClass": "center","bSortable":false}
        ]
	});

});