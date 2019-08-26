var shopAcctBillMonth = {
		finish:function(shopId,billMonth){
			var data = [];
			data.push({name:'shopId',value:shopId},
					{name:'billMonth',value:billMonth}
					);
			$.eAjax({
				url : GLOBAL.WEBROOT + '/shopmgr/shopAcctBillMonth/finish',
				data :data,
				async : true,
				type : "post",
				dataType : "json",
				success : function(result) {
					if(result&&result.resultFlag=='ok'){
						bDialog.closeCurrent({result:'ok'});
						eDialog.alert('操作成功',function(){
							$("#btnFormSearch").trigger("click");
						},'confirmation');
					}else{
						eDialog.info(result.resultMsg);
					}
				},
				failure:function(){
					bDialog.closeCurrent();
				}
			});
		},
		detail:function(shopId,billMonth){
			var url=GLOBAL.WEBROOT + '/shopmgr/shopAcctBillMonth/detail?shopId='+shopId+'&billMonth='+billMonth;
			window.open(url);
		}
}
$(function(){
	//表格数据初始化
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/shopmgr/shopAcctBillMonth/gridlist',
        'params':[
              {name:"shopId",value:$("select[name='shopId']").attr("selected",true).val()},
	          {name:"begDate",value:$("input[name='begDate']").val()},
	          {name:"endDate",value:$("input[name='endDate']").val()}
        ],
        //指定列数据位置
        "aoColumns": [
                      { "mData": "billMonth", "sTitle":"结算月", "sClass":"center", "bSortable":false},
                      { "mData": "shopId", "sTitle":"店铺编码", "sClass":"center", "bSortable":false},
                      { "mData": "shopName", "sTitle":"店铺名称", "sClass":"center", "bSortable":false},
                      { "mData": "monRealMoney", "sTitle":"订单实洋", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monPayFee", "sTitle":"支付费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monServFee", "sTitle":"服务费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monInFee", "sTitle":"手续费", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "inMoney", "sTitle":"月结收入", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monBackMoney", "sTitle":"退款总额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monBackPayfee", "sTitle":"退款支付费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monBackServfee", "sTitle":"退款服务费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monBackBackfee", "sTitle":"退款费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "monBackFee", "sTitle":"退款手续费", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "backMoney", "sTitle":"退款支出", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "adjMoney", "sTitle":"月结调账", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "money", "sTitle":"月结入账", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "withdrawMoney", "sTitle":"月结提现", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "status", "sTitle":"状态",  "sClass":"center","bSortable":false, "mRender": function(data,type,row){
        				if(data == "0" ){
        					return "未对账";
        				} else if(data == "1"){
        					return "未提现";
        				} else if(data == "2"){
        					return "已提现";
        				} else if(data == "3"){
        					return "提现流程中";
        				}else {
        					return "";
        				}
        			  }},
        			  { "mData": "shopId", "sTitle":"操作", "sWidth":"150px",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
        					var val = '';
//        					0:未对账、1:未提现、2:已提现、3:提现申请流程中
        					if(row.status =="0"){
           						val = '<a class="_send" href="javascript:void(0)" onclick="shopAcctBillMonth.finish(\''+data+'\',\''+row.billMonth+'\')">完成对账</a> <span>|</span>\
        						<a class="_send" href="javascript:void(0)" onclick="shopAcctBillMonth.detail(\''+data+'\',\''+row.billMonth+'\')">查看明细</a>';
        					}else if(row.status =="1"||row.status =="2"||row.status =="3"){
        						val = '<a class="_send" href="javascript:void(0)" onclick="shopAcctBillMonth.detail(\''+data+'\',\''+row.billMonth+'\')">查看明细</a>';
        					}else{
        						val = "";
        					}
        					return val;
        			  }}
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	//查询
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	//重置
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});

});