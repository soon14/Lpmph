/**
 * e.core.js 
 * JavaScript核心功能库
 * 
 * 功能更新
 * 2016.07.22 重构eAjax
 */
;(function ($) {
	"use strict";
	
	//属性复制
	var copyProperties = function(to, from) {
		if (!to || !from)return;
		if ($.isArray(to) && $.isArray(from)) {
			$.each(from, function(i, n) {
				var flag = false;
				$.each(to, function(j, m) {
					if (n.name == m.name) {
						m.value = n.value;
						flag = true;
						return;
					}
				});
				if (!flag)to.push(n);
				return;
			});
			return to;
		}
	};
	window.copyProperties = copyProperties;
	
	//通用Ajax调用入口
	$.eAjax = function(p) {
		var defaults = {
			url : undefined,//请求地址
			async : true,//默认为异步模式
			type : "POST",
			dataType : "json",//默认请求格式为JSON
			data : undefined,//请求参数
			cache : false,
			layer : false,//是否显示全屏遮罩
			exception : $.noop //业务异常处理
		};
		var params = $.extend({},defaults,p);
		//显示全屏遮罩
		if(params.layer && window.eLayer) eLayer.show({text:'数据处理中……'});
		
		if(params.url.indexOf('http://') != -1){//提交路径里出现了完整的路径时，要避免转换http://中的双斜线为单斜线
			params.url = 'http://' + params.url.replace("http://","").replace("//","/");
		}else if(params.url.indexOf('https://') != -1){
			params.url = 'https://' + params.url.replace("https://","").replace("//","/");
		}else params.url = params.url.replace("//","/");
		
		//请求成功回调
		params.success = function(data,textStatus,jqXHR){
			/**
			 * 若返回文本内容，则尝试转换为json对象，失败则表示为非json对象文本
			 * 则将文本内容直接返回给调用者
			 */
			if($.type(data) == 'string'){
				try {
					data = eval('(' + data + ')');
				} catch (e) {}
			}
			if(data && $.isPlainObject(data) && $.type(data.ajaxResult)!='undefined'){
				switch (data.ajaxResult) {
					case 0://访问失败、后台异常、业务异常等非正常结果
						var errmsg = new Array();
						if(data.errorMessage && $.isArray(data.errorMessage) && data.errorMessage.length > 0){
							$.each(data.errorMessage, function(i, row) {
								errmsg.push(row.message);
							});
						}
						if(errmsg.length > 0) eDialog.alert(errmsg.join("<br/>"),params.exception?params.exception:$.noop,'error');
						else eDialog.alert('系统异常，请联系管理员！',params.exception?params.exception:$.noop,'error');
						break;
					case 1://请求成功
						p.success(data.values, textStatus, jqXHR);
						break;
					case 2://后台校验信息
						var errmsg = new Array();
						if(data.errorMessage && $.isArray(data.errorMessage) && data.errorMessage.length > 0){
							$.each(data.errorMessage, function(i, row) {
								errmsg.push(row.message);
							});
						}
						//页面若存在后台校验信息统一显示框，则将校验信息填入
						if(errmsg.length > 0){
							if($('div.formValidateMessages').size()>0){
								$('div.formValidateMessages').html(errmsg.join("<br/>"));
								$('div.formValidateMessages').slideDown('fast');
								if(params.exception && $.isFunction(params.exception)) params.exception();
							}else{//若不存在统一显示框，则弹出信息提示框进行信息展示
								eDialog.alert(errmsg.join("<br/>"),params.exception?params.exception:$.noop,'error');
							}
						}else
							eDialog.alert('表单验证不通过，但未提供相关提示信息，请联系系统管理员！',params.exception?params.exception:$.noop,'error');
						break;
					case 3://登录超时
						var url = $webroot + 'login' + '?Referer=' + window.location.href;
						window.top.location.replace(url);
						break;
					default:
						break;
				}
				//关闭遮罩
				if(params.layer && window.eLayer) eLayer.hide();
			}else{
				//读取文本内容模式
				if(params.dataType=='text' || params.dataType=='jsonp' || params.dataType=='html'){
					p.success(data, textStatus, jqXHR);
					//关闭遮罩
					if(params.layer && window.eLayer) eLayer.hide();
					return;
				}else{
					if(p.error && $.isFunction(p.error)) p.error(data,textStatus,jqXHR);
					//关闭遮罩
					if(params.layer && window.eLayer) eLayer.hide();
				}
			}
		};
		
		//读取错误回调处理
		params.error = function(xhr, msg, errorThrown){
			if(p.error && $.isFunction(p.error)) p.error(xhr, msg, errorThrown);
			//关闭遮罩
			if(params.layer && window.eLayer) eLayer.hide();
		};
		//请求完成回调处理（请求成功、失败都会执行）
		params.complete = function(xhr,msg){
			if(p.complete && $.isFunction(p.complete)) p.complete(xhr,msg);
			//关闭遮罩
			if(params.layer && window.eLayer) eLayer.hide();
		}
		$.ajax(params);
	};
	
	
	/**
	 * 读取文本模式
	 * url：请求地址
	 * data：请求参数
	 * callback：回调处理
	 */
	$.fn.eAjaxLoad = function(url,data,callback){
		if($(this).size() == 0 || !url) return;
		var _this = this;
		$.eAjax({
			dataType : 'text',
			data : data ? data : undefined,
			success : function(response){
				$(_this).html(response);
				if(callback && $.isFunction(callback)) callback();
			}
		});
	};
	
	
	
	/**
     * 遮罩层显示请使用eLayer.show()，此处仅为兼容已在业务中使用的部分
     * 在功能稳定后，将完全去除该方法
     * 
     * 
     * 
     * 实现遮罩效果,使用用例 $.gridLoading({ message:"需要提醒的信息，例如：正在加载中.." })
     * 增加了对页面部分元素进行解除遮罩的功能，增加了参数 el：jquery获取元素的选择器
     * @param p
     * p.el 需要进行遮罩的jquery选择器；
     * p.message 信息；
     */
    $.gridLoading = function(p) {
        var opt = $.extend({
            el:window,
            message : "正在获取数据..."
        }, p);
        $(opt.el).block({
            message : "<div class='loadLevel'>" + opt.message + "</div>",
            overlayCSS : {
                border : "medium none",
                width : "100%",
                height : "100%",
                top : "0pt",
                left : "0pt",
                backgroundColor : "#000",
                opacity : 0.3,
                cursor : "wait",
                position : "fixed"
            }
        });
    };

    /**
     * 遮罩层关闭请使用eLayer.hide()，此处仅为兼容已在业务中使用的部分
     * 在功能稳定后，将完全去除该方法
     * 
     * 
     * 
     * 
     * 解除遮罩效果；增加了对页面部分元素进行解除遮罩的功能
     * @param p
     * p.el 需要解除遮罩的jquery选择器
     */
    $.gridUnLoading = function(p) {
    	var opt = $.extend({
    		el:window
    	}, p);
    	$(opt.el).unblock();
    };
})(window.jQuery);