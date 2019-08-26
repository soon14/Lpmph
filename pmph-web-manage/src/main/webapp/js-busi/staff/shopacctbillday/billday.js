var shopAcctBillDay = {
		finish:function(shopId,billDay){
			var data = [];
			data.push({name:'shopId',value:shopId},
					{name:'billDay',value:billDay}
					);
			$.eAjax({
				url : GLOBAL.WEBROOT + '/shopmgr/shopAcctBillDay/finish',
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
						eDialog.alert(result.resultMsg,function(){
						},'error');
					}
				},
				failure:function(){
					bDialog.closeCurrent();
				}
			});
		},
		applyWdaj:function(shopId,billDay){
			var url=GLOBAL.WEBROOT + '/shopAcctAdjust/index?shopId='+shopId+'&billDay='+billDay;
			window.location.href=url;
		},
		detail:function(shopId,billDay){
			var url=GLOBAL.WEBROOT + '/shopmgr/shopAcctBillDay/detail?shopId='+shopId+'&billDay='+billDay;
			window.open(url);
		}
}
$(function(){
	//表格数据初始化
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/shopmgr/shopAcctBillDay/gridlist',
        'params':[
              {name:"shopId",value:$("select[name='shopId']").attr("selected",true).val()},
	          {name:"begDate",value:$("input[name='begDate']").val()},
	          {name:"endDate",value:$("input[name='endDate']").val()}
        ],
        //指定列数据位置
        "aoColumns": [
                      { "mData": "billDay", "sTitle":"结算日期", "sClass":"center", "bSortable":false},
                      { "mData": "shopId", "sTitle":"店铺编码", "sClass":"center", "bSortable":false},
                      { "mData": "shopName", "sTitle":"店铺名称", "sClass":"center", "bSortable":false},
                      { "mData": "dayRealMoney", "sTitle":"订单实洋", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayPayFee", "sTitle":"支付费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayServFee", "sTitle":"服务费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayInFee", "sTitle":"手续费", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "inMoney", "sTitle":"日结收入", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayBackMoney", "sTitle":"退款总额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayBackPayfee", "sTitle":"退款支付费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayBackServfee", "sTitle":"退款服务费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayBackBackfee", "sTitle":"退款费用", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "dayBackFee", "sTitle":"退款手续费", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "backMoney", "sTitle":"退款支出", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "adjMoney", "sTitle":"日结调账", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "money", "sTitle":"日结入账", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "withdrawMoney", "sTitle":"日结提现", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
          				  return ebcUtils.numFormat(data/100, 2);
          			  }},
          			  { "mData": "status", "sTitle":"状态",  "sClass":"center","bSortable":false, "mRender": function(data,type,row){
        				if(data == "0" ){
        					return "未对账";
        				} else if(data == "1"){
        					return "已对账";
        				} else {
        					return "";
        				}
        			  }},
        			  { "mData": "shopId", "sTitle":"操作", "sWidth":"150px",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
        					var val = '';
//        					0表示未对账1表示已对账
        					if(row.status =="0"){
        						val = '<a class="_send" href="javascript:void(0)" onclick="shopAcctBillDay.finish(\''+data+'\',\''+row.billDay+'\')">完成对账</a> <span>|</span>\
        						<a class="_send" href="javascript:void(0)" onclick="shopAcctBillDay.applyWdaj(\''+data+'\',\''+row.billDay+'\')">申请调账</a> <span>|</span>\
        						<a class="_send" href="javascript:void(0)" onclick="shopAcctBillDay.detail(\''+data+'\',\''+row.billDay+'\')">查看明细</a>';
        					}else if(row.status =="1"){
        						val = '<a class="_send" href="javascript:void(0)" onclick="shopAcctBillDay.detail(\''+data+'\',\''+row.billDay+'\')">查看明细</a>';
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
		if(!$("#searchForm").valid())return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	//重置
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});

});