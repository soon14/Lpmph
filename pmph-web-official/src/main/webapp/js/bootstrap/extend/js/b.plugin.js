/**
 * 基于Bootstrap 的功能插件集合
 */

$.fn.extend({
	//自动完成列表
	//p:允许接受三种类型参数【array，function，ajax url】，但返回值都必须为数组
	bAutocomplete : function(p){
		var _this = $(this);
		var _init = function(data){
			if(data && $.isArray(data) && data.length >0){
				$(_this).typeahead({
					source : data
				});
			}
		};
		
		return this.each(function(){
			if(!p) return;
			//提供的参数为数组内容
			if($.isArray(p)){
				_init(p);
			//提供参数为URL
			}else if(typeof(p) == 'string'){
				$.eAjax({
					url : p,
					success : function(returnInfo) {
						_init(returnInfo);
					}
				});
			//提供的参数为funciton
			}else if($.isFunction(p)){
				_init(p());
			}
		});
	},
	//指定区域的复选框全选功能
	//参数匀为jquery选择器表达式
	//box：处理全选功能的区域，在该区域内的所有checkbox对象都会被操作
	bSelectAll : function(box){
		if(!box || $(box).size()==0) return;
		var _this = this;
		var checkboxs = $(':checkbox',$(box)).not(_this);
		$(_this).on('click',function(e){
			e.stopPropagation();
			var checked = $(this).prop('checked');
			checkboxs.prop('checked',checked);
			$(_this).prop('checked',checked);
		});
		checkboxs.on('click',function(e){
			e.stopPropagation();
			if($(':checkbox:checked',$(box)).size() == checkboxs.size()){
				$(_this).prop('checked',true);
			}else{
				$(_this).prop('checked',false);
			}
		});
	}
});