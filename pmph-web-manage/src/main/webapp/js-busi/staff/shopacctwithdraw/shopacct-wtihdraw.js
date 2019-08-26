var withdrawApply = {
	detail:function(id){
		var url=GLOBAL.WEBROOT + '/shopmgr/shopAcctWithdraw/withdrawApplyDetail?applyId='+id;
		window.open(url);
	},
	confirmWithdraw:function(applyId){
		/*bDialog.open({
		    title : '提现备注',
		    width : 400,
		    height : 300,
		    scroll : true,
		    onShowed : function(_this){
		    	var dlg = $(_this);
				$('#confirmForm_applyId',$(dlg)).val(id);

				bValidate.clearValidate($('#confirmForm',$(dlg)));
				bValidate.labelValidate($('#confirmForm',$(dlg)));
		    },
		    callback:function(data){
		    	if(data && data.results && data.results.length > 0 ){
		    		if(!$("#searchForm").valid()) return false;
		    		var p = ebcForm.formParams($("#searchForm"));
		    		$('#pendTable').gridSearch(p);
		    	}
		    }
		},$('#confirmFormDiv'));

		//清空内容
		$('#remark').val('');*/
		/*$.eAjax({
			url:GLOBAL.WEBROOT+'/shopmgr/shopAcctWithdraw/confirmWithdraw',
			data:{
				"applyId" : id
			},
			success:function(result){
				if(result&&result.resultFlag=='ok'){
					eDialog.alert('提现成功',function(){
						window.location.href = GLOBAL.WEBROOT+'/shopmgr/shopAcctWithdraw/index';
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
						window.location.href = GLOBAL.WEBROOT+'/shopmgr/shopAcctWithdraw/index';
					},'error');
				}
			},
			failure:function(){
				eDialog.alert('提现失败 ',function(){
				},'confirmation');
			}
		});*/
		
		if (applyId == null) {
			eDialog.alert("编码为空，请联系管理员");
			return false;
		}
		eDialog.confirm('您确定要提现吗？', {
			buttons : [ {
				caption : '确定',
				callback : function() {
					var param = {
							"applyId" : applyId
					};
					$.eAjax({
						url : GLOBAL.WEBROOT + "/shopmgr/shopAcctWithdraw/confirmWithdraw",
						data : param,
						datatype:'json',
						beforeSend:function(){
							$.gridLoading({"message":"正在处理中...."});
						},
						success : function(returnInfo) {
							if (returnInfo.resultFlag == 'ok') {
//								eDialog.success('提现成功！');
								window.location.href = GLOBAL.WEBROOT+'/shopmgr/shopAcctWithdraw/index';
							} else if(returnInfo.resultFlag == 'fail'){
								eDialog.info(returnInfo.resultMsg);
							}else{
//								eDialog.alert(" 提现失败，请联系管理员");
								window.location.href = GLOBAL.WEBROOT+'/shopmgr/shopAcctWithdraw/index';
							}
						},error : function(e,xhr,opt){
							eDialog.error("出现异常!");
						},complete:function(){
							$.gridUnLoading();
						}
					});
				}
			}, {
				caption : '取消',
				callback : function() {
					return true;
				}
			} ]
		});
	}
}
$(function(){
	var apply_url = GLOBAL.WEBROOT + '/shopmgr/shopAcctWithdraw/withdrawApplyList';
	var baseCol1 = [
	        { "mData": "id", "sTitle":"提现申请编码", "sClass":"center" ,"bSortable":false},
			{ "mData": "money", "sTitle":"提现金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "applyTime", "sTitle":"申请时间", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "billMonths", "sTitle":"结算账期",  "sClass":"center","bSortable":false},
			{ "mData": "shopName", "sTitle":"店铺名称",  "sClass":"center","bSortable":false},
			{ "mData": "applyStaffCode", "sTitle":"申请人",  "sClass":"center","bSortable":false},
			{ "mData": "applyDesc", "sTitle":"申请人备注",  "sClass":"center","bSortable":false},
			{ "mData": "status", "sTitle":"申请单状态",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = acctUtil.constant.withdrawApplyStatus[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
			{ "mData": "id", "sTitle":"操作",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = '';
				if(row.status == "02"){// onclick="withdrawApply.confirmWithdraw(\''+row.id+'\')"
					val = '<a class="_send" href="javascript:void(0)" onclick="withdrawApply.confirmWithdraw(\''+row.id+'\')">确认提现</a> <span>|</span>\
						<a class="_send" href="javascript:void(0)" onclick="withdrawApply.detail(\''+row.id+'\')">查看详情</a>';
				} else {
					val = '<a class="_send" href="javascript:void(0)" onclick="withdrawApply.detail(\''+row.id+'\')">查看详情</a>';
				}
				return val;
			}}
		];
	
	var baseCol2 = [
	        { "mData": "id", "sTitle":"提现申请编码", "sClass":"center" ,"bSortable":false},
			{ "mData": "money", "sTitle":"提现金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "applyTime", "sTitle":"申请时间", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "billMonths", "sTitle":"结算账期",  "sClass":"center","bSortable":false},
			{ "mData": "shopName", "sTitle":"店铺名称",  "sClass":"center","bSortable":false},
			{ "mData": "applyStaffCode", "sTitle":"申请人",  "sClass":"center","bSortable":false},
			{ "mData": "applyDesc", "sTitle":"申请人备注",  "sClass":"center","bSortable":false},
			{ "mData": "status", "sTitle":"申请单状态",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = acctUtil.constant.withdrawApplyStatus[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
			{ "mData": "withdrawTime", "sTitle":"提现时间", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "id", "sTitle":"操作",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = '<a class="_send" href="javascript:void(0)" onclick="withdrawApply.detail(\''+row.id+'\')">查看详情</a>';
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
	        'params':[{name:"begDate",value:$("input[name='begDate']").val()},{name:"endDate",value:$("input[name='endDate']").val()},
	                  {name:"shopId",value:$("#shopId").val()},{name:"tabFlag",value:"0"+tabFlag}],
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
//		var type = getTabId();
		var tabName= tabType.replace('#','');
		if(tabName == "pend"){
			reqVO.push({name : 'tabFlag',value :'00'}); 
		} else if(tabName == "deal") {
			reqVO.push({name : 'tabFlag',value :'01'});
		} else{
			return;
		}
		$(tabType+'Table').gridSearch(reqVO);
	}
	$('#myTab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
		var id = getTabId();
//		console.info(id);  
		evenCallback(id);
    });
	$('#btnFormBaseSearch').click(function(){
		//console.info(ebcForm.formParams($("#searchForm")));
		var tabType = getTabId();
		evenCallback(tabType);
	});
	function getTabId(){
		var type = $("#myTab>li.active").find("a").attr("href");
		var _id= type.replace('Tab','');
		return _id;
	}
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	
	$('body').delegate('div.dialogInActive #confirm', 'click', function(e) {
		var dlg = bDialog.getDialog();
		var _this = this;
		if($(_this).hasClass('disabled')){
			return false;
		}

		if(!$('#confirmForm',$(dlg)).valid()) return false;

		var data = ebcForm.formParams($('#confirmForm',$(dlg)));

		data.push();
		$(_this).addClass('disabled');
		$.eAjax({
			url:GLOBAL.WEBROOT+'/shopmgr/shopAcctWithdraw/confirmWithdraw',
			data:data,
			success:function(result){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent({result:'ok'});
				if(result&&result.resultFlag=='ok'){
					eDialog.alert('提现成功',function(){
					},'confirmation');
				}else{
					eDialog.alert(result.resultMsg,function(){
						window.location.href = GLOBAL.WEBROOT+'/shopmgr/shopAcctWithdraw/withdrawApplyList';
					},'error');
				}
			},
			failure:function(){
				$(_this).removeClass('disabled');
				bDialog.closeCurrent();
			}
		});

	});
	
});