function addLoadParamsToUrl(_load_url_) {
	var shopId = $('select[name="shopId"]').val();
	if (shopId != "" && shopId != undefined) {
		_load_url_ += '&shopId=' + shopId;
	}
	var begDate = $('input[name="begDate"]').val();
	if (begDate != "" && begDate != undefined) {
		_load_url_ += '&begDate=' + begDate;
	}
	var endDate = $('input[name="endDate"]').val();
	if (endDate != "" && endDate != undefined) {
		_load_url_ += '&endDate=' + endDate;
	}
	var siteId = $('select[name="siteId"]').val();
	if (siteId != "" && siteId != undefined) {
		_load_url_ += '&siteId=' + siteId;
	}
	var orderId = $('input[name="orderId"]').val();
	if (orderId != "" && orderId != undefined) {
		_load_url_ += '&orderId=' + orderId;
	}
	var status = '00' ;
	if (status != "" && status != undefined) {
		_load_url_ += '&status=' + status;
	}
	var tabFlag = "00" ;
	if (tabFlag != "" && tabFlag != undefined) {
		_load_url_ += '&tabFlag=' + tabFlag;
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
            	var _load_url_ = '/seller/backReview/queryOrder?';
            	_load_url_ = addLoadParamsToUrl(_load_url_);
        	    
            	$(this).attr("class","active");
            	$('#backgdsHandled').attr("class","");
            	$('#order-contentDiv').load(GLOBAL.WEBROOT + _load_url_);
            });
			
			// 查询
			$('#btnFormSearch').click(function() {
				var cur_active_tab = getTabId();
				$('#'+cur_active_tab).click();
			});
			// 重置
			$('#btnFormReset').click(function() {
				ebcForm.resetForm('#searchForm');
				$("#begDate").val($("#resetBegDate").val());
				$("#endDate").val($("#resetEndDate").val());
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
