var withdraw = {
		confirmReview:function(id){
			var $form = $("#withdraw-check");
			$form.attr("action",GLOBAL.WEBROOT + '/withdrawReview/checkDetail/');
			$form.attr("method","post");
			var contentHTML="";
			contentHTML = "<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">";
			$form.html(contentHTML);
			$form.submit();
		},
		detail:function(id){
			var url=GLOBAL.WEBROOT + '/shopmgr/shopAcctWithdraw/withdrawApplyDetail?applyId='+id;
			window.open(url);
		}
}
$(function(){
	//表格数据初始化
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/withdrawReview/queryWithdraw',
        'params':[
              {name:"shopId",value:$("select[name='shopId']").attr("selected",true).val()},
	          {name:"begDate",value:$("input[name='begDate']").val()},
	          {name:"endDate",value:$("input[name='endDate']").val()},
	          {name:"tabFlag",value:"01"}
        ],
        //指定列数据位置
        "aoColumns": [
                        { "mData": "id", "sTitle":"提现申请编码", "sClass":"center" ,"bSortable":false, "mRender": function(data,type,row){
				        	return data;
				        }},
				        { "mData": "money", "sTitle":"提现金额", "sClass":"center","bSortable":false, "mRender": function(data,type,row){
				        	if(data == null){
								return '';
							}
							return ebcUtils.numFormat(data/100, 2);
						}},
						{ "mData": "applyTime", "sTitle":"申请时间", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
							return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
						}},
						{ "mData": "billMonths", "sTitle":"结算账期", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
							return data;
						}},
						{ "mData": "shopName", "sTitle":"店铺名称", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
							return data;
						}},
						{ "mData": "applyName", "sTitle":"申请人", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
							return data;
						}},
						{ "mData": "applyDesc", "sTitle":"申请人备注", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
							return data;
						}},
						{ "mData": "status", "sTitle":"申请单状态", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
							if(data == "00" ){
								return "待一级审核";
							} else if(data == "01"){
								return "待二级审核";
							} else {
								return "";
							}
						}},
        			  { "mData": "id", "sTitle":"操作", "sWidth":"150px",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
        					var val = '';
       						val = '<a class="_send" href="javascript:void(0)" onclick="withdraw.confirmReview(\''+data+'\')">审核</a> <span>|</span>\
    						<a class="_send" href="javascript:void(0)" onclick="withdraw.detail(\''+data+'\')">查看详情</a>';
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
	//点击待处理页签
	$('#myTab a').click(function (e) {
		$('#btnFormSearch').trigger('click');
	});
});