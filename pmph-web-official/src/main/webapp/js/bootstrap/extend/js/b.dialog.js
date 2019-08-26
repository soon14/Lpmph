//使用Bootstrap的Modal对话框进行二次封装
var bDialog = {
	defaults : {
		'backdrop' : 'static',//静态模式窗口，鼠标点击背景不关闭窗口，false：不显示背景遮罩，true，显示背景遮罩，但鼠标点击遮罩会关闭窗口
		'title' : '对话框',
		'width' : 700,
		'height' : 400,
		'animation' : false, //动画效果，默认关闭
		'closeButton' : false,//对话框底部的关闭窗口按钮
		'scroll' : true,      //是否显示滚动条，默认显示
		'url' : false,
		'onShow' : $.noop,     //显示对话前执行的回调
		'onShowed' : $.noop,   //显示完成对话框后执行的回调
		'onHide' : $.noop,     //关闭/隐藏对话框前执行的回调
		'onHidden' : $.noop,   //关闭/隐藏对话框后执行的回调
		'callback' : $.noop    //窗口回调函数，参数1：回调后返回的数据(callback(data))
	},
	//打开对话框
	//p:参数集
	//obj:jquery对象，用于网页片断式的显示内容，若设置了URL方式打开窗口，则不需要设置该参数
	open : function(p,obj){
		//合并参数
		p = $.extend({},bDialog.defaults,p);
		var main = $("#ebcModal");
		if(!main) alert('未导入弹出窗口定义代码！');
		$("#ebcModalLabel",main).html(p.title);//设置标题
		if(p.animation) $(main).addClass('fade');//设置动画效果
		if(p.closeButton){
			$("#ebcModalFooter",main).empty().append('<button class="btn btn-inverse" data-dismiss="modal" aria-hidden="true">关闭</button>');
			$("#ebcModalFooter",main).show();
		}else{
			$("#ebcModalFooter",main).hide();
		}
		$("#ebcModalBody",main).empty();
		
		
		// ****************************处理回调及参数******************
		//对数据传递进行格式封装
		var _callback = null;
		if(p.callback && $.isFunction(p.callback)){
			_callback = function(data){
				if(data){
					if($.isArray(data)){
						p.callback({"results" : data});
						return ;
					}else{
						p.callback({"results" : [data]});
						return ;
					}
				}else{
					p.callback({"results" : null});
					return ;
				}
			};
		}
		//设置回调
		if(p.callback) main[0].callback = _callback;
		//设置参数
		if(p.params) main[0].params = p.params; else main[0].params = {};
		main[0].returnData = null;
		
		var _init = function(){
			//事件绑定
			if(p.onShow && $.isFunction(p.onShow)) $(main).off('show').on('show',p.onShow);
			if(p.onShowed && $.isFunction(p.onShowed)) $(main).off('shown').on('shown',p.onShowed);
			if(p.onHide && $.isFunction(p.onHide)) $(main).off('hide').on('hide',p.onHide);
			$(main).off('hidden').on('hidden',function(){
				$(main).removeClass('dialogInActive');
				var data = main[0].returnData;
				if(p.onHidden && $.isFunction(p.onHidden)) p.onHidden();
				if(_callback && $.isFunction(_callback)) _callback(data);
			});
			$(main).modal({
				backdrop : p.backdrop
			}).css({
				'width' : p.width,
				'height' : p.height,
				'margin-left' : function () {
					return -($(this).width() / 2);
				}
			}).removeClass('hide');
		};
		if(obj){//页面片断模式
			$("#ebcModalBody",main).append($(obj).show());
			_init();
		}else if(p.url) {//iframe模式
			_init();
			var tmp = p.scroll ? 'yes' : 'no';
			$("#ebcModalBody",main).append('<iframe id="ebcModalBodyFrame" frameborder="0" scrolling="'+tmp+'" style="width:100%;height:100%;border:0px;" src="'+p.url+'"></iframe>');
		}
		

		
		
		// ****************************处理样式************************
		var head = $("#ebcModalHeader",main).outerHeight(true);
		var footer = p.closeButton ? $("#ebcModalFooter",main).outerHeight(true) : 0;
		//alert('head-h:'+head + ' footer-h:' + footer);
		//alert('box:'+$(main).innerHeight());
		//alert(p.height - head - footer -30);
		//高度减去30 是去掉上下内边距各15，一共30
		var bodyPaddingTop = parseFloat($("#ebcModalBody",main).css('padding-top'));
		var bodyPaddingBottom = parseFloat($("#ebcModalBody",main).css('padding-bottom'));
		var newBodyHeight = $(main).innerHeight() - head - footer - bodyPaddingTop - bodyPaddingBottom;
		var bodyCss = { 'height' : newBodyHeight };
		if(newBodyHeight>400) bodyCss.maxHeight = newBodyHeight;
		$("#ebcModalBody",main).css(bodyCss);
		
		//若是iFrame模式则设置iFrame高度等样式
		if(!obj){
			//iFrame不需要减去上下边距，因为父容器的高度其实跟iframe一致即可，
			//var newBodyFrameHeight = newBodyHeight;// - bodyPaddingTop - bodyPaddingBottom;
			//var bodyFrameCss = { 'height' : newBodyFrameHeight };
			//if(newBodyFrameHeight>(400 - bodyPaddingTop - bodyPaddingBottom)) bodyFrameCss.maxHeight = newBodyFrameHeight;
			//$("#ebcModalBodyFrame",main).css(bodyFrameCss);
			$("#ebcModalBodyFrame",main).css(bodyCss);
		}
		
		$(main).addClass('dialogInActive');
		return main;
	},
	//关闭当前弹出窗口
	closeCurrent : function(data){
		var _hideDialog = function(dlg){
			if(dlg && $(dlg).hasClass('dialogInActive')){
				//清除参数
				dlg[0].callback = null;
				dlg[0].selectorparams = null;
				dlg[0].returnData = data;
				$("#ebcModalCloseButton",dlg).click();
				return true;
			}
			return false;
		};
		var dialog = $("#ebcModal");
		if(_hideDialog(dialog)) return;
		
		//关闭父层窗口
		dialog = $("#ebcModal",$(window.parent.document));
		_hideDialog(dialog);
	},
	//获得弹出窗口对象
	getDialog : function(){
		var dlg = $("#ebcModal");
		if(dlg && $(dlg).hasClass('dialogInActive')) return dlg;
		return $("#ebcModal",$(window.parent.document));
	},
	// 获得选择器中的传递参数
	getDialogParams : function(dlg){
		if(dlg) return dlg[0].params;
		return null;
	},
	// 获得选择器中的回调函数
	getDialogCallback : function(dlg){
		if(dlg) return dlg[0].callback;
		return null;
	}
};

$(function(){
	var modal_template = '<!-- 弹出式对话框基础模板，请勿修改 -->';
	modal_template += '<div id="ebcModal" class="modal hide" tabindex="-1" role="dialog" aria-labelledby="ebcModalLabel" aria-hidden="true">';
	modal_template += '<div class="modal-header" id="ebcModalHeader">';
	modal_template += '<button type="button" id="ebcModalCloseButton" class="close" data-dismiss="modal" aria-hidden="true" title="关闭对话框(快捷键ESC)">×</button>';
	modal_template += '<h3 id="ebcModalLabel"></h3>';
	modal_template += '</div>';
	modal_template += '<div class="modal-body" id="ebcModalBody" style="overflow:hidden;"></div>';
	modal_template += '<div class="modal-footer" id="ebcModalFooter">&nbsp;</div>';
	modal_template += '</div>';
	$("body").append(modal_template);
});