(function($) {
    var _timeout = "600000";// 超时
    var loading = false;

    /**
     * 框架采用的ajax提交方法；
     * 
     * @param opts.url
     *            请求路径
     * @param opts.data
     *            请求参数
     * @param opts.async
     *            请求是否异步，true为异步、false为同步
     * @param opts.success
     *            成功返回之后的处理函数
     * @param opts.type
     *            是否是GET 还是 POST
     * @param opts.error
     *            失败返回之后的处理函数
     * @param opts.loadingMessage
     * 
     * @author yugn
     */
    $.appAjax = function(opts) {
        if (!opts.async) {
            opts.async = false;
        }
        /*
         * if (!opts.async && loading) { return; }
         */
        if (!opts.data) {
            opts.data = [];
        }
        if (!opts.type) {
            opts.type = 'POST';
        }
        if (!$.isArray(opts.data)) {
            var o = [];
            $.each(opts.data, function(i, n) {
                        o.push({
                                    name : i,
                                    value : n
                                })
                    });
            opts.data = o;
        }

        // 头"//" -> "/"
        if (opts.url && opts.url.substring(0, 2) == "//") {
            opts.url = opts.url.substring(1);
        }
        
        if(!opts.dataType){
        	opts.dataType="json";
        }
        
        if(!opts.jsonp){
        	opts.jsonp = "callback";
        }

        /**
         * 走jquery的ajax提交方式
         */
        $.ajax({
                    // url : opts.url + '?_=' + new Date().getTime(),
                    url : opts.url,
                    data : opts.data,
                    async : opts.async,
                    dataType : opts.dataType,
                    timeout : _timeout,
                    type : opts.type,
                    jsonp : opts.jsonp,
                    success : function(data, textStatus) {
                        if (!opts.async) {
                            loading = false;
                        }
                        // 成功返回
                        opts.success(data, textStatus);
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                    	if (!opts.async) {
                            loading = false;
                        }
                        if ("timeout" == status) {
                            alert("系统请求超时，请联系管理员！");
                        }
                        if (opts.error) {
                            opts.error(XMLHttpRequest, textStatus, errorThrown);
                        }
                    },
                    beforeSend : function(XMLHttpRequest) {
                        if (opts.beforeSend) {
                            opts.beforeSend(XMLHttpRequest);
                        }
                    },
                    complete : function(XMLHttpRequest, textStatus) {
                        if (opts.complete) {
                            opts.complete(XMLHttpRequest, textStatus);
                        }
                    }
                });

    }

    /**
     * 获取制定表单的所有input元素，拼装成AJAX请求的参数
     * 
     * @param p
     *            页面表单的对象
     * 
     * @return data
     * 
     * @author huxiang
     */
    $.formParams = function(p) {
        if (p == undefined) {
            return;
        }

        var data = new Array;

        $(p).find(":input").each(function(i, n) {
            var flag = "false";
            if ($(n).attr("param") == undefined) {
                flag = "true";
            } else {
                flag = $(n).attr("param");
            }
            if (flag == true || flag == "true") {
                if (($(n).attr('type') == "checkbox" || $(n).attr('type') == "radio") && $(n).attr('checked')) {
                    data.push({
                                name : $(n).attr("name"),
                                value : $(n).attr("value")
                            })
                } else if ($(n).attr('type') == "text" || $(n).attr('type') == "password"
                        || $(n).attr('type') == "hidden" || $(n).attr('type') == "textarea") {
                    data.push({
                                name : $(n).attr("name"),
                                value : $(n).attr("value")
                            })
                }
            }
        });
        $(p).find("select").each(function(i, n) {

                    var flag = "false";
                    if ($(n).attr("param") == undefined) {
                        flag = "true";
                    } else {
                        flag = $(n).attr("param");
                    }
                    if (flag == true || flag == "true") {
                        if ($.trim($(n).attr('value')) != '') {
                            data.push({
                                        name : $(n).attr("name"),
                                        value : $(n).attr("value")
                                    })
                        } else {
                            data.push({
                                        name : $(n).attr("name"),
                                        value : ''
                                    })
                        }

                    }

                })
        return data;
    };

    /**
     * 实现遮罩效果,使用用例 $.gridLoading({ message:"需要提醒的信息，例如：正在加载中.." })
     * 增加了对页面部分元素进行解除遮罩的功能，增加了参数 el：jquery获取元素的选择器
     * @param p
     *            p.el 需要进行遮罩的jquery选择器；
     *            p.message 信息；
     */
    $.gridLoading = function(p) {
        var opt = $.extend({
                    el:window,
                    message : "正在获取数据..."
                }, p)
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
    }

    /**
     * 解除遮罩效果；增加了对页面部分元素进行解除遮罩的功能
     * @param p
     *     p.el 需要解除遮罩的jquery选择器
     */
    $.gridUnLoading = function(p) {
        var opt = $.extend({
                    el:window
                }, p);
         $(opt.el).unblock();
    }
    
    //把 form 表单转json
	$.fn.serializeJson=function(){
		var serializeObj={};
		var array=this.serializeArray();
		var str=this.serialize();
		$(array).each(function(){
			if(serializeObj[this.name]){
				if($.isArray(serializeObj[this.name])){
					serializeObj[this.name].push(this.value);
				}else{
					serializeObj[this.name]=[serializeObj[this.name],this.value];
				}
			}else{
				serializeObj[this.name]=this.value;	
			}
		});
		return serializeObj;
	};
    
    /**
     * 将url 返回的信息，填充到 select 的 options;
     * 
     * @param p
     *            参数； p.url 获取数据的url; p.select 填入的select 符合jquery选择器 例如 #id
     *            p.isBlank 是否显示空值
     */
    $.fillSelect = function(p) {
        if (!p.url || !p.select) {
            $.alert('必须设置url属性和select属性');
            return;
        }

        var opts = $.extend({
                    url : "",
                    isBlank : false
                }, p);
        var s = opts.select;
        var width = $(s).attr("width");
        if (!width) {
            width = $(s).attr("style");
        }
        $.appAjax({
                    url : opts.url,
                    data : opts.data,
                    async : true,
                    dataType : 'json',
                    type : 'POST',
                    success : function(data) {
                        var d = data;
                        if (opts.onResultData && $.isFunction(opts.onResultData)) {
                            d = opts.onResultData(data);
                        }

                        $(s).empty();
                        if (opts.isBlank) {
                            $(s).append('<option value="">－－全部－－</option>');
                        }
                        if (!d.options) {
                            return;
                        }
                        $.each(d.options, function(i, n) {
                                    var op = '<option value="' + n.value + '" ';
                                    if (n.selected) {
                                        op = op + ' selected ';
                                    }
                                    $.each(n.attrs,function(key,value){
                                        op = op + ' '+key+' = "'+value+'"';
                                    })
                                    op = op + '>' + n.label + '</option>';
                                    $(s).append(op);
                                });
                        // 设置Select宽度
                        $(s).attr("style", width ? width : '120px');
                        if (opts.callback && $.isFunction(opts.callback)) {
                            opts.callback();
                        }
                    }
                });
    }
})(jQuery)
