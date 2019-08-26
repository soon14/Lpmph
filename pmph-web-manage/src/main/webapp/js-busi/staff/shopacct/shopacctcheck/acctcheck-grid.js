var shopAcctAdjust = {
		finish:function(data,id){
			var $form = $("#backreviewForm");
			url=GLOBAL.WEBROOT + '/shopAcctAdjust/adjustReview?id='+id;
			window.location.href=url;
		},
		detail:function(data,id){
			var $form = $("#backreviewForm");
			url=GLOBAL.WEBROOT + '/shopAcctAdjust/backAdjust?id='+id;
			window.open(url);
		}
}
$(function(){
	var uncheck_url = GLOBAL.WEBROOT + '/shopAcctAdjust/gridList?status='+0;

	gridInitDT({url:uncheck_url,id:"uncheckGridTable",checkColumn:false});

	function gridInitDT(json){
		var url = json.url,id=json.id;
		var pCheckColumn = json.checkColumn;
		var aoColumns = [
			{ "mData": "id", "sTitle":"调账申请编码","bSortable":false, "sClass":"center"},
			{ "mData": "money", "sTitle":"调账金额","bSortable":false, "sClass":"center","mRender":function(data,type,row){
				return ebcUtils.numFormat(data/100,2);
			}},
			{ "mData": "applyTime", "sTitle":"申请时间","bSortable":false,"sClass":"center", "mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "billDay", "sTitle":"调账结算日", "sClass":"center", "bSortable":false},
			{ "mData": "billMonth", "sTitle":"调账结算月", "sClass":"center", "bSortable":false},
			{ "mData": "shopName", "sTitle":"店铺名称", "sClass":"center", "bSortable":false},
			{ "mData": "staffCode", "sTitle":"申请人", "sClass":"center", "bSortable":false},
			{ "mData": "applyDesc", "sTitle":"申请人备注", "sClass":"center", "bSortable":false},
			{ "mData": "status", "sTitle":"申请单状态", "sClass":"center", "bSortable":false},
			{ "mData": "", "sTitle":"操作", "sWidth":"150px",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = '';
				if(json.id == "uncheckGridTable"){
					val = '<a class="_send" href="javascript:void(0)" onclick="shopAcctAdjust.finish(\''+data+'\',\''+row.id+'\')">审核</a> <span>|</span>\
						<a class="_send" href="javascript:void(0)" onclick="shopAcctAdjust.detail(\''+data+'\',\''+row.id+'\')">查看明细</a>';
				}
				return val;
		  }}
		];
		$("#"+id).initDT({
			'pTableTools' : false,
			'pCheckRow' : false,
			'pCheckColumn' : pCheckColumn,
			'pSingleCheckClean' : false,
			'params':[{name:"shopId",value:$("#offline_grid_shopId").val()},
			          {name:"begDate",value:$("input[name='begDate']").val()},
			          {name:"endDate",value:$("input[name='endDate']").val()}
			          ],
			"sAjaxSource": url,
			//指定列数据位置
			"aoColumns": aoColumns,
			"eDbClick" : function(){
				//modifyBiz(json);
			}
		});
	}

	
	var modifyBiz = function(){
		var ids = $('#searchGridTable').getCheckIds();
	};
	
	$('#btnFormSearch').click(function(){
		if(!$('#searchForm').valid()) return false;
		var p = ebcForm.formParams($('#searchForm'));
		$(getTabId()+'GridTable').gridSearch(p);
	});
	
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});

	/**
	 * 获取当前tab页面的id
	 */
	function getTabId(){
		var type = $("#myTab>li.active").find("a").attr("href");
		var _id= type.replace('Tab','');
		return _id;
	}
	//去弹出label位置
	//$('#notcontrols').css('margin-left','0');
	$('#myTab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
		var id = getTabId();
		if(!$("#searchForm").valid()) return false;

		/*这个方法是ajax的，后台跳转的，自然和它无关了，难道这就叫ajax跨域吗*/
		/*带了tab之后就必须要获取当前是那个tab的数据*/
		var p = ebcForm.formParams($("#searchForm"));
		$(id+'GridTable').gridSearch(p);
	});

	$('body').delegate('div.dialogInActive #allow', 'click', function(e) {
		var dlg = bDialog.getDialog();
		var _this = this;
		if($(_this).hasClass('disabled')){
			return false;
		}

		if(!$('#reviewForm',$(dlg)).valid()) return false;

		var data = ebcForm.formParams($('#reviewForm',$(dlg)));

		data.push({name:'status',value:1});
		$(_this).addClass('disabled');
		$.eAjax({
			url:GLOBAL.WEBROOT+'/order/pay/offlinesave',
			data:data,
			success:function(result){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent({result:'ok'});
				if(result&&result.resultFlag=='ok'){
					eDialog.alert('审核成功',function(){
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
						window.location.href = GLOBAL.WEBROOT+'/order/pay/offlinegrid';
					},'error');
				}
			},
			failure:function(){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent();
			}
		});

	});

	$('body').delegate('div.dialogInActive #unallow', 'click', function(e) {
		var dlg = bDialog.getDialog();
		var _this = this;
		if($(_this).hasClass('disabled')){
			return false;
		}

		if(!$('#reviewForm',$(dlg)).valid()) return false;
		var data = ebcForm.formParams($('#reviewForm',$(dlg)));
		data.push({name:'status',value:2});
		$(_this).addClass('disabled');
		$.eAjax({
			url:GLOBAL.WEBROOT+'/order/pay/offlinesave',
			data:data,
			success:function(result){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent();
				if(result&&result.resultFlag=='ok'){

					eDialog.alert("审核不通过",function(){
						window.location.href = GLOBAL.WEBROOT+'/order/pay/offlinegrid';
					});
				}else{
					eDialog.alert(result.resultMsg,function(){
						window.location.href = GLOBAL.WEBROOT+'/order/pay/offlinegrid';
					});
				}
			},
			failure:function(){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent();
			}
		});
	});

});