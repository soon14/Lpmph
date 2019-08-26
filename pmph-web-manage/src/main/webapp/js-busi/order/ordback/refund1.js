var backgds = {
		reviewBack:function(backId,orderId){
			var $form = $("#backreviewForm");
			$form.attr("action",GLOBAL.WEBROOT + '/refundReview/review');
			$form.attr("method","post");
			var contentHTML="";
			contentHTML = "<input type=\"hidden\" id=\"orderId\" name=\"orderId\" value=\""+ orderId +"\">";
			contentHTML = contentHTML + "<input type=\"hidden\" id=\"backId\" name=\"backId\" value=\""+ backId +"\">";
			$form.html(contentHTML);
			$form.submit();
		},
		compensatedetail:function(backId,orderId,siteId){
			var $form = $("#backreviewForm");
			url=GLOBAL.WEBROOT + '/refundReview/detailCompensateBack?orderId='+orderId+'&backId='+backId;
			window.open(url);
		},
		detail:function(backId,orderId,siteId){
			var $form = $("#backreviewForm");
			url=GLOBAL.WEBROOT + '/refundReview/detail?orderId='+orderId+'&backId='+backId;
			window.open(url);
		},
		reviewCompensateBack:function(backId,orderId){
			var $form = $("#backreviewForm");
			$form.attr("action",GLOBAL.WEBROOT + '/refundReview/reviewCompensateBack');
			$form.attr("method","post");
			var contentHTML="";
			contentHTML = "<input type=\"hidden\" id=\"orderId\" name=\"orderId\" value=\""+ orderId +"\">";
			contentHTML = contentHTML + "<input type=\"hidden\" id=\"backId\" name=\"backId\" value=\""+ backId +"\">";
			$form.html(contentHTML);
			$form.submit();
		},

		queryRefund:function(backId,orderId,applyType,payType){
			if(payType=="0"){ //线下支付
				bDialog.open({
					title : '确认退款',
					width : 850,
					height : 550,
					params : {
				    },
					url:GLOBAL.WEBROOT + '/ordrefund/queryRefund?backId='+backId+'&orderId='+orderId+'&applyType='+applyType,
					callback:function(data){
						$("#pendTable").gridReload();
					}
				});
			} else if(payType == "1"){ //线上支付
				var data = [];
				data.push({name:'orderId',value:orderId},
						{name:'backId',value:backId}
						);
				$.gridLoading({"message":"正在加载中...."});
				$.eAjax({
					url : GLOBAL.WEBROOT + '/ordrefund/onlineRefund',
					data :data,
					async : false,
					type : "post",
					dataType : "json",
					success : function(result) {
						$.gridUnLoading();
						if(result.refundMethod == "01"){  //不跳页面
							eDialog.alert(result.message,function(){
							},'confirmation');
						}else{  //跳页面
							var actionUrl = result.payRequestData.actionUrl;
							var method = result.payRequestData.method;
							var charset = result.payRequestData.charset;
							var formData = result.payRequestData.formData;
							requestPayment.submitPayData(actionUrl,method,charset,formData);
						}
					},
					failure:function(){
					}
				});
				
			} else {
				return;
			}
			
			
		},
		backDatil:function(backId,orderId,siteId){
			var url =  "";
        	var siteUrl  = $.trim($("#site1").val());
        	if(siteId == 1){
        		url = siteUrl+"/order/return/returnMoney";
        	} else if(siteId == 2){
        		url = siteUrl+"/order/point/return/returnMoney/"+ backId+"/"+orderId;
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
		backCompensateDatil:function(backId,orderId,siteId){
			var url =  "";
        	var siteUrl  = $.trim($("#site1").val());
        	if(siteId == 1){
        		url = siteUrl+"/order/return/returnCompensateMoney";
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
	var back_url = GLOBAL.WEBROOT + '/refundReview/queryOrder';
	var baseCol = [
	        { "mData": "rBackApplyResp.backId", "sTitle":"退款申请编号", "sClass":"center" ,"bSortable":false, "mRender": function(data,type,row){
	        	if(row.rBackApplyResp.isCompenstate!=0){
	        		return '<a href="javascript:void(0)" target="_blank" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">'+data+'</a>';
	        	}else{
	        		return '<a href="javascript:void(0)" target="_blank" onclick="backgds.compensatedetail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">'+data+'</a>';
	        	}
	        }},
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
			{ "mData": "rBackApplyResp.applyTime", "sTitle":"申请日期", "sClass":"center" ,"bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "rBackApplyResp.status", "sTitle":"申请单状态",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = oUtil.refundStatus[data];
				if(val == null){
					val = '';
					return val;
				}
				return val;
			}},
			{ "mData": "rBackApplyResp.realMoney", "sTitle":"交易金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "rBackApplyResp.backMoney", "sTitle":"退款金额",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				if(data == null){
					return '';
				}
				return ebcUtils.numFormat(data/100, 2);
			}},
			{ "mData": "rBackApplyResp.payType", "sTitle":"退款方式",  "sClass":"center","bSortable":false, "mRender": function(data,type,row){
				var payType = "";
				if(data == "1"){
					payType = "线上退款";
				} else if(data == "0"){
					payType = "线下退款";
				}
				return payType;
			}},
			{ "mData": "rBackApplyResp.isCompenstate", "sTitle":"补偿性退款",  "sClass":"center","bSortable":false, "mRender": function(data,type,row){
				var isCompenstate = "";
				if(data == "0"){
					isCompenstate = "是";
				} else {
					isCompenstate = "否";
				}
				return isCompenstate;
			}},
			{ "mData": "rBackApplyResp.backId", "sTitle":"操作", "sWidth":"150px",  "sClass":"center","bSortable":false,"mRender": function(data,type,row){
				var val = '';
				if(row.rBackApplyResp.status =="00"){
					if(row.rBackApplyResp.isCompenstate!=0){
						val = '<a class="_send" href="javascript:void(0)" onclick="backgds.reviewBack(\''+data+'\',\''+row.rBackApplyResp.orderId+'\')">审核</a> <span>|</span>\
						<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
					}else{
						val = '<a class="_send" href="javascript:void(0)" onclick="backgds.reviewCompensateBack(\''+data+'\',\''+row.rBackApplyResp.orderId+'\')">审核</a> <span>|</span>\
						<a class="_send" href="javascript:void(0)" onclick="backgds.compensatedetail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
					}
				} else if(row.rBackApplyResp.status =="01"){
					val = '<a class="_send" href="javascript:void(0)" onclick="backgds.queryRefund(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.applyType+'\',\''+row.rBackApplyResp.payType+'\')">确认退款</a> <span>|</span>\
					<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
				} else {
					if(row.rBackApplyResp.isCompenstate!=0){
						val = '<a class="_send" href="javascript:void(0)" onclick="backgds.detail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
					}else{
						val = '<a class="_send" href="javascript:void(0)" onclick="backgds.compensatedetail(\''+data+'\',\''+row.rBackApplyResp.orderId+'\',\''+row.rBackApplyResp.siteId+'\')">查看详情</a>';
					}
				}
				return val;
			}}
		];
	for(var i = 0 ; i < 2; i ++){
		var tabName="";
		var aoColumns = baseCol;
		switch(i){
		case 0:
			tabName="pend";
			break;
		case 1:
			tabName="deal";
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
	                  {name:"orderId",value:$("#orderId").val()},{name:"tabFlag",value:"0"+tabFlag},{name:"status",value:"00"},
	                  {name:"isCompensate",value:$("#isCompensate").val()}],
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
			reqVO.push({name : 'tabFlag',value :'00'},{name:"status",value:"00"});
		} else if(tabName == "deal"){
			reqVO.push({name : 'tabFlag',value :'01'},{name:"status",value:"00"});
		} else{
			return;
		}
		$(tabType+'Table').gridSearch(reqVO);
	}
	$('#myTab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
		var id = getTabId();
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
	
	$('#btnCompensate').click(function(){
		self.location.href=GLOBAL.WEBROOT + '/refundReview/compensate';
	});
	
	
});