function addLoadParamsToUrl(_load_url_) {
	var begDate = $('input[name="begDate"]').val();
	if (begDate != "" && begDate != undefined) {
		_load_url_ += '&begDate=' + begDate;
	}
	var endDate = $('input[name="endDate"]').val();
	if (endDate != "" && endDate != undefined) {
		_load_url_ += '&endDate=' + endDate;
	}
	var shopId = $('select[name="shopId"]').val();
	if (shopId != "" && shopId != undefined) {
		_load_url_ += '&shopId=' + shopId;
	}
	var siteId = $('select[name="siteId"]').val();
	if (siteId != "" && siteId != undefined) {
		_load_url_ += '&siteId=' + siteId;
	}
	var orderId = $('input[name="orderId"]').val();
	if (orderId != "" && orderId != undefined) {
		_load_url_ += '&orderId=' + orderId;
	}
	var status = $('select[name="status"]').val();
	if (status != "" && status != undefined) {
		_load_url_ += '&status=' + status;
	}
	var payTranNo = $('input[name="payTranNo"]').val();
	if (payTranNo != "" && payTranNo != undefined) {
		_load_url_ += '&payTranNo=' + payTranNo;
	}
	var payWay = $("#payWay").val();
    if(payWay !="" && payWay != undefined){
        _load_url_ += '&payWay=' + payWay;
    }
    var date = new Date();
    _load_url_ += '&t='+date.getTime();
	return _load_url_;
}

function getTabId(){
	var type = $("#refundTab>li.active").attr("id");
	var _id= type;
	return _id;
}
// 页面初始化模块
$(function() {
	// 页面业务逻辑处理内容
	var pInit = function() {

		var init = function() {
			//待处理
            $('#backgdsTodo').click(function(){
            	var s = '';
            	var _load_url_ = '/seller/order/backgds/querytodo?ls='+s;
            	_load_url_ = addLoadParamsToUrl(_load_url_);
            	$("#btnPrint").hide();
            	$(this).attr("class","active");
            	$('#backgdsHandled').attr("class","");
            	$('#backgdsRefundConfirm').attr("class","");
            	$('#order-contentDiv').load(GLOBAL.WEBROOT + _load_url_);
            });
          //确认退款
            $('#backgdsRefundConfirm').click(function(){
            	var s = '';
            	var _load_url_ = '/seller/order/backgds/queryRefundConfirm?ls='+s;
            	_load_url_ = addLoadParamsToUrl(_load_url_);
            	$("#btnPrint").hide();
            	$(this).attr("class","active");
            	$('#backgdsTodo').attr("class","");
            	$('#backgdsHandled').attr("class","");
            	$('#order-contentDiv').load(GLOBAL.WEBROOT + _load_url_);
            	
            });

	    	//已处理
            $('#backgdsHandled').click(function(){
            	var s = '';
            	var _load_url_ = '/seller/order/backgds/queryhandled?ls='+s;
            	_load_url_ = addLoadParamsToUrl(_load_url_);
            	$("#btnPrint").show();
            	$(this).attr("class","active");
            	$('#backgdsTodo').attr("class","");
            	$('#backgdsRefundConfirm').attr("class","");
                $('#order-contentDiv').load(GLOBAL.WEBROOT + _load_url_);
            
            });
			
			// 查询
			$('#btnFormSearch').click(function() {
				if (!$('#searchForm').valid())
					return;
				var cur_active_tab = getTabId();
				$('#'+cur_active_tab).click();
			});
			// 重置
			$('#btnFormReset').click(function() {
				ebcForm.resetForm('#searchForm');
				$("#begDate").val($("#resetBegDate").val());
				$("#endDate").val($("#resetEndDate").val());
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
		    	$("#searchForm").attr("action",GLOBAL.WEBROOT + '/seller/order/backgds/printList');
		    	$("#searchForm").submit();
			});

			//初始化完成后，加载数据
    		var cur_active_tab = getTabId();
    		$('#'+cur_active_tab).click();
		};
		return {
			init : init
		};
	};
	pageConfig.config({
		// 指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : [ 'bForm', 'bPage' ],
		// 指定页面
		init : function() {
			var p = new pInit();
			p.init();

		}
	});

});
