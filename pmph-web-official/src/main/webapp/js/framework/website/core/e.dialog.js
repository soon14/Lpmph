//require(['ZebraDialog']);
	

//网站前台弹出窗口
var eDialog = {
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
	//弹出式通知窗口默认参数
	alertDefault : {
	    'type':'information',
	    'title':false,//(p && p.title)?p.title:'提示',
	    'overlay_close' : false,
	    'show_close_button':false,//是否显示关闭按钮
	    'position' : ['center','middle'],
	    'overlay_opacity' : .6,
	    'animation_speed_hide' : 0,
	    'animation_speed_show' : 0,
	    'buttons' : [{
	    	'caption' : '确定',callback : $.noop
	    }],
	    'onClose' : $.noop
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
		var _dlg = eDialog.getDomain();
		var _type = 'information';
		var _callback = $.noop;
		if(type) _type = type;
		if($.isFunction(callback)) _callback = callback;
		var _p = $.extend({},eDialog.alertDefault,_p);
		_p.type = _type;
		_p.buttons = [{
			caption : '确定',
			callback : function(){
				_callback();
			}
		}];
		_dlg.Zebra_Dialog(content?content.toString():'', _p);
	},
	//信息提示
	info : function(content,p){
		// 窗口类型一共有：information,warning,error,confirmation,question
		var dlg = eDialog.getDomain();
		p = $.extend({},eDialog.alertDefault,p);
		//p.auto_close = 3000;//三秒后自动关闭消息窗口
		dlg.Zebra_Dialog(content.toString(), p);
	},
	//警告信息
	warning : function(content,p){
		// 窗口类型一共有：information,warning,error,confirmation,question
		var dlg = eDialog.getDomain();
		p = $.extend({},eDialog.alertDefault,p);
		p.type = 'warning';
		dlg.Zebra_Dialog(content.toString(), p);
	},
	//错误信息
	error : function(content,p){
		// 窗口类型一共有：information,warning,error,confirmation,question
		var dlg = eDialog.getDomain();
		p = $.extend({},eDialog.alertDefault,p);
		p.type = 'error';
		dlg.Zebra_Dialog(content.toString(), p);
	},
	//成功信息
	success : function(content,p){
		// 窗口类型一共有：information,warning,error,confirmation,question
		var dlg = eDialog.getDomain();
		p = $.extend({},eDialog.alertDefault,p);
		p.type = 'confirmation';
		dlg.Zebra_Dialog(content.toString(), p);
	},
	//对话框模式
	//按钮定义格式：{caption: 'Yes', callback: function() { alert('"Yes" was clicked')}}
	confirm : function(content,p){
		// 窗口类型一共有：information,warning,error,confirmation,question
		var dlg = eDialog.getDomain();
		p = $.extend({},eDialog.alertDefault,p);
		p.type = 'question';
		dlg.Zebra_Dialog(content.toString(), p);
	}
	//打开窗口
	/*
	,open : function(p){
		//合并参数
		p = $.extend({},eDialog.defaults,p);
		var _winContent=null;		
		if(p.title){
			var titleBox = '<div style="color:white;font-size:14px;background-color:#5281AD;height:26px;line-height:26px;padding-left:5px;">'+p.title+'</div>';
			_winContent = $(titleBox+'<iframe src="' + p.url + '" frameborder="0" height="' + (p.height-26)  + '" style="border:0;width:100%;">');
		}else{
			_winContent = $('<iframe src="' + p.url + '" frameborder="0" height="' + p.height + '" style="border:0;width:100%;">');
		}
		var modalWin=_winContent[0].contentWindow;
		initModalWin();
		//初始化接口
		function initModalWin(){
			try{
				modalWin.callback=callbackModal;
			}catch(ex){}
		}
		//模式窗口回调
		function callbackModal(v){
			modalWin.document.write('');
			if(v!=null && p.callback)p.callback(v);	
			$.modal.close();
		}
		
		$.modal(_winContent, {
			containerCss:{
				backgroundColor:"#FFFFFF",
				borderColor:"#888888",
				height:p.height,
				padding:0,
				width:p.width
			},
			opacity:20,
			onClose:p.onclose?p.onclose:$.noop,
			onOpen:p.onopen?p.onopen:$.noop
		});
	}
	*/
};

//方法扩展
/*
$(function(){
	$.fn.extend({
		//将页面的部分片段在弹出窗口中显示
		eDialog : function(p){
			//参数合并
			p = $.extend({},eDialog.defaults,p);
			this.each(function(i,d){
				var dialog = new Boxy($("<div>").append($(d).html()), {
					behaviours: function(r) {
//						$(r).hover(function() {
//							$(this).css("background-color", "red");
//						}, function() {
//							$(this).css("background-color", "white");
//						});
					},
					title: p.title,
					modal : p.modal,
					clone : true,
					draggable : true,
					closeable: p.closeable,
					closeText : p.closeText
				});
				//设置窗口大小
				if(p.width && p.height){
					dialog.resize(p.width,p.height);
				}
			});
		}
	});
});
*/

//指定脚本样式依赖
//引入公用脚本库定义
//层级说明：内层依赖外层
//加载顺序：外层优先加载
//require(['/ecp-web-demo/js/common.js'],function(){
	
//});
	//异步加载指定插件，插件加载顺序从左至右
