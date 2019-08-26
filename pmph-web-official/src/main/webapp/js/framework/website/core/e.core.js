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
var EBC = {
	// sbar: show sidebar
	keyCode: {
		ENTER: 13, ESC: 27, END: 35, HOME: 36,
		SHIFT: 16, TAB: 9,
		LEFT: 37, RIGHT: 39, UP: 38, DOWN: 40,
		DELETE: 46, BACKSPACE:8
	},
	isOverAxis: function(x, reference, size) {
		//Determines when x coordinate is over "b" element axis
		return (x > reference) && (x < (reference + size));
	},
	isOver: function(y, x, top, left, height, width) {
		//Determines when x, y coordinates is over "b" element
		return this.isOverAxis(y, top, height) && this.isOverAxis(x, left, width);
	},
	
	pageInfo: {pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"},
	statusCode: {ok:200, error:300, timeout:301},
	ui:{sbar:true},
	frag:{}, //page fragment
	_msg:{}, //alert message
	_set:{
		loginUrl : $webroot + "login", //session timeout
		loginTitle:"", //if loginTitle open a login dialog
		debug:false
	},
	msg:function(key, args){
		var _format = function(str,args) {
			args = args || [];
			var result = str;
			for (var i = 0; i < args.length; i++){
				result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
			}
			return result;
		};
		return _format(this._msg[key], args);
	},
	debug:function(msg){
		if (this._set.debug) {
			if (typeof(console) != "undefined") console.log(msg);
			else alert(msg);
		}
	},
	loadLogin:function(){
//		if ($.pdialog && EBC._set.loginTitle) {
//			$.pdialog.open(EBC._set.loginUrl, "login", EBC._set.loginTitle, {mask:true,width:520,height:260});
//		} else {
			window.location = system.loginUrl;
//		}
	},
	jsonEval:function(data) {
		try{
			if ($.type(data) == 'string')
				return eval('(' + data + ')');
			else return data;
		} catch (e){
			return {};
		}
	},
	ajaxError:function(xhr, ajaxOptions, thrownError){
		if (eDialog) {			
			eDialog.error("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" 
				+ "<div>ajaxOptions: "+ajaxOptions + "</div>"
				+ "<div>thrownError: "+thrownError + "</div>"
				+ "<div>"+xhr.responseText+"</div>");
		} else {
			alert("Http status: " + xhr.status + " " + xhr.statusText + "\najaxOptions: " + ajaxOptions + "\nthrownError:"+thrownError + "\n" +xhr.responseText);
		}
	},
	ajaxDone:function(json){
		if (json.statusCode === undefined && json.message === undefined) { // for iframeCallback
			if (alertMsg) return alertMsg.error(json);
			else return alert(json);
		} 
		if(json.statusCode == EBC.statusCode.error) {
			if(json.message && alertMsg) alertMsg.error(json.message);
		} else if (json.statusCode == EBC.statusCode.timeout) {
			if(alertMsg) alertMsg.error(json.message || EBC.msg("sessionTimout"), {okCall:EBC.loadLogin});
			else EBC.loadLogin();
		} else {
			if(json.message && alertMsg) alertMsg.correct(json.message);
		};
	}
};


$(function(){
	//通用Ajax调用入口
	$.eAjax = function(opts) {
		
		opts = $.extend({
			async : true,
			type : "POST",
			dataType : "json",
			cache : false,
			notice : true,//是否显示全屏遮罩
			exception : $.noop //业务异常处理
		}, opts);
		//if(!opts.notice) EBC.ajaxNotice = opts.notice;
		if(opts.url.indexOf('http://') != -1){//提交路径里出现了完整的路径时，要避免转换http://中的双斜线为单斜线
			opts.url = 'http://' + opts.url.replace("http://","").replace("//","/");
		}else if(opts.url.indexOf('https://') != -1){
			opts.url = 'https://' + opts.url.replace("https://","").replace("//","/");
		}else{
			opts.url = opts.url.replace("//","/");
		}
		$.ajax({
			url : opts.url,
			data : opts.data,
			async : opts.async,
			type : opts.type,
			dataType : opts.dataType,
			cache : opts.cache,
			success : function(data, textStatus) {
				//读取文本内容模式
				if(opts.dataType=='text' || opts.dataType=='jsonp' || opts.dataType=='html'){
					opts.success(data, textStatus);
					return;
				}
				
				switch (data.ajaxResult) {
				case 0://访问失败、后台异常、业务异常等非正常结果
					var errmsg = new Array();
					if(data.errorMessage){//后台校验错误信息
						var msgs = data.errorMessage;
						for(i in msgs) errmsg.push(msgs[i].message);
					}
					if(errmsg.length >0 ) eDialog.alert(errmsg.join("<br/>"),opts.exception?opts.exception:$.noop,'error');
					else eDialog.alert('系统异常，请联系管理员！',opts.exception?opts.exception:$.noop,'error');
					break;
				case 1://请求成功
					opts.success(data.values, textStatus);
					break;
				case 2://后台校验信息
					var errmsg = new Array();
					if(data.errorMessage && $.isArray(data.errorMessage) && data.errorMessage.length >0){
						var msgs = data.errorMessage;
						for(i in msgs) errmsg.push(msgs[i].message);
					}
					//页面若存在后台校验信息统一显示框，则将校验信息填入
					if(errmsg.length > 0){
						if($('div.formValidateMessages').size()>0){
							$('div.formValidateMessages').html(errmsg.join("<br/>"));
							$('div.formValidateMessages').slideDown('fast');
							if(opts.exception && $.isFunction(opts.exception)) opts.exception();
						}else{//若不存在统一显示框，则弹出信息提示框进行信息展示
							eDialog.alert(errmsg.join("<br/>"),opts.exception?opts.exception:$.noop,'error');
						}
					}else{
						eDialog.alert('表单验证不通过，但未提供相关提示信息，请联系系统管理员！',opts.exception?opts.exception:$.noop,'error');
					}
					break;
				case 3://登录超时
					//EBC.msg("sessionTimout")提示文字内容在dwz.frag.xml内
					eDialog.error('会话超时，请重新登录!', {
						buttons : [{
							caption : '确定',
							callback : function(){
								//EBC.loadLogin();
								if(data && data.values.url){
									window.location.href = data.values.url;
								}
							}
						}]
					});
					break;
				default:
					
					break;
				}

			},
			error : function(errors, textStatus) {
				if (opts.error)opts.error();
				//EBC.ajaxError(errors, opts, textStatus);
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