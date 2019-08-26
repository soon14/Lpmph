//require(['ZebraDialog']);
	

/**
 * 网站前台弹出窗口
 * 基于Amaze UI
 */
var eDialog = {
	template : '<div class="am-modal am-modal-alert" tabindex="-1" id="mobileAlertDialog">'+
			   '<div class="am-modal-dialog">' + 
			   '<div class="am-modal-hd">提示</div>' + 
			   '<div class="am-modal-bd"></div>' + 
			   '<div class="am-modal-footer"></div>' + 
			   '</div>' + 
			   '</div>',
	defaults : {
		url : '',
		title : '',
		closeable : true,
		closeText : '关闭窗口',
		modal : false,//是否模式窗口
		width : 400,
		height : 300,		
		callback : false
	},
	//获得作用域的jQuery对象，解决在弹出窗口(iframe)中使用消息框位置显示不正确的问题
	getDomain : function(){
		return window.self == window.top?window.self.$:window.top.$;
	},
	//消息通知统一入口
	//type：窗口类型（图标类型）
	//     【'information','warning','error','confirmation'】
	//      其中默认为'information'不需要设置，'warning'为警告，'error'为错误，'confirmation'为成功
	//content：消息框显示内容
	//callback：点击'确定'按钮后的回调函数
	alert : function(content,callback,type){
		//var _type = 'information';
		var _callback = $.noop;
		var _template = eDialog.template;
		var main = $(_template);
		$(window.top.document.body).append(main);
		//if(type) _type = type;
		if($.isFunction(callback)) _callback = callback;
		//var _p = $.extend({},eDialog.defaults,_p);
		
		$('div.am-modal-bd',$(main)).html(content);
		$('div.am-modal-footer',$(main)).html('<span class="am-modal-btn">确定</span>');
		$(main).off('closed.modal.amui').on('closed.modal.amui', function(){
			_callback();
		});
		$(main).modal();
	},
	//对话框模式
	//按钮定义格式：{caption: 'Yes', callback: function() { alert('"Yes" was clicked')}}
	confirm : function(content,callback){
		//var _type = 'information';
		var _callback = $.noop;
		var _template = eDialog.template;
		var main = $(_template);
		$(window.top.document.body).append(main);
		//if(type) _type = type;
		if($.isFunction(callback)) _callback = callback;
		//var _p = $.extend({},eDialog.defaults,_p);
		$('div.am-modal-hd',$(main)).text('确认');
		$('div.am-modal-bd',$(main)).html(content);
		$('div.am-modal-footer',$(main)).html('<span class="am-modal-btn" data-am-modal-confirm>确定</span><span class="am-modal-btn" data-am-modal-cancel>取消</span>');
		/*
		$(main).off('closed.modal.amui').on('closed.modal.amui', function(){
			_callback();
		});
		*/
		$(main).modal({
			onConfirm : function(){
				_callback();
			}
		});
	}
};


//指定脚本样式依赖
//引入公用脚本库定义
//层级说明：内层依赖外层
//加载顺序：外层优先加载
//require(['/ecp-web-demo/js/common.js'],function(){
	
//});
	//异步加载指定插件，插件加载顺序从左至右
