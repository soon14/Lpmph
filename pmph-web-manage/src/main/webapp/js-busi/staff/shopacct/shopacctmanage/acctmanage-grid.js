var shopAcctAdjust = {
		finish:function(data,id,applyDesc){
			var data = [];
			data.push({name:'id',value:id},{name:"applyDesc",value:applyDesc});
			eDialog.confirm("您确认要撤销吗？", {
				buttons : [{
					caption : '确认',
					callback : function(){
						$.eAjax({
							url : GLOBAL.WEBROOT + "/shopAcctAdjust/adjustUpdate",
							data : data,
							async : true,
							type : "post",
							datatype:'json',
							success : function(returnInfo) {
								eDialog.success('已撤销！',{
									buttons:[{
										caption:"确定",
										callback:function(){
											$("#btnFormBaseSearch").trigger("click");
								        }
									}]
								}); 
							}
						});
					}
				},{
					caption : '取消',
					callback : $.noop
				}]
			});
		},
		detail:function(data,id){
			var $form = $("#backreviewForm");
			url=GLOBAL.WEBROOT + '/shopAcctAdjust/backAdjust?id='+id;
			window.open(url);
		}
}
$(function(){
	var apply_url = GLOBAL.WEBROOT + '/shopAcctAdjust/gridList';
		var baseCol1 = [
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
					val = '<a class="_send" href="javascript:void(0)" onclick="shopAcctAdjust.finish(\''+data+'\',\''+row.id+'\',\''+row.applyDesc+'\')">撤销</a> <span>|</span>\
						<a class="_send" href="javascript:void(0)" onclick="shopAcctAdjust.detail(\''+data+'\',\''+row.id+'\')">查看明细</a>';
				return val;
		  }}
		];
		var baseCol2 = [
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
		     			{ "mData": "adjTime", "sTitle":"调账时间","bSortable":false,"sClass":"center", "mRender": function(data,type,row){
		     				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
		     			}},
		     			{ "mData": "", "sTitle":"操作", "sWidth":"150px",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
		     				var val = '';
		     					val = '<a class="_send" href="javascript:void(0)" onclick="shopAcctAdjust.detail(\''+data+'\',\''+row.id+'\')">查看明细</a>';
		     				return val;
		     		  }}
		     		];
		for(var i = 0 ; i < 2; i ++){
			var tabName="";
			var aoColumns;
			switch(i){
			case 0:
				tabName="pend";
				aoColumns = baseCol1;
				break;
			case 1:
				tabName="deal";
				aoColumns = baseCol2;
				break;
			}
			tableInitDT(apply_url,aoColumns,tabName,i);
		}
		function tableInitDT(apply_url,aoColumns,tabName,tabFlag){
			$("#"+tabName+"Table").initDT({
		        'pTableTools' : false,
		        'pCheckRow' : false,
		        'pCheckColumn' : false,
		        'pSingleCheckClean' : false,
		        'params':[{name:"shopId",value:$("#offline_grid_shopId").val()},
				          {name:"begDate",value:$("input[name='begDate']").val()},
				          {name:"status",value:tabFlag},
				          {name:"endDate",value:$("input[name='endDate']").val()}
				          ],
		        "sAjaxSource": apply_url,
		        "aoColumns": aoColumns,
		        //双击事件
		        "eDbClick" : function(){
		        }
			});
		} 
		function evenCallback(tabType){
			if(!$("#searchForm").valid()) return false;
			/*这个方法是ajax的，后台跳转的，自然和它无关了，难道这就叫ajax跨域吗*/
			/*带了tab之后就必须要获取当前是那个tab的数据*/
			var reqVO = ebcForm.formParams($("#searchForm"));
//			var type = getTabId();
			var tabName= tabType.replace('#','');
			if(tabName == "pend"){
				reqVO.push({name : 'status',value :'0'}); 
			} else if(tabName == "deal") {
				reqVO.push({name : 'status',value :'1'});
			} else{
				return;
			}
			$(tabType+'Table').gridSearch(reqVO);
		}
		$('#myTab a').click(function (e) {
			e.preventDefault();
			$(this).tab('show');
			var id = getTabId();
//			console.info(id);  
			evenCallback(id);
	    });
		$('#btnFormBaseSearch').click(function(){
			//console.info(ebcForm.formParams($("#searchForm")));
			var tabType = getTabId();
			evenCallback(tabType);
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

});