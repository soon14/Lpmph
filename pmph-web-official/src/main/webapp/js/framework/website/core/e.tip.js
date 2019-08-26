//网站前台消息提示气泡
//基于qTip2进行二次封装
var eTip = {
	//显示错误提示风格气泡（bootstrap风格），通常用于表单验证
	//obj : 目标对象（jquery对象）
	errorTip : function(obj,content){
		if(!obj)return ;
		$(obj).qtip({
			content : content?content:'',
			position : {
				my : 'left center',
				at : 'right center'
			},
			style: {
				classes: 'qtip-bootstrap qtip-bootstrap-error',
				width : '150'
			},
			show : {
				ready : true
			},
			hide : false,
			events: {
				visible: function(event, api) {
					$(api.elements.tooltip).css('opacity','0.9');
				}
			}
		});
	},
	//关闭消息提示气泡
	//obj : 目标对象（jquery对象）
	closeTip : function(obj){
		if(!obj)return ;
		$(obj).qtip('destroy');
	}
};