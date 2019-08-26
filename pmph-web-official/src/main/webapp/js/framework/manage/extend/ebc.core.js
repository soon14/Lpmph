// ===================> EBC JavaScript Core <===================
// EBC 核心脚本库
// 
// 曾海沥（Terry）
// -----------------------------------------------------
// 更新记录
// ===================> EBC JavaScript Core <===================
var ebc = {
	//EBC常量
	constants : {
		//附件管理界面处理JSP的路径
		attachURL : $webroot + 'framework/jsLib/jquery/xheditor/resourse/ImgUpload.jsp',
		uploaderURL : $webroot + 'framework/jsLib/manage/extend/upload/AttachUpload.jsp',
		selectorDialogPrefix : 'selectorDialog_',
		domain : {
			dlg : 'dialog',//弹出窗口作用域
			nav : 'navtab'//主页面导行面板
		}
	},
	//根据参数返回作用域
	getDomain : function(domain){
		if(domain==ebc.constants.domain.dlg){
			return $.pdialog.getCurrent();
		}else{//默认返回主页面导行面板作用域
			return navTab.getCurrentPanel();
		}
	}
};



$(function(){
	//校验Ajax请求同时系统登陆是否已超时
	$.checkAjaxTimeOut = function(json){
		var tempJson = $.isPlainObject(json)?json:DWZ.jsonEval(json);
		// 登陆超时后的转向处理
		if (tempJson.statusCode && tempJson.statusCode == DWZ.statusCode.timeout) {
			alertMsg.error(DWZ.msg("sessionTimout"), {
				okCall : function() {DWZ.loadLogin();}
			});
		}
	};
	
	
	//通用Ajax调用入口
	$.eAjax = function(opts) {
		opts = $.extend({
			async : true,
			type : "POST",
			dataType : "json",
			cache : false,
			notice : true//是否显示全屏遮罩
		}, opts);
		if(!opts.notice){
			DWZ.ajaxNotice = opts.notice;
		}
		if(opts.url.indexOf('http://') != -1){//提交路径里出现了完整的路径时，要避免转换http://中的双斜线为单斜线
			opts.url = 'http://' + opts.url.replace("http://","").replace("//","/");
		}else if(opts.url.indexOf('https://') != -1){
			opts.url = 'https://' + opts.url.replace("https://","").replace("//","/");
		}else{
			opts.url=opts.url.replace("//","/");
		}
		$.ajax({
			url : opts.url,
			data : opts.data,
			async : opts.async,
			type : opts.type,
			dataType : opts.dataType,
			cache : opts.cache,
			success : function(data, textStatus) {
				//检查系统登陆是否超时
				$.checkAjaxTimeOut(data);
				//读取文本内容模式
				if(opts.dataType=='text' || opts.dataType=='jsonp'){
					opts.success(data, textStatus);
					return;
				}
				
				if (data.ajaxResult == 1) {//请求成功
					opts.success(data.values, textStatus);
				} else if(data.ajaxResult == 0){//访问失败
					alertMsg.error(data.errorMessage);
				} else if(data.ajaxResult == 2){//后台校验信息
					var errmsg = new Array();
					if(data.fieldErrors){//后台校验错误信息
						for(i in data.fieldErrors){
							errmsg.push(data.fieldErrors[i]);
						}
						alertMsg.error(errmsg.toString());
					}
					if(data.errors){//后台校验错误信息(expression)
						if($.isArray(data.errors)){
							for(var i =0 ;i<data.errors.length;i++){
								errmsg.push(data.errors[i]);
							}
						} else if($.type(data.errors) === "string"){
							errmsg.push(data.errors);
						}
					}
					alertMsg.error(errmsg.join("<br/>"));
				}
			},
			error : function(errors, textStatus) {
				if (opts.error)opts.error();
				DWZ.ajaxError(errors, opts, textStatus);
			}
		});
	};
});


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
