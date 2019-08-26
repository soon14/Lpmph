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
	var status = $('select[name="status"]').val();
	if (status != "" && status != undefined) {
		_load_url_ += '&status=' + status;
	}
	var orderId = $('input[name="orderId"]').val();
	if (orderId != "" && orderId != undefined) {
		_load_url_ += '&orderId=' + orderId;
	}
	var contactName = $('input[name="contactName"]').val();
	if (contactName != "" && contactName != undefined) {
		_load_url_ += '&contactName=' + contactName;
	}
	var invoiceType = $('select[name="invoiceType"]').val();
	if (invoiceType != "" && invoiceType != undefined) {
		_load_url_ += '&invoiceType=' + invoiceType;
	}
	var payType = $('select[name="payType"]').val();
	if (payType != "" && payType != undefined) {
		_load_url_ += '&payType=' + payType;
	}
	var staffCode = $('input[name="staffCode"]').val();
	if (staffCode != "" && staffCode != undefined) {
		_load_url_ += '&staffCode=' + staffCode;
	}
	var payFlag = $('select[name="payFlag"]').val();
	if (payFlag != "" && payFlag != undefined) {
		_load_url_ += '&payFlag=' + payFlag;
	}
	var payWay = $('select[name="payWay"]').val();
	if (payWay != "" && payWay != undefined) {
		_load_url_ += '&payWay=' + payWay;
	}
    var date = new Date();
    _load_url_ += '&t='+date.getTime();
	return _load_url_;
}
// 页面初始化模块
$(function() {

	// 页面业务逻辑处理内容
	var pInit = function() {

		var init = function() {
			// 查询
			$('#btnFormSearch').click(function() {
				var _load_url_ = '/seller/order/customer/gridlist?';
            	_load_url_ = addLoadParamsToUrl(_load_url_);
            	$(this).attr("class","active");
            	$('#order-contentDiv').load(GLOBAL.WEBROOT + _load_url_);
            	$('#btnFormSearch').attr("class","sbtn sbtn-blue");
			});
			// 重置
			$('#btnFormReset').click(function() {
				ebcForm.resetForm('#searchForm');
				$("#begDate").val($("#resetBegDate").val());
				$("#endDate").val($("#resetEndDate").val());
			});
			//初始化完成后，加载数据
			$('#btnFormSearch').click();
			$('#btnFormSearch').attr("class","sbtn sbtn-blue");
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
