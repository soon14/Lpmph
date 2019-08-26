var poUtil = {
	pubOrderStatus:{
		'01':'待支付',
		'02':'待发货',
		'04':'部分发货',
		'05':'全部发货',
		'99':'取消'
	}
};
var pubOrderFunction = {
	markedPayMent:function(pubOrderId){
		bDialog.open({
		    title : '备注',
		    width : 400,
		    height : 300,
		    scroll : true,
		    onShowed : function(_this){
		    	var dlg = $(_this);
				$('#reivewForm_pubOrderId',$(dlg)).val(pubOrderId);
				bValidate.clearValidate($('#reviewForm',$(dlg)));
				bValidate.labelValidate($('#reviewForm',$(dlg)));
		    },
		    callback:function(data){
		    	if(data && data.results && data.results.length > 0 ){
		    		$("#btnFormSearch").trigger("click");
		    	}
		    }
		},$('#reviewFormDiv'));

		//清空内容
		$('#checkCont').val('');
	}
}
$(function(){
	//汇总信息查询
	pubOrderSum();
	
	$("#dataGridTable").initDT({ 
		'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false, 
        "sAjaxSource": GLOBAL.WEBROOT + '/pub/order/gridlist',
        'params':[
                  {name:"begDate",value:$("input[name='begDate']").val()},
                  {name:"endDate",value:$("input[name='endDate']").val()},
                  {name:"pubStatus",value:$("input[name='pubStatus']").val()}
        ],
        //指定列数据位置
        "aoColumns": [
            { "mData": "id", "sTitle":"征订单号", "sClass":"center", "bSortable":false},
            { "mData": "orderTime", "sTitle":"下单日期", "sClass":"center", "bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "realMoney", "sTitle":"征订单总额", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},   
			{ "mData": "discountMoney", "sTitle":"优惠", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}}, 
			{ "mData": "realExpressFee", "sTitle":"运费", "sClass":"center", "bSortable":false, "mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "status", "sTitle":"征订单状态",  "sClass":"center","bSortable":false, "mRender": function(data,type,row){
				var val = poUtil.pubOrderStatus[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
			{ "mData": "pubCode", "sTitle":"公众号名", "sClass":"center", "bSortable":false},       
			{ "mData": "staffCode", "sTitle":"会员名", "sClass":"center", "bSortable":false},
			{ "mData": "staffName", "sTitle":"联系人", "sClass":"center", "bSortable":false},
			{ "mData": "resName", "sTitle":"负责人", "sClass":"center", "bSortable":false},
			{ "mData": "id", "sTitle":"操作","sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = '';
				if(row.status =="01"){
					val = '<a class="_send" href="javascript:void(0)" onclick="pubOrderFunction.markedPayMent(\''+data+'\')">标记为已付款</a>';
				} 
				return val;
			}}
                      
        ],
        "eDbClick" : function(){
        	
        }
	});
	
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
		pubOrderSum();
		
	});
	
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	
	function pubOrderSum(){
		$.eAjax({
			url : GLOBAL.WEBROOT + '/pub/order/pubOrdersum',
			data :ebcForm.formParams($("#searchForm")),
			async : true,
			type : "post",
			dataType : "json",
			success : function(datas) {
				$("#sumPubOrderMoney").text(ebcUtils.numFormat(accDiv(datas.sumPubOrderMoney,100),2));
				$("#sumRealMoney").text(ebcUtils.numFormat(accDiv(datas.sumRealMoney,100),2));
				$("#pubOrderCount").text(datas.pubOrderCount);
				$("#payedCount").text(datas.payedCount);
				if(datas.pubOrderCount == 0){
					$("#payedRate").text("0.00");
				} else {
					$("#payedRate").text(ebcUtils.numFormat(accMul(accDiv(datas.payedCount,datas.pubOrderCount),100),2));
				}
			}
		});
	};
	
	$('body').delegate('div.dialogInActive #submit', 'click', function(e) {
		var dlg = bDialog.getDialog();
		var _this = this;
		if($(_this).hasClass('disabled')){
			return false;
		}

		if(!$('#reviewForm',$(dlg)).valid()) return false;

		var data = ebcForm.formParams($('#reviewForm',$(dlg)));

		$(_this).addClass('disabled');
		$.eAjax({
			url:GLOBAL.WEBROOT+'/pub/order/offlinesave',
			data:data,
			success:function(result){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent({result:'ok'});
				if(result&&result.resultFlag=='ok'){
					eDialog.alert('标记成功',function(){
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
						window.location.href = GLOBAL.WEBROOT+'/pub/order/managegrid';
					},'error');
				}
			},
			failure:function(){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent();
			}
		});
	});
	$('body').delegate('div.dialogInActive #btnCancel1', 'click', function(e) {
		var _this = this;
		$(_this).removeClass('disabled');
		bDialog.closeCurrent();
	});
});