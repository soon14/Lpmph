// 选择器
var eSelector = {
	//选择器
	//适用于bootstrap的二次封装弹出窗口
	selector : function(opt,e){
		if(e){
			try{
				e.stopPropagation();
			}catch(evt){}	
		}
		opt = $.extend({
			url : '',
			width : 600,
			height : 450,
			title : '选择器',
			checktype : "multi",//数据选择模式multi：多选；single：单选模式
			params : null,//自定义参数集
			domain : null,
			//回调函数function(data)返回的数据结构：
			//数据grid : results:[{id:'xx',cell:['aa',1,'bb',{...}]},....]
			//标准数据格式：results:[{id:'aa',value:'aaa'},{...}]
			callback : null
		},opt);
		if(opt.url && opt.callback && opt.title){
			var dlg = eDlg.open({
				url : opt.url,
				width : opt.width,
				height : opt.height,
				title : opt.title
			});
			//对数据传递进行格式封装
			var _callback = null;
			if(opt.callback && $.isFunction(opt.callback)){
				_callback = function(data){
					if(data){
						if($.isArray(data)){
							opt.callback({"results" : data});
							return ;
						}else{
							opt.callback({"results" : [data]});
							return ;
						}
					}else{
						opt.callback({"results" : null});
						return ;
					}
				};
			}
			//设置回调
			if(opt.callback) dlg[0].callback = _callback;
			if(opt.params){
				dlg[0].selectorparams = $.extend({checktype:opt.checktype},opt.params);
			} else {
				dlg[0].selectorparams = {checktype:opt.checktype};
			}
		}else{
			eDialog.error("选择器参数传递不正确或参数个数不完整!");
		}
	},
	// 获得选择器中的传递参数
	getSelectorParams : function(dlg){
		if(dlg){
			return dlg[0].selectorparams;
		}
		return null;
	},
	// 获得选择器中的回调函数
	getSelectorCallback : function(dlg){
		if(dlg){
			return dlg[0].callback;
		}
		return null;
	}
};