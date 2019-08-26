/**
 * 基于Bootstrap的表单扩展
 * 
 */
var bForm = {
	//为表单的输入框的标题设置for属性，达到点击标题后，自动将焦点设置到输入框上的功能
	//form : 表单jquery对象
	setHeadNav : function(form){
		if(!form) return;
		var inputs = $('.control-group',$(form));
		inputs.each(function(i,row){
			var label = $('.control-label',$(row));
			var input = $(':input',$(row)).not(':button,:checkbox,:radio,:submit,:hidden,:reset,:image');
			var key = $(input).attr('id');
			if(!key){
				key = $(input).attr('name');
				if(key) $(input).attr('id',key);
			}
			if(key) $(label).attr('for',key);
		});
	},
	//设置表单中必填字段的标题前增加红色星号
	setRequiredHead : function(form){
		if(!form) return;
		var inputs = $('.required',$(form));
		inputs.each(function(i,row){
			var ctl = $(this).parent();
			var label = $(ctl).prevAll('.control-label');
			$(label).html('<span style="color:red;">* </span>' + $(label).html());
		});
	}
};