var backgds = {
		reviewBack:function(backId,orderId){
			var $form = $("#backreviewForm");
			$form.attr("action",GLOBAL.WEBROOT + '/backReview/review');
			$form.attr("method","post");
			var contentHTML="";
			contentHTML = "<input type=\"hidden\" id=\"orderId\" name=\"orderId\" value=\""+ orderId +"\">";
			contentHTML = contentHTML + "<input type=\"hidden\" id=\"backId\" name=\"backId\" value=\""+ backId +"\">";
			$form.html(contentHTML);
			$form.submit();
		},
		detail:function(backId,orderId,siteId){
			//var $form = $("#backreviewForm");
			var url=GLOBAL.WEBROOT + '/backReview/detail?orderId='+orderId+'&backId='+backId;
			window.open(url);
		},
		confirmReceipt:function(backId,orderId){
			var data = [];
			data.push({name:'orderId',value:orderId},
					{name:'backId',value:backId}
					);
			$.eAjax({
				url : GLOBAL.WEBROOT + '/ordback/receipt',
				data :data,
				async : true,
				type : "post",
				dataType : "json",
				success : function(result) {
					if(result&&result.resultFlag=='ok'){
						bDialog.closeCurrent({result:'ok'});
						eDialog.alert('操作成功',function(){
//							var reqVO = ebcForm.formParams($("#searchForm"));
//							$('#agreebackTable').gridSearch(reqVO);
							$("#pendTable").gridReload();
						},'confirmation');
						
					}else{
						eDialog.alert(result.resultMsg,function(){
//							window.location.href = GLOBAL.WEBROOT+'/ordback/queryback';
						},'error');
					}
				},
				failure:function(){
					bDialog.closeCurrent();
				}
			});
		},
		queryRefund:function(obj,backId,orderId,applyType,payType){
			
			/*var $form = $("#confirmRefundForm");
			$form.attr("action",GLOBAL.WEBROOT + '/ordback/queryrefund');
			$form.attr("method","post");
			var contentHTML="";
			contentHTML = "<input type=\"hidden\" id=\"orderId\" name=\"orderId\" value=\""+ orderId +"\">";
			contentHTML = contentHTML + "<input type=\"hidden\" id=\"backId\" name=\"backId\" value=\""+ backId +"\">";
			$form.html(contentHTML);
			$form.submit();*/
			if(payType=="0"){ //线下支付
				
				bDialog.open({
					title : '确认退款',
					width : 850,
					height : 550,
//					params : [{name:"backId",value:backId},{name:"orderId",value:orderId}],
					params : {},
					url:GLOBAL.WEBROOT + '/ordback/queryRefund?backId='+backId+'&orderId='+orderId+'&applyType='+applyType,
					onHidden:function(){			
//						$("#payTable").gridReload();
					},
					callback:function(data){
						$("#payTable").gridReload();
					}
				});
			} else if(payType == "1"){ //线上支付
				var data = [];
				data.push({name:'orderId',value:orderId},
						{name:'backId',value:backId}
						);
				$.gridLoading({"message":"正在加载中...."});
				var refundMethod = "";
				var sourceKey = "";
				$.eAjax({
					url : GLOBAL.WEBROOT + '/ordback/onlineRefund',
					data :data,
					async : false,
					type : "post",
					dataType : "json",
					success : function(result) {
						$.gridUnLoading();
						refundMethod = result.refundMethod;
						if(refundMethod == "01"){  //不跳页面
							eDialog.alert(result.message,function(){
								$("#payTable").gridReload();
							},'confirmation');
						}else{  
							//跳页面
							var actionUrl = result.payRequestData.actionUrl;
							var method = result.payRequestData.method;
							var charset = result.payRequestData.charset;
							var formData = result.payRequestData.formData;
							sourceKey = result.sourceKey;
							//requestPayment.submitPayData(actionUrl,method,charset,formData);
						}
					},
					failure:function(){
					}
				});
				if(refundMethod != "01"){ 
					var url = GLOBAL.WEBROOT+'/ordrefund/refundReview?sourceKey='+sourceKey;
					bDialog.open({
						title : '确认退款',
					    width : 1100,
					    height : 550,
					    scroll : true,
					    url : url,
						callback:function(data){
							$("#payTable").gridReload();
						}	   
					});
				}
			} else {
				return ;
			}
			
		},
		backDatil:function(backId,orderId,siteId){
			var url =  "";
        	var siteUrl  = $.trim($("#site1").val());
        	if(siteId == 1){
        		url = siteUrl+"/order/return/returnDetail";
        	} else if(siteId == 2){
        		url = siteUrl+"/order/point/return/returnDetail/"+ backId+"/"+orderId;
        		window.open(url);
        	}
			var $form = $("#listForm");
			$form.attr("action",url);
			$form.attr("method","post");
			var contentHTML="";
			contentHTML = "<input type=\"hidden\" id=\"orderId\" name=\"orderId\" value=\""+ orderId +"\">";
			contentHTML = contentHTML + "<input type=\"hidden\" id=\"backId\" name=\"backId\" value=\""+ backId +"\">";
			$form.html(contentHTML);
			$form.submit();
		},
		ordDatil:function(orderId,siteId){
			oUtil.openOrdDetail(orderId);
		}

}
$(function(){
	var back_url = GLOBAL.WEBROOT + '/ordback/queryOrder';
	var baseCol1 = [
	        { "mData": "rBackApplyResp.backId", "sTitle":"退货编号", "sClass":"center" ,"bSortable":false, "mRender": function(data,type,row){
	        	return '<a href="javascript:void(0)" target="_blank" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">'+data+'</a>';
			}},
			{ "mData": "rBackApplyResp.payTranNo", "sTitle":"商户订单号",  "sClass":"center","bSortable":false},
	        { "mData": "rBackApplyResp.orderId", "sTitle":"订单编号", "sClass":"center","bSortable":false, "mRender": function(data,type,row){
	        	return '<a href="javascript:void(0)" target="_blank" onclick="backgds.ordDatil(\''+data+'\',\''+row.rBackApplyResp.siteId+'\')">'+data+'</a>';
			}},
			{ "mData": "payType", "sTitle":"支付方式", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				var val = oUtil.constant.pay[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
			{ "mData": "payWay", "sTitle":"支付通道", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				var val = oUtil.constant.payWay[data];
				if(val == null){
					val = '';
					return val;
				}
				return val;
			}},
			{ "mData": "rBackApplyResp.payTime", "sTitle":"支付时间", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "rBackApplyResp.applyTime", "sTitle":"申请日期", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "rBackApplyResp.status", "sTitle":"申请单状态",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = oUtil.backStatus[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
			{ "mData": "rBackApplyResp.num", "sTitle":"数量",  "sClass":"center","bSortable":false},
			{ "mData": "rBackApplyResp.realMoney", "sTitle":"交易金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "rBackApplyResp.backMoney", "sTitle":"退款金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				if(data == null){
					return '';
				}
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "rBackApplyResp.backId", "sTitle":"操作",  "sWidth":"150px", "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = '';
				if(row.rBackApplyResp.status =="00"){
					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
				} else if(row.rBackApplyResp.status == "01" || row.rBackApplyResp.status == "02"){
					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.confirmReceipt(\''+data+'\',\''+row.rBackApplyResp.orderId+'\')">确认收货</a> <span>|</span>\
					<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
				} else if(row.rBackApplyResp.status == "03"){
					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.queryRefund(this,\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.applyType+'\',\''+row.rBackApplyResp.payType+'\')">确认退款</a> <span>|</span>\
					<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
				} else {
					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
				}
				return val;
			}}
		];
	
	var baseCol2 = [
	   	        { "mData": "rBackApplyResp.backId", "sTitle":"退货编号", "sClass":"center" ,"bSortable":false, "mRender": function(data,type,row){
	   	        	return '<a href="javascript:void(0)" target="_blank" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">'+data+'</a>';
	   			}},
	   			{ "mData": "rBackApplyResp.payTranNo", "sTitle":"商户订单号",  "sClass":"center","bSortable":false},
	   	        { "mData": "rBackApplyResp.orderId", "sTitle":"订单编号", "sClass":"center","bSortable":false, "mRender": function(data,type,row){
	   	        	return '<a href="javascript:void(0)" target="_blank" onclick="backgds.ordDatil(\''+data+'\',\''+row.rBackApplyResp.siteId+'\')">'+data+'</a>';
	   			}},
	   			{ "mData": "payType", "sTitle":"支付方式", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
	   				var val = oUtil.constant.pay[data];
	   				if(val == null){
	   					val = '';
	   				}
	   				return val;
	   			}},
	   			{ "mData": "payWay", "sTitle":"支付通道", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
	   				var val = oUtil.constant.payWay[data];
	   				if(val == null){
	   					val = '';
	   					return val;
	   				}
	   				return val;
	   			}},
	   			{ "mData": "rBackApplyResp.payTime", "sTitle":"支付时间", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				}},
	   			{ "mData": "rBackApplyResp.applyTime", "sTitle":"申请日期", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
	   				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
	   			}},
	   			{ "mData": "rBackApplyResp.status", "sTitle":"申请单状态",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
	   				var val = oUtil.backStatus[data];
	   				if(val == null){
	   					val = '';
	   				}
	   				return val;
	   			}},
	   			{ "mData": "rBackApplyResp.num", "sTitle":"数量",  "sClass":"center","bSortable":false},
	   			{ "mData": "rBackApplyResp.realMoney", "sTitle":"交易金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
	   				return ebcUtils.numFormat(data/100, 2);
	   			}},
	   			{ "mData": "rBackApplyResp.backMoney", "sTitle":"退款金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
	   				if(data == null){
	   					return '';
	   				}
	   				return ebcUtils.numFormat(data/100, 2);
	   			}},
	   			{ "mData": "rBackApplyResp.refundTime", "sTitle":"退款时间", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
	   				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
	   			}},
	   			{ "mData": "rBackApplyResp.backId", "sTitle":"操作",  "sWidth":"150px", "sClass":"center","bSortable":false,"mRender": function(data,type,row){
	   				var val = '';
	   				if(row.rBackApplyResp.status =="00"){
	   					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
	   				} else if(row.rBackApplyResp.status == "01" || row.rBackApplyResp.status == "02"){
	   					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.confirmReceipt(\''+data+'\',\''+row.rBackApplyResp.orderId+'\')">确认收货</a> <span>|</span>\
	   					<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
	   				} else if(row.rBackApplyResp.status == "03"){
	   					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.queryRefund(this,\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.applyType+'\',\''+row.rBackApplyResp.payType+'\')">确认退款</a> <span>|</span>\
	   					<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
	   				} else {
	   					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
	   				}
	   				return val;
	   			}}
	   		];
	
	for(var i = 0 ; i < 3; i ++){
		var tabName="";
		var aoColumns;
		switch(i){
		case 0:
			tabName="pend";
			aoColumns = baseCol1;
			break;
		case 1:
			tabName="pay";
			aoColumns = baseCol1;
			break;
		case 2:
			tabName="deal";
			aoColumns = baseCol2;
			break;
		}
		tableInitDT(back_url,aoColumns,tabName,i);
	}
	function tableInitDT(back_url,aoColumns,tabName,tabFlag){
		$("#"+tabName+"Table").initDT({
	        'pTableTools' : false,
	        'pCheckRow' : false,
	        'pCheckColumn' : false,
	        'pSingleCheckClean' : false,
	        'params':[{name:"begDate",value:$("input[name='begDate']").val()},{name:"endDate",value:$("input[name='endDate']").val()},
	                  {name:"siteId",value:$("#siteId").val()},{name:"shopId",value:$("#offline_grid_shopId").val()},
	                  {name:"orderId",value:$("#orderId").val()},{name:"tabFlag",value:"0"+tabFlag}],
	        "sAjaxSource": back_url,
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
			$(".printDiv").hide();
		} else if(tabName == "deal") {
			reqVO.push({name : 'tabFlag',value :'01'});
			$(".printDiv").show();
		} else if(tabName == "pay") {
			reqVO.push({name : 'tabFlag',value :'02'});
			$(".printDiv").hide();
		} else {
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
	
	/**
	 * 批量打印
	 */
    $('#btnPrint').click(function(){
    	$('#btnFormBaseSearch').trigger('click');
    	if(!$("#searchForm").valid()) return false;
    	$("#searchForm").append("<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"1000\">");
    	$("#searchForm").append("<input type=\"hidden\" id=\"tabFlag\" name=\"tabFlag\" value=\"01\">");
    	$("#searchForm").attr("target","_blank");
    	$("#searchForm").attr("method","post");
    	$("#searchForm").attr("action",GLOBAL.WEBROOT + '/ordback/printList');
    	$("#searchForm").submit();
	});
	
});